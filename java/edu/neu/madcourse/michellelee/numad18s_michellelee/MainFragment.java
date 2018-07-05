package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Context;

import edu.neu.madcourse.michellelee.numad18s_michellelee.realtimeDatabase.LeaderboardActivity;
import edu.neu.madcourse.michellelee.numad18s_michellelee.realtimeDatabase.RealtimeDatabaseActivity;
import edu.neu.madcourse.michellelee.numad18s_michellelee.realtimeDatabase.ScoreboardActivity;


public class MainFragment extends Fragment {

    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // SET UP ABOUT BUTTON
        View aboutButton = rootView.findViewById(R.id.about_button);

        // determining action when clicked
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // adding custom dialog with image
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.custom_dial, null);     // create view for custom dialog
            Drawable myShot = getResources().getDrawable(R.drawable.ic_headshot);    // make drawable from headshot image
            ImageView pic = (ImageView) dialogView.findViewById(R.id.headshot_image);   // set imageview ID
            pic.setImageDrawable(myShot);   // set imageview picture to drawable
            builder.setView(dialogView);    // set view to custom dialog
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // nothing
                        }
                    });
            mDialog = builder.show();
            }
        });

        // GENERATE ERROR BUTTON
        View generateButton = rootView.findViewById(R.id.generate_button);  // create button view
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
                throw new RuntimeException();
            }
        });

        // DICTIONARY BUTTON
        View dictionaryButton = rootView.findViewById(R.id.dictionary_button);  // create button view
        dictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
            Intent dictionaryIntent = new Intent(getActivity(), TestDictionary.class);
            startActivity(dictionaryIntent);
            }
        });

        // NEW GAME AND CONTINUE BUTTON
        View newGameButton = rootView.findViewById(R.id.newgame_button);  // create button view
        View continueButton = rootView.findViewById(R.id.continue_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
            GameFragment.isActive = 0;
            Intent newGameIntent = new Intent(getActivity(), GameActivity.class);
            startActivity(newGameIntent);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            GameFragment.isActive = 0;
            Intent intent = new Intent(getActivity(), GameActivity.class);
            intent.putExtra(GameActivity.KEY_RESTORE, true);
            getActivity().startActivity(intent);
            }
        });

        // LEADERBOARD AND SCOREBOARD BUTTONS
        View leaderboardButton = rootView.findViewById(R.id.leaderboard_button);  // create button view
        View scoreboardButton = rootView.findViewById(R.id.scoreboard_button);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws RuntimeException {
                startActivity(new Intent(getActivity(), LeaderboardActivity.class));

            }
        });
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScoreboardActivity.class));
            }
        });

        // ACKNOWLEDGEMENTS
        View acknowledgmentsButton = rootView.findViewById(R.id.ack_button);  // create button view
        acknowledgmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.scroggle_ack, null);     // create view for custom dialog
                builder.setCancelable(false);
                builder.setView(dialogView);    // set view to ack dialog
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                    }
                });
                mDialog = builder.show();
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get rid of the about dialog if it's still up
        if (mDialog != null)
            mDialog.dismiss();
    }

}
