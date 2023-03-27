/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.PuntosGps;

import GPS.wayPoints.MyWayPoint;
import aplicacionlogistica.configuracion.Inicio;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;
import eu.bitm.NominatimReverseGeocoding.Address;
import eu.bitm.NominatimReverseGeocoding.NominatimReverseGeocodingJAPI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author lelopez
 */
public class Carro {

    String placa;
    String conductor;
    private ImageIcon icon;
    //private Set<Cliente> listadeClientes;
    // private List<Marker> listaDeMarcadoresPuntosRecorridos;
    ImageIcon iconNorte;
    ImageIcon iconSur;
    ImageIcon iconEste;
    ImageIcon iconOeste;
    ImageIcon iconNorEste;
    ImageIcon iconSurEste;
    ImageIcon iconNorOEste;
    ImageIcon iconSurOeste;

    Set<MyWayPoint> wpClientes;
    Set<MyWayPoint> wpPuntosRecorridos;

    public List<MyWayPoint> listaDeMarcadoresClientes;

    MyWayPoint puntoCarro;

    List<Polyline> listaDePoliLineas;
    List<PuntoDeRecorrido> listaDePuntosDeRecorrido;

    String fechaDeInicio;
    String fechaFinal;

    String fechaDistribucion;

    Double course;
    String nombreCourse;
    Double odometro;
    GeoPosition ultimaUbicacion;
    Double Speed;
    int idMovimiento;
    private PuntosGps mapa;

    Inicio ini;

    //Connection connServidorRuta = null;
   // Connection connServidorGps = null;

    public PuntosGps getMapa() {
        return mapa;
    }

    public void setMapa(PuntosGps mapa) {
        this.mapa = mapa;
    }

    
    
    public Carro(Connection conRuta, Connection conGps) {

        //this.connServidorRuta = conRuta;
        //this.connServidorGps = conGps;
        setIconos();
        this.listaDePuntosDeRecorrido = new ArrayList<>();

    }

    public Carro(Inicio ini) {

        this.ini = ini;
        //this.connServidorRuta = this.ini.getConnRemota();
        //this.connServidorGps = this.ini.getConnGPS();
        setIconos();
        this.listaDePuntosDeRecorrido = new ArrayList<>();

    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public Set<MyWayPoint> getWpClientes() {
        return wpClientes;
    }

    public void setWpClientes(Set<MyWayPoint> listadeClientes) {
        this.wpClientes = listadeClientes;
    }

    public void setWpClientes(ImageIcon icon) {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        String sql = "";
        this.wpClientes = new HashSet<>();
        double lt = 0.0;
        double lg = 0.0;
        double i = 1.0;

        try {
            con = ini.getConnRemota();
            sql = "SELECT codigoCliente,nombreDeCliente,nombreEstablecimiento,direccion,barrio,ciudad,"
                    + "telefono,latitud,longitud "
                    + " FROM rutero "
                    + "WHERE "
                    + "vehiculo='" + placa + "' and fechaDistribucion='" + fechaDistribucion + "' AND "
                    + "latitud <>'0' ;";

//            String cadenaDeConexion = String.format("jdbc:mysql://%s/%s?%s%s",
//                    "190.144.23.186",
//                    "rutero",
//                    "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota",
//                    "&useSSL=false");
//            con = DriverManager.getConnection(cadenaDeConexion, "luislopez", "jslslpzmjc1212");
            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setCodigoCliente(rst.getString("codigoCliente"));
                    cliente.setDireccion(rst.getString("direccion") + ",\n B. " + rst.getString("barrio") + ",\n" + rst.getString("ciudad"));
                    cliente.setIcon(icon);
                    cliente.setLatitud(rst.getDouble("latitud"));
                    cliente.setLongitud(rst.getDouble("longitud"));
                    cliente.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    cliente.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    cliente.setTelefono(rst.getString("telefono"));
                    cliente.setIcon(icon);

                    //LatLng punto = new LatLng(rst.getDouble("latitud"), rst.getDouble("longitud"));
                    // listadeClientes.add(cliente);
                    System.out.println("cliente codigo " + placa);

                    /**
                     * variables para calcular el centro del mapa en la ruta
                     */
                    lt += rst.getDouble("latitud");
                    lg += rst.getDouble("longitud");

                    String info = "<html><body>"
                            + "<table>"
                            + "<tr>"
                            + "<td>Placa Vehiculo: </td>"
                            + "<td>" + getPlaca() + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Nombre Conductor : </td>"
                            + "<td>" + getConductor() + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Id Cliente : </td>"
                            + "<td>" + rst.getString("codigoCliente") + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Nombre Cliente : </td>"
                            + "<td>" + rst.getString("nombreDeCliente") + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Establecimiento : </td>"
                            + "<td>" + rst.getString("nombreEstablecimiento") + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Direccion : </td>"
                            + "<td>" + rst.getString("direccion") + " <br>"
                            +  " B. " + rst.getString("barrio") + " <br>"
                            +  rst.getString("ciudad") + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Telefono : </td>"
                            + "<td>" + rst.getString("telefono") + "</td>"
                            + "</tr>"
                            + "<tr>"
                            + "<td>Ubicacion : </td>"
                            + "<td>" + rst.getDouble("latitud") + " ,  " + rst.getDouble("longitud") + "</td>"
                            + "</tr>"
                            + "</table>"
                            + "</body></html>";
                    final MyWayPoint marker = new MyWayPoint(
                            rst.getString("codigoCliente"),
                            new GeoPosition(rst.getDouble("latitud"),
                                    rst.getDouble("longitud")),
                            this.icon,
                            info);

                    wpClientes.add(marker);
                    System.out.println("Asignando marcador Cliente  a vehiculo " + getPlaca());

                }

                rst.close();
                st.close();
                // con.close();

            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ImageIcon getImageIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public List<MyWayPoint> getListaDeMarcadoresClientes() {
        return listaDeMarcadoresClientes;
    }

    public void setListaDeMarcadoresClientes(List<MyWayPoint> listaDeMarcadoresClientes) {
        this.listaDeMarcadoresClientes = listaDeMarcadoresClientes;
    }

    public void setListaDeMarcadoresClientes() {
        LatLng puntoFinal = null;

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.setGeodesic(true);
        lineOptions.setStrokeColor("#4480CC");
        lineOptions.setStrokeOpacity(1);
        lineOptions.setStrokeWeight(2);

        for (final PuntoDeRecorrido punto : getListaDePuntosDeRecorrido()) {

            GeoPosition a = punto.getPath();

            DecimalFormat formato = new DecimalFormat("#,###.##");
            String info = "<html><body>"
                    + "<table>"
                    + "<tr>"
                    + "<td>Id Movimiento : </td>"
                    + "<td>" + punto.getId() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Conductor : </td>"
                    + "<td>" + punto.getConductor() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Localizacion : </td>"
                    + "<td>" + punto.getPath().getLatitude() + "," + punto.getPath().getLongitude() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Orientacion : </td>"
                    + "<td>" + punto.getCourse() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Hora : </td>"
                    + "<td>" + punto.getServertime() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Velocidad : </td>"
                    + "<td>" + formato.format(punto.getSpeed()) + " Km/h </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Distancia : </td>"
                    + "<td>" + formato.format(punto.getDistancia()) + " mts </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Total Recorrido : </td>"
                    + "<td>" + formato.format(punto.getDistanciaRecorrida() / 1000) + " Km </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Odometro  : </td>"
                    + "<td>" + formato.format(punto.getKilometrajeFinal()) + " Km </td>"
                    + "</tr>"
                    + "</table>"
                    + "</body></html>";

            final MyWayPoint marker = new MyWayPoint(
                    "" + punto.getId(),
                    punto.getPath(),
                    punto.getIcon(),
                    info);

            // mapa.wpClientes.add(marker);
//            listaDeMarcadoresPuntoRecorridos.add(marker);
//            listaDePuntosRecorridosHash.add(marker);
        }
    }

    /*  
    public List<MyWayPoint> getListaDeMarcadoresRecorrido() {
        return listaDeMarcadoresPuntoRecorridos;
    }

    public void setListaDeMarcadoresRecorrido(List<MyWayPoint> listaDeMarcadoresRecorrido) {
        this.listaDeMarcadoresPuntoRecorridos = listaDeMarcadoresRecorrido;
    }

    public void setListaDeMarcadoresRecorrido() {
          LatLng puntoFinal = null;

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.setGeodesic(true);
        lineOptions.setStrokeColor("#4480CC");
        lineOptions.setStrokeOpacity(1);
        lineOptions.setStrokeWeight(2);
        
        listaDePuntosRecorridosHash = new HashSet<>();
        listaDeMarcadoresPuntoRecorridos = new ArrayList<>();
       

        for (final PuntoDeRecorrido punto : getListaDePuntosDeRecorrido()) {

            GeoPosition a = punto.getPath();

            DecimalFormat formato = new DecimalFormat("#,###.##");
            String info = "<html><body>"
                    + "<table>"
                    + "<tr>"
                    + "<td>Id Movimiento : </td>"
                    + "<td>" + punto.getId() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Conductor : </td>"
                    + "<td>" + punto.getConductor() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Localizacion : </td>"
                    + "<td>" + punto.getPath().getLatitude() + "," + punto.getPath().getLongitude() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Orientacion : </td>"
                    + "<td>" + punto.getCourse() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Hora : </td>"
                    + "<td>" + punto.getServertime() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Velocidad : </td>"
                    + "<td>" + formato.format(punto.getSpeed()) + " Km/h </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Distancia : </td>"
                    + "<td>" + formato.format(punto.getDistancia()) + " mts </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Total Recorrido : </td>"
                    + "<td>" + formato.format(punto.getDistanciaRecorrida() / 1000) + " Km </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Odometro  : </td>"
                    + "<td>" + formato.format(punto.getKilometrajeFinal()) + " Km </td>"
                    + "</tr>"
                    + "</table>"
                    + "</body></html>";

            final MyWayPoint marker = new MyWayPoint(
                    "" + punto.getId(),
                    punto.getPath(),
                    punto.getIcon(),
                    info);

            // mapa.wpClientes.add(marker);
            listaDeMarcadoresPuntoRecorridos.add(marker);
            listaDePuntosRecorridosHash.add(marker);
            

           

        }
    }
    
     */
    public List<Polyline> getListaDePoliLineas() {
        return listaDePoliLineas;
    }

    public void setListaDePoliLineas(List<Polyline> listaDePoliLineas) {
        this.listaDePoliLineas = listaDePoliLineas;
    }

    public List<PuntoDeRecorrido> getListaDePuntosDeRecorrido() {
        return listaDePuntosDeRecorrido;
    }
    
    public void setListaDePuntosDeRecorrido(List<PuntoDeRecorrido> listaDePuntosDeRecorrido) {
        this.listaDePuntosDeRecorrido = listaDePuntosDeRecorrido;
    }

    public boolean setListaDePuntosDeRecorrido() {

        boolean verificado = false;

        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        String sql = "";
        //String fechaDeInicio = "2022-06-21 06:00:00";
        //String fechaFinal = "2022-06-21 23:59:59";
        Double distanciaRecorrida = 0.0;
        List<PuntoDeRecorrido> listaDeWayPoints = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        //  c.add(Calendar.DATE, -300);
        Date nowMinus15 = c.getTime();
        //this.listaDePuntosDeRecorrido = new ArrayList<>();

        //"fechaDistribucion=(select DATE_SUB(curdate(),INTERVAL 1 day))
        try {
//            sql = "SELECT latitude,longitude,course,DATE_SUB(fechaDeInicio, INTERVAL 5 HOUR) as fechaDeInicio,speed,attributes,"
//                    + "JSON_EXTRACT(attributes,'$.distance')as distancia, round((JSON_EXTRACT(attributes,'$.totalDistance')/1000),2) as kilometraje "
//                    + "FROM tc_positions "
//                    + "WHERE "
//                    + "deviceid='" + device + "' and fechaDeInicio >= '" + fechaDeInicio + "' AND "
//                    + "fechaDeInicio<='" + fechaFinal + "'";

            sql = "SELECT p.id, d.name,p.latitude,p.longitude,p.course,"
                    // + "DATE_SUB(p.servertime, INTERVAL 5 HOUR) as servertime,"
                    + "p.servertime as servertime,"
                    // + "p.fechaDeInicio as fechaDeInicio,"
                    + "round(p.speed,2) as speed ,p.attributes, "
                    + "round(JSON_EXTRACT(p.attributes,'$.distance'),2) as distancia, "
                    + "round((JSON_EXTRACT(p.attributes,'$.totalDistance')/1000),2) as kilometraje, "
                    + "JSON_EXTRACT(d.attributes,'$.Kilometraje') as kilometrajeInical, "
                    + "round(sum(JSON_EXTRACT(d.attributes,'$.Kilometraje') + JSON_EXTRACT(p.attributes,'$.totalDistance')/1000),2) as kilometrajeFinal "
                    + " FROM tc_positions p "
                    + " join tc_devices  d on p.deviceid= d.id "
                    + " WHERE  "
                    + " d.name='" + this.placa + "' and  "
                    + " p.servertime "
                    // + "BETWEEN DATE_ADD('" + fechaDeInicio + "', interval 5 HOUR) AND "
                    // + "DATE_ADD('" + fechaFinal + "',INTERVAL 5 HOUR) "
                    + "BETWEEN '" + fechaDeInicio + "' AND "
                    + "'" + fechaFinal + "' "
                    // + "BETWEEN '" + fechaDeInicio + "' AND '" + fechaFinal + "' "
                    + "group by  p.id ;";
            //  + "order by p.id asc";

            String cadenaDeConexion = String.format("jdbc:mysql://%s/%s?%s%s",
                    "129.151.107.181",
                    "traccar",
                    "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota",
                    "&useSSL=false");
            // con = DriverManager.getConnection(cadenaDeConexion, "luislopez", "%jslslpzmjC12%");
            con= ini.getConnGPS();

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                int i = 0;
                while (rst.next()) {

                    System.out.println(" control consecutivo " + rst.getInt("id"));

                    GeoPosition punto = new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude"));

                    /* se valida el punto */
                    if (punto.getLatitude() != 0.0 && punto.getLongitude() != 0.0) {
                        Double curso = rst.getDouble("course");
                        PuntoDeRecorrido pr = new PuntoDeRecorrido();

                        pr.setId(rst.getInt("id"));
                        pr.setPlaca(rst.getString("name"));
                        pr.setPath(punto);
                        pr.setServertime(rst.getString("servertime"));
                        pr.setSpeed(rst.getDouble("speed") * 1.852); // velocidad dada en Nudos se cambia a KM/hora
                        pr.setAtributos(rst.getString("attributes"));
                        pr.setDistancia(rst.getDouble("distancia"));
                        pr.setKilometraje(rst.getDouble("kilometraje"));
                        Double kilometrajeFinal;
                        Double kilometrajeIncial = rst.getDouble("kilometrajeInical");

                        kilometrajeFinal = rst.getDouble("kilometraje") + rst.getDouble("kilometrajeInical");
                        pr.setKilometrajeFinal(rst.getDouble("kilometrajeFinal"));
                        pr.setConductor(getConductor());

                        distanciaRecorrida += rst.getDouble("distancia");

                        pr.setDistanciaRecorrida(distanciaRecorrida);

                        if ((curso >= 0.0 && curso <= 30.0) || (curso > 360.0 && curso <= 359.999)) {
                            pr.setIcon(iconNorte);
                            pr.setCourse("Norte");
                            //norte
                        } else if ((curso > 30.0 && curso <= 60.0)) {
                            //noreste
                            pr.setIcon(iconNorEste);
                            pr.setCourse("Noreste");

                        } else if ((curso > 60.0 && curso <= 120.0)) {
                            //es60
                            pr.setIcon(iconEste);
                            pr.setCourse("Este");

                        } else if ((curso > 120.0 && curso <= 150.0)) {
                            // sureste
                            pr.setIcon(iconSurEste);
                            pr.setCourse("Sur Este");

                        } else if ((curso > 150.0 && curso <= 210.0)) {
                            //sur
                            pr.setIcon(iconSur);
                            pr.setCourse("Sur");

                        } else if ((curso > 210.0 && curso <= 240.0)) {
                            // suroes40
                            pr.setIcon(iconSurOeste);
                            pr.setCourse("Sur Oeste");

                        } else if ((curso > 240.0 && curso <= 300.0)) {
                            //oeste
                            pr.setIcon(iconOeste);
                            pr.setCourse("Oeste");

                        } else if ((curso > 300.0 && curso <= 360.0)) {
                            //noroeste
                            pr.setIcon(iconNorOEste);
                            pr.setCourse("NorOeste");
                        }

                        listaDePuntosDeRecorrido.add(i++, pr);

                    }
                }

            }

            //listaDePuntosDeRecorrido = listaDeWayPoints;
            rst.close();
            st.close();
            // con.close();

            if (listaDePuntosDeRecorrido.size() > 0) {
                verificado = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        }

        return verificado;

    }

    public void borrarPuntosDeRecorrido() {
        listaDePuntosDeRecorrido.clear();
    }

    private void setIconos() {
        iconNorte = new ImageIcon();
        iconSur = new ImageIcon();
        iconEste = new ImageIcon();
        iconOeste = new ImageIcon();
        iconNorEste = new ImageIcon();
        iconSurEste = new ImageIcon();
        iconNorOEste = new ImageIcon();
        iconSurOeste = new ImageIcon();

        iconNorte = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_norte-23x23.png"), "png");
        iconSur = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_sur-23x23.png"), "png");
        iconEste = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_este-23x23.png"), "png");
        iconOeste = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_oeste-23x23.png"), "png");
        iconNorEste = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_noreste-23x23.png"), "png");
        iconSurEste = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_sureste-23x23.png"), "png");
        iconNorOEste = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_noroeste-23x23.png"), "png");
        iconSurOeste = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/flecha_suroeste-23x23.png"), "png");
    }

    public Double getOdometro() {
        return odometro;
    }

    public void setOdometro(Double odometro) {
        this.odometro = odometro;
    }

    public String getFechaDeInicio() {
        return fechaDeInicio;
    }

    public void setFechaDeInicio(String servertime) {
        this.fechaDeInicio = servertime;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String servertime2) {
        this.fechaFinal = servertime2;
    }

    public String getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(String fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public GeoPosition getUltimaUbicacion() {
        return ultimaUbicacion;
    }

    public void setUltimaUbicacion(GeoPosition ultimaUbicacion) {
        this.ultimaUbicacion = ultimaUbicacion;
    }

    /**
     * Funcion que Actualiza la ultima ubicacion del vehiculo. Este vehiculo se
     * encuentra dentro de la lista de vehiculos con GPS
     */
    public void setUltimaUbicacion() {

        ResultSet rst = null;
        Statement st = null;
        Connection con = ini.getConnGPS();
        String sql = "";
        //String fechaDistribucion = "2022-06-21";

        try {
            sql = "SELECT p.id, p.protocol, p.deviceid,d.name,d.positionid,d.attributes,d.lastupdate,"
                    + "JSON_EXTRACT(d.attributes,'$.Kilometraje') as kilometrajeInical, "
                    + "ROUND((JSON_EXTRACT(p.attributes,'$.totalDistance')/1000),2) as distanciaTotal,"
                    + "p.servertime as servertime,"
                    // + " p.fechaDeInicio, "
                    + "p.devicetime, p.fixtime, p.valid, p.latitude, "
                    + "p.longitude, p.altitude, p.speed, p.course, p.address, p.attributes, p.accuracy, p.network "
                    + "FROM tc_positions p "
                    + "join tc_devices d on d.id = p.deviceid "
                    + "where "
                    + "d.positionid = p.id  and "
                    + "d.name ='" + placa + "' ;";

            String cadenaDeConexion = String.format("jdbc:mysql://%s/%s?%s%s",
                    "129.151.107.181",
                    "traccar",
                    "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota",
                    "&useSSL=false");
            //con = DriverManager.getConnection(cadenaDeConexion, "luislopez", "%jslslpzmjC12%");

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {

                    setIdMovimiento(rst.getInt("id"));
                    setSpeed(rst.getDouble("speed"));
                    setPlaca(rst.getString("name"));
                    //setIcon(new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/truck_32x32.png")));
                    setFechaDeInicio(rst.getString("servertime"));
                    setFechaFinal("");
                    setFechaDistribucion(fechaDistribucion);
                    setUltimaUbicacion(new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude")));
                    setOdometro(rst.getDouble("kilometrajeInical") + rst.getDouble("distanciaTotal"));
                    setCourse(rst.getDouble("course"));

                    rst.close();

                    st.close();
                    //con.close();
                    setPuntoCarro();

                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Funcion que Actualiza la ultima ubicacion del vehiculo. Este vehiculo se
     * encuentra dentro de la lista de vehiculos con GPS
     */
    public GeoPosition getUltimaUbicacion(String placa) {

        GeoPosition punto = null;
        ResultSet rst = null;
        Statement st = null;
        Connection con = ini.getConnGPS();
                
                ;
        String sql = "";
        //String fechaDistribucion = "2022-06-21";

        try {
            sql = "SELECT "
                    + " p.latitude , p.longitude "
                    + "FROM tc_positions p "
                    + "join tc_devices d on d.id = p.deviceid "
                    + "where "
                    + "d.positionid = p.id  and "
                    + "d.name ='" + placa + "' ;";

            String cadenaDeConexion = String.format("jdbc:mysql://%s/%s?%s%s",
                    "129.151.107.181",
                    "traccar",
                    "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota",
                    "&useSSL=false");
            //con = DriverManager.getConnection(cadenaDeConexion, "luislopez", "%jslslpzmjC12%");

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {
                    setUltimaUbicacion(new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude")));
                    punto = new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude"));
                }

                rst.close();
                st.close();
                //con.close();

                //setPuntoCarro();
            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return punto;
    }

    private void setPuntoCarro() {

        NominatimReverseGeocodingJAPI nominatim1;
        Address addres = null;
        GeoPosition a;
        String direcccion;
        try {
            nominatim1 = new NominatimReverseGeocodingJAPI(); //create instance with default zoom level (18)

            addres = nominatim1.getAdress(getUltimaUbicacion().getLatitude(), getUltimaUbicacion().getLongitude()); //returns Address object for the given position

            direcccion = addres.getDisplayName();

        } catch (Exception e) {
            direcccion = getUltimaUbicacion().getLatitude() + "," + getUltimaUbicacion().getLongitude();
        }

        DecimalFormat formato = new DecimalFormat("#,###.##");
        String info = "<html><body>"
                + "<table>"
                + "<tr>"
                + "<td>Id Placa : </td>"
                + "<td WIDTH=300 >" + getPlaca() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Conductor : </td>"
                + "<td WIDTH=300>" + getConductor() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Localizacion : </td>"
                + "<td WIDTH=300>" + direcccion + "</td>"
                // + "<td>" + carro.getUltimaUbicacion().getLatitude() + "," + carro.getUltimaUbicacion().getLongitude() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Orientacion : </td>"
                + "<td WIDTH=300>" + getNombreCourse() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Hora : </td>"
                + "<td WIDTH=300>" + getFechaDeInicio() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Velocidad : </td>"
                + "<td WIDTH=300>" + formato.format(getSpeed()) + " Km/h </td>"
                + "</tr>"
                + "<tr>"
                + "<td>Odometro  : </td>"
                + "<td WIDTH=300>" + formato.format(getOdometro()) + " Km </td>"
                + "</tr>"
                + "</table>"
                + "</body></html>";

        final MyWayPoint marker = new MyWayPoint(
                "" + getPlaca(),
                getUltimaUbicacion(),
                info);
        puntoCarro = marker;

    }

    public MyWayPoint getPuntoCarro() {
        return puntoCarro;
    }

    public void setPuntoCarro(MyWayPoint puntoCarro) {
        this.puntoCarro = puntoCarro;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(Double course) {
        this.course = course;
        setNombreCourse();

    }

    public Double getSpeed() {
        return Speed;
    }

    public void setSpeed(Double Speed) {
        this.Speed = Speed;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getNombreCourse() {
        return nombreCourse;
    }

    public void setNombreCourse(String nombreCourse) {
        this.nombreCourse = nombreCourse;

    }

    public void setNombreCourse() {
        if ((course >= 0.0 && course <= 30.0) || (course > 360.0 && course <= 359.999)) {
            setNombreCourse("Norte");
            //norte
        } else if ((course > 30.0 && course <= 60.0)) {
            //noreste
            setNombreCourse("Noreste");

        } else if ((course > 60.0 && course <= 120.0)) {
            //es60
            setNombreCourse("Este");

        } else if ((course > 120.0 && course <= 150.0)) {
            // sureste
            setNombreCourse("Sur Este");

        } else if ((course > 150.0 && course <= 210.0)) {
            //sur
            setNombreCourse("Sur");

        } else if ((course > 210.0 && course <= 240.0)) {
            // suroes40
            setNombreCourse("Sur Oeste");

        } else if ((course > 240.0 && course <= 300.0)) {
            //oeste
            setNombreCourse("Oeste");

        } else if ((course > 300.0 && course <= 360.0)) {
            //noroeste
            setNombreCourse("NorOeste");
        }

    }
    /*
    public List<MyWayPoint> getListaDeMarcadoresPuntoRecorridos() {
        return listaDeMarcadoresPuntoRecorridos;
    }

    public void setListaDeMarcadoresPuntoRecorridos(List<MyWayPoint> listaDeMarcadoresPuntoRecorridos) {
        this.listaDeMarcadoresPuntoRecorridos = listaDeMarcadoresPuntoRecorridos;
    }

    public Set<MyWayPoint> getListaDePuntosRecorridos() {
        return listaDePuntosRecorridosHash;
    }

    public void setListaDePuntosRecorridos(Set<MyWayPoint> listaDePuntosRecorridos) {
        this.listaDePuntosRecorridosHash = listaDePuntosRecorridos;
    }
  
     */

}
