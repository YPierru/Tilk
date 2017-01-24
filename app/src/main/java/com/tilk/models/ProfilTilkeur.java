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
    private ArrayList<String> listPseudoFriends;

    public ProfilTilkeur(String pseudo, String departement, int nbAdults, int nbKids) {
        this.pseudo = pseudo;
        this.departement = departement;
        this.nbAdults = nbAdults;
        this.nbKids = nbKids;
    }

    public ProfilTilkeur(String pseudo){
        this.pseudo=pseudo;
        listPseudoFriends = new ArrayList<>();
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

    public ArrayList<String> getListPseudoFriends() {
        return listPseudoFriends;
    }

    public void setListPseudoFriends(ArrayList<String> listPseudoFriends) {
        this.listPseudoFriends = listPseudoFriends;
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
