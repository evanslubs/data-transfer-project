/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dataportabilityproject.serviceProviders.instagram.model;

/** DataModel for a media feed in the Instagram API. Instantiated by JSON mapping. */
public final class MediaFeedData {

  private String id;

  private String type;

  private String created_time;

  private ImageObject images;

  private Caption caption;

  public String getId() {
    return id;
  }

  public Caption getCaption() {
    return caption;
  }

  public ImageObject getImages() {
    return images;
  }

  public String getCreatedTime() {
    return created_time;
  }

  public String getType() {
    return type;
  }
}
