/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld;

import rife.bld.dependencies.*;
import rife.bld.help.*;
import rife.bld.operations.*;
import rife.tools.Convert;
import rife.tools.FileUtils;

import java.io.File;
import java.util.*;

import static rife.bld.dependencies.Scope.runtime;
import static rife.tools.FileUtils.JAR_FILE_PATTERN;
import static rife.tools.FileUtils.JAVA_FILE_PATTERN;

/**
 * Provides the configuration and commands of a Java project for the
 * build system.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.5
 */
public class Project extends BuildExecutor {
    /**
     * The work directory of the project.
     *
     * @see #workDirectory()
     * @since 1.5
     */
    protected File workDirectory = new File(System.getProperty("user.dir"));
    /**
     * The project's package.
     *
     * @see #pkg()
     * @since 1.5
     */
    protected String pkg = null;
    /**
     * The project's name.
     *
     * @see #name()
     * @since 1.5
     */
    protected String name = null;
    /**
     * The project's version.
     *
     * @see #version()
     * @since 1.5
     */
    protected VersionNumber version = null;
    /**
     * The project's main class.
     *
     * @see #mainClass()
     * @since 1.5
     */
    protected String mainClass = null;

    /**
     * The project's repositories for dependency resolution.
     *
     * @see #repositories()
     * @since 1.5
     */
    protected List<Repository> repositories = new ArrayList<>();
    /**
     * The project's dependencies.
     *
     * @see #dependencies()
     * @since 1.5
     */
    protected DependencyScopes dependencies = new DependencyScopes();

    /**
     * The project's precompiled template types.
     *
     * @see #precompiledTemplateTypes()
     * @since 1.5
     */
    protected List<TemplateType> precompiledTemplateTypes = new ArrayList<>();
    /**
     * The project's Java release version for compilation.
     *
     * @see #compileJavacOptions()
     * @since 1.5.6
     */
    protected Integer javaRelease = null;
    /**
     * The project's javac options for compilation.
     *
     * @see #compileJavacOptions()
     * @since 1.5
     */
    protected List<String> compileJavacOptions = new ArrayList<>();
    /**
     * The tool that is used for running the java main class.
     *
     * @see #javaTool()
     * @since 1.5
     */
    protected String javaTool = null;
    /**
     * The project's java options for the run command.
     *
     * @see #runJavaOptions()
     * @since 1.5
     */
    protected List<String> runJavaOptions = new ArrayList<>();
    /**
     * The project's java options for the test command.
     *
     * @see #testJavaOptions()
     * @since 1.5
     */
    protected List<String> testJavaOptions = new ArrayList<>();
    /**
     * The project's test tool main class.
     *
     * @see #testToolMainClass()
     * @since 1.5
     */
    protected String testToolMainClass;
    /**
     * The project's test tool options.
     *
     * @see #testToolOptions()
     * @since 1.5
     */
    protected List<String> testToolOptions = new ArrayList<>();
    /**
     * The base name that is used for creating archives.
     *
     * @see #archiveBaseName()
     * @since 1.5
     */
    protected String archiveBaseName = null;
    /**
     * The filename to use for the main jar archive creation.
     *
     * @see #jarFileName()
     * @since 1.5
     */
    protected String jarFileName = null;
    /**
     * The filename to use for the uber jar archive creation.
     *
     * @see #uberJarFileName()
     * @since 1.5
     */
    protected String uberJarFileName = null;
    /**
     * The main class to run the UberJar with.
     *
     * @see #uberJarMainClass()
     * @since 1.5
     */
    protected String uberJarMainClass = null;

    /**
     * The source code directory.
     *
     * @see #srcDirectory()
     * @since 1.5
     */
    protected File srcDirectory = null;
    /**
     * The bld source code directory.
     *
     * @see #srcBldDirectory()
     * @since 1.5
     */
    protected File srcBldDirectory = null;
    /**
     * The bld java source code directory.
     *
     * @see #srcBldJavaDirectory()
     * @since 1.5
     */
    protected File srcBldJavaDirectory = null;
    /**
     * The main source code directory.
     *
     * @see #srcMainDirectory()
     * @since 1.5
     */
    protected File srcMainDirectory = null;
    /**
     * The main java source code directory.
     *
     * @see #srcMainJavaDirectory()
     * @since 1.5
     */
    protected File srcMainJavaDirectory = null;
    /**
     * The main resources source code directory.
     *
     * @see #srcMainResourcesDirectory()
     * @since 1.5
     */
    protected File srcMainResourcesDirectory = null;
    /**
     * The main template resources source code directory.
     *
     * @see #srcMainResourcesTemplatesDirectory()
     * @since 1.5
     */
    protected File srcMainResourcesTemplatesDirectory = null;
    /**
     * The test source code directory.
     *
     * @see #srcTestDirectory()
     * @since 1.5
     */
    protected File srcTestDirectory = null;
    /**
     * The test java source code directory.
     *
     * @see #srcTestJavaDirectory()
     * @since 1.5
     */
    protected File srcTestJavaDirectory = null;
    /**
     * The lib directory.
     *
     * @see #libDirectory()
     * @since 1.5
     */
    protected File libDirectory = null;
    /**
     * The compile scope lib directory.
     *
     * @see #libCompileDirectory()
     * @since 1.5
     */
    protected File libCompileDirectory = null;
    /**
     * The runtime scope lib directory.
     *
     * @see #libRuntimeDirectory()
     * @since 1.5
     */
    protected File libRuntimeDirectory = null;
    /**
     * The standalone scope lib directory.
     *
     * @see #libStandaloneDirectory()
     * @since 1.5
     */
    protected File libStandaloneDirectory = null;
    /**
     * The standalone scope lib directory.
     *
     * @see #libTestDirectory()
     * @since 1.5
     */
    protected File libTestDirectory = null;
    /**
     * The build directory.
     *
     * @see #buildDirectory()
     * @since 1.5
     */
    protected File buildDirectory = null;
    /**
     * The bld build directory.
     *
     * @see #buildBldDirectory()
     * @since 1.5
     */
    protected File buildBldDirectory = null;
    /**
     * The dist build directory.
     *
     * @see #buildDistDirectory()
     * @since 1.5
     */
    protected File buildDistDirectory = null;
    /**
     * The main build directory.
     *
     * @see #buildMainDirectory()
     * @since 1.5
     */
    protected File buildMainDirectory = null;
    /**
     * The templates build directory.
     *
     * @see #buildTemplatesDirectory()
     * @since 1.5
     */
    protected File buildTemplatesDirectory = null;
    /**
     * The test build directory.
     *
     * @see #buildTestDirectory()
     * @since 1.5
     */
    protected File buildTestDirectory = null;

    /*
     * Standard build commands
     */

    /**
     * Standard build command, cleans the build files.
     *
     * @since 1.5
     */
    @BuildCommand(help = CleanHelp.class)
    public void clean()
    throws Exception {
        new CleanOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, compiles the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = CompileHelp.class)
    public void compile()
    throws Exception {
        new CompileOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, downloads all dependencies of the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = DownloadHelp.class)
    public void download() {
        new DownloadOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, pre-compiles RIFE2 templates to class files.
     *
     * @since 1.5
     */
    @BuildCommand(help = PrecompileHelp.class)
    public void precompile() {
        new PrecompileOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, creates a jar archive for the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = JarHelp.class)
    public void jar()
    throws Exception {
        compile();
        precompile();
        new JarOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, purges all unused artifacts from the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = PurgeHelp.class)
    public void purge()
    throws Exception {
        new PurgeOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, runs the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = RunHelp.class)
    public void run()
    throws Exception {
        new RunOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, tests the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = TestHelp.class)
    public void test()
    throws Exception {
        new TestOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, creates an UberJar archive for the project.
     *
     * @since 1.5
     */
    @BuildCommand(help = UberJarHelp.class)
    public void uberjar()
    throws Exception {
        jar();
        new UberJarOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, checks for updates of the project dependencies.
     *
     * @since 1.5
     */
    @BuildCommand(help = UpdatesHelp.class)
    public void updates()
    throws Exception {
        new UpdatesOperation().fromProject(this).execute();
    }

    /**
     * Standard build command, outputs the version of the build system.
     *
     * @since 1.5.2
     */
    @BuildCommand(value = "version", help = VersionHelp.class)
    public void printVersion() {
        new VersionOperation().fromArguments(arguments()).execute();
    }

    /*
     * Useful methods
     */

    /**
     * Includes the RIFE2 instrumentation agent as a runtime dependency,
     * and activates it for run and test commands.
     *
     * @param version the version of the instrumentation agent to use
     * @since 1.5.5
     */
    public void useRife2Agent(VersionNumber version) {
        var agent = new Dependency("com.uwyn.rife2", "rife2", version, "agent");
        scope(runtime).include(agent);
        runJavaOptions.add("-javaagent:" + new File(libRuntimeDirectory(), agent.toFileName()));
        testJavaOptions.add("-javaagent:" + new File(libRuntimeDirectory(), agent.toFileName()));
    }

    /**
     * Creates a new repository instance.
     *
     * @param url the repository URL
     * @return a newly created {@code Repository} instance
     * @since 1.5.6
     */
    public static Repository repository(String url) {
        return new Repository(url);
    }

    /**
     * Creates a new version instance.
     *
     * @param major the major component of the version number
     * @return a newly created {@code VersionNumber} instance
     * @since 1.5
     */
    public static VersionNumber version(int major) {
        return new VersionNumber(major);
    }

    /**
     * Creates a new version instance.
     *
     * @param major the major component of the version number
     * @param minor the minor component of the version number
     * @return a newly created {@code VersionNumber} instance
     * @since 1.5
     */
    public static VersionNumber version(int major, int minor) {
        return new VersionNumber(major, minor);
    }

    /**
     * Creates a new version instance.
     *
     * @param major    the major component of the version number
     * @param minor    the minor component of the version number
     * @param revision the revision component of the version number
     * @return a newly created {@code VersionNumber} instance
     * @since 1.5
     */
    public static VersionNumber version(int major, int minor, int revision) {
        return new VersionNumber(major, minor, revision);
    }

    /**
     * Creates a new version instance.
     *
     * @param major     the major component of the version number
     * @param minor     the minor component of the version number
     * @param revision  the revision component of the version number
     * @param qualifier the qualifier component of the version number
     * @return a newly created {@code VersionNumber} instance
     * @since 1.5
     */
    public static VersionNumber version(int major, int minor, int revision, String qualifier) {
        return new VersionNumber(major, minor, revision, qualifier);
    }

    /**
     * Creates a new version instance.
     *
     * @param description the textual description of the version number
     * @return a newly created {@code VersionNumber} instance; or
     * {@link VersionNumber#UNKNOWN} if the description couldn't be parsed
     * @since 1.5
     */
    public static VersionNumber version(String description) {
        return VersionNumber.parse(description);
    }

    /**
     * Retrieves the dependency set for a particular scope, intializing it
     * if it doesn't exist yet.
     *
     * @param scope the scope to retrieve dependencies for
     * @return the scope's dependency set
     * @since 1.5
     */
    public DependencySet scope(Scope scope) {
        return dependencies().scope(scope);
    }

    /**
     * Creates a new dependency instance.
     *
     * @param groupId    the dependency group identifier
     * @param artifactId the dependency artifact identifier
     * @return a newly created {@code Dependency} instance
     * @since 1.5
     */
    public static Dependency dependency(String groupId, String artifactId) {
        return new Dependency(groupId, artifactId);
    }

    /**
     * Creates a new dependency instance.
     *
     * @param groupId    the dependency group identifier
     * @param artifactId the dependency artifact identifier
     * @param version    the dependency version
     * @return a newly created {@code Dependency} instance
     * @since 1.5
     */
    public static Dependency dependency(String groupId, String artifactId, VersionNumber version) {
        return new Dependency(groupId, artifactId, version);
    }

    /**
     * Creates a new dependency instance.
     *
     * @param groupId    the dependency group identifier
     * @param artifactId the dependency artifact identifier
     * @param version    the dependency version
     * @param classifier the dependency classifier
     * @return a newly created {@code Dependency} instance
     * @since 1.5
     */
    public static Dependency dependency(String groupId, String artifactId, VersionNumber version, String classifier) {
        return new Dependency(groupId, artifactId, version, classifier);
    }

    /**
     * Creates a new dependency instance.
     *
     * @param groupId    the dependency group identifier
     * @param artifactId the dependency artifact identifier
     * @param version    the dependency version
     * @param classifier the dependency classifier
     * @param type       the dependency type
     * @return a newly created {@code Dependency} instance
     * @since 1.5
     */
    public static Dependency dependency(String groupId, String artifactId, VersionNumber version, String classifier, String type) {
        return new Dependency(groupId, artifactId, version, classifier, type);
    }

    /**
     * Creates a new dependency instance from a string representation.
     * The format is {@code groupId:artifactId:version:classifier@type}.
     * The {@code version}, {@code classifier} and {@code type} are optional.
     * <p>
     * If the string can't be successfully parsed, {@code null} will be returned.
     *
     * @param description the dependency string to parse
     * @return a parsed instance of {@code Dependency}; or
     * {@code null} when the string couldn't be parsed
     * @since 1.5.2
     */
    public static Dependency dependency(String description) {
        return Dependency.parse(description);
    }

    /**
     * Creates a local dependency instance.
     * <p>
     * If the local dependency points to a directory, it will be scanned for jar files.
     *
     * @param path the file system path of the local dependency
     * @since 1.5.2
     */

    public static LocalDependency local(String path) {
        return new LocalDependency(path);
    }

    /*
     * Project directories
     */

    /**
     * Returns the work directory of the project.
     * Defaults to this process's user working directory.
     *
     * @since 1.5
     */
    public File workDirectory() {
        return Objects.requireNonNullElseGet(workDirectory, () -> new File(System.getProperty("user.dir")));
    }

    /**
     * Returns the project source code directory.
     * Defaults to {@code "src"} relative to {@link #workDirectory()}.
     *
     * @since 1.5
     */
    public File srcDirectory() {
        return Objects.requireNonNullElseGet(srcDirectory, () -> new File(workDirectory(), "src"));
    }

    /**
     * Returns the project bld source code directory.
     * Defaults to {@code "bld"} relative to {@link #srcDirectory()}.
     *
     * @since 1.5
     */
    public File srcBldDirectory() {
        return Objects.requireNonNullElseGet(srcBldDirectory, () -> new File(srcDirectory(), "bld"));
    }

    /**
     * Returns the project bld java source code directory.
     * Defaults to {@code "java"} relative to {@link #srcBldDirectory()}.
     *
     * @since 1.5
     */
    public File srcBldJavaDirectory() {
        return Objects.requireNonNullElseGet(srcBldJavaDirectory, () -> new File(srcBldDirectory(), "java"));
    }

    /**
     * Returns the project main source code directory.
     * Defaults to {@code "main"} relative to {@link #srcDirectory()}.
     *
     * @since 1.5
     */
    public File srcMainDirectory() {
        return Objects.requireNonNullElseGet(srcMainDirectory, () -> new File(srcDirectory(), "main"));
    }

    /**
     * Returns the project main java source code directory.
     * Defaults to {@code "java"} relative to {@link #srcMainDirectory()}.
     *
     * @since 1.5
     */
    public File srcMainJavaDirectory() {
        return Objects.requireNonNullElseGet(srcMainJavaDirectory, () -> new File(srcMainDirectory(), "java"));
    }

    /**
     * Returns the project main resources source code directory.
     * Defaults to {@code "resources"} relative to {@link #srcMainDirectory()}.
     *
     * @since 1.5
     */
    public File srcMainResourcesDirectory() {
        return Objects.requireNonNullElseGet(srcMainResourcesDirectory, () -> new File(srcMainDirectory(), "resources"));
    }

    /**
     * Returns the project main template resources source code directory.
     * Defaults to {@code "templates"} relative to {@link #srcMainResourcesDirectory()}.
     *
     * @since 1.5
     */
    public File srcMainResourcesTemplatesDirectory() {
        return Objects.requireNonNullElseGet(srcMainResourcesTemplatesDirectory, () -> new File(srcMainResourcesDirectory(), "templates"));
    }

    /**
     * Returns the project test source code directory.
     * Defaults to {@code "test"} relative to {@link #srcDirectory()}.
     *
     * @since 1.5
     */
    public File srcTestDirectory() {
        return Objects.requireNonNullElseGet(srcTestDirectory, () -> new File(srcDirectory(), "test"));
    }

    /**
     * Returns the project test java source code directory.
     * Defaults to {@code "java"} relative to {@link #srcTestDirectory()}.
     *
     * @since 1.5
     */
    public File srcTestJavaDirectory() {
        return Objects.requireNonNullElseGet(srcTestJavaDirectory, () -> new File(srcTestDirectory(), "java"));
    }

    /**
     * Returns the project lib directory.
     * Defaults to {@code "lib"} relative to {@link #workDirectory()}.
     *
     * @since 1.5
     */
    public File libDirectory() {
        return Objects.requireNonNullElseGet(libDirectory, () -> new File(workDirectory(), "lib"));
    }

    /**
     * Returns the {@code lib/bld} directory relative to {@link #workDirectory()}.
     *
     * @since 1.5
     */
    public final File libBldDirectory() {
        return new File(new File(workDirectory(), "lib"), "bld");
    }

    /**
     * Returns the project compile scope lib directory.
     * Defaults to {@code "compile"} relative to {@link #libDirectory()}.
     *
     * @since 1.5
     */
    public File libCompileDirectory() {
        return Objects.requireNonNullElseGet(libCompileDirectory, () -> new File(libDirectory(), "compile"));
    }

    /**
     * Returns the project runtime scope lib directory.
     * Defaults to {@code "runtime"} relative to {@link #libDirectory()}.
     *
     * @since 1.5
     */
    public File libRuntimeDirectory() {
        return Objects.requireNonNullElseGet(libRuntimeDirectory, () -> new File(libDirectory(), "runtime"));
    }

    /**
     * Returns the project standalone scope lib directory.
     * Defaults to {@code null}.
     *
     * @since 1.5
     */
    public File libStandaloneDirectory() {
        return null;
    }

    /**
     * Returns the project test scope lib directory.
     * Defaults to {@code "test"} relative to {@link #libDirectory()}.
     *
     * @since 1.5
     */
    public File libTestDirectory() {
        return Objects.requireNonNullElseGet(libTestDirectory, () -> new File(libDirectory(), "test"));
    }

    /**
     * Returns the project build directory.
     * Defaults to {@code "build"} relative to {@link #workDirectory()}.
     *
     * @since 1.5
     */
    public File buildDirectory() {
        return Objects.requireNonNullElseGet(buildDirectory, () -> new File(workDirectory(), "build"));
    }

    /**
     * Returns the project bld build directory.
     * Defaults to {@code "bld"} relative to {@link #buildDirectory()}.
     *
     * @since 1.5
     */
    public File buildBldDirectory() {
        return Objects.requireNonNullElseGet(buildBldDirectory, () -> new File(buildDirectory(), "bld"));
    }

    /**
     * Returns the project dist build directory.
     * Defaults to {@code "dist"} relative to {@link #buildDirectory()}.
     *
     * @since 1.5
     */
    public File buildDistDirectory() {
        return Objects.requireNonNullElseGet(buildDistDirectory, () -> new File(buildDirectory(), "dist"));
    }

    /**
     * Returns the project main build directory.
     * Defaults to {@code "main"} relative to {@link #buildDirectory()}.
     *
     * @since 1.5
     */
    public File buildMainDirectory() {
        return Objects.requireNonNullElseGet(buildMainDirectory, () -> new File(buildDirectory(), "main"));
    }

    /**
     * Returns the project templates build directory.
     * Defaults to {@link #buildMainDirectory()}.
     *
     * @since 1.5
     */
    public File buildTemplatesDirectory() {
        return Objects.requireNonNullElseGet(buildTemplatesDirectory, this::buildMainDirectory);
    }

    /**
     * Returns the project test build directory.
     * Defaults to {@code "test"} relative to {@link #buildDirectory()}.
     *
     * @since 1.5
     */
    public File buildTestDirectory() {
        return Objects.requireNonNullElseGet(buildTestDirectory, () -> new File(buildDirectory(), "test"));
    }

    /**
     * Creates the project structure based on the directories that are specified in the other methods.
     *
     * @since 1.5
     */
    public void createProjectStructure() {
        srcMainJavaDirectory().mkdirs();
        srcMainResourcesTemplatesDirectory().mkdirs();
        srcBldJavaDirectory().mkdirs();
        srcTestJavaDirectory().mkdirs();
        libCompileDirectory().mkdirs();
        libBldDirectory().mkdirs();
        libRuntimeDirectory().mkdirs();
        libTestDirectory().mkdirs();
        if (libStandaloneDirectory() != null) {
            libStandaloneDirectory().mkdirs();
        }
    }

    /**
     * Creates the project build structure based on the directories that are specified in the other methods.
     *
     * @since 1.5
     */
    public void createBuildStructure() {
        buildBldDirectory().mkdirs();
        buildDistDirectory().mkdirs();
        buildMainDirectory().mkdirs();
        buildTestDirectory().mkdirs();
    }

    /*
     * Project options
     */

    /**
     * Returns the project's package.
     *
     * @since 1.5
     */
    public String pkg() {
        if (pkg == null) {
            throw new IllegalStateException("The pkg variable has to be set.");
        }
        return pkg;
    }

    /**
     * Returns the project's name.
     *
     * @since 1.5
     */
    public String name() {
        if (name == null) {
            throw new IllegalStateException("The name variable has to be set.");
        }
        return name;
    }

    /**
     * Returns the project's version.
     *
     * @since 1.5
     */
    public VersionNumber version() {
        if (version == null) {
            throw new IllegalStateException("The version variable has to be set.");
        }
        return version;
    }

    /**
     * Returns the project's main class.
     *
     * @since 1.5
     */
    public String mainClass() {
        if (mainClass == null) {
            throw new IllegalStateException("The mainClass variable has to be set.");
        }
        return mainClass;
    }

    /**
     * Returns the list of repositories for this project.
     * <p>
     * This list can be modified to change the repositories that the project uses.
     *
     * @since 1.5
     */
    public List<Repository> repositories() {
        if (repositories == null) {
            repositories = new ArrayList<>();
        }
        return repositories;
    }

    /**
     * Adds repositories to this project.
     *
     * @param repositories the repositories to add
     * @since 1.5.6
     */
    public void repositories(Repository... repositories) {
        for (var repository : repositories) {
            repositories().add(repository);
        }
    }

    /**
     * Returns the project's dependencies.
     * <p>
     * This collection can be modified to change the dependencies that the project uses.
     *
     * @since 1.5
     */
    public DependencyScopes dependencies() {
        if (dependencies == null) {
            dependencies = new DependencyScopes();
        }
        return dependencies;
    }

    /**
     * Returns the project's precompiled template types.
     * <p>
     * This list can be modified to change the template types that are precompiled in the project.
     *
     * @since 1.5
     */
    public List<TemplateType> precompiledTemplateTypes() {
        if (precompiledTemplateTypes == null) {
            precompiledTemplateTypes = new ArrayList<>();
        }
        return precompiledTemplateTypes;
    }

    /**
     * Adds precompiled template types to this project.
     *
     * @param types the template types to add
     * @since 1.5.6
     */
    public void precompiledTemplateTypes(TemplateType... types) {
        for (var type : types) {
            precompiledTemplateTypes().add(type);
        }
    }

    /**
     * Returns the project's javac options for compilation.
     * <p>
     * This list can be modified to change the javac options for the project.
     *
     * @since 1.5
     */
    public List<String> compileJavacOptions() {
        if (compileJavacOptions == null) {
            compileJavacOptions = new ArrayList<>();
        }
        if (javaRelease != null &&
            !compileJavacOptions.contains("--release")) {
            compileJavacOptions.add("--release");
            compileJavacOptions.add(Convert.toString(javaRelease));
        }
        return compileJavacOptions;
    }

    /**
     * Adds javac compilation options to this project.
     *
     * @param options the options to add
     * @since 1.5.6
     */
    public void compileJavacOptions(String... options) {
        for (var option : options) {
            compileJavacOptions().add(option);
        }
    }

    /**
     * Returns the tool that is used for running the java main class.
     *
     * @since 1.5
     */
    public String javaTool() {
        return Objects.requireNonNullElse(javaTool, "java");
    }

    /**
     * Returns the project's java options for the run command.
     * <p>
     * This list can be modified to change the java run command options for the project.
     *
     * @since 1.5
     */
    public List<String> runJavaOptions() {
        if (runJavaOptions == null) {
            runJavaOptions = new ArrayList<>();
        }
        return runJavaOptions;
    }

    /**
     * Adds java run options to this project.
     *
     * @param options the options to add
     * @since 1.5.6
     */
    public void runJavaOptions(String... options) {
        for (var option : options) {
            runJavaOptions().add(option);
        }
    }

    /**
     * Returns the project's java options for the test command.
     * <p>
     * This list can be modified to change the java test command options for the project.
     *
     * @since 1.5
     */
    public List<String> testJavaOptions() {
        if (testJavaOptions == null) {
            testJavaOptions = new ArrayList<>();
        }
        return testJavaOptions;
    }

    /**
     * Adds java test options to this project.
     *
     * @param options the options to add
     * @since 1.5.6
     */
    public void testJavaOptions(String... options) {
        for (var option : options) {
            testJavaOptions().add(option);
        }
    }

    /**
     * Returns the project's test tool main class.
     * By default, this returns the tool to run JUnit 5 tests.
     *
     * @since 1.5
     */
    public String testToolMainClass() {
        return Objects.requireNonNullElse(testToolMainClass, "org.junit.platform.console.ConsoleLauncher");

    }

    /**
     * Returns the project's test tool options.
     * By default, these include a good selection for running tests with JUnit 5.
     * <p>
     * This list can be modified to change the test tool options for the project.
     *
     * @since 1.5
     */
    public List<String> testToolOptions() {
        if (testToolOptions == null || testToolOptions.isEmpty()) {
            var result = new ArrayList<String>();
            result.add("--details=verbose");
            result.add("--scan-classpath");
            result.add("--disable-banner");
            result.add("--disable-ansi-colors");
            result.add("--exclude-engine=junit-platform-suite");
            result.add("--exclude-engine=junit-vintage");
            testToolOptions = result;
        }

        return testToolOptions;
    }

    /**
     * Adds test tool options to this project.
     *
     * @param options the options to add
     * @since 1.5.6
     */
    public void testToolOptions(String... options) {
        for (var option : options) {
            testToolOptions().add(option);
        }
    }

    /**
     * Returns the base name that is used for creating archives.
     * By default, this returns the lower-cased project name.
     *
     * @since 1.5
     */
    public String archiveBaseName() {
        return Objects.requireNonNullElseGet(archiveBaseName, () -> name().toLowerCase(Locale.ENGLISH));
    }

    /**
     * Returns the filename to use for the main jar archive creation.
     * By default, appends the version and the {@code jar} extension to the {@link #archiveBaseName()}.
     *
     * @since 1.5
     */
    public String jarFileName() {
        return Objects.requireNonNullElseGet(jarFileName, () -> archiveBaseName() + "-" + version() + ".jar");
    }

    /**
     * Returns the filename to use for the uber jar archive creation.
     * By default, appends the version, the {@code "-uber"} suffix and the {@code jar} extension to the {@link #archiveBaseName()}.
     *
     * @since 1.5
     */
    public String uberJarFileName() {
        return Objects.requireNonNullElseGet(uberJarFileName, () -> archiveBaseName() + "-" + version() + "-uber.jar");
    }

    /**
     * Returns main class to run the UberJar with.
     * By default, returns the same as {@link #mainClass()}.
     *
     * @since 1.5
     */
    public String uberJarMainClass() {
        return Objects.requireNonNullElseGet(uberJarMainClass, this::mainClass);
    }

    /*
     * File collections
     */

    /**
     * Returns all a list with all the main Java source files.
     *
     * @since 1.5
     */
    public List<File> mainSourceFiles() {
        // get all the main java sources
        var src_main_java_dir_abs = srcMainJavaDirectory().getAbsoluteFile();
        return FileUtils.getFileList(src_main_java_dir_abs, JAVA_FILE_PATTERN, null)
            .stream().map(file -> new File(src_main_java_dir_abs, file)).toList();
    }

    /**
     * Returns all a list with all the test Java source files.
     *
     * @since 1.5
     */
    public List<File> testSourceFiles() {
        // get all the test java sources
        var src_test_java_dir_abs = srcTestJavaDirectory().getAbsoluteFile();
        return FileUtils.getFileList(src_test_java_dir_abs, JAVA_FILE_PATTERN, null)
            .stream().map(file -> new File(src_test_java_dir_abs, file)).toList();
    }

    /*
     * Project classpaths
     */

    /**
     * Returns all the jar files that are in the compile scope classpath.
     * <p>
     * By default, this collects all the jar files in the {@link #libCompileDirectory()}
     * and adds all the jar files from the compile scope local dependencies.
     *
     * @since 1.5
     */
    public List<File> compileClasspathJars() {
        // detect the jar files in the compile lib directory
        var dir_abs = libCompileDirectory().getAbsoluteFile();
        var jar_files = FileUtils.getFileList(dir_abs, JAR_FILE_PATTERN, null);

        // build the compilation classpath
        var classpath = new ArrayList<>(jar_files.stream().map(file -> new File(dir_abs, file)).toList());
        addLocalDependencies(classpath, Scope.compile);
        return classpath;
    }

    /**
     * Returns all the jar files that are in the runtime scope classpath.
     * <p>
     * By default, this collects all the jar files in the {@link #libRuntimeDirectory()}
     * and adds all the jar files from the runtime scope local dependencies.
     *
     * @since 1.5
     */
    public List<File> runtimeClasspathJars() {
        // detect the jar files in the runtime lib directory
        var dir_abs = libRuntimeDirectory().getAbsoluteFile();
        var jar_files = FileUtils.getFileList(dir_abs, JAR_FILE_PATTERN, null);

        // build the runtime classpath
        var classpath = new ArrayList<>(jar_files.stream().map(file -> new File(dir_abs, file)).toList());
        addLocalDependencies(classpath, Scope.runtime);
        return classpath;
    }

    /**
     * Returns all the jar files that are in the standalone scope classpath.
     * <p>
     * By default, this collects all the jar files in the {@link #libStandaloneDirectory()}
     * and adds all the jar files from the standalone scope local dependencies.
     *
     * @since 1.5
     */
    public List<File> standaloneClasspathJars() {
        // build the standalone classpath
        List<File> classpath;
        if (libStandaloneDirectory() == null) {
            classpath = new ArrayList<>();
        } else {
            // detect the jar files in the standalone lib directory
            var dir_abs = libStandaloneDirectory().getAbsoluteFile();
            var jar_files = FileUtils.getFileList(dir_abs, JAR_FILE_PATTERN, null);

            classpath = new ArrayList<>(jar_files.stream().map(file -> new File(dir_abs, file)).toList());
        }
        addLocalDependencies(classpath, Scope.standalone);
        return classpath;
    }

    /**
     * Returns all the jar files that are in the test scope classpath.
     * <p>
     * By default, this collects all the jar files in the {@link #libTestDirectory()}
     * and adds all the jar files from the test scope local dependencies.
     *
     * @since 1.5
     */
    public List<File> testClasspathJars() {
        // detect the jar files in the test lib directory
        var dir_abs = libTestDirectory().getAbsoluteFile();
        var jar_files = FileUtils.getFileList(dir_abs, JAR_FILE_PATTERN, null);

        // build the test classpath
        var classpath = new ArrayList<>(jar_files.stream().map(file -> new File(dir_abs, file)).toList());
        addLocalDependencies(classpath, Scope.test);
        return classpath;
    }

    private void addLocalDependencies(List<File> classpath, Scope scope) {
        if (dependencies.get(scope) == null) {
            return;
        }

        for (var dependency : dependencies.get(scope).localDependencies()) {
            var local_file = new File(workDirectory(), dependency.path());
            if (local_file.exists()) {
                if (local_file.isDirectory()) {
                    var local_jar_files = FileUtils.getFileList(local_file.getAbsoluteFile(), JAR_FILE_PATTERN, null);
                    classpath.addAll(new ArrayList<>(local_jar_files.stream().map(file -> new File(local_file, file)).toList()));
                } else {
                    classpath.add(local_file);
                }
            }
        }
    }

    /**
     * Returns all the classpath entries for compiling the main sources.
     * <p>
     * By default, this converts the files from {@link #compileClasspathJars()} to absolute paths.
     *
     * @since 1.5
     */
    public List<String> compileMainClasspath() {
        return FileUtils.combineToAbsolutePaths(compileClasspathJars());
    }

    /**
     * Returns all the classpath entries for compiling the test sources.
     * <p>
     * By default, this converts the files from {@link #compileClasspathJars()} and
     * {@link #testClasspathJars()} to absolute paths, as well as the {@link #buildMainDirectory()}
     *
     * @since 1.5
     */
    public List<String> compileTestClasspath() {
        var paths = FileUtils.combineToAbsolutePaths(compileClasspathJars(), testClasspathJars());
        paths.add(buildMainDirectory().getAbsolutePath());
        return paths;
    }

    /**
     * Returns all the classpath entries for running the application.
     * <p>
     * By default, this converts the files from {@link #compileClasspathJars()},
     * {@link #runtimeClasspathJars()} and {@link #standaloneClasspathJars()} to absolute paths,
     * as well as the {@link #srcMainResourcesDirectory()} and {@link #buildMainDirectory()}
     *
     * @since 1.5
     */
    public List<String> runClasspath() {
        var paths = FileUtils.combineToAbsolutePaths(compileClasspathJars(), runtimeClasspathJars(), standaloneClasspathJars());
        paths.add(srcMainResourcesDirectory().getAbsolutePath());
        paths.add(buildMainDirectory().getAbsolutePath());
        return paths;
    }

    /**
     * Returns all the classpath entries for testing the application.
     * <p>
     * By default, this converts the files from {@link #compileClasspathJars()},
     * {@link #runtimeClasspathJars()} {@link #standaloneClasspathJars()} and {@link #testClasspathJars()}
     * to absolute paths, as well as the {@link #srcMainResourcesDirectory()},
     * {@link #buildMainDirectory()} and {@link #buildTestDirectory()}
     *
     * @since 1.5
     */
    public List<String> testClasspath() {
        var paths = FileUtils.combineToAbsolutePaths(compileClasspathJars(), runtimeClasspathJars(), standaloneClasspathJars(), testClasspathJars());
        paths.add(srcMainResourcesDirectory().getAbsolutePath());
        paths.add(buildMainDirectory().getAbsolutePath());
        paths.add(buildTestDirectory().getAbsolutePath());
        return paths;
    }
}
