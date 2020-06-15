package com.example.authenticate;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.authenticate.ui.main.PageViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class modifyFragment extends Fragment implements modify_dialog.dialogListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_name;

    Attendance_Adapter attendance_adapter;

    String name_student;


    public modifyFragment() {
        // Required empty public constructor
    }

    public static modifyFragment newInstance(int index) {
        modifyFragment fragment = new modifyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modify, container, false);
        SharedPreferences pref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MyPref", 0); // 0 - for private mode
        String s;
        s = pref.getString("subject_key","");
        setupRecyclerView(root,s);
        return root;
    }

    public void setupRecyclerView(final View root, final String s){


        if(user!=null){
            user_name = user.getDisplayName().toUpperCase();
        }

        db.document("teachers/"+user_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               DocumentSnapshot documentSnapshot = task.getResult();
                                               if (documentSnapshot.exists()) {
                                                   final String branch = documentSnapshot.getString("Branch");


                                                   Query query = db.collection("/Subjects/" + branch + "/" + "sem6" + "/" + s + "/List_of_Students");

                                                   FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                                                           .setQuery(query, Attendance.class).build();

                                                   final RecyclerView recyclerView = root.findViewById(R.id.list_of_students);
                                                   recyclerView.setHasFixedSize(true);
                                                   recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                                   for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                                                       View nextChild = recyclerView.getChildAt(i);
                                                       nextChild.setTag("unmarked");
                                                   }

                                                   FirebaseRecyclerViewOnClickListener mListener = new FirebaseRecyclerViewOnClickListener() {
                                                       @SuppressLint("ResourceAsColor")
                                                       @Override
                                                       public void onItemClick(final View view, DocumentSnapshot documentSnapshot, int position) {
/*
                view.setBackgroundColor(R.color.colorPrimary);
                for(int i=0; i < recyclerView.getChildCount(); ++i){
                    View nextChild = recyclerView.getChildAt(i);
                    if(nextChild != view){
                        nextChild.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }*/
                                                           Attendance attendance = documentSnapshot.toObject(Attendance.class);
                                                           name_student = attendance.getName();
                                                           final String attd = attendance.getAttended();
                                                           final String total = attendance.getTotal();

                                                           opendialog(attd);

                                                       }
                                                   };

                                                   attendance_adapter = new Attendance_Adapter(options,mListener);
                                                   recyclerView.setAdapter(attendance_adapter);
                                                   attendance_adapter.startListening();
                                               }
                                           }
                                       });



    }

    public void opendialog(String attd){

        modify_dialog dialog = new modify_dialog(attd);
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(),"Modify");
        dialog.setTargetFragment(modifyFragment.this,1);
    }

    @Override
    public void get_data(final String value) {

        SharedPreferences pref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String s;
        s = pref.getString("subject_key","");

        if(user!=null){
            user_name = user.getDisplayName().toUpperCase();
        }

        db.document("teachers/"+user_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            final String branch = documentSnapshot.getString("Branch");

                            db.document("/Subjects/" + branch + "/" + "sem6" + "/" + s + "/List_of_Students/" + name_student)
                                    .update("Attended", value)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Attendance modified", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            db.document("/students/"+name_student+"/sub/"+s)
                                    .update("Attended", value)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Attendance modified", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

    }
}
