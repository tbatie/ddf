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

public class CamelFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("camel")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private CamelFeatureFile() {}

  private CamelFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  public static FeatureFile featureFile() {
    return new CamelFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new CamelFeatureFile(filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static Feature camel() {
    return new FeatureImpl(featureUrl, "camel");
  }

  public static Feature camelCore() {
    return new FeatureImpl(featureUrl, "camel-catalog");
  }

  public static Feature camelCatalog() {
    return new FeatureImpl(featureUrl, "camel-blueprint");
  }

  public static Feature camelSpring() {
    return new FeatureImpl(featureUrl, "camel-spring");
  }

  public static Feature camelAmqp() {
    return new FeatureImpl(featureUrl, "camel-amqp");
  }

  public static Feature camelAws() {
    return new FeatureImpl(featureUrl, "camel-aws");
  }

  public static Feature camelContext() {
    return new FeatureImpl(featureUrl, "camel-context");
  }

  public static Feature camelCxf() {
    return new FeatureImpl(featureUrl, "camel-cxf");
  }

  public static Feature camelEventadmin() {
    return new FeatureImpl(featureUrl, "camel-eventadmin");
  }

  public static Feature camelFreemarker() {
    return new FeatureImpl(featureUrl, "camel-freemarker");
  }

  public static Feature camelHttp() {
    return new FeatureImpl(featureUrl, "camel-http");
  }

  public static Feature camelJackson() {
    return new FeatureImpl(featureUrl, "camel-jackson");
  }

  public static Feature camelJaxb() {
    return new FeatureImpl(featureUrl, "camel-jaxb");
  }

  public static Feature camelJetty9() {
    return new FeatureImpl(featureUrl, "camel-jetty9");
  }

  public static Feature camelJms() {
    return new FeatureImpl(featureUrl, "camel-jms");
  }

  public static Feature camelProtobuf() {
    return new FeatureImpl(featureUrl, "camel-protobuf");
  }

  public static Feature camelQuartz2() {
    return new FeatureImpl(featureUrl, "camel-quartz2");
  }

  public static Feature camelSjms() {
    return new FeatureImpl(featureUrl, "camel-sjms");
  }

  public static Feature camelSjms2() {
    return new FeatureImpl(featureUrl, "camel-sjms2");
  }

  public static Feature camelStax() {
    return new FeatureImpl(featureUrl, "camel-stax");
  }

  public static Feature camelTest() {
    return new FeatureImpl(featureUrl, "camel-test");
  }
}
