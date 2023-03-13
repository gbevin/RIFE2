/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.template.exceptions;

import java.io.Serial;

public class CircularConstructionException extends ProcessingException {
    @Serial private static final long serialVersionUID = 5707444018880036132L;

    public CircularConstructionException() {
        super("The value constructions reference themselves in a circular way. This is not allowed since it would cause a stack overflow.");
    }
}
