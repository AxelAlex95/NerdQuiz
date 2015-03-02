package matsematics.nerdquiz;

/**
 * Created by Sommer on 24.02.2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HighscoreDialog extends DialogFragment {

    interface inputResult {
        void Submit();
        void Cancel();
    }

    private inputResult inputResult;
    private String name;
    private static View view;
    public HighscoreDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_highscore, null);
        ((TextView) view.findViewById(R.id.highscore_text)).setText("Enter your name to submit your Score");
        ((TextView) view.findViewById(R.id.score)).setText("Score: " + GameActivity.getHighscore());

        builder.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        inputResult.Submit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        inputResult.Cancel();
                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            inputResult = (inputResult) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement inputResult");
        }
    }

    public static String getName() {
        return ((EditText) view.findViewById(R.id.highscore_name)).getText().toString();
    }
}
