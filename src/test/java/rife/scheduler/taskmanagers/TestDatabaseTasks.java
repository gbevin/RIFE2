/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.scheduler.taskmanagers;

import java.util.Calendar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import rife.database.Datasource;
import rife.database.TestDatasources;
import rife.scheduler.*;
import rife.scheduler.exceptions.FrequencyException;
import rife.scheduler.exceptions.SchedulerManagerException;
import rife.scheduler.exceptions.TaskManagerException;
import rife.scheduler.schedulermanagers.DatabaseSchedulingFactory;
import rife.tools.ExceptionUtils;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabaseTasks {
    public void setup(Datasource datasource) {
        var scheduler_manager = DatabaseSchedulingFactory.instance(datasource);
        try {
            scheduler_manager.install();
        } catch (SchedulerManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    public void tearDown(Datasource datasource) {
        var scheduler_manager = DatabaseSchedulingFactory.instance(datasource);
        try {
            scheduler_manager.remove();
        } catch (SchedulerManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testInstantiateTaskManager(Datasource datasource) {
        var manager = DatabaseTasksFactory.instance(datasource);
        assertNotNull(manager);
    }

    public void testAddTask(Datasource datasource) {
        setup(datasource);

        var task_id = -1;

        var type = TestTasktypes.UPLOAD_GROUPS;
        var cal = Calendar.getInstance();
        cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);
        var planned = cal.getTime().getTime();
        var frequency = Frequency.MINUTELY;
        var busy = false;

        var task = new Task();
        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(frequency);
            task.setBusy(busy);

            task_id = manager.addTask(task);
            assertTrue(task_id >= 0);
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetTask(Datasource datasource) {
        setup(datasource);

        var task_id = -1;

        var type = TestTasktypes.UPLOAD_GROUPS;
        var cal = Calendar.getInstance();
        cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);
        var planned = cal.getTime().getTime();
        var frequency = Frequency.MINUTELY;
        var busy = false;

        var task = new Task();
        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(frequency);
            task.setBusy(busy);

            task_id = manager.addTask(task);
            task = manager.getTask(task_id);
            assertNotNull(task);

            assertEquals(task.getId(), task_id);
            assertEquals(task.getType(), TestTasktypes.UPLOAD_GROUPS);
            assertTrue(task.getPlanned() <= cal.getTime().getTime());
            assertEquals(task.getFrequency().toString(), "* * * * *");
            assertFalse(task.isBusy());
            assertSame(task.getTaskManager(), manager);
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testUpdateTask(Datasource datasource) {
        setup(datasource);

        var task_id = -1;

        var type = TestTasktypes.UPLOAD_GROUPS;
        var cal = Calendar.getInstance();
        cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);
        var planned = cal.getTime().getTime();
        var busy = false;

        var task = new Task();
        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(Frequency.MINUTELY);
            task.setBusy(busy);

            task_id = manager.addTask(task);

            type = TestTasktypes.SEND_RANKING;
            cal.set(2002, Calendar.MARCH, 12, 0, 0, 0);
            planned = cal.getTime().getTime();
            busy = true;

            task = new Task();
            task.setId(task_id);
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(new Frequency().everyMinute(10));
            task.setBusy(busy);

            assertTrue(manager.updateTask(task));

            task = manager.getTask(task_id);
            assertNotNull(task);

            assertEquals(task.getId(), task_id);
            assertEquals(task.getType(), type);
            assertTrue(task.getPlanned() <= planned);
            assertEquals(task.getFrequency().toString(), "*/10 * * * *");
            assertEquals(task.isBusy(), busy);
            assertSame(task.getTaskManager(), manager);
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testRemoveTask(Datasource datasource) {
        setup(datasource);

        var task_id = -1;

        var type = TestTasktypes.UPLOAD_GROUPS;
        var cal = Calendar.getInstance();
        cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);
        var planned = cal.getTime().getTime();
        var frequency = Frequency.MINUTELY;
        var busy = false;

        var task = new Task();
        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(frequency);
            task.setBusy(busy);

            task_id = manager.addTask(task);
            assertTrue(manager.removeTask(task_id));
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetNonExistingTask(Datasource datasource) {
        setup(datasource);

        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        var task_nonexisting_id = 0;
        try {
            assertNull(manager.getTask(task_nonexisting_id));
        } catch (TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testRemoveNonExistingTask(Datasource datasource) {
        setup(datasource);

        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        var task_nonexisting_id = 0;
        try {
            assertFalse(manager.removeTask(task_nonexisting_id));
        } catch (TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetTasksToProcess(Datasource datasource) {
        setup(datasource);

        var one_hour = 1000 * 60 * 60;

        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            var task1 = new Task();
            task1.setType(TestTasktypes.UPLOAD_GROUPS);
            task1.setPlanned(System.currentTimeMillis() - one_hour);
            task1.setFrequency(null);
            task1.setBusy(false);

            var task2 = new Task();
            task2.setType(TestTasktypes.UPLOAD_GROUPS);
            task2.setPlanned(System.currentTimeMillis() - one_hour);
            task2.setFrequency(null);
            task2.setBusy(false);

            var task3 = new Task();
            task3.setType(TestTasktypes.UPLOAD_GROUPS);
            task3.setPlanned(System.currentTimeMillis() - one_hour);
            task3.setFrequency(null);
            task3.setBusy(true);

            var task4 = new Task();
            task4.setType(TestTasktypes.UPLOAD_GROUPS);
            task4.setPlanned(System.currentTimeMillis() + one_hour);
            task4.setFrequency(null);
            task4.setBusy(false);

            task1.setId(manager.addTask(task1));
            task2.setId(manager.addTask(task2));
            task3.setId(manager.addTask(task3));
            task4.setId(manager.addTask(task4));

            var tasks_to_process = manager.getTasksToProcess();

            manager.removeTask(task1.getId());
            manager.removeTask(task2.getId());
            manager.removeTask(task3.getId());
            manager.removeTask(task4.getId());

            assertEquals(2, tasks_to_process.size());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testGetScheduledAnAllTasks(Datasource datasource) {
        setup(datasource);

        var one_hour = 1000 * 60 * 60;

        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            var task1 = new Task();
            task1.setType(TestTasktypes.UPLOAD_GROUPS);
            task1.setPlanned(System.currentTimeMillis() - one_hour);
            task1.setFrequency(null);
            task1.setBusy(false);

            var task2 = new Task();
            task2.setType(TestTasktypes.UPLOAD_GROUPS);
            task2.setPlanned(System.currentTimeMillis() + one_hour);
            task2.setFrequency(null);
            task2.setBusy(true);

            var task3 = new Task();
            task3.setType(TestTasktypes.UPLOAD_GROUPS);
            task3.setPlanned(System.currentTimeMillis() + one_hour);
            task3.setFrequency(null);
            task3.setBusy(false);

            var task4 = new Task();
            task4.setType(TestTasktypes.UPLOAD_GROUPS);
            task4.setPlanned(System.currentTimeMillis() + one_hour);
            task4.setFrequency(null);
            task4.setBusy(false);

            task1.setId(manager.addTask(task1));
            task2.setId(manager.addTask(task2));
            task3.setId(manager.addTask(task3));
            task4.setId(manager.addTask(task4));

            var scheduled_tasks = manager.getScheduledTasks();
            var all_tasks = manager.getAllTasks();

            manager.removeTask(task1.getId());
            manager.removeTask(task2.getId());
            manager.removeTask(task3.getId());
            manager.removeTask(task4.getId());

            assertEquals(2, scheduled_tasks.size());
            assertEquals(4, all_tasks.size());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testTaskConclusion(Datasource datasource) {
        setup(datasource);

        var one_hour = 1000 * 60 * 60;

        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            var task1 = new Task();
            task1.setType(TestTasktypes.UPLOAD_GROUPS);
            task1.setPlanned(System.currentTimeMillis() - one_hour);
            task1.setFrequency(null);
            task1.setBusy(false);

            var task2 = new Task();
            task2.setType(TestTasktypes.UPLOAD_GROUPS);
            task2.setPlanned(System.currentTimeMillis() - one_hour);
            task2.setFrequency(Frequency.MINUTELY);
            task2.setBusy(false);

            var task3 = new Task();
            task3.setType(TestTasktypes.UPLOAD_GROUPS);
            task3.setPlanned(System.currentTimeMillis() + one_hour);
            task3.setFrequency(null);
            task3.setBusy(false);

            task1.setId(manager.addTask(task1));
            task2.setId(manager.addTask(task2));
            task3.setId(manager.addTask(task3));
            task1 = manager.getTask(task1.getId());
            task2 = manager.getTask(task2.getId());
            task3 = manager.getTask(task3.getId());

            var was_task1_concluded = manager.concludeTask(task1);
            var was_task2_concluded = manager.concludeTask(task2);
            var was_task3_concluded = manager.concludeTask(task3);

            var task1_new = manager.getTask(task1.getId());
            var task2_new = manager.getTask(task2.getId());
            var task3_new = manager.getTask(task3.getId());

            manager.removeTask(task2.getId());
            manager.removeTask(task3.getId());

            assertTrue(was_task1_concluded);
            assertTrue(was_task2_concluded);
            assertFalse(was_task3_concluded);
            assertNull(task1_new);
            assertNotNull(task2_new);
            assertNotNull(task3_new);
            assertSame(task2_new.getTaskManager(), manager);
            assertSame(task3_new.getTaskManager(), manager);
            assertEquals(task2_new.getId(), task2.getId());
            assertEquals(task2_new.getType(), task2.getType());
            assertTrue(task2_new.getPlanned() >= task2.getPlanned());
            assertEquals(task2_new.getFrequency(), task2.getFrequency());
            assertEquals(task2_new.isBusy(), task2.isBusy());
            assertEquals(task3_new.getId(), task3.getId());
            assertEquals(task3_new.getType(), task3.getType());
            assertTrue(task3_new.getPlanned() <= task3.getPlanned());
            assertEquals(task3_new.getFrequency(), task3.getFrequency());
            assertEquals(task3_new.isBusy(), task3.isBusy());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDatasources.class)
    void testTaskActivation(Datasource datasource) {
        setup(datasource);

        TaskManager manager = DatabaseTasksFactory.instance(datasource);
        try {
            var task = new Task();
            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(System.currentTimeMillis());
            task.setFrequency(null);
            task.setBusy(false);

            var taskid = manager.addTask(task);

            manager.activateTask(taskid);
            task = manager.getTask(taskid);
            assertSame(task.getTaskManager(), manager);
            assertTrue(task.isBusy());
            manager.deactivateTask(taskid);
            task = manager.getTask(taskid);
            assertSame(task.getTaskManager(), manager);
            assertFalse(task.isBusy());

            manager.removeTask(task.getId());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        } finally {
            tearDown(datasource);
        }
    }
}
