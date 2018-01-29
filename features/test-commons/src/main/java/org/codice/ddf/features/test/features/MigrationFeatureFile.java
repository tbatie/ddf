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

public class MigrationFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("migration")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private MigrationFeatureFile() {}

  private MigrationFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  public static FeatureFile featureFile() {
    return new MigrationFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new MigrationFeatureFile(filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static Feature platformMigratableApi() {
    return new FeatureImpl(featureUrl, "platform-migratable-api");
  }

  public static Feature platformMigratable() {
    return new FeatureImpl(featureUrl, "platform-migratable");
  }

  public static Feature platformMigration() {
    return new FeatureImpl(featureUrl, "platform-migration");
  }

  public static Feature io() {
    return new FeatureImpl(featureUrl, "io");
  }

  public static Feature platformMigrateAll() {
    return new FeatureImpl(featureUrl, "platform-migrate-all");
  }

  public static Feature securityMigratable() {
    return new FeatureImpl(featureUrl, "security-migratable");
  }
}
