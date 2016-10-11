package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.Action.ActionMethod.POST;
import static org.codice.ui.admin.security.stage.Stage.DataType.HOSTNAME;
import static org.codice.ui.admin.security.stage.Stage.DataType.PORT;
import static org.codice.ui.admin.security.stage.Stage.DataType.STRING_ENUM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.stage.Action;
import org.codice.ui.admin.security.stage.form.Form;
import org.codice.ui.admin.security.stage.form.Question;
import org.codice.ui.admin.security.stage.Stage;

public class LdapNetworkSettingsStage extends Stage {

    public static final String LDAP_NETWORK_SETTINGS_STAGE_ID = "ldapNetworkSettingsStage";

    public static final String LDAP_HOST_NAME_ID = "hostName";

    public static final String LDAP_PORT_ID = "port";

    public static final String LDAP_ENCRYPTION_METHOD = "ldapEncryptionMethod";

    public static final String[] LDAP_ENCRYPTION_METHODS =
            new String[] {"No encryption", "Use ldaps", "Use startTls"};

    public LdapNetworkSettingsStage(Map<String, String> state, String wizardUrl) {
        super(state, wizardUrl);
    }

    @Override
    public Stage validateFields(Stage ldapNetworkSettingsStage, Map<String, String> params) {
        Form ldapNetworkSettingsForm = ldapNetworkSettingsStage.getForm();
        Question ldapHostNameQ = ldapNetworkSettingsForm.getContent(LDAP_HOST_NAME_ID);
        Question ldapPortQ = ldapNetworkSettingsForm.getContent(LDAP_PORT_ID);
        Question ldapEncryptionMethodQ = ldapNetworkSettingsForm.getContent(
                LDAP_ENCRYPTION_METHOD);

        if (ldapHostNameQ.getValue() == null) {
            ldapHostNameQ.setError("LDAP host name cannot be empty.");
        }

        if (ldapPortQ.getValue() == null) {
            ldapPortQ.setError("LDAP port number cannot be empty.");
        }

        if (ldapEncryptionMethodQ.getValue() == null || !Arrays.asList(LDAP_ENCRYPTION_METHODS)
                .contains(ldapEncryptionMethodQ.getValue()
                        .toString())) {
            ldapEncryptionMethodQ.setError("Invalid encryption method.");
        }

        return ldapNetworkSettingsStage;
    }

    @Override
    public Stage testFields(Stage ldapNetworkSettingsStage, Map<String, String> params) {
        boolean skipConnectionTest = params.get("skip") == null ? false : Boolean.getBoolean(
                params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            //TODO: test ldap connection here
            connectionSuccessful = true;
        }

        if (!connectionSuccessful && !skipConnectionTest) {
            ldapNetworkSettingsStage.clearAction();
            ldapNetworkSettingsStage.addActions(getDefaultActions());
            ldapNetworkSettingsStage.addAction(new Action(POST,
                    getWizardUrl() + "?skip=true",
                    "skip"));
        }

        return ldapNetworkSettingsStage;
    }

    @Override
    public Stage setNewState(Stage currentStage, Map<String, String> params) {
        Map<String, String> newState = currentStage.getState();
        if (newState == null) {
            newState = new HashMap<>();
        }

        newState.put(LDAP_HOST_NAME_ID,
                currentStage.getForm()
                        .getContentValue(LDAP_HOST_NAME_ID).toString());
        newState.put(LDAP_PORT_ID,
                currentStage.getForm()
                        .getContentValue(LDAP_PORT_ID).toString());
        newState.put(LDAP_ENCRYPTION_METHOD,
                currentStage.getForm()
                        .getContentValue(LDAP_ENCRYPTION_METHOD).toString());
        currentStage.setState(newState);
        return currentStage;
    }

    @Override
    public Form getDefaultForm() {
        return Form.builder("LDAP Network Settings")
                .add(Question.builder(LDAP_HOST_NAME_ID, HOSTNAME)
                        .label("LDAP Host name"))
                .add(Question.builder(LDAP_PORT_ID, PORT)
                        .defaults(389, 636)
                        .label("LDAP Port"))
                .add(Question.builder(LDAP_ENCRYPTION_METHOD, STRING_ENUM)
                        .defaults(LDAP_ENCRYPTION_METHODS)
                        .label("Encryption method"));
    }

    @Override
    public List<Action> getDefaultActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(POST, getWizardUrl() + "/" + LDAP_NETWORK_SETTINGS_STAGE_ID, "check"));
        return actions;
    }

    @Override
    public Stage getNewStage(Map<String, String> state, String wizardUrl) {
        return new LdapNetworkSettingsStage(state, wizardUrl);
    }

    @Override
    public String getStageId() {
        return LDAP_NETWORK_SETTINGS_STAGE_ID;
    }
}