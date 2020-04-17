package com.herault.comptecible;

public class Resultat_archer {
    public String name = "";
    public long value;
    public double x;
    public double y;
    public String information = "";

    public Resultat_archer() {
        value = 0;
        x = 0.;
        y = 0.;
    }

    Resultat_archer(long Value, double X, double Y) {
        value = Value;
        x = X;
        y = Y;

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

    public long getValue() {
        return value;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
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
}
