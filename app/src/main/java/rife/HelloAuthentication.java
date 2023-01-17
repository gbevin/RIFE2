/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife;

import rife.authentication.credentialsmanagers.RoleUserAttributes;
import rife.authentication.elements.*;
import rife.authentication.sessionvalidators.MemorySessionValidator;
import rife.engine.*;
import rife.template.TemplateFactory;

public class HelloAuthentication extends Site {
    final MemorySessionValidator validator = new MemorySessionValidator();
    final AuthConfig config = new AuthConfig(validator);

    Route login;
    Route landing;
    Route logout;

    public void setup() {
        login = getPost("/login", new Login(config, TemplateFactory.HTML.get("HelloLogin")));
        group(new Router() {
            public void setup() {
                before(new Authenticated(config));

                landing = get("/authentication", c -> {
                    var t = c.template("HelloAuthenticated");
                    t.setValue("user", config.identityAttribute(c).getLogin());
                    c.print(t);
                });
                logout = get("/logout", new Logout(config, TemplateFactory.HTML.get("HelloLogout")));
            }
        });

        config
            .loginRoute(login)
            .landingRoute(landing);
        validator.getCredentialsManager()
            .addUser("testUser", new RoleUserAttributes().password("testPassword"));
    }

    public static void main(String[] args) {
        new Server().start(new HelloAuthentication());
    }
}