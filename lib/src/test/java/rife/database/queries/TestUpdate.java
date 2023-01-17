/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.database.queries;

import rife.database.BeanImpl;
import rife.database.Datasource;
import rife.database.DbPreparedStatementHandler;
import rife.database.DbQueryManager;
import rife.database.exceptions.DatabaseException;

public abstract class TestUpdate extends TestQuery {
    public DbQueryManager setupQuery(Datasource datasource) {
        var manager = new DbQueryManager(datasource);

        var createtable = new CreateTable(datasource);
        createtable.table("tablename")
            .columns(BeanImpl.class)
            .column("nullColumn", String.class)
            .precision("propertyBigDecimal", 18, 9)
            .precision("propertyChar", 1)
            .precision("propertyDouble", 12, 3)
            .precision("propertyFloat", 13, 2)
            .precision("propertyString", 255)
            .precision("propertyStringBuffer", 100)
            .precision("nullColumn", 255);

        try {
            // prepare table and data
            manager.executeUpdate(createtable);

            createtable.table("table2");
            manager.executeUpdate(createtable);

            var insert = new Insert(datasource);
            insert.into("tablename")
                .fields(BeanImpl.getPopulatedBean());
            manager.executeUpdate(insert);

            insert.into("table2");
            manager.executeUpdate(insert);
            insert.clear();

            insert.into("tablename")
                .fields(BeanImpl.getNullBean());
            manager.executeUpdate(insert);

            insert.into("table2");
            manager.executeUpdate(insert);
        } catch (DatabaseException e) {
            cleanupQuery(manager);
            throw new RuntimeException(e);
        }

        return manager;
    }

    private void cleanupQuery(DbQueryManager manager) {
        // clean up nicely
        var drop_table = new DropTable(manager.getDatasource());
        try {
            drop_table.table("tablename");
            manager.executeUpdate(drop_table);

            drop_table.clear();
            drop_table.table("table2");
            manager.executeUpdate(drop_table);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public boolean execute(Update query) {
        var success = false;
        var manager = setupQuery(query.getDatasource());

        try {
            // try to execute insert statement
            if (manager.executeUpdate(query) > 0) {
                success = true;
            }
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            cleanupQuery(manager);
        }

        return success;
    }

    public boolean execute(Update query, DbPreparedStatementHandler handler) {
        var success = false;
        var manager = setupQuery(query.getDatasource());

        try {
            if (manager.executeUpdate(query, handler) > 0) {
                success = true;
            }
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        } finally {
            cleanupQuery(manager);
        }

        return success;
    }
}
