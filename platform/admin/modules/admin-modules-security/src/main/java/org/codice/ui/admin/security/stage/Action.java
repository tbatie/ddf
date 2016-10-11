package org.codice.ui.admin.security.stage;

public class Action {
    public ActionMethod method;

    public String url;

    public String label;

    public Action(ActionMethod method, String url, String label) {
        this.method = method;
        this.url = url;
        this.label = label;
    }

    public enum ActionMethod {
        POST, GET
    }
}