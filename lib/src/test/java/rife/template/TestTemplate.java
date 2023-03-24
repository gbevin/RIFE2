/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.template;

import org.junit.jupiter.api.Test;
import rife.template.exceptions.BlockUnknownException;
import rife.template.exceptions.TemplateException;
import rife.template.exceptions.ValueUnknownException;
import rife.tools.ArrayUtils;
import rife.tools.ExceptionUtils;
import rife.tools.StringUtils;
import rife.validation.ConstrainedProperty;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestTemplate {
    @Test
    void testInstantiation() {
        var template = TemplateFactory.HTML.get("empty");
        assertNotNull(template);
        assertTrue(template.getModificationTime() <= System.currentTimeMillis());
        assertEquals("", template.getContent());
        assertEquals("empty", template.getName());
        assertEquals("empty", template.getFullName());
        try {
            template.getBlock("TEST");
            fail();
        } catch (BlockUnknownException e) {
            assertEquals("TEST", e.getId());
        }
        try {
            template.getValue("TEST");
            fail();
        } catch (ValueUnknownException e) {
            assertEquals("TEST", e.getId());
        }
        assertEquals(template.countValues(), 0);
        assertEquals(template.getAvailableValueIds().length, 0);
        assertEquals(template.getFilteredValues("empty").size(), 0);
        assertEquals(template.getUnsetValueIds().size(), 0);
    }

    @Test
    void testClone() {
        var template1 = TemplateFactory.HTML.get("values");
        assertEquals("values", template1.getName());
        assertEquals("values", template1.getFullName());
        var value1 = "aaab";
        var value2 = "bbbc";
        var value3 = "ccccd";
        try {
            template1.setValue("VALUE1", value1);
            template1.setValue("VALUE2", value2);
            template1.setValue("VALUE3", value3);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        var template2 = (Template) template1.clone();
        assertNotNull(template2);
        assertNotSame(template1, template2);
        assertEquals(template1.getContent(), template2.getContent());
    }

    @Test
    void testSetValues() {
        var template = TemplateFactory.HTML.get("values");
        assertNull(template.getValue("VALUE1"));
        assertNull(template.getValue("VALUE2"));
        assertNull(template.getValue("VALUE3"));
        assertEquals(template.countValues(), 0);

        try {
            template.getValue("VALUE4");
            fail();
        } catch (ValueUnknownException e) {
            assertEquals("VALUE4", e.getId());
        }

        var value1 = "één";
        var value2 = "bbbc";
        var value3 = "ccccd";
        try {
            template.setValue("VALUE1", value1);
            assertEquals(template.countValues(), 1);
            template.setValue("VALUE2", value2);
            assertEquals(template.countValues(), 2);
            template.setValue("VALUE3", value3);
            assertEquals(template.countValues(), 3);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
        assertEquals(template.getValue("VALUE1"), value1);
        assertEquals(template.getValue("VALUE2"), value2);
        assertEquals(template.getValue("VALUE3"), value3);
    }

    @Test
    void testSetValuesEncoded() {
        var template = TemplateFactory.HTML.get("values");
        assertNull(template.getValue("VALUE1"));
        assertNull(template.getValue("VALUE2"));
        assertNull(template.getValue("VALUE3"));
        assertEquals(template.countValues(), 0);

        template.setValueEncoded("VALUE1", "één");
        assertEquals(template.countValues(), 1);
        template.setValueEncoded("VALUE2", "<>-");
        assertEquals(template.countValues(), 2);
        template.setValueEncoded("VALUE3", "çåø");
        assertEquals(template.countValues(), 3);
        assertEquals(template.getValue("VALUE1"), "&eacute;&eacute;n");
        assertEquals(template.getValue("VALUE2"), "&lt;&gt;-");
        assertEquals(template.getValue("VALUE3"), "&ccedil;&aring;&oslash;");
    }

    @Test
    void testSetValuesTyped() {
        var template = TemplateFactory.HTML.get("values_typed");

        String value_string = "abcde";
        boolean value_boolean = false;
        char value_char = 'k';
        char[] value_chararray = "abcdefgh".toCharArray();
        double value_double = 7483.343d;
        float value_float = 233.45f;
        int value_int = 34878;
        long value_long = 938649837L;
        Object value_object = Boolean.TRUE;
        var value_template = TemplateFactory.HTML.get("values");

        value_template.setValue("VALUE1", "thevalue1");
        value_template.setValue("VALUE3", "thevalue3");

        try {
            template.setValue("STRING", value_string);
            assertEquals(template.countValues(), 1);
            template.setValue("BOOLEAN", value_boolean);
            assertEquals(template.countValues(), 2);
            template.setValue("CHAR", value_char);
            assertEquals(template.countValues(), 3);
            template.setValue("CHAR[]", value_chararray, 3, 2);
            assertEquals(template.countValues(), 4);
            template.setValue("DOUBLE", value_double);
            assertEquals(template.countValues(), 5);
            template.setValue("FLOAT", value_float);
            assertEquals(template.countValues(), 6);
            template.setValue("INT", value_int);
            assertEquals(template.countValues(), 7);
            template.setValue("LONG", value_long);
            assertEquals(template.countValues(), 8);
            template.setValue("OBJECT", value_object);
            assertEquals(template.countValues(), 9);
            template.setValue("TEMPLATE", value_template);
            assertEquals(template.countValues(), 10);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
        assertEquals(template.getValue("STRING"), value_string);
        assertEquals(template.getValue("BOOLEAN"), "" + value_boolean);
        assertEquals(template.getValue("CHAR"), "" + value_char);
        assertEquals(template.getValue("CHAR[]").toString(), "de");
        assertEquals(template.getValue("DOUBLE"), "" + value_double);
        assertEquals(template.getValue("FLOAT"), "" + value_float);
        assertEquals(template.getValue("INT"), "" + value_int);
        assertEquals(template.getValue("LONG"), "" + value_long);
        assertEquals(template.getValue("OBJECT"), "" + value_object);
        assertEquals(template.getValue("TEMPLATE"), "thevalue1<!--v VALUE2/-->thevalue3\n");
    }

    @Test
    void testAppendValuesEncoded() {
        var template = TemplateFactory.HTML.get("values");
        assertNull(template.getValue("VALUE1"));
        assertNull(template.getValue("VALUE2"));
        assertNull(template.getValue("VALUE3"));
        assertEquals(template.countValues(), 0);

        template.setValue("VALUE1", "één");
        assertEquals(template.countValues(), 1);
        template.setValue("VALUE2", "<>-");
        assertEquals(template.countValues(), 2);
        template.setValue("VALUE3", "çåø");
        assertEquals(template.countValues(), 3);

        template.appendValueEncoded("VALUE1", "één");
        assertEquals(template.countValues(), 3);
        template.appendValueEncoded("VALUE2", "<>-");
        assertEquals(template.countValues(), 3);
        template.appendValueEncoded("VALUE3", "çåø");
        assertEquals(template.countValues(), 3);
        assertEquals(template.getValue("VALUE1"), "één&eacute;&eacute;n");
        assertEquals(template.getValue("VALUE2"), "<>-&lt;&gt;-");
        assertEquals(template.getValue("VALUE3"), "çåø&ccedil;&aring;&oslash;");
    }

    @Test
    void testAppendValuesTyped() {
        var template = TemplateFactory.HTML.get("values_typed");

        String value_string = "abcde";
        boolean value_boolean = false;
        char value_char = 'k';
        char[] value_chararray = "abcdefgh".toCharArray();
        double value_double = 7483.343d;
        float value_float = 233.45f;
        int value_int = 34878;
        long value_long = 938649837L;
        Object value_object = Boolean.TRUE;

        try {
            template.appendValue("VALUE", value_string);
            template.appendValue("VALUE", value_boolean);
            template.appendValue("VALUE", value_char);
            template.appendValue("VALUE", value_chararray, 3, 2);
            template.appendValue("VALUE", value_double);
            template.appendValue("VALUE", value_float);
            template.appendValue("VALUE", value_int);
            template.appendValue("VALUE", value_long);
            template.appendValue("VALUE", value_object);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
        assertEquals(template.getValue("VALUE"),
            value_string +
                value_boolean +
                value_char +
                "de" +
                value_double +
                value_float +
                value_int +
                value_long +
                value_object);
    }

    @Test
    void testRemoveValues() {
        var template = TemplateFactory.HTML.get("values");
        var value1 = "aaab";
        var value2 = "bbbc";
        var value3 = "ccccd";
        try {
            template.setValue("VALUE1", value1);
            template.setValue("VALUE2", value2);
            template.setValue("VALUE3", value3);
            assertEquals(template.countValues(), 3);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
        template.removeValue("VALUE1");
        assertEquals(template.countValues(), 2);
        template.removeValue("VALUE2");
        assertEquals(template.countValues(), 1);
        template.removeValue("VALUE3");
        assertEquals(template.countValues(), 0);

        try {
            template.removeValue("VALUE4");
            fail();
        } catch (ValueUnknownException e) {
            assertEquals("VALUE4", e.getId());
        }
    }

    @Test
    void testBlankValues() {
        var template = TemplateFactory.HTML.get("values");
        var value1 = "aaab";
        var value2 = "bbbc";
        var value3 = "ccccd";
        try {
            template.setValue("VALUE1", value1);
            template.setValue("VALUE2", value2);
            template.setValue("VALUE3", value3);
            assertEquals(template.countValues(), 3);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        assertNotEquals("", template.getValue("VALUE1"));
        assertNotEquals("", template.getValue("VALUE2"));
        assertNotEquals("", template.getValue("VALUE3"));

        template.blankValue("VALUE1");
        template.blankValue("VALUE2");
        template.blankValue("VALUE3");
        assertEquals(template.countValues(), 3);

        assertEquals(template.getValue("VALUE1"), "");
        assertEquals(template.getValue("VALUE2"), "");
        assertEquals(template.getValue("VALUE3"), "");

        try {
            template.blankValue("VALUE4");
            fail();
        } catch (ValueUnknownException e) {
            assertEquals("VALUE4", e.getId());
        }
    }

    @Test
    void testClearValues() {
        var template = TemplateFactory.HTML.get("values");
        var value1 = "aaab";
        var value2 = "bbbc";
        var value3 = "ccccd";
        try {
            template.setValue("VALUE1", value1);
            template.setValue("VALUE2", value2);
            template.setValue("VALUE3", value3);
            assertEquals(template.countValues(), 3);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
        template.clear();
        assertEquals(template.countValues(), 0);
    }

    @Test
    void testDefaultValues() {
        var template = TemplateFactory.HTML.get("values_default");
        var defaultvalue1 = "azerty";
        assertTrue(template.hasDefaultValue("DEFAULTVALUE"));
        assertEquals(template.getDefaultValue("DEFAULTVALUE"), defaultvalue1);
        assertEquals(template.getValue("DEFAULTVALUE"), defaultvalue1);
        var value1 = "hdijjk";
        try {
            template.setValue("DEFAULTVALUE", value1);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
        assertTrue(template.hasDefaultValue("DEFAULTVALUE"));
        assertEquals(template.getDefaultValue("DEFAULTVALUE"), defaultvalue1);
        assertEquals(template.getValue("DEFAULTVALUE"), value1);
        template.removeValue("DEFAULTVALUE");
        assertTrue(template.hasDefaultValue("DEFAULTVALUE"));
        assertEquals(template.getDefaultValue("DEFAULTVALUE"), defaultvalue1);
        assertEquals(template.getValue("DEFAULTVALUE"), defaultvalue1);
    }

    @Test
    void testUnsetValues() {
        Template template;
        Collection<String> unset_value_ids;
        try {
            template = TemplateFactory.HTML.get("values_short_in");

            unset_value_ids = template.getUnsetValueIds();
            assertNotNull(unset_value_ids);
            assertEquals(unset_value_ids.size(), 7);
            assertTrue(unset_value_ids.contains("VALUE1"));
            assertTrue(unset_value_ids.contains("VALUE2"));
            assertTrue(unset_value_ids.contains("VALUE3"));
            assertTrue(unset_value_ids.contains("VALUE4"));
            assertTrue(unset_value_ids.contains("VALUE5"));
            assertTrue(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE1", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 6);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertTrue(unset_value_ids.contains("VALUE2"));
            assertTrue(unset_value_ids.contains("VALUE3"));
            assertTrue(unset_value_ids.contains("VALUE4"));
            assertTrue(unset_value_ids.contains("VALUE5"));
            assertTrue(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE2", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 5);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertFalse(unset_value_ids.contains("VALUE2"));
            assertTrue(unset_value_ids.contains("VALUE3"));
            assertTrue(unset_value_ids.contains("VALUE4"));
            assertTrue(unset_value_ids.contains("VALUE5"));
            assertTrue(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE3", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 4);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertFalse(unset_value_ids.contains("VALUE2"));
            assertFalse(unset_value_ids.contains("VALUE3"));
            assertTrue(unset_value_ids.contains("VALUE4"));
            assertTrue(unset_value_ids.contains("VALUE5"));
            assertTrue(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE4", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 3);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertFalse(unset_value_ids.contains("VALUE2"));
            assertFalse(unset_value_ids.contains("VALUE3"));
            assertFalse(unset_value_ids.contains("VALUE4"));
            assertTrue(unset_value_ids.contains("VALUE5"));
            assertTrue(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE5", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 2);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertFalse(unset_value_ids.contains("VALUE2"));
            assertFalse(unset_value_ids.contains("VALUE3"));
            assertFalse(unset_value_ids.contains("VALUE4"));
            assertFalse(unset_value_ids.contains("VALUE5"));
            assertTrue(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE6", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 1);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertFalse(unset_value_ids.contains("VALUE2"));
            assertFalse(unset_value_ids.contains("VALUE3"));
            assertFalse(unset_value_ids.contains("VALUE4"));
            assertFalse(unset_value_ids.contains("VALUE5"));
            assertFalse(unset_value_ids.contains("VALUE6"));
            assertTrue(unset_value_ids.contains("VALUE7"));

            template.setValue("VALUE7", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);
            assertFalse(unset_value_ids.contains("VALUE1"));
            assertFalse(unset_value_ids.contains("VALUE2"));
            assertFalse(unset_value_ids.contains("VALUE3"));
            assertFalse(unset_value_ids.contains("VALUE4"));
            assertFalse(unset_value_ids.contains("VALUE5"));
            assertFalse(unset_value_ids.contains("VALUE6"));
            assertFalse(unset_value_ids.contains("VALUE7"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testUnsetValuesDefaults() {
        Template template;
        Collection<String> unset_value_ids;
        try {
            template = TemplateFactory.HTML.get("values_long_in");

            unset_value_ids = template.getUnsetValueIds();
            assertNotNull(unset_value_ids);
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE1", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE2", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE3", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE4", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE5", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE6", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);

            template.setValue("VALUE7", "value");
            unset_value_ids = template.getUnsetValueIds();
            assertEquals(unset_value_ids.size(), 0);
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConditionalBlockRetrieval() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_simple_in");
            assertTrue(template.hasBlock("BLOCK1"));
            assertFalse(template.hasBlock("BLOCK1b"));
            try {
                template.getBlock("BLOCK1b");
                fail();
            } catch (BlockUnknownException e) {
                assertTrue(true);
            }
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionSimpleHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_simple_in");
            template.appendBlock("CONTENT", "BLOCK1");
            template.appendBlock("CONTENT", "BLOCK3");
            template.appendBlock("CONTENT", "BLOCK2");
            template.appendBlock("CONTENT", "BLOCK4");
            template.appendBlock("CONTENT", "BLOCK1");
            template.setValue("VALUE3", "value 3 early");    // will be overridden
            template.appendBlock("CONTENT", "BLOCK3");
            template.appendBlock("CONTENT", "BLOCK2");
            template.setValue("VALUE4", "value 4 early");    // will be removed
            template.appendBlock("CONTENT", "BLOCK4");
            template.removeValue("VALUE4");
            template.setValue("VALUE1", "value 1 late");    // late setting
            template.setValue("VALUE3", "value 3 late");    // late setting
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("construction_simple_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionSimpleTxt() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("construction_simple_in");
            template.appendBlock("CONTENT", "BLOCK1");
            template.appendBlock("CONTENT", "BLOCK3");
            template.appendBlock("CONTENT", "BLOCK2");
            template.appendBlock("CONTENT", "BLOCK4");
            template.appendBlock("CONTENT", "BLOCK1");
            template.setValue("VALUE3", "value 3 early");    // will be overridden
            template.appendBlock("CONTENT", "BLOCK3");
            template.appendBlock("CONTENT", "BLOCK2");
            template.setValue("VALUE4", "value 4 early");    // will be removed
            template.appendBlock("CONTENT", "BLOCK4");
            template.removeValue("VALUE4");
            template.setValue("VALUE1", "value 1 late");    // late setting
            template.setValue("VALUE3", "value 3 late");    // late setting
            assertEquals(template.getContent(), TemplateFactory.TXT.getParser().getTemplateContent("construction_simple_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionRepeatedHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_repeated_in");
            template.setBlock("VALUE2", "BLOCK1");
            template.setBlock("VALUE3", "BLOCK2");
            template.setBlock("VALUE4", "BLOCK3");
            template.setBlock("CONTENT", "BLOCK4");
            template.setValue("VALUE1", "value 1 late");
            template.setValue("VALUE2", "value 2 late");    // has no influence
            template.setValue("VALUE3", "value 3 late");    // has no influence
            template.setValue("VALUE4", "value 4 late");    // has no influence
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("construction_repeated_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionRepeatedTxt() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("construction_repeated_in");
            template.setBlock("VALUE2", "BLOCK1");
            template.setBlock("VALUE3", "BLOCK2");
            template.setBlock("VALUE4", "BLOCK3");
            template.setBlock("CONTENT", "BLOCK4");
            template.setValue("VALUE1", "value 1 late");
            template.setValue("VALUE2", "value 2 late");    // has no influence
            template.setValue("VALUE3", "value 3 late");    // has no influence
            template.setValue("VALUE4", "value 4 late");    // has no influence
            assertEquals(template.getContent(), TemplateFactory.TXT.getParser().getTemplateContent("construction_repeated_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionOverridingHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_overriding_in");

            assertFalse(template.isValueSet("VALUE2"));
            template.setValue("VALUE2", "value2");
            assertTrue(template.isValueSet("VALUE2"));
            template.setBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.HTML.getParser().getTemplateContent("construction_overriding_out_1"));
            template.clear();

            assertFalse(template.isValueSet("VALUE2"));
            template.setBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE2", "value2");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.HTML.getParser().getTemplateContent("construction_overriding_out_2"));
            template.clear();

            assertFalse(template.isValueSet("VALUE2"));
            template.setValue("VALUE2", "value2 ");
            assertTrue(template.isValueSet("VALUE2"));
            template.appendBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.HTML.getParser().getTemplateContent("construction_overriding_out_3"));
            template.clear();

            assertFalse(template.isValueSet("VALUE2"));
            template.setBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.appendValue("VALUE2", " value2");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.HTML.getParser().getTemplateContent("construction_overriding_out_4"));
            template.clear();
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionOverridingTxt() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("construction_overriding_in");

            assertFalse(template.isValueSet("VALUE2"));
            template.setValue("VALUE2", "value2");
            assertTrue(template.isValueSet("VALUE2"));
            template.setBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.TXT.getParser().getTemplateContent("construction_overriding_out_1"));
            template.clear();

            assertFalse(template.isValueSet("VALUE2"));
            template.setBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE2", "value2");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.TXT.getParser().getTemplateContent("construction_overriding_out_2"));
            template.clear();

            assertFalse(template.isValueSet("VALUE2"));
            template.setValue("VALUE2", "value2 ");
            assertTrue(template.isValueSet("VALUE2"));
            template.appendBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.TXT.getParser().getTemplateContent("construction_overriding_out_3"));
            template.clear();

            assertFalse(template.isValueSet("VALUE2"));
            template.setBlock("VALUE2", "BLOCK1");
            assertTrue(template.isValueSet("VALUE2"));
            template.appendValue("VALUE2", " value2");
            assertTrue(template.isValueSet("VALUE2"));
            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.TXT.getParser().getTemplateContent("construction_overriding_out_4"));
            template.clear();
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionDefaultValueHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_defaultvalue_in");

            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.HTML.getParser().getTemplateContent("construction_defaultvalue_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionDefaultValueText() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("construction_defaultvalue_in");

            template.setValue("VALUE1", "value1");
            assertEquals(template.getBlock("BLOCK2"), TemplateFactory.TXT.getParser().getTemplateContent("construction_defaultvalue_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionEmbeddedHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_embedded_in");

            template.setValue("member_value1", 1);
            template.appendBlock("rows", "row_first");
            template.setValue("member_value1", 2);
            template.appendBlock("rows", "row_second");
            template.setValue("member_value1", 3);
            template.appendBlock("rows", "row_first");
            template.setValue("member_value1", 4);
            template.appendBlock("rows", "row_second");
            template.appendBlock("rows", "row_first");
            template.setValue("member_value2", 5);
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("construction_embedded_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionEmbeddedTxt() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("construction_embedded_in");

            template.setValue("member_value1", 1);
            template.appendBlock("rows", "row_first");
            template.setValue("member_value1", 2);
            template.appendBlock("rows", "row_second");
            template.setValue("member_value1", 3);
            template.appendBlock("rows", "row_first");
            template.setValue("member_value1", 4);
            template.appendBlock("rows", "row_second");
            template.appendBlock("rows", "row_first");
            template.setValue("member_value2", 5);
            assertEquals(template.getContent(), TemplateFactory.TXT.getParser().getTemplateContent("construction_embedded_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    class TreeNode {
        private final Template template_;
        private final String text_;
        private final List<TreeNode> children_ = new ArrayList<>();
        private TreeNode parent_ = null;

        public TreeNode(Template template) {
            text_ = null;
            template_ = template;
        }

        public TreeNode(TreeNode parent, String text) {
            if (null == text) throw new IllegalArgumentException("title can't be null.");

            parent.addChild(this);
            text_ = text;
            template_ = parent.template_;
        }

        public void output() {
            if (0 == children_.size()) {
                template_.setValue("level", "");
            } else {
                InternalValue nodes = template_.createInternalValue();
                InternalValue text = null;
                int depth = 0;
                int counter = 0;

                for (TreeNode child : children_) {
                    child.output();
                    depth = child.getDepth();
                    template_.removeValue("indent");
                    if (1 == depth) {
                        template_.setValue("indent", "");
                    } else {
                        for (int i = 1; i < depth; i++) {
                            template_.appendBlock("indent", "indent");
                        }
                    }
                    template_.setValue("depth", depth);
                    text = template_.createInternalValue();
                    text.appendValue(++counter);
                    text.appendValue("-");
                    text.appendValue(child.getText());
                    template_.setValue("text", text);
                    nodes.appendBlock("node");
                }
                template_.setValue("nodes", nodes);
                template_.setBlock("level", "level");
            }
        }

        private void addChild(TreeNode child) {
            child.parent_ = this;
            children_.add(child);
        }

        public String getText() {
            return text_;
        }

        public TreeNode getParent() {
            return parent_;
        }

        public int getDepth() {
            TreeNode parent = getParent();
            int depth = 0;
            while (parent != null) {
                parent = parent.getParent();
                depth++;
            }

            return depth;
        }
    }

    @Test
    void testConstructionInternalValuesHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("construction_internalvalues_in");

            var tree = new TreeNode(template);
            var node1 = new TreeNode(tree, "node1");
            var node2 = new TreeNode(tree, "node2");
            var node3 = new TreeNode(tree, "node3");
            new TreeNode(node1, "node1a");
            new TreeNode(node1, "node1b");
            new TreeNode(node1, "node1c");
            var node2a = new TreeNode(node2, "node2a");
            new TreeNode(node2, "node2b");
            new TreeNode(node3, "node3a");
            new TreeNode(node3, "node3b");
            new TreeNode(node2a, "node2a1");
            new TreeNode(node2a, "node2a2");

            tree.output();
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("construction_internalvalues_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionInternalValuesTxt() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("construction_internalvalues_in");

            var tree = new TreeNode(template);
            var node1 = new TreeNode(tree, "node1");
            var node2 = new TreeNode(tree, "node2");
            var node3 = new TreeNode(tree, "node3");
            new TreeNode(node1, "node1a");
            new TreeNode(node1, "node1b");
            new TreeNode(node1, "node1c");
            var node2a = new TreeNode(node2, "node2a");
            new TreeNode(node2, "node2b");
            new TreeNode(node3, "node3a");
            new TreeNode(node3, "node3b");
            new TreeNode(node2a, "node2a1");
            new TreeNode(node2a, "node2a2");

            tree.output();
            assertEquals(template.getContent(), TemplateFactory.TXT.getParser().getTemplateContent("construction_internalvalues_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testConstructionInternalBlocksNameHashcodeConflicts() {
        try {
            assertEquals("DMn0".hashCode(), "Cln0".hashCode());
            assertEquals("DMn0".hashCode(), "DNNO".hashCode());
            assertEquals("FMmO".hashCode(), "EmMn".hashCode());
            assertTrue("DMn0".hashCode() != "FMmO".hashCode());
            assertTrue("DMn0".hashCode() != "HNMn".hashCode());
            assertTrue("FMmO".hashCode() != "HNMn".hashCode());
            var template = TemplateFactory.HTML.get("blocks_stringconflicts_in");
            var internal = template.createInternalValue();
            internal.appendBlock("DMn0");
            internal.appendBlock("Cln0");
            internal.appendBlock("DNNO");
            internal.appendBlock("FMmO");
            internal.appendBlock("EmMn");
            internal.appendBlock("HNMn");
            template.setValue("result", internal);
            assertEquals(template.getValue("result"),
                "1 : the first block" +
                    "1 : the second block" +
                    "1 : the third block" +
                    "2 : the first block" +
                    "2 : the second block" +
                    "3 : the first block");
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testHasValuesHtml() {
        Template template;
        try {
            template = TemplateFactory.HTML.get("defaultvalues_in");
            assertTrue(template.hasValueId("VALUE1"));
            assertTrue(template.hasValueId("VALUE2"));
            assertTrue(template.hasValueId("VALUE3"));
            assertFalse(template.hasValueId("VALUE4"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testHasValuesTxt() {
        Template template;
        try {
            template = TemplateFactory.TXT.get("defaultvalues_in");
            assertTrue(template.hasValueId("VALUE1"));
            assertTrue(template.hasValueId("VALUE2"));
            assertTrue(template.hasValueId("VALUE3"));
            assertFalse(template.hasValueId("VALUE4"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testUnsetValuesOutput() {
        var template = TemplateFactory.HTML.get("unsetvalues_output_in");
        assertTrue(template.hasValueId("VALUE1"));
        assertTrue(template.hasValueId("VALUE2"));
        assertTrue(template.hasValueId("VALUE3"));
        assertTrue(template.hasValueId("VALUE4"));
        assertTrue(template.hasValueId("VALUE5"));
        assertTrue(template.hasValueId("VALUE6"));
        assertTrue(template.hasValueId("VALUE7"));
        assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("unsetvalues_output_in"));
    }

    @Test
    void testSetBeanValues() {
        try {
            var template = TemplateFactory.HTML.get("values_bean_in");
            var bean = new BeanImpl();
            template.setBean(bean);
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testRemoveBeanValues() {
        try {
            var template = TemplateFactory.HTML.get("values_bean_in");
            var bean = new BeanImpl();
            template.setBean(bean);
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_out"));
            template.removeBean(bean);
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_in"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testSetBeanValuesPrefix() {
        try {
            var template = TemplateFactory.HTML.get("values_bean_prefix_in");
            var bean = new BeanImpl();
            template.setBean(bean, "PREFIX:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_prefix_out"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testRemoveBeanValuesPrefix() {
        try {
            var template = TemplateFactory.HTML.get("values_bean_prefix_in");
            var bean = new BeanImpl();
            template.setBean(bean, "PREFIX:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_prefix_out"));
            template.removeBean(bean, "WRONGPREFIX:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_prefix_out"));
            template.removeBean(bean, "PREFIX:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_prefix_in"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testSetBeanValuesHtml() {
        try {
            Template template;
            var bean = new HtmlBeanImpl();

            template = TemplateFactory.HTML.get("values_bean_html_in");
            template.setBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_emptyproperty"));

            bean = new HtmlBeanImpl();
            bean.setColors(new String[]{"red", "blue", "yellow"});
            bean.setWantsupdates(true);
            bean.setFirstname("Geert");
            bean.setLastname("Bevin");
            template = TemplateFactory.HTML.get("values_bean_html_in");
            template.setBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content1"));

            bean = new HtmlBeanImpl();
            bean.setColors(new String[]{"red", "orange", "white"});
            bean.setWantsupdates(false);
            bean.setFirstname("Angela");
            bean.setLastname("&<>");
            template = TemplateFactory.HTML.get("values_bean_html_in");
            template.setBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content2"));

            bean.addConstraint(new ConstrainedProperty("lastname").displayedRaw(true));
            template = TemplateFactory.HTML.get("values_bean_html_in");
            template.setBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content3"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testRemoveBeanValuesHtml() {
        try {
            Template template;
            HtmlBeanImpl bean;

            bean = new HtmlBeanImpl();
            bean.setColors(new String[]{"red", "blue", "yellow"});
            bean.setWantsupdates(true);
            bean.setFirstname("Geert");
            bean.setLastname("Bevin");
            template = TemplateFactory.HTML.get("values_bean_html_in");
            template.setBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content1"));
            template.removeBean(bean, "wrongparam:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content1"));
            template.removeBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_empty"));

            bean = new HtmlBeanImpl();
            bean.setColors(new String[]{"red", "orange", "white"});
            bean.setWantsupdates(false);
            bean.setFirstname("Angela");
            bean.setLastname("&<>");
            template = TemplateFactory.HTML.get("values_bean_html_in");
            template.setBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content2"));
            template.removeBean(bean, "wrongparam:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_content2"));
            template.removeBean(bean, "param:");
            assertEquals(template.getContent(), TemplateFactory.HTML.getParser().getTemplateContent("values_bean_html_out_empty"));
        } catch (TemplateException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    void testAttributes() {
        var template = TemplateFactory.HTML.get("empty");
        assertNotNull(template.getAttributes());
        assertEquals(0, template.getAttributes().size());

        assertNull(template.getAttribute("date"));
        assertNull(template.getAttribute("template"));
        assertFalse(template.hasAttribute("date"));
        assertFalse(template.hasAttribute("template"));
        var date = new Date();
        template.setAttribute("date", date);
        template.setAttribute("template", template);

        assertSame(date, template.getAttribute("date"));
        assertSame(template, template.getAttribute("template"));
        assertTrue(template.hasAttribute("date"));
        assertTrue(template.hasAttribute("template"));

        assertEquals(2, template.getAttributes().size());
        assertSame(date, template.getAttributes().get("date"));
        assertSame(template, template.getAttributes().get("template"));

        template.removeAttribute("date");
        assertNull(template.getAttribute("date"));
        assertSame(template, template.getAttribute("template"));

        assertFalse(template.hasAttribute("date"));
        assertTrue(template.hasAttribute("template"));

        assertEquals(1, template.getAttributes().size());
        assertNull(template.getAttributes().get("date"));
        assertSame(template, template.getAttributes().get("template"));

        var map = new HashMap<String, Object>();
        var date2 = new Date();
        var cal = Calendar.getInstance();
        map.put("date", date2);
        map.put("cal", cal);

        assertFalse(template.hasAttribute("date"));
        assertTrue(template.hasAttribute("template"));
        assertFalse(template.hasAttribute("cal"));

        template.setAttributes(map);
        assertEquals(3, template.getAttributes().size());
        assertTrue(template.hasAttribute("date"));
        assertTrue(template.hasAttribute("template"));
        assertTrue(template.hasAttribute("cal"));
        assertSame(date2, template.getAttributes().get("date"));
        assertSame(template, template.getAttributes().get("template"));
        assertSame(cal, template.getAttributes().get("cal"));
    }

    @Test
    void testGetValueOrAttribute() {
        var template1 = TemplateFactory.HTML.get("values");

        assertNull(template1.getValue("VALUE1"));
        assertNull(template1.getAttribute("VALUE1"));
        assertNull(template1.getValueOrAttribute("VALUE1"));

        template1.setValue("VALUE1", "CONTENT");
        template1.setAttribute("VALUE1", new StringBuilder("DATA"));

        assertEquals("CONTENT", template1.getValueOrAttribute("VALUE1"));
        template1.removeValue("VALUE1");
        assertEquals("DATA", template1.getValueOrAttribute("VALUE1"));
        template1.removeAttribute("VALUE1");
        assertNull(template1.getValueOrAttribute("VALUE1"));

        var template2 = TemplateFactory.HTML.get("defaultvalues_in");

        assertEquals("default1", template2.getValue("VALUE1"));
        assertNull(template2.getAttribute("VALUE1"));
        assertEquals("default1", template2.getValueOrAttribute("VALUE1"));
        
        template2.setValue("VALUE1", "CONTENT");
        template2.setAttribute("VALUE1", new StringBuilder("DATA"));

        assertEquals("CONTENT", template2.getValueOrAttribute("VALUE1"));
        template2.removeValue("VALUE1");
        assertEquals("DATA", template2.getValueOrAttribute("VALUE1"));
        template2.removeAttribute("VALUE1");
        assertEquals("default1", template2.getValueOrAttribute("VALUE1"));
    }
}
