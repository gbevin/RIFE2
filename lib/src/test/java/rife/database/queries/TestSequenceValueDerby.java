/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.database.queries;

import rife.database.DatasourceEnabledIf;
import rife.database.TestDatasourceIdentifier;
import rife.database.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestSequenceValueDerby extends TestSequenceValue {
    @DatasourceEnabledIf(TestDatasourceIdentifier.DERBY)
    void testInstantiationDerby() {
        var query = new SequenceValue(DERBY);
        assertNotNull(query);
        try {
            query.getSql();
            fail();
        } catch (SequenceNameRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.DERBY)
    void testInvalidDerby() {
        var query = new SequenceValue(DERBY);
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

    @DatasourceEnabledIf(TestDatasourceIdentifier.DERBY)
    void testClearDerby() {
        var query = new SequenceValue(DERBY);
        query
            .name("sequencename")
            .next();
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
        query
            .clear();
        try {
            query.getSql();
            fail();
        } catch (SequenceNameRequiredException e) {
            assertEquals(e.getQueryName(), "SequenceValue");
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.DERBY)
    void testNextDerby() {
        var query = new SequenceValue(DERBY);
        query
            .name("sequencename")
            .next();
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.DERBY)
    void testCurrentDerby() {
        var query = new SequenceValue(DERBY);
        query
            .name("sequencename")
            .current();
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.DERBY)
    void testCloneDerby() {
        var query = new SequenceValue(DERBY);
        query
            .name("sequencename")
            .next();
        var query_clone = query.clone();
        try {
            query_clone.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }
}
