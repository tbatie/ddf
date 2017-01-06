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
 */
package org.codice.ui.admin.sources.config.test;

import static org.codice.ui.admin.ldap.config.test.LdapTestingCommons.cannotBeNullFields;
import static org.codice.ui.admin.sources.config.SourceConfigurationHandlerImpl.MANUAL_URL_TEST_ID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.codice.ui.admin.sources.config.SourceUtils;
import org.codice.ui.admin.sources.config.opensearch.OpenSearchSourceConfiguration;
import org.codice.ui.admin.sources.config.opensearch.OpenSearchSourceUtils;
import org.codice.ui.admin.wizard.api.ConfigurationMessage;
import org.codice.ui.admin.wizard.api.test.TestMethod;
import org.codice.ui.admin.wizard.api.test.TestReport;

import com.google.common.collect.ImmutableMap;

public class OpenSearchUrlTestMethod extends TestMethod<OpenSearchSourceConfiguration> {

    public static final String OPENSEARCH_URL_TEST_ID = MANUAL_URL_TEST_ID;

    public static final String DESCRIPTION = "Attempts to verify a given URL is an OpenSearch endpoint.";

    public static final String TEST_URL = "testUrl";

    public static final Map<String, String> REQUIRED_FIELDS = ImmutableMap.of(TEST_URL,
            "The URL to attempt to verify as an OpenSearch Endpoint.");

    private static final String VERIFIED_URL = "urlVerified";

    private static final String CANNOT_CONNECT = "cannotConnect";

    private static final String CANNOT_VERIFY = "cannotVerify";

    public static final Map<String, String> SUCCESS_TYPES = ImmutableMap.of(VERIFIED_URL,
            "URL has been verified as an OpenSearch endpoint.");

    public static final Map<String, String> FAILURE_TYPES = ImmutableMap.of(CANNOT_CONNECT,
            "Could not reach specified URL.");

    public static final Map<String, String> WARNING_TYPES = ImmutableMap.of(CANNOT_VERIFY,
            "Reached URL, but could not verify as an OpenSearch endpoint.");

    public OpenSearchUrlTestMethod() {
        super(OPENSEARCH_URL_TEST_ID,
                DESCRIPTION,
                REQUIRED_FIELDS,
                null,
                SUCCESS_TYPES,
                FAILURE_TYPES,
                WARNING_TYPES);
    }

    @Override
    public TestReport test(OpenSearchSourceConfiguration configuration) {
        Map<String, Object> requiredFields = new HashMap<>();
        requiredFields.put(TEST_URL, configuration.endpointUrl());
        TestReport cannotBeNullFieldsTest = cannotBeNullFields(requiredFields);
        if (cannotBeNullFieldsTest.containsUnsuccessfulMessages()) {
            return cannotBeNullFieldsTest;
        }
        Optional<ConfigurationMessage> message = SourceUtils.endpointIsReachable(configuration);
        if (message.isPresent()) {
            return new TestReport(message.get());
        }
        return OpenSearchSourceUtils.discoverUrlCapabilities(configuration);
    }
}
