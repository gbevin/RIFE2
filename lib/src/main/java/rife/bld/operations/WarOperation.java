/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.operations;

import rife.bld.*;
import rife.tools.FileUtils;
import rife.tools.StringUtils;
import rife.tools.exceptions.FileUtilsErrorException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WarOperation {
    public static class Help implements BuildHelp {
        public String getDescription() {
            return "Creates a war archive for a RIFE2 application";
        }

        public String getHelp(String topic) {
            return StringUtils.replace("""
                Creates a war archive for a RIFE2 application.
                The standard war command will automatically also execute
                the jar command beforehand.
                            
                Usage : ${topic}""", "${topic}", topic);
        }
    }

    private List<File> libSourceDirectories_ = new ArrayList<>();
    private List<File> classesSourceDirectories_ = new ArrayList<>();
    private List<NamedFile> jarSourceFiles_ = new ArrayList<>();
    private File webappDirectory_;
    private File webXmlFile_;
    private File destinationDirectory_;
    private String destinationFileName_;

    public WarOperation() {
    }

    public void execute()
    throws Exception {
        var tmp_dir = Files.createTempDirectory("war").toFile();

        try {
            var web_inf_dir = createWebInfDirectory(tmp_dir);
            copyWebappDirectory(tmp_dir);
            copyWebInfLibJars(web_inf_dir);
            copyWebInfClassesFiles(web_inf_dir);
            copyWebXmlFile(web_inf_dir);

            createWarArchive(tmp_dir);
        } finally {
            FileUtils.deleteDirectory(tmp_dir);
        }
    }

    public File createWebInfDirectory(File destination) {
        var web_inf_dir = new File(destination, "WEB-INF");
        web_inf_dir.mkdirs();
        return web_inf_dir;
    }

    public void copyWebappDirectory(File destination)
    throws FileUtilsErrorException {
        FileUtils.copyDirectory(webappDirectory(), destination);
    }

    public void copyWebInfLibJars(File webInfDirectory)
    throws FileUtilsErrorException {
        var web_inf_lib_dir = new File(webInfDirectory, "lib");
        if (!libSourceDirectories().isEmpty()) {
            web_inf_lib_dir.mkdirs();
            for (var dir : libSourceDirectories()) {
                FileUtils.copyDirectory(dir, web_inf_lib_dir);
            }
        }

        if (!jarSourceFiles().isEmpty()) {
            web_inf_lib_dir.mkdirs();
            for (var file : jarSourceFiles()) {
                FileUtils.copy(file.file(), new File(web_inf_lib_dir, file.name()));
            }
        }
    }

    public void copyWebInfClassesFiles(File webInfDirectory)
    throws FileUtilsErrorException {
        var web_inf_classes_dir = new File(webInfDirectory, "classes");
        if (!classesSourceDirectories().isEmpty()) {
            web_inf_classes_dir.mkdirs();
            for (var dir : classesSourceDirectories()) {
                FileUtils.copyDirectory(dir, web_inf_classes_dir);
            }
        }
    }

    public void copyWebXmlFile(File webInfDirectory)
    throws FileUtilsErrorException {
        var web_xml_file = webXmlFile();
        if (web_xml_file != null) {
            FileUtils.copy(web_xml_file, new File(webInfDirectory, "web.xml"));
        }
    }

    public void createWarArchive(File stagingDirectory)
    throws Exception {
        new JarOperation()
            .sourceDirectories(List.of(stagingDirectory))
            .destinationDirectory(destinationDirectory())
            .destinationFileName(destinationFileName())
            .execute();
    }

    public WarOperation fromProject(Project project) {
        return libSourceDirectories(List.of(project.libCompileDirectory(), project.libRuntimeDirectory()))
            .jarSourceFiles(List.of(new NamedFile(project.jarFileName(), new File(project.buildDistDirectory(), project.jarFileName()))))
            .webappDirectory(project.srcMainWebappDirectory())
            .destinationDirectory(project.buildDistDirectory())
            .destinationFileName(project.warFileName());
    }

    public WarOperation libSourceDirectories(List<File> sources) {
        libSourceDirectories_ = new ArrayList<>(sources);
        return this;
    }

    public List<File> libSourceDirectories() {
        return libSourceDirectories_;
    }

    public WarOperation classesSourceDirectories(List<File> sources) {
        classesSourceDirectories_ = new ArrayList<>(sources);
        return this;
    }

    public List<File> classesSourceDirectories() {
        return classesSourceDirectories_;
    }

    public WarOperation jarSourceFiles(List<NamedFile> files) {
        jarSourceFiles_ = new ArrayList<>(files);
        return this;
    }

    public List<NamedFile> jarSourceFiles() {
        return jarSourceFiles_;
    }

    public WarOperation webappDirectory(File directory) {
        webappDirectory_ = directory;
        return this;
    }

    public File webappDirectory() {
        return webappDirectory_;
    }

    public WarOperation webXmlFile(File directory) {
        webXmlFile_ = directory;
        return this;
    }

    public File webXmlFile() {
        return webXmlFile_;
    }

    public WarOperation destinationDirectory(File directory) {
        destinationDirectory_ = directory;
        return this;
    }

    public File destinationDirectory() {
        return destinationDirectory_;
    }

    public WarOperation destinationFileName(String name) {
        destinationFileName_ = name;
        return this;
    }

    public String destinationFileName() {
        return destinationFileName_;
    }
}
