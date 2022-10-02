package com.herault.comptecible.utils;




import java.util.ArrayList;
import java.util.List;

public class FiltersContainer {


    private ArrayList <FilterContainer> lFilterContainer = new ArrayList<>();

    public FiltersContainer(String initValue)
    {
    deserialize(initValue);
    }

    public String [] getFilter ()
    {
        String [] retour ;
        retour = new String [lFilterContainer.size()];

            for (int i = 0; i < retour.length; i++) {
               retour [i]= lFilterContainer.get(i).getValue() ;
            }

        return retour ;
    }

    public String serialize() {

        String result = "";
        for (int i = 0; i < lFilterContainer.size();i++)
        {
            if(result.isEmpty()) {
                result = lFilterContainer.get(i).getColor();
            }
            else {
                result += " "+ lFilterContainer.get(i).getColor();
            }
                result += " "+lFilterContainer.get(i).getValue();
    }

    return result;
}

    public void  deserialize (String initiate)
     {
     String [] init =  initiate.split("\\s+");
     if (!lFilterContainer.isEmpty())
            lFilterContainer.clear();
     int length = (init.length/2 )*2 ;
     for (int i=0; i < length  ; i+=2)
     {
         lFilterContainer.add(new FilterContainer(init[i],init[i+1]));
     }
    }
    public FilterContainer get(int position) {
        if (position < lFilterContainer.size() || position > 0)
        {
            return lFilterContainer.get(position);
        }
        return(new FilterContainer("_black_","0"));
    }
    public String [] getTextview (int indice) {
        String [] result = new String[2];
       if(indice < lFilterContainer.size())
       {
        return  lFilterContainer.get(indice).getFilterContainer();
        }
       result [0] = "_black_" ;
       result [1] = "0" ;
       return result ;
    }
    public int getLength() {
        return lFilterContainer.size() ;
    }

    public int getFindValue (String value)
        {
            for (int i = 0; i < lFilterContainer.size(); i++) {
                if( lFilterContainer.get(i).getValue().equals(value) )
                    return i;
            }
            return -1;
        }
    public    boolean deleteValue(String WordToDelete) {
        int position = getFindValue(WordToDelete);
        if(position == -1)
            return false ;
        lFilterContainer.remove(position) ;
        return true;
    }
    public    boolean addValue(FilterContainer filterContainer) {
        int position = getFindValue(filterContainer.getValue());
        if(position != -1)
            return false ;
        lFilterContainer.add( filterContainer) ;
        return true;
    }
    public void clear()
    {
        lFilterContainer = new ArrayList<>();
    }

    public ArrayList <FilterContainer> getListFilterContainer()
    {
        return lFilterContainer ;
    }

}
