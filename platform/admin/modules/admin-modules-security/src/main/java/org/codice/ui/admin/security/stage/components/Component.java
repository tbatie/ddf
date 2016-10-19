package org.codice.ui.admin.security.stage.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Component<T> {

    public enum ComponentType {
        PORT, HOSTNAME, STRING_ENUM, STRING, PASSWORD, BUTTON, BASE_CONTAINER
    }

    private String id;

    private String title;

    private ComponentType type;

    private List<Component> children;

    private List<String> errors;

    private T value;

    private List<T> defaults;

    public Component(String id, ComponentType type) {
        this.id = id;
        this.type = type;
        children = new ArrayList<>();
        errors = new ArrayList<>();
        defaults = new ArrayList<>();
    }

    public void addError(String error) {
        errors.add(error);
    }

    /**
     * Clears all errors from this component and all sub components
     */
    public void clearAllErrors() {
        errors = new ArrayList<>();

        if (children != null) {
            for (Component subComponent : children) {
                subComponent.clearAllErrors();
            }
        }
    }

    /**
     * Checks this component and all sub components for errors
     *
     * @return
     */
    public boolean containsErrors() {
        if (errors != null && !errors.isEmpty()) {
            return true;
        }

        return children != null && children.stream()
                .anyMatch(p -> p.containsErrors());
    }

    public Component getComponent(String componentId) {
        if (id != null && componentId.equals(id)) {
            return this;
        }

        if(children != null) {
            for (Component subComponent : children) {
                if (subComponent.getId() != null && componentId.equals(id)) {
                    return subComponent;
                }

                Component evenMoreSubComponent = subComponent.getComponent(componentId);
                if (evenMoreSubComponent != null) {
                    return evenMoreSubComponent;
                }
            }
        }
        return null;
    }


    public String getId() {
        return id;
    }

    public void setComponentType(ComponentType componentType) {
        this.type = componentType;
    }

    public void getComponentType(ComponentType componentType) {
        this.type = componentType;
    }

    public T getValue() {
        return value;
    }

    //
    //  Builder Methods
    //
    public static Component builder(String id, ComponentType componentType) {
        return new Component(id, componentType);
    }


    public Component<T> defaults(T... defaults) {
        this.defaults.addAll(Arrays.asList(defaults));
        return this;
    }

    public Component title(String title) {
        this.title = title;
        return this;
    }

    public Component subComponents(Component... subCompotents) {
        this.children.addAll(Arrays.asList(subCompotents));
        return this;
    }
}
