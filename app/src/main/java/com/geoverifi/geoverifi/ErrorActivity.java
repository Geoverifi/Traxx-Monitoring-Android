package com.geoverifi.geoverifi;

/**
 * Created by carolyne on 12/18/17.
 */


        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        getSupportActionBar().setTitle("Sync Error");

        TextView txtError = findViewById(R.id.txtError);
        txtError.setText(getIntent().getStringExtra("error"));
    }
}
