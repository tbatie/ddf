package org.codice.ui.admin.security;

import static org.boon.HTTP.APPLICATION_JSON;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.boon.Boon;
import org.codice.ui.admin.security.stage.Composer;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

import spark.servlet.SparkApplication;

public class LdapWizard implements SparkApplication {

    private Composer composer = getStageComposer();

    public String LDAP_WIZARD_CONTEXT_PATH = "/admin/wizard/ldap";

    @Override
    public void init() {
        get("/:stageId", (req, res) -> {
            return StageProbe.getStage(req.params(":stageId"), new HashMap<>(), getContextPath());
        }, Boon::toJson);

        post("/:stageId", (req, res) -> {
            Stage nextStage = composer.processStage(StageProbe.getStage(req), req.params());
            if (nextStage.containsError()) {
                res.status(HttpStatus.SC_BAD_REQUEST);
            }
            return nextStage;
        }, Boon::toJson);
        after("/*", (req, res) -> res.type(APPLICATION_JSON));
    }

    public Composer getStageComposer() {
        Map<String, String> tree = new HashMap<>();
        tree.put(LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID,
                LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID);
        tree.put(LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID,
                LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID);

        return new Composer(tree, new HashMap<>(), getContextPath());
    }

    public String getContextPath(){
        return LDAP_WIZARD_CONTEXT_PATH;
    }
}


