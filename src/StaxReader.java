import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxReader {
    static FileWriter output;
    static String delimiter;

    public static void main(String[] args) {
        Reader input;
        try {
            input = new FileReader("ArcheryTournament.xml");
            output = new FileWriter("ArcheryTournament.json");
            XMLInputFactory f = XMLInputFactory.newInstance();
            XMLStreamReader r = f.createXMLStreamReader(input);

            moveToStartElement(r);

            if (! r.getLocalName().equals("tournament")) {
                throw new Exception("ArcheryTournaments should have a <tournament> root element.");
            }

            delimiter = "{\n";
            readTournamentContent(r);
            output.write("\n}\n");

            //moveToEndElem(r, "tournament");

            output.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    /*  helpers */

    static void moveToStartElement(XMLStreamReader r) throws XMLStreamException {
        //skip everything until the start of a new element
        while(r.next() != XMLStreamReader.START_ELEMENT) {}
    }

    static void readTournamentContent(XMLStreamReader r) throws Exception {
        //title
        r.nextTag();

        if (! r.getLocalName().equals("title")) {
            throw new Exception("<title> expected.");
        }

        writeKeyValue("title", r.getElementText(), 1);
        delimiter = ",\n";

        //shooting-styles
        r.nextTag();

        if (! r.getLocalName().equals("shooting-style")) {
            throw new Exception("at least one <shooting-style> expected.");
        }

        writeQuotedString("shooting-styles", 1);
        delimiter = " : [";
        do {
            readShootingStyleContent(r);
            delimiter =",\n";
            r.nextTag();
        } while (r.getLocalName().equals("shooting-style"));
        output.write("\n\t]");

        //locations
        if (! r.getLocalName().equals("location")) {
            throw new Exception("at least one <location> expected.");
        }

        writeQuotedString("locations", 1);
        delimiter = " : [";
        do {
            readLocationContent(r);
            delimiter =",\n";
            r.nextTag();
        } while (r.getLocalName().equals("location"));
        output.write("\n\t]");

        //groups
        if (! r.getLocalName().equals("group")) {
            throw new Exception("at least one <group> expected.");
        }

        writeQuotedString("groups", 1);
        delimiter = " : [";

        do {
            readGroupContent(r);
            delimiter =",\n";
            r.nextTag();
        } while (r.getLocalName().equals("group"));
        output.write("\n\t]");

        //persons
        if (! r.getLocalName().equals("person")) {
            throw new Exception("at least one <person> expected.");
        }

        writeQuotedString("persons", 1);
        delimiter = " : [";

        do {
            readPersonContent(r);
            delimiter =",\n";

            r.nextTag();

        } while (r.getLocalName().equals("person"));
        output.write("\n\t]");
    }

    static void readShootingStyleContent(XMLStreamReader r) throws Exception {
        if (delimiter != null && delimiter.length() > 0) {
            output.write(delimiter);
            delimiter = null;
        }
        output.write("\n\t\t{\n");

        //attributes
        for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
            writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),3);
            delimiter = ",\n";
        }

        r.nextTag();
        //element content
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);

        output.write("\n\t\t}");

        r.nextTag();
    }

    static void readLocationContent(XMLStreamReader r) throws Exception {
        if (delimiter != null && delimiter.length() > 0) {
            output.write(delimiter);
            delimiter = null;
        }
        output.write("\n\t\t{\n");

        //attributes
        for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
            writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),3);
            delimiter = ",\n";
        }

        //element content
        writeKeyValue("name", r.getElementText(), 3);

        output.write("\n\t\t}");
    }

    static void readGroupContent(XMLStreamReader r) throws Exception {
        if (delimiter != null && delimiter.length() > 0) {
            output.write(delimiter);
            delimiter = null;
        }
        output.write("\n\t\t{\n");

        //attributes
        for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
            writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),3);
            delimiter = ",\n";
        }

        //element content
        r.nextTag();
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);
        delimiter = ",\n";
        r.nextTag();
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);
        delimiter = ",\n";

        r.nextTag();
        for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
            writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),3);
            delimiter = ",\n";
        }

        r.nextTag();
        r.nextTag();
        for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
            writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),3);
            delimiter = ",\n";
        }

        r.nextTag();
        r.nextTag();

        writeQuotedString("group-members", 3);
        delimiter = " : [\n";
        do {
            for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
                writeQuotedString(r.getAttributeValue(i),4);
                delimiter = ",\n";
            }
            r.nextTag();
            r.nextTag();
        } while (r.getLocalName().equals("group-member"));
        output.write("\n\t\t\t]");

        output.write("\n\t\t}");
    }

    static void readPersonContent(XMLStreamReader r) throws Exception {
        if (delimiter != null && delimiter.length() > 0) {
            output.write(delimiter);
            delimiter = null;
        }
        output.write("\n\t\t{\n");

        //attributes
        for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
            writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),3);
            delimiter = ",\n";
        }

        //element content
        r.nextTag();
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);
        delimiter = ",\n";
        r.nextTag();
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);
        delimiter = ",\n";
        r.nextTag();
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);
        delimiter = ",\n";
        r.nextTag();
        writeKeyValue(r.getLocalName(), r.getElementText(), 3);
        delimiter = ",\n";

        r.nextTag();
        writeQuotedString("styles", 3);
        delimiter = " : [\n";
        do {
            for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
                writeQuotedString(r.getAttributeValue(i), 4);
                delimiter = ",\n";
            }

            r.nextTag();
            r.nextTag();

        } while (r.getLocalName().equals("style"));
        output.write("\n\t\t\t]");

        writeQuotedString("scores", 3);
        delimiter = " : [\n";
        do {
            if (delimiter != null && delimiter.length() > 0) {
                output.write(delimiter);
                delimiter = null;
            }
            output.write("\t\t\t\t{\n");

            for (int i = r.getAttributeCount() - 1; i >= 0; i--) {
                writeKeyValue(r.getAttributeLocalName(i), r.getAttributeValue(i),5);
                delimiter = ",\n";
            }

            writeKeyValue(r.getLocalName(), r.getElementText(), 5);
            delimiter = "\n\t\t\t\t},\n";

            r.nextTag();

        } while (r.getLocalName().equals("score"));
        output.write("\n\t\t\t\t}");
        output.write("\n\t\t\t]");

        output.write("\n\t\t}");
    }

    static void writeKeyValue(String key, String value, int indent) throws IOException {
        writeQuotedString(key, indent);
        delimiter = " : ";
        writeQuotedString(value.trim(), 0);
    }

    static void writeQuotedString(String s, int indent) throws IOException {
        if (delimiter != null && delimiter.length() > 0) {
            output.write(delimiter);
            delimiter = null;
        }
        for (int i = indent; i > 0; i--) {
            output.write('\t');
        }
        output.write('"');
        output.write(s);
        output.write('"');
    }


    static void moveToEndElem(XMLStreamReader r, String elemName) throws XMLStreamException {
        //skip everything until an end tag of elemName
        do {
            //skip for the next end element
            while(r.next() != XMLStreamReader.END_ELEMENT) {}
        } while (!r.getLocalName().equals(elemName));
    }

}