package de.elrebo;

import net.sf.saxon.s9api.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class XPathHelper simplifies Access to XML documents from Java programs.
 * It evaluates an XPath expression against an XdmNode.
 * <p>
 * Copyright 2025 Christoph Oberle
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class XPathHelper {
    /**
     * compiled XPath Executable
     */
    private final XPathExecutable xPathExecutable;
    /**
     * the Selector for the XPath execution
     */
    private XPathSelector selector;
    /**
     * Constructor from processor, filePath, namespaces, variables and xPath
     * @param processor the Saxon processor
     * @param namespaces the mapping of namespace prefixes to namespaces
     * @param variables the list of variables (names and types)
     * @param xPath the XPath expression
     * @throws SaxonApiException if an error occurs
     */
    public XPathHelper(Processor processor, String[][] namespaces, String[][] variables, String xPath) throws SaxonApiException {
        ItemTypeFactory itf = new ItemTypeFactory(processor);
        List<ItemType> types = new ArrayList<>();
        for (String[] variable : variables) {
            types.add(itf.getAtomicType(new QName("http://www.w3.org/2001/XMLSchema", variable[1])));
        }
        // compile XPath-Expression from String xPath
        XPathCompiler xpathcompiler = processor.newXPathCompiler();
        for (int i = 0; i < variables.length; i++) {
            xpathcompiler.declareVariable(new QName("", variables[i][0]), types.get(i), OccurrenceIndicator.ONE);
        }
        for (String[] namespace : namespaces) {
            xpathcompiler.declareNamespace(namespace[0], namespace[1]);
        }
        xPathExecutable = xpathcompiler.compile(xPath);
    }
    /**
     * load xPath Executable into selector
     */
    public void loadXPath() {
        // 1. load selector from xPathExecutable
        selector = xPathExecutable.load();
    }
    /**
     * set an int variable
     * @param name the name of the variable
     * @param value the int value for the variable
     * @throws SaxonApiException if the variable can't be set
     */
    public void setIntVariable(String name, int value) throws SaxonApiException {
        // 2. set Variable values in selector
        selector.setVariable(new QName("", name), new XdmAtomicValue(value));
    }
    /**
     * set a double variable
     * @param name the name of the variable
     * @param value the double value for the variable
     * @throws SaxonApiException if the variable can't be set
     */
    public void setDoubleVariable(String name, double value) throws SaxonApiException {
        // 2. set Variable values in selector
        selector.setVariable(new QName("", name), new XdmAtomicValue(value));
    }
    /**
     * set a String variable
     * @param name the name of the variable
     * @param value the String value for the variable
     * @throws SaxonApiException if the variable can't be set
     */
    public void setStringVariable(String name, String value) throws SaxonApiException {
        // 2. set Variable values in selector
        selector.setVariable(new QName("", name), new XdmAtomicValue(value));
    }
    /**
     * set the context for the XPath evaluation to an XdmNode containing the XML document
     * @param xdmNode the XdmNode containing the XML document
     * @return XPathSelector the selector for the XPath evaluation results
     * @throws SaxonApiException if an error occurs
     */
    public XPathSelector setContextItem(XdmNode xdmNode) throws SaxonApiException {
        // 3. set document as contextNode in selector and return selector
        selector.setContextItem(xdmNode);
        return selector;
    }

}
