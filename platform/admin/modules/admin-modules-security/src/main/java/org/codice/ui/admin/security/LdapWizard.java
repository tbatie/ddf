package org.codice.ui.admin.security;

import static org.codice.ui.admin.security.LdapWizard.ActionMethod.POST;
import static org.codice.ui.admin.security.LdapWizard.DataType.HOSTNAME;
import static org.codice.ui.admin.security.LdapWizard.DataType.PASSWORD;
import static org.codice.ui.admin.security.LdapWizard.DataType.PORT;
import static org.codice.ui.admin.security.LdapWizard.DataType.STRING;
import static org.codice.ui.admin.security.LdapWizard.DataType.STRING_ENUM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.boon.Boon;
import org.boon.json.JsonParser;
import org.boon.json.implementation.JsonParserCharArray;
import org.springframework.util.StringUtils;

import static spark.Spark.after;
import static spark.Spark.get;
import static org.boon.HTTP.APPLICATION_JSON;
import static spark.Spark.post;

import spark.servlet.SparkApplication;
import spark.Request;

public class LdapWizard implements SparkApplication {
    public static final String LDAP_HOST_NAME_ID = "hostName";

    public static final String LDAP_PORT_ID = "port";

    public static final String LDAP_ENCRYPTION_METHOD = "ldapEncryptionMethod";

    public static final String[] LDAP_ENCRYPTION_METHODS =
            new String[] {"No encryption", "Use ldaps", "Use startTls"};
    ///

    public static final String BIND_USER_DN = "bindUserDN";

    public static final String BIND_USER_PASS = "bindUserPassword";

    ///

    public static final String BASE_USER_DN = "baseUserDN";

    public static final String BASE_USERNAME_ATTRIBUTE = "baseUserNameAttribute";

    public static final String BASE_GROUP_DN = "baseGroupDN";

    Map<String, Stage> stages = new HashMap<>();
    @Override
    public void init() {

//        for (Map.Entry<String, Stage> stage : stages.entrySet()) {
//            get(stage.getKey(), (req, res) -> stage.getValue(), Boon::toJson);
//            post(stage.getKey(), (req, res) -> stage.getValue().procces(req), Boon::toJson);
//        }


        get("/network", (req, res) -> getLdapNetworkSettingsStage(getStateFromRequest(req)), Boon::toJson);
        post("/network", (req, res) -> validateLdapNetworkSettings(req), Boon::toJson);

        get("/bind", (req, res) -> getLdapBindUserSettingsStage(getStateFromRequest(req)), Boon::toJson);
        post("/bind", (req, res) -> validateLdapBindUserSettings(req), Boon::toJson);

        get("/dir", (req, res) -> getLdapDirectorySettingsStage(getStateFromRequest(req)), Boon::toJson);
        post("/dir", (req, res) -> validateLdapDirectorySettingsStage(req), Boon::toJson);

        after("/*", (req, res) -> res.type(APPLICATION_JSON));

    }


    public Map<String, Object> getStateFromRequest(Request req) {
        Map<String, Object> state = new HashMap<>();

        try {
            Stage stage = Boon.fromJson(req.body(), Stage.class);
            if(stage != null) {
                state = stage.getState();
            }
        } catch (Exception e) {

        }

        return state;
    }

    public Action defaultNetworkSettingsAction() {
        return new Action(POST,
                "/security/wizard/network",
                "check");
    }

    public Action defaultBindSettingsAction() {
        return new Action(POST,
                "/security/wizard/bind",
                "check");
    }

    public Action defaultLdapDirectorySettings() {
        return new Action(POST,
                "/security/wizard/dir",
                "check");
    }

    public Stage getLdapNetworkSettingsStage(Map<String, Object> state) {
        Form ldapNetworkSettingsForm = Form.builder("LDAP Network Settings")
                .add(Question.builder(LDAP_HOST_NAME_ID, HOSTNAME)
                        .label("LDAP Host name"))
                .add(Question.builder(LDAP_PORT_ID, PORT)
                        .defaults(389, 636)
                        .label("LDAP Port"))
                .add(Question.builder(LDAP_ENCRYPTION_METHOD, STRING_ENUM)
                        .defaults(LDAP_ENCRYPTION_METHODS)
                        .label("Encryption method"));

        return new Stage(ldapNetworkSettingsForm, state, defaultNetworkSettingsAction());
    }

    public Stage getLdapBindUserSettingsStage(Map<String, Object> state) {
        Form ldapBindUserForm = Form.builder("LDAP Bind User Settings")
                .add(Question.builder(BIND_USER_DN, STRING)
                        .label("LDAP Bind User DN"))
                .add(Question.builder(BIND_USER_PASS, PASSWORD)
                        .label("LDAP Bind User Password"));

        return new Stage(ldapBindUserForm, state, defaultBindSettingsAction());
    }

    public Stage getLdapDirectorySettingsStage(Map<String, Object> state) {
        Form ldapDirSettingsForm = Form.builder("LDAP Directory Settings").add(Question.builder(BASE_USER_DN, STRING).label("Base User DN"))
                                    .add(Question.builder(BASE_GROUP_DN, STRING).label("Group User DN"))
                                    .add(Question.builder(BASE_USERNAME_ATTRIBUTE, STRING).label("User name attribute"));

        return new Stage(ldapDirSettingsForm, state, defaultLdapDirectorySettings());
    }

    public Stage validateLdapNetworkSettings(Request req) {
        Stage ldapNetworkSettingsStage = Boon.fromJson(req.body(), Stage.class);
        Map<String, String> params = req.params();
        Form ldapNetworkSettingsForm = ldapNetworkSettingsStage.getForm();
        Question ldapHostNameQ = ldapNetworkSettingsForm.getQuestion(LDAP_HOST_NAME_ID);
        Question ldapPortQ = ldapNetworkSettingsForm.getQuestion(LDAP_PORT_ID);
        Question ldapEncryptionMethodQ =
                ldapNetworkSettingsForm.getQuestion(LDAP_ENCRYPTION_METHOD);

        String ldapHostName = null;
        String ldapPort = null;
        String ldapEncryptionMethod = null;

        //Validate input
        if (ldapHostNameQ.getValue() == null) {
            ldapHostNameQ.setErrorMsg("LDAP host name cannot be empty.");
        } else {
            ldapHostName = ldapHostNameQ.getValue().toString();
        }

        if (ldapPortQ.getValue() == null) {
            ldapPortQ.setErrorMsg("LDAP port number cannot be empty.");
        } else {
            ldapPort = ldapPortQ.getValue().toString();
        }

        if (ldapEncryptionMethodQ.getValue() == null || !Arrays.asList(LDAP_ENCRYPTION_METHODS)
                .contains(ldapEncryptionMethodQ.getValue().toString())) {
            ldapEncryptionMethodQ.setErrorMsg("Invalid encryption method.");
        } else {
            ldapEncryptionMethod = ldapEncryptionMethodQ.getValue().toString();
        }

        if (StringUtils.isEmpty(ldapHostName) || StringUtils.isEmpty(ldapPort)
                || StringUtils.isEmpty(ldapEncryptionMethod)) {
            ldapNetworkSettingsStage.setForm(ldapNetworkSettingsForm);
            return ldapNetworkSettingsStage;
        }

        //Test ldap connection
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            connectionSuccessful = testLdapConnection(ldapHostName, ldapPort, ldapEncryptionMethod);
        }

        if (connectionSuccessful || skipConnectionTest) {
            Map<String, Object> newState = ldapNetworkSettingsStage.getState();
            if(newState == null) {
                newState = new HashMap<>();
            }

            newState.put(LDAP_HOST_NAME_ID, ldapHostName);
            newState.put(LDAP_PORT_ID, ldapPort);
            newState.put(LDAP_ENCRYPTION_METHOD, ldapEncryptionMethod);
            return getLdapBindUserSettingsStage(newState);
        } else {
            ldapNetworkSettingsStage.clearAction();
            ldapNetworkSettingsStage.addAction(defaultNetworkSettingsAction());
            ldapNetworkSettingsStage.addAction(new Action(POST,
                    "/security/wizard/network?skip=true",
                    "skip"));
            return ldapNetworkSettingsStage;
        }
    }

    public Stage validateLdapBindUserSettings(Request req) {
        Stage bindUserStage = Boon.fromJson(req.body(), Stage.class);
        Map<String, String> params = req.params();
        Form bindUserForm = bindUserStage.getForm();

        Question bindUserDNQ = bindUserForm.getQuestion(BIND_USER_DN);
        Question bindUserPassQ = bindUserForm.getQuestion(BIND_USER_PASS);

        String bindUserDN = null;
        String bindUserPassword = null;

        if(bindUserDNQ.getValue() == null) {
            bindUserDNQ.setErrorMsg("Invalid bind user DN");
        } else {
            bindUserDN = bindUserDNQ.getValue().toString();
        }

        if(bindUserPassQ.getValue() == null) {
            bindUserPassQ.setErrorMsg("Invalid password entry");
        } else {
            bindUserPassword = bindUserPassQ.getValue().toString();
        }

        if (StringUtils.isEmpty(bindUserDN) || StringUtils.isEmpty(bindUserPassword)) {
            bindUserStage.setForm(bindUserForm);
            return bindUserStage;
        }

        //Test ldap connection
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            connectionSuccessful = testBindUserConnection(bindUserStage.getState(), bindUserDN, bindUserPassword);
        }

        if (connectionSuccessful || skipConnectionTest) {
            Map<String, Object> newState = bindUserStage.getState();
            if(newState == null) {
                newState = new HashMap<>();
            }
            newState.put(BIND_USER_DN, bindUserDN);
            newState.put(BIND_USER_PASS, bindUserPassword);
            return getLdapDirectorySettingsStage(newState);
        } else {
            bindUserStage.clearAction();
            bindUserStage.addAction(defaultBindSettingsAction());
            bindUserStage.addAction(new Action(POST,
                    "/security/wizard/bind?skip=true",
                    "skip"));
            return bindUserStage;
        }
    }

    public Stage validateLdapDirectorySettingsStage(Request req) {
        Stage ldapDirSettingsStage = Boon.fromJson(req.body(), Stage.class);
        Map<String, String> params = req.params();
        Form ldapDirSettingsForm = ldapDirSettingsStage.getForm();
        Question baseUserDnQ = ldapDirSettingsForm.getQuestion(BASE_USER_DN);
        Question baseGroupDnQ = ldapDirSettingsForm.getQuestion(BASE_GROUP_DN);
        Question baseUsernameAttriQ =
                ldapDirSettingsForm.getQuestion(BASE_USERNAME_ATTRIBUTE);

        String baseUserDn = null;
        String baseGroupDn = null;
        String baseUsernameAttri = null;


        if(baseUserDnQ.getValue() == null) {
            ldapDirSettingsForm.getQuestion(BASE_USER_DN).setErrorMsg("The specified user DN does not exist.");
        } else {
            baseUserDn = baseUserDnQ.getValue().toString();
        }

        if(baseGroupDnQ.getValue() == null) {
            baseGroupDnQ.setErrorMsg("The specified group DN does not exist.");
        } else {
            baseGroupDn = baseGroupDnQ.getValue().toString();
        }

        if(baseUsernameAttriQ.getValue() == null) {
            baseUsernameAttriQ.setErrorMsg("The specified username attribute does not appear to exist.");
        } else {
            baseUsernameAttri = baseUsernameAttriQ.getValue().toString();
        }


        if (StringUtils.isEmpty(baseUserDn) || StringUtils.isEmpty(baseGroupDn) || StringUtils.isEmpty(baseUsernameAttri)) {
            ldapDirSettingsStage.setForm(ldapDirSettingsForm);
            return ldapDirSettingsStage;
        }

        //Test ldap connection
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            //test baserUserDN, baseGroupDN, baseUsernameAttri
            connectionSuccessful = true;

        }

        if (connectionSuccessful || skipConnectionTest) {
            Map<String, Object> newState = ldapDirSettingsStage.getState();
            if(newState == null) {
                newState = new HashMap<>();
            }
            newState.put(BASE_USER_DN, baseUserDn);
            newState.put(BASE_GROUP_DN, baseGroupDn);
            newState.put(BASE_USERNAME_ATTRIBUTE, baseUsernameAttri);
            return new Stage(Form.builder("You guessed it!"), newState, null);
        } else {
            ldapDirSettingsStage.clearAction();
            ldapDirSettingsStage.addAction(defaultLdapDirectorySettings());
            ldapDirSettingsStage.addAction(new Action(POST,
                    "/wizard/security/dir?skip=true",
                    "check"));
            return ldapDirSettingsStage;
        }
    }

    public boolean testLdapConnection(String ldapHostname, String ldapPort,
            String ldapEncryptionMethod) {
        return true;
    }

    public boolean testBindUserConnection(Map<String, Object> state, String bindUserDN, String bindUserPass) {
        return true;
    }

    public boolean testBaseUserDN(Map<String, Object> state, String baseUserDN) {
        return true;
    }


    public enum DataType {
        URL, PORT, HOSTNAME, STRING_ENUM, STRING, PASSWORD, SUBMIT
    }

    public enum ActionMethod {
        POST, GET
    }

    public static class Action {
        public ActionMethod method;

        public String url;

        public String label;

        public Action(ActionMethod method, String url, String label) {
            this.method = method;
            this.url = url;
            this.label = label;
        }
    }

    public static class BindHostSettingsStage extends Stage {

        @Override
        public Stage validateFields(Stage stageToCheck, Map<String, String> params) {
            return null;
        }

        @Override
        public Stage testFields(Stage stageToTest, Map<String, String> params) {
            return null;
        }

        @Override
        public Stage setNewState(Stage currentStage, Map<String, String> params) {
            return null;
        }

        @Override
        public Stage getNextStage(Stage prevStage, Map<String, String> params) {
            return null;
        }
    }

    public static class LdapNetworkSettingsStage extends Stage {
        
        private Form defaultForm = Form.builder("LDAP Network Settings")
                .add(Question.builder(LDAP_HOST_NAME_ID, HOSTNAME).label("LDAP Host name"))
                .add(Question.builder(LDAP_PORT_ID, PORT).defaults(389, 636).label("LDAP Port"))
                .add(Question.builder(LDAP_ENCRYPTION_METHOD, STRING_ENUM).defaults(LDAP_ENCRYPTION_METHODS).label("Encryption method"));

        private Action defaultAction = new Action(POST,
                "/security/wizard/network",
                "check");

        public LdapNetworkSettingsStage() {
            setStage(defaultForm, new HashMap<>(), defaultAction);
        }

        public LdapNetworkSettingsStage(Map<String, Object> state) {
            setStage(defaultForm, state, defaultAction);
        }

        @Override
        public Stage validateFields(Stage ldapNetworkSettingsStage, Map<String, String> params) {
            Form ldapNetworkSettingsForm = ldapNetworkSettingsStage.getForm();
            Question ldapHostNameQ = ldapNetworkSettingsForm.getQuestion(LDAP_HOST_NAME_ID);
            Question ldapPortQ = ldapNetworkSettingsForm.getQuestion(LDAP_PORT_ID);
            Question ldapEncryptionMethodQ =
                    ldapNetworkSettingsForm.getQuestion(LDAP_ENCRYPTION_METHOD);

            String ldapHostName = null;
            String ldapPort = null;
            String ldapEncryptionMethod = null;

            //Validate input
            if (ldapHostNameQ.getValue() == null) {
                ldapHostNameQ.setErrorMsg("LDAP host name cannot be empty.");
            } else {
                ldapHostName = ldapHostNameQ.getValue().toString();
            }

            if (ldapPortQ.getValue() == null) {
                ldapPortQ.setErrorMsg("LDAP port number cannot be empty.");
            } else {
                ldapPort = ldapPortQ.getValue().toString();
            }

            if (ldapEncryptionMethodQ.getValue() == null || !Arrays.asList(LDAP_ENCRYPTION_METHODS)
                    .contains(ldapEncryptionMethodQ.getValue().toString())) {
                ldapEncryptionMethodQ.setErrorMsg("Invalid encryption method.");
            } else {
                ldapEncryptionMethod = ldapEncryptionMethodQ.getValue().toString();
            }

            if (StringUtils.isEmpty(ldapHostName) || StringUtils.isEmpty(ldapPort)
                    || StringUtils.isEmpty(ldapEncryptionMethod)) {
                ldapNetworkSettingsStage.setForm(ldapNetworkSettingsForm);
            }

            return ldapNetworkSettingsStage;
        }

        public Stage testFields(Stage ldapNetworkSettingsStage, Map<String, String> params) {
            boolean skipConnectionTest =
                    params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
            boolean connectionSuccessful = false;
            if (!skipConnectionTest) {
                //TODO: test ldap connection here
                connectionSuccessful = true;
            }

            if (!connectionSuccessful && !skipConnectionTest) {
                ldapNetworkSettingsStage.clearAction();
                ldapNetworkSettingsStage.addAction(defaultAction);
                ldapNetworkSettingsStage.addAction(new Action(POST,
                        "/security/wizard/network?skip=true",
                        "skip"));
            }

            return ldapNetworkSettingsStage;
        }

        public Stage setNewState(Stage currentStage, Map<String, String> params) {
            Map<String, Object> newState = currentStage.getState();
            if(newState == null) {
                newState = new HashMap<>();
            }

            newState.put(LDAP_HOST_NAME_ID, currentStage.getForm().getQuestionValue(LDAP_HOST_NAME_ID));
            newState.put(LDAP_PORT_ID, currentStage.getForm().getQuestionValue(LDAP_PORT_ID));
            newState.put(LDAP_ENCRYPTION_METHOD, currentStage.getForm().getQuestionValue(LDAP_ENCRYPTION_METHOD));
            currentStage.setState(newState);
            return currentStage;
        }

        public Stage getNextStage(Stage prevStage, Map<String, String> params) {
            return getLdapBindUserSettingsStage(newState);
        }
    }

    public static class Question<T> {
        private String label;

        private List<T> defaults;

        private T value;

        private String description;

        private DataType type;

        private String errorMsg;

        private String id;

        public Question(String id, DataType type) {
            this.id = id;
            this.type = type;
            this.defaults = new ArrayList<>();
        }

        public static Question builder(String id, DataType type) {
            return new Question(id, type);
        }

        public Question label(String label) {
            this.label = label;
            return this;
        }

        public String getId() {
            return id;
        }

        public T getValue() {
            return value;
        }

        public Question defaults(T... defaults) {
            this.defaults.addAll(Arrays.asList(defaults));
            return this;
        }

        public List<T> getDefaults() {
            return defaults;
        }

        public Question value(T value) {
            this.value = value;
            return this;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    public static class Form {
        private String title;

        private List<Question> questions;

        public Form(String title) {
            this.questions = new ArrayList<>();
            this.title = title;
        }

        public static Form builder(String title) {
            return new Form(title);
        }

        public Question getQuestion(String questionId) {
            for (Question question : questions) {
                if (question.getId()
                        .equals(questionId)) {
                    return question;
                }
            }
            return null;
        }

        public Object getQuestionValue(String questionId) {
            for (Question question : questions) {
                if (question.getId()
                        .equals(questionId)) {
                    return question.getValue();
                }
            }

            return null;
        }

        public Form add(Question q) {
            questions.add(q);
            return this;
        }

        public boolean containsErrorMsgs(){
            for(Question q : questions) {
                if(!StringUtils.isEmpty(q.errorMsg)) {
                    return true;
                }
            }
            return false;
        }
    }



    public static abstract class Stage {
        private Form form;

        private Map<String, Object> state;

        private List<Action> actions;

        public Stage() {
            state = new HashMap<>();
            actions = new ArrayList<>();
        }

        public abstract Stage validateFields(Stage stageToCheck, Map<String, String> params);

        public abstract Stage testFields(Stage stageToTest, Map<String, String> params);

        public abstract Stage setNewState(Stage currentStage, Map<String, String> params);

        public abstract Stage getNextStage(Stage prevStage, Map<String, String> params);

        public Stage procces(Request req) {
            Stage currentStage = Boon.fromJson(req.body(), Stage.class);
            Map<String, String> params = req.params();

            Stage validatedStage = validateFields(currentStage, params);
            if (validatedStage.getForm()
                    .containsErrorMsgs()) {
                return validatedStage;
            }

            Stage testedStage = testFields(currentStage, params);
            if (testedStage.getForm()
                    .containsErrorMsgs()) {
                return testedStage;
            }

            return getNextStage(setNewState(currentStage, params), params);
        }

        public void setStage(Form form, Map<String, Object> state, Action action,
                Action... otherActions) {
            this.form = form;
            this.state = state;
            this.actions = new ArrayList<>();
            actions.add(action);
            actions.addAll(Arrays.asList(otherActions));
        }

        public Map<String, Object> getState() {
            return state;
        }

        public void setState(Map<String, Object> state) {
            this.state = state;
        }

        public Form getForm() {
            return form;
        }

        public void setForm(Form form) {
            this.form = form;
        }

        public void clearAction() {
            actions.clear();
        }

        public void addAction(Action action) {
            actions.add(action);
        }
    }
}
