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
package org.codice.ddf.features.solr.test;

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
import org.codice.ddf.features.test.features.SolrFeatureFile;
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
public class ITSolrFeature {

  private static final String SOLR_FEATURE_FILE =
      ITSolrFeature.class.getResource(File.separator + "features.xml").getPath();

  @Configuration
  public Option[] examConfiguration() {
    return options(
        kernelDistributionOption(),
        defaultDebuggingOptions(),
        defaultVmOptions(),
        defaultPortsOptions(),
        includeDependencyPropertiesFile(),
        addFeaturesToFeatureRepo(SolrFeatureFile.featureFile(SOLR_FEATURE_FILE)),
        addBootFeatureOption(TestUtilitiesFeatureFile.featureTestingUtils()));
  }

  @Inject FeatureServiceWrapper featuresService;

  @Test
  public void installSolr() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SolrFeatureFile.solr());
  }

  @Test
  public void installSolrDecanterAppender()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SolrFeatureFile.solrDecanterAppender());
  }

  @Test
  public void installPersistenceCoreApi()
      throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SolrFeatureFile.persistenceCoreApi());
  }

  @Test
  public void installPersistenceCore() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SolrFeatureFile.persistenceCore());
  }

  @Test
  public void installBanana() throws FeatureUninstallException, FeatureInstallException {
    featuresService.installAndUninstallFeature(SolrFeatureFile.banana());
  }
}
