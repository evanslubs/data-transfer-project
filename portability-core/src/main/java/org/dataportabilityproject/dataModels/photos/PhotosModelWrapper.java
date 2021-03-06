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
package org.dataportabilityproject.dataModels.photos;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.dataportabilityproject.dataModels.ContinuationInformation;
import org.dataportabilityproject.dataModels.DataModel;

/** A Wrapper for all the possible objects that can be returned by a photos exporter. */
public class PhotosModelWrapper implements DataModel {
  private final Collection<PhotoAlbum> albums;
  private final Collection<PhotoModel> photos;
  private ContinuationInformation continuationInformation;

  public PhotosModelWrapper(
      Collection<PhotoAlbum> albums,
      Collection<PhotoModel> photos,
      ContinuationInformation continuationInformation) {
    this.albums = albums == null ? ImmutableList.of() : albums;
    this.photos = photos == null ? ImmutableList.of() : photos;
    this.continuationInformation = continuationInformation;
  }

  public Collection<PhotoAlbum> getAlbums() {
    return albums;
  }

  public Collection<PhotoModel> getPhotos() {
    return photos;
  }

  @Override
  public ContinuationInformation getContinuationInformation() {
    return continuationInformation;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("albums", albums)
        .add("photos", photos)
        .add("continuationInformation", continuationInformation)
        .toString();
  }
}
