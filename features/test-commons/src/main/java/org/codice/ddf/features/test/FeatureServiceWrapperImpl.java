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

import org.apache.karaf.features.FeaturesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureServiceWrapperImpl implements FeatureServiceWrapper {

  private static final Logger LOGGER =
          LoggerFactory.getLogger(FeatureServiceWrapperImpl.class);

  private FeaturesService featuresService;

  public FeatureServiceWrapperImpl(FeaturesService featuresService) {
    this.featuresService = featuresService;
  }

  public void installAndUninstallFeature(Feature feature)
      throws FeatureInstallException, FeatureUninstallException {
    installFeature(feature);
    uninstallFeature(feature);
  }

  @Override
  public void installAndUninstallFeature(String feature)
          throws FeatureUninstallException, FeatureInstallException {
    installFeature(feature);
    uninstallFeature(feature);
  }

  public void installFeature(Feature feature) throws FeatureInstallException {
    installFeature(feature.featureName());
  }

  @Override
  public void installFeature(String feature) throws FeatureInstallException {
    try {
      System.out.println("Installing feature::::: " + feature);
      LOGGER.info("Installing feature: " + feature);
      featuresService.installFeature(feature);
    } catch (Exception e) {
      throw new FeatureInstallException(e);
    }
  }

  public void uninstallFeature(String feature) throws FeatureUninstallException {
    try {
      LOGGER.info("Uninstalling feature: " + feature);
      featuresService.uninstallFeature(feature);
    } catch (Exception e) {
      throw new FeatureUninstallException(e);
    }
  }

  public void uninstallFeature(Feature feature) throws FeatureUninstallException {
    uninstallFeature(feature.featureName());
  }
}
