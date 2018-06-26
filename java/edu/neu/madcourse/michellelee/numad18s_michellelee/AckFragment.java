package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AckFragment extends Fragment {

    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ack_fragment, container, false);

        // SET UP ACKNOWLEDGMENTS BUTTON
        View ackButton = rootView.findViewById(R.id.ack_button);

        // determining action when clicked
        ackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.ack_dial, null);     // create view for custom dialog
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
}
