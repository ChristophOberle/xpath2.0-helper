# xpath2.0-helper
xpath2.0-helper simplifies the use of XPath2.0 to navigate an XML document from Java

## Motivation
When a Java application needs to look up a table to map some source values to a target value 
this can be achieved by

- using an XML document for the table data
- navigating the XML document with an XPath expression

## Dependencies
xpath2.0-helper uses Saxon through the s9api. 
This means, that all XPath2.0 features are available.

## Features
XML documents may use namespaces.
These namespaces must be available to the XPath expression.

XPath expressions may use variables. 
In XPath2.0 these variables are of a certain data type.
The variable values must be set before execution of the XPath expression.

The following example shows how xpath2.0-helper can be used:

    /**
     * this test uses bankCodeTable.xml to find the calculationMethod for a bankCode at a specific asOfDate
     */
    public void testXpathHelper()
    {
        Processor processor;
        XmlDocumentHelper bankCodeTable;
        XPathHelper calculationMethodForBankCodeAtAsOfDate;
        try {
            processor = new Processor(false);
            bankCodeTable = new XmlDocumentHelper(processor, "src/test/resources/bankCodeTable.xml");
            calculationMethodForBankCodeAtAsOfDate = new XPathHelper(
                    processor,
                    new String[][] { {"fn", "http://www.w3.org/2005/xpath-functions"} },
                    new String[][] { {"bankCode", "string" }, {"asOfDate", "string"} },
                    "/file/bankCode[@id=$bankCode and fn:compare(validFrom, $asOfDate) <= 0 and " +
                            "fn:compare(validTo, $asOfDate) >= 0]/calculationMethod"
            );

            String bankCode = "50010517";
            String asOfDate = "2025-03-01";

            // Execution of XPath-Expression returning the calculationMethod for the bankCode at the specified asOfDate
            // 1. load XPath
            calculationMethodForBankCodeAtAsOfDate.loadXPath();
            // 2. set Variables
            calculationMethodForBankCodeAtAsOfDate.setStringVariable("bankCode", bankCode);
            calculationMethodForBankCodeAtAsOfDate.setStringVariable("asOfDate", asOfDate);
            // 3. set ContextItem from XdmNode
            XPathSelector selector = calculationMethodForBankCodeAtAsOfDate.setContextItem(bankCodeTable.asXdmNode());
            // 4. read result
            String calculationMethod = null;
            for (XdmItem item : selector) {
                if (item != null) {
                    calculationMethod = item.getStringValue();
                    break;
                }
            }
            assertEquals("C1", calculationMethod);
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }


