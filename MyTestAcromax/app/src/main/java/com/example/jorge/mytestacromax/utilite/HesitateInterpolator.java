package com.example.jorge.mytestacromax.utilite;

import android.view.animation.Interpolator;

/**
 * Created by jorge on 01/11/2017.
 */

public class HesitateInterpolator implements Interpolator {
    public HesitateInterpolator(int i) {}
    public float getInterpolation(float t) {
        return (float) (1 - Math.pow(1 - (2 * t), 3)) / 2;
    }
}
