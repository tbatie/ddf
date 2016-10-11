package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.Action.ActionMethod.POST;
import static org.codice.ui.admin.security.stage.Stage.DataType.STRING;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.stage.Action;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.form.Form;
import org.codice.ui.admin.security.stage.form.Question;

public class CustomStage extends Stage {

    public static final String CUSTOM_STAGE_ID = "customStageId";

    public static final String CUSTOM_FIELD_1 = "customField1";

    public static final String CUSTOM_FIELD_2 = "customField2";

    public CustomStage(Map<String, String> state, String wizardUrl) {
        super(state, wizardUrl);
    }

    @Override
    public Stage validateFields(Stage stageToCheck, Map<String, String> params) {
        Question bindUserDNQ = stageToCheck.getForm()
                .getContent(CUSTOM_FIELD_1);
        Question bindUserPassQ = stageToCheck.getForm()
                .getContent(CUSTOM_FIELD_2);

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
        return stageToTest;
    }

    @Override
    public Stage setNewState(Stage currentStage, Map<String, String> params) {
        return currentStage;
    }

    @Override
    public Form getDefaultForm() {
        return Form.builder("Special project field")
                .add(Question.builder(CUSTOM_FIELD_1, STRING)
                        .label("Another special project field"))
                .add(Question.builder(CUSTOM_FIELD_2, STRING)
                        .label("Super specific to this project field"));
    }

    @Override
    public List<Action> getDefaultActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(POST, getWizardUrl() + "/" + CUSTOM_STAGE_ID, "check"));
        return actions;
    }

    @Override
    public String getStageId() {
        return CUSTOM_STAGE_ID;
    }

    @Override
    public Stage getNewStage(Map<String, String> state, String wizardUrl) {
        return new CustomStage(state, wizardUrl);
    }
}
