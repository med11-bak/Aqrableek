package com.school.aqrableek;

public class User {
    public String nomUser,tel,pays,ville,district,Ecole,password,image;

    public User() {
    }

    public User(String nomUser,String tel, String pays, String ville, String ecole, String password,String image,String dist) {
        this.nomUser = nomUser;
        this.pays = pays;
        this.ville = ville;
        Ecole = ecole;
        this.password = password;
        this.tel = tel;
        this.image = image;
        district = dist;
    }
}
