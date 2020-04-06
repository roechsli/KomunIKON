package com.example.myfirstapp;

import android.content.Context;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;


public class TextToIkon extends AppCompatActivity {
    String textInput = "";
    int ikon_count = 0;
    int MAX_IKON_COUNT = 4;


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
            @Override
            public void onClick(View view) {
                TextInputEditText inputBox = (TextInputEditText) findViewById(R.id.input_box);
                textInput = inputBox.getText().toString().toLowerCase();

                if (ikon_count == MAX_IKON_COUNT){
                    hideKeyboard();
                    setIkonsInvisible();
                    ImageView finalSentence = (ImageView) findViewById(R.id.final_sentence);
                    finalSentence.setVisibility(View.VISIBLE);
                    ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
                    sendButton.setVisibility(View.VISIBLE);
                }
                if (!textInput.equals("")){
                    ImageView ikon1 = (ImageView) findViewById(R.id.ikon_1);
                    ImageView ikon2 = (ImageView) findViewById(R.id.ikon_2);
                    ImageView ikon3 = (ImageView) findViewById(R.id.ikon_3);
                    ImageView ikon4 = (ImageView) findViewById(R.id.ikon_4);
                    ArrayList<ImageView> image_view_arr = new ArrayList();
                    image_view_arr.add(ikon1);
                    image_view_arr.add(ikon2);
                    image_view_arr.add(ikon3);
                    image_view_arr.add(ikon4);
                    String ikon_filename = get_ikon_of(textInput);
                    if (!ikon_filename.equals("")) {
                        if (ikon_count < MAX_IKON_COUNT){
                            int res_id = getResources().getIdentifier(ikon_filename, "drawable", getPackageName());
                            image_view_arr.get(ikon_count).setImageResource(res_id);
                            image_view_arr.get(ikon_count).setVisibility(View.VISIBLE);
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
                if (ikon_count > 0) ikon_count--;
                int loop_counter = 0;
                for (ImageView ikon : image_view_arr){
                    if (loop_counter == ikon_count)
                        ikon.setVisibility(View.INVISIBLE);
                    loop_counter++;
                }
            }
        });

    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputmanager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputmanager != null) {
                inputmanager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
        }

    }
}
