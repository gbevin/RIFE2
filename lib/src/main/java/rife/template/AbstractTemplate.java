/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.template;

import rife.config.RifeConfig;
import rife.forms.FormBuilder;
import rife.resources.ResourceFinder;
import rife.template.exceptions.*;
import rife.tools.Convert;
import rife.tools.Localization;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

public abstract class AbstractTemplate implements Template {
    protected String factoryIdentifier_ = null;
    protected String encoding_ = null;
    protected TemplateInitializer initializer_ = null;
    protected Set<String> generatedValues_ = new HashSet<>();
    protected Map<String, InternalString> fixedValues_ = new HashMap<>();
    protected Map<String, InternalValue> constructedValues_ = new HashMap<>();
    protected BeanHandler beanHandler_ = null;
    protected TemplateEncoder encoder_ = EncoderDummy.instance();
    protected List<ResourceBundle> defaultResourceBundles_ = null;
    protected List<ResourceBundle> resourceBundles_ = null;
    protected Map<String, Object> attributes_ = null;
    protected String language_ = null;
    protected String defaultContentType_ = null;

    public final void appendBlock(String valueBlockId) {
        appendBlock(valueBlockId, valueBlockId);
    }

    public final void appendBlock(String valueId, String blockId)
    throws TemplateException {
        if (null == valueId ||
            0 == valueId.length() ||
            !hasValueId(valueId)) {
            throw new ValueUnknownException(valueId);
        }
        if (null == blockId ||
            0 == blockId.length()) {
            throw new BlockUnknownException(blockId);
        }

        generatedValues_.remove(valueId);

        if (fixedValues_.containsKey(valueId)) {
            var constructed_value = new InternalValue(this);
            constructed_value.appendText(fixedValues_.get(valueId));
            if (!appendBlockInternalForm(blockId, constructed_value)) {
                throw new BlockUnknownException(blockId);
            }
            constructedValues_.put(valueId, constructed_value);
            fixedValues_.remove(valueId);
        } else if (constructedValues_.containsKey(valueId)) {
            if (!appendBlockInternalForm(blockId, constructedValues_.get(valueId))) {
                throw new BlockUnknownException(blockId);
            }
        } else {
            var constructed_value = new InternalValue(this);
            if (!appendBlockInternalForm(blockId, constructed_value)) {
                throw new BlockUnknownException(blockId);
            }
            constructedValues_.put(valueId, constructed_value);
        }
    }

    public final void setBlock(String valueBlockId)
    throws TemplateException {
        setBlock(valueBlockId, valueBlockId);
    }

    public final void setBlock(String valueId, String blockId)
    throws TemplateException {
        if (null == valueId ||
            0 == valueId.length() ||
            !hasValueId(valueId)) {
            throw new ValueUnknownException(valueId);
        }
        if (null == blockId ||
            0 == blockId.length()) {
            throw new BlockUnknownException(blockId);
        }

        generatedValues_.remove(valueId);
        fixedValues_.remove(valueId);

        var constructed_value = new InternalValue(this);
        if (!appendBlockInternalForm(blockId, constructed_value)) {
            throw new BlockUnknownException(blockId);
        }
        constructedValues_.put(valueId, constructed_value);
    }

    public String getBlock(String id)
    throws TemplateException {
        if (null == id ||
            0 == id.length()) {
            throw new BlockUnknownException(id);
        }

        var result = new ExternalValue();

        if (!appendBlockExternalForm(id, result)) {
            throw new BlockUnknownException(id);
        }
        return result.toString();
    }

    public final String getContent()
    throws TemplateException {
        var set_values = processLateTags();

        var result = new ExternalValue();

        if (!appendBlockExternalForm("", result)) {
            throw new BlockUnknownException("");
        }
        var content = result.toString();
        removeValues(set_values);
        return content;
    }

    public void writeBlock(String id, OutputStream out)
    throws IOException, TemplateException {
        writeBlock(id, out, null);
    }

    public void writeBlock(String id, OutputStream out, String charsetName)
    throws IOException, TemplateException {
        if (null == out) {
            return;
        }
        if (null == id ||
            0 == id.length()) {
            throw new BlockUnknownException(id);
        }

        var result = new ExternalValue();

        if (!appendBlockExternalForm(id, result)) {
            throw new BlockUnknownException(id);
        }

        result.write(out, charsetName);
    }

    public final void writeContent(OutputStream out)
    throws IOException, TemplateException {
        writeContent(out, null);
    }

    public final void writeContent(OutputStream out, String charsetName)
    throws IOException, TemplateException {
        if (null == out) {
            return;
        }

        var result = new ExternalValue();

        if (!appendBlockExternalForm("", result)) {
            throw new BlockUnknownException("");
        }

        result.write(out, charsetName);
    }

    public final void write(OutputStream out)
    throws IOException, TemplateException {
        writeContent(out);
    }

    public final List<CharSequence> getDeferredBlock(String id)
    throws TemplateException {
        if (null == id ||
            0 == id.length()) {
            throw new BlockUnknownException(id);
        }

        var result = new ExternalValue();

        if (!appendBlockExternalForm(id, result)) {
            throw new BlockUnknownException(id);
        }

        return result;
    }

    public final List<CharSequence> getDeferredContent()
    throws TemplateException {
        var set_values = processLateTags();

        var result = new ExternalValue();

        if (!appendBlockExternalForm("", result)) {
            throw new BlockUnknownException("");
        }

        removeValues(set_values);

        return result;
    }

    private List<String> processLateTags() {
        var set_values = new ArrayList<String>();

        _evaluateL10nTags(set_values);
        _evaluateLangTags(set_values, null);

        return set_values;
    }

    public List<String> evaluateRenderTags()
    throws TemplateException {
        List<String> set_values = new ArrayList<>();
        _evaluateRenderTags(set_values);
        return set_values;
    }

    private void _evaluateRenderTags(List<String> setValues)
    throws TemplateException {
        if (hasFilteredValues(TemplateFactoryFilters.TAG_RENDER)) {
            var render_tags = getFilteredValues(TemplateFactoryFilters.TAG_RENDER);
            for (var captured_groups : render_tags) {
                var tag = captured_groups[0];

                var result = evaluateRenderTag(tag, captured_groups[1], captured_groups[2]);
                setValue(tag, result);
                if (setValues != null) {
                    setValues.add(tag);
                }
            }
        }

        generatedValues_.addAll(setValues);
    }

    private String evaluateRenderTag(String name, String classname, String differentiator) {
        try {
            Class klass = Class.forName(classname);
            if (!ValueRenderer.class.isAssignableFrom(klass)) {
                throw new RendererWrongTypeException(this, classname);
            }

            ValueRenderer renderer;
            try {
                renderer = (ValueRenderer) klass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RendererInstantiationException(this, classname, e);
            }

            return renderer.render(this, name, differentiator);
        } catch (ClassNotFoundException e) {
            throw new RendererNotFoundException(this, classname, e);
        }
    }

    public List<String> evaluateL10nTags() {
        var set_values = new ArrayList<String>();
        _evaluateL10nTags(set_values);
        return set_values;
    }

    private void _evaluateL10nTags(List<String> setValues) {
        // process the localization keys
        var l10n_tags = getFilteredValues(TemplateFactoryFilters.TAG_L10N);
        if (l10n_tags != null && l10n_tags.size() > 0) {
            String l10n_key;
            String l10n_value;
            String l10n_bundle;

            for (var captured_groups : l10n_tags) {
                // only set the config value if the value hasn't been set in the
                // template yet
                if (!isValueSet(captured_groups[0])) {
                    l10n_value = null;

                    // check if an explicit bundle name was provided
                    // if not go through all the bundles that have been registered
                    // for this template instance
                    if (null == captured_groups[2]) {
                        if (hasResourceBundles()) {
                            l10n_key = captured_groups[1];

                            for (var bundle : resourceBundles_) {
                                // obtain the configuration value
                                try {
                                    l10n_value = bundle.getString(l10n_key);
                                    break;
                                } catch (MissingResourceException e) {
                                    // no-op, go to the next resource bundle
                                }
                            }
                        }
                    } else {
                        l10n_bundle = captured_groups[1];
                        l10n_key = captured_groups[2];

                        var bundle = Localization.getResourceBundle(l10n_bundle);
                        if (bundle != null) {
                            l10n_value = bundle.getString(l10n_key);
                        } else {
                            throw new ResourceBundleNotFoundException(getName(), captured_groups[0], l10n_bundle);
                        }
                    }

                    // don't continue if the l10n parameter doesn't exist
                    if (l10n_value != null) {
                        // set the l10n value in the template
                        setValue(captured_groups[0], getEncoder().encodeDefensive(l10n_value));
                        if (setValues != null) {
                            setValues.add(captured_groups[0]);
                        }
                    }
                }
            }
        }

        generatedValues_.addAll(setValues);
    }

    public List<String> evaluateLangTags(String id) {
        if (null == id) throw new IllegalArgumentException("id can't be null.");

        var set_values = new ArrayList<String>();
        _evaluateLangTags(set_values, TemplateFactoryFilters.PREFIX_LANG + id);
        return set_values;
    }

    private void _evaluateLangTags(List<String> setValues, String id) {
        // process the lang keys
        var lang_blocks = getFilteredBlocks(TemplateFactoryFilters.TAG_LANG);
        var language = getLanguage();
        if (lang_blocks != null &&
            language != null) {

            for (var lang_block : lang_blocks) {
                if (id != null &&
                    !id.equals(lang_block[1])) {
                    continue;
                }

                if (null == id &&
                    isValueSet(lang_block[1])) {
                    continue;
                }

                var block_lang = lang_block[lang_block.length - 1];
                if (block_lang.equals(language)) {
                    setBlock(lang_block[1], lang_block[0]);
                    if (setValues != null) {
                        setValues.add(lang_block[1]);
                    }
                }
            }
        }

        generatedValues_.addAll(setValues);
    }

    public final InternalValue createInternalValue() {
        return new InternalValue(this);
    }

    public final void setValue(String id, List<CharSequence> deferredContent)
    throws TemplateException {
        if (null == id ||
            0 == id.length() ||
            !hasValueId(id)) {
            throw new ValueUnknownException(id);
        }

        generatedValues_.remove(id);
        fixedValues_.remove(id);

        constructedValues_.put(id, new InternalValue(this, deferredContent));
    }

    public final void setValue(String id, InternalValue internalValue)
    throws TemplateException {
        if (null == id ||
            0 == id.length() ||
            !hasValueId(id)) {
            throw new ValueUnknownException(id);
        }
        if (null == internalValue) {
            internalValue = createInternalValue();
        }

        generatedValues_.remove(id);
        fixedValues_.remove(id);

        constructedValues_.put(id, internalValue);
    }

    public final void setValue(String id, Template template)
    throws TemplateException {
        if (null == template) {
            setValue(id, "");
        } else {
            setValue(id, template.getContent());
        }
    }

    public final void setValue(String id, Object value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, boolean value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, char value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, char[] value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, char[] value, int offset, int count)
    throws TemplateException {
        setValue(id, String.valueOf(value, offset, count));
    }

    public final void setValue(String id, double value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, float value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, int value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, long value)
    throws TemplateException {
        setValue(id, String.valueOf(value));
    }

    public final void setValue(String id, String value)
    throws TemplateException {
        setValue(id, (CharSequence) value);
    }

    public final void setValue(String id, CharSequence value)
    throws TemplateException {
        if (null == id ||
            0 == id.length() ||
            !hasValueId(id)) {
            throw new ValueUnknownException(id);
        }
        if (null == value) {
            value = "";
        }

        generatedValues_.remove(id);
        fixedValues_.remove(id);
        constructedValues_.remove(id);
        fixedValues_.put(id, new InternalString(value));
    }

    public final void setValueEncoded(String id, Object value)
    throws TemplateException {
        setValue(id, getEncoder().encode(String.valueOf(value)));
    }

    public void setBean(Object bean)
    throws TemplateException {
        setBean(bean, null, true);
    }

    public void setBean(Object bean, String prefix)
    throws TemplateException {
        setBean(bean, prefix, true);
    }

    public void setBean(Object bean, String prefix, boolean encode)
    throws TemplateException {
        if (null == beanHandler_) {
            throw new BeanHandlerUnsupportedException(this, bean);
        }

        beanHandler_.setBean(this, bean, prefix, encode);
    }

    public void removeBean(Object bean)
    throws TemplateException {
        removeBean(bean, null);
    }

    public void removeBean(Object bean, String prefix)
    throws TemplateException {
        if (null == beanHandler_) {
            throw new BeanHandlerUnsupportedException(this, bean);
        }

        beanHandler_.removeBean(this, bean, prefix);
    }

    public final void appendValue(String id, Object value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, boolean value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, char value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, char[] value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, char[] value, int offset, int count)
    throws TemplateException {
        appendValue(id, String.valueOf(value, offset, count));
    }

    public final void appendValue(String id, double value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, float value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, int value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, long value)
    throws TemplateException {
        appendValue(id, String.valueOf(value));
    }

    public final void appendValue(String id, String value)
    throws TemplateException {
        if (null == id ||
            0 == id.length() ||
            !hasValueId(id)) {
            throw new ValueUnknownException(id);
        }
        if (null == value) {
            return;
        }

        generatedValues_.remove(id);

        if (fixedValues_.containsKey(id)) {
            fixedValues_.get(id).append(value);
        } else if (constructedValues_.containsKey(id)) {
            constructedValues_.get(id).appendText(value);
        } else {
            fixedValues_.put(id, new InternalString(value));
        }
    }

    public final void appendValueEncoded(String id, Object value)
    throws TemplateException {
        appendValue(id, getEncoder().encode(String.valueOf(value)));
    }

    public final String getValue(String id)
    throws TemplateException {
        if (null == id ||
            0 == id.length() ||
            !hasValueId(id)) {
            throw new ValueUnknownException(id);
        }

        if (fixedValues_.containsKey(id)) {
            return fixedValues_.get(id).toString();
        }
        if (constructedValues_.containsKey(id)) {
            var result = new ExternalValue();
            constructedValues_.get(id).appendExternalForm(result);
            return result.toString();
        }

        return getDefaultValue(id);
    }

    public abstract String getDefaultValue(String id);

    public boolean hasDefaultValue(String id) {
        if (null == getDefaultValue(id)) {
            return false;
        }

        return true;
    }

    public abstract Collection<String[]> getFilteredBlocks(String filter);

    public abstract boolean hasFilteredBlocks(String filter);

    public abstract String[] getFilteredBlock(String filter, String id);

    public abstract Collection<String[]> getFilteredValues(String filter);

    public abstract boolean hasFilteredValues(String filter);

    public abstract String[] getFilteredValue(String filter, String id);

    public final boolean hasBlock(String id) {
        if (null == id ||
            0 == id.length()) {
            return false;
        }

        var temp_value = new ExternalValue();

        return appendBlockExternalForm(id, temp_value);
    }

    public final boolean isValueSet(String id) {
        if (null == id ||
            0 == id.length()) {
            return false;
        }
        return !generatedValues_.contains(id) && (fixedValues_.containsKey(id) || constructedValues_.containsKey(id));
    }

    public final boolean isValueGenerated(String id) {
        if (null == id ||
            0 == id.length()) {
            return false;
        }

        return generatedValues_.contains(id);
    }

    public final void addGeneratedValue(String valueId) {
        generatedValues_.add(valueId);
    }

    public final void addGeneratedValues(Collection<String> valueIds) {
        generatedValues_.addAll(valueIds);
    }

    public final int countValues() {
        return fixedValues_.size() + constructedValues_.size();
    }

    public final void removeValue(String id) {
        if (null == id ||
            0 == id.length() ||
            !hasValueId(id)) {
            throw new ValueUnknownException(id);
        }

        generatedValues_.remove(id);
        fixedValues_.remove(id);
        constructedValues_.remove(id);
    }

    public final void removeValues(Collection<String> ids) {
        if (null == ids ||
            0 == ids.size()) {
            return;
        }

        for (var id : ids) {
            removeValue(id);
        }
    }

    public final void removeGeneratedValues() {
        var generated_values = generatedValues_;
        generatedValues_ = new HashSet<>();
        removeValues(generated_values);
    }

    public final void blankValue(String id) {
        setValue(id, "");
    }

    public final void clear() {
        generatedValues_ = new HashSet<>();
        fixedValues_ = new HashMap<>();
        constructedValues_ = new HashMap<>();
        resourceBundles_ = null;
        if (defaultResourceBundles_ != null) {
            resourceBundles_ = new ArrayList<>(defaultResourceBundles_);
        }
        initialize();
    }

    public abstract String[] getAvailableValueIds();

    public abstract Collection<String> getUnsetValueIds();

    public abstract boolean hasValueId(String id);

    public abstract long getModificationTime();

    // make the template only instantiatable from within this package or from derived classes
    protected AbstractTemplate() {
    }

    protected void appendTextInternal(InternalValue value, CharSequence text) {
        value.appendText(text);
    }

    protected void increasePartsCapacityInternal(InternalValue value, int size) {
        value.increasePartsCapacity(size);
    }

    protected void increaseValuesCapacityInternal(InternalValue value, int size) {
        value.increaseValuesCapacity(size);
    }

    protected abstract boolean appendBlockExternalForm(String id, ExternalValue result);

    protected abstract boolean appendBlockInternalForm(String id, InternalValue result);

    protected final String potentiallyEvaluateRenderTag(String id) {
        var captured_groups = getFilteredValue(TemplateFactoryFilters.TAG_RENDER, id);
        if (captured_groups != null) {
            return evaluateRenderTag(id, captured_groups[1], captured_groups[2]);
        }
        return null;
    }

    protected final void appendValueExternalForm(String id, String tag, ExternalValue result) {
        assert id != null;
        assert id.length() != 0;

        CharSequence fixed_value = potentiallyEvaluateRenderTag(id);
        if (fixed_value == null) {
            fixed_value = fixedValues_.get(id);
        }

        if (fixed_value != null) {
            result.add(fixed_value);
            return;
        }

        var constructed_value = constructedValues_.get(id);
        if (constructed_value != null) {
            constructed_value.appendExternalForm(result);
            return;
        }

        if (!appendDefaultValueExternalForm(id, result)) {
            result.add(tag);
        }
    }

    protected abstract boolean appendDefaultValueExternalForm(String id, ExternalValue result);

    protected final void appendValueInternalForm(String id, String tag, InternalValue result) {
        CharSequence fixed_value = potentiallyEvaluateRenderTag(id);
        if (fixed_value == null) {
            fixed_value = fixedValues_.get(id);
        }

        if (fixed_value != null) {
            result.appendText(fixed_value);
            return;
        }

        var constructed_value = constructedValues_.get(id);
        if (constructed_value != null) {
            result.appendConstructedValue(constructed_value);
            return;
        }

        if (!appendDefaultValueInternalForm(id, result)) {
            result.appendValueId(id, tag);
        }
    }

    protected abstract boolean appendDefaultValueInternalForm(String id, InternalValue result);

    public Template createNewInstance() {
        return TemplateFactory.getFactory(factoryIdentifier_).get(getFullName(), encoding_);
    }

    public final String getFactoryIdentifier() {
        return factoryIdentifier_;
    }

    final void setFactoryIdentifier(String identifier) {
        factoryIdentifier_ = identifier;
    }

    public final String getEncoding() {
        return encoding_;
    }

    final void setEncoding(String encoding) {
        encoding_ = encoding;
    }

    public final BeanHandler getBeanHandler() {
        return beanHandler_;
    }

    final void setBeanHandler(BeanHandler beanHandler) {
        beanHandler_ = beanHandler;
    }

    public final FormBuilder getFormBuilder() {
        var bean_handler = getBeanHandler();
        if (null == bean_handler) {
            return null;
        }
        return bean_handler.getFormBuilder();
    }

    public final TemplateEncoder getEncoder() {
        return encoder_;
    }

    final void setEncoder(TemplateEncoder encoder) {
        if (null == encoder) {
            encoder_ = EncoderDummy.instance();
        } else {
            encoder_ = encoder;
        }
    }

    void setDefaultResourceBundles(ArrayList<ResourceBundle> bundles) {
        defaultResourceBundles_ = bundles;
        if (bundles != null) {
            resourceBundles_ = new ArrayList<>(bundles);
        }
    }

    public final void addResourceBundles(Collection<ResourceBundle> resourceBundles) {
        if (null == resourceBundles) {
            return;
        }

        if (null == resourceBundles_) {
            resourceBundles_ = new ArrayList<>();
        }

        resourceBundles_.addAll(resourceBundles);
    }

    public final void addResourceBundle(ResourceBundle resourceBundle) {
        if (null == resourceBundle) {
            return;
        }

        if (null == resourceBundles_) {
            resourceBundles_ = new ArrayList<>();
        }

        resourceBundles_.add(resourceBundle);
    }

    public final List<ResourceBundle> getResourceBundles() {
        if (null == resourceBundles_) {
            resourceBundles_ = new ArrayList<>();
        }

        return resourceBundles_;
    }

    public final boolean hasResourceBundles() {
        return resourceBundles_ != null && resourceBundles_.size() > 0;
    }

    public void setLanguage(String lang) {
        language_ = lang;
    }

    public String getLanguage() {
        if (null == language_) {
            return RifeConfig.tools().getDefaultLanguage();
        }

        return language_;
    }

    public void setAttribute(String name, Object value) {
        if (null == attributes_) {
            attributes_ = new HashMap<>();
        }

        attributes_.put(name, value);
    }

    public void setAttributes(Map<String, Object> map) {
        if (null == attributes_) {
            attributes_ = new HashMap<>();
        }

        attributes_.putAll(map);
    }

    public void removeAttribute(String name) {
        if (null == attributes_) {
            return;
        }

        attributes_.remove(name);
    }

    public boolean hasAttribute(String name) {
        if (null == attributes_) {
            return false;
        }
        return attributes_.containsKey(name);
    }

    public Object getAttribute(String name) {
        if (null == attributes_) {
            return null;
        }
        return attributes_.get(name);
    }

    public Map<String, Object> getAttributes() {
        if (attributes_ == null) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(attributes_);
    }

    public String getValueOrAttribute(String name) {
        String value = null;
        if (isValueSet(name)) {
            value = getValue(name);
        }
        if (value == null && hasAttribute(name)) {
            value = Convert.toString(getAttribute(name));
        }
        if (value == null && hasDefaultValue(name)) {
            value = getDefaultValue(name);
        }
        return value;
    }

    final void initialize()
    throws TemplateException {
        evaluateL10nTags();

        if (null == initializer_) {
            return;
        }

        initializer_.initialize(this);
    }

    final void setInitializer(TemplateInitializer initializer) {
        initializer_ = initializer;
    }

    public String getDefaultContentType() {
        return defaultContentType_;
    }

    public void setDefaultContentType(String defaultContentType) {
        defaultContentType_ = defaultContentType;
    }

    protected static boolean isTemplateClassModified(URL templateResource, long templateModificationTime,
                                                     Map<URL, Long> templateDependencies, String templateModificationState,
                                                     ResourceFinder resourceFinder, String modificationState) {
        try {
            if (Parser.getModificationTime(resourceFinder, templateResource) > templateModificationTime) {
                return true;
            }

            if (templateDependencies.size() > 0) {
                for (var dependency_resource : templateDependencies.keySet()) {
                    if (Parser.getModificationTime(resourceFinder, dependency_resource) > templateDependencies.get(dependency_resource)) {
                        return true;
                    }
                }
            }

            if (templateModificationState != null || modificationState != null) {
                if (null == templateModificationState || null == modificationState) {
                    return true;
                }

                if (!templateModificationState.equals(modificationState)) {
                    return true;
                }
            }
        } catch (TemplateException e) {
            return false;
        }

        return false;
    }

    public Template clone() {
        AbstractTemplate new_template;
        try {
            new_template = (AbstractTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            new_template = null;
        }

        new_template.beanHandler_ = beanHandler_;
        new_template.initializer_ = initializer_;
        new_template.encoder_ = encoder_;
        new_template.language_ = language_;
        new_template.defaultContentType_ = defaultContentType_;

        new_template.generatedValues_ = new HashSet<>();
        for (var value_id : generatedValues_) {
            new_template.generatedValues_.add(value_id);
        }

        new_template.fixedValues_ = new HashMap<>();
        for (var value_id : fixedValues_.keySet()) {
            new_template.fixedValues_.put(value_id, fixedValues_.get(value_id));
        }

        new_template.constructedValues_ = new HashMap<>();
        for (var constructed_value_id : constructedValues_.keySet()) {
            new_template.constructedValues_.put(constructed_value_id, constructedValues_.get(constructed_value_id));
        }

        if (attributes_ != null) {
            new_template.attributes_ = new HashMap<>(attributes_);
        }

        return new_template;
    }
}
