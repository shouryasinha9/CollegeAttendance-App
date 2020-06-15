package com.example.authenticate;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Reg_Activity extends Activity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_);

        mAuth = FirebaseAuth.getInstance();

        Button signUp = findViewById(R.id.Sign_Up);
        Button cancel = findViewById(R.id.cancel);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registernewuser();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void Registernewuser(){

        EditText Email = findViewById(R.id.email);
        EditText Password = findViewById(R.id.enter_password);

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Toast.makeText(Reg_Activity.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                            add_new_user();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Reg_Activity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Reg_Activity.this, "createUserWithEmail:failure", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
public void add_new_user(){

    FirebaseUser user_logged = FirebaseAuth.getInstance().getCurrentUser();

    RadioGroup radioGroup = findViewById(R.id.radio);
        EditText Name = findViewById(R.id.name);
        EditText Email = findViewById(R.id.email);

        String new_name = Name.getText().toString().toUpperCase();
        String new_email = Email.getText().toString();

     int selectedId = radioGroup.getCheckedRadioButtonId();
    RadioButton radioButton = findViewById(selectedId);

     String role = radioButton.getText().toString();

    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(new_name).build();

    assert user_logged != null;
    user_logged.updateProfile(profileUpdates)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "User profile updated.");
                    }
                }
            });

    if(role.equals("Student")) {

        Map<String, Object> user = new HashMap<>();
        user.put("Name", new_name);
        user.put("Email", new_email);
        user.put("Roll_no", "");
        user.put("Role",role);
        user.put("Branch", "");
        user.put("Semester", "");
        user.put("Section", "");

        db.collection("students").document(new_name)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Reg_Activity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                        Toast.makeText(Reg_Activity.this, "Adding Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(getBaseContext(),Student_Activity.class);
        startActivity(intent);
        finish();

    }
    else if(role.equals("Teacher")){

        Map<String, Object> user_t = new HashMap<>();
        user_t.put("Name", new_name);
        user_t.put("Email", new_email);
        user_t.put("Role",role);

        db.collection("teachers").document(new_name)
                .set(user_t)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Reg_Activity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                        Toast.makeText(Reg_Activity.this, "Adding Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(getBaseContext(),Teacher_details.class);
        startActivity(intent);
        finish();

    }

    }
}

