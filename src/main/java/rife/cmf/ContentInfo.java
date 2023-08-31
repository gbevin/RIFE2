/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.cmf;

import rife.tools.Localization;
import rife.validation.ConstrainedProperty;
import rife.validation.Validation;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Map;

/**
 * This class represents all the information that is stored in the backend
 * about a certain {@link rife.cmf.Content Content} instance.
 * <p>The setters of this class are only present to make it possible for the
 * back-ends to automatically populate the information.
 *
 * @author Geert Bevin (gbevin[remove] at uwyn dot com)
 * @since 1.0
 */
public class ContentInfo extends Validation {
    private String path_ = null;
    private int version_ = -1;
    private Timestamp created_ = null;
    private String mimeType_ = null;
    private boolean fragment_ = false;
    private String name_ = null;
    private Map<String, String> attributes_ = null;
    private int size_ = -1;
    private Map<String, String> properties_ = null;

    /**
     * Instantiates a new {@code ContentInfo} instance.
     */
    public ContentInfo() {
    }

    public void activateValidation() {
        addConstraint(new ConstrainedProperty("path")
            .notNull(true)
            .notEmpty(true)
            .maxLength(255));
        addConstraint(new ConstrainedProperty("mimeType")
            .notNull(true)
            .notEmpty(true)
            .maxLength(80));
        addConstraint(new ConstrainedProperty("version")
            .notNull(true)
            .rangeBegin(0)
            .saved(false));
        addConstraint(new ConstrainedProperty("name")
            .maxLength(100));
        addConstraint(new ConstrainedProperty("created")
            .notNull(true)
            .saved(false));
        addConstraint(new ConstrainedProperty("attributes")
            .persistent(false));
        addConstraint(new ConstrainedProperty("size")
            .persistent(false));
        addConstraint(new ConstrainedProperty("properties")
            .persistent(false));
    }

    /**
     * Sets the path of the stored {@code Content} instance.
     * <p>The path has to be unique and will be used to retrieve this
     * particular {@code Content}.
     *
     * @param path the absolute and unique path
     * @see #getPath()
     * @since 1.0
     */
    public void setPath(String path) {
        path_ = path;
    }

    /**
     * Retrieves the path of the stored {@code Content} instance.
     *
     * @return the {@code Content}'s path
     * @see #setPath(String)
     * @see #getOptimalPath()
     * @since 1.0
     */
    public String getPath() {
        return path_;
    }

    /**
     * Retrieves the path of the stored {@code Content} instance in the
     * most optimal form for usage in the cmf.
     *
     * @return the {@code Content}'s most optimal path
     * @see #getPath()
     * @since 1.0
     */
    public String getOptimalPath() {
        if (null == path_) {
            return null;
        }

        if (null == name_) {
            return path_;
        }

        return path_ + '/' + name_;
    }

    /**
     * Sets the version of the stored {@code Content} instance.
     * <p>Version numbers are unique and should be increased successively when
     * the data on a certain {@code Content} is updated.
     * <p>The path and the version together identify exactly one particular
     * {@code Content} with one particular data.
     *
     * @param version the version as a unique integer
     * @see #getVersion()
     * @since 1.0
     */
    public void setVersion(int version) {
        version_ = version;
    }

    /**
     * Retrieves the version of the stored {@code Content} instance.
     *
     * @return the {@code Content}'s version
     * @see #setVersion(int)
     * @since 1.0
     */
    public int getVersion() {
        return version_;
    }

    /**
     * Sets the mime type of the stored {@code Content} instance.
     *
     * @param mimeType the {@code String} that identifies the mime type
     * @see #getMimeType()
     * @since 1.0
     */
    public void setMimeType(String mimeType) {
        mimeType_ = mimeType;
    }

    /**
     * Retrieves the mime type of the stored {@code Content} instance.
     *
     * @return the {@code Content}'s mime type textual identifier
     * @see #setMimeType(String)
     * @since 1.0
     */
    public String getMimeType() {
        return mimeType_;
    }

    /**
     * Sets whether the stored {@code Content} instance is a fragment or
     * not.
     *
     * @param fragment {@code true} if it's a fragment; or
     *                 <p>{@code false} otherwise
     * @see #isFragment()
     * @since 1.0
     */
    public void setFragment(boolean fragment) {
        fragment_ = fragment;
    }

    /**
     * Retrieves whether the stored {@code Content} instance is a fragment
     * or not.
     *
     * @return {@code true} if it's a fragment; or
     * <p>{@code false} otherwise
     * @see #setFragment(boolean)
     * @since 1.0
     */
    public boolean isFragment() {
        return fragment_;
    }

    /**
     * Sets the name of the stored {@code Content} instance.
     *
     * @param name the name
     * @see #getName()
     * @see #hasName()
     * @since 1.0
     */
    public void setName(String name) {
        name_ = name;
    }

    /**
     * Retrieves the name of the stored {@code Content} instance.
     *
     * @return {@code null} if the stored {@code Content} instance
     * has no name; or
     * <p>the name of the content
     * @see #setName(String)
     * @see #hasName()
     * @since 1.0
     */
    public String getName() {
        return name_;
    }

    /**
     * Indicates whether the stored {@code Content} instance has a name.
     *
     * @return {@code true} if it has a name; or
     * <p>{@code false} otherwise
     * @see #setName(String)
     * @see #getName()
     * @since 1.0
     */
    public boolean hasName() {
        return name_ != null;
    }

    /**
     * Sets the moment when the {@code Content} instance was stored.
     *
     * @param created the moment of creation
     * @see #getCreated()
     * @since 1.0
     */
    public void setCreated(Timestamp created) {
        created_ = created;
    }

    /**
     * Retrieves the moment when the {@code Content} instance was stored.
     *
     * @return the moment of creation
     * @see #setCreated(Timestamp)
     * @since 1.0
     */
    public Timestamp getCreated() {
        return created_;
    }

    /**
     * Sets the attributes map of the stored {@code Content} instance.
     *
     * @param attributes the attributes map with {@code String} keys and
     *                   value.
     * @see #getAttributes()
     * @since 1.0
     */
    public void setAttributes(Map<String, String> attributes) {
        attributes_ = attributes;
    }

    /**
     * Retrieves the attributes map of the stored {@code Content}
     * instance.
     *
     * @return the attributes map
     * @see #setAttributes(Map)
     * @since 1.0
     */
    public Map<String, String> getAttributes() {
        return attributes_;
    }

    /**
     * Indicates whether named content attributes are present.
     *
     * @return {@code true} if named content attributes are present; or
     * <p>{@code false} otherwise
     * @see #setAttributes(Map)
     * @see #getAttributes()
     * @since 1.0
     */
    public boolean hasAttributes() {
        return attributes_ != null && attributes_.size() > 0;
    }

    /**
     * Indicates whether a specific named content attribute is present.
     *
     * @param name the name of the attribute
     * @return {@code true} if the name content attribute is present; or
     * <p>{@code false} otherwise
     * @see #getAttribute(String)
     * @since 1.0
     */
    public boolean hasAttribute(String name) {
        if (null == attributes_) {
            return false;
        }

        return attributes_.containsKey(name);
    }

    /**
     * Retrieves the value of a named content attribute.
     *
     * @param name the name of the attribute
     * @return the value of the named content attribute; or
     * <p>{@code null} if no such attribute could be found
     * @see #hasAttribute(String)
     * @since 1.0
     */
    public String getAttribute(String name) {
        if (null == attributes_) {
            return null;
        }

        return attributes_.get(name);
    }

    /**
     * Sets the size of the stored {@code Content} instance.
     *
     * @param size the size of the content data
     * @see #getSize()
     * @since 1.0
     */
    public void setSize(int size) {
        size_ = size;
    }

    /**
     * Retrieves the size of the stored {@code Content} instance.
     *
     * @return the {@code Content}'s size
     * @see #setSize(int)
     * @see #getFormattedSize()
     * @since 1.0
     */
    public int getSize() {
        return size_;
    }

    /**
     * Retrieves the size of the stored {@code Content} instance as a
     * formatted string.
     *
     * @return the {@code Content}'s formatted size
     * @see #getSize()
     * @since 1.0
     */
    public String getFormattedSize() {
        var format = NumberFormat.getNumberInstance(Localization.getLocale());
        double size = getSize();
        var size_kb = size / 1024;
        if (size_kb >= 1024) {
            var size_mb = size_kb / 1024;
            if (size_mb >= 1024) {
                var size_gb = size_mb / 1024;
                format.setMaximumFractionDigits(2);
                return format.format(size_gb) + "GB";
            } else {
                format.setMaximumFractionDigits(2);
                return format.format(size_mb) + "MB";
            }
        } else {
            if (size_kb >= 100) {
                format.setMaximumFractionDigits(0);
            } else {
                format.setMaximumFractionDigits(2);
            }
            return format.format(size_kb) + "KB";
        }
    }

    /**
     * Sets the content data properties of the stored {@code Content}
     * instance.
     *
     * @param properties the content data properties
     * @see #hasProperties()
     * @see #getProperties()
     * @since 1.0
     */
    public void setProperties(Map<String, String> properties) {
        properties_ = properties;
    }

    /**
     * Indicates whether content data properties are present for the stored
     * {@code Content} instance.
     *
     * @return {@code true} if properties are present; or
     * <p>{@code false} otherwise
     * @see #setProperties(Map)
     * @see #getProperties()
     * @since 1.0
     */
    public boolean hasProperties() {
        return properties_ != null && properties_.size() > 0;
    }

    /**
     * Indicates whether a specific named content property is present.
     *
     * @param name the name of the property
     * @return {@code true} if the name content property is present; or
     * <p>{@code false} otherwise
     * @see #getProperty(String)
     * @since 1.0
     */
    public boolean hasProperty(String name) {
        if (null == properties_) {
            return false;
        }

        return properties_.containsKey(name);
    }

    /**
     * Retrieves the value of a named content property.
     *
     * @param name the name of the property
     * @return the value of the named content property; or
     * <p>{@code null} if no such property could be found
     * @see #hasProperty(String)
     * @since 1.0
     */
    public String getProperty(String name) {
        if (null == properties_) {
            return null;
        }

        return properties_.get(name);
    }

    /**
     * Retrieves the content data properties of the stored
     * {@code Content} instance.
     *
     * @return the content data properties; or
     * <p>{@code null} if no content data properties are present
     * @see #setProperties(Map)
     * @see #hasProperties()
     * @since 1.0
     */
    public Map<String, String> getProperties() {
        return properties_;
    }
}
