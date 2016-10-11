package org.codice.ui.admin.security;

import java.util.List;
import java.util.Map;

import org.boon.Boon;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.sample.CustomStage;
import org.codice.ui.admin.security.stage.sample.ExtendedBindHostSettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage;
import org.codice.ui.admin.security.stage.sample.SetupUserStoreStage;

import com.google.common.collect.ImmutableList;

import spark.Request;

public class StageProbe {

    static List<Stage> stages = ImmutableList.of(new CustomStage(null, null),
            new LdapBindHostSettingsStage(null, null),
            new LdapDirectorySettingsStage(null, null),
            new LdapNetworkSettingsStage(null, null),
            new SetupUserStoreStage(null, null),
            new ExtendedBindHostSettingsStage(null, null));

    public static Stage getStage(String stageId, Map<String, String> state, String wizardUrl) {
        for(Stage stage : stages) {
            if(stage.getStageId().equals(stageId)) {
                return stage.getNewStage(state, wizardUrl);
            }
        }
        return null;
    }



    public static Class<? extends Stage> getStageClass(String stageId) {
        for(Stage stage : stages) {
            if(stage.getStageId().equals(stageId)) {
                return stage.getClass();
            }
        }
        return null;
    }

    public static Stage getStage(Request req) {
        return Boon.fromJson(req.body(), StageProbe.getStageClass(req.params(":stageId")));
    }
}
