package com.example.project.models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.example.project.CherryApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Profile {

    private class Profiles {
        private class EmbeddedArray {
            public List<Profile> users;
        }
        public EmbeddedArray _embedded;
    }

    private class Links {
        private class Link {
            public URL href;
        }
        public Link self;
    }

    public String uuid;

    //Profile elements
    public String email;
    public String username;
    public String firstname;
    public String lastname;
    public int age;
    public Date birthday;
    public com.example.project.models.Gender gender;
    public String password;
    public String introduction;
    public Bitmap image;
    public AstrologicalSign astrologicalSign;

    public Links _links;

    public static Profile parse(String json) {
        Gson gson = new GsonBuilder().create();
        Profile p = gson.fromJson(json, Profile.class);
        p.uuid = p._links.self.href.getFile().split("/")[2];
        return p;
    }

    public static List<Profile> parseArray(String json) {
        Gson gson = new GsonBuilder().create();
        Profiles profiles = gson.fromJson(json, Profiles.class);

        for (Profile p : profiles._embedded.users) {
//            m.fromuser = m.profiles.fromuser;
//            m.match = m._embedded._match;
//            p.uuid = p._links.self.href.getFile().split("/")[2];
        }

        return profiles._embedded.users;
    }

    public String format(CherryApplication application) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        return gson.toJson(this);
    }

    public Profile() {}

    public Profile(String Username,String Firstname, String Lastname, int Age, Date Birthday, Gender Gender, String Password, String Introduction, Bitmap Image){
        this.username = Username;
        this.firstname = Firstname;
        this.lastname = Lastname;
        this.age = Age;
        this.gender = Gender;
        this.password = Password;
        this.introduction = Introduction;
        this.image = Image;
        this.birthday = Birthday;
        this.astrologicalSign = getAstrologicalSign(Birthday);
    }

    //Gets the user's astrological sign depending on his birthday
    public AstrologicalSign getAstrologicalSign(Date Birthday){
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(Birthday);
        switch (birthday.get(Calendar.MONTH)){
            case Calendar.JANUARY:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=19){
                    return com.example.project.models.AstrologicalSign.Capricorn;
                }else{
                    return com.example.project.models.AstrologicalSign.Aquarius;
                }
            case Calendar.FEBRUARY:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=18){
                    return com.example.project.models.AstrologicalSign.Aquarius;
                }else{
                    return com.example.project.models.AstrologicalSign.Pisces;
                }
            case Calendar.MARCH:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=20){
                    return com.example.project.models.AstrologicalSign.Pisces;
                }else{
                    return com.example.project.models.AstrologicalSign.Aries;
                }
            case Calendar.APRIL:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=19){
                    return com.example.project.models.AstrologicalSign.Aries;
                }else{
                    return com.example.project.models.AstrologicalSign.Taurus;
                }
            case Calendar.MAY:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=21){
                    return com.example.project.models.AstrologicalSign.Taurus;
                }else{
                    return com.example.project.models.AstrologicalSign.Gemini;
                }
            case Calendar.JUNE:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=20){
                    return com.example.project.models.AstrologicalSign.Gemini;
                }else{
                    return com.example.project.models.AstrologicalSign.Cancer;
                }
            case Calendar.JULY:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=22){
                    return com.example.project.models.AstrologicalSign.Cancer;
                }else{
                    return com.example.project.models.AstrologicalSign.Leo;
                }
            case Calendar.AUGUST:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=22){
                    return com.example.project.models.AstrologicalSign.Leo;
                }else{
                    return com.example.project.models.AstrologicalSign.Virgo;
                }
            case Calendar.SEPTEMBER:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=22){
                    return com.example.project.models.AstrologicalSign.Virgo;
                }else{
                    return com.example.project.models.AstrologicalSign.Libra;
                }
            case Calendar.OCTOBER:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=22){
                    return com.example.project.models.AstrologicalSign.Libra;
                }else{
                    return com.example.project.models.AstrologicalSign.Scorpio;
                }
            case Calendar.NOVEMBER:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=21){
                    return com.example.project.models.AstrologicalSign.Scorpio;
                }else{
                    return com.example.project.models.AstrologicalSign.Sagittarius;
                }
            case Calendar.DECEMBER:
                if(birthday.get(Calendar.DAY_OF_MONTH)<=21){
                    return com.example.project.models.AstrologicalSign.Sagittarius;
                }else{
                    return com.example.project.models.AstrologicalSign.Capricorn;
                }
        }
     return null;
    }

    public String getAge() {
        long ageInMillis = new Date().getTime() - birthday.getTime();


        Calendar bd = Calendar.getInstance();
        bd.setTime(birthday);

        Integer ag =Calendar.getInstance().get(Calendar.YEAR)-bd.get(Calendar.YEAR);
        if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < bd.get(Calendar.DAY_OF_YEAR)){
            ag--;
        }
        return Integer.toString(ag);
    }


    @NonNull
    @Override
    public String toString() {
        return this.firstname + " " + this.lastname + " " + this.astrologicalSign;
    }
}
