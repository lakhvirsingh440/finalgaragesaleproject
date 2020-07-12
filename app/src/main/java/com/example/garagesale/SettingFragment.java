package com.example.garagesale;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.garagesale.utils.ShareStorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class SettingFragment extends Fragment {
    View view;
    Spinner etcate;
    Button btnSave;

    RadioButton rbEnglish, rbFrench;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_setting, container, false);

        rbEnglish = view.findViewById(R.id.rbEnglish);
        rbFrench = view.findViewById(R.id.rbFrench);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbEnglish.isChecked() || rbFrench.isChecked()) {
                    if (rbEnglish.isChecked()) {
                        setAppLocale("en");
                    } else {
                        setAppLocale("fr");
                    }
                }
            }
        });

        if (ShareStorage.getLanguage(getContext()).equals("fr")) {
            rbFrench.setChecked(true);
        } else {
            rbEnglish.setChecked(true);
        }

        return view;

    }

    private void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
            Locale locale = new Locale(localeCode);
            Locale.setDefault(locale);
            conf.locale = locale;
        }
        else{
            conf.locale=new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);

        ShareStorage.setLanguage(getContext(), localeCode);
        Intent intent = new Intent(getContext(), Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }


}



