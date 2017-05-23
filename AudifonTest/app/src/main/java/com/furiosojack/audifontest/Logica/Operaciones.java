package com.furiosojack.audifontest.Logica;

/**
 * Esta clase se encargara de hacer las operaciones logicas para decidir cual es audifonos que funciona
 */

public class Operaciones {
    private static final short VOLUMENMAXIMO = 1;
    private static Operaciones instancia;
    private boolean mejorBajos; //false derecho true izquierdo

    private float minIzqVolumen;
    private float minDerVolumen;
    private float maxIzqVolumen;
    private float maxDerVolumen;





    private Operaciones(){

    }

    public static Operaciones getInstancia(){
        if(instancia == null){
            instancia = new Operaciones();
            return instancia;
        }else{
            return  instancia;
        }
    }




    public void setminIzqVolumen(float volumen){
        this.minIzqVolumen = volumen;
    }
    public void setminDerVolumen(float volumen){
        this.minDerVolumen = volumen;
    }
    public void setMaxIzqVolumen(float volumen){
        this.maxIzqVolumen=volumen;
    }
    public void setMaxDerVolumen(float volumen){
        this.maxDerVolumen=volumen;
    }

    public float getMinIzqVolumen(){
        return this.minIzqVolumen;
    }
    public  float getMinDerVolumen(){
        return this.minDerVolumen;
    }

    public float porcentajeMinDerecho(){
        return (this.minDerVolumen*100)/VOLUMENMAXIMO;
    }
    public float porcentajeMinIzquierdo(){
        return (this.minIzqVolumen*100)/VOLUMENMAXIMO;
    }


    /**
     *
     * @return retornara un numero dependiendo el tipo de daño
     * -1 para no hay daño
     * 0 para dañor en auricular izquierdo
     * 1 para daño auricular derecho
     *
     */
    public short auriculardañado(){
        short dañado=-1;
        if(this.minIzqVolumen < this.minDerVolumen){
            //Daño en el derecho
            dañado=1;
        }else if(this.minIzqVolumen > this.minDerVolumen) {
            //Daeño en el izquierdo
            dañado=0;
        }

        return dañado;
    }

    /**
     * encargada de setear el dato del aiuricular que se esucha mejor en la prueba de bajos
     */
    public void setAuricularMejorBajo(Boolean esizquierdo){
        mejorBajos = esizquierdo;


    }
    public boolean getAuricularMejorBjao(){
        return  mejorBajos;
    }
    public float valorrestarDiferencia(){
        return (diferenciaVolumen()*30)/70;
    }


    public float diferenciaVolumen(){
        float resta = this.minDerVolumen-this.minIzqVolumen;
        return (resta<0)?resta*(-1):resta;
    }


}
