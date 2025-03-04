package de.elrebo;

import net.sf.saxon.s9api.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * XmlDocumentHelper
 *
 * Class XmlDocumentHelper simplifies Access to XML documents from Java programs.
 * It prepares any XML document as XdmNode.
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
public class XmlDocumentHelper {

    // prepared XML document
    private final XdmNode xdmNode;

    // Constructor from filePath
    public XmlDocumentHelper(Processor processor, String filePath) throws SaxonApiException {
        // create an XdmNode document from file <pathname>
        File file = new File(filePath);
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder docBuilder;
        try {
            docBuilder = dfactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new SaxonApiException(e);
        }
        Document doc;
        try {
            doc = docBuilder.parse(new InputSource(file.toURI().toString()));
        } catch (SAXException | IOException e) {
            throw new SaxonApiException(e);
        }
        DocumentBuilder builder = processor.newDocumentBuilder();
        xdmNode = builder.wrap(doc);
    }

    // return the XML document as an XdmNode
    public XdmNode asXdmNode() {
        return xdmNode;
    }

}
