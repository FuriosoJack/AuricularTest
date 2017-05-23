package com.furiosojack.audifontest.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.furiosojack.audifontest.Activitys.MainActivity;
import com.furiosojack.audifontest.Logica.ListenerReceiber;
import com.furiosojack.audifontest.Logica.Operaciones;
import com.furiosojack.audifontest.ObServador.ObjObservador;
import com.furiosojack.audifontest.R;

public class Test3 extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, ObjObservador{

    private static final String TAG="ActivityTEST3";
    private MediaPlayer elsonido;
    private Equalizer ecualizador;

    //---------------------
    //Para salir dado dos veces atraz
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    //---------------------

    private ListenerReceiber bloqueadodeLayout;
    private IntentFilter receiverFilter;
    //Vista
    private SeekBar salidaVolumenAuricular;
    private Button izquierdo;
    private Button derecho;
    private ImageView animnaciondeslizar;

    //Paramatros ecualizador
    private int numerobandas;
    private short minimoNivel;
    private short maximoNivel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        //Botones
        izquierdo = (Button) findViewById(R.id.btntest1_izquierdo);
        izquierdo.setOnClickListener(this);
        derecho = (Button) findViewById(R.id.btntest1_derecho);
        derecho.setOnClickListener(this);

        //Animacion de deslizar el progressbar
        animnaciondeslizar = (ImageView)findViewById(R.id.animacionDelizar);
        animnaciondeslizar.setBackgroundResource(R.drawable.deslizarvolumen);
        AnimationDrawable animacion = (AnimationDrawable)animnaciondeslizar.getBackground();
        animacion.start();



        //Preparando onido

        this.elsonido = MediaPlayer.create(this,R.raw.calibracion);
        this.elsonido.setLooping(true);
        this.elsonido.start();

        //Barra de progreso
        this.salidaVolumenAuricular = (SeekBar)findViewById(R.id.test3Volumen);
        this.salidaVolumenAuricular.setOnSeekBarChangeListener(this);

        //Ecualizador
        this.ecualizador = new Equalizer(0, this.elsonido.getAudioSessionId());
        inicardatosEQ();
        //rAuriculares
        this.bloqueadodeLayout = ListenerReceiber.getClase();

        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Leeme!!!");
        dialogo.setMessage("Colocate los dos Auriculares");
        dialogo.create();
        dialogo.show();


    }

    private void inicardatosEQ(){
        this.ecualizador.setEnabled(true);
        numerobandas = this.ecualizador.getNumberOfBands();
        Log.i(TAG,"Numero Bandas"+numerobandas);


        this.minimoNivel= this.ecualizador.getBandLevelRange()[0];
        this.maximoNivel = this.ecualizador.getBandLevelRange()[1];


        for (short nbanda=0;nbanda<numerobandas;nbanda++ ){
            if(nbanda<(short)2){
                int nivelbajos = minimoNivel + (maximoNivel - minimoNivel) * 100 / 100;
                this.ecualizador.setBandLevel(nbanda,(short)nivelbajos);

            }else{
                int nivelbajos = minimoNivel + (maximoNivel - minimoNivel) * 0 / 100;
                this.ecualizador.setBandLevel(nbanda, (short)nivelbajos);

            }

        }
        BassBoost bb = new BassBoost(0, this.elsonido.getAudioSessionId());
        bb.setStrength((short)100);





    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int nivel, boolean b) {
        if(seekBar.getId() == this.salidaVolumenAuricular.getId()){
            float valorDerecho=1;
            float valorIzquierdo=1;
            if(nivel>50){
                //Le quito volumen al izquierdo
                valorIzquierdo = (float)(1-((nivel-50)*0.02));
                Log.i(TAG, "Volumen Izquiedo"+valorDerecho);
                valorDerecho=1;

            }else if(nivel <50){
                //Le resto al derecho
                valorDerecho = (float)(nivel*0.02);
                valorIzquierdo=1;
            }
            this.elsonido.setVolume(valorIzquierdo,valorDerecho);

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
    public void onClick(View view) {

        //Verificacion del boton
        if(view.getId() == izquierdo.getId()){
            Operaciones.getInstancia().setAuricularMejorBajo(true);
            Log.i(TAG, "Auricular Izquierdo Oprimiedo");
        }else if(view.getId() == derecho.getId()){
            Operaciones.getInstancia().setAuricularMejorBajo(false);
            Log.i(TAG, "Auricular derecho Oprimiedo");
        }
        Intent siguiente = new Intent(this,Finalizar.class);
        startActivity(siguiente);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(bloqueadodeLayout);
        elsonido.pause();
        super.onPause();
    }

    @Override
    public void accionObservador(Boolean conectados) {
        //true contectados false desconectados
        if(conectados){

            Log.i(TAG,"Auriduculares conectados");
        }else{
            Intent bloqueo = new Intent(this, com.furiosojack.audifontest.Activitys.bloqueo.class);
            startActivityForResult(bloqueo,0);

        }
    }

    @Override
    protected void onResume() {
        //Se poner el observador aca para cuando haya un onresult cambie el observador
        this.bloqueadodeLayout.setMiObservador(this);
        registerReceiver(bloqueadodeLayout, receiverFilter );
        this.elsonido.start();
        super.onResume();
    }
}
