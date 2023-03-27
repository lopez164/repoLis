/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.Inicio;

/**
 *
 * @author lelopez
 */
public class TiposDeMantenimientos {
    
int idMantenimiento;
String nombreMantenimiento;
int activo;
String usuario;
int flag;
Inicio ini;

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public String getNombreMantenimiento() {
        return nombreMantenimiento;
    }

    public void setNombreMantenimiento(String nombreMantenimiento) {
        this.nombreMantenimiento = nombreMantenimiento;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public TiposDeMantenimientos() {
        
    }

    public TiposDeMantenimientos(Inicio ini) {
        this.ini=ini;
    }
    
    public static String arrListadoDeMantenimientos(){
          //idCargo, nombreCargo, activo, fechaIng, usuario, flag
         String sql= "SELECT * "
                 + "FROM tiposdemantenimientosvehiculos;";
         return sql;
   }
}
