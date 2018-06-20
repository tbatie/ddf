package org.codice.ddf.config.mapper;

import org.apache.karaf.system.SystemService;
import org.codice.ddf.config.api.SystemInformationConfiguration;
import org.codice.ddf.config.api.restarter.SystemRestarter;
import org.codice.ddf.config.mapper.api.ConfigurationMapper;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

public class SystemInformationConfigurationMapper implements ConfigurationMapper, EventHandler {

  public static final String ID  = "system.information.mapper";
  private static final Logger LOGGER = LoggerFactory.getLogger(SystemInformationConfigurationMapper.class);

  private static final Event SYS_RESTART_EVENT = new Event(SystemRestarter.RESTART_SYSTEM_EVENT, ImmutableMap.of());
  private EventAdmin eventAdmin;
  private SystemService systemService;
  private SystemInformationConfiguration sysPropsConfig;

  private State state;

  public SystemInformationConfigurationMapper(EventAdmin eventAdmin, SystemInformationConfiguration sysPropsConfig, SystemService systemService) {
    this.eventAdmin = eventAdmin;
    this.state = State.FINISHED;
    this.sysPropsConfig = sysPropsConfig;
    this.systemService = systemService;
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public void handleEvent(Event event) {
    LOGGER.info("Received event to update system information. Updating system.properties file and notifying for a restart.");
    writeSystemProperties();
    notifyRestartRequired();
  }

  private void writeSystemProperties() {
    this.state = State.WRITING;
    systemService.setSystemProperty("org.codice.ddf.system.httpPort", "" + sysPropsConfig.getHttpPort(),true);
    systemService.setSystemProperty("org.codice.ddf.system.httpsPort", "" + sysPropsConfig.getHttpsPort(),true);
    systemService.setSystemProperty("org.codice.ddf.system.hostname", sysPropsConfig.getHostname(),true);
    systemService.setSystemProperty("org.codice.ddf.system.protocol", sysPropsConfig.getProtocol() == SystemInformationConfiguration.Protocol.HTTPS ? "https://" : "http://",true);
    systemService.setSystemProperty("org.codice.ddf.system.siteName", sysPropsConfig.getSiteName(),true);
    systemService.setSystemProperty("org.codice.ddf.system.siteContact", sysPropsConfig.getSiteContact(),true);
    systemService.setSystemProperty("org.codice.ddf.system.version", sysPropsConfig.getSystemVersion(),true);
    systemService.setSystemProperty("org.codice.ddf.system.organization", sysPropsConfig.getOrganization(),true);
    this.state = State.FINISHED;
  }

  private void notifyRestartRequired() {
    eventAdmin.postEvent(SYS_RESTART_EVENT);
  }
}
