package com.tangl.tanglapp;

import android.os.Parcel;

class HairTask extends Task {
    public HairTask(String name, String desc, String instruction, Float order) {
        super(name, desc, instruction, order);
    }

    public HairTask(Product product) {
        super(product);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
