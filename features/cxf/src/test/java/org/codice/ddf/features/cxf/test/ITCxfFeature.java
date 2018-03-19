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
package org.codice.ddf.features.cxf.test;

import static org.codice.ddf.features.test.config.DebugOptions.defaultDebuggingOptions;
import static org.codice.ddf.features.test.config.DistributionOptions.includeDependencyPropertiesFile;
import static org.codice.ddf.features.test.config.DistributionOptions.kernelDistributionOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addBootFeatureOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addFeaturesToFeatureRepo;
import static org.codice.ddf.features.test.config.PortOptions.defaultPortsOptions;
import static org.codice.ddf.features.test.config.VmOptions.defaultVmOptions;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import javax.inject.Inject;
import org.codice.ddf.features.test.FeatureInstallException;
import org.codice.ddf.features.test.FeatureServiceWrapper;
import org.codice.ddf.features.test.FeatureUninstallException;
import org.codice.ddf.features.test.features.CxfFeatureFile;
import org.codice.ddf.features.test.features.TestUtilitiesFeatureFile;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ITCxfFeature {

  private static final String CXF_FEATURE_FILE =
      ITCxfFeature.class.getResource(File.separator + "features.xml").getPath();

  @Configuration
  public Option[] examConfiguration() {
    return options(
        kernelDistributionOption(),
        defaultDebuggingOptions(),
        includeDependencyPropertiesFile(),
        defaultVmOptions(),
        defaultPortsOptions(),
        addFeaturesToFeatureRepo(CxfFeatureFile.featureFile(CXF_FEATURE_FILE)),
        addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Inject FeatureServiceWrapper featuresService;

  @Test
  public void installCxfSpecs() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfSpecs());
  }

  @Test
  public void installCxfJaxb() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfJaxb());
  }

  @Test
  public void installCxfAbdera() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfAbdera());
  }

  @Test
  public void installWss4j() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.wss4j());
  }

  @Test
  public void installCxfCore() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfCore());
  }

  @Test
  public void installCxfCommands() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfCommands());
  }

  @Test
  public void installCxfWsdl() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsdl());
  }

  @Test
  public void installCxfWsPolicy() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsPolicy());
  }

  @Test
  public void installCxfWsAddr() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsAddr());
  }

  @Test
  public void installCxfWsRm() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsRm());
  }

  @Test
  public void installCxfWsMex() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsMex());
  }

  @Test
  public void installCxfWsSecurity() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsSecurity());
  }

  @Test
  public void installCxfRtSecurity() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRtSecurity());
  }

  @Test
  public void installCxfRtSecuritySaml() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRtSecuritySaml());
  }

  @Test
  public void installCxfHttpClient() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfHttpClient());
  }

  @Test
  public void installCxfHttp() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfHttp());
  }

  @Test
  public void installCxfHttpJetty() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfHttpJetty());
  }

  @Test
  public void installCxfHttpAsync() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfHttpAsync());
  }

  @Test
  public void installCxfHttpNettyClient()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfHttpNettyClient());
  }

  @Test
  public void installCxfHttpNettyServer()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfHttpNettyServer());
  }

  @Test
  public void installCxfBindingsSoap() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfBindingsSoap());
  }

  @Test
  public void installCxfJaxws() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfJaxws());
  }

  @Test
  public void installCxfJaxrs() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfJaxrs());
  }

  @Test
  public void installCxfRsSecurityXml() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRsSecurityXml());
  }

  @Test
  public void installCxfRsSecuritySsoSaml()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRsSecuritySsoSaml());
  }

  @Test
  public void installCxfRsSecurityCors() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRsSecurityCors());
  }

  @Test
  public void installCxfRsSecurityJose() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRsSecurityJose());
  }

  @Test
  public void installCxfJackson() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfJackson());
  }

  @Test
  public void installCxfRsDescriptionSwagger2()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfRsDescriptionSwagger2());
  }

  @Test
  public void installCxfDatabindingAegis()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfDatabindingAegis());
  }

  @Test
  public void installCxfDatabindingJibx()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfDatabindingJibx());
  }

  @Test
  public void installCxfDatabindingJaxb()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfDatabindingJaxb());
  }

  @Test
  public void installCxfDatabindingXmlbeans()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfDatabindingXmlbeans());
  }

  @Test
  public void installCxfFeaturesClustering()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfFeaturesClustering());
  }

  @Test
  public void installCxfFeaturesLogging()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfFeaturesLogging());
  }

  @Test
  public void installCxfFeaturesThrottling()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfFeaturesThrottling());
  }

  @Test
  public void installCxfFeaturesMetrics()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfFeaturesMetrics());
  }

  @Test
  public void installCxfBindingsCorba() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfBindingsCorba());
  }

  @Test
  public void installCxfBindingsColoc() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfBindingsColoc());
  }

  @Test
  public void installCxfBindingsObject() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfBindingsObject());
  }

  @Test
  public void installCxfTransportsLocal()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfTransportsLocal());
  }

  @Test
  public void installCxfTransportsJms() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfTransportsJms());
  }

  @Test
  public void installCxfTransportsWebsocketClient()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfTransportsWebsocketClient());
  }

  @Test
  public void installCxfTransportsWebsocketServer()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfTransportsWebsocketServer());
  }

  @Test
  public void installCxfJavascript() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfJavascript());
  }

  @Test
  public void installCxfFrontendJavascript()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfFrontendJavascript());
  }

  @Test
  public void installCxfXjcRuntime() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfXjcRuntime());
  }

  @Test
  public void installCxfTools() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfTools());
  }

  @Test
  public void installCxfSts() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfSts());
  }

  @Test
  public void installCxfWsnApi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfWsnApi());
  }

  @Test
  public void installCxfBeanValidationCore()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfBeanValidationCore());
  }

  @Test
  public void installCxfBeanValidation() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfBeanValidation());
  }

  @Test
  public void installCxfManagementWeb() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfManagementWeb());
  }

  @Test
  @Ignore // Requires a bundle with a value of (osgi.extender=pax.cdi)
  public void installCxfJaxrsCdi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CxfFeatureFile.cxfJaxrsCdi());
  }
}
