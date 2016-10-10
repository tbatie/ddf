package org.codice.ui.admin.security.stage.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codice.ui.admin.security.stage.Stage.DataType;
import org.springframework.util.StringUtils;

public class Question<T> {

    private String label;

    private List<T> defaults;

    private T value;

    private String description;

    private DataType type;

    private String error;

    private String id;

    private Question(String id, DataType type) {
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

    public String getLabel() {
        return label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean hasErrors() {
        return !StringUtils.isEmpty(getError());
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

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
