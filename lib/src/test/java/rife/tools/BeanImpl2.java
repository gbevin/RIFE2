/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.tools;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;

public class BeanImpl2 {
    private int propertyReadonly_ = 23;
    private String propertyString_ = null;
    private StringBuffer propertyStringBuffer_ = null;
    private StringBuilder propertyStringBuilder_ = null;
    private Date propertyDate_ = null;
    private Instant propertyInstant_ = null;
    private LocalDateTime propertyLocalDateTime_ = null;
    private LocalDate propertyLocalDate_ = null;
    private LocalTime propertyLocalTime_ = null;
    private char propertyChar_ = 0;
    private boolean propertyBoolean_ = false;
    private byte propertyByte_ = 0;
    private double propertyDouble_ = 0.0d;
    private float propertyFloat_ = 0.0f;
    private int propertyInt_ = 0;
    private long propertyLong_ = 0;
    private short propertyShort_ = 0;
    private Character propertyCharacterObject_ = null;
    private Boolean propertyBooleanObject_ = null;
    private Byte propertyByteObject_ = null;
    private Double propertyDoubleObject_ = null;
    private Float propertyFloatObject_ = null;
    private Integer propertyIntegerObject_ = null;
    private Long propertyLongObject_ = null;
    private Short propertyShortObject_ = null;
    private BigDecimal propertyBigDecimal_ = null;
    private String[] propertyStringArray_ = null;
    private StringBuffer[] propertyStringBufferArray_ = null;
    private StringBuilder[] propertyStringBuilderArray_ = null;
    private Date[] propertyDateArray_ = null;
    private Instant[] propertyInstantArray_ = null;
    private LocalDateTime[] propertyLocalDateTimeArray_ = null;
    private LocalDate[] propertyLocalDateArray_ = null;
    private LocalTime[] propertyLocalTimeArray_ = null;
    private char[] propertyCharArray_ = null;
    private boolean[] propertyBooleanArray_ = null;
    private byte[] propertyByteArray_ = null;
    private double[] propertyDoubleArray_ = null;
    private float[] propertyFloatArray_ = null;
    private int[] propertyIntArray_ = null;
    private long[] propertyLongArray_ = null;
    private short[] propertyShortArray_ = null;
    private Character[] propertyCharacterObjectArray_ = null;
    private Boolean[] propertyBooleanObjectArray_ = null;
    private Byte[] propertyByteObjectArray_ = null;
    private Double[] propertyDoubleObjectArray_ = null;
    private Float[] propertyFloatObjectArray_ = null;
    private Integer[] propertyIntegerObjectArray_ = null;
    private Long[] propertyLongObjectArray_ = null;
    private Short[] propertyShortObjectArray_ = null;
    private BigDecimal[] propertyBigDecimalArray_ = null;

    public BeanImpl2() {
    }

    public int getPropertyReadonly() {
        return propertyReadonly_;
    }

    public void setPropertyWriteOnly(long propertyWriteOnly) {
    }

    public int getPropertyInt() {
        return propertyInt_;
    }

    public void setPropertyInt(int propertyInt) {
        propertyInt_ = propertyInt;
    }

    public String getPropertyString() {
        return propertyString_;
    }

    public void setPropertyString(String propertyString) {
        propertyString_ = propertyString;
    }

    public double getPropertyDouble() {
        return propertyDouble_;
    }

    public void setPropertyDouble(double propertyDouble) {
        propertyDouble_ = propertyDouble;
    }

    public StringBuffer getPropertyStringBuffer() {
        return propertyStringBuffer_;
    }

    public void setPropertyStringBuffer(StringBuffer propertyStringBuffer) {
        propertyStringBuffer_ = propertyStringBuffer;
    }

    public StringBuilder getPropertyStringBuilder() {
        return propertyStringBuilder_;
    }

    public void setPropertyStringBuilder(StringBuilder propertyStringBuilder) {
        propertyStringBuilder_ = propertyStringBuilder;
    }

    public java.util.Date getPropertyDate() {
        return propertyDate_;
    }

    public void setPropertyDate(java.util.Date propertyDate) {
        propertyDate_ = propertyDate;
    }

    public Instant getPropertyInstant() {
        return propertyInstant_;
    }

    public void setPropertyInstant(Instant propertyInstant) {
        propertyInstant_ = propertyInstant;
    }

    public LocalDateTime getPropertyLocalDateTime() {
        return propertyLocalDateTime_;
    }

    public void setPropertyLocalDateTime(LocalDateTime propertyLocalDateTime) {
        propertyLocalDateTime_ = propertyLocalDateTime;
    }

    public LocalDate getPropertyLocalDate() {
        return propertyLocalDate_;
    }

    public void setPropertyLocalDate(LocalDate propertyLocalDate) {
        propertyLocalDate_ = propertyLocalDate;
    }

    public LocalTime getPropertyLocalTime() {
        return propertyLocalTime_;
    }

    public void setPropertyLocalTime(LocalTime propertyLocalTime) {
        propertyLocalTime_ = propertyLocalTime;
    }

    public boolean isPropertyBoolean() {
        return propertyBoolean_;
    }

    public void setPropertyBoolean(boolean propertyBoolean) {
        propertyBoolean_ = propertyBoolean;
    }

    public byte getPropertyByte() {
        return propertyByte_;
    }

    public void setPropertyByte(byte propertyByte) {
        propertyByte_ = propertyByte;
    }

    public float getPropertyFloat() {
        return propertyFloat_;
    }

    public void setPropertyFloat(float propertyFloat) {
        propertyFloat_ = propertyFloat;
    }

    public long getPropertyLong() {
        return propertyLong_;
    }

    public void setPropertyLong(long propertyLong) {
        propertyLong_ = propertyLong;
    }

    public short getPropertyShort() {
        return propertyShort_;
    }

    public void setPropertyShort(short propertyShort) {
        propertyShort_ = propertyShort;
    }

    public char getPropertyChar() {
        return propertyChar_;
    }

    public void setPropertyChar(char propertyChar) {
        propertyChar_ = propertyChar;
    }

    public Short getPropertyShortObject() {
        return propertyShortObject_;
    }

    public void setPropertyShortObject(Short propertyShortObject) {
        propertyShortObject_ = propertyShortObject;
    }

    public void setPropertyBigDecimal(BigDecimal propertyBigDecimal) {
        propertyBigDecimal_ = propertyBigDecimal;
    }

    public BigDecimal getPropertyBigDecimal() {
        return propertyBigDecimal_;
    }

    public Character getPropertyCharacterObject() {
        return propertyCharacterObject_;
    }

    public void setPropertyCharacterObject(Character propertyCharacterObject) {
        propertyCharacterObject_ = propertyCharacterObject;
    }

    public Boolean getPropertyBooleanObject() {
        return propertyBooleanObject_;
    }

    public void setPropertyBooleanObject(Boolean propertyBooleanObject) {
        propertyBooleanObject_ = propertyBooleanObject;
    }

    public Byte getPropertyByteObject() {
        return propertyByteObject_;
    }

    public void setPropertyByteObject(Byte propertyByteObject) {
        propertyByteObject_ = propertyByteObject;
    }

    public Double getPropertyDoubleObject() {
        return propertyDoubleObject_;
    }

    public void setPropertyDoubleObject(Double propertyDoubleObject) {
        propertyDoubleObject_ = propertyDoubleObject;
    }

    public Float getPropertyFloatObject() {
        return propertyFloatObject_;
    }

    public void setPropertyFloatObject(Float propertyFloatObject) {
        propertyFloatObject_ = propertyFloatObject;
    }

    public Integer getPropertyIntegerObject() {
        return propertyIntegerObject_;
    }

    public void setPropertyIntegerObject(Integer propertyIntegerObject) {
        propertyIntegerObject_ = propertyIntegerObject;
    }

    public Long getPropertyLongObject() {
        return propertyLongObject_;
    }

    public void setPropertyLongObject(Long propertyLongObject) {
        propertyLongObject_ = propertyLongObject;
    }


    public String[] getPropertyStringArray() {
        return propertyStringArray_;
    }

    public void setPropertyStringArray(String[] propertyStringArray) {
        propertyStringArray_ = propertyStringArray;
    }

    public StringBuffer[] getPropertyStringBufferArray() {
        return propertyStringBufferArray_;
    }

    public void setPropertyStringBufferArray(StringBuffer[] propertyStringBufferArray) {
        propertyStringBufferArray_ = propertyStringBufferArray;
    }

    public StringBuilder[] getPropertyStringBuilderArray() {
        return propertyStringBuilderArray_;
    }

    public void setPropertyStringBuilderArray(StringBuilder[] propertyStringBuilderArray) {
        propertyStringBuilderArray_ = propertyStringBuilderArray;
    }

    public Date[] getPropertyDateArray() {
        return propertyDateArray_;
    }

    public void setPropertyDateArray(Date[] propertyDateArray) {
        propertyDateArray_ = propertyDateArray;
    }

    public Instant[] getPropertyInstantArray() {
        return propertyInstantArray_;
    }

    public void setPropertyInstantArray(Instant[] propertyInstantArray) {
        propertyInstantArray_ = propertyInstantArray;
    }

    public LocalDateTime[] getPropertyLocalDateTimeArray() {
        return propertyLocalDateTimeArray_;
    }

    public void setPropertyLocalDateTimeArray(LocalDateTime[] propertyLocalDateTimeArray) {
        propertyLocalDateTimeArray_ = propertyLocalDateTimeArray;
    }

    public LocalDate[] getPropertyLocalDateArray() {
        return propertyLocalDateArray_;
    }

    public void setPropertyLocalDateArray(LocalDate[] propertyLocalDateArray) {
        propertyLocalDateArray_ = propertyLocalDateArray;
    }

    public LocalTime[] getPropertyLocalTimeArray() {
        return propertyLocalTimeArray_;
    }

    public void setPropertyLocalTimeArray(LocalTime[] propertyLocalTimeArray) {
        propertyLocalTimeArray_ = propertyLocalTimeArray;
    }

    public char[] getPropertyCharArray() {
        return propertyCharArray_;
    }

    public void setPropertyCharArray(char[] propertyCharArray) {
        propertyCharArray_ = propertyCharArray;
    }

    public boolean[] getPropertyBooleanArray() {
        return propertyBooleanArray_;
    }

    public void setPropertyBooleanArray(boolean[] propertyBooleanArray) {
        propertyBooleanArray_ = propertyBooleanArray;
    }

    public byte[] getPropertyByteArray() {
        return propertyByteArray_;
    }

    public void setPropertyByteArray(byte[] propertyByteArray) {
        propertyByteArray_ = propertyByteArray;
    }

    public double[] getPropertyDoubleArray() {
        return propertyDoubleArray_;
    }

    public void setPropertyDoubleArray(double[] propertyDoubleArray) {
        propertyDoubleArray_ = propertyDoubleArray;
    }

    public float[] getPropertyFloatArray() {
        return propertyFloatArray_;
    }

    public void setPropertyFloatArray(float[] propertyFloatArray) {
        propertyFloatArray_ = propertyFloatArray;
    }

    public int[] getPropertyIntArray() {
        return propertyIntArray_;
    }

    public void setPropertyIntArray(int[] propertyIntArray) {
        propertyIntArray_ = propertyIntArray;
    }

    public long[] getPropertyLongArray() {
        return propertyLongArray_;
    }

    public void setPropertyLongArray(long[] propertyLongArray) {
        propertyLongArray_ = propertyLongArray;
    }

    public short[] getPropertyShortArray() {
        return propertyShortArray_;
    }

    public void setPropertyShortArray(short[] propertyShortArray) {
        propertyShortArray_ = propertyShortArray;
    }

    public Character[] getPropertyCharacterObjectArray() {
        return propertyCharacterObjectArray_;
    }

    public void setPropertyCharacterObjectArray(Character[] propertyCharacterObjectArray) {
        propertyCharacterObjectArray_ = propertyCharacterObjectArray;
    }

    public Boolean[] getPropertyBooleanObjectArray() {
        return propertyBooleanObjectArray_;
    }

    public void setPropertyBooleanObjectArray(Boolean[] propertyBooleanObjectArray) {
        propertyBooleanObjectArray_ = propertyBooleanObjectArray;
    }

    public Byte[] getPropertyByteObjectArray() {
        return propertyByteObjectArray_;
    }

    public void setPropertyByteObjectArray(Byte[] propertyByteObjectArray) {
        propertyByteObjectArray_ = propertyByteObjectArray;
    }

    public Double[] getPropertyDoubleObjectArray() {
        return propertyDoubleObjectArray_;
    }

    public void setPropertyDoubleObjectArray(Double[] propertyDoubleObjectArray) {
        propertyDoubleObjectArray_ = propertyDoubleObjectArray;
    }

    public Float[] getPropertyFloatObjectArray() {
        return propertyFloatObjectArray_;
    }

    public void setPropertyFloatObjectArray(Float[] propertyFloatObjectArray) {
        propertyFloatObjectArray_ = propertyFloatObjectArray;
    }

    public Integer[] getPropertyIntegerObjectArray() {
        return propertyIntegerObjectArray_;
    }

    public void setPropertyIntegerObjectArray(Integer[] propertyIntegerObjectArray) {
        propertyIntegerObjectArray_ = propertyIntegerObjectArray;
    }

    public Long[] getPropertyLongObjectArray() {
        return propertyLongObjectArray_;
    }

    public void setPropertyLongObjectArray(Long[] propertyLongObjectArray) {
        propertyLongObjectArray_ = propertyLongObjectArray;
    }

    public Short[] getPropertyShortObjectArray() {
        return propertyShortObjectArray_;
    }

    public void setPropertyShortObjectArray(Short[] propertyShortObjectArray) {
        propertyShortObjectArray_ = propertyShortObjectArray;
    }

    public void setPropertyBigDecimalArray(BigDecimal[] propertyBigDecimalArray) {
        propertyBigDecimalArray_ = propertyBigDecimalArray;
    }

    public BigDecimal[] getPropertyBigDecimalArray() {
        return propertyBigDecimalArray_;
    }
}
