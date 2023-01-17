/*
 * Copyright 2001-2023 Steven Grimm (koreth[remove] at midwinter dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.database;

import rife.database.exceptions.DbQueryException;
import rife.scheduler.Executor;
import rife.scheduler.Task;

/**
 * Periodic probe job to keep connections non-idle and probe for dead ones.
 * This is primarily useful for MySQL, which closes connections after a
 * period of inactivity.
 *
 * <p>There are two optional parameters.
 * <dl>
 * <dt>{@code datasource}</dt>
 * <dd>The name of the Datasource to probe. If not specified, the
 * default is "datasource".</dd>
 * <dt>{@code query}</dt>
 * <dd>The dummy query to send. If not specified, the default is
 * "select 1".</dd>
 * </dl>
 *
 * @author Steven Grimm (koreth[remove] at midwinter dot com)
 * @since 1.0
 */
public class DbProbeExecutor extends Executor {
    @Override
    public boolean executeTask(Task task) {
        try {
            var ds_name = task.getTaskOptionValue("datasource");
            if (null == ds_name) {
                ds_name = "datasource";
            }

            var query = task.getTaskOptionValue("query");
            if (null == query) {
                query = "select 1";
            }

            var ds = Datasources.instance().getDatasource(ds_name);
            if (null == ds) {
                throw new DbQueryException("Can't find Datasource '" + ds_name + "'");
            }

            var cp = ds.getPool();
            if (null == cp) {
                throw new DbQueryException("Datasource '" + ds_name + "' has no ConnectionPool");
            }

            /*
             * Now fetch all the connections that should be in the pool,
             * and run a dummy statement on each of them to keep it from
             * going idle.
             *
             * This relies on the fact that ConnectionPool returns
             * DbConnection objects in a round-robin fashion. We can just
             * fetch the next connection the appropriate number of times
             * and be guaranteed to hit all of them.
             *
             * If there are transactions active on other threads, we will
             * not be given those threads' DbConnection objects, so we
             * might end up being handed the same connection twice. No
             * harm in that, and any connection that has an active
             * transaction isn't idle anyway so doesn't need to be probed.
             */
            synchronized (cp) {
                for (var i = 0; i < cp.getPoolSize(); i++) {
                    var conn = ds.getConnection();
                    if (null == conn) {
                        throw new DbQueryException("Can't get connection");
                    }

                    var stmt = conn.getPreparedStatement(query);
                    if (null == stmt) {
                        throw new DbQueryException("Can't prepare dummy statement");
                    }

                    try {
                        /*
                         * Probe the connection. If this fails, RIFE will remove
                         * the connection from the pool automatically.
                         */
                        stmt.executeQuery();
                    } finally {
                        stmt.close();
                        conn.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new DbQueryException("Can't probe MySQL connection", e);
        }

        return true;
    }

    @Override
    public String getHandledTaskType() {
        return "DbProbe";
    }
}
