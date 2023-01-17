/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.database.exceptions;

import rife.database.DbPreparedStatement;

import java.io.Serial;

public class NoParametrizedQueryException extends DatabaseException {
    @Serial private static final long serialVersionUID = -1606716036753773612L;

    private final DbPreparedStatement preparedStatement_;

    public NoParametrizedQueryException(DbPreparedStatement statement) {
        super("The statement with sql '" + statement.getSql() + "' doesn't contain a parametrized query.");
        preparedStatement_ = statement;
    }

    public DbPreparedStatement getPreparedStatement() {
        return preparedStatement_;
    }
}
