package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.LdapWizard.BASE_GROUP_DN;
import static org.codice.ui.admin.security.LdapWizard.BASE_USERNAME_ATTRIBUTE;
import static org.codice.ui.admin.security.LdapWizard.BASE_USER_DN;
import static org.codice.ui.admin.security.stage.Action.ActionMethod.POST;
import static org.codice.ui.admin.security.stage.Stage.DataType.STRING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.stage.Action;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.form.Form;
import org.codice.ui.admin.security.stage.form.Question;

public class LdapDirectorySettingsStage extends Stage {

    public static final String LDAP_DIRECTORY_SETTINGS_STAGE_ID = "ldapDirectorySetttingStage";

    public LdapDirectorySettingsStage(Map<String, String> state, String wizardUrl) {
        super(state, wizardUrl);
    }

    @Override
    public Stage validateFields(Stage stageToCheck, Map<String, String> params) {
        Form ldapDirSettingsForm = stageToCheck.getForm();
        Question baseUserDnQ = (Question) ldapDirSettingsForm.getContent(BASE_USER_DN);
        Question baseGroupDnQ = (Question) ldapDirSettingsForm.getContent(BASE_GROUP_DN);
        Question baseUsernameAttriQ = (Question) ldapDirSettingsForm.getContent(
                BASE_USERNAME_ATTRIBUTE);

        if (baseUserDnQ.getValue() == null) {
            ldapDirSettingsForm.getContent(BASE_USER_DN)
                    .setError("The specified user DN does not exist.");
        }

        if (baseGroupDnQ.getValue() == null) {
            baseGroupDnQ.setError("The specified group DN does not exist.");
        }

        if (baseUsernameAttriQ.getValue() == null) {
            baseUsernameAttriQ.setError(
                    "The specified username attribute does not appear to exist.");
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
            //test baserUserDN, baseGroupDN, baseUsernameAttri
            connectionSuccessful = true;
        }

        if (!connectionSuccessful && !skipConnectionTest) {
            stageToTest.clearAction();
            stageToTest.addActions(getDefaultActions());
            stageToTest.addAction(new Action(POST, getWizardUrl() + "?skip=true", "check"));
        }

        return stageToTest;
    }

    @Override
    public Stage setNewState(Stage currentStage, Map<String, String> params) {
        Map<String, String> newState = currentStage.getState();
        if (newState == null) {
            newState = new HashMap<>();
        }
        newState.put(BASE_USER_DN,
                currentStage.getForm()
                        .getContentValue(BASE_USER_DN).toString());
        newState.put(BASE_GROUP_DN,
                currentStage.getForm()
                        .getContentValue(BASE_GROUP_DN).toString());
        newState.put(BASE_USERNAME_ATTRIBUTE,
                currentStage.getForm()
                        .getContentValue(BASE_USERNAME_ATTRIBUTE).toString());
        currentStage.setState(newState);
        return currentStage;
    }

    @Override
    public Form getDefaultForm() {
        return Form.builder("LDAP Directory Settings")
                .add(Question.builder(BASE_USER_DN, STRING)
                        .label("Base User DN"))
                .add(Question.builder(BASE_GROUP_DN, STRING)
                        .label("Group User DN"))
                .add(Question.builder(BASE_USERNAME_ATTRIBUTE, STRING)
                        .label("User name attribute"));
    }

    @Override
    public List<Action> getDefaultActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(POST, getWizardUrl() + "/" + LDAP_DIRECTORY_SETTINGS_STAGE_ID, "check"));
        return actions;
    }

    @Override
    public String getStageId() {
        return LDAP_DIRECTORY_SETTINGS_STAGE_ID;
    }
}