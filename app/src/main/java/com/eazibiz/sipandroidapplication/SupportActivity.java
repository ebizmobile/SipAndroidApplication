package com.eazibiz.sipandroidapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eazibiz.sipandroidapplication.R;

/**
 * Created by Rajesh Kumar on 28-Dec-16.
 */

public class SupportActivity extends Activity {
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.sample_layout);


        Button b = (Button)findViewById(R.id.submitbtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"testing",Toast.LENGTH_LONG).show();
            }
        });

    }
}
