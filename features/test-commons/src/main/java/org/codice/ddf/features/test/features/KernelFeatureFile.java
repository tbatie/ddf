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

public class KernelFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("kernel")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private KernelFeatureFile() {}

  private KernelFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  public static FeatureFile featureFile() {
    return new KernelFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new KernelFeatureFile(filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static Feature javaxValidation() {
    return new FeatureImpl(featureUrl, "javax-validation");
  }

  public static Feature jodaConverter() {
    return new FeatureImpl(featureUrl, "joda-converter");
  }

  public static Feature javaxInject() {
    return new FeatureImpl(featureUrl, "javax-inject");
  }

  public static Feature slf4j() {
    return new FeatureImpl(featureUrl, "slf4j");
  }

  public static Feature paxSwissbox() {
    return new FeatureImpl(featureUrl, "pax-swissbox");
  }

  public static Feature apacheCommons() {
    return new FeatureImpl(featureUrl, "apache-commons");
  }

  public static Feature saxon() {
    return new FeatureImpl(featureUrl, "saxon");
  }

  public static Feature guava() {
    return new FeatureImpl(featureUrl, "guava");
  }
}
