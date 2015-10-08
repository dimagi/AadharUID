package org.commcarehq.aadharuid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    int BARCODE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFinishing()) {
            callBarcodeScanner();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String result = data.getStringExtra("SCAN_RESULT");
                    returnOdkResult(result);
                    break;
                case RESULT_CANCELED:
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callBarcodeScanner() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        try {
            this.startActivityForResult(intent, BARCODE_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                    getResources().getString(R.string.barcode_scanner_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void returnOdkResult(String result) {
        ScanResult scanResult = new ScanResult(result);
        Intent data = new Intent();

        Bundle responses = new Bundle();
        responses.putString("statusCode", Integer.toString(scanResult.statusCode));
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

        data.putExtra("odk_intent_bundle", responses);
        switch (scanResult.statusCode) {
            case ScanResult.STATUS_SUCCESS:
                data.putExtra("odk_intent_data", "✓");
                break;
            default:
                data.putExtra("odk_intent_data", "✗");
                break;
        }

        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
