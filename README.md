Layout Framework
================

[![Build Status](https://travis-ci.org/imsweb/layout.svg?branch=master)](https://travis-ci.org/imsweb/layout)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.imsweb/layout/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.imsweb/layout)

The layout framework allows file formats to be declared using XML definition files; those formats can then be used to read and write corresponding data files.

Features
--------

* Supports XML definitions for *fixed-length-columns* and *comma-separated-values* formats.
* Other types of format can be supported, but those need to be defined programmatically.
* Recent [NAACCR](http://www.naaccr.org/) formats are included with the library, including all the data items documentation.
* Provides data file format auto-discovery.

Download
--------

The library will soon be available on Maven Central.

Reading a NAACCR file
---------------------

Each line is read as a record and represented as a map using keys defined 
in the [internal XML definition files](https://github.com/imsweb/layout/tree/master/src/main/resources/layout/fixed/naaccr). 

``` java
NaaccrLayout layout = (NaaccrLayout)LayoutFactory.getLayout(LayoutFactory.LAYOUT_ID_NAACCR_16_ABSTRACT);
for (<Map<String, String> record : layout.readAllRecords(new File("my_file.txt")))
    processRecord(record);
```

Using format auto-discovery
---------------------------

``` java
List<LayoutInfo> possibleFormats = LayoutFactory.discoverFormat(new File("my_file.txt"));
if (!possibleFormats.isEmpty())
    System.out.println("Best format for this data file is " + possibleFormats.get(0));
else
    System.out.println("Unknown format for this data file");
```

Registering new formats
-----------------------

From an XML file:

``` xml
<fixed-column-layout id="my-layout" name="My Layout" length="10">
    <field name="field1" start="1" end="10"/>
</fixed-column-layout>
```

``` java
FixedColumnsLayout layout = new FixedColumnsLayout(new File("my_layout.xml"))
LayoutFactory.registerLayout(layout);
```

Programmatically:

``` java
FixedColumnsLayout layout = new FixedColumnsLayout();
layout.setLayoutId("my-layout");
layout.setLayoutName("My Layout");
layout.setLayoutLineLength(10);
FixedColumnsField field1 = new FixedColumnsField();
field1.setName("field1");
field1.setStart(1);
field1.setEnd(10);
layout.setFields(Collections.singleton(field1));
LayoutFactory.registerLayout(layout);
````

Note that it is not necessary to register a layout for using it but the auto-discovery feature will work only with registered layouts.

