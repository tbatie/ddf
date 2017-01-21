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
package org.codice.ddf.admin.security.ldap.probe;

import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.BIND_KDC;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.BIND_METHOD;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.BIND_REALM;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.BIND_USER_DN;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.BIND_USER_PASSWORD;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.ENCRYPTION_METHOD;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.HOST_NAME;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.LDAP_TYPE;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.PORT;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.QUERY;
import static org.codice.ddf.admin.api.config.ldap.LdapConfiguration.QUERY_BASE;
import static org.codice.ddf.admin.security.ldap.test.LdapTestingCommons.bindUserToLdapConnection;
import static org.codice.ddf.admin.security.ldap.test.LdapTestingCommons.getLdapQueryResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codice.ddf.admin.api.config.ldap.LdapConfiguration;
import org.codice.ddf.admin.api.handler.method.ProbeMethod;
import org.codice.ddf.admin.api.handler.report.ProbeReport;
import org.forgerock.opendj.ldap.Attribute;
import org.forgerock.opendj.ldap.Connection;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;

import com.google.common.collect.ImmutableList;

public class LdapQueryProbe extends ProbeMethod<LdapConfiguration> {


    public static final String LDAP_QUERY_ID = "query";
    private static final String DESCRIPTION = "Probe to execute arbitrary query against an LDAP server and return the results.";
    public static final int MAX_QUERY_RESULTS = 50;

    private static final List<String> REQUIRED_FIELDS = ImmutableList.of(
            LDAP_TYPE,
            HOST_NAME,
            PORT,
            ENCRYPTION_METHOD,
            BIND_USER_DN,
            BIND_USER_PASSWORD,
            BIND_METHOD,
            QUERY,
            QUERY_BASE);

    private static final List<String> OPTIONAL_FIELDS = ImmutableList.of(
            BIND_REALM,
            BIND_KDC);

    private static final String LDAP_QUERY_RESULTS = "ldapQueryResults";

    private static final List<String> RETURN_TYPES = ImmutableList.of(LDAP_QUERY_RESULTS);

    // TODO: tbatie - 1/19/17 - Set success types and failure types
    public LdapQueryProbe() {
        super(LDAP_QUERY_ID, DESCRIPTION, REQUIRED_FIELDS, OPTIONAL_FIELDS, null, null, null, RETURN_TYPES);
    }

    @Override
    public ProbeReport probe(LdapConfiguration configuration) {
        ProbeReport probeReport = new ProbeReport(configuration.validate(REQUIRED_FIELDS));

        if (probeReport.containsFailureMessages()) {
            return probeReport;
        }

        Connection connection = bindUserToLdapConnection(configuration).connection();
        List<SearchResultEntry> searchResults = getLdapQueryResults(connection,
                configuration.query(),
                configuration.queryBase(), MAX_QUERY_RESULTS);
        List<Map<String, String>> convertedSearchResults = new ArrayList<>();

        for (SearchResultEntry entry : searchResults) {
            Map<String, String> entryMap = new HashMap<>();
            for (Attribute attri : entry.getAllAttributes()) {
                entryMap.put("name",
                        entry.getName()
                                .toString());
                if (!attri.getAttributeDescriptionAsString().toLowerCase().contains("password")) {
                    entryMap.put(attri.getAttributeDescriptionAsString(), attri.firstValueAsString());
                }
            }
            convertedSearchResults.add(entryMap);
        }

        return probeReport.probeResult(LDAP_QUERY_RESULTS,
                convertedSearchResults);
    }
}
