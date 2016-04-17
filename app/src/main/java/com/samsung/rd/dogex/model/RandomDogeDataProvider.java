package com.samsung.rd.dogex.model;


import android.support.annotation.NonNull;
import android.util.Log;

import com.samsung.rd.dogex.model.domain.Doge;
import com.samsung.rd.dogex.model.domain.DogeGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDogeDataProvider implements DogeDataProvider {
    private static final String TAG = RandomDogeDataProvider.class.getSimpleName();
    private static final String CATEGORIES_JSON_GROUP = "categories";
    private static final String ADJECTIVE_JSON_GROUP = "adjectives";
    private final InputStream jsonInputStream;
    private Random randomGenerator = new Random();
    private String data;

    public RandomDogeDataProvider(InputStream jsonInputStream) {
        this.jsonInputStream = jsonInputStream;
        this.data = readJsonRawData();
    }

    @Override
    public List<DogeGroup> getAllNotEmpty() {
        return getRandomDoges();
    }

    private List<DogeGroup> getRandomDoges() {
        List<DogeGroup> doges = new ArrayList<>();
        try {
            List<String> categoryNames = getFromJSON(CATEGORIES_JSON_GROUP, data);
            for(int i = 0 ; i < categoryNames.size(); i++) {
                DogeGroup group = new DogeGroup(i, categoryNames.get(i).toString() + " Doges");
                int randomDoges = randomGenerator.nextInt(300) + 3;
                for(int j = 0 ; j < randomDoges; j++) {
                    Doge doge = new Doge(j, i, getRandomAdjective().toString() + " " + getRandomAdjective().toString() + " doge");
                    group.addDoge(doge);
                }
                doges.add(group);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return doges;
    }

    @NonNull
    private String getRandomAdjective() {
        String categoryName = "";
        try {
            List<String> names = getFromJSON(ADJECTIVE_JSON_GROUP, data);
            Random random = new Random();
            categoryName = names.get(random.nextInt(names.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categoryName;
    }

    private String readJsonRawData() {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonInputStream));
        try {
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {

        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    private List<String> getFromJSON(String elementId, String jsonRawData) throws JSONException {
        List<String> categoryNames = new ArrayList<>();
        JSONObject json = new JSONObject(jsonRawData);
        JSONArray groups = json.getJSONArray(elementId);
        for(int i = 0; i < groups.length(); i++) {
            categoryNames.add(groups.getString(i));
        }
        Log.d(TAG, "read " + groups.length() + " groups");
        return categoryNames;
    }
}
