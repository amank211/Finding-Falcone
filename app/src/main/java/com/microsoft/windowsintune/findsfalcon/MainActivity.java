package com.microsoft.windowsintune.findsfalcon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.microsoft.windowsintune.findsfalcon.DataModels.Planet;
import com.microsoft.windowsintune.findsfalcon.DataModels.Rocket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    final String URL_ROCKETS = "https://findfalcone.herokuapp.com/vehicles";
    final String URL_PLANETS = "https://findfalcone.herokuapp.com/planets";
    ArrayList<Rocket> mRockets;
    List<Planet> mPlanets;
    PlanetRadioButton mSelectedPlanet;
    RocketRadioButton mLastSelectedRocket;

    Set<Pair<PlanetRadioButton, RocketRadioButton>> mPairs = new HashSet<>();

    RadioGroup mPlanetsRadioGroup;
    RadioGroup mRocketListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlanetsRadioGroup = findViewById(R.id.planets);
        mRocketListView = findViewById(R.id.rockets);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final RequestQueue queue = Volley.newRequestQueue(this);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        mPlanetsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                PlanetRadioButton button = radioGroup.findViewById(i);
                mSelectedPlanet = button;
                mRocketListView.clearCheck();

                for (Pair p: mPairs) {
                    if(p.first == mSelectedPlanet){
                        RocketRadioButton a = (RocketRadioButton) p.second;
                        a.setChecked(true);
                        break;
                    }
                }
                mLastSelectedRocket = null;
            }
        });

        mRocketListView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RocketRadioButton button = radioGroup.findViewById(i);

                if(button == null || !button.isChecked()) return;
                Rocket rocket = button.getmRocket();
                if(mSelectedPlanet == null){
                    Toast.makeText(getBaseContext(), "Pls Select a Planet" + rocket.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    if(mSelectedPlanet.getmPlanet().getDistance() > rocket.getMaxDistance()){
                        Toast.makeText(getBaseContext(), "This can not reach", Toast.LENGTH_SHORT).show();
                    } else if(mLastSelectedRocket != null){
                        mLastSelectedRocket.getmRocket().setAvailableUnits(mLastSelectedRocket.getmRocket().getAvailableUnits() + 1);
                        rocket.setAvailableUnits(rocket.getAvailableUnits() -1);
                        mPairs.add(new Pair<>(mSelectedPlanet, button));
                    }
                }
            }
        });


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<Rocket>>(){}.getType();
                mRockets = gson.fromJson(response, listType);
                for (int i = 0; i<mRockets.size(); i++) {
                    mRockets.get(i).setAvailableUnits(mRockets.get(i).getTotalNo());
                    RocketRadioButton radioButton = new RocketRadioButton(getBaseContext());
                    radioButton.setmRocket(mRockets.get(i));
                    mRocketListView.addView(radioButton);
                }

//                RocketAdapter adapter = new RocketAdapter(getBaseContext(), R.layout.rocket_cards, mRockets);
//
//                mRocketListView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

            }
        };
        final String wrong = "Wrong";
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),wrong, Toast.LENGTH_LONG).show();
            }
        };


        final StringRequest requestForRockets = new StringRequest(URL_ROCKETS, responseListener, errorListener);
        queue.add(requestForRockets);

        Response.Listener<String> planetResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<ArrayList<Planet>>(){}.getType();
                mPlanets = gson.fromJson(response, listType);
                for(Planet planet: mPlanets){

                    PlanetRadioButton radioButton = new PlanetRadioButton(getBaseContext());
                    radioButton.setmPlanet(planet);
                    mPlanetsRadioGroup.addView(radioButton);
                }
            }
        };

        final StringRequest requestForPlanets = new StringRequest(URL_PLANETS, planetResponse, errorListener);
        queue.add(requestForPlanets);
        queue.start();
    }
}