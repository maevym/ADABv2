package com.example.adabv2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn;

    TextView ad;
    Editable a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("test", "test");

    }
}