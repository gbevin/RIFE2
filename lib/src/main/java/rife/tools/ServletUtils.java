/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.tools;

import rife.engine.Response;

public final class ServletUtils {
    private ServletUtils() {
        // no-op
    }

    public static void preventCaching(Response response) {
        response.addHeader("Cache-Control", "max-age=0");           // HTTP/1.0
        response.addHeader("Cache-Control", "no-cache");            // HTTP/1.1
        response.addHeader("Cache-Control", "no-store");            // HTTP/1.1
        response.addHeader("Cache-Control", "must-revalidate");     // HTTP/1.1
        response.addHeader("Pragma", "no-cache");                   // HTTP 1.0
        response.addHeader("Expires", "1");
    }
}

