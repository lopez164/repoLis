/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class CSvcIncidencias {
    
    CManifiestosDeDistribucion objManifiesto;
    CFacturas objFacturaCamdun;

    String numeroFactura;
    String numeroManifiesto;
    String consecutivo;
    int idtipoDeMovimiento;
    String nombreTipoDeMovimiento;
    String nombreCausalDeRechazo;
    String fechaIncidencia;
    String responsable;
    String nombrResponsable;
    String observaciones;
    int idEstado;
    int cantidadGestiones;
    String fechaDeCierre;
    int medioDePeticion;
    int idMovimientoFinal;
    int activo;
    String fechaIng;
    String usuario;
    int flag;
   
    
    ArrayList<CGestiones> listadoDeGestiones = null;
    Inicio ini;

    public String getNombreTipoDeMovimiento() {
        return nombreTipoDeMovimiento;
    }

    public void setNombreTipoDeMovimiento(String nombreTipoDeMovimiento) {
        this.nombreTipoDeMovimiento = nombreTipoDeMovimiento;
    }

    public String getNombreCausalDeRechazo() {
        return nombreCausalDeRechazo;
    }

    public void setNombreCausalDeRechazo(String nombreCausalDeRechazo) {
        this.nombreCausalDeRechazo = nombreCausalDeRechazo;
    }
    
    

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNombrResponsable() {
        return nombrResponsable;
    }

    public void setNombreResponsable(String nombrResponsable) {
        this.nombrResponsable = nombrResponsable;
    }

    
    public int getIdtipoDeMovimiento() {
        return idtipoDeMovimiento;
    }

    public void setIdtipoDeMovimiento(int idtipoDeMovimiento) {
        this.idtipoDeMovimiento = idtipoDeMovimiento;
    }

    public String getFechaIncidencia() {
        return fechaIncidencia;
    }

    public void setFechaIncidencia(String fechaIncidencia) {
        this.fechaIncidencia = fechaIncidencia;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getCantidadGestiones() {
        return cantidadGestiones;
    }

    public void setCantidadGestiones(int gestiones) {
        this.cantidadGestiones = gestiones;
    }

    public String getFechaDeCierre() {
        return fechaDeCierre;
    }

    public void setFechaDeCierre(String fechaDeCierre) {
        this.fechaDeCierre = fechaDeCierre;
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

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }
  
    public CManifiestosDeDistribucion getObjManifiesto() {
        return objManifiesto;
    }

    public void setObjManifiesto(CManifiestosDeDistribucion objManifiesto) {
        this.objManifiesto = objManifiesto;
    }

    public CFacturas getObjFacturaCamdun() {
        return objFacturaCamdun;
    }

    public void setObjFacturaCamdun(CFacturas objFacturaCamdun) {
        this.objFacturaCamdun = objFacturaCamdun;
    }

   
    public int getIdMovimientoFinal() {
        return idMovimientoFinal;
    }

    public void setIdMovimientoFinal(int idMovimientoFinal) {
        this.idMovimientoFinal = idMovimientoFinal;
    }

    public int getMedioDePeticion() {
        return medioDePeticion;
    }

    public void setMedioDePeticion(int medioDePeticion) {
        this.medioDePeticion = medioDePeticion;
    }

   
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    
    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public ArrayList<CGestiones> getListadoDeGestiones() {
        return listadoDeGestiones;
    }

    public void setListadoDeGestiones(ArrayList<CGestiones> listadoDeGestiones) {
        this.listadoDeGestiones = listadoDeGestiones;
    }

    public void setListadoDeGestiones(String consecutivo) {
        cargarListadoDegestiones(consecutivo);
    }

    public void cargarListadoDegestiones(String consecutivo1) {
        ArrayList<CGestiones> listadoDeGestiones = null;
        Connection con;
        Statement st;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        listadoDeGestiones = new ArrayList<>();
        String sql = "SELECT g.idgestionIncidencias, g.consecutivo, g.gestion, g.respuesta,"
                + " g.activo, g.fechaIng, g.usuario,t.nombreTipoDeGestion  "
                + "FROM gestionincidencias  g "
                + "join tiposDeGestion t "
                + " on g.gestion=t.idtiposDeGestion "
                + "where "
                + "g.consecutivo='" + consecutivo1 + "' and " + " g.activo=1 order by g.fechaIng asc;";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {

                    CGestiones gestion = new CGestiones(ini);

                    gestion.setActivo(rst.getInt("activo"));
                    gestion.setConsecutivo(rst.getString("consecutivo"));
                    gestion.setFechaIng(rst.getTimestamp("fechaIng"));
                    //gestion.setFlag(rst.getInt("flag"));
                    gestion.setGestion(rst.getString("nombreTipoDeGestion"));
                    gestion.setIdGestion(rst.getInt("gestion"));
                    gestion.setIdgestionIncidencias(rst.getInt("idgestionIncidencias"));
                    gestion.setRespuesta(rst.getString("respuesta"));
                    gestion.setUsuario(rst.getString("usuario"));

                    listadoDeGestiones.add(gestion);

                }

                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CtiposGestiones.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.listadoDeGestiones = listadoDeGestiones;
        }
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public CSvcIncidencias() {
    }

    public CSvcIncidencias(Inicio ini) {
        this.ini = ini;
        //cargarListadoDegestiones(consecutivo);
    }

    public CSvcIncidencias(Inicio ini, String numeroFactura) {
        this.ini = ini;
        ResultSet rst = null;
        Statement st = null;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeFacturasPorManifiesto");

        String sql = null; 
       
               
        
          sql = "SELECT consecutivo,numeroManifiesto,numeroFactura "
                + " FROM rutero r "
                  + ""
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "' "
                + "ORDER BY fechaIng desc "
                + "limit 1";
        
        try {

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.consecutivo=rst.getString("consecutivo");
                    this.numeroManifiesto=rst.getString("numeroManifiesto");
                    this.numeroFactura= rst.getString("numeroFactura");
                    
                    
                }else{
                     this.numeroFactura=null;
                }
                rst.close();
                st.close();
                con.close();
                this.objManifiesto = new CManifiestosDeDistribucion(ini,Integer.parseInt(numeroManifiesto));
                this.objFacturaCamdun = new CFacturas(ini, numeroFactura);
               
                
            }
        }catch(Exception e){
            
        }
        
    }
    
      public CSvcIncidencias(Inicio ini, int incidencia) {
        this.ini = ini;
        ResultSet rst = null;
        Statement st = null;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeFacturasPorManifiesto");

        String sql = null; 
       
               
        
          sql = "SELECT r.consecutivo,r.numeroManifiesto,r.numeroFactura "
                + " FROM rutero r "
                  + ""
                + "WHERE "
                + "r.numeroFactura='" + numeroFactura + "' an r.estadoManifiesto='3'"
                + "ORDER BY r.fechaIng desc "
                + "limit 1";
        
        try {

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.consecutivo=rst.getString("consecutivo");
                    this.numeroManifiesto=rst.getString("numeroManifiesto");
                    this.numeroFactura= rst.getString("numeroFactura");
                    
                    
                }else{
                     this.numeroFactura=null;
                }
                rst.close();
                st.close();
                con.close();
                this.objManifiesto = new CManifiestosDeDistribucion(ini,Integer.parseInt(numeroManifiesto));
                this.objFacturaCamdun = new CFacturas(ini, numeroFactura);
               
                
            }
        }catch(Exception e){
            
        }
        
    }

    public boolean grabarIncidencia() {
        boolean grabado = false;
        String sql = "update svcIncidencias set responsable=" + this.responsable + " where consecutivo='" + this.consecutivo + "';";

        sql = "INSERT INTO svcIncidencias "
                + " (consecutivo,numeroManifiesto,numeroFactura,idtipoDeMovimiento,fechaIncidencia, "
                + "responsable,observaciones,idEstado,gestiones,medioDePeticion, "
                + " idMovimientoFinal,activo,fechaIng,usuario,flag) VALUES('"
                + this.consecutivo + "','"
                + this.numeroManifiesto  + "','"
                + this.numeroFactura + "','"
                + this.idtipoDeMovimiento + "',"
                + "curdate() ,'"
                + this.responsable + "','"
                + this.observaciones + "','"
                + this.idEstado  + "','"
                + this.cantidadGestiones + "','"           
                + this.medioDePeticion + "','"
                + this.idMovimientoFinal + "','"
                + this.activo  + "',"
                + "CURRENT_TIMESTAMP,'"
                + this.usuario  + "','"
                + "1') on duplicate key update flag=-1;";

        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public boolean actualizarResponsable() {
        boolean grabado = false;
        String sql = "update svcIncidencias set responsable=" + this.responsable + " where consecutivo='" + this.consecutivo + "';";
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public boolean cerrarIncidencia() {
        boolean grabado = false;
        String sql = "update svcIncidencias set idEstado='2', fechaDeCierre=CURRENT_DATE() where consecutivo='" + this.consecutivo + "';";
        grabado = ini.insertarDatosRemotamente(sql);
        //this.estadoManifiesto = "2";
        return grabado;
    }
}
