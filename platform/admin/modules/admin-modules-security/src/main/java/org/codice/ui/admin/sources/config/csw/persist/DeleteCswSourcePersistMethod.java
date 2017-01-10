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
package org.codice.ui.admin.sources.config.csw.persist;

import static org.codice.ui.admin.sources.config.SourceConfigurationHandler.DELETE;
import static org.codice.ui.admin.wizard.api.ConfigurationMessage.MessageType.FAILURE;
import static org.codice.ui.admin.wizard.api.ConfigurationMessage.MessageType.SUCCESS;
import static org.codice.ui.admin.wizard.api.ConfigurationMessage.buildMessage;

import java.util.Map;

import org.codice.ui.admin.sources.config.csw.CswSourceConfiguration;
import org.codice.ui.admin.wizard.api.persist.PersistMethod;
import org.codice.ui.admin.wizard.api.test.TestReport;
import org.codice.ui.admin.wizard.config.ConfigReport;
import org.codice.ui.admin.wizard.config.Configurator;

import com.google.common.collect.ImmutableMap;

public class DeleteCswSourcePersistMethod extends PersistMethod<CswSourceConfiguration> {

    public static final String DELETE_CSW_SOURCE_ID = DELETE;

    public static final String DESCRIPTION =
            "Attempts to delete a CSW source with the given configuration.";

    public static final String SERVICE_PID = "servicePid";

    private static final String SOURCE_DELETED = "sourceDeleted";

    private static final String DELETE_FAILED = "deleteFailed";

    private static final Map<String, String> REQUIRED_FIELDS = ImmutableMap.of(SERVICE_PID,
            "The unique pid of the service to be deleted.");

    private static final Map<String, String> SUCCESS_TYPES = ImmutableMap.of(SOURCE_DELETED,
            "The CSW Source was successfully deleted.");

    private static final Map<String, String> FAILURE_TYPES = ImmutableMap.of(DELETE_FAILED,
            "Failed to delete CSW source.");

    public DeleteCswSourcePersistMethod() {
        super(DELETE_CSW_SOURCE_ID,
                DESCRIPTION,
                REQUIRED_FIELDS,
                null,
                SUCCESS_TYPES,
                FAILURE_TYPES,
                null);
    }

    @Override
    public TestReport persist(CswSourceConfiguration configuration) {
        Configurator configurator = new Configurator();
        ConfigReport report;
        // TODO: tbatie - 12/20/16 - Passed in factory pid and commit totally said it passed, should have based servicePid
        configurator.deleteManagedService(configuration.servicePid());
        report = configurator.commit();
        return report.containsFailedResults() ? new TestReport(buildMessage(FAILURE,
                "Failed to delete CSW Source")) : new TestReport(buildMessage(SUCCESS,
                "CSW Source deleted"));
    }
}
