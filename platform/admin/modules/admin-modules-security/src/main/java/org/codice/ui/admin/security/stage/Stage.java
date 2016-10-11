package org.codice.ui.admin.security.stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.stage.form.Form;

/**
 * Created by tbatie123 on 10/10/16.
 */
public abstract class Stage {

    public static final String NEXT_STAGE_ID = "nextStageId";

    private Form form;

    private Map<String, String> state;

    private List<Action> actions;

    private String wizardUrl;

    public Stage(Map<String, String> state, String wizardUrl) {
        this.state = state;
        this.wizardUrl = wizardUrl;
        this.form = getDefaultForm();
        this.actions =  getDefaultActions();
    }

    public abstract Stage validateFields(Stage stageToCheck, Map<String, String> params);

    public abstract Stage testFields(Stage stageToTest, Map<String, String> params);

    public abstract Stage setNewState(Stage currentStage, Map<String, String> params);

    public abstract Form getDefaultForm();

    public abstract List<Action> getDefaultActions();

    public abstract String getStageId();

    public abstract Stage getNewStage(Map<String, String> state, String wizardUrl);

    public Map<String, String> getState() {
        return state;
    }

    public void setState(Map<String, String> state) {
        this.state = state;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getWizardUrl() {
        return wizardUrl;
    }

    public void clearAction() {
        actions.clear();
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addActions(List<Action> action) {
        actions.addAll(action);
    }

    public boolean containsError() {
        return form.containsErrors();
    }

    public void clearErrors(){
        form.clearErrors();
    }

    public enum DataType {
        URL, PORT, HOSTNAME, STRING_ENUM, STRING, PASSWORD
    }
}
