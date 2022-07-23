package com.microsoft.windowsintune.findsfalcon;

import android.content.Context;
import android.widget.RadioButton;

import com.microsoft.windowsintune.findsfalcon.DataModels.Planet;
import com.microsoft.windowsintune.findsfalcon.DataModels.Rocket;

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
}
