package com.example.voicerecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRecordPressed = (Button) findViewById(R.id.button1);
        Button btnStopPressed = (Button) findViewById(R.id.button2);
        Button btnPlayPressed = (Button) findViewById(R.id.button3);

        if(isMicrophonePresent()){
            getMicrophonePermission();
        }

        btnRecordPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RCORDING 1 ", "RECORDING");
                if (mediaRecorder != null) {
                    Toast.makeText(getApplicationContext(), "Un enregistrement est déjà en cours", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Log.d("RCORDING 2 ", "RECORDING");
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(getRecordingFilePath());
                    mediaRecorder.prepare();
                    mediaRecorder.start();

                    Log.d("RCORDING 3", "RECORDING");
                    Toast.makeText(getApplicationContext(), "Recording is started", Toast.LENGTH_SHORT).show();
                } catch(Exception e){
                    Log.d("RCORDING 4 ", "RECORDING");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                    Log.d("RCORDING 5 ", "RECORDING");
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }
                }
            }
        });

        btnStopPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                Toast.makeText(getApplicationContext(), "Recoding is stopped", Toast.LENGTH_SHORT).show();
            }
        });

        btnPlayPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(getRecordingFilePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    Log.d("DIRECTION ", getRecordingFilePath());
                    Toast.makeText(getApplicationContext(), "Recording is playing"+getRecordingFilePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isMicrophonePresent(){
        //cette ligne de code, vous pouvez vérifier si le dispositif sur lequel l'application est exécutée dispose de la fonctionnalité du microphone.
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile"+".mp3");
        return file.getPath();
    }
}