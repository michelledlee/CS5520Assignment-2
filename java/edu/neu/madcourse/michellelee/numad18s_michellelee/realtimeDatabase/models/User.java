package edu.neu.madcourse.michellelee.numad18s_michellelee.realtimeDatabase.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class User {

    public String username;
    public String score;
    public String datePlayed;
    public String word;
    public String wordScore;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String score, String date, String word, String wordScore){
        this.username = username;
        this.score = score;
        this.datePlayed = date;
        this.word = word;
        this.wordScore = wordScore;
    }

    public String getUsername() {
        return username;
    }

    public String getScore() {
        return score;
    }

    public String getDatePlayed() {
        return datePlayed;
    }

    public String getWord() {
        return word;
    }

    public String getWordScore() {
        return wordScore;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDatePlayed (String date) {
        this.datePlayed = date;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setWordScore(String wordScore) {
        this.wordScore = wordScore;
    }
}