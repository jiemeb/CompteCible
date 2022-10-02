package com.herault.comptecible.utils;

public class FilterContainer {
    private String _color;
    private String _value;


    public  FilterContainer(String color,String value)
    {
        _color= color ;
        _value= value ;
    }
    public void setColor (String color) {
        this._color=color ;
    }
    public void setValue (String value) {
        this._value=value ;
    }
    public String getValue (){
        return _value;
    }
    public String getColor (){
        return _color;
    }
    public String [] getFilterContainer () {

        return new String [] {_color,_value} ;
    }
}
