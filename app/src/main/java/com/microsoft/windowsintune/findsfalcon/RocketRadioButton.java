package com.microsoft.windowsintune.findsfalcon;

import android.content.Context;
import android.util.Log;
import android.widget.RadioButton;

import com.microsoft.windowsintune.findsfalcon.DataModels.Planet;
import com.microsoft.windowsintune.findsfalcon.DataModels.Rocket;

import java.util.Observable;
import java.util.Observer;

public class RocketRadioButton extends RadioButton {

    private Rocket mRocket;

    public RocketRadioButton(Context context) {
        super(context);
    }

    public void setmRocket(Rocket mRocket) {
        this.mRocket = mRocket;
        setText(mRocket.getName() + " Distance: " +mRocket.getMaxDistance() + "ava: " + mRocket.getAvailableUnits()  + "toal: " + mRocket.getTotalNo());
    }

    public Rocket getmRocket() {
        return mRocket;
    }

    public void update() {
        Log.d("DEBUG", "update");
        setText(mRocket.getName() + " Distance: " +mRocket.getMaxDistance() + "ava: " + mRocket.getAvailableUnits()  + "toal: " + mRocket.getTotalNo());
    }
}
