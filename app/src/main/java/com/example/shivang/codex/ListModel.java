package com.example.shivang.codex;

/**
 * Created by shivang on 05-03-2017.
 */

public class ListModel {
    private String name;
    private String number;
    /***********set methods*********/

    public void setName(String name){
        this.name=name;
    }
    public void setNumber(String number){
        this.number=number;
    }

    /*******get methods*********/

    public String getName()
    {
        return this.name;
    }

    public String getNumber()
    {
        return this.number;
    }
}
