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
package org.codice.ddf.features.camel.test;

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
import org.codice.ddf.features.test.features.CamelFeatureFile;
import org.codice.ddf.features.test.features.TestUtilitiesFeatureFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ITCamelFeature {

  private static final String CAMEL_FEATURE_FILE =
      ITCamelFeature.class.getResource(File.separator + "features.xml").getPath();

  @Configuration
  public Option[] examConfiguration() {
    return options(
        kernelDistributionOption(),
        defaultDebuggingOptions(),
        includeDependencyPropertiesFile(),
        defaultVmOptions(),
        defaultPortsOptions(),
        addFeaturesToFeatureRepo(CamelFeatureFile.featureFile(CAMEL_FEATURE_FILE)),
        addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Inject FeatureServiceWrapper featuresService;

  @Test
  public void installCamel() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camel());
  }

  @Test
  public void installCamelCore() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelCore());
  }

  @Test
  public void installCamelCatalog() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelCatalog());
  }

  @Test
  public void installCamelSpring() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelSpring());
  }

  @Test
  public void installCamelAmqp() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelAmqp());
  }

  @Test
  public void installCamelAws() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelAws());
  }

  @Test
  public void installCamelContext() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelContext());
  }

  @Test
  public void installCamelCxf() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelCxf());
  }

  @Test
  public void installCamelEventadmin() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelEventadmin());
  }

  @Test
  public void installCamelFreemarker() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelFreemarker());
  }

  @Test
  public void installCamelHttp() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelHttp());
  }

  @Test
  public void installCamelJackson() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelJackson());
  }

  @Test
  public void installCamelJaxb() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelJaxb());
  }

  @Test
  public void installCamelJetty9() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelJetty9());
  }

  @Test
  public void installCamelJms() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelJms());
  }

  @Test
  public void installCamelProtobuf() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelProtobuf());
  }

  @Test
  public void installCamelQuartz2() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelQuartz2());
  }

  @Test
  public void installCamelSjms() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelSjms());
  }

  @Test
  public void installCamelSjms2() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelSjms2());
  }

  @Test
  public void installCamelStax() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelStax());
  }

  @Test
  public void installCamelTest() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(CamelFeatureFile.camelTest());
  }
}
