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
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_XML);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.statusText, "✓");
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
        assertEquals(scanResult.dobGuess, "1999-11-23");
    }

    public void testSheelXML() {
        String rawString = "</?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<PrintLetterBarcodeData uid=\"987098654123\" name=\"Mockit\" " +
                "gender=\"MALE\" yob=\"1989\" co=\"S/O Jhailendno Kaear Kuioy\" " +
                "lm=\"null\" loc=\"Vaishali Fiuhy\" vtc=\"Jaipur\" po=\"Vaishali Hugar\" " +
                "dist=\"Jaipur\" state=\"Rajasthan\" pc=\"345234\" dob=\"1989-06-07\"/>";
        ScanResult scanResult = new ScanResult(rawString);
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_XML);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.statusText, "✓");
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
        assertEquals(scanResult.dobGuess, "1989-06-07");
    }

    public void testJustUID() {
        ScanResult scanResult = new ScanResult("123456789012");
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_UID_NUMBER);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.statusText, "✓");
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
        assertEquals(scanResult.dobGuess, "");
    }

    public void testBizarreInput() {
        ScanResult scanResult = new ScanResult("http://i.imgur.com/XpgmoU1.jpg");
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_UNKNOWN);
        assertEquals(scanResult.statusCode, 1);
        assertEquals(scanResult.statusText, "✗");
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
        assertEquals(scanResult.dobGuess, "");
    }

    public void testNoDob() {
        String rawString = "</?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                "<PrintLetterBarcodeData uid=\"987098654123\" name=\"Mockit\" " +
                "gender=\"MALE\" yob=\"1989\" co=\"S/O Jhailendno Kaear Kuioy\" " +
                "lm=\"null\" loc=\"Vaishali Fiuhy\" vtc=\"Jaipur\" po=\"Vaishali Hugar\" " +
                "dist=\"Jaipur\" state=\"Rajasthan\" pc=\"345234\"/>";
        ScanResult scanResult = new ScanResult(rawString);
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_XML);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.statusText, "✓");
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
        assertEquals(scanResult.dob, "");
        assertEquals(scanResult.dobGuess, "1989-06-01");
    }
    public void testMisplacedQuestionMark() {
        // same as testXML, but with a bad xml declaration: ...?"> intead of ..."?>
        String rawString = "<?xml version=\"1.0\" encoding=\"UTF-8?\"> " +
                "<PrintLetterBarcodeData uid=\"987098654123\" name=\"Mockit\" " +
                "gender=\"MALE\" yob=\"1989\" co=\"S/O Jhailendno Kaear Kuioy\" " +
                "lm=\"null\" loc=\"Vaishali Fiuhy\" vtc=\"Jaipur\" po=\"Vaishali Hugar\" " +
                "dist=\"Jaipur\" state=\"Rajasthan\" pc=\"345234\" dob=\"1989-06-07\"/>";
        ScanResult scanResult = new ScanResult(rawString);
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_XML);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.statusText, "✓");
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
        assertEquals(scanResult.dobGuess, "1989-06-07");
    }

    public void testSecureQRCode() {
        // This is test data published from UIDAI
        String rawString = "6979414848205548481619299442879901900893978332594614407044767717485407280104077714658698163325401659212830920734233047578454701810567032015270223682917915825234703754712504887921309181789607809168884583848396456653007022479356336240198130363930881632367124738541517499494458139647378808680614169273221404741476596583953169248831376224396335169577064812987140578144885819479190173537644970232125142253963784979138011318798385442436099901621998283624816070080504830712594525760596934341576755626791590403636878139861665599383319429228364434183913197958738697001410493839281298692342829951566712530309758759364649701153639921979798429707566199261950037418171329283207372048014948669160666776198414040633384677104717697507521717586776709084200364956178863636105988867260929887577092955570407803783021397897341999914616790441029837229129746669225095633201097644321593502503404440714110515167034889128258965583435965030225845348564582051521348800742574442877087774194668983516629631073341202705453382780613775427336949283388084891654484225446940941660942440637784744293259916479841407088189462964489670231866481904237338494872813098890875845640034370370387108798950180220865436012752487216677041817312930119747601017807577565413977545693375480131324240696099879479436722576566447939593195590684591261809038023122178172006150499569185218838749337238281597037288924464009997530938336798176023597292328320965086990184531426188862965408313308973495924965144113396593829090645266653313774582036138982013368561474719154447134894466611560589758251829063226370300282175823479569847261439348404558251402273730865053482214589180028302043821438357583302818374143973997002745047526405755760407045006694423501337081780299815080324840337828812644300041900356816429114261098230198976752026002079876882796597235615015594486182057781476152918170746403157005216896239428521706033466061587608065036133153074432195952131368564234168005447770190345777024917629879639171161719929852078265309160759260989590618158889891835294735614366674503961584445497685736312628248483551986529867423016255476553691922054241686230968975229511700928171281549902682365302333677412951788839806869796040512235899311734337858684531156721416280114473368826463098485252394260075790386415875290922570568686439586036262465414002334117870088922801660529414759784318799843806130096998190881240404138869293309782335305296720666220243304175086358278211355789957998014801209332293458940463859106591986434520433810583569309224929264228263841477378949329312443958215939294432669464260216534074560882723006838459792812340253078330291135526952675203790833430237852831740601433198364243363569730205351077393441691141240055900819091229931605146865520183001810239708464322588389956036291760175558843819105418234580239610174323636606095262722940143706063698846499673285377621180570537788160304936809915237889489342387891057012783726694920184573202789672963922380028271124448024265644396686341508447830351380242127542393849410283830409594988503246799544444687606954881510597515686410993828907588979699141180160893062603338104857903239845856783130275935413569275439908789983311663211937449259444259898972766208";
        ScanResult scanResult = new ScanResult(rawString);
        assertEquals(scanResult.type, ScanResult.QR_CODE_TYPE_SECURE);
        assertEquals(scanResult.statusCode, 0);
        assertEquals(scanResult.statusText, "✓");
        assertEquals(scanResult.rawString, rawString);
        assertEquals(scanResult.uid, "8908");
        assertEquals(scanResult.name, "Penumarthi Venkat");
        assertEquals(scanResult.gender, "M");
        assertEquals(scanResult.yob, "1987");
        assertEquals(scanResult.co, "S/O: Pattabhi Rama Rao");
        assertEquals(scanResult.house, "4-83");
        assertEquals(scanResult.street, "Main Road");
        assertEquals(scanResult.lm, "Near Siva Temple");
        assertEquals(scanResult.loc, "Sctor-2");
        assertEquals(scanResult.vtc, "Aratlakatta");
        assertEquals(scanResult.po, "Aratlakatta");
        assertEquals(scanResult.dist, "East Godavari");
        assertEquals(scanResult.subdist, "Karapa");
        assertEquals(scanResult.state, "Andhra Pradesh");
        assertEquals(scanResult.pc, "533016");
        assertEquals(scanResult.dob, "1987-05-07");
        assertEquals(scanResult.dobGuess, "1987-05-07");
    }
}