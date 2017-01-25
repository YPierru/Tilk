package com.tilk.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YPierru on 24/01/2017.
 */

public class ProfilTilkeur implements Serializable{

    private String pseudo;
    private String departement;
    private int nbAdults;
    private int nbKids;
    private ArrayList<FriendTilkeur> listFriends;

    public ProfilTilkeur(String pseudo, String departement, int nbAdults, int nbKids) {
        this.pseudo = pseudo;
        this.departement = departement;
        this.nbAdults = nbAdults;
        this.nbKids = nbKids;
        listFriends = new ArrayList<>();
    }

    public ProfilTilkeur(String pseudo){
        this.pseudo=pseudo;
        listFriends = new ArrayList<>();
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public int getNbAdults() {
        return nbAdults;
    }

    public void setNbAdults(int nbAdults) {
        this.nbAdults = nbAdults;
    }

    public int getNbKids() {
        return nbKids;
    }

    public void setNbKids(int nbKids) {
        this.nbKids = nbKids;
    }

    public void setListFriends(ArrayList<FriendTilkeur> listFriends) {
        this.listFriends = listFriends;
    }

    public ArrayList<FriendTilkeur> getListFriends() {
        return listFriends;
    }

    public String getJson(int userId){

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{ \"user_id\" : \""+userId+"\"");
        jsonBuilder.append(", ");
        jsonBuilder.append("\"pseudo\" : \""+pseudo+"\"");
        jsonBuilder.append(", ");
        jsonBuilder.append("\"departement\" : \""+departement+"\"");
        jsonBuilder.append(", ");
        jsonBuilder.append("\"nbAdults\" : \""+nbAdults+"\"");
        jsonBuilder.append(", ");
        jsonBuilder.append("\"nbKids\" : \""+nbKids+"\" }");

        return jsonBuilder.toString();
    }
}
