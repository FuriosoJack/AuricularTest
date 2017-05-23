package com.furiosojack.audifontest.Logica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.furiosojack.audifontest.ObServador.ObjObserbable;
import com.furiosojack.audifontest.ObServador.ObjObservador;

/**
 * Clase solo se encargara de notificar cuando se conecta o desconecta auricualar a su acutal observador
 */

public class ListenerReceiber extends BroadcastReceiver implements ObjObserbable {


    private ObjObservador miObservador;
    private static final String TAG ="ListenerR";
    private static ListenerReceiber instancia;


    public ListenerReceiber(){

    }
    /**
     * Patron Singleton
     *
     * @return
     */
    public static ListenerReceiber getClase(){
        if(instancia == null){
            instancia = new ListenerReceiber();
            Log.i(TAG, "CREADO");
            return instancia;
        }else{
            return  instancia;
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Recibe");
        if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
            int estadoAudifonos = intent.getIntExtra("state",-1);
            //Solo se ejecuta una vez
            switch (estadoAudifonos){
                case 0:
                    //Auriculares Desconectados
                     notificar(false);
                    //Cambiendo el view a desconectado
                    Log.i(TAG, "Auriculares Desconectados");
                    break;
                case 1:
                    //Auriculares Conectado
                    notificar(true);
                    Log.i(TAG, "Auriculares conectados");
                    break;
                default:
                    notificar(false);
                    break;
            }
        }
    }

    /**
     *
     * @param estado si es true es que estan conectado si no es que estan desconectados
     */
    @Override
    public void notificar(Boolean estado) {
        //Se notifica el estado al observador
        this.miObservador.accionObservador(estado);

    }


    //Setea observador
    public void setMiObservador(ObjObservador i){
        Log.d(TAG, "Nuevo Observador: "+ i.getClass().getName());
        this.miObservador=i;
    }
}
