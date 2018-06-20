package org.codice.ddf.config.restarter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.codice.ddf.config.api.restarter.SystemRestarter;

import org.apache.karaf.system.SystemService;
import org.codice.ddf.config.mapper.api.ConfigurationMapper;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemRestarterImpl implements SystemRestarter, EventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemRestarterImpl.class);
  private static final long MAX_WAIT_SYSTEM_READY = TimeUnit.MINUTES.convert(1, TimeUnit.MILLISECONDS);

  private SystemService systemService;
  private List<ConfigurationMapper> configMappers;

  public SystemRestarterImpl(SystemService systemService, List<ConfigurationMapper> configMappers) {
    this.systemService = systemService;
    this.configMappers = configMappers;
  }

  @Override
  public void performRestart() {
    try {
      waitForSystemReadyForReboot();
      systemService.reboot();
    } catch (Exception e) {
      LOGGER.debug("Failed to reboot system.", e);
      LOGGER.error("Failed to reboot system. Please manually restart system.", e);
    }
  }

  public void waitForSystemReadyForReboot() throws Exception {
    long maxWait = System.currentTimeMillis() + MAX_WAIT_SYSTEM_READY;

    while(true) {
      boolean sysReady = configMappers.stream().allMatch(writer -> writer.getState() == ConfigurationMapper.State.FINISHED);

      if(sysReady) {
        return;
      } else {
        String unfinishedWriters = configMappers
                .stream()
                .filter(writer -> writer.getState() == ConfigurationMapper.State.WRITING)
                .map(ConfigurationMapper::getId)
                .collect(Collectors.joining(","));
        LOGGER.info("Waiting on config mappers to finish before system restart: " + unfinishedWriters);
      }

      if(System.currentTimeMillis() > maxWait) {
        throw new Exception("Failed system restart waiting for config mappers to finish.");
      }
    }
  }

  @Override
  public void handleEvent(Event event) {
    LOGGER.info("Received event to perform a system restart.");
    // TODO: tbatie - 6/19/18 - Only trigger events that require a system restart.
//    performRestart();
  }
}
