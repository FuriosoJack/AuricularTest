package com.furiosojack.audifontest.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.furiosojack.audifontest.Logica.ListenerReceiber;
import com.furiosojack.audifontest.ObServador.*;
import com.furiosojack.audifontest.R;

public class Calibracion extends AppCompatActivity implements ObjObservador {

    private static final String TAG = "CalibracionActivity";
    //---------------------
    //Para salir dado dos veces atraz
    public static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    //---------------------
    private IntentFilter receiverFilter;
    private MediaPlayer elsonido;
    private ListenerReceiber bloqueadodeLayout;
    private ImageView aprovadopaso1;
    private ImageView aprovadopaso2;
    private TextView textopaso2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibracion);

        //Colocarndo color al icono de informacion del teercer texto
        ImageView infocalibracion =(ImageView) findViewById(R.id.infocalibracion);
        infocalibracion.setColorFilter(Color.parseColor("#ffae00"));

        this.aprovadopaso1 = (ImageView) findViewById(R.id.imgcalibracionpaso1);
        this.aprovadopaso2 = (ImageView) findViewById(R.id.imgcalibracionpaso2);
        this.textopaso2 = (TextView)findViewById(R.id.calibracion_paso2);


        //-----------------------------------------------------------------------------
        //Creancion de sonido
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.elsonido = MediaPlayer.create(this,R.raw.calibracion);
        this.elsonido.setLooping(true);
        this.elsonido.setVolume(0,0);
        elsonido.start();



        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        sonidoMaximo();
        //Auriculares
        this.bloqueadodeLayout = ListenerReceiber.getClase();

        Log.i(TAG, "Me crearon");

    }




    /**
     * Metodo encargado de poner el sonido esta al maximo
     */
    private void sonidoMaximo(){
        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int valuess = 10;//range(0-15)
       mgr.setStreamVolume(AudioManager.STREAM_MUSIC,valuess,0);
        this.aprovadopaso1.setImageResource(R.drawable.aprovado);

    }



    /**
     * Metodo encargado de alertar que es el audifono izquierdo el que se identifico y ademas me envia a la
     * siguiente actividad que seria el test
     * @param param
     */
    public void todook(View param){
        final Intent siguiente = new Intent(this, Test1.class);
        //Codigo de Alerta que es el audifono izquierdo, y que solo se deje puesto ese audifono  ###########
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Leeme!!!");
        dialogo.setMessage("SOLO dejate puesto el AURICULAR en el que ESCUCHAS el sonido");
        dialogo.setPositiveButton("Visto", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                elsonido.stop();
                startActivity(siguiente);
            }
        });
        dialogo.create();
        dialogo.show();


    }

    /**
     *  encargado de cambiar los valores del view para que de un efecto de animacion
     * es necesario implementarlo ya que cuando se quita los audifonos y se ponenen se
     * seteal el view original y queda todo con x pero repoduciendo sonido
*/
    public void efectoreproduccion(){
        this.aprovadopaso2.setImageResource(R.drawable.aprovado);
        this.textopaso2.setText("2: Reproduciendo");


    }


    @Override
    protected void onResume() {
        registerReceiver(bloqueadodeLayout, receiverFilter );
        //Se setema el observador ya que si se desconecta y se vuelve a desconectar no aparece el bloqueo
        this.bloqueadodeLayout.setMiObservador(this);
        this.elsonido.start();
        Log.i(TAG,"Estoy Activa Nuevamente");
        super.onResume();

    }

    @Override
    protected void onPause() {
        unregisterReceiver(bloqueadodeLayout);
        this.elsonido.pause();
        Log.i(TAG,"entrare en pausa");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onBackPressed() {
        if(this.tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            return;
        }else{
            Toast.makeText(this,"Vuelve a precionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }


    @Override
    public void accionObservador(Boolean estado) {
        if(estado){
            efectoreproduccion();
            //Se seteal el sonido ya que si se inicia la actividad sin los auriculares desconectados
            //ENtonces cuando se conecten se repoducira pero con el sonido que se setio en el oncreate
            //que es de 0,0
            this.elsonido.setVolume(1,0);
            this.elsonido.start();
            Log.i(TAG, "Audifonos  Conectados, Se reproduce");
        }else{
            Intent bloqueo = new Intent(this, com.furiosojack.audifontest.Activitys.bloqueo.class);
            startActivity(bloqueo);
            this.elsonido.pause();
            Log.i(TAG, "Audifonos No Conectados, No Se reproduce");

        }

    }


}
