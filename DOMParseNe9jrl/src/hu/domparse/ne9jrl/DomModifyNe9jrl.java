package hu.domparse.ne9jrl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomModifyNe9jrl {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		File xmlFile = new File("XMLNE9JRL.xml"); // Létrehozunk egy File objektumot, az XMl fájlunkal.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 
		DocumentBuilder docBuilder = factory.newDocumentBuilder(); // Dobhat ParserConfigurationException-t.
		Document doc = docBuilder.parse(xmlFile); // Létrehoztunk egy DocumentBuilderFactory-t, majd utána egy DocumentBuilder-t ebből a factoryból. Ezzel beolvassuk az XML fájlunkat, dobhat SAXExceptiont és IOExceptiont.
		doc.getDocumentElement().normalize(); // Fa struktúrává alakítja a beolvasott fájlt. -- ezzel szeretnénk dolgozni.
		
		//U_0 ID-vel rendelkező felhasználó email címének a módosítása.
		NodeList nodes =doc.getElementsByTagName("user");
		for(int i =0;i<nodes.getLength();i++) {
			Node node = nodes.item(i);
			if(node.getNodeType()== Node.ELEMENT_NODE) {
				if(node.getAttributes().getNamedItem("id").getTextContent().equals("U_0")) {
					NodeList childNodes = node.getChildNodes();
					for(int j=0 ; j < childNodes.getLength();j++) {
						Node childNode = childNodes.item(j);
						if(childNode.getNodeName().equals("Email")) {
							childNode.setTextContent("ivancsik.krisztian@megmindignemletezoemail.com"); // Az előző feladatok alapján végigmegyünk a mezőinket, megkeressük az U_0 id-jű felhasználó Email tag-ját, és módosítjuk a tartalmát.
						}
					}
				}
			}
		}
		
		File newFile = new File("XMLNE9JRL_modositott.xml");
		writeXml(doc,newFile);
	}
	
	private static void writeXml(Document doc, File output) throws TransformerException,UnsupportedEncodingException{
		TransformerFactory transformerFactory= TransformerFactory.newInstance();
		Transformer transf =transformerFactory.newTransformer();
		transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transf.setOutputProperty(OutputKeys.INDENT, "yes");
		transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		
		StreamResult console = new StreamResult(System.out);
		StreamResult file = new StreamResult(output);
		
		transf.transform(source, console); // kiiratás konzolra
		transf.transform(source, file); // kiiratás fájlba.
	}
}
