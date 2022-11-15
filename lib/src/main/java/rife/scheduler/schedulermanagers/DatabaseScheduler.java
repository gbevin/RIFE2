/*
 * Copyright 2001-2022 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.scheduler.schedulermanagers;

import rife.database.Datasource;
import rife.database.DbQueryManager;
import rife.scheduler.Scheduler;
import rife.scheduler.SchedulerFactory;
import rife.scheduler.exceptions.SchedulerException;
import rife.scheduler.exceptions.SchedulerManagerException;
import rife.scheduler.schedulermanagers.exceptions.InstallSchedulerErrorException;
import rife.scheduler.schedulermanagers.exceptions.RemoveSchedulerErrorException;
import rife.scheduler.taskmanagers.DatabaseTasks;
import rife.scheduler.taskmanagers.DatabaseTasksFactory;
import rife.scheduler.taskoptionmanagers.DatabaseTaskOptions;
import rife.scheduler.taskoptionmanagers.DatabaseTaskOptionsFactory;

public abstract class DatabaseScheduler extends DbQueryManager implements SchedulerFactory {
    protected DatabaseScheduler(Datasource datasource) {
        super(datasource);
    }

    public Scheduler getScheduler() {
        return new Scheduler(DatabaseTasksFactory.getInstance(getDatasource()), DatabaseTaskOptionsFactory.getInstance(getDatasource()));
    }

    public abstract boolean install()
    throws SchedulerManagerException;

    public abstract boolean remove()
    throws SchedulerManagerException;

    protected boolean install_()
    throws SchedulerManagerException {
        try {
            DatabaseTasks tasks_manager = DatabaseTasksFactory.getInstance(getDatasource());
            DatabaseTaskOptions taskoptions_manager = DatabaseTaskOptionsFactory.getInstance(getDatasource());

            tasks_manager.install();
            taskoptions_manager.install();
        } catch (SchedulerException e) {
            throw new InstallSchedulerErrorException(e);
        }

        return true;
    }

    protected boolean remove_()
    throws SchedulerManagerException {
        try {
            DatabaseTasks tasks_manager = DatabaseTasksFactory.getInstance(getDatasource());
            DatabaseTaskOptions taskoptions_manager = DatabaseTaskOptionsFactory.getInstance(getDatasource());

            taskoptions_manager.remove();
            tasks_manager.remove();
        } catch (SchedulerException e) {
            throw new RemoveSchedulerErrorException(e);
        }

        return true;
    }
}
