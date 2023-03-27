/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos;

import GPS.PuntosGps.Cliente;
import GPS.PuntosGps.PuntoDeRecorrido;
import GPS.PuntosGps.PuntosGps;
import GPS.wayPoints.MyWayPoint;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import aplicacionlogistica.distribucion.objetos.personas.CPersonas;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.PolylineOptions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import mtto.documentos.objetos.DocumentosPorVehiculo;
import mtto.ingresoDeRegistros.objetos.MantenimientosAsignados;
import mtto.ingresoDeRegistros.objetos.MantenimientosPorPlaca;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Usuario
 */
/**
 *
 * @author Usuario
 */
public class CCarros extends CVehiculos {

    int tipoServicio;
    int tipoCombustible;
    int kmInicial;
    int activoCarro;
    int conGps;
    ImageIcon iconClientes;
    ImageIcon iconoGPS;

    Tc_positions ultimaPosicion;
    Set<MyWayPoint> wpClientes;
    Set<MyWayPoint> wpPuntosRecorridos;
    List<CClientes> listaDeClientes;
    GeoPosition geoposition;
    int lastUpdate=0;

    ImageIcon iconNorte;
    ImageIcon iconSur;
    ImageIcon iconEste;
    ImageIcon iconOeste;
    ImageIcon iconNorEste;
    ImageIcon iconSurEste;
    ImageIcon iconNorOEste;
    ImageIcon iconSurOeste;
    // Set<MyWayPoint> wpClientes;

    List<MantenimientosPorPlaca> listaUltimosMantenimientos;
    List<MantenimientosAsignados> listaDeMantenimientosAsignados;
    List<PuntoDeRecorrido> listaDePuntosDeRecorrido;

    List<DocumentosPorVehiculo> listaDeDocumentosPorVehiculoVencidos;
    boolean vehiculoConDocumentosVencidos = false;

    public Set<MyWayPoint> getWpClientes() {
        return wpClientes;
    }

    public void setWpClientes(Set<MyWayPoint> wpClientes) {
        this.wpClientes = wpClientes;
    }

    public void setWpClientes() {
        this.wpClientes = wpClientes;
    }

    public List<CClientes> getListaDeClientes() {
        return listaDeClientes;
    }

    public void setListaDeClientes(List<CClientes> listaDeClientes) {
        this.listaDeClientes = listaDeClientes;
    }

    /*Ete metodo permte crear una lista de los clientes que un vehiculo tiene en
    distribucion en determinada fecha. igualmente puede crear la lista de los 
    geocodigos de los mismos clientes para ser ubicados enun mapa.*/
    public void setListaDeClientes(String fecha) {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        String sql = "";
        this.wpClientes = new HashSet<>();
        this.listaDeClientes = new ArrayList<>();

        double lt = 0.0;
        double lg = 0.0;
        double i = 1.0;

        try {
            con = ini.getConnRemota();

            sql = "SELECT codigoCliente,nitCliente,nombreDeCliente,nombreEstablecimiento,direccion,barrio,ciudad,"
                    + "telefono,latitud,longitud "
                    + " FROM rutero "
                    + "WHERE "
                    + "vehiculo='" + placa + "' and fechaDistribucion='" + fecha + "';";
            //  + " AND "
            //  + "latitud <>'0' ;";

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {

                    CClientes cliente = new CClientes(ini);
                    cliente.setCodigoInterno(rst.getString("codigoCliente"));
                    cliente.setNitCliente(rst.getString("nitCliente"));
                    cliente.setDireccion(rst.getString("direccion") + ",\n B. " + rst.getString("barrio") + ",\n" + rst.getString("ciudad"));
                    cliente.setBarrio(rst.getString("barrio"));
                    cliente.setIcon(iconClientes);
                    cliente.setLatitud(rst.getString("latitud"));
                    cliente.setLongitud(rst.getString("longitud"));
                    cliente.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    cliente.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    cliente.setCelularCliente(rst.getString("telefono"));
                    cliente.setIcon(iconClientes);

                    //LatLng punto = new LatLng(rst.getDouble("latitud"), rst.getDouble("longitud"));
                    listaDeClientes.add(cliente);
                    System.out.println("cliente codigo " + placa);

                    if (!cliente.getLatitud().equals("0") && !cliente.getLatitud().equals("0.0") ) {

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
                                + " B. " + rst.getString("barrio") + " <br>"
                                + rst.getString("ciudad") + "</td>"
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
                                this.iconClientes,
                                info);

                        wpClientes.add(marker);
                    }
                    System.out.println("Asignando marcador Cliente  a vehiculo " + getPlaca());

                }

                rst.close();
                st.close();
                // con.close();

            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

     /*Ete metodo permte crear una lista de los clientes que un vehiculo tiene en
    distribucion en determinada fecha. igualmente puede crear la lista de los 
    geocodigos de los mismos clientes para ser ubicados enun mapa.*/
    public void setListaDeClientes(String fecha, String numeroManifiesto) {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        String sql = "";
        this.wpClientes = new HashSet<>();
        this.listaDeClientes = new ArrayList<>();

        double lt = 0.0;
        double lg = 0.0;
        double i = 1.0;

        try {
            con = ini.getConnRemota();

            sql = "SELECT codigoCliente,nitCliente,nombreDeCliente,nombreEstablecimiento,direccion,barrio,ciudad,"
                    + "telefono,latitud,longitud "
                    + " FROM rutero "
                    + "WHERE "
                    + "vehiculo='" + placa + "' and "
                    + "fechaDistribucion='" + fecha + "' and "
                    + "numeroManifiesto ='" + numeroManifiesto + "';";
            //  + " AND "
            //  + "latitud <>'0' ;";

            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {

                    CClientes cliente = new CClientes(ini);
                    cliente.setCodigoInterno(rst.getString("codigoCliente"));
                    cliente.setNitCliente(rst.getString("nitCliente"));
                    cliente.setDireccion(rst.getString("direccion") + ",\n B. " + rst.getString("barrio") + ",\n" + rst.getString("ciudad"));
                    cliente.setBarrio(rst.getString("barrio"));
                    cliente.setCiudad(rst.getString("ciudad"));
                    cliente.setIcon(iconClientes);
                    cliente.setLatitud(rst.getString("latitud"));
                    cliente.setLongitud(rst.getString("longitud"));
                    cliente.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    cliente.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    cliente.setCelularCliente(rst.getString("telefono"));
                    cliente.setIcon(iconClientes);

                    //LatLng punto = new LatLng(rst.getDouble("latitud"), rst.getDouble("longitud"));
                    listaDeClientes.add(cliente);
                    System.out.println("cliente codigo " + placa);

                    if (!cliente.getLatitud().equals("0") && !cliente.getLatitud().equals("0.0") ) {

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
                                + " B. " + rst.getString("barrio") + " <br>"
                                + rst.getString("ciudad") + "</td>"
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
                                this.iconClientes,
                                info);

                        wpClientes.add(marker);
                    }
                    System.out.println("Asignando marcador Cliente  a vehiculo " + getPlaca());

                }

                rst.close();
                st.close();
                // con.close();

            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public ImageIcon getIconClientes() {
        return iconClientes;
    }

    public void setIconClientes(ImageIcon icon) {
        this.iconClientes = icon;
    }

    public ImageIcon getIconoGPS() {
        return iconoGPS;
    }

    public void setIconoGPS(ImageIcon iconoGPS) {
        this.iconoGPS = iconoGPS;
    }

    public List<MantenimientosPorPlaca> getListaUltimosMantenimientosPorPlaca() {
        return listaUltimosMantenimientos;
    }

    public void setListaUltimosMantenimientosPorPlaca(List<MantenimientosPorPlaca> listaUltimosMantenimientos) {
        this.listaUltimosMantenimientos = listaUltimosMantenimientos;
    }

    public void setListaUltimosMantenimientosPorPlaca() {
        ResultSet rst = null;
        Statement st;
        Connection con;

        MantenimientosPorPlaca mxp;
        con = ini.getConnRemota();
        String sql = "select mxp.idMantenimientosPorPlaca, mxp.placaVehiculo, mxp.idMantenimiento,tmv.nombreMantenimiento, "
                + "mxp.conductorVehiculo, p.nombres, p.apellidos,mxp.idNumeroDeorden, mxp.kilometraje, mxp.fechaMantenimiento, "
                + "mxp.zona, mxp.regional, mxp.agencia, mxp.observaciones, mxp.sucursalProveedor, "
                + "mxp.numeroFactura, mxp.valorfactura, mxp.activo, mxp.fechaIng, mxp.usuario, mxp.flag "
                + "from mantenimientosporplaca mxp "
                + "join tiposdemantenimientosvehiculos tmv on tmv.idMantenimiento = mxp.idMantenimiento "
                + "join personas p on p.cedula = mxp.conductorVehiculo "
                + "where mxp.placaVehiculo='" + this.placa + "' and mxp.activo=1";

        listaDeMantenimientosAsignados = new ArrayList();

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {

                    mxp = new MantenimientosPorPlaca(ini);

                    mxp.setIdMantenimientosPorPlaca(rst.getInt("idMantenimientosPorPlaca"));
                    mxp.setPlacaVehiculo(rst.getString("placaVehiculo"));
                    mxp.setIdtipoMantenimiento(rst.getInt("idMantenimiento"));
                    mxp.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                    mxp.setConductorVehiculo(rst.getString("conductorVehiculo"));
                    mxp.setNombreConductor(rst.getString("nombres"));
                    mxp.setApellidosConductor(rst.getString("apellidos"));
                    mxp.setIdNumeroDeorden(rst.getInt("idNumeroDeorden"));
                    mxp.setKilometrajeMantenimiento(rst.getInt("kilometraje"));
                    mxp.setFechaMantenimiento(rst.getDate("fechaMantenimiento"));
                    mxp.setZona(rst.getInt("zona"));
                    mxp.setNombreZona(rst.getString(""));
                    mxp.setRegional(rst.getInt("regional"));
                    mxp.setNombreRegional(rst.getString(""));
                    mxp.setAgencia(rst.getInt("agencia"));
                    mxp.setNombreAgencia(rst.getString(""));
                    mxp.setObservaciones(rst.getString("observaciones"));
                    mxp.setActivo(rst.getInt("activo"));
                    mxp.setFechaIng(rst.getDate("fechaIng"));
                    mxp.setUsuario(rst.getString("usuario"));
                    mxp.setFlag(rst.getInt("flag"));

                    listaDeMantenimientosPorPlaca.add(mxp);
                    Thread.sleep(10);
                }
                rst.close();
                st.close();
                //con.close();

            } // fin try // fin try // fin try // fin try
            catch (SQLException | InterruptedException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        this.listaUltimosMantenimientos = listaUltimosMantenimientos;
    }

    public List<MantenimientosAsignados> getListaDeMantenimientosAsignados() {
        return listaDeMantenimientosAsignados;
    }

    public void setListaDeMantenimientosAsignados(List<MantenimientosAsignados> listaDeMantenimientosAsignados) {
        this.listaDeMantenimientosAsignados = listaDeMantenimientosAsignados;
    }

    public void setListaDeMantenimientosAsignados() {
        ResultSet rst = null;
        Statement st;
        Connection con;

        MantenimientosAsignados ma;
        con = ini.getConnRemota();
        String sql = "select asm.placa, asm.idMantenimiento, tmv.nombreMantenimiento,asm.frecuencia, asm.activo "
                + " from asignacionmantenimientos asm "
                + "join tiposdemantenimientosvehiculos tmv on tmv.idMantenimiento = asm.idMantenimiento "
                + "where asm.placa='" + this.placa + "'";

        listaDeMantenimientosAsignados = new ArrayList();

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {

                    ma = new MantenimientosAsignados(ini);
                    ma.setPlaca(rst.getString("placa"));
                    ma.setIdMantenimiento(rst.getInt("idMantenimiento"));
                    ma.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                    ma.setFrecuencia(rst.getString("frecuencia"));
                    ma.setActivo(rst.getInt("activo"));

                    // System.out.println("Cargando vehiculo de placa  -> " + carro.getPlaca());
                    listaDeMantenimientosAsignados.add(ma);
                    Thread.sleep(10);
                }
                rst.close();
                st.close();
                //con.close();

            } // fin try // fin try // fin try // fin try
            catch (SQLException | InterruptedException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public boolean isVehiculoConDocumentosVencidos() {
        return vehiculoConDocumentosVencidos;
    }

    public void setVehiculoConDocumentosVencidos(boolean vehiculoConDocumentosVencidos) {
        this.vehiculoConDocumentosVencidos = vehiculoConDocumentosVencidos;
    }

    public void setListaDeDocumentosPorVehiculoVencidos(List<DocumentosPorVehiculo> listaDeDocumentosPorVehiculoVencidos) {
        this.listaDeDocumentosPorVehiculoVencidos = listaDeDocumentosPorVehiculoVencidos;
    }

    public List<DocumentosPorVehiculo> getListaDeDocumentosPorVehiculoVencidos() {
        return listaDeDocumentosPorVehiculoVencidos;
    }

    public void setListaDeDocumentosPorVehiculoVencidos() {
        try {
            Connection con;
            Statement st;
            ResultSet rst;

            String sq = "select "
                    + " dpv.idTipoDocumento,"
                    + "  dpv.numeroDocumento,"
                    + "  td.nombreTipoDocumento,"
                    + "  td.formato as prefijo,"
                    + "  IFNULL(dpv.entidadEmisora,'PENDIENTE') as entidadEmisora,"
                    + "  IFNULL(dpv.fechaExpedicion,'PENDIENTE') as fechaExpedicion,"
                    + "  dpv.lugarExpedicion,"
                    + "  IFNULL(dpv.fechaVencimiento,'PENDIENTE') as fechaVencimiento,"
                    + "  dpv.activo,"
                    + "  dpv.idConsecutivo,"
                    + "  dpv.placa,"
                    + "  dpv.usuario "
                    + "  from  documentosportipodevehiculo dtv "
                    + "  join vehiculos v on v.tipoVehiculo=dtv.idTipoVehiculo and v.placa='" + this.placa + "' "
                    + "  left outer join documentosporvehiculo dpv on dpv.idTipoDocumento=dtv.idTipoDocumento and dpv.placa='" + this.placa + "' and dpv.activo=1 "
                    + "  join tiposdocumentos td on td.idtiposDocumentos=dtv.idTipoDocumento "
                    + " where dtv.idTipoVehiculo= v.tipoVehiculo "
                    + " and dtv.activo=1;";

            String sql = "SELECT  dxv.idConsecutivo,dxv.placa, t.formato,t.nombreTipoDocumento,dxv.idTipoDocumento,"
                    + "dxv.numeroDocumento,dxv.formatoSoporteDocumento,dxv.entidadEmisora,dxv.lugarExpedicion,"
                    + "dxv.activo,dxv.fechaIng,dxv.usuario,if(dxv.fechaVencimiento < curdate(),1,0) as documentoVencido,"
                    + "dxv.fechaExpedicion,dxv.fechaVencimiento "
                    + "FROM documentosporvehiculo dxv "
                    + "join tiposdocumentos t on t.idtiposDocumentos=dxv.idTipoDocumento "
                    + "where dxv.activo = 1 and dxv.placa='" + this.placa + "' "
                    + "and (dxv.fechaVencimiento >= curdate() and dxv.fechaVencimiento <= (select date_add(curdate(),INTERVAL 15 DAY)) "
                    + "OR dxv.fechaVencimiento <= curdate());";

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            listaDeDocumentosPorVehiculoVencidos = new ArrayList<>();
            st = con.createStatement();
            rst = st.executeQuery(sq);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaActual = new Date();
            String fechaSistema = dateFormat.format(fechaActual);

            while (rst.next()) {
                DocumentosPorVehiculo dxv = new DocumentosPorVehiculo(ini);

                dxv.setActivo(rst.getInt("activo"));
                dxv.setEntidadEmisora(rst.getString("entidadEmisora"));
                dxv.setFechaExpedicion(rst.getString("fechaExpedicion"));
                dxv.setFechaVencimiento(rst.getString("fechaVencimiento"));
                dxv.setFormatoSoporteDocumento("pdf");
                dxv.setIdConsecutivo(rst.getInt("idConsecutivo"));
                dxv.setIdTipoDocumento(rst.getInt("idTipoDocumento"));
                dxv.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
                dxv.setPrefijo(rst.getString("prefijo"));
                dxv.setLugarExpedicion(rst.getString("lugarExpedicion"));
                dxv.setNumeroDocumento(rst.getString("NumeroDocumento"));
                dxv.setPlaca(rst.getString("placa"));
                dxv.setUsuario(rst.getString("usuario"));

                /* si no existe el documento */
                if (dxv.getEntidadEmisora().equals("PENDIENTE")) {
                    dxv.setObs("DOCUMENTO PENDIENTE POR INGRESAR ");
                    listaDeDocumentosPorVehiculoVencidos.add(dxv);
                } else {
                    Date fechaVencimiento = dateFormat.parse(dxv.getFechaVencimiento());
                    Date fechaAhora = dateFormat.parse(fechaSistema);

                    /* Documento Vencido */
                    if (fechaVencimiento.before(fechaAhora)) {
                        dxv.setObs("DOCUMENTO YA ESTA VENCIDO");
                        listaDeDocumentosPorVehiculoVencidos.add(dxv);

                    } else {

                        long diff = fechaVencimiento.getTime() - fechaAhora.getTime();

                        TimeUnit time = TimeUnit.DAYS;
                        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

                        /*Estan a punto de vencer */
                        if (diffrence <= 15) {
                            dxv.setObs("DOCUMENTO POR VENCER EN " + diffrence + " DIA(S)");
                            listaDeDocumentosPorVehiculoVencidos.add(dxv);
                        }
                        System.out.println("The difference in days is : " + diffrence);

                    }

                }

                Thread.sleep(10);
                System.out.println("documento vencido");

            }

            if (listaDeDocumentosPorVehiculoVencidos.size() == 0) {
                listaDeDocumentosPorVehiculoVencidos = null;
            }
            rst.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(CVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(int tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(int tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public int getActivoCarro() {
        return activoCarro;
    }

    public void setActivoCarro(int activoCarro) {
        this.activoCarro = activoCarro;
    }

    public int getConGps() {
        return conGps;
    }

    public void setConGps(int conGps) {
        this.conGps = conGps;
    }

    /*Este kilometraje se refiere a los kilometros recorridos antes de ingresar a la
    compañia */
    public int getKmInicial() {
        return kmInicial;
    }

    /*Este kilometraje se refiere a los kilometros recorridos antes de ingresar a la
    compañia */
    public void setKmInicial(int kmInicial) {
        this.kmInicial = kmInicial;
    }

    public CCarros() {

    }

    public CCarros(Inicio ini) {
        super(ini);
        setIconos();
    }

    public CCarros(Inicio ini, String placa) {
        super(ini);
        setIconos();
        String sql;
        ResultSet rst = null;
        Connection con;
        Statement st = null;
        this.ini = ini;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT * FROM vst_vehiculos where placa='" + placa + "';";

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {

                    this.placa = rst.getString("placa");
                    this.tipoTraccion = 1; // 1 corresponde a carros
                    this.conductor = rst.getString("conductor");
                    this.nombreConductor = rst.getString("nombreConductor");
                    this.apellidosConductor = rst.getString("apellidosConductor");
                    this.pesoTotalSinCarga = rst.getDouble("pesoTotalSinCarga");
                    this.largoVehiculo = rst.getDouble("largoVehiculo");
                    this.alturaVehiculo = rst.getDouble("alturaVehiculo");
                    this.anchuraVehiculo = rst.getDouble("anchuraVehiculo");
                    this.longitudVehiculo = rst.getDouble("longitudVehiculo");
                    this.pesoTotalAutorizado = rst.getDouble("pesoTotalAutorizado");
                    this.capacidadInstalada = rst.getDouble("capacidadInstalada");
                    this.idLineaVehiculo = rst.getInt("lineaVehiculo");
                    this.tipoVehiculo = rst.getInt("tipoVehiculo");
                    this.tipoCarroceria = rst.getInt("tipoCarroceria");
                    this.tipoContrato = rst.getInt("tipoContrato");
                    this.propietario = rst.getString("propietario");
                    this.tarjetaPropiedad = rst.getString("tarjetaPropiedad");
                    this.cantidadLLantas = rst.getInt("cantidadLLantas");
                    this.tamanioLlantas = rst.getString("tamanioLlantas");
                    this.serialChasis = rst.getString("serialChasis");
                    this.trailer = rst.getString("trailer");
                    this.agencia = rst.getInt("agencia");
                    this.modelo = rst.getString("modelo");
                    this.tipoServicio = rst.getInt("idTipoServicio");
                    this.serialMotor = rst.getString("serialMotor");
                    this.tipoCombustible = rst.getInt("idTipoCombustible");
                    this.kmCambioValvulinaTrasmision = rst.getInt("kmCambioValvulinaTrasmision");
                    this.kilometrajeActual = rst.getInt("kilometrajeActual");
                    this.kmCambioAceiteMotor = rst.getInt("kmCambioAceiteMotor");
                    this.kmCambioValvulinaCaja = rst.getInt("kmCambioValvulinaCaja");
                    this.idLineaVehiculo = rst.getInt("lineaVehiculo");
                    this.tipoMime = rst.getString("tipoMime");
                    this.activoVehiculo = rst.getInt("activo");
                    this.activoCarro = rst.getInt("activo");
                    this.conGps = rst.getInt("conGps");

//                    InputStream is = rst.getBinaryStream("imagen");
//                    BufferedImage bi = ImageIO.read(is);
//                    if (bi != null) {
//                        this.imagen = new ImageIcon(bi);
//                    } else {
//                        this.imagen = null;
//                 
                } else {
                    this.placa = null;

                }

                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en grabar carros " + ex);
                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }

    }

    public boolean grabarCarros() {
        boolean grab = false;
        try {

            String cadena = "INSERT INTO vehiculos(placa,tipoTraccion,conductor,pesoTotalSinCarga,largoVehiculo,alturaVehiculo,"
                    + "anchuraVehiculo,longitudVehiculo,pesoTotalAutorizado,capacidadInstalada,lineaVehiculo,tipoVehiculo,"
                    + "tipoCarroceria,tipoContrato,propietario,tarjetaPropiedad,cantidadLLantas,tamanioLlantas,serialChasis,"
                    + "trailer,agencia,tipoMime,kilometraje,activo,usuario) "
                    + "VALUES('"
                    + this.placa + "','"
                    + this.tipoTraccion + "','"
                    + this.conductor + "','"
                    + this.pesoTotalSinCarga + "','"
                    + this.largoVehiculo + "','"
                    + this.alturaVehiculo + "','"
                    + this.anchuraVehiculo + "','"
                    + this.longitudVehiculo + "','"
                    + this.pesoTotalAutorizado + "','"
                    + this.capacidadInstalada + "','"
                    + this.idLineaVehiculo + "','"
                    + this.tipoVehiculo + "','"
                    + this.tipoCarroceria + "','"
                    + this.tipoContrato + "','"
                    + this.propietario + "','"
                    + this.tarjetaPropiedad + "','"
                    + this.cantidadLLantas + "','"
                    + this.tamanioLlantas + "','"
                    + this.serialChasis + "','"
                    + this.trailer + "','"
                    + this.agencia + "','"
                    + this.tipoMime + "','"
                    + this.kilometrajeActual + "','"
                    + this.activoVehiculo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                    + "  ON DUPLICATE KEY UPDATE "
                    + "tipoTraccion='" + this.tipoTraccion + "',"
                    + "conductor='" + this.conductor + "',"
                    + "pesoTotalSinCarga='" + this.pesoTotalSinCarga + "',"
                    + "largoVehiculo='" + this.largoVehiculo + "',"
                    + "alturaVehiculo='" + this.alturaVehiculo + "',"
                    + "anchuraVehiculo='" + this.anchuraVehiculo + "',"
                    + "longitudVehiculo='" + this.longitudVehiculo + "',"
                    + "pesoTotalAutorizado='" + this.pesoTotalAutorizado + "',"
                    + "capacidadInstalada='" + this.capacidadInstalada + "',"
                    + "lineaVehiculo='" + this.idLineaVehiculo + "',"
                    + "tipoVehiculo='" + this.tipoVehiculo + "',"
                    + "tipoCarroceria='" + this.tipoCarroceria + "',"
                    + "tipoContrato='" + this.tipoContrato + "',"
                    + "propietario='" + this.propietario + "',"
                    + "tarjetaPropiedad='" + this.tarjetaPropiedad + "',"
                    + "cantidadLLantas='" + this.cantidadLLantas + "',"
                    + "tamanioLlantas='" + this.tamanioLlantas + "',"
                    + "serialChasis='" + this.serialChasis + "',"
                    + "trailer='" + this.trailer + "',"
                    + "agencia='" + this.agencia + "',"
                    + "tipoMime='" + this.tipoMime + "',"
                    + "kilometraje='" + this.kilometrajeActual + "',"
                    + "activo='" + this.activoVehiculo + "',"
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "'; ";

            ini.insertarDatosRemotamente(cadena);
            insertarFofografia(1);

            cadena = "INSERT INTO carros(placa,modelo,tipoServicio,serialMotor,tipoCombustible,"
                    + "kmCambioValvulinaTrasmision,kmCambioAceiteMotor,kmCambioValvulinaCaja,kmInicial,"
                    + "activo,usuario,flag) VALUES('"
                    + this.placa + "','"
                    + this.modelo + "','"
                    + this.tipoServicio + "','"
                    + this.serialMotor + "','"
                    + this.tipoCombustible + "','"
                    + this.kmCambioValvulinaTrasmision + "','"
                    + this.kmCambioAceiteMotor + "','"
                    + this.kmCambioValvulinaCaja + "','"
                    + this.kmInicial + "','"
                    + this.activoCarro + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1) "
                    + " ON DUPLICATE KEY UPDATE "
                    + "modelo='" + this.modelo + "',"
                    + "tipoServicio='" + this.tipoServicio + "',"
                    + "serialMotor='" + this.serialMotor + "',"
                    + "tipoCombustible='" + this.tipoCombustible + "',"
                    + "kmCambioValvulinaTrasmision='" + this.kmCambioValvulinaTrasmision + "',"
                    + "kmCambioAceiteMotor='" + this.kmCambioAceiteMotor + "',"
                    + "kmCambioValvulinaCaja='" + this.kmCambioValvulinaCaja + "',"
                    + "kmInicial='" + this.kmInicial + "',"
                    + "activo='" + this.activoCarro + "';";

            grab = this.ini.insertarDatosRemotamente(cadena);

            if (tipoContrato == 1) {
                insertarFofografia(2);
            }

        } catch (Exception ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabar carros " + ex);
            grab = false;
        }

        return grab;
    }

    public ArrayList<String> buscarCarros(String placa) {
        ArrayList<String> lista = null;
        Connection con;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        if (placa.equals("")) {
            sql = "SELECT vehiculos.placa FROM vehiculos, carros where "
                    + "vehicuclos.placa=carros.placa; ";

        } else {
            sql = "SELECT vehiculos.placa FROM vehiculos, carros where "
                    + "vehicuclos.placa=carros.placa  and "
                    + "vehicuclos.placa like '%" + placa + "%';";
        }

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                lista = new ArrayList();
                while (rst.next()) {
                    lista.add(rst.getString(1));
                }
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en grabar carros " + ex);
                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return lista;
    }

    public static String arrListadoDeVehiculos(int caso) {
        String sql = null;

        switch (caso) {

            case 1: // caso en que la consulta sea del area de operaciones
                sql = "select * from vst_vehiculos order by placa asc ;";
                break;
            case 2:  // caso en que la consulta sea del area de Mantenimientos
                sql = "select * from vst_vehiculos where tipoContrato=1 and activo=1  order by placa asc ;";
                break;
        }

        return sql;
    }

    private synchronized void liberarCarro(boolean liberar) {
        String sql;
        if (liberar) {
            sql = "update carros set isFree= 1 where placa='" + this.placa + "';";
        } else {
            sql = "update carros set isFree= 0 where placa='" + this.placa + "';";
        }
        ini.insertarDatosRemotamente(sql);
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    
    
    public Set<MyWayPoint> getWpPuntosRecorridos() {
        return wpPuntosRecorridos;
    }

    public void setWpPuntosRecorridos(Set<MyWayPoint> wpPuntosRecorridos) {
        this.wpPuntosRecorridos = wpPuntosRecorridos;
    }

    public Tc_positions getUltimaPosicion() {

        return ultimaPosicion;
    }

    public void setUltimaPosicion(Tc_positions ultimaPosicion) {

        this.ultimaPosicion = ultimaPosicion;
        setGeoposition();
    }

    /*Metodo que permite asignar la ultima posicion reportada por el gps al sistema*/
    public void setUltimaPosicion() {

        this.ultimaPosicion = new Tc_positions(ini, placa);
        setGeoposition();

    }

    public GeoPosition getGeoposition() {
        return geoposition;
    }

    public void setGeoposition(GeoPosition geoposition) {
        this.geoposition = geoposition;
    }

    public void setGeoposition() {
        if (ultimaPosicion != null) {
            this.geoposition = new GeoPosition(Double.parseDouble(ultimaPosicion.getLatitud()),
                    Double.parseDouble(ultimaPosicion.getLongitud()));
        }
    }

    public List<PuntoDeRecorrido> getListaDePuntosDeRecorrido() {
        return listaDePuntosDeRecorrido;
    }

    public void setListaDePuntosDeRecorrido(List<PuntoDeRecorrido> listaDePuntosDeRecorrido) {
        this.listaDePuntosDeRecorrido = listaDePuntosDeRecorrido;
    }

    public boolean setListaDePuntosDeRecorrido(String fechaDeInicio, String fechaFinal) {

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
            con = ini.getConnGPS();

            if (con != null) {
                listaDePuntosDeRecorrido = new ArrayList<>();

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
                
                this.lastUpdate = rst.getInt("id");
                
                }

            }

            asignarWPPuntosRecorridos();

            rst.close();
            st.close();
            // con.close();

            if (listaDePuntosDeRecorrido.size() > 0) {
                verificado = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        }

        return verificado;

    }
    public boolean setListaDePuntosDeRecorrido(int lastUpdate) {

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
        Date nowMinus15 = c.getTime();
        try {

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
                    + " d.name='" + this.placa + "'  "
                    + "AND p.id >='" + this.lastUpdate +"' "
                    + "group by  p.id ;";
            //  + "order by p.id asc";
            con = ini.getConnGPS();

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
                   /* Actualiza la ultima posicion */
                    this.lastUpdate = rst.getInt("id");
                }

            }

            asignarWPPuntosRecorridos();

            rst.close();
            st.close();
            // con.close();

            if (listaDePuntosDeRecorrido.size() > 0) {
                verificado = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        }

        return verificado;

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

    private void asignarWPPuntosRecorridos() throws InterruptedException {
        LatLng puntoFinal = null;

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.setGeodesic(true);
        lineOptions.setStrokeColor("#4480CC");
        lineOptions.setStrokeOpacity(1);
        lineOptions.setStrokeWeight(0.8);

        wpPuntosRecorridos = new HashSet<>();
//       /mapa.carro.listaDeMarcadoresClientes = new ArrayList<>();

        int i = 0;
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
            wpPuntosRecorridos.add(marker);

            Thread.sleep(1);

        }

    }

}
