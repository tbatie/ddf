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
package org.codice.ddf.features.utilities.test;

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
import org.codice.ddf.features.test.features.TestUtilitiesFeatureFile;
import org.codice.ddf.features.test.features.UtilitiesFeatureFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ITUtilitiesFeature {

  private static final String UTILITIES_FEATURE_FILE =
      ITUtilitiesFeature.class.getResource(File.separator + "features.xml").getPath();

  @Configuration
  public Option[] examConfiguration() {
    return options(
        kernelDistributionOption(),
        defaultDebuggingOptions(),
        defaultVmOptions(),
        defaultPortsOptions(),
        includeDependencyPropertiesFile(),
        addFeaturesToFeatureRepo(UtilitiesFeatureFile.featureFile(UTILITIES_FEATURE_FILE)),
        addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Inject FeatureServiceWrapper featuresService;

  @Test
  public void installUtilUuidgeneratorApi()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.utilUuidgeneratorApi());
  }

  @Test
  public void installUtilUuidgenerator() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.utilUuidgenerator());
  }

  @Test
  public void installPlatformCountryLocal()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.platformCountryLocal());
  }

  @Test
  public void installPlatformUsng4j() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.platformUsng4j());
  }

  @Test
  public void installParserXml() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.parserXml());
  }

  @Test
  public void installPlatformEmail() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.platformEmail());
  }

  @Test
  public void installActionCoreApi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.actionCoreApi());
  }

  @Test
  public void installActionCoreImpl() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.actionCoreImpl());
  }

  @Test
  public void installError() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.error());
  }

  @Test
  public void installMimeCoreApi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.mimeCoreApi());
  }

  @Test
  public void installMimeCore() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.mimeCore());
  }

  @Test
  public void installMimeTikaResolver() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.mimeTikaResolver());
  }

  @Test
  public void installCompressionExi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.compressionExi());
  }

  @Test
  public void installCompressionGzip() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.compressionGzip());
  }

  @Test
  public void installCommonSystem() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.commonSystem());
  }

  @Test
  public void installPlatformApi() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.platformApi());
  }

  @Test
  public void installPlatformLogging() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(UtilitiesFeatureFile.platformLogging());
  }
}
