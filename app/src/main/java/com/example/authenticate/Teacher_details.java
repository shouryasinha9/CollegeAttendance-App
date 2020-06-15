package com.example.authenticate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Teacher_details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] branch = { "CSE"};
    String[] semester = { "sem4", "sem6" };

    String user_name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);

        final Spinner branch_spinner = (Spinner) findViewById(R.id.Branch);
        branch_spinner.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter branch_adapter = new ArrayAdapter(this,R.layout.spinner_item_selected,branch);
        branch_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        branch_spinner.setAdapter(branch_adapter);


        final Spinner Semester_spinner = (Spinner) findViewById(R.id.semester);
        branch_spinner.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter semester_adapter = new ArrayAdapter(this,R.layout.spinner_item_selected,semester);
        semester_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        Semester_spinner.setAdapter(semester_adapter);


        findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if(user!=null){
                    user_name = user.getDisplayName().toUpperCase();
                }

                final String Selected_branch = branch_spinner.getSelectedItem().toString();
                final String Selected_sem = Semester_spinner.getSelectedItem().toString();

                SharedPreferences pref_s = getApplicationContext().getSharedPreferences("Semester", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref_s.edit();
                editor.putString("Sem",Selected_sem);
                editor.apply();

                Intent i = new Intent(v.getContext(), Teacher_Subject.class);
                v.getContext().startActivity(i);


                Map<String,String> mp = new HashMap<>();
                mp.put("Branch",Selected_branch);

                db.document("teachers/"+user_name)
                        .set(mp, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Teacher_details.this, "Details Updated", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
