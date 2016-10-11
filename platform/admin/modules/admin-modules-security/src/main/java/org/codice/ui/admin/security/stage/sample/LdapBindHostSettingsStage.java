package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.Action.ActionMethod.POST;
import static org.codice.ui.admin.security.stage.Stage.DataType.PASSWORD;
import static org.codice.ui.admin.security.stage.Stage.DataType.STRING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.stage.Action;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.form.Form;
import org.codice.ui.admin.security.stage.form.Question;

public class LdapBindHostSettingsStage extends Stage {

    public static final String LDAP_BIND_HOST_SETTINGS_STAGE_ID = "ldapBindHostSettingsStageId";

    public static final String BIND_USER_DN = "bindUserDN";

    public static final String BIND_USER_PASS = "bindUserPassword";

    public LdapBindHostSettingsStage(Map<String, String> state, String wizardUrl) {
        super(state, wizardUrl);
    }

    @Override
    public Form getDefaultForm() {
        return Form.builder("LDAP Bind User Settings")
                .add(Question.builder(BIND_USER_DN, STRING)
                        .label("LDAP Bind User DN"))
                .add(Question.builder(BIND_USER_PASS, PASSWORD)
                        .label("LDAP Bind User Password"));
    }


    @Override
    public List<Action> getDefaultActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(POST, getWizardUrl() + "/" + getStageId(), "check"));
        return actions;
    }

    @Override
    public Stage validateFields(Stage stageToCheck, Map<String, String> params) {
        Question bindUserDNQ = stageToCheck.getForm()
                .getContent(BIND_USER_DN);
        Question bindUserPassQ = stageToCheck.getForm()
                .getContent(BIND_USER_PASS);

        if (bindUserDNQ.getValue() == null) {
            bindUserDNQ.setError("Invalid bind user DN");
        }

        if (bindUserPassQ.getValue() == null) {
            bindUserPassQ.setError("Invalid password entry");
        }

        return stageToCheck;
    }

    @Override
    public Stage testFields(Stage stageToTest, Map<String, String> params) {
        //Test ldap connection
        boolean skipConnectionTest = params.get("skip") == null ? false : Boolean.getBoolean(
                params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            //// TODO: tbatie - 10/5/16 - Test bind user connection
            connectionSuccessful = true;
        }

        if (!connectionSuccessful && !skipConnectionTest) {
            stageToTest.clearAction();
            stageToTest.addActions(getDefaultActions());
            stageToTest.addAction(new Action(POST,
                    getWizardUrl() + "?skip=true",
                    "skip"));
        }

        return stageToTest;
    }

    @Override
    public Stage setNewState(Stage currentStage, Map<String, String> params) {
        Map<String, String> newState = currentStage.getState();
        if (newState == null) {
            newState = new HashMap<>();
        }
        newState.put(BIND_USER_DN,
                currentStage.getForm()
                        .getContentValue(BIND_USER_DN).toString());
        newState.put(BIND_USER_PASS,
                currentStage.getForm()
                        .getContentValue(BIND_USER_PASS).toString());
        currentStage.setState(newState);
        return currentStage;
    }

    @Override
    public String getStageId() {
        return LDAP_BIND_HOST_SETTINGS_STAGE_ID;
    }

    @Override
    public Stage getNewStage(Map<String, String> state, String wizardUrl) {
        return new LdapBindHostSettingsStage(state, wizardUrl);
    }
}