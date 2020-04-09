package com.example.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;


public class TextToIkon extends AppCompatActivity {
    String textInput = "";
    int ikon_count = 0;
    int MAX_IKON_COUNT = 4;
    ArrayList<Integer> sentenceIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_ikon);

        configureButtons();
        setIkonsInvisible();
    }

    public String get_ikon_of(String word){
        String json;
        try {
            InputStream inputStream = getAssets().open("ikons.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONObject(json).getJSONArray("data");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray meanings = (JSONArray) jsonObject.get("meaning");
                //Toast.makeText(getApplicationContext(), meanings.toString(), Toast.LENGTH_SHORT).show();
                for (int n = 0; n < meanings.length(); n++) {
                    String object = meanings.getString(n);
                    if (object.equals(word)) return jsonObject.getString("filename");
                }
            }

        } catch (IOException  e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setIkonsInvisible(){
        ImageView ikon1 = (ImageView) findViewById(R.id.ikon_1);
        ImageView ikon2 = (ImageView) findViewById(R.id.ikon_2);
        ImageView ikon3 = (ImageView) findViewById(R.id.ikon_3);
        ImageView ikon4 = (ImageView) findViewById(R.id.ikon_4);
        ImageView finalSentence = (ImageView) findViewById(R.id.final_sentence);
        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        ikon1.setVisibility(View.INVISIBLE);
        ikon2.setVisibility(View.INVISIBLE);
        ikon3.setVisibility(View.INVISIBLE);
        ikon4.setVisibility(View.INVISIBLE);
        finalSentence.setVisibility(View.INVISIBLE);
        sendButton.setVisibility(View.INVISIBLE);
    }

    private void configureButtons(){
        ImageButton homeButtton = (ImageButton) findViewById(R.id.home_button);
        homeButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast messageSent = Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG);
                messageSent.show();
            }
        });

        ImageButton acceptButtton = (ImageButton) findViewById(R.id.accept_button);
        acceptButtton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                TextInputEditText inputBox = (TextInputEditText) findViewById(R.id.input_box);
                textInput = inputBox.getText().toString().toLowerCase();
                ImageView ikon1 = (ImageView) findViewById(R.id.ikon_1);
                ImageView ikon2 = (ImageView) findViewById(R.id.ikon_2);
                ImageView ikon3 = (ImageView) findViewById(R.id.ikon_3);
                ImageView ikon4 = (ImageView) findViewById(R.id.ikon_4);
                ImageView finalSentence = (ImageView) findViewById(R.id.final_sentence);
                ArrayList<ImageView> image_view_arr = new ArrayList();
                image_view_arr.add(ikon1);
                image_view_arr.add(ikon2);
                image_view_arr.add(ikon3);
                image_view_arr.add(ikon4);

                if (ikon_count == MAX_IKON_COUNT){
                    hideKeyboard();
                    setIkonsInvisible();
                    Bitmap finalImage = BitmapFactory.decodeResource(getResources(), sentenceIds.get(0));
                    for (int i=1; i<ikon_count; i++){
                        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), sentenceIds.get(i));
                        finalImage = joinImages(finalImage, bmp2);
                    }
                    ikon_count = 0;
                    // TODO here

                    finalSentence.setImageBitmap(finalImage);
                    finalSentence.setVisibility(View.VISIBLE);

                    ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
                    sendButton.setVisibility(View.VISIBLE);
                } else if (!textInput.equals("")){
                    String ikon_filename = get_ikon_of(textInput);
                    if (!ikon_filename.equals("")) {
                        if (ikon_count < MAX_IKON_COUNT){
                            int res_id = getResources().getIdentifier(ikon_filename, "drawable", getPackageName());
                            image_view_arr.get(ikon_count).setImageResource(res_id);
                            image_view_arr.get(ikon_count).setVisibility(View.VISIBLE);
                            sentenceIds.add(res_id);
                            ikon_count++;
                        }
                        inputBox.setText("");
                    } else {
                        Toast messageNotFound = Toast.makeText(getApplicationContext(), "Ikon not found!", Toast.LENGTH_SHORT);
                        messageNotFound.show();
                    }
                } else {
                    Toast messageNoInput = Toast.makeText(getApplicationContext(), "Enter input!", Toast.LENGTH_SHORT);
                    messageNoInput.show();
                }
            }
        });

        ImageButton rejectButtton = (ImageButton) findViewById(R.id.reject_button);
        rejectButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView ikon1 = (ImageView) findViewById(R.id.ikon_1);
                ImageView ikon2 = (ImageView) findViewById(R.id.ikon_2);
                ImageView ikon3 = (ImageView) findViewById(R.id.ikon_3);
                ImageView ikon4 = (ImageView) findViewById(R.id.ikon_4);
                ArrayList<ImageView> image_view_arr = new ArrayList();
                image_view_arr.add(ikon1);
                image_view_arr.add(ikon2);
                image_view_arr.add(ikon3);
                image_view_arr.add(ikon4);
                if (ikon_count > 0) {
                    ikon_count--;
                    sentenceIds.remove(ikon_count);
                }
                int loop_counter = 0;
                for (ImageView ikon : image_view_arr){
                    if (loop_counter == ikon_count)
                        ikon.setVisibility(View.INVISIBLE);
                    loop_counter++;
                }
            }
        });

    }

    private Bitmap joinImages(Bitmap bmp1, Bitmap bmp2)
    {
        if (bmp1 == null || bmp2 == null)
            return bmp1;
        int height = bmp1.getHeight();
        if (height < bmp2.getHeight())
            height = bmp2.getHeight();

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth() + bmp2.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, bmp1.getWidth(), 0, null);
        return bmOverlay;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideKeyboard() {
        try {
            InputMethodManager inputmanager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputmanager != null) {
                inputmanager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
        }

    }
}
