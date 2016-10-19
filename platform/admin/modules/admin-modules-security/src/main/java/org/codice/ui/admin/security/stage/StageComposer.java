package org.codice.ui.admin.security.stage;

import static org.codice.ui.admin.security.stage.Stage.NEXT_STAGE_ID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.util.StringUtils;

public class StageComposer {

    private String wizardUrl;

    StageFinder stageFinder;

    private Map<String, Function<Stage, String>> stageLinks = new HashMap<>();

    public StageComposer(String wizardUrl, StageFinder stageFinder) {
        this.wizardUrl = wizardUrl;
        this.stageFinder = stageFinder;
        stageLinks = new HashMap<>();
    }

    /**
     * Validates fields of stage. If there are no errors from validation, then the fields are tested.
     * If there are no errors resulting from testing, then the state and configuration are persisted then the next stage is looked up
     * @param stageToProcess - Stage to be validated, tested and used as a look up for the
     * @param params - Additional parameters used during validation and testing from the request
     * @return Stage with errors that must be addressed or the next corresponding stage
     */
    public Stage processStage(Stage stageToProcess, Map<String, String> params) {
        stageToProcess.clearErrors();

        Stage validatedStage = stageToProcess.validateStage(stageToProcess, params);
        if (validatedStage.containsError()) {
            return validatedStage;
        }

        Stage testedStage = stageToProcess.testStage(stageToProcess, params);
        if (testedStage.containsError()) {
            return testedStage;
        }

        Stage stageWithNewState = stageToProcess.commitStage(stageToProcess, params);
        return persistAndlookUpNextStage(stageWithNewState, params);
    }

    /**
     *
     * @param previousStage
     * @param params
     * @return
     */
    public Stage persistAndlookUpNextStage(Stage previousStage, Map<String, String> params) {
        String linkedStageId = null;

        if (previousStage.getState() != null && previousStage.getState()
                .containsKey(NEXT_STAGE_ID)) {
            linkedStageId = previousStage.getState()
                    .get(NEXT_STAGE_ID);
        }

        if (StringUtils.isEmpty(linkedStageId)) {
            linkedStageId = stageLinks.get(previousStage.getStageId())
                    .apply(previousStage);
        }

        return stageFinder.getStage(linkedStageId,
                new StageParameters(wizardUrl,
                        previousStage.getState(),
                        previousStage.getConfiguration()));
    }

    public static StageComposer builder(String wizardUrl, StageFinder stageFinder) {
        return new StageComposer(wizardUrl, stageFinder);
    }

    public StageComposer link(String origin, String destination) {
        stageLinks.put(origin, (m) -> destination);
        return this;
    }

    public StageComposer link(String origin, Function<Stage, String> nextStageCondition) {
        stageLinks.put(origin, nextStageCondition);
        return this;
    }
}
