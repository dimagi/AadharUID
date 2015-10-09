package org.commcarehq.aadharuid;

import android.support.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_PARSE_ERROR = 1;
    public final int statusCode;
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
    public final String dobGuess;  // date of birth or June 1 of year of birth
    public final String statusText;  // either `"✓"` (success) or `"✗"` (any failure)

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
            // Replace </?xml... with <?xml...
            if (input.startsWith("</?")) {
                input = input.replaceFirst("</\\?", "<?");
            }
            // Replace <?xml...?"> with <?xml..."?>
            input = input.replaceFirst("^<\\?xml ([^>]+)\\?\">", "<?xml $1\"?>");
            //parse using builder to get DOM representation of the XML file
            dom = db.parse(new ByteArrayInputStream(input.getBytes("UTF-8")));


        } catch (ParserConfigurationException | SAXException | IOException e) {
            dom = null;
        }
        if (dom != null) {
            Node node = dom.getChildNodes().item(0);
            NamedNodeMap attributes = node.getAttributes();
            statusCode = STATUS_SUCCESS;
            uid = getAttributeOrEmptyString(attributes, "uid");
            name = getAttributeOrEmptyString(attributes, "name");
            String rawGender = getAttributeOrEmptyString(attributes, "gender");
            try {
                rawGender = formatGender(rawGender);
            } catch (ParseException e) {
                System.err.println("Expected gender to be one of m, f, male, female; got " + rawGender);
            }
            gender = rawGender;
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
                System.err.println("Expected dob to be in dd/mm/yyyy or yyyy-mm-dd format, got " + rawDob);
            }
            dob = rawDob;
        } else if (rawString.matches("\\d{12}")) {
            statusCode = STATUS_SUCCESS;
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
        } else {
            statusCode = STATUS_PARSE_ERROR;
            uid = "";
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
        dobGuess = getDobGuess(dob, yob);
        statusText = getStatusText(statusCode);
    }

    protected String formatDate(String rawDateString) throws ParseException {
        if (rawDateString.equals("")) {
            return "";
        }
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat[] possibleFormats = {
                new SimpleDateFormat("dd/MM/yyyy"),
                new SimpleDateFormat("yyyy-MM-dd")};
        Date date = null;
        ParseException parseException = null;
        for (SimpleDateFormat fromFormat : possibleFormats) {
            try {
                date = fromFormat.parse(rawDateString);
                break;
            } catch (ParseException e) {
                parseException = e;
            }
        }
        if (date != null) {
            return toFormat.format(date);
        } else if (parseException != null){
            throw parseException;
        } else {
            throw new AssertionError("This code is unreachable");
        }
    }

    @NonNull
    protected String formatGender(String gender) throws ParseException {
        String lowercaseGender = gender.toLowerCase();
        if (lowercaseGender.equals("male") || lowercaseGender.equals("m")) {
            return "M";
        } else if (lowercaseGender.equals("female") || lowercaseGender.equals("f")) {
            return "F";
        } else if (lowercaseGender.equals("other") || lowercaseGender.equals("o")) {
            return "O";
        } else {
            throw new ParseException("404 gender not found", 0);
        }
    }

    @NonNull
    private String getStatusText(int statusCode) {
        switch (statusCode) {
            case ScanResult.STATUS_SUCCESS:
                return "✓";
            default:
                return "✗";
        }
    }

    @NonNull
    private String getDobGuess(String dob, String yob) {
        if (dob.equals("")) {
            Integer yearInt;
            try {
                yearInt = Integer.parseInt(yob);
            } catch (NumberFormatException e) {
                return "";
            }
            // June 1 of the year
            return Integer.toString(yearInt) + "-06-01";
        } else {
            return dob;
        }
    }
}
