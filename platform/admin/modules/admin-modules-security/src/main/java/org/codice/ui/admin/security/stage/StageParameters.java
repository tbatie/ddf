package org.codice.ui.admin.security.stage;

import java.util.HashMap;
import java.util.Map;

import org.codice.ui.admin.security.config.Configuration;

public class StageParameters {

    private String wizardUrl;
    private Map<String, String> state;
    private Configuration configuration;

    public StageParameters(String wizardUrl) {
        this.wizardUrl = wizardUrl;
        state = new HashMap<>();
        configuration = new Configuration();
    }

    public StageParameters(String wizardUrl, Map<String, String> state, Configuration configuration) {
        this.wizardUrl = wizardUrl;
        this.state = state;
        this.configuration = configuration;
    }

    public String getWizardUrl() {
        return wizardUrl;
    }

    public Map<String, String> getState() {
        return state;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
