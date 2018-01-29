/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.features.test.features;

import static org.ops4j.pax.exam.CoreOptions.maven;

import org.codice.ddf.features.test.DependencyVersionResolver;
import org.codice.ddf.features.test.Feature;
import org.codice.ddf.features.test.FeatureFile;
import org.codice.ddf.features.test.FeatureImpl;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.ops4j.pax.exam.options.UrlReference;

public class NotificationsFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("notifications")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private NotificationsFeatureFile() {}

  private NotificationsFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  public static FeatureFile featureFile() {
    return new NotificationsFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new NotificationsFeatureFile(filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static Feature notificationsCoreApi() {
    return new FeatureImpl(featureUrl, "notifications-core-api");
  }

  public static Feature notificationsCore() {
    return new FeatureImpl(featureUrl, "notifications-core");
  }

  public static Feature notificationsCommands() {
    return new FeatureImpl(featureUrl, "notifications-commands");
  }
}
