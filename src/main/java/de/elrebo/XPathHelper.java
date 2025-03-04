package de.elrebo;

import net.sf.saxon.s9api.*;
import java.util.ArrayList;
import java.util.List;

/**
 * XPathHelper
 *
 * Class XPathHelper simplifies Access to XML documents from Java programs.
 * It prepares any XML document to be inspected by any XPath2.0 instruction.
 * It suppports Namespaces and Variables.
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

public class XPathHelper {
    // compiled XPath Executable
    private final XPathExecutable xPathExecutable;
    // the Selector for the XPath execution
    private XPathSelector selector;
    // Constructor from filePath, namespaces, variableNames and xPath
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
    // load xPath Executable into selector
    public void loadXPath() {
        // 1. load selector from xPathExecutable
        selector = xPathExecutable.load();
    }
    public void setIntVariable(String name, int value) throws SaxonApiException {
        // 2. set Variable values in selector
        selector.setVariable(new QName("", name), new XdmAtomicValue(value));
    }
    public void setDoubleVariable(String name, double value) throws SaxonApiException {
        // 2. set Variable values in selector
        selector.setVariable(new QName("", name), new XdmAtomicValue(value));
    }
    public void setStringVariable(String name, String value) throws SaxonApiException {
        // 2. set Variable values in selector
        selector.setVariable(new QName("", name), new XdmAtomicValue(value));
    }
    public XPathSelector setContextItem(XdmNode xdmNode) throws SaxonApiException {
        // 3. set document as contextNode in selector and return selector
        selector.setContextItem(xdmNode);
        return selector;
    }

}
