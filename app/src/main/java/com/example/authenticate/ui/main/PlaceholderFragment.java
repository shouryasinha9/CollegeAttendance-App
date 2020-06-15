package com.example.authenticate.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authenticate.Attendance;
import com.example.authenticate.Attendance_Adapter;
import com.example.authenticate.FirebaseRecyclerViewOnClickListener;
import com.example.authenticate.R;
import com.example.authenticate.SetAttendance;
import com.example.authenticate.Student;
import com.example.authenticate.StudentAdapter;
import com.example.authenticate.subjects;
import com.example.authenticate.ui.home.HomeFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String user_name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Attendance_Adapter attendance_adapter;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
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
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_set_attendance, container, false);

        String currentDateString = DateFormat.getDateInstance().format(new Date());
        TextView date = root.findViewById(R.id.date);
        date.setText(currentDateString);

        set_buffer_subname(root);

        SharedPreferences pref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MyPref", 0); // 0 - for private mode
        String s;
        String sem;
        s = pref.getString("subject_key","");
        sem = pref.getString("Sem","");

        setupRecyclerView(root,s,sem);

        return root;
    }


    public void setupRecyclerView(final View root, final String s,final String sem){


        System.out.println("Current Subject" + s);

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

                            Query query = db.collection("/Subjects/" + branch+ "/" + sem + "/" + s + "/List_of_Students");

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

                                    view.setBackgroundColor(R.color.colorPrimary);
                                    for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                                        View nextChild = recyclerView.getChildAt(i);
                                        if (nextChild != view) {
                                            nextChild.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                        }
                                    }

                                    Attendance attendance = documentSnapshot.toObject(Attendance.class);
                                    final String name_student = attendance.getName();
                                    final String attd = attendance.getAttended();
                                    final String total = attendance.getTotal();

                                    root.findViewById(R.id.add_attd).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RadioGroup radio = root.findViewById(R.id.radiogroup);
                                            int selectedId = radio.getCheckedRadioButtonId();
                                            if (radio.getCheckedRadioButtonId() == -1)
                                            {
                                                Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            RadioButton get_status = root.findViewById(selectedId);
                                            String status = get_status.getText().toString();

                                            if (status.equals("Present")) {
                                                Integer Att = Integer.parseInt(attd);
                                                Integer Tatt = Integer.parseInt(total);
                                                Att += 1;
                                                Tatt += 1;
                                                String updated_attd = String.valueOf(Att);
                                                String updated_total = String.valueOf(Tatt);

                                                db.document("/Subjects/" +  branch+ "/" + sem + "/" + s + "/List_of_Students/" + name_student)
                                                        .update("Attended", updated_attd, "Total", updated_total).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                                                        view.setTag("present");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                                db.document("students/" + name_student + "/sub/" +s)
                                                        .update("Attended", updated_attd, "Total", updated_total).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                            } else if (status.equals("Absent")) {

                                                Integer Tatt = Integer.parseInt(total);

                                                Tatt += 1;
                                                String updated_total = String.valueOf(Tatt);

                                                db.document("/Subjects/" + branch+ "/" + sem + "/" + s + "/List_of_Students/" + name_student)
                                                        .update("Total", updated_total).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                                                        view.setTag("absent");

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                                db.document("students/" + name_student + "/sub/" + s)
                                                        .update("Total", updated_total).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                            }
                                            radio.clearCheck();
                                        }
                                    });


                                }
                            };
                            attendance_adapter = new Attendance_Adapter(options,mListener);
                            recyclerView.setAdapter(attendance_adapter);
                            attendance_adapter.startListening();


                        }
                    }
                    });
    }

    public void set_buffer_subname(final View root){

        db.document("Buffer/buffer")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Listen failed.", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {

                            String subject = snapshot.getString("Sub_Name");
                            TextView sub_name = root.findViewById(R.id.subject_name);
                            sub_name.setText(subject);

                        }
                    }
                });

    }

}

