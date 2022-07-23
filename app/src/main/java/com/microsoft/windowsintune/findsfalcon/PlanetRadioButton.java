package com.microsoft.windowsintune.findsfalcon;

import android.content.Context;
import android.widget.RadioButton;

import com.microsoft.windowsintune.findsfalcon.DataModels.Planet;

public class PlanetRadioButton extends RadioButton {

    private Planet mPlanet;
    public RocketRadioButton mRocketButton;

    public PlanetRadioButton(Context context) {
        super(context);
    }

    public void setmPlanet(Planet mPlanet) {
        this.mPlanet = mPlanet;
        setText(mPlanet.getName() + " Disatance: " +mPlanet.getDistance());
    }

    public Planet getmPlanet() {
        return mPlanet;
    }
}
