package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.erminesoft.nfcpp.R;

/**
 * Created by Aleks on 31.03.2016.
 */
public class CreateNewCardFragment extends GenericFragment {
    private EditText nameEt;
    private EditText descriptionEt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_new_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEt = (EditText)view.findViewById(R.id.place_name_et);
        descriptionEt = (EditText)view.findViewById(R.id.description_et);

    }

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.save_new_place_button:
                    break;
            }

        }
    }
}
