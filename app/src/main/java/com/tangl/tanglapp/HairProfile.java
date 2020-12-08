package com.tangl.tanglapp;

import android.content.Context;

final class HairProfile {
    //curl element
    final Float mCurlCoefficient;
    final Integer mType;
    public HairProfile(Float curlCoefficient){
        this.mCurlCoefficient = curlCoefficient;
        this.mType = setType(this.mCurlCoefficient);

    }

    private Integer setType(Float curlCoefficient){

        return Math.round(curlCoefficient);
    }

}
