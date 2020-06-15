package com.example.authenticate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authenticate.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Teacher_Subject extends AppCompatActivity{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String name;

    String[] sem6 = {"IAP", "CN", "OOMD"};
    private LinearLayout mlinearlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__subject);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if(user!=null){
            name = user.getDisplayName().toUpperCase();
        }

        mlinearlayout = findViewById(R.id.linearLayout);


        final ArrayList<String> reg_subjects = new ArrayList<String>();

        SharedPreferences pref_s = Objects.requireNonNull(getSharedPreferences("Semester", 0));

        String selected_sem = pref_s.getString("Sem","");

/*
        if(selected_sem.equals("sem4")){
            for (int i = 0;i<3;i++){
                current_sem[i] = sem4[i];
            }
        }
        else if(selected_sem.equals("sem6"))
        { for (int i = 0;i<3;i++){
            current_sem[i] = sem6[i];
        }
        }
*/
        final Spinner select_subject = findViewById(R.id.spinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> branch_adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_selected,sem6);
        branch_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        select_subject.setAdapter(branch_adapter);


        findViewById(R.id.add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Selected = select_subject.getSelectedItem().toString();

                for(int i=0; i < mlinearlayout.getChildCount(); ++i){
                    View nextChild = mlinearlayout.getChildAt(i);
                    if(nextChild.getTag() == Selected){
                        Toast.makeText(Teacher_Subject.this, "Already there", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                reg_subjects.add(Selected);

                LayoutInflater inflater =(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View rowView = inflater.inflate(R.layout.subject_card,null);
                rowView.setTag(Selected);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0,0, 10);
                rowView.setLayoutParams(layoutParams);
                TextView sub = rowView.findViewById(R.id.subject_name);
                sub.setText(Selected);
                ImageButton delete = (ImageButton) rowView.findViewById(R.id.delete_btn);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mlinearlayout.removeView(rowView);
                        reg_subjects.remove(rowView.getTag().toString());
                    }
                });
                // Add the new row before the add field button.
                mlinearlayout.addView(rowView, mlinearlayout.getChildCount());
            }
        });



        findViewById(R.id.save_subjects).setOnClickListener(new View.OnClickListener() {

           // String sem = getIntent().getStringExtra("Semester");

            @Override
            public void onClick(View v) {

                for (String s : reg_subjects){

                    Map<String, Object> set_user = new HashMap<>();
                    set_user.put("Sub_name",s);
                    set_user.put("Semester","sem6");


                    db.document("/teachers/"+name+"/Subjects/"+s)
                            .set(set_user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error writing document", e);
                                }
                            });
                }

                Intent intent = new Intent(v.getContext(),Teacher.class);
                v.getContext().startActivity(intent);
                finish();
            }
        });
    }
}
