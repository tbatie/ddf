/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 **/
package org.codice.ui.admin.wizard.config.handlers;

import static org.apache.karaf.features.FeaturesService.Option.NoAutoRefreshBundles;

import java.util.EnumSet;

import org.apache.karaf.features.FeatureState;
import org.apache.karaf.features.FeaturesService;
import org.codice.ui.admin.wizard.config.ConfigHandler;
import org.codice.ui.admin.wizard.config.ConfiguratorException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transactional handler for starting and stopping features.
 */
public class FeatureConfigHandler implements ConfigHandler<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureConfigHandler.class);

    private final String featureName;

    private final boolean newState;

    private final BundleContext bundleContext;

    private final boolean initActivationState;

    private FeatureConfigHandler(String featureName, boolean newState,
            BundleContext bundleContext) {
        this.featureName = featureName;
        this.newState = newState;
        this.bundleContext = bundleContext;

        initActivationState = lookupFeatureStatus(getFeaturesService(), featureName);
    }

    /**
     * Creates a handler that will start a feature as part of a transaction.
     *
     * @param featureName   the name of the feature to start
     * @param bundleContext context needed for OSGi interaction
     * @return instance of this class
     */
    public static FeatureConfigHandler forStart(String featureName, BundleContext bundleContext) {
        return new FeatureConfigHandler(featureName, true, bundleContext);
    }

    /**
     * Creates a handler that will stop a feature as part of a transaction.
     *
     * @param featureName   the name of the feature to stop
     * @param bundleContext context needed for OSGi interaction
     * @return instance of this class
     */
    public static FeatureConfigHandler forStop(String featureName, BundleContext bundleContext) {
        return new FeatureConfigHandler(featureName, false, bundleContext);
    }

    @Override
    public Void commit() throws ConfiguratorException {
        FeaturesService featuresService = getFeaturesService();
        try {
            if (initActivationState != newState) {
                if (newState) {
                    featuresService.installFeature(featureName, EnumSet.of(NoAutoRefreshBundles));
                } else {
                    featuresService.uninstallFeature(featureName);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Error installing/uninstalling feature", e);
            throw new ConfiguratorException("Internal error");
        }

        return null;
    }

    @Override
    public Void rollback() throws ConfiguratorException {
        FeaturesService featuresService = getFeaturesService();
        try {
            if (initActivationState != lookupFeatureStatus(featuresService, featureName)) {
                if (initActivationState) {
                    featuresService.installFeature(featureName, EnumSet.of(NoAutoRefreshBundles));
                } else {
                    featuresService.uninstallFeature(featureName);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Error installing/uninstalling feature", e);
            throw new ConfiguratorException("Internal error");
        }

        return null;
    }

    private FeaturesService getFeaturesService() {
        ServiceReference<FeaturesService> serviceReference = bundleContext.getServiceReference(
                FeaturesService.class);
        return bundleContext.getService(serviceReference);
    }

    private Boolean lookupFeatureStatus(FeaturesService featuresService, String featureName) {
        return featuresService.getState(featureName) == FeatureState.Started;
    }
}
