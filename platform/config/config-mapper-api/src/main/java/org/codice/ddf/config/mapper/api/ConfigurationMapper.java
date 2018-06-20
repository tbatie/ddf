package org.codice.ddf.config.mapper.api;

public interface ConfigurationMapper {

  String getId();

  State getState();

  enum State {
    WRITING, FINISHED
  }
}
