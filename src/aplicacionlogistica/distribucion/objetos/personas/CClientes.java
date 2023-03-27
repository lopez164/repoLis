/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.personas;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class CClientes {//extends Inicio {

    String codigoInterno;
    String nitCliente;
    String nombreEstablecimiento;
    String nombreDeCliente;
    String direccion;
    String barrio;
    String ciudad;
    String clasificacion;
    String celularCliente;
    String emailCliente;
    Date fechaDeIngresoCliente;
    String latitud;
    String longitud;
    int canalDeVenta;
    int ruta;
    int frecuencia;
    int zona;
    int regional;
    int agencia;
    double porcentajeDescuento;
    int activoCliente;
    Inicio ini;
    List<CFacturas> listaDeFacturasPorCliente;
    List<Vst_FacturasDescargadas> listaDeFacturasdescargadas;
    
    ImageIcon icon;

    /**
     * Método que devuelve un String correspondiente al codigo de identificación
     * del cliente,
     *
     * @return una cadena con el código del cliente
     */
    public String getCodigoInterno() {
        return codigoInterno;
    }

    /**
     * Método que asiga un String correspondiente al codigo de identificación
     * del cliente,
     *
     * @param codigoInterno String correspondiente al codigo de identificación
     * del cliente
     */
    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    /**
     * Método que devuelve un String correspondiente al NIT(Número de
     * dentificación Tributaria) asignado por organismo estatal
     *
     * @return una cadena con el NIT del cliente
     */
    public String getNitCliente() {
        return nitCliente;
    }

    /**
     * Método que asigna un String correspondiente al NIT(Número de
     * dentificación Tributaria) asignado por organismo estatal
     *
     * @param nitCliente una cadena con el NIT del cliente
     */
    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    /**
     * Método que devuelve un String correspondiente al nombre el
     * establecimiento, negocio
     *
     * @return una cadena con el nombre el establecimiento, negocio
     */
    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    /**
     * Método que asigna un String correspondiente al nombre el establecimiento,
     * negocio
     *
     * @param nombreEstablecimiento un String correspondiente al nombre el
     * establecimiento, negocio
     */
    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    /**
     * Método que devuelve un String correspondiente al nombre del dueño,
     * administrador del establecimiento, negocio
     *
     * @return una cadena con el nombre el establecimiento, negocio
     */
    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    /**
     * Método que asigna un String correspondiente al nombre del dueño,
     * administrador del establecimiento, negocio
     *
     * @param nombreDeCliente una cadena ombre del dueño, administrador del
     * establecimiento, negocio
     */
    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public Date getFechaDeIngresoCliente() {
        return fechaDeIngresoCliente;
    }

    public void setFechaDeIngresoCliente(Date fechaDeIngresoCliente) {
        this.fechaDeIngresoCliente = fechaDeIngresoCliente;
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

    public int getCanalDeVenta() {
        return canalDeVenta;
    }

    public void setCanalDeVenta(int canalDeVenta) {
        this.canalDeVenta = canalDeVenta;
    }

    public int getRuta() {
        return ruta;
    }

    public void setRuta(int ruta) {
        this.ruta = ruta;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public int getActivoCliente() {
        return activoCliente;
    }

    public void setActivoCliente(int activoCliente) {
        this.activoCliente = activoCliente;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    
    
    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public List<Vst_FacturasDescargadas> getListaDeFacturasdescargadas() {
        return listaDeFacturasdescargadas;
    }

    public void setListaDeFacturasdescargadas(List<Vst_FacturasDescargadas> listaDeFacturasdescargadas) {
        this.listaDeFacturasdescargadas = listaDeFacturasdescargadas;
    }

    public List<CFacturas> getListaDeFacturasPorCliente() {
        return listaDeFacturasPorCliente;
    }

    public void setListaDeFacturasPorCliente(List<CFacturas> listaDeFacturasPorCliente) {
        this.listaDeFacturasPorCliente = listaDeFacturasPorCliente;
    }

    /**
     * Método que asigna las factura movilizadas por un cliente en los últimos
     * 30 dias calendario
     *
     */
    public void setListaDeFacturasPorCliente() throws Exception {
        listaDeFacturasPorCliente = new ArrayList();
        Connection con;
        Statement st;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        String sql = "SELECT * "
                + "FROM  vst_fcturas "
                + "WHERE "
                + "cliente='" + this.codigoInterno + "' AND "
                + "fechaDeVenta >= DATE_SUB(NOW(),INTERVAL 30 DAY)";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturas factura = new CFacturas(ini, rst.getString("numeroFactura"));
                    factura.setNumeroDeFactura(rst.getString("numeroFactura"));
                    factura.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    factura.setCodigoDeCliente(rst.getString("idCliente"));
                    factura.setNitCliente(rst.getString("nitCliente"));
                    factura.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    factura.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    factura.setDireccion(rst.getString("direccionDeCliente"));
                    factura.setClasificacionCliente(rst.getString("clasificacionCliente"));
                    factura.setBarrio(rst.getString("barrio"));
                    factura.setCiudad(rst.getString("ciudad"));
                    factura.setTelefono(rst.getString("telefonoCliente"));
                    factura.setVendedor(rst.getString("vendedor"));
                    factura.setFormaDePago(rst.getString("formaDePago"));
                    factura.setCanal(rst.getInt("canal"));
                    factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    //factura.setvalorIvaFactura(rst.getDouble("valorIvaFactura"));
                    factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    factura.setValorRechazo(rst.getDouble("valorRechazo"));
                    factura.setValorDescuento(rst.getDouble("valorDescuento"));
                    factura.setPlazoDias(rst.getInt("plazoDias"));
                    factura.setTelefonoVendedor(rst.getString("telefonoVendedor"));
                    factura.setZona(rst.getInt("zona"));
                    factura.setRegional(rst.getInt("regional"));
                    factura.setAgencia(rst.getInt("agencia"));
                    factura.setIsFree(rst.getInt("isFree"));
                    factura.setActivoFactura(rst.getInt("activo"));
                    factura.setEstadoFactura(rst.getInt("estadoFactura"));
                    factura.setNombreEstadoFactura(rst.getString("nombreEstadoFactura"));
                    factura.setNumeroDescuento(rst.getString("numeroDescuento"));
                    factura.setNumeroRecogida(rst.getString("numeroRecogida"));
                    factura.setPesofactura(rst.getDouble("pesofactura"));
                    factura.setTrasmitido(rst.getInt("trasmitido"));
                    factura.setNombreMovimiento(rst.getString("nombreEstadoFactura"));
                    listaDeFacturasPorCliente.add(factura);
                }

                // listaDeFacturasPorCliente;
            } catch (SQLException ex) {
                Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void setListaDeFacturasPorCliente(boolean remoto) {
        listaDeFacturasPorCliente = new ArrayList();
        Connection con;
        Statement st;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        String sql = "SELECT * "
                + "FROM  vst_fcturas "
                + "WHERE "
                + "idCliente='" + this.codigoInterno + "' AND "
                + "fechaDeVenta >= DATE_SUB(NOW(),INTERVAL 360 DAY) order by fechaDeVenta desc";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturas factura = new CFacturas(ini);

                    factura.setNumeroDeFactura(rst.getString("numeroFactura"));
                    factura.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    factura.setCodigoDeCliente(rst.getString("idCliente"));
                    factura.setNitCliente(rst.getString("nitCliente"));
                    factura.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    factura.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    factura.setDireccion(rst.getString("direccionDeCliente"));
                    factura.setClasificacionCliente(rst.getString("clasificacionCliente"));
                    factura.setBarrio(rst.getString("barrio"));
                    factura.setCiudad(rst.getString("ciudad"));
                    factura.setTelefono(rst.getString("telefonoCliente"));
                    factura.setVendedor(rst.getString("vendedor"));
                    factura.setFormaDePago(rst.getString("formaDePago"));
                    factura.setCanal(rst.getInt("canal"));
                    factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    //factura.setvalorIvaFactura(rst.getDouble("valorIvaFactura"));
                    factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    factura.setValorRechazo(rst.getDouble("valorRechazo"));
                    factura.setValorDescuento(rst.getDouble("valorDescuento"));
                    factura.setPlazoDias(rst.getInt("plazoDias"));
                    factura.setTelefonoVendedor(rst.getString("telefonoVendedor"));
                    factura.setZona(rst.getInt("zona"));
                    factura.setRegional(rst.getInt("regional"));
                    factura.setAgencia(rst.getInt("agencia"));
                    factura.setIsFree(rst.getInt("isFree"));
                    factura.setActivoFactura(rst.getInt("activo"));
                    factura.setEstadoFactura(rst.getInt("estadoFactura"));
                    factura.setNombreEstadoFactura(rst.getString("nombreEstadoFactura"));
                    factura.setNumeroDescuento(rst.getString("numeroDescuento"));
                    factura.setNumeroRecogida(rst.getString("numeroRecogida"));
                    factura.setPesofactura(rst.getDouble("pesofactura"));
                    factura.setTrasmitido(rst.getInt("trasmitido"));
                    factura.setNombreMovimiento(rst.getString("nombreEstadoFactura"));

                    factura.setPlazoDias(rst.getInt("plazoDias"));
                    factura.setPrefijo(rst.getString("prefijo"));
                    factura.setNumero(rst.getString("numero"));
                    factura.setFpContado(rst.getDouble("fpContado"));

                    listaDeFacturasPorCliente.add(factura);
                }

            } catch (SQLException ex) {
                Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void setListaDeFacturasDescargadas(boolean remoto) {
        listaDeFacturasdescargadas = new ArrayList();
        Connection con;
        Statement st;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        String sql = "SELECT * "
                + "FROM  vst_movilizacionfacturasdescargadas "
                + "WHERE "
                + "cliente='" + this.codigoInterno + "' AND "
                + "fechaDeVenta >= DATE_SUB(NOW(),INTERVAL 360 DAY) order by fechaDeVenta desc";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    Vst_FacturasDescargadas registro = new Vst_FacturasDescargadas();

                    registro.setIdCanal(rst.getInt("idCanalDistribucion"));
                    registro.setNombreCanal(rst.getString("nombreCanalDistribucion"));
                    registro.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    registro.setAdherencia(rst.getInt("adherencia"));
                    registro.setNumeroFactura(rst.getString("numeroFactura"));
                    registro.setFechaDistribucion(rst.getDate("fechaDistribucion"));
                    registro.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    registro.setVehiculo(rst.getString("vehiculo"));
                    registro.setTipoContrato(rst.getString("tipoContrato"));
                    registro.setConductor(rst.getString("conductor"));
                    registro.setNombreConductor(rst.getString("nombreConductor"));
                    registro.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    registro.setTipoRuta(rst.getString("tipoRuta"));
                    registro.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    registro.setDireccion(rst.getString("direccion"));
                    registro.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    registro.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    registro.setValorRechazo(rst.getDouble("valorRechazo"));
                    registro.setValorDescuento(rst.getDouble("valorDescuento"));
                    registro.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    registro.setIdTipoDeMovimiento(rst.getInt("idTipoMovimiento"));
                    registro.setNombreTipoDeMovimiento(rst.getString("nombreTipoDeMovimiento"));
                    registro.setCausalDeRechazo(rst.getInt("idMotivoRechazo"));
                    registro.setNombreCausalDeRechazo(rst.getString("nombreCausalDeRechazo"));
                    registro.setCompetencia(rst.getString("competencia"));
                    registro.setVendedor(rst.getString("vendedor"));
                    registro.setSalidasDistribucion(rst.getInt("salidasDistribucion"));

                    listaDeFacturasdescargadas.add(registro);
                }

                // listaDeFacturasPorCliente;
            } catch (SQLException ex) {
                Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public CClientes() {
    }

    public CClientes(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CClientes(Inicio ini, String codigoCliente) throws Exception {
        this.ini = ini;

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

            sql = "SELECT codigoInterno,"
                    + "nitCliente,"
                    + "nombreEstablecimiento,"
                    + "nombreDeCliente,"
                    + "direccion,"
                    + "barrio,"
                    + "ciudad,"
                    + "clasificacion,"
                    + "celularCliente,"
                    + "emailCliente,"
                    + "fechaDeIngresoCliente,"
                    + "latitud,"
                    + "longitud,"
                    + "canalDeVenta,"
                    + "ruta,"
                    + "frecuencia,"
                    + "zona,"
                    + "regional,"
                    + "agencia,"
                    + "porcentajeDescuento,"
                    + "activo "
                    + "FROM  clientes "
                    + "WHERE "
                    + "codigoInterno='" + codigoCliente + "'";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.codigoInterno = rst.getString("codigoInterno");
                    this.nitCliente = rst.getString("nitCliente");
                    this.nombreEstablecimiento = rst.getString("nombreEstablecimiento");
                    this.nombreDeCliente = rst.getString("nombreDeCliente");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.ciudad = rst.getString("ciudad");
                    this.clasificacion = rst.getString("clasificacion");
                    this.celularCliente = rst.getString("celularCliente");
                    this.emailCliente = rst.getString("emailCliente");
                    this.fechaDeIngresoCliente = rst.getDate("fechaDeIngresoCliente");
                    this.latitud = rst.getString("latitud");
                    this.longitud = rst.getString("longitud");
                    this.canalDeVenta = rst.getInt("canalDeVenta");
                    this.ruta = rst.getInt("ruta");
                    this.frecuencia = rst.getInt("frecuencia");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.porcentajeDescuento = rst.getInt("porcentajeDescuento");
                    this.activoCliente = rst.getInt("activo");

                } else {
                    this.codigoInterno = null;
                    this.nitCliente = "";
                    this.nombreEstablecimiento = "";
                    this.nombreDeCliente = "";
                    this.direccion = "";
                    this.barrio = "";
                    this.ciudad = "";
                    this.clasificacion = "";
                    this.celularCliente = "";
                    this.emailCliente = "";
                    this.fechaDeIngresoCliente = null;
                    this.activoCliente = 0;
                    this.latitud = "";
                    this.longitud = "";
                    this.canalDeVenta = 0;
                    this.ruta = 0;
                    this.frecuencia = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.porcentajeDescuento = 0.0;
                    this.activoCliente = 0;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar clientes consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CClientes(Inicio ini, String codigoCliente, boolean remoto) throws Exception {
        this.ini = ini;

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT codigoInterno,"
                    + "nitCliente,"
                    + "nombreEstablecimiento,"
                    + "nombreDeCliente,"
                    + "direccion,"
                    + "barrio,"
                    + "ciudad,"
                    + "clasificacion,"
                    + "celularCliente,"
                    + "emailCliente,"
                    + "fechaDeIngresoCliente,"
                    + "latitud,"
                    + "longitud,"
                    + "canalDeVenta,"
                    + "ruta,"
                    + "frecuencia,"
                    + "zona,"
                    + "regional,"
                    + "agencia,"
                    + "porcentajeDescuento,"
                    + "activo "
                    + " FROM  clientes "
                    + "WHERE "
                    + "codigoInterno='" + codigoCliente + "'";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.codigoInterno = rst.getString("codigoInterno");
                    this.nitCliente = rst.getString("nitCliente");
                    this.nombreEstablecimiento = rst.getString("nombreEstablecimiento");
                    this.nombreDeCliente = rst.getString("nombreDeCliente");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.ciudad = rst.getString("ciudad");
                    this.clasificacion = rst.getString("clasificacion");
                    this.celularCliente = rst.getString("celularCliente");
                    this.emailCliente = rst.getString("emailCliente");
                    this.fechaDeIngresoCliente = rst.getDate("fechaDeIngresoCliente");
                    this.latitud = rst.getString("latitud");
                    this.longitud = rst.getString("longitud");
                    this.canalDeVenta = rst.getInt("canalDeVenta");
                    this.ruta = rst.getInt("ruta");
                    this.frecuencia = rst.getInt("frecuencia");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.porcentajeDescuento = rst.getInt("porcentajeDescuento");
                    this.activoCliente = rst.getInt("activo");

                } else {
                    this.codigoInterno = null;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar clientes consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CClientes(Inicio ini, String nombreCliente, String nombreEstablecimiento) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            sql = "SELECT codigoInterno,"
                    + "nitCliente,"
                    + "nombreEstablecimiento,"
                    + "nombreDeCliente,"
                    + "direccion,"
                    + "barrio,"
                    + "ciudad,"
                    + "clasificacion,"
                    + "celularCliente,"
                    + "emailCliente,"
                    + "fechaDeIngresoCliente,"
                    + "latitud,"
                    + "longitud,"
                    + "canalDeVenta,"
                    + "ruta,"
                    + "frecuencia,"
                    + "zona,"
                    + "regional,"
                    + "agencia,"
                    + "porcentajeDescuento,"
                    + "activo,"
                    + "FROM clientes "
                    + "WHERE ";

            if (!nombreCliente.isEmpty() || nombreCliente != null) {
                sql += "nombreDeCliente=" + nombreCliente + "' ";
            } else {
                sql += "nombreEstablecimiento=" + nombreEstablecimiento + "'; ";
            }

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.codigoInterno = rst.getString("codigoInterno");
                    this.nitCliente = rst.getString("nitCliente");
                    this.nombreEstablecimiento = rst.getString("nombreEstablecimiento");
                    this.nombreDeCliente = rst.getString("nombreDeCliente");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.ciudad = rst.getString("ciudad");
                    this.clasificacion = rst.getString("clasificacion");
                    this.celularCliente = rst.getString("celularCliente");
                    this.emailCliente = rst.getString("emailCliente");
                    this.fechaDeIngresoCliente = rst.getDate("fechaDeIngresoCliente");
                    this.latitud = rst.getString("latitud");
                    this.longitud = rst.getString("longitud");
                    this.canalDeVenta = rst.getInt("canalDeVenta");
                    this.ruta = rst.getInt("ruta");
                    this.frecuencia = rst.getInt("frecuencia");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.porcentajeDescuento = rst.getInt("porcentajeDescuento");
                    this.activoCliente = rst.getInt("activo");

                } else {
                    this.codigoInterno = "";
                    this.nitCliente = "";
                    this.nombreEstablecimiento = "";
                    this.nombreDeCliente = "";
                    this.direccion = "";
                    this.barrio = "";
                    this.ciudad = "";
                    this.clasificacion = "";
                    this.celularCliente = "";
                    this.emailCliente = "";
                    this.fechaDeIngresoCliente = null;
                    this.activoCliente = 0;
                    this.latitud = "";
                    this.longitud = "";
                    this.canalDeVenta = 0;
                    this.ruta = 0;
                    this.frecuencia = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.porcentajeDescuento = 0.0;
                    this.activoCliente = 0;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar Clientes consulta sql " + ex.getMessage());
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarClientes() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO clientes(codigoInterno,nitCliente,"
                    + "nombreEstablecimiento,nombreDeCliente,direccion,barrio,"
                    + "ciudad,clasificacion,celularCliente,emailCliente,"
                    + "fechaDeIngresoCliente,latitud,longitud,canalDeVenta,"
                    + "ruta,frecuencia,zona,regional,agencia,porcentajeDescuento,"
                    + "activo,usuario,flag) VALUES ('"
                    + this.codigoInterno + "','"
                    + this.nitCliente + "','"
                    + this.nombreEstablecimiento + "','"
                    + this.nombreDeCliente + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.ciudad + "','"
                    + this.clasificacion + "','"
                    + this.celularCliente + "','"
                    + this.emailCliente + "','"
                    + this.fechaDeIngresoCliente + "','"
                    + this.latitud + "','"
                    + this.longitud + "','"
                    + this.canalDeVenta + "','"
                    + this.ruta + "','"
                    + this.frecuencia + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + this.porcentajeDescuento + "','"
                    + this.activoCliente + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + "codigoInterno='" + this.codigoInterno + "',"
                    + "nitCliente='" + this.nitCliente + "',"
                    + "nombreEstablecimiento='" + this.nombreEstablecimiento + "',"
                    + "nombreDeCliente='" + this.nombreDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    //+ "ciudad='" + this.ciudad + "',"
                    + "clasificacion='" + this.clasificacion + "',"
                    //+ "celularCliente='" + this.celularCliente + "',"
                    + "emailCliente= '" + this.emailCliente + "',"
                    + "fechaDeIngresoCliente = '" + this.fechaDeIngresoCliente + "',"
                    + "latitud = '" + this.latitud + "',"
                    + "longitud= '" + this.longitud + "',"
                    + "canalDeVenta = '" + this.canalDeVenta + "',"
                    + "ruta = '" + this.ruta + "',"
                    + "frecuencia= '" + this.frecuencia + "',"
                    + "zona= '" + this.zona + "',"
                    + "regional = '" + this.regional + "',"
                    + "agencia = '" + this.agencia + "',"
                    + "porcentajeDescuento= '" + this.porcentajeDescuento + "',"
                    + "activo='" + this.activoCliente + "',"
                    + "flag='-1';";

            if (grabado = ini.insertarDatosRemotamente(sql)) {
                //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
            }

        } catch (Exception ex) {
            System.out.println("Error en grabarClientes sql " + ex.getMessage());
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarClientes() {
        boolean grabado = false;
        try {

            String sql = "UPDATE clientes "
                    + "SET "
                    + "nitCliente='" + this.nitCliente + "',"
                    + "nombreEstablecimiento='" + this.nombreEstablecimiento + "',"
                    + "nombreDeCliente='" + this.nombreDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.ciudad + "',"
                    + "clasificacion='" + this.clasificacion + "',"
                    + "celularCliente='" + this.celularCliente + "',"
                    + "emailCliente= '" + this.emailCliente + "',"
                    + "latitud = '" + this.latitud + "',"
                    + "longitud= '" + this.longitud + "',"
                    + "canalDeVenta = '" + this.canalDeVenta + "',"
                    + "ruta = '" + this.ruta + "',"
                    + "frecuencia= '" + this.frecuencia + "',"
                    + "zona= '" + this.zona + "',"
                    + "regional = '" + this.regional + "',"
                    + "agencia = '" + this.agencia + "',"
                    + "porcentajeDescuento= '" + this.porcentajeDescuento + "',"
                    + "activo='" + this.activoCliente + "',"
                    + "flag='-1' "
                    + "WHERE "
                    + "codigoInterno='" + this.codigoInterno + "'; "
                    + ";";

            if (grabado = ini.insertarDatosRemotamente(sql)) {

            }
            if (!this.latitud.equals("0") || !this.longitud.equals("0")) {

                sql = "UPDATE clientes "
                        + "SET "
                        + "latitud = '" + this.latitud + "',"
                        + "longitud= '" + this.longitud + "' "
                        + "WHERE "
                        + "codigoInterno='" + this.codigoInterno + "';\n";
                GuardaConsultaEnFichero(sql);
            }

        } catch (Exception ex) {
            System.out.println("Error en actualizarClientes sql " + ex.getMessage());
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que devuelve una cadena con la lista de los campos de la BBDD
     * separados por ","
     *
     * @return una cadena con la lista de todos los campos de la tabla clientes
     */
    public String getCadenaConLosCampos() {
        String cadena = null;
        cadena = this.codigoInterno + ","
                + this.nitCliente + ","
                + this.nombreEstablecimiento + ","
                + this.nombreDeCliente + ","
                + this.direccion + ","
                + this.barrio + ","
                + this.ciudad + ","
                + this.clasificacion + ","
                + this.celularCliente + ","
                + this.emailCliente + ","
                + this.fechaDeIngresoCliente + ","
                + this.latitud + ","
                + this.longitud + ","
                + this.canalDeVenta + ","
                + this.ruta + ","
                + this.frecuencia + ","
                + this.zona + ","
                + this.regional + ","
                + this.agencia + ","
                + this.porcentajeDescuento + ","
                + this.activoCliente;

        return cadena;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {
            sql = "INSERT INTO clientes(codigoInterno,nitCliente,"
                    + "nombreEstablecimiento,nombreDeCliente,direccion,barrio,"
                    + "ciudad,clasificacion,celularCliente,emailCliente,"
                    + "fechaDeIngresoCliente,latitud,longitud,canalDeVenta,"
                    + "ruta,frecuencia,zona,regional,agencia,porcentajeDescuento,"
                    + "activo,usuario,flag) VALUES ('"
                    + this.codigoInterno + "','"
                    + this.nitCliente + "','"
                    + this.nombreEstablecimiento + "','"
                    + this.nombreDeCliente + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.ciudad + "','"
                    + this.clasificacion + "','"
                    + this.celularCliente + "','"
                    + this.emailCliente + "','"
                    + this.fechaDeIngresoCliente + "','"
                    + this.latitud + "','"
                    + this.longitud + "','"
                    + this.canalDeVenta + "','"
                    + this.ruta + "','"
                    + this.frecuencia + "','"
                    + ini.getIdZona() + "','"
                    + ini.getIdRegional() + "','"
                    + ini.getIdAgencia() + "','"
                    + this.porcentajeDescuento + "','"
                    + this.activoCliente + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "2') ON DUPLICATE KEY UPDATE "
                    + "codigoInterno='" + this.codigoInterno + "',"
                    + "nitCliente='" + this.nitCliente + "',"
                    + "nombreEstablecimiento='" + this.nombreEstablecimiento + "',"
                    + "nombreDeCliente='" + this.nombreDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.ciudad + "',"
                    //+ "celularCliente='" + this.celularCliente + "',"
                    + "activo='" + this.activoCliente + "',"
                    + "flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }

    public String getSentenciaInsertSQLImpExcel() {
        String sql = null;
        try {
            sql = "INSERT INTO clientes(codigoInterno,nitCliente,"
                    + "nombreEstablecimiento,nombreDeCliente,direccion,barrio,"
                    + "ciudad,"
                    + "clasificacion,celularCliente,emailCliente,"
                    + "fechaDeIngresoCliente,latitud,longitud,canalDeVenta,"
                    + "ruta,frecuencia,zona,regional,agencia,porcentajeDescuento,"
                    + "activo,usuario,flag) VALUES ('"
                    + this.codigoInterno + "','"
                    + this.nitCliente + "','"
                    + this.nombreEstablecimiento + "','"
                    + this.nombreDeCliente + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.ciudad + "','"
                    + this.clasificacion + "','"
                    + this.celularCliente + "','"
                    + this.emailCliente + "','"
                    + this.fechaDeIngresoCliente + "','"
                    + this.latitud + "','"
                    + this.longitud + "','"
                    + this.canalDeVenta + "','"
                    + this.ruta + "','"
                    + this.frecuencia + "','"
                    + ini.getIdZona() + "','"
                    + ini.getIdRegional() + "','"
                    + ini.getIdAgencia() + "','"
                    + this.porcentajeDescuento + "','"
                    + this.activoCliente + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "2') ON DUPLICATE KEY UPDATE "
                    + "codigoInterno='" + this.codigoInterno + "',"
                    + "nitCliente='" + this.nitCliente + "',"
                    + "nombreEstablecimiento='" + this.nombreEstablecimiento + "',"
                    + "nombreDeCliente='" + this.nombreDeCliente + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.ciudad + "',"
                    + "celularCliente='" + this.celularCliente + "',"
                    + "emailCliente='" + this.emailCliente + "',"
                   // + "latitud='" + this.latitud + "',"
                    //+ "longitud='" + this.longitud + "',"
                    + "activo='" + this.activoCliente + "',"
                    + "clasificacion='" + this.clasificacion + "',"
                    + "flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }

    /**
     * Método que cre un archivo de tipo CSV donde se encuentran todas las
     * direcciones de los clientes geo referenciado de tipo latitud y longitud
     * con identificación de cada cliente
     *
     * @param numeroDeManifiesto que equival al numero de manifiesto asignado al
     * conductor con todas las facturas correspondientes.
     * @return
     */
    public boolean exportarWayPointsCSV(int numeroDeManifiesto) {
        String sql;
        sql = " SELECT codigoInterno,nombreDeCliente,direccion,barrio,latitud,longitud "
                + " FROM clientes "
                + " WHERE codigointerno in ("
                + " SELECT facturas.cliente FROM facturas,facturaspormanifiesto "
                + " WHERE "
                + " facturas.numeroFactura=facturaspormanifiesto.numeroFactura and "
                + " facturaspormanifiesto.numeroManifiesto='" + numeroDeManifiesto + "' "
                + " ) and latitud <>0; ";

        return false;
    }

    public String getListadoDeClientes(String parteDelNombre) {

        String sql = "SELECT * "
                + "FROM clientes "
                + "WHERE "
                + "nombreDeCliente LIKE '%" + parteDelNombre + "%' "
                + "AND activo=1";
        return sql;
    }

    public String getListadoDeClientes() {

        String sql = "SELECT distinct c.codigoInterno, c.nitCliente, c.nombreEstablecimiento, c.nombreDeCliente, c.direccion, c.barrio, c.ciudad, "
                + "c.clasificacion, c.celularCliente, c.emailCliente, c.fechaDeIngresoCliente, c.latitud, c.longitud, c.canalDeVenta, c.ruta, "
                + "c.frecuencia, c.zona, c.regional, c.agencia, c.porcentajeDescuento, c.activo, c.fechaIng, c.usuario, c.flag "
                + "from clientes c "
                + "join facturas f on f.cliente=c.codigoInterno "
                + "where c.activo=1 and f.fechaDeVenta <=CURRENT_DATE and f.fechaDeVenta >=CURRENT_DATE -30;";
        return sql;
    }

    /**
     * Método que crea un archivo de tipo sql donde se guanrdas todas
     * actualizaciones con las direcciones de los clientes geo referenciado de
     * tipo latitud y longitud con identificación de cada cliente
     *
     * @param consulta que equival a la cadena SQL qu se va a guardar en el
     * archivo
     */
    private static void GuardaConsultaEnFichero(String consulta) {

        try {

            java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("puntosGeoreferenciacion.txt", true));
            //bufferedWriter.newLine();
            bufferedWriter.append(consulta);

            bufferedWriter.flush();

        } catch (IOException ex) {
            System.out.println("Error en GuardaConsultaEnFichero() " + ex);
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en GuardaConsultaEnFichero() " + ex, "No Existe", 1);

        }
    }
    
   

}
