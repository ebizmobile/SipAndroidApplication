package com.eazibiz.sipandroidapplication;

/**
 * Created by RAJESH on 11/15/2016.
 */

public class MyBounceInterpolator implements android.view.animation.Interpolator {
    double mAmplitude = 1;
    double mFrequency = 10;

    MyBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}