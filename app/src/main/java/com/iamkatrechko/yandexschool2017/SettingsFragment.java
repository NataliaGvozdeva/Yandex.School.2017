package com.iamkatrechko.yandexschool2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iamkatrechko.yandexschool2017.util.Util;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;

/**
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        v.findViewById(R.id.button_clear_history).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getActivity().getContentResolver().delete(Record.CONTENT_URI, null, null);
                Toast.makeText(getActivity(), R.string.cleared_history, Toast.LENGTH_SHORT).show();
            }
        });

        v.findViewById(R.id.button_dev_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.goToGooglePlayDeveloper(getActivity());
            }
        });

        return v;
    }
}
