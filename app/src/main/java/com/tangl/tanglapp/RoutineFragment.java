/*
package com.tangl.tanglapp;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineFragment extends Fragment {
    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private ArrayList<Task> mTasks;
    private String mRoutineTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
       mTasks = (ArrayList<Parcelable>) getArguments().getParcelableArrayList("routine_list");
        mRoutineTitle = (String) getArguments().getString("routine_title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.routine_fragment,container, false);
        mTaskRecyclerView = (RecyclerView) v.findViewById(R.id.task_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mTaskRecyclerView.setLayoutManager(linearLayoutManager);
        
        if(mAdapter == null){
            mAdapter = new TaskAdapter(mTasks);
            mTaskRecyclerView.setAdapter(mAdapter);
        }
        return v;
    }


    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        private ArrayList<Task> mTasks;

        public TaskAdapter(ArrayList<Task> tasks){
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TaskHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position){
            System.out.println("onBindViewHolder called");
            Task task = mTasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount(){
            return mTasks.size();
        }
    }


    private class TaskHolder extends RecyclerView.ViewHolder {
        private Task mTask;
        private TextView mTaskTitle;
        private TextView mTaskDesc;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.card_fragment,parent, false));
            mTaskTitle = (TextView) itemView.findViewById(R.id.task_title);
            mTaskDesc = (TextView) itemView.findViewById(R.id.task_desc);
            //set on click listener
        }

        void bind(Task task){
            System.out.println("binding views ");
            mTask = task;
            mTaskTitle.setText(task.getName());
            mTaskDesc.setText(task.getDesc());

        }
    }


    public static RoutineFragment newInstance(Routine routine){
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("routine_list", routine.getTasks());
        arguments.putString("routine_title",routine.getTitle());
        System.out.println(" routine fragment instance created ");
        System.out.println("number of tasks "+routine.getTasks().size());
        RoutineFragment fragment = new RoutineFragment();
        fragment.setArguments(arguments);

        return fragment;
    }
}
*/