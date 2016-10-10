package org.codice.ui.admin.security.stage.form;

public interface FormContent<T> {
    String getId();

    String getLabel();

    boolean hasErrors();

    String getError();

     T getValue();

    void setError(String errorMessage);
}
