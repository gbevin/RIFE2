/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.tools;

import rife.config.RifeConfig;
import rife.validation.ConstrainedProperty;
import rife.validation.MetaData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Locale;

public class BeanImpl3 extends MetaData {
    private Date propertyDate_ = null;
    private Instant propertyInstant_ = null;
    private LocalDateTime propertyLocalDateTime_ = null;
    private LocalDate propertyLocalDate_ = null;
    private LocalTime propertyLocalTime_ = null;
    private byte propertyByte_ = 0;
    private double propertyDouble_ = 0.0d;
    private float propertyFloat_ = 0.0f;
    private int propertyInt_ = 0;
    private long propertyLong_ = 0;
    private short propertyShort_ = 0;
    private Byte propertyByteObject_ = null;
    private Double propertyDoubleObject_ = null;
    private Float propertyFloatObject_ = null;
    private Integer propertyIntegerObject_ = null;
    private Long propertyLongObject_ = null;
    private Short propertyShortObject_ = null;
    private BigDecimal propertyBigDecimal_ = null;
    private SerializableType propertySerializableType_ = null;
    private Date[] propertyDateArray_ = null;
    private Instant[] propertyInstantArray_ = null;
    private LocalDateTime[] propertyLocalDateTimeArray_ = null;
    private LocalDate[] propertyLocalDateArray_ = null;
    private LocalTime[] propertyLocalTimeArray_ = null;
    private byte[] propertyByteArray_ = null;
    private double[] propertyDoubleArray_ = null;
    private float[] propertyFloatArray_ = null;
    private int[] propertyIntArray_ = null;
    private long[] propertyLongArray_ = null;
    private short[] propertyShortArray_ = null;
    private Byte[] propertyByteObjectArray_ = null;
    private Double[] propertyDoubleObjectArray_ = null;
    private Float[] propertyFloatObjectArray_ = null;
    private Integer[] propertyIntegerObjectArray_ = null;
    private Long[] propertyLongObjectArray_ = null;
    private Short[] propertyShortObjectArray_ = null;
    private BigDecimal[] propertyBigDecimalArray_ = null;
    private SerializableType[] propertySerializableTypeArray_ = null;

    public void activateMetaData() {
        var date_format = RifeConfig.tools().getSimpleDateFormat("'custom format' yyyy-MM-dd HH:mm");
        var int_format = NumberFormat.getCurrencyInstance(Locale.US);
        var double_format = NumberFormat.getNumberInstance(Locale.US);
        var byte_format = NumberFormat.getPercentInstance(Locale.US);
        var float_format = NumberFormat.getNumberInstance(Locale.FRANCE);
        var long_format = NumberFormat.getCurrencyInstance(Locale.US);
        var short_format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        var bigdecimal_format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.FRANCE);
        bigdecimal_format.setParseBigDecimal(true);
        var serialization_formatter = new SerializationFormatter();

        addConstraint(new ConstrainedProperty("propertyDate").format(date_format));
        addConstraint(new ConstrainedProperty("propertyInstant").format(date_format));
        addConstraint(new ConstrainedProperty("propertyLocalDateTime").format(date_format));
        addConstraint(new ConstrainedProperty("propertyLocalDate").format(date_format));
        addConstraint(new ConstrainedProperty("propertyLocalTime").format(date_format));
        addConstraint(new ConstrainedProperty("propertyInt").format(int_format));
        addConstraint(new ConstrainedProperty("propertyIntegerObject").format(int_format));
        addConstraint(new ConstrainedProperty("propertyDouble").format(double_format));
        addConstraint(new ConstrainedProperty("propertyDoubleObject").format(double_format));
        addConstraint(new ConstrainedProperty("propertyByte").format(byte_format));
        addConstraint(new ConstrainedProperty("propertyByteObject").format(byte_format));
        addConstraint(new ConstrainedProperty("propertyFloat").format(float_format));
        addConstraint(new ConstrainedProperty("propertyFloatObject").format(float_format));
        addConstraint(new ConstrainedProperty("propertyLong").format(long_format));
        addConstraint(new ConstrainedProperty("propertyLongObject").format(long_format));
        addConstraint(new ConstrainedProperty("propertyShort").format(short_format));
        addConstraint(new ConstrainedProperty("propertyShortObject").format(short_format));
        addConstraint(new ConstrainedProperty("propertyBigDecimal").format(bigdecimal_format));
        addConstraint(new ConstrainedProperty("propertySerializableType").format(serialization_formatter));

        addConstraint(new ConstrainedProperty("propertyDateArray").format(date_format));
        addConstraint(new ConstrainedProperty("propertyInstantArray").format(date_format));
        addConstraint(new ConstrainedProperty("propertyLocalDateTimeArray").format(date_format));
        addConstraint(new ConstrainedProperty("propertyLocalDateArray").format(date_format));
        addConstraint(new ConstrainedProperty("propertyLocalTimeArray").format(date_format));
        addConstraint(new ConstrainedProperty("propertyIntArray").format(int_format));
        addConstraint(new ConstrainedProperty("propertyIntegerObjectArray").format(int_format));
        addConstraint(new ConstrainedProperty("propertyDoubleArray").format(double_format));
        addConstraint(new ConstrainedProperty("propertyDoubleObjectArray").format(double_format));
        addConstraint(new ConstrainedProperty("propertyByteArray").format(byte_format));
        addConstraint(new ConstrainedProperty("propertyByteObjectArray").format(byte_format));
        addConstraint(new ConstrainedProperty("propertyFloatArray").format(float_format));
        addConstraint(new ConstrainedProperty("propertyFloatObjectArray").format(float_format));
        addConstraint(new ConstrainedProperty("propertyLongArray").format(long_format));
        addConstraint(new ConstrainedProperty("propertyLongObjectArray").format(long_format));
        addConstraint(new ConstrainedProperty("propertyShortArray").format(short_format));
        addConstraint(new ConstrainedProperty("propertyShortObjectArray").format(short_format));
        addConstraint(new ConstrainedProperty("propertyBigDecimalArray").format(bigdecimal_format));
        addConstraint(new ConstrainedProperty("propertySerializableTypeArray").format(serialization_formatter));
    }

    public BeanImpl3() {
    }

    public int getPropertyInt() {
        return propertyInt_;
    }

    public void setPropertyInt(int propertyInt) {
        propertyInt_ = propertyInt;
    }

    public double getPropertyDouble() {
        return propertyDouble_;
    }

    public void setPropertyDouble(double propertyDouble) {
        propertyDouble_ = propertyDouble;
    }

    public Date getPropertyDate() {
        return propertyDate_;
    }

    public void setPropertyDate(Date propertyDate) {
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

    public SerializableType getPropertySerializableType() {
        return propertySerializableType_;
    }

    public void setPropertySerializableType(SerializableType propertySerializableType) {
        propertySerializableType_ = propertySerializableType;
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

    public void setPropertyBigDecimalArray(BigDecimal[] propertyBigDecimalArray) {
        propertyBigDecimalArray_ = propertyBigDecimalArray;
    }

    public BigDecimal[] getPropertyBigDecimalArray() {
        return propertyBigDecimalArray_;
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

    public SerializableType[] getPropertySerializableTypeArray() {
        return propertySerializableTypeArray_;
    }

    public void setPropertySerializableTypeArray(SerializableType[] propertySerializableTypeArray) {
        propertySerializableTypeArray_ = propertySerializableTypeArray;
    }

    public static class SerializableType implements Serializable {
        private int number_ = -1;
        private String string_ = null;

        public SerializableType(int number, String string) {
            number_ = number;
            string_ = string;
        }

        public void setNumber(int number) {
            number_ = number;
        }

        public int getNumber() {
            return number_;
        }

        public void setString(String string) {
            string_ = string;
        }

        public String getString() {
            return string_;
        }

        public String toString() {
            return number_ + ":" + string_;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (null == other) {
                return false;
            }

            if (!(other instanceof SerializableType)) {
                return false;
            }

            var other_datalink = (SerializableType) other;
            if (!other_datalink.getString().equals(getString())) {
                return false;
            }
            if (other_datalink.getNumber() != getNumber()) {
                return false;
            }

            return true;
        }
    }
}
