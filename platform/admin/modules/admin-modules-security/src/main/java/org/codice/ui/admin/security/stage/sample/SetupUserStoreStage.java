package org.codice.ui.admin.security.stage.sample;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.stage.Action;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.form.Form;

public class SetupUserStoreStage extends Stage {

    public static final String SETUP_USER_STORE_STAGE_ID = "setupUserStoreStageId";

    public static final String SETUP_LDAP = "setupLdap";

    public static final String SETUP_IDP = "setupLdap";

    public static List<String> USER_STORE_SELECTIONS = Arrays.asList(SETUP_LDAP, SETUP_IDP);

    public SetupUserStoreStage(Map<String, String> state, String wizardUrl) {
        super(state, wizardUrl);
    }

    @Override
    public Stage validateFields(Stage stageToCheck, Map<String, String> params) {
        if (stageToCheck.getForm()
                .getContentValue(SETUP_LDAP) == null && stageToCheck.getForm()
                .getContentValue(SETUP_IDP) == null) {
        }

        return stageToCheck;
    }

    @Override
    public Stage testFields(Stage stageToTest, Map<String, String> params) {
        return stageToTest;
    }

    @Override
    public Stage setNewState(Stage currentStage, Map<String, String> params) {
        String nextStage;

        if (currentStage.getForm()
                .getContentValue(SETUP_LDAP) != null) {
            nextStage = currentStage.getForm()
                    .getContentValue(SETUP_LDAP)
                    .toString();
        } else {
            nextStage = currentStage.getForm()
                    .getContentValue(SETUP_IDP)
                    .toString();
        }

        Map<String, String> newState = currentStage.getState();
        newState.put("nextState", nextStage);
        currentStage.setState(newState);
        return currentStage;
    }

    @Override
    public Form getDefaultForm() {
        return null;
    }

    @Override
    public List<Action> getDefaultActions() {
        return null;
    }

    @Override
    public Stage getNewStage(Map<String, String> state, String wizardUrl) {
        return new SetupUserStoreStage(state, wizardUrl);
    }

    @Override
    public String getStageId() {
        return SETUP_USER_STORE_STAGE_ID;
    }
}