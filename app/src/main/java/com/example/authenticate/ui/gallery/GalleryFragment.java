package com.example.authenticate.ui.gallery;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.authenticate.R;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryFragment extends Fragment {


    LinearLayout mlinearlayout;
    private GalleryViewModel galleryViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        String[] subject = { "Choose Branch" , "CSE", "ISE", "ECE", "MECH", "CIVIL","IP" };

        mlinearlayout = root.findViewById(R.id.linearLayout);

        final ArrayList<String> reg_subjects = new ArrayList<String>();

        final Spinner select_subject = (Spinner) root.findViewById(R.id.spinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> branch_adapter = new ArrayAdapter<String>(this.getActivity(),R.layout.spinner_item_selected,subject);
        branch_adapter.setDropDownViewResource(R.layout.spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        select_subject.setAdapter(branch_adapter);

        root.findViewById(R.id.add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Selected = select_subject.getSelectedItem().toString();

                reg_subjects.add(Selected);

                for(int i=0; i<mlinearlayout.getChildCount(); ++i){
                    View nextChild = mlinearlayout.getChildAt(i);
                    if(nextChild.getTag() == Selected){
                        Toast.makeText(getActivity(), "Already there", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                LayoutInflater inflater =(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.subject_card,null);
                // Add the new row before the add field button.
                mlinearlayout.addView(rowView, mlinearlayout.getChildCount());


               /*     CardView card = new CardView(Objects.requireNonNull(getContext()));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                card.setLayoutParams(params);

                // Set CardView corner radius
                card.setRadius(9);

                //set CardView id
                card.setTag(Selected);

                // Set cardView content padding
                card.setContentPadding(15, 15, 15, 15);

                // Set a background color for CardView
                card.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));

                // Set the CardView maximum elevation
                card.setMaxCardElevation(15);

                // Set CardView elevation
                card.setCardElevation(9);

                // Initialize a new TextView to put in CardView
                TextView tv = new TextView(getContext());
                tv.setLayoutParams(params);
                tv.setText(Selected);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                tv.setTextColor(Color.RED);

                Button btn = new Button(getContext());
                btn.setLayoutParams(params);
                btn.setText("Remove");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                // Put the TextView in CardView
                card.addView(tv);

                mlinearlayout.addView(card);*/

            }
        });
        return root;
    }

}
