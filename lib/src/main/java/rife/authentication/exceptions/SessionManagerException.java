/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.authentication.exceptions;

import java.io.Serial;

public class SessionManagerException extends RuntimeException {
    @Serial private static final long serialVersionUID = 4691297582200595999L;

    public SessionManagerException(String message) {
        super(message);
    }

    public SessionManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionManagerException(Throwable cause) {
        super(cause);
    }
}
