/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.operations;

import org.junit.jupiter.api.Test;
import rife.bld.WebProject;
import rife.bld.dependencies.*;
import rife.tools.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPurgeOperation {
    @Test
    void testInstantiation() {
        var operation = new PurgeOperation();
        assertTrue(operation.dependencies().isEmpty());
        assertTrue(operation.repositories().isEmpty());
        assertNull(operation.libCompileDirectory());
        assertNull(operation.libRuntimeDirectory());
        assertNull(operation.libStandaloneDirectory());
        assertNull(operation.libTestDirectory());
    }

    @Test
    void testPopulation() {
        var repository1 = new Repository("repository1");
        var repository2 = new Repository("repository2");
        var dependency1 = new Dependency("group1", "artifact1");
        var dependency2 = new Dependency("group2", "artifact2");
        var dir1 = new File("dir1");
        var dir2 = new File("dir2");
        var dir3 = new File("dir3");
        var dir4 = new File("dir4");

        var operation1 = new PurgeOperation()
            .repositories(List.of(repository1, repository2))
            .libCompileDirectory(dir1)
            .libRuntimeDirectory(dir2)
            .libStandaloneDirectory(dir3)
            .libTestDirectory(dir4);
        var dependency_scopes = new DependencyScopes();
        dependency_scopes.scope(Scope.compile).include(dependency1).include(dependency2);
        operation1.dependencies(dependency_scopes);
        assertTrue(operation1.repositories().contains(repository1));
        assertTrue(operation1.repositories().contains(repository2));
        assertTrue(operation1.dependencies().scope(Scope.compile).contains(dependency1));
        assertTrue(operation1.dependencies().scope(Scope.compile).contains(dependency2));
        assertEquals(dir1, operation1.libCompileDirectory());
        assertEquals(dir2, operation1.libRuntimeDirectory());
        assertEquals(dir3, operation1.libStandaloneDirectory());
        assertEquals(dir4, operation1.libTestDirectory());

        var operation2 = new PurgeOperation()
            .libCompileDirectory(dir1)
            .libRuntimeDirectory(dir2)
            .libStandaloneDirectory(dir3)
            .libTestDirectory(dir4);
        operation2.repositories().add(repository1);
        operation2.repositories().add(repository2);
        operation2.dependencies().scope(Scope.compile).include(dependency1).include(dependency2);
        operation2.dependencies(dependency_scopes);
        assertTrue(operation2.repositories().contains(repository1));
        assertTrue(operation2.repositories().contains(repository2));
        assertTrue(operation2.dependencies().scope(Scope.compile).contains(dependency1));
        assertTrue(operation2.dependencies().scope(Scope.compile).contains(dependency2));
        assertEquals(dir1, operation2.libCompileDirectory());
        assertEquals(dir2, operation2.libRuntimeDirectory());
        assertEquals(dir3, operation2.libStandaloneDirectory());
        assertEquals(dir4, operation2.libTestDirectory());
    }

    @Test
    void testExecution()
    throws Exception {
        var tmp = Files.createTempDirectory("test").toFile();
        try {
            var dir1 = new File(tmp, "dir1");
            var dir2 = new File(tmp, "dir2");
            var dir3 = new File(tmp, "dir3");
            var dir4 = new File(tmp, "dir4");
            dir1.mkdirs();
            dir2.mkdirs();
            dir3.mkdirs();
            dir4.mkdirs();

            var operation_download1 = new DownloadOperation()
                .repositories(List.of(Repository.MAVEN_CENTRAL))
                .libCompileDirectory(dir1)
                .libRuntimeDirectory(dir2)
                .libStandaloneDirectory(dir3)
                .libTestDirectory(dir4);
            operation_download1.dependencies().scope(Scope.compile)
                .include(new Dependency("org.apache.commons", "commons-lang3", new VersionNumber(3, 1)));
            operation_download1.dependencies().scope(Scope.runtime)
                .include(new Dependency("org.apache.commons", "commons-collections4", new VersionNumber(4, 3)));
            operation_download1.dependencies().scope(Scope.standalone)
                .include(new Dependency("org.slf4j", "slf4j-simple", new VersionNumber(2, 0, 0)));
            operation_download1.dependencies().scope(Scope.test)
                .include(new Dependency("org.apache.httpcomponents.client5", "httpclient5", new VersionNumber(5, 0)));

            operation_download1.execute();

            var operation_download2 = new DownloadOperation()
                .repositories(List.of(Repository.MAVEN_CENTRAL))
                .libCompileDirectory(dir1)
                .libRuntimeDirectory(dir2)
                .libStandaloneDirectory(dir3)
                .libTestDirectory(dir4);
            operation_download2.dependencies().scope(Scope.compile)
                .include(new Dependency("org.apache.commons", "commons-lang3", new VersionNumber(3, 12, 0)));
            operation_download2.dependencies().scope(Scope.runtime)
                .include(new Dependency("org.apache.commons", "commons-collections4", new VersionNumber(4, 4)));
            operation_download2.dependencies().scope(Scope.standalone)
                .include(new Dependency("org.slf4j", "slf4j-simple", new VersionNumber(2, 0, 6)));
            operation_download2.dependencies().scope(Scope.test)
                .include(new Dependency("org.apache.httpcomponents.client5", "httpclient5", new VersionNumber(5, 2, 1)));

            operation_download2.execute();

            assertEquals("""
                    /dir1
                    /dir1/commons-lang3-3.1.jar
                    /dir1/commons-lang3-3.12.0.jar
                    /dir2
                    /dir2/commons-collections4-4.3.jar
                    /dir2/commons-collections4-4.4.jar
                    /dir3
                    /dir3/slf4j-api-2.0.0.jar
                    /dir3/slf4j-api-2.0.6.jar
                    /dir3/slf4j-simple-2.0.0.jar
                    /dir3/slf4j-simple-2.0.6.jar
                    /dir4
                    /dir4/commons-codec-1.13.jar
                    /dir4/httpclient5-5.0.jar
                    /dir4/httpclient5-5.2.1.jar
                    /dir4/httpcore5-5.0.jar
                    /dir4/httpcore5-5.2.jar
                    /dir4/httpcore5-h2-5.0.jar
                    /dir4/httpcore5-h2-5.2.jar
                    /dir4/slf4j-api-1.7.25.jar
                    /dir4/slf4j-api-1.7.36.jar""",
                FileUtils.generateDirectoryListing(tmp));

            var operation_purge = new PurgeOperation()
                .repositories(List.of(Repository.MAVEN_CENTRAL))
                .libCompileDirectory(dir1)
                .libRuntimeDirectory(dir2)
                .libStandaloneDirectory(dir3)
                .libTestDirectory(dir4);
            operation_purge.dependencies().scope(Scope.compile)
                .include(new Dependency("org.apache.commons", "commons-lang3", new VersionNumber(3, 12, 0)));
            operation_purge.dependencies().scope(Scope.runtime)
                .include(new Dependency("org.apache.commons", "commons-collections4", new VersionNumber(4, 4)));
            operation_purge.dependencies().scope(Scope.standalone)
                .include(new Dependency("org.slf4j", "slf4j-simple", new VersionNumber(2, 0, 6)));
            operation_purge.dependencies().scope(Scope.test)
                .include(new Dependency("org.apache.httpcomponents.client5", "httpclient5", new VersionNumber(5, 2, 1)));

            operation_purge.execute();

            assertEquals("""
                    /dir1
                    /dir1/commons-lang3-3.12.0.jar
                    /dir2
                    /dir2/commons-collections4-4.4.jar
                    /dir3
                    /dir3/slf4j-api-2.0.6.jar
                    /dir3/slf4j-simple-2.0.6.jar
                    /dir4
                    /dir4/httpclient5-5.2.1.jar
                    /dir4/httpcore5-5.2.jar
                    /dir4/httpcore5-h2-5.2.jar
                    /dir4/slf4j-api-1.7.36.jar""",
                FileUtils.generateDirectoryListing(tmp));

        } finally {
            FileUtils.deleteDirectory(tmp);
        }
    }

    static class TestProject extends WebProject {
        public TestProject(File tmp) {
            workDirectory = tmp;
            pkg = "test.pkg";
        }
    }

    @Test
    void testFromProject()
    throws Exception {
        var tmp = Files.createTempDirectory("test").toFile();
        try {
            var project1 = new TestProject(tmp);
            project1.createProjectStructure();
            project1.repositories().add(Repository.MAVEN_CENTRAL);
            project1.dependencies().scope(Scope.compile)
                .include(new Dependency("org.apache.commons", "commons-lang3", new VersionNumber(3, 1)));
            project1.dependencies().scope(Scope.runtime)
                .include(new Dependency("org.apache.commons", "commons-collections4", new VersionNumber(4, 3)));
            project1.dependencies().scope(Scope.standalone)
                .include(new Dependency("org.slf4j", "slf4j-simple", new VersionNumber(2, 0, 0)));
            project1.dependencies().scope(Scope.test)
                .include(new Dependency("org.apache.httpcomponents.client5", "httpclient5", new VersionNumber(5, 0)));

            var project2 = new TestProject(tmp);
            project2.createProjectStructure();
            project2.repositories().add(Repository.MAVEN_CENTRAL);
            project2.dependencies().scope(Scope.compile)
                .include(new Dependency("org.apache.commons", "commons-lang3", new VersionNumber(3, 12, 0)));
            project2.dependencies().scope(Scope.runtime)
                .include(new Dependency("org.apache.commons", "commons-collections4", new VersionNumber(4, 4)));
            project2.dependencies().scope(Scope.standalone)
                .include(new Dependency("org.slf4j", "slf4j-simple", new VersionNumber(2, 0, 6)));
            project2.dependencies().scope(Scope.test)
                .include(new Dependency("org.apache.httpcomponents.client5", "httpclient5", new VersionNumber(5, 2, 1)));

            new DownloadOperation()
                .fromProject(project1)
                .execute();
            new DownloadOperation()
                .fromProject(project2)
                .execute();

            assertEquals("""
                    /lib
                    /lib/bld
                    /lib/compile
                    /lib/compile/commons-lang3-3.1.jar
                    /lib/compile/commons-lang3-3.12.0.jar
                    /lib/runtime
                    /lib/runtime/commons-collections4-4.3.jar
                    /lib/runtime/commons-collections4-4.4.jar
                    /lib/standalone
                    /lib/standalone/slf4j-api-2.0.0.jar
                    /lib/standalone/slf4j-api-2.0.6.jar
                    /lib/standalone/slf4j-simple-2.0.0.jar
                    /lib/standalone/slf4j-simple-2.0.6.jar
                    /lib/test
                    /lib/test/commons-codec-1.13.jar
                    /lib/test/httpclient5-5.0.jar
                    /lib/test/httpclient5-5.2.1.jar
                    /lib/test/httpcore5-5.0.jar
                    /lib/test/httpcore5-5.2.jar
                    /lib/test/httpcore5-h2-5.0.jar
                    /lib/test/httpcore5-h2-5.2.jar
                    /lib/test/slf4j-api-1.7.25.jar
                    /lib/test/slf4j-api-1.7.36.jar
                    /src
                    /src/bld
                    /src/bld/java
                    /src/main
                    /src/main/java
                    /src/main/resources
                    /src/main/resources/templates
                    /src/test
                    /src/test/java""",
                FileUtils.generateDirectoryListing(tmp));

            new PurgeOperation()
                .fromProject(project2)
                .execute();

            assertEquals("""
                    /lib
                    /lib/bld
                    /lib/compile
                    /lib/compile/commons-lang3-3.12.0.jar
                    /lib/runtime
                    /lib/runtime/commons-collections4-4.4.jar
                    /lib/standalone
                    /lib/standalone/slf4j-api-2.0.6.jar
                    /lib/standalone/slf4j-simple-2.0.6.jar
                    /lib/test
                    /lib/test/httpclient5-5.2.1.jar
                    /lib/test/httpcore5-5.2.jar
                    /lib/test/httpcore5-h2-5.2.jar
                    /lib/test/slf4j-api-1.7.36.jar
                    /src
                    /src/bld
                    /src/bld/java
                    /src/main
                    /src/main/java
                    /src/main/resources
                    /src/main/resources/templates
                    /src/test
                    /src/test/java""",
                FileUtils.generateDirectoryListing(tmp));
        } finally {
            FileUtils.deleteDirectory(tmp);
        }
    }
}
