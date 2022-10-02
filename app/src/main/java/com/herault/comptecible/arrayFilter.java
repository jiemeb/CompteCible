package com.herault.comptecible;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.herault.comptecible.utils.FilterContainer;

import java.util.ArrayList;

public class arrayFilter extends ArrayAdapter<FilterContainer> {
    private final Context _context;
    public ArrayList<FilterContainer> _filters;
    public arrayFilter(Context context, ArrayList<FilterContainer> Filter) {
        super(context, 0, Filter);
        _context = context;
        _filters = Filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cell_grid_view, parent, false);
            viewHolder.viewName = convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.viewName.setText(getItem(position).getValue());
        TextView viewText = (TextView) convertView.findViewById(R.id.textview);
        viewText.setText(_filters.get(position).getValue());
        //   viewText.setBackground(_filters.get(position).getColor());
        String color = _filters.get(position).getColor();
        switch (color) {
            case "_blue_":
                viewText.setTextColor(Color.WHITE);
                viewText.setBackgroundResource(R.drawable.am_buttonshape_blue);
                break;
            case "_red_":
                viewText.setTextColor(Color.BLACK);
                viewText.setBackgroundResource(R.drawable.am_buttonshape_red);
                break;
            case "_yellow_":
                viewText.setTextColor(Color.BLACK);
                viewText.setBackgroundResource(R.drawable.am_buttonshape_yellow);
                break;
            case "_black_":
                viewText.setTextColor(Color.WHITE);
                viewText.setBackgroundResource(R.drawable.am_buttonshape_black);
                break;


            default:
                viewText.setTextColor(Color.BLACK);
                viewText.setBackgroundResource(R.drawable.am_buttonshape_white);
                break;


        }
        return convertView;
    }

    private static class ViewHolder {
        TextView viewName;
    }

}

