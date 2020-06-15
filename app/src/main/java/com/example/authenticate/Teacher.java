package com.example.authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authenticate.ui.main.PlaceholderFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Teacher extends AppCompatActivity {

    FirebaseAuth mAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String user_name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        mAuth = FirebaseAuth.getInstance();

        String currentDateString = DateFormat.getDateInstance().format(new Date());
         TextView date = findViewById(R.id.date);
         date.setText(currentDateString);

        if(user!=null){
            String name= user.getDisplayName();
            TextView Name = findViewById(R.id.User_name);
            Name.setText(name);
        }

        setupRecyclerView();
    }

    public void setupRecyclerView(){



        if(user!=null){
            user_name = user.getDisplayName();
        }

        Query query = db.collection("/teachers/"+user_name+"/Subjects");

        FirestoreRecyclerOptions<subject> options = new FirestoreRecyclerOptions.Builder<subject>()
                .setQuery(query, subject.class).build();

        FirebaseRecyclerViewOnClickListener mListener = new FirebaseRecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, DocumentSnapshot documentSnapshot, int position) {
                subject subject_name = documentSnapshot.toObject(subject.class);
                String name_sub = Objects.requireNonNull(subject_name).getSub_name();
                String sem = Objects.requireNonNull(subject_name).getSemester();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("subject_key", name_sub);
                editor.putString("Sem",sem);
                editor.apply();

                db.document("Buffer/buffer")
                        .update("Sem",sem,"Sub_Name",name_sub)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                Intent intent = new Intent(view.getContext(), SetAttendance.class);
                view.getContext().startActivity(intent);
            }

        };

        SubjectAdapter subjectAdapter = new SubjectAdapter(options,mListener);
        RecyclerView recyclerView = findViewById(R.id.subject_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(subjectAdapter);

        subjectAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.update_sub){
            Intent intent = new Intent(getBaseContext(), Teacher_Subject.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void get_Sub() {

    }
}
