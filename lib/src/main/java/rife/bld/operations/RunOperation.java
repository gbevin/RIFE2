/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.operations;

import rife.bld.Project;
import rife.bld.operations.exceptions.ExitStatusException;
import rife.bld.operations.exceptions.OperationOptionException;
import rife.tools.FileUtils;
import rife.tools.exceptions.FileUtilsErrorException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Runs a Java application.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.5
 */
public class RunOperation {
    public static final String DEFAULT_JAVA_TOOL = "java";

    private File workDirectory_ = new File(System.getProperty("user.dir"));
    private String javaTool_ = DEFAULT_JAVA_TOOL;
    private final List<String> javaOptions_ = new ArrayList<>();
    private final List<String> classpath_ = new ArrayList<>();
    private String mainClass_;
    private Function<String, Boolean> outputProcessor_;
    private Function<String, Boolean> errorProcessor_;
    private Process process_;

    /**
     * Performs the run operation.
     *
     * @throws InterruptedException when the run operation was interrupted
     * @throws IOException when an exception occurred during the execution of the process
     * @throws FileUtilsErrorException when an exception occurred during the retrieval of the run operation output
     * @throws ExitStatusException     when the exit status was changed during the operation
     * @since 1.5
     */
    public void execute()
    throws IOException, FileUtilsErrorException, InterruptedException, ExitStatusException {
        process_ = executeStartProcess();
        int status = process_.waitFor();
        if (!executeHandleProcessOutput(
            FileUtils.readString(process_.getInputStream()),
            FileUtils.readString(process_.getErrorStream()))) {
            status = ExitStatusException.EXIT_FAILURE;
        }
        if (status != 0) {
            throw new ExitStatusException(status);
        }
    }

    /**
     * Part of the {@link #execute} operation, constructs the command list
     * to use for building the process.
     *
     * @since 1.5
     */
    public List<String> executeConstructProcessCommandList() {
        var args = new ArrayList<String>();
        args.add(javaTool());
        args.addAll(javaOptions());
        if (!classpath().isEmpty()) {
            args.add("-cp");
            args.add(FileUtils.joinPaths(classpath()));
        }
        args.add(mainClass());
        return args;
    }

    /**
     * Part of the {@link #execute} operation, starts the process.
     *
     * @since 1.5
     */
    public Process executeStartProcess()
    throws IOException {
        var builder = new ProcessBuilder(executeConstructProcessCommandList());
        builder.directory(workDirectory());
        if (outputProcessor() == null) {
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        } else {
            builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        }
        if (errorProcessor() == null) {
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        } else {
            builder.redirectError(ProcessBuilder.Redirect.PIPE);
        }
        return builder.start();
    }

    /**
     * Part of the {@link #execute} operation, handles providing the
     * output and error data to the configured consumers.
     *
     * @return {@code true} when the process output was valid; or
     * {@code false} when it was erroneous
     * @since 1.5.1
     */
    public boolean executeHandleProcessOutput(String output, String error) {
        boolean result = true;
        if (outputProcessor() != null) {
            result |= outputProcessor().apply(output);
        }
        if (errorProcessor() != null) {
            result |= errorProcessor().apply(error);
        }
        return result;
    }

    /**
     * Configures a run operation from a {@link Project}.
     *
     * @param project the project to configure the run operation from
     * @since 1.5
     */
    public RunOperation fromProject(Project project) {
        return workDirectory(project.workDirectory())
            .javaTool(project.javaTool())
            .javaOptions(project.runJavaOptions())
            .classpath(project.runClasspath())
            .mainClass(project.mainClass());
    }

    /**
     * Provides the work directory in which the run operation will be performed.
     * <p>
     * If no work directory is provided, the JVM working directory will be used.
     *
     * @param directory the directory to use as a work directory
     * @return this operation instance
     * @since 1.5
     */
    public RunOperation workDirectory(File directory) {
        if (!directory.exists()) {
            throw new OperationOptionException("ERROR: The work directory '" + directory + "' doesn't exist.");
        }
        if (!directory.isDirectory()) {
            throw new OperationOptionException("ERROR: '" + directory + "' is not a directory.");
        }
        if (!directory.canWrite()) {
            throw new OperationOptionException("ERROR: The work directory '" + directory + "' is not writable.");
        }

        workDirectory_ = directory;
        return this;
    }

    /**
     * Provides the name of the tool to use for {@code java} execution.
     * <p>
     * If no java tool is provided {@code java} will be used.
     *
     * @param tool the name of the java tool
     * @return this operation instance
     * @since 1.5
     */
    public RunOperation javaTool(String tool) {
        javaTool_ = tool;
        return this;
    }

    /**
     * Provides the options to provide to the java tool.
     *
     * @param options the java tool's options
     * @return this operation instance
     * @since 1.5
     */
    public RunOperation javaOptions(List<String> options) {
        javaOptions_.addAll(options);
        return this;
    }

    /**
     * Provides the classpath to use for the run operation.
     *
     * @param classpath the run operation's classpath
     * @return this operation instance
     * @since 1.5
     */
    public RunOperation classpath(List<String> classpath) {
        classpath_.addAll(classpath);
        return this;
    }

    /**
     * Provides the main class to run with the java tool.
     *
     * @param name the main class to run
     * @return this operation instance
     * @since 1.5
     */
    public RunOperation mainClass(String name) {
        mainClass_ = name;
        return this;
    }

    /**
     * Provides the processor that will be used to handle the process output.
     *
     * @param processor the output processor
     * @return this operation instance
     * @since 1.5.1
     */
    public RunOperation outputProcessor(Function<String, Boolean> processor) {
        outputProcessor_ = processor;
        return this;
    }

    /**
     * Provides the processor that will be used to handle the process errors.
     *
     * @param processor the error processor
     * @return this operation instance
     * @since 1.5.1
     */
    public RunOperation errorProcessor(Function<String, Boolean> processor) {
        errorProcessor_ = processor;
        return this;
    }

    /**
     * Retrieves the work directory in which the run operation will be performed.
     *
     * @return the directory to use as a work directory
     * @since 1.5
     */
    public File workDirectory() {
        return workDirectory_;
    }

    /**
     * retrieves the name of the tool to use for {@code java} execution.
     *
     * @return the name of the java tool
     * @since 1.5
     */
    public String javaTool() {
        return javaTool_;
    }

    /**
     * Retrieves the options to provide to the java tool.
     * <p>
     * This is a modifiable list that can be retrieved and changed.
     *
     * @return the java tool's options
     * @since 1.5
     */
    public List<String> javaOptions() {
        return javaOptions_;
    }

    /**
     * Retrieves the classpath to use for the run operation.
     * <p>
     * This is a modifiable list that can be retrieved and changed.
     *
     * @return the run operation's classpath
     * @since 1.5
     */
    public List<String> classpath() {
        return classpath_;
    }

    /**
     * Retrieves the main class to run with the java tool.
     *
     * @return the main class to run
     * @since 1.5
     */
    public String mainClass() {
        return mainClass_;
    }

    /**
     * Retrieves the processor that is used to handle the process output.
     *
     * @return the output processor
     * @since 1.5.1
     */
    public Function<String, Boolean> outputProcessor() {
        return outputProcessor_;
    }

    /**
     * Retrieves the processor that is used to handle the process errors.
     *
     * @return the error processor
     * @since 1.5.1
     */
    public Function<String, Boolean> errorProcessor() {
        return errorProcessor_;
    }

    /**
     * Retrieves the process that was used for the execution.
     *
     * @return the process that was executed
     * @since 1.5
     */
    public Process process() {
        return process_;
    }
}
