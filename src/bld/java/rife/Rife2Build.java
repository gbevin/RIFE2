/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife;

import rife.bld.BuildCommand;
import rife.bld.operations.*;
import rife.bld.publish.*;
import rife.tools.FileUtils;

import java.io.File;
import java.util.List;
import java.util.jar.Attributes;
import java.util.regex.Pattern;

import static rife.bld.dependencies.Scope.*;
import static rife.bld.operations.TemplateType.*;

public class Rife2Build extends AbstractRife2Build {
    public Rife2Build()
    throws Exception {
        pkg = "rife";
        name = "RIFE2";
        version = version(FileUtils.readString(new File(srcMainResourcesDirectory(), "RIFE_VERSION")));

        scope(provided)
            .include(dependency("org.jsoup", "jsoup", version(1,16,1)))
            .include(dependency("jakarta.servlet", "jakarta.servlet-api", version(5,0,0)))
            .include(dependency("org.eclipse.jetty", "jetty-server", version(11,0,15)))
            .include(dependency("org.eclipse.jetty", "jetty-servlet", version(11,0,15)))
            .include(dependency("org.apache.tomcat.embed", "tomcat-embed-core", version(10, 1, 12)))
            .include(dependency("org.apache.tomcat.embed", "tomcat-embed-jasper", version(10, 1, 12)))
            .include(dependency("net.imagej", "ij", version("1.54d")));
        scope(test)
            .include(dependency("org.jsoup", "jsoup", version(1,16,1)))
            .include(dependency("jakarta.servlet", "jakarta.servlet-api", version(5,0,0)))
            .include(dependency("org.eclipse.jetty", "jetty-server", version(11,0,15)))
            .include(dependency("org.eclipse.jetty", "jetty-servlet", version(11,0,15)))
            .include(dependency("net.imagej", "ij", version("1.54d")));

        var core_directory = new File(workDirectory(), "core");
        var core_src_directory = new File(core_directory, "src");
        var core_src_main_directory = new File(core_src_directory, "main");
        var core_src_main_java_directory = new File(core_src_main_directory, "java");
        var core_src_main_resources_directory = new File(core_src_main_directory, "resources");
        var core_src_test_directory = new File(core_src_directory, "test");
        var core_src_test_java_directory = new File(core_src_test_directory, "java");
        var core_src_test_resources_directory = new File(core_src_test_directory, "resources");
        var core_src_main_resources_templates_directory = new File(core_src_main_resources_directory, "templates");

        antlr4Operation
            .sourceDirectories(List.of(new File(core_src_main_directory, "antlr")))
            .outputDirectory(new File(buildDirectory(), "generated/rife/template/antlr"));

        precompileOperation()
            .sourceDirectories(core_src_main_resources_templates_directory)
            .templateTypes(HTML, XML, SQL, TXT, JSON);

        compileOperation()
            .mainSourceDirectories(antlr4Operation.outputDirectory(), core_src_main_java_directory)
            .testSourceDirectories(core_src_test_java_directory)
            .compileOptions()
                .debuggingInfo(JavacOptions.DebuggingInfo.ALL);

        jarOperation()
            .sourceDirectories(core_src_main_resources_directory)
            .excluded(Pattern.compile("^\\Q" + core_src_main_resources_templates_directory.getAbsolutePath() + "\\E.*"));

        jarAgentOperation
            .fromProject(this)
            .destinationFileName("rife2-" + version() + "-agent.jar")
            .manifestAttribute(new Attributes.Name("Premain-Class"), "rife.instrument.RifeAgent")
            .included(
                "rife/asm/",
                "rife/instrument/",
                "rife/continuations/ContinuationConfigInstrument",
                "rife/continuations/instrument/",
                "rife/database/querymanagers/generic/instrument/",
                "rife/engine/EngineContinuationConfigInstrument",
                "rife/tools/ClassBytesLoader",
                "rife/tools/FileUtils",
                "rife/tools/InstrumentationUtils",
                "rife/tools/RawFormatter",
                "rife/tools/exceptions/FileUtils",
                "rife/validation/instrument/",
                "rife/validation/MetaDataMerged",
                "rife/validation/MetaDataBeanAware",
                "rife/workflow/config/ContinuationInstrument");

        jarContinuationsOperation
            .fromProject(this)
            .destinationFileName("rife2-" + version() + "-agent-continuations.jar")
            .manifestAttribute(new Attributes.Name("Premain-Class"), "rife.continuations.instrument.ContinuationsAgent")
            .included(
                "rife/asm/",
                "rife/instrument/",
                "rife/continuations/ContinuationConfigInstrument",
                "rife/continuations/instrument/",
                "rife/tools/ClassBytesLoader",
                "rife/tools/FileUtils",
                "rife/tools/InstrumentationUtils",
                "rife/tools/RawFormatter",
                "rife/tools/exceptions/FileUtils");

        testsBadgeOperation
            .classpath(core_src_main_resources_directory.getAbsolutePath())
            .classpath(core_src_test_resources_directory.getAbsolutePath())
            .javaOptions()
                .javaAgent(new File(buildDistDirectory(), jarAgentOperation.destinationFileName()));

        javadocOperation()
            .sourceFiles(FileUtils.getJavaFileList(core_src_main_java_directory))
            .javadocOptions()
                .docTitle("<a href=\"https://rife2.com\">RIFE2</a> " + version())
                .overview(new File(srcMainJavaDirectory(), "overview.html"));

        publishOperation()
            .repository(version.isSnapshot() ? repository("rife2-snapshots") : repository("rife2-releases"))
            .repository(version.isSnapshot() ? repository("sonatype-snapshots") : repository("sonatype-releases"))
            .info(new PublishInfo()
                .groupId("com.uwyn.rife2")
                .artifactId("rife2")
                .name("RIFE2")
                .description("Full-stack, no-declaration, framework to quickly and effortlessly create web applications with modern Java.")
                .url("https://github.com/rife2/rife2")
                .developer(new PublishDeveloper()
                    .id("gbevin")
                    .name("Geert Bevin")
                    .email("gbevin@uwyn.com")
                    .url("https://github.com/gbevin"))
                .license(new PublishLicense()
                    .name("The Apache License, Version 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
                .scm(new PublishScm()
                    .connection("scm:git:https://github.com/rife2/rife2.git")
                    .developerConnection("scm:git:git@github.com:rife2/rife2.git")
                    .url("https://github.com/rife2/rife2"))
                .signKey(property("sign.key"))
                .signPassphrase(property("sign.passphrase")))
            .artifacts(
                new PublishArtifact(jarAgentOperation.destinationFile(), "agent", "jar"),
                new PublishArtifact(jarContinuationsOperation.destinationFile(), "agent-continuations", "jar"));

        examples = new ExamplesBuild(this);
    }

    final JarOperation jarAgentOperation = new JarOperation();
    @BuildCommand(value = "jar-agent", summary = "Creates the agent jar archive")
    public void jarAgent()
    throws Exception {
        compile();
        jarAgentOperation.executeOnce();
    }

    final JarOperation jarContinuationsOperation = new JarOperation();
    @BuildCommand(value = "jar-continuations", summary = "Creates the continuations agent jar archive")
    public void jarContinuations()
    throws Exception {
        compile();
        jarContinuationsOperation.executeOnce();
    }

    public void test()
    throws Exception {
        jarAgent();
        super.test();
    }

    @BuildCommand(summary = "Creates all the distribution artifacts")
    public void all()
    throws Exception {
        jar();
        jarSources();
        jarJavadoc();
        jarAgent();
        jarContinuations();
    }

    public void publish()
    throws Exception {
        all();
        super.publish();
    }

    public void publishLocal()
    throws Exception {
        all();
        super.publishLocal();
    }

    final ExamplesBuild examples;

    @BuildCommand(value = "compile-examples", summary = "Compiles the RIFE2 examples")
    public void compileExamples()
    throws Exception {
        compile();
        precompile();
        examples.compile();
    }

    @BuildCommand(value = "test-examples", summary = "Tests the RIFE2 examples")
    public void testExamples()
    throws Exception {
        jarAgent();
        examples.test();
    }

    @BuildCommand(value = "run-examples", summary = "Run the RIFE2 examples")
    public void runExamples()
    throws Exception {
        jarAgent();
        examples.run();
    }

    public static void main(String[] args)
    throws Exception {
        new Rife2Build().start(args);
    }
}