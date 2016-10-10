package org.codice.ui.admin.security;

import static org.boon.HTTP.APPLICATION_JSON;
import static org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.security.stage.sample.SetupUserStoreStage.SETUP_USER_STORE_STAGE_ID;

import java.util.HashMap;
import java.util.Map;

import org.boon.Boon;
import org.codice.ui.admin.security.stage.Composer;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.sample.LdapBindHostSettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapDirectorySettingsStage;
import org.codice.ui.admin.security.stage.sample.LdapNetworkSettingsStage;
import org.codice.ui.admin.security.stage.sample.SetupUserStoreStage;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

import com.google.common.collect.ImmutableMap;

import spark.Request;
import spark.servlet.SparkApplication;

public class LdapWizard implements SparkApplication {
    ///

    Map<String, Stage> strToClass = ImmutableMap.of(LDAP_DIRECTORY_SETTINGS_STAGE_ID,
            new LdapDirectorySettingsStage(null, null),
            LDAP_BIND_HOST_SETTINGS_STAGE_ID,
            new LdapBindHostSettingsStage(null, null),
            LDAP_NETWORK_SETTINGS_STAGE_ID,
            new LdapNetworkSettingsStage(null, null));

    public static final String BIND_USER_DN = "bindUserDN";

    public static final String BIND_USER_PASS = "bindUserPassword";

    public static final String BASE_USER_DN = "baseUserDN";

    public static final String BASE_USERNAME_ATTRIBUTE = "baseUserNameAttribute";

    public static final String BASE_GROUP_DN = "baseGroupDN";

    public static final String LDAP_WIZARD_CONTEXT_PATH = "/admin/wizard/ldap";

    @Override
    public void init() {

        Map<String, String> tree = new HashMap<>();
        tree.put(LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID,
                LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID);
        tree.put(LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID,
                LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID);

        Composer composer = new Composer(tree, new HashMap<>(), LDAP_WIZARD_CONTEXT_PATH);
        get("/*", (req, res) -> getNewStage(req), Boon::toJson);
        post("/:stageId", (req, res) -> composer.processStage(getStage(req), req.params()), Boon::toJson);
        after("/*", (req, res) -> res.type(APPLICATION_JSON));
    }

    public Stage getStage(Request req) {
        Stage stageClass = strToClass.get(req.params(":stageId"));
        return Boon.fromJson(req.body(), stageClass.getClass());
    }

    public Stage getNewStage(Request req) {
        String stageRequest = req.url();
        if (stageRequest.contains(LDAP_DIRECTORY_SETTINGS_STAGE_ID)) {
            return new LdapDirectorySettingsStage(new HashMap<>(), LDAP_WIZARD_CONTEXT_PATH);
        } else if (stageRequest.contains(LDAP_NETWORK_SETTINGS_STAGE_ID)) {
            return new LdapNetworkSettingsStage(new HashMap<>(), LDAP_WIZARD_CONTEXT_PATH);

        } else if (stageRequest.contains(LDAP_BIND_HOST_SETTINGS_STAGE_ID)) {
            return new LdapBindHostSettingsStage(new HashMap<>(), LDAP_WIZARD_CONTEXT_PATH);

        } else if (stageRequest.contains(SETUP_USER_STORE_STAGE_ID)) {
            return new SetupUserStoreStage(new HashMap<>(), LDAP_WIZARD_CONTEXT_PATH);
        }
        return null;
    }
}


