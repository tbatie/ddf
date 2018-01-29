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

import static org.codice.ddf.features.test.config.KarafManagementProperties.KARAF_MGMT_CFG_FILE_PATH;
import static org.codice.ddf.features.test.config.KarafManagementProperties.RMI_REGISTRY_PORT_PROPERTY;
import static org.codice.ddf.features.test.config.KarafManagementProperties.RMI_SERVER_PORT_PROPERTY;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

public class PortOptions {

  public static Option defaultPortsOptions() {
    // TODO: tbatie - 1/28/18 - These should be using PortFinder
    return new DefaultCompositeOption(
        ddfHome("${karaf.home}"),
        httpsPort("9993"),
        httpPort("9994"),
        ftpPort("9995"),
        rmiRegistryPort("20001"),
        rmiServerPort("20002"),
        debugPortOption("5005", Boolean.getBoolean("isDebugEnabled")));
  }

  public static Option ddfHome(String path) {
    return KarafDistributionOption.editConfigurationFilePut(
        SystemProperties.SYSTEM_PROPERTIES_FILE_PATH, SystemProperties.DDF_HOME_PROPERTY, path);
  }

  public static Option httpsPort(String port) {
    return KarafDistributionOption.editConfigurationFilePut(
        SystemProperties.SYSTEM_PROPERTIES_FILE_PATH, SystemProperties.HTTPS_PORT_PROPERTY, port);
  }

  public static Option httpPort(String port) {
    return KarafDistributionOption.editConfigurationFilePut(
        SystemProperties.SYSTEM_PROPERTIES_FILE_PATH, SystemProperties.HTTP_PORT_PROPERTY, port);
  }

  public static Option ftpPort(String port) {
    return KarafDistributionOption.editConfigurationFilePut(
        SystemProperties.SYSTEM_PROPERTIES_FILE_PATH, SystemProperties.FTP_PORT_PROPERTY, port);
  }

  public static Option rmiRegistryPort(String port) {
    return KarafDistributionOption.editConfigurationFilePut(
        KARAF_MGMT_CFG_FILE_PATH, RMI_REGISTRY_PORT_PROPERTY, port);
  }

  public static Option rmiServerPort(String port) {
    return KarafDistributionOption.editConfigurationFilePut(
        KARAF_MGMT_CFG_FILE_PATH, RMI_SERVER_PORT_PROPERTY, port);
  }

  public static Option debugPortOption(String port, boolean waitForDebug) {
    return KarafDistributionOption.debugConfiguration(port, waitForDebug);
  }
}
