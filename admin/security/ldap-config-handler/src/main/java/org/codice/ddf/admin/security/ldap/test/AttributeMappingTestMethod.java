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

package org.codice.ddf.admin.security.ldap.test;

import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.ATTRIBUTE_MAPPINGS;
import static org.codice.ddf.admin.api.handler.ConfigurationMessage.INVALID_FIELD;
import static org.codice.ddf.admin.api.handler.ConfigurationMessage.buildMessage;
import static org.codice.ddf.admin.api.handler.ConfigurationMessage.createInvalidFieldMsg;
import static org.codice.ddf.admin.api.handler.commons.HandlerCommons.SUCCESSFUL_TEST;
import static org.codice.ddf.admin.api.services.PolicyManagerServiceProperties.STS_CLAIMS_CONFIGURATION_CONFIG_ID;
import static org.codice.ddf.admin.api.services.PolicyManagerServiceProperties.STS_CLAIMS_PROPS_KEY_CLAIMS;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.codice.ddf.admin.api.config.ldap.LdapConfiguration;
import org.codice.ddf.admin.api.configurator.Configurator;
import org.codice.ddf.admin.api.handler.method.TestMethod;
import org.codice.ddf.admin.api.handler.report.Report;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class AttributeMappingTestMethod extends TestMethod<LdapConfiguration> {

    public static final String LDAP_ATTRIBUTE_MAPPING_TEST_ID = "attribute-mapping";

    public static final String DESCRIPTION =
            "Verifies that sts mapping values are valid and exist.";

    public static final List<String> REQUIRED_FIELDS = ImmutableList.of(ATTRIBUTE_MAPPINGS);

    public static final Map<String, String> SUCCESS_TYPES = ImmutableMap.of(SUCCESSFUL_TEST,
            "Attribute mapping was successfully validated.");

    public static final Map<String, String> FAILURE_TYPES = ImmutableMap.of(INVALID_FIELD, "The given attribute mapping is invalid.");

    public AttributeMappingTestMethod() {
        super(LDAP_ATTRIBUTE_MAPPING_TEST_ID,
                DESCRIPTION,
                REQUIRED_FIELDS,
                null,
                SUCCESS_TYPES,
                null,
                null);
    }

    @Override
    public Report test(LdapConfiguration configuration) {
        List stsClaims = Arrays.asList((String[]) new Configurator().getConfig(
                STS_CLAIMS_CONFIGURATION_CONFIG_ID)
                .get(STS_CLAIMS_PROPS_KEY_CLAIMS));
        Optional<String> unknownStsClaim = configuration.attributeMappings()
                .keySet()
                .stream()
                .filter(claim -> !stsClaims.contains(claim))
                .findFirst();

        if (unknownStsClaim.isPresent()) {
            return new Report(createInvalidFieldMsg(String.format(
                    "Unknown STS claim [%s], the STS properties are not set to handle this claim.",
                    unknownStsClaim.get()), ATTRIBUTE_MAPPINGS));
        }

        return new Report(buildMessage(SUCCESS_TYPES, FAILURE_TYPES, null, SUCCESSFUL_TEST));
    }

}
