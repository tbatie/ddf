package org.codice.ddf.features.test.config;

import static org.ops4j.pax.exam.CoreOptions.when;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

public class DebugOptions {

  public static Option keepRuntimeFolder() {
    return when(Boolean.getBoolean("keepRuntimeFolder")).useOptions(org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder());
  }

  public static Option enableRemoteDebugging() {
    // TODO: tbatie - 3/7/18 - Change this to a dynamic port
    String debugPort = System.getProperty("debugPort", "5006");
    return KarafDistributionOption.debugConfiguration(debugPort, Boolean.getBoolean("waitForDebug"));
  }

  public static Option defaultDebuggingOptions() {
    return new DefaultCompositeOption(keepRuntimeFolder(), enableRemoteDebugging());
  }
}
