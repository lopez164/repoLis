/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos; 

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CDescuentosPorFactura;
import aplicacionlogistica.costumerService.objetos.CRecogidasPorFactura;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Esta clase define el Objeto Manifesto de Distribucion
 *
 * @author Luis Eduardo López Casanova
 *
 */
public class CManifiestosDeDistribucion {//extends Inicio {

//  Se definen los atributos de la clase
    String numeroManifiesto,
            vehiculo,
            tipoContrato,
            conductor,
            nombreConductor,
            apellidosConductor, telefonoConductor,
            despachador,
            nombreDespachador,
            apellidosDespachador,
            nombreCanal,
            nombreDeRuta,
            tipoRuta,
            usuarioManifiesto,
            tipoVehiculo,
            observaciones, horaDeDespacho, horaDeLiquidacion;

    int idCanal,
            idRuta,
            estadoManifiesto,
            kmSalida,
            kmEntrada,
            kmRecorrido,
            isFree,
            cantidadPedidos,
            zona,
            regional,
            agencia,
            activo,
            trazabilidad,
            impreso,
            cantDeSalidas,flag;

    Double valorTotalManifiesto,
            valorRecaudado,
            valorConsignado,
            pesoKgManifiesto;

    String fechaDistribucion, fechaReal;

    Inicio ini;

    List<CFacturasPorManifiesto> listaFacturasPorManifiesto;
    List<CFacturasPorManifiesto> listaFacturasDescargadas;
    List<CFacturasPorManifiesto> listaFacturasSinDescargar;
    List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar;

    List<CFacturasPorManifiesto> listaDeFacturasSinDespachar = null;

    //List<Vst_FacturasPorManifiesto> vistaFacturasPorManifiesto;
    //List<Vst_FacturasDescargadas> vistaFacturasDescargadas;
    List<CEmpleados> listaDeAuxiliares = null;
    List<String> listaFacturasSinManifestar;
    List<CSoportesConsignaciones> listaDeSoportesConsignaciones = null;

    List<CProductosPorFactura> listaCProductosPorFacturaDescargados;
    List<CRecogidasPorManifiesto> listaDeRecogidasPorManifiesto;

    List<CDescuentosPorFactura> listaDeDescuentos;
    List<CRecogidasPorFactura> listaDeRecogidas;

    List<CProductosPorMinuta> listaDeProductosMinuta;

    String rutaArchivoDescargueFacturas;//= "tmp/tmp_" + codificarManifiesto() + "_FacturasDescargados.txt";
    String rutaArchivoDescargueporductosPorFactura;//= "tmp/tmp_" + codificarManifiesto() + "_ProductosDescargados.txt";
    String rutArchivoRecogidasporManifiesto;//= "tmp/tmp_" + codificarManifiesto() + "_RecogidasDescargados.txt";
    String rutArchivofacturasporManifiesto;//= "tmp/tmp_" + codificarManifiesto() + ".txt";
    String rutArchivoFacturasSinManifestar;//= "tmp/tmp_" + codificarManifiesto() + "_SinManifestar.txt";
    String rutaArchivoSoportesDeConsignaciones;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    
    public String getTelefonoConductor() {
        return telefonoConductor;
    }

    public void setTelefonoConductor(String telefonoConductor) {
        this.telefonoConductor = telefonoConductor;
    }

    public int getCantDeSalidas() {
        return cantDeSalidas;
    }

    public void setCantDeSalidas(int cantDeSalidas) {
        this.cantDeSalidas = cantDeSalidas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;

    }

    public List<CFacturasPorManifiesto> getListaDeFacturasSinDespachar() {
        return listaDeFacturasSinDespachar;
    }

    public void setListaDeFacturasSinDespachar(List<CFacturasPorManifiesto> listaDeFacturasSinDespachar) {
        this.listaDeFacturasSinDespachar = listaDeFacturasSinDespachar;
    }

    public void setListaDeFacturasSinDespachar() {

        ResultSet rst = null;
        Statement st = null;
        Connection con;

        listaDeFacturasSinDespachar = new ArrayList<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaFacturasPorManifiesto");
        con = this.ini.getConnRemota();

        String sql = "select * "
                + " FROM vst_defintivofacturaspormanifiesto "
                + "where "
                + "numeroManifiesto='" + this.numeroManifiesto + "' "
                + "AND despachado='0';";

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
                    fxm.setTrasmitido(rst.getInt("trasmitido"));
                    fxm.setNumeroDescuento(rst.getString("numeroDescuento"));
                    fxm.setNumeroRecogida(rst.getString("numeroRecogida"));
                    fxm.setPesoFactura(rst.getDouble("pesoFactura"));
                    fxm.setAdherenciaDescargue(0);
                    fxm.setIdTipoDeMovimiento(1);
                    fxm.setNombreIdmovimiento("NINGUNO");
                    fxm.setCausalDeRechazo(1);
                    fxm.setNombreCausalDeDevolucion("NINGUNO");
                    fxm.setLatitud(rst.getString("latitud"));
                    fxm.setLongitud(rst.getString("longitud"));

                    fxm.setDespachado(rst.getInt("despachado"));
                    fxm.setUsuariodespachador(rst.getString("usuariodespachador"));

                    listaDeFacturasSinDespachar.add(fxm);
                    Thread.sleep(10);
                }

                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public List<CFacturasPorManifiesto> getListaFacturasPendientesPorDescargar() {
        return listaFacturasPendientesPorDescargar;
    }

    public List<CFacturasPorManifiesto> setListaFacturasPendientesPorDescargar() {
        listaFacturasPendientesPorDescargar = new ArrayList<>();
        CFacturasPorManifiesto factura;

        for (CFacturasPorManifiesto fac : listaFacturasPorManifiesto) {
            boolean existe = false;
            if (listaFacturasDescargadas != null) {
                for (CFacturasPorManifiesto facDes : listaFacturasDescargadas) {
                    if (facDes.getNumeroFactura().equals(fac.getNumeroFactura())) {
                        existe = true;
                        break;
                    }
                }

            }
            if (!existe) {
                listaFacturasPendientesPorDescargar.add(fac);

            }
        }
        return listaFacturasPendientesPorDescargar;
    }

    public void setListaFacturasPendientesPorDescargar(List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar) {
        this.listaFacturasPendientesPorDescargar = listaFacturasPendientesPorDescargar;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
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

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    public String getNombreDespachador() {
        return nombreDespachador;
    }

    public void setNombreDespachador(String nombreDespachador) {
        this.nombreDespachador = nombreDespachador;
    }

    public String getApellidosDespachador() {
        return apellidosDespachador;
    }

    public void setApellidosDespachador(String apellidosDespachador) {
        this.apellidosDespachador = apellidosDespachador;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public String getNombreDeRuta() {
        return nombreDeRuta;
    }

    public void setNombreDeRuta(String nombreDeRuta) {
        this.nombreDeRuta = nombreDeRuta;
    }

    public String getTipoRuta() {
        return tipoRuta;
    }

    public void setTipoRuta(String tipoRuta) {
        this.tipoRuta = tipoRuta;
    }

    public String getUsuarioManifiesto() {
        return usuarioManifiesto;
    }

    public void setUsuarioManifiesto(String usuarioManifiesto) {
        this.usuarioManifiesto = usuarioManifiesto;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public int getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getEstadoManifiesto() {
        return estadoManifiesto;
    }

    public void setEstadoManifiesto(int estadoManifiesto) {
        this.estadoManifiesto = estadoManifiesto;
    }

    public int getKmSalida() {
        return kmSalida;
    }

    public void setKmSalida(int kmSalida) {
        this.kmSalida = kmSalida;
    }

    public int getKmEntrada() {
        return kmEntrada;
    }

    public void setKmEntrada(int kmEntrada) {
        this.kmEntrada = kmEntrada;
    }

    public int getKmRecorrido() {
        return kmRecorrido;
    }

    public void setKmRecorrido(int kmRecorrido) {
        this.kmRecorrido = kmRecorrido;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
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

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getTrazabilidad() {
        return trazabilidad;
    }

    public void setTrazabilidad(int trazabilidad) {
        this.trazabilidad = trazabilidad;
    }

    public Double getValorTotalManifiesto(Boolean sinLista) {
        valorTotalManifiesto = 0.0;
        // setListaFacturasPorManifiesto();
        for (CFacturasPorManifiesto fxm : this.listaFacturasPorManifiesto) {
            valorTotalManifiesto += fxm.getValorTotalFactura();
        }
        return valorTotalManifiesto;
    }

    public Double getValorTotalManifiesto() {

        return valorTotalManifiesto;
    }

    public void setValorTotalManifiesto(Double valorTotalManifiesto) {
        this.valorTotalManifiesto = valorTotalManifiesto;
    }

    public Double getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(Double valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public Double getValorConsignado() {
        return valorConsignado;
    }

    public void setValorConsignado(Double valorConsignado) {
        this.valorConsignado = valorConsignado;
    }

    public String getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(String fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public String getHoraDeDespacho() {
        return horaDeDespacho;
    }

    public void setHoraDeDespacho(String horaDeDespacho) {
        this.horaDeDespacho = horaDeDespacho;
    }

    public String getHoraDeLiquidacion() {
        return horaDeLiquidacion;
    }

    public void setHoraDeLiquidacion(String horaDeLiquidacion) {
        this.horaDeLiquidacion = horaDeLiquidacion;
    }

    public String getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(String fechaReal) {
        this.fechaReal = fechaReal;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public int getImpreso() {
        return impreso;
    }

    public void setImpreso(int impreso) {
        this.impreso = impreso;
    }

    public String getRutaArchivoSoportesDeConsignaciones() {
        return rutaArchivoSoportesDeConsignaciones;
    }

    public List<CSoportesConsignaciones> getListaDeSoportesConsignaciones() {
        return listaDeSoportesConsignaciones;
    }

    public void setListaDeSoportesConsignaciones(List<CSoportesConsignaciones> listaDeSoportesConsignaciones) {
        this.listaDeSoportesConsignaciones = listaDeSoportesConsignaciones;
    }

    public void setListaDeRecogidasPorManifiesto(List<CRecogidasPorManifiesto> listaDeRecogidasPorManifiesto) {
        this.listaDeRecogidasPorManifiesto = listaDeRecogidasPorManifiesto;
    }

    public void setListaDeSoportesConsignaciones() {
        Connection con = null;
        Statement st = null;

        ResultSet rst = null;
        String sql = "select * "
                + "from vst_soportesconsignaciones "
                + "where "
                + "numeroManifiesto='" + this.numeroManifiesto + "' "
                + "ORDER BY numeroManifiesto asc;";

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeSoportesConsignaciones");
        con = this.ini.getConnRemota();
        listaDeSoportesConsignaciones = new ArrayList<>();
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CSoportesConsignaciones soporte = new CSoportesConsignaciones(ini);

                    soporte.setConseutivo(rst.getInt("consecutivo"));
                    soporte.setIdBanco(rst.getInt("idBanco"));
                    soporte.setMedioDePago(rst.getString("medioDePago"));
                    soporte.setNombreDelBanco(rst.getString("nombreBanco"));
                    soporte.setNumeroDeCuenta(rst.getString("numerodeCuenta"));
                    soporte.setNumeroManifiesto(rst.getInt("numeroManifiesto"));
                    soporte.setNumeroSoporte(rst.getString("numeroSoporte"));
                    soporte.setValor(rst.getDouble("valor"));
                    soporte.setFecha(rst.getDate("fecha"));
                    soporte.setFechaOperacion(rst.getDate("fechaOperacion"));

                    listaDeSoportesConsignaciones.add(soporte);

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }

    }

    public void setListaDeSoportesConsignaciones(File fichero) {

        try {

            listaDeSoportesConsignaciones = new ArrayList<>();

            if (fichero.exists()) {
                // Abrimos el archivo
                FileInputStream fstream = new FileInputStream(fichero);

                // Creamos el objeto de entrada
                DataInputStream entrada = new DataInputStream(fstream);

                // Creamos el Buffer de Lectura
                BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

                String strLinea;
                // Leer el archivo linea por linea

                /*Se recorre el archivo y nos traemos los números de las facturas guardadas
            en el archivo temporal */
                while ((strLinea = buffer.readLine()) != null) {
                    CSoportesConsignaciones soporte = new CSoportesConsignaciones(ini);
                    String[] cadena = strLinea.split(",");

                    soporte.setNumeroManifiesto(Integer.parseInt(cadena[0]));
                    soporte.setNumeroSoporte(cadena[1]);
                    // soporte.setIdBanco(Integer.parseInt(cadena[2]));
                    soporte.setIdCuentaBancaria(Integer.parseInt(cadena[2]));
                    for (CCuentasBancarias obj : ini.getListaDeCuentasBancarias()) {
                        if (obj.getIdcuentasBancarias() == soporte.getIdCuentaBancaria()) {
                            soporte.setNombreDelBanco(obj.getNombreDeBanco());
                            soporte.setIdBanco(obj.getIdBanco());
                            break;
                        }
                    }
                    //soporte.setNumeroDeCuenta(cadena[3]);

                    SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd");
                    Date fechaOrigen = ini.deStringToDate(cadena[3]);

                    long d = fechaOrigen.getTime();
                    java.sql.Date fech = new java.sql.Date(d);
                    //fechaOrigen=Inicio.getFechaSql(fechaOrigen);
                    soporte.setFechaOperacion(fech);
                    soporte.setMedioDePago(cadena[4]);
                    soporte.setValor(Double.parseDouble(cadena[5]));

                    listaDeSoportesConsignaciones.add(soporte);

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setListaFacturasPorManifiesto(List<CFacturasPorManifiesto> listaFacturasPorManifiesto) {
        this.listaFacturasPorManifiesto = listaFacturasPorManifiesto;
    }

    public void setListaFacturasPorManifiesto() {

        ResultSet rst = null;
        Statement st = null;
        Connection con;

        listaFacturasPorManifiesto = new ArrayList<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaFacturasPorManifiesto");
        con = this.ini.getConnRemota();

        String sql = "select * "
                + " FROM vst_defintivofacturaspormanifiesto "
                + "WHERE "
                + "numeroManifiesto='" + numeroManifiesto + "' and activo=1 ORDER BY adherencia ASC ";

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
                    fxm.setTrasmitido(rst.getInt("trasmitido"));
                    fxm.setNumeroDescuento(rst.getString("numeroDescuento"));
                    fxm.setNumeroRecogida(rst.getString("numeroRecogida"));
                    fxm.setPesoFactura(rst.getDouble("pesoFactura"));
                    fxm.setAdherenciaDescargue(0);
                    fxm.setIdTipoDeMovimiento(1);
                    fxm.setNombreIdmovimiento("NINGUNO");
                    fxm.setCausalDeRechazo(1);
                    fxm.setNombreCausalDeDevolucion("NINGUNO");
                    fxm.setLatitud(rst.getString("latitud"));
                    fxm.setLongitud(rst.getString("longitud"));
                    fxm.setDespachado(rst.getInt("despachado"));
                    fxm.setUsuariodespachador(rst.getString("usuariodespachador"));
                    fxm.setPlazoDias(rst.getInt("plazoDias"));
                    fxm.setFpContado(rst.getDouble("fpContado"));

                    listaFacturasPorManifiesto.add(fxm);
                    Thread.sleep(10);
                }

                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public List<CFacturasPorManifiesto> getListaFacturasPorManifiesto() {
        return listaFacturasPorManifiesto;
    }

    /**
     * Método que devuelve el listado de auxiliares de una manifiesto
     *
     * @return un array con la lista de auxiliares asignados al manifiesto.
     */
    public List<CEmpleados> getListaDeAuxiliares() {

        return listaDeAuxiliares;
    }

    /**
     * Método que devuelve el listado de auxiliares de una manifiesto
     *
     * @param numeroManifiesto es el numero de documento que identifica los
     * auxiliarees asignado
     * @return un array con la lista de auxiliares asignados al manifiesto.
     */
    public List<CEmpleados> getListaDeAuxiliares(String numeroManifiesto) {
        Connection con = null;
        Statement st = null;

        ResultSet rst = null;
        String sql = "select * "
                + "from auxiliarespormanifiesto "
                + "where "
                + "numeroManifiesto='" + this.numeroManifiesto + "' "
                + "ORDER BY numeroManifiesto asc;";

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.getListaDeAuxiliares");
        con = this.ini.getConnRemota();
        listaDeAuxiliares = new ArrayList<>();
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                int x = st.getFetchSize();

                while (rst.next()) {
                    for (CEmpleados aux : ini.getListaDeEmpleados()) {
                        if (rst.getString("cedulaAuxiliar").equals(aux.getCedula())) {
                            listaDeAuxiliares.add(aux);
                        }
                    }

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
        return listaDeAuxiliares;
    }

    public void setListaDeAuxiliares(List<CEmpleados> listaDeAuxiliares) {
        this.listaDeAuxiliares = listaDeAuxiliares;
    }

    /**
     * Método que asigna al manifiesto los auxiliares del manifiesto cundo ya
     * fueron grabados en el sistema, y teniendo el manifiesto actual
     *
     *
     * @param numeroManifiesto
     */
    public void setListaDeAuxiliares(String numeroManifiesto) {
        listaDeAuxiliares = new ArrayList();
        Connection con = null;
        Statement st = null;

        ResultSet rst = null;
        String sql = "select * "
                + "from auxiliarespormanifiesto "
                + "where "
                + "numeroManifiesto='" + Integer.parseInt(numeroManifiesto) + "' ;";

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeAuxiliares");
        con = this.ini.getConnRemota();
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    for (CEmpleados aux : ini.getListaDeEmpleados()) {
                        if (rst.getString("cedulaAuxiliar").equals(aux.getCedula())) {
                            listaDeAuxiliares.add(aux);
                            break;
                        }
                    }

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
    }

    public void setListaDeAuxiliares() {
        listaDeAuxiliares = new ArrayList();
        Connection con = null;
        Statement st = null;

        ResultSet rst = null;
        String sql = "select * "
                + "from auxiliarespormanifiesto "
                + "where "
                + "numeroManifiesto='" + this.numeroManifiesto + "' ;";

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeAuxiliares");
        con = this.ini.getConnRemota();
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    for (CEmpleados aux : ini.getListaDeEmpleados()) {
                        if (rst.getString("cedulaAuxiliar").equals(aux.getCedula())) {
                            listaDeAuxiliares.add(aux);
                            break;
                        }
                    }

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
    }

    /**
     * Método que devuelve el listado de RECOGIDAS del manifiesto
     *
     * @return un array con la lista de objetos CRecogidasporManifiesto
     */
    public List<CRecogidasPorManifiesto> getListaDeRecogidasPorManifiesto() {
        return listaDeRecogidasPorManifiesto;
    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * recogidas que se hayan realizado
     *
     * @param listaDeRecogidasPorManifiesto lista de las recogidas
     *
     */
    public void setListaDeRecogidasPorManifiestoDescargadas(List<CRecogidasPorManifiesto> listaDeRecogidasPorManifiesto) {
        this.listaDeRecogidasPorManifiesto = listaDeRecogidasPorManifiesto;
    }

    /**
     * Método que devuelve al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto
     *
     *
     * @return la lista de las facturas que se encuentran sin manifestar
     */
    public List<String> getListaDefacturasSinManifestar() {
        return listaFacturasSinManifestar;
    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto
     *
     *
     * @param listaDefacturasSinManifestar lista de las facturas sin manifestar
     */
    public void setListaDefacturasSinManifestar(List<String> listaDefacturasSinManifestar) {
        this.listaFacturasSinManifestar = listaDefacturasSinManifestar;
    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto
     *
     *
     * @param file archivo temporal donde se encuentrab grabados los datos
     */
    public void setListaDefacturasSinManifestar(File file) {

        try {

            listaFacturasSinManifestar = new ArrayList<>();

            if (file.exists()) {
                // Abrimos el archivo
                FileInputStream fstream = new FileInputStream(file);

                // Creamos el objeto de entrada
                DataInputStream entrada = new DataInputStream(fstream);

                // Creamos el Buffer de Lectura
                BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

                String strLinea;
                // Leer el archivo linea por linea

                /*Se recorre el archivo y nos traemos los números de las facturas guardadas
            en el archivo temporal */
                while ((strLinea = buffer.readLine()) != null) {

                    listaFacturasSinManifestar.add(strLinea);

                    // Imprimimos la línea por pantalla
                    System.out.println(strLinea);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto a partir de
     * un archivo temporal
     *
     *
     */
    public void setListaDefacturasSinManifestar() {
        try {

            listaFacturasSinManifestar = new ArrayList<>();
            File file = new File(rutArchivoFacturasSinManifestar);

            if (file.exists()) {
                // Abrimos el archivo
                FileInputStream fstream = new FileInputStream(file);

                // Creamos el objeto de entrada
                DataInputStream entrada = new DataInputStream(fstream);

                // Creamos el Buffer de Lectura
                BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

                String strLinea;
                // Leer el archivo linea por linea

                /*Se recorre el archivo y nos traemos los números de las facturas guardadas
            en el archivo temporal */
                while ((strLinea = buffer.readLine()) != null) {

                    listaFacturasSinManifestar.add(strLinea);

                    // Imprimimos la línea por pantalla
                    System.out.println(strLinea);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que devuelve una cadena con la idRuta del archivo donde se guardan
     * temporamente las facturasque no se encuenetran en el manifiesto
     *
     *
     * @return la cadena con l idRuta del archivo
     */
    public String getRutArchivoFacturasSinManifestar() {
        return rutArchivoFacturasSinManifestar;
    }

    /**
     * Método que asigna al manifiesto actual, los registros de RECOGIDAS del
     * manifiesto cuando se encuentran grabadas en la BBDD porque el manifiesto
     * está cerrado ó descargado"
     *
     * @see
     */
    public void setListaDeRecogidasPorManifiesto() {
        Connection con;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.getRutArchivoFacturasSinManifestar");

        con = this.ini.getConnRemota();
        sql = "SELECT idRecogidasPorManifiesto, idNumeroManifiesto, numeroFactura,"
                + "facturaAfectada, numeroDeSoporte, valorRecogida,valorRecaudadoRecogida, activo "
                + "FROM recogidaspormanifiesto "
                + " WHERE "
                + " idNumeroManifiesto='" + this.numeroManifiesto + "' ;";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                listaDeRecogidasPorManifiesto = new ArrayList<>();
                while (rst.next()) {
                    CRecogidasPorManifiesto recxman = new CRecogidasPorManifiesto(ini);
                    recxman.setIdRecogidasPorManifiesto(rst.getInt("idRecogidasPorManifiesto"));
                    recxman.setIdNumeroManifiesto(rst.getString("idNumeroManifiesto"));
                    recxman.setNumeroFactura(rst.getString("numeroFactura"));
                    recxman.setFacturaAfectada(rst.getString("facturaAfectada"));
                    recxman.setNumeroDeSoporte(rst.getString("numeroDeSoporte"));
                    recxman.setValorRecogida(rst.getDouble("valorRecogida"));
                    recxman.setValorRecaudadoRecogida(rst.getDouble("valorRecaudadoRecogida"));
                    listaDeRecogidasPorManifiesto.add(recxman);
                }
                rst.close();
                st.close();
                //con.close();

            } catch (SQLException ex) { //Catch de excepciones
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    /**
     * Método que asigna un array list de objetos CFacturasPorManifiesto los
     * cuales corrsponden a las facturas asignadas al manifiesto actual.
     *
     * @return un array list de objetos CFacturasPorManifiesto que son las
     * factuas asignadas al manifiesto
     */
//    public List<CFacturasPorManifiesto> getListaFacturasPorManifiesto() {
//        return listaFacturasPorManifiesto;
//    }
//
//    public void setListaFacturasPorManifiesto(List<CFacturasPorManifiesto> arrFacturasEnManifiesto) {
//        this.listaFacturasPorManifiesto = arrFacturasEnManifiesto;
//    }
    /**
     * Método que asigan una array list de objetos CfacturasPorManifiesto las
     * cuales se encuentran asignadas al manifiesto actual
     *
     * /**
     * Método que asigan una array list de objetos CfacturasPorManifiesto las
     * cuales se encuentran asignadas al manifiesto actual
     *
     */
    @SuppressWarnings("empty-statement")
//
//    public void setListaFacturasPorManifiesto() {
//        List<CFacturasPorManifiesto> arrFM = new ArrayList<>();
//        ResultSet rst = null;
//        Statement st = null;
//        Connection con = null;
//        String sql = null;
//
//        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
//        sql = " SELECT consecutivo, numeroManifiesto, numeroFactura, "
//                + "valorARecaudarFactura, adherencia, zona, regional, agencia "
//                + " FROM facturaspormanifiesto "
//                + "WHERE "
//                + "numeroManifiesto='" + numeroManifiesto + "' and activo=1; ";
//        if (con != null) {
//            try {
//                st = con.createStatement();
//                rst = st.executeQuery(sql);
//                while (rst.next()) {
//                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
//
//                    fxm.setConsecutivo(rst.getInt("consecutivo"));
//                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
//                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
//                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
//                    fxm.setAdherencia(rst.getInt("adherencia"));
//                    fxm.setZona(rst.getInt("zona"));
//                    fxm.setRegional(rst.getInt("regional"));
//                    fxm.setAgencia(rst.getInt("agencia"));
//
//                    System.out.println("Cargando facturaPorManifiesto número -> " + fxm.getNumeroFactura());
//
//                    arrFM.add(fxm);
//
//                }
//                rst.close();
//                st.close();
//                con.close();
//
//            } catch (SQLException ex) { //Catch de excepciones
//                try {
//                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//                    rst.close();
//                    st.close();
//                    con.close();
//                } catch (SQLException ex1) {
//                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        this.listaFacturasPorManifiesto = arrFM;;
//    }

    public List<CFacturasPorManifiesto> getListaFacturasDescargadas() {
        return listaFacturasDescargadas;
    }

    public List<CFacturasPorManifiesto> setListaFacturasDescargadas() {
        listaFacturasDescargadas = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        String sql = "";
        ResultSet rst = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.getListaFacturasDescargadas");
            con = this.ini.getConnRemota();
            sql = "select * from vst_movilizacionfacturasdescargadas "
                    + "WHERE "
                    + "numeroManifiesto='" + numeroManifiesto + "' "
                    + "ORDER BY  numeroFactura ASC ";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto facdes = new CFacturasPorManifiesto(ini);

                    facdes.setConsecutivo(rst.getInt("consecutivo"));
                    facdes.setIdCanal(rst.getInt("idCanalDistribucion"));//
                    facdes.setNombreCanal(rst.getString("nombreCanalDistribucion"));
                    facdes.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    facdes.setAdherencia(rst.getInt("adherencia"));
                    facdes.setNumeroFactura(rst.getString("numeroFactura"));
                    facdes.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    facdes.setVehiculo(rst.getString("vehiculo"));
                    facdes.setConductor(rst.getString("conductor"));
                    facdes.setNombreConductor(rst.getString("nombreConductor"));
                    facdes.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    facdes.setDireccionDeCliente(rst.getString("direccion"));
                    facdes.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    facdes.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    facdes.setValorRechazo(rst.getDouble("valorRechazo"));
                    facdes.setValorDescuento(rst.getDouble("valorDescuento"));
                    facdes.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    facdes.setIdTipoDeMovimiento(rst.getInt("idTipoMovimiento"));
                    facdes.setNombreIdmovimiento(rst.getString("nombreTipoDeMovimiento"));
                    facdes.setCausalDeRechazo(rst.getInt("idMotivoRechazo"));
                    facdes.setNombreCausalDeDevolucion(rst.getString("nombreCausalDeRechazo"));
                    facdes.setVendedor(rst.getString("vendedor"));
                    facdes.setUsuario(rst.getString("usuario"));
                    facdes.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    facdes.setNombreCanal(rst.getString("nombreCanalDeVenta"));
                    facdes.setFechaIng(rst.getString("fechaIng"));

                    this.listaFacturasDescargadas.add(facdes);

                }
                rst.close();
                st.close();
                //con.close();

//                if (listaFacturasDescargadas.isEmpty()) {
//                    listaFacturasDescargadas = null;
//                }
            }

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return listaFacturasDescargadas;
    }

    /**
     * Método que asigna al manifiesto actual, los registros de RECOGIDAS del
     * manifiesto cuando "NO" se encuentran grabadas en la BBDD, ya que el
     * manifiesto se está descargando y los registros se encuentran guardados
     * temporalmente en un archivo de texto"
     *
     * @param archivo, corresponde al archivo de texto
     */
    public void setListaDeRecogidasPorManifiesto(File archivo) {
        this.listaDeRecogidasPorManifiesto = new ArrayList<>();
        try {
            if (archivo.exists()) {
                // Abrimos el archivo
                FileInputStream fstream = new FileInputStream(archivo);

                // Creamos el objeto de entrada
                DataInputStream entrada = new DataInputStream(fstream);

                // Creamos el Buffer de Lectura
                BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

                String strLinea;
                // Leer el archivo linea por linea
                while ((strLinea = buffer.readLine()) != null) {
                    String[] cadena = strLinea.split(",");

                    CRecogidasPorManifiesto recogidaDescargada = new CRecogidasPorManifiesto(ini);

                    recogidaDescargada.setIdRecogidasPorManifiesto(Integer.parseInt(cadena[0]));
                    recogidaDescargada.setIdNumeroManifiesto(cadena[1]);
                    recogidaDescargada.setNumeroFactura(cadena[2]);
                    recogidaDescargada.setFacturaAfectada(cadena[3]);
                    recogidaDescargada.setNumeroDeSoporte(cadena[4]);
                    recogidaDescargada.setValorRecogida(Double.parseDouble(cadena[5]));
                    recogidaDescargada.setValorRecaudadoRecogida(Double.parseDouble(cadena[6]));

                    this.listaDeRecogidasPorManifiesto.add(recogidaDescargada);

                    // Imprimimos la línea por pantalla
                    System.out.println(strLinea);
                }
                // Cerramos el archivo
                entrada.close();
            } else {
                System.out.println("Nombre de archivo no existe : " + archivo);
            }
        } catch (IOException | NumberFormatException e) { //Catch de excepciones
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

    /**
     * Método que asigna al manifiesto actual, las facturas descargadas del
     * manifiesto cuando "NO" se encuentran grabadas en la BBDD, ya que el
     * manifiesto se está descargando y los registros se encuentran guardados
     * temporalmente en un archivo de texto"
     *
     * @param archivo, corresponde al archivo de texto
     */
    public void setListaFacturasDescargadas(File archivo) {

        listaFacturasDescargadas = new ArrayList<>();
        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(archivo);

            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);

            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

            String strLinea;
            // Leer el archivo linea por linea
            int i = 0;
            while ((strLinea = buffer.readLine()) != null) {
                String[] cadena = strLinea.split(",");

                //CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);
                for (CFacturasPorManifiesto fac : listaFacturasPorManifiesto) {
                    if (fac.getConsecutivo() == Integer.parseInt(cadena[0])) {
                        //fac.setConsecutivo(Integer.parseInt(cadena[0]));
                        //fac.setNumeroManifiesto(cadena[1]);
                        fac.setAdherenciaDescargue(i++);
                        //fac.setNumeroFactura(cadena[3]);
                        fac.setValorRechazo(Double.parseDouble(cadena[4]));
                        fac.setValorDescuento(Double.parseDouble(cadena[5]));
                        fac.setValorRecaudado(Double.parseDouble(cadena[6]));
                        fac.setIdTipoDeMovimiento(Integer.parseInt(cadena[7]));
                        fac.setCausalDeRechazo(Integer.parseInt(cadena[8]));
                        fac.setActivo(Integer.parseInt(cadena[9]));
                        this.listaFacturasDescargadas.add(fac);
                    }
                }

                System.out.println(strLinea);
            }
            // Cerramos el archivo
            entrada.close();

        } catch (Exception e) { //Catch de excepciones
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Ocurrio un error: " + e.getMessage());
        }

    }

    /**
     * Método que asigna al manifiesto actual, el lstado de la facturas
     * descargadas del manifiesto cuando se encuentran grabadas en la BBDD, ya
     * que el manifiesto se está descargado
     *
     * @param numeroDeManifiesto corresponde al manifiesto que se solicita
     */
    /**
     * Método que asigna al manifiesto actual, el lstado de la facturas
     * descargadas del manifiesto cuando se encuentran grabadas en la BBDD, ya
     * que el manifiesto se está descargado
     *
     * @param numeroDeManifiesto corresponde al manifiesto que se solicita
     */
    /**
     * Método que asigna al manifiesto actual, el lstado de la facturas
     * descargadas del manifiesto cuando se encuentran grabadas en la BBDD, ya
     * que el manifiesto se está descargado
     *
     * @param numeroDeManifiesto corresponde al manifiesto que se solicita
     */
    public void setListaFacturasDescargadas(int numeroDeManifiesto) {

        listaFacturasDescargadas = new ArrayList<>();
        ResultSet rst = null;
        Statement st = null;
        Connection con;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaFacturasDescargadas");
        con = this.ini.getConnRemota();

        // numeroManifiesto, numeroManifiesto, numeroDeFactura, valorRechazo, valorDescuento, valorRecaudado, movimientoFactura, motivoRechazo, activo, fechaIng, usuarioManifiesto, flag
        String sql = " SELECT facturasdescargadas.consecutivo, facturasdescargadas.numeroManifiesto, facturasdescargadas.numeroFactura,"
                + "facturasdescargadas. valorRechazo,facturasdescargadas. valorDescuento, facturasdescargadas.valorRecaudado, "
                + "facturasdescargadas.movimientoFactura,facturasdescargadas. motivoRechazo,facturasdescargadas. activo, "
                + "facturasdescargadas.fechaIng, facturasdescargadas.usuario, facturasdescargadas.flag "
                + " FROM facturasdescargadas "
                + "WHERE "
                + "facturasdescargadas.numeroManifiesto='" + numeroDeManifiesto + "'  order by fechaIng ASC;";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {

                    CFacturasPorManifiesto fd = new CFacturasPorManifiesto(ini);

                    fd.setConsecutivo(rst.getInt("consecutivo"));
                    fd.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fd.setNumeroFactura(rst.getString("numeroFactura"));
                    fd.setValorRechazo(rst.getDouble("valorRechazo"));
                    fd.setValorDescuento(rst.getDouble("valorDescuento"));
                    fd.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    fd.setIdTipoDeMovimiento(rst.getInt("movimientoFactura"));
                    fd.setCausalDeRechazo(rst.getInt("motivoRechazo"));
                    fd.setActivo(rst.getInt("activo"));

                    System.out.println("Cargando factura descargada número -> " + fd.getNumeroFactura());

                    listaFacturasDescargadas.add(fd);

                    System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                //con.close();

                if (listaFacturasDescargadas.isEmpty()) {
                    listaFacturasDescargadas = null;
                }

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (Exception ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void setListaFacturasDescargadas(List<CFacturasPorManifiesto> arrFacturasDescargadas) {
        this.listaFacturasDescargadas = arrFacturasDescargadas;
    }

    public List<CProductosPorFactura> getListaCProductosPorFacturaDescargados() {
        return listaCProductosPorFacturaDescargados;
    }

    public void setListaCProductosPorFacturaDescargados(List<CProductosPorFactura> listaCProductosPorFacturaDescargados) {
        this.listaCProductosPorFacturaDescargados = listaCProductosPorFacturaDescargados;
    }

    public void setListaCProductosPorFacturaDescargados() {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        listaCProductosPorFacturaDescargados = new ArrayList<>();
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaCProductosPorFacturaDescargados");
            con = this.ini.getConnRemota();

            String sql = "Select productosporfacturadescargados.consecutivoFacturasDescargadas, productosporfacturadescargados.consecutivoProductosPorFactura, "
                    + "productosporfacturadescargados.descuento, "
                    + "productosporfacturadescargados.cantidadRechazada,productosporfacturadescargados.valorRechazo, "
                    + "productosporfacturadescargados.cantidadEntregada, productosporfacturadescargados.totalRecaudado, productosporfacturadescargados.entregado, "
                    + "productosporfacturadescargados.causalDeRechazo "
                    + "From productosporfacturadescargados, facturaspormanifiesto "
                    + "WHERE "
                    + "productosporfacturadescargados.consecutivo = facturaspormanifiesto.consecutivo AND "
                    + "facturaspormanifiesto.numeroManifiesto='" + numeroManifiesto + "' and facturaspormanifiesto.activo=1 ;";
            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CProductosPorFacturaDescargados pxfd = new CProductosPorFacturaDescargados(ini);

                    pxfd.setConsecutivoFacturasDescargadas(rst.getInt("consecutivoFacturasDescargadas"));
                    pxfd.setConsecutivoProductosPorFactura(rst.getInt("consecutivoProductosPorFactura"));
                    pxfd.setValorDescuentoFactura(rst.getDouble("descuento"));
                    pxfd.setCantidadRechazadaItem(rst.getInt("cantidadRechazada"));
                    pxfd.setValorRechazoFactura(rst.getDouble("valorRechazo"));
                    pxfd.setCantidadEntregadaItem(rst.getInt("cantidadEntregada"));
                    pxfd.setValorTotalRecaudadoProducto(rst.getDouble("totalRecaudado"));
                    pxfd.setEntregado(rst.getInt("entregado"));
                    pxfd.setCausalDeRechazo(rst.getInt("causalDeRechazo"));
                    pxfd.setActivo(1);

                    listaCProductosPorFacturaDescargados.add(pxfd);
                }
                rst.close();
                st.close();
                // con.close();

            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void setListaCProductosPorFacturaDescargados(File archivo) {

        FileWriter fichero = null;
        FileReader fr = null;
        BufferedReader br = null;
        PrintWriter escrive = null;
        try {
            System.out.println(idRuta);

            listaCProductosPorFacturaDescargados = new ArrayList<>();
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {

                try {
                    String[] cadena = linea.split(",");

                    for (CFacturasPorManifiesto fac : listaFacturasPorManifiesto) {

                        if (Integer.parseInt(cadena[0]) == fac.getConsecutivo()) {

                            for (CProductosPorFactura pxf : fac.getListaProductosPorFactura()) {

                                if (pxf.getConsecutivoProductoPorFactura() == Integer.parseInt(cadena[1])) {
                                    pxf.setValorDescuentoItem(Double.parseDouble(cadena[2]));
                                    pxf.setCantidadRechazadaItem(Double.parseDouble(cadena[3]));
                                    pxf.setValorRechazoItem(Double.parseDouble(cadena[4]));
                                    pxf.setCantidadEntregadaItem(Double.parseDouble(cadena[5]));
                                    pxf.setValorTotalLiquidacionItem(Double.parseDouble(cadena[6]));
                                    pxf.setEntregado(Integer.parseInt(cadena[7]));
                                    pxf.setCausalDeRechazo(Integer.parseInt(cadena[8]));
                                    pxf.setActivo(Integer.parseInt(cadena[9]));
                                    listaCProductosPorFacturaDescargados.add(pxf);

                                }
                            }

                        }

                    }

                    System.out.println("llama a la lista de productos por factura" + linea);

                } catch (Exception e) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, e);
                    System.out.println(e);
                }

            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setListaCProductosPorFacturaDescargados(String rutaDelAarchivo) {
        File archivo = null;
        FileWriter fichero = null;
        FileReader fr = null;
        BufferedReader br = null;
        PrintWriter escrive = null;
        listaCProductosPorFacturaDescargados = new ArrayList();
        try {
            System.out.println(rutaDelAarchivo);
            archivo = new File(rutaDelAarchivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {

                String[] cadena = linea.split(",");
                CProductosPorFacturaDescargados pxfd = new CProductosPorFacturaDescargados(ini);

                pxfd.setConsecutivoFacturasDescargadas(Integer.parseInt(cadena[0]));
                pxfd.setConsecutivoProductosPorFactura(Integer.parseInt(cadena[1]));
                pxfd.setValorDescuentoFactura(Double.parseDouble(cadena[2]));
                pxfd.setCantidadRechazadaItem(Double.parseDouble(cadena[3]));
                pxfd.setValorRechazoFactura(Double.parseDouble(cadena[4]));
                pxfd.setCantidadEntregadaItem(Double.parseDouble(cadena[5]));
                pxfd.setValorTotalRecaudadoProducto(Double.parseDouble(cadena[6]));
                pxfd.setEntregado(Integer.parseInt(cadena[7]));
                pxfd.setCausalDeRechazo(Integer.parseInt(cadena[8]));
                pxfd.setActivo(Integer.parseInt(cadena[9]));

                listaCProductosPorFacturaDescargados.add(pxfd);

                System.out.println(linea);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getIdManifiestoDeDistribucion(String placa, Date fecha) {
        int id = 0;
        Connection con;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.getIdManifiestoDeDistribucion");

        con = this.ini.getConnRemota();
        sql = "SELECT consecutivo "
                + "FROM manifiestosdedistribucion "
                + "WHERE "
                + "vehiculo='" + placa + "' AND fechaDistribucion='" + fecha + "' and "
                + "activo=1;";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    id = rst.getInt(1);
                } else {
                    id = 0;
                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
        return id;
    }

    /**
     * Método que devuelve una cadena con la idRuta donde se guardará
     * temporalmente los datos de las facturas descargadas de un manifiesto,
     * para luego ser guradadas en la BBDD
     *
     * @return una cadena con la idRuta del archivo
     */
    public String getRutaArchivoDescargueFacturas() {

        return rutaArchivoDescargueFacturas;
    }

    /**
     * Método que devuelve una cadena con la idRuta donde se guardará
     * temporalmente los datos de los productos de facturas descargadas de un
     * manifiesto, para luego ser guradadas en la BBDD
     *
     * @return una cadena con la idRuta del archivo
     */
    public String getRutaArchivoDescargueporductosPorFactura() {
        return rutaArchivoDescargueporductosPorFactura;
    }

    public void setRutaArchivoDescargueFacturas(String rutaArchivoDescargueFacturas) {
        this.rutaArchivoDescargueFacturas = rutaArchivoDescargueFacturas;
    }

    public void setRutaArchivoDescargueporductosPorFactura(String rutaArchivoDescargueporductosPorFactura) {
        this.rutaArchivoDescargueporductosPorFactura = rutaArchivoDescargueporductosPorFactura;
    }

    public void setRutArchivoRecogidasporManifiesto(String rutArchivoRecogidasporManifiesto) {
        this.rutArchivoRecogidasporManifiesto = rutArchivoRecogidasporManifiesto;
    }

    public void setRutArchivoFacturasSinManifestar(String rutArchivoFacturasSinManifestar) {
        this.rutArchivoFacturasSinManifestar = rutArchivoFacturasSinManifestar;
    }

    public void setRutaArchivoSoportesDeConsgnaciones(String rutaArchivoSoportesDeConsgnaciones) {
        this.rutaArchivoSoportesDeConsignaciones = rutaArchivoSoportesDeConsgnaciones;
    }

    /**
     * Método que devuelve una cadena con la idRuta donde se guardará
     * temporalmente los datos de las recogidas dealizadas en una idRuta
     * (manifiesto), para luego ser guradadas en la BBDD
     *
     * @return una cadena con la idRuta del archivo
     */
    public String getRutArchivoRecogidasporManifiesto() {
        return rutArchivoRecogidasporManifiesto;
    }

    /**
     * Método que devuelve una cadena con la idRuta donde se guardará
     * temporalmente los datos de las facturasdel manifiesto
     *
     * @return cadena con la idRuta del archivo
     */
    public String getRutArchivofacturasporManifiesto() {
        return rutArchivofacturasporManifiesto;
    }

    /**
     * Método que asigna una cadena con la idRuta donde se guardará
     * temporalmente los facturas asignadas al manifiesto
     *
     * @param rutArchivofacturasporManifiesto cadena que contiene la idRuta del
     * archivo
     */
    public void setRutArchivofacturasporManifiesto(String rutArchivofacturasporManifiesto) {
        this.rutArchivofacturasporManifiesto = rutArchivofacturasporManifiesto;
    }

    public List<CDescuentosPorFactura> getListaDeDescuentos() {
        return listaDeDescuentos;
    }

    /**
     * Metodo encargado de asignar los descuentos de las facturas que estan en
     * distribucion al manifiesto de carga.
     *
     */
    public void setListaDeDescuentos() {
        String sql = "select * from descuentoporfactura df "
                + "join facturaspormanifiesto fm on fm.numeroFactura=df.numeroFactura "
                + "join manifiestosdedistribucion m on m.consecutivo=fm.numeroManifiesto "
                + "where m.consecutivo='" + this.numeroManifiesto + "';";
        Connection con;
        Statement st = null;
        ResultSet rst = null;

        listaDeDescuentos = new ArrayList();
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeDescuentos");

        con = this.ini.getConnRemota();

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CDescuentosPorFactura dxf = new CDescuentosPorFactura(ini);

                    dxf.setActivo(rst.getInt("activo"));
                    dxf.setFechaIng(rst.getDate("fechaIng"));
                    dxf.setFlag(rst.getInt("flag"));
                    dxf.setIddescuentoporfactura(rst.getInt("iddescuentoporfactura"));
                    dxf.setNumeroDocumento(rst.getString("numeroDocumento"));
                    dxf.setUsuario(rst.getString("usuario"));
                    dxf.setValorDescuento(rst.getDouble("valorDescuento"));
                    dxf.setNumeroFactura(rst.getString("numeroFactura"));

                    System.out.println("Cargando descuento -> " + dxf.getNumeroFactura());

                    listaDeDescuentos.add(dxf);

                }
                if (listaDeDescuentos.size() < 1) {
                    listaDeDescuentos = null;
                }

                rst.close();
                st.close();
                //con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
    }

    public void setListaDeDescuentos(List<CDescuentosPorFactura> listaDeDescuentos) {
        this.listaDeDescuentos = listaDeDescuentos;
    }

    public void setListaDeDescuentos(String cadenaDeFacturas) {

        if (cadenaDeFacturas == null) {
            return;
        }
        String sql = "select * from descuentoporfactura where numeroFactura in" + cadenaDeFacturas;
        Connection con;
        Statement st = null;
        ResultSet rst = null;

        listaDeDescuentos = new ArrayList();
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeDescuentos");

        con = this.ini.getConnRemota();
        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CDescuentosPorFactura dxf = new CDescuentosPorFactura(ini);

                    dxf.setActivo(rst.getInt("activo"));
                    dxf.setFechaIng(rst.getDate("fechaIng"));
                    dxf.setFlag(rst.getInt("flag"));
                    dxf.setIddescuentoporfactura(rst.getInt("iddescuentoporfactura"));
                    dxf.setNumeroDocumento("" + rst.getInt("numeroDocumento"));
                    dxf.setNumeroFactura(rst.getString("numeroFactura"));
                    dxf.setUsuario(rst.getString("usuario"));
                    dxf.setValorDescuento(rst.getDouble("valorDescuento"));

                    System.out.println("Cargando descuento -> " + dxf.getNumeroFactura());

                    listaDeDescuentos.add(dxf);

                }
                if (listaDeDescuentos.size() < 1) {
                    listaDeDescuentos = null;
                }

                rst.close();
                st.close();
                //con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//                    rst.close();
//                    st.close();
//                    con.close();
            }

        }

    }

    public List<CRecogidasPorFactura> getListaDeRecogidas() {
        return listaDeRecogidas;
    }

    /**
     * Metodo encargado de asignar los descuentos de las facturas que estan en
     * distribucion al manifiesto de carga.
     *
     */
    public void setListaDeRecogidas() {
        String sql = "select * from recogidaporfactura df "
                + "join facturaspormanifiesto fm on fm.numeroFactura=df.numeroFactura "
                + "join manifiestosdedistribucion m on m.consecutivo=fm.numeroManifiesto "
                + "where m.consecutivo='" + this.numeroManifiesto + "';";
        Connection con;
        Statement st = null;
        ResultSet rst = null;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeRecogidas");
        con = this.ini.getConnRemota();

        if (con != null) {
            listaDeRecogidas = new ArrayList();

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CRecogidasPorFactura rxf = new CRecogidasPorFactura(ini);

                    rxf.setActivo(rst.getInt("activo"));
                    rxf.setFechaIng(rst.getDate("fechaIng"));
                    rxf.setFlag(rst.getInt("flag"));
                    rxf.setIdRecogidaPorFactura(rst.getInt("idrecogidaporfactura"));
                    rxf.setNumeroDocumento("" + rst.getInt("numeroDocumento"));
                    rxf.setUsuario(rst.getString("usuario"));
                    rxf.setValorRecogida(rst.getDouble("valorRecogida"));
                    rxf.setNumeroFactura(rst.getString("numeroFactura"));

                    System.out.println("Cargando descuento -> " + rxf.getNumeroFactura());

                    listaDeRecogidas.add(rxf);

                }

                if (listaDeRecogidas.size() < 1) {
                    listaDeRecogidas = null;
                }
                rst.close();
                st.close();
                // con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void setListaDeRecogidas(List<CRecogidasPorFactura> listaDerecogidas) {
        this.listaDeRecogidas = listaDerecogidas;
    }

    public void setListaDeRecogidas(String cadenaDeFacturas) {

        if (cadenaDeFacturas == null) {
            return;
        }

        String sql = "select * from recogidaporfactura where numeroFactura in" + cadenaDeFacturas;
        Connection con;
        Statement st = null;
        ResultSet rst = null;

        listaDeRecogidas = new ArrayList();
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaDeRecogidas");

        con = this.ini.getConnRemota();

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CRecogidasPorFactura rxf = new CRecogidasPorFactura(ini);

                    rxf.setActivo(rst.getInt("activo"));
                    rxf.setFechaIng(rst.getDate("fechaIng"));
                    rxf.setFlag(rst.getInt("flag"));
                    rxf.setIdRecogidaPorFactura(rst.getInt("idrecogidaporfactura"));
                    rxf.setNumeroDocumento("" + rst.getInt("numeroDocumento"));
                    rxf.setNumeroFactura(rst.getString("numeroFactura"));
                    rxf.setUsuario(rst.getString("usuario"));
                    rxf.setValorRecogida(rst.getDouble("valorRecogida"));

                    System.out.println("Cargando descuento -> " + rxf.getNumeroFactura());

                    listaDeRecogidas.add(rxf);

                }
                if (listaDeRecogidas.size() < 1) {
                    listaDeRecogidas = null;
                }

                rst.close();
                st.close();
                // con.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }

    }

    public Double getPesoKgManifiesto() {
         if (listaFacturasPorManifiesto != null) {
             pesoKgManifiesto = 0.0;
            for (CFacturasPorManifiesto fac : listaFacturasPorManifiesto) {
                pesoKgManifiesto += fac.getPesoFactura();
            }
        }

        return pesoKgManifiesto;
    }

    public void setPesoKgManifiesto(Double pesoManifiesto) {
        this.pesoKgManifiesto = pesoManifiesto;
    }

    /**
     * Método que asignael peos en Kg del manifiesto de carga No tiene
     * parametros
     *
     */
    public void setPesoKgManifiesto() {

        String sql = "select pesoManifiesto from manifiestosdedistribucion m "
                + "where m.consecutivo='" + this.numeroManifiesto + "';";
        int id = 0;
        Connection con;
        Statement st = null;
        ResultSet rst = null;
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setPesoKgManifiesto");

        con = this.ini.getConnRemota();

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.pesoKgManifiesto = rst.getDouble("pesoManifiesto");
                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }

    }

    /**
     * Constructor sin parámetros
     */
    public CManifiestosDeDistribucion() {

    }

    /**
     * Constructor del objeto
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini) throws Exception {
        this.ini = ini;
        this.estadoManifiesto = 0;

    }

    /**
     * Constructor del objeto
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini, int numeroManifiesto) throws Exception {
        this.ini = ini;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;

        listaFacturasPorManifiesto = new ArrayList<>();
        listaDeAuxiliares = new ArrayList<>();
        listaFacturasSinManifestar = new ArrayList<>();
        listaFacturasSinDescargar = new ArrayList<>();
        listaDeSoportesConsignaciones = new ArrayList<>();
        listaFacturasDescargadas = new ArrayList<>();
        listaCProductosPorFacturaDescargados = new ArrayList<>();
        listaDeRecogidasPorManifiesto = new ArrayList<>();
        listaDeDescuentos = new ArrayList<>();
        listaDeRecogidas = new ArrayList<>();

        try {

            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion");
            con = this.ini.getConnRemota();
            sql = "SELECT * "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE  "
                    + "numeroManifiesto='" + numeroManifiesto + "' AND "
                    + "activo=1;";

            //System.out.println(sql);
            llenarLosDatos(con, sql, ini);

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Constructor del objeto
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini, String conductor, String placa, String fecha) throws Exception {
        this.ini = ini;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;

        listaFacturasPorManifiesto = new ArrayList<>();
        listaDeAuxiliares = new ArrayList<>();
        listaFacturasSinManifestar = new ArrayList<>();
        listaFacturasSinDescargar = new ArrayList<>();
        listaDeSoportesConsignaciones = new ArrayList<>();
        listaFacturasDescargadas = new ArrayList<>();
        listaCProductosPorFacturaDescargados = new ArrayList<>();
        listaDeRecogidasPorManifiesto = new ArrayList<>();
        listaDeDescuentos = new ArrayList<>();
        listaDeRecogidas = new ArrayList<>();

        try {

            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion");
            con = this.ini.getConnRemota();

            sql = "SELECT * "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE  "
                    + "conductor='" + conductor + "' AND "
                    + "vehiculo='" + placa + "' AND "
                    + "fechaDistribucion='" + fecha + "'; ";

            //System.out.println(sql);
            llenarLosDatos(con, sql, ini);

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Constructor del objeto
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini, String conductor, String placa, String despachador, String fecha) throws Exception {
        this.ini = ini;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;

        listaFacturasPorManifiesto = new ArrayList<>();
        listaDeAuxiliares = new ArrayList<>();
        listaFacturasSinManifestar = new ArrayList<>();
        listaFacturasSinDescargar = new ArrayList<>();
        listaDeSoportesConsignaciones = new ArrayList<>();
        listaFacturasDescargadas = new ArrayList<>();
        listaCProductosPorFacturaDescargados = new ArrayList<>();
        listaDeRecogidasPorManifiesto = new ArrayList<>();
        listaDeDescuentos = new ArrayList<>();
        listaDeRecogidas = new ArrayList<>();

        try {

            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion");
            con = this.ini.getConnRemota();
            sql = "SELECT * "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE  "
                    + "conductor='" + conductor + "' AND "
                    + "vehiculo='" + placa + "' AND "
                    + "despachador='" + despachador + "' AND "
                    + "fechaDistribucion='" + fecha + "'; ";

            //System.out.println(sql);
            llenarLosDatos(con, sql, ini);

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                //con.close();

            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Constructor del objeto
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini, String conductor, String fecha) throws Exception {
        this.ini = ini;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;

        listaFacturasPorManifiesto = new ArrayList<>();
        listaDeAuxiliares = new ArrayList<>();
        listaFacturasSinManifestar = new ArrayList<>();
        listaFacturasSinDescargar = new ArrayList<>();
        listaDeSoportesConsignaciones = new ArrayList<>();
        listaFacturasDescargadas = new ArrayList<>();
        listaCProductosPorFacturaDescargados = new ArrayList<>();
        listaDeRecogidasPorManifiesto = new ArrayList<>();
        listaDeDescuentos = new ArrayList<>();
        listaDeRecogidas = new ArrayList<>();

        try {

            //con = CConexiones.GetConnection(this.ini.getCadenaRemota(), this.ini.getUsuarioBDRemota(), this.ini.getClaveBDRemota(), "CManifiestosDeDistribucion");
            con = this.ini.getConnRemota();
            sql = "SELECT * "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE  "
                    + "conductor='" + conductor + "' AND fechaDistribucion='" + fecha + "' and "
                    + "activo=1;";

            //System.out.println(sql);
            llenarLosDatos(con, sql, ini);

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Constructor del objeto
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @param idManifiesto numero entero que identifica llave primaria del
     * objeto manifiesto
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini, int idManifiesto, String fecha) throws Exception {
        this.ini = ini;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion");
            con = this.ini.getConnRemota();

            sql = "SELECT * "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE  "
                    + "numeroManifiesto='" + idManifiesto + "' AND "
                    + "fechaIng >='" + fecha + "' AND "
                    + "activo=1;";

            System.out.println(sql);

            llenarLosDatos(con, sql, ini);

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Constructor del objeto que trae el último manifiesto del vehiculo que se
     * ingresó en el parámetro
     *
     * @param ini clase que contiene datos de la configuración del sistema
     * @param placa identifica al vehiculo que tiene asignado un manifiesto de
     * distribucion
     * @throws java.lang.Exception
     */
    public CManifiestosDeDistribucion(Inicio ini, String placa) throws Exception {
        this.ini = ini;
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            // con = CConexiones.GetConnection(this.ini.getCadenaRemota(), this.ini.getUsuarioBDRemota(), this.ini.getClaveBDRemota(), "CManifiestosDeDistribucion");
            con = this.ini.getConnRemota();
            sql = "SELECT *  "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE "
                    + "vehiculo ='" + placa + "' AND "
                    + "activo=1 "
                    + "ORDER BY numeroManifiesto  desc limit 1";

            llenarLosDatos(con, sql, ini);

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Método que permite guardar los registros en la base de datos
     *
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    public boolean grabarManifiestoDeDistribucion() {

        CControladorDeDocumentos controlador = new CControladorDeDocumentos(ini);

        boolean grabado = false;
        try {
            controlador.setIsFree(1);
            controlador.setTipoDocumento(1);
            controlador.grabarControladorDeDocumentos();

//            this.agencia = ini.getUser().getAgencia();
//            this.zona = ini.getUser().getZona();
//            this.regional = ini.getUser().getRegional();
            this.numeroManifiesto = "" + controlador.getIdcontrolador();

            String sql = "";

            sql = "INSERT INTO manifiestosdedistribucion (consecutivo, fechaDistribucion, vehiculo, conductor,despachador,"
                    + " canal,ruta, estadoManifiesto, kmSalida, kmEntrada, kmRecorrido, "
                    + "zona, regional,"
                    //  + " agencia,isFree, horaDeDespacho,horaDeLiquidacion,valorTotalManifiesto,valorRecaudado,valorRecogida,activo, usuarioManifiesto, flag) VALUES ('"
                    + " agencia,isFree, horaDeDespacho,horaDeLiquidacion,"
                    + "valorTotalManifiesto,valorRecaudado,activo, usuario, flag,observaciones,cantDeSalidas) VALUES ('"
                    + this.numeroManifiesto + "','"
                    + this.fechaDistribucion + "','"
                    + this.vehiculo + "','"
                    + this.conductor + "','"
                    + this.despachador + "','"
                    + this.idCanal + "','"
                    + this.idRuta + "','"
                    + this.estadoManifiesto + "','"
                    + this.kmSalida + "','"
                    + this.kmEntrada + "','"
                    + this.kmRecorrido + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + this.isFree + "','"
                    + this.horaDeDespacho + "','"
                    + this.horaDeLiquidacion + "','"
                    + this.valorTotalManifiesto + "','"
                    + this.valorRecaudado + "','"
                    + this.activo + "','"
                    + this.usuarioManifiesto + "','1','"
                    + this.observaciones + "','"
                    + this.cantDeSalidas + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + " fechaDistribucion='" + this.fechaDistribucion + "',"
                    + " vehiculo='" + this.vehiculo + "',"
                    + " conductor='" + this.conductor + "',"
                    + " despachador='" + this.despachador + "',"
                    + " canal='" + this.idCanal + "',"
                    + " ruta='" + this.idRuta + "',"
                    + " estadoManifiesto='" + this.estadoManifiesto + "',"
                    + " kmSalida='" + this.kmSalida + "',"
                    + " kmEntrada='" + this.kmEntrada + "',"
                    + " kmRecorrido='" + this.kmRecorrido + "',"
                    + " zona='" + this.zona + "',"
                    + " regional='" + this.regional + "',"
                    + " agencia='" + this.agencia + "',"
                    + " isFree='" + this.isFree + "',"
                    + " valorTotalManifiesto='" + this.valorTotalManifiesto + "',"
                    + " valorRecaudado='" + this.valorRecaudado + "',"
                    //+ " valorRecogida='" + this.valorRecogida + "'," 
                    + " activo='" + this.activo + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',flag='-1',"
                    + "observaciones ='" + this.observaciones + "';";

            grabado = ini.insertarDatosRemotamente(sql, "CManifiestosDeDistribucion.grabarManifiestoDeDistribucion");

            if (!(listaDeAuxiliares.isEmpty() || listaDeAuxiliares == null || listaDeAuxiliares.size() == 0)) {
                List<String> sentencias = new ArrayList<>();

                for (CEmpleados aux : listaDeAuxiliares) {
                    sql = "INSERT INTO auxiliarespormanifiesto (numeroManifiesto, cedulaAuxiliar, activo, usuario, flag) VALUES "
                            + "('" + this.numeroManifiesto + "',"
                            + " '0',"
                            + " '1',"
                            + " '" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                            + " '-1') ON DUPLICATE KEY UPDATE flag=1;";
                    sentencias.add(sql);
                }

                grabado = ini.insertarDatosRemotamente(sentencias, "CManifiestosDeDistribucion.grabarManifiestoDeDistribucion");

            } else {
                List<String> cad = new ArrayList<>();
                for (CEmpleados aux : listaDeAuxiliares) {
                    cad.add("INSERT INTO auxiliarespormanifiesto (numeroManifiesto, cedulaAuxiliar, activo, usuario, flag) VALUES "
                            + "('" + this.numeroManifiesto + "',"
                            + " '" + aux.getCedula() + "',"
                            + " '1',"
                            + " '" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                            + " '-1') ON DUPLICATE KEY UPDATE flag=1;");
                }
                grabado = ini.insertarDatosRemotamente(cad, "CManifiestosDeDistribucion.grabarManifiestoDeDistribucion");

            }

//            rutaArchivoDescargueFacturas = ini.getRutaDeApp() + "tmp/tmp_" + codificarManifiesto() + "_FacturasDescargados.txt";
//            rutaArchivoDescargueporductosPorFactura = ini.getRutaDeApp() + "tmp/tmp_" + codificarManifiesto() + "_ProductosDescargados.txt";
//            rutArchivoRecogidasporManifiesto = ini.getRutaDeApp() + "tmp/tmp_" + codificarManifiesto() + "_RecogidasDescargados.txt";
//            rutArchivofacturasporManifiesto = ini.getRutaDeApp() + "tmp/tmp_" + codificarManifiesto() + ".txt";
//            rutArchivoFacturasSinManifestar = ini.getRutaDeApp() + "tmp/tmp_" + codificarManifiesto() + "_SinManifestar.txt";
        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite actualizar los registros en la base de datos
     *
     * @return true sí el registro fue grabado, retorna false sí hubo errror al
     * grabar el registro en la BBDD
     */
    public boolean actualizarManifiestoDeDistribucions() {
        boolean grabado = false;
        try {

            String sql = "UPDATE  manifiestosdedistribucion SET "
                    + " fechaDistribucion='" + this.fechaDistribucion + "',"
                    + " vehiculo='" + this.vehiculo + "',"
                    + " conductor='" + this.conductor + "',"
                    + " despachador='" + this.despachador + "',"
                    + " canal='" + this.idCanal + "',"
                    + " ruta='" + this.idRuta + "',"
                    + " estadoManifiesto='" + this.estadoManifiesto + "',"
                    + " kmSalida='" + this.kmSalida + "',"
                    + " kmEntrada='" + this.kmEntrada + "',"
                    + " kmRecorrido='" + this.kmRecorrido + "',"
                    + " zona='" + ini.getIdZona() + "',"
                    + " regional='" + ini.getIdRegional() + "',"
                    + " agencia='" + ini.getIdAgencia() + "',"
                    + " isFree='" + this.isFree + "',"
                    + " valorTotalManifiesto='" + this.valorTotalManifiesto + "',"
                    + " valorRecaudado='" + this.valorRecaudado + "',"
                    + " cantidadPedidos='" + this.cantidadPedidos + "',"
                    + " cantDeSalidas='" + this.cantDeSalidas + "',"
                    + " activo='" + this.activo + "', "
                    + " observaciones='" + this.observaciones + "', "
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + " flag ='3' "
                    + "WHERE  "
                    + "consecutivo='" + this.numeroManifiesto + "';";

            grabado = ini.insertarDatosRemotamente(sql, "CManifiestosDeDistribucion.actualizarManifiestoDeDistribucions");

            if (!(listaDeAuxiliares.isEmpty() || listaDeAuxiliares == null || listaDeAuxiliares.size() == 0)) {
                 List<String> sentencias = new ArrayList<>();
                 for (CEmpleados aux : listaDeAuxiliares) {
                    sql = "INSERT INTO auxiliarespormanifiesto (numeroManifiesto, cedulaAuxiliar, activo, usuario, flag) VALUES "
                            + "('" + this.numeroManifiesto + "','"
                            + aux.getCedula() +"',"
                            + " '1',"
                            + " '" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                            + " '-1') ON DUPLICATE KEY UPDATE "
                            + "cedulaAuxiliar='" + aux.getCedula() + "',"
                            + "flag=1;";
                    sentencias.add(sql);
                }
                  grabado = ini.insertarDatosRemotamente(sentencias, "CManifiestosDeDistribucion.grabarManifiestoDeDistribucion");
            }
            
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite actualizar los registros en la base de datos
     *
     * @return true sí el registro fue grabado, retorna false sí hubo errror al
     * grabar el registro en la BBDD
     */
    public boolean cerrarManifiestoDeDistribucions() {
        boolean grabado = false;
        try {

            String sql = "UPDATE  manifiestosdedistribucion SET "
                    + " estadoManifiesto='4', horaDeLiquidacion = CURRENT_TIMESTAMP "
                    + "WHERE  "
                    + "consecutivo='" + this.numeroManifiesto + "';";

            grabado = ini.insertarDatosRemotamente(sql, "CManifiestosDeDistribucion.cerrarManifiestoDeDistribucions");

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @param cadena cadena que constiene la sentencia sql con los respectivos
     * datos a guardar en la BBDD
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasEnManifiesto(String cadena) {
        boolean grabado = false;
        String sql = null;
        try {

            try {
                sql = "INSERT INTO facturaspormanifiesto (numeroManifiesto,"
                        + " numeroFactura, valorARecaudarFactura,pesoFactura,adherencia,zona, regional, agencia, activo, usuario,"
                        + " flag) VALUES " + cadena;

                grabado = ini.insertarDatosRemotamente(sql, "CManifiestosDeDistribucion.grabarFacturasEnManifiesto");

            } catch (Exception ex) {
                System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
            }

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error engrabarFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
        }
        return grabado;

    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @param cadena cadena que constiene la sentencia sql con los respectivos
     * datos a guardar en la BBDD
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasEnManifiesto() {
        boolean grabado = false;
        String sql = null;
        try {

            try {
                sql = "insert into facturaspormanifiesto("
                        + "consecutivo, "
                        + "numeroManifiesto, "
                        + "numeroFactura, "
                        + "valorARecaudarFactura, "
                        + "pesoFactura,"
                        + "adherencia, "
                        + "zona, "
                        + "regional, "
                        + "agencia, "
                        + "activo, "
                        + "usuario,"
                        + "despachado) "
                        + "select null,'"
                        + numeroManifiesto
                        + "',f.numeroFactura,"
                        + "f.valorTotalFactura,"
                        + "f.pesofactura,"
                        + "@rownum:=@rownum+1,'"
                        + this.zona + "','"
                        + this.regional + "','"
                        + ini.getCodigoAgencia() + "','"
                        + "1','"
                        + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','0'  "
                        + "from facturas f "
                        + " JOIN    (SELECT @rownum := 0) r "
                        + "where f.numeroFactura in";
                String cadena = ("('");
                for (CFacturasPorManifiesto fac : listaFacturasPorManifiesto) {
                    cadena += fac.getNumeroFactura() + "','";
                }

                cadena = cadena.substring(0, cadena.length() - 2);
                cadena = cadena + (");");
                sql = sql + cadena;
                grabado = ini.insertarDatosRemotamente(sql, "CManifiestosDeDistribucion.grabarFacturasEnManifiesto");

            } catch (Exception ex) {
                System.out.println("Error en insertar registro sql " + ex.getMessage() + "\n " + sql);
            }

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error engrabarFacturasPoManifiesto sql " + ex.getMessage() + "\n " + sql);
        }
        return grabado;

    }

    /**
     * Método que permite borrar de una tabla temporal los registros de las
     * facturas que provisionalmente fueron asignadas al manifiesto
     *
     * @param manifiesto ccorresonde a un entero que identifica el numero de
     * manifiesto
     * @return devuelve true si los registros fueron borrados, retorna false sí
     * hubo problemas al borrar los registros.
     */
    public boolean borrarTMPFacturasPoManifiesto(int manifiesto) {
        boolean borrado = false;
        try {

            String sql = "DELETE FROM tmpfacturaspormanifiesto "
                    + "WHERE "
                    + "numeroManifiesto='" + manifiesto + "';";
            borrado = ini.insertarDatosLocalmente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }

    /**
     * Método que devuelve un List <CRecogidasPorManifiesto> , las cuales
     * corresponden a las objetos facturas CRecogidasPorManifiesto que se
     * encuentran en el manifiesto de distribución
     *
     * @param numeroDeManifiesto numero entero que corresponde al Id del numero
     * de manifiesto
     * @param caso si es igual a cero trae datos de la tabla temporal y uno de
     * la tabla definitiva distribucion
     *
     * @return List con los objetos CRecogidasPorManifiesto del manifiesto
     */
//    private List<CRecogidasPorManifiesto> listadoDeRecogidasPorManifiesto(int numeroManifiesto) {
//        List<CRecogidasPorManifiesto> lista = new ArrayList();
//        try {
//
//            Connection con;
//            Statement st;
//            String sql = null;
//            ResultSet rst;
//            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
//
//            sql = "SELECT idRecogidasPorManifiesto, idNumeroManifiesto, numeroFactura, facturaAfectada, "
//                    + "numeroDeSoporte, valorRecogida "
//                    + "FROM recogidaspormanifiesto "
//                    + " WHERE "
//                    + " idNumeroManifiesto='" + numeroManifiesto + "'; ";
//
//            if (con != null) {
//                st = con.createStatement();
//                rst = st.executeQuery(sql);
//                while (rst.next()) {
//                    CRecogidasPorManifiesto recogida = new CRecogidasPorManifiesto();
//
//                    recogida.setIdRecogidasPorManifiesto(rst.getInt("idRecogidasPorManifiesto"));
//                    recogida.setIdNumeroManifiesto(rst.getInt("idNumeroManifiesto"));
//                    recogida.setNumeroFactura(rst.getString("numeroFactura"));
//                    recogida.setFacturaAfectada(rst.getString("facturaAfectada"));
//                    recogida.setNumeroDeSoporte(rst.getString("numeroDeSoporte"));
//                    recogida.setValorRecogida(rst.getDouble("valorRecogida"));
//
//                    lista.add(recogida);
//                }
//                rst.close();
//                st.close();
//                con.close();
//            }
//
//        } catch (SQLException ex) {
//            System.out.println("Error en listadoDeDevolucionesPorManifiesto sql " + ex.getMessage());
//            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            System.out.println("Error en listadoDeDevolucionesPorManifiesto sql " + ex.getMessage());
//            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return lista;
//
//    }
    /**
     * Método que devuelve un List de tipo Cadena , las cuales corresponden a
     * las numeros de las facturas que se encuentran en el manifiesto de
     * distribución pendientes por descargar del sistema
     *
     * @return retorna cadena con los numeros de las facturas pendientes por
     * descargar
     */
    public List<String> listaDeManifiestosSinDescargar() {
        List<String> lista = new ArrayList();
        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.listaDeManifiestosSinDescargar");
            con = this.ini.getConnRemota();
            sql = " SELECT manifiestosdedistribucion.consecutivo "
                    + " FROM manifiestosdedistribucion "
                    + "WHERE "
                    + "manifiestosdedistribucion.estadoManifiesto=3";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    lista.add("" + rst.getInt("consecutivo"));
                }
                rst.close();
                st.close();
                //con.close();
            }

        } catch (SQLException ex) { //Catch de excepciones
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex) {
            System.out.println("Error en listadoDeProductosPorFactura sql " + ex.getMessage());
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;

    }

    /**
     * Método que devuelve un List de tipo CFacturas , las cuales corresponden a
     * las objetos facturas que se encuentran pendientes en el manifiesto de
     * distribución
     *
     * @return retorna List con los objetos de las facturas pendientes por
     * descargar
     */
    public List<CFacturas> listadoDeFacturasPendientes() {
        Connection con;
        Statement st = null;
        ResultSet rst = null;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.listadoDeFacturasPendientes");
        con = this.ini.getConnRemota();
        List<CFacturas> arrFac = null;
        String sql = " SELECT  numeroFactura "
                + "FROM facturaspormanifiesto "
                + "WHERE numeroFactura  NOT IN (SELECT numeroFactura FROM facturasdescargadas WHERE numeroManifiesto='" + numeroManifiesto + "') AND "
                + "numeroManifiesto='" + numeroManifiesto + "' and activo=1;";
        if (con != null) {
            try {
                arrFac = new ArrayList<>();
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturas fac = new CFacturas(ini, rst.getString("numeroFactura"));
                    arrFac.add(fac);
                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) { //Catch de excepciones
                try {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                    rst.close();
                    st.close();
                    //con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (Exception ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arrFac;
    }

    /**
     * Método que asigna los objetos CFacturasPorManifiesto , las cuales
     * corresponden a las las facturas que se encuentran en el manifiesto de
     * distribución
     *
     * @param numeroDeManifiesto corresponde al Id del numero de manifiestos de
     * distribución
     */
//    public void setCFacturasPorManifiesto(String numeroDeManifiesto) {// caso= 0 en la tabla temporal 1= en la tabla definitiva
//        List<CFacturasPorManifiesto> arrFM = new ArrayList<>();
//        ResultSet rst = null;
//        Statement st = null;
//        Connection con;
//
//        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
//
//        String sql = " SELECT facturaspormanifiesto.consecutivo, facturaspormanifiesto.numeroManifiesto, facturaspormanifiesto.numeroFactura, "
//                + "facturaspormanifiesto.adherencia, facturaspormanifiesto.zona, facturaspormanifiesto.regional, facturaspormanifiesto.agencia,"
//                + "facturaspormanifiesto. activo, facturaspormanifiesto.fechaIng, facturaspormanifiesto.usuario, facturaspormanifiesto.flag,"
//                + "facturas.valorTotalFactura "
//                + " FROM facturaspormanifiesto, facturas "
//                + "WHERE "
//                + "facturaspormanifiesto.numeroFactura=facturas.numeroFactura AND "
//                + "facturas.zona='" + this.ini.getUser().getZona() + "' AND "
//                + "facturas.regional='" + this.ini.getUser().getRegional() + "' AND "
//                + "facturas.agencia='" + this.ini.getUser().getAgencia() + "' AND "
//                + "facturaspormanifiesto.numeroManifiesto='" + numeroDeManifiesto + "' "
//                + "and facturaspormanifiesto.activo=1  "
//                + "ORDER BY facturaspormanifiesto.adherencia ASC ";
//        if (con != null) {
//            try {
//                st = con.createStatement();
//                rst = st.executeQuery(sql);
//                while (rst.next()) {
//                    CFacturasDescargadas fxm = new CFacturasDescargadas(ini);
//                    fxm.setConsecutivo(rst.getInt("consecutivo"));
//                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
//                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
//                    fxm.setAdherencia(rst.getInt("adherencia"));
//                    fxm.setZona(rst.getInt("zona"));
//                    fxm.setRegional(rst.getInt("regional"));
//                    fxm.setAgencia(rst.getInt("agencia"));
//                    fxm.setValorARecaudarFactura(rst.getDouble("valorTotalFactura"));
//
//                    System.out.println("Cargando factura descargada número -> " + fxm.getNumeroFactura());
//
//                    arrFM.add(fxm);
//
//                }
//                rst.close();
//                st.close();
//                con.close();
//                this.listaFacturasPorManifiesto = arrFM;
//
//            } catch (SQLException ex) { //Catch de excepciones
//                try {
//                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//                    rst.close();
//                    st.close();
//                    con.close();
//                } catch (SQLException ex1) {
//                    Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
    /**
     * Método que devuelve el código del manifiesto para imprimirlo en el label
     * de la vista
     *
     * @return Una cadena con el código del manifiesto
     */
    public String codificarManifiesto() {
        String textoManifiesto = null;
        if (this.fechaDistribucion == null) {
            return textoManifiesto = "sin manifiesto";
        }

        String numeroDeManifiesto = String.valueOf(this.numeroManifiesto);
        String codruta = String.valueOf(this.idRuta);
        String fecha = this.fechaDistribucion.toString().replace("-", "");
        String codagencia = String.valueOf(ini.getCodigoAgencia());
        switch (numeroDeManifiesto.length()) {
            case 1:
                numeroDeManifiesto = "000" + numeroDeManifiesto;
                break;
            case 2:
                numeroDeManifiesto = "00" + numeroDeManifiesto;
                break;
            case 3:
                numeroDeManifiesto = "0" + numeroDeManifiesto;
                break;

        }
        switch (codruta.length()) {
            case 1:
                codruta = "00" + codruta;
                break;
            case 2:
                codruta = "0" + codruta;
                break;

        }
        switch (codagencia.length()) {
            case 1:
                codagencia = "00" + codagencia;
                break;
            case 2:
                codagencia = "0" + codagencia;
                break;
        }
        textoManifiesto = this.vehiculo + "-" + fecha + "-" + codagencia + "-" + codruta + "-" + numeroDeManifiesto;
        return textoManifiesto;
    }

    /**
     * Método que permite bloquear ó liberar de tal modo que otro
     * usuarioManifiesto pueda usar ó no dicho manifiesto
     *
     * @param liberar true para liberar y false para bloquear manifiesto
     * @return devuelve verdadero si manifiesto es liberado y false para
     * bloquear manifiesto.
     */
    public boolean liberarManifiesto(boolean liberar) {
        boolean liberado = false;
        String sql;
        if (numeroManifiesto != null) {

            if (liberar) {
                sql = "UPDATE  manifiestosdedistribucion set isFree=1  WHERE consecutivo='" + this.numeroManifiesto + "';";
                this.isFree = 1;
                liberado = true;
            } else {
                sql = "UPDATE manifiestosdedistribucion set isFree=0 WHERE consecutivo='" + this.numeroManifiesto + "';";
                this.isFree = 0;

            }
            ini.insertarDatosRemotamente(sql, "CManifiestosDeDistribucion.liberarManifiesto");
        }

        return liberado;
    }

    /**
     * Método que devuelve una cadena con alsentencia SQL que permite actualizar
     * el manifiesto actual para bloquearlo ó desbloquearlo
     *
     * @param liberar true para liberar y false para bloquear manifiesto
     * @param i parameto que hace nada, solo diferenciar del otro método con el
     * mismo nombre.
     * @return Cadena con la sentencia SQL.
     */
    public String liberarManifiesto(boolean liberar, int i) {

        String sql;
        if (this.numeroManifiesto != null) {
            if (liberar) {
                sql = "UPDATE  manifiestosdedistribucion set isFree=1  WHERE consecutivo='" + this.numeroManifiesto + "';";
                this.isFree = 1;
            } else {
                sql = "UPDATE manifiestosdedistribucion set isFree=0 WHERE consecutivo='" + this.numeroManifiesto + "';";
                this.isFree = 0;
            }
        } else {
            sql = "UPDATE  manifiestosdedistribucion set isFree=1  WHERE consecutivo='0';";
        }

        return sql;
    }

    /**
     * Método que devuelve una cadena con la lista de los campos de la BBDD
     * separados por ","
     *
     * @return una cadena con la lusta de todos los campos de la tabla C
     */
    public String getCadenaConLosCampos() {
        String cadena = null;
        cadena = this.numeroManifiesto + ","
                + this.fechaDistribucion + ","
                + this.vehiculo + ","
                + this.conductor + ","
                + this.idCanal + ","
                + this.idRuta + ","
                + this.estadoManifiesto + ","
                + this.kmSalida + ","
                + this.kmEntrada + ","
                + this.kmRecorrido + ","
                + this.zona + ","
                + this.regional + ","
                + ini.getCodigoAgencia() + ","
                + this.isFree + ","
                + this.valorTotalManifiesto + ","
                + this.valorRecaudado + ","
                //+ this. valorRecogida + "," 
                + this.horaDeDespacho + ","
                + this.horaDeLiquidacion + ","
                + this.activo;

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
            sql = "INSERT INTO manifiestosdedistribucion ( "
                    + "consecutivo, fechaDistribucion, vehiculo, conductor, despachador,"
                    + "canal, ruta, estadoManifiesto, kmSalida, kmEntrada,"
                    + "kmRecorrido, zona, regional, agencia, isFree, "
                    + "valorTotalManifiesto, valorRecaudado, horaDeDespacho, horaDeLiquidacion, pesoManifiesto,"
                    + "cantidadPedidos, activo,usuario, flag,"
                    + "observaciones, cantDeSalidas) "
                    + "VALUES ('"
                    + this.numeroManifiesto + "','"
                    + this.fechaDistribucion + "','"
                    + this.vehiculo + "','"
                    + this.conductor + "','"
                    + this.despachador + "','"
                    + this.idCanal + "','"
                    + this.idRuta + "','"
                    + this.estadoManifiesto + "','"
                    + this.kmSalida + "','"
                    + this.kmEntrada + "','"
                    + this.kmRecorrido + "','"
                    + ini.getIdZona()+ "','"
                    + ini.getIdRegional() + "','"
                    + ini.getIdAgencia() + "','"
                    + this.isFree + "','"
                    + this.valorTotalManifiesto + "','"
                    + this.valorRecaudado + "','"
                    + this.horaDeDespacho + "','"
                    + this.horaDeLiquidacion + "','"
                    + this.pesoKgManifiesto + "','"
                    + this.cantidadPedidos + "','"
                    + this.activo + "','"
                    + this.usuarioManifiesto + "',"
                    + "'1','"
                    + this.observaciones + "','"
                    + this.cantDeSalidas + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + " fechaDistribucion='" + this.fechaDistribucion + "',"
                    + " vehiculo='" + this.vehiculo + "',"
                    + " conductor='" + this.conductor + "',"
                    + " despachador='" + this.despachador + "',"
                    + " canal='" + this.idCanal + "',"
                    + " ruta='" + this.idRuta + "',"
                    + " estadoManifiesto='" + this.estadoManifiesto + "',"
                    + " kmSalida='" + this.kmSalida + "',"
                    + " kmEntrada='" + this.kmEntrada + "',"
                    + " kmRecorrido='" + this.kmRecorrido + "',"
                    + " zona='" + this.zona + "',"
                    + " regional='" + this.regional + "',"
                    + " agencia='" + ini.getCodigoAgencia() + "',"
                    + " isFree='" + this.isFree + "',"
                    + " valorTotalManifiesto='" + this.valorTotalManifiesto + "',"
                    + " valorRecaudado='" + this.valorRecaudado + "',"
                    + " cantidadPedidos='" + this.cantidadPedidos + "',"
                    + " activo='" + this.activo + "',"
                    + "observaciones='" + this.observaciones + "';";

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }
      /**
     * Método que devuelve una cadena con sentencia SQL para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertSQLSinActualizacion() {
        String sql = null;
        try {
            sql = "INSERT INTO manifiestosdedistribucion ( "
                    + "consecutivo, fechaDistribucion, vehiculo, conductor, despachador,"
                    + "canal, ruta, estadoManifiesto, kmSalida, kmEntrada,"
                    + "kmRecorrido, zona, regional, agencia, isFree, "
                    + "valorTotalManifiesto, valorRecaudado, horaDeDespacho, horaDeLiquidacion, pesoManifiesto,"
                    + "cantidadPedidos, activo,usuario, flag,"
                    + "observaciones, cantDeSalidas) "
                    + "VALUES ('"
                    + this.numeroManifiesto + "','"
                    + this.fechaDistribucion + "','"
                    + this.vehiculo + "','"
                    + this.conductor + "','"
                    + this.despachador + "','"
                    + this.idCanal + "','"
                    + this.idRuta + "','"
                    + this.estadoManifiesto + "','"
                    + this.kmSalida + "','"
                    + this.kmEntrada + "','"
                    + this.kmRecorrido + "','"
                    + ini.getIdZona()+ "','"
                    + ini.getIdRegional() + "','"
                    + ini.getIdAgencia() + "','"
                    + this.isFree + "','"
                    + this.valorTotalManifiesto + "','"
                    + this.valorRecaudado + "','"
                    + this.horaDeDespacho + "','"
                    + this.horaDeLiquidacion + "','"
                    + this.pesoKgManifiesto + "','"
                    + this.cantidadPedidos + "','"
                    + this.activo + "','"
                    + this.usuarioManifiesto + "','"
                    + this.flag + "','"
                    + this.observaciones + "','"
                    + this.cantDeSalidas + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "flag='" + this.flag + "';";

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    /**
     * Método que devuelve una cadena con la lista de los números de factura que
     * están registrdos en el mnifiesto, genera el array de objetos
     * CfacturasPorManifiesto
     *
     * @param archivo archivo donde se encuentran grabados los datos
     */
    public void setListaFacturasPorManifiesto(File archivo) {
        listaFacturasPorManifiesto = new ArrayList<>();
        DataInputStream entrada = null;
        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(archivo);

            // Creamos el objeto de entrada
            entrada = new DataInputStream(fstream);

            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

            String strLinea;
            // Leer el archivo linea por linea

            /*Se recorre el archivo y nos traemos los números de las facturas guardadas
              en el archivo temporal */
            while ((strLinea = buffer.readLine()) != null) {
                String[] cadena = strLinea.split(",");
                Vst_Factura fac = new Vst_Factura(ini, cadena[2], false);

                CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);

                fxm.setConsecutivo(0);
                fxm.setIdCanal(this.idCanal);
                fxm.setNombreCanal(this.nombreCanal);
                fxm.setAdherencia(Integer.parseInt(cadena[0]));
                fxm.setNumeroManifiesto(cadena[1]);
                fxm.setNumeroFactura(cadena[2]);
                fxm.setValorARecaudarFactura(Double.parseDouble(cadena[3]));
                fxm.setFechaDistribucion(this.fechaDistribucion);
                fxm.setVehiculo(this.vehiculo);
                fxm.setConductor(this.conductor);
                fxm.setNombreConductor(this.nombreConductor);
                fxm.setNombreDeCliente(fac.getNombreDeCliente());
                fxm.setDireccionDeCliente(fac.getDireccionDeCliente());
                fxm.setValorFacturaSinIva(fac.getValorFacturaSinIva());
                fxm.setValorTotalFactura(fac.getValorTotalFactura());
                fxm.setValorRechazo(0.0);
                fxm.setValorDescuento(0.0);
                fxm.setValorRecaudado(0.0);
                fxm.setVendedor(fac.getVendedor());
                fxm.setPesoFactura(fac.getPesofactura());
                fxm.setFormaDePago("");

                listaFacturasPorManifiesto.add(fxm);

                // Imprimimos la línea por pantalla
                System.out.println(strLinea);
            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) {
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("Ocurrio un error: " + e.getMessage());
                entrada.close();
            } catch (IOException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Método que devuelve una cadena con la lista de los números de factura que
     * están registrdos en el mnifiesto, genera el array de objetos
     * CfacturasPorManifiesto
     *
     * @param archivo
     * @return una cadena con la lista de las facturas
     */
    public String recuperarListaFacturasEnManifiesto(File archivo) {
        DataInputStream entrada;
        String lista = "";
        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(archivo);

            // Creamos el objeto de entrada
            entrada = new DataInputStream(fstream);

            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

            String strLinea;
            // Leer el archivo linea por linea

            int i = 0;
            /*Se recorre el archivo y nos traemos los números de las facturas guardadas
              en el archivo temporal */
            while ((strLinea = buffer.readLine()) != null) {
                String[] cadena = strLinea.split(",");
                CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                fxm.setAdherencia(Integer.parseInt(cadena[0]));
                fxm.setNumeroManifiesto(cadena[1]);
                fxm.setNumeroFactura(cadena[2]);
                fxm.setValorARecaudarFactura(Double.parseDouble(cadena[3]));
                if (i < 1) {
                    lista += cadena[2];
                } else {
                    lista += "," + cadena[2];
                }
                i++;

                // Imprimimos la línea por pantalla
                System.out.println(strLinea);
            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) { //Catch de excepciones
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
        return lista;

    }

    /**
     * Método que devuelve un array con todos los objetos Cfacturas que están
     * registrdos en el mnifiesto
     *
     * @param numFactura
     */
//    public List<CFacturas> getListaCFacturas(File archivo) throws Exception {
//       
//        String listaFacturas =recuperarListaFacturasEnManifiesto(archivo);
//        listaCFacturas= new ArrayList<>();
//        
//        try {
//
//            Connection con;
//            Statement st;
//            String sql;
//            ResultSet rst;
//    if(!listaFacturas.isEmpty()){        
//            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//            
//            sql = "SELECT numeroFactura,fechaDeVenta, cliente, vendedor,formaDePago,idCanal,valorFacturaSinIva,valorIvaFactura,"
//                    + "valorTotalFactura,valorRechazo,valorDescuento,valorTotalRecaudado,imagenFactura,formato,zona,regional,agencia,isFree,"
//                    + "activo,fechaIng,usuarioManifiesto,flag "
//                    + "FROM facturas "
//                    + "WHERE numeroFactura IN ("
//                    + listaFacturas + ");";
//            
//
//    
//
//
//            if (con != null) {
//                st = con.createStatement();
//                rst = st.executeQuery(sql);
//                while (rst.next()) {
//                    
//                    CFacturas factura = new CFacturas(ini);
//                    
//                   factura.setNumeroFactura(rst.getString("numeroFactura"));
//                   factura.setFechaDeVenta(rst.getDate("fechaDeVenta"));
//                   factura.setCodigoDeCliente(rst.getString("cliente"));
//                   factura.setVendedor(rst.getString("vendedor"));
//                   factura.setFormaDePago(rst.getInt("formaDePago"));
//                   factura.setIdCanal(rst.getInt("idCanal"));
//                   factura.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
//                   factura.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
//                   factura.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
//                   factura.setValorRechazo(rst.getDouble("valorRechazo"));
//                   factura.setValorDescuento(rst.getDouble("valorDescuento"));
//                   factura.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
//                   factura.setFormato(rst.getString("formato"));
//                   factura.setZona(rst.getInt("zona"));
//                   factura.setRegional(rst.getInt("regional"));
//                   factura.setAgencia(rst.getInt("agencia"));
//                   factura.setIsFree(rst.getInt("isFree"));
//                   factura.setActivoFactura(rst.getInt("activo"));
//                   
//                   listaCFacturas.add(factura);
//                }
//                rst.close();
//                st.close();
//                con.close();
//            }
//            }
//
//        } catch (SQLException ex) {
//            System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage().toString());
//            Logger.getLogger(CFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return listaCFacturas;
//
//    }
//  
    public void eliminarRecogidaDelArchivo(String numFactura) {
        double valor = 0.0;

        try {

            /* Eliminamos la fila del archivo de las Facturas descargadasr */
//            String idRuta = this.rutArchivoRecogidasporManifiesto;
            ArchivosDeTexto archivo = new ArchivosDeTexto(this.rutArchivoRecogidasporManifiesto);

            for (CRecogidasPorManifiesto obj : listaDeRecogidasPorManifiesto) {
                if (obj.getNumeroFactura().equals(numFactura)) {
                    listaDeRecogidasPorManifiesto.remove(obj);
                    archivo.borrarLinea(obj.getCadenaConLosCampos());

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que permite Actualiza el traslado de los registros en la base de
     * datos
     *
     * @param manifiestoOrigen
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    public boolean grabarTrasladoDeFacturas(String manifiestoOrigen, List<CFacturasPorManifiesto> listaDeFacturasParaDescargar) {

        List<String> senTenciasSql = new ArrayList<>();
        String sql = "";

        boolean grabado = false;
        try {
            sql = "update facturaspormanifiesto set numeroManifiesto='" + this.numeroManifiesto + "', "
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "fechaIng = CURRENT_TIMESTAMP "
                    + "where numeroManifiesto='" + manifiestoOrigen + "' and numeroFactura in(";
            String lista = "'";

            for (CFacturasPorManifiesto obj : listaDeFacturasParaDescargar) {
                lista += obj.getNumeroFactura() + "','";

            }
            lista = lista.substring(0, lista.length() - 2);
            sql += lista + ")";

            // sql = sql.substring(0, sql.length() - 2) + ");";
            //sql+="update manifiestosdedistribucion set estadoManifiesto='3' where consecutivo='" + this.numeroManifiesto + "';"; 
            senTenciasSql.add(sql);
            sql = "";
            sql = "update facturasdescargadas set numeroManifiesto='" + this.numeroManifiesto + "', "
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "fechaIng =  CURRENT_TIMESTAMP "
                    + "where numeroManifiesto='" + manifiestoOrigen + "' and numeroFactura in(";

//            for (CFacturasPorManifiesto obj : listaDeFacturasParaDescargar ) {
//                sql += obj.getNumeroFactura() + "','";
//
//            }
            sql += lista + ")";
            //sql = sql.substring(0, sql.length() - 2) + ");";
            //sql+="update manifiestosdedistribucion set estadoManifiesto='3' where consecutivo='" + this.numeroManifiesto + "';"; 

            senTenciasSql.add(sql);

            grabado = ini.insertarDatosRemotamente(senTenciasSql, "CamnifiestosDeDistribucion.grabarTrasladoDeFacturas");

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean habilitarManifiesto() {

        String sql;

        boolean grabado = false;
        try {
            sql = "update manifiestosdedistribucion set isFree='1' "
                    + "where consecutivo='" + this.numeroManifiesto + "' " + " and (estadoManifiesto='2' or estadoManifiesto='3');";

            grabado = ini.insertarDatosRemotamente(sql);
            new Thread(new HiloListadoDeManifiestosSinDescargar(ini));

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    private void llenarLosDatos(Connection con, String sql, Inicio ini1) throws SQLException {
        Statement st;
        ResultSet rst;
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {

                // String 
                this.numeroManifiesto = rst.getString("numeroManifiesto");
                this.vehiculo = rst.getString("vehiculo");
                this.tipoContrato = rst.getString("tipoContrato");
                this.conductor = rst.getString("conductor");
                this.nombreConductor = rst.getString("nombreConductor");
                this.apellidosConductor = rst.getString("apellidosConductor");
                this.despachador = rst.getString("despachador");
                this.nombreDespachador = rst.getString("nombreDespachador");
                this.apellidosDespachador = rst.getString("apellidosDespachador");
                this.telefonoConductor = rst.getString("telefonoConductor");
                this.nombreCanal = rst.getString("nombreCanal");
                this.nombreDeRuta = rst.getString("nombreDeRuta");
                this.tipoRuta = rst.getString("tipoRuta");
                this.usuarioManifiesto = rst.getString("usuarioManifiesto");
                this.tipoVehiculo = rst.getString("tipoVehiculo");

                //int 
                this.idCanal = rst.getInt("idCanal");
                this.idRuta = rst.getInt("idRuta");
                this.estadoManifiesto = rst.getInt("estadoManifiesto");
                this.kmSalida = rst.getInt("kmSalida");
                this.kmEntrada = rst.getInt("kmEntrada");
                this.kmRecorrido = rst.getInt("kmRecorrido");
                this.isFree = rst.getInt("isFree");
                this.cantidadPedidos = rst.getInt("cantidadPedidos");
                this.zona = rst.getInt("zona");
                this.regional = rst.getInt("regional");
                this.agencia = rst.getInt("agencia");
                this.activo = rst.getInt("activo");
                this.trazabilidad = rst.getInt("trazabilidad");
                // Double 
                this.valorTotalManifiesto = rst.getDouble("valorTotalManifiesto");
                this.valorRecaudado = rst.getDouble("valorRecaudado");
                this.valorConsignado = rst.getDouble("valorConsignado");
                // Date 
                this.fechaDistribucion = rst.getString("fechaDistribucion");
                this.horaDeDespacho = rst.getString("horaDeDespacho");
                this.horaDeLiquidacion = rst.getString("horaDeLiquidacion");
                this.fechaReal = rst.getString("fechaReal");
                this.pesoKgManifiesto = rst.getDouble("pesoManifiesto");
                this.observaciones = rst.getString("observaciones");
                this.cantDeSalidas = rst.getInt("cantDeSalidas");

                crearRutasDeArchivos();

                this.setListaDeAuxiliares("" + this.numeroManifiesto);
                // this.setListaFacturasPorManifiesto();

            } else {
                // String 
                this.numeroManifiesto = null;

            }
            rst.close();
            st.close();
            //con.close();
        }
    }

    public void crearRutasDeArchivos() {
        String codigomanifiesto = codificarManifiesto();

        rutaArchivoDescargueFacturas = ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_FacturasDescargados.txt";
        rutaArchivoDescargueporductosPorFactura = ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_ProductosDescargados.txt";
        rutArchivoRecogidasporManifiesto = ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_RecogidasDescargados.txt";
        rutArchivofacturasporManifiesto = ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + ".txt";
        rutArchivoFacturasSinManifestar = ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_SinManifestar.txt";
        rutaArchivoSoportesDeConsignaciones = ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_consignaciones.txt";
    }

    public int getCantidadManifiestosPendientesConductor() {
        int cantidadMannifiestosPendientes = 0;
        String cadena = "select count(consecutivo) from manifiestosdedistribucion where conductor='" + this.conductor + "' and estadoManifiesto=3 "
                + "and canal='" + this.idCanal + "';";
        Connection con;
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.crearRutasDeArchivos");

        con = this.ini.getConnRemota();
        Statement st;
        ResultSet rst;
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(cadena);
                if (rst.next()) {

                    cantidadMannifiestosPendientes = rst.getInt(1);
                }

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cantidadMannifiestosPendientes;
    }

    public List<CFacturasPorManifiesto> getListaFacturasSinDescargar() {
        return listaFacturasSinDescargar;
    }

    public void setListaFacturasSinDescargar(List<CFacturasPorManifiesto> listaFacturasSinDescargar) {
        this.listaFacturasSinDescargar = listaFacturasSinDescargar;
    }

    public List<CProductosPorMinuta> getListaDeProductosMinuta() {
        return listaDeProductosMinuta;
    }

    public void setListaDeProductosMinuta(List<CProductosPorMinuta> listaDeProductosMinuta) {
        this.listaDeProductosMinuta = listaDeProductosMinuta;
    }

    public void setListaDeProductosMinuta() {

        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        String sql;
        CProductosPorMinuta minuta;
        try {
            sql = "select pf.codigoProducto,p.descripcionProducto,sum(pf.cantidad) as cantidad, sum(pf.pesoProducto)/1000 as peso  "
                    + "from facturaspormanifiesto fm "
                    + "join productosporfactura pf on pf.factura=fm.numerofactura "
                    + "join productos p on p.codigoProducto=pf.codigoProducto "
                    + "join facturas f on f.numeroFactura=pf.factura "
                    + "where fm.numeroManifiesto='" + this.numeroManifiesto + "' and fm.activo=1 "
                    // + "group by f.vendedor,pf.codigoProducto "
                    // + "order by f.vendedor,pf.codigoProducto asc ;";
                    + "group by pf.codigoProducto "
                    + "order by pf.codigoProducto asc ;";
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeCargos");

            con = this.ini.getConnRemota();

            listaDeProductosMinuta = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    minuta = new CProductosPorMinuta(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    minuta.setCodigoProducto(rst.getString("codigoProducto"));
                    minuta.setDescripcionProducto(rst.getString("descripcionProducto"));
                    minuta.setCantidad(rst.getDouble("cantidad"));
                    minuta.setPeso(rst.getDouble("peso"));

                    listaDeProductosMinuta.add(minuta);

                    System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                // con.close();
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, e);
                rst.close();
                st.close();
                // con.close();
                System.err.println(e.getMessage());

            } catch (SQLException ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasDescargadas100(boolean isContado) {
        boolean grabado = false;
        String sql = null;
        String listaFacturas;
        Connection con;//= null;
        Statement st = null;//= null;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifeistosDeDistribucion.grabarFacturasDescargadas100");
        con = this.ini.getConnRemota();
        try {
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                listaFacturas = "('";

                for (CFacturasPorManifiesto fact : listaFacturasPorManifiesto) {
                    System.out.println("plazo dias = " + fact.getPlazoDias());
                    sql = "INSERT INTO facturasdescargadas(consecutivo,numeroManifiesto,adherencia,numeroFactura,"
                            + "valorRechazo,valorDescuento,valorRecaudado,movimientoFactura,motivoRechazo,activo,fechaIng,usuario,flag)VALUES('"
                            + fact.getConsecutivo() + "','"
                            + fact.getNumeroManifiesto() + "','"
                            + fact.getAdherencia() + "','"
                            + fact.getNumeroFactura() + "','"
                            + "0','"
                            + "0','";
                    if (fact.getPlazoDias() <= 1) {
                        sql += fact.getValorTotalFactura() + "','";
                        fact.setTipoDeDEscargue("E. T. Cn");
                    } else {
                        sql += fact.getFpContado() + "','"; // valor a cancelar contado
                        fact.setTipoDeDEscargue("E. T. Cr");

                    }
                    sql += "2','"
                            + "1','"
                            + "1',"
                            + "CURRENT_TIMESTAMP(),'"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                            + "1') on duplicate key update flag='-1';";

                    st.addBatch(sql);
                    listaFacturas += fact.getNumeroFactura() + "','";

                }
                st.executeBatch();
                grabado = true;
                con.commit();
                st.close();
                //  con.close();
                listaFacturas = listaFacturas.substring(0, listaFacturas.length() - 2).concat(");");

                /*Actualiza las facturas localmente para no ser utilizadas posteriormente en otro manifiesot*/
                sql = "update facturas set estadoFactura='2' where numeroFactura in" + listaFacturas;
                grabado = ini.insertarDatosLocalmente(sql);

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();
                //this.setListaFacturasDescargadas();

            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasPorManifiesto(boolean isContado) {
        boolean grabado = false;
        String sql = null;
        String listaFacturas;
        Connection con;//= null;
        Statement st = null;//= null;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.grabarFacturasPorManifiesto");
        con = this.ini.getConnRemota();
        try {
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                listaFacturas = "('";
                int adherencia = 0;

                for (CFacturasPorManifiesto fact : listaFacturasPorManifiesto) {
                    sql = "INSERT INTO facturaspormanifiesto(numeroManifiesto,numeroFactura,"
                            + "valorARecaudarFactura,pesoFactura,adherencia,zona,"
                            + "regional,agencia,activo,fechaIng,usuario,flag,"
                            + "despachado,vehiculoAsignado) VALUES('"
                            + fact.getNumeroManifiesto() + "','"
                            + fact.getNumeroFactura() + "','"
                            + fact.getValorARecaudarFactura() + "','"
                            + fact.getPesoFactura() + "','"
                            + adherencia++ + "','"
                            + "1','"
                            + "1','"
                            + "1','"
                            + "1',"
                            + "CURRENT_TIMESTAMP(),'"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                            + "1','0','"
                            + vehiculo + ") on duplicate key update flag='-1';";

                    st.addBatch(sql);
                    listaFacturas += fact.getNumeroFactura() + "','";
                }
                st.executeBatch();
                grabado = true;
                con.commit();
                st.close();
                // con.close();
                listaFacturas = listaFacturas.substring(0, listaFacturas.length() - 2).concat(");");

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();

                /*Actualiza las facturas localmente para no ser utilizadas posteriormente en otro manifiesot*/
                sql = "update facturas set isFree='0' where numeroFactura in" + listaFacturas;
                grabado = ini.insertarDatosLocalmente(sql);

            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasPorManifiesto() {
        boolean grabado = false;
        String sql = null;
        String listaFacturas;
        Connection con;//= null;
        Statement st = null;//= null;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.grabarFacturasPorManifiesto");
        con = this.ini.getConnRemota();

        try {
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                listaFacturas = "('";

                for (CFacturasPorManifiesto fact : listaFacturasPorManifiesto) {
                    sql = "INSERT INTO facturaspormanifiesto(numeroManifiesto,numeroFactura,"
                            + "valorARecaudarFactura,pesoFactura,adherencia,zona,"
                            + "regional,agencia,activo,fechaIng,usuario,flag,"
                            + "despachado,vehiculoAsignado) VALUES('"
                            + fact.getNumeroManifiesto() + "','"
                            + fact.getNumeroFactura() + "','"
                            + fact.getValorARecaudarFactura() + "','"
                            + fact.getPesoFactura() + "','"
                            + fact.getAdherencia() + "','"
                            + "1','"
                            + "1','"
                            + ini.getCodigoAgencia() + "','" // agencia
                            + "1',"
                            + "CURRENT_TIMESTAMP(),'"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                            + "1','0','"
                            + vehiculo + "') on duplicate key update flag='-1';";

                    st.addBatch(sql);
                    listaFacturas += fact.getNumeroFactura() + "','";
                    Thread.sleep(10);
                }
                st.executeBatch();
                grabado = true;
                con.commit();
                st.close();
                //con.close();

                listaFacturas = listaFacturas.substring(0, listaFacturas.length() - 2).concat(");");

                // new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();
                /*Actualiza las facturas localmente para no ser utilizadas posteriormente en otro manifiesot*/
                sql = "update facturas set isFree='0' where numeroFactura in" + listaFacturas;
                grabado = ini.insertarDatosLocalmente(sql);

            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @param cadena cadena que constiene la sentencia sql con los respectivos
     * datos a guardar en la BBDD
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasDescargadas() {
        boolean grabado = false;
        String sql = null;
        List<String> listaFacturas;
        Connection con;//= null;
        Statement st = null;//= null;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifeistosDeDistribucion.grabarFacturasDescargadas");
        con = this.ini.getConnRemota();

        try {
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                listaFacturas = new ArrayList<>();

                for (CFacturasPorManifiesto fact : listaFacturasDescargadas) {
                    String factura;
                    sql = "INSERT INTO facturasdescargadas(consecutivo,numeroManifiesto,adherencia,numeroFactura,"
                            + "valorRechazo,valorDescuento,valorRecaudado,movimientoFactura,motivoRechazo,activo,fechaIng,usuario,flag)VALUES('"
                            + fact.getConsecutivo() + "','"
                            + fact.getNumeroManifiesto() + "','"
                            + fact.getAdherencia() + "','"
                            + fact.getNumeroFactura() + "','"
                            + fact.getValorRechazo() + "','"
                            + fact.getValorDescuento() + "','";
                    if (fact.getPlazoDias() <= 1) {
                        sql += fact.getValorRecaudado() + "','";
                    } else {
                        sql += fact.getFpContado() + "','";
                    }
                    // + fact.getValorRecaudado() + "','"
                    sql += fact.getIdMovimientoFactura() + "','"
                            + fact.getCausalDeRechazo() + "','"
                            + "1',"
                            + "CURRENT_TIMESTAMP(),'"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                            + "1') on duplicate key update flag='-1';";

                    st.addBatch(sql);

                    factura = "update facturas set estadoFactura='" + fact.getIdMovimientoFactura()
                            + "' where numeroFactura ='" + fact.getNumeroFactura() + "';";

                    listaFacturas.add(factura);
                }
                st.executeBatch();
                grabado = true;
                con.commit();
                st.close();
                // con.close();

                /*Actualiza las facturas localmente para no ser utilizadas posteriormente en otro manifiesot*/
                grabado = ini.insertarDatosLocalmente(listaFacturas);

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();

            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public void sacarMinutaManifiesto(int seleccion) {
        String listadeproductos = null;
        if (this != null) {
            MinutasDeDistribucion sacarMinuta = new MinutasDeDistribucion(ini);
            String lista = "'";

            switch (this.getEstadoManifiesto()) {
                case 1:
                    if (seleccion == 0) {

                    }
                    if (seleccion == 1) {

                    }

                case 2:
                    /* manifiesto creado pero facturas no han sido grabadas */
                    for (CFacturasPorManifiesto facs : this.getListaFacturasPorManifiesto()) {
                        lista += facs.getNumeroFactura() + "','";

                    }
                    lista = lista.substring(0, lista.length() - 2);
                    if (seleccion == 0) {
                        sacarMinuta.minutaPorListaDeFacturas(lista);
                    }
                    if (seleccion == 1) {
                        ini.setListaDeProductosParaMinuta();
                        listadeproductos = ini.getListaDeProductosParaMinuta();
                        sacarMinuta.minutaPorListaDeFacturas(lista, listadeproductos);

                    }

                    //sacarMinuta.minutaPorManifiestoAbierto(manifiestoActual.getListaFacturasPorManifiesto());
                    break;
                case 3:
                    /* manifiesto creado con las facturas grabadas */
                    if (seleccion == 0) {

                        sacarMinuta.minutaPoManifiesto(this.getNumeroManifiesto());
                    }
                    if (seleccion == 1) {
                        ini.setListaDeProductosParaMinuta();
                        listadeproductos = ini.getListaDeProductosParaMinuta();
                        sacarMinuta.minutaPoManifiesto(this.getNumeroManifiesto(), listadeproductos);

                    }
                    break;
                case 4:
                    if (seleccion == 0) {

                        sacarMinuta.minutaPoManifiesto(this.getNumeroManifiesto());
                    }
                    if (seleccion == 1) {
                        ini.setListaDeProductosParaMinuta();
                        listadeproductos = ini.getListaDeProductosParaMinuta();
                        sacarMinuta.minutaPoManifiesto(this.getNumeroManifiesto(), listadeproductos);

                    }
                    break;

                case 5:
                    if (seleccion == 0) {

                    }
                    if (seleccion == 1) {

                    }
                    break;

                default:
                    break;

            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay manifiesto de ruta selecccionado ", "Error", 0);
        }
    }

}
