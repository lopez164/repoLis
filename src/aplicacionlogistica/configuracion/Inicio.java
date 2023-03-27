/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import aplicacionlogistica.costumerService.objetos.CtiposGestiones;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CFacturasParaAnular;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCargos;
import aplicacionlogistica.distribucion.objetos.CCuentasBancarias;
import aplicacionlogistica.distribucion.objetos.CDocumentos;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CEntidadesBancarias;
import aplicacionlogistica.distribucion.objetos.CEstadosCiviles;
import aplicacionlogistica.distribucion.objetos.CNivelesDeAcceso;
import aplicacionlogistica.distribucion.objetos.CTiposDeAcceso;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CTiposDeContratosPersonas;
import aplicacionlogistica.distribucion.objetos.CTiposDeSangre;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCentrosDeCosto;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.CDestinosFacturas;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasEnDistribucion;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;
import mtto.ingresoDeRegistros.objetos.TiposDeMantenimientos;
import mtto.proveedores.Cproveedores;
import mtto.proveedores.SucursalesPorproveedor;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.Administracion.CLineasPorMarca;
import mtto.vehiculos.Administracion.CMarcasDeVehiculos;
import mtto.vehiculos.Administracion.CTiposDeCarrocerias;
import mtto.vehiculos.Administracion.CTiposDeCombustibles;
import mtto.vehiculos.Administracion.CTiposDeContratosVehiculos;
import mtto.vehiculos.Administracion.CTiposDeServicio;
import mtto.vehiculos.Administracion.CTiposDeVehiculos;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;
import aplicacionlogistica.costumerService.objetos.CTiposDePeticion;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCausalesdeDevolucion;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCentrosDeCosto;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCiudadesPorDepaartamento;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeContratosPersonas;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeDepartamentos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEmpleados;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEntidadesBancarias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEstadosDeVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeMarcasLLantas;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeCombustibles;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeContratosVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeSangre;
import mtto.vehiculos.Administracion.CMarcasDeLLantas;
import aplicacionlogistica.distribucion.objetos.CProductos;
import mtto.vehiculos.Administracion.CTiposDeDocumentos;
import mtto.vehiculos.Administracion.CTiposEstadosVehiculos;
import aplicacionlogistica.distribucion.objetos.CEstadosManifiestos;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CTiposDeEmpresas;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.Timer;
import mtto.documentos.Threads.HiloListadoDeCuentasSecundarias;
import mtto.documentos.Threads.HiloListadoDeSucursales;
import mtto.documentos.Threads.HiloListadoDeTiposDeDocumentos;
import mtto.documentos.Threads.HiloListadoDeTiposDeMantenimiento;
import mtto.ingresoDeRegistros.objetos.CCuentasPrincipalesLogistica;

/**
 *
 * @author Luis Edo
 */
public final class Inicio {

    ActionListener actionListener;
    Timer timer;
    boolean conectado = false;

    private static byte[] linebreak = {}; // Remove Base64 encoder default linebreak  
    private static String secret = "tvnw63ufg9gh5392"; // secret key length must be 16  
    private static SecretKey key; //   tvnw63ufg9gh5392
    private static Cipher cipher;
    private static Base64 coder;

    public JDesktopPane escritorio; // = new JDesktopPane();
    public InputStream foto1;
    Properties propiedades;
    String codigoAgencia;

    private Connection connRemota;
    private Connection connLocal;
    private Connection connGPS;

    int rutaDeEntrada;

    URL urlInicio;
    String idAliado = null;
    String nitCliente = null; // 
    int sucursalDelCliente;
    String nombreDelCliente = null;
    String direccionCliente = null;
    String ciudadCliente = null;
    String barrioCliente = null;
    String emailCliente = null;
    String telefonoCliente = null;
    String celularCliente = null;
    String contactoCliente = null;
    boolean estaClienteActivo;
    boolean estaConectado;
    String rutaDeApp = "" + (new File(".").getAbsolutePath()).replace(".", "");

    String cedulaUsuarioDelSistema = null;
    String usuarioDelSistema = null; // corresponde al usuario del sistema usuario= base de dato
    String nombreUsuarioDelSistema = null;
    boolean usuarioHabilitado = false;
    String usuarioBDLocal = null;
    String claveBDLocal = null;

    int tipoDeacceso;
    int idZona;
    int idRegional;
    int idAgencia;

    String urlLocal = null;
    String bdLocal = null;
    String serverLocal = null;
    String puertoLocal = null;

    String bdRemota = null;
    String urlRemota = null;
    String serverRemota = null;
    String puertoRemota = null;
    String usuarioBDRemota = null;
    String claveBDRemota = null;

    String bdGPS = null;
    String urlGPS = null;
    String serverGPS = null;
    String puertoGPS = null;
    String usuarioBDGPS = null;
    String claveBDGPS = null;

    String cadenaLocal = null;
    String cadenaRemota = null;
    String cadenaGPS = null;
    String cadenaDeConfiguracion = null;

    String mensaje = null;
    String hostFtp = null;
    String usuarioFtp = null;
    String claveFtp = null;
    String directorioFtp = null;
    String mac = null;

    public int enviaSMS;
    private int ralenti = 0;

    String usuarioSMS = null;
    String claveSMS = null;
    String origenSMS = null;
    String urlLinkSMS = null;
    String mensajeSMS = null;
    String indicativoSMS = null;

    String direcionIpLocal = null;
    String nombreEstacionLocal = null;

    int resolucion;
    Dimension dimension;
    CUsuarios user = null;
    String geoPositionCliente;

    // Organizacion
    List<CDepartamentos> listaDeDepartamentos = null;
    List<CCiudades> listaDeCiudades = null;
    List<CZonas> listaDeZonas = null;
    List<CRegionales> listaDeRegionales = null;
    List<CAgencias> listaDeAgenciass = null;
// Personas
    List<CCargos> listaDeCargos = null;
    List<TiposDeMantenimientos> listaDeTiposDeMantenimientos = null;
    List<CTiposDeSangre> listaDeTiposDeSangre = null;
    List<CEstadosCiviles> listaDeEstadosCiviles = null;
    List<CCentrosDeCosto> listaDeCentrosDeCosto = null;
    List<CTiposDeContratosPersonas> listaDeTiposContratosPer = null;
    List<CEntidadesBancarias> listaDeEntidadesBancarias = null;
    List<CCuentasBancarias> listaDeCuentasBancarias = null;
    List<CUsuarios> arrUsuarios = null;
    List<CTiposDeAcceso> listaDeTiposDeAcceso = null;
    List<CNivelesDeAcceso> listaDeNivelesDeAcceso = null;
    List<CDocumentos> arrTiposDocumentos = null;
    List<CEmpleados> listadeEmpleados = null;

    // Vehiculos
    List<CTiposDeEmpresas> ListaDeTiposDeEmpresas = null;
    List<CMarcasDeVehiculos> ListaDeMarcasDeVehiculos = null;
    List<CLineasPorMarca> ListaLineasPorMarca = null;
    List<CTiposDeCarrocerias> listaDeTiposDeCarrocerias = null;
    List<CTiposDeCombustibles> listaDeTiposDeCombustibles = null;
    List<CTiposDeContratosVehiculos> listaDeTiposDeContratosVehiculos = null;
    List<CTiposDeServicio> listaDeTiposDeServicios = null;
    List<CTiposDeVehiculos> listaDeTiposDeVehiculos = null;
    List<CMarcasDeLLantas> listaDeMarcasdeLLantas = null;
    List<CTiposEstadosVehiculos> listaEstadosVehiculos = null;
    /*  Mantenimientos y Flota de transporte */
    List<Cproveedores> listaDeProveedores = null;
    List<SucursalesPorproveedor> listaDeSucursales = null;
    List<CCuentaSecundariaLogistica> listadeCuentasSecundarias = null;
    List<CCuentasPrincipalesLogistica> listadeCuentasPrincipales = null;
    List<CTiposDeDocumentos> listaDeTiposDeDocumentos = null;

    // Logistica
    List<CCausalesDeDevolucion> listaDeCausalesDeDevolucion = null;
    List<CMovimientosManifiestosfacturas> listaDeMovimientosManifiestosfacturas = null;
    List<CProductosPorFactura> arrProductosPorFactura = null;
    List<CClientes> arrClientes = null;

// DISTRIBUCION 
    List<CManifiestosDeDistribucion> listaDeManifiestossinDescargar = null;
    List<CEstadosManifiestos> listaDeEstadosManifiestos = null;

    List<String> listaDeNumeroDeManifiestosPendientes = null;
    List<Vst_FacturasEnDistribucion> listaDeFacturasEnDistribucion = null;
    List<CManifiestosDeDistribucion> listaDeManifiestossinConciliar = null;
    List<CManifiestosDeDistribucion> listaDeManifiestosEnDistribucion = null;
    List<CProductos> listaDeProductosCamdun = null;
    List<String> listaDeLineasDeProductos = null;
    List<String> listaDeVendedores = null;
    List<CFacturas> listaDeFacturasSinMovimiento = null;
    List<CFacturas> listaDeFacturasDelDia = null;

    List<CCarros> listaDeVehiculos = null;
    List<CCarros> listaDeCarrosPropios = null;
    List<CCarros> listaDeCarrosConGps = null;
    List<String> listaDePlacas = null;
    List<CManifiestosDeDistribucion> arrManifiestos = null;
    List<CRutasDeDistribucion> listaDeRutasDeDistribucion = null;
    List<CCanalesDeVenta> listaDeCanalesDeVenta = null;
    List<CFacturasParaAnular> listaDeFacturasParaAnular = null;
    List<CtiposGestiones> listaDeTiposgestiones = null;

    /* proceso de picking */
    List<CDestinosFacturas> arrDestinosFacturas = null;

    /* proceso de Costumer Service */
    List<CTiposDePeticion> listaDeTiposDePeticion = null;

    String listaDeProductosParaMinuta = null;

    String prefijos = null;

    String uRLFuente = null;
    String servidorFuente = null;
    String dbFuente = null;
    String userFuente = null;
    String psdFuente = null;

    boolean imprimirMinuta = false;
    boolean permitirVariosManifiestos = false;
    boolean isGPSservice = false;
    boolean isAPPservice = false;
    boolean isMTTOservice = false;

    String novedadEnFactura = null;

    public boolean isGPSservice() {
        return isGPSservice;
    }

    public void setGPSservice(boolean isGPSservice) {
        this.isGPSservice = isGPSservice;
    }

    public boolean isAPPservice() {
        return isAPPservice;
    }

    public void setAPPservice(boolean isAPPservice) {
        this.isAPPservice = isAPPservice;
    }

    public boolean isMTTOservice() {
        return isMTTOservice;
    }

    public void setMTTOservice(boolean isMTTOservice) {
        this.isMTTOservice = isMTTOservice;
    }

    public Connection getConnRemota() {
        if (connRemota == null) {
            setConnRemota();
        }
        return connRemota;
    }

    public void setConnRemota() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.connRemota = DriverManager.getConnection(cadenaRemota, usuarioBDRemota, claveBDRemota);
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setConnRemota(Connection conn) {
        this.connRemota = conn;
    }

    public Connection getConnLocal() {
        if (connLocal == null) {
            setConnLocal();
        }
        return connLocal;
    }

    public void setConnLocal() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.connLocal = DriverManager.getConnection(cadenaLocal, usuarioBDLocal, claveBDLocal);
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setConnLocal(Connection connLocal) {
        this.connLocal = connLocal;
    }

    public Connection getConnGPS() {
        if (connGPS == null) {
            setConnGPS();
        }
        return connGPS;
    }

    public void setConnGPS(Connection connGPS) {
        this.connGPS = connGPS;
    }

    public void setConnGPS() {
        try {
             Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
              this.connGPS = DriverManager.getConnection(cadenaGPS, usuarioBDGPS, claveBDGPS);

        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public String getNovedadEnFactura() {
        return novedadEnFactura;
    }

    public void setNovedadEnFactura(String novedadEnFactura) {
        this.novedadEnFactura = novedadEnFactura;
    }

    public List<CFacturas> getListaDeFacturasDelDia() {
        return listaDeFacturasDelDia;
    }

    public void setListaDeFacturasDelDia(List<CFacturas> listaDeFacturasDelDia) {
        this.listaDeFacturasDelDia = listaDeFacturasDelDia;
    }

    public void setListaDeFacturasDelDia() {
        ResultSet rst = null;
        Statement st = null;
        Connection con;

        this.listaDeFacturasDelDia = new ArrayList<>();
        //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "ini.setListaDeFacturasSinMovimiento()");

        con = getConnRemota();
        String sql = null;

        sql = "SELECT "
                + " f.numeroFactura AS numeroFactura,"
                + " f.fechaDeVenta AS fechaDeVenta,"
                + " f.cliente AS idCliente,"
                + " c.nitCliente AS nitCliente,"
                + " c.nombreDeCliente AS nombreDeCliente,"
                + " f.direccion AS direccionDeCliente,"
                + " f.barrio AS barrio,"
                + " c.ciudad AS ciudad,"
                + " c.celularCliente AS telefonoCliente,"
                + " c.emailCliente AS emailCliente,"
                + " c.latitud AS latitud,"
                + " c.longitud AS longitud,"
                + " c.nombreEstablecimiento AS nombreEstablecimiento,"
                + " f.vendedor AS vendedor,"
                + " f.telefonoVendedor AS telefonoVendedor,"
                + " f.formaDePago AS formaDePago,"
                + " IF((f.formaDePago = 0),"
                + "     'NO DEFINIDO',"
                + "    IF((f.formaDePago = 1),"
                + "        'CONTADO',"
                + "        'CREDITO')) AS pago,"
                + " f.canal AS canal,"
                + " cv.nombreCanalDeVenta AS nombreCanalDeVenta,"
                + " f.valorFacturaSinIva AS valorFacturaSinIva,"
                + " f.valorIvaFactura AS valorIvaFactura,"
                + " f.valorTotalFactura AS valorTotalFactura,"
                + " f.valorRechazo AS valorRechazo,"
                + " f.valorDescuento AS valorDescuento,"
                + " f.valorTotalRecaudado AS valorTotalRecaudado,"
                + " f.zona AS zona,"
                + " f.regional AS regional,"
                + " f.agencia AS agencia,"
                + " f.isFree AS isFree,"
                + " f.estadoFactura AS estadoFactura,"
                + " tmf.nombreTipoDeMovimiento AS nombreEstadoFactura,"
                + " f.activo AS activo,"
                + " f.flag AS salidasDistribucion,"
                + " f.trasmitido AS trasmitido,"
                + " f.numeroDescuento AS numeroDescuento,"
                + " f.numeroRecogida AS numeroRecogida,"
                + " f.pesofactura AS pesofactura,"
                + " f.plazoDias AS plazoDias,"
                + " f.prefijo AS prefijo,"
                + " f.numero AS numero,"
                + " f.fpContado AS fpContado,"
                + " f.observaciones AS observaciones,"
                + " f.fechaIng AS fechaIng,"
                + " fm.numeroManifiesto as numeroManifiesto,"
                + " m.conductor as condcutor,"
                + " concat(p.nombres,' ',p.apellidos) as nombreConductor"
                + " FROM "
                + "  facturas f "
                + "  Left outer Join facturaspormanifiesto fm on fm.numeroFactura= f.numeroFactura "
                + "  left outer join manifiestosdedistribucion m on m.consecutivo= fm.numeroManifiesto "
                + "  left outer join personas p on p.cedula=m.conductor "
                + "  JOIN clientes c ON f.cliente = c.codigoInterno "
                + "  JOIN tiposcanaldeventas cv ON f.canal = cv.idCanalDeVenta "
                + "  JOIN tiposdemovimientosmanifiestosfacturas tmf ON tmf.idtipoDeMovimiento = f.estadoFactura "
                + "  where ";

        if ((propiedades.getProperty("idOperador").equals("2"))) {
            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

                // fechaInicial = " select concat(date_sub(current_date(), interval 1 day),' 05:00:00')";
                // fechaFinal = " select concat(current_date(),' 04:59:59')";
                sql += "f.fechaIng >=(select concat(date_sub(current_date(), interval 1 day),' 05:00:00')) AND "
                        + "f.fechaIng <= (select concat(current_date(),' 04:59:59')) ";

            } else {
                sql += "f.fechaIng >=(select concat(current_date(),' 05:00:00')) AND "
                        + "f.fechaIng <= (select concat(date_add(current_date(), interval 1 day),' 04:59:59')) ";

            }

        } else {
            sql += "fechaDeVenta= curdate() ";

        }

        sql += "order by  f.isFree, f.estadoFactura desc ;";

        try {
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {

                    CFacturas factura = new CFacturas(this);
                    factura.setNumeroDeFactura(rst.getString("numeroFactura"));
                    factura.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    factura.setCodigoDeCliente(rst.getString("idCliente"));
                    factura.setNitCliente(rst.getString("nitCliente"));
                    factura.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    factura.setDireccion(rst.getString("direccionDeCliente"));
                    factura.setBarrio(rst.getString("barrio"));
                    factura.setCiudad(rst.getString("ciudad"));
                    factura.setTelefono(rst.getString("telefonoCliente"));
                    factura.setEmailCliente(rst.getString("emailCliente"));
                    factura.setLatitud(rst.getString("latitud"));
                    factura.setLongitud(rst.getString("longitud"));
                    factura.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    factura.setVendedor(rst.getString("vendedor"));
                    factura.setTelefonoVendedor(rst.getString("telefonoVendedor"));
                    factura.setFormaDePago(rst.getString("formaDePago"));
                    factura.setCanal(rst.getInt("canal"));
                    factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    factura.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    factura.setValorRechazo(rst.getDouble("valorRechazo"));
                    factura.setValorDescuento(rst.getDouble("valorDescuento"));
                    factura.setValorTotalRecaudado(rst.getDouble("valorTotalRecaudado"));
                    factura.setZona(rst.getInt("zona"));
                    factura.setRegional(rst.getInt("regional"));
                    factura.setAgencia(rst.getInt("agencia"));
                    factura.setIsFree(rst.getInt("isFree"));
                    factura.setEstadoFactura(rst.getInt("estadoFactura"));
                    factura.setNombreEstadoFactura(rst.getString("nombreEstadoFactura"));
                    factura.setActivoFactura(rst.getInt("activo"));
                    factura.setTrasmitido(rst.getInt("trasmitido"));
                    factura.setNumeroDescuento(rst.getString("numeroDescuento"));
                    factura.setNumeroRecogida(rst.getString("numeroRecogida"));
                    factura.setPesofactura(rst.getDouble("pesofactura"));
                    factura.setPlazoDias(rst.getInt("plazoDias"));
                    factura.setPrefijo(rst.getString("prefijo"));
                    factura.setNumero(rst.getString("numero"));
                    factura.setFpContado(rst.getDouble("fpContado"));
                    factura.setObservaciones(rst.getString("observaciones"));
                    factura.setFechaIng(rst.getString("fechaIng"));
                    factura.setNombreDeConductor(rst.getString("nombreConductor"));

                    factura.setClasificacionCliente("");

                    factura.setNombreMovimiento(rst.getString("nombreEstadoFactura"));

                    listaDeFacturasDelDia.add(factura);

                }
                rst.close();
                st.close();
                //con.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(HiloListadoDeMarcasLLantas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CFacturas> getListaDeFacturasSinMovimiento() {
        return listaDeFacturasSinMovimiento;
    }

    public void setListaDeFacturasSinMovimiento(List<CFacturas> listaDeFacturasSinMovimiento) {
        this.listaDeFacturasSinMovimiento = listaDeFacturasSinMovimiento;
    }

    public void setListaDeFacturasSinMovimiento() {

        ResultSet rst = null;
        Statement st = null;
        Connection con;

        this.listaDeFacturasSinMovimiento = new ArrayList<>();
        // con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "ini.setListaDeFacturasSinMovimiento()");

        con = getConnRemota();
        String sql = null;

        sql = "SELECT "
                + " f.numeroFactura AS numeroFactura,"
                + " f.fechaDeVenta AS fechaDeVenta,"
                + " f.cliente AS idCliente,"
                + " c.nitCliente AS nitCliente,"
                + " c.nombreDeCliente AS nombreDeCliente,"
                + " f.direccion AS direccionDeCliente,"
                + " f.barrio AS barrio,"
                + " c.ciudad AS ciudad,"
                + " c.celularCliente AS telefonoCliente,"
                + " c.emailCliente AS emailCliente,"
                + " c.latitud AS latitud,"
                + " c.longitud AS longitud,"
                + " c.nombreEstablecimiento AS nombreEstablecimiento,"
                + " f.vendedor AS vendedor,"
                + " f.telefonoVendedor AS telefonoVendedor,"
                + " f.formaDePago AS formaDePago,"
                + " IF((f.formaDePago = 0),"
                + "     'NO DEFINIDO',"
                + "    IF((f.formaDePago = 1),"
                + "        'CONTADO',"
                + "        'CREDITO')) AS pago,"
                + " f.canal AS canal,"
                + " cv.nombreCanalDeVenta AS nombreCanalDeVenta,"
                + " f.valorFacturaSinIva AS valorFacturaSinIva,"
                + " f.valorIvaFactura AS valorIvaFactura,"
                + " f.valorTotalFactura AS valorTotalFactura,"
                + " f.valorRechazo AS valorRechazo,"
                + " f.valorDescuento AS valorDescuento,"
                + " f.valorTotalRecaudado AS valorTotalRecaudado,"
                + " f.zona AS zona,"
                + " f.regional AS regional,"
                + " f.agencia AS agencia,"
                + " f.isFree AS isFree,"
                + " f.estadoFactura AS estadoFactura,"
                + " tmf.nombreTipoDeMovimiento AS nombreEstadoFactura,"
                + " f.activo AS activo,"
                + " f.flag AS salidasDistribucion,"
                + " f.trasmitido AS trasmitido,"
                + " f.numeroDescuento AS numeroDescuento,"
                + " f.numeroRecogida AS numeroRecogida,"
                + " f.pesofactura AS pesofactura,"
                + " f.plazoDias AS plazoDias,"
                + " f.prefijo AS prefijo,"
                + " f.numero AS numero,"
                + " f.fpContado AS fpContado,"
                + " f.observaciones AS observaciones,"
                + " f.fechaIng AS fechaIng,"
                + " fm.numeroManifiesto as numeroManifiesto,"
                + " m.conductor as condcutor,"
                + " concat(p.nombres,' ',p.apellidos) as nombreConductor"
                + " FROM "
                + "  facturas f "
                + "  Left outer Join facturaspormanifiesto fm on fm.numeroFactura= f.numeroFactura "
                + "  left outer join manifiestosdedistribucion m on m.consecutivo= fm.numeroManifiesto "
                + "  left outer join personas p on p.cedula=m.conductor "
                + "  JOIN clientes c ON f.cliente = c.codigoInterno "
                + "  JOIN tiposcanaldeventas cv ON f.canal = cv.idCanalDeVenta "
                + "  JOIN tiposdemovimientosmanifiestosfacturas tmf ON tmf.idtipoDeMovimiento = f.estadoFactura "
                + "  where ";

        if ((propiedades.getProperty("idOperador").equals("2"))) {
            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

                // fechaInicial = " select concat(date_sub(current_date(), interval 1 day),' 05:00:00')";
                // fechaFinal = " select concat(current_date(),' 04:59:59')";
                sql += "f.fechaIng >=(select concat(date_sub(current_date(), interval 1 day),' 05:00:00')) AND "
                        + "f.fechaIng <= (select concat(current_date(),' 04:59:59')) AND ";

            } else {
                sql += "f.fechaIng >=(select concat(current_date(),' 05:00:00')) AND "
                        + "f.fechaIng <= (select concat(date_add(current_date(), interval 1 day),' 04:59:59')) AND ";

            }

        } else {
            sql += "fechaDeVenta= curdate() AND ";

        }

        sql += "f.estadoFactura='1' AND "//
                + "f.isFree = '1' "
                + "order by  f.isFree, f.estadoFactura desc ;";

        try {
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {

                    CFacturas factura = new CFacturas(this);
                    factura.setNumeroDeFactura(rst.getString("numeroFactura"));
                    factura.setFechaDeVenta(rst.getString("fechaDeVenta"));
                    factura.setCodigoDeCliente(rst.getString("idCliente"));
                    factura.setNitCliente(rst.getString("nitCliente"));
                    factura.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    factura.setDireccion(rst.getString("direccionDeCliente"));
                    factura.setBarrio(rst.getString("barrio"));
                    factura.setCiudad(rst.getString("ciudad"));
                    factura.setTelefono(rst.getString("telefonoCliente"));
                    factura.setEmailCliente(rst.getString("emailCliente"));
                    factura.setLatitud(rst.getString("latitud"));
                    factura.setLongitud(rst.getString("longitud"));
                    factura.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    factura.setVendedor(rst.getString("vendedor"));
                    factura.setTelefonoVendedor(rst.getString("telefonoVendedor"));
                    factura.setFormaDePago(rst.getString("formaDePago"));
                    factura.setCanal(rst.getInt("canal"));
                    factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    factura.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    factura.setValorRechazo(rst.getDouble("valorRechazo"));
                    factura.setValorDescuento(rst.getDouble("valorDescuento"));
                    factura.setValorTotalRecaudado(rst.getDouble("valorTotalRecaudado"));
                    factura.setZona(rst.getInt("zona"));
                    factura.setRegional(rst.getInt("regional"));
                    factura.setAgencia(rst.getInt("agencia"));
                    factura.setIsFree(rst.getInt("isFree"));
                    factura.setEstadoFactura(rst.getInt("estadoFactura"));
                    factura.setNombreEstadoFactura(rst.getString("nombreEstadoFactura"));
                    factura.setActivoFactura(rst.getInt("activo"));
                    factura.setTrasmitido(rst.getInt("trasmitido"));
                    factura.setNumeroDescuento(rst.getString("numeroDescuento"));
                    factura.setNumeroRecogida(rst.getString("numeroRecogida"));
                    factura.setPesofactura(rst.getDouble("pesofactura"));
                    factura.setPlazoDias(rst.getInt("plazoDias"));
                    factura.setPrefijo(rst.getString("prefijo"));
                    factura.setNumero(rst.getString("numero"));
                    factura.setFpContado(rst.getDouble("fpContado"));
                    factura.setObservaciones(rst.getString("observaciones"));
                    factura.setFechaIng(rst.getString("fechaIng"));

                    factura.setClasificacionCliente("");

                    factura.setNombreMovimiento(rst.getString("nombreEstadoFactura"));

                    listaDeFacturasSinMovimiento.add(factura);

                }
                rst.close();
                st.close();
                // con.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(HiloListadoDeMarcasLLantas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getListaDeProductosParaMinuta() {
        return listaDeProductosParaMinuta;
    }

    public void setListaDeProductosParaMinuta(String listaDeProductosParaMinuta) {
        this.listaDeProductosParaMinuta = listaDeProductosParaMinuta;
    }

    public void setListaDeProductosParaMinuta() {

        File archivo = new File(this.getRutaDeApp() + "listaCodigosDeProductosParaMinuta.txt");
        this.listaDeProductosParaMinuta = "'";
        try {
            if (archivo != null) {
                FileReader fr = new FileReader(archivo);
                BufferedReader br = new BufferedReader(fr);
                String linea;
                while ((linea = br.readLine()) != null) {
                    this.listaDeProductosParaMinuta += linea + "','";

                }
                this.listaDeProductosParaMinuta = this.listaDeProductosParaMinuta.substring(0, this.listaDeProductosParaMinuta.length() - 2);
                br.close();
                fr.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

    }

    public boolean isPermitirVariosManifiestos() {
        return permitirVariosManifiestos;
    }

    public void setPermitirVariosManifiestos(boolean permitirVariosManifiestos) {
        this.permitirVariosManifiestos = permitirVariosManifiestos;
    }

    public boolean isImprimirMinuta() {
        return imprimirMinuta;
    }

    public void setImprimirMinuta(boolean imprimirMinuta) {
        this.imprimirMinuta = imprimirMinuta;
    }

    public String getuRLFuente() {
        return uRLFuente;
    }

    public void setuRLFuente(String uRLFuente) {
        this.uRLFuente = uRLFuente;
    }

    public String getServidorFuente() {
        return servidorFuente;
    }

    public void setServidorFuente(String servidorFuente) {
        this.servidorFuente = servidorFuente;
    }

    public String getDbFuente() {
        return dbFuente;
    }

    public void setDbFuente(String dbFuente) {
        this.dbFuente = dbFuente;
    }

    public String getUserFuente() {
        return userFuente;
    }

    public void setUserFuente(String userFuente) {
        this.userFuente = userFuente;
    }

    public String getPsdFuente() {
        return psdFuente;
    }

    public void setPsdFuente(String psdFuente) {
        this.psdFuente = psdFuente;
    }

    public String getPrefijos() {
        return prefijos;
    }

//    public void setPrefijos(List<String> prefijos) {
//        this.prefijos = prefijos;
//    }
    public void setPrefijos(String prefijos) {
        this.prefijos = prefijos;
    }

    public List<CTiposDeDocumentos> getListaDeTiposDeDocumentos() {
        return listaDeTiposDeDocumentos;
    }

    public void setListaDeTiposDeDocumentos(List<CTiposDeDocumentos> listaDeTiposDeDocumentos) {
        this.listaDeTiposDeDocumentos = listaDeTiposDeDocumentos;
    }

    public void setListaDeTiposDeDocumentos() {
        try {
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CconexionMtto.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos());
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            String sql = "select * from tiposdocumentos  order by idtiposDocumentos;";
            CTiposDeDocumentos tiposDeDocumentos = new CTiposDeDocumentos(this);
            listaDeTiposDeDocumentos = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    tiposDeDocumentos = new CTiposDeDocumentos(this);

                    tiposDeDocumentos.setIdtiposDocumentos(rst.getInt("idtiposDocumentos"));
                    tiposDeDocumentos.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
                    tiposDeDocumentos.setFormato(rst.getString("formato"));
                    tiposDeDocumentos.setTieneVencimiento(rst.getInt("tieneVencimiento"));
                    tiposDeDocumentos.setFechaIng(rst.getDate("fechaIng"));
                    tiposDeDocumentos.setUsuario(rst.getString("usuario"));
                    tiposDeDocumentos.setActivo(rst.getInt("activo"));
                    tiposDeDocumentos.setFlag(rst.getInt("flag"));

                    listaDeTiposDeDocumentos.add(tiposDeDocumentos);
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeDocumentos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getRutaDeEntrada() {
        return rutaDeEntrada;
    }

    public void setRutaDeEntrada(int rutaDeEntrada) {
        this.rutaDeEntrada = rutaDeEntrada;
    }

    public Properties getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Properties propiedades) {
        this.propiedades = propiedades;
    }

    public int getRalenti() {
        return ralenti;
    }

    public void setRalenti(int ralenti) {
        this.ralenti = ralenti;
    }

    public JDesktopPane getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(JDesktopPane escritorio) {
        this.escritorio = escritorio;
    }

    public String getRutaDeApp() {
        return rutaDeApp;
    }

    public void setRutaDeApp(String rutaDeArchivo) {
        this.rutaDeApp = rutaDeArchivo;
    }

    public URL getUrlInicio() {
        return urlInicio;
    }

    public void setUrlInicio(URL urlInicio) {
        this.urlInicio = urlInicio;
    }

    public boolean isEstaConectado() {
        return estaConectado;
    }

    public String getIndicativoSMS() {
        return indicativoSMS;
    }

    public void setIndicativoSMS(String indicativoSMS) {
        this.indicativoSMS = indicativoSMS;
    }

    public String getMensajeSMS() {
        return mensajeSMS;
    }

    public void setMensajeSMS(String mensajeSMS) {
        this.mensajeSMS = mensajeSMS;
    }

    public int enviaSMS() {
        return enviaSMS;
    }

    public void setEnviaSMS(int useSMS) {
        this.enviaSMS = useSMS;
    }

    public String getUSuarioSMS() {
        return usuarioSMS;
    }

    public void setUSuarioSMS(String suarioSMS) {
        this.usuarioSMS = suarioSMS;
    }

    public String getClaveSMS() {
        return claveSMS;
    }

    public void setClaveSMS(String claveSMS) {
        this.claveSMS = claveSMS;
    }

    public String getOrigenSMS() {
        return origenSMS;
    }

    public void setOrigenSMS(String origenSMS) {
        this.origenSMS = origenSMS;
    }

    public String getUrlLinkSMS() {
        return urlLinkSMS;
    }

    public void setUrlLinkSMS(String urlLinkSMS) {
        this.urlLinkSMS = urlLinkSMS;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public void setNombreDelCliente(String nombreDelCliente) {
        this.nombreDelCliente = nombreDelCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public void setUsuarioBDLocal(String usuarioBDLocal) {
        this.usuarioBDLocal = usuarioBDLocal;
    }

    public void setUrlRemota(String urlRemota) {
        this.urlRemota = urlRemota;
    }

    public void setPuertoRemota(String puertoRemota) {
        this.puertoRemota = puertoRemota;
    }

    public void setUsuarioBDRemota(String usuarioBDRemota) {
        this.usuarioBDRemota = usuarioBDRemota;
    }

    public String getBdGPS() {
        return bdGPS;
    }

    public void setBdGPS(String bdGPS) {
        this.bdGPS = bdGPS;
    }

    public String getUrlGPS() {
        return urlGPS;
    }

    public void setUrlGPS(String urlGPS) {
        this.urlGPS = urlGPS;
    }

    public String getServerGPS() {
        return serverGPS;
    }

    public void setServerGPS(String serverGPS) {
        this.serverGPS = serverGPS;
    }

    public String getPuertoGPS() {
        return puertoGPS;
    }

    public void setPuertoGPS(String puertoGPS) {
        this.puertoGPS = puertoGPS;
    }

    public String getUsuarioBDGPS() {
        return usuarioBDGPS;
    }

    public void setUsuarioBDGPS(String usuarioBDGPS) {
        this.usuarioBDGPS = usuarioBDGPS;
    }

    public String getClaveBDGPS() {
        return claveBDGPS;
    }

    public void setClaveBDGPS(String claveBDGPS) {
        this.claveBDGPS = claveBDGPS;
    }

    public String getCadenaGPS() {
        return cadenaGPS;
    }

    public void setCadenaGPS(String cadenaGPS) {
        this.cadenaGPS = cadenaGPS;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public void setCiudadCliente(String ciudadCliente) {
        this.ciudadCliente = ciudadCliente;
    }

    public void setBarrioCliente(String barrioCliente) {
        this.barrioCliente = barrioCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
    }

    public void setContactoCliente(String contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public void setClaveBDLocal(String claveBDLocal) {
        this.claveBDLocal = claveBDLocal;
    }

    public void setBdRemota(String bdRemota) {
        this.bdRemota = bdRemota;
    }

    public void setClaveBDRemota(String claveBDRemota) {
        this.claveBDRemota = claveBDRemota;
    }

    public String getUrlLocal() {
        return urlLocal;
    }

    public void setUrlLocal(String urlLocal) {
        this.urlLocal = urlLocal;
    }

    public String getBdLocal() {
        return bdLocal;
    }

    public void setBdLocal(String bdLocal) {
        this.bdLocal = bdLocal;
    }

    public String getServerLocal() {
        return serverLocal;
    }

    public void setServerLocal(String serverLocal) {
        this.serverLocal = serverLocal;
    }

    public String getPuertoLocal() {
        return puertoLocal;
    }

    public void setPuertoLocal(String puertoLocal) {
        this.puertoLocal = puertoLocal;
    }

    public void setServerRemota(String serverRemota) {
        this.serverRemota = serverRemota;
    }

    public List<CClientes> getListaDeClientes() {
        return arrClientes;
    }

    public void setListaDeClientes(List<CClientes> arrClientes) {
        this.arrClientes = arrClientes;
    }

    public List<CLineasPorMarca> getListaDeLineasPorMarca() {
        return ListaLineasPorMarca;
    }

    public void setListaDeLineasPorMarca(List<CLineasPorMarca> arrLineasPorMarca) {
        this.ListaLineasPorMarca = arrLineasPorMarca;
    }

    public void setListaDeLineasPorMarca() {

        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            con = getConnRemota();

            CLineasPorMarca lineaVeh = new CLineasPorMarca(this);
            ListaLineasPorMarca = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(lineaVeh.arrListadoDeLineasPorMarca());

                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                while (rst.next()) {

                    lineaVeh = new CLineasPorMarca(this);
                    lineaVeh.setIdMarcaDeVehiculo(rst.getInt("idMarcaDeVehiculo"));
                    lineaVeh.setIdlineasVehiculos(rst.getInt("idlineasVehiculos"));
                    lineaVeh.setNombreMarcaDeVehiculos(rst.getString("nombreMarcaDeVehiculo"));
                    lineaVeh.setNombreLineaVehiculo(rst.getString("nombreLineaVehiculo"));
                    lineaVeh.setActivoLinea(rst.getInt("activo"));

                    ListaLineasPorMarca.add(lineaVeh);

                    k++;
                    System.out.println("Cargando Lineas -> " + k);

                    /*Le asigna valor a la barra inferior*/
                    Thread.sleep(1);
                }

                rst.close();
                st.close();
                // con.close();

                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CMarcasDeLLantas> getListaDeMarcasdeLLantas() {
        return listaDeMarcasdeLLantas;
    }

    public void setListaDeMarcasdeLLantas(List<CMarcasDeLLantas> listaDeMarcasdeLLantas) {
        this.listaDeMarcasdeLLantas = listaDeMarcasdeLLantas;
    }

    public void setListaDeMarcasdeLLantas() {
        ResultSet rst = null;
        Statement st = null;

        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "Inicio.setListaDeMarcasdeLLantas");
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CMarcasDeLLantas marcaLLanta = new CMarcasDeLLantas(this);
            listaDeMarcasdeLLantas = new ArrayList();
            if (con != null) {
                String sql = "SELECT idMarcaDeLlanta, nombreMarcaDeLlanta, activo, fechaIng, usuario, flag "
                        + "FROM marcasdellantas"
                        + " order by nombreMarcaDeLlanta;";

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    marcaLLanta = new CMarcasDeLLantas(this);
                    marcaLLanta.setIdMarcaDeLLantas(rst.getInt("idMarcaDeLlanta"));
                    marcaLLanta.setNombreMarcaDeLLantas(rst.getString("nombreMarcaDeLlanta"));
                    marcaLLanta.setActivoMarcaDeLLantas(rst.getInt("activo"));

                    System.out.println("Cargando Marcas de llantas -> " + marcaLLanta.getNombreMarcaDeLLantas());

                    listaDeMarcasdeLLantas.add(marcaLLanta);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                IngresoAlSistema.band = true;
                rst.close();
                st.close();
                // con.close();

                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeMarcasLLantas.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeMarcasLLantas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CTiposEstadosVehiculos> getListaEstadosVehiculos() {
        return listaEstadosVehiculos;
    }

    public void setListaEstadosVehiculos(List<CTiposEstadosVehiculos> listaEstadosVehiculos) {
        this.listaEstadosVehiculos = listaEstadosVehiculos;
    }

    public void setListaEstadosVehiculos() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "Inicio.setListaEstadosVehiculos");
            //  con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

            con = getConnRemota();
            CTiposEstadosVehiculos estadoVehiculo = new CTiposEstadosVehiculos(this);
            this.listaEstadosVehiculos = new ArrayList();
            if (con != null) {
                String sql = "SELECT * "
                        + "FROM tipodeestadosvehiculo;";
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    estadoVehiculo = new CTiposEstadosVehiculos(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    estadoVehiculo.setIdtipoDeEstadosVehiculo(rst.getInt("idtipoDeEstadosVehiculo"));
                    estadoVehiculo.setNombreEstadoVehiculo(rst.getString("nombreEstadoVehiculo"));
                    estadoVehiculo.setActivoEstadoVehiculo(rst.getInt("activo"));
                    estadoVehiculo.setUsuario(rst.getString("usuario"));
                    estadoVehiculo.setFechaIng(rst.getString("fechaIng"));

                    System.out.println("Cargando Cargos -> " + estadoVehiculo.getNombreEstadoVehiculo());

                    listaEstadosVehiculos.add(estadoVehiculo);

                    System.out.println("tiempo 2 " + new java.util.Date());
                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                // con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeEstadosDeVehiculos.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeEstadosDeVehiculos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CTiposDeCombustibles> getListaDeTiposDeCombustibles() {
        return listaDeTiposDeCombustibles;
    }

    public void setListaDeTiposDeCombustibles(List<CTiposDeCombustibles> arrTiposDeCombustibles) {
        this.listaDeTiposDeCombustibles = arrTiposDeCombustibles;
    }

    public void setListaDeTiposDeCombustibles() {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "Inicio.setListaDeTiposDeCombustibles");
            //  con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CTiposDeCombustibles tipComb = new CTiposDeCombustibles(this);
            listaDeTiposDeCombustibles = new ArrayList();
            if (con != null) {
                String sql = "SELECT idTipoCombustible, nombreTipoCombustible, activo, fechaIng, usuario, flag  "
                        + "FROM tiposdecombustible;";

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    tipComb = new CTiposDeCombustibles(this);

                    tipComb.setIdCombustible(rst.getInt("idTipoCombustible"));
                    tipComb.setNombreCombustible(rst.getString("nombreTipoCombustible"));
                    tipComb.setActivoCombustible(rst.getInt("activo"));

                    System.out.println("Cargando Tipos de Combustibles -> " + tipComb.getNombreCombustible());

                    listaDeTiposDeCombustibles.add(tipComb);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeTiposDeCombustibles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeCombustibles.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CCarros> getListaDeVehiculos() {
        return listaDeVehiculos;
    }

    /**
     * Metodo que trae la lista de objetos Carros de la base de datos
     * dependiendo del parametro si es logistica o mantenimiento
     *
     * @param area "1" = area de logistica y "2" = area de mantenimietno
     * @return un array con la lista de objetos carros correspondiente al area
     */
    public List<CCarros> setListaDeVehiculos(int area) {
        ResultSet rst1 = null;
        Statement st;
        Connection con = null;
        String sql = null;
        area = 1;

        // caso = 1;
        switch (area) {

            case 1: // caso en que la consulta sea del area de operaciones
                sql = "select * from vst_vehiculos where activo=1 "
                        + "order by placa asc;";
                //  con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
                con = getConnRemota();
                break;
            case 2:  // caso en que la consulta sea del area de Mantenimientos
                sql = "select * from vst_vehiculos where tipoContrato=1 and activo=1;";
//                con = CConexiones.GetConnection(this.getCadenaMantenimientos(), this.getUsuarioBDMantenimientos(), this.getClaveBDMantenimientos(), "HiloListadoDeVehiculos");
                //  con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

                con = getConnRemota();
                break;
            //     case 3:  // caso en que la consulta sea del area de Mantenimientos
            //     sql = "select * from vst_vehiculos;";
            //     break;
        }
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

        CCarros carro;
        listaDeVehiculos = new ArrayList();

        if (con != null) {
            try {
                st = con.createStatement();
                rst1 = st.executeQuery(sql);

                while (rst1.next()) {

                    carro = new CCarros(this);

                    carro.setPlaca(rst1.getString("placa"));
                    carro.setTipoDeTracccion(1); // 1 corresponde a carros
                    carro.setConductor(rst1.getString("conductor"));
                    carro.setNombreConductor(rst1.getString("nombreConductor"));
                    carro.setApellidosConductor(rst1.getString("apellidosConductor"));
                    carro.setPesoTotalSinCarga(rst1.getDouble("pesoTotalSinCarga"));
                    carro.setLargoVehiculo(rst1.getDouble("largoVehiculo"));
                    carro.setAlturaVehiculo(rst1.getDouble("alturaVehiculo"));
                    carro.setAnchuraVehiculo(rst1.getDouble("anchuraVehiculo"));
                    carro.setLongitudVehiculo(rst1.getDouble("longitudVehiculo"));
                    carro.setPesoTotalAutorizado(rst1.getDouble("pesoTotalAutorizado"));
                    carro.setCapacidadInstalada(rst1.getDouble("capacidadInstalada"));
                    carro.setIdLineaVehiculo(rst1.getInt("lineaVehiculo"));
                    carro.setNombreLineaVehiculo(rst1.getString("nombreLineaVehiculo"));
                    carro.setNombreMarcaDeVehiculo(rst1.getString("nombreMarcaDeVehiculo"));
                    carro.setTipoVehiculo(rst1.getInt("tipoVehiculo"));
                    carro.setTipoCarroceria(rst1.getInt("tipoCarroceria"));
                    carro.setNombreTipoCarroceria(rst1.getString("nombreTipoCarroceria"));
                    carro.setNombreTipoCombustible(rst1.getString("nombreTipoCombustible"));
                    carro.setNombreEstadoVehiculo(rst1.getString("nombreEstadoVehiculo"));

                    carro.setTipoContrato(rst1.getInt("tipoContrato"));
                    carro.setPropietario(rst1.getString("propietario"));
                    carro.setTarjetaPropiedad(rst1.getString("tarjetaPropiedad"));
                    carro.setCantidadLLantas(rst1.getInt("cantidadLLantas"));
                    carro.setTamanioLlantas(rst1.getString("tamanioLlantas"));
                    carro.setSerialChasis(rst1.getString("serialChasis"));
                    carro.setTrailer(rst1.getString("trailer"));
                    carro.setAgencia(rst1.getInt("agencia"));
                    carro.setModelo(rst1.getString("modelo"));
                    carro.setTipoServicio(rst1.getInt("idTipoServicio"));
                    carro.setSerialMotor(rst1.getString("serialMotor"));
                    carro.setTipoCombustible(rst1.getInt("idTipoCombustible"));
                    carro.setKmCambioValvulinaTrasmision(rst1.getInt("kmCambioValvulinaTrasmision"));
                    carro.setKilometrajeActual(rst1.getInt("kilometrajeActual"));
                    carro.setKmCambioAceiteMotor(rst1.getInt("kmCambioAceiteMotor"));
                    carro.setKmCambioValvulinaCaja(rst1.getInt("kmCambioValvulinaCaja"));
                    carro.setIdLineaVehiculo(rst1.getInt("lineaVehiculo"));
                    carro.setTipoMime(rst1.getString("tipoMime"));
                    carro.setActivoVehiculo(rst1.getInt("activo"));
                    carro.setActivoCarro(rst1.getInt("activo"));
                    carro.setConGps(rst1.getInt("conGps"));

                    System.out.println("Cargando vehiculo de placa  -> " + carro.getPlaca());

                    listaDeVehiculos.add(carro);
                    Thread.sleep(10);
                }

                rst1.close();
                st.close();
                //con.close();

            } // fin try // fin try
            catch (SQLException | InterruptedException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return listaDeVehiculos;
    }

    public void setListaDeVehiculos(List<CCarros> arrVehiculos) {
        this.listaDeVehiculos = arrVehiculos;
    }

    public void setListaDeCarrosPropios() {

        listaDeCarrosPropios = new ArrayList();

        for (CCarros carro : listaDeVehiculos) {
            if (carro.getTipoContrato() == 1) {
                listaDeCarrosPropios.add(carro);
            }
        }

    }

    public void setListaDeCarrosPropios(List<CCarros> arrVehiculos) {
        this.listaDeCarrosPropios = arrVehiculos;
    }

    public List<CCarros> getListaDeCarrosPropios() {
        return listaDeCarrosPropios;
    }

    public List<CCarros> getListaDeCarrosConGps() {
        return listaDeCarrosConGps;
    }

    public void setListaDeCarrosConGps(List<CCarros> listaDeCarrosConGps) {
        this.listaDeCarrosConGps = listaDeCarrosConGps;
    }

    public void setListaDeCarrosConGps() {

        listaDeCarrosConGps = new ArrayList();

        for (CCarros carro : listaDeVehiculos) {

            try {
                if (carro.getConGps() == 1) {
                    listaDeCarrosConGps.add(carro);
                }

                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<String> getListaDePlacas() {
        return listaDePlacas;
    }

    public void setListaDePlacas(List<String> listaDePlacas) {
        this.listaDePlacas = listaDePlacas;
    }

    public List<CManifiestosDeDistribucion> getArrManifiestos() {
        return arrManifiestos;
    }

    public void setArrManifiestos(List<CManifiestosDeDistribucion> arrManifiestos) {
        this.arrManifiestos = arrManifiestos;
    }

    public List<CRutasDeDistribucion> getListaDeRutasDeDistribucion() {
        return listaDeRutasDeDistribucion;
    }

    public void setListaDeRutasDeDistribucion(List<CRutasDeDistribucion> arrRutasDeDistribucion) {
        this.listaDeRutasDeDistribucion = arrRutasDeDistribucion;
    }

    public void setListaDeRutasDeDistribucion() {
        try {

            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Iniico.setListaDeRutasDeDistribucion");
            con = getConnRemota();
            CRutasDeDistribucion rutDist = new CRutasDeDistribucion(this);

            listaDeRutasDeDistribucion = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery("SELECT * FROM rutasdedistribucion ;");

                while (rst.next()) { // idRuta, nombreDeRuta, agencia, activo, fechaIng, usuario, flag, tipoRuta
                    System.out.println("Cargando  -> " + new java.util.Date());
                    rutDist = new CRutasDeDistribucion(this);

                    rutDist.setIdRutasDeDistribucion(rst.getInt("idRuta"));
                    rutDist.setNombreRutasDeDistribucion(rst.getString("nombreDeRuta"));
                    rutDist.setAgencia(rst.getInt("agencia"));
                    rutDist.setFechaIng(rst.getString("fechaIng"));
                    rutDist.setUsuario(rst.getString("usuario"));
                    rutDist.setFlag(rst.getInt("flag"));
                    rutDist.setTipoRuta(rst.getString("tipoRuta"));
                    rutDist.setActivoRutasDeDistribucion(rst.getInt("activo"));

                    System.out.println("Cargando Rutas de Distribución -> " + rutDist.getNombreRutasDeDistribucion());

                    listaDeRutasDeDistribucion.add(rutDist);

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(10);
            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);

        }

    }

    public List<CCanalesDeVenta> getListaDeCanalesDeVenta() {
        return listaDeCanalesDeVenta;
    }

    public void setListaDeCanalesDeVenta(List<CCanalesDeVenta> arrCanalesDeVenta) {
        this.listaDeCanalesDeVenta = arrCanalesDeVenta;
    }

    public void setListaDeCanalesDeVenta() {
        try {

            ResultSet rst = null;
            Statement st;
            Connection con;
            //  con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.setListaDeCanalesDeVenta");

            con = getConnRemota();
            CCanalesDeVenta canVenta = new CCanalesDeVenta(this);
            listaDeCanalesDeVenta = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(canVenta.arrListadoDeCanaleDeVenta());

                while (rst.next()) {
                    canVenta = new CCanalesDeVenta(this);

                    canVenta.setIdCanalDeVenta(rst.getInt("idCanalDeVenta"));
                    canVenta.setNombreCanalDeVenta(rst.getString("nombreCanalDeVenta"));
                    canVenta.setActivoCanal(rst.getInt("activo"));

                    listaDeCanalesDeVenta.add(canVenta);
                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CManifiestosDeDistribucion> getListaDeManifiestossinDescargar() {
        return listaDeManifiestossinDescargar;
    }

    public List<CManifiestosDeDistribucion> setListaDeManifiestossinDescargar(int estado, boolean isFromToday) {

        int caso = 0;
        int estadoManifiesto = estado;
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        boolean existe = false;
        String sql = "";

        if (isFromToday) {
            caso = 3;
            estadoManifiesto = 3;
        } else {
            caso = 2;
        }

        switch (caso) {
            case 1:
                sql = "SELECT * "
                        + "FROM vst_manifiestodedistribucion ;";

                break;
            case 2:
                sql = "SELECT * "
                        + "FROM vst_manifiestodedistribucion "
                        + "WHERE  estadoManifiesto<='" + estadoManifiesto + "'  order by  numeroManifiesto asc;";
                break;

            case 3:
                String fechaInicial = null;
                String fechaFinal = null;
                if (propiedades.getProperty("idOperador").equals("1")) {
                    sql = "SELECT * "
                            + "FROM vst_manifiestodedistribucion "
                            + "WHERE  estadoManifiesto<='" + estadoManifiesto + "' "
                            + "and fechaDistribucion = '" + Inicio.getFechaSql() + "' "
                            + "order by  numeroManifiesto asc; ";
                }

                if (propiedades.getProperty("idOperador").equals("2")) {
                    int dia = LocalDate.now().getDayOfMonth();

                    if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                        //if (true) {

                        dia -= 1;

                        //fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                        // fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";
                        fechaInicial = " select concat(date_sub(current_date(), interval 1 day),' 05:00:00')";
                        fechaFinal = " select concat(current_date(),' 04:59:59')";

                    } else {
                        dia += 1;

                        //fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                        //fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";
                        fechaInicial = " select concat(current_date(),' 05:00:00')";
                        fechaFinal = " select concat(date_add(current_date(), interval 1 day),' 04:59:59')";

                    }
                    sql = "SELECT * "
                            + "FROM vst_manifiestodedistribucion "
                            + "WHERE  "
                            + "estadoManifiesto<='" + estadoManifiesto + "' "
                            + "and fechaReal >=(" + fechaInicial + ") "
                            + "and fechaReal <=(" + fechaFinal + ") "
                            + "order by  numeroManifiesto asc; ";
                }

                break;

            case 4:

                break;

        }
        try {

            listaDeManifiestossinDescargar = new ArrayList();

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //  con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeManifiestosSinDescargar");
            con = getConnRemota();
            CManifiestosDeDistribucion mfto; //= new CManifiestosDeDistribucion(this);

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    mfto = new CManifiestosDeDistribucion(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    mfto.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    mfto.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    mfto.setVehiculo(rst.getString("vehiculo"));
                    mfto.setTipoContrato(rst.getString("tipoContrato"));
                    mfto.setConductor(rst.getString("conductor"));
                    mfto.setNombreConductor(rst.getString("nombreConductor"));
                    mfto.setApellidosConductor(rst.getString("apellidosConductor"));
                    mfto.setDespachador(rst.getString("despachador"));
                    mfto.setNombreDespachador(rst.getString("nombreDespachador"));
                    mfto.setApellidosDespachador(rst.getString("apellidosDespachador"));
                    mfto.setIdCanal(rst.getInt("idCanal"));
                    mfto.setNombreCanal(rst.getString("nombreCanal"));
                    mfto.setIdRuta(rst.getInt("idRuta"));
                    mfto.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    mfto.setTipoRuta(rst.getString("tipoRuta"));
                    mfto.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                    mfto.setKmSalida(rst.getInt("kmSalida"));
                    mfto.setKmEntrada(rst.getInt("kmEntrada"));
                    mfto.setKmRecorrido(rst.getInt("kmRecorrido"));
                    //mfto.setzona(rst.getInt("zona"));
                    //mfto.setregional(rst.getInt("regional"));
                    mfto.setAgencia(rst.getInt("agencia"));
                    mfto.setIsFree(rst.getInt("isFree"));
                    mfto.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    mfto.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    mfto.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                    mfto.setPesoKgManifiesto(rst.getDouble("pesoManifiesto"));
                    mfto.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                    mfto.setActivo(rst.getInt("activo"));
                    mfto.setUsuarioManifiesto(rst.getString("usuarioManifiesto"));
                    mfto.setFechaReal(rst.getString("fechaReal"));
                    mfto.setCantidadPedidos(rst.getInt("cantidadPedidos"));
                    mfto.setTrazabilidad(rst.getInt("trazabilidad"));
                    mfto.setObservaciones(rst.getString("observaciones"));

                    //mfto.setListaDeAuxiliares("" + rst.getInt("numeroManifiesto"));
                    String codigomanifiesto = mfto.codificarManifiesto();

                    mfto.setRutaArchivoDescargueFacturas(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_FacturasDescargados.txt");
                    mfto.setRutaArchivoDescargueporductosPorFactura(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_ProductosDescargados.txt");
                    mfto.setRutArchivoRecogidasporManifiesto(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_RecogidasDescargados.txt");
                    mfto.setRutArchivofacturasporManifiesto(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + ".txt");
                    mfto.setRutArchivoFacturasSinManifestar(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_SinManifestar.txt");
                    mfto.setRutaArchivoSoportesDeConsgnaciones(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_consignaciones.txt");

                    listaDeManifiestossinDescargar.add(mfto);

                    Thread.sleep(1);

                }

                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            try {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);

        }

        return listaDeManifiestossinDescargar;

    }

    public List<String> getListaDeConductoresConManifiestosPedientes(boolean isFromToday) {

        int caso = 0;
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        boolean existe = false;
        String sql = "";
        List<String> listaDeconductoresConManifiestosPendiente = null;

        if (isFromToday) {
            caso = 3;
        } else {
            caso = 2;
        }

        switch (caso) {
            case 1:
                sql = "SELECT * "
                        + "FROM vst_manifiestodedistribucion ;";

                break;
            case 2:
                sql = "SELECT * "
                        + "FROM vst_manifiestodedistribucion "
                        + "WHERE  estadoManifiesto<='3'  order by  numeroManifiesto asc;";
                break;

            case 3:
                String fechaInicial = null;
                String fechaFinal = null;
                if (propiedades.getProperty("idOperador").equals("1")) {
                    sql = "select distinct conductor,nombreConductor,apellidosConductor,fechaDistribucion "
                            + "FROM vst_manifiestodedistribucion "
                            + "WHERE  estadoManifiesto  <= '3' "
                            + "and fechaDistribucion = '" + Inicio.getFechaSql() + "' "
                            + "order by  apellidosConductor asc; ";

                }

                if (propiedades.getProperty("idOperador").equals("2")) {

                    int dia = LocalDate.now().getDayOfMonth();

                    if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                        dia -= 1;
                        fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                        fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";

                    } else {
                        dia += 1;
                        fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                        fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";

                    }

                    sql = "select distinct conductor,nombreConductor,apellidosConductor,fechaDistribucion,numeroManifiesto "
                            + "FROM vst_manifiestodedistribucion "
                            + "WHERE  estadoManifiesto <= '3' "
                            // + "and fechaDistribucion = '" + Inicio.getFechaSql() + "' "
                            + "and fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                            + "order by  apellidosConductor asc; ";
                }

                break;

            case 4:

                break;

        }
        try {

            listaDeconductoresConManifiestosPendiente = new ArrayList();

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeManifiestosSinDescargar");
            con = getConnRemota();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    String cadena = rst.getString("conductor") + "," + rst.getString("nombreConductor") + "," + rst.getString("apellidosConductor")
                            + "," + rst.getString("fechaDistribucion") + "," + rst.getString("numeroManifiesto");
                    listaDeconductoresConManifiestosPendiente.add(cadena);
                }

                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            try {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);

        }

        return listaDeconductoresConManifiestosPendiente;

    }

    public List<CManifiestosDeDistribucion> setListaDeManifiestossinDescargar(int estado, boolean isFromToday, String xxx) {

        int caso = 0;
        int estadoManifiesto = estado;
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        boolean existe = false;
        String sql = "";

        if (isFromToday) {
            caso = 3;
        } else {
            caso = 2;
        }

        switch (caso) {
            case 1:
                sql = "SELECT * "
                        + "FROM vst_manifiestodedistribucion ;";

                break;
            case 2:
                sql = "SELECT * "
                        + "FROM vst_manifiestodedistribucion "
                        + "WHERE  estadoManifiesto<='" + estadoManifiesto + "'  order by  numeroManifiesto asc;";
                break;

            case 3:
                String fechaInicial = null;
                String fechaFinal = null;
                if (propiedades.getProperty("idOperador").equals("1")) {
                    sql = "SELECT * "
                            + "FROM vst_manifiestodedistribucion "
                            + "WHERE  estadoManifiesto<='" + estadoManifiesto + "' "
                            + "and fechaDistribucion = '" + Inicio.getFechaSql() + "' "
                            + "order by  numeroManifiesto asc; ";
                }

                if (propiedades.getProperty("idOperador").equals("2")) {

                    int dia = LocalDate.now().getDayOfMonth();

                    if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                        dia -= 1;
                        fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                        fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";

                    } else {
                        dia += 1;
                        fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                        fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";

                    }
                    sql = "SELECT * "
                            + "FROM vst_manifiestodedistribucion "
                            + "WHERE  estadoManifiesto<='" + estadoManifiesto + "' "
                            + "and fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                            + "order by  numeroManifiesto asc; ";
                }

                break;

            case 4:

                break;

        }
        try {

            List<CManifiestosDeDistribucion> listaDeManifiestosSinDescargar = new ArrayList();

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeManifiestosSinDescargar");
            con = getConnRemota();
            CManifiestosDeDistribucion mfto = new CManifiestosDeDistribucion(this);
            this.setListaDeManifiestossinDescargar(listaDeManifiestosSinDescargar);

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    mfto = new CManifiestosDeDistribucion(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    mfto.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    mfto.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    mfto.setVehiculo(rst.getString("vehiculo"));
                    mfto.setTipoContrato(rst.getString("tipoContrato"));
                    mfto.setConductor(rst.getString("conductor"));
                    mfto.setNombreConductor(rst.getString("nombreConductor"));
                    mfto.setApellidosConductor(rst.getString("apellidosConductor"));
                    mfto.setDespachador(rst.getString("despachador"));
                    mfto.setNombreDespachador(rst.getString("nombreDespachador"));
                    mfto.setApellidosDespachador(rst.getString("apellidosDespachador"));
                    mfto.setIdCanal(rst.getInt("idCanal"));
                    mfto.setNombreCanal(rst.getString("nombreCanal"));
                    mfto.setIdRuta(rst.getInt("idRuta"));
                    mfto.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    mfto.setTipoRuta(rst.getString("tipoRuta"));
                    mfto.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                    mfto.setKmSalida(rst.getInt("kmSalida"));
                    mfto.setKmEntrada(rst.getInt("kmEntrada"));
                    mfto.setKmRecorrido(rst.getInt("kmRecorrido"));
                    //mfto.setzona(rst.getInt("zona"));
                    //mfto.setregional(rst.getInt("regional"));
                    mfto.setAgencia(rst.getInt("agencia"));
                    mfto.setIsFree(rst.getInt("isFree"));
                    mfto.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    mfto.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    mfto.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                    mfto.setPesoKgManifiesto(rst.getDouble("pesoManifiesto"));
                    mfto.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                    mfto.setActivo(rst.getInt("activo"));
                    mfto.setUsuarioManifiesto(rst.getString("usuarioManifiesto"));
                    mfto.setFechaReal(rst.getString("fechaReal"));
                    mfto.setCantidadPedidos(rst.getInt("cantidadPedidos"));
                    mfto.setTrazabilidad(rst.getInt("trazabilidad"));
                    mfto.setObservaciones(rst.getString("observaciones"));

                    //mfto.setListaDeAuxiliares("" + rst.getInt("numeroManifiesto"));
                    String codigomanifiesto = mfto.codificarManifiesto();

                    mfto.setRutaArchivoDescargueFacturas(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_FacturasDescargados.txt");
                    mfto.setRutaArchivoDescargueporductosPorFactura(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_ProductosDescargados.txt");
                    mfto.setRutArchivoRecogidasporManifiesto(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_RecogidasDescargados.txt");
                    mfto.setRutArchivofacturasporManifiesto(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + ".txt");
                    mfto.setRutArchivoFacturasSinManifestar(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_SinManifestar.txt");
                    mfto.setRutaArchivoSoportesDeConsgnaciones(this.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_consignaciones.txt");

                    if (this.getListaDeManifiestossinDescargar() != null) {
                        for (CManifiestosDeDistribucion mf : this.getListaDeManifiestossinDescargar()) {
                            if (mfto.getNumeroManifiesto().equals(mf.getNumeroManifiesto())) {
                                existe = true;
                                break;
                            }

                        }
                        if (!existe) {
                            this.getListaDeManifiestossinDescargar().add(mfto);
                            System.out.println("Cargando Manifiestos 2 # -> " + mfto.getNumeroManifiesto());
                            existe = false;
                        }
                    } else {
                        this.setListaDeManifiestossinDescargar(listaDeManifiestosSinDescargar);
                        this.getListaDeManifiestossinDescargar().add(mfto);
                        System.out.println("Cargando Manifiestos 1 # -> " + mfto.getNumeroManifiesto());
                    }
                    System.out.println("tiempo 2 " + new java.util.Date());
                    Thread.sleep(1);

                }

                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            try {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);

        }

        return listaDeManifiestossinDescargar;

    }

    public void setListaDeManifiestossinDescargar(List<CManifiestosDeDistribucion> arrManifiestossinDescargar) {
        this.listaDeManifiestossinDescargar = arrManifiestossinDescargar;
    }

    public List<String> getListaDeNumeroDeManifiestosPendientes() {
        return listaDeNumeroDeManifiestosPendientes;
    }

    public void setListaDeNumeroDeManifiestosPendientes(List<String> listaDeNumeroDeManifiestosPendientes) {
        this.listaDeNumeroDeManifiestosPendientes = listaDeNumeroDeManifiestosPendientes;
    }

    public List<String> getListaDeLineasDeProductos() {
        return listaDeLineasDeProductos;
    }

    public void setListaDeLineasDeProductos(List<String> listaDeLineasDeProductos) {
        this.listaDeLineasDeProductos = listaDeLineasDeProductos;
    }

    public List<CProductos> getListaDeProductosCamdun() {
        return listaDeProductosCamdun;
    }

    public void setListaDeProductosCamdun(List<CProductos> listaDeProductosCamdun) {
        this.listaDeProductosCamdun = listaDeProductosCamdun;
    }

    public List<CManifiestosDeDistribucion> getListaDeManifiestossinConciliar() {
        return listaDeManifiestossinConciliar;
    }

    public void setListaDeManifiestossinConciliar(List<CManifiestosDeDistribucion> listaDeManifiestossinConciliar) {
        this.listaDeManifiestossinConciliar = listaDeManifiestossinConciliar;
    }

    public void setListaDeManifiestosEnDistribucion(List<CManifiestosDeDistribucion> listaDeManifiestosEnDistribucion) {
        this.listaDeManifiestosEnDistribucion = listaDeManifiestosEnDistribucion;
    }

    public List<CProductosPorFactura> getListaDeProductosPorFactura() {
        return arrProductosPorFactura;
    }

    public void setListaDeProductosPorFactura(List<CProductosPorFactura> arrProductosPorFactura) {
        this.arrProductosPorFactura = arrProductosPorFactura;
    }

    public List<CMovimientosManifiestosfacturas> getListaDeMovimientosManifiestosfacturas() {
        return listaDeMovimientosManifiestosfacturas;
    }

    public void setListaMovimientosManifiestosfacturas(List<CMovimientosManifiestosfacturas> arrMovimientosManifiestosfacturas) {
        this.listaDeMovimientosManifiestosfacturas = arrMovimientosManifiestosfacturas;
    }

    public void setListaMovimientosManifiestosfacturas() {
        ResultSet rst = null;
        Statement st;
        Connection con;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeMovimientosManifiestosfacturas");
            con = getConnRemota();

            CMovimientosManifiestosfacturas movimiento = new CMovimientosManifiestosfacturas(this);

            listaDeMovimientosManifiestosfacturas = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(movimiento.arrListadoDeCMovimientosManifiestosfacturas());

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    movimiento = new CMovimientosManifiestosfacturas(this);
                    //  idtipoDeMovimiento, nombreTipoDeMovimiento, activo, fechaIng, usuario, flag
                    movimiento.setIdMovimientosManifiestosfacturas(rst.getInt("idtipoDeMovimiento"));
                    movimiento.setNombreMovimientosManifiestosfacturas(rst.getString("nombreTipoDeMovimiento"));
                    movimiento.setActivoMovimientosManifiestosfacturas(rst.getInt("activo"));

                    System.out.println("Cargando Movimientos facturas -> " + movimiento.getNombreMovimientosManifiestosfacturas());

                    listaDeMovimientosManifiestosfacturas.add(movimiento);

                    Thread.sleep(1);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                // con.close();
                // ini.setListaDeMovimientosFacturas(listaDeMovimientosManifiestosfacturas);
                Thread.sleep(1);

            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CMovimientosManifiestosfacturas> getListaDeMovimientosFacturas() {
        return listaDeMovimientosManifiestosfacturas;
    }

    public void setListaDeMovimientosFacturas(List<CMovimientosManifiestosfacturas> arrMovimientosManifiestosfacturas) {
        this.listaDeMovimientosManifiestosfacturas = arrMovimientosManifiestosfacturas;
    }

    public List<CCausalesDeDevolucion> getListaDeCausalesDeDevolucion() {
        return listaDeCausalesDeDevolucion;
    }

    public void setListaDeCausalesDeDevolucion() {

        try {
            ResultSet rst = null;
            Statement st;
            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.getListaDeCausalesDeDevolucion");

            con = getConnRemota();
            CCausalesDeDevolucion causalDevolucion = new CCausalesDeDevolucion(this);
            listaDeCausalesDeDevolucion = new ArrayList();
            String sql = "SELECT * "
                    + "FROM causalesderechazo  "
                    + "ORDER BY idcausalesDeRechazo ASC";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    causalDevolucion = new CCausalesDeDevolucion(this);
                    // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
                    causalDevolucion.setIdCausalesDeDevolucion(rst.getInt("idcausalesDeRechazo"));
                    causalDevolucion.setNombreCausalesDeDEvolucion(rst.getString("nombreCausalDeRechazo"));
                    causalDevolucion.setActivoCausalesDeDEvolucion(rst.getInt("activo"));

                    listaDeCausalesDeDevolucion.add(causalDevolucion);
                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCausalesdeDevolucion.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setListaDeCausalesDeDevolucion(List<CCausalesDeDevolucion> arrCausalesDeRechazo) {
        this.listaDeCausalesDeDevolucion = arrCausalesDeRechazo;
    }

    public List<CEstadosManifiestos> getListaDeEstadosManifiestos() {
        return listaDeEstadosManifiestos;
    }

    public void setListaDeEstadosManifiestos(List<CEstadosManifiestos> listaDeEstadosManifiestos) {
        this.listaDeEstadosManifiestos = listaDeEstadosManifiestos;
    }

    public void setListaDeEstadosManifiestos() {
        try {
            ResultSet rst = null;
            Statement st;
            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.setListaDeEstadosManifiestos");

            con = getConnRemota();
            CEstadosManifiestos estadoMan = new CEstadosManifiestos(this);
            listaDeEstadosManifiestos = new ArrayList();
            String sql = "SELECT idEstadoManifiesto, nombreDeEstadoManifiesto, activo, fechaIng, usuario, flag "
                    + "FROM estadosDeManifiesto  "
                    + "ORDER BY idEstadoManifiesto ASC";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    estadoMan = new CEstadosManifiestos(this);
                    // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
                    estadoMan.setIdEstadoManifiesto(rst.getInt("idEstadoManifiesto"));
                    estadoMan.setNombreDeEstadoManifiesto(rst.getString("nombreDeEstadoManifiesto"));
                    estadoMan.setActivoEstadoManifiesto(rst.getInt("activo"));
                    estadoMan.setUsuario(rst.getString("usuario"));
                    estadoMan.setFechaIng(rst.getString("fechaIng"));

                    listaDeEstadosManifiestos.add(estadoMan);
                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCausalesdeDevolucion.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getListaDeVendedores() {
        return listaDeVendedores;
    }

    public void setListaDeVendedores(List<String> listaDeVendedores) {
        this.listaDeVendedores = listaDeVendedores;
    }

    public List<CCuentaSecundariaLogistica> getListadeCuentasSecundarias() {
        return listadeCuentasSecundarias;
    }

    public void setListadeCuentasSecundarias(List<CCuentaSecundariaLogistica> listadeCuentasSecundarias) {
        this.listadeCuentasSecundarias = listadeCuentasSecundarias;
    }

    public void setListadeCuentasSecundarias() {
        ResultSet rst = null;
        Statement st;
        Connection con;
        try {
            listadeCuentasSecundarias = new ArrayList<>();
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos());
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

            con = getConnRemota();
            String sql = null;

            sql = "SELECT "
                    + "cp.idCuentaPrincipal, "
                    + "cp.nombreCuentaPrincipal, "
                    + "cs.idCuentaSecundaria, "
                    + "cs.nombreCuentaSecundaria, "
                    + "cs.codigoCuentaSecundaria, "
                    + "cs.activo "
                    + "FROM cuentassecundariaslogistica cs "
                    + "join cuentasprincipaleslogistica cp on cs.idcuentaPrincipal=cp.idCuentaPrincipal "
                    + "where cs.activo=1 "
                    + "order by cs.idCuentaPrincipal,cs.nombreCuentaSecundaria;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CCuentaSecundariaLogistica cuenta = new CCuentaSecundariaLogistica();

                    cuenta.setIdCuentaPrincipal(rst.getInt("idCuentaPrincipal"));
                    cuenta.setNombreCuentaPrincipal(rst.getString("nombreCuentaPrincipal"));
                    cuenta.setIdCuentaSecundaria(rst.getInt("idCuentaSecundaria"));
                    cuenta.setNombreCuentaSecundaria(rst.getString("nombreCuentaSecundaria"));
                    cuenta.setCodigoCuentaSecundaria(rst.getString("codigoCuentaSecundaria"));
                    cuenta.setActivo(rst.getInt("activo"));

                    listadeCuentasSecundarias.add(cuenta);

                }
                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCuentasSecundarias.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CCuentasPrincipalesLogistica> getListadeCuentasPrincipales() {
        return listadeCuentasPrincipales;
    }

    public void setListadeCuentasPrincipales(List<CCuentasPrincipalesLogistica> listadeCuentasPrincipales) {
        this.listadeCuentasPrincipales = listadeCuentasPrincipales;
    }

    public void setListadeCuentasPrincipales() {
        ResultSet rst = null;
        Statement st;
        Connection con;
        try {
            listadeCuentasPrincipales = new ArrayList<>();
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos());
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

            con = getConnRemota();
            String sql = "SELECT idCuentaPrincipal, nombreCuentaPrincipal, codigoCuenta, activo, fechaIng, usuario, flag "
                    + "FROM cuentasprincipaleslogistica "
                    + "order by nombreCuentaPrincipal asc;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CCuentasPrincipalesLogistica cuenta = new CCuentasPrincipalesLogistica(this);
                    // idCuentaPrincipal, nombreCuentaPrincipal, codigoCuenta, activo, fechaIng, usuario, flag "
                    cuenta.setIdCuentasPpal(rst.getInt("idCuentaPrincipal"));
                    cuenta.setNombreCuentaPpal(rst.getString("nombreCuentaPrincipal"));
                    cuenta.setCodigoCuenta(rst.getString("codigoCuenta"));
                    cuenta.setActivoCuentasPpal(rst.getInt("activo"));
                    cuenta.setUsuario(rst.getString("usuario"));
                    cuenta.setListaDeCuentasSecundarias();
                    listadeCuentasPrincipales.add(cuenta);

                }
                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCuentasSecundarias.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDirecionIpLocal() {
        return direcionIpLocal;
    }

    public void setDirecionIpLocal(String direcionIpLocal) {
        this.direcionIpLocal = direcionIpLocal;
    }

    public String getNombreEstacionLocal() {
        return nombreEstacionLocal;
    }

    public void setNombreEstacionLocal(String nombreHostLocal) {
        this.nombreEstacionLocal = nombreHostLocal;
    }

    public void setListaDeCausalesDeRechazo() {

        ResultSet rst = null;
        Statement st;
        Connection con;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, "Inicio.setArrCausalesDeRechazo");
            con = getConnRemota();

            CCausalesDeDevolucion causarechazo = new CCausalesDeDevolucion();
            listaDeCausalesDeDevolucion = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(causarechazo.arrListadoDeCCausalesDeRechazo());

                while (rst.next()) {

                    causarechazo = new CCausalesDeDevolucion();
                    // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
                    causarechazo.setIdCausalesDeDevolucion(rst.getInt("idcausalesDeRechazo"));
                    causarechazo.setNombreCausalesDeDEvolucion(rst.getString("nombreCausalDeRechazo"));
                    causarechazo.setActivoCausalesDeDEvolucion(rst.getInt("activo"));
                    listaDeCausalesDeDevolucion.add(causarechazo);

                }
                rst.close();
                st.close();
                //con.close();

            }

        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        }

    }

    public List<CtiposGestiones> getListaDeTiposgestione() {
        return listaDeTiposgestiones;
    }

    public void setListaDeTiposgestione(List<CtiposGestiones> arrTiposgestione) {
        this.listaDeTiposgestiones = arrTiposgestione;
    }

    public void setListaDeTiposgestione() {

        ResultSet rst = null;
        Statement st;
        Connection con;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, "Inicio.setListadeTiposgestione");
            con = getConnRemota();

            CtiposGestiones tipoGestion = new CtiposGestiones();
            listaDeTiposgestiones = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(tipoGestion.arrListadoDeTiposDeGestion());

                while (rst.next()) {

                    tipoGestion = new CtiposGestiones();
                    // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
                    tipoGestion.setIdtiposDeGestion(rst.getInt("idtiposDeGestion"));
                    tipoGestion.setNombreTipoDeGestion(rst.getString("nombreTipoDeGestion"));
                    tipoGestion.setActivo(rst.getInt("activo"));
                    listaDeTiposgestiones.add(tipoGestion);

                }
                rst.close();
                st.close();
                //con.close();

            }

        } // fin try // fin try // fin try // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        }
        ;
    }

    public static Cipher getCipher() {
        return cipher;
    }

    public static void setCipher(Cipher cipher) {
        Inicio.cipher = cipher;
    }

    public static Base64 getCoder() {
        return coder;
    }

    public static void setCoder(Base64 coder) {
        Inicio.coder = coder;
    }

    public List<CDepartamentos> getListaDeDepartamentos() {
        return listaDeDepartamentos;
    }

    public void setListaDeDepartamentos(List<CDepartamentos> arrDepartamentos) {
        this.listaDeDepartamentos = arrDepartamentos;
    }

    public void setListaDeDepartamentos() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "HiloListadoDeDepartamentos");
            con = getConnRemota();
            CDepartamentos dep = new CDepartamentos(this);
            String sql = " select * from departamentos where activo = '1'";
            listaDeDepartamentos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  " + new java.util.Date());
                    dep = new CDepartamentos(this);
                    //  idDepartamento, idPais, nombreDepartamento, activo, fechaIng, usuario, flag
                    dep.setIdDepartamento(rst.getInt("idDepartamento"));
                    dep.setNombreDepartamento(rst.getString("nombreDepartamento"));
                    dep.setActivoDepartamento(rst.getInt("activo"));
                    // dep.setListaDeCiudades();
                    System.out.println("Cargando Departameno -> " + dep.getNombreDepartamento());

                    listaDeDepartamentos.add(dep);
                }
                if (listaDeDepartamentos.size() == 0) {
                    listaDeDepartamentos = null;
                }
                rst.close();
                st.close();
                //con.close();
                for (CDepartamentos dpto : listaDeDepartamentos) {
                    new Thread(new HiloListadoDeCiudadesPorDepaartamento(this, dpto)).start();
                }

                Thread.sleep(10);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CCiudades> getListaDeCiudades() {
        return listaDeCiudades;
    }

    public void setListaDeCiudades(List<CCiudades> listaDeCiudades) {
        this.listaDeCiudades = listaDeCiudades;
    }

    public void setListaDeCiudades() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "HiloListadoDeCiudades");
            con = getConnRemota();
            CCiudades ciu = new CCiudades(this);
            listaDeCiudades = new ArrayList();
            if (con != null) {
                st = con.createStatement();

                String sql = "SELECT  c.idciudades,c. idDepartamento, d.nombreDepartamento,"
                        + "c.nombreCiudad, c.activo, c.fechaIng, c.usuario, c.flag "
                        + "FROM ciudades c "
                        + "join departamentos d on d.idDepartamento=c.idDepartamento "
                        + "where c.activo = '1' "
                        + "ORDER BY c.nombreCiudad asc;";

                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    ciu = new CCiudades(this);
                    ciu.setIdCiudad(rst.getInt("idciudades"));
                    ciu.setIdDepartamento(rst.getInt("idDepartamento"));
                    ciu.setNombreDepartamento(rst.getString("nombreDepartamento"));
                    ciu.setNombreCiudad(rst.getString("nombreCiudad"));
                    ciu.setActivoCiudad(rst.getInt("activo"));
                    System.out.println("Cargando Ciudades -> " + ciu.getNombreCiudad());

                    listaDeCiudades.add(ciu);
                    Thread.sleep(1);
                }
                if (listaDeCiudades.size() == 0) {
                    listaDeCiudades = null;
                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

            } catch (SQLException ex) {
                Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CZonas> getListaDeZonas() {
        return listaDeZonas;
    }

    public void setListaDeZonas(List<CZonas> arrZonas) {
        this.listaDeZonas = arrZonas;
    }

    public void setListaDeZonas() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeZonas");
            con = getConnRemota();
            CZonas zona = new CZonas(this);
            String sql = "SELECT idZona, nombreZona, activo, fechaIng, usuario, flag "
                    + "FROM zonas;  ";
            listaDeZonas = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    zona = new CZonas(this);

                    zona.setIdZona(rst.getInt("idZona"));
                    zona.setNombreZona(rst.getString("nombreZona"));
                    zona.setActivoZona(rst.getInt("activo"));

                    System.out.println("Cargando zonas -> " + zona.getNombreZona());

                    listaDeZonas.add(zona);

                    System.out.println("tiempo 2 " + new java.util.Date());
                    Thread.sleep(1);
                }

                rst.close();
                st.close();
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CRegionales> getListaDeRegionales() {
        return listaDeRegionales;
    }

    public void setListaDeRegionales(List<CRegionales> arrRegionales) {
        this.listaDeRegionales = arrRegionales;
    }

    public void setListaDeRegionales() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeRegionales");
            con = getConnRemota();
            CRegionales regional = new CRegionales(this);

            String sql = "SELECT regionales.idRegional, regionales.idZona, "
                    + "regionales.nombreRegional, regionales.activo, "
                    + "regionales.fechaIng, regionales.usuario, regionales.flag,"
                    + "zonas.nombreZona "
                    + "FROM regionales,zonas  "
                    + "WHERE "
                    + "regionales.idZona=zonas.idZona ;";

            listaDeRegionales = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    regional = new CRegionales(this);
                    regional.setIdRegional(rst.getInt("idRegional"));
                    regional.setIdZona(rst.getInt("idZona"));
                    regional.setNombreRegional(rst.getString("nombreRegional"));
                    regional.setNombreZona(rst.getString("nombreZona"));
                    regional.setActivoRegional(rst.getInt("activo"));

                    System.out.println("Cargando regionales -> " + regional.getNombreRegional());

                    listaDeRegionales.add(regional);

                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                Thread.sleep(10);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {

            try {
                rst.close();
                st.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CAgencias> getListaDeAgencias() {
        return listaDeAgenciass;
    }

    public void setListaDeAgencias(List<CAgencias> arrAgenciass) {
        this.listaDeAgenciass = arrAgenciass;
    }

    public void setListaDeAgencias() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.setListaDeCargos");
            con = getConnRemota();

            CAgencias agencia = new CAgencias(this);
            listaDeAgenciass = new ArrayList();
            String sql = "SELECT ag.idagencias, ag.idRegional,r.nombreRegional,"
                    + "ag.idZona,z.nombreZona, ag.nombreAgencia, ag.idCiudad, "
                    + "ag.activo, ag.fechaIng, ag.usuario, ag.flag "
                    + "FROM agencias ag "
                    + "join regionales r on r.idRegional = ag.idRegional "
                    + "join zonas z on z.idZona = ag.idZona "
                    + "where ag.activo = '1'"
                    + "order by ag.nombreAgencia asc;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    agencia = new CAgencias(this);
                    agencia.setIdAgencia(rst.getInt("idagencias"));
                    agencia.setIdRegional(rst.getInt("idRegional"));
                    agencia.setNombreRegional(rst.getString("nombreRegional"));
                    agencia.setIdZona(rst.getInt("idZona"));
                    agencia.setNombreZona(rst.getString("nombreZona"));
                    agencia.setNombreAgencia(rst.getString("nombreAgencia"));
                    agencia.setCiudad(rst.getInt("idCiudad"));
                    agencia.setActivoAgencia(rst.getInt("activo"));

                    System.out.println("Cargando agencias -> " + agencia.getNombreAgencia());

                    listaDeAgenciass.add(agencia);
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CCargos> getListaDeCargos() {
        return listaDeCargos;
    }

    public void setListaDeCargos() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.setListaDeCargos");
            con = getConnRemota();
            CCargos cargo = new CCargos(this);
            listaDeCargos = new ArrayList();
            String sql = "SELECT idCargo, nombreCargo, activo, fechaIng, usuario, flag "
                    + "FROM cargos "
                    + "where activo = '1'"
                    + "order by nombreCargo asc;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    //System.out.println("Cargando  -> " + new Date());
                    cargo = new CCargos(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    cargo.setIdCargo(rst.getInt("idCargo"));
                    cargo.setNombreCargo(rst.getString("nombreCargo"));
                    cargo.setActivoCargo(rst.getInt("activo"));

                    System.out.println("Cargando Cargos -> " + cargo.getNombreCargo());

                    listaDeCargos.add(cargo);
                    Thread.sleep(1);
                    // System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setListaDeCargos(List<CCargos> arrCargos) {
        this.listaDeCargos = arrCargos;
    }

    public List<CTiposDePeticion> getListaDeTiposDePeticion() {
        return listaDeTiposDePeticion;
    }

    public void setListaDeTiposDePeticion(List<CTiposDePeticion> listaDeTiposDePeticion) {
        this.listaDeTiposDePeticion = listaDeTiposDePeticion;
    }

    public List<TiposDeMantenimientos> getListaDeTiposDeMantenimientos() {
        return listaDeTiposDeMantenimientos;
    }

    public void setListaDeTiposDeMantenimientos(List<TiposDeMantenimientos> arrTiposDeMantenimientos) {
        this.listaDeTiposDeMantenimientos = arrTiposDeMantenimientos;
    }

    public void setListaDeTiposDeMantenimientos() {
        ResultSet rst = null;
        Statement st;
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarra;
        Connection con;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(this.getCadenaMantenimientos(), this.getUsuarioBDMantenimientos(), this.getClaveBDMantenimientos());
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

            con = getConnRemota();
            listaDeTiposDeMantenimientos = new ArrayList();
            if (con != null) {
                String sql = "SELECT * "
                        + "FROM tiposdemantenimientosvehiculos;";
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {

                    TiposDeMantenimientos tipo = new TiposDeMantenimientos(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    tipo.setIdMantenimiento(rst.getInt("idMantenimiento"));
                    tipo.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                    tipo.setActivo(rst.getInt("activo"));

                    System.out.println("Cargando mantenimientos -> " + tipo.getNombreMantenimiento());

                    listaDeTiposDeMantenimientos.add(tipo);

                    Thread.sleep(1);

                }

                rst.close();
                st.close();
                //con.close();
                if (listaDeTiposDeMantenimientos.size() == 0) {
                    listaDeTiposDeMantenimientos = null;
                }
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeMantenimiento.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CTiposDeSangre> getListaDeTiposDeSangre() {
        return listaDeTiposDeSangre;
    }

    public void setListaDeTiposDeSangre(List<CTiposDeSangre> arrTiposDeSangre) {
        this.listaDeTiposDeSangre = arrTiposDeSangre;
    }

    public void setListaDeTiposDeSangre() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeTiposDeSangre");
            con = getConnRemota();
            String sql = "SELECT IdtipoDeSangre, nombreTipoDeSangre, activo, fechaIng, usuario, flag "
                    + "FROM tiposdesangre ";

            CTiposDeSangre tipSan = new CTiposDeSangre(this);
            listaDeTiposDeSangre = new ArrayList();
            if (con != null) {
                st = con.createStatement();

                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    tipSan = new CTiposDeSangre(this);

                    tipSan.setIdTipoDeSaangre(rst.getInt("IdtipoDeSangre"));
                    tipSan.setNombreTipoDeSangre(rst.getString("nombreTipoDeSangre"));
                    tipSan.setActivoTipoDeSangre(rst.getInt("activo"));;

                    System.out.println("Cargando Tipos de Sangre -> " + tipSan.getNombreTipoDeSAngre());

                    listaDeTiposDeSangre.add(tipSan);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                // con.close();
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeTiposDeSangre.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeSangre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CEstadosCiviles> getListaDeEstadosCiviles() {
        return listaDeEstadosCiviles;
    }

    public void setListaDeEstadosCiviles(List<CEstadosCiviles> arrEstadosCiviles) {
        this.listaDeEstadosCiviles = arrEstadosCiviles;
    }

    public void setListaDeEstadosCiviles() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeEstadosCiviles");
            con = getConnRemota();
            String sql = "SELECT idEstadoCivil, nombreEstadoCivil, activo, fechaIng, usuario, flag "
                    + "FROM `estadosciviles` "
                    + "ORDER BY nombreEstadoCivil ASC;";

            CEstadosCiviles estCiv = new CEstadosCiviles(this);
            listaDeEstadosCiviles = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    estCiv = new CEstadosCiviles(this);

                    estCiv.setIdEstadiCivil(rst.getInt("idEstadoCivil"));
                    estCiv.setNombreEStadoCivil(rst.getString("nombreEstadoCivil"));
                    estCiv.setActivoEstadoCivil(rst.getInt("activo"));

                    System.out.println("Cargando Estados Civiles -> " + estCiv.getNombreEstadoCivil());

                    listaDeEstadosCiviles.add(estCiv);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(10);

            }

        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CCentrosDeCosto> getListaDeCentrosDeCosto() {
        return listaDeCentrosDeCosto;
    }

    public void setListaDeCentrosDeCosto(List<CCentrosDeCosto> arrCentrosDeCosto) {
        this.listaDeCentrosDeCosto = arrCentrosDeCosto;
    }

    public void setListaDeCentrosDeCosto() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.setListaDeCentrosDeCosto");
            con = getConnRemota();
            CCentrosDeCosto centroDeCosto = new CCentrosDeCosto(this);

            listaDeCentrosDeCosto = new ArrayList();
            if (con != null) {
                String sql = "SELECT idCentroDeCosto, nombreCentroDeCosto, activo, fechaIng, usuario, flag "
                        + "FROM centrosdecosto  "
                        + "ORDER BY nombreCentroDeCosto ASC";
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    // idCentroDeCosto, nombreCentroDeCosto, activo, fechaIng, usuario, flag
                    centroDeCosto = new CCentrosDeCosto(this);

                    centroDeCosto.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    centroDeCosto.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    centroDeCosto.setactivoCentroDeCosto(rst.getInt("activo"));;

                    System.out.println("Cargando centroDeCostos -> " + centroDeCosto.getNombreCentroDeCosto());

                    listaDeCentrosDeCosto.add(centroDeCosto);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CTiposDeContratosPersonas> getListaDeTiposContratosPer() {
        return listaDeTiposContratosPer;
    }

    public void setListaDeTiposContratosPer(List<CTiposDeContratosPersonas> arrTiposContratos) {
        this.listaDeTiposContratosPer = arrTiposContratos;
    }

    public void setListaDeTiposContratosPer() {
        ResultSet rst = null;
        Statement st = null;

        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "HiloListadoDeContratosPersonas");
            con = getConnRemota();
            CTiposDeContratosPersonas contrato = new CTiposDeContratosPersonas(this);
            listaDeTiposContratosPer = new ArrayList();
            if (con != null) {
                String sql = "SELECT idTipoContrato, nombreTipoDeContrato, activo, fechaIng, usuario, flag "
                        + "from tiposcontratospersonas "
                        + " ORDER BY nombreTipoDeContrato ASC";

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    contrato = new CTiposDeContratosPersonas(this);
                    // iidTipoContrato, nombreTipoDeContrato, activo, fechaIng, usuario, flag
                    contrato.setIdTipoDeContrato(rst.getInt("idTipoContrato"));
                    contrato.setNombreTipoDeContrato(rst.getString("nombreTipoDeContrato"));
                    contrato.setActivoTipoDeContrato(rst.getInt("activo"));;

                    System.out.println("Cargando Contratos personas -> " + contrato.getNombreTipoDeContrato());

                    listaDeTiposContratosPer.add(contrato);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeContratosPersonas.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeContratosPersonas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CEntidadesBancarias> getListaDeEntidadesBancarias() {
        return listaDeEntidadesBancarias;
    }

    public void setListaDeEntidadesBancarias(List<CEntidadesBancarias> arrEntidadesBancarias) {
        this.listaDeEntidadesBancarias = arrEntidadesBancarias;
    }

    public void setListaDeEntidadesBancarias() {
        ResultSet rst = null;
        Statement st = null;

        Connection con = null;

        try {

            //con = CConexiones.GetConnection(getCadenaRemota(), getUsuarioBDRemota(), getClaveBDRemota(), "Inicio.setListaDeEntidadesBancarias");
            con = getConnRemota();
            CEntidadesBancarias banco = new CEntidadesBancarias(this);
            listaDeEntidadesBancarias = new ArrayList();
            if (con != null) {
                String sql = "SELECT idBanco, nombreBanco, activo, fechaIng, usuario, flag "
                        + "FROM bancos ";
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    banco = new CEntidadesBancarias(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    banco.setIdEntidadBancaria(rst.getInt("idBanco"));
                    banco.setNombreEntidadBancaria(rst.getString("nombreBanco"));
                    banco.setActivoEntidadBancaria(rst.getInt("activo"));

                    System.out.println("Cargando Entidades Bancarias -> " + banco.getNombreEntidadBancaria());

                    listaDeEntidadesBancarias.add(banco);

                    Thread.sleep(1);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);

            }
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeEntidadesBancarias.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CCuentasBancarias> getListaDeCuentasBancarias() {
        return listaDeCuentasBancarias;
    }

    public void setLIstaDeCuentasBancarias(List<CCuentasBancarias> arrCuentasBancarias) {
        this.listaDeCuentasBancarias = arrCuentasBancarias;
    }

    public void setLIstaDeCuentasBancarias() {

        ResultSet rst = null;
        Statement st;
        Connection con;
        try {

            con = getConnRemota();

            CCuentasBancarias cuenta = new CCuentasBancarias(this);
            listaDeCuentasBancarias = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(cuenta.arrListadoDeCuentasBancarias());
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    cuenta = new CCuentasBancarias(this);

                    cuenta.setIdcuentasBancarias(rst.getInt("idCuenta"));
                    cuenta.setActivo(rst.getInt("activo"));
                    cuenta.setIdBanco(rst.getInt("idBanco"));
                    cuenta.setNombreDeBanco(rst.getString("nombreBanco"));
                    cuenta.setTipoDeCuenta(rst.getString("tipoCuenta"));
                    cuenta.setNumeroDeCuenta(rst.getString("numeroDeCuenta"));

                    System.out.println("Cargando Cuenteas Bancarias -> " + cuenta.getNumeroDeCuenta());

                    listaDeCuentasBancarias.add(cuenta);

                    Thread.sleep(1);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CUsuarios> getListaDeUsuarios() {
        return arrUsuarios;
    }

    public void setArrUsuarios(List<CUsuarios> arrUsuarios) {
        this.arrUsuarios = arrUsuarios;
    }

    public List<CTiposDeAcceso> getListaDeTiposDeAcceso() {
        return listaDeTiposDeAcceso;
    }

    public void setListaDeTiposDeAcceso(List<CTiposDeAcceso> arrTiposDeAcceso) {
        this.listaDeTiposDeAcceso = arrTiposDeAcceso;
    }

    public void setListaDeTiposDeAcceso() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeTiposDeAcceso");
            con = getConnRemota();
            CTiposDeAcceso tipAcc = new CTiposDeAcceso(this);

            listaDeTiposDeAcceso = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(tipAcc.rstListadoDeTiposDeAcceso());

                while (rst.next()) {
                    // System.out.println("Cargando  -> " + new Date());

                    tipAcc = new CTiposDeAcceso(this);

                    tipAcc.setIdTipoDeAcceso(rst.getInt("idTipoDeAcceso"));
                    tipAcc.setNombreTipoDeAcceso(rst.getString("nombreTipoDeAcceso"));
                    tipAcc.setActivoTipoDeAcceso(rst.getInt("activo"));

                    System.out.println("Cargando Tipos de acceso -> " + tipAcc.getNombreTipoDeAcceso());

                    listaDeTiposDeAcceso.add(tipAcc);
                    // System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                //  con.close();
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //  con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CNivelesDeAcceso> getListaDeNivelesDeAcceso() {
        return listaDeNivelesDeAcceso;
    }

    public void setListaDeNivelesDeAcceso(List<CNivelesDeAcceso> arrNivelesDeAcceso) {
        this.listaDeNivelesDeAcceso = arrNivelesDeAcceso;
    }

    public void setListaDeNivelesDeAcceso() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeNivelesDeAcceso");
            con = getConnRemota();

            CNivelesDeAcceso nivAcc = new CNivelesDeAcceso(this);
            listaDeNivelesDeAcceso = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(nivAcc.rstListadoDeNivelesDeAcceso());
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    nivAcc = new CNivelesDeAcceso(this);

                    nivAcc.setIdNivelDeAcceso(rst.getInt("idNivelDeAcceso"));
                    nivAcc.setNombreNivelDeAcceso(rst.getString("nombreNivelDeAcceso"));
                    nivAcc.setActivoNivelDeAcceso(rst.getInt("activo"));;

                    System.out.println("Cargando niveles de Acceso -> " + nivAcc.getNombreNivelDeAcceso());

                    listaDeNivelesDeAcceso.add(nivAcc);
                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                // con.close();
                //ini.setListaDeNivelesDeAcceso(listaDeNivelesDeAccesos);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                // con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CDocumentos> getArrTiposDocumentos() {
        return arrTiposDocumentos;
    }

    public void setArrTiposDocumentos(List<CDocumentos> arrTiposDocumentos) {
        this.arrTiposDocumentos = arrTiposDocumentos;
    }

    public List<CEmpleados> getListaDeEmpleados() {
        return listadeEmpleados;
    }

    public List<CEmpleados> setListaDeEmpleados(String apellidos) {
        String sql;
        try {
            ResultSet rst = null;
            Statement st;
            Connection con;
            if (apellidos != "") {
                sql = "select * from  vst_empleados where (apellidos like '%" + apellidos + "%' or "
                        + "nombres like '%" + apellidos + "%');";
            } else {
                sql = "select * from  vst_empleados where activo=1;";
            }

            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeEmpleados");
            con = getConnRemota();
            List<CEmpleados> listaDeEmpleados = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CEmpleados empleado = new CEmpleados(this);
                    empleado = new CEmpleados(this);
                    empleado.setCedula(rst.getString("cedula"));
                    empleado.setNombres(rst.getString("nombres"));
                    empleado.setApellidos(rst.getString("apellidos"));
                    empleado.setDireccion(rst.getString("direccion"));
                    empleado.setBarrio(rst.getString("barrio"));
                    empleado.setCiudad(rst.getInt("ciudad"));
                    empleado.setTelefonoFijo(rst.getString("telefonoFijo"));
                    empleado.setTelefonoCelular(rst.getString("telefonoCelular"));
                    empleado.setEscolaridad(rst.getString("escolaridad"));
                    empleado.setGenero(rst.getString("genero"));
                    empleado.setCumpleanios(rst.getDate("cumpleanios"));
                    empleado.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    empleado.setIdEstadoCivil(rst.getInt("idEstadoCivil"));
                    empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
                    empleado.setEmail(rst.getString("eMail"));
                    empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
                    empleado.setTipoSangre(rst.getString("tipoSangre"));
                    empleado.setIdCargo(rst.getInt("idcargo"));
                    empleado.setCargo(rst.getString("cargo"));
                    empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
                    empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
                    empleado.setIdAgencia(rst.getInt("idAgencia"));
                    empleado.setNombreAgencia(rst.getString("nombreAgencia"));
                    // empleado.setRegional(rst.getInt("idRegional"));
                    //empleado.setZona(rst.getInt("idZona"));
                    empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));

                    empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
                    empleado.setIdBanco(rst.getInt("idBanco"));

                    empleado.setEmpleadoActivo(rst.getInt("activo"));

                    listaDeEmpleados.add(empleado);

                    Thread.sleep(10);
                    System.out.println("Colaborador -> " + empleado.getCedula());
                }
                this.listadeEmpleados = listaDeEmpleados;
                rst.close();
                st.close();
                //con.close();

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeEmpleados.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return listadeEmpleados;
    }

    public void setListaDeEmpleados(List<CEmpleados> arrEmpleados) {
        this.listadeEmpleados = arrEmpleados;
    }

    public List<CEmpleados> setListaDeEmpleados() {
        String sql;
        try {
            ResultSet rst = null;
            Statement st;
            Connection con;

            listadeEmpleados = new ArrayList<>();

            sql = "select * from  vst_empleados;";

            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeEmpleados");
            con = getConnRemota();
            List<CEmpleados> listaDeEmpleados = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CEmpleados empleado = new CEmpleados(this);

                    empleado.setCedula(rst.getString("cedula"));
                    empleado.setNombres(rst.getString("nombres"));
                    empleado.setApellidos(rst.getString("apellidos"));
                    empleado.setDireccion(rst.getString("direccion"));
                    empleado.setBarrio(rst.getString("barrio"));
                    empleado.setCiudad(rst.getInt("ciudad"));
                    empleado.setTelefonoFijo(rst.getString("telefonoFijo"));
                    empleado.setTelefonoCelular(rst.getString("telefonoCelular"));
                    empleado.setEscolaridad(rst.getString("escolaridad"));
                    empleado.setGenero(rst.getString("genero"));
                    empleado.setCumpleanios(rst.getDate("cumpleanios"));
                    empleado.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    empleado.setIdEstadoCivil(rst.getInt("idEstadoCivil"));
                    empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
                    empleado.setEmail(rst.getString("eMail"));
                    empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
                    empleado.setTipoSangre(rst.getString("tipoSangre"));
                    empleado.setIdCargo(rst.getInt("idcargo"));
                    empleado.setCargo(rst.getString("cargo"));
                    empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
                    empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
                    empleado.setIdAgencia(rst.getInt("idAgencia"));
                    empleado.setNombreAgencia(rst.getString("nombreAgencia"));
                    // empleado.setRegional(rst.getInt("idRegional"));
                    //empleado.setZona(rst.getInt("idZona"));
                    empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));

                    empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
                    empleado.setIdBanco(rst.getInt("idBanco"));

                    empleado.setEmpleadoActivo(rst.getInt("activo"));

                    listadeEmpleados.add(empleado);

                    Thread.sleep(10);
                    System.out.println("Colaborador -> " + empleado.getCedula());
                }

                rst.close();
                st.close();
                //con.close();

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeEmpleados.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return listadeEmpleados;
    }

    public List<CTiposDeEmpresas> getListaDeTiposDeEmpresas() {
        return ListaDeTiposDeEmpresas;
    }

    public void setListaDeTiposDeEmpresas(List<CTiposDeEmpresas> ListaDeTiposDeEmpresas) {
        this.ListaDeTiposDeEmpresas = ListaDeTiposDeEmpresas;
    }

    public void setListaDeTiposDeEmpresas() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "inicio.setListaDeMarcasDeVehiculos()");
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CTiposDeEmpresas tipoDeEmpresa = new CTiposDeEmpresas(this);
            this.ListaDeTiposDeEmpresas = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(tipoDeEmpresa.strListadoDeTiposDeempresas());
                while (rst.next()) {
                    //System.out.println("Cargando  -> " + new Date());
                    tipoDeEmpresa = new CTiposDeEmpresas(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    tipoDeEmpresa.setIdTipoEmpresa(rst.getInt("idTipoEmpresa"));
                    tipoDeEmpresa.setNombreTipoEmpresa(rst.getString("nombreTipoEmpresa"));
                    tipoDeEmpresa.setActivoTipoEmpresa(rst.getInt("activo"));

                    // System.out.println("Cargando Cargos -> " + tipoDeEmpresa.getNombreMarcaDeVehiculos());
                    this.ListaDeTiposDeEmpresas.add(tipoDeEmpresa);
                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CMarcasDeVehiculos> getListaDeMarcasDeVehiculos() {
        return ListaDeMarcasDeVehiculos;
    }

    public void setListaDeMarcasDeVehiculos(List<CMarcasDeVehiculos> arrMarcasDeVehiculos) {
        this.ListaDeMarcasDeVehiculos = arrMarcasDeVehiculos;
    }

    public void setListaDeMarcasDeVehiculos() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "inicio.setListaDeMarcasDeVehiculos()");
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CMarcasDeVehiculos marcaVehiculo = new CMarcasDeVehiculos(this);
            this.ListaDeMarcasDeVehiculos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(marcaVehiculo.arrListadoDeMarcasDeVehiculos());
                while (rst.next()) {
                    //System.out.println("Cargando  -> " + new Date());
                    marcaVehiculo = new CMarcasDeVehiculos(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    marcaVehiculo.setIdMarcaDeVehiculos(rst.getInt("idMarcaDeVehiculo"));
                    marcaVehiculo.setNombreMarcaDeVehiculos(rst.getString("nombreMarcaDeVehiculo"));
                    marcaVehiculo.setActivoMarcaDeVehiculos(rst.getInt("activo"));

                    // marcaVehiculo.setListaDeLineasDelVehiculo();
                    System.out.println("Cargando Cargos -> " + marcaVehiculo.getNombreMarcaDeVehiculos());

                    this.ListaDeMarcasDeVehiculos.add(marcaVehiculo);
                    Thread.sleep(1);
                }

                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                // con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CTiposDeCarrocerias> getListaDeTiposDeCarrocerias() {
        return listaDeTiposDeCarrocerias;
    }

    public void setListaDeTiposDeCarrocerias(List<CTiposDeCarrocerias> arrTiposDeCarrocerias) {
        this.listaDeTiposDeCarrocerias = arrTiposDeCarrocerias;
    }

    public void setListaDeTiposDeCarrocerias() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "HiloListadoDeTiposDeCarrocerias");
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CTiposDeCarrocerias tipCarr = new CTiposDeCarrocerias(this);
            listaDeTiposDeCarrocerias = new ArrayList();
            if (con != null) {
                String sql = "SELECT idTipoCarroceria, nombreTipoCarroceria, activo, fechaIng, usuario, flag  "
                        + "FROM tiposdecarrocerias;";

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    tipCarr = new CTiposDeCarrocerias(this);

                    tipCarr.setIdCarroceria(rst.getInt("idTipoCarroceria"));
                    tipCarr.setNombreCarroceria(rst.getString("nombreTipoCarroceria"));
                    tipCarr.setActivoCarroceria(rst.getInt("activo"));;

                    System.out.println("Cargando Tipos de carroceria -> " + tipCarr.getNombreCarroceria());

                    listaDeTiposDeCarrocerias.add(tipCarr);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                // con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CTiposDeContratosVehiculos> getListaDeTiposDeContratosVehiculos() {
        return listaDeTiposDeContratosVehiculos;
    }

    public void setListaDeTiposDeContratosVehiculos(List<CTiposDeContratosVehiculos> arrTiposDeContratosVehiculos) {
        this.listaDeTiposDeContratosVehiculos = arrTiposDeContratosVehiculos;
    }

    public void setListaDeTiposDeContratosVehiculos() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "Inicio.setListaDeTiposDeContratosVehiculos");
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CTiposDeContratosVehiculos tipContVeh = new CTiposDeContratosVehiculos(this);
            listaDeTiposDeContratosVehiculos = new ArrayList();
            if (con != null) {
                String sql = "SELECT idTipoContrato, nombreTipoContrato, activo, fechaIng, usuario, flag  "
                        + "FROM tiposcontratosvehiculos;";
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    tipContVeh = new CTiposDeContratosVehiculos(this);

                    tipContVeh.setIdTipoDeContrato(rst.getInt("idTipoContrato"));
                    tipContVeh.setNombreTipoDeContrato(rst.getString("nombreTipoContrato"));
                    tipContVeh.setActivoTipoDeContrato(rst.getInt("activo"));;

                    System.out.println("Cargando Tipos de acceso -> " + tipContVeh.getNombreTipoDeContrato());

                    listaDeTiposDeContratosVehiculos.add(tipContVeh);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();

                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeTiposDeContratosVehiculos.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeContratosVehiculos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CTiposDeServicio> getListaDeTiposDeServicios() {
        return listaDeTiposDeServicios;
    }

    public void setListaDeTiposDeServicios(List<CTiposDeServicio> arrTiposDeServicios) {
        this.listaDeTiposDeServicios = arrTiposDeServicios;
    }

    public void setListaDeTiposDeServicios() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "Inicio.setListaDeTiposDeServicios");
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            CTiposDeServicio tipServ = new CTiposDeServicio(this);
            listaDeTiposDeServicios = new ArrayList();
            if (con != null) {
                String sql = "SELECT idtiposDeServicio, nombreTipoServicio, activo, fechaIng, usuario, flag  "
                        + "FROM tiposdeservicio;";
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    tipServ = new CTiposDeServicio(this);

                    tipServ.setIdTipoDeServicio(rst.getInt("idtiposDeServicio"));
                    tipServ.setNombreTipoDeServicio(rst.getString("nombreTipoServicio"));
                    tipServ.setActivoTipoDeServicio(rst.getInt("activo"));;

                    System.out.println("Cargando Tipos de carroceria -> " + tipServ.getNombreTipoDeServicio());

                    listaDeTiposDeServicios.add(tipServ);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                //con.close();
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {

            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CTiposDeVehiculos> getListaDeTiposDeVehiculos() {
        return listaDeTiposDeVehiculos;
    }

    public void setListaDeTiposDeVehiculos(List<CTiposDeVehiculos> arrTiposDeVehiculos) {
        this.listaDeTiposDeVehiculos = arrTiposDeVehiculos;
    }

    public void setListaDeTiposDeVehiculos() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos(), "Inicio.setListaDeTiposDeVehiculos");
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

            con = getConnRemota();
            CTiposDeVehiculos tipoVehiculo = new CTiposDeVehiculos(this);
            listaDeTiposDeVehiculos = new ArrayList();
            if (con != null) {
                String sql = "SELECT idTipoVehiculo, nombreTipoVehiculo, activo, fechaIng, usuario, flag "
                        + "FROM tiposdevehiculos;";
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new java.util.Date());
                    tipoVehiculo = new CTiposDeVehiculos(this);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    tipoVehiculo.setIdTipoDeVehiculo(rst.getInt("idTipoVehiculo"));
                    tipoVehiculo.setNombreTipoDeVehiculo(rst.getString("nombreTipoVehiculo"));
                    tipoVehiculo.setActivoTipoDeVehiculo(rst.getInt("activo"));;

                    System.out.println("Cargando Cargos -> " + tipoVehiculo.getNombreTipoDeVehiculo());

                    listaDeTiposDeVehiculos.add(tipoVehiculo);

                    System.out.println("tiempo 2 " + new java.util.Date());

                }
                rst.close();
                st.close();
                // con.close();

                Thread.sleep(1);
            }

        } catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CFacturasParaAnular> getListaDeRechasosParaAnular() {
        return listaDeFacturasParaAnular;
    }

    public void setListaDeRechasosParaAnular(List<CFacturasParaAnular> listaDeRechasosParaAnular) {
        this.listaDeFacturasParaAnular = listaDeRechasosParaAnular;
    }

    public List<CtiposGestiones> getListaDeTiposgestiones() {
        return listaDeTiposgestiones;
    }

    public void setListaDeTiposgestiones(List<CtiposGestiones> listaDeTiposgestiones) {
        this.listaDeTiposgestiones = listaDeTiposgestiones;
    }

    public List<Cproveedores> getListaDeProveedores() {
        return listaDeProveedores;
    }

    public void setListaDeProveedores(List<Cproveedores> listaDeProveedores) {
        this.listaDeProveedores = listaDeProveedores;
    }

    public void setListaDeProveedores() {
        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CconexionMtto.GetConnection(getCadenaMantenimientos(), getUsuarioBDMantenimientos(), getClaveBDMantenimientos());
            //con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            String sql = "select * from view_proveedores where activo=1;";
            listaDeProveedores = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    Cproveedores proveedor = new Cproveedores(this);
                    proveedor.setCedula(rst.getString("cedula"));
                    proveedor.setNombres(rst.getString("nombres"));
                    proveedor.setApellidos(rst.getString("apellidos"));
                    proveedor.setDireccion(rst.getString("direccion"));
                    proveedor.setBarrio(rst.getString("barrio"));
                    proveedor.setCiudad(rst.getInt("ciudad"));
                    proveedor.setTelefonoFijo(rst.getString("telefonoFijo"));
                    proveedor.setTelefonoCelular(rst.getString("telefonoCelular"));
                    proveedor.setEscolaridad(rst.getString("escolaridad"));
                    proveedor.setGenero(rst.getString("genero"));
                    proveedor.setCumpleanios(rst.getDate("cumpleanios"));
                    proveedor.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    proveedor.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    proveedor.setEmail(rst.getString("eMail"));
                    proveedor.setIdTipoSangre(rst.getInt("tipoSangre"));
                    proveedor.setContacto(rst.getString("contacto"));
                    proveedor.setCelularCorporativo(rst.getString("celularCorporativo"));
                    proveedor.setFechaDeIngreso(rst.getString("fechaDeIngreso"));
                    proveedor.setAgencia(rst.getInt("agencia"));
                    proveedor.setFechaIng(rst.getString("fechaIng"));
                    proveedor.setUsuario(rst.getString("usuario"));
                    proveedor.setActivo(rst.getInt("activo"));
                    proveedor.setFlag(rst.getInt("flag"));
                    proveedor.setCoordenadas(rst.getString("coordenadas"));

                    String parts[] = proveedor.getCoordenadas().split(",");
                    proveedor.setLatitud(parts[0]);
                    proveedor.setLongitud(parts[1]);

                    listaDeProveedores.add(proveedor);
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, e);

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SucursalesPorproveedor> getListaDeSucursales() {
        return listaDeSucursales;
    }

    public void setListaDeSucursales(List<SucursalesPorproveedor> listaDeSucursales) {
        this.listaDeSucursales = listaDeSucursales;
    }

    public void setListaDeSucursales() {
        ResultSet rst = null;
        Statement st;
        Connection con;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//              con = CconexionMtto.GetConnection(this.getCadenaMantenimientos(), this.getUsuarioBDMantenimientos(), this.getClaveBDMantenimientos());
            // con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");

            con = getConnRemota();
            String sql = "select * from view_sucursales where activo=1;";

            listaDeSucursales = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    SucursalesPorproveedor sucursal = new SucursalesPorproveedor(this);

                    sucursal.setIdSucursal(rst.getInt("idSucursalProveedor"));
                    sucursal.setCedula(rst.getString("nit"));
                    sucursal.setNombreProveedor(rst.getString("proveedor"));
                    sucursal.setNombreSucursal(rst.getString("nombreSucursal"));
                    sucursal.setCodigoInterno(rst.getString("codigo"));
                    sucursal.setDireccionSucursal(rst.getString("direccion"));
                    sucursal.setBarrioSucursal(rst.getString("barrio"));
                    sucursal.setCiudadSucursal(rst.getInt("codCiudad"));
                    sucursal.setNombreCiudad(rst.getString("nombreCiudad"));
                    sucursal.setTelefonoSucursal(rst.getString("telefono"));
                    sucursal.setCelularSucursal(rst.getString("celular"));
                    sucursal.setEmailSucursal(rst.getString("emailContacto"));
                    sucursal.setContactoSucursal(rst.getString("contacto"));
                    sucursal.setCelularCorporativo(rst.getString("celularcorporativo"));
                    sucursal.setFechaDeIngreso(rst.getString("fechaIngreso"));
                    sucursal.setAgencia(rst.getInt("codAgencia"));
                    sucursal.setNombreAgencia(rst.getString("nombreAgencia"));
                    sucursal.setFechaIng(rst.getString("fechaIng"));
                    sucursal.setUsuario(rst.getString("usuario"));
                    sucursal.setActivo(rst.getInt("activo"));
                    //sucursal.setFlag(rst.getInt("flag"));

                    sucursal.setListaDeCuentasSecundarias();

                    listaDeSucursales.add(sucursal);

                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                //con.close();
                if (listaDeSucursales.size() == 0) {
                    listaDeSucursales = null;
                }

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CUsuarios getUser() {
        return user;
    }

    public void setUser(CUsuarios user) {
        this.user = user;
    }

    public String getCadenaLocal() {
        return cadenaLocal;
    }

    public String getCadenaRemota() {
        return cadenaRemota;
    }

    public String getBdRemota() {
        return bdRemota;
    }

    public String getUrlRemota() {
        return urlRemota;
    }

    public String getServerRemota() {
        return serverRemota;
    }

    public String getPuertoRemota() {
        return puertoRemota;
    }

    public String getUsuarioBDRemota() {
        return usuarioBDRemota;
    }

    public String getClaveBDRemota() {
        return claveBDRemota;
    }

    public String getCedulaUsuarioDelSistema() {
        return cedulaUsuarioDelSistema;
    }

    public int getTipoDeacceso() {
        return tipoDeacceso;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public void setIdRegional(int idRegional) {
        this.idRegional = idRegional;
    }

    public void setIdAgencia(int idAgencia) {
        this.idAgencia = idAgencia;
    }

    
    public int getIdZona() {
        return idZona;
    }

    public int getIdRegional() {
        return idRegional;
    }

    public int getIdAgencia() {
        return idAgencia;
    }

    public String getUsuarioBDLocal() {
        return usuarioBDLocal;
    }

    public String getClaveBDLocal() {
        return claveBDLocal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getNombreUsuarioDelSistema() {
        return nombreUsuarioDelSistema;
    }

    public void setNombreUsuarioDelSistema(String nombreUsuarioDelSistema) {
        this.nombreUsuarioDelSistema = nombreUsuarioDelSistema;
    }

    public boolean isUsuarioHabilitado() {
        return usuarioHabilitado;
    }

    public void setUsuarioHabilitado(boolean usuarioHabilitado) {
        this.usuarioHabilitado = usuarioHabilitado;
    }

    public int getResolucion() {
        return resolucion;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public String getMac() {
        return mac;
    }

    public String getNombreDelCliente() {
        return nombreDelCliente;
    }

    public boolean isEstaClienteActivo() {
        return estaClienteActivo;
    }

    public void setEstaClienteActivo(boolean estaClienteActivo) {
        this.estaClienteActivo = estaClienteActivo;
    }

    public String getCiudadCliente() {

        return ciudadCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public String getBarrioCliente() {
        return barrioCliente;
    }

    public String getContactoCliente() {
        return contactoCliente;
    }

    public static String getSecret() {
        return secret;
    }

    public String getHostFtp() {
        return hostFtp;
    }

    public String getUsuarioFtp() {
        return usuarioFtp;
    }

    public String getClaveFtp() {
        return claveFtp;
    }

    public String getDirectorioFtp() {
        return directorioFtp;
    }

    public String getPuerto() {
        return puertoLocal;
    }

    public void setUsuarioDelSistema(String user) {
        this.usuarioDelSistema = user;
    }

    public String getUsuarioDelSistema() {
        return usuarioDelSistema;
    }

    public String getServer() {
        return serverLocal;
    }

    public void setServer(String server) {
        this.serverLocal = server;
    }

    public String getBd() {
        return bdLocal;
    }

    public void setBd(String bd) {
        this.bdLocal = bd;
    }

    public String getUrl() {
        return urlLocal;
    }

    public void setUrl(String url) {
        this.urlLocal = url;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public String getIdAliado() {
        return idAliado;
    }

    public void setIdAliado(String idAliado) {
        this.idAliado = idAliado;
    }

    public List<Vst_FacturasEnDistribucion> getListaDeFacturasEnDistribucion() {
        return listaDeFacturasEnDistribucion;
    }

    public void setListaDeFacturasEnDistribucion(List<Vst_FacturasEnDistribucion> listaDeFacturasEnDistribucion) {
        this.listaDeFacturasEnDistribucion = listaDeFacturasEnDistribucion;
    }

    public List<CDestinosFacturas> getArrDestinosFacturas() {
        return arrDestinosFacturas;
    }

    public void setArrDestinosFacturas(List<CDestinosFacturas> arrDestinosFacturas) {
        this.arrDestinosFacturas = arrDestinosFacturas;
    }

    public Inicio() {
        //config();
    }

    public Inicio(File file) {
        config(file);

    }

    public Inicio(String usuario) {
        Statement st;
        ResultSet rst;
        String sql;
        try {
            config();
            // Connection con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, "remoto");

            Connection con = getConnRemota();
            if (con != null) {
                st = con.createStatement();
                sql = "select * from usuarios where nombreUsuario='" + usuario + "'";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.usuarioDelSistema = usuario;
                    this.claveBDLocal = deCifrar(rst.getString("claveUsuario"));
                    this.usuarioHabilitado = true;
                    this.tipoDeacceso = rst.getInt(4);
                    this.idZona = rst.getInt(5);
                    this.idRegional = rst.getInt(6);
                    this.idAgencia = rst.getInt(7);

                }
                rst.close();
                st.close();
                // con.close();

            }

        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Inicio(String nombre, String clav) throws Exception {

        if (verificarConexion()) {
            if (isClienteActivo()) { //()) {
                if (comparar(nombre, clav)) {
                    this.usuarioHabilitado = true;
                } else {
                    mensaje = "Usuario de Sistema No existe ó no está ctivo verificar con el administrador";
                    this.usuarioHabilitado = false;
                }
            } else {
                mensaje = "Cliente No se encuentra activo verificar con el administrador";
            }
            // JOptionPane.showMessageDialog(null, "No se pudo ingreasr al sistema, confirmar mensaje \n\n " + mensaje);
        } else {
            mensaje = "No se pudo ingreasr al sistema Error de conexión, no hay internet  \n\n ";

        }
    }

    private boolean comparar(String user, String psswd) {
        boolean verificado = false;
        String sql;
        Statement st;
        ResultSet rst;
        if (estaClienteActivo) {

            try {

                //Connection con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");
                Connection con = this.getConnLocal();

                if (con != null) {
                    st = con.createStatement();
                    sql = "select * from usuarios where nombreUsuario='" + cifrar(user)
                            + "' and claveUsuario='" + cifrar(psswd) + "' and activo=1;";
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        cedulaUsuarioDelSistema = rst.getString(1);
                        //this.usuarioDelSistema = user;
                        this.tipoDeacceso = rst.getInt(4);
                        this.idZona = rst.getInt(5);
                        this.idRegional = rst.getInt(6);
                        this.idAgencia = rst.getInt(7);
                        this.usuarioHabilitado = true;
                        sql = "select * from personas where cedula='" + cedulaUsuarioDelSistema + "';";
                        ResultSet rs;
                        Statement s = null;
                        s = con.createStatement();
                        rs = s.executeQuery(sql);
                        if (rs.next()) {
                            this.nombreUsuarioDelSistema = rs.getString(2) + " " + rs.getString(3);
                        }
                        rs.close();
                        verificado = true;
                        this.usuarioHabilitado = true;
                    } else {
                        verificado = false;
                        this.usuarioHabilitado = false;
                    }
                    rst.close();
                    st.close();
                    //con.close();
                    return verificado;

                }

            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println("Error en comparar() consulta sql " + ex);
                JOptionPane.showMessageDialog(null, "Error al retornar dato : \n" + ex);
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
        return verificado;
    }

    public String buscarValor(String sql) {
        String buscar = null;
        ResultSet rs = null;
        Statement st;
        //Connection con = null;

        try {
            //Connection con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");

            Connection con = this.getConnLocal();
            if (con != null) {
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    buscar = rs.getString(1);
                }
                rs.close();
                //con.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en ini.BuscarValor() conexion " + ex);
            JOptionPane.showMessageDialog(null, "Error al retornar datos BuscarValor : \n" + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return buscar;
    }

    public boolean siExisteb(String sql) {
        boolean buscar = false;
        ResultSet rs;
        Statement st;
        //Connection con = null;

        try {
            //Connection con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");
            Connection con = this.getConnLocal();

            if (con != null) {
                st = con.createStatement();
                //sql="select * from usuarios where login='" + usuario +"' and clave='" + clave + "' and activo=1" ;
                rs = st.executeQuery(sql);
                if (rs.next()) {
                    buscar = true;
                    rs.close();
                }

                //con.close();
                st.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en ini.siExisteb() conexion " + ex);
            JOptionPane.showMessageDialog(null, "Error al retornar datos : \n" + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return buscar;
    }

    public boolean insertarDatosLocalmente(String sql) throws SQLException {
        boolean insertar = false;
        Statement st;
        Connection con = null;
        try {
            //con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");
            con = this.getConnLocal();
            //GuardaConsultaEnFichero2(sql);
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                st.execute(sql);
                insertar = true;
                //GuardaConsultaEnFichero(sql);
                con.commit();
                st.close();
                // con.close();
            }

        } catch (Exception ex) {
            con.rollback();
            System.out.println("Error en insertarDatosLocalmente() consulta sql " + ex + "(sql=" + sql + ")");
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            insertar = false;

            if (con != null) {
                con.rollback();
                //con.close();
            }

            //JOptionPane.showMessageDialog(null, "No se pudo grabar  en e sistema porque : \n" + ex);
        }

        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Remota, los
     * cuales pueden venir de un archivo en excel o de otro formato pero
     * encapsulado en un Array list de tipo String, tomado previamente y del
     * cual puede salir la lista de Sentencias SQL para la inserción de los
     * datos."
     *
     * @param sql corresponde a una sentencias SQL guardadas en un arrayLIs
     * @return devuelve verdadero si graba, false si se genera algún problema
     */
    public boolean insertarDatosRemotamente(String sql) {
        boolean insertar = false;
        Connection con = null;
        Statement st = null;
        try {

            //con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, "remoto");
            con = getConnRemota();

            con.setAutoCommit(false);

            if (con != null) {
                st = con.createStatement();
                st.execute(sql);
                insertar = true;

                con.commit();
                st.close();
                //con.close();

            }

        } catch (SQLException ex) {
            try {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertarDatosRemotamente() consulta sql " + ex + "(sql=" + sql + ")");
                insertar = false;
                con.rollback();
                st.close();
                //con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex1);
                System.out.println("Error en insertarDatosRemotamente() consulta sql " + ex + "(sql=" + sql + ")");
            }
        }
        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Remota, los
     * cuales pueden venir de un archivo en excel o de otro formato pero
     * encapsulado en un Array list de tipo String, tomado previamente y del
     * cual puede salir la lista de Sentencias SQL para la inserción de los
     * datos."
     *
     * @param sql corresponde a una sentencias SQL guardadas en un arrayLIs
     * @param claseOrigen
     * @return devuelve verdadero si graba, false si se genera algún problema
     */
    public boolean insertarDatosRemotamente(String sql, String claseOrigen) {
        boolean insertar = false;
        Connection con = null;
        Statement st = null;
        try {

            //con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, claseOrigen);
            con = getConnRemota();

            con.setAutoCommit(false);

            if (con != null) {
                st = con.createStatement();
                st.execute(sql);
                insertar = true;

                con.commit();
                st.close();
                // con.close();

            }

        } catch (SQLException ex) {
            try {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertarDatosRemotamente() consulta sql " + claseOrigen + " \n " + ex + "(sql=" + sql + ")");

                insertar = false;
                con.rollback();
                st.close();
                // con.close();
            } catch (SQLException ex1) {
                System.out.println("Error en insertarDatosRemotamente() consulta sql " + claseOrigen + " \n " + ex + "(sql=" + sql + ")");
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Remota, los
     * cuales pueden venir de un archivo en excel o de otro formato pero
     * encapsulado en un Array list de tipo String, tomado previamente y del
     * cual puede salir la lista de Sentencias SQL para la inserción de los
     * datos."
     *
     * @param listaDeSentenciasSQL corresponde a una lista de sentencias SQL
     * guardadas en un arrayLIs
     * @param claseOrigen
     * @return devuelve verdadero si graba, false si se genera algún problema
     * @throws java.sql.SQLException
     */
    public boolean insertarDatosRemotamente(List<String> listaDeSentenciasSQL, String claseOrigen) throws SQLException {
        boolean insertar = false;
        Connection con;//= null;
        Statement st = null;//= null;
        String cadena = null;

        //con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, claseOrigen);
        con = getConnRemota();
        try {
            if (con != null) {

                con.setAutoCommit(false);

                int contador = 0;
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {

                    if (obj.length() > 0) {
                        System.out.println("dato para grabar Remoto :  " + obj);
                        //st.execute(obj);
                        st.addBatch(obj);
                        contador++;

                    }
                    if (contador == 100) {
                        contador = 0;

                    }
                    Thread.sleep(2);
                }
                st.executeBatch();
                insertar = true;
                con.commit();
                st.close();
                // con.close();

            }
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar() consulta sql " + claseOrigen + " \n " + ex + "(sql=" + cadena + ")");
            insertar = false;
            con.rollback();
            st.close();
            //  con.close();
        } catch (InterruptedException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Remota, los
     * cuales pueden venir de un archivo en excel o de otro formato pero
     * encapsulado en un Array list de tipo String, tomado previamente y del
     * cual puede salir la lista de Sentencias SQL para la inserción de los
     * datos."
     *
     * @param listaDeSentenciasSQL corresponde a una lista de sentencias SQL
     * guardadas en un arrayLIs
     * @return devuelve verdadero si graba, false si se genera algún problema
     * @throws java.sql.SQLException
     */
    public boolean insertarDatosLocalmente(List<String> listaDeSentenciasSQL, String claseOrigen) throws SQLException {
        boolean insertar = false;
        Connection con;//= null;
        Statement st = null;//= null;
        String cadena = null;

        //con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, claseOrigen);
        con = getConnLocal();
        try {
            if (con != null) {

                con.setAutoCommit(false);

                int contador = 0;
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {

                    if (obj.length() > 0) {
                        System.out.println("dato para grabar Remoto :  " + obj);
                        //st.execute(obj);
                        st.addBatch(obj);
                        contador++;

                        System.out.println("dato insertado Remoto :  " + obj);
                    }
                    if (contador == 100) {
                        contador = 0;

                    }
                    Thread.sleep(2);
                }
                st.executeBatch();
                insertar = true;
                con.commit();
                st.close();
                //con.close();

            }
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar() consulta sql " + claseOrigen + " \n " + ex + "(sql=" + cadena + ")");
            insertar = false;
            con.rollback();
            st.close();
            // con.close();
        } catch (InterruptedException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local, los cuales
     * pueden venir de un archivo en excel o de otro formato pero encapsulado en
     * un Array list de tipo String, tomado previamente y del cual puede salir
     * la lista de Sentencias SQL para la inserción de los datos."
     *
     * @param listaDeSentenciasSQL corresponde a una lista de sentencias SQL
     * guardadas en un arrayLIs
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    public boolean insertarDatosLocalmente(List<String> listaDeSentenciasSQL) {
        boolean insertar = false;
        Connection con;
        Statement st = null;
        String cadena = "";

        try {

            //con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");
            con = this.getConnLocal();

            con.setAutoCommit(false);

            if (con != null) {
                try {
                    int contador = 0;
                    st = con.createStatement();
                    for (String obj : listaDeSentenciasSQL) {

                        if (obj.length() > 0) {
                            System.out.println("dato para grabar Localmente :  " + obj);
                            //st.execute(obj);
                            st.addBatch(obj);
                            contador++;

                            System.out.println("dato insertado Localmente :  " + obj);
                        }
                        if (contador == 100) {
                            contador = 0;
                            Thread.sleep(300);
                        }
                        Thread.sleep(10);
                    }
                    st.executeBatch();
                    insertar = true;
                    con.commit();
                    st.close();
                    // con.close();

                } catch (SQLException ex) {
                    try {
                        Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
                        con.rollback();
                        st.close();
                        //  con.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Remota l cual
     * utiliza un servicio Web para ejecutar la sentencia SQL"
     *
     * @param sql corresponde a sentencia SQL para insertarDatosLocalmente dato
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    // @SuppressWarnings("empty-statement")
    public boolean insertarMetodoPost(String sql) {
        String rpta = "";
        boolean ok = false;
        try {
            // DESCRIPCIÓN SERVICIO POR STIVENSON RINCÓN

            /*
            
             * Url del servicio: http://www.logarea.net/distribucion/public/service/insert (Solo se atiende peticiones por post)
             *
             * SE DEBE ENVIAR
             * Variable de nombre: SQL (Contiene el insert que se quiere ejecutar)
             * Variable de nombre: USERNAME
             * Variable de nombre: PASSWORD
            
             *  DEVUELVE
             *   0: Si hubo un error autenticando
             *   1: Si el insert se ejecuto con éxito
             *   2: Si huvo un error en la ejecución de la consulta
             *   3: Si hubo en error recogiendo los parametros que se reciben
             */
            //* ESTE CODIGO FUE PROBADO CON java version "1.8.0_25", compilado con javac 1.8.0_25 y funciona.
            URL url = new URL("http://www.logarea.net/distribucion/public/service/insert");
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("SQL", sql);
            params.put("USERNAME", "distribucion");
            params.put("PASSWORD", "123456");

            StringBuilder postData = new StringBuilder();

            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }

                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);

            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0; rpta += (char) c); // Se recibe respuesta

        } catch (MalformedURLException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        // return rpta;
        if (rpta.equals("1")) {
            ok = true;
        }
        return ok;
    }

    private static void GuardaConsultaEnFichero(String consulta) {

        try {

            java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sql.txt", true));
            bufferedWriter.newLine();
            bufferedWriter.append(consulta);
            bufferedWriter.flush();

        } catch (IOException ex) {
            System.out.println("Error en GuardaConsultaEnFichero() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en GuardaConsultaEnFichero() " + ex, "No Existe", 1);

        }
    }

    public static void GuardaConsultaEnFichero(String consulta, String pathArchivo) {

        try {

            java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathArchivo, true));
            // bufferedWriter.newLine();
            bufferedWriter.append(consulta);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException ex) {
            System.out.println("Error en GuardaConsultaEnFichero() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en GuardaConsultaEnFichero() " + ex, "No Existe", 1);

        }
    }

    private static void GuardaConsultaEnFichero2(String consulta) {

        try {

            java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sql2.txt", true));
            bufferedWriter.newLine();
            bufferedWriter.append(consulta);
            bufferedWriter.flush();

        } catch (IOException ex) {
            System.out.println("Error en GuardaConsultaEnFichero2() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en GuardaConsultaEnFichero2() " + ex, "No Existe", 1);
        }
    }

    public static void GuardaEnFichero(String texto) {

        try {

            java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("configServi2.txt", true));
            bufferedWriter.newLine();
            bufferedWriter.append(texto);
            bufferedWriter.flush();

        } catch (IOException ex) {
            System.out.println("Error en GuardaEnFichero() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en GuardaConsultaEnFichero() " + ex, "No Existe", 1);
        }
    }

    public static boolean esFechaValida(String fechax) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechax);
        } catch (ParseException ex) {
            System.out.println("Error en esFechaValida() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en esFechaValida() " + ex, "No Existe", 1);
            return false;
        }
        return true;
    }

    private void config() {

        try {

            estaClienteActivo = true;

            idAliado = "" + 6;
            nitCliente = "807.003.072";
            nombreDelCliente = "COMERCIALIZADORA CAMDUN S.A.S.";
            direccionCliente = "Av  4A # 7N-54 Zona Industrial";
            emailCliente = "";
            telefonoCliente = "5651604";
            celularCliente = "3133096439";
            barrioCliente = "ZONA INDUSTRIAL";
            contactoCliente = "RICARDO GONZALEZ";
            ciudadCliente = "CUCUTA";

            secret = "tvnw63ufg9gh5392";

            bdLocal = "comercializadoraCamdun";
            urlLocal = "jdbc:mysql";
            serverLocal = "localhost";
            //serverLocal = "50.0.0.114";
            puertoLocal = "3306";
            usuarioBDLocal = "luislopez"; // usuario localhost
            claveBDLocal = "jslslpzmjc1212"; // usuario localhost

            bdRemota = "comercializadoraCam";
            urlRemota = "jdbc:mysql";
            serverRemota = "186.115.154.90";
            //serverRemota = "www.logarea.net";
            puertoRemota = "3306";
            usuarioBDRemota = "luislopez";
            claveBDRemota = "jslslpzmjc1212";

            hostFtp = deCifrar("khOvIWycNWbdS6FJIxwvEQ");
            usuarioFtp = deCifrar("gKgT7211gDgzA3iPJ9GR9A");
            claveFtp = deCifrar("KRdqnQwVogS3twsRWCZJ2A");
            directorioFtp = deCifrar("hgbgfDKgGNHjX6T-02_3XGCEYVPDTp_CPbvZ5eXQcgw");
            mac = deCifrar("djACYohibJ4iYQjhQUxFljt3zFQtmU5H0gOpJckotIc");

            cadenaLocal = urlLocal + "://" + serverLocal + ":" + puertoLocal + "/" + bdLocal;
            cadenaRemota = urlRemota + "://" + serverRemota + ":" + puertoRemota + "/" + bdRemota;

            // cadenaRemota = "jdbc:mysql" + "://" + serverRemota + ":" + puertoRemota + "/" + bdRemota;
            rutaDeApp = "" + (new File(".").getAbsolutePath()).replace(".", "");

            escritorio = new JDesktopPane();

//           
            this.dimension = Toolkit.getDefaultToolkit().getScreenSize();
//            resolucion = Toolkit.getDefaultToolkit().getScreenResolution();
//            
            ipLocal();
        } catch (Exception ex) {
            System.out.println("Error en config() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error de configuración, pregunte al administrador del sistema " + ex, "No Existe", 1);
        }

    }

    private void config(String rutaArchivo) {

        try {
            FileReader f = new FileReader(rutaArchivo);
            BufferedReader b = new BufferedReader(f);

            idAliado = (Inicio.deCifrar(b.readLine()));
            nitCliente = Inicio.deCifrar(b.readLine());
            nombreDelCliente = b.readLine();
            direccionCliente = b.readLine();
            barrioCliente = b.readLine();
            ciudadCliente = b.readLine();
            contactoCliente = b.readLine();
            emailCliente = b.readLine();
            telefonoCliente = b.readLine();
            celularCliente = b.readLine();

            bdLocal = b.readLine();
            urlLocal = b.readLine();
            serverLocal = b.readLine();
            puertoLocal = b.readLine();
            usuarioBDLocal = b.readLine();
            claveBDLocal = b.readLine();

            bdRemota = b.readLine();
            urlRemota = b.readLine();
            serverRemota = b.readLine();
            puertoRemota = b.readLine();
            usuarioBDRemota = b.readLine();
            claveBDRemota = b.readLine();

            b.close();

            this.dimension = Toolkit.getDefaultToolkit().getScreenSize();
            cadenaLocal = urlLocal + "://" + serverLocal + ":" + puertoLocal + "/" + bdLocal;
            cadenaRemota = urlRemota + "://" + serverRemota + ":" + puertoRemota + "/" + bdRemota;

            rutaDeApp = "" + (new File(".").getAbsolutePath()).replace(".", "");

            escritorio = new JDesktopPane();

            ipLocal();

        } catch (Exception ex) {
            System.out.println("Error en config() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error de configuración, pregunte al administrador del sistema " + ex, "No Existe", 1);
        }

    }

    private void config(File file) {

        try {
            if (file.exists()) {

                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                idAliado = Inicio.deCifrar(br.readLine());
                nitCliente = Inicio.deCifrar(br.readLine());
                nombreDelCliente = Inicio.deCifrar(br.readLine());
                direccionCliente = Inicio.deCifrar(br.readLine());
                barrioCliente = Inicio.deCifrar(br.readLine());
                ciudadCliente = Inicio.deCifrar(br.readLine());
                contactoCliente = Inicio.deCifrar(br.readLine());
                emailCliente = Inicio.deCifrar(br.readLine());
                telefonoCliente = Inicio.deCifrar(br.readLine());
                celularCliente = Inicio.deCifrar(br.readLine());

                bdLocal = Inicio.deCifrar(br.readLine());
                urlLocal = Inicio.deCifrar(br.readLine());//   + "&useSSL=false" + "&requireSSL=false";
                serverLocal = Inicio.deCifrar(br.readLine());
                puertoLocal = Inicio.deCifrar(br.readLine());
                usuarioBDLocal = Inicio.deCifrar(br.readLine());
                claveBDLocal = Inicio.deCifrar(br.readLine());

                bdRemota = Inicio.deCifrar(br.readLine());
                urlRemota = Inicio.deCifrar(br.readLine());
                serverRemota = Inicio.deCifrar(br.readLine());
                puertoRemota = Inicio.deCifrar(br.readLine());
                usuarioBDRemota = Inicio.deCifrar(br.readLine());
                claveBDRemota = Inicio.deCifrar(br.readLine());

                bdGPS = Inicio.deCifrar(br.readLine());
                urlGPS = Inicio.deCifrar(br.readLine());//  + "&useSSL=false" + "&requireSSL=false";
                serverGPS = Inicio.deCifrar(br.readLine());
                puertoGPS = Inicio.deCifrar(br.readLine());
                usuarioBDGPS = Inicio.deCifrar(br.readLine());
                claveBDGPS = Inicio.deCifrar(br.readLine());

                cadenaLocal = urlLocal + "://?useSSL=false" + serverLocal + ":" + puertoLocal + "/" + bdLocal;
                cadenaRemota = urlRemota + "://?useSSL=false" + serverRemota + ":" + puertoRemota + "/" + bdRemota;
                cadenaGPS = urlGPS + "://?useSSL=false" + serverGPS + ":" + puertoGPS + "/" + bdGPS;

                /* ESto es un ejemplo de cadena de conexion
                https://es.stackoverflow.com/questions/168663/porque-java-resta-un-d%C3%ADa-a-la-fecha-obtenida-de-un-executequery-ejecutado-a-la-b
                
            public static Connection getMySQLConnection() throws Exception {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost/imss"+
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true"+
            "&useLegacyDatetimeCode=false&serverTimezone=America/Mexico_City"+
            "&verifyServerCertificate=false"+
            "&useSSL=true"+
            "&requireSSL=true";
            String username = "root";
             String password = "root";
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
}
                 */
                String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota";
                String noUsarSSL = "&useSSL=false";
                cadenaLocal = String.format("jdbc:mysql://%s/%s?%s%s", serverLocal, bdLocal, husoHorario, noUsarSSL);
                cadenaRemota = String.format("jdbc:mysql://%s/%s?%s%s", serverRemota, bdRemota, husoHorario, noUsarSSL);
                cadenaGPS = String.format("jdbc:mysql://%s/%s?%s%s", serverGPS, bdGPS, husoHorario, noUsarSSL);
                cadenaDeConfiguracion = String.format("jdbc:mysql://%s/%s?%s%s", "129.151.107.181", "misClientes", husoHorario, noUsarSSL);

                rutaDeApp = "" + (new File(".").getAbsolutePath()).replace(".", "");

                escritorio = new JDesktopPane();

                //this.isClienteActivo();
                this.dimension = Toolkit.getDefaultToolkit().getScreenSize();

                try {
                    /*Variable de envio de SMS*/
                    enviaSMS = Integer.parseInt(Inicio.deCifrar(br.readLine()));
                    usuarioSMS = Inicio.deCifrar(br.readLine());
                    claveSMS = Inicio.deCifrar(br.readLine());
                    origenSMS = Inicio.deCifrar(br.readLine());
                    urlLinkSMS = Inicio.deCifrar(br.readLine());
                    mensajeSMS = Inicio.deCifrar(br.readLine());
                    indicativoSMS = Inicio.deCifrar(br.readLine());
                } catch (Exception ex) {

                }

                br.close();

                ipLocal();

            } else {
                JOptionPane.showMessageDialog(null, "No existe el archivo de configuracion", "Error de archivo", JOptionPane.ERROR_MESSAGE);
                // System.exit(0);
            }

        } catch (Exception ex) {

            System.out.println("Error en config() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en variable " + ex, "Error de archivo", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    static {
        try {

            key = new SecretKeySpec(secret.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            coder = new Base64(32, linebreak, true);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            System.out.println("Error en static() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al instanciar objeto " + ex, "No Existe", 1);
        }
    }

    public static String cifrar(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return new String(coder.encode(cipherText));
    }

    public static String deCifrar(String codedText) throws Exception {
        byte[] encypted = coder.decode(codedText.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encypted);
        return new String(decrypted);
    }

    public static boolean validateEmail(String email) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]+[.[a-zA-Z0-9_-]+]*@[a-z0-9][\\w\\.-]*[a-z0-9]\\.[a-z][a-z\\.]*[a-z]$");//me gusta esta
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static java.util.Date getFechaSql(JDateChooser jDatefecha) {
        java.util.Date date = jDatefecha.getDate();
        long d = date.getTime();
        java.sql.Date fecha = new java.sql.Date(d);
        return fecha;
    }

    public static java.util.Date getFechaSql(Date fechaOrigen) {
        java.util.Date date = fechaOrigen;
        long d = date.getTime();
        java.sql.Date fecha = new java.sql.Date(d);
        return fecha;
    }

    public static java.util.Date getFechaSql2(Date fechaOrigen) {
        java.util.Date date = fechaOrigen;
        long d = date.getTime();
        java.sql.Date fecha = new java.sql.Date(d);
        return fecha;
    }

    public static java.util.Date getFechaSql(String fechaOrigen) {
        java.util.Date fechaEnviar = null;
        try {
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd");

            fechaEnviar = formatoDelTexto.parse(fechaOrigen);

        } catch (ParseException ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fechaEnviar;
    }

    public static java.util.Date getFechaSql() {
        java.util.Date date = new java.util.Date();
        long d = date.getTime();
        java.sql.Date fech = new java.sql.Date(d);
        return fech;
    }

    public java.util.Date deStringToDate(String strFecha) {

        String str = strFecha.replace(".", "/");
        str = strFecha.replace("-", "/");

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date fechaEnviar = null;
        try {
            fechaEnviar = formatoDelTexto.parse(str);
            return fechaEnviar;
        } catch (ParseException ex) {
            System.out.println("Error en Inicio  deStringToDate() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String deDateToString(Date fechaini) {
        String fechax;

        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        fechax = fecha.format(fechaini);

        return fechax;
    }

    public boolean isClienteActivo() {
        boolean verificado = false;
        this.estaClienteActivo = true;
        Connection con;
        ResultSet rs = null;
        Statement st;
        String servidor = "jdbc:mysql://200.116.155.139:3306/misClientes";
        java.util.Date fechaActual = new java.util.Date();
        try {

            /* Valor que se coloco provisionanlemnte para Camdun 
            if (propiedades.getProperty("cuentaGoogleCloud").equals("false")) {
                return true;
            } **/
            String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String noUsarSSL = "&useSSL=false";
            String cadena = String.format("jdbc:mysql://%s/%s?%s%s", "129.151.107.181", "misClientes", husoHorario, noUsarSSL);
            // cadena = "jdbc:mysql" + "://" + "200.116.155.1339" + ":" + "3306" + "/" + "misCliemtes";

            con = CConexiones.GetConnection(cadenaDeConfiguracion, "luislopez", "%jslslpzmjC12%", "Inicio.isClienteActivo");

            //Connection con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota());
            if (con != null) {
                st = con.createStatement();
                String sql = "select * from configuracion where nit='"
                        + nitCliente + "';";
                rs = st.executeQuery(sql);

                if (rs.next()) {
                    // java.util.Date fechaSistema = new java.util.Date();
                    // fechaSistema = (rs.getDate(1));

                    if (rs.getDate("fechaFin").before(fechaActual) || rs.getInt("activo") == 0) {
                        verificado = false;
                        this.estaClienteActivo = false;
                        JOptionPane.showMessageDialog(null, "Cliente no activo, la licencia ha caducado \n" + fechaActual, "Licencia no activa", JOptionPane.ERROR_MESSAGE);
                    } else {
                        verificado = true;
                        this.estaClienteActivo = true;

                    }
                    if (rs.getInt("GPSservice") == 1) {
                        setGPSservice(true);
                    }
                    if (rs.getInt("APPservice") == 1) {
                        setAPPservice(true);
                    }
                    if (rs.getInt("MTTOservice") == 1) {
                        setMTTOservice(true);
                    }
                    setIdAliado(rs.getString("consecutivo"));

                } else {
                    this.estaClienteActivo = false;
                    System.out.println("No existe el cliente en la BBDD " + verificado);
                }
                System.out.println("mensaje al salir de la verificacion " + verificado);
                //JOptionPane.showMessageDialog(null, "mensaje al salir de la verificacion " + sql, "No Existe", 1);
                rs.close();
                st.close();
                con.close();
            }

        } catch (SQLException | HeadlessException ex) {
            System.out.println("Error en verificarClienteActivo() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(null, "Error al retornar datos : \n" + e);
        }

        return verificado;
        //  return true;
    }

    public boolean verificarClienteActivo(int aliado) {
        boolean verificado = false;
        this.estaClienteActivo = false;
        //config();
        ResultSet rs = null;
        Statement st;

        String servidor = "jdbc:mysql://www.logarea.net:3306/misClientes";

        try {
            Connection con = CConexiones.GetConnection(servidor, "luislopez", "jslslpzmjc1212", "Inicio.verificarClienteActivo");

            if (con != null) {
                st = con.createStatement();
                String sql = "select fechaFin,activo from configuracion where idAliado='" + aliado + "';";
                rs = st.executeQuery(sql);

                if (rs.next()) {

                    java.util.Date fechaActual = new java.util.Date();
                    if (rs.getDate("fechaFin").before(fechaActual) || rs.getInt("activo") == 0) {
                        verificado = false;
                        this.estaClienteActivo = false;
                    } else {
                        verificado = true;
                        this.estaClienteActivo = true;

                    }

                }
                rs.close();
                st.close();
                con.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en verificarClienteActivo() " + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(null, "Error al retornar datos : \n" + e);
        }

        return verificado;
    }

    public boolean verificarConexion() {
        boolean verificado = true;

        if (propiedades.getProperty("cuentaGoogleCloud").equals("true")) {

            try {

                URL url = new URL("http://www.google.com");
                // URL url = new URL("http://www.logarea.net");
                URLConnection urlc = url.openConnection();
                InputStream is = urlc.getInputStream();
                byte b = (byte) is.read();
                if (b != -1) {
                    // System.out.write(b);
                    // b = (byte) is.read();
                    verificado = true;

                }

            } catch (MalformedURLException ex) {
                System.err.println("Error al conectar al website de Java: " + ex);
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
                verificado = false;
            } catch (IOException ex) {
                System.err.println("Error al establecer lax conexión: " + ex);
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
                verificado = false;
            }
        }

        return verificado;
        // http://www.latindevelopers.com/forum/conectar-a-internet-t630.html
    }

    public boolean validarInternet() {
        boolean validado = false;
        try {
            InetAddress address = InetAddress.getByName("www.facebook.com");
            System.out.println(address);
            validado = true;
        } catch (UnknownHostException e) {
            System.err.println("Couldn't find www.facebook.com");
        }
        return validado;
    }

    public String Encriptexto(String texto) {
        String tx = null;
        try {
            tx = cifrar(texto);
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en Encriptexto() " + ex);
        }
        return tx;
    }

    public String deCriptexto(String texto) {
        String tx = null;
        try {
            tx = deCifrar(texto);
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en deCriptexto() " + ex);
        }
        return tx;
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public static void moverArchivo(String origen, String destino) {
        System.out.println("Desde: " + origen);
        System.out.println("Hacia: " + destino);

        try {
            File inFile = new File(origen);
            File outFile = new File(destino);

            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile);

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }

            in.close();
            out.close();

            File file = new File(origen);
            if (file.exists()) {
                // file.delete();
            }

        } catch (IOException ex) {
            System.err.println("Hubo un error de entrada/salida!!!" + ex);
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Image getImage(byte[] bytes, boolean isThumbnail) throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis; // File or InputStream
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        if (isThumbnail) {

            param.setSourceSubsampling(4, 4, 0, 0);

        }
        return reader.read(0, param);

    }

    public Date getFechaActualServidor() throws SQLException {
        Date fecha = null;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        String sql = "SELECT CURDATE();";
        try {
            //con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");

            con = getConnLocal();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    fecha = rst.getDate(1);
                }
                st.close();
                //con.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en consulta sql " + ex + "(sql=" + sql + ")");
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
                //con.close();
            }

            //JOptionPane.showMessageDialog(null, "No se pudo grabar  en e sistema porque : \n" + ex);
        }
        return fecha;
    }

    public Date getFechaYHoraActualServidor() throws SQLException {
        Date fecha = null;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        String sql = "SELECT CURRENT_TIMESTAMP();";
        try {
            //con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal, "local");
            con = getConnLocal();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {
                    fecha = rst.getDate(1);
                }
                st.close();
                // con.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + sql + ")");
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
            if (con != null) {
                // con.close();
            }

            //JOptionPane.showMessageDialog(null, "No se pudo grabar  en e sistema porque : \n" + ex);
        }
        return fecha;
    }

    public String arrListadoDeManifiestos() {
        String sql = null;
        sql = " SELECT * "
                + "FROM "
                + "vst_manifiestodedistribucion "
                + "WHERE  fechaDistribucion <=  CURDATE()  AND "
                + " fechaDistribucion <=  DATE_SUB(CURDATE(), INTERVAL 1 WEEK)";

        return sql;
    }

    public String arrListadoDeManifiestos(int estadoManifiesto) {
        String sql = null;
        sql = "SELECT * "
                + "FROM vst_manifiestodedistribucion "
                + "WHERE  estadoManifiesto<='" + estadoManifiesto + "'  order by  numeroManifiesto asc;";

        return sql;
    }

    public String arrListadoDeManifiestosSinConciliar() {
        String sql = null;

        sql = "SELECT * "
                + "FROM vst_manifiestodedistribucion "
                + "WHERE  (estadoManifiesto='4' and trazabilidad=1) or "
                + " (estadoManifiesto='3' )  order by  fechaDistribucion,numeroManifiesto asc;";

        return sql;
    }

    public String arrListadoDeFacturasEnDistribucion(java.sql.Date fecha) {
        String sql = null;
        sql = "SELECT * "
                + "FROM vst_defintivofacturaspormanifiesto "
                + "WHERE  fechaDistribucion='" + fecha + "' "
                + "order by  fechaDistribucion,numeroManifiesto,adherencia asc;";

        return sql;
    }

    public String arrListadoDeManifiestosEnDistribucion(java.sql.Date fecha) {
        String sql = null;
        sql = "SELECT * "
                + "FROM vst_manifiestodedistribucion "
                + "WHERE  fechaDistribucion='" + fecha + "' "
                + "order by  nombreCanal,vehiculo,tipoVehiculo asc;";

        return sql;
    }

    public String arrListadoDeManifiestosSinDescargar() {
        String sql = null;
        sql = "SELECT * "
                + "FROM vst_manifiestodedistribucion "
                + "WHERE  estadoManifiesto<='3' "
                + "order by fechaDistribucion,nombreCanal,vehiculo,tipoVehiculo asc;";

        return sql;
    }

    /**
     * Método que llena una array list con todos los manifiestos que se
     * encuentran en distribucion
     *
     */
    public void LLenarListaDeManifiestosSinDescargar() {// 
        listaDeManifiestossinDescargar = new ArrayList();

        try {

            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            //con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota, "Inicio.LLenarListaDeManifiestosSinDescargar");

            con = getConnRemota();
            sql = " SELECT * "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE "
                    + "estadoManifiesto<=3 order by numeroManifiesto";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CManifiestosDeDistribucion man = new CManifiestosDeDistribucion();

                    man.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    man.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    man.setVehiculo(rst.getString("vehiculo"));
                    man.setTipoContrato(rst.getString("tipoContrato"));
                    man.setTipoVehiculo(rst.getString("tipoVehiculo"));
                    man.setConductor(rst.getString("conductor"));
                    man.setApellidosConductor(idAliado);
                    man.setNombreConductor(rst.getString("nombreConductor"));
                    man.setApellidosConductor(rst.getString("apellidosConductor"));
                    man.setDespachador(rst.getString("despachador"));
                    man.setNombreDespachador(rst.getString("nombreDespachador"));
                    man.setIdCanal(rst.getInt("idCanal"));
                    man.setNombreCanal(rst.getString("nombreCanal"));
                    man.setIdRuta(rst.getInt("idRuta"));
                    man.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    man.setTipoRuta(rst.getString("tipoRuta"));
                    man.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                    man.setKmSalida(rst.getInt("kmSalida"));
                    man.setKmEntrada(rst.getInt("kmEntrada"));
                    man.setKmRecorrido(rst.getInt("kmRecorrido"));
                    man.setIsFree(rst.getInt("isFree"));
                    man.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    man.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    man.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                    man.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                    man.setActivo(rst.getInt("activo"));
                    man.setFechaReal(rst.getString("fechaReal"));
                    man.setUsuarioManifiesto(rst.getString("usuarioManifiesto"));
                    man.setTrazabilidad(rst.getInt("trazabilidad"));
                    man.setCantidadPedidos(rst.getInt("CantidadPedidos"));
                    man.setValorConsignado(rst.getDouble("valorConsignado"));
                    man.setZona(rst.getInt("zona"));
                    man.setRegional(rst.getInt("regional"));
                    man.setAgencia(rst.getInt("agencia"));

                    listaDeManifiestossinDescargar.add(man);
                }
                rst.close();
                st.close();
                // con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en LLenarListaDeManifiestosSinDescargar sql " + ex.getMessage());
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Error en LLenarListaDeManifiestosSinDescargar sql " + ex.getMessage());
            Logger.getLogger(Inicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local, los cuales
     * provienen de un archivo en excel tomado previamente y del cual puede
     * salir la lista de Sentencias SQL para la inserción de los datos."
     *
     * @param listaDeSentenciasSQL corresponde a una lista de sentencias SQL
     * guardadas en un arrayLIs
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    public boolean insertarBBDDRemota(List<String> listaDeSentenciasSQL, String tabla) {
        boolean insertar = false;
        Connection con = null;
        Statement st;
        String cadena = "";

        // con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota);
        con = getConnRemota();
        //con = CconexionMtto.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota);
        if (con != null) {
            try {
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {
                    try {
                        st.execute(obj);
                        cadena = obj;
                        System.out.println("dato insertado servidor remoto -->" + tabla);
                    } catch (Exception ex) {
                        System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
                        Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                insertar = true;
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
            }

        }

        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Remota, los
     * cuales pueden venir de un archivo en excel o de otro formato pero
     * encapsulado en un Array list de tipo String, tomado previamente y del
     * cual puede salir la lista de Sentencias SQL para la inserción de los
     * datos."
     *
     * @param sql corresponde a una sentencias SQL guardadas en un arrayLIs
     * @return devuelve verdadero si graba, false si se genera algún problema
     */
    public boolean insertarBBDDMantenimientos_(String sql) {
        boolean insertar = false;
        Connection con = null;
        try {

            Statement st;//= null;

//            con = CConexiones.GetConnection(cadenaMantenimientos, usuarioBDMantenimientos, claveBDMantenimientos);
            //           con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
            con = getConnRemota();
            con.setAutoCommit(false);

            if (con != null) {
                st = con.createStatement();
                st.execute(sql);
                insertar = true;

                con.commit();
                st.close();
                // con.close();

            }

        } catch (SQLException ex) {
            try {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
                insertar = false;
                con.rollback();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return insertar;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD Mantenimientos,
     * los cuales provienen de un archivo en excel tomado previamente y del cual
     * puede salir la lista de Sentencias SQL para la inserción de los datos."
     *
     * @param listaDeSentenciasSQL corresponde a una lista de sentencias SQL
     * guardadas en un arrayLIs
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    public boolean insertarBBDDMantenimientos_(List<String> listaDeSentenciasSQL) {
        boolean insertar = false;
        Connection con = null;
        Statement sentencia;
        String cadena = "";

//        con = CConexiones.GetConnection(cadenaMantenimientos, usuarioBDMantenimientos, claveBDMantenimientos);
        //con = CconexionMtto.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota);
        //    con = CConexiones.GetConnection(this.getCadenaRemota(), this.getUsuarioBDRemota(), this.getClaveBDRemota(), "HiloListadoDeVehiculos");
        con = getConnRemota();
        if (con != null) {
            try {
                sentencia = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {
                    try {
                        cadena = obj;
                        sentencia.execute(obj);

                        System.out.println("dato insertado servidor remoto -->" + obj);
                    } catch (Exception ex) {
                        System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
                        Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                insertar = true;
                sentencia.close();
                // con.close();
            } catch (SQLException ex) {
                System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);

            }

        }

        return insertar;
    }

    public int contarRegistros(String[] lista) {
        int valor = 0;
        String sql;
        Connection con = null;
        Statement st = null;
        ResultSet rst;

        //con = CConexiones.GetConnection(cadenaLocal, usuarioBDLocal, claveBDLocal);
        con = getConnLocal();

        if (con != null) {
            try {
                st = con.createStatement();

                for (String obj : lista) {
                    sql = "Select count flag from " + obj + " where activo=1";
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        valor += rst.getInt("flag");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return valor;
    }

    /**
     * Método encargado de evaluar un array que contiene los numeros de las
     * facuras y evalua cuáles de ellos se pueden eliminar ó anular para no ser
     * contabilizados
     *
     * @return lista de factura ya evaluadas y verificadas
     * @see http://www.aulaclic.es/sql/spr_3_1_1.htm
     * @param listaDeFacturas para evaluar
     */
    public List<String> getNumerosDeFacturasAptasParaAnular(List<String> listaDeFacturas) {

        String sql, cadena;
        Connection con = null;
        Statement st = null;
        ResultSet rst;
        List<String> numerosFacturasEvaluadas = new ArrayList();
        List<String> numerosFacturasPendiente = new ArrayList();

        //  con = CConexiones.GetConnection(cadenaRemota, usuarioBDRemota, claveBDRemota);
        con = getConnRemota();
        if (con != null) {
            try {
                st = con.createStatement();
                cadena = "";

                int i = listaDeFacturas.size();
                for (String obj : listaDeFacturas) {
                    if (i != listaDeFacturas.size()) {
                        cadena += obj + ",";
                    } else {
                        cadena += obj;
                    }
                }

                /*selecciona los registros que hayan salido a distribucion */
                sql = "SELECT * "
                        + "FROM "
                        + "facturasdescargadas "
                        + "WHERE "
                        + "numeroFactura  IN (" + cadena + ") and activo=1";

                rst = st.executeQuery(sql);
                cadena = "";

                while (rst.next()) {
                    CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(this);

                    facturaDescargada.setConsecutivo((rst.getInt("consecutivo")));
                    facturaDescargada.setNumeroManifiesto((rst.getString("numeroManifiesto")));
                    facturaDescargada.setNumeroFactura(rst.getString("numeroFactura"));
                    facturaDescargada.setValorRechazo((rst.getDouble("valorRechazo")));
                    facturaDescargada.setValorDescuento((rst.getDouble("valorDescuento")));
                    facturaDescargada.setValorRecaudado((rst.getDouble("valorRecaudado")));
                    facturaDescargada.setIdTipoDeMovimiento((rst.getInt("movimientoFactura")));
                    facturaDescargada.setCausalDeRechazo((rst.getInt("motivoRechazo")));
                    facturaDescargada.setActivo((rst.getInt("activo")));

                    /*Se evalúa que haya sido un rechazo total */
                    if (facturaDescargada.getIdTipoMovimiento() == 3) {

                        /* se agrega a la lista ya evaluada*/
                        numerosFacturasEvaluadas.add(facturaDescargada.getNumeroFactura());

                    } else {
                        cadena = facturaDescargada.getNumeroFactura() + ",";
                    }

                }
                cadena = cadena.substring(0, cadena.length() - 1);

                /* SELECCIONA LAS FACTURAS QUE NO HAYAN SALIDO A DISTRIBUCION APTAS PARA ANULAR*/
                sql = " SELECT facturascamdun.numeroFactura,facturascamdun.vendedor,facturaspormanifiesto.numeroManifiesto"
                        + "FROM facturaspormanifiesto "
                        + "right OUTER JOIN facturascamdun ON facturaspormanifiesto.numeroFactura=facturascamdun.numeroFactura"
                        + "WHERE "
                        + "facturascamdun.numeroFactura in(" + cadena + ")";

                rst = st.executeQuery(sql);
                while (rst.next()) {
                    String valor = "" + rst.getInt("numeroManifiesto");

                    if (valor == null || valor.isEmpty()) {
                        /* se agrega a la lista ya evaluada*/
                        numerosFacturasEvaluadas.add(rst.getString("numeroFactura"));
                    }

                }

            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Inicio.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return numerosFacturasEvaluadas;
    }

    public String getMes(int mes) {
        String elMes = "";
        switch (mes) {

            case 0:
                elMes = "Enero";
                break;
            case 1:
                elMes = "Febrero";
                break;
            case 2:
                elMes = "Marzo";
                break;
            case 3:
                elMes = "Abril";
                break;
            case 4:
                elMes = "Mayo";
                break;
            case 5:
                elMes = "Junio";
                break;
            case 6:
                elMes = "Julio";
                break;
            case 7:
                elMes = "Agosto";
                break;
            case 8:
                elMes = "Septiembre";
                break;
            case 9:
                elMes = "Octubre";
                break;
            case 10:
                elMes = "Noviembre";
                break;
            case 11:
                elMes = "Diciembre";
                break;
        }
        return elMes;
    }

    public void ipLocal() {
        try {
            //Obtener nombre y direccion IP del equipo local juntos
            InetAddress direccion = InetAddress.getLocalHost();
            System.out.println("Localhost = " + direccion);

            direcionIpLocal = direccion.getHostAddress();
            nombreEstacionLocal = direccion.getHostName();

            // Obtener nombre y direccion Ip del equipo local por separado
            System.out.println("HostAddress: " + direccion.getHostAddress());
            System.out.println("HostName: " + direccion.getHostName());

            //Obtener el nombre y direccion IP de un host remoto mediente su nombre
            // System.out.println("Direccion UTEZ: " + direccion.getByName("www.utez.edu.mx"));
            //Obtener el nombre y direccion IP de un equipo remoto conectado a una misma red
            // System.out.println("Direccion RYUK: " + direccion.getByName("PC-RPGRAMER"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(FRegistroDePedidosFinLineaDePicking.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*Funcion que permite colocar imagen al fondo del escritorio */
    public final void cargarImagenEscritorio(javax.swing.JDesktopPane jDeskp) {
        try {

            String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/imagenesDeFondo/" + propiedades.getProperty("fondoApp");
            File f = new File(ruta);
            InputStream foto1 = new FileInputStream(f);

            BufferedImage image = ImageIO.read(foto1);
            jDeskp.setBorder(new Fondo(image));

        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
    }

    public String getGeoPositionCliente() {
        return geoPositionCliente;
    }

    public void setGeoPositionCliente(String geoPositionCliente) {
        this.geoPositionCliente = geoPositionCliente;
    }

    private boolean timerEvent(java.awt.event.ActionEvent evt) {
        boolean valor = false;
        if (conectado) {
            timer.stop();
            valor = true;
            // JOptionPane.showMessageDialog(null, "No hay conexion con el servidor Remoto", "Error de conexion", JOptionPane.ERROR_MESSAGE);
            //System.exit(0);
//            timer = new Timer(60000, actionListener);
//            System.out.println("se llama al timer");
            timer.stop();
            System.out.println("inicia el timer");
//            System.out.println("expired en funcion");
        } else {
            JOptionPane.showMessageDialog(null, "No hay conexion con el servidor Remoto", "Error de conexion", JOptionPane.ERROR_MESSAGE);
            timer.stop();

        }
        return valor;
    }

}
