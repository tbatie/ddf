package org.codice.ddf.config.api;

public interface SystemInformationConfiguration extends DdfConfiguration {

  String SYS_INFO_CHANGE_EVENT = "system_info_config_change";

  int getHttpsPort();

  int getHttpPort();

  Protocol getProtocol();

  String getHostname();

  String getSiteName();

  String getSiteContact();

  String getSystemVersion();

  String getOrganization();

  enum Protocol {
    HTTP, HTTPS
  }
}
