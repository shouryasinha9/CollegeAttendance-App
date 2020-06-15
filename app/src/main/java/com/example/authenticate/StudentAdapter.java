package com.example.authenticate;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class StudentAdapter extends FirestoreRecyclerAdapter<Student, StudentAdapter.StudentHolder> {

    public StudentAdapter(@NonNull FirestoreRecyclerOptions<Student> options) {
        super(options);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull StudentHolder holder, int position, @NonNull Student model) {

        holder.Subject_Code.setText((model.getSubject_Code()));
        holder.Attended.setText(model.getAttended());
        holder.Total.setText(model.getTotal());
        holder.Prediction.setText(model.getPrediction());
        holder.Percentage.setText(model.getPercentage());
        int percent_progress = model.get_computablePercentage();
        holder.progress.setProgress(percent_progress);
        holder.progress.setBackgroundColor(R.color.colorAccent);

        if (percent_progress >= 75){
            holder.progress.setProgressTintList(ColorStateList.valueOf(Color.rgb(40,189,79)));
        }
        else if(percent_progress >= 50 &&  percent_progress < 75){
            holder.progress.setProgressTintList(ColorStateList.valueOf(Color.rgb(250, 225, 35)));
        }
        else {
            holder.progress.setProgressTintList(ColorStateList.valueOf(Color.rgb(232, 62, 39)));
        }
    }

    @Override
    public void startListening() {
        super.startListening();
    }

    @Override
    public void stopListening() {
        super.stopListening();
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent,false);
        return new StudentHolder(v);
    }

    class StudentHolder extends RecyclerView.ViewHolder{
        TextView Subject_Code;
        TextView Attended;
        TextView Total;
        TextView Percentage;
        TextView Prediction;
        ProgressBar progress;

        public StudentHolder(@NonNull View itemView) {
            super(itemView);

            Subject_Code = itemView.findViewById(R.id.subject);
            Attended = itemView.findViewById(R.id.attended);
            Total = itemView.findViewById(R.id.total_classes);
            Percentage = itemView.findViewById(R.id.percentage);
            Prediction = itemView.findViewById(R.id.predict);
            progress = itemView.findViewById(R.id.progress);
        }
    }


}

