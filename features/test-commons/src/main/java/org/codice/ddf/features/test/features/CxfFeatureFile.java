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

public class CxfFeatureFile implements FeatureFile {

  private static UrlReference featureUrl =
      maven()
          .groupId("ddf.features")
          .artifactId("cxf")
          .type("xml")
          .classifier("features")
          .version(DependencyVersionResolver.resolver());

  private CxfFeatureFile() {}

  private CxfFeatureFile(String filePath) {
    featureUrl = new UrlProvisionOption("file:" + filePath);
  }

  public static FeatureFile featureFile() {
    return new CxfFeatureFile();
  }

  public static FeatureFile featureFile(String filePath) {
    return new CxfFeatureFile(filePath);
  }

  @Override
  public UrlReference getFeatureFileUrl() {
    return featureUrl;
  }

  public static Feature cxfSpecs() {
    return new FeatureImpl(featureUrl, "cxf-specs");
  }

  public static Feature cxfJaxb() {
    return new FeatureImpl(featureUrl, "cxf-jaxb");
  }

  public static Feature cxfAbdera() {
    return new FeatureImpl(featureUrl, "cxf-abdera");
  }

  public static Feature wss4j() {
    return new FeatureImpl(featureUrl, "wss4j");
  }

  public static Feature cxfCore() {
    return new FeatureImpl(featureUrl, "cxf-core");
  }

  public static Feature cxfCommands() {
    return new FeatureImpl(featureUrl, "cxf-commands");
  }

  public static Feature cxfWsdl() {
    return new FeatureImpl(featureUrl, "cxf-wsdl");
  }

  public static Feature cxfWsPolicy() {
    return new FeatureImpl(featureUrl, "cxf-ws-policy");
  }

  public static Feature cxfWsAddr() {
    return new FeatureImpl(featureUrl, "cxf-ws-addr");
  }

  public static Feature cxfWsRm() {
    return new FeatureImpl(featureUrl, "cxf-ws-rm");
  }

  public static Feature cxfWsMex() {
    return new FeatureImpl(featureUrl, "cxf-ws-mex");
  }

  public static Feature cxfWsSecurity() {
    return new FeatureImpl(featureUrl, "cxf-ws-security");
  }

  public static Feature cxfRtSecurity() {
    return new FeatureImpl(featureUrl, "cxf-rt-security");
  }

  public static Feature cxfRtSecuritySaml() {
    return new FeatureImpl(featureUrl, "cxf-rt-security-saml");
  }

  public static Feature cxfHttpClient() {
    return new FeatureImpl(featureUrl, "cxf-http-client");
  }

  public static Feature cxfHttp() {
    return new FeatureImpl(featureUrl, "cxf-http");
  }

  public static Feature cxfHttpJetty() {
    return new FeatureImpl(featureUrl, "cxf-http-jetty");
  }

  public static Feature cxfHttpAsync() {
    return new FeatureImpl(featureUrl, "cxf-http-async");
  }

  public static Feature cxfHttpNettyClient() {
    return new FeatureImpl(featureUrl, "cxf-http-netty-client");
  }

  public static Feature cxfHttpNettyServer() {
    return new FeatureImpl(featureUrl, "cxf-http-netty-server");
  }

  public static Feature cxfBindingsSoap() {
    return new FeatureImpl(featureUrl, "cxf-bindings-soap");
  }

  public static Feature cxfJaxws() {
    return new FeatureImpl(featureUrl, "cxf-jaxws");
  }

  public static Feature cxfJaxrs() {
    return new FeatureImpl(featureUrl, "cxf-jaxrs");
  }

  public static Feature cxfRsSecurityXml() {
    return new FeatureImpl(featureUrl, "cxf-rs-security-xml");
  }

  public static Feature cxfRsSecuritySsoSaml() {
    return new FeatureImpl(featureUrl, "cxf-rs-security-sso-saml");
  }

  public static Feature cxfRsSecurityCors() {
    return new FeatureImpl(featureUrl, "cxf-rs-security-cors");
  }

  public static Feature cxfRsSecurityJose() {
    return new FeatureImpl(featureUrl, "cxf-rs-security-jose");
  }

  public static Feature cxfJackson() {
    return new FeatureImpl(featureUrl, "cxf-jackson");
  }

  public static Feature cxfRsDescriptionSwagger2() {
    return new FeatureImpl(featureUrl, "cxf-rs-description-swagger2");
  }

  public static Feature cxfDatabindingAegis() {
    return new FeatureImpl(featureUrl, "cxf-databinding-aegis");
  }

  public static Feature cxfDatabindingJibx() {
    return new FeatureImpl(featureUrl, "cxf-databinding-jibx");
  }

  public static Feature cxfDatabindingJaxb() {
    return new FeatureImpl(featureUrl, "cxf-databinding-jaxb");
  }

  public static Feature cxfDatabindingXmlbeans() {
    return new FeatureImpl(featureUrl, "cxf-databinding-xmlbeans");
  }

  public static Feature cxfFeaturesClustering() {
    return new FeatureImpl(featureUrl, "cxf-features-clustering");
  }

  public static Feature cxfFeaturesLogging() {
    return new FeatureImpl(featureUrl, "cxf-features-logging");
  }

  public static Feature cxfFeaturesThrottling() {
    return new FeatureImpl(featureUrl, "cxf-features-throttling");
  }

  public static Feature cxfFeaturesMetrics() {
    return new FeatureImpl(featureUrl, "cxf-features-metrics");
  }

  public static Feature cxfBindingsCorba() {
    return new FeatureImpl(featureUrl, "cxf-bindings-corba");
  }

  public static Feature cxfBindingsColoc() {
    return new FeatureImpl(featureUrl, "cxf-bindings-coloc");
  }

  public static Feature cxfBindingsObject() {
    return new FeatureImpl(featureUrl, "cxf-bindings-object");
  }

  public static Feature cxfTransportsLocal() {
    return new FeatureImpl(featureUrl, "cxf-transports-local");
  }

  public static Feature cxfTransportsJms() {
    return new FeatureImpl(featureUrl, "cxf-transports-jms");
  }

  public static Feature cxfTransportsWebsocketClient() {
    return new FeatureImpl(featureUrl, "cxf-transports-websocket-client");
  }

  public static Feature cxfTransportsWebsocketServer() {
    return new FeatureImpl(featureUrl, "cxf-transports-websocket-server");
  }

  public static Feature cxfJavascript() {
    return new FeatureImpl(featureUrl, "cxf-javascript");
  }

  public static Feature cxfFrontendJavascript() {
    return new FeatureImpl(featureUrl, "cxf-frontend-javascript");
  }

  public static Feature cxfXjcRuntime() {
    return new FeatureImpl(featureUrl, "cxf-xjc-runtime");
  }

  public static Feature cxfTools() {
    return new FeatureImpl(featureUrl, "cxf-tools");
  }

  public static Feature cxfSts() {
    return new FeatureImpl(featureUrl, "cxf-sts");
  }

  public static Feature cxfWsnApi() {
    return new FeatureImpl(featureUrl, "cxf-wsn-api");
  }

  public static Feature cxfBeanValidationCore() {
    return new FeatureImpl(featureUrl, "cxf-bean-validation-core");
  }

  public static Feature cxfBeanValidation() {
    return new FeatureImpl(featureUrl, "cxf-bean-validation");
  }

  public static Feature cxfManagementWeb() {
    return new FeatureImpl(featureUrl, "cxf-management-web");
  }

  public static Feature cxfJaxrsCdi() {
    return new FeatureImpl(featureUrl, "cxf-jaxrs-cdi");
  }
}
