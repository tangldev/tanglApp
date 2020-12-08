package com.tangl.tanglapp;

import android.os.Parcel;
import android.os.Parcelable;

abstract class Task implements Parcelable{
    private final Float order;
    private final String desc;
    private final String name;
    private final String instruction;
    //private final Integer duration;

    Task(String name, String desc, String instruction, Float order){
        this.name = name;
        this.desc = desc;
        this.instruction = instruction;
        this.order = order;
        //this.duration = duration;
    }

    Task(Product product){
        this.name = product.getName();
        this.desc = product.getDesc();
        this.instruction = product.getDesc();
        this.order = product.getType();//change
        //this.duration = product.getDuration();
    }

    public Task(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.name = data[0];
        this.desc = data[1];
        this.instruction = data[2];
        this.order = Float.parseFloat(data[3]);
    }


    Float getOrder() {
        return order;
    }

    String getDesc() {
        return desc;
    }

    String getName() {
        return name;
    }

    String getInstruction() {
        return instruction;
    }


}
