/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ui.admin.docs;

import static org.codice.ui.admin.docs.DocsSparkApplication.DOCS_DIR;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.codice.ddf.ui.admin.api.module.AdminModule;

public class DocsModule implements AdminModule {

    public static final String DEFAULT_DOCS_CONTEXT = "./documentation";
    @Override
    public String getName() {
        return "Documentation";
    }

    @Override
    public String getId() {
        return "docs";
    }

    @Override
    public URI getJSLocation() {
        return null;
    }

    @Override
    public URI getCSSLocation() {
        return null;
    }

    @Override
    public URI getIframeLocation() {
        try {
            return new URI(getDocumentationContextPath());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks the ddf.home/documentation directory to find the documentation context path.
     * If the documentation folder is not present or documentation.html does not exist, the default documentation context "./documentation" is returned
     * @return documentation path
     */
    public String getDocumentationContextPath() {

        if(!Files.exists(Paths.get(DOCS_DIR))) {
            return DEFAULT_DOCS_CONTEXT;
        }

        Collection<File> files = FileUtils.listFiles(new File(DOCS_DIR),
                new NameFileFilter("documentation.html"),
                TrueFileFilter.INSTANCE);

        if(files.isEmpty()) {
            return DEFAULT_DOCS_CONTEXT;
        }

        String documentationPath = files.iterator().next().getAbsolutePath();
        return "." + documentationPath.split(System.getProperty("karaf.home"))[1];
    }
}
