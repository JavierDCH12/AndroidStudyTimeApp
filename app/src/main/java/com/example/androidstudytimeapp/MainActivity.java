package com.example.androidstudytimeapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.androidstudytimeapp.UI.DialogTimerSettingsFragment;
import com.example.androidstudytimeapp.UI.FragmentTimer;
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

        FragmentTimer fragmentTimer = (FragmentTimer) getSupportFragmentManager().findFragmentById(R.id.fragmentTimer);
        if (fragmentTimer != null) {
            // Pasar el Bundle al FragmentTimer
            Bundle args = new Bundle();
            args.putInt("studyTime", studyTime);
            args.putInt("shortBreakTime", shortBreakTime);
            args.putInt("longBreakTime", longBreakTime);

            fragmentTimer.setArguments(args);
            fragmentTimer.updateTimerSettings();

        }else {
            // Si el fragmento no se encuentra, lo añadimos al FragmentManager
            Log.e("FRAGMENTO", "FragmentTimer no está en el FragmentManager. Añadiéndolo ahora.");

            // Crear una nueva instancia de FragmentTimer
            fragmentTimer = new FragmentTimer();
            Bundle args = new Bundle();
            args.putInt("studyTime", studyTime);
            args.putInt("shortBreakTime", shortBreakTime);
            args.putInt("longBreakTime", longBreakTime);
            //log de cada uno de los datos
            Log.d("STUDY TIME", "STUDY TIME: " + studyTime);
            Log.d("SHORT BREAK TIME", "SHORT BREAK TIME: " + shortBreakTime);
            Log.d("LONG BREAK TIME", "LONG BREAK TIME: " + longBreakTime);

            // Añadir el fragmento al FragmentManager
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTimer, fragmentTimer).commit();

            fragmentTimer.setArguments(args);


        }
    }




}//MAIN END