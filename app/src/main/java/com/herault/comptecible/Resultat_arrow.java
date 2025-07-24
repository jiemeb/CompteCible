package com.herault.comptecible;

public class Resultat_arrow {

    public long value;
    public double sumX;
    public double sumY;
    public int nbValue;

    public int arrowName = 0 ;


    public Resultat_arrow() {
        value = 0;
        sumX = 0.;
        sumY = 0.;
        arrowName = 0 ;
    }

     void addArrowMoy(double X,double Y, int arrowName) {
        sumX += X;
        sumY += Y;
        nbValue += 1 ;
        this.arrowName = arrowName;
    }


    Resultat_arrow(int Name, long Value, double X, double Y) {
        arrowName = Name;
        value = Value;
        sumX = X;
        sumY = Y;
        nbValue = 1 ;


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

    public double getMoyenX() {
        return sumX /nbValue;
    }

    public double getMoyenY() {
        return sumY /nbValue;
    }

    public int getArrowName() {
        return this.arrowName;
    }



}
