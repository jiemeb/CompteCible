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
        switch (arrowName)
        {
            case 0 :
                color = (R.color.arrowDefault) ;
                break;
            case  1 :
                color = (R.color.arrowOne) ;
                break;
            case  2 :
                color = (R.color.arrowTwo) ;
                break;
            case  3 :
                color = (R.color.arrowThree) ;
                break;
            case  4 :
                color = (R.color.arrowFour) ;
                break;
            case  5 :
                color = (R.color.arrowFive) ;
                break;
            case  6 :
                color = (R.color.arrowSix) ;
                break;
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
