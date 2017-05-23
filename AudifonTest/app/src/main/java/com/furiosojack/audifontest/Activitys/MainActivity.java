package com.furiosojack.audifontest.Activitys;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.furiosojack.audifontest.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Metodo que me llama a la siguiente actividad en este caso la de calibracion
     */
    public void siguienteActividad(View param){
        final Intent siguiente = new Intent(this,Calibracion.class);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Atencion!!!");
        dialogo.setMessage("1. Ponte los Dos Auriculares. \n2. Escucha y Lee todo atentamente.");
        dialogo.setPositiveButton("Visto", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(siguiente);
            }
        });
        dialogo.create();
        dialogo.show();


    }



    @Override
    protected void onResume() {
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        Log.i(TAG,"Activity resumida");
        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.i(TAG,"Activity Pausada");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
