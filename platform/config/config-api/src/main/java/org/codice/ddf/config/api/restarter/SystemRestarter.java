package org.codice.ddf.config.api.restarter;

public interface SystemRestarter {

  String RESTART_SYSTEM_EVENT = "system_restart";

  /**
   * Will wait for the system to be a in ready state before performing a restart
   */
  void performRestart();
}
