package org.codice.ddf.config.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.codice.ddf.config.api.SystemInformationConfiguration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableMap;

public class SystemInformationConfigurationImpl implements SystemInformationConfiguration, ArtifactInstaller {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemInformationConfigurationImpl.class);
  private static final String SYS_INFO_FILE  ="system-information.yaml";
  private static final Path SYS_INFO_FILE_PATH  = Paths.get("config", SYS_INFO_FILE);

  private static final Event SYS_INFO_CHANGE_EVENT = new Event(SystemInformationConfiguration.SYS_INFO_CHANGE_EVENT, ImmutableMap.of());
  private static final String ID  = "system.information.config";
  private static final String VERSION = "1.0";

  private EventAdmin eventAdmin;

  private int httpsPort;
  private int httpPort;
  private String hostname;
  private String siteName;
  private String siteContact;
  private String systemVersion;
  private String organization;
  private Protocol protocol;

  public SystemInformationConfigurationImpl(EventAdmin eventAdmin) {
    this.eventAdmin = eventAdmin;
  }

  public SystemInformationConfigurationImpl() {}

  @Override
  public String getVersion() {
    return VERSION;
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public int getHttpsPort() {
    return httpsPort;
  }

  @Override
  public int getHttpPort() {
    return httpPort;
  }

  @Override
  public Protocol getProtocol() {
    return protocol;
  }

  @Override
  public String getHostname() {
    return hostname;
  }

  @Override
  public String getSiteName() {
    return siteName;
  }

  @Override
  public String getSiteContact() {
    return siteContact;
  }

  @Override
  public String getSystemVersion() {
    return systemVersion;
  }

  @Override
  public String getOrganization() {
    return organization;
  }

  public void setProperties(SystemInformationConfiguration sysInfoConfig) {
    httpsPort = sysInfoConfig.getHttpsPort();
    httpPort = sysInfoConfig.getHttpPort();
    hostname = sysInfoConfig.getHostname();
    siteName = sysInfoConfig.getSiteName();
    siteContact = sysInfoConfig.getSiteContact();
    systemVersion = sysInfoConfig.getSystemVersion();
    organization = sysInfoConfig.getOrganization();
    protocol = sysInfoConfig.getProtocol();
  }

  private void handleConfigChange() {
    pullConfiguration();
    notifyConfigurationChange();
  }

  /**
   * Returns true if a change was made.
   * @return
   */
  private void pullConfiguration() {
    LOGGER.info("Pulling configuration file!");

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      SystemInformationConfigurationImpl sysInfo = mapper.readValue(SYS_INFO_FILE_PATH.toFile(), SystemInformationConfigurationImpl.class);
      setProperties(sysInfo);
      LOGGER.info("Updating sys information config with values: " + toString());
    } catch (IOException e) {
      LOGGER.error("Failed to read " + SYS_INFO_FILE_PATH.toString() + " :", e);
    }
  }

  private void notifyConfigurationChange() {
    eventAdmin.postEvent(SYS_INFO_CHANGE_EVENT);
  }

  @Override
  public void install(File file) {
    LOGGER.debug("Install method called!" + file.getName());
    handleConfigChange();
  }

  @Override
  public void update(File file) {
    LOGGER.debug("Update method called!" + file.getName());
    handleConfigChange();
  }

  @Override
  public void uninstall(File file) {
    LOGGER.debug("Uninstall method called!"+ file.getName());
  }

  @Override
  public boolean canHandle(File file) {
    return file.getName().equals(SYS_INFO_FILE);
  }

  @Override
  public String toString() {
    return "\nhttpsPort:" + httpsPort +
    "\nhttpPort:" + httpPort +
    "\nhostname:" + hostname +
    "\nsiteName:" + siteName +
    "\nsiteContact:" + siteContact +
    "\nsystemVersion:" + systemVersion +
    "\norganization:" + organization +
    "\nprotocol:" + protocol;
  }
}
