package com.example.project.models;

public enum Gender {
    Female(1),Male(2),Other(3);

    private int genderId;

    //Create a gender with a specific gender id
    Gender(int genderId){
        this.genderId=genderId;
    }

    //Return the specific gender ID
    public int getGenderId(){
        return genderId;
    }
}

