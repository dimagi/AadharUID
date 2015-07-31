package org.commcarehq.aadharuid;

import junit.framework.TestCase;

/**
 * Created by droberts on 7/24/15.
 */
public class ScanResultTest extends TestCase {
    public void testXML() {
        ScanResult scanResult = new ScanResult(
                "<PrintLetterBarcodeData uid=\"123456789012\" name=\"First Last\" gender=\"M/F\"" +
                        " yob=\"YYYY\" co=\"D/O: Father Name\" house=\"house num\"" +
                        " street=\"street name\" lm=\"address\" loc=\"neighborhood\" vtc=\"village\"" +
                        " po=\"city\" dist=\"district\" subdist=\"region\" state=\"state\"" +
                        " pc=\"postalcode\" dob=\"23/11/1999\"/>"
        );
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

    public void testJustUID() {
        ScanResult scanResult = new ScanResult("123456789012");
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
        // Is this the behavior we want?
        ScanResult scanResult = new ScanResult("http://i.imgur.com/XpgmoU1.jpg");
        assertEquals(scanResult.uid, "http://i.imgur.com/XpgmoU1.jpg");
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