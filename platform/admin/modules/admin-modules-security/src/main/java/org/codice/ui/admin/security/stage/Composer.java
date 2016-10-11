package org.codice.ui.admin.security.stage;

import static org.codice.ui.admin.security.stage.Stage.NEXT_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.CustomStage.CUSTOM_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.SetupUserStoreStage.SETUP_USER_STORE_STAGE_ID;

import java.util.HashMap;
import java.util.Map;

import org.codice.ui.admin.security.StageProbe;
import org.codice.ui.admin.security.stage.sample.CustomStage;
import org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage;
import org.codice.ui.admin.security.stage.sample.SetupUserStoreStage;
import org.springframework.util.StringUtils;

public class Composer {

    private String wizardUrl;

    Map<String, String> stageLinks = new HashMap<>();

    //Stage to link, key condition name, key condition value, Stage to link to
    Map<String, Map<String, Map<String, String>>> conditionalStageLinks = new HashMap<>();


    public Composer(Map<String, String> stageLinks,
            Map<String, Map<String, Map<String, String>>> conditionalStageLinks, String wizardUrl) {

        this.stageLinks = stageLinks;
        this.conditionalStageLinks = conditionalStageLinks;
        this.wizardUrl = wizardUrl;
        // TODO: tbatie - 10/10/16 - Change to a single map of <String, Function<Map<String, String>, String>>
        // the key on the map is the current stage id
        // the Function takes the current state and returns the next stage id
        // Simple case function:
        // Function<Map<String, String>, String> f = (m) -> { return "nextStageId"};
        // More complex, where there is branching logic at a given stage:
        // Function<Map<String, String>, String> f = (m) -> { if (m.hasKey("foobar") {
        //         return "nextStage1";
        //     }
        //     if (m.get("bar").equals("doit") && m.get("baz").equals("really")) {
        //         return "nextStage2";;
        //     }
        // };

        /**
         * link(stage).to(destinationStage)
         * onCondition(stateKey, stateValue).and().onCondition(stateKey, stateValue).link(stage).to(destionation)
         * onCondition(Predicate<Stage></>).to(desintationStage)
         */
    }

    public Stage processStage(Stage stageToProcess, Map<String, String> params) {

        stageToProcess.clearErrors();

        Stage validatedStage = stageToProcess.validateFields(stageToProcess, params);
        if (validatedStage.containsError()) {
            return validatedStage;
        }

        Stage testedStage = stageToProcess.testFields(stageToProcess, params);
        if (testedStage.containsError()) {
            return testedStage;
        }

        Stage stageWithNewState = stageToProcess.setNewState(stageToProcess, params);
        return lookUpNextStage(stageWithNewState, params);
    }

    public Stage lookUpNextStage(Stage previousStage, Map<String, String> params) {
        String linkedStageId = null;

        if(previousStage.getState().containsKey(NEXT_STAGE_ID)) {
            linkedStageId = previousStage.getState().get(NEXT_STAGE_ID);
        }
        if (StringUtils.isEmpty(linkedStageId)) {
            linkedStageId = stageLinks.get(previousStage.getStageId());
        }

        if (StringUtils.isEmpty(linkedStageId)) {
            linkedStageId = lookupConditionalNextStage(previousStage.getState(), previousStage.getStageId());
        }

        return StageProbe.getStage(linkedStageId, previousStage.getState(), wizardUrl);
    }


    public String lookupConditionalNextStage(Map<String, String> state,
            String previousStageId) {

        //condition key, condition value, next stageId
        Map<String, Map<String, String>> conditionalLinksToStageId = conditionalStageLinks.get(
                previousStageId);

        for (Map.Entry<String, String> stateEntry : state.entrySet()) {
            if (conditionalLinksToStageId.containsKey(stateEntry.getKey())) {
                Map<String, String> conditionToNextStage = conditionalLinksToStageId.get(
                        stateEntry.getKey());
                if (conditionToNextStage.get(stateEntry.getValue()) != null) {
                    return conditionToNextStage.get(stateEntry.getValue());
                }
            }
        }

        return null;
    }

    public void setStageLink(String origin, String destination){
        stageLinks.put(origin, destination);
    }

    public void setWizardUrl(String wizardUrl) {
        this.wizardUrl = wizardUrl;
    }
}
