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
package org.codice.ddf.features.test.config;

import java.util.Arrays;
import org.codice.ddf.features.test.Feature;
import org.codice.ddf.features.test.FeatureFile;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

public class FeatureOptions {

  public static Option addFeaturesToFeatureRepo(FeatureFile... features) {
    return new DefaultCompositeOption(
        Arrays.stream(features)
            .map(FeatureOptions::addFeatureToFeatureRepo)
            .toArray(Option[]::new));
  }

  public static Option addBootFeatureOption(Feature... features) {
    return new DefaultCompositeOption(
        Arrays.stream(features).map(FeatureOptions::addBootFeatureOption).toArray(Option[]::new));
  }

  public static Option addFeatureToFeatureRepo(FeatureFile feature) {
    return KarafDistributionOption.features(feature.getFeatureFileUrl());
  }

  public static Option addBootFeatureOption(Feature feature) {
    return KarafDistributionOption.features(feature.getFeatureFileUrl(), feature.featureName());
  }
}
