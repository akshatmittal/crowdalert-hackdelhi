package com.roalts.hackdelhiclient;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RechargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        String uuid = getIntent().getStringExtra("uuid");
//        Log.d("uuid" , uuid);
        String text = "1CNeB6jQSJqRebnrvYFhb1rdfhZJXM56Kz";
        ImageView imageView = (ImageView) findViewById(R.id.qrcode_payment);
        imageView.setImageResource(R.drawable.crypt);
        final TextView stringPayment = (TextView) findViewById(R.id.string_payment);
        stringPayment.setText(text);
        Button copyToClipboard = (Button) findViewById(R.id.copy_payment);
        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringYouExtracted = stringPayment.getText().toString();
                stringYouExtracted = stringYouExtracted.substring(0, stringYouExtracted.length()-1);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(stringYouExtracted);
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT)
                        .show();

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.mycelium.testnetwallet");
                try {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    // No app!
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "com.mycelium.testnetwallet"));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recharge, menu);
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
