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

import java.util.ArrayList;


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
                //messageSent.setMargin(50, 50);
                messageSent.show();
            }
        });

        ImageButton acceptButtton = (ImageButton) findViewById(R.id.accept_button);
        acceptButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText inputBox = (TextInputEditText) findViewById(R.id.input_box);
                textInput = inputBox.getText().toString();

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
                    if (ikon_count < MAX_IKON_COUNT) ikon_count++;
                    int loop_counter = 0;
                    for (ImageView ikon : image_view_arr){
                        ikon.setVisibility(View.VISIBLE);
                        loop_counter++;
                        if (loop_counter == ikon_count) break;
                    }
                    inputBox.setText("");
                }

                // extract word from input
                // display next icon
                //
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
        } catch (Exception var2) {
        }

    }
}
