package org.codice.ui.admin.security;

import static org.boon.HTTP.APPLICATION_JSON;
import static org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID;

import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.StageComposer;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageFinder;
import org.codice.ui.admin.security.stage.StageParameters;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.Request;
import spark.servlet.SparkApplication;

public class LdapWizard implements SparkApplication, Wizard {

    public static final String LDAP_WIZARD_CONTEXT_PATH = "/admin/wizard/ldap";

    private StageComposer composer;

    private StageFinder stageFinder;

    public LdapWizard(StageFinder stageFinder) {
        this.stageFinder = stageFinder;
        this.composer = getStageComposer(LDAP_WIZARD_CONTEXT_PATH, stageFinder);
    }

    @Override
    public String getTitle() {
        return "LDAP Wizard";
    }

    @Override
    public String getDescription() {
        return "Help setup that thing called LDAP!";
    }

    @Override
    public String getContextPath() {
        return LDAP_WIZARD_CONTEXT_PATH;
    }

    @Override
    public String initialStageId() {
        return LDAP_NETWORK_SETTINGS_STAGE_ID;
    }

    @Override
    public void init() {
        get("/:stageId", (req, res) -> {
            return stageFinder.getStage(req.params(":stageId"),
                    new StageParameters(getContextPath()));
        }, new Gson()::toJson);

        post("/:stageId", (req, res) -> {
            Stage nextStage = composer.processStage(getStageFromRequest(req), req.params());
            if (nextStage.containsError()) {
                res.status(HttpStatus.SC_BAD_REQUEST);
            }
            return nextStage;
        }, new Gson()::toJson);

        after("/*", (req, res) -> res.type(APPLICATION_JSON));
    }

    public StageComposer getStageComposer(String contextPath, StageFinder stageFinder) {
        return StageComposer.builder(contextPath, stageFinder)
                .link(LDAP_NETWORK_SETTINGS_STAGE_ID, LDAP_BIND_HOST_SETTINGS_STAGE_ID)
                .link(LDAP_BIND_HOST_SETTINGS_STAGE_ID, LDAP_DIRECTORY_SETTINGS_STAGE_ID);
    }

    public Stage getStageFromRequest(Request req) {
        // TODO: tbatie - 10/19/16 - Clean this up, this is a really ugly way to get the class associated to the stageId
        Stage stageFoundById = stageFinder.getStage(req.params(":stageId"),
                new StageParameters(getContextPath()));

        return getGsonParser().fromJson(req.body(), stageFoundById.getClass());
    }

    public Gson getGsonParser() {
//        // TODO: tbatie - 10/18/16 - This is for different types of components in a list. Any other types extending component will need to perform a switch on the ComponentType field to specificy the object to be converted to
        // TODO: tbatie - 10/18/16uses gson-extras, see if it's okay to use. We should probably be copying the RuntimeTypeAdapterFactory into ddf instead
// This is
//        RuntimeTypeAdapterFactory rtaf = RuntimeTypeAdapterFactory.of(Component.class,
//                "componentType")
//                .registerSubtype(InputComponent.class, InputComponent.INPUT_TYPE)
//                .registerSubtype(Component.class, Component.BASE_TYPE);
//        return new GsonBuilder().registerTypeAdapterFactory(rtaf)
//                .create();
        return new Gson();
    }
}
