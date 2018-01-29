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

public class UtilitiesFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("utilities")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private UtilitiesFeatureFile() {}

  private UtilitiesFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  public static FeatureFile featureFile() {
    return new UtilitiesFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new UtilitiesFeatureFile(filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static Feature utilUuidgeneratorApi() {
    return new FeatureImpl(featureUrl, "util-uuidgenerator-api");
  }

  public static Feature utilUuidgenerator() {
    return new FeatureImpl(featureUrl, "util-uuidgenerator");
  }

  public static Feature platformCountryLocal() {
    return new FeatureImpl(featureUrl, "platform-country-local");
  }

  public static Feature platformUsng4j() {
    return new FeatureImpl(featureUrl, "platform-usng4j");
  }

  public static Feature parserXml() {
    return new FeatureImpl(featureUrl, "parser-xml");
  }

  public static Feature platformEmail() {
    return new FeatureImpl(featureUrl, "platform-email");
  }

  public static Feature actionCoreApi() {
    return new FeatureImpl(featureUrl, "action-core-api");
  }

  public static Feature actionCoreImpl() {
    return new FeatureImpl(featureUrl, "action-core-impl");
  }

  public static Feature error() {
    return new FeatureImpl(featureUrl, "error");
  }

  public static Feature mimeCoreApi() {
    return new FeatureImpl(featureUrl, "mime-core-api");
  }

  public static Feature mimeCore() {
    return new FeatureImpl(featureUrl, "mime-core");
  }

  public static Feature mimeTikaResolver() {
    return new FeatureImpl(featureUrl, "mime-tika-resolver");
  }

  public static Feature compressionExi() {
    return new FeatureImpl(featureUrl, "compression-exi");
  }

  public static Feature compressionGzip() {
    return new FeatureImpl(featureUrl, "compression-gzip");
  }

  public static Feature commonSystem() {
    return new FeatureImpl(featureUrl, "common-system");
  }

  public static Feature platformApi() {
    return new FeatureImpl(featureUrl, "platform-api");
  }

  public static Feature platformLogging() {
    return new FeatureImpl(featureUrl, "platform-logging");
  }
}
