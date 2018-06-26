package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.app.AlertDialog;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;

public class TestDictionary extends AppCompatActivity {

    EditText editText;              // text box for entering words
    ListView wordListView;          // listview for words
    ArrayList<String> itemList;     // word list;
    ArrayAdapter<String> adapter;   // string adapter
    AlertDialog mDialog;            // dialog for acknowledgments
    HashSet<String> dictionary; // storage for dictionary words

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_dictionary);

        dictionary = new HashSet<>();
        // MAKING THE DICTIONARY
        try {
            InputStream is = getApplicationContext().getAssets().open("dictionary");
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line=r.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // SETTING UP CLEAR BUTTON
        editText = (EditText) findViewById(R.id.edit_text_input);
        Button clearButton = (Button) findViewById(R.id.clear_button);

        clearButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                editText.setText("");           // sets the text box to "" when clicked, clearing the it
               // wordListView.setAdapter(null);  // clear the list of words
                adapter.clear();

            }
        });

        // SETTING UP LIST VIEW
        wordListView = (ListView) findViewById(R.id.list_view);
        itemList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.word_list_row, R.id.listRowTextView, itemList);
        wordListView.setAdapter(adapter);

        // ADD WORDS ENTERED TO LIST
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no action before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no action when text is changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String word = s.toString();             // make editable into a String

                if (word.length() >= 3) {               // check if the length of the word is >= 3
                    if (dictionary.contains(word)) {        // check if the dictionary contains the word
                        if (!itemList.contains(word)) {     // if the word is not already in the list
                            adapter.add(word);              // add word to list view
                            // play beep
                            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        }
                    }

                }
            }
        });
    }
}