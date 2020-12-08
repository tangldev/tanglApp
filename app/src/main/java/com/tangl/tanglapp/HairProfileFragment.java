package com.tangl.tanglapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HairProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HairProfileFragment extends Fragment {
    FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private HashMap mHairProfile;
    ImageButton mPressed;
    public HairProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HairProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HairProfileFragment newInstance() {
        HairProfileFragment fragment = new HairProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pullRemote();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View hairProfileView = inflater.inflate(R.layout.hair_profile_fragment, container, false);
        //refactor all this
        ImageButton hair1Button = hairProfileView.findViewById(R.id.hair_type1);
        ImageButton hair2Button = hairProfileView.findViewById(R.id.hair_type2);
        ImageButton hair3Button = hairProfileView.findViewById(R.id.hair_type3);
        ImageButton hair4Button = hairProfileView.findViewById(R.id.hair_type4);

        hair1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSelector(view);
            }
        });
        return hairProfileView;
    }

    private void pullRemote(){
        DocumentReference userProfileDocRef = mDatabase.collection("Users").document(mUser.getUid());

        userProfileDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    Object docSnap = documentSnapshot.get("hair_profile");
                    if( docSnap instanceof HashMap){
                        mHairProfile = (HashMap) docSnap;
                    };
                    assert mHairProfile != null;
                }
            }
        });
    }

    private void changeSelector(View view){
        if(mPressed != null){
            mPressed.setForeground(null);
        }
        view.setForeground(getResources().getDrawable(R.drawable.hair_type_selector));
        mPressed = (ImageButton) view;
    }
}