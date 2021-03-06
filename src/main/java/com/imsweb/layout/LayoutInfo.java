/*
 * Copyright (C) 2011 Information Management Services, Inc.
 */
package com.imsweb.layout;

/**
 * This class encapsulates the information about a given layout that should be used to read/write a given file.
 * <p/>
 * See <code>LayoutFactory.discoverFormat</code>.
 * <p/>
 * Created on Sep 18, 2011 by Fabian
 * @author Fabian
 */
public class LayoutInfo {

    // layout ID
    public String _layoutId;

    // layout name
    public String _layoutName;

    // line length (not applicable to all layout types)
    public Integer _lineLength;

    // number of fields (not applicable to all layout types)
    public Integer _numFields;

    public String getLayoutId() {
        return _layoutId;
    }

    public void setLayoutId(String layoutId) {
        _layoutId = layoutId;
    }

    public String getLayoutName() {
        return _layoutName;
    }

    public void setLayoutName(String layoutName) {
        _layoutName = layoutName;
    }

    public Integer getLineLength() {
        return _lineLength;
    }

    public void setLineLength(Integer lineLength) {
        _lineLength = lineLength;
    }

    public Integer getNumFields() {
        return _numFields;
    }

    public void setNumFields(Integer numFields) {
        _numFields = numFields;
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LayoutInfo other = (LayoutInfo)o;

        return _layoutId == null ? other._layoutId == null : _layoutId.equals(other._layoutId); 
    }

    @Override
    public int hashCode() {
        return _layoutId != null ? _layoutId.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        // do not change this method; it is used by several applications
        buf.append(_layoutName);
        if (_lineLength != null)
            buf.append(" [").append(LayoutUtils.formatNumber(_lineLength)).append(" char]");
        else if (_numFields != null) {
            buf.append(" [").append(_numFields).append(" field");
            if (_numFields > 1)
                buf.append("s");
            buf.append("]");
        }

        return buf.toString();
    }
}
