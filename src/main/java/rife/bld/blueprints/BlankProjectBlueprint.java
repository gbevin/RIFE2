/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.blueprints;

import rife.bld.Project;
import rife.bld.dependencies.VersionNumber;
import rife.tools.StringUtils;

import java.io.File;
import java.util.List;

import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;
import static rife.bld.dependencies.Repository.SONATYPE_SNAPSHOTS;
import static rife.bld.dependencies.Scope.test;

/**
 * Provides the dependency information required to create a new blank project.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.5
 */
public class BlankProjectBlueprint extends Project {
    public BlankProjectBlueprint(File work, String packageName, String projectName) {
        this(work, packageName, projectName, new VersionNumber(0,0,1));
    }

    public BlankProjectBlueprint(File work, String packageName, String projectName, VersionNumber versionNumber) {
        workDirectory = work;

        pkg = packageName;
        name = projectName;
        mainClass = packageName + "." + StringUtils.capitalize(projectName) + "Main";
        version = versionNumber;

        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);
        scope(test)
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,3)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,3)));
    }
}