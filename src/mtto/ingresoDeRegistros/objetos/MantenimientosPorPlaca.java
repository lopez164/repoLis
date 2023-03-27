/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtto.documentos.objetos.GastosPorVehiculo;

/**
 *
 * @author lelopez
 */
public class MantenimientosPorPlaca {

    int idMantenimientosPorPlaca;
    String placaVehiculo;
    int idtipoMantenimiento;
    String nombreMantenimiento;
    String conductorVehiculo;
    String nombreConductor;
    String apellidosConductor;
    int idNumeroDeorden;
    int kilometrajeMantenimiento;
    Date fechaMantenimiento;
    int zona;
    String nombreZona;
    int regional;
    String nombreRegional;
    int agencia;
    String nombreAgencia;
    String observaciones;
    int activo;
    Date fechaIng;
    String usuario;
    int flag;

    List<GastosFlota> listaGastosFlota;
    Inicio ini;

    public int getIdMantenimientosPorPlaca() {
        return idMantenimientosPorPlaca;
    }

    public void setIdMantenimientosPorPlaca(int idMantenimientosPorPlaca) {
        this.idMantenimientosPorPlaca = idMantenimientosPorPlaca;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public int getIdtipoMantenimiento() {
        return idtipoMantenimiento;
    }

    public void setIdtipoMantenimiento(int idtipoMantenimiento) {
        this.idtipoMantenimiento = idtipoMantenimiento;
    }

    public String getNombreMantenimiento() {
        return nombreMantenimiento;
    }

    public void setNombreMantenimiento(String nombreMantenimiento) {
        this.nombreMantenimiento = nombreMantenimiento;
    }

    public String getConductorVehiculo() {
        return conductorVehiculo;
    }

    public void setConductorVehiculo(String conductorVehiculo) {
        this.conductorVehiculo = conductorVehiculo;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getApellidosConductor() {
        return apellidosConductor;
    }

    public void setApellidosConductor(String apellidosConductor) {
        this.apellidosConductor = apellidosConductor;
    }

    public int getIdNumeroDeorden() {
        return idNumeroDeorden;
    }

    public void setIdNumeroDeorden(int idNumeroDeorden) {
        this.idNumeroDeorden = idNumeroDeorden;
    }

    public int getKilometrajeMantenimiento() {
        return kilometrajeMantenimiento;
    }

    public void setKilometrajeMantenimiento(int kilometrajeMantenimiento) {
        this.kilometrajeMantenimiento = kilometrajeMantenimiento;
    }

    public Date getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(Date fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public String getNombreRegional() {
        return nombreRegional;
    }

    public void setNombreRegional(String nombreRegional) {
        this.nombreRegional = nombreRegional;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
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

    public List<GastosFlota> getListaFacturasmantenimientosPOrPlaca() {
        return listaGastosFlota;
    }

    public void setListaFacturasmantenimientosPOrPlaca(List<GastosFlota> listaFacturasmantenimientosPOrPlaca) {
        this.listaGastosFlota = listaFacturasmantenimientosPOrPlaca;
    }

    public MantenimientosPorPlaca() {

    }

    public MantenimientosPorPlaca(Inicio ini) {
        this.ini = ini;
    }

    public boolean guardarMantenimientoPorPlaca() {
        boolean grabado = false;
        ResultSet rst = null;
        Connection con;

        PreparedStatement sentencia;

        try {
          
            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = ini.getConnRemota();

            /*Se guarda el registro del mantenimiento */
            String sql = "INSERT INTO mantenimientosporplaca("
                    + "placaVehiculo,idMantenimiento,conductorVehiculo,idNumeroDeorden,"
                    + "kilometraje,fechaMantenimiento,zona,regional,agencia,observaciones,"
                    + "activo,fechaIng,usuario,flag)"
                    + "VALUES ('"
                    + this.placaVehiculo + "','"
                    + this.idtipoMantenimiento + "','"
                    + this.conductorVehiculo + "','"
                    + this.idNumeroDeorden + "','"
                    + this.kilometrajeMantenimiento + "','"
                    + this.fechaMantenimiento + "','"
                    + ini.getUser().getZona() + "','"
                    + ini.getUser().getRegional() + "','"
                    + ini.getUser().getAgencia() + "','"
                    + this.observaciones + "','"
                    + this.activo + "',"
                    + "CURRENT_TIMESTAMP,'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',' "
                    + this.flag + "') "
                    + "ON DUPLICATE KEY UPDATE flag=-1;";

            //https://trellat.es/recuperar-id-autogenerado-tras-insert-java/
            sentencia = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 0) {
                 grabado= false;
                throw new SQLException("No se pudo guardar en la Sentencia =" + sql);
               
            }

            rst = sentencia.getGeneratedKeys();
            if (rst.next()) {
                this.idMantenimientosPorPlaca = rst.getInt(1);
                System.out.print("Consecutivo del mantenimiento " + this.idMantenimientosPorPlaca);
            }

            //con.close();
            rst.close();
            sentencia.close();
            
              /* Se guardan los registros de las Facturas */
            for (GastosFlota factura : listaGastosFlota) {
                factura.guardar();

            }


            /* se asignan los gastos asociados al mantenimiento */
            List<String> sentencias = new ArrayList<>();
            
            for (GastosFlota gasto : listaGastosFlota) {
                
                sql = "INSERT INTO gastospormantenimiento("
                        + "idGastoFlota,idMttoPlaca,activo,fechaIng,usuario,flag) VALUES('"
                        + gasto.getIdGastoFlota() + "','"
                        + idMantenimientosPorPlaca + "','"
                        + "1',"
                        + "CURRENT_TIMESTAMP,'"
                        + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                        + "1') ON DUPLICATE KEY UPDATE flag='-1';";
               
                sentencias.add(sql);

            }
           grabado = ini.insertarDatosRemotamente(sentencias, "MantenimientosPOrPlaca");

        } catch (Exception ex) {
            Logger.getLogger(MantenimientosPorPlaca.class.getName()).log(Level.SEVERE, null, ex);
            grabado = false;
        }
        return grabado;
    }

}
