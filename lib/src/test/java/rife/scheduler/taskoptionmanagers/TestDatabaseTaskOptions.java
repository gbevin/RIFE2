/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.scheduler.taskoptionmanagers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import rife.database.Datasource;
import rife.database.TestDatasources;
import rife.scheduler.*;
import rife.scheduler.exceptions.SchedulerException;
import rife.scheduler.exceptions.SchedulerManagerException;
import rife.scheduler.exceptions.TaskOptionManagerException;
import rife.scheduler.schedulermanagers.DatabaseSchedulingFactory;
import rife.scheduler.taskoptionmanagers.exceptions.InexistentTaskIdException;
import rife.tools.ExceptionUtils;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabaseTaskOptions {
    public void setup(Datasource datasource) {
        var manager = DatabaseSchedulingFactory.instance(datasource);

        try {
            assertTrue(manager.install());
        } catch (SchedulerManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    public void tearDown(Datasource datasource) {
        var manager = DatabaseSchedulingFactory.instance(datasource);

        try {
            assertTrue(manager.remove());
        } catch (SchedulerManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testInstantiateTaskOptionManager(Datasource datasource) {
        setup(datasource);
        try {
            TaskOptionManager manager = DatabaseTaskOptionsFactory.instance(datasource);
            assertNotNull(manager);
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testAddTaskOptionWithInexistentTaskId(Datasource datasource) {
        setup(datasource);
        try {
            var taskoption = new TaskOption();
            taskoption.setTaskId(0);
            taskoption.setName("name");
            taskoption.setValue("value");

            TaskOptionManager manager = DatabaseTaskOptionsFactory.instance(datasource);
            try {
                manager.addTaskOption(taskoption);
                fail();
            } catch (InexistentTaskIdException e) {
                assertTrue(true);
            } catch (TaskOptionManagerException e) {
                fail(ExceptionUtils.getExceptionStackTrace(e));
            }
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testAddTaskOption(Datasource datasource) {
        setup(datasource);

        var task_id = 0;
        var task = new Task();
        var task_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskManager();
        var taskoption_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        try {
            var cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime());
            task.setFrequency(Frequency.MINUTELY);

            task_id = task_manager.addTask(task);
            assertTrue(task_id >= 0);

            var taskoption_name = "name";
            var value = "value";

            var taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.addTaskOption(taskoption));
        } catch (SchedulerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testAddDuplicateTaskOption(Datasource datasource) {
        setup(datasource);

        var task_id = 0;
        var task = new Task();
        var task_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskManager();
        var taskoption_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        try {
            var cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime());
            task.setFrequency(Frequency.MINUTELY);

            task_id = task_manager.addTask(task);
            assertTrue(task_id >= 0);

            var taskoption_name = "name";
            var value = "value";

            var taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.addTaskOption(taskoption));

            taskoption_manager.addTaskOption(taskoption);
            fail();
        } catch (SchedulerException e) {
            assertTrue(true);
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetTaskOption(Datasource datasource) {
        setup(datasource);

        var task_id = 0;
        var task = new Task();
        var task_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskManager();
        var taskoption_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        try {
            var cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setFrequency(Frequency.MINUTELY);

            task_id = task_manager.addTask(task);
            assertTrue(task_id >= 0);

            var taskoption_name = "name";
            var value = "value";

            var taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.addTaskOption(taskoption));

            taskoption = taskoption_manager.getTaskOption(task_id, taskoption_name);
            assertNotNull(taskoption);

            assertEquals(taskoption.getTaskId(), task_id);
            assertEquals(taskoption.getName(), taskoption_name);
            assertEquals(taskoption.getValue(), value);

            task = task_manager.getTask(task_id);
            assertEquals(task.getTaskOptionValue(taskoption_name), value);
        } catch (SchedulerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testUpdateTaskOption(Datasource datasource) {
        setup(datasource);

        var task_id = 0;
        var task = new Task();
        var task_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskManager();
        var taskoption_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        try {
            var cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime());
            task.setFrequency(Frequency.MINUTELY);

            task_id = task_manager.addTask(task);
            assertTrue(task_id >= 0);

            var taskoption_name = "name";
            var value = "value";

            var taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.addTaskOption(taskoption));

            value = "new_value";

            taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.updateTaskOption(taskoption));

            taskoption = taskoption_manager.getTaskOption(task_id, taskoption_name);
            assertNotNull(taskoption);

            assertEquals(taskoption.getTaskId(), task_id);
            assertEquals(taskoption.getName(), taskoption_name);
            assertEquals(taskoption.getValue(), value);
        } catch (SchedulerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetTaskOptions(Datasource datasource) {
        setup(datasource);

        var task_id = 0;
        var task = new Task();
        var task_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskManager();
        var taskoption_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        try {
            var cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime());
            task.setFrequency(Frequency.MINUTELY);

            task_id = task_manager.addTask(task);
            assertTrue(task_id >= 0);

            var taskoption_name = "name";
            var value = "some_value";

            var taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.addTaskOption(taskoption));

            var taskoptions = taskoption_manager.getTaskOptions(task_id);
            assertEquals(1, taskoptions.size());

            taskoption = taskoptions.iterator().next();
            assertEquals(taskoption.getTaskId(), task_id);
            assertEquals(taskoption.getName(), taskoption_name);
            assertEquals(taskoption.getValue(), "some_value");
        } catch (SchedulerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testRemoveTaskOption(Datasource datasource) {
        setup(datasource);

        var task_id = 0;
        var task = new Task();
        var task_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskManager();
        var taskoption_manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        try {
            var cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime());
            task.setFrequency(Frequency.MINUTELY);

            task_id = task_manager.addTask(task);
            assertTrue(task_id >= 0);

            var taskoption_name = "name";
            var value = "value";

            var taskoption = new TaskOption();
            taskoption.setTaskId(task_id);
            taskoption.setName(taskoption_name);
            taskoption.setValue(value);

            assertTrue(taskoption_manager.addTaskOption(taskoption));

            assertTrue(taskoption_manager.removeTaskOption(task_id, taskoption_name));

            assertTrue(task_manager.removeTask(task_id));
            task_id = 0;
            taskoption_name = null;
        } catch (SchedulerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetNonExistingTaskOption(Datasource datasource) {
        setup(datasource);

        var manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
        var task_nonexisting_id = 340;
        try {
            assertNull(manager.getTaskOption(task_nonexisting_id, "unknownname"));
        } catch (TaskOptionManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testRemoveNonExistingTaskOption(Datasource datasource) {
        setup(datasource);

        try {
            var manager = DatabaseSchedulingFactory.instance(datasource).getScheduler().getTaskOptionManager();
            var task_nonexisting_id = 120;
            try {
                assertFalse(manager.removeTaskOption(task_nonexisting_id, "unknownname"));
            } catch (TaskOptionManagerException e) {
                fail(ExceptionUtils.getExceptionStackTrace(e));
            }
        } finally {
            tearDown(datasource);
        }
    }
}
