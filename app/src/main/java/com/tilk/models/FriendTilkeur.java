package com.tilk.models;

import java.io.Serializable;

/**
 * Created by YPierru on 25/01/2017.
 */

public class FriendTilkeur implements Serializable{

    private int friendId;
    private String pseudo;
    private int consoYear;

    public FriendTilkeur(int friendId, String pseudo, int consoYear) {
        this.friendId = friendId;
        this.pseudo = pseudo;
        this.consoYear = consoYear;
    }

    public int getFriendId() {
        return friendId;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getConsoYear() {
        return consoYear;
    }

    @Override
    public String toString() {
        return "FriendTilkeur{" +
                "friendId=" + friendId +
                ", pseudo='" + pseudo + '\'' +
                ", consoYear=" + consoYear +
                '}';
    }
}
