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
package org.codice.ddf.test.common.features;

import static org.codice.ddf.test.common.options.TestResourcesOptions.getTestResource;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.bundle.core.BundleState;
import org.apache.karaf.features.FeaturesService;
import org.codice.ddf.platform.util.XMLUtils;
import org.codice.ddf.test.common.options.BasicOptions;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FeatureUtilities {

  private static final Logger LOGGER = LoggerFactory.getLogger(FeatureUtilities.class);

  public static final String FEATURE_NAME_XPATH = "//*[local-name() = 'feature']/@name";

  // TODO: tbatie - 8/6/18 - Look into the TestWatcher or something else for all this test logging crap
  public static final String FEATURE_INSTALL_TIMES_LOG = getTestResource("/feature-install-times.log");

  public static final String FEATURE_UNINSTALL_TIMES_LOG = getTestResource(
          "/feature-uninstall-times.log");

  private static BundleService bundleService;

  /**
   * Returns a list of feature names defined in a feature file.
   *
   * @param featureFilePath
   * @return feature names in feature file
   */
  public static List<String> getFeaturesFromFeatureRepo(String featureFilePath) {
    XPath xPath = XPathFactory.newInstance().newXPath();
    List<String> featureNames = new ArrayList<>();

    try (FileInputStream fi = new FileInputStream(new File(featureFilePath))) {
      Document featuresFile = XMLUtils.getInstance().getSecureDocumentBuilder(false).parse(fi);

      NodeList features =
          (NodeList)
              xPath.compile(FEATURE_NAME_XPATH).evaluate(featuresFile, XPathConstants.NODESET);

      for (int i = 0; i < features.getLength(); i++) {
        featureNames.add(features.item(i).getNodeValue());
      }
    } catch (ParserConfigurationException
        | XPathExpressionException
        | IOException
        | SAXException e) {
      throw new RuntimeException(
              "Unable to read features names in feature file at: " + featureFilePath, e);
    }
    return featureNames;
  }

  /**
   * Converts the given feature file into a list of feature name parameters for parameterized
   * testing.
   *
   * @param featureFilePath
   * @return feature name parameters
   */
  public static List<Object[]> featureRepoToFeatureParameters(String featureFilePath) {
    return featureRepoToFeatureParameters(featureFilePath, Collections.emptyList());
  }

  /**
   * Converts the given feature file into a list of feature name parameters for parameterized
   * testing.
   *
   * @param featureFilePath
   * @param ignoredFeatures excludes the specified features from the parameters
   * @return feature name parameters
   */
  public static List<Object[]> featureRepoToFeatureParameters(
      String featureFilePath, List<String> ignoredFeatures) {
    return getFeaturesFromFeatureRepo(featureFilePath)
        .stream()
            .filter(f -> !ignoredFeatures.contains(f))
            .map(feat -> new Object[] {feat})
            .collect(Collectors.toList());
  }

  /**
   * Creates a feature repo object from the specified feature file
   *
   * @param filePath
   * @return
   */
  public static FeatureRepo toFeatureRepo(String filePath) {
    return new FeatureRepoImpl(new UrlProvisionOption("file:" + filePath));
  }

  /**
   * Creates a feature object from the specified feature file
   *
   * @param filePath
   * @return
   */
  public static Feature toFeature(String filePath, String feature) {
    return new FeatureImpl(toFeatureRepo(filePath).getFeatureFileUrl(), feature);
  }

  /**
   * Installs and uninstalls the specified feature. Ensures all bundles move into the Active state
   * before uninstalling.
   *
   * @param featuresService
   * @param featureName
   * @throws Exception
   */
  public static void installAndUninstallFeature(FeaturesService featuresService, String featureName)
          throws Exception {
    try {
      installFeature(featuresService, featureName);
    } finally {
      if (featuresService.isInstalled(featuresService.getFeature(featureName))) {
        uninstallFeature(featuresService, featureName);
      }
    }
  }

  /**
   * Installs the specified feature. Waits for all bundles to move into the Active state.
   *
   * @param featuresService
   * @param featureName
   * @throws Exception
   */
  public static void installFeature(FeaturesService featuresService, String featureName)
          throws Exception {
    long startTime = System.currentTimeMillis();
    LOGGER.info("{} feature installing", featureName);
    featuresService.installFeature(featureName);
    List<Bundle> inactiveBundles = waitForBundles();
    if (!inactiveBundles.isEmpty()) {
      fail("Failed to install feature: " + featureName + ", exceeded bundle startup timeout of: "
              + FEATURES_AND_BUNDLES_TIMEOUT + "\n" + bundleDiagsToString(inactiveBundles));
    }
    long installTime = (System.currentTimeMillis() - startTime);

    LOGGER.info("{} feature installed in {} ms.",
            featureName,
            installTime);
    recordFeatureInstallTime(featureName, installTime);
  }

  /**
   * Uninstalls the specified feature.
   *
   * @param featuresService
   * @param featureName
   * @throws Exception
   */
  public static void uninstallFeature(FeaturesService featuresService, String featureName)
          throws Exception {
    long startTime = System.currentTimeMillis();
    LOGGER.info("{} feature uninstalling", featureName);
    featuresService.uninstallFeature(featureName);
    long uninstallTime = (System.currentTimeMillis() - startTime);
    LOGGER.info("{} feature uninstalled in {} ms.",
            featureName,
            uninstallTime);
    recordFeatureUninstallTime(featureName, uninstallTime);
  }

  private static void recordFeatureInstallTime(String feature, long installTime) {
    BasicOptions.writeToFile(FEATURE_INSTALL_TIMES_LOG, "%s: %n ms", feature, Long.toString(installTime));
  }

  private static void recordFeatureUninstallTime(String feature, long uninstallTime) {
    BasicOptions.writeToFile(FEATURE_UNINSTALL_TIMES_LOG, "%s: %n ms", feature, Long.toString(uninstallTime));
  }

  // DDF-3768 ServiceManager should be moved to test-common and this duplicate code removed.
  public static final long FEATURES_AND_BUNDLES_TIMEOUT = TimeUnit.MINUTES.toMillis(1);

  public static String bundleDiagsToString(List<Bundle> bundles) {
    return bundles.stream()
            .map(b -> b.getSymbolicName() + "\n" + getBundleService().getDiag(b) + "\n")
            .collect(Collectors.joining());
  }

  public static String bundleNamesToString(List<Bundle> bundles) {
    return bundles.stream()
            .map(b -> getBundleService().getInfo(b)
                    .getName() + "\n")
            .collect(Collectors.joining());
  }

  public static List<Bundle> getInactiveBundles() {
    List<Bundle> inactiveBundles = new ArrayList<>();

    for (Bundle bundle : getBundleContext().getBundles()) {
      BundleInfo bundleInfo = getBundleService().getInfo(bundle);
      BundleState bundleState = bundleInfo.getState();
      if (bundleInfo.isFragment()) {
        if (!BundleState.Resolved.equals(bundleState)) {
          inactiveBundles.add(bundle);
        }
      } else if (bundleState != null) {
        if (BundleState.Failure.equals(bundleState)) {
          inactiveBundles.add(bundle);
        } else if (!BundleState.Active.equals(bundleState)) {
          inactiveBundles.add(bundle);
        }
      }
    }

    return inactiveBundles;
  }

  public static List<Bundle> waitForBundles() throws InterruptedException {
    boolean ready = getInactiveBundles().isEmpty();

    long timeoutLimit = System.currentTimeMillis() + FEATURES_AND_BUNDLES_TIMEOUT;
    while (!ready) {
      List<Bundle> inactiveBundles = getInactiveBundles();
      ready = inactiveBundles.isEmpty();
      if (!ready) {
        LOGGER.info("Waiting on bundles:\n" + bundleNamesToString(inactiveBundles));
        if (System.currentTimeMillis() > timeoutLimit) {
          return inactiveBundles;
        }
        Thread.sleep(5000);
      }
    }

    return new ArrayList<>();
  }

  public static BundleService getBundleService() {
    if(bundleService == null) {
      bundleService = getService(BundleService.class);
    }

    return bundleService;
  }

  public static <S> S getService(Class<S> aClass) {
    return getService(getBundleContext().getServiceReference(aClass));
  }

  public static <S> S getService(ServiceReference<S> serviceReference) {
    return getBundleContext().getService(serviceReference);
  }

  public static <S> ServiceReference<S> getServiceReference(Class<S> aClass) {
    return getBundleContext().getServiceReference(aClass);
  }

  public static BundleContext getBundleContext() {
    Bundle bundle = FrameworkUtil.getBundle(FeatureUtilities.class);
    if (bundle != null) {
      return bundle.getBundleContext();
    }
    return null;
  }
}
