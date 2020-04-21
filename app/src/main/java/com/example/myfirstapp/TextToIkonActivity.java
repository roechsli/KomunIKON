package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextToIkonActivity extends AppCompatActivity {
    private List<Integer> images = new ArrayList<Integer>();
    private List<String> values = new ArrayList<String>();
    String textInput = "";
    int MAX_IKON_COUNT = 6;
    private AutoCompleteTextView autoTextView;
    private IkonAdapter ikonAdapter;
    private ImageView finalSentence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_ikon_grid_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        autoTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        initiateAutoCompleteTextView();

        autoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (images.size() < MAX_IKON_COUNT) {
                    Ikon ikon = (Ikon) adapterView.getItemAtPosition(i);
                    int res_id = ikon.getImage();
                    images.add(res_id);
                    values.add(ikon.getFilename());
                    updateGridView();
                    autoTextView.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Maximum of Icons reached.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        finalSentence = findViewById(R.id.final_sentence);

        updateGridView();
        configureButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_button:
                startActivity(new Intent(TextToIkonActivity.this, MainActivity.class));
                return true;
            case R.id.settings:
                return true;
            case R.id.about_us:
                return true;
            case R.id.sample_sentences:
                startActivity(new Intent(TextToIkonActivity.this, SampleSentencesActivity.class));
                return true;
        }
        return false;
    }

    private void updateGridView(){
        GridView gridView = (GridView) findViewById(R.id.ikon_grid_view);
        GridAdapter gridAdapter = new GridAdapter(this, values, images);
        gridView.setAdapter(gridAdapter);
    }

    public void initiateAutoCompleteTextView(){
        IkonDAO ikonDAO = new IkonDAO(getApplicationContext());
        List<Ikon> allIkons = ikonDAO.getAll();
        updateAutoCompleteTextView(allIkons);
    }

    private void updateAutoCompleteTextView(List<Ikon> completionList){
        ikonAdapter = new IkonAdapter(this, R.layout.custom_row_auto_complete_text_view, completionList);
        autoTextView.setThreshold(1); //will start working from first character
        autoTextView.setAdapter(ikonAdapter);
    }


    private void configureButtons(){
        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Placeholder to export message
                Toast messageSent = Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG);
                messageSent.show();
            }
        });

        ImageButton acceptButtton = (ImageButton) findViewById(R.id.accept_button);
        acceptButtton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                hideKeyboard();
                compileFinalSentence();
                clearGridView();
                ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
                sendButton.setVisibility(View.VISIBLE);
            }
        });

        ImageButton rejectButtton = (ImageButton) findViewById(R.id.reject_button);
        rejectButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (images.size() > 0) {
                    images.remove(images.size() - 1);
                }
                if (values.size() > 0) {
                    values.remove(values.size() - 1);
                }
                updateGridView();
            }
        });

    }

    public void compileFinalSentence(){
        // merge all images to one
        Toast.makeText(getApplicationContext(), "Will compile final sentence", Toast.LENGTH_SHORT).show();
        Bitmap finalImage = BitmapFactory.decodeResource(getResources(), images.get(0));
        for (int i=1; i<images.size(); i++){
            Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), images.get(i));
            finalImage = ImageHandler.joinImages(finalImage, bmp2);
        }
        clearGridView();
        finalSentence.setImageBitmap(finalImage);
        finalSentence.setVisibility(View.VISIBLE);
    }

    public void clearGridView(){
        images.clear();
        values.clear();
        updateGridView();
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
                JSONArray meanings = (JSONArray) jsonObject.get("meanings");
                //Toast.makeText(getApplicationContext(), meanings.toString(), Toast.LENGTH_SHORT).show();
                for (int n = 0; n < meanings.length(); n++) {
                    String object = meanings.getString(n);
                    if (object.equals(word)) return jsonObject.getString("filename");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
