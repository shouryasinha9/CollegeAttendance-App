package com.example.authenticate;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class SubjectAdapter extends FirestoreRecyclerAdapter<subject, SubjectAdapter.SubjectHolder> {

    private FirebaseRecyclerViewOnClickListener mListener;


    public SubjectAdapter(@NonNull FirestoreRecyclerOptions<subject> options,FirebaseRecyclerViewOnClickListener listener) {
        super(options);
        mListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectHolder holder, int position, @NonNull subject model) {

        holder.Sub_name.setText(model.getSub_name());
        holder.Semester.setText(model.getSemester());

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
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_subject_card, parent,false);
        return new SubjectHolder(v,mListener);
    }

    class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

       TextView Sub_name;
       TextView Semester;

        private FirebaseRecyclerViewOnClickListener mListener;

        public SubjectHolder(@NonNull View itemView,FirebaseRecyclerViewOnClickListener listener) {
            super(itemView);
            mListener = listener;

            itemView.setOnClickListener(this);

            Sub_name = itemView.findViewById(R.id.subject_name);
            Semester = itemView.findViewById(R.id.semester);
        }
        @Override
        public void onClick(View v) {
            mListener.onItemClick(v,getSnapshots().getSnapshot(getAdapterPosition()), getAdapterPosition());
        }
    }
}
