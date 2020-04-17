package com.example.myfirstapp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IkonDAO implements DAO<Ikon> {
    private List<Ikon> ikons = new ArrayList<Ikon>();
    private Context context;

    public IkonDAO(Context context){
        this.context = context;
        ikons = loadAllIkons();
    }

    public List<Ikon> loadAllIkons(){
        List<Ikon> ikonList = new ArrayList<Ikon>();

        try {
            JSONArray jsonArray = readJSONFromFile("ikons.json");
            for (int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Toast.makeText(getApplicationContext(), meanings.toString(), Toast.LENGTH_SHORT).show();
                List<String> meaningsList = new ArrayList<String>();
                JSONArray allMeanings = jsonObject.getJSONArray("meanings");
                for (int w =0; w<allMeanings.length(); w++){
                    meaningsList.add(allMeanings.getString(w));
                }
                ikonList.add(
                        new Ikon(context,
                                jsonObject.getLong("id"),
                                jsonObject.getString("name"),
                                meaningsList,
                                jsonObject.getString("filename")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ikonList;
    }

    @Override
    public Optional<Ikon> get(long id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Optional.ofNullable(ikons.get((int) id));
        } else return null;
    }

    @Override
    public List<Ikon> getAll() {
        return ikons;
    }

    @Override
    public void save(Ikon ikon) {
        ikons.add(ikon);
    }

    @Override
    public void update(int id, Ikon ikon) {
        ikons.set(id, ikon);
    }

    @Override
    public void delete(Ikon ikon) {
        ikons.remove(ikon);
    }

    public List<Ikon> getMatchingIkons(int amount, String word){
        List<Ikon> ikonList = new ArrayList<Ikon>();

        try {
            JSONArray jsonArray = readJSONFromFile("ikons.json");
            for (int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray meanings = (JSONArray) jsonObject.get("meanings");
                //Toast.makeText(getApplicationContext(), meanings.toString(), Toast.LENGTH_SHORT).show();
                for (int n=0; n < meanings.length(); n++) {
                    String meaning = meanings.getString(n);
                    if (meaning.length() - word.length() > 2) {
                        // do nothing if word is much longer than input
                        // TODO think about a similarity indicator: 0 if they are the same
                    } else if (meaning.contains(word)) {
                        List<String> meaningsList = new ArrayList<String>();
                        JSONArray allMeanings = jsonObject.getJSONArray("meanings");
                        for (int w =0; w<allMeanings.length(); w++){
                            meaningsList.add(allMeanings.getString(w));
                        }
                        ikonList.add(
                                new Ikon(context,
                                        jsonObject.getLong("id"),
                                        jsonObject.getString("name"),
                                        meaningsList,
                                        jsonObject.getString("filename")));
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ikonList;
    }

    public JSONArray readJSONFromFile(String filename) throws IOException, JSONException {
        InputStream inputStream = context.getAssets().open(filename);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        String json = new String(buffer, "UTF-8");
        return  new JSONObject(json).getJSONArray("data");
    }
}
