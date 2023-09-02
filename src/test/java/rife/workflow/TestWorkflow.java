/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.workflow;

import org.junit.jupiter.api.Test;
import rifeworkflowtests.TestEventTypes;
import rifeworkflowtests.WorkDep1;
import rifeworkflowtests.WorkDep2;
import rifeworkflowtests.WorkPauseType1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWorkflow {
    @Test
    void testCodependency()
    throws Throwable {
        final var one_ended = new CountDownLatch(1);
        final var all_ended = new CountDownLatch(3);
        final var sum = new LongAdder();

        var workflow = new Workflow();
        workflow.addListener(event -> {
            if (TestEventTypes.END == event.getType()) {
                sum.add((Integer) event.getData());

                one_ended.countDown();
                all_ended.countDown();
            }
        });

        workflow.start(new WorkDep1());
        workflow.start(WorkDep2.class);
        one_ended.await();

        workflow.start(new WorkDep2());
        all_ended.await();

        assertEquals(45 + 90 + 145, sum.sum());
    }

    @Test
    void testTrigger()
    throws Throwable {
        var wf = new Workflow();
        wf.trigger(TestEventTypes.TYPE1, 1);
        var work = new WorkPauseType1();
        wf.start(work);
        wf.waitForNoWork();

        assertEquals(1, work.getEvent().getData());
    }

    @Test
    void testInform()
    throws Throwable {
        var wf = new Workflow();
        wf.inform(TestEventTypes.TYPE1, 1);
        var work = new WorkPauseType1();
        wf.start(work);
        wf.waitForPausedWork();

        wf.inform(TestEventTypes.TYPE1, 2);
        wf.waitForNoWork();

        assertEquals(2, work.getEvent().getData());
    }
}