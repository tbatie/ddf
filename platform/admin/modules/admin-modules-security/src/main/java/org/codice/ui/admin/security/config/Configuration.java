package org.codice.ui.admin.security.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private Map<String, Object> values;

    public Configuration(){
        values = new HashMap<>();
    }

    public void addValue(String key, Object value) {
        values.put(key, value);
    }
}
