package com.example.sanket.sudoku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Button capture;
    private Button show;
    private Button solveitbutton;
    private Button resetbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onResume(){
        super.onResume();
        this.capture = (Button)findViewById(R.id.capture);
        this.show = (Button)findViewById(R.id.show);
        this.solveitbutton = (Button)findViewById(R.id.solveit);
        this.resetbutton = (Button)findViewById(R.id.reset);
        resetbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                resetSudokuGrid(view);
            }
        });
        final String prefix_id = "editText";
        for(int row = 1;row < 10;row++){
            for(int column = 1;column < 10;column++){
                final int temp_row = row;
                final int temp_column = column;
                String id = prefix_id + row + "" + column;
                EditText editText = (EditText)findViewById(getResId(id,R.id.class));
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(b){
                            makeEditTextFieldWhite(view);
                            for (int temp = 1;temp < 10;temp++){
                                String temp_id = prefix_id + "" + temp_row + "" + temp;
                                EditText editText = (EditText)findViewById(getResId(temp_id,R.id.class));
                                editText.setBackgroundColor(Color.LTGRAY);
                                temp_id = prefix_id + "" + temp + "" + temp_column;
                                editText = (EditText)findViewById(getResId(temp_id,R.id.class));
                                editText.setBackgroundColor(Color.LTGRAY);
                            }
                        }
                    }
                });
            }
        }
        solveitbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                solveMannuallyFilledSudoku(view);
            }
        });
        capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                takePicture();
            }
        });
    }

    public void makeEditTextFieldWhite(View view){
        String prefix_id = "editText";
        for(int tmp_row = 1;tmp_row < 10;tmp_row++){
            for (int tmp_col = 1;tmp_col < 10;tmp_col++){
                String temp_id = prefix_id + "" + tmp_row + "" + tmp_col;
                EditText editText = (EditText)findViewById(getResId(temp_id,R.id.class));
                editText.setBackgroundColor(Color.WHITE);
            }
        }

    }

    public void resetSudokuGrid(View view){
        String id_prefix = "editText";
        for(int row = 1;row < 10;row++) {
            for (int column = 1; column < 10; column++) {
                String id = id_prefix + row + "" + column;
                EditText editText = (EditText) findViewById(getResId(id, R.id.class));
                editText.setEnabled(true);
                editText.setBackgroundColor(Color.WHITE);
                editText.setText("");
            }
        }
    }

    public void solveMannuallyFilledSudoku(View view){
        int grid[][] = new int[9][9];
        String id_prefix = "editText";
        for(int row = 1;row < 10;row++){
            for (int column = 1;column < 10;column++){
                String id = id_prefix + row + "" + column;
                EditText editText = (EditText)findViewById(getResId(id,R.id.class));
                if(editText.getText().length() == 0){
                    editText.setBackgroundColor(Color.WHITE);
                    continue;
                }
                grid[row-1][column-1] = Integer.parseInt(editText.getText().toString());
                editText.setBackgroundColor(Color.GRAY);
            }
        }
        SudokuSolver sudokuSolver = new SudokuSolver();
        if(sudokuSolver.initialChecking(grid) == -1){
            if(sudokuSolver.solveSudoku(grid)){
                for(int row = 1;row < 10;row++) {
                    for (int column = 1; column < 10; column++) {
                        String id = id_prefix + row + "" + column;
                        EditText editText = (EditText) findViewById(getResId(id, R.id.class));
                        editText.setText(grid[row-1][column-1]+"");
                        editText.setEnabled(false);
                    }
                }
            }
        }
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){

         Uri photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath(),"image_"+String.valueOf(System.currentTimeMillis())+".jpg"));
         takePictureIntent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE,photoUri);
         startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == MainActivity.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image_bitmap = (Bitmap) extras.get("data");
            Intent tmp = new Intent(this,Main2Activity.class);
            tmp.putExtra("image",image_bitmap);
            startActivityForResult(tmp,3);
        }
    }

    private File createImageFile() throws IOException{
        String fileName = "Image "+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName,".jpg",storageDir);
        this.mCurrentPhotoPath = "file: " + image.getAbsolutePath();
        return image;
    }

}
