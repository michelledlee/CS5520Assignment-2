package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GameFragment extends Fragment {
    // BOARD SET UP
    private View rootView;
    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};
    private Tile mEntireBoard = new Tile(this);
    private Tile mLargeTiles[] = new Tile[9];
    private Tile mSmallTiles[][] = new Tile[9][9];
    private Tile.Owner mPlayer = Tile.Owner.X;
    private Set<Tile> mAvailable = new HashSet<Tile>();
    private int mLastLarge;
    private int mLastSmall;

    // SELECTED SMALL TILES
    private int tile1Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile2Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile3Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile4Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile5Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile6Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile7Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile8Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int tile9Int[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int allTilesInt[][] = {tile1Int, tile2Int, tile3Int, tile4Int, tile5Int, tile6Int, tile7Int, tile8Int, tile9Int};

    // PLAYER WORD GUESSES
    private StringBuilder tile1 = new StringBuilder();
    private StringBuilder tile2 = new StringBuilder();
    private StringBuilder tile3 = new StringBuilder();
    private StringBuilder tile4 = new StringBuilder();
    private StringBuilder tile5 = new StringBuilder();
    private StringBuilder tile6 = new StringBuilder();
    private StringBuilder tile7 = new StringBuilder();
    private StringBuilder tile8 = new StringBuilder();
    private StringBuilder tile9 = new StringBuilder();
    private StringBuilder allTiles[] = {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9};
    private int[] stringSubmitted = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // BUTTON LIST
    private Button buttonList[][] = new Button[9][9];

    // LIST VIEW SET UP
    private ListView wordListView;          // listview for words
    private ArrayList<String> itemList;     // word list
    private ArrayAdapter<String> adapter;   // string adapter

    // SOUND MANAGEMENT
    private int mSoundClick, mSoundCorrect, mSoundIncorrect;
    private SoundPool mSoundPool;
    private float mVolume = 1f;

    // SCOREBOARD
    private TextView scoreBoard;            // score board
    private int pointsScore;                // points

    // TIMER
    private AlertDialog startDialog;    // start dialog
    private AlertDialog gameOverDialog; // game over dialog
    private AlertDialog mDialog;        // paused dialog
    private boolean isPaused = false;   // paused status
    private long timeRemaining = 0;     // CountDownTimer remaining time
    private int phase = 1;              // current phase
    private CountDownTimer timer;       // timer object

    // PHASE TWO SET UP
    private AlertDialog phaseTwoDialog;    // start dialog
    private StringBuilder phaseTwoString = new StringBuilder();  // string that stores letters selected by player
    private StringBuilder phaseTwoAnswer = new StringBuilder();  // the answer that the player submits
    private CountDownTimer phaseTwoTimer;  // timer object
    private int[] phaseTwoIsSelected = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // checking which boards are selected
    private Button buttonListPhaseTwo[] = new Button[9];
    private int bigTilesInt[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    static private int mBigTileIDs[] = {R.id.big1, R.id.big2, R.id.big3,
            R.id.big4, R.id.big5, R.id.big6, R.id.big7, R.id.big8,
            R.id.big9,};

    //DICTIONARY
    private HashSet<String> dictionary;

    // DONE TRACKER
    private boolean[] submittedTracker = {false, false, false, false, false, false, false, false, false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);    // retain this fragment across configuration changes.
        initGame();
        initMusic();
        dictionary = createDictionary();
    }

    private void clearAvailable() {
        mAvailable.clear();
    }

    private void addAvailable(Tile tile) {
        mAvailable.add(tile);
    }

    public boolean isAvailable(Tile tile) {
        return mAvailable.contains(tile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.large_board, container, false);
        initViews(rootView);
        updateAllTiles();
        showStartDialog(rootView);  // start dialog and instructions
        return rootView;
    }

    /**
     * Initialize music and sounds for game
     */
    public void initMusic() {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundClick = mSoundPool.load(getActivity(), R.raw.click, 1);
        mSoundCorrect = mSoundPool.load(getActivity(), R.raw.correct, 1);
        mSoundIncorrect = mSoundPool.load(getActivity(), R.raw.wrong, 1);
    }

    /**
     * Start dialog with instructions and initial play button
     * @param rootView
     */
    public void showStartDialog(final View rootView) {
        AlertDialog.Builder startBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater startInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = startInflater.inflate(R.layout.start_screen, null);     // create view for custom dialog
        startBuilder.setCancelable(false);
        startBuilder.setView(dialogView);    // set view to start screen layout
        Button start = (Button) dialogView.findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
                initTimer(rootView);
            }
        });
        startDialog = startBuilder.show();
    }

    /**
     * Initialize timer
     * @param rootView
     */
    // get reference of the XML layout's widgets
    public void initTimer(View rootView) {
        final TextView tView = (TextView) rootView.findViewById(R.id.timer);
        final Button btnPause = (Button) rootView.findViewById(R.id.btn_pause);

        // before start, pause is disabled
        btnPause.setEnabled(false);

        // once started, pause button is active
        btnPause.setEnabled(true);

        long millisInFuture = 90000;  // 1.5 minutes
        long countDownInterval = 1000; // 1 second

        // Initialize a new CountDownTimer instance
        timer = new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

                if (millis >= 10000 && millis <= 11000) {                                       // if time is 10 seconds
                    Toast.makeText(getActivity(), "FINAL COUNTDOWN", Toast.LENGTH_LONG).show();    // entering final countdown
                }

                // display time in minutes and seconds
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                if (isPaused) {   // cancel current instance if paused
                    cancel();
                } else {
                    tView.setText(text);        // display current time set above
                    timeRemaining = millisUntilFinished;    // store remaining time
                }
            }

            public void onFinish() {
                if (phase == 1) {
                    phase = 2;
                    phaseTwo();
                } else if (phase == 2) {
                    gameOver(); // game over dialog
                }

                //Disable the pause, resume and cancel button
                btnPause.setEnabled(false);     // on finish, pause does not work
            }
        }.start();

        // Set a Click Listener for pause button
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            isPaused = true;
            btnPause.setEnabled(false); // disable the pause button

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.paused_screen, null);     // create view for custom dialog
            builder.setCancelable(false);
            builder.setView(dialogView);    // set view to paused screen layout
            Button resume = (Button) dialogView.findViewById(R.id.btn_resume);
            resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    btnPause.setEnabled(true);   // enable the pause button

                    // specify the current state is not paused
                    isPaused = false;

                    // initialize a new CountDownTimer instance
                    long millisInFuture = timeRemaining;
                    long countDownInterval = 1000;
                    timer = new CountDownTimer(millisInFuture, countDownInterval) {
                        public void onTick(long millisUntilFinished) {
                            long millis = millisUntilFinished;

                            if (millis >= 10000 && millis <= 11000) {                                       // if time is 10 seconds
                                Toast.makeText(getActivity(), "FINAL COUNTDOWN", Toast.LENGTH_LONG).show();    // entering final countdown
                            }

                            // display time in minutes and seconds
                            String text = String.format(Locale.getDefault(), "%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(millis),
                                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                            if (isPaused) { // pause requested
                                cancel();
                            } else {
                                tView.setText(text);
                                timeRemaining = millisUntilFinished;    // remember time remaining
                            }
                        }

                        public void onFinish() {
                            if (phase == 1) {
                                phase = 2;
                                phaseTwo();
                            } else if (phase == 2) {
                                gameOver(); // game over dialog
                            }

                            // disable all buttons
                            btnPause.setEnabled(false);
                        }
                    }.start();
                }
            });
            mDialog = builder.show();
            }
        });
    }

    /**
     * Initialize the board with words
     * @param rootView the board layout
     */
    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);                             // set view outer board

        // CREATE WORD BANK
        final ArrayList<String> wordBank = createWordBank();        // 9 letter word bank

        // INITIALIZE THE BOARDS
        for (int large = 0; large < 9; large++) {                   // intitialize each 1/9 board
            View outer = rootView.findViewById(mLargeIds[large]);   // get view for inner board
            mLargeTiles[large].setView(outer);                      // set view for 1/9 board
            int r = (int) (Math.random() * wordBank.size());        // get random number of index in wordBank
            String word = wordBank.get(r);                          // get word from wordBank
            //            Log.e("word = ", word);
            char[] shuffled = shuffle(word).toCharArray();          // shuffle the letters of the word

            for (int small = 0; small < 9; small++) {                                   // small boards within the large board
                final Button inner = (Button) outer.findViewById(mSmallIds[small]);     // initialize the button
                char letter = Character.toUpperCase(shuffled[small]);            // get current letter in word
                inner.setText("" + letter);                                      // set text of the button to be the current letter
                final Tile smallTile = mSmallTiles[large][small];                // small tile within large tile
                buttonList[large][small] = inner;                                // put this button into the button array
                smallTile.setView(inner);           // set view of the inner tile to be the button view
                inner.setBackgroundResource(R.drawable.tile_gray);
                smallTile.setLargeTileNo(large);
                smallTile.setSmallTileNo(small);
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    // make a sound when clicked
                    mSoundPool.play(mSoundClick, mVolume, mVolume, 1, 0, 1f);

                    // get tile coordinates
                    int largeI = smallTile.getLargeTileNo();
                    //                        Log.e("large tile no", String.valueOf(largeI));
                    int smallI = smallTile.getSmallTileNo();
                    //                        Log.e("small tile no", String.valueOf(smallI));
                    // change background color
                    inner.setSelected(!inner.isSelected());
                    if (phase == 1) {
                        if (inner.isSelected()) {
                            // selected state change
                            inner.setBackgroundResource(R.drawable.tile_blue);  // selected background turns blue
                            String letter = (inner.getText()).toString();
                            //                            Log.e("letter", String.valueOf(letter));
                            allTiles[largeI].append(letter);                    // add new letter selected to current string
                            //                            Log.e("character appended", String.valueOf(letter));
                            allTilesInt[largeI][smallI] = 1;                    // set background highlighted
                        } else {
                            // de-selected state change
                            inner.setBackgroundResource(R.drawable.tile_gray);          // deselected background is grey
                            String letter = (inner.getText()).toString();               // get letter from inner tile clicked
                            //                            Log.e("letter", String.valueOf(letter));
                            allTiles[largeI].deleteCharAt(allTiles[largeI].length() - 1); // remove most recently selected
                            allTilesInt[largeI][smallI] = 0;                            // deselect background highlighted
                        }
                    } else {
                        // selected state change
                        if (phaseTwoString.length() != 9) {
                            if (phaseTwoIsSelected[largeI] == 0) {
                                inner.setBackgroundResource(R.drawable.tile_purple);  // selected background turns blue
                                String letter = (inner.getText()).toString();
                                //                            Log.e("letter", String.valueOf(letter));
                                phaseTwoString.append(letter);                    // add new letter selected to current string
                                //                            Log.e("character appended", String.valueOf(letter));
                                phaseTwoIsSelected[largeI] = 1;                    // set background highlighted
                            }
                        }
                    }
                    }
                });
            }
        }

        // INITIALIZE SUBMISSION BUTTONS FOR CHECKING
        Button b1 = (Button) rootView.findViewById(R.id.B1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[0] == 1) return;
                checkWord(1);
            }
        });
        Button b2 = (Button) rootView.findViewById(R.id.B2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[1] == 1) return;
                checkWord(2);
            }
        });
        Button b3 = (Button) rootView.findViewById(R.id.B3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[2] == 1) return;
                checkWord(3);
            }
        });
        Button b4 = (Button) rootView.findViewById(R.id.B4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[3] == 1) return;
                checkWord(4);
            }
        });
        Button b5 = (Button) rootView.findViewById(R.id.B5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[4] == 1) return;
                checkWord(5);
            }
        });
        Button b6 = (Button) rootView.findViewById(R.id.B6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[5] == 1) return;
                checkWord(6);
            }
        });
        Button b7 = (Button) rootView.findViewById(R.id.B7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[6] == 1) return;
                checkWord(7);
            }
        });
        Button b8 = (Button) rootView.findViewById(R.id.B8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[7] == 1) return;
                checkWord(8);
            }
        });
        Button b9 = (Button) rootView.findViewById(R.id.B9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringSubmitted[8] == 1) return;
                checkWord(9);
            }
        });
        Button b10 = (Button) rootView.findViewById(R.id.B10);
        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phaseTwoCheck();
            }
        });


        // SETTING UP LIST VIEW
        wordListView = (ListView) rootView.findViewById(R.id.accepted_words);
        itemList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.word_list_row, R.id.listRowTextView, itemList);
        wordListView.setAdapter(adapter);

        // SETTING UP SCOREBOARD
        scoreBoard = (TextView) rootView.findViewById(R.id.score);
    }

    /**
     * Create word bank for 9 letter words
     * @return array list with all the 9 letter words
     */
    public ArrayList<String> createWordBank() {
        ArrayList<String> wordBank = new ArrayList<String>();
        AssetManager assetM = getActivity().getAssets();                        // get asset manager to access dictionary file
        try {
            InputStream is = assetM.open("dictionary");                // open input stream to read dictionary
            BufferedReader r = new BufferedReader(new InputStreamReader(is));   // read from input stream
            String line;
            while ((line = r.readLine()) != null) {             // while line is not null
                if (line.length() == 9)  wordBank.add(line);    // take only the 9 letter words
            }
        } catch (IOException e) {
        }
        return wordBank;
    }

    /**
     * Create look up dictionary for all words > 3 letters
     */
    public HashSet<String> createDictionary() {
        HashSet<String> dictionary = new HashSet<String>();
        AssetManager assetM = getActivity().getAssets();                        // get asset manager to access dictionary file
        try {
            InputStream is = assetM.open("dictionary");
            BufferedReader r = new BufferedReader(new InputStreamReader(is));   // read from input stream
            String line;
            while ((line = r.readLine()) != null) {              // while line is not null
                if (line.length() >= 3) dictionary.add(line);    // take only the 3 letter word
            }
        } catch (IOException e) {
        }
        return dictionary;
    }

    /**
     * If a guess is greater than or equal to three words, the dictionary is checked
     * to see if the word is in there. If the word is not in the found words set, the
     * word is added.
     * @param largeTile the large tile for the word to check
     */
    public void checkWord(int largeTile) {
        String guess = "";
        int length = 0;
        int score = 0;
        boolean correct = false;                    // flag for whether the word is valid

        // get word string based on which button was pressed
        switch(largeTile) {
            case 1:
                guess = tile1.toString();
                break;
            case 2:
                guess = tile2.toString();
                break;
            case 3:
                guess = tile3.toString();
                break;
            case 4:
                guess = tile4.toString();
                break;
            case 5:
                guess = tile5.toString();
                break;
            case 6:
                guess = tile6.toString();
                break;
            case 7:
                guess = tile7.toString();
                break;
            case 8:
                guess = tile8.toString();
                break;
            case 9:
                guess = tile9.toString();
                break;
            default:
                guess = "";
        }
        length = guess.length();

        // check if valid word
        if (length >= 3) {                  // check if the length of the word is >= 3
            if (dictionary.contains(guess.toLowerCase())) {       // check if the dictionary contains the word
                correct = true;                     // true if found

                stringSubmitted[largeTile-1] = 1;     // word has been submitted for this tile

                // set amount of points
                int bonus = 0;
                for (char c : guess.toCharArray()) {
                    if (c == 'E' || c == 'A' || c == 'I' || c == 'O' || c == 'N' || c == 'R' || c == 'T' || c == 'L' || c == 'S') {
                        pointsScore += 1;   // bonus for x, y, or z in word
                    } else if (c == 'D' || c == 'G') {
                        pointsScore += 2;
                    } else if (c == 'B' || c == 'C' || c == 'M' || c == 'P') {
                        pointsScore += 3;
                    } else if (c == 'F' || c == 'H' || c == 'V' || c == 'W' || c == 'Y') {
                        pointsScore += 4;
                    } else if (c == 'K') {
                        pointsScore += 5;
                    } else if (c == 'J' || c == 'X') {
                        pointsScore += 8;
                    } else {
                        pointsScore += 10;
                    }
                }
                if (length == 9 && phase == 1) bonus += length;         // bonus for 9 letter word during phase 1
                pointsScore += length + bonus;                          // calculate total score
                scoreBoard.setText(""+pointsScore);                     // set scoreboard to total

                // mark as submitted in tile tacker array
                submittedTracker[largeTile-1] = true;
                int done = 0;
                for (boolean b : submittedTracker) {
                    if (b == true) done++;
                }
                if (done == 9) {
                    timer.cancel();
                    gameOver();
                }
            }
        } else {
            correct = false;    // false if not found
            pointsScore -= 3;   // penalize for submitting an incorrect word
            scoreBoard.setText(""+pointsScore);                     // set scoreboard to total
        }

        // if word is in the dictionary
        if (correct) {
            mSoundPool.play(mSoundCorrect, mVolume, mVolume, 1, 0, 1f);    // chime when correct
            adapter.add(guess);         // add to list
//            Log.e("word", String.valueOf(tile1));

            // set unused tiles to be blank
            for (int small = 0; small < 9; small++) {           // go through entire list
//                Log.e("tile # ", Integer.toString(small));
//                Log.e("selected? ", allTilesInt[largeTile-1][small] == 0 ? "no" : "yes");
                if (allTilesInt[largeTile-1][small] == 0) {     // if the tile was not selected
                    buttonList[largeTile-1][small].setText(""); // remove the letter from the button
                }
            }

        // if word was not in the dictionary
        } else {
            mSoundPool.play(mSoundIncorrect, mVolume, mVolume, 1, 0, 1f);   // quack when incorrect
        }
    }

    /**
     * Check if the phase 2 word is correct.
     */
    public void phaseTwoCheck() {
        String guess = phaseTwoAnswer.toString();
        int length = guess.length();

        // check if valid word
        if (length >= 3) {                  // check if the length of the word is >= 3
            if (dictionary.contains(guess.toLowerCase())) {       // check if the dictionary contains the word
                mSoundPool.play(mSoundCorrect, mVolume, mVolume, 1, 0, 1f);    // chime when correct
                // set amount of points
                int bonus = 0;
                for (char c : guess.toCharArray()) {
                    if (c == 'E' || c == 'A' || c == 'I' || c == 'O' || c == 'N' || c == 'R' || c == 'T' || c == 'L' || c == 'S') {
                        pointsScore += 1;   // bonus for x, y, or z in word
                    } else if (c == 'D' || c == 'G') {
                        pointsScore += 2;
                    } else if (c == 'B' || c == 'C' || c == 'M' || c == 'P') {
                        pointsScore += 3;
                    } else if (c == 'F' || c == 'H' || c == 'V' || c == 'W' || c == 'Y') {
                        pointsScore += 4;
                    } else if (c == 'K') {
                        pointsScore += 5;
                    } else if (c == 'J' || c == 'X') {
                        pointsScore += 8;
                    } else {
                        pointsScore += 10;
                    }
                }
                if (length == 9 && phase == 1) bonus += length;         // bonus for 9 letter word during phase 1
                pointsScore += (length + bonus) * 2;                    // calculate total score
                scoreBoard.setText(""+pointsScore);                     // set scoreboard to total
                timer.cancel();
                timer.onFinish();
            }
        } else {
            mSoundPool.play(mSoundIncorrect, mVolume, mVolume, 1, 0, 1f);   // quack when incorrect
            pointsScore -= 3;   // penalize for submitting an incorrect word
            scoreBoard.setText(""+pointsScore);                     // set scoreboard to total
        }
    }

    /**
     * Shuffle the word pulled from the word bank
     * @param text word from the word bank to be shuffled
     * @return the shuffled string
     */
    public static String shuffle(String text) {
        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = (int)(Math.random() * characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    public void phaseTwo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.phase_two, null);     // create view for custom dialog
        Button phaseTwoGo = (Button) dialogView.findViewById(R.id.phase_two_go);
        builder.setCancelable(false);
        builder.setView(dialogView);    // set view to phase two explanation screen layout
        phaseTwoDialog = builder.show();
        phaseTwoGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phaseTwoDialog.dismiss();
                startMiniTimer();   // 10 second countdown for picking word
            }
        });
    }

    public void drawNewBoards() {
        // word to draw
        int lengthString = phaseTwoString.length();
        if (lengthString < 9) {
            for (int i = 0; i < 9 - lengthString; i++) {
                phaseTwoString.append(" ");
            }
        }
        char[] playerWord = phaseTwoString.toString().toCharArray();

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View thiccButtons = inflater.inflate(R.layout.big_buttons, null);     // create view for custom dialog
        Button finalSubmission = (Button) rootView.findViewById(R.id.B10);
        finalSubmission.setVisibility(View.VISIBLE);

        for (int large = 0; large < 9; large++) {                   // intitialize each 1/9 board
//            View thiccButt = rootView.findViewById(mLargeIds[large]);   // get view for inner board
            final Button inner = (Button) rootView.findViewById(mBigTileIDs[large]);     // initialize the button
            mLargeTiles[large].setView(inner);
            inner.setVisibility(View.VISIBLE);
            inner.setText(""+playerWord[large]);                    // set text of the button to be the current letter
            buttonListPhaseTwo[large] = inner;
//            Tile bigTile = mBigTiles[large];
//            mBigTiles[large].setView(inner);
//            bigTile.setLargeTileNo(large);
            inner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inner.setSelected(!inner.isSelected());
                    if (inner.isSelected()) {
                        // selected state change
                        inner.setBackgroundResource(R.drawable.tile_red);  // selected background turns blue
                        String letter = (inner.getText()).toString();
                        //                            Log.e("letter", String.valueOf(letter));
                        phaseTwoAnswer.append(letter);                    // add new letter selected to current string
                        //                            Log.e("character appended", String.valueOf(letter));
                        for (int i = 0; i < 9; i++) {
                            if (buttonListPhaseTwo[i] == inner) {
                                bigTilesInt[i] = 1;                    // set background highlighted
                            }
                        }
                    } else {
                        // de-selected state change
                        inner.setBackgroundResource(R.drawable.tile_gray);          // deselected background is grey
                        String letter = (inner.getText()).toString();               // get letter from inner tile clicked
                        //                            Log.e("letter", String.valueOf(letter));
                        phaseTwoAnswer.deleteCharAt(phaseTwoAnswer.length() - 1); // remove most recently selected
                        for (int i = 0; i < 9; i++) {
                            if (buttonListPhaseTwo[i] == inner) {
                                bigTilesInt[i] = 0;                    // deselect background highlighted
                            }
                        }
                    }
                }
            });
//            final Tile bigTile = mLargeTiles[large];                // small tile within large tile
//            bigTile.setView(inner);
//            inner.setBackgroundResource(R.drawable.tile_gray);
//            bigTile.setLargeTileNo(large);
        }

    }

    public void startMiniTimer() {
        // initialize a new CountDownTimer instance
        long millisInFuture = 10000;
        long countDownInterval = 1000;
        phaseTwoTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;

                // notifications for time
                if (millis <= 10000 && millis > 9001) {
                    Toast.makeText(getActivity(), "10", Toast.LENGTH_LONG).show();
                } else if (millis <= 9000 && millis >= 8001) {
                    Toast.makeText(getActivity(), "9", Toast.LENGTH_LONG).show();
                } else if (millis <= 8000 && millis >= 7001) {
                    Toast.makeText(getActivity(), "8", Toast.LENGTH_LONG).show();
                } else if (millis <= 7000 && millis >= 6001) {
                    Toast.makeText(getActivity(), "7", Toast.LENGTH_LONG).show();
                } else if (millis <= 6000 && millis >= 5001) {
                    Toast.makeText(getActivity(), "6", Toast.LENGTH_LONG).show();
                } else if (millis <= 5000 && millis >= 4001) {
                    Toast.makeText(getActivity(), "5", Toast.LENGTH_LONG).show();
                } else if (millis <= 4000 && millis >= 3001) {
                    Toast.makeText(getActivity(), "4", Toast.LENGTH_LONG).show();
                } else if (millis <= 3000 && millis >= 2001) {
                    Toast.makeText(getActivity(), "3", Toast.LENGTH_LONG).show();
                } else if (millis <= 2000 && millis >= 1001) {
                    Toast.makeText(getActivity(), "2", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "1", Toast.LENGTH_LONG).show();
                }
            }

            public void onFinish() {
                Toast.makeText(getActivity(), "PHASE 2", Toast.LENGTH_LONG).show();    // entering phase 2
                phase = 2;
                initTimer(rootView);
                drawNewBoards();
            }
        }.start();
    }

//    private void switchTurns() {
//        mPlayer = mPlayer == Tile.Owner.X ? Tile.Owner.O : Tile
//                .Owner.X;
//    }
//
//    /**
//     * Set owner of small tile to current player
//     * @param large index of large tile being moved
//     * @param small index of small tile being moved
//     */
//    private void makeMove(int large, int small) {
//        mLastLarge = large;
//        mLastSmall = small;
//        Tile smallTile = mSmallTiles[large][small];
//        Tile largeTile = mLargeTiles[large];
//        smallTile.setOwner(mPlayer);                // set owner of small tile to current player
//        setAvailableFromLastMove(small);
//        Tile.Owner oldWinner = largeTile.getOwner();
//        Tile.Owner winner = largeTile.findWinner(); // see if there's a winner for the board containing the small tile
//        if (winner != oldWinner) {
//            largeTile.setOwner(winner);
//        }
//        winner = mEntireBoard.findWinner();         // see if someone has won the entire board
//        mEntireBoard.setOwner(winner);
//        updateAllTiles();
//        if (winner != Tile.Owner.NEITHER) {         // if someone has won
//            ((GameActivity)getActivity()).reportWinner(winner);
//        }
//    }

    /**
     * Redraw all boards
     */
    public void restartGame() {
        initGame();
        initViews(getView());
        updateAllTiles();
        initTimer(getView());
        pointsScore = 0;
        scoreBoard.setText(""+pointsScore);

    }

    /**
     * Displays the game over dialog
     */
    public void gameOver() {
        // game over dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.game_over, null);     // create view for custom dialog
        builder.setCancelable(false);
        builder.setView(dialogView);    // set view to game over layout

        // final score and time remaining
        TextView finalScore = (TextView) dialogView.findViewById(R.id.final_score);
        finalScore.setText("Your final score is "+pointsScore);


        // restart button within dialog
        Button gameoverRestart = (Button) dialogView.findViewById(R.id.gameover_restart);
        gameoverRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverDialog.dismiss();
                ((GameActivity) getActivity()).restartGame();
            }
        });

        // main menu button within dialog
        Button gameoverMain = (Button) dialogView.findViewById(R.id.gameover_main);
        gameoverMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverDialog.dismiss();
                getActivity().finish();
            }
        });
        gameOverDialog = builder.show();
    }


//                                    // REPLACE???
////                        if (isAvailable(smallTile)) {
////                            makeMove(fLarge, fSmall);
////                            switchTurns();
////                        }
    /**
     * Sets up data structures
     */
    public void initGame() {
        Log.d("UT3", "init game");
        mEntireBoard = new Tile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new Tile(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        // If the player moves first, set which spots are available
        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastSmall);
    }

    /**
     * Clears the available list and then marks unoccupied tiles
     * at the available large board as available
     * @param small
     */
    private void setAvailableFromLastMove(int small) {
        clearAvailable();
        // Make all the tiles at the destination available
        if (small != -1) {
            for (int dest = 0; dest < 9; dest++) {
                Tile tile = mSmallTiles[small][dest];
                if (tile.getOwner() == Tile.Owner.NEITHER)
                    addAvailable(tile);
            }
        }
        // If there were none available, make all squares available
        if (mAvailable.isEmpty()) {
            setAllAvailable();
        }
    }

    /**
     * If there are no possible moves, mark all unoccupied tiles as available
     */
    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                Tile tile = mSmallTiles[large][small];
                if (tile.getOwner() == Tile.Owner.NEITHER)
                    addAvailable(tile);
            }
        }
    }

    /**
     * Updates the state of the tile representing the overall board,
     * then each of the large tiles for the first 9 level boards,
     * then the small tiles that contain the letters
     */
    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large].updateDrawableState();
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateDrawableState();
            }
        }
    }

    /**
     * Create a string containing the state of the game.
     * @return
     */
    public String getState() {
        StringBuilder builder = new StringBuilder();
        builder.append(mLastLarge);
        builder.append(',');
        builder.append(mLastSmall);
        builder.append(',');
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                builder.append(mSmallTiles[large][small].getOwner().name());
                builder.append(',');
            }
        }
        return builder.toString();
    }

    /**
     * Restore the state of the game from the given string.
     * @param gameData
     */
    public void putState(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;
        mLastLarge = Integer.parseInt(fields[index++]);
        mLastSmall = Integer.parseInt(fields[index++]);
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                Tile.Owner owner = Tile.Owner.valueOf(fields[index++]);
                mSmallTiles[large][small].setOwner(owner);
            }
        }
        setAvailableFromLastMove(mLastSmall);
        updateAllTiles();
    }
}
