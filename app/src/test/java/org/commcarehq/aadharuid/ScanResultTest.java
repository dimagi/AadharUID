package org.commcarehq.aadharuid;

import junit.framework.TestCase;

/**
 * Created by droberts on 7/24/15.
 */
public class ScanResultTest extends TestCase {
    public void testXML() {
        String rawString = "<PrintLetterBarcodeData uid=\"123456789012\" name=\"First Last\" gender=\"M/F\"" +
                " yob=\"YYYY\" co=\"D/O: Father Name\" house=\"house num\"" +
                " street=\"street name\" lm=\"address\" loc=\"neighborhood\" vtc=\"village\"" +
                " po=\"city\" dist=\"district\" subdist=\"region\" state=\"state\"" +
                " pc=\"postalcode\" dob=\"23/11/1999\"/>";
        ScanResult scanResult = new ScanResult(rawString);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.rawString, rawString);
        assertEquals(scanResult.uid, "123456789012");
        assertEquals(scanResult.name, "First Last");
        assertEquals(scanResult.gender, "M/F");
        assertEquals(scanResult.yob, "YYYY");
        assertEquals(scanResult.co, "D/O: Father Name");
        assertEquals(scanResult.house, "house num");
        assertEquals(scanResult.street, "street name");
        assertEquals(scanResult.lm, "address");
        assertEquals(scanResult.loc, "neighborhood");
        assertEquals(scanResult.vtc, "village");
        assertEquals(scanResult.po, "city");
        assertEquals(scanResult.dist, "district");
        assertEquals(scanResult.subdist, "region");
        assertEquals(scanResult.state, "state");
        assertEquals(scanResult.pc, "postalcode");
        assertEquals(scanResult.dob, "1999-11-23");
    }

    public void testSheelXML() {
        String rawString = "</?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<PrintLetterBarcodeData uid=\"987098654123\" name=\"Mockit\" " +
                "gender=\"MALE\" yob=\"1989\" co=\"S/O Jhailendno Kaear Kuioy\" " +
                "lm=\"null\" loc=\"Vaishali Fiuhy\" vtc=\"Jaipur\" po=\"Vaishali Hugar\" " +
                "dist=\"Jaipur\" state=\"Rajasthan\" pc=\"345234\" dob=\"1989-06-07\"/>";
        ScanResult scanResult = new ScanResult(rawString);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.rawString, rawString);
        assertEquals(scanResult.uid, "987098654123");
        assertEquals(scanResult.name, "Mockit");
        assertEquals(scanResult.gender, "M");
        assertEquals(scanResult.yob, "1989");
        assertEquals(scanResult.co, "S/O Jhailendno Kaear Kuioy");
        assertEquals(scanResult.house, "");
        assertEquals(scanResult.street, "");
        assertEquals(scanResult.lm, "null");
        assertEquals(scanResult.loc, "Vaishali Fiuhy");
        assertEquals(scanResult.vtc, "Jaipur");
        assertEquals(scanResult.po, "Vaishali Hugar");
        assertEquals(scanResult.dist, "Jaipur");
        assertEquals(scanResult.subdist, "");
        assertEquals(scanResult.state, "Rajasthan");
        assertEquals(scanResult.pc, "345234");
        assertEquals(scanResult.dob, "1989-06-07");
    }

    public void testJustUID() {
        ScanResult scanResult = new ScanResult("123456789012");
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.rawString, "123456789012");
        assertEquals(scanResult.uid, "123456789012");
        assertEquals(scanResult.name, "");
        assertEquals(scanResult.gender, "");
        assertEquals(scanResult.yob, "");
        assertEquals(scanResult.co, "");
        assertEquals(scanResult.house, "");
        assertEquals(scanResult.street, "");
        assertEquals(scanResult.lm, "");
        assertEquals(scanResult.loc, "");
        assertEquals(scanResult.vtc, "");
        assertEquals(scanResult.po, "");
        assertEquals(scanResult.dist, "");
        assertEquals(scanResult.subdist, "");
        assertEquals(scanResult.state, "");
        assertEquals(scanResult.pc, "");
        assertEquals(scanResult.dob, "");
    }

    public void testBizarreInput() {
        ScanResult scanResult = new ScanResult("http://i.imgur.com/XpgmoU1.jpg");
        assertEquals(scanResult.statusCode, 1);
        assertEquals(scanResult.rawString, "http://i.imgur.com/XpgmoU1.jpg");
        assertEquals(scanResult.uid, "");
        assertEquals(scanResult.name, "");
        assertEquals(scanResult.gender, "");
        assertEquals(scanResult.yob, "");
        assertEquals(scanResult.co, "");
        assertEquals(scanResult.house, "");
        assertEquals(scanResult.street, "");
        assertEquals(scanResult.lm, "");
        assertEquals(scanResult.loc, "");
        assertEquals(scanResult.vtc, "");
        assertEquals(scanResult.po, "");
        assertEquals(scanResult.dist, "");
        assertEquals(scanResult.subdist, "");
        assertEquals(scanResult.state, "");
        assertEquals(scanResult.pc, "");
        assertEquals(scanResult.dob, "");
    }
}