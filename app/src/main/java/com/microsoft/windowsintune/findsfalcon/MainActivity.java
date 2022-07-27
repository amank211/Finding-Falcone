package com.microsoft.windowsintune.findsfalcon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.microsoft.windowsintune.findsfalcon.DataModels.Planet;
import com.microsoft.windowsintune.findsfalcon.DataModels.Rocket;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final String URL_ROCKETS = "https://findfalcone.herokuapp.com/vehicles";
    final String URL_PLANETS = "https://findfalcone.herokuapp.com/planets";
    final String URL_TOKEN = "https://findfalcone.herokuapp.com/token";
    ArrayList<Rocket> mRockets;
    List<Planet> mPlanets;
    PlanetRadioButton mSelectedPlanet;
    RocketRadioButton mLastSelectedRocket;
    RocketRadioButton mSelectedRocket;
    boolean isCLeared = false;
    boolean restore = false;

    PlanetRadioButton clearThis;

    Map<PlanetRadioButton, RocketRadioButton> mPairs = new HashMap<>();

    RadioGroup mPlanetsRadioGroup;
    RadioGroup mRocketListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    void printMap(){
        for (Map.Entry<PlanetRadioButton, RocketRadioButton> set :
                mPairs.entrySet()) {
            if(set.getValue() != null) {
                Log.d("MAP", "planet: " + set.getKey().getmPlanet().getName());
                Log.d("MAP", "Rocket: " + set.getValue().getmRocket().getName());
            }

        }
    }

    int getSelectedRockets(){
        int count = 0;
        for (Map.Entry<PlanetRadioButton, RocketRadioButton> set :
                mPairs.entrySet()) {
            if(set.getValue() != null) {
                Log.d("MAP", "planet: " + set.getKey().getmPlanet().getName());
                Log.d("MAP", "Rocket: " + set.getValue().getmRocket().getName());
                count++;
            }
        }
        return count;
    }

    void initViews(){
        mPlanetsRadioGroup = findViewById(R.id.planets);
        mRocketListView = findViewById(R.id.rockets);
        mPlanetsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                PlanetRadioButton button = radioGroup.findViewById(i);
                Log.d("DEBUG", "rocket:" +(mSelectedRocket != null) +" planet: " + (mSelectedPlanet != null) );
                if(mSelectedRocket != null && mSelectedPlanet != null){
                    Log.d("DEBUG", "putting values");
                    mPairs.put(mSelectedPlanet,mSelectedRocket);
                }
                isCLeared = !(mSelectedRocket == null);
                Log.d("DEBUG", "isClear: " + isCLeared);
                mRocketListView.clearCheck();
                mSelectedPlanet = button;
                RocketRadioButton rocket = mPairs.get(mSelectedPlanet);

                if(clearThis != null){
                    clearThis = button;
                }
                if(rocket != null){
                    restore = true;
                    rocket.setChecked(true);
                }
                printMap();
            }
        });

        mRocketListView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RocketRadioButton button = radioGroup.findViewById(i);

                if(button == null) return;
                Log.d("CHECK", "isCheked: " + button.isChecked());
                if(isCLeared) {
                    mSelectedRocket = null;
                    isCLeared = false;
                    return;
                }
                if(restore){
                    mSelectedRocket = button;
                    restore = false;
                    return;
                }
                Rocket rocket = button.getmRocket();

                if(mSelectedPlanet == null){
                    Toast.makeText(getBaseContext(), "Pls Select a Planet" + rocket.getName(), Toast.LENGTH_SHORT).show();
                    button.setChecked(false);
                } else {
                    if(mSelectedPlanet.getmPlanet().getDistance() > rocket.getMaxDistance()){
                        mSelectedRocket = null;
                        Toast.makeText(getBaseContext(), "This can not reach", Toast.LENGTH_SHORT).show();
                        button.setChecked(false);
                    } else if(rocket.getAvailableUnits() <= 0) {
                        mSelectedRocket = null;
                        Toast.makeText(getBaseContext(), "No more rockets of this model available", Toast.LENGTH_SHORT).show();
                        button.setChecked(false);
                    } else if(getSelectedRockets() >= 4) {
                        Toast.makeText(getBaseContext(), "Four Planets are selected", Toast.LENGTH_SHORT).show();
                        mSelectedRocket = null;
                        button.setChecked(false);
                    } else {
                        Log.d("DEBUG", "applied rocket");
                        rocket.setAvailableUnits(rocket.getAvailableUnits() - 1);
                        button.update();
                        if(mSelectedRocket != null){
                            mSelectedRocket.getmRocket().setAvailableUnits(mSelectedRocket.getmRocket().getAvailableUnits() + 1);
                            Log.d("DEBUG", " restored");
                            mSelectedRocket.update();
                        }
                        mSelectedRocket = button;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final RequestQueue queue = Volley.newRequestQueue(this);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Response.Listener<String> tokenResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TOK", "token received");
            }
        };
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<Rocket>>() {
                }.getType();
                mRockets = gson.fromJson(response, listType);
                for (int i = 0; i < mRockets.size(); i++) {
                    mRockets.get(i).setAvailableUnits(mRockets.get(i).getTotalNo());
                    RocketRadioButton radioButton = new RocketRadioButton(getBaseContext());
                    radioButton.setmRocket(mRockets.get(i));
                    mRocketListView.addView(radioButton);
                }
            }

        };
            final String wrong = "Wrong";
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), wrong, Toast.LENGTH_LONG).show();
                }
            };
            Response.ErrorListener errorListenerToken = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "Token not received", Toast.LENGTH_LONG).show();
                }
            };

            final StringRequest requestForRockets = new StringRequest(URL_ROCKETS, responseListener, errorListener);
            queue.add(requestForRockets);

            Response.Listener<String> planetResponse = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Type listType = new TypeToken<ArrayList<Planet>>() {
                    }.getType();
                    mPlanets = gson.fromJson(response, listType);
                    for (Planet planet : mPlanets) {
                        PlanetRadioButton radioButton = new PlanetRadioButton(getBaseContext());
                        PlanetRadioButton radioButtonForAll = new PlanetRadioButton(getBaseContext());
                        radioButton.setmPlanet(planet);
                        radioButtonForAll.setmPlanet(planet);
                        mPairs.put(radioButton, null);
                        mPlanetsRadioGroup.addView(radioButton);
                    }
                }
            };
            final JSONObject jsonBody = new JSONObject();
            final String requestBody = jsonBody.toString();

        Log.d("JSON",requestBody);

            final StringRequest requestForPlanets = new StringRequest(URL_PLANETS, planetResponse, errorListener);
        queue.add(requestForPlanets);

            final StringRequest requestForToken = new StringRequest(Request.Method.POST, URL_TOKEN, tokenResponseListener, errorListenerToken) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody.getBytes();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
        queue.add(requestForToken);
        queue.start();
        }
}