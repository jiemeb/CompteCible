package com.herault.comptecible;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.herault.comptecible.utils.Stockage;

public class Activity_apropos extends AppCompatActivity {
    Stockage stock ;
    int index = 0 ;
 int[] pageId = { R.drawable.doc_apropos,
            R.drawable.doc_page1 ,
            R.drawable.doc_page2 ,
            R.drawable.doc_page3 ,
            R.drawable.doc_page4 ,
            R.drawable.doc_page5 ,
            R.drawable.doc_page7,
            R.drawable.doc_page7 ,
            R.drawable.doc_page8 ,
            R.drawable.doc_page9 ,

    } ;
    ImageView docView ;
    Button bp_next;
    Button bp_previous;
    private Context _context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String version = "CompteCible version : ";
        _context = this ;
        setContentView(R.layout.activity_apropos);
        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);

        version += stock.getValue("version");

        TextView Version = findViewById(R.id.app_version);
        Version.setText(version);

        bp_next = findViewById(R.id.bNextArcher);

        bp_next.setOnClickListener(v -> {

        index ++ ;
        if  (index >= pageId.length)
                index = pageId.length -1 ;
            docView.setBackground(ContextCompat.getDrawable(_context,pageId[index]));

        });



        bp_previous = findViewById(R.id.bPreviousArcher);
        bp_previous.setOnClickListener(v -> {
            index -- ;
            if  (index <= 0)
                index = 0 ;
            docView.setBackground(ContextCompat.getDrawable(_context,pageId[index]));
        });


        docView =  findViewById(R.id.apropos_doc);
    //    WebView  = (WebView) findViewById(R.id.apropos_webview);
        docView.setBackground(ContextCompat.getDrawable(_context,pageId[index]));

        Button bpCibleG = findViewById(R.id.CibleG);

        bpCibleG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK) ;
                Activity_apropos.this.finish();
            }
        });

        Button bpCibleD = findViewById(R.id.CibleD);

        bpCibleD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK) ;
                Activity_apropos.this.finish();
            }
        });

    }
/* Override some function like exit     */

public void onBackPressed() {
setResult(RESULT_OK) ;
finish();
}

    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        stock.openDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stock.closeDB();

    }

}
