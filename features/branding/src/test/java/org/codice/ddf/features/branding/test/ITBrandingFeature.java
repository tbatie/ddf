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
package org.codice.ddf.features.branding.test;

import static org.codice.ddf.features.test.config.DebugOptions.defaultDebuggingOptions;
import static org.codice.ddf.features.test.config.DistributionOptions.includeDependencyPropertiesFile;
import static org.codice.ddf.features.test.config.DistributionOptions.kernelDistributionOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addBootFeatureOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addFeaturesToFeatureRepo;
import static org.codice.ddf.features.test.config.PortOptions.defaultPortsOptions;
import static org.codice.ddf.features.test.config.VmOptions.defaultVmOptions;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.codice.ddf.features.test.FeatureFileUtils;
import org.codice.ddf.features.test.FeatureInstallException;
import org.codice.ddf.features.test.FeatureServiceWrapper;
import org.codice.ddf.features.test.FeatureUninstallException;
import org.codice.ddf.features.test.features.BrandingFeatureFile;
import org.codice.ddf.features.test.features.TestUtilitiesFeatureFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExamParameterized;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExamParameterized.class)
@ExamReactorStrategy(PerClass.class)
public class ITBrandingFeature {

  private static final URL BRANDING_FEATURE_PATH = ITBrandingFeature.class.getResource("/features.xml");

  @Configuration
  public static Option[] examConfiguration() {
    return options(
            kernelDistributionOption(),
            includeDependencyPropertiesFile(),
            defaultVmOptions(),
            defaultDebuggingOptions(),
            defaultPortsOptions(),
            addFeaturesToFeatureRepo(BrandingFeatureFile.featureFile(BRANDING_FEATURE_PATH.getPath())),
            addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Parameterized.Parameters(name = "feature: {0}")
  public static List<Object[]> getParameters() {
//    return Arrays.asList(new Object[][]{
//            {"branding-api"},
//            {"ddf-branding"}
//    });
    return FeatureFileUtils.featureFileToFeatureParameters(getFeatureFile());
  }

  @Inject FeatureServiceWrapper featuresService;

  private String featureName;

  public ITBrandingFeature(String featureName) {
    this.featureName = featureName;
  }

  public static String getFeatureFile() {
    try {
      return IOUtils.toString(BRANDING_FEATURE_PATH, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
   }

  @Test
  public void installAndUninstallFeature() throws FeatureInstallException, FeatureUninstallException {
    featuresService.installAndUninstallFeature(featureName);
  }
}
