package com.herault.comptecible;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListNote extends ArrayAdapter<Note> {
    boolean IgotIt = false;
    private final Context _context;
    public ArrayList<String> _note;
    public ListNote(Context context, ArrayList<Note> ANote) {
        super(context,R.layout.note, ANote);
        _context = context;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);
        //       final ListNote.ViewHolder viewHolder; // view lookup cache stored in tag
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            //          viewHolder = new ListNote.ViewHolder();
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.note, parent, false);

            viewHolder.idNote = convertView.findViewById(R.id.idNote);
            viewHolder.note = convertView.findViewById(R.id.note);

            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            //           viewHolder = (ListNote.ViewHolder) convertView.getTag();
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.note.setText(note.getNote());
        viewHolder.idNote.setText(Long.toString(note.getIdNote()));

        if ((position & 1) != 1) {
            viewHolder.note.setBackgroundColor(0xFFC5BFA8);
            viewHolder.idNote.setBackgroundColor(0xFFC5BFA8);
        } else {
            viewHolder.note.setBackgroundColor(Color.WHITE);
            viewHolder.idNote.setBackgroundColor(Color.WHITE);
        }


/*      viewHolder.note.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {
                 String snote = viewHolder.note.getText().toString();
                 String sidNote= viewHolder.idNote.getText().toString();
             }

             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }
         }); */

 /*       viewHolder.note.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    switch (v.getId()) {
                        case R.id.note:
                            IgotIt = false;
                            Toast.makeText(_context, "get value", Toast.LENGTH_LONG).show();
                            String snote = viewHolder.note.getText().toString();
                            String sidNote = viewHolder.idNote.getText().toString();
                            // Validate EditText1
                            break;
                    }

                } else {
                    switch (v.getId()) {
                        case R.id.note:
                            IgotIt = false;
                            Toast.makeText(_context, "Focus", Toast.LENGTH_LONG).show();
                            // Validate EditText1
                            break;
                    }
                }
            }
        });*/


        return convertView;
    }


    private static class ViewHolder {
        TextView idNote;
        TextView note;
    }

}



