/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.operations.exceptions;

import java.io.Serial;

/**
 * When thrown, indicates that the exit status has changed due
 * to the operation execution.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.5
 */
public class ExitStatusException extends Exception {
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;

    @Serial private static final long serialVersionUID = 5474282790384325513L;

    private final int exitStatus_;

    public ExitStatusException(int exitStatus) {
        exitStatus_ = exitStatus;
    }

    public int getExitStatus() {
        return exitStatus_;
    }
}