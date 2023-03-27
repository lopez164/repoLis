/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class CCuentaSecundariaLogistica {

    int idCuentaPrincipal;
    String nombreCuentaPrincipal;
    int idCuentaSecundaria;
    String nombreCuentaSecundaria;
    String codigoCuentaSecundaria;
    int activo;
    Inicio ini;

    public CCuentaSecundariaLogistica() {

    }

    public CCuentaSecundariaLogistica(Inicio ini) {
        this.ini = ini;
    }

    public CCuentaSecundariaLogistica(int idCuentaPrincipal, String nombreCuentaPrincipal, int idCuentaSecundaria, String nombreCuentaSecundaria, String codigoCuentaSecundaria, int activo) {
        this.idCuentaPrincipal = idCuentaPrincipal;
        this.nombreCuentaPrincipal = nombreCuentaPrincipal;
        this.idCuentaSecundaria = idCuentaSecundaria;
        this.nombreCuentaSecundaria = nombreCuentaSecundaria;
        this.codigoCuentaSecundaria = codigoCuentaSecundaria;
        this.activo = activo;
    }

    public int getIdCuentaPrincipal() {
        return idCuentaPrincipal;
    }

    public void setIdCuentaPrincipal(int idCuentaPrincipal) {
        this.idCuentaPrincipal = idCuentaPrincipal;
    }

    public String getNombreCuentaPrincipal() {
        return nombreCuentaPrincipal;
    }

    public void setNombreCuentaPrincipal(String nombreCuentaPrincipal) {
        this.nombreCuentaPrincipal = nombreCuentaPrincipal;
    }

    public int getIdCuentaSecundaria() {
        return idCuentaSecundaria;
    }

    public void setIdCuentaSecundaria(int idCuentaSecundaria) {
        this.idCuentaSecundaria = idCuentaSecundaria;
    }

    public String getNombreCuentaSecundaria() {
        return nombreCuentaSecundaria;
    }

    public void setNombreCuentaSecundaria(String nombreCuentaSecundaria) {
        this.nombreCuentaSecundaria = nombreCuentaSecundaria;
    }

    public String getCodigoCuentaSecundaria() {
        return codigoCuentaSecundaria;
    }

    public void setCodigoCuentaSecundaria(String codigoCuentaSecundaria) {
        this.codigoCuentaSecundaria = codigoCuentaSecundaria;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
    
     public boolean grabarCuentaSecundaria() {
         boolean grabado= false;
         try {
           
            String sql = "INSERT cuentassecundariaslogistica "
                    + "(idcuentaPrincipal,nombreCuentaSecundaria,codigoCuentaSecundaria,"
                    + "activo,fechaIng,usuario,flag)VALUES ('"
                    + this.idCuentaPrincipal + "','"
                    + this.nombreCuentaSecundaria + "','"
                    + this.codigoCuentaSecundaria + "','"
                    + this.activo + "',"
                    + "CURRENT_TIMESTAMP,'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1') "
                    + "on duplicate key update "
                    + "idcuentaPrincipal='" + this.idCuentaPrincipal + "',"
                    + "nombreCuentaSecundaria='" +  this.nombreCuentaSecundaria + "',"
                    + "codigoCuentaSecundaria='" +  this.codigoCuentaSecundaria + "',"
                    + "activo='" +  this.activo + "';";
            
            grabado = ini.insertarDatosRemotamente(sql);
            
           
        } catch (Exception ex) {
            Logger.getLogger(CCuentaSecundariaLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
         return grabado;
    }
    
     
     public boolean actualizarCuentaSecundaria() {
        boolean grabado= false;
         try {
           
            String sql= "UPDATE cuentassecundariaslogistica "
                    + "SET "
                    + "idcuentaPrincipal = '" + this.idCuentaPrincipal + "',"
                    + "nombreCuentaSecundaria = '" +  this.nombreCuentaSecundaria + "',"
                    + "codigoCuentaSecundaria = '" +  this.codigoCuentaSecundaria + "',"
                    + "activo = '" + this.activo + "',"
                    + "fechaIng = CURRENT_TIMESTAMP,"
                    + "usuario = '" +  Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "flag = '-1' "
                    + "WHERE idCuentaSecundaria = '" + this.idCuentaSecundaria + "';";
            
            
            grabado = ini.insertarDatosRemotamente(sql);
           
        } catch (Exception ex) {
            Logger.getLogger(CCuentaSecundariaLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
         return grabado;
    }

}
