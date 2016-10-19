package org.codice.ui.admin.security;

import org.codice.ui.admin.security.stage.StageComposer;
import org.codice.ui.admin.security.stage.StageFinder;

public interface Wizard {

    String getContextPath();

    StageComposer getStageComposer(String contextPath, StageFinder stageFinder);

    String getTitle();

    String getDescription();

    String initialStageId();
}
