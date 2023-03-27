/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos;

import GPS.PuntosGps.PuntosGps;
import aplicacionlogistica.configuracion.Inicio;
import eu.bitm.NominatimReverseGeocoding.Address;
import eu.bitm.NominatimReverseGeocoding.NominatimReverseGeocodingJAPI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author lelopez
 */
class Tc_positions {

    int idMovimiento; //(rst.getInt("id");
    double speed; //(rst.getDouble("speed");
    String placa;//(rst.getString("name");
    //Icon(new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/truck_32x32.png"));
    String fechaDeInicio;//(rst.getString("servertime");
    String fechaActualizacion;
    String fechaFinal;//("");
    GeoPosition ultimaUbicacion;//(new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude"));
    Double odometro;//(rst.getDouble("kilometrajeInical") + rst.getDouble("distanciaTotal");
    Double course;//(rst.getDouble("course");
    String latitud;
    String longitud;
    String direccion;
    Inicio ini;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFechaDeInicio() {
        return fechaDeInicio;
    }

    public void setFechaDeInicio(String fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public GeoPosition getUltimaUbicacion() {
        return ultimaUbicacion;
    }

    public void setUltimaUbicacion(GeoPosition ultimaUbicacion) {
        this.ultimaUbicacion = ultimaUbicacion;
    }

    public Double getOdometro() {
        return odometro;
    }

    public void setOdometro(Double odometro) {
        this.odometro = odometro;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(Double course) {
        this.course = course;
    }

    public Tc_positions(Inicio ini) {
        this.ini = ini;
    }

    
    
    
    public Tc_positions(Inicio ini,String  placa) {
        this.ini = ini;
        this.placa = placa;
        ResultSet rst = null;
        Statement st = null;
        Connection con = this.ini.getConnGPS();
        String sql = "";

        try {
            sql = "SELECT p.id, p.protocol, p.deviceid,d.name,d.positionid,d.attributes,d.lastupdate,"
                    + "JSON_EXTRACT(d.attributes,'$.Kilometraje') as kilometrajeInical, "
                    + "ROUND((JSON_EXTRACT(p.attributes,'$.totalDistance')/1000),2) as distanciaTotal,"
                    + "p.servertime as servertime,"
                    // + " p.fechaDeInicio, "
                    + "p.devicetime, p.fixtime, p.valid, p.latitude, "
                    + "p.longitude, p.altitude, p.speed, p.course, p.address, p.attributes, p.accuracy, p.network "
                    + "FROM tc_devices d "
                    // + "FROM tc_positions p "
                    + "join tc_positions p on  p.id = d.positionid "
                    + "where "
                    // + "d.positionid = p.id  and "
                    + "d.name ='" + this.placa + "' ;";

          
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {

                    this.idMovimiento = rst.getInt("id");
                    this.speed = rst.getDouble("speed");
                    this.placa = rst.getString("name");
                    this.fechaDeInicio = rst.getString("servertime");
                    this.fechaFinal = "";
                    this.fechaActualizacion = rst.getString("lastupdate");
                    this.latitud = "" + rst.getDouble("latitude");
                    this.longitud = "" + rst.getDouble("longitude");
                    this.ultimaUbicacion = new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude"));
                    this.odometro = rst.getDouble("kilometrajeInical") + rst.getDouble("distanciaTotal");
                    this.course = rst.getDouble("course");

                    NominatimReverseGeocodingJAPI nominatim1 = new NominatimReverseGeocodingJAPI(); //create instance with default zoom level (18)
                    Address addres = nominatim1.getAdress(getUltimaUbicacion().getLatitude(), getUltimaUbicacion().getLongitude()); //returns Address object for the given position
                    this.direccion = addres.getDisplayName();

                    rst.close();

                    st.close();
                    //con.close();

                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
