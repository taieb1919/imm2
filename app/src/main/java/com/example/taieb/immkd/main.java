package com.example.taieb.immkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.taieb.immkd.parsing.parsingxls;
import com.example.taieb.immkd.util.SystemUiHider;
import com.javacodegeeks.androidqrcodeexample.R;

import java.io.InputStream;

import static android.view.View.OnClickListener;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class main extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        parsingxls fileparser = new parsingxls();
        InputStream myInput=getResources().openRawResource(R.raw.a);
        fileparser.readExcelFile(this, myInput);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        /*
        *
        *           boucle FOR creation des
        *           boutons de chaque modele
        *
        *
        * */
        for (  int i= 0; i < fileparser.getALLSHEET().size(); i++) {
            String modele_name= fileparser.getALLSHEET().get(i).getName();
            final Button myButton = new Button(this);
            myButton.setText(modele_name);
            myButton.setId(i);
            myButton.setGravity(Gravity.CENTER);
            myButton.setPadding(0,8,0,8);
            LinearLayout ll = (LinearLayout) findViewById(R.id.buttoncontainer);
            ll.addView(myButton);
            myButton.setOnClickListener(new OnClickListener() {

                                            public void onClick(View v) {
                                                main.this.StartNEWActivity(myButton.getId(),myButton.getText().toString());
                                            }

                                        }
            );
        }
        /*
        *
        *               END FOR LOOP
        *
        *
        * */
    }


    public  void StartNEWActivity(int id,String modelename){
        Intent myIntent = new Intent(this, start.class);
        myIntent.putExtra("id_model", id); //Optional parameters
        myIntent.putExtra("model_name", modelename);
        this.startActivity(myIntent);
}

    public void scanQR(View v) {

     }


}
