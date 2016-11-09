package org.jboss.as.quickstarts.service;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.ejb.Local;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by F4977165 on 6/22/2016.
 */
@Local
public class MessageProcessorService {

    String xmlDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Company><Name>My Company</Name><Executive type=\"CEO\"><LastName>Smith</LastName><FirstName>Jim</FirstName><street>123 Main Street</street><city>Mytown</city><state>NY</state><zip>11234</zip></Executive></Company>";

    public String getRootNode(){
        String root = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlDocument);
            doc.getDocumentElement().normalize();

            // Get the document's root XML node
            root = doc.getDocumentElement().getNodeName();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }

        return root;
    }
}
