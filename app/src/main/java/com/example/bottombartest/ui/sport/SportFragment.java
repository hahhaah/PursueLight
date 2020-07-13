package com.example.bottombartest.ui.sport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bottombartest.R;

public class SportFragment extends Fragment {

  private SportViewModel mSportViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mSportViewModel =
            ViewModelProviders.of(this).get(SportViewModel.class);
    View root = inflater.inflate(R.layout.fragment_sport, container, false);
    final TextView textView = root.findViewById(R.id.text_dashboard);
    mSportViewModel.getText().observe(this, new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });
    return root;
  }
}