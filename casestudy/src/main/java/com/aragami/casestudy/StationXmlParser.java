package com.aragami.casestudy;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class StationXmlParser {
    public static String[] getSection(String _xmlFilePath, int _trainNumber, int _waggonNumber)
            throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String getSectionString =
                "station/tracks/track/trains/train[trainNumbers/trainNumber=\""
                        + _trainNumber
                        + "\"]/waggons/waggon[number=\""
                        + _waggonNumber
                        + "\"]/sections/identifier";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(_xmlFilePath);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression expr = xpath.compile(getSectionString);
        NodeList result = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        HashSet<String> retVal = new HashSet<String>();
        for (int i = 0; i < result.getLength(); i++) {
            retVal.add(result.item(i).getTextContent());
        }
        return retVal.toArray(new String[0]);
    }
}
