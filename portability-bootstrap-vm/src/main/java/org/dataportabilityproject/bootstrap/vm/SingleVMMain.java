package org.dataportabilityproject.bootstrap.vm;

import com.google.common.util.concurrent.UncaughtExceptionHandlers;
import org.dataportabilityproject.gateway.ApiMain;
import org.dataportabilityproject.gateway.reference.ReferenceApiServer;
import org.dataportabilityproject.worker.WorkerMain;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstraps all services (Gateway and 1..N Workers) in a single VM.
 *
 * <p>Intended for demonstration purposes.
 */
public class SingleVMMain {
  private final Consumer<Exception> errorCallback;
  private ExecutorService executorService;
  private final static Logger logger = LoggerFactory.getLogger(SingleVMMain.class);

  public static void main(String[] args) {
    logger.debug("Starting SingleVMMain");
    Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandlers.systemExit());

    SingleVMMain singleVMMain = new SingleVMMain(SingleVMMain::exitError);

    Runtime.getRuntime().addShutdownHook(new Thread(singleVMMain::shutdown));

    // TODO make number of workers configurable
    singleVMMain.initializeWorkers(1);
    singleVMMain.initializeGateway();
  }

  public SingleVMMain(Consumer<Exception> errorCallback) {
    this.errorCallback = errorCallback;
  }

  public void initializeGateway() {
    logger.debug("Initializing Gateway");

    ApiMain apiMain = new ApiMain();

    try (InputStream stream =
        SingleVMMain.class.getClassLoader().getResourceAsStream("portability.keystore.jks")) {
      if (stream == null) {
        throw new IllegalArgumentException("Demo keystore was not found");
      }

      // initialise the keystore
      char[] password = "password".toCharArray();
      KeyStore keyStore = KeyStore.getInstance("JKS");
      keyStore.load(stream, password);
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
      keyManagerFactory.init(keyStore, password);

      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
      trustManagerFactory.init(keyStore);

      apiMain.initializeHttps(trustManagerFactory, keyManagerFactory);
      logger.debug("Starting apiMain");
      apiMain.start();

    } catch (Exception e) {
      logger.warn("Error occurred trying to start apiMain");
      errorCallback.accept(e);
    }
  }

  public void initializeWorkers(int workers) {
    logger.debug("Initializing Workers");

    if (workers < 1) {
      errorCallback.accept(new IllegalArgumentException("Invalid number of workers: " + workers));
      return;
    }

    executorService = Executors.newFixedThreadPool(workers);

    for (int i = 0; i < workers; i++) {
      logger.debug("Creating workerRunner");
      WorkerRunner workerRunner = new WorkerRunner();
      executorService.submit(workerRunner);
    }
  }

  public void shutdown() {
    if (executorService != null) {
      executorService.shutdownNow();
    }
  }

  private static void exitError(Exception exception) {
    exception.printStackTrace();
    logger.warn("Exiting abnormally");
    System.exit(-1);
  }

  private class WorkerRunner implements Runnable {
    public void run() {
      //noinspection InfiniteLoopStatement
      while (true) {
        WorkerMain workerMain = new WorkerMain();
        try {
          workerMain.initialize();
        } catch (Exception e) {
          errorCallback.accept(e);
        }
        workerMain.poll();
      }
    }
  }
}
