package com.example.authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Student_Activity extends Activity implements AdapterView.OnItemSelectedListener{

    String[] branch = {"CSE"};
    String[] semester = {"sem6"};
    String[] Section  = {"A" , "B"};
    private FirebaseAuth mAuth;

    String user_name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_);

        mAuth = FirebaseAuth.getInstance();

        final EditText enter_roll_no = findViewById(R.id.roll_no2);

        final Spinner branch_spinner = findViewById(R.id.Branch);
        branch_spinner.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter branch_adapter = new ArrayAdapter(this,R.layout.spinner_item_selected,branch);
        branch_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        branch_spinner.setAdapter(branch_adapter);


        final Spinner Semester_spinner = findViewById(R.id.semester);
        branch_spinner.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter semester_adapter = new ArrayAdapter(this,R.layout.spinner_item_selected,semester);
        semester_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        Semester_spinner.setAdapter(semester_adapter);


        final Spinner Section_spinner = findViewById(R.id.Section);
        branch_spinner.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter section_adapter = new ArrayAdapter(this,R.layout.spinner_item_selected,Section);
        section_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        Section_spinner.setAdapter(section_adapter);


        findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(user!=null){
                    user_name = user.getDisplayName().toUpperCase();
                }

                String roll_no = enter_roll_no.getText().toString();

                final String Selected_branch = branch_spinner.getSelectedItem().toString();
                final String Selected_sem = Semester_spinner.getSelectedItem().toString();
                final String Selected_Section = Section_spinner.getSelectedItem().toString();

                SharedPreferences pref_std = getApplicationContext().getSharedPreferences("get", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref_std.edit();
                editor.putString("Sem",Selected_sem);
                editor.apply();

                Map<String,Object> mp = new HashMap<>();
                mp.put("Branch",Selected_branch);
                mp.put("Roll_no",roll_no);
                mp.put("Section",Selected_Section);
                mp.put("Semester",Selected_sem);

                db.document("students/"+user_name)
                        .set(mp, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Student_Activity.this, "Details Updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(v.getContext(), Student_Main.class);
                        v.getContext().startActivity(i);
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
