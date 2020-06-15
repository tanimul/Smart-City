package com.example.nirmol_nogori.FragmentAdmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nirmol_nogori.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Admin_Fragment extends Fragment {

    public Profile_Admin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile__admin_, container, false);
    }
}
