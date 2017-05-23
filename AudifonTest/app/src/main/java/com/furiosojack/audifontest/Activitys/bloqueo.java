package com.furiosojack.audifontest.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.furiosojack.audifontest.Activitys.Calibracion;
import com.furiosojack.audifontest.Activitys.MainActivity;
import com.furiosojack.audifontest.Logica.ListenerReceiber;
import com.furiosojack.audifontest.ObServador.ObjObservador;

public class bloqueo extends Activity implements ObjObservador{

    private final static String TAG = "Bloqueo";
    private long tiempoPrimerClick;
    private IntentFilter receiverFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListenerReceiber.getClase().setMiObservador(this);
        receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    }


    @Override
    protected void onResume() {
        registerReceiver(ListenerReceiber.getClase(), receiverFilter );

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(ListenerReceiber.getClase());
        super.onPause();
    }

    @Override
    public void accionObservador(Boolean estado) {
        if(estado){
            setResult(Activity.RESULT_OK);
            finish();
            Log.i(TAG,"Auriculares Conectados, me devuelvo");
        }
        Log.i(TAG, "Me notificaron");

    }
    @Override
    public void onBackPressed() {
        if(this.tiempoPrimerClick + Calibracion.INTERVALO > System.currentTimeMillis()){
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
}
