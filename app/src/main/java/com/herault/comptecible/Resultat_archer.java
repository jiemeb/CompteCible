package com.herault.comptecible;

public class Resultat_archer {
    public String name = "";
    public long value;
    public double x;
    public double y;
    public int dixPlus;
    public String information = "";
    public int arrowName = 0 ;


    public Resultat_archer() {
        value = 0;
        x = 0.;
        y = 0.;
        arrowName = 0 ;
    }

    Resultat_archer(long Value, double X, double Y, int dixPlusR) {
        value = Value;
        x = X;
        y = Y;
        dixPlus=dixPlusR;

    }

    Resultat_archer(long Value, double X, double Y, int dixPlusR, int arrowName) {
        value = Value;
        x = X;
        y = Y;
        dixPlus=dixPlusR;
        this.arrowName = arrowName ;
    }

    Resultat_archer(long Value, double X, double Y, String Information) {
        value = Value;
        x = X;
        y = Y;
        information = Information;

    }

    Resultat_archer(String Name, long Value, double X, double Y) {
        name = Name;
        value = Value;
        x = X;
        y = Y;

    }
    public int getColorArrow ()
    {
        int color = 0;
        if(arrowName == 0)
           color = (R.color.arrowDefault) ;
        else {
            color = switch ((arrowName % 6) + 1) {
                case 1 -> (R.color.arrowOne);
                case 2 -> (R.color.arrowTwo);
                case 3 -> (R.color.arrowThree);
                case 4 -> (R.color.arrowFour);
                case 5 -> (R.color.arrowFive);
                case 6 -> (R.color.arrowSix);
                default -> color;
            };
        }
        return (color);
    }
    public long getValue() {
        return value;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getArrowName() {
        return this.arrowName;
    }

    public String getName() {
        return this.name;
    }

    public String getInformation() {
        return information;
    }

    public void SetName(String Name) {
        name = Name;
    }

    public void SetInformation(String Information) {
        information = Information;
    }
    public boolean isDixPlus() {
        return (dixPlus == 0 ? false:true) ;
    }
}
