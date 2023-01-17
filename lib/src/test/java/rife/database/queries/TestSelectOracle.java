/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.database.queries;

import rife.database.*;
import rife.database.exceptions.TableNameOrFieldsRequiredException;
import rife.database.exceptions.UnsupportedSqlFeatureException;
import rife.tools.Convert;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class TestSelectOracle extends TestSelect {
    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testInstantiationOracle() {
        var query = new Select(ORACLE);
        assertNotNull(query);
        try {
            query.getSql();
            fail();
        } catch (TableNameOrFieldsRequiredException e) {
            assertEquals(e.getQueryName(), "Select");
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testIncompleteQueryOracle() {
        var query = new Select(ORACLE);
        try {
            query.getSql();
            fail();
        } catch (TableNameOrFieldsRequiredException e) {
            assertEquals(e.getQueryName(), "Select");
        }
        query.from("tablename");
        assertNotNull(query.getSql());

        query = new Select(ORACLE);
        try {
            query.getSql();
            fail();
        } catch (TableNameOrFieldsRequiredException e) {
            assertEquals(e.getQueryName(), "Select");
        }
        query.field("field");
        assertNotNull(query.getSql());
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testClearOracle() {
        var query = new Select(ORACLE);
        query.from("tablename");
        assertNotNull(query.getSql());
        query.clear();
        try {
            query.getSql();
            fail();
        } catch (TableNameOrFieldsRequiredException e) {
            assertEquals(e.getQueryName(), "Select");
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testBasicOracle() {
        var query = new Select(ORACLE);
        query.from("tablename");
        assertEquals(query.getSql(), "SELECT * FROM tablename");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testHintOracle() {
        var query = new Select(ORACLE)
            .hint("NO_INDEX")
            .from("tablename");
        assertEquals(query.getSql(), "SELECT /*+ NO_INDEX */ * FROM tablename");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testOrderByAscendingOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .orderBy("propertyInt", Select.ASC);
        assertEquals(query.getSql(), "SELECT * FROM tablename ORDER BY propertyInt ASC");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testOrderByDescendingOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .orderBy("propertyInt", Select.DESC);
        assertEquals(query.getSql(), "SELECT * FROM tablename ORDER BY propertyInt DESC");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testBeanOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .fields(BeanImpl.class);
        assertEquals(query.getSql(), "SELECT propertyBigDecimal, propertyBoolean, propertyBooleanObject, propertyByte, propertyByteObject, propertyCalendar, propertyChar, propertyCharacterObject, propertyDate, propertyDouble, propertyDoubleObject, propertyEnum, propertyFloat, propertyFloatObject, propertyInstant, propertyInt, propertyIntegerObject, propertyLocalDate, propertyLocalDateTime, propertyLocalTime, propertyLong, propertyLongObject, propertyShort, propertyShortObject, propertySqlDate, propertyString, propertyStringBuffer, propertyTime, propertyTimestamp FROM tablename");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testBeanConstrainedOracle() {
        var query = new Select(ORACLE, BeanImplConstrained.class);
        query.from("tablename");
        assertEquals(query.getSql(), "SELECT * FROM tablename ORDER BY propertyString ASC, propertyInt DESC");
        assertTrue(execute(query));

        query = new Select(ORACLE, BeanImplConstrained.class);
        query.from("tablename")
            .orderBy("propertyByte");
        assertEquals(query.getSql(), "SELECT * FROM tablename ORDER BY propertyByte ASC");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testBeanExcludedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .fieldsExcluded(BeanImpl.class, "propertyCalendar", "propertyFloat", "propertyShort");
        assertEquals(query.getSql(), "SELECT propertyBigDecimal, propertyBoolean, propertyBooleanObject, propertyByte, propertyByteObject, propertyChar, propertyCharacterObject, propertyDate, propertyDouble, propertyDoubleObject, propertyEnum, propertyFloatObject, propertyInstant, propertyInt, propertyIntegerObject, propertyLocalDate, propertyLocalDateTime, propertyLocalTime, propertyLong, propertyLongObject, propertyShortObject, propertySqlDate, propertyString, propertyStringBuffer, propertyTime, propertyTimestamp FROM tablename");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testBeanTableOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .fields("tablename", BeanImpl.class);
        assertEquals(query.getSql(), "SELECT tablename.propertyBigDecimal, tablename.propertyBoolean, tablename.propertyBooleanObject, tablename.propertyByte, tablename.propertyByteObject, tablename.propertyCalendar, tablename.propertyChar, tablename.propertyCharacterObject, tablename.propertyDate, tablename.propertyDouble, tablename.propertyDoubleObject, tablename.propertyEnum, tablename.propertyFloat, tablename.propertyFloatObject, tablename.propertyInstant, tablename.propertyInt, tablename.propertyIntegerObject, tablename.propertyLocalDate, tablename.propertyLocalDateTime, tablename.propertyLocalTime, tablename.propertyLong, tablename.propertyLongObject, tablename.propertyShort, tablename.propertyShortObject, tablename.propertySqlDate, tablename.propertyString, tablename.propertyStringBuffer, tablename.propertyTime, tablename.propertyTimestamp FROM tablename");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testBeanExcludedTableOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .fieldsExcluded("tablename", BeanImpl.class, "propertyCalendar", "propertyFloat", "propertyShort");
        assertEquals(query.getSql(), "SELECT tablename.propertyBigDecimal, tablename.propertyBoolean, tablename.propertyBooleanObject, tablename.propertyByte, tablename.propertyByteObject, tablename.propertyChar, tablename.propertyCharacterObject, tablename.propertyDate, tablename.propertyDouble, tablename.propertyDoubleObject, tablename.propertyEnum, tablename.propertyFloatObject, tablename.propertyInstant, tablename.propertyInt, tablename.propertyIntegerObject, tablename.propertyLocalDate, tablename.propertyLocalDateTime, tablename.propertyLocalTime, tablename.propertyLong, tablename.propertyLongObject, tablename.propertyShortObject, tablename.propertySqlDate, tablename.propertyString, tablename.propertyStringBuffer, tablename.propertyTime, tablename.propertyTimestamp FROM tablename");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereTypedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename");

        var cal = Calendar.getInstance();
        cal.set(2003, Calendar.MARCH, 3, 10, 1, 28);
        cal.set(Calendar.MILLISECOND, 154);

        query
            .where("propertyBigDecimal", ">=", new BigDecimal("53443433.9784567"))
            .whereAnd("propertyBoolean", "=", false)
            .whereOr("propertyByte", "=", (byte) 54)
            .whereAnd("propertyCalendar", "<=", cal)
            .whereOr("propertyChar", "=", 'f')
            .whereAnd("propertyDate", "=", cal.getTime())
            .whereAnd("propertyDouble", "!=", 73453.71d)
            .whereOr("propertyFloat", ">=", 1987.14f)
            .whereAnd("propertyInt", "=", 973)
            .whereAnd("propertyLong", "<", 347678L)
            .whereAnd("propertyShort", "=", (short) 78)
            .whereOr("propertySqlDate", "=", Convert.toSqlDate(cal))
            .whereAnd("propertyString", "LIKE", "someotherstring%")
            .whereAnd("propertyStringBuffer", "=", new StringBuffer("someotherstringbuff"))
            .whereOr("propertyTime", "=", Convert.toSqlTime(cal))
            .whereAnd("propertyTimestamp", "<=", Convert.toSqlTimestamp(cal));

        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal >= 53443433.9784567 AND propertyBoolean = 0 OR propertyByte = 54 AND propertyCalendar <= TO_DATE('2003/03/03 10:01:28', 'YYYY/MM/DD HH24:MI:SS') OR propertyChar = 'f' AND propertyDate = TO_DATE('2003/03/03 10:01:28', 'YYYY/MM/DD HH24:MI:SS') AND propertyDouble != 73453.71 OR propertyFloat >= 1987.14 AND propertyInt = 973 AND propertyLong < 347678 AND propertyShort = 78 OR propertySqlDate = TO_DATE('2003/03/03 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyString LIKE 'someotherstring%' AND propertyStringBuffer = 'someotherstringbuff' OR propertyTime = TO_DATE('10:01:28', 'HH24:MI:SS') AND propertyTimestamp <= TO_DATE('2003/03/03 10:01:28', 'YYYY/MM/DD HH24:MI:SS')");
        assertFalse(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereTypedMixedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename");

        final var cal = Calendar.getInstance();
        cal.set(2003, Calendar.MARCH, 3, 10, 1, 28);
        cal.set(Calendar.MILLISECOND, 154);

        query
            .where("propertyBigDecimal", ">=", new BigDecimal("53443433.9784567"))
            .whereAnd("propertyBoolean", "=", false)
            .whereOr("propertyByte = 54")
            .whereAnd("propertyCalendar", "<=", cal)
            .whereOr("propertyChar", "=", 'f')
            .whereAnd("propertyDate", "=", cal.getTime())
            .whereAnd("propertyDouble", "!=", 73453.71d)
            .whereOr("propertyFloat >= 1987.14")
            .whereAnd("propertyInt", "=", 973)
            .whereAnd("propertyLong", "<", 347678L)
            .whereAnd("propertyShort", "=", (short) 78)
            .whereParameterOr("propertySqlDate", "=")
            .whereAnd("propertyString", "LIKE", "someotherstring%")
            .whereAnd("propertyStringBuffer", "=", new StringBuffer("someotherstringbuff"))
            .whereOr("propertyTime", "=", Convert.toSqlTime(cal))
            .whereAnd("propertyTimestamp", "<=", Convert.toSqlTimestamp(cal));

        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal >= 53443433.9784567 AND propertyBoolean = 0 OR propertyByte = 54 AND propertyCalendar <= TO_DATE('2003/03/03 10:01:28', 'YYYY/MM/DD HH24:MI:SS') OR propertyChar = 'f' AND propertyDate = TO_DATE('2003/03/03 10:01:28', 'YYYY/MM/DD HH24:MI:SS') AND propertyDouble != 73453.71 OR propertyFloat >= 1987.14 AND propertyInt = 973 AND propertyLong < 347678 AND propertyShort = 78 OR propertySqlDate = ? AND propertyString LIKE 'someotherstring%' AND propertyStringBuffer = 'someotherstringbuff' OR propertyTime = TO_DATE('10:01:28', 'HH24:MI:SS') AND propertyTimestamp <= TO_DATE('2003/03/03 10:01:28', 'YYYY/MM/DD HH24:MI:SS')");

        assertFalse(execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setDate("propertySqlDate", Convert.toSqlDate(cal));
            }
        }));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereParametersOracle() {
        var query = new Select(ORACLE);
        query.from("tablename");

        assertNull(query.getParameters());

        query.whereParameter("propertyInt", "=")
            .whereParameterAnd("propertyLong", "<")
            .whereParameterOr("propertyChar", "=");

        assertEquals(query.getParameters().getOrderedNames().size(), 3);
        assertEquals(query.getParameters().getOrderedNames().get(0), "propertyInt");
        assertEquals(query.getParameters().getOrderedNames().get(1), "propertyLong");
        assertEquals(query.getParameters().getOrderedNames().get(2), "propertyChar");
        assertArrayEquals(query.getParameters().getOrderedNamesArray(), new String[]{"propertyInt", "propertyLong", "propertyChar"});

        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyInt = ? AND propertyLong < ? OR propertyChar = ?");
        assertTrue(execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setInt(1, 545)
                    .setLong(2, 50000)
                    .setString(3, "v");
            }
        }));

        query.where("propertyInt = 545");

        assertNull(query.getParameters());
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyInt = 545");
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereParametersMixedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .where("propertyInt = 545")
            .whereParameterAnd("propertyLong", "<")
            .whereParameterOr("propertyChar", "=");

        assertEquals(query.getParameters().getOrderedNames().get(0), "propertyLong");
        assertEquals(query.getParameters().getOrderedNames().get(1), "propertyChar");
        assertArrayEquals(query.getParameters().getOrderedNamesArray(), new String[]{"propertyLong", "propertyChar"});

        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyInt = 545 AND propertyLong < ? OR propertyChar = ?");
        assertTrue(execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setLong(1, 50000)
                    .setString(2, "v");
            }
        }));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereConstructionOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .where("propertyInt = 545")
            .whereAnd("propertyLong < 50000")
            .whereOr("propertyChar = 'v'");
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyInt = 545 AND propertyLong < 50000 OR propertyChar = 'v'");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereConstructionGroupOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .startWhere()
            .where("propertyInt", "=", 545)
            .whereAnd("propertyByte", "=", 89)
            .end()
            .whereAnd("propertyLong < 50000")
            .startWhereOr()
            .whereParameter("propertyString", "=")
            .whereAnd("propertyByte", "<=", (byte) 0)
            .startWhereAnd()
            .where("propertyBoolean", "!=", true)
            .whereParameterOr("propertyStringBuffer", "LIKE")
            .end()
            .end()
            .whereOr("propertyChar = 'v'");

        assertEquals(query.getParameters().getOrderedNames().get(0), "propertyString");
        assertEquals(query.getParameters().getOrderedNames().get(1), "propertyStringBuffer");
        assertArrayEquals(query.getParameters().getOrderedNamesArray(), new String[]{"propertyString", "propertyStringBuffer"});

        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE (propertyInt = 545 AND propertyByte = 89) AND propertyLong < 50000 OR (propertyString = ? AND propertyByte <= 0 AND (propertyBoolean != 1 OR propertyStringBuffer LIKE ?)) OR propertyChar = 'v'");

        assertTrue(execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setString("propertyString", "someotherstring")
                    .setString("propertyStringBuffer", "stringbuff");
            }
        }));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereBeanOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .where(BeanImpl.getPopulatedBean());
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal = 219038743.392874 AND propertyBoolean = 1 AND propertyBooleanObject = 0 AND propertyByte = 89 AND propertyByteObject = 34 AND propertyCalendar = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyChar = 'v' AND propertyCharacterObject = 'r' AND propertyDate = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyDouble = 53348.34 AND propertyDoubleObject = 143298.692 AND propertyEnum = 'VALUE_THREE' AND propertyFloat = 98634.2 AND propertyFloatObject = 8734.7 AND propertyInstant = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyInt = 545 AND propertyIntegerObject = 968 AND propertyLocalDate = TO_DATE('2002/06/18 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyLocalDateTime = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyLocalTime = TO_DATE('15:26:14', 'HH24:MI:SS') AND propertyLong = 34563 AND propertyLongObject = 66875 AND propertyShort = 43 AND propertyShortObject = 68 AND propertySqlDate = TO_DATE('2002/06/18 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyString = 'someotherstring' AND propertyStringBuffer = 'someotherstringbuff' AND propertyTime = TO_DATE('15:26:14', 'HH24:MI:SS') AND propertyTimestamp = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS')");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereBeanConstrainedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .where(BeanImplConstrained.getPopulatedBean());
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal = 219038743.392874 AND propertyBoolean = 1 AND propertyBooleanObject = 0 AND propertyByte = 89 AND propertyByteObject = 34 AND propertyCalendar = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyChar = 'v' AND propertyCharacterObject = 'r' AND propertyDate = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyDouble = 53348.34 AND propertyDoubleObject = 143298.692 AND propertyFloat = 98634.2 AND propertyFloatObject = 8734.7 AND propertyInstant = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyInt = 545 AND propertyIntegerObject = 968 AND propertyLocalDate = TO_DATE('2002/06/18 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyLocalDateTime = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyLocalTime = TO_DATE('15:26:14', 'HH24:MI:SS') AND propertyLongObject = 66875 AND propertyShort = 43 AND propertySqlDate = TO_DATE('2002/06/18 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyString = 'someotherstring' AND propertyStringBuffer = 'someotherstringbuff' AND propertyTime = TO_DATE('15:26:14', 'HH24:MI:SS') AND propertyTimestamp = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS')");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereBeanNullValuesOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .where(BeanImpl.getNullBean());
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBoolean = 0 AND propertyBooleanObject = 0 AND propertyByte = 0 AND propertyByteObject = 0 AND propertyDouble = 0.0 AND propertyDoubleObject = 0.0 AND propertyFloat = 0.0 AND propertyFloatObject = 0.0 AND propertyInt = 0 AND propertyIntegerObject = 0 AND propertyLong = 0 AND propertyLongObject = 0 AND propertyShort = 0 AND propertyShortObject = 0");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereBeanIncludedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .whereIncluded(BeanImpl.getPopulatedBean(), new String[]{"propertyByte", "propertyDouble", "propertyShort", "propertyStringBuffer", "propertyTime"});
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyByte = 89 AND propertyDouble = 53348.34 AND propertyShort = 43 AND propertyStringBuffer = 'someotherstringbuff' AND propertyTime = TO_DATE('15:26:14', 'HH24:MI:SS')");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereBeanExcludedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .whereExcluded(BeanImpl.getPopulatedBean(), new String[]{"propertyByte", "propertyDouble", "propertyShort", "propertyStringBuffer", "propertyTime"});
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal = 219038743.392874 AND propertyBoolean = 1 AND propertyBooleanObject = 0 AND propertyByteObject = 34 AND propertyCalendar = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyChar = 'v' AND propertyCharacterObject = 'r' AND propertyDate = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyDoubleObject = 143298.692 AND propertyEnum = 'VALUE_THREE' AND propertyFloat = 98634.2 AND propertyFloatObject = 8734.7 AND propertyInstant = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyInt = 545 AND propertyIntegerObject = 968 AND propertyLocalDate = TO_DATE('2002/06/18 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyLocalDateTime = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS') AND propertyLocalTime = TO_DATE('15:26:14', 'HH24:MI:SS') AND propertyLong = 34563 AND propertyLongObject = 66875 AND propertyShortObject = 68 AND propertySqlDate = TO_DATE('2002/06/18 00:00:00', 'YYYY/MM/DD HH24:MI:SS') AND propertyString = 'someotherstring' AND propertyTimestamp = TO_DATE('2002/06/18 15:26:14', 'YYYY/MM/DD HH24:MI:SS')");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereBeanFilteredOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .whereFiltered(BeanImpl.getPopulatedBean(), new String[]{"propertyByte", "propertyDouble", "propertyShort", "propertyStringBuffer", "propertyTime"}, new String[]{"propertyByte", "propertyShort", "propertyTime"});
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyDouble = 53348.34 AND propertyStringBuffer = 'someotherstringbuff'");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereParametersBeanOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .whereParameters(BeanImpl.class);
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal = ? AND propertyBoolean = ? AND propertyBooleanObject = ? AND propertyByte = ? AND propertyByteObject = ? AND propertyCalendar = ? AND propertyChar = ? AND propertyCharacterObject = ? AND propertyDate = ? AND propertyDouble = ? AND propertyDoubleObject = ? AND propertyEnum = ? AND propertyFloat = ? AND propertyFloatObject = ? AND propertyInstant = ? AND propertyInt = ? AND propertyIntegerObject = ? AND propertyLocalDate = ? AND propertyLocalDateTime = ? AND propertyLocalTime = ? AND propertyLong = ? AND propertyLongObject = ? AND propertyShort = ? AND propertyShortObject = ? AND propertySqlDate = ? AND propertyString = ? AND propertyStringBuffer = ? AND propertyTime = ? AND propertyTimestamp = ?");

        assertEquals(query.getParameters().getOrderedNames().size(), 29);
        assertEquals(query.getParameters().getOrderedNames().get(0), "propertyBigDecimal");
        assertEquals(query.getParameters().getOrderedNames().get(1), "propertyBoolean");
        assertEquals(query.getParameters().getOrderedNames().get(2), "propertyBooleanObject");
        assertEquals(query.getParameters().getOrderedNames().get(3), "propertyByte");
        assertEquals(query.getParameters().getOrderedNames().get(4), "propertyByteObject");
        assertEquals(query.getParameters().getOrderedNames().get(5), "propertyCalendar");
        assertEquals(query.getParameters().getOrderedNames().get(6), "propertyChar");
        assertEquals(query.getParameters().getOrderedNames().get(7), "propertyCharacterObject");
        assertEquals(query.getParameters().getOrderedNames().get(8), "propertyDate");
        assertEquals(query.getParameters().getOrderedNames().get(9), "propertyDouble");
        assertEquals(query.getParameters().getOrderedNames().get(10), "propertyDoubleObject");
        assertEquals(query.getParameters().getOrderedNames().get(11), "propertyEnum");
        assertEquals(query.getParameters().getOrderedNames().get(12), "propertyFloat");
        assertEquals(query.getParameters().getOrderedNames().get(13), "propertyFloatObject");
        assertEquals(query.getParameters().getOrderedNames().get(14), "propertyInstant");
        assertEquals(query.getParameters().getOrderedNames().get(15), "propertyInt");
        assertEquals(query.getParameters().getOrderedNames().get(16), "propertyIntegerObject");
        assertEquals(query.getParameters().getOrderedNames().get(17), "propertyLocalDate");
        assertEquals(query.getParameters().getOrderedNames().get(18), "propertyLocalDateTime");
        assertEquals(query.getParameters().getOrderedNames().get(19), "propertyLocalTime");
        assertEquals(query.getParameters().getOrderedNames().get(20), "propertyLong");
        assertEquals(query.getParameters().getOrderedNames().get(21), "propertyLongObject");
        assertEquals(query.getParameters().getOrderedNames().get(22), "propertyShort");
        assertEquals(query.getParameters().getOrderedNames().get(23), "propertyShortObject");
        assertEquals(query.getParameters().getOrderedNames().get(24), "propertySqlDate");
        assertEquals(query.getParameters().getOrderedNames().get(25), "propertyString");
        assertEquals(query.getParameters().getOrderedNames().get(26), "propertyStringBuffer");
        assertEquals(query.getParameters().getOrderedNames().get(27), "propertyTime");
        assertEquals(query.getParameters().getOrderedNames().get(28), "propertyTimestamp");
        assertArrayEquals(query.getParameters().getOrderedNamesArray(), new String[]{"propertyBigDecimal", "propertyBoolean", "propertyBooleanObject", "propertyByte", "propertyByteObject", "propertyCalendar", "propertyChar", "propertyCharacterObject", "propertyDate", "propertyDouble", "propertyDoubleObject", "propertyEnum", "propertyFloat", "propertyFloatObject", "propertyInstant", "propertyInt", "propertyIntegerObject", "propertyLocalDate", "propertyLocalDateTime", "propertyLocalTime", "propertyLong", "propertyLongObject", "propertyShort", "propertyShortObject", "propertySqlDate", "propertyString", "propertyStringBuffer", "propertyTime", "propertyTimestamp"});

        // don't check if actual rows were returned, since Oracle doesn't
        // match on the date types
        execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                var cal = Calendar.getInstance();
                cal.set(2002, Calendar.JUNE, 18, 15, 26, 14);
                cal.set(Calendar.MILLISECOND, 0);
                statement
                    .setBigDecimal(1, new BigDecimal("219038743.392874"))
                    .setBoolean(2, true)
                    .setBoolean(3, false)
                    .setByte(4, (byte) 89)
                    .setByte(5, (byte) 34)
                    .setTimestamp(6, Convert.toSqlTimestamp(cal))
                    .setString(7, "v")
                    .setString(8, "r")
                    .setTimestamp(9, Convert.toSqlTimestamp(cal))
                    .setDouble(10, 53348.34d)
                    .setDouble(11, 143298.692d)
                    .setString(12, "VALUE_THREE")
                    .setDouble(13, 98634.2d)
                    .setDouble(14, 8734.7d)
                    .setTimestamp(15, Convert.toSqlTimestamp(cal))
                    .setInt(16, 545)
                    .setInt(17, 968)
                    .setDate(18, Convert.toSqlDate(cal))
                    .setTimestamp(19, Convert.toSqlTimestamp(cal))
                    .setTime(20, Convert.toSqlTime(cal))
                    .setLong(21, 34563L)
                    .setLong(22, 66875L)
                    .setShort(23, (short) 43)
                    .setShort(24, (short) 68)
                    .setDate(25, Convert.toSqlDate(cal))
                    .setString(26, "someotherstring")
                    .setString(27, "someotherstringbuff")
                    .setTime(28, Convert.toSqlTime(cal))
                    .setTimestamp(29, Convert.toSqlTimestamp(cal));
            }
        });
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereParametersBeanConstrainedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .whereParameters(BeanImplConstrained.class);
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal = ? AND propertyBoolean = ? AND propertyBooleanObject = ? AND propertyByte = ? AND propertyByteObject = ? AND propertyCalendar = ? AND propertyChar = ? AND propertyCharacterObject = ? AND propertyDate = ? AND propertyDouble = ? AND propertyDoubleObject = ? AND propertyFloat = ? AND propertyFloatObject = ? AND propertyInstant = ? AND propertyInt = ? AND propertyIntegerObject = ? AND propertyLocalDate = ? AND propertyLocalDateTime = ? AND propertyLocalTime = ? AND propertyLongObject = ? AND propertyShort = ? AND propertySqlDate = ? AND propertyString = ? AND propertyStringBuffer = ? AND propertyTime = ? AND propertyTimestamp = ?");

        assertEquals(query.getParameters().getOrderedNames().size(), 26);
        assertEquals(query.getParameters().getOrderedNames().get(0), "propertyBigDecimal");
        assertEquals(query.getParameters().getOrderedNames().get(1), "propertyBoolean");
        assertEquals(query.getParameters().getOrderedNames().get(2), "propertyBooleanObject");
        assertEquals(query.getParameters().getOrderedNames().get(3), "propertyByte");
        assertEquals(query.getParameters().getOrderedNames().get(4), "propertyByteObject");
        assertEquals(query.getParameters().getOrderedNames().get(5), "propertyCalendar");
        assertEquals(query.getParameters().getOrderedNames().get(6), "propertyChar");
        assertEquals(query.getParameters().getOrderedNames().get(7), "propertyCharacterObject");
        assertEquals(query.getParameters().getOrderedNames().get(8), "propertyDate");
        assertEquals(query.getParameters().getOrderedNames().get(9), "propertyDouble");
        assertEquals(query.getParameters().getOrderedNames().get(10), "propertyDoubleObject");
        assertEquals(query.getParameters().getOrderedNames().get(11), "propertyFloat");
        assertEquals(query.getParameters().getOrderedNames().get(12), "propertyFloatObject");
        assertEquals(query.getParameters().getOrderedNames().get(13), "propertyInstant");
        assertEquals(query.getParameters().getOrderedNames().get(14), "propertyInt");
        assertEquals(query.getParameters().getOrderedNames().get(15), "propertyIntegerObject");
        assertEquals(query.getParameters().getOrderedNames().get(16), "propertyLocalDate");
        assertEquals(query.getParameters().getOrderedNames().get(17), "propertyLocalDateTime");
        assertEquals(query.getParameters().getOrderedNames().get(18), "propertyLocalTime");
        assertEquals(query.getParameters().getOrderedNames().get(19), "propertyLongObject");
        assertEquals(query.getParameters().getOrderedNames().get(20), "propertyShort");
        assertEquals(query.getParameters().getOrderedNames().get(21), "propertySqlDate");
        assertEquals(query.getParameters().getOrderedNames().get(22), "propertyString");
        assertEquals(query.getParameters().getOrderedNames().get(23), "propertyStringBuffer");
        assertEquals(query.getParameters().getOrderedNames().get(24), "propertyTime");
        assertEquals(query.getParameters().getOrderedNames().get(25), "propertyTimestamp");
        assertArrayEquals(query.getParameters().getOrderedNamesArray(), new String[]{"propertyBigDecimal", "propertyBoolean", "propertyBooleanObject", "propertyByte", "propertyByteObject", "propertyCalendar", "propertyChar", "propertyCharacterObject", "propertyDate", "propertyDouble", "propertyDoubleObject", "propertyFloat", "propertyFloatObject", "propertyInstant", "propertyInt", "propertyIntegerObject", "propertyLocalDate", "propertyLocalDateTime", "propertyLocalTime", "propertyLongObject", "propertyShort", "propertySqlDate", "propertyString", "propertyStringBuffer", "propertyTime", "propertyTimestamp"});

        // don't check if actual rows were returned, since Oracle doesn't
        // match on the date types
        execute(query, new DbPreparedStatementHandler() {
            public void setParameters(DbPreparedStatement statement) {
                var cal = Calendar.getInstance();
                cal.set(2002, Calendar.JUNE, 18, 15, 26, 14);
                cal.set(Calendar.MILLISECOND, 0);
                statement
                    .setBigDecimal(1, new BigDecimal("219038743.392874"))
                    .setBoolean(2, true)
                    .setBoolean(3, false)
                    .setByte(4, (byte) 89)
                    .setByte(5, (byte) 34)
                    .setTimestamp(6, Convert.toSqlTimestamp(cal))
                    .setString(7, "v")
                    .setString(8, "r")
                    .setTimestamp(9, Convert.toSqlTimestamp(cal))
                    .setDouble(10, 53348.34d)
                    .setDouble(11, 143298.692d)
                    .setFloat(12, 98634.2f)
                    .setFloat(13, 8734.7f)
                    .setTimestamp(14, Convert.toSqlTimestamp(cal))
                    .setInt(15, 545)
                    .setInt(16, 968)
                    .setDate(17, Convert.toSqlDate(cal))
                    .setTimestamp(18, Convert.toSqlTimestamp(cal))
                    .setTime(19, Convert.toSqlTime(cal))
                    .setLong(20, 66875L)
                    .setShort(21, (short) 43)
                    .setDate(22, Convert.toSqlDate(cal))
                    .setString(23, "someotherstring")
                    .setString(24, "someotherstringbuff")
                    .setTime(25, Convert.toSqlTime(cal))
                    .setTimestamp(26, Convert.toSqlTimestamp(cal));
            }
        });
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testWhereParametersBeanExcludedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .whereParametersExcluded(BeanImpl.class,
                new String[]{"propertyBoolean", "propertyByte", "propertyChar",
                    "propertyDouble", "propertyInt", "propertyLong",
                    "propertySqlDate", "propertyStringBuffer", "propertyTimestamp"});
        assertEquals(query.getSql(), "SELECT * FROM tablename WHERE propertyBigDecimal = ? AND propertyBooleanObject = ? AND propertyByteObject = ? AND propertyCalendar = ? AND propertyCharacterObject = ? AND propertyDate = ? AND propertyDoubleObject = ? AND propertyEnum = ? AND propertyFloat = ? AND propertyFloatObject = ? AND propertyInstant = ? AND propertyIntegerObject = ? AND propertyLocalDate = ? AND propertyLocalDateTime = ? AND propertyLocalTime = ? AND propertyLongObject = ? AND propertyShort = ? AND propertyShortObject = ? AND propertyString = ? AND propertyTime = ?");

        assertEquals(query.getParameters().getOrderedNames().size(), 20);
        assertEquals(query.getParameters().getOrderedNames().get(0), "propertyBigDecimal");
        assertEquals(query.getParameters().getOrderedNames().get(1), "propertyBooleanObject");
        assertEquals(query.getParameters().getOrderedNames().get(2), "propertyByteObject");
        assertEquals(query.getParameters().getOrderedNames().get(3), "propertyCalendar");
        assertEquals(query.getParameters().getOrderedNames().get(4), "propertyCharacterObject");
        assertEquals(query.getParameters().getOrderedNames().get(5), "propertyDate");
        assertEquals(query.getParameters().getOrderedNames().get(6), "propertyDoubleObject");
        assertEquals(query.getParameters().getOrderedNames().get(7), "propertyEnum");
        assertEquals(query.getParameters().getOrderedNames().get(8), "propertyFloat");
        assertEquals(query.getParameters().getOrderedNames().get(9), "propertyFloatObject");
        assertEquals(query.getParameters().getOrderedNames().get(10), "propertyInstant");
        assertEquals(query.getParameters().getOrderedNames().get(11), "propertyIntegerObject");
        assertEquals(query.getParameters().getOrderedNames().get(12), "propertyLocalDate");
        assertEquals(query.getParameters().getOrderedNames().get(13), "propertyLocalDateTime");
        assertEquals(query.getParameters().getOrderedNames().get(14), "propertyLocalTime");
        assertEquals(query.getParameters().getOrderedNames().get(15), "propertyLongObject");
        assertEquals(query.getParameters().getOrderedNames().get(16), "propertyShort");
        assertEquals(query.getParameters().getOrderedNames().get(17), "propertyShortObject");
        assertEquals(query.getParameters().getOrderedNames().get(18), "propertyString");
        assertEquals(query.getParameters().getOrderedNames().get(19), "propertyTime");
        assertArrayEquals(query.getParameters().getOrderedNamesArray(), new String[]{"propertyBigDecimal", "propertyBooleanObject", "propertyByteObject", "propertyCalendar", "propertyCharacterObject", "propertyDate", "propertyDoubleObject", "propertyEnum", "propertyFloat", "propertyFloatObject", "propertyInstant", "propertyIntegerObject", "propertyLocalDate", "propertyLocalDateTime", "propertyLocalTime", "propertyLongObject", "propertyShort", "propertyShortObject", "propertyString", "propertyTime"});

        // don't check if actual rows were returned, since Oracle doesn't
        // match on the date types
        execute(query, new DbPreparedStatementHandler() {
            public void setParameters(DbPreparedStatement statement) {
                var cal = Calendar.getInstance();
                cal.set(2002, Calendar.JUNE, 18, 15, 26, 14);
                cal.set(Calendar.MILLISECOND, 0);
                statement
                    .setBigDecimal(1, new BigDecimal("219038743.392874"))
                    .setBoolean(2, false)
                    .setByte(3, (byte) 34)
                    .setTimestamp(4, Convert.toSqlTimestamp(cal))
                    .setString(5, "r")
                    .setTimestamp(6, Convert.toSqlTimestamp(cal))
                    .setDouble(7, 143298.692d)
                    .setString(8, "VALUE_THREE")
                    .setFloat(9, 98634.2f)
                    .setFloat(10, 8734.7f)
                    .setTimestamp(11, Convert.toSqlTimestamp(cal))
                    .setInt(12, 968)
                    .setDate(13, Convert.toSqlDate(cal))
                    .setTimestamp(14, Convert.toSqlTimestamp(cal))
                    .setTime(15, Convert.toSqlTime(cal))
                    .setLong(16, 66875L)
                    .setShort(17, (short) 43)
                    .setShort(18, (short) 68)
                    .setString(19, "someotherstring")
                    .setTime(20, Convert.toSqlTime(cal));
            }
        });
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testDistinctOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .distinct()
            .where("propertyByte = 89")
            .orderBy("propertyDouble")
            .orderBy("propertyShort")
            .orderBy("propertyTime");
        assertEquals(query.getSql(), "SELECT DISTINCT * FROM tablename WHERE propertyByte = 89 ORDER BY propertyDouble ASC, propertyShort ASC, propertyTime ASC");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testDistinctOnOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .distinctOn("propertyDouble")
            .distinctOn("propertyShort")
            .distinctOn("propertyTime")
            .where("propertyByte = 89")
            .orderBy("propertyDouble")
            .orderBy("propertyShort")
            .orderBy("propertyTime");
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testComplexOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .field("field1")
            .field("field2")
            .field("field3")
            .join("table2")
            .joinCross("table3")
            .where("this = that")
            .groupBy("gexpr1")
            .groupBy("gexpr2")
            .having("hexpr1")
            .having("hexpr2")
            .distinct()
            .unionAll("uexpr1")
            .union("uexpr2")
            .limit(3)
            .offset(1);
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testGroupByBeanOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .fields(BeanImpl.class)
            .groupBy(BeanImpl.class);
        assertEquals(query.getSql(), "SELECT propertyBigDecimal, propertyBoolean, propertyBooleanObject, propertyByte, propertyByteObject, propertyCalendar, propertyChar, propertyCharacterObject, propertyDate, propertyDouble, propertyDoubleObject, propertyEnum, propertyFloat, propertyFloatObject, propertyInstant, propertyInt, propertyIntegerObject, propertyLocalDate, propertyLocalDateTime, propertyLocalTime, propertyLong, propertyLongObject, propertyShort, propertyShortObject, propertySqlDate, propertyString, propertyStringBuffer, propertyTime, propertyTimestamp FROM tablename GROUP BY propertyBigDecimal, propertyBoolean, propertyBooleanObject, propertyByte, propertyByteObject, propertyCalendar, propertyChar, propertyCharacterObject, propertyDate, propertyDouble, propertyDoubleObject, propertyEnum, propertyFloat, propertyFloatObject, propertyInstant, propertyInt, propertyIntegerObject, propertyLocalDate, propertyLocalDateTime, propertyLocalTime, propertyLong, propertyLongObject, propertyShort, propertyShortObject, propertySqlDate, propertyString, propertyStringBuffer, propertyTime, propertyTimestamp");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testGroupByBeanExcludedOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .fieldsExcluded(BeanImpl.class, "propertyCalendar", "propertyFloat", "propertyShort")
            .groupByExcluded(BeanImpl.class, "propertyCalendar", "propertyFloat", "propertyShort");
        assertEquals(query.getSql(), "SELECT propertyBigDecimal, propertyBoolean, propertyBooleanObject, propertyByte, propertyByteObject, propertyChar, propertyCharacterObject, propertyDate, propertyDouble, propertyDoubleObject, propertyEnum, propertyFloatObject, propertyInstant, propertyInt, propertyIntegerObject, propertyLocalDate, propertyLocalDateTime, propertyLocalTime, propertyLong, propertyLongObject, propertyShortObject, propertySqlDate, propertyString, propertyStringBuffer, propertyTime, propertyTimestamp FROM tablename GROUP BY propertyBigDecimal, propertyBoolean, propertyBooleanObject, propertyByte, propertyByteObject, propertyChar, propertyCharacterObject, propertyDate, propertyDouble, propertyDoubleObject, propertyEnum, propertyFloatObject, propertyInstant, propertyInt, propertyIntegerObject, propertyLocalDate, propertyLocalDateTime, propertyLocalTime, propertyLong, propertyLongObject, propertyShortObject, propertySqlDate, propertyString, propertyStringBuffer, propertyTime, propertyTimestamp");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testJoinOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .join("table2")
            .join("table3");
        assertEquals(query.getSql(), "SELECT * FROM tablename, table2, table3");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testJoinCustomOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .joinCustom("INNER JOIN table3 USING (propertyInt)")
            .joinCustom("CROSS JOIN table2");
        assertEquals(query.getSql(), "SELECT * FROM tablename INNER JOIN table3 USING (propertyInt) CROSS JOIN table2");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testJoinCrossOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .joinCross("table2")
            .joinCross("table3");
        assertEquals(query.getSql(), "SELECT * FROM tablename CROSS JOIN table2 CROSS JOIN table3");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testJoinInnerOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .joinInner("table2", Select.NATURAL, null);
        assertEquals(query.getSql(), "SELECT * FROM tablename NATURAL INNER JOIN table2");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinInner("table2", Select.ON, "tablename.propertyInt = table2.propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename INNER JOIN table2 ON (tablename.propertyInt = table2.propertyInt)");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinInner("table2", Select.USING, "propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename INNER JOIN table2 USING (propertyInt)");
        assertTrue(execute(query));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testJoinOuterOracle() {
        var query = new Select(ORACLE);

        query.from("tablename")
            .joinOuter("table2", Select.FULL, Select.NATURAL, null);
        assertEquals(query.getSql(), "SELECT * FROM tablename NATURAL FULL OUTER JOIN table2");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinOuter("table2", Select.LEFT, Select.NATURAL, null);
        assertEquals(query.getSql(), "SELECT * FROM tablename NATURAL LEFT OUTER JOIN table2");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinOuter("table2", Select.RIGHT, Select.NATURAL, null);
        assertEquals(query.getSql(), "SELECT * FROM tablename NATURAL RIGHT OUTER JOIN table2");
        assertTrue(execute(query));
        query.clear();

        query.from("tablename")
            .joinOuter("table2", Select.FULL, Select.ON, "tablename.propertyInt = table2.propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename FULL OUTER JOIN table2 ON (tablename.propertyInt = table2.propertyInt)");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinOuter("table2", Select.LEFT, Select.ON, "tablename.propertyInt = table2.propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename LEFT OUTER JOIN table2 ON (tablename.propertyInt = table2.propertyInt)");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinOuter("table2", Select.RIGHT, Select.ON, "tablename.propertyInt = table2.propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename RIGHT OUTER JOIN table2 ON (tablename.propertyInt = table2.propertyInt)");
        assertTrue(execute(query));
        query.clear();

        query.from("tablename")
            .joinOuter("table2", Select.FULL, Select.USING, "propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename FULL OUTER JOIN table2 USING (propertyInt)");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinOuter("table2", Select.LEFT, Select.USING, "propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename LEFT OUTER JOIN table2 USING (propertyInt)");
        assertTrue(execute(query));
        query.clear();
        query.from("tablename")
            .joinOuter("table2", Select.RIGHT, Select.USING, "propertyInt");
        assertEquals(query.getSql(), "SELECT * FROM tablename RIGHT OUTER JOIN table2 USING (propertyInt)");
        assertTrue(execute(query));
        query.clear();
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testLimitOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .limit(3);
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
        query.offset(1);
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testLimitParameterOracle() {
        var query = new Select(ORACLE);
        query.from("tablename")
            .limitParameter("limit");
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }

        query.offsetParameter("offset");
        try {
            query.getSql();
            fail();
        } catch (UnsupportedSqlFeatureException e) {
            assertTrue(true);
        }
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testSubselectParamsOracle() {
        var fieldquery = new Select(ORACLE);
        fieldquery
            .from("table2")
            .field("min(propertyLong)")
            .whereParameter("propertyInt", ">");
        var tablequery = new Select(ORACLE);
        tablequery
            .from("table2")
            .whereParameter("propertyLong", "<");
        var wherequery = new Select(ORACLE);
        wherequery
            .from("table3")
            .field("max(propertyShort)")
            .whereParameter("propertyShort", "!=");
        var unionquery1 = new Select(ORACLE);
        unionquery1
            .from("table2")
            .field("propertyString")
            .field("max(propertyByte)")
            .whereParameter("propertyByte", "=")
            .groupBy("propertyString");
        var unionquery2 = new Select(ORACLE);
        unionquery2
            .from("table2")
            .field("propertyStringBuffer")
            .field("min(propertyByte)")
            .whereParameter("propertyByte", ">")
            .groupBy("propertyStringBuffer");

        // Manual subselect creation
        var query = new Select(ORACLE);
        // shuffled the structure around a bit to test the correct order usage
        query
            .unionAll(unionquery1)
            .union(unionquery2)
            .where("tablename.propertyShort >= (" + wherequery + ")")
            .whereSubselect(wherequery)
            .whereParameterOr("tablename.propertyString", "propertyString", "=")
            .from("tablename")
            .join("(" + tablequery + ") tablesubselect")
            .tableSubselect(tablequery)
            .field("tablename.propertyString")
            .field("(" + fieldquery + ") AS propertyLong")
            .fieldSubselect(fieldquery);
        assertEquals(query.getSql(), "SELECT tablename.propertyString, (SELECT min(propertyLong) FROM table2 WHERE propertyInt > ?) AS propertyLong FROM tablename, (SELECT * FROM table2 WHERE propertyLong < ?) tablesubselect WHERE tablename.propertyShort >= (SELECT max(propertyShort) FROM table3 WHERE propertyShort != ?) OR tablename.propertyString = ? UNION ALL SELECT propertyString, max(propertyByte) FROM table2 WHERE propertyByte = ? GROUP BY propertyString UNION SELECT propertyStringBuffer, min(propertyByte) FROM table2 WHERE propertyByte > ? GROUP BY propertyStringBuffer");
        var parameters = query.getParameters().getOrderedNamesArray();
        assertEquals(6, parameters.length);
        assertEquals(parameters[0], "propertyInt");
        assertEquals(parameters[1], "propertyLong");
        assertEquals(parameters[2], "propertyShort");
        assertEquals(parameters[3], "propertyString");
        assertEquals(parameters[4], "propertyByte");
        assertEquals(parameters[5], "propertyByte");

        assertTrue(execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setInt("propertyInt", 1)
                    .setLong("propertyLong", 99999999)
                    .setShort("propertyShort", (short) 5)
                    .setString("propertyString", "thestring")
                    .setByte("propertyByte", (byte) 4);
            }
        }));

        //Automated subselect creation
        query = new Select(ORACLE);
        query
            .union(unionquery1)
            .union(unionquery2)
            .where("tablename.propertyShort", ">=", wherequery)
            .whereParameterOr("tablename.propertyString", "propertyString", "=")
            .whereOr("tablename.propertyFloat", ">", new Select(ORACLE)
                .from("table3")
                .field("max(propertyLong)")
                .whereParameter("propertyLong", "!="))
            .from("tablename", new Select(ORACLE)
                .from("tablename"))
            .join("tablesubselect", tablequery)
            .whereAnd("tablename.propertyDouble", "<=", new Select(ORACLE)
                .from("table2")
                .field("max(propertyFloat)")
                .whereParameter("propertyFloat", "!="))
            .field("tablename.propertyString")
            .field("propertyLong", fieldquery);
        assertEquals(query.getSql(), "SELECT tablename.propertyString, (SELECT min(propertyLong) FROM table2 WHERE propertyInt > ?) AS propertyLong FROM (SELECT * FROM tablename) tablename, (SELECT * FROM table2 WHERE propertyLong < ?) tablesubselect WHERE tablename.propertyShort >= (SELECT max(propertyShort) FROM table3 WHERE propertyShort != ?) OR tablename.propertyString = ? OR tablename.propertyFloat > (SELECT max(propertyLong) FROM table3 WHERE propertyLong != ?) AND tablename.propertyDouble <= (SELECT max(propertyFloat) FROM table2 WHERE propertyFloat != ?) UNION SELECT propertyString, max(propertyByte) FROM table2 WHERE propertyByte = ? GROUP BY propertyString UNION SELECT propertyStringBuffer, min(propertyByte) FROM table2 WHERE propertyByte > ? GROUP BY propertyStringBuffer");
        parameters = query.getParameters().getOrderedNamesArray();
        assertEquals(8, parameters.length);
        assertEquals(parameters[0], "propertyInt");
        assertEquals(parameters[1], "propertyLong");
        assertEquals(parameters[2], "propertyShort");
        assertEquals(parameters[3], "propertyString");
        assertEquals(parameters[4], "propertyLong");
        assertEquals(parameters[5], "propertyFloat");
        assertEquals(parameters[6], "propertyByte");
        assertEquals(parameters[7], "propertyByte");

        assertTrue(execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setInt("propertyInt", 1)
                    .setLong("propertyLong", 99999999)
                    .setShort("propertyShort", (short) 5)
                    .setString("propertyString", "thestring")
                    .setFloat("propertyFloat", -1f)
                    .setByte("propertyByte", (byte) 4);
            }
        }));
    }

    @DatasourceEnabledIf(TestDatasourceIdentifier.ORACLE)
    void testCloneOracle() {
        var fieldquery = new Select(ORACLE);
        fieldquery
            .from("table2")
            .field("min(propertyLong)")
            .whereParameter("propertyInt", ">");
        var tablequery = new Select(ORACLE);
        tablequery
            .from("table2")
            .whereParameter("propertyLong", "<");
        var wherequery = new Select(ORACLE);
        wherequery
            .from("table3")
            .field("max(propertyShort)")
            .whereParameter("propertyShort", "!=");
        var unionquery1 = new Select(ORACLE);
        unionquery1
            .from("table2")
            .field("propertyString")
            .field("max(propertyByte)")
            .whereParameter("propertyByte", "=")
            .groupBy("propertyString");
        var unionquery2 = new Select(ORACLE);
        unionquery2
            .from("table2")
            .field("propertyStringBuffer")
            .field("min(propertyByte)")
            .whereParameter("propertyByte", ">")
            .groupBy("propertyStringBuffer");
        var query = new Select(ORACLE);
        query
            .hint("NO_INDEX")
            .from("tablename")
            .join("(" + tablequery + ") tablesubselect")
            .tableSubselect(tablequery)
            .joinCross("table3")
            .joinOuter("table2", Select.RIGHT, Select.ON, "table3.propertyInt = table2.propertyInt")
            .field("tablename.propertyString")
            .field("(" + fieldquery + ") propertyLongNew")
            .fieldSubselect(fieldquery)
            .where("tablename.propertyShort >= (" + wherequery + ")")
            .whereSubselect(wherequery)
            .whereParameterOr("tablename.propertyString", "propertyString", "=")
            .whereOr("tablename.propertyByte", "=", (byte) 54)
            .whereAnd("tablename.propertyDouble", "!=", 73453.71d)
            .whereParameterOr("tablename.propertyInt", "propertyInt", "=")
            .whereParameterAnd("tablename.propertyLong", "propertyLong", "<")
            .whereParameterOr("tablename.propertyChar", "propertyChar", "=")
            .groupBy("tablename.propertyShort")
            .groupBy("tablename.propertyLong")
            .groupBy("tablename.propertyString")
            .having("tablename.propertyLong = 1")
            .unionAll(unionquery1)
            .union(unionquery2);
        var query_clone = query.clone();
        assertEquals(query.getSql(), query_clone.getSql());
        assertNotSame(query, query_clone);
        execute(query, new DbPreparedStatementHandler<>() {
            public void setParameters(DbPreparedStatement statement) {
                statement
                    .setInt("propertyInt", 1)
                    .setLong("propertyLong", 99999999)
                    .setShort("propertyShort", (short) 5)
                    .setString("propertyString", "thestring")
                    .setByte("propertyByte", (byte) 4)
                    .setString("propertyChar", "c");
            }
        });
    }
}

