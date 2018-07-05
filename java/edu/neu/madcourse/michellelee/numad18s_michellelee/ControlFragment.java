package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;


public class ControlFragment extends Fragment {

    public MediaPlayer mMediaPlayer;// for music
    private AlertDialog mDialog;     // acknowledgments dialog

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        View main = rootView.findViewById(R.id.button_main);
        View restart = rootView.findViewById(R.id.button_restart);
        final Button music = (Button) rootView.findViewById(R.id.button_music);

        // start playing background music on startup
        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.stardust);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFragment.isActive = 1;
                getActivity().finish();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFragment.isActive = 1;
                ((GameActivity) getActivity()).restartGame();
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                music.setSelected(!music.isSelected());
                if (music.isSelected()) {
                    music.setText(getResources().getString(R.string.music_on));
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                } else {
                    music.setText(getResources().getString(R.string.music_off));
                    mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.stardust);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                }
            }
        });

        return rootView;
    }

}