package com.furiosojack.audifontest.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.furiosojack.audifontest.Logica.Operaciones;
import com.furiosojack.audifontest.R;

public class Finalizar extends AppCompatActivity {

    private static final String TAG ="ActivityFinal";
    private Operaciones operacion;
    //---------------------
    //Para salir dado dos veces atraz
    public static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    //---------------------

    private TextView mensaje;
    private Button finalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar);

        this.mensaje = (TextView)findViewById(R.id.finalizarActtxtMensaje);
        this.mensaje.setText("Mensaje Por defecto");

        float niveldaño =Operaciones.getInstancia().diferenciaVolumen();
        if((Operaciones.getInstancia().auriculardañado() == 1 && Operaciones.getInstancia().getAuricularMejorBjao()||
            Operaciones.getInstancia().auriculardañado() == 0 && !Operaciones.getInstancia().getAuricularMejorBjao()) ){
            //Daño volumen auricular derecho
            niveldaño = Operaciones.getInstancia().diferenciaVolumen()-Operaciones.getInstancia().valorrestarDiferencia();
        }
        String labelDaño= "No tiene Daño";
        String auricularDañado = "";
        if(Operaciones.getInstancia().auriculardañado() == 1){
            auricularDañado = "Derecho";

        }else{
            auricularDañado ="Izquierdo";
        }

        if(niveldaño > (float)0 && niveldaño < 0.009){
            labelDaño = "El Auricular "+auricularDañado+" tiene un Daño Leve";
        }else if(niveldaño>= 0.009 && niveldaño <=0.025){
            labelDaño="El Auricular "+auricularDañado+" tiene un Daño Moderado";
        }else if(niveldaño >= 0.026){
            labelDaño="El Auricular "+auricularDañado+" tiene un Daño Grave";
        }
        Log.i(TAG,Operaciones.getInstancia().diferenciaVolumen()+"");
        mensaje.setText(labelDaño);

    }

    public void salir(View sal){
        //Codigo de Alerta que es el audifono izquierdo, y que solo se deje puesto ese audifono  ###########
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Advertencia");
        dialogo.setMessage("Estas Seguro?.");
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        });
        dialogo.create();
        dialogo.show();

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
}
