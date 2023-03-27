/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class CGestiones {

    int idgestionIncidencias, idGestion;
    String consecutivo, gestion, respuesta, usuario;
    int activo, flag;
    Date fechaIng;
    Inicio ini;
    

    public int getIdgestionIncidencias() {
        return idgestionIncidencias;
    }

    public void setIdgestionIncidencias(int idgestionIncidencias) {
        this.idgestionIncidencias = idgestionIncidencias;
    }

    public int getIdGestion() {
        return idGestion;
    }

    public void setIdGestion(int idGestion) {
        this.idGestion = idGestion;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getGestion() {
        return gestion;
    }

    public void setGestion(String gestion) {
        this.gestion = gestion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
        this.fechaIng = fechaIng;
    }

    public CGestiones(Inicio ini) {
        this.ini = ini;
    }

    public CGestiones(Inicio ini, int idgestionIncidencias, String consecutivo, String gestion, String respuesta, String usuario, int activo, int flag, Date fechaIng) {
        this.ini = ini;
        this.idgestionIncidencias = idgestionIncidencias;
        this.consecutivo = consecutivo;
        this.gestion = gestion;
        this.respuesta = respuesta;
        this.usuario = usuario;
        this.activo = activo;
        this.flag = flag;
        this.fechaIng = fechaIng;
    }

    public boolean grabarGestion() {
        boolean grabado = false;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "INSERT INTO gestionincidencias(consecutivo,gestion,respuesta,"
                    + "activo,usuario) VALUES('"
                    + this.consecutivo + "','"
                    + this.idGestion + "','"
                    + this.respuesta + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "');";
            //+ "'lopez164')";
            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CGestiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    // 3143747637 guacharo
   
}
