package com.example.androidstudytimeapp;

import androidx.fragment.app.DialogFragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.androidstudytimeapp.databinding.FragmentDialogTimerSettingsBinding;


public class DialogTimerSettingsFragment extends DialogFragment {

    FragmentDialogTimerSettingsBinding binding;
    private OnSettingsChangedListener listener;

    public DialogTimerSettingsFragment() {
        // Constructor vacío
    }

    public interface OnSettingsChangedListener {
        void onSettingsChanged(int studyTime, int breakTime, int longBreakTime);
    }

    public void setOnSettingsChangedListener(OnSettingsChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDialogTimerSettingsBinding.inflate(inflater, container, false);

        // Limitar el máximo de cada SeekBar
        binding.studyTimeSeekBar.setMax(60);  // Máximo 60 minutos para estudio
        binding.shortBreakSeekBar.setMax(30); // Máximo 30 minutos para descanso corto
        binding.longBreakSeekBar.setMax(30);  // Máximo 30 minutos para descanso largo

        // Establecer valores predeterminados en los SeekBars
        binding.studyTimeSeekBar.setProgress(25); // Estudio: 25 minutos
        binding.shortBreakSeekBar.setProgress(5); // Descanso corto: 5 minutos
        binding.longBreakSeekBar.setProgress(15); // Descanso largo: 15 minutos

        // Actualizar los textos con los valores predeterminados
        updateSeekBarTexts();

        // Listener para el SeekBar de Estudio
        binding.studyTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.studyTimeText.setText("Estudio: " + progress + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Listener para el SeekBar de Descanso Corto
        binding.shortBreakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.shortBreakText.setText("Descanso corto: " + progress + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Listener para el SeekBar de Descanso Largo
        binding.longBreakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.longBreakText.setText("Descanso largo: " + progress + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Botón de confirmación
        binding.confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                // Llamamos al listener para pasar los valores de los SeekBars
                listener.onSettingsChanged(
                        binding.studyTimeSeekBar.getProgress(),
                        binding.shortBreakSeekBar.getProgress(),
                        binding.longBreakSeekBar.getProgress()
                );
            }
            dismiss(); // Cerrar el DialogFragment
        });

        return binding.getRoot();
    }

    // Actualizar los textos de los SeekBars con los valores actuales
    private void updateSeekBarTexts() {
        binding.studyTimeText.setText("Estudio: " + binding.studyTimeSeekBar.getProgress() + " min");
        binding.shortBreakText.setText("Descanso corto: " + binding.shortBreakSeekBar.getProgress() + " min");
        binding.longBreakText.setText("Descanso largo: " + binding.longBreakSeekBar.getProgress() + " min");
    }
}
