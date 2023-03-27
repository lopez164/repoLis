/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;

/**
 *
 * @author VLI_488
 */
public class CSoportesPorRuta { //extends Inicio {

    Inicio ini;
    int idConsecutivo;
    int numeroManifiesto;
    String numeroSoporte;
    int entidadBancaria;
    String nombreDelBanco;
    String numeroDeCuenta;
    String medioDepago;
    Double valor;

    public CSoportesPorRuta() {
    }

    public CSoportesPorRuta(Inicio ini) throws Exception {
        this.ini = ini;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public int getIdConsecutivo() {
        return idConsecutivo;
    }

    public void setIdConsecutivo(int idConsecutivo) {
        this.idConsecutivo = idConsecutivo;
    }

    public int getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(int numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNumeroSoporte() {
        return numeroSoporte;
    }

    public void setNumeroSoporte(String numeroSoporte) {
        this.numeroSoporte = numeroSoporte;
    }

    public int getEentidadBancaria() {
        return entidadBancaria;
    }

    public void setEentidadBancaria(int eentidadBancaria) {
        this.entidadBancaria = eentidadBancaria;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    public void setNumeroDeCuenta(String numeroDeCuenta) {
        this.numeroDeCuenta = numeroDeCuenta;
    }

    public String getMedioDepago() {
        return medioDepago;
    }

    public void setMedioDepago(String medioDepago) {
        this.medioDepago = medioDepago;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    
    
   
    public String cadenalistadoDeSoportesPorRuta() {

        // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
        String sql= "SELECT idCanalDeVenta, nombreCanalDeVenta, activo, fechaIng, usuario, flag "
                + "FROM tiposcanaldeventas  "
                + "WHERE "
                + "tiposcanaldeventas.activo=1;  ";
       
        return sql;
    }

}
