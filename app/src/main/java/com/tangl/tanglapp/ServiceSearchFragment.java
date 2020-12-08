
/*
package com.tangl.tanglapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
]
public class ScrollSectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mServicesRecyclerView;
    private FirestoreRecyclerAdapter<MenuViewDataModel, MenuItemViewHolder> mServicesAdapter;
    public ServiceSearchFragment() {
        // Required empty public constructor
    }

 ]
    // TODO: Rename and change types and number of parameters
    public static ServiceSearchFragment newInstance() {
        ServiceSearchFragment fragment = new ServiceSearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore mRootReference = FirebaseFirestore.getInstance();
        Query mServicesQuery = mRootReference.collection("Services");
        FirestoreRecyclerOptions<MenuViewDataModel> options = new FirestoreRecyclerOptions.Builder<MenuViewDataModel>().setQuery(mServicesQuery, MenuViewDataModel.class).build();
        mServicesAdapter = new FirestoreRecyclerAdapter<MenuViewDataModel, MenuItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position, @NonNull MenuViewDataModel model) {
                holder.setName(model.getName());
            }

            @NonNull
            @Override
            public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_view,parent, false);
                return new MenuItemViewHolder(view);
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.service_search_fragment,container, false);
        mServicesRecyclerView = v.findViewById(R.id.services_recyclerview);
        mServicesRecyclerView.setAdapter(mServicesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mServicesRecyclerView.setLayoutManager(linearLayoutManager);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        mServicesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mServicesAdapter != null) {
            mServicesAdapter.stopListening();
        }
    }


    private class MenuItemViewHolder extends RecyclerView.ViewHolder{
        private View view;

        MenuItemViewHolder(View itemView){
            super(itemView);
            view = itemView;
        }

        void setName(String ServiceTitle){
            TextView serviceTitle = view.findViewById(R.id.service_title);
            serviceTitle.setText(ServiceTitle);
        }
    }

}
*/
