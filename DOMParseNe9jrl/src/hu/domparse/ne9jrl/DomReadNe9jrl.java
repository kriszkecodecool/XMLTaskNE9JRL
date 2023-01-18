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

public class DomReadNe9jrl {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		File xmlFile = new File("XMLNE9JRL.xml"); // Létrehozunk egy File objektumot, az XMl fájlunkal.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 
		DocumentBuilder docBuilder = factory.newDocumentBuilder(); // Dobhat ParserConfigurationException-t.
		Document doc = docBuilder.parse(xmlFile); // Létrehoztunk egy DocumentBuilderFactory-t, majd utána egy DocumentBuilder-t ebből a factoryból. Ezzel beolvassuk az XML fájlunkat, dobhat SAXExceptiont és IOExceptiont.
		doc.getDocumentElement().normalize(); // Fa struktúrává alakítja a beolvasott fájlt. -- ezzel szeretnénk dolgozni.
		
		System.out.println("Root element: "+doc.getDocumentElement().getNodeName());
		
		String[] childNodes= {"category" ,"paymentmethod", "user", "shop", "order", "review", "item"}; // a root element gyermek-ei
		
		for (String child : childNodes) {
			NodeList nodes = doc.getElementsByTagName(child); // végig iterálunk a childNodes tömbön, és elmentjük az XML-ünk nodejait egy NodeList objektumba.
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i); // végigiterálunk a nodelistán, és lekérjük az i-edik elemet.
				System.out.println("\nCurrent Element: " + node.getNodeName()); // kiírjuk, hogy melyik Element-nél járunk, és mi a neve.
				
				if (node.getNodeType() == Node.ELEMENT_NODE) { // Bemegyünk az element-be.
					Element elem = (Element) node;
					
					String id = elem.getAttribute("id");
					System.out.println("	ID: " + id); // id kiiratása.
					
					String nodeContent="";
					NodeList moreChildNodes = elem.getChildNodes();
					
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
					for (int j = 0; j < moreChildNodes.getLength(); j++) {
						if (moreChildNodes.item(j).getTextContent().trim() != "") {
							nodeContent =moreChildNodes.item(j).getTextContent().trim();
							System.out.println("	"+moreChildNodes.item(j).getNodeName()+": "+nodeContent); // Kiiratás
						}
						
					}
				}
			}
		}
	}

}
