package com.tangl.tanglapp.UISubSectionFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.tangl.tanglapp.R;
import com.tangl.tanglapp.DataModels.MenuPreviewDataModel;


public class ScrollSectionFragment extends Fragment {
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private RecyclerView mScrollSectionRecyclerView;
    FirestoreRecyclerAdapter mScrollInfoAdapter;
    private String mCollection;
    private int mOrientation;
    private int mViewHolderLayout;
    private static final String COLLECTION_PARAM = "Collection";
    private static final String VIEWHOLDER_LAYOUT_PARAM = "Layout";
    private static final String ORIENTATION = "Orientation";

    public ScrollSectionFragment() {
        // Required empty public constructor
    }


    public static ScrollSectionFragment newInstance(String collection, int viewHolderLayout, int orientation) {
        ScrollSectionFragment fragment = new ScrollSectionFragment();
        Bundle arguments = new Bundle();
        arguments.putString(COLLECTION_PARAM, collection);
        arguments.putInt(VIEWHOLDER_LAYOUT_PARAM, viewHolderLayout);
        arguments.putInt(ORIENTATION, orientation);
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCollection = getArguments().getString(COLLECTION_PARAM);
            mViewHolderLayout = getArguments().getInt(VIEWHOLDER_LAYOUT_PARAM);
            mOrientation = getArguments().getInt(ORIENTATION);
        }
        FirebaseFirestore rootReference = FirebaseFirestore.getInstance();
        Query collectionQuery = rootReference.collection(mCollection);

        FirestoreRecyclerOptions<MenuPreviewDataModel> options = new FirestoreRecyclerOptions.Builder<MenuPreviewDataModel>().setQuery(collectionQuery, MenuPreviewDataModel.class).build();
        mScrollInfoAdapter = new FirestoreRecyclerAdapter<MenuPreviewDataModel, ScrollSectionFragment.MenuItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScrollSectionFragment.MenuItemViewHolder holder, int position, @NonNull MenuPreviewDataModel model) {
                holder.setTitle(model.getTitle());
                holder.setImage(model.getPreviewImg());
                holder.setSubtitle(model.getSubtitle());
            }

            @NonNull
            @Override
            public ScrollSectionFragment.MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(mViewHolderLayout, parent, false);
                return new ScrollSectionFragment.MenuItemViewHolder(view);
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scroll_section_fragment, container, false);
        System.out.println("create view of scrollsection");
        TextView sectionTitle = v.findViewById(R.id.section_title);
        System.out.print("section title shoudl be " + sectionTitle);
        sectionTitle.setText(R.string.featured_title);
        mScrollSectionRecyclerView = v.findViewById(R.id.scroll_section_recycler_view);
        mScrollSectionRecyclerView.setAdapter(mScrollInfoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),mOrientation,false);
        mScrollSectionRecyclerView.setLayoutManager(linearLayoutManager);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        mScrollInfoAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mScrollInfoAdapter != null) {
            mScrollInfoAdapter.stopListening();
        }
    }


    private class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private View view;

        MenuItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setTitle(String remoteTitle) {
            TextView title = view.findViewById(R.id.featured_title);
            title.setText(remoteTitle);
        }

        void setSubtitle(String remoteSubtitle) {
            TextView subtitle = view.findViewById(R.id.featured_subtitle);
            subtitle.setText(remoteSubtitle);
        }

        void setImage(String url) {
            final long ONE_MEGABYTE = 2024 * 2024;
            final ImageView img = view.findViewById(R.id.featured_image);
            System.out.println("url " + url);
            StorageReference imageReference = mStorage.getReferenceFromUrl(url);
            imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                   @Override
                   public void onSuccess(byte[] bytes) {
                       Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                       img.setImageBitmap(bitmap);
                   }
               }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //to do
                }
            });
        }
    }


}