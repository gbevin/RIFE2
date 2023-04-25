/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife;

import rife.database.*;
import rife.database.querymanagers.generic.GenericQueryManager;
import rife.database.querymanagers.generic.GenericQueryManagerFactory;
import rife.engine.*;
import rife.models.Person;

public class HelloGenericQueryManager extends Site {
    Datasource datasource = new Datasource(
        "org.h2.Driver", "jdbc:h2:./embedded_dbs/h2/hello", "sa", "", 5);
    GenericQueryManager<Person> manager =
        GenericQueryManagerFactory.instance(datasource, Person.class);

    Route install = get("/install", c -> {
        manager.install();
        c.print("Installed");
    });
    Route remove = get("/remove", c -> {
        manager.remove();
        c.print("Removed");
    });
    Route addForm = get("/add", c -> c.print("""
        <form method='post'>
        <input name='name' /><input type='submit' />
        </form>""")
    );
    Route list = get("/list", c -> {
        manager.restore(manager.getRestoreQuery().orderBy("name"),
            person -> c.print(person.getName() + "<br>"));
        c.print("<br><a href='" + c.urlFor(addForm) + "'>Add more</a><br>");
    });
    Route add = post("/add", c -> {
        var person = c.parametersBean(Person.class);
        manager.save(person);
        c.print("Added " + person.getName() + "<br><br>");
        c.print("<a href='" + c.urlFor(addForm) + "'>Add more</a><br>");
        c.print("<a href='" + c.urlFor(list) + "'>List names</a><br>");
    });

    public static void main(String[] args) {
        new Server().start(new HelloGenericQueryManager());
    }
}