/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.tools;

import org.junit.jupiter.api.Test;
import rife.config.RifeConfig;
import rife.tools.exceptions.BeanUtilsException;
import rife.tools.exceptions.SerializationUtilsErrorException;
import rife.validation.ConstrainedProperty;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestBeanUtils {
    private BeanImpl getPopulatedBean() {
        BeanImpl bean = new BeanImpl();
        var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
        bean.setPropertyString("thisisastring");
        bean.setPropertyStringBuffer(new StringBuffer("butthisisastringbuffer"));
        bean.setPropertyDate(Convert.toDate(cal));
        bean.setPropertyCalendar(cal);
        bean.setPropertySqlDate(Convert.toSqlDate(cal));
        bean.setPropertyTime(Convert.toSqlTime(cal));
        bean.setPropertyTimestamp(Convert.toSqlTimestamp(cal));
        bean.setPropertyInstant(Convert.toInstant(cal));
        bean.setPropertyLocalDateTime(Convert.toLocalDateTime(cal));
        bean.setPropertyLocalDate(Convert.toLocalDate(cal));
        bean.setPropertyLocalTime(Convert.toLocalTime(cal));
        bean.setPropertyChar('g');
        bean.setPropertyBoolean(false);
        bean.setPropertyByte((byte) 53);
        bean.setPropertyDouble(84578.42d);
        bean.setPropertyFloat(35523.967f);
        bean.setPropertyInt(978);
        bean.setPropertyLong(87346L);
        bean.setPropertyShort((short) 31);
        bean.setPropertyBigDecimal(new BigDecimal("8347365990.387437894678"));

        return bean;
    }

    @Test
    void testSetUppercaseBeanPropertyIllegalArguments()
    throws BeanUtilsException {
        Map<String, PropertyDescriptor> bean_properties = BeanUtils.getUppercasedBeanProperties(BeanImpl2.class);

        try {
            BeanUtils.setUppercasedBeanProperty(null, null, null, bean_properties, new BeanImpl2(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            BeanUtils.setUppercasedBeanProperty("propertyString", null, null, null, new BeanImpl2(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            BeanUtils.setUppercasedBeanProperty("propertyString", null, null, bean_properties, null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException ignored) {
        }

        try {
            BeanUtils.setUppercasedBeanProperty("propertyString", null, null, bean_properties, new BeanImpl2(), null);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException not expected.");
        }
    }

    @Test
    void testSetUppercaseBeanPropertyNoOpArguments()
    throws BeanUtilsException {
        BeanImpl2 bean;
        Map<String, PropertyDescriptor> bean_properties = BeanUtils.getUppercasedBeanProperties(BeanImpl2.class);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyString", null, null, bean_properties, bean, null);
        assertNull(bean.getPropertyString());

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyString", new String[0], null, bean_properties, bean, null);
        assertNull(bean.getPropertyString());

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyString", new String[]{"one", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyString(), "one");
    }

    @Test
    void testSetUppercaseBeanPropertyNoSetter()
    throws BeanUtilsException {
        BeanImpl2 bean;
        Map<String, PropertyDescriptor> bean_properties = BeanUtils.getUppercasedBeanProperties(BeanImpl2.class);

        bean = new BeanImpl2();
        assertEquals(bean.getPropertyReadonly(), 23L);
        BeanUtils.setUppercasedBeanProperty("propertyReadonly", new String[]{"42131"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyReadonly(), 23L);
    }

    @Test
    void testSetUppercaseBeanProperty()
    throws BeanUtilsException, ParseException, SerializationUtilsErrorException {
        BeanImpl2 bean;
        Map<String, PropertyDescriptor> bean_properties = BeanUtils.getUppercasedBeanProperties(BeanImpl2.class);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyString", new String[]{"one", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyString(), "one");

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyInt", new String[]{"438", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyInt(), 438);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyChar", new String[]{"E", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyChar(), 'E');

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyBoolean", new String[]{"true", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertTrue(bean.isPropertyBoolean());

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyByte", new String[]{"27", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyByte(), 27);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyDouble", new String[]{"80756.6287", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyDouble(), 80756.6287d);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyFloat", new String[]{"435.557", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyFloat(), 435.557f);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLong", new String[]{"122875", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyLong(), 122875);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyShort", new String[]{"3285", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyShort(), 3285);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyBigDecimal", new String[]{"983743.343", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyBigDecimal(), new BigDecimal("983743.343"));

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyIntegerObject", new String[]{"438", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyIntegerObject(), 438);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyCharacterObject", new String[]{"E", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyCharacterObject(), 'E');

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyBooleanObject", new String[]{"true", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyBooleanObject(), Boolean.TRUE);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyByteObject", new String[]{"27", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyByteObject(), (byte) 27);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyDoubleObject", new String[]{"80756.6287", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyDoubleObject(), 80756.6287d);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyFloatObject", new String[]{"435.557", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyFloatObject(), 435.557f);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLongObject", new String[]{"122875", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyLongObject(), 122875);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyShortObject", new String[]{"3285", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyShortObject(), (short) 3285);

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyStringBuffer", new String[]{"one1", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyStringBuffer().toString(), "one1");

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyStringBuilder", new String[]{"one2", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyStringBuilder().toString(), "one2");

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyDate", new String[]{"2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyDate(), RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45"));

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyInstant", new String[]{"2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyInstant(), Convert.toInstant(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")));

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDateTime", new String[]{"2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyLocalDateTime(), Convert.toLocalDateTime(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")));

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDate", new String[]{"2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyLocalDate(), Convert.toLocalDate(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 00:00")));

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLocalTime", new String[]{"10:45", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertyLocalTime(), Convert.toLocalTime(RifeConfig.tools().getDefaultInputTimeFormat().parse("10:45")));


        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyStringArray", new String[]{"one", "two"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyStringArray(), new String[]{"one", "two"});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyIntArray", new String[]{"438", "98455", "711"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyIntArray(), new int[]{438, 98455, 711});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyCharArray", new String[]{"E", "a", "x"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyCharArray(), new char[]{'E', 'a', 'x'});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyBooleanArray", new String[]{"true", "0", "t", "1"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyBooleanArray(), new boolean[]{true, false, true, true});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyByteArray", new String[]{"27", "78"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyByteArray(), new byte[]{27, 78});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyDoubleArray", new String[]{"80756.6287", "3214.75", "85796.6237"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyDoubleArray(), new double[]{80756.6287d, 3214.75d, 85796.6237d});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyFloatArray", new String[]{"435.557", "589.5"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyFloatArray(), new float[]{435.557f, 589.5f});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLongArray", new String[]{"122875", "8526780", "3826589"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyLongArray(), new long[]{122875, 8526780, 3826589});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyShortArray", new String[]{"3285", "58"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyShortArray(), new short[]{3285, 58});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyIntegerObjectArray", new String[]{"438", "7865", "475"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyIntegerObjectArray(), new Integer[]{438, 7865, 475});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyCharacterObjectArray", new String[]{"E", "z"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyCharacterObjectArray(), new Character[]{'E', 'z'});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyBooleanObjectArray", new String[]{"fslse", "1", "true"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyBooleanObjectArray(), new Boolean[]{false, true, true});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyByteObjectArray", new String[]{"27", "78"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyByteObjectArray(), new Byte[]{(byte) 27, (byte) 78});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyDoubleObjectArray", new String[]{"80756.6287", "5876.14", "3268.57"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyDoubleObjectArray(), new Double[]{80756.6287d, 5876.14d, 3268.57d});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyFloatObjectArray", new String[]{"435.557", "7865.66"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyFloatObjectArray(), new Float[]{435.557f, 7865.66f});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLongObjectArray", new String[]{"122875", "5687621", "66578"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyLongObjectArray(), new Long[]{122875L, 5687621L, 66578L});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyShortObjectArray", new String[]{"3285", "6588"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyShortObjectArray(), new Short[]{(short) 3285, (short) 6588});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyBigDecimalArray", new String[]{"32859837434343983.83749837498373434", "65884343.343"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyBigDecimalArray(), new BigDecimal[]{new BigDecimal("32859837434343983.83749837498373434"), new BigDecimal("65884343343E-3")});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyStringBufferArray", new String[]{"one1", "two2"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(ArrayUtils.createStringArray(bean.getPropertyStringBufferArray()), new String[]{"one1", "two2"});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyStringBuilderArray", new String[]{"three3", "four4"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(ArrayUtils.createStringArray(bean.getPropertyStringBuilderArray()), new String[]{"three3", "four4"});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyDateArray", new String[]{"2006-08-04 10:45", "2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyDateArray(), new Date[]{RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45"), RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 11:05")});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyInstantArray", new String[]{"2006-08-04 10:45", "2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyInstantArray(), new Instant[]{Convert.toInstant(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")), Convert.toInstant(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 11:05"))});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDateTimeArray", new String[]{"2006-08-04 10:45", "2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyLocalDateTimeArray(), new LocalDateTime[]{Convert.toLocalDateTime(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")), Convert.toLocalDateTime(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 11:05"))});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDateArray", new String[]{"2006-08-04 10:45", "2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyLocalDateArray(), new LocalDate[]{Convert.toLocalDate(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 00:00")), Convert.toLocalDate(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 00:00"))});

        bean = new BeanImpl2();
        BeanUtils.setUppercasedBeanProperty("propertyLocalTimeArray", new String[]{"10:45", "11:05"}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertyLocalTimeArray(), new LocalTime[]{Convert.toLocalTime(RifeConfig.tools().getDefaultInputTimeFormat().parse("10:45")), Convert.toLocalTime(RifeConfig.tools().getDefaultInputTimeFormat().parse("11:05"))});
    }

    @Test
    void testSetUppercaseBeanPropertyConstrained()
    throws BeanUtilsException, ParseException, SerializationUtilsErrorException {
        BeanImpl3 bean;
        Map<String, PropertyDescriptor> bean_properties = BeanUtils.getUppercasedBeanProperties(BeanImpl3.class);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyDate", new String[]{"custom format 2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyDate(), RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45"));

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyInstant", new String[]{"custom format 2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyInstant(), Convert.toInstant(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")));

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDateTime", new String[]{"custom format 2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyLocalDateTime(), Convert.toLocalDateTime(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")));

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDate", new String[]{"custom format 2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyLocalDate(), Convert.toLocalDate(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 00:00")));

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLocalTime", new String[]{"custom format 2006-08-04 10:45", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyLocalTime(), Convert.toLocalTime(RifeConfig.tools().getDefaultInputTimeFormat().parse("10:45")));

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyInt", new String[]{"$438", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyInt(), 438);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyByte", new String[]{"2,700%", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyByte(), 27);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyDouble", new String[]{"80,756.6287", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyDouble(), 80756.6287d);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyFloat", new String[]{"435,557", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyFloat(), 435.557f);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLong", new String[]{"$122,875.00", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyLong(), 122875);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyShort", new String[]{"¤3285", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyShort(), 3285);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyBigDecimal", new String[]{"4353344987349830948394893,55709384093", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyBigDecimal(), new BigDecimal("435334498734983094839489355709384093E-11"));

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyIntegerObject", new String[]{"$438", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyIntegerObject(), 438);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyByteObject", new String[]{"2,700%", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyByteObject(), (byte) 27);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyDoubleObject", new String[]{"80,756.6287", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyDoubleObject(), 80756.6287d);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyFloatObject", new String[]{"435,557", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyFloatObject(), 435.557f);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLongObject", new String[]{"$122,875.00", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyLongObject(), 122875);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyShortObject", new String[]{"¤3285", "two"}, null, bean_properties, bean, new BeanImpl3());
        assertEquals(bean.getPropertyShortObject(), (short) 3285);

        bean = new BeanImpl3();
        BeanImpl3.SerializableType serializable = new BeanImpl3.SerializableType(5686, "Testing");
        BeanUtils.setUppercasedBeanProperty("propertySerializableType", new String[]{SerializationUtils.serializeToString(serializable), "two"}, null, bean_properties, bean, new BeanImpl2());
        assertEquals(bean.getPropertySerializableType(), serializable);

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyDateArray", new String[]{"custom format 2006-08-04 10:45", "custom format 2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyDateArray(), new Date[]{RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45"), RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 11:05")});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyInstantArray", new String[]{"custom format 2006-08-04 10:45", "custom format 2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyInstantArray(), new Instant[]{Convert.toInstant(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")), Convert.toInstant(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 11:05"))});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDateTimeArray", new String[]{"custom format 2006-08-04 10:45", "custom format 2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyLocalDateTimeArray(), new LocalDateTime[]{Convert.toLocalDateTime(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 10:45")), Convert.toLocalDateTime(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 11:05"))});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLocalDateArray", new String[]{"custom format 2006-08-04 10:45", "custom format 2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyLocalDateArray(), new LocalDate[]{Convert.toLocalDate(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-08-04 00:00")), Convert.toLocalDate(RifeConfig.tools().getDefaultInputDateFormat().parse("2006-07-08 00:00"))});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLocalTimeArray", new String[]{"custom format 2006-08-04 10:45", "custom format 2006-07-08 11:05"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyLocalTimeArray(), new LocalTime[]{Convert.toLocalTime(RifeConfig.tools().getDefaultInputTimeFormat().parse("10:45")), Convert.toLocalTime(RifeConfig.tools().getDefaultInputTimeFormat().parse("11:05"))});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyIntArray", new String[]{"$438", "$98455", "$711"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyIntArray(), new int[]{438, 98455, 711});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyByteArray", new String[]{"2,700%", "7,800%"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyByteArray(), new byte[]{27, 78});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyDoubleArray", new String[]{"80,756.6287", "3,214.75", "85,796.6237"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyDoubleArray(), new double[]{80756.6287d, 3214.75d, 85796.6237d});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyFloatArray", new String[]{"435,557", "589,5"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyFloatArray(), new float[]{435.557f, 589.5f});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLongArray", new String[]{"$122,875.00", "$8,526,780.00", "$3,826,589.00"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyLongArray(), new long[]{122875, 8526780, 3826589});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyShortArray", new String[]{"¤3285", "¤58"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyShortArray(), new short[]{3285, 58});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyIntegerObjectArray", new String[]{"$438", "$7865", "$475"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyIntegerObjectArray(), new Integer[]{438, 7865, 475});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyByteObjectArray", new String[]{"2,700%", "7,800%"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyByteObjectArray(), new Byte[]{(byte) 27, (byte) 78});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyDoubleObjectArray", new String[]{"80,756.6287", "5,876.14", "3,268.57"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyDoubleObjectArray(), new Double[]{80756.6287d, 5876.14d, 3268.57d});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyFloatObjectArray", new String[]{"435,557", "7865,66"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyFloatObjectArray(), new Float[]{435.557f, 7865.66f});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyLongObjectArray", new String[]{"$122,875.00", "$5,687,621.00", "$66,578.00"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyLongObjectArray(), new Long[]{122875L, 5687621L, 66578L});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyShortObjectArray", new String[]{"¤3285", "¤6588"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyShortObjectArray(), new Short[]{(short) 3285, (short) 6588});

        bean = new BeanImpl3();
        BeanUtils.setUppercasedBeanProperty("propertyBigDecimalArray", new String[]{"97687687998978673545669789,0000000000001", "34353"}, null, bean_properties, bean, new BeanImpl3());
        assertArrayEquals(bean.getPropertyBigDecimalArray(), new BigDecimal[]{new BigDecimal("976876879989786735456697890000000000001E-13"), new BigDecimal("3.4353E4")});

        bean = new BeanImpl3();
        BeanImpl3.SerializableType serializable1 = new BeanImpl3.SerializableType(5682, "AnotherTest");
        BeanImpl3.SerializableType serializable2 = new BeanImpl3.SerializableType(850, "WhatTest");
        BeanUtils.setUppercasedBeanProperty("propertySerializableTypeArray", new String[]{SerializationUtils.serializeToString(serializable1), SerializationUtils.serializeToString(serializable2)}, null, bean_properties, bean, new BeanImpl2());
        assertArrayEquals(bean.getPropertySerializableTypeArray(), new BeanImpl3.SerializableType[]{serializable1, serializable2});
    }

    @Test
    void testPropertyNamesIllegal() {
        try {
            assertEquals(0, BeanUtils.getPropertyNames(null, null, null, null).size());
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesEmpty() {
        try {
            assertEquals(0, BeanUtils.getPropertyNames(Object.class, null, null, null).size());
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNames() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class, null, null, null);
            assertEquals(property_names.size(), 20);
            assertTrue(property_names.contains("propertyString"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyDate"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertySqlDate"));
            assertTrue(property_names.contains("propertyTime"));
            assertTrue(property_names.contains("propertyTimestamp"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDateTime"));
            assertTrue(property_names.contains("propertyLocalDate"));
            assertTrue(property_names.contains("propertyLocalTime"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyBoolean"));
            assertTrue(property_names.contains("propertyByte"));
            assertTrue(property_names.contains("propertyDouble"));
            assertTrue(property_names.contains("propertyFloat"));
            assertTrue(property_names.contains("propertyInt"));
            assertTrue(property_names.contains("propertyLong"));
            assertTrue(property_names.contains("propertyShort"));
            assertTrue(property_names.contains("propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class, null, null, null);
            assertEquals(property_names.size(), 21);
            assertTrue(property_names.contains("propertyReadonly"));
            assertTrue(property_names.contains("propertyString"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyDate"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertySqlDate"));
            assertTrue(property_names.contains("propertyTime"));
            assertTrue(property_names.contains("propertyTimestamp"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDateTime"));
            assertTrue(property_names.contains("propertyLocalDate"));
            assertTrue(property_names.contains("propertyLocalTime"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyBoolean"));
            assertTrue(property_names.contains("propertyByte"));
            assertTrue(property_names.contains("propertyDouble"));
            assertTrue(property_names.contains("propertyFloat"));
            assertTrue(property_names.contains("propertyInt"));
            assertTrue(property_names.contains("propertyLong"));
            assertTrue(property_names.contains("propertyShort"));
            assertTrue(property_names.contains("propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class, null, null, null);
            assertEquals(property_names.size(), 21);
            assertTrue(property_names.contains("propertyWriteOnly"));
            assertTrue(property_names.contains("propertyString"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyDate"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertySqlDate"));
            assertTrue(property_names.contains("propertyTime"));
            assertTrue(property_names.contains("propertyTimestamp"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDateTime"));
            assertTrue(property_names.contains("propertyLocalDate"));
            assertTrue(property_names.contains("propertyLocalTime"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyBoolean"));
            assertTrue(property_names.contains("propertyByte"));
            assertTrue(property_names.contains("propertyDouble"));
            assertTrue(property_names.contains("propertyFloat"));
            assertTrue(property_names.contains("propertyInt"));
            assertTrue(property_names.contains("propertyLong"));
            assertTrue(property_names.contains("propertyShort"));
            assertTrue(property_names.contains("propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesPrefix() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class, null, null, "PREFIX:");
            assertEquals(property_names.size(), 20);
            assertTrue(property_names.contains("PREFIX:propertyString"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyDate"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertySqlDate"));
            assertTrue(property_names.contains("PREFIX:propertyTime"));
            assertTrue(property_names.contains("PREFIX:propertyTimestamp"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDateTime"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDate"));
            assertTrue(property_names.contains("PREFIX:propertyLocalTime"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyBoolean"));
            assertTrue(property_names.contains("PREFIX:propertyByte"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
            assertTrue(property_names.contains("PREFIX:propertyFloat"));
            assertTrue(property_names.contains("PREFIX:propertyInt"));
            assertTrue(property_names.contains("PREFIX:propertyLong"));
            assertTrue(property_names.contains("PREFIX:propertyShort"));
            assertTrue(property_names.contains("PREFIX:propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesPrefixGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class, null, null, "PREFIX:");
            assertEquals(property_names.size(), 21);
            assertTrue(property_names.contains("PREFIX:propertyReadonly"));
            assertTrue(property_names.contains("PREFIX:propertyString"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyDate"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertySqlDate"));
            assertTrue(property_names.contains("PREFIX:propertyTime"));
            assertTrue(property_names.contains("PREFIX:propertyTimestamp"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDateTime"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDate"));
            assertTrue(property_names.contains("PREFIX:propertyLocalTime"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyBoolean"));
            assertTrue(property_names.contains("PREFIX:propertyByte"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
            assertTrue(property_names.contains("PREFIX:propertyFloat"));
            assertTrue(property_names.contains("PREFIX:propertyInt"));
            assertTrue(property_names.contains("PREFIX:propertyLong"));
            assertTrue(property_names.contains("PREFIX:propertyShort"));
            assertTrue(property_names.contains("PREFIX:propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesPrefixSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class, null, null, "PREFIX:");
            assertEquals(property_names.size(), 21);
            assertTrue(property_names.contains("PREFIX:propertyWriteOnly"));
            assertTrue(property_names.contains("PREFIX:propertyString"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyDate"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertySqlDate"));
            assertTrue(property_names.contains("PREFIX:propertyTime"));
            assertTrue(property_names.contains("PREFIX:propertyTimestamp"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDateTime"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDate"));
            assertTrue(property_names.contains("PREFIX:propertyLocalTime"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyBoolean"));
            assertTrue(property_names.contains("PREFIX:propertyByte"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
            assertTrue(property_names.contains("PREFIX:propertyFloat"));
            assertTrue(property_names.contains("PREFIX:propertyInt"));
            assertTrue(property_names.contains("PREFIX:propertyLong"));
            assertTrue(property_names.contains("PREFIX:propertyShort"));
            assertTrue(property_names.contains("PREFIX:propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesIncluded() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null,
                null);
            assertEquals(property_names.size(), 9);
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertySqlDate"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDate"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyByte"));
            assertTrue(property_names.contains("propertyDouble"));
            assertTrue(property_names.contains("propertyShort"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesIncludedGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null,
                null);
            assertEquals(property_names.size(), 10);
            assertTrue(property_names.contains("propertyReadonly"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertySqlDate"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDate"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyByte"));
            assertTrue(property_names.contains("propertyDouble"));
            assertTrue(property_names.contains("propertyShort"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesIncludedSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null,
                null);
            assertEquals(property_names.size(), 10);
            assertTrue(property_names.contains("propertyWriteOnly"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertySqlDate"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDate"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyByte"));
            assertTrue(property_names.contains("propertyDouble"));
            assertTrue(property_names.contains("propertyShort"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesIncludedPrefix() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_names.size(), 9);
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertySqlDate"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDate"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyByte"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
            assertTrue(property_names.contains("PREFIX:propertyShort"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesIncludedPrefixGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_names.size(), 10);
            assertTrue(property_names.contains("PREFIX:propertyReadonly"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertySqlDate"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDate"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyByte"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
            assertTrue(property_names.contains("PREFIX:propertyShort"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesIncludedPrefixSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_names.size(), 10);
            assertTrue(property_names.contains("PREFIX:propertyWriteOnly"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertySqlDate"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDate"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyByte"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
            assertTrue(property_names.contains("PREFIX:propertyShort"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesExcluded() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class,
                null,
                new String[]{"propertyStringBuffer", "propertyCalendar", "propertySqlDate", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null);
            assertEquals(property_names.size(), 12);
            assertTrue(property_names.contains("propertyString"));
            assertTrue(property_names.contains("propertyDate"));
            assertTrue(property_names.contains("propertyTime"));
            assertTrue(property_names.contains("propertyTimestamp"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDateTime"));
            assertTrue(property_names.contains("propertyLocalTime"));
            assertTrue(property_names.contains("propertyBoolean"));
            assertTrue(property_names.contains("propertyFloat"));
            assertTrue(property_names.contains("propertyInt"));
            assertTrue(property_names.contains("propertyLong"));
            assertTrue(property_names.contains("propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesExcludedGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                null,
                new String[]{"propertyStringBuffer", "propertyCalendar", "propertySqlDate", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null);
            assertEquals(property_names.size(), 13);
            assertTrue(property_names.contains("propertyReadonly"));
            assertTrue(property_names.contains("propertyString"));
            assertTrue(property_names.contains("propertyDate"));
            assertTrue(property_names.contains("propertyTime"));
            assertTrue(property_names.contains("propertyTimestamp"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDateTime"));
            assertTrue(property_names.contains("propertyLocalTime"));
            assertTrue(property_names.contains("propertyBoolean"));
            assertTrue(property_names.contains("propertyFloat"));
            assertTrue(property_names.contains("propertyInt"));
            assertTrue(property_names.contains("propertyLong"));
            assertTrue(property_names.contains("propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesExcludedSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                null,
                new String[]{"propertyStringBuffer", "propertyCalendar", "propertySqlDate", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null);
            assertEquals(property_names.size(), 13);
            assertTrue(property_names.contains("propertyWriteOnly"));
            assertTrue(property_names.contains("propertyString"));
            assertTrue(property_names.contains("propertyDate"));
            assertTrue(property_names.contains("propertyTime"));
            assertTrue(property_names.contains("propertyTimestamp"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyLocalDateTime"));
            assertTrue(property_names.contains("propertyLocalTime"));
            assertTrue(property_names.contains("propertyBoolean"));
            assertTrue(property_names.contains("propertyFloat"));
            assertTrue(property_names.contains("propertyInt"));
            assertTrue(property_names.contains("propertyLong"));
            assertTrue(property_names.contains("propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesExcludedPrefix() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(property_names.size(), 12);
            assertTrue(property_names.contains("PREFIX:propertyString"));
            assertTrue(property_names.contains("PREFIX:propertyDate"));
            assertTrue(property_names.contains("PREFIX:propertyTime"));
            assertTrue(property_names.contains("PREFIX:propertyTimestamp"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDateTime"));
            assertTrue(property_names.contains("PREFIX:propertyLocalTime"));
            assertTrue(property_names.contains("PREFIX:propertyBoolean"));
            assertTrue(property_names.contains("PREFIX:propertyFloat"));
            assertTrue(property_names.contains("PREFIX:propertyInt"));
            assertTrue(property_names.contains("PREFIX:propertyLong"));
            assertTrue(property_names.contains("PREFIX:propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesExcludedPrefixGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(property_names.size(), 13);
            assertTrue(property_names.contains("PREFIX:propertyReadonly"));
            assertTrue(property_names.contains("PREFIX:propertyString"));
            assertTrue(property_names.contains("PREFIX:propertyDate"));
            assertTrue(property_names.contains("PREFIX:propertyTime"));
            assertTrue(property_names.contains("PREFIX:propertyTimestamp"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDateTime"));
            assertTrue(property_names.contains("PREFIX:propertyLocalTime"));
            assertTrue(property_names.contains("PREFIX:propertyBoolean"));
            assertTrue(property_names.contains("PREFIX:propertyFloat"));
            assertTrue(property_names.contains("PREFIX:propertyInt"));
            assertTrue(property_names.contains("PREFIX:propertyLong"));
            assertTrue(property_names.contains("PREFIX:propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesExcludedPrefixSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(property_names.size(), 13);
            assertTrue(property_names.contains("PREFIX:propertyWriteOnly"));
            assertTrue(property_names.contains("PREFIX:propertyString"));
            assertTrue(property_names.contains("PREFIX:propertyDate"));
            assertTrue(property_names.contains("PREFIX:propertyTime"));
            assertTrue(property_names.contains("PREFIX:propertyTimestamp"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyLocalDateTime"));
            assertTrue(property_names.contains("PREFIX:propertyLocalTime"));
            assertTrue(property_names.contains("PREFIX:propertyBoolean"));
            assertTrue(property_names.contains("PREFIX:propertyFloat"));
            assertTrue(property_names.contains("PREFIX:propertyInt"));
            assertTrue(property_names.contains("PREFIX:propertyLong"));
            assertTrue(property_names.contains("PREFIX:propertyBigDecimal"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesFiltered() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                new String[]{"propertySqlDate", "propertyLocalDate", "propertyByte", "propertyShort"},
                null);
            assertEquals(property_names.size(), 5);
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyDouble"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesFilteredGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                new String[]{"propertySqlDate", "propertyLocalDate", "propertyByte", "propertyShort"},
                null);
            assertEquals(property_names.size(), 6);
            assertTrue(property_names.contains("propertyReadonly"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyDouble"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesFilteredSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                new String[]{"propertySqlDate", "propertyLocalDate", "propertyByte", "propertyShort"},
                null);
            assertEquals(property_names.size(), 6);
            assertTrue(property_names.contains("propertyWriteOnly"));
            assertTrue(property_names.contains("propertyStringBuffer"));
            assertTrue(property_names.contains("propertyCalendar"));
            assertTrue(property_names.contains("propertyInstant"));
            assertTrue(property_names.contains("propertyChar"));
            assertTrue(property_names.contains("propertyDouble"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesFilteredPrefix() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyByte", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(property_names.size(), 5);
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesFilteredPrefixGetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyByte", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(property_names.size(), 6);
            assertTrue(property_names.contains("PREFIX:propertyReadonly"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyNamesFilteredPrefixSetters() {
        try {
            Set<String> property_names = BeanUtils.getPropertyNames(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyByte", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(property_names.size(), 6);
            assertTrue(property_names.contains("PREFIX:propertyWriteOnly"));
            assertTrue(property_names.contains("PREFIX:propertyStringBuffer"));
            assertTrue(property_names.contains("PREFIX:propertyCalendar"));
            assertTrue(property_names.contains("PREFIX:propertyInstant"));
            assertTrue(property_names.contains("PREFIX:propertyChar"));
            assertTrue(property_names.contains("PREFIX:propertyDouble"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIllegal() {
        try {
            assertEquals(0, BeanUtils.countProperties(null, null, null, null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountProperties() {
        try {
            int count = BeanUtils.countProperties(BeanImpl.class, null, null, null);
            assertEquals(count, 20);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesGetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class, null, null, null);
            assertEquals(count, 21);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesSetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class, null, null, null);
            assertEquals(count, 21);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesPrefix() {
        try {
            int count = BeanUtils.countProperties(BeanImpl.class, null, null, "PREFIX:");
            assertEquals(count, 20);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesPrefixGetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class, null, null, "PREFIX:");
            assertEquals(count, 21);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesPrefixSetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class, null, null, "PREFIX:");
            assertEquals(count, 21);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIncluded() {
        try {
            assertEquals(9, BeanUtils.countProperties(BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null,
                null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIncludedGetters() {
        try {
            assertEquals(10, BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null,
                null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIncludedSetters() {
        try {
            assertEquals(10, BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null,
                null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIncludedPrefix() {
        try {
            assertEquals(9, BeanUtils.countProperties(BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                null,
                "PREFIX:"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIncludedPrefixGetters() {
        try {
            assertEquals(10, BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                null,
                "PREFIX:"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesIncludedPrefixSetters() {
        try {
            assertEquals(10, BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                null,
                "PREFIX:"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesExcluded() {
        try {
            int count = BeanUtils.countProperties(BeanImpl.class,
                null,
                new String[]{"propertyStringBuffer", "propertyCalendar", "propertySqlDate", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null);
            assertEquals(count, 12);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesExcludedGetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                null,
                new String[]{"propertyStringBuffer", "propertyCalendar", "propertySqlDate", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null);
            assertEquals(count, 13);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesExcludedSetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                null,
                new String[]{"propertyStringBuffer", "propertyCalendar", "propertySqlDate", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                null);
            assertEquals(count, 13);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesExcludedPrefix() {
        try {
            int count = BeanUtils.countProperties(BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(count, 12);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesExcludedPrefixGetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(count, 13);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesExcludedPrefixSetters() {
        try {
            int count = BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                "PREFIX:");
            assertEquals(count, 13);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesFiltered() {
        try {
            assertEquals(4, BeanUtils.countProperties(BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                new String[]{"propertyStringBuffer", "propertyLocalDate", "propertyChar", "propertyByte", "propertyShort"},
                null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesFilteredGetters() {
        try {
            assertEquals(5, BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                new String[]{"propertyStringBuffer", "propertyLocalDate", "propertyChar", "propertyByte", "propertyShort"},
                null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesFilteredSetters() {
        try {
            assertEquals(5, BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyStringBuffer", "propertyCalendar", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate",
                    "propertyChar", "propertyByte", "propertyDouble", "propertyShort"},
                new String[]{"propertyStringBuffer", "propertyLocalDate", "propertyChar", "propertyByte", "propertyShort"},
                null));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesFilteredPrefix() {
        try {
            assertEquals(4, BeanUtils.countProperties(BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyLocalDate", "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyShort"},
                "PREFIX:"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesFilteredPrefixGetters() {
        try {
            assertEquals(5, BeanUtils.countProperties(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyLocalDate", "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyShort"},
                "PREFIX:"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testCountPropertiesFilteredPrefixSetters() {
        try {
            assertEquals(5, BeanUtils.countProperties(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyStringBuffer", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyDouble", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyStringBuffer", "PREFIX:propertyLocalDate", "PREFIX:propertyChar", "PREFIX:propertyByte", "PREFIX:propertyShort"},
                "PREFIX:"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypeIllegal() {
        try {
            BeanUtils.getPropertyType(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyType(Object.class, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyType(Object.class, "");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyType() {
        try {
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyString"), String.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyStringBuffer"), StringBuffer.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyDate"), java.util.Date.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyCalendar"), java.util.Calendar.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertySqlDate"), java.sql.Date.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyTime"), java.sql.Time.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyInstant"), Instant.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyLocalDateTime"), LocalDateTime.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyLocalDate"), LocalDate.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyLocalTime"), LocalTime.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyChar"), char.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyBoolean"), boolean.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyByte"), byte.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyDouble"), double.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyFloat"), float.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyInt"), int.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyLong"), long.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyShort"), short.class);
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            assertSame(BeanUtils.getPropertyType(BeanImpl.class, "unknown"), String.class);
            fail();
        } catch (BeanUtilsException e) {
            assertSame(e.getBeanClass(), BeanImpl.class);
        }
    }

    @Test
    void testPropertyTypesIllegal() {
        try {
            assertEquals(0, BeanUtils.getPropertyTypes(null, null, null, null).size());
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypes() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class, null, null, null);
            assertEquals(property_types.size(), 20);
            assertTrue(property_types.containsKey("propertyString"));
            assertTrue(property_types.containsKey("propertyStringBuffer"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyCalendar"));
            assertTrue(property_types.containsKey("propertySqlDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyTimestamp"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDateTime"));
            assertTrue(property_types.containsKey("propertyLocalDate"));
            assertTrue(property_types.containsKey("propertyLocalTime"));
            assertTrue(property_types.containsKey("propertyChar"));
            assertTrue(property_types.containsKey("propertyBoolean"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyDouble"));
            assertTrue(property_types.containsKey("propertyFloat"));
            assertTrue(property_types.containsKey("propertyInt"));
            assertTrue(property_types.containsKey("propertyLong"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertTrue(property_types.containsKey("propertyBigDecimal"));
            assertSame(property_types.get("propertyString"), String.class);
            assertSame(property_types.get("propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyCalendar"), java.util.Calendar.class);
            assertSame(property_types.get("propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("propertyChar"), char.class);
            assertSame(property_types.get("propertyBoolean"), boolean.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyDouble"), double.class);
            assertSame(property_types.get("propertyFloat"), float.class);
            assertSame(property_types.get("propertyInt"), int.class);
            assertSame(property_types.get("propertyLong"), long.class);
            assertSame(property_types.get("propertyShort"), short.class);
            assertSame(property_types.get("propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class, null, null, null);
            assertEquals(property_types.size(), 21);
            assertTrue(property_types.containsKey("propertyReadonly"));
            assertTrue(property_types.containsKey("propertyString"));
            assertTrue(property_types.containsKey("propertyStringBuffer"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyCalendar"));
            assertTrue(property_types.containsKey("propertySqlDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyTimestamp"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDateTime"));
            assertTrue(property_types.containsKey("propertyLocalDate"));
            assertTrue(property_types.containsKey("propertyLocalTime"));
            assertTrue(property_types.containsKey("propertyChar"));
            assertTrue(property_types.containsKey("propertyBoolean"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyDouble"));
            assertTrue(property_types.containsKey("propertyFloat"));
            assertTrue(property_types.containsKey("propertyInt"));
            assertTrue(property_types.containsKey("propertyLong"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertTrue(property_types.containsKey("propertyBigDecimal"));
            assertSame(property_types.get("propertyReadonly"), int.class);
            assertSame(property_types.get("propertyString"), String.class);
            assertSame(property_types.get("propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyCalendar"), java.util.Calendar.class);
            assertSame(property_types.get("propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("propertyChar"), char.class);
            assertSame(property_types.get("propertyBoolean"), boolean.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyDouble"), double.class);
            assertSame(property_types.get("propertyFloat"), float.class);
            assertSame(property_types.get("propertyInt"), int.class);
            assertSame(property_types.get("propertyLong"), long.class);
            assertSame(property_types.get("propertyShort"), short.class);
            assertSame(property_types.get("propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class, null, null, null);
            assertEquals(property_types.size(), 21);
            assertTrue(property_types.containsKey("propertyWriteOnly"));
            assertTrue(property_types.containsKey("propertyString"));
            assertTrue(property_types.containsKey("propertyStringBuffer"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyCalendar"));
            assertTrue(property_types.containsKey("propertySqlDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyTimestamp"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDateTime"));
            assertTrue(property_types.containsKey("propertyLocalDate"));
            assertTrue(property_types.containsKey("propertyLocalTime"));
            assertTrue(property_types.containsKey("propertyChar"));
            assertTrue(property_types.containsKey("propertyBoolean"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyDouble"));
            assertTrue(property_types.containsKey("propertyFloat"));
            assertTrue(property_types.containsKey("propertyInt"));
            assertTrue(property_types.containsKey("propertyLong"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertTrue(property_types.containsKey("propertyBigDecimal"));
            assertSame(property_types.get("propertyWriteOnly"), long.class);
            assertSame(property_types.get("propertyString"), String.class);
            assertSame(property_types.get("propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyCalendar"), java.util.Calendar.class);
            assertSame(property_types.get("propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("propertyChar"), char.class);
            assertSame(property_types.get("propertyBoolean"), boolean.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyDouble"), double.class);
            assertSame(property_types.get("propertyFloat"), float.class);
            assertSame(property_types.get("propertyInt"), int.class);
            assertSame(property_types.get("propertyLong"), long.class);
            assertSame(property_types.get("propertyShort"), short.class);
            assertSame(property_types.get("propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesPrefix() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class, null, null, "PREFIX:");
            assertEquals(property_types.size(), 20);
            assertTrue(property_types.containsKey("PREFIX:propertyString"));
            assertTrue(property_types.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyCalendar"));
            assertTrue(property_types.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyChar"));
            assertTrue(property_types.containsKey("PREFIX:propertyBoolean"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_types.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_types.containsKey("PREFIX:propertyInt"));
            assertTrue(property_types.containsKey("PREFIX:propertyLong"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertTrue(property_types.containsKey("PREFIX:propertyBigDecimal"));
            assertSame(property_types.get("PREFIX:propertyString"), String.class);
            assertSame(property_types.get("PREFIX:propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyCalendar"), java.util.Calendar.class);
            assertSame(property_types.get("PREFIX:propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("PREFIX:propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("PREFIX:propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("PREFIX:propertyChar"), char.class);
            assertSame(property_types.get("PREFIX:propertyBoolean"), boolean.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyDouble"), double.class);
            assertSame(property_types.get("PREFIX:propertyFloat"), float.class);
            assertSame(property_types.get("PREFIX:propertyInt"), int.class);
            assertSame(property_types.get("PREFIX:propertyLong"), long.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
            assertSame(property_types.get("PREFIX:propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesPrefixGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class, null, null, "PREFIX:");
            assertEquals(property_types.size(), 21);
            assertTrue(property_types.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_types.containsKey("PREFIX:propertyString"));
            assertTrue(property_types.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyCalendar"));
            assertTrue(property_types.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyChar"));
            assertTrue(property_types.containsKey("PREFIX:propertyBoolean"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_types.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_types.containsKey("PREFIX:propertyInt"));
            assertTrue(property_types.containsKey("PREFIX:propertyLong"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertTrue(property_types.containsKey("PREFIX:propertyBigDecimal"));
            assertSame(property_types.get("PREFIX:propertyReadonly"), int.class);
            assertSame(property_types.get("PREFIX:propertyString"), String.class);
            assertSame(property_types.get("PREFIX:propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyCalendar"), java.util.Calendar.class);
            assertSame(property_types.get("PREFIX:propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("PREFIX:propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("PREFIX:propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("PREFIX:propertyChar"), char.class);
            assertSame(property_types.get("PREFIX:propertyBoolean"), boolean.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyDouble"), double.class);
            assertSame(property_types.get("PREFIX:propertyFloat"), float.class);
            assertSame(property_types.get("PREFIX:propertyInt"), int.class);
            assertSame(property_types.get("PREFIX:propertyLong"), long.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
            assertSame(property_types.get("PREFIX:propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesPrefixSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class, null, null, "PREFIX:");
            assertEquals(property_types.size(), 21);
            assertTrue(property_types.containsKey("PREFIX:propertyWriteOnly"));
            assertTrue(property_types.containsKey("PREFIX:propertyString"));
            assertTrue(property_types.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyCalendar"));
            assertTrue(property_types.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyChar"));
            assertTrue(property_types.containsKey("PREFIX:propertyBoolean"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_types.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_types.containsKey("PREFIX:propertyInt"));
            assertTrue(property_types.containsKey("PREFIX:propertyLong"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertTrue(property_types.containsKey("PREFIX:propertyBigDecimal"));
            assertSame(property_types.get("PREFIX:propertyWriteOnly"), long.class);
            assertSame(property_types.get("PREFIX:propertyString"), String.class);
            assertSame(property_types.get("PREFIX:propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyCalendar"), java.util.Calendar.class);
            assertSame(property_types.get("PREFIX:propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("PREFIX:propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("PREFIX:propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("PREFIX:propertyChar"), char.class);
            assertSame(property_types.get("PREFIX:propertyBoolean"), boolean.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyDouble"), double.class);
            assertSame(property_types.get("PREFIX:propertyFloat"), float.class);
            assertSame(property_types.get("PREFIX:propertyInt"), int.class);
            assertSame(property_types.get("PREFIX:propertyLong"), long.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
            assertSame(property_types.get("PREFIX:propertyBigDecimal"), BigDecimal.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesIncluded() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyString", "propertyDate", "propertySqlDate", "propertyTime",
                    "propertyInstant", "propertyLocalDate",
                    "propertyByte", "propertyFloat", "propertyShort"},
                null,
                null);
            assertEquals(property_types.size(), 9);
            assertTrue(property_types.containsKey("propertyString"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertySqlDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDate"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyFloat"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyString"), String.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyFloat"), float.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesIncludedGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyString", "propertyDate", "propertySqlDate", "propertyTime",
                    "propertyInstant", "propertyLocalDate",
                    "propertyByte", "propertyFloat", "propertyShort"},
                null,
                null);
            assertEquals(property_types.size(), 10);
            assertTrue(property_types.containsKey("propertyReadonly"));
            assertTrue(property_types.containsKey("propertyString"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertySqlDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDate"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyFloat"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyReadonly"), int.class);
            assertSame(property_types.get("propertyString"), String.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyFloat"), float.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesIncludedSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly",
                    "propertyString", "propertyDate", "propertySqlDate", "propertyTime",
                    "propertyInstant", "propertyLocalDate",
                    "propertyByte", "propertyFloat", "propertyShort"},
                null,
                null);
            assertEquals(property_types.size(), 10);
            assertTrue(property_types.containsKey("propertyWriteOnly"));
            assertTrue(property_types.containsKey("propertyString"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertySqlDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDate"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyFloat"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyWriteOnly"), long.class);
            assertSame(property_types.get("propertyString"), String.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyFloat"), float.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesIncludedPrefix() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_types.size(), 9);
            assertTrue(property_types.containsKey("PREFIX:propertyString"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyString"), String.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyFloat"), float.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesIncludedPrefixGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_types.size(), 10);
            assertTrue(property_types.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_types.containsKey("PREFIX:propertyString"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyReadonly"), int.class);
            assertSame(property_types.get("PREFIX:propertyString"), String.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyFloat"), float.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesIncludedPrefixSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly",
                    "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate",
                    "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_types.size(), 10);
            assertTrue(property_types.containsKey("PREFIX:propertyWriteOnly"));
            assertTrue(property_types.containsKey("PREFIX:propertyString"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyWriteOnly"), long.class);
            assertSame(property_types.get("PREFIX:propertyString"), String.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertySqlDate"), java.sql.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDate"), LocalDate.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyFloat"), float.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesExcluded() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class,
                null,
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_types.size(), 13);
            assertTrue(property_types.containsKey("propertyStringBuffer"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyTimestamp"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDateTime"));
            assertTrue(property_types.containsKey("propertyLocalTime"));
            assertTrue(property_types.containsKey("propertyChar"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyDouble"));
            assertTrue(property_types.containsKey("propertyInt"));
            assertTrue(property_types.containsKey("propertyLong"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("propertyChar"), char.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyDouble"), double.class);
            assertSame(property_types.get("propertyInt"), int.class);
            assertSame(property_types.get("propertyLong"), long.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesExcludedGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                null,
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_types.size(), 14);
            assertTrue(property_types.containsKey("propertyReadonly"));
            assertTrue(property_types.containsKey("propertyStringBuffer"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyTimestamp"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDateTime"));
            assertTrue(property_types.containsKey("propertyLocalTime"));
            assertTrue(property_types.containsKey("propertyChar"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyDouble"));
            assertTrue(property_types.containsKey("propertyInt"));
            assertTrue(property_types.containsKey("propertyLong"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyReadonly"), int.class);
            assertSame(property_types.get("propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("propertyChar"), char.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyDouble"), double.class);
            assertSame(property_types.get("propertyInt"), int.class);
            assertSame(property_types.get("propertyLong"), long.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesExcludedSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                null,
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_types.size(), 14);
            assertTrue(property_types.containsKey("propertyWriteOnly"));
            assertTrue(property_types.containsKey("propertyStringBuffer"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyTimestamp"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyLocalDateTime"));
            assertTrue(property_types.containsKey("propertyLocalTime"));
            assertTrue(property_types.containsKey("propertyChar"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyDouble"));
            assertTrue(property_types.containsKey("propertyInt"));
            assertTrue(property_types.containsKey("propertyLong"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyWriteOnly"), long.class);
            assertSame(property_types.get("propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("propertyChar"), char.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyDouble"), double.class);
            assertSame(property_types.get("propertyInt"), int.class);
            assertSame(property_types.get("propertyLong"), long.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesExcludedPrefix() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_types.size(), 13);
            assertTrue(property_types.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyChar"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_types.containsKey("PREFIX:propertyInt"));
            assertTrue(property_types.containsKey("PREFIX:propertyLong"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("PREFIX:propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("PREFIX:propertyChar"), char.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyDouble"), double.class);
            assertSame(property_types.get("PREFIX:propertyInt"), int.class);
            assertSame(property_types.get("PREFIX:propertyLong"), long.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesExcludedPrefixGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_types.size(), 14);
            assertTrue(property_types.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_types.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyChar"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_types.containsKey("PREFIX:propertyInt"));
            assertTrue(property_types.containsKey("PREFIX:propertyLong"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyReadonly"), int.class);
            assertSame(property_types.get("PREFIX:propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("PREFIX:propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("PREFIX:propertyChar"), char.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyDouble"), double.class);
            assertSame(property_types.get("PREFIX:propertyInt"), int.class);
            assertSame(property_types.get("PREFIX:propertyLong"), long.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesExcludedPrefixSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                null,
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_types.size(), 14);
            assertTrue(property_types.containsKey("PREFIX:propertyWriteOnly"));
            assertTrue(property_types.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyChar"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_types.containsKey("PREFIX:propertyInt"));
            assertTrue(property_types.containsKey("PREFIX:propertyLong"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyWriteOnly"), long.class);
            assertSame(property_types.get("PREFIX:propertyStringBuffer"), StringBuffer.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyTimestamp"), java.sql.Timestamp.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyLocalDateTime"), LocalDateTime.class);
            assertSame(property_types.get("PREFIX:propertyLocalTime"), LocalTime.class);
            assertSame(property_types.get("PREFIX:propertyChar"), char.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyDouble"), double.class);
            assertSame(property_types.get("PREFIX:propertyInt"), int.class);
            assertSame(property_types.get("PREFIX:propertyLong"), long.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesFiltered() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate", "propertyTime",
                    "propertyInstant", "propertyLocalDate", "propertyByte", "propertyFloat", "propertyShort"},
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_types.size(), 5);
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesFilteredGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate", "propertyTime",
                    "propertyInstant", "propertyLocalDate", "propertyByte", "propertyFloat", "propertyShort"},
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_types.size(), 6);
            assertTrue(property_types.containsKey("propertyReadonly"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyReadonly"), int.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesFilteredSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate", "propertyTime",
                    "propertyInstant", "propertyLocalDate", "propertyByte", "propertyFloat", "propertyShort"},
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_types.size(), 6);
            assertTrue(property_types.containsKey("propertyWriteOnly"));
            assertTrue(property_types.containsKey("propertyDate"));
            assertTrue(property_types.containsKey("propertyTime"));
            assertTrue(property_types.containsKey("propertyInstant"));
            assertTrue(property_types.containsKey("propertyByte"));
            assertTrue(property_types.containsKey("propertyShort"));
            assertSame(property_types.get("propertyWriteOnly"), long.class);
            assertSame(property_types.get("propertyDate"), java.util.Date.class);
            assertSame(property_types.get("propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("propertyInstant"), Instant.class);
            assertSame(property_types.get("propertyByte"), byte.class);
            assertSame(property_types.get("propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesFilteredPrefix() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_types.size(), 5);
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesFilteredPrefixGetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.GETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_types.size(), 6);
            assertTrue(property_types.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyReadonly"), int.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testPropertyTypesFilteredPrefixSetters() {
        try {
            var property_types = BeanUtils.getPropertyTypes(BeanUtils.Accessors.SETTERS, BeanImpl.class,
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_types.size(), 6);
            assertTrue(property_types.containsKey("PREFIX:propertyWriteOnly"));
            assertTrue(property_types.containsKey("PREFIX:propertyDate"));
            assertTrue(property_types.containsKey("PREFIX:propertyTime"));
            assertTrue(property_types.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_types.containsKey("PREFIX:propertyByte"));
            assertTrue(property_types.containsKey("PREFIX:propertyShort"));
            assertSame(property_types.get("PREFIX:propertyWriteOnly"), long.class);
            assertSame(property_types.get("PREFIX:propertyDate"), java.util.Date.class);
            assertSame(property_types.get("PREFIX:propertyTime"), java.sql.Time.class);
            assertSame(property_types.get("PREFIX:propertyInstant"), Instant.class);
            assertSame(property_types.get("PREFIX:propertyByte"), byte.class);
            assertSame(property_types.get("PREFIX:propertyShort"), short.class);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValueIllegal() {
        try {
            BeanUtils.getPropertyValue(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyValue(Object.class, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyValue(new Object(), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyValue(new Object(), "");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValue() {
        Object bean = getPopulatedBean();
        try {
            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyString"), "thisisastring");
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyDate"), Convert.toDate(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyCalendar"), cal);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyTime"), Convert.toSqlTime(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyInstant"), Convert.toInstant(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyChar"), 'g');
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyBoolean"), Boolean.FALSE);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyByte"), (byte) 53);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyDouble"), 84578.42d);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyFloat"), 35523.967f);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyInt"), 978);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyLong"), 87346L);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyShort"), (short) 31);
            assertEquals(BeanUtils.getPropertyValue(bean, "propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyValue(bean, "unknown");
            fail();
        } catch (BeanUtilsException e) {
            assertSame(e.getBeanClass(), bean.getClass());
        }
    }

    @Test
    void testSetPropertyValue() {
        BeanImpl bean = new BeanImpl();
        try {
            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            BeanUtils.setPropertyValue(bean, "propertyString", "thisisastring");
            BeanUtils.setPropertyValue(bean, "propertyStringBuffer", new StringBuffer("butthisisastringbuffer"));
            BeanUtils.setPropertyValue(bean, "propertyDate", Convert.toDate(cal));
            BeanUtils.setPropertyValue(bean, "propertyCalendar", cal);
            BeanUtils.setPropertyValue(bean, "propertySqlDate", Convert.toSqlDate(cal));
            BeanUtils.setPropertyValue(bean, "propertyTime", Convert.toSqlTime(cal));
            BeanUtils.setPropertyValue(bean, "propertyTimestamp", Convert.toSqlTimestamp(cal));
            BeanUtils.setPropertyValue(bean, "propertyInstant", Convert.toInstant(cal));
            BeanUtils.setPropertyValue(bean, "propertyLocalDateTime", Convert.toLocalDateTime(cal));
            BeanUtils.setPropertyValue(bean, "propertyLocalDate", Convert.toLocalDate(cal));
            BeanUtils.setPropertyValue(bean, "propertyLocalTime", Convert.toLocalTime(cal));
            BeanUtils.setPropertyValue(bean, "propertyChar", 'g');
            BeanUtils.setPropertyValue(bean, "propertyBoolean", Boolean.FALSE);
            BeanUtils.setPropertyValue(bean, "propertyByte", (byte) 53);
            BeanUtils.setPropertyValue(bean, "propertyDouble", 84578.42d);
            BeanUtils.setPropertyValue(bean, "propertyFloat", 35523.967f);
            BeanUtils.setPropertyValue(bean, "propertyInt", 978);
            BeanUtils.setPropertyValue(bean, "propertyLong", 87346L);
            BeanUtils.setPropertyValue(bean, "propertyShort", (short) 31);
            BeanUtils.setPropertyValue(bean, "propertyBigDecimal", new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        BeanImpl populated = getPopulatedBean();
        assertEquals(bean.getPropertyString(), populated.getPropertyString());
        assertEquals(bean.getPropertyStringBuffer().toString(), populated.getPropertyStringBuffer().toString());
        assertEquals(bean.getPropertyDate(), populated.getPropertyDate());
        assertEquals(bean.getPropertyCalendar(), populated.getPropertyCalendar());
        assertEquals(bean.getPropertySqlDate(), populated.getPropertySqlDate());
        assertEquals(bean.getPropertyTime(), populated.getPropertyTime());
        assertEquals(bean.getPropertyTimestamp(), populated.getPropertyTimestamp());
        assertEquals(bean.getPropertyChar(), populated.getPropertyChar());
        assertEquals(bean.isPropertyBoolean(), populated.isPropertyBoolean());
        assertEquals(bean.getPropertyByte(), populated.getPropertyByte());
        assertEquals(bean.getPropertyDouble(), populated.getPropertyDouble());
        assertEquals(bean.getPropertyFloat(), populated.getPropertyFloat());
        assertEquals(bean.getPropertyInt(), populated.getPropertyInt());
        assertEquals(bean.getPropertyLong(), populated.getPropertyLong());
        assertEquals(bean.getPropertyShort(), populated.getPropertyShort());
        assertEquals(bean.getPropertyBigDecimal(), populated.getPropertyBigDecimal());

        try {
            BeanUtils.setPropertyValue(bean, "unknown", "ok");
            fail();
        } catch (BeanUtilsException e) {
            assertSame(e.getBeanClass(), bean.getClass());
        }
    }

    @Test
    void testGetPropertyValuesIllegal() {
        try {
            assertEquals(0, BeanUtils.getPropertyValues(null, null, null, null).size());
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        try {
            BeanUtils.getPropertyValues(Object.class, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValues() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(), null, null, null);
            assertEquals(property_values.size(), 20);
            assertTrue(property_values.containsKey("propertyString"));
            assertTrue(property_values.containsKey("propertyStringBuffer"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyCalendar"));
            assertTrue(property_values.containsKey("propertySqlDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyTimestamp"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDateTime"));
            assertTrue(property_values.containsKey("propertyLocalDate"));
            assertTrue(property_values.containsKey("propertyLocalTime"));
            assertTrue(property_values.containsKey("propertyChar"));
            assertTrue(property_values.containsKey("propertyBoolean"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyDouble"));
            assertTrue(property_values.containsKey("propertyFloat"));
            assertTrue(property_values.containsKey("propertyInt"));
            assertTrue(property_values.containsKey("propertyLong"));
            assertTrue(property_values.containsKey("propertyShort"));
            assertTrue(property_values.containsKey("propertyBigDecimal"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyString"), "thisisastring");
            assertEquals(property_values.get("propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyCalendar"), cal);
            assertEquals(property_values.get("propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("propertyChar"), 'g');
            assertEquals(property_values.get("propertyBoolean"), Boolean.FALSE);
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyDouble"), 84578.42d);
            assertEquals(property_values.get("propertyFloat"), 35523.967f);
            assertEquals(property_values.get("propertyInt"), 978);
            assertEquals(property_values.get("propertyLong"), 87346L);
            assertEquals(property_values.get("propertyShort"), (short) 31);
            assertEquals(property_values.get("propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(), null, null, null);
            assertEquals(property_values.size(), 21);
            assertTrue(property_values.containsKey("propertyReadonly"));
            assertTrue(property_values.containsKey("propertyString"));
            assertTrue(property_values.containsKey("propertyStringBuffer"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyCalendar"));
            assertTrue(property_values.containsKey("propertySqlDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyTimestamp"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDateTime"));
            assertTrue(property_values.containsKey("propertyLocalDate"));
            assertTrue(property_values.containsKey("propertyLocalTime"));
            assertTrue(property_values.containsKey("propertyChar"));
            assertTrue(property_values.containsKey("propertyBoolean"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyDouble"));
            assertTrue(property_values.containsKey("propertyFloat"));
            assertTrue(property_values.containsKey("propertyInt"));
            assertTrue(property_values.containsKey("propertyLong"));
            assertTrue(property_values.containsKey("propertyShort"));
            assertTrue(property_values.containsKey("propertyBigDecimal"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyReadonly"), 23);
            assertEquals(property_values.get("propertyString"), "thisisastring");
            assertEquals(property_values.get("propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyCalendar"), cal);
            assertEquals(property_values.get("propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("propertyChar"), 'g');
            assertEquals(property_values.get("propertyBoolean"), Boolean.FALSE);
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyDouble"), 84578.42d);
            assertEquals(property_values.get("propertyFloat"), 35523.967f);
            assertEquals(property_values.get("propertyInt"), 978);
            assertEquals(property_values.get("propertyLong"), 87346L);
            assertEquals(property_values.get("propertyShort"), (short) 31);
            assertEquals(property_values.get("propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(), null, null, null);
            assertEquals(property_values.size(), 20);
            assertTrue(property_values.containsKey("propertyString"));
            assertTrue(property_values.containsKey("propertyStringBuffer"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyCalendar"));
            assertTrue(property_values.containsKey("propertySqlDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyTimestamp"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDateTime"));
            assertTrue(property_values.containsKey("propertyLocalDate"));
            assertTrue(property_values.containsKey("propertyLocalTime"));
            assertTrue(property_values.containsKey("propertyChar"));
            assertTrue(property_values.containsKey("propertyBoolean"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyDouble"));
            assertTrue(property_values.containsKey("propertyFloat"));
            assertTrue(property_values.containsKey("propertyInt"));
            assertTrue(property_values.containsKey("propertyLong"));
            assertTrue(property_values.containsKey("propertyShort"));
            assertTrue(property_values.containsKey("propertyBigDecimal"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyString"), "thisisastring");
            assertEquals(property_values.get("propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyCalendar"), cal);
            assertEquals(property_values.get("propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("propertyChar"), 'g');
            assertEquals(property_values.get("propertyBoolean"), Boolean.FALSE);
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyDouble"), 84578.42d);
            assertEquals(property_values.get("propertyFloat"), 35523.967f);
            assertEquals(property_values.get("propertyInt"), 978);
            assertEquals(property_values.get("propertyLong"), 87346L);
            assertEquals(property_values.get("propertyShort"), (short) 31);
            assertEquals(property_values.get("propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesPrefix() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(), null, null, "PREFIX:");
            assertEquals(property_values.size(), 20);
            assertTrue(property_values.containsKey("PREFIX:propertyString"));
            assertTrue(property_values.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyCalendar"));
            assertTrue(property_values.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyChar"));
            assertTrue(property_values.containsKey("PREFIX:propertyBoolean"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_values.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_values.containsKey("PREFIX:propertyInt"));
            assertTrue(property_values.containsKey("PREFIX:propertyLong"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));
            assertTrue(property_values.containsKey("PREFIX:propertyBigDecimal"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyString"), "thisisastring");
            assertEquals(property_values.get("PREFIX:propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyCalendar"), cal);
            assertEquals(property_values.get("PREFIX:propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("PREFIX:propertyChar"), 'g');
            assertEquals(property_values.get("PREFIX:propertyBoolean"), Boolean.FALSE);
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyDouble"), 84578.42d);
            assertEquals(property_values.get("PREFIX:propertyFloat"), 35523.967f);
            assertEquals(property_values.get("PREFIX:propertyInt"), 978);
            assertEquals(property_values.get("PREFIX:propertyLong"), 87346L);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
            assertEquals(property_values.get("PREFIX:propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesPrefixGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(), null, null, "PREFIX:");
            assertEquals(property_values.size(), 21);
            assertTrue(property_values.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_values.containsKey("PREFIX:propertyString"));
            assertTrue(property_values.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyCalendar"));
            assertTrue(property_values.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyChar"));
            assertTrue(property_values.containsKey("PREFIX:propertyBoolean"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_values.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_values.containsKey("PREFIX:propertyInt"));
            assertTrue(property_values.containsKey("PREFIX:propertyLong"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));
            assertTrue(property_values.containsKey("PREFIX:propertyBigDecimal"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyReadonly"), 23);
            assertEquals(property_values.get("PREFIX:propertyString"), "thisisastring");
            assertEquals(property_values.get("PREFIX:propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyCalendar"), cal);
            assertEquals(property_values.get("PREFIX:propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("PREFIX:propertyChar"), 'g');
            assertEquals(property_values.get("PREFIX:propertyBoolean"), Boolean.FALSE);
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyDouble"), 84578.42d);
            assertEquals(property_values.get("PREFIX:propertyFloat"), 35523.967f);
            assertEquals(property_values.get("PREFIX:propertyInt"), 978);
            assertEquals(property_values.get("PREFIX:propertyLong"), 87346L);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
            assertEquals(property_values.get("PREFIX:propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesPrefixSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(), null, null, "PREFIX:");
            assertEquals(property_values.size(), 20);
            assertTrue(property_values.containsKey("PREFIX:propertyString"));
            assertTrue(property_values.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyCalendar"));
            assertTrue(property_values.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyChar"));
            assertTrue(property_values.containsKey("PREFIX:propertyBoolean"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_values.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_values.containsKey("PREFIX:propertyInt"));
            assertTrue(property_values.containsKey("PREFIX:propertyLong"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));
            assertTrue(property_values.containsKey("PREFIX:propertyBigDecimal"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyString"), "thisisastring");
            assertEquals(property_values.get("PREFIX:propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyCalendar"), cal);
            assertEquals(property_values.get("PREFIX:propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("PREFIX:propertyChar"), 'g');
            assertEquals(property_values.get("PREFIX:propertyBoolean"), Boolean.FALSE);
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyDouble"), 84578.42d);
            assertEquals(property_values.get("PREFIX:propertyFloat"), 35523.967f);
            assertEquals(property_values.get("PREFIX:propertyInt"), 978);
            assertEquals(property_values.get("PREFIX:propertyLong"), 87346L);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
            assertEquals(property_values.get("PREFIX:propertyBigDecimal"), new BigDecimal("8347365990.387437894678"));
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesIncluded() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(),
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate", "propertyTime", "propertyByte", "propertyFloat", "propertyShort"},
                null,
                null);
            assertEquals(property_values.size(), 9);
            assertTrue(property_values.containsKey("propertyString"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertySqlDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDate"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyFloat"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyString"), "thisisastring");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyFloat"), 35523.967f);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesIncludedGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(),
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate", "propertyTime", "propertyByte", "propertyFloat", "propertyShort"},
                null,
                null);
            assertEquals(property_values.size(), 10);
            assertTrue(property_values.containsKey("propertyReadonly"));
            assertTrue(property_values.containsKey("propertyString"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertySqlDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDate"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyFloat"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyReadonly"), 23);
            assertEquals(property_values.get("propertyString"), "thisisastring");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyFloat"), 35523.967f);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesIncludedSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(),
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate", "propertyTime", "propertyByte", "propertyFloat", "propertyShort"},
                null,
                null);
            assertEquals(property_values.size(), 9);
            assertTrue(property_values.containsKey("propertyString"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertySqlDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDate"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyFloat"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyString"), "thisisastring");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyFloat"), 35523.967f);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesIncludedPrefix() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(),
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_values.size(), 9);
            assertTrue(property_values.containsKey("PREFIX:propertyString"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyString"), "thisisastring");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyFloat"), 35523.967f);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesIncludedPrefixGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(),
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_values.size(), 10);
            assertTrue(property_values.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_values.containsKey("PREFIX:propertyString"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyReadonly"), 23);
            assertEquals(property_values.get("PREFIX:propertyString"), "thisisastring");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyFloat"), 35523.967f);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesIncludedPrefixSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(),
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate", "PREFIX:propertySqlDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                null,
                "PREFIX:");
            assertEquals(property_values.size(), 9);
            assertTrue(property_values.containsKey("PREFIX:propertyString"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertySqlDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyFloat"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyString"), "thisisastring");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertySqlDate"), Convert.toSqlDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDate"), Convert.toLocalDate(cal));
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyFloat"), 35523.967f);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesExcluded() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(),
                null,
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_values.size(), 13);
            assertTrue(property_values.containsKey("propertyStringBuffer"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyTimestamp"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDateTime"));
            assertTrue(property_values.containsKey("propertyLocalTime"));
            assertTrue(property_values.containsKey("propertyChar"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyDouble"));
            assertTrue(property_values.containsKey("propertyInt"));
            assertTrue(property_values.containsKey("propertyLong"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("propertyChar"), 'g');
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyDouble"), 84578.42d);
            assertEquals(property_values.get("propertyInt"), 978);
            assertEquals(property_values.get("propertyLong"), 87346L);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesExcludedGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(),
                null,
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_values.size(), 14);
            assertTrue(property_values.containsKey("propertyReadonly"));
            assertTrue(property_values.containsKey("propertyStringBuffer"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyTimestamp"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDateTime"));
            assertTrue(property_values.containsKey("propertyLocalTime"));
            assertTrue(property_values.containsKey("propertyChar"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyDouble"));
            assertTrue(property_values.containsKey("propertyInt"));
            assertTrue(property_values.containsKey("propertyLong"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyReadonly"), 23);
            assertEquals(property_values.get("propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("propertyChar"), 'g');
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyDouble"), 84578.42d);
            assertEquals(property_values.get("propertyInt"), 978);
            assertEquals(property_values.get("propertyLong"), 87346L);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesExcludedSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(),
                null,
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_values.size(), 13);
            assertTrue(property_values.containsKey("propertyStringBuffer"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyTimestamp"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyLocalDateTime"));
            assertTrue(property_values.containsKey("propertyLocalTime"));
            assertTrue(property_values.containsKey("propertyChar"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyDouble"));
            assertTrue(property_values.containsKey("propertyInt"));
            assertTrue(property_values.containsKey("propertyLong"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("propertyChar"), 'g');
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyDouble"), 84578.42d);
            assertEquals(property_values.get("propertyInt"), 978);
            assertEquals(property_values.get("propertyLong"), 87346L);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesExcludedPrefix() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(),
                null,
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_values.size(), 13);
            assertTrue(property_values.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyChar"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_values.containsKey("PREFIX:propertyInt"));
            assertTrue(property_values.containsKey("PREFIX:propertyLong"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("PREFIX:propertyChar"), 'g');
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyDouble"), 84578.42d);
            assertEquals(property_values.get("PREFIX:propertyInt"), 978);
            assertEquals(property_values.get("PREFIX:propertyLong"), 87346L);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesExcludedPrefixGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(),
                null,
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_values.size(), 14);
            assertTrue(property_values.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_values.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyChar"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_values.containsKey("PREFIX:propertyInt"));
            assertTrue(property_values.containsKey("PREFIX:propertyLong"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyReadonly"), 23);
            assertEquals(property_values.get("PREFIX:propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("PREFIX:propertyChar"), 'g');
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyDouble"), 84578.42d);
            assertEquals(property_values.get("PREFIX:propertyInt"), 978);
            assertEquals(property_values.get("PREFIX:propertyLong"), 87346L);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesExcludedPrefixSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(),
                null,
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_values.size(), 13);
            assertTrue(property_values.containsKey("PREFIX:propertyStringBuffer"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyTimestamp"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalDateTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyLocalTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyChar"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyDouble"));
            assertTrue(property_values.containsKey("PREFIX:propertyInt"));
            assertTrue(property_values.containsKey("PREFIX:propertyLong"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyStringBuffer").toString(), "butthisisastringbuffer");
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyTimestamp"), Convert.toSqlTimestamp(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalDateTime"), Convert.toLocalDateTime(cal));
            assertEquals(property_values.get("PREFIX:propertyLocalTime"), Convert.toLocalTime(cal));
            assertEquals(property_values.get("PREFIX:propertyChar"), 'g');
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyDouble"), 84578.42d);
            assertEquals(property_values.get("PREFIX:propertyInt"), 978);
            assertEquals(property_values.get("PREFIX:propertyLong"), 87346L);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesFiltered() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(),
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate", "propertyTime", "propertyByte", "propertyFloat", "propertyShort"},
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_values.size(), 5);
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesFilteredGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(),
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate", "propertyTime", "propertyByte", "propertyFloat", "propertyShort"},
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_values.size(), 6);
            assertTrue(property_values.containsKey("propertyReadonly"));
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyReadonly"), 23);
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesFilteredSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(),
                new String[]{"propertyReadonly", "propertyWriteOnly", "propertyString", "propertyDate", "propertySqlDate",
                    "propertyInstant", "propertyLocalDate", "propertyTime", "propertyByte", "propertyFloat", "propertyShort"},
                new String[]{"propertyString", "propertyCalendar", "propertySqlDate", "propertyLocalDate", "propertyBoolean", "propertyFloat", "propertyBigDecimal"},
                null);
            assertEquals(property_values.size(), 5);
            assertTrue(property_values.containsKey("propertyDate"));
            assertTrue(property_values.containsKey("propertyTime"));
            assertTrue(property_values.containsKey("propertyInstant"));
            assertTrue(property_values.containsKey("propertyByte"));
            assertTrue(property_values.containsKey("propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("propertyByte"), (byte) 53);
            assertEquals(property_values.get("propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesFilteredPrefix() {
        try {
            var property_values = BeanUtils.getPropertyValues(getPopulatedBean(),
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertySqlDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_values.size(), 5);
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesFilteredPrefixGetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, getPopulatedBean(),
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertySqlDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_values.size(), 6);
            assertTrue(property_values.containsKey("PREFIX:propertyReadonly"));
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyReadonly"), 23);
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testGetPropertyValuesFilteredPrefixSetters() {
        try {
            var property_values = BeanUtils.getPropertyValues(BeanUtils.Accessors.SETTERS, getPopulatedBean(),
                new String[]{"PREFIX:propertyReadonly", "PREFIX:propertyWriteOnly", "PREFIX:propertyString", "PREFIX:propertyDate",
                    "PREFIX:propertyInstant", "PREFIX:propertyLocalDate", "PREFIX:propertySqlDate", "PREFIX:propertyTime", "PREFIX:propertyByte", "PREFIX:propertyFloat", "PREFIX:propertyShort"},
                new String[]{"PREFIX:propertyString", "PREFIX:propertyCalendar", "PREFIX:propertySqlDate", "PREFIX:propertyLocalDate", "PREFIX:propertyBoolean", "PREFIX:propertyFloat", "PREFIX:propertyBigDecimal"},
                "PREFIX:");
            assertEquals(property_values.size(), 5);
            assertTrue(property_values.containsKey("PREFIX:propertyDate"));
            assertTrue(property_values.containsKey("PREFIX:propertyTime"));
            assertTrue(property_values.containsKey("PREFIX:propertyInstant"));
            assertTrue(property_values.containsKey("PREFIX:propertyByte"));
            assertTrue(property_values.containsKey("PREFIX:propertyShort"));

            var cal = RifeConfig.tools().getCalendarInstance(2002, Calendar.DECEMBER, 26, 22, 52, 31, 153);
            assertEquals(property_values.get("PREFIX:propertyDate"), Convert.toDate(cal));
            assertEquals(property_values.get("PREFIX:propertyTime"), Convert.toSqlTime(cal));
            assertEquals(property_values.get("PREFIX:propertyInstant"), Convert.toInstant(cal));
            assertEquals(property_values.get("PREFIX:propertyByte"), (byte) 53);
            assertEquals(property_values.get("PREFIX:propertyShort"), (short) 31);
        } catch (BeanUtilsException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @SuppressWarnings("deprecated")
    @Test
    void testFormatPropertyValues() {
        var cal = RifeConfig.tools().getCalendarInstance(2023, Calendar.JANUARY, 23, 13, 45, 23, 142);
        assertEquals("20230123134523142-0500", BeanUtils.formatPropertyValue(Convert.toDate(cal), null));
        assertEquals("20230123134523142-0500", BeanUtils.formatPropertyValue(Convert.toInstant(cal), null));
        assertEquals("20230123134523142-0500", BeanUtils.formatPropertyValue(Convert.toLocalDateTime(cal), null));
        assertEquals("20230123000000000-0500", BeanUtils.formatPropertyValue(Convert.toLocalDate(cal), null));
        assertEquals("134523142-0500", BeanUtils.formatPropertyValue(Convert.toLocalTime(cal), null));
    }

    @Test
    void testFormatPropertyValuesConstrained() {
        var cal = RifeConfig.tools().getCalendarInstance(2023, Calendar.JANUARY, 23, 13, 45, 23, 142);
        var format = RifeConfig.tools().getSimpleDateFormat("d MMM yyyy HH:mm:ss");
        assertEquals("23 Jan 2023 13:45:23", BeanUtils.formatPropertyValue(Convert.toDate(cal), new ConstrainedProperty("property").format(format)));
        assertEquals("23 Jan 2023 13:45:23", BeanUtils.formatPropertyValue(Instant.parse("2023-01-23T18:45:23.00Z"), new ConstrainedProperty("property").format(format)));
        assertEquals("23 Jan 2023 13:45:23", BeanUtils.formatPropertyValue(LocalDateTime.of(2023, Month.JANUARY, 23, 13, 45, 23, 142000000), new ConstrainedProperty("property").format(format)));
        assertEquals("23 Jan 2023 00:00:00", BeanUtils.formatPropertyValue(LocalDate.of(2023, Month.JANUARY, 23), new ConstrainedProperty("property").format(format)));
        assertEquals("1 Jan 1970 13:45:23", BeanUtils.formatPropertyValue(LocalTime.of(13, 45, 23, 142000000), new ConstrainedProperty("property").format(format)));
    }
}
