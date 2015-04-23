package com.example.taieb.immkd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.example.taieb.immkd.parsing.Article;
import com.example.taieb.immkd.parsing.parsingxls;
import com.example.taieb.immkd.util.SystemUiHider;
import com.javacodegeeks.androidqrcodeexample.R;

import java.io.InputStream;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ScanScreen extends Activity {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";



    private int idmodele;
    private static final int QR_SEARCH = 1;
    private static final int PART_NUM_SEARCH = 2;
    private int SearchMode=-1;
    private parsingxls fileparser;


    //QRCODE search

    private void scanQR() {

                try {

            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(ScanScreen.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }

    }
/*
*                                          BLOC SEARCH BY QRCODE
*
*
* */
            private void sEARCHQRCode(String contents)  {
                final ProgressDialog dialog =ProgressDialog.show(this, "Searching", "Please wait...", true);
                new Thread()
                {
                    public void run()
                    {
                        try
                        { sleep(4000);
                        }
                        catch (Exception e)
                        {
                        }
                        dialog.dismiss();
                    }
                }.start();

                LinearLayout rl =(LinearLayout) this.findViewById(R.id.resultview);
                int searchresult=-1;
                int sUbstructindice=12;
                String ValueToSearch ="";



                View myView = findViewById(R.id.containersearch);
                if(myView!=null) {
                    ViewGroup parent = (ViewGroup) myView.getParent();
                    parent.removeView(myView);
                }

                do {
                    ValueToSearch = contents.substring(0,sUbstructindice);
                    searchresult=fileparser.findRow(this.idmodele,ValueToSearch);
                    sUbstructindice--;

                }while(sUbstructindice>6 && searchresult<1);

                if(searchresult<1) {
                    View itemInfo1 = getLayoutInflater().inflate(R.layout.layooutnotfound, rl, true);
           //         TextView txtsearch = (TextView) findViewById(R.id.textToSearch);
           //         txtsearch.setText("Search result for : "+ValueToSearch);
                }else {

                    Article art= fileparser.getallrows(idmodele, searchresult, ValueToSearch);

                    View itemInfo1 = getLayoutInflater().inflate(R.layout.layoutsearchok, rl, true);

              //      TextView txtsearch = (TextView) findViewById(R.id.textToSearch);
              //      txtsearch.setText("Search result for : "+ValueToSearch);
                    TextView txtcase = (TextView) findViewById(R.id.CASETEXT);
                    txtcase.setText(art.getCASE());

                    TextView txtbox = (TextView) findViewById(R.id.BOXTEXT);
                    txtbox.setText(art.getListe_BOX().get(0));

                    TextView txtPartName = (TextView) findViewById(R.id.PARTNAMETEXT);
                    txtPartName.setText(art.getPart_Name());
                    TableLayout TL=(TableLayout)findViewById(R.id.tableresultat);
                    TableRow TRow=null;
        /*
        *                   LOOP FOR :Creation contenu TableLayout
        * */
                    for (int j=0;j<art.getListe_Station().size();j++)
                    {

                        TextView textSTAT;
                        textSTAT = new TextView(this);

                        TextView textQTY;
                        textQTY = new TextView(this);

                        TRow =new TableRow(this);
                        TRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT,1f));
                        TRow.setGravity(Gravity.CENTER_HORIZONTAL);

                        textSTAT.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT, 1f));
                        textSTAT.setBackgroundResource(R.drawable.cell_shape);
                        textSTAT.setPadding(2, 2, 2, 2);
                        textSTAT.setGravity(Gravity.CENTER_HORIZONTAL);
                        textSTAT.setTextColor(Color.WHITE);
                        textSTAT.setTextSize(20);
                        textSTAT.setTypeface(null, Typeface.BOLD);
                        textSTAT.setText(art.getListe_Station().get(j).getStat_Name());



                        textQTY.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT,1f));
                        textQTY.setBackgroundResource(R.drawable.cell_shape);
                        textQTY.setPadding(2, 2, 2, 2);
                        textQTY.setGravity(Gravity.CENTER_HORIZONTAL);
                        textQTY.setTextColor(Color.WHITE);
                        textQTY.setTextSize(20);
                        textQTY.setTypeface(null, Typeface.BOLD);
                        textQTY.setText(art.getListe_Station().get(j).getQTY());


                        TRow.addView(textSTAT);

                        TRow.addView(textQTY);

                        TL.addView(TRow,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
        /*                              END
        *                   LOOP FOR :Creation contenu TableLayout
        * */

                }

            }

    /*
*                                          END
*                                   BLOC SEARCH BY QRCODE
*
* */






/*
*                                          BLOC AUTOMATIC QRCODE READER(ZXING)
*
*
* */

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });


        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
     //           String format = intent.getStringExtra("SCAN_RESULT_FORMAT");


                sEARCHQRCode(contents);




                // Handle successful scan
/*
                Toast toast = makeText(this, "Content:" + contents + " Format:" + format, LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
     */       } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = makeText(this, "Scan was Cancelled!", LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }

/*
*                                                   END
*                                    BLOC AUTOMATIC QRCODE READER(ZXING)
*
* */




    /*
    *
    *
    *                               SEARCH BY PARTNUM ENTERED IN EDITTEXT
    *
    *
    * */
public void simplesearch(View v) {

    final ProgressDialog dialog =ProgressDialog.show(this, "Searching", "Please wait...", true);
    new Thread()
    {
        public void run()
        {
            try
            { sleep(1000);
            }
            catch (Exception e)
            {
            }
            dialog.dismiss();
        }
    }.start();

    LinearLayout rl =(LinearLayout) this.findViewById(R.id.resultview);

    EditText mEdit = (EditText) findViewById(R.id.editText);

    String ValueToSearch= mEdit.getText().toString().trim();

    InputMethodManager imm = (InputMethodManager)getSystemService(
            Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);

    int searchresult=fileparser.findRow(this.idmodele,ValueToSearch);


    View myView = findViewById(R.id.containersearch);
if(myView!=null) {
    ViewGroup parent = (ViewGroup) myView.getParent();
    parent.removeView(myView);
}


    if(searchresult<1) {
        View itemInfo1 = getLayoutInflater().inflate(R.layout.layooutnotfound, rl, true);
        TextView txtsearch = (TextView) findViewById(R.id.textToSearch);
        txtsearch.setText("Search result for : "+ValueToSearch);
    }else {

        Article art= fileparser.getallrows(idmodele, searchresult, ValueToSearch);

        View itemInfo1 = getLayoutInflater().inflate(R.layout.layoutsearchok, rl, true);

        TextView txtsearch = (TextView) findViewById(R.id.textToSearch);
        txtsearch.setText("Search result for : "+ValueToSearch);
        TextView txtcase = (TextView) findViewById(R.id.CASETEXT);
        txtcase.setText(art.getCASE());

        TextView txtbox = (TextView) findViewById(R.id.BOXTEXT);
        txtbox.setText(art.getListe_BOX().get(0));

        TextView txtPartName = (TextView) findViewById(R.id.PARTNAMETEXT);
        txtPartName.setText(art.getPart_Name());
        TableLayout TL=(TableLayout)findViewById(R.id.tableresultat);
        TableRow TRow=null;
        /*
        *                   LOOP FOR :Creation contenu TableLayout
        * */
        for (int j=0;j<art.getListe_Station().size();j++)
        {

            TextView textSTAT;
            textSTAT = new TextView(this);

            TextView textQTY;
            textQTY = new TextView(this);

            TRow =new TableRow(this);
            TRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT,1f));
            TRow.setGravity(Gravity.CENTER_HORIZONTAL);

            textSTAT.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT, 1f));
            textSTAT.setBackgroundResource(R.drawable.cell_shape);
            textSTAT.setPadding(2, 2, 2, 2);
            textSTAT.setGravity(Gravity.CENTER_HORIZONTAL);
            textSTAT.setTextColor(Color.WHITE);
            textSTAT.setTextSize(20);
            textSTAT.setTypeface(null, Typeface.BOLD);
            textSTAT.setText(art.getListe_Station().get(j).getStat_Name());



            textQTY.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT,1f));
            textQTY.setBackgroundResource(R.drawable.cell_shape);
            textQTY.setPadding(2, 2, 2, 2);
            textQTY.setGravity(Gravity.CENTER_HORIZONTAL);
            textQTY.setTextColor(Color.WHITE);
            textQTY.setTextSize(20);
            textQTY.setTypeface(null, Typeface.BOLD);
            textQTY.setText(art.getListe_Station().get(j).getQTY());


            TRow.addView(textSTAT);

            TRow.addView(textQTY);

            TL.addView(TRow,new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
        /*                              END
        *                   LOOP FOR :Creation contenu TableLayout
        * */

    }
}
/*
*
*                                         END
*                               SEARCH BY PARTNUM ENTERED IN EDITTEXT
*
*
*
*
* */











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        this.idmodele = intent.getIntExtra("id_model", -1);
        this.SearchMode=intent.getIntExtra("searchmode", -1);

        fileparser = new parsingxls();
        InputStream myInput=getResources().openRawResource(R.raw.a);
        fileparser.readExcelFile(this, myInput);


        if (SearchMode == QR_SEARCH) {
            setContentView(R.layout.activity_scan_screen);
            scanQR();
        }
        else if (SearchMode == PART_NUM_SEARCH) {

            setContentView(R.layout.menusearch);
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        simplesearch(ScanScreen.this.getCurrentFocus());
                        handled = true;
                    }
                    return handled;
                }
            });

           //simplesearch();
        }
    }



}
