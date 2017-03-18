/*
 * Copyright (C) 2017 Information Management Services, Inc.
 */
package com.imsweb.layout.hl7.entity;

import java.util.HashMap;
import java.util.Map;

public class Hl7RepeatedField {

    private Hl7Field _field;

    private Map<Integer, Hl7Component> _components;

    public Hl7RepeatedField(Hl7Field field) {
        _field = field;
        _components = new HashMap<>();

        if (field != null)
            field.addRepeatedField(this);
    }

    public Hl7RepeatedField(Hl7Field field, String value) {
        this(field);
        addComponent(new Hl7Component(this, 1, value));
    }

    public Hl7Field getField() {
        return _field;
    }

    public void setField(Hl7Field field) {
        if (field == null)
            throw new RuntimeException("Parent field cannot be null");
        _field = field;
    }

    public Map<Integer, Hl7Component> getComponents() {
        return _components;
    }

    public void setComponents(Map<Integer, Hl7Component> components) {
        _components = components == null ? new HashMap<>() : components;
    }

    public Hl7Component addComponent(Hl7Component component) {
        _components.put(component.getIndex(), component);
        return component;
    }

    public Hl7Component getComponent(int componentIdx) {
        Hl7Component result = _components.get(componentIdx);
        return result == null ? new Hl7Component(null, componentIdx) : result;
    }

    public String getValue() {
        String value = Hl7Utils.repeatedFieldToString(this);
        return value.isEmpty() ? null : value;
    }
}
