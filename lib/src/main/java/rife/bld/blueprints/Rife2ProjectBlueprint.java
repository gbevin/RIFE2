/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.blueprints;

import rife.Version;
import rife.bld.WebProject;
import rife.bld.dependencies.VersionNumber;
import rife.bld.operations.TemplateType;
import rife.tools.StringUtils;

import java.io.File;
import java.util.List;

import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;
import static rife.bld.dependencies.Repository.SONATYPE_SNAPSHOTS;
import static rife.bld.dependencies.Scope.*;

/**
 * Provides the dependency information required to create a new RIFE2 project.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.5
 */
public class Rife2ProjectBlueprint extends WebProject {
    public Rife2ProjectBlueprint(File work, String packageName, String projectName) {
        workDirectory = work;

        pkg = packageName;
        name = projectName;
        mainClass = packageName + "." + StringUtils.capitalize(projectName) + "Site";
        version = new VersionNumber(0,0,1);

        precompiledTemplateTypes = List.of(TemplateType.HTML);

        repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);
        scope(compile)
            .include(dependency("com.uwyn.rife2", "rife2", Version.getVersionNumber()));
        scope(test)
            .include(dependency("org.jsoup", "jsoup", version(1,15,4)))
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,2)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,2)));
        scope(standalone)
            .include(dependency("org.eclipse.jetty", "jetty-server", version(11,0,14)))
            .include(dependency("org.eclipse.jetty", "jetty-servlet", version(11,0,14)))
            .include(dependency("org.slf4j", "slf4j-simple", version(2,0,7)));
    }
}