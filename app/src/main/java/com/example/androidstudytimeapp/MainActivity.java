package com.example.androidstudytimeapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.androidstudytimeapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  implements DialogTimerSettingsFragment.OnSettingsChangedListener {

    ActivityMainBinding binding;

    private int studyTime=15;
    private int shortBreakTime=5;
    private int longBreakTime=15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        showSettingsDialog();











    }//ONCREATE END

    private void showSettingsDialog() {
        DialogTimerSettingsFragment dialog = new DialogTimerSettingsFragment();
        dialog.setOnSettingsChangedListener(this);

        dialog.show(getSupportFragmentManager(), "TimerSettings");
    }


    @Override
    public void onSettingsChanged(int studyTime, int breakTime, int longBreakTime) {
        this.studyTime=studyTime;
        this.shortBreakTime=breakTime;
        this.longBreakTime=longBreakTime;

        navigateToTimerFragment();

    }

    private void navigateToTimerFragment() {
        NavController navController = Navigation.findNavController(this, R.id.navHost);
        Bundle args = new Bundle();
        args.putInt("studyTime", studyTime);
        args.putInt("shortBreakTime", shortBreakTime);
        args.putInt("longBreakTime", longBreakTime);

        navController.navigate(R.id.action_dialogTimerSettingsFragment_to_fragmentTimer, args);
    }



}//MAIN END