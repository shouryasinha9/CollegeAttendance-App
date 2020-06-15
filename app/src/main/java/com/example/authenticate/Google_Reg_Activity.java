package com.example.authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Google_Reg_Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__reg_);

        add_new_user();
    }

    public void add_new_user(){

        FirebaseUser user_logged = FirebaseAuth.getInstance().getCurrentUser();
        RadioGroup radioGroup = findViewById(R.id.radio);
        EditText Name = findViewById(R.id.name);

        String new_name = Name.getText().toString().toUpperCase();
        String new_email = user_logged.getEmail();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        String role = radioButton.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(new_name).build();

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
            user.put("Google_reg","1");

            db.collection("students").document(new_name)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Google_Reg_Activity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error writing document", e);
                            Toast.makeText(Google_Reg_Activity.this, "Adding Failed", Toast.LENGTH_SHORT).show();
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
            user_t.put("Google_reg","1");

            db.collection("teachers").document(new_name)
                    .set(user_t)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Google_Reg_Activity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error writing document", e);
                            Toast.makeText(Google_Reg_Activity.this, "Adding Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

            Intent intent = new Intent(getBaseContext(),Teacher_details.class);
            startActivity(intent);
            finish();

        }
    }
}
