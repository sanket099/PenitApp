package com.sankets.penit.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sankets.penit.R;
import com.sankets.penit.SharedPref;
import com.sankets.penit.activities.DisplayActivity;

public class IntroFragment3 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IntroFragment3() {
        // Required empty public constructor
    }


    public static IntroFragment3 newInstance(String param1, String param2) {
        IntroFragment3 fragment = new IntroFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPref sharedPref = new SharedPref(getContext());
        View view = inflater.inflate(R.layout.fragment_intro3, container, false);
        Button button = view.findViewById(R.id.btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref.save_first(false);
                startActivity(new Intent(getActivity(), DisplayActivity.class));
                getActivity().finish();
            }
        });


        return view;
    }
}