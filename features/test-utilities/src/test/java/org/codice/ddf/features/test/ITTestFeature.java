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
package org.codice.ddf.features.test;

import static org.codice.ddf.features.test.config.DistributionOptions.includeDependencyPropertiesFile;
import static org.codice.ddf.features.test.config.DistributionOptions.kernelDistributionOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addBootFeatureOption;
import static org.codice.ddf.features.test.config.FeatureOptions.addFeaturesToFeatureRepo;
import static org.codice.ddf.features.test.config.PortOptions.defaultPortsOptions;
import static org.codice.ddf.features.test.config.VmOptions.defaultVmOptions;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;

import javax.inject.Inject;

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
public class ITTestFeature {

  private static final String TEST_FEATURE_FILE =
          ITTestFeature.class.getResource(File.separator + "features.xml").getPath();

  @Configuration
  public Option[] examConfiguration() {
    return options(
        kernelDistributionOption(),
        includeDependencyPropertiesFile(),
        defaultVmOptions(),
        defaultPortsOptions(),
        addFeaturesToFeatureRepo(TestUtilitiesFeatureFile.featureFile(TEST_FEATURE_FILE)),
        addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Inject FeatureServiceWrapper featuresService;

  @Test
  public void installHamcrestAll() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(TestUtilitiesFeatureFile.hamcrestAll());
  }
}
