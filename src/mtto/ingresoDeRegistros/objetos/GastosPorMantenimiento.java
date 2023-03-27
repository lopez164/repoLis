/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class GastosPorMantenimiento {

    int id;
    int idGastoFlota;
    int idMttoPlaca;
    int activo;
    String fechaIng;
    String usuario;
    int flag;
    List<GastosFlota> listaGastosFlota;
    Inicio ini;

    public GastosPorMantenimiento(Inicio ini) {
        this.ini = ini;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGastoFlota() {
        return idGastoFlota;
    }

    public void setIdGastoFlota(int idGastoFlota) {
        this.idGastoFlota = idGastoFlota;
    }

    public int getIdMttoPlaca() {
        return idMttoPlaca;
    }

    public void setIdMttoPlaca(int idMttoPlaca) {
        this.idMttoPlaca = idMttoPlaca;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
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

    public List<GastosFlota> getListaGastosFlota() {
        return listaGastosFlota;
    }

    public void setListaGastosFlota(List<GastosFlota> listaGastosFlota) {
        this.listaGastosFlota = listaGastosFlota;
    }
    
    

    public boolean guardar() {
        List<String> listadeSenrenciasSQL = new ArrayList<>();
        boolean guardado = false;
        String sql = "";
        

        try {

            for (GastosFlota factura : listaGastosFlota) {
                sql = "INSERT INTO gastospormantenimiento("
                        + "idGastoFlota,"
                        + "idMttoPlaca,"
                        + "activo,"
                        + "fechaIng,"
                        + "usuario,"
                        + "flag) VALUES('"
                        + factura.getIdGastoFlota() + "','"
                        + this.idMttoPlaca + "','"
                        + this.activo + "',"
                        + "CURRENT_TIMESTAMP,'"
                        + this.usuario + "',"
                        + this.flag + "' "
                        + "on duplicate key update flag = -1;";

                listadeSenrenciasSQL.add(sql);
            }
            guardado = ini.insertarDatosRemotamente(listadeSenrenciasSQL, "GastosporMantenimiento");

        } catch (SQLException ex) {
            Logger.getLogger(GastosPorMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return guardado;

    }

}
