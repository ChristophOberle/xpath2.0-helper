package de.elrebo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.saxon.s9api.*;

/**
 * Unit test for Class XPathHelper
 *
 * Copyright 2025 Christoph Oberle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class XPathHelperTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public XPathHelperTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( XPathHelperTest.class );
    }

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
                    "/file/bankCode[@id=$bankCode and fn:compare(validFrom, $asOfDate) <= 0 and fn:compare(validTo, $asOfDate) >= 0]/calculationMethod"
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
}
