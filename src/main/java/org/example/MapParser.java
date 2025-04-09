package org.example;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MapParser {
    List<Node> nodes = new ArrayList<>();
    List<Arc> arcs = new ArrayList<>();
    public MapParser() {
        String filePath = "src/main/resources/hartaLuxembourg.xml";

        long startTime = System.currentTimeMillis();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fis);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String elementName = reader.getLocalName();
                    if ("node".equals(elementName)) {
                        int id = Integer.parseInt(reader.getAttributeValue(null, "id"));
                        double longitude = Double.parseDouble(reader.getAttributeValue(null, "longitude"));
                        double latitude = Double.parseDouble(reader.getAttributeValue(null, "latitude"));
                        nodes.add(new Node(id, longitude, latitude));
                    } else if ("arc".equals(elementName)) {
                        int from = Integer.parseInt(reader.getAttributeValue(null, "from"));
                        int to = Integer.parseInt(reader.getAttributeValue(null, "to"));
                        int length = Integer.parseInt(reader.getAttributeValue(null, "length"));
                        arcs.add(new Arc(from, to, length));
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Parsed " + nodes.size() + " nodes and " + arcs.size() + " arcs in " + (endTime - startTime) + "ms.");

    }
}