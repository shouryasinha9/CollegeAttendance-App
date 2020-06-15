package com.example.authenticate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

public class modify_dialog extends AppCompatDialogFragment {

    private EditText modify_value;
    private dialogListener listener;

    private String old_value;

    public modify_dialog(String old_value) {
        this.old_value = old_value;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout,null);

        builder.setView(view)
                .setTitle("Modify")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_attd = modify_value.getText().toString();
                        try {
                            int check_attd = Integer.parseInt(new_attd);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), "Only Enter number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listener.get_data(new_attd);
                    }
                });

        modify_value = view.findViewById(R.id.modify_value);
        modify_value.setText(old_value);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (dialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    " Must implement listener");
        }
    }

    public interface dialogListener{
        void get_data(String value);
    }
}
