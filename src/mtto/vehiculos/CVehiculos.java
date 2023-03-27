/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos;

import mtto.ingresoDeRegistros.objetos.MantenimientosPorPlaca;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import mtto.vehiculos.Administracion.CLineasPorMarca;
import aplicacionlogistica.distribucion.objetos.personas.CPersonas;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import mtto.documentos.objetos.DocumentosPorTipoDeVehiculo;
import mtto.documentos.objetos.DocumentosPorVehiculo;

/**
 *
 * @author Usuario
 */
public class CVehiculos {

    String placa;
    int tipoTraccion;
    String conductor;
    String nombreConductor;
    String apellidosConductor;
    String trailer;
    Double pesoTotalSinCarga;
    Double largoVehiculo;
    Double alturaVehiculo;
    Double anchuraVehiculo;
    Double longitudVehiculo;
    Double pesoTotalAutorizado;
    Double capacidadInstalada;
    int idLineaVehiculo;
    String nombreLineaVehiculo;
    String nombreMarcaDeVehiculo;
    int tipoVehiculo;
    String nombreTipoVehiculo;
    int tipoCarroceria;
    String nombreTipoCarroceria;
    int tipoContrato;
    String nombreTipoContrato;
    int estadoActual;
    String nombreEstadoVehiculo;
    String propietario;
    String tarjetaPropiedad;
    int cantidadLLantas;
    String tamanioLlantas;
    String serialChasis;
    int activoVehiculo;
    ImageIcon imagen;
    File fotografia = null;
    String tipoMime;
    Date fechaKilometraje;
    int kilometrajeActual;
    int agencia;
    int flag;
    String usuario;
    Inicio ini;
    CLineasPorMarca lineaVehiculo;

    String modelo;
    int idTipoServicio;
    String nombreTipoServicio;
    String serialMotor;
    int idTipoCombustible;
    String nombreTipoCombustible;
    int kmCambioValvulinaTrasmision;
    int kmCambioAceiteMotor;
    int kmCambioValvulinaCaja;
    boolean vehiculoConDocumentosVencidos;

    List<MantenimientosPorPlaca> listaDeMantenimientosPorPlaca;
    List<DocumentosPorVehiculo> listaDeDocumentosPorVehiculo;
    List<DocumentosPorTipoDeVehiculo> listaDeDocumentosPorTipoDeVehiculo;

    public List<DocumentosPorTipoDeVehiculo> getListaDeDocumentosPorTipoDeVehiculo() {
        return listaDeDocumentosPorTipoDeVehiculo;
    }

    public void setListaDeDocumentosPorTipoDeVehiculo(List<DocumentosPorTipoDeVehiculo> listaDeDocumentosPorTipoDeVehiculo) {
        this.listaDeDocumentosPorTipoDeVehiculo = listaDeDocumentosPorTipoDeVehiculo;
    }

    public String getNombreMarcaDeVehiculo() {
        return nombreMarcaDeVehiculo;
    }

    public void setNombreMarcaDeVehiculo(String nombreMarcaDeVehiculo) {
        this.nombreMarcaDeVehiculo = nombreMarcaDeVehiculo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(int estadoActual) {
        this.estadoActual = estadoActual;
    }

    public int getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(int kilometraje) {
        this.kilometrajeActual = kilometraje;
    }

    public void setKilometrajeActual() {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        //String val1, val2;

        con = ini.getConnGPS();

        sql = "SELECT d.id, d.name, "
                + "JSON_EXTRACT(d.attributes,'$.Kilometraje') as kilometrajeInical,"
                + "ROUND((JSON_EXTRACT(p.attributes,'$.totalDistance')/1000),2) as distanciaTotal "
                + "FROM tc_devices d "
                + "join tc_positions p on p.deviceid = d.id "
                + "where d.name ='" + this.placa + "' and "
                + "d.positionid = p.id ;";
                //+ "order by p.id desc "
                //+ "limit 0, 1;";
        try {
            st = con.createStatement();
            rst = st.executeQuery(sql);

            if (rst.next()) {
                //val1 = "" + rst.getInt("kilometrajeInical");
                //val2 = "" + rst.getInt("distanciaTotal");
                this.kilometrajeActual = rst.getInt("kilometrajeInical") + rst.getInt("distanciaTotal");

            }
            rst.close();
            st.close();
            // // con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getTipoTraccion() {
        return tipoTraccion;
    }

    public void setTipoTraccion(int tipoTraccion) {
        this.tipoTraccion = tipoTraccion;
    }

    public Date getFechaKilometraje() {
        return fechaKilometraje;
    }

    public void setFechaKilometraje(Date fechaKilometraje) {
        this.fechaKilometraje = fechaKilometraje;
    }

    public CLineasPorMarca getLineaVehiculo() {
        return lineaVehiculo;
    }

    public void setLineaVehiculo(CLineasPorMarca lineaVehiculo) {
        this.lineaVehiculo = lineaVehiculo;
    }

    public void setLineaVehiculo() {
        try {
            lineaVehiculo = new CLineasPorMarca(ini, idLineaVehiculo);

        } catch (Exception ex) {
            Logger.getLogger(CVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getTipoDeTracccion() {
        return tipoTraccion;
    }

    public void setTipoDeTracccion(int tipoDeTracccion) {
        this.tipoTraccion = tipoDeTracccion;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public void setConductor(String placa, String conductor) {
        String sql = "UPDATE vehiculos set conductor='" + conductor + "' WHERE placa='" + placa + "';";
        ini.insertarDatosRemotamente(sql);
        this.conductor = conductor;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public Double getPesoTotalSinCarga() {
        return pesoTotalSinCarga;
    }

    public void setPesoTotalSinCarga(Double pesoTotalSinCarga) {
        this.pesoTotalSinCarga = pesoTotalSinCarga;
    }

    public Double getLargoVehiculo() {
        return largoVehiculo;
    }

    public void setLargoVehiculo(Double largoVehiculo) {
        this.largoVehiculo = largoVehiculo;
    }

    public Double getAlturaVehiculo() {
        return alturaVehiculo;
    }

    public void setAlturaVehiculo(Double alturaVehiculo) {
        this.alturaVehiculo = alturaVehiculo;
    }

    public Double getAnchuraVehiculo() {
        return anchuraVehiculo;
    }

    public void setAnchuraVehiculo(Double anchuraVehiculo) {
        this.anchuraVehiculo = anchuraVehiculo;
    }

    public Double getLongitudVehiculo() {
        return longitudVehiculo;
    }

    public void setLongitudVehiculo(Double longitudVehiculo) {
        this.longitudVehiculo = longitudVehiculo;
    }

    public Double getPesoTotalAutorizado() {
        return pesoTotalAutorizado;
    }

    public void setPesoTotalAutorizado(Double pesoTotalAutorizado) {
        this.pesoTotalAutorizado = pesoTotalAutorizado;
    }

    public Double getCapacidadInstalada() {
        return capacidadInstalada;
    }

    public void setCapacidadInstalada(Double capacidadInstalada) {
        this.capacidadInstalada = capacidadInstalada;
    }

    public int getIdLineaVehiculo() {
        return idLineaVehiculo;
    }

    public void setIdLineaVehiculo(int idLneaVehiculo) {
        this.idLineaVehiculo = idLneaVehiculo;
    }

    public int getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(int tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public int getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(int tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }

    public int getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(int tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getTarjetaPropiedad() {
        return tarjetaPropiedad;
    }

    public void setTarjetaPropiedad(String tarjetaPropiedad) {
        this.tarjetaPropiedad = tarjetaPropiedad;
    }

    public int getCantidadLLantas() {
        return cantidadLLantas;
    }

    public void setCantidadLLantas(int cantidadLLantas) {
        this.cantidadLLantas = cantidadLLantas;
    }

    public String getTamanioLlantas() {
        return tamanioLlantas;
    }

    public void setTamanioLlantas(String tamanioLlantas) {
        this.tamanioLlantas = tamanioLlantas;
    }

    public String getSerialChasis() {
        return serialChasis;
    }

    public void setSerialChasis(String serialChasis) {
        this.serialChasis = serialChasis;
    }

    public int getActivoVehiculo() {
        return activoVehiculo;
    }

    public void setActivoVehiculo(int activoVehiculo) {
        this.activoVehiculo = activoVehiculo;
    }

    public int getFlag() {
        return flag;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public ImageIcon getImagen() {
        return imagen;
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
    }

    public ImageIcon setImagen() {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "SELECT  imagen "
                + " FROM vehiculos"
                + " where "
                + "vehiculos.placa='" + placa + "';";
        try {
            st = con.createStatement();
            rst = st.executeQuery(sql);

            if (rst.next()) {

                InputStream is = rst.getBinaryStream("imagen");
                BufferedImage bi = ImageIO.read(is);
                if (bi != null) {
                    this.imagen = new ImageIcon(bi);
                } else {
                    this.imagen = null;
                }

            }
            rst.close();
            st.close();
            //// con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.imagen;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public File getFotografia() {
        return fotografia;
    }

    public void setFotografia(File fotografia) {
        this.fotografia = fotografia;
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

    public String getNombreLineaVehiculo() {
        return nombreLineaVehiculo;
    }

    public void setNombreLineaVehiculo(String nombreLineaVehiculo) {
        this.nombreLineaVehiculo = nombreLineaVehiculo;
    }

    public String getNombreTipoVehiculo() {
        return nombreTipoVehiculo;
    }

    public void setNombreTipoVehiculo(String nombreTipoVehiculo) {
        this.nombreTipoVehiculo = nombreTipoVehiculo;
    }

    public String getNombreTipoCarroceria() {
        return nombreTipoCarroceria;
    }

    public void setNombreTipoCarroceria(String nombreTipoCarroceria) {
        this.nombreTipoCarroceria = nombreTipoCarroceria;
    }

    public String getNombreTipoContrato() {
        return nombreTipoContrato;
    }

    public void setNombreTipoContrato(String nombreTipoContrato) {
        this.nombreTipoContrato = nombreTipoContrato;
    }

    public String getNombreEstadoVehiculo() {
        return nombreEstadoVehiculo;
    }

    public void setNombreEstadoVehiculo(String nombreEstadoVehiculo) {
        this.nombreEstadoVehiculo = nombreEstadoVehiculo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(int idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }

    public String getNombreTipoServicio() {
        return nombreTipoServicio;
    }

    public void setNombreTipoServicio(String nombreTipoServicio) {
        this.nombreTipoServicio = nombreTipoServicio;
    }

    public String getSerialMotor() {
        return serialMotor;
    }

    public void setSerialMotor(String serialMotor) {
        this.serialMotor = serialMotor;
    }

    public int getIdTipoCombustible() {
        return idTipoCombustible;
    }

    public void setIdTipoCombustible(int idTipoCombustible) {
        this.idTipoCombustible = idTipoCombustible;
    }

    public String getNombreTipoCombustible() {
        return nombreTipoCombustible;
    }

    public void setNombreTipoCombustible(String nombreTipoCombustible) {
        this.nombreTipoCombustible = nombreTipoCombustible;
    }

    public int getKmCambioValvulinaTrasmision() {
        return kmCambioValvulinaTrasmision;
    }

    public void setKmCambioValvulinaTrasmision(int kmCambioValvulinaTrasmision) {
        this.kmCambioValvulinaTrasmision = kmCambioValvulinaTrasmision;
    }

    public int getKmCambioAceiteMotor() {
        return kmCambioAceiteMotor;
    }

    public void setKmCambioAceiteMotor(int kmCambioAceiteMotor) {
        this.kmCambioAceiteMotor = kmCambioAceiteMotor;
    }

    public int getKmCambioValvulinaCaja() {
        return kmCambioValvulinaCaja;
    }

    public void setKmCambioValvulinaCaja(int kmCambioValvulinaCaja) {
        this.kmCambioValvulinaCaja = kmCambioValvulinaCaja;

    }

    public List<MantenimientosPorPlaca> getListaDeMantenimientos() {
        return listaDeMantenimientosPorPlaca;
    }

    public void setListaDeMantenimientos(List<MantenimientosPorPlaca> listaDeMantenimientos) {
        this.listaDeMantenimientosPorPlaca = listaDeMantenimientos;
    }

    public List<DocumentosPorVehiculo> getListaDeDocumentosPorVehiculo() {
        return listaDeDocumentosPorVehiculo;
    }

    public void setListaDeDocumentosPorVehiculo(List<DocumentosPorVehiculo> listaDeDocumentosPorVehiculo) {
        this.listaDeDocumentosPorVehiculo = listaDeDocumentosPorVehiculo;
    }

    public void setListaDeDocumentosPorVehiculo() {
        try {
            Connection con;
            Statement st;
            ResultSet rst;
            String sql = "select * from vst_DocumentosPorVehiculo where placa='" + this.placa + "';";
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            con = ini.getConnRemota();

            listaDeDocumentosPorVehiculo = new ArrayList<>();
            st = con.createStatement();
            rst = st.executeQuery(sql);

            if (rst.next()) {
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
                listaDeDocumentosPorVehiculo.add(dxv);

            }

        } catch (SQLException ex) {
            Logger.getLogger(CVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CVehiculos() {

    }

    public CVehiculos(Inicio ini) {
        this.ini = ini;
        
    }

    public CVehiculos(Inicio ini, String placa) throws Exception {
        this.ini = ini;
        this.placa = placa;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = this.ini.getConnRemota();

        sql = "select * from vst_vehiculos where placa='" + placa + "';";

        llenarDatos(con, sql);

    }

    public CVehiculos(Inicio ini, String placa, boolean mantenimientos) throws Exception {
        this.ini = ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = this.ini.getConnRemota();
        sql = "select * from vst_vehiculos where placa='" + placa + "';";

        llenarDatos(con, sql);

    }

    private void llenarDatos(Connection con, String sql) throws IOException {
        Statement st;
        ResultSet rst;
        try {
            st = con.createStatement();
            rst = st.executeQuery(sql);

            if (rst.next()) {

                this.placa = rst.getString("placa");
                this.tipoTraccion = rst.getInt("tipoTraccion");
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
                this.nombreLineaVehiculo = rst.getString("nombreLineaVehiculo");
                this.nombreMarcaDeVehiculo = rst.getString("nombreMarcaDeVehiculo");
                this.tipoVehiculo = rst.getInt("tipoVehiculo");
                this.nombreTipoVehiculo = rst.getString("nombreTipoVehiculo");
                this.tipoCarroceria = rst.getInt("tipoCarroceria");
                this.nombreTipoCarroceria = rst.getString("nombreTipoCarroceria");
                this.tipoContrato = rst.getInt("tipoContrato");
                this.nombreTipoContrato = rst.getString("nombreTipoContrato");
                this.propietario = rst.getString("propietario");
                this.tarjetaPropiedad = rst.getString("tarjetaPropiedad");
                this.cantidadLLantas = rst.getInt("cantidadLLantas");
                this.tamanioLlantas = rst.getString("tamanioLlantas");
                this.serialChasis = rst.getString("serialChasis");
                this.trailer = rst.getString("trailer");
                this.agencia = rst.getInt("agencia");
                this.modelo = rst.getString("modelo");
                this.idTipoServicio = rst.getInt("idTipoServicio");
                this.nombreTipoServicio = rst.getString("nombreTipoServicio");
                this.serialMotor = rst.getString("serialMotor");
                this.idTipoCombustible = rst.getInt("idTipoCombustible");
                this.nombreTipoCombustible = rst.getString("nombreTipoCombustible");
                this.kmCambioValvulinaTrasmision = rst.getInt("kmCambioValvulinaTrasmision");
                this.kmCambioAceiteMotor = rst.getInt("kmCambioAceiteMotor");
                this.kmCambioValvulinaCaja = rst.getInt("kmCambioValvulinaCaja");

                InputStream is = rst.getBinaryStream("imagen");
                BufferedImage bi = ImageIO.read(is);
                if (bi != null) {
                    this.imagen = new ImageIcon(bi);
                } else {
                    this.imagen = null;
                }

                this.tipoMime = rst.getString("tipoMime");
                this.estadoActual = rst.getInt("estadoActual");
                this.nombreEstadoVehiculo = rst.getString("nombreEstadoVehiculo");
                this.kilometrajeActual = rst.getInt("kilometrajeActual");
                this.fechaKilometraje = rst.getDate("fechaKilometraje");
                this.activoVehiculo = rst.getInt("activo");

            } else {
                this.placa = null;
                this.conductor = null;
                this.pesoTotalSinCarga = 0.0;
                this.largoVehiculo = 0.0;
                this.alturaVehiculo = 0.0;
                this.anchuraVehiculo = 0.0;
                this.longitudVehiculo = 0.0;
                this.pesoTotalAutorizado = 0.0;
                this.capacidadInstalada = 0.0;
                this.idLineaVehiculo = 0;
                this.tipoVehiculo = 0;
                this.tipoCarroceria = 0;
                this.tipoContrato = 0;
                this.propietario = "";
                this.tarjetaPropiedad = "";
                this.cantidadLLantas = 0;
                this.tamanioLlantas = "";
                this.serialChasis = "";
                this.trailer = "";
                this.agencia = 0;
            }
            rst.close();
            st.close();
            //// con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarVehiculo() throws Exception {
        boolean grab;
        String sql;

        sql = "INSERT INTO vehiculos(placa,tipoTraccion,conductor,pesoTotalSinCarga,largoVehiculo,alturaVehiculo,"
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

        grab = this.ini.insertarDatosRemotamente(sql);
        if (tipoContrato == 1) {
            //this.ini.insertarDatosRemotamente(sql);
        }

        return grab;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para insertar Datos en
     * la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     * @throws java.lang.Exception null si ini no existe.
     */
    public String getSentenciaInsertSQL() throws Exception {

        String sql;

        sql = "INSERT INTO vehiculos(placa,tipoTraccion,conductor,pesoTotalSinCarga,largoVehiculo,alturaVehiculo,"
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

        return sql;
    }

    protected boolean insertarFofografia(int bd) {
        boolean grab;
        Connection con = null;
        try {

            if (bd == 1) {

                // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                con = ini.getConnRemota();
            }
            if (bd == 2) {
                // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                ini.getConnRemota();
            }

            if (fotografia != null) {
                //String extension = fotografia.getAbsolutePath().substring(fotografia.getAbsolutePath().lastIndexOf(".")+1);
                Statement sentencia = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                String sql2 = "select * from vehiculos  where placa='" + this.placa + "';";
                ResultSet rs;
                rs = sentencia.executeQuery(sql2);
                con.setAutoCommit(false);
                PreparedStatement pst;
                pst = con.prepareStatement("Update vehiculos set imagen=?, tipoMime=? where placa=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                while (rs.next()) {
                    //File file = new File(fotografia.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(fotografia);
                    pst.setBinaryStream(1, fis, fotografia.length());
                    pst.setString(2, this.getTipoMime());
                    pst.setString(3, rs.getString(1));
                    pst.executeUpdate();
                    con.commit();
                }

                pst.close();
                rs.close();
                // con.close();

            }

            grab = true;

            // con.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error en insertar() archivo no encontrado " + ex.getMessage());
            grab = false;
        } catch (SQLException ex) {
            System.out.println("Error en insertar() consulta sql " + ex.getMessage() + "(sql=");
            grab = false;
        }
        return grab;
    }

    public String StringSQLUpdatekilometraje() {
        String sql = null;
        try {
            sql = "update vehiculos set kilometraje='" + this.kilometrajeActual + "' where vehiculos.placa='" + this.placa + "'";

        } catch (Exception ex) {
            Logger.getLogger(CCarros.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }

    public MantenimientosPorPlaca getUltimoMantenimiento(int tipoMantenimiento) {
        String sql;
        ResultSet rst;
        Connection con;
        Statement st;
        MantenimientosPorPlaca mxPlaca = null;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "select * from vst_mantenimientosporplaca where placaVehiculo='" + this.placa + "' and activo=1 and "
                + "idtipoMantenimiento='" + tipoMantenimiento + "' "
                + "order by fechaIng desc limit 1 ;";

        if (con != null) {
            mxPlaca = new MantenimientosPorPlaca(ini);
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    mxPlaca.setPlacaVehiculo(rst.getString("placaVehiculo"));
                    mxPlaca.setIdMantenimientosPorPlaca(rst.getInt("idMantenimientosPorPlaca"));
                    mxPlaca.setIdtipoMantenimiento(rst.getInt("idtipoMantenimiento"));
                    mxPlaca.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                    mxPlaca.setConductorVehiculo(rst.getString("conductorVehiculo"));
                    mxPlaca.setNombreConductor(rst.getString("nombreConductor"));
                    mxPlaca.setApellidosConductor(rst.getString("apellidosConductor"));
                    mxPlaca.setIdNumeroDeorden(rst.getInt("idNumeroDeorden"));
                    mxPlaca.setKilometrajeMantenimiento(rst.getInt("kilometrajeMantenimiento"));
                    mxPlaca.setFechaMantenimiento(rst.getDate("fechaMantenimiento"));
                    mxPlaca.setZona(rst.getInt("zona"));
                    mxPlaca.setNombreZona(rst.getString("nombreZona"));
                    mxPlaca.setRegional(rst.getInt("regional"));
                    mxPlaca.setNombreRegional(rst.getString("nombreRegional"));
                    mxPlaca.setAgencia(rst.getInt("agencia"));
                    mxPlaca.setNombreAgencia(rst.getString("nombreAgencia"));
                    mxPlaca.setObservaciones(rst.getString("observaciones"));
                    mxPlaca.setActivo(rst.getInt("activo"));
                    mxPlaca.setFechaIng(rst.getDate("fechaIng"));
                    mxPlaca.setUsuario(rst.getString("usuario"));
                    mxPlaca.setFlag(rst.getInt("flag"));
                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabar carros " + ex);
            }

        }

        return mxPlaca;
    }

    public void setListaDeMantenimientosPorPlaca() {
        String sql;
        ResultSet rst;
        Connection con;
        Statement st;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "select * from vst_mantenimientosporplaca where placa='" + this.placa + "' and activo=1;";

        if (con != null) {
            listaDeMantenimientosPorPlaca = new ArrayList<>();
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                asignarValoresMantenimientos(rst);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabar carros " + ex);
            }

        }

    }

    private void asignarValoresMantenimientos(ResultSet rst) throws SQLException {
        while (rst.next()) {

            MantenimientosPorPlaca mxPlaca = new MantenimientosPorPlaca();

            mxPlaca.setPlacaVehiculo(rst.getString("placaVehiculo"));
            mxPlaca.setIdMantenimientosPorPlaca(rst.getInt("idMantenimientosPorPlaca"));
            mxPlaca.setIdtipoMantenimiento(rst.getInt("idtipoMantenimiento"));
            mxPlaca.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
            mxPlaca.setConductorVehiculo(rst.getString("conductorVehiculo"));
            mxPlaca.setNombreConductor(rst.getString("nombreConductor"));
            mxPlaca.setApellidosConductor(rst.getString("apellidosConductor"));
            mxPlaca.setIdNumeroDeorden(rst.getInt("idNumeroDeorden"));
            mxPlaca.setKilometrajeMantenimiento(rst.getInt("kilometrajeMantenimiento"));
            mxPlaca.setFechaMantenimiento(rst.getDate("fechaMantenimiento"));
            mxPlaca.setZona(rst.getInt("zona"));
            mxPlaca.setNombreZona(rst.getString("nombreZona"));
            mxPlaca.setRegional(rst.getInt("regional"));
            mxPlaca.setNombreRegional(rst.getString("nombreRegional"));
            mxPlaca.setAgencia(rst.getInt("agencia"));
            mxPlaca.setNombreAgencia(rst.getString("nombreAgencia"));
            mxPlaca.setObservaciones(rst.getString("observaciones"));
            mxPlaca.setActivo(rst.getInt("activo"));
            mxPlaca.setFechaIng(rst.getDate("fechaIng"));
            mxPlaca.setUsuario(rst.getString("usuario"));
            mxPlaca.setFlag(rst.getInt("flag"));

            listaDeMantenimientosPorPlaca.add(mxPlaca);

        }
    }

    public void setListaDeMantenimientosFlota() {
        String sql;
        ResultSet rst;
        Connection con;
        Statement st;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "select * from vst_mantenimientosporplaca where  and activo=1;";

        if (con != null) {
            listaDeMantenimientosPorPlaca = new ArrayList<>();
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                asignarValoresMantenimientos(rst);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabar carros " + ex);
            }

        }

    }

    public MantenimientosPorPlaca setListaDeMantenimientos(int tipoMantenimiento) {
        String sql;
        ResultSet rst;
        Connection con;
        Statement st;
        MantenimientosPorPlaca mxPlaca = new MantenimientosPorPlaca();

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "select * from vst_mantenimientosporplaca where idtipoMantenimiento='" + tipoMantenimiento + "' and placaVehiculo='"
                + this.placa + "'and activo=1;";

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    mxPlaca.setPlacaVehiculo(rst.getString("placaVehiculo"));
                    mxPlaca.setIdMantenimientosPorPlaca(rst.getInt("idMantenimientosPorPlaca"));
                    mxPlaca.setIdtipoMantenimiento(rst.getInt("idtipoMantenimiento"));
                    mxPlaca.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                    mxPlaca.setConductorVehiculo(rst.getString("conductorVehiculo"));
                    mxPlaca.setNombreConductor(rst.getString("nombreConductor"));
                    mxPlaca.setApellidosConductor(rst.getString("apellidosConductor"));
                    mxPlaca.setIdNumeroDeorden(rst.getInt("idNumeroDeorden"));
                    mxPlaca.setKilometrajeMantenimiento(rst.getInt("kilometrajeMantenimiento"));
                    mxPlaca.setFechaMantenimiento(rst.getDate("fechaMantenimiento"));
                    mxPlaca.setZona(rst.getInt("zona"));
                    mxPlaca.setNombreZona(rst.getString("nombreZona"));
                    mxPlaca.setRegional(rst.getInt("regional"));
                    mxPlaca.setNombreRegional(rst.getString("nombreRegional"));
                    mxPlaca.setAgencia(rst.getInt("agencia"));
                    mxPlaca.setNombreAgencia(rst.getString("nombreAgencia"));
                    mxPlaca.setObservaciones(rst.getString("observaciones"));
                    mxPlaca.setActivo(rst.getInt("activo"));
                    mxPlaca.setFechaIng(rst.getDate("fechaIng"));
                    mxPlaca.setUsuario(rst.getString("usuario"));
                    mxPlaca.setFlag(rst.getInt("flag"));

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabar carros " + ex);
            }

        }
        return mxPlaca;
    }

    public void setListaDeDocumentosPorTipoDeVehiculo() {
        String sql;
        ResultSet rst;
        Connection con;
        Statement st;
        List<DocumentosPorTipoDeVehiculo> lista = null;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "SELECT d.idDocumentosPorTipoVehiculo, d.idTipoVehiculo,v.nombreTipoVehiculo, d.idTipoDocumento,t.nombreTipoDocumento,"
                + "d.isLogistica "
                + "FROM documentosportipodevehiculo d "
                + "join tiposdocumentos t on t.idtiposDocumentos=d.idTipoDocumento "
                + "join tiposdevehiculos v on v.idTipoVehiculo=d.idTipoVehiculo "
                + "and d.idTipoVehiculo='" + this.tipoVehiculo + "' and d.activo=1;";

        if (con != null) {

            try {

                listaDeDocumentosPorTipoDeVehiculo = new ArrayList<DocumentosPorTipoDeVehiculo>();

                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    DocumentosPorTipoDeVehiculo doc = new DocumentosPorTipoDeVehiculo(ini);

                    doc.setIdDocumentosPorTipoVehiculo(rst.getInt("IdDocumentosPorTipoVehiculo"));
                    doc.setIdTipoDocumento(rst.getInt("idTipoDocumento"));
                    doc.setIdTipoVehiculo(rst.getInt("idTipoVehiculo"));
                    doc.setIsLogistica(rst.getInt("isLogistica"));
                    doc.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
                    doc.setNombreTipoVehiculo(rst.getString("nombreTipoVehiculo"));

                    listaDeDocumentosPorTipoDeVehiculo.add(doc);

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabar carros " + ex);
            }

        }

    }

    public List<DocumentosPorTipoDeVehiculo> getlistaDeDocumentosPorTipoDeVehiculo() {
        return listaDeDocumentosPorTipoDeVehiculo;
    }

    public void setListaDeDocumentosPorPlaca() {
        String sql;
        ResultSet rst;
        Connection con;
        Statement st;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "select dpv.idTipoDocumento, dpv.numeroDocumento,td.nombreTipoDocumento,td.formato as prefijo, "
                + "dpv.entidadEmisora,IFNULL(dpv.fechaExpedicion,'PENDIENTE') as fechaExpedicion,"
                + "dpv.lugarExpedicion, IFNULL(dpv.fechaVencimiento,'PENDIENTE') as fechaVencimiento, dpv.activo,dpv.idConsecutivo,dpv.placa,dpv.usuario "
                + "from  "
                + "documentosportipodevehiculo dtv "
                + "join vehiculos v on v.tipoVehiculo=dtv.idTipoVehiculo and v.placa='" + this.placa + "' "
                + "left outer join documentosporvehiculo dpv on dpv.idTipoDocumento=dtv.idTipoDocumento and dpv.placa='" + this.placa + "' and dpv.activo=1 "
                + "join tiposdocumentos td on td.idtiposDocumentos=dtv.idTipoDocumento "
                + "where "
                + "dtv.idTipoVehiculo= v.tipoVehiculo and dtv.activo=1;";

        if (con != null) {

            try {

                listaDeDocumentosPorVehiculo = new ArrayList<DocumentosPorVehiculo>();

                st = con.createStatement();
                rst = st.executeQuery(sql);
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

                    listaDeDocumentosPorVehiculo.add(dxv);

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabar carros " + ex);
            }

        }

    }
}
