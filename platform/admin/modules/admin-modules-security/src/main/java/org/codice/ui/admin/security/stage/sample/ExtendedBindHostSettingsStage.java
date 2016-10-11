package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.Stage.DataType.STRING;

import java.util.Map;

import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.form.Form;
import org.codice.ui.admin.security.stage.form.Question;

public class ExtendedBindHostSettingsStage extends LdapBindHostSettingsStage {

    public static final String CUSTOM_FIELD = "customField";

    public static final String EXTENDED_LDAP_BIND_HOST_SETTINGS_STAGE_ID = "extendedLdapBindHostSettingsStageId";

    public ExtendedBindHostSettingsStage(Map<String, String> state, String wizardUrl) {
        super(state, wizardUrl);
    }

    @Override
    public Form getDefaultForm() {
        return super.getDefaultForm().add(Question.builder(CUSTOM_FIELD, STRING)
                .label("Extended stage question"));
    }

    @Override
    public String getStageId() {
        return EXTENDED_LDAP_BIND_HOST_SETTINGS_STAGE_ID;
    }

    @Override
    public Stage getNewStage(Map<String, String> state, String wizardUrl) {
        return new ExtendedBindHostSettingsStage(state, wizardUrl);
    }
}
