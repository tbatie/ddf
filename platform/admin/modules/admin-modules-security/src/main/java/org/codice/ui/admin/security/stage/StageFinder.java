package org.codice.ui.admin.security.stage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StageFinder implements IStageFinder {

    private Map<String, Function<StageParameters, Stage>> stages;

    public StageFinder() {
        stages = new HashMap<>();
    }

    public Stage getStage(String stageId, StageParameters stageParameters) {
        return stages.get(stageId)
                .apply(stageParameters);
    }

    public void registerStage(String stageId, Function<StageParameters, Stage> newStageFunction) {
        // TODO: tbatie - 10/18/16 - Warn user they may be overwritting a stage
        stages.put(stageId, newStageFunction);
    }
}
