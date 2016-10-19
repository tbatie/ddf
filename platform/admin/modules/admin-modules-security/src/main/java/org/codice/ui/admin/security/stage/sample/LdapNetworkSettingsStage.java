package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.Action.ActionMethod.POST;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.BASE_CONTAINER;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.BUTTON;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.HOSTNAME;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.PORT;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.STRING_ENUM;

import java.util.Arrays;
import java.util.Map;

import org.codice.ui.admin.security.stage.StageFinder;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.Action;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.Stage;

public class LdapNetworkSettingsStage extends Stage {

    public static final String LDAP_NETWORK_SETTINGS_STAGE_ID = "ldapNetworkSettingsStage";

    public static final String LDAP_HOST_NAME_ID = "hostName";

    public static final String LDAP_PORT_ID = "port";

    public static final String LDAP_ENCRYPTION_METHOD = "ldapEncryptionMethod";

    public static final String[] LDAP_ENCRYPTION_METHODS =
            new String[] {"No encryption", "Use ldaps", "Use startTls"};

    public LdapNetworkSettingsStage(StageFinder stageFinder) {
        super(stageFinder);
    }
    public LdapNetworkSettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public void registerStage(StageFinder stageFinder) {
        stageFinder.registerStage(getStageId(), LdapNetworkSettingsStage::new);
    }

    @Override
    public Stage validateStage(Stage ldapNetworkSettingsStage, Map<String, String> params) {
        Component ldapHostNameQ =
                ldapNetworkSettingsStage.getComponent(LDAP_HOST_NAME_ID);
        Component ldapPortQ = ldapNetworkSettingsStage.getComponent(LDAP_PORT_ID);
        Component ldapEncryptionMethodQ = ldapNetworkSettingsStage.getComponent(
                LDAP_ENCRYPTION_METHOD);

        if (ldapHostNameQ.getValue() == null) {
            ldapHostNameQ.addError("LDAP host name cannot be empty.");
        }

        if (ldapPortQ.getValue() == null) {
            ldapPortQ.addError("LDAP port number cannot be empty.");
        }

        if (ldapEncryptionMethodQ.getValue() == null || !Arrays.asList(LDAP_ENCRYPTION_METHODS)
                .contains(ldapEncryptionMethodQ.getValue()
                        .toString())) {
            ldapEncryptionMethodQ.addError("Invalid encryption method.");
        }

        return ldapNetworkSettingsStage;
    }

    @Override
    public Stage testStage(Stage ldapNetworkSettingsStage, Map<String, String> params) {
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            //TODO: test ldap connection here
            connectionSuccessful = true;
        }

        if (!connectionSuccessful && !skipConnectionTest) {
            ldapNetworkSettingsStage.getRootComponent()
                    .subComponents(Component.builder(null, BUTTON)
                            .defaults(new Action(POST, getWizardUrl() + "?skip=true", "skip")));
        }

        return ldapNetworkSettingsStage;
    }

    @Override
    public Stage commitStage(Stage currentStage, Map<String, String> params) {
        Configuration newConfiguration = currentStage.getConfiguration();
        if (newConfiguration == null) {
            newConfiguration = new Configuration();
        }

        newConfiguration.addValue(LDAP_HOST_NAME_ID,
                currentStage.getComponent(LDAP_HOST_NAME_ID)
                        .getValue());
        newConfiguration.addValue(LDAP_PORT_ID,
                currentStage.getComponent(LDAP_PORT_ID)
                        .getValue());
        newConfiguration.addValue(LDAP_ENCRYPTION_METHOD,
                currentStage.getComponent(LDAP_ENCRYPTION_METHOD)
                        .getValue());
        currentStage.setConfiguration(newConfiguration);
        return currentStage;
    }

    @Override
    public Component getDefaultRootComponent() {
        Action checkStage = new Action(POST,
                getWizardUrl() + "/" + LDAP_NETWORK_SETTINGS_STAGE_ID,
                "check");

        return Component.builder("LDAP Network Settings", BASE_CONTAINER)
                .subComponents(Component.builder(LDAP_HOST_NAME_ID, HOSTNAME)
                                .title("LDAP Host name"),
                        Component.builder(LDAP_PORT_ID, PORT)
                                .defaults(389, 636)
                                .title("LDAP Port"),
                        Component.builder(LDAP_ENCRYPTION_METHOD,
                                STRING_ENUM)
                                .defaults(LDAP_ENCRYPTION_METHODS)
                                .title("Encryption method"),
                        Component.builder(null, BUTTON)
                                .defaults(checkStage).title("Check"));
    }

    @Override
    public String getStageId() {
        return LDAP_NETWORK_SETTINGS_STAGE_ID;
    }
}