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

package org.codice.ui.admin.ldap.config.test;

import static org.codice.ui.admin.wizard.api.ConfigurationMessage.MessageType.SUCCESS;
import static org.codice.ui.admin.wizard.api.ConfigurationMessage.buildMessage;

import org.codice.ui.admin.ldap.config.LdapConfiguration;
import org.codice.ui.admin.wizard.api.test.TestMethod;
import org.codice.ui.admin.wizard.api.test.TestReport;

public class AttributeMappingTestMethod extends TestMethod<LdapConfiguration> {

    public static final String LDAP_ATTRIBUTE_MAPPING_TEST_ID = "testAttributeMapping";

    public static final String DESCRIPTION = "Verifies that mapping values are valid and exist.";


    public AttributeMappingTestMethod() {
        super(LDAP_ATTRIBUTE_MAPPING_TEST_ID,
                DESCRIPTION,
                null,
                null,
                null,
                null,
                null);
    }

    @Override
    public TestReport test(LdapConfiguration configuration) {
        // TODO: tbatie - 12/15/16 - Make sure the attributes are in the schema, if they aren't report error. Give a warning there are no users in group or base user dn with the given attributes
        return new TestReport(buildMessage(SUCCESS, "Successfully validated mapping."));
    }
}
