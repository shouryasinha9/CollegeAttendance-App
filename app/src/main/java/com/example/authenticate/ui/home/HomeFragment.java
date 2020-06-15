package com.example.authenticate.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authenticate.FirebaseRecyclerViewOnClickListener;
import com.example.authenticate.R;
import com.example.authenticate.Student;
import com.example.authenticate.StudentAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String name;
    private StudentAdapter studentAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setupRecyclerView(root);

        return root;
    }


    public void setupRecyclerView(View root){
        if (user != null) {
            name = user.getDisplayName();}

        Query query = db.collection("students/"+name+"/sub");

        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class).build();

        studentAdapter = new StudentAdapter(options);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(studentAdapter);

        studentAdapter.startListening();

    }
}