package com.redcells;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class searchview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);
        Button srch=(Button) findViewById(R.id.button3);
        final Spinner blood=(Spinner) findViewById(R.id.spinner3);
        final Spinner pan=(Spinner) findViewById(R.id.spinner4);
        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btype=blood.getSelectedItem().toString();
                String panc=pan.getSelectedItem().toString();
                Intent re=new Intent(searchview.this, Search.class);
                re.putExtra("bt",btype);
                re.putExtra("pa",panc);
                startActivity(re);

            }
        });

    }
}
