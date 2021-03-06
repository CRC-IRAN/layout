/*
 * Copyright (C) 2013 Information Management Services, Inc.
 */
package com.imsweb.layout.record.fixed.naaccr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.imsweb.layout.Field;
import com.imsweb.layout.LayoutFactory;
import com.imsweb.layout.LayoutInfo;
import com.imsweb.layout.LayoutInfoDiscoveryOptions;
import com.imsweb.layout.LayoutUtils;
import com.imsweb.layout.record.fixed.FixedColumnsField;
import com.imsweb.layout.record.fixed.FixedColumnsLayout;
import com.imsweb.layout.record.fixed.xml.FixedColumnLayoutFieldXmlDto;
import com.imsweb.layout.record.fixed.xml.FixedColumnLayoutXmlDto;

/**
 * This class contains the logic related to all NAACCR layouts.
 */
public class NaaccrLayout extends FixedColumnsLayout {

    //  following styles are used in all the NAACCR versions
    private static final StringBuilder _CSS_STYLE_SUMMARY_TABLE = new StringBuilder();

    static {
        _CSS_STYLE_SUMMARY_TABLE.append(".naaccr-summary-table { width: 100%; }\n");
        _CSS_STYLE_SUMMARY_TABLE.append(".naaccr-summary-header { text-align: center; padding: 2px; background-color: #E0E0E0; }\n");
        _CSS_STYLE_SUMMARY_TABLE.append(".naaccr-summary-cell { vertical-align:top; padding: 2px; }\n");
        _CSS_STYLE_SUMMARY_TABLE.append(".naaccr-summary-centered { text-align: center; }\n");
        _CSS_STYLE_SUMMARY_TABLE.append(".naaccr-borders { border: 1px solid gray; border-collapse: collapse; }\n");
        _CSS_STYLE_SUMMARY_TABLE.append(".naaccr-underline { text-decoration:underline; }\n");
    }

    // following styles are used only in version 13 and prior
    private static final StringBuilder _CSS_STYLE_13_AND_PRIOR = new StringBuilder();

    static {
        _CSS_STYLE_13_AND_PRIOR.append(".naaccr-codes-cell { text-align: left; vertical-align:top; padding: 2px; padding-left: 5px; padding-right: 5px }\n");
        _CSS_STYLE_13_AND_PRIOR.append(".naaccr-codes-padding { padding-right: 40px; }\n");
        _CSS_STYLE_13_AND_PRIOR.append(".naaccr-codes-nowrap { white-space: nowrap; }\n");
    }

    // following styles are used only in version 14 and later (those come straight from the online NAACCR HTML page)
    private static final StringBuilder _DEFAULT_CSS_14_AND_LATER = new StringBuilder();

    static {
        _DEFAULT_CSS_14_AND_LATER.append("#readerWrapper { margin-top: 20px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#pnlSearch { width: 100%;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#lblSearch { margin-right: 20px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".wrapper { margin-bottom: 50px; width: 775px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(
                ".col { border-bottom: 1px solid black; border-left: 1px solid black; border-top: 1px solid black; float: left; height: 1100px; margin-bottom: 50px; padding: 0 5px; width: 180px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(
                ".colnoborder { border-bottom: 1px solid black; border-top: 1px solid black; float: left; height: 1100px; margin-bottom: 50px; padding: 0 5px; width: 180px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColHead td:first-child { text-align: left;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColBody { text-align: center; vertical-align: top;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColTitle { background-color: #165185; color: #FABA44;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColDataStripe td:first-child { border-left: 0 none;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColDataStripe table tr td { padding: 0; text-align: center;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColData td:first-child { border-left: 0 none;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColData table tr td { padding: 0; text-align: center;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColData td.HighlightToolTip,\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableColDataStripe td.HighlightToolTip { border-bottom: 1px dashed black; background-color: #a7d9f2;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".tableAppendixBody { border-left: 0 none; display: table-row; padding-bottom: 1px; text-align: left;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".code-nbr { padding-right: 20px; vertical-align: top;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#stateList { padding-bottom: 30px; text-align: center; width: 720px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#chapter { width: 950px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder { display: none; left: 0; position: absolute; top: 0; z-index: 99999;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_top { padding-bottom: 5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_bottom { padding-top: 5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_right { padding-left: 5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_left { padding-right: 5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tblSearch { margin-top: 20px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".dataDictionaryHeader { padding-right: 10px; text-align: right;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#menuWrapper { height: 200px; margin-top: 20px; width: 950px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".chapterColumn { border-right: 2px solid white; float: left;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("a .chapter { color: #FFFFFF; padding: 0; text-decoration: none;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".chapter:hover { background: none repeat scroll 0 0 #FF9900; color: #FFFFFF; text-decoration: none;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_arrow, #tiptip_arrow_inner { border-color: transparent; border-style: solid; border-width: 6px; height: 0; position: absolute; width: 0;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_top #tiptip_arrow { border-top-color: #FFFFCE;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_bottom #tiptip_arrow { border-bottom-color: #FFFFCE;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_right #tiptip_arrow { border-right-color: #FFFFCE;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_left #tiptip_arrow { border-left-color: #FFFFCE;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_top #tiptip_arrow_inner { border-top-color: #FFFFCE; margin-left: -6px; margin-top: -7px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_bottom #tiptip_arrow_inner { border-bottom-color: #FFFFCE; margin-left: -6px; margin-top: -5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_right #tiptip_arrow_inner { border-right-color: #FFFFCE; margin-left: -5px; margin-top: -6px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_left #tiptip_arrow_inner { border-left-color: #FFFFCE; margin-left: -7px; margin-top: -6px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".GeoCtrAlpha { float: left; width: 300px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".threeColSubTitle { float: left; font-style: italic; margin-bottom: 12px; width: 300px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".colRight { border-bottom: 1px solid black; border-left: 1px solid black; float: right; height: 1150px; padding-left: 20px; width: 454px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".colLeft { border-bottom: 1px solid black; float: left; height: 1150px; padding-left: 20px; width: 455px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".GeoCtr { float: left; width: 460px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".threeCol { border-bottom: 1px solid black; float: left; height: 1135px; padding-left: 10px; width: 305px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".threeColTitle { float: left; font-weight: bold; width: 300px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".CountryContinentName { float: left; font-weight: bold; margin-bottom: 15px; margin-top: 15px; width: 400px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(
                ".threeColMid { -moz-border-bottom-colors: none; -moz-border-image: none; -moz-border-left-colors: none; -moz-border-right-colors: none; -moz-border-top-colors: none; border-color: -moz-use-text-color black black; border-style: none solid solid; border-width: medium 1px 1px; float: left; height: 1135px; padding-left: 10px; width: 305px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_content { background-color: #FFFFCE; padding: 4px 8px 5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_bottom #tiptip_arrow_inner { border-bottom-color: #FFFFCE;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#tiptip_holder.tip_top #tiptip_arrow_inner { border-top-color: rgba(20, 20, 20, 0.92);}\n");
        _DEFAULT_CSS_14_AND_LATER.append("h1 { margin: 1em 0;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("h2, h3 { margin: 0 0 1.33em 0;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("h4 { margin: 0;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("ul, ol { margin: 0 0 1.33em 35px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("ul li, ol li { margin-bottom: 1.33em;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("table.padded td, table.padded th { padding: 5px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("ul.nobullets { list-style: none;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("ul.notspaced li, ol.notspaced li { margin-bottom: 0;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".c8cell1 { width: 70px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#pnlSearch { width: 100%;}\n");
        _DEFAULT_CSS_14_AND_LATER.append("#lblSearch { margin-right: 20px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".chap10-head-table { margin-top: 15px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".chap10-para-head { font-weight: bold; padding-top: 10px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".chap10-para { padding-bottom: 10px;}\n");
        _DEFAULT_CSS_14_AND_LATER.append(".chap10-para ul li { margin-bottom: 0;}\n");
    }

    protected String _naaccrVersion;

    protected String _majorNaaccrVersion;

    protected String _minorNaaccrVersion;

    protected String _recordType;

    protected int _naaccrLineLength;

    public NaaccrLayout(String naaccrVersion, String recordType, int naaccrLineLength, String layoutId, boolean loadFields) {
        super();

        if (!(naaccrVersion.matches("\\d\\d\\d")))
            throw new RuntimeException("Unexpected NAACCR version format for " + layoutId);

        _naaccrVersion = naaccrVersion;
        _majorNaaccrVersion = naaccrVersion.substring(0, 2);
        _minorNaaccrVersion = naaccrVersion.substring(2);
        _recordType = recordType;
        _naaccrLineLength = naaccrLineLength;

        String fullRecType;
        if ("A".equals(getRecordType()))
            fullRecType = "Abstract";
        else if ("M".equals(getRecordType()))
            fullRecType = "Modified";
        else if ("C".equals(getRecordType()))
            fullRecType = "Confidential";
        else if ("I".equals(getRecordType()))
            fullRecType = "Incidence";
        else
            throw new RuntimeException("Unsupported rec type: " + getRecordType());

        try {
            FixedColumnLayoutXmlDto xmlLayout = new FixedColumnLayoutXmlDto();
            xmlLayout.setId(layoutId);
            xmlLayout.setName(LayoutFactory.getAvailableInternalLayouts().get(layoutId));
            xmlLayout.setVersion(naaccrVersion);
            xmlLayout.setDescription("Latest version of NAACCR " + _majorNaaccrVersion + " " + fullRecType + " (" + _majorNaaccrVersion + "." + _minorNaaccrVersion + ")");
            xmlLayout.setLength(getNaaccrLineLength());

            // I needed this optimization because NAACCR layouts have tons of fields, and we can't afford loading them each time we need to identify a file...
            if (loadFields) {
                String xmlFilename = "naaccr-" + getMajorNaaccrVersion() + "-layout.xml";
                FixedColumnLayoutXmlDto tmpXmlLayout = LayoutUtils.readFixedColumnsLayout(Thread.currentThread().getContextClassLoader().getResourceAsStream("layout/fixed/naaccr/" + xmlFilename));

                // do not add the fields pass the required line length
                List<FixedColumnLayoutFieldXmlDto> fields = new ArrayList<>();
                for (FixedColumnLayoutFieldXmlDto f : tmpXmlLayout.getField())
                    if (f.getStart() <= getNaaccrLineLength() || f.getStart() > tmpXmlLayout.getLength()) // allow fields greater than the max length will ensure errors are properly reported...
                        fields.add(f);
                xmlLayout.setField(fields);

                // set default value for the record type
                FixedColumnLayoutFieldXmlDto field = xmlLayout.getField().get(0);
                if (!"recordType".equals(field.getName()))
                    throw new RuntimeException("Record Type should be the first field of a NAACCR layout!");
                field.setDefaultValue(getRecordType());
            }

            init(xmlLayout);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to instanciate NAACCR layout", e);
        }
    }

    protected String getMajorNaaccrVersion() {
        return _majorNaaccrVersion;
    }

    protected String getRecordType() {
        return _recordType;
    }

    protected int getNaaccrLineLength() {
        return _naaccrLineLength;
    }

    @Override
    public String getFieldDocByNaaccrItemNumber(Integer num) {
        Field field = getFieldByNaaccrItemNumber(num);
        if (field == null)
            return null;

        return getFieldDocByName(field.getName());
    }

    @Override
    public String getFieldDocByName(String name) {
        String result = null;

        FixedColumnsField field = getFieldByName(name);
        if (field == null)
            return null;

        boolean reservedField = field.getName().startsWith("reserved");
        
        URL docPath;
        if (reservedField)
            docPath = Thread.currentThread().getContextClassLoader().getResource("layout/fixed/naaccr/doc/reserved.html");
        else
            docPath = Thread.currentThread().getContextClassLoader().getResource("layout/fixed/naaccr/doc/" + getDocFolder() + "/" + name + ".html");
        if (docPath == null)
            return null;

        try {
            InputStream is = docPath.openStream();
            if (is == null)
                return null;

            StringWriter writer = new StringWriter();
            Reader reader = new InputStreamReader(is, "UTF-8");

            char[] buf = new char[1024];
            int n;
            while ((n = reader.read(buf, 0, 1024)) != -1)
                writer.write(buf, 0, n);
            writer.flush();
            writer.close();
            reader.close();

            result = writer.toString();

        }
        catch (IOException e) {
            /* do nothing, result will be null, as per specs */
        }
        
        if (reservedField && result != null)
            result = result.replace("[:ITEM_NUM:]", field.getNaaccrItemNum().toString()).replace("[:COLUMNS:]", field.getStart() + " - " + field.getEnd());

        return result;
    }

    protected String getDocFolder() {
        return "naaccr" + getMajorNaaccrVersion();
    }

    @Override
    public String getFieldDocDefaultCssStyle() {
        // I know, the correct way to do this is to use the class hierarchy, but I don't want to repeat the CSS in all the NAACCR sub-classes...
        String docFolder = getDocFolder();

        StringBuilder buf = new StringBuilder();
        buf.append(_CSS_STYLE_SUMMARY_TABLE);
        if ("naaccr12".equals(docFolder) || "naaccr13".equals(docFolder))
            buf.append(_CSS_STYLE_13_AND_PRIOR);
        else
            buf.append(_DEFAULT_CSS_14_AND_LATER);

        return buf.toString();
    }

    @Override
    public String validateLine(String line, Integer lineNumber) {
        StringBuilder msg = new StringBuilder();

        if (line == null || line.isEmpty()) {
            msg.append("line ").append(lineNumber).append(": line is empty");
        }
        else {
            String naaccrVersion = extractNaaccrVersion(line);
            String recordType = extractRecordType(line);

            if (naaccrVersion.isEmpty() || recordType.isEmpty()) {
                String ver = naaccrVersion.isEmpty() ? "blank" : ("'" + naaccrVersion + "'");
                String type = recordType.isEmpty() ? "blank" : ("'" + recordType + "'");
                msg.append("line ").append(lineNumber).append(": unable to determine data format; NAACCR Record Version (Item #50) is ").append(ver).append(", Record Type (Item #10) is ");
                msg.append(type).append(" and line length is ").append(LayoutUtils.formatNumber(line.length()));
            }
            else if (naaccrVersion.startsWith(getMajorNaaccrVersion())) {
                if (getRecordType().equals(recordType)) {
                    if (line.length() != getNaaccrLineLength()) {
                        String real = LayoutUtils.formatNumber(line.length());
                        msg.append("line ").append(lineNumber).append(": conflict between NAACCR Record Version (Item #50), Record Type (Item #10) and line length.\n        Rec Version=");
                        msg.append(naaccrVersion).append(". Rec Type=").append(recordType).append(". Expected length ").append(getNaaccrLineLength()).append(" but length in file is ").append(real);
                    }
                }
                else
                    msg.append("line ").append(lineNumber).append(": invalid Record Type (Item #10): '").append(recordType).append("' ; value should be '").append(getRecordType()).append("'.");
            }
            else
                msg.append("line ").append(lineNumber).append(": invalid NAACCR Record Version (Item #50): '").append(naaccrVersion).append("' ; value should start with '").append(
                        getMajorNaaccrVersion()).append("'.");
        }

        return msg.length() == 0 ? null : msg.toString();
    }

    @Override
    protected String cleanValue(String value, Field field) {
        if ("naaccrRecordVersion".equals(field.getName()))
            return getLayoutVersion();
        if ("recordType".equals(field.getName()))
            return getRecordType();
        return value;
    }

    protected String extractNaaccrVersion(String line) {
        return line.length() <= 19 ? "" : line.substring(16, 19).trim();
    }

    protected String extractRecordType(String line) {
        return line.isEmpty() ? "" : line.substring(0, 1).trim();
    }

    @Override
    public LayoutInfo buildFileInfo(String firstRecord, LayoutInfoDiscoveryOptions options) {
        if (firstRecord == null || firstRecord.isEmpty())
            return null;

        String naaccrVersion = extractNaaccrVersion(firstRecord);
        String recordType = extractRecordType(firstRecord);

        LayoutInfo result = null;

        if (naaccrVersion.startsWith(getMajorNaaccrVersion()) || (naaccrVersion.isEmpty() && options.isNaaccrAllowBlankRecordType())) {
            if (getRecordType().equals(recordType) || (recordType.isEmpty() && options.isNaaccrAllowBlankVersion())) {
                if (options.isFixedColumnAllowDiscoveryFromLineLength() && firstRecord.length() == getNaaccrLineLength()) {
                    result = new LayoutInfo();
                    result.setLayoutId(getLayoutId());
                    result.setLayoutName(getLayoutName());
                    result.setLineLength(getLayoutLineLength());
                }
            }
        }

        return result;
    }
}
