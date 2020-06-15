package com.example.authenticate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Attendance_Adapter  extends FirestoreRecyclerAdapter<Attendance, Attendance_Adapter.AttendanceHolder> {


    private FirebaseRecyclerViewOnClickListener mListener;

    public Attendance_Adapter(@NonNull FirestoreRecyclerOptions<Attendance> options, FirebaseRecyclerViewOnClickListener listener) {
        super(options);
        mListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendanceHolder holder, int position, @NonNull Attendance model) {

        holder.Name.setText(model.getName());
        holder.Total.setText(model.getTotal());
        holder.Attended.setText(model.getAttended());

    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_layout, parent,false);
        return new Attendance_Adapter.AttendanceHolder(v,mListener);
    }

    class AttendanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView Name,Attended,Total;

        public AttendanceHolder(@NonNull View itemView,FirebaseRecyclerViewOnClickListener listener) {
            super(itemView);

            mListener = listener;

            itemView.setOnClickListener(this);

            Name = itemView.findViewById(R.id.student_name);
            Attended = itemView.findViewById(R.id.classes_attended);
            Total = itemView.findViewById(R.id.classes_total);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v,getSnapshots().getSnapshot(getAdapterPosition()), getAdapterPosition());
        }


    }


}
