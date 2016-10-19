package com.example.sanket.sudoku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        Intent intent = getIntent();
        imageView.setImageBitmap((Bitmap)intent.getParcelableExtra("image"));

    }
}
