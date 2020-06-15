package com.example.authenticate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.MultiFactorResolver;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class MainActivity extends Activity {

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 0;

    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sign_in_google = findViewById(R.id.google_btn);
        Button facebook = findViewById(R.id.facebook);
        Button login = findViewById(R.id.login);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


        sign_in_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Google signIn fixing bugs", Toast.LENGTH_SHORT).show();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Making in Progress", Toast.LENGTH_SHORT).show();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if(Email.trim().equals("")){
                    Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();

                    return;
                }
                else if(Password.trim().equals("")){
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();

                    return;
                }

                signInByEmail();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_student = new Intent(v.getContext(), Reg_Activity.class);
                v.getContext().startActivity(to_student);
            }
        });

        if(user!=null){

            checkforstudent();
            checkforTeacher();

        }

    }

    private void signInByEmail(){

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "Successful login");

                            checkforstudent();
                            checkforTeacher();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "", task.getException());
                            Toast.makeText(MainActivity.this, "Login Failed, Wring Email or Password", Toast.LENGTH_SHORT).show();
                            // [END_EXCLUDE]
                        }


                    }
                });

    }

    public void checkforstudent() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user_email = user.getEmail();
        }

        db.collection("students").whereEqualTo("Email", user_email)
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                String role = doc.getString("Role");

                                if(role.equals("Student")){

                                    //current_role = "Student";
                                    Intent intent = new Intent(getBaseContext(), Student_Main.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                    }
                });

    }

    public void checkforstudent_google() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user_email = user.getEmail();
        }

        db.collection("students").whereEqualTo("Email", user_email)
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                String role = doc.getString("Role");
                                String check = doc.getString("Google_Reg");

                                if(role.equals("Student") && check.equals("1")){

                                    //current_role = "Student";
                                    Intent intent = new Intent(getBaseContext(), Student_Main.class);
                                    startActivity(intent);
                                    finish();

                                }


                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                    }
                });

    }



    public void checkforTeacher() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user_email = user.getEmail();
        }
        db.collection("teachers").whereEqualTo("Email",user_email)
                .limit(1)
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for(DocumentSnapshot doc : task.getResult()) {
                                    String role = doc.getString("Role");

                                    if(role.equals("Teacher")){

                                        //current_role = "Teacher";
                                        Intent intent = new Intent(getBaseContext(), Teacher.class);
                                        startActivity(intent);
                                        finish();

                                    }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                    }
                });

    }

    public void checkforTeacher_google() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user_email = user.getEmail();
        }
        db.collection("teachers").whereEqualTo("Email",user_email)
                .limit(1)
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                String role = doc.getString("Role");
                                String check = doc.getString("Google_Reg");

                                if(role.equals("Teacher")  && check.equals("1")){

                                    //current_role = "Teacher";
                                    Intent intent = new Intent(getBaseContext(), Teacher.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                    }
                });

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "Success");
                            checkforstudent_google();
                            checkforTeacher_google();
                            Intent intent = new Intent(getBaseContext(), Google_Reg_Activity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "signIn failure", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {

    }



}
