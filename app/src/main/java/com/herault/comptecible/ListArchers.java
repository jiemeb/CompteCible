package com.herault.comptecible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListArchers extends ArrayAdapter<String> {
    private final Context _context;
    public ArrayList<String> _archers;
    public ListArchers(Context context, ArrayList<String> Archers) {
        super(context, 0, Archers);
        _context = context;
        _archers = Archers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.archer, parent, false);
            viewHolder.viewName = convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.viewName.setText(getItem(position));
        //   EditText viewText = (EditText) convertView.findViewById(R.id.archer_id);
        //  viewText.setText(_archers.get(position).getIndex());
        return convertView;
    }

    private static class ViewHolder {
        TextView viewName;
    }

}

