package org.codice.ui.admin.security.stage.form;

import java.util.ArrayList;
import java.util.List;

public class Form {

    private String title;

    private List<Question> formContents;

    public Form(String title) {
        this.formContents = new ArrayList<>();
        this.title = title;
    }

    public static Form builder(String title) {
        return new Form(title);
    }

    public Question getContent(String contentId) {
        for (Question content : formContents) {
            if (content.getId()
                    .equals(contentId)) {
                return content;
            }
        }
        return null;
    }

    public Object getContentValue(String contentId) {
        for (Question content : formContents) {
            if (content.getId()
                    .equals(contentId)) {
                return content.getValue();
            }
        }
        return null;
    }

    public void setFormContent(List<Question> formContents) {
        this.formContents = formContents;
    }

    public Form add(Question formContent) {
        formContents.add(formContent);
        return this;
    }

    public boolean containsErrors(){
        for(Question content : formContents) {
            if(content.hasErrors()) {
                return true;
            }
        }

        return false;
    }

    public void clearErrors() {

        List<Question> contentToRemove = new ArrayList<>();

        for(Question content : formContents) {

            if(content instanceof Question) {
                ((Question)content).setError(null);
            }
        }

        formContents.removeAll(contentToRemove);
    }
}
