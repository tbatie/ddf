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
package org.codice.ddf.test.common.options;

import static org.codice.ddf.test.common.options.TestResourcesOptions.getTestResource;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Options for configuring log levels during test execution
 */
public class LoggingOptions extends BasicOptions implements BundleActivator {

  private static final String DISPLAY_TEST_CONFIG_KEY = "display.test.config";

  private static final String LOGGER_CONFIGURATION_FILE_PATH = "etc/org.ops4j.pax.logging.cfg";

  private static final String DEFAULT_DDF_PKG = "ddf";

  private static final String DEFAULT_CODICE_PKG = "org.codice";

  private static final String DEFAULT_LOG_LEVEL = "INFO";

  public static Option defaultLogging() {
    return new DefaultCompositeOption(ddfLoggingLevel(DEFAULT_LOG_LEVEL),
            codiceLoggingLevel(DEFAULT_LOG_LEVEL));
  }

  public static Option ddfLoggingLevel(String logLevel) {
    recordConfiguration("ddf packages log level: %s=%s", DEFAULT_DDF_PKG, logLevel);
    return logLevelOption(DEFAULT_DDF_PKG, logLevel);
  }

  public static Option codiceLoggingLevel(String logLevel) {
    recordConfiguration("codice packages log level: %s=%s", DEFAULT_CODICE_PKG, logLevel);
    return logLevelOption(DEFAULT_CODICE_PKG, logLevel);
  }

  /**
   * Creates options to add log configuration lines to the etc/org.ops4j.pax.logging.cfg file. See
   * {@see org.apache.karaf.log.core.internal.LogServiceLog4j2Impl}.
   *
   * @param name  name of the logger to set
   * @param level String value to set the logger level
   * @return options to set the log level
   */
  public static Option logLevelOption(String name, String level) {
    final String loggerPrefix = "log4j2.logger.";
    final String loggerKey = name.replace('.', '_')
            .toLowerCase();
    return new DefaultCompositeOption(editConfigurationFilePut(LOGGER_CONFIGURATION_FILE_PATH,
            String.format("%s%s.name", loggerPrefix, loggerKey),
            name),
            editConfigurationFilePut(LOGGER_CONFIGURATION_FILE_PATH,
                    String.format("%s%s.level", loggerPrefix, loggerKey),
                    level));
  }

  //Prints test configuration on startup
  // Uses system property to start the since this bundle is often refreshed from installing features causing the configs to be printed out frequently.
  @Override
  public void start(BundleContext bundleContext) throws Exception {
    String configLogFilePath = getTestResource("/" + TEST_CONFIGURATION_FILE_NAME);

    if (Paths.get(configLogFilePath)
            .toFile()
            .exists() && displayTestConfig()) {
      try (Stream<String> lines = Files.lines(Paths.get(configLogFilePath))) {
        System.out.println("\n########## TEST EXAM CONFIGURATIONS ##########\n");
        lines.forEach(System.out::println);
        System.out.println("\n#############################################\n");
      } catch (Exception e) {
        System.out.println("Failed to print test configuration file");
      }

      setDisplayTestConfig(false);
    }
  }

  private boolean displayTestConfig() {
    return Boolean.valueOf(System.getProperty(DISPLAY_TEST_CONFIG_KEY, "true"));
  }

  private void setDisplayTestConfig(boolean displayTestConfig) {
    System.setProperty(DISPLAY_TEST_CONFIG_KEY, Boolean.toString(displayTestConfig));
  }

  @Override
  public void stop(BundleContext bundleContext) throws Exception {

  }
}
