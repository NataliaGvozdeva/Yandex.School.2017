package com.iamkatrechko.yandexschool2017;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance(int num) {
        Bundle args = new Bundle();
        args.putInt("a", num);

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ((TextView) v.findViewById(R.id.text)).setText(String.valueOf(getArguments().getInt("a")));
        return v;
    }
}
