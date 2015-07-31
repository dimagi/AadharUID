package org.commcarehq.aadharuid;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by droberts on 7/24/15.
 * <PrintLetterBarcodeData uid="123456789012" name="First Last" gender="M/F" yob="YYYY"
 *  co="D/O: Father Name" house="house num" street="street name" lm="address" loc="neighborhood"
 *  vtc="village" po="city" dist="district" subdist="region" state="state" pc="postalcode"
 *  dob="dd/mm/yyyy"/>
 */
public class ScanResult {
    public final String rawString;
    public final String uid;
    public final String name;
    public final String gender;  // M/F
    public final String yob;  // year of birth
    public final String co;  // "D/O: Father Name"  (DMR): not sure what this means
    public final String house;
    public final String street;
    public final String lm;  // address
    public final String loc;  // neighborhood
    public final String vtc;  // village
    public final String po;  // city
    public final String dist;  // district
    public final String subdist;  // region
    public final String state;
    public final String pc;  // postal code
    public final String dob;  // date of birth

    private String getAttributeOrEmptyString(NamedNodeMap attributes, String attributeName) {
        Node node = attributes.getNamedItem(attributeName);
        if (node != null) {
            return node.getTextContent();
        } else {
            return "";
        }
    }

    public ScanResult(String input) {
        rawString = input;
        // copied from http://www.java-samples.com/showtutorial.php?tutorialid=152
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document dom;
        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));


        } catch(ParserConfigurationException|SAXException|IOException e) {
            dom = null;
        }
        if (dom != null) {
            Node node = dom.getChildNodes().item(0);
            NamedNodeMap attributes = node.getAttributes();
            uid = getAttributeOrEmptyString(attributes, "uid");
            name = getAttributeOrEmptyString(attributes, "name");
            gender = getAttributeOrEmptyString(attributes, "gender");
            yob = getAttributeOrEmptyString(attributes, "yob");
            co = getAttributeOrEmptyString(attributes, "co");
            house = getAttributeOrEmptyString(attributes, "house");
            street = getAttributeOrEmptyString(attributes, "street");
            lm = getAttributeOrEmptyString(attributes, "lm");
            loc = getAttributeOrEmptyString(attributes, "loc");
            vtc = getAttributeOrEmptyString(attributes, "vtc");
            po = getAttributeOrEmptyString(attributes, "po");
            dist = getAttributeOrEmptyString(attributes, "dist");
            subdist = getAttributeOrEmptyString(attributes, "subdist");
            state = getAttributeOrEmptyString(attributes, "state");
            pc = getAttributeOrEmptyString(attributes, "pc");
            String rawDob = getAttributeOrEmptyString(attributes, "dob");
            try {
                rawDob = formatDate(rawDob);
            } catch (ParseException e) {
                System.err.println("Expected dob to be in dd/mm/yyyy format, got " + rawDob);
            }
            dob = rawDob;
        } else {
            uid = rawString;
            name = "";
            gender = "";
            yob = "";
            co = "";
            house = "";
            street = "";
            lm = "";
            loc = "";
            vtc = "";
            po = "";
            dist = "";
            subdist = "";
            state = "";
            pc = "";
            dob = "";
        }
    }

    private String formatDate(String rawDateString) throws ParseException {
        SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = fromFormat.parse(rawDateString);
        return toFormat.format(date);
    }
}
