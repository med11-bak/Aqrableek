package com.school.aqrableek;

public class Post {
    String ID;
    String nomUser;
    String picture;
    String time;
    String content;
   public int year;
    public int month;
    public int day;
    public  int heur;
    public int min;
    public String replyContent;
    public String fulltime;
    public String replyingto;
    static public String timepost;
    public String image;
    public String audio;
    public String country;
    public String ville;
    public String district;
    public String profrssion;




    public Post() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfrssion() {
        return profrssion;
    }

    public void setProfrssion(String profrssion) {
        this.profrssion = profrssion;
    }
}
