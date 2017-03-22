/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.layout.hl7.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import com.imsweb.layout.hl7.entity.Hl7Component;
import com.imsweb.layout.hl7.entity.Hl7RepeatedField;

@XStreamAlias("hl7-field")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"_value"})
public class Hl7FieldDefinitionXmlDto {

    private String _value;

    @XStreamAsAttribute
    private Integer _index;

    @XStreamImplicit
    private List<Hl7RepeatedField> _hl7RepeatedFields;

    @XStreamImplicit
    private List<Hl7Component> _hl7Components;

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    public Integer getIndex() {
        return _index;
    }

    public void setIndex(Integer index) {
        _index = index;
    }

    public List<Hl7RepeatedField> getHl7RepeatedField() {
        if (_hl7RepeatedFields == null)
            _hl7RepeatedFields = new ArrayList<>();
        return _hl7RepeatedFields;
    }

    public List<Hl7Component> getHl7Components() {
        if (_hl7Components == null)
            _hl7Components = new ArrayList<>();
        return _hl7Components;
    }
}
