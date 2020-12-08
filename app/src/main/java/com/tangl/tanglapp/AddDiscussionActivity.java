package com.tangl.tanglapp;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class AddDiscussionActivity extends FragmentActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discussion_activity);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on

    }
}