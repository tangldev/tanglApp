package com.tangl.tanglapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private TextView mHairProfileTextView;
    private TextView mSettingsTextView;
    private int LOGIN_REQUEST_CODE = 918;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

         */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mUser == null){
            Intent intent = new Intent(getContext(),LoginActivity.class);
            int requestCode = LOGIN_REQUEST_CODE;
            startActivityForResult(intent, requestCode);
        }

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View SettingsView = inflater.inflate(R.layout.setting_fragment, container, false);
        mSettingsTextView = SettingsView.findViewById(R.id.settings_account);
        mSettingsTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(),"nothing yet",Toast.LENGTH_LONG).show();
            }
        });
        mHairProfileTextView = SettingsView.findViewById(R.id.settings_hair_profile);
        mHairProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction openHairProfileTransaction = fragmentManager.beginTransaction();
                openHairProfileTransaction.replace(R.id.base_linear_layout,new HairProfileFragment());
                openHairProfileTransaction.addToBackStack("Hair Profile");
                openHairProfileTransaction.commit();
            }
        });
        return  SettingsView;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent intent){
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            mUser  = FirebaseAuth.getInstance().getCurrentUser();
        }
        else{
            Toast.makeText(getContext(),getResources().getString(R.string.login_failed),Toast.LENGTH_LONG).show();
        }
    }
}