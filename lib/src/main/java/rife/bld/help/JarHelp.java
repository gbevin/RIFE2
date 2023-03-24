/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.help;

import rife.bld.CommandHelp;
import rife.tools.StringUtils;

/**
 * Provides help for the jar command.
 *
 * @since 1.5
 */
public class JarHelp implements CommandHelp {
    public String getSummary() {
        return "Creates a jar archive for the project";
    }

    public String getDescription(String topic) {
        return StringUtils.replace("""
            Creates a jar archive for the project.
            The standard jar command will automatically also execute
            the compile and precompile commands beforehand.
                        
            Usage : ${topic}""", "${topic}", topic);
    }
}
