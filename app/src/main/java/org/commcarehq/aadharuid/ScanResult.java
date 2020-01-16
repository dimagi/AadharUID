package org.commcarehq.aadharuid;

import android.support.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by droberts on 7/24/15.
 * <PrintLetterBarcodeData uid="123456789012" name="First Last" gender="M/F" yob="YYYY"
 * co="D/O: Father Name" house="house num" street="street name" lm="address" loc="neighborhood"
 * vtc="village" po="city" dist="district" subdist="region" state="state" pc="postalcode"
 * dob="dd/mm/yyyy"/>
 */
public class ScanResult {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_PARSE_ERROR = 1;
    private static final int NUMBER_OF_PARAMS_IN_SECURE_QR_CODE = 15;

    public static final String QR_CODE_TYPE_SECURE = "secure";
    public static final String QR_CODE_TYPE_XML = "xml";
    public static final String QR_CODE_TYPE_UID_NUMBER = "uid_number";
    public static final String QR_CODE_TYPE_UNKNOWN = "unknown";

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
    public final String type;  // either `"✓"` (success) or `"✗"` (any failure)

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
            type = QR_CODE_TYPE_XML;
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
            dob= formatDate(getAttributeOrEmptyString(attributes, "dob"),
                    new String[] {"dd/MM/yyyy", "yyyy-MM-dd"});

        } else if (rawString.matches("\\d{12}")) {
            type = QR_CODE_TYPE_UID_NUMBER;
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
        } else if (rawString.matches("[0-9]*")) {
            type = QR_CODE_TYPE_SECURE;
            byte[] msgInBytes = null;
            try {
                msgInBytes = decompressByteArray(new BigInteger(rawString).toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (msgInBytes != null) {
                int[] delimiters = locateDelimiters(msgInBytes);
                String referenceId = getValueInRange(msgInBytes, delimiters[0] + 1, delimiters[1]);
                uid = referenceId.substring(0, 4);
                name = getValueInRange(msgInBytes, delimiters[1] + 1, delimiters[2]);
                dob = formatDate(getValueInRange(msgInBytes, delimiters[2] + 1, delimiters[3]),
                        new String[] {"dd-MM-yyyy", "dd/MM/yyyy"});
                yob = dob.substring(0, 4);
                gender = getValueInRange(msgInBytes, delimiters[3] + 1, delimiters[4]);
                co = getValueInRange(msgInBytes, delimiters[4] + 1, delimiters[5]);
                dist = getValueInRange(msgInBytes, delimiters[5] + 1, delimiters[6]);
                lm = getValueInRange(msgInBytes, delimiters[6] + 1, delimiters[7]);
                house = getValueInRange(msgInBytes, delimiters[7] + 1, delimiters[8]);
                loc = getValueInRange(msgInBytes, delimiters[8] + 1, delimiters[9]);
                pc = getValueInRange(msgInBytes, delimiters[9] + 1, delimiters[10]);
                po = getValueInRange(msgInBytes, delimiters[10] + 1, delimiters[11]);
                state = getValueInRange(msgInBytes, delimiters[11] + 1, delimiters[12]);
                street = getValueInRange(msgInBytes, delimiters[12] + 1, delimiters[13]);
                subdist = getValueInRange(msgInBytes, delimiters[13] + 1, delimiters[14]);
                vtc = getValueInRange(msgInBytes, delimiters[14] + 1, delimiters[15]);
                statusCode = STATUS_SUCCESS;
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
        } else {
            type = QR_CODE_TYPE_UNKNOWN;
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

    private static int[] locateDelimiters(byte[] msgInBytes) {
        int[] delimiters = new int[NUMBER_OF_PARAMS_IN_SECURE_QR_CODE + 1];
        int index = 0;
        int delimiterIndex;
        for (int i = 0; i <= NUMBER_OF_PARAMS_IN_SECURE_QR_CODE; i++) {
            delimiterIndex = getNextDelimiterIndex(msgInBytes, index);
            delimiters[i] = delimiterIndex;
            index = delimiterIndex + 1;
        }
        return delimiters;
    }


    private static String getValueInRange(byte[] msgInBytes, int start, int end) {
        return new String(Arrays.copyOfRange(msgInBytes, start, end), Charset.forName("ISO-8859-1"));
    }

    private static int getNextDelimiterIndex(byte[] msgInBytes, int index) {
        int i = index;
        for (; i < msgInBytes.length; i++) {
            if (msgInBytes[i] == -1) {
                break;
            }
        }
        return i;
    }

    private static byte[] decompressByteArray(byte[] bytes) throws IOException {
        java.io.ByteArrayInputStream bytein = new ByteArrayInputStream(bytes);
        java.util.zip.GZIPInputStream gzin = new GZIPInputStream(bytein);
        java.io.ByteArrayOutputStream byteout = new ByteArrayOutputStream();

        int res = 0;
        byte buf[] = new byte[1024];
        while (res >= 0) {
            res = gzin.read(buf, 0, buf.length);
            if (res > 0) {
                byteout.write(buf, 0, res);
            }
        }
        return byteout.toByteArray();
    }

    private String formatDate(String rawDateString, String[] possibleFormats) {
        if (rawDateString.equals("")) {
            return "";
        }
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        ParseException parseException = null;
        for (String fromFormatPattern : possibleFormats) {
            try {
                SimpleDateFormat fromFormat = new SimpleDateFormat(fromFormatPattern);
                date = fromFormat.parse(rawDateString);
                break;
            } catch (ParseException e) {
                parseException = e;
            }
        }
        if (date != null) {
            return toFormat.format(date);
        } else if (parseException != null) {
            System.err.println("Expected dob to be in dd/mm/yyyy or yyyy-mm-dd format, got " + rawDateString);
            return rawDateString;
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
