/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.database.queries;

import rife.database.DatasourceEnabledIf;
import rife.database.TestDatasourceIdentifier;
import rife.database.exceptions.SequenceNameRequiredException;
import rife.database.exceptions.SequenceOperationRequiredException;

import static org.junit.jupiter.api.Assertions.*;

public class TestSequenceValueOracle extends TestSequenceValue {
    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testInstantiationOracle() {
        var query = new SequenceValue(ORACLE);
        assertNotNull(query);
        try {
            query.getSql();
            fail();
        } catch (SequenceNameRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testInvalidOracle() {
        var query = new SequenceValue(ORACLE);
        try {
            query.getSql();
            fail();
        } catch (SequenceNameRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
        query.name("sequencename");
        try {
            query.getSql();
            fail();
        } catch (SequenceOperationRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
        query.clear();
        query.next();
        try {
            query.getSql();
            fail();
        } catch (SequenceNameRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
        query.clear();
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testClearOracle() {
        var query = new SequenceValue(ORACLE);
        query
            .name("sequencename")
            .next();
        assertNotNull(query.getSql());
        query
            .clear();
        try {
            query.getSql();
            fail();
        } catch (SequenceNameRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testNextOracle() {
        var query = new SequenceValue(ORACLE);
        query
            .name("sequencename")
            .next();
        assertEquals(query.getSql(), "SELECT sequencename.nextval FROM DUAL");
        assertTrue(execute(ORACLE, query) >= 0);
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testCurrentOracle() {
        var query = new SequenceValue(ORACLE);
        query
            .name("sequencename")
            .current();
        assertEquals(query.getSql(), "SELECT sequencename.currval FROM DUAL");
        assertTrue(execute(ORACLE, query) >= 0);
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testCloneOracle() {
        var query = new SequenceValue(ORACLE);
        query
            .name("sequencename")
            .next();
        var query_clone = query.clone();
        assertEquals(query.getSql(), query_clone.getSql());
        assertNotSame(query, query_clone);
        assertTrue(execute(ORACLE, query_clone) >= 0);
    }
}
