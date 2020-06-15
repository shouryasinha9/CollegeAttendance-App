package com.example.authenticate;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

public interface FirebaseRecyclerViewOnClickListener {

    void onItemClick(View view,DocumentSnapshot documentSnapshot, int position);

}
