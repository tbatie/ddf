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
package ddf.lib;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class OwaspDiffRunner {

    public static final String MAVEN_VERSION_COMMAND = "mvn -version";

    public static final String MAVEN_SETTINGS_COMMAND = "mvn help:effective-settings";

    public static final String GIT_DIFF_NAME_COMMAND = "git diff --name-only ";

    public static final String GIT_SHORT_BRANCH_NAME_COMMAND = "git rev-parse --abbrev-ref HEAD";

    public static final String PROJECT_ROOT = System.getProperty("user.dir")
            .split("/libs/owasp-diff-runner")[0];

    private static String mavenHome;

    private static String localRepo;

    private static Runtime runTime = Runtime.getRuntime();

    private static Invoker invoker = new DefaultInvoker();

    public OwaspDiffRunner(Runtime runTime, Invoker invoker) {
        this.runTime = runTime;
        this.invoker = invoker;
    }

    public static void main(String[] args) throws OwaspDiffRunnerException {
        mavenHome = getMavenHome();
        localRepo = getLocalRepo();
        String modulesOfChangedPoms = getModulesOfChangedPoms();
        if (modulesOfChangedPoms.isEmpty()) {
            return;
        }

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(PROJECT_ROOT + File.separator + "pom.xml"));
        request.setBaseDirectory(new File(PROJECT_ROOT));
        request.setLocalRepositoryDirectory(new File(localRepo));
        request.setGoals(Arrays.asList("dependency-check:check",
                "-pl",
                modulesOfChangedPoms,
                "-Powasp"));
        invoker.setMavenHome(new File(mavenHome));

        System.out.println(
                "------------------------------------------------------------------------ BEGIN OWASP AUDIT: ------------------------------------------------------------------------");
        System.out.println("-- Maven home: " + mavenHome);
        System.out.println("-- Local Maven repo: " + localRepo);

        InvocationResult mavenBuildResult = null;

        try {
            mavenBuildResult = invoker.execute(request);
        } catch (MavenInvocationException e) {
            new OwaspDiffRunnerException(OwaspDiffRunnerException.UNABLE_TO_RUN_MAVEN, e);
        }
        System.out.println(
                "------------------------------------------------------------------------ END OWASP AUDIT: --------------------------------------------------------------------------");
        if (mavenBuildResult.getExitCode() != 0) {
            throw new OwaspDiffRunnerException(OwaspDiffRunnerException.FOUND_VULNERABILITIES);
        }
    }

    public static String getModulesOfChangedPoms() throws OwaspDiffRunnerException {
        String changedFiles;
        String currentBranchName;

        try {
            currentBranchName = IOUtils.toString(runTime.exec(GIT_SHORT_BRANCH_NAME_COMMAND)
                    .getInputStream())
                    .replace(File.separator, "")
                    .replace(System.getProperty("line.separator"), "");

            changedFiles = IOUtils.toString(runTime.exec(
                    GIT_DIFF_NAME_COMMAND + currentBranchName + "..master")
                    .getInputStream());
        } catch (IOException e) {
            throw new OwaspDiffRunnerException(OwaspDiffRunnerException.UNABLE_TO_RETRIEVE_GIT_INFO,
                    e);
        }

        System.out.println(
                "Comparing commits of branch " + currentBranchName + " to master. Changed poms: ");

        List<String> changedPoms = Arrays.stream(changedFiles.split(System.getProperty(
                "line.separator")))
                .filter(path -> path.endsWith("pom.xml"))
                .collect(Collectors.toList());

        changedPoms.stream()
                .forEach(System.out::println);

        return changedPoms.stream()
                .map(path -> path.endsWith(File.separator + "pom.xml") ?
                        path.replace(File.separator + "pom.xml", "") :
                        path.replace("pom.xml", ""))
                .collect(Collectors.joining(","));
    }

    public static String getMavenHome() throws OwaspDiffRunnerException {
        String mavenHome;
        String mavenVersionInfo;

        try {
            mavenVersionInfo = IOUtils.toString(runTime.exec(MAVEN_VERSION_COMMAND)
                    .getInputStream());

            mavenHome = Arrays.stream(mavenVersionInfo.split(System.getProperty("line.separator")))
                    .filter(str -> str.contains("Maven home:"))
                    .findFirst()
                    .get()
                    .split("Maven home:")[1].trim();

        } catch (Exception e) {
            throw new OwaspDiffRunnerException(OwaspDiffRunnerException.UNABLE_TO_RETRIEVE_MAVEN_HOME,
                    e);
        }

        return mavenHome;
    }

    public static String getLocalRepo() throws OwaspDiffRunnerException {

        String mavenHelpInfo;
        String localRepoProperty = System.getProperty("maven.repo.local");

        if (localRepoProperty != null && !localRepoProperty.isEmpty()) {
            if (!new File(localRepoProperty).exists()) {
                throw new OwaspDiffRunnerException(
                        OwaspDiffRunnerException.UNABLE_TO_RETRIEVE_LOCAL_MAVEN_REPO
                                + localRepoProperty);
            }
            return localRepoProperty;
        }

        try {
            mavenHelpInfo = IOUtils.toString(runTime.exec(MAVEN_SETTINGS_COMMAND)
                    .getInputStream());

            return mavenHelpInfo.split("<localRepository(.*\")?>")[1].split("</localRepository>")[0];

        } catch (Exception e) {
            throw new OwaspDiffRunnerException(OwaspDiffRunnerException.UNABLE_TO_RETRIEVE_LOCAL_MAVEN_REPO,
                    e);
        }

    }
}
