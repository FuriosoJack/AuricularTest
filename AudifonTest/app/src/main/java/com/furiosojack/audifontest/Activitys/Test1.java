package com.furiosojack.audifontest.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;


import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.furiosojack.audifontest.Logica.ListenerReceiber;
import com.furiosojack.audifontest.Logica.Operaciones;
import com.furiosojack.audifontest.ObServador.*;
import com.furiosojack.audifontest.R;


public class Test1 extends AppCompatActivity implements View.OnClickListener, ObjObservador{

    private static final String TAG = "Test1Activity";

    //---------------------
    //Para salir dado dos veces atraz
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    //---------------------


    private TextView texto;
    private Button comenzar;
    private ImageView animacionDerecha;
    private ImageView animacionIzquierda;
    private  TextView nprueba;
    private TextView txtauricular;

    private ListenerReceiber bloqueadodeLayout;
    private IntentFilter receiverFilter;
    private MediaPlayer elsonido;
    private float valordelsonido;
    private boolean sonidoparado; //Variable utilizada como bandera para que se detenga lo de subir cuando se para el sonido
    private boolean nopararvolumen; //Varible utilizada como bandera para detener el ciclo del volumen cuando el usuario ya escucha el sonido
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        this.texto = (TextView) findViewById(R.id.txt_tezt1);
        this.comenzar =(Button) findViewById(R.id.btn_test1_comenzar);
        this.comenzar.setOnClickListener(this);
        this.txtauricular = (TextView) findViewById(R.id.txtauricular);

        this.animacionDerecha = (ImageView)findViewById(R.id.animacionderecha);
        animacionDerecha.setBackgroundResource(R.drawable.animacionsenalr);
        this.animacionIzquierda = (ImageView)findViewById(R.id.animacionizquierda);
        this.animacionIzquierda.setBackgroundResource(R.drawable.animacionsenall);

        AnimationDrawable animacionD = (AnimationDrawable)animacionDerecha.getBackground();
        animacionD.start();
        AnimationDrawable animacionI = (AnimationDrawable)animacionIzquierda.getBackground();
        animacionI.start();
        this.animacionDerecha.setVisibility(View.GONE);
        this.animacionIzquierda.setVisibility(View.GONE);

        this.nprueba = (TextView)findViewById(R.id.txt_test1_nprueba);

        //-----------------------------------------------------------------------------
        //Creancion de sonido
        this.elsonido = MediaPlayer.create(this,R.raw.calibracion);
        this.elsonido.setLooping(true);
        this.elsonido.setVolume(0,0);
        elsonido.start();

        //rAuriculares
        this.bloqueadodeLayout = ListenerReceiber.getClase();
        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);


        sonidoMaximo();
        this.nopararvolumen = true;
        Log.i(TAG, "Me crearon");



    }


    /**
     * Metodo encargado de poner el sonido esta al maximo
     */
    private void sonidoMaximo(){
        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int valuess = 10;//range(0-15)
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC,valuess,0);

    }

    /**
     * Metodo encargado de subir el volumen del sonido, lo hace como un hilo ya que se hace cada segundo
     * y para que no se congele el activiry
     */
    private void subirVolumen(final Boolean esElizquierdo) {
        elsonido.start();//COnfirmar que el sonido este iniciado
        //esElizquierdo es la variable que me indica a cual auricular se debee subir el volumen
        Log.i(TAG, "Comienzo Subiendo volumen");
        class ActualizaVolumen extends AsyncTask<Boolean, Float, Float> {
            @Override

            /**
             * Valor que viene or el plubish
             */
            protected void onProgressUpdate(Float... values) {
                //Validacion al auricular que se va asubir el volumen
                Log.i(TAG, "Valor Subiendo VOlumen: "+ values[0]);
                if(esElizquierdo){
                    elsonido.setVolume(values[0],0);
                }else{
                    elsonido.setVolume(0,values[0]);
                }



            }

            @Override
            protected Float doInBackground(Boolean... esElizquierdo) {
                //
                valordelsonido = 0;
                while (valordelsonido <= 1 && nopararvolumen) {
                    if (!sonidoparado) {
                        valordelsonido += 0.001f;
                        //Verificacion si es el auricular izquierdo o derecho al que se le va ausbir el volumen
                        publishProgress(valordelsonido);
                        Log.i(TAG, "Subiendo volumen");
                    } else {
                        Log.i(TAG, "No se ha subido volumen, por que estan desconectados");
                    }
                    SystemClock.sleep(1000);
                }
                return valordelsonido;
            }
        }
        new ActualizaVolumen().execute(esElizquierdo);
    }





    @Override
    protected void onPause() {
        unregisterReceiver(bloqueadodeLayout);
        elsonido.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Se poner el observador aca para cuando haya un onresult cambie el observador
        this.bloqueadodeLayout.setMiObservador(this);
        registerReceiver(bloqueadodeLayout, receiverFilter );
        this.elsonido.start();
        super.onResume();
    }




    @Override
    public void onClick(View view) {
        if(view.getId() == comenzar.getId()){
            if("Comenzar>>".equals(this.comenzar.getText().toString())){
                if("Prueba 1".equals(this.nprueba.getText().toString())){
                    sonidoparado=false;// EStar seguro que si aumente el sonido
                    subirVolumen(true); //Es true por que se le esta subiendo el volumen al izquierdo ya que es con el que se va a comenzar
                    this.animacionIzquierda.setVisibility(View.VISIBLE);
                }else if ("Prueba 2".equals(this.nprueba.getText().toString())){
                    nopararvolumen=true;//Para que si inicie a subir volumen y entre al while
                    sonidoparado=false; //estar seguro que si aumente el sonido tambien se implementa por que si no la prueba do no inicia y se queda en silencio
                    subirVolumen(false);
                    this.animacionDerecha.setVisibility(View.VISIBLE);
                }else{
                    Log.i(TAG,"Error en la prueba");
                    return;

                }
                this.texto.setText(R.string.txtTest1_1);
                this.comenzar.setText(R.string.btntest_2);
                Toast.makeText(this,"Reproduciendo..",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Comenzando Reproduccion, Accion de Usuario");
            }else if("Lo Escucho".equals(this.comenzar.getText().toString())){
                //Cuando el usurio ya esuchco el sonido en el auricular izquierdo
                nopararvolumen=false; //Parando el aumento del volumen
                elsonido.setVolume(0,0); //Colocando el volumen en ceros
                elsonido.pause();
                final AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                if("Prueba 1".equals(this.nprueba.getText().toString())){

                    //-------------Ingresando Valores al operador
                    Operaciones.getInstancia().setminIzqVolumen(this.valordelsonido);
                    //-------------
                    this.nprueba.setText(R.string.nprueba2); //Bandera para pasar a la prueba dos
                    this.txtauricular.setText(R.string.auricularR);
                    alerta.setTitle("Prueba Exitosa");
                    alerta.setMessage("Colocate el otro auricular en el mismo oido");
                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ///Inicializo los datos de la vista
                            animacionIzquierda.setVisibility(View.GONE);



                        }
                    });

                    alerta.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            alerta.show();
                        }
                    });

                    texto.setText(R.string.descipciontest1);
                    comenzar.setText(R.string.btntest);
                    alerta.create();
                    alerta.show();


                }else if("Prueba 2".equals(this.nprueba.getText().toString())){
                    //-------------Ingresando Valores al operador
                    Operaciones.getInstancia().setminDerVolumen(this.valordelsonido);
                    //-------------

                    alerta.setMessage("Test Finalizado Satisfactoriamente");
                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            elsonido.stop();
                            Intent siguiente = new Intent(Test1.this,Test3.class);
                            startActivity(siguiente);
                        }
                    });
                    alerta.create();
                    alerta.show();
                }
                Log.i(TAG, "Valor del sonido: "+ this.valordelsonido);

            }



        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if("Prueba 1".equals(this.nprueba.getText().toString())){
            sonidoparado=false;// EStar seguro que si aumente el sonido
            subirVolumen(true); //Es true por que se le esta subiendo el volumen al izquierdo ya que es con el que se va a comenzar
        }else if ("Prueba 2".equals(this.nprueba.getText().toString())){
            nopararvolumen=true;//Para que si inicie a subir volumen y entre al while
            sonidoparado=false; //estar seguro que si aumente el sonido tambien se implementa por que si no la prueba do no inicia y se queda en silencio
            subirVolumen(false);
        }
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
    public void accionObservador(Boolean conectados) {
        //true contectados false desconectados
        if(conectados){
            sonidoparado=false;
            Log.i(TAG,"Auriduculares conectados");
        }else{
            Intent bloqueo = new Intent(this, com.furiosojack.audifontest.Activitys.bloqueo.class);
            startActivityForResult(bloqueo,0);
            sonidoparado = true;
        }
    }




}
