package hu.domparse.ne9jrl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomQueryNe9jrl {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		File xmlFile = new File("XMLNE9JRL.xml"); // Létrehozunk egy File objektumot, az XMl fájlunkal.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 
		DocumentBuilder docBuilder = factory.newDocumentBuilder(); // Dobhat ParserConfigurationException-t.
		Document doc = docBuilder.parse(xmlFile); // Létrehoztunk egy DocumentBuilderFactory-t, majd utána egy DocumentBuilder-t ebből a factoryból. Ezzel beolvassuk az XML fájlunkat, dobhat SAXExceptiont és IOExceptiont.
		doc.getDocumentElement().normalize(); // Fa struktúrává alakítja a beolvasott fájlt. -- ezzel szeretnénk dolgozni.

		String kerdes = "Minden olyan Fizetési Szolgáltató kiírása, ami több, mint 250 ft-ot kér el tranzakciónként..";
		System.out.println(kerdes + "\n");
		Lekerdezes(doc);

	}
	
	private static void Lekerdezes(Document doc) throws XPathExpressionException {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPathExpression xpathExp = xpathFactory.newXPath().compile(
		    "//text()[normalize-space(.) = '']");  
		NodeList emptyTextNodes = (NodeList) 
		    xpathExp.evaluate(doc, XPathConstants.NODESET);
		// XPath kifejezéssel megkeressük az üres node-okat. Csak így tudtam eltűntetni a #text: mezőket.
		for (int k = 0; k < emptyTextNodes.getLength(); k++) {
			  Node emptyTextNode = emptyTextNodes.item(k);
			emptyTextNode.getParentNode().removeChild(emptyTextNode);
			}
		NodeList paymentmethods = doc.getElementsByTagName("paymentmethod"); // az összes payment methodot elmentjük egy nodelistbe.
		for(int i = 0; i < paymentmethods.getLength(); i++) {
			Element paymentmethod =(Element)paymentmethods.item(i); // kiválasztunk egy payment methodot
			NodeList childNodes = paymentmethod.getChildNodes();
			for(int j =0;j<childNodes.getLength();j++) {
				Node childNode = childNodes.item(j); // belépünk a Node-ba
				if(childNode.getNodeName().equals("fee")) { // ha a node amiben vagyunk megegyezik a fee-vel akkor
					if(Integer.parseInt(childNode.getTextContent())>250) { // ha a fee nagyobb, mint 250, akkor kiiratjuk a printElement metódussal.
						printElement(paymentmethod);
					}
				}
			}
		}
	}
	
	private static void printElement(Element elem) {
		String id = elem.getAttribute("id");
		System.out.println("ID: "+ id);
		
		String nodeContent="";
		NodeList childNodes = elem.getChildNodes();
		for(int j =0;j<childNodes.getLength() ; j++) {
			if(childNodes.item(j).getTextContent().trim()!="") {
				nodeContent = childNodes.item(j).getTextContent().trim();
				System.out.println(childNodes.item(j).getNodeName()+": "+nodeContent);
			}
		}
	}
}
