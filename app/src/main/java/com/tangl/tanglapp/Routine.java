package com.tangl.tanglapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Routine {
    private final String title;
    private final ArrayList<Task> tasks;
    //what is the order of operation
    //routines of products and tasks
    //task duration
    //must be date aware

    Routine(String routineTitle,ArrayList<Task> unOrderedTasks){
        this.title = routineTitle;
        tasks = orderTasks(unOrderedTasks);
    }

    private ArrayList<Task> orderTasks(ArrayList<Task> tasks){
        Collections.sort(tasks, new Comparator<Task>(){

            @Override
            public int compare(Task task1, Task task2){
                return task1.getOrder().compareTo(task2.getOrder());
            }
        });
        return tasks;

    }


    public Routine addTask(Task task){
        ArrayList<Task> newTasks = this.tasks;
        newTasks.add(task);
        return new Routine(this.title,newTasks);
    }


    public Routine addTasks(ArrayList<Task> tasks){
        ArrayList<Task> newTasks = this.tasks;
        newTasks.addAll(tasks);
        return new Routine(this.title,newTasks);
    }


    public Routine changeTitle(String title){
        return new Routine(title,this.tasks);
    }


    ArrayList<Task> getTasks(){
        return this.tasks;
    }



    String getTitle(){
        return this.title;
    }

}
