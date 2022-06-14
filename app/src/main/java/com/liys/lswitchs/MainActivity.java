package com.liys.lswitchs;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.liys.lswitch.BaseSwitch;
import com.liys.lswitch.LSwitch;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    LSwitch lswitch;
    int[] ids = {
            R.id.track_radius_seekBar,
            R.id.track_height_seekBar,
            R.id.thumb_radius_seekBar,
            R.id.thumb_height_seekBar,
            R.id.thumb_width_seekBar,
    };
    SeekBar[] seekBars = new SeekBar[ids.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lswitch = findViewById(R.id.lswitch);

        lswitch.setChecked(true);

        for (int i = 0; i < seekBars.length; i++) {
            seekBars[i] = findViewById(ids[i]);
            seekBars[i].setOnSeekBarChangeListener(this);
            seekBars[i].setProgress(seekBars[i].getMax());
        }

        lswitch.setOnCheckedListener(new BaseSwitch.OnCheckedListener() {
            @Override
            public void onChecked(boolean isChecked) {
                Log.d("66", "isChecked="+isChecked);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.track_radius_seekBar:
                lswitch.setTrackRadius(progress);
                break;
            case R.id.track_height_seekBar:
                lswitch.setTrackHeight(progress);
                break;

            case R.id.thumb_radius_seekBar:
                lswitch.setThumbRadius(progress);
                break;
            case R.id.thumb_height_seekBar:
                lswitch.setThumbHeight(progress);
                break;
            case R.id.thumb_width_seekBar:
                lswitch.setThumbWidth(progress);
                break;
        }
        lswitch.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
