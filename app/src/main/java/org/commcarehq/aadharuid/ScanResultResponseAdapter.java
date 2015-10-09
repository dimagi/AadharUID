package org.commcarehq.aadharuid;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by droberts on 10/8/15.
 */
public class ScanResultResponseAdapter {
    private ScanResult scanResult;

    private String odkIntentDataField;
    private Intent responseIntent;
    private boolean hasOdkIntentDataFieldError = false;

    public String getOdkIntentDataField() {
        return odkIntentDataField;
    }

    public boolean getHasOdkIntentDataFieldError() {
        return hasOdkIntentDataFieldError;
    }

    public Intent getResponseIntent() {
        return responseIntent;
    }

    public ScanResultResponseAdapter(ScanResult scanResult, Intent requestIntent) {
        this.scanResult = scanResult;
        parseRequestIntent(requestIntent);
        populateResponseIntent();
    }

    private void parseRequestIntent(Intent inputIntent) {
        odkIntentDataField = inputIntent.getStringExtra("odk_intent_data_field");
        if (odkIntentDataField == null) {
            odkIntentDataField = "statusText";
        }
    }

    private void populateResponseIntent() {
        responseIntent = new Intent();

        Bundle responses = new Bundle();
        responses.putString("statusCode", Integer.toString(scanResult.statusCode));
        responses.putString("statusText", scanResult.statusText);
        responses.putString("rawString", scanResult.rawString);

        responses.putString("uid", scanResult.uid);
        responses.putString("name", scanResult.name);
        responses.putString("gender", scanResult.gender);
        responses.putString("yob", scanResult.yob);
        responses.putString("co", scanResult.co);
        responses.putString("house", scanResult.house);
        responses.putString("street", scanResult.street);
        responses.putString("lm", scanResult.lm);
        responses.putString("loc", scanResult.loc);
        responses.putString("vtc", scanResult.vtc);
        responses.putString("po", scanResult.po);
        responses.putString("dist", scanResult.dist);
        responses.putString("subdist", scanResult.subdist);
        responses.putString("state", scanResult.state);
        responses.putString("pc", scanResult.pc);
        responses.putString("dob", scanResult.dob);
        responses.putString("dobGuess", scanResult.dobGuess);

        String odkIntentData = responses.getString(odkIntentDataField);
        responseIntent.putExtra("odk_intent_bundle", responses);
        responseIntent.putExtra("odk_intent_data", odkIntentData);
        if (odkIntentData == null) {
            hasOdkIntentDataFieldError = true;
        }
    }
}
