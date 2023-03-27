/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CFacturasDescargadas extends CFacturasPorManifiesto {

    Double valorRechazo;
    Double valorDescuento;
    Double valorRecaudado;
    int adherenciaDescargue;
    int idMovimientoFactura;
    int idMotivoRechazo;
    String usuario;
    int activo;

    ArrayList<CProductosPorFacturaDescargados> listaDEProductosPorFacturaDescargada;

    //ArrayList<CProductosPorFactura> productosPorFactura;
    // CFacturas factura;
    /**
     * Método que devuelve un entero que indica el orden en que se descargó la
     * factura
     *
     * @return el valor del rechazo
     */
    public int getAdherenciaDescargue() {
        return adherenciaDescargue;
    }

    /**
     * Método que asigna un entero que indica el orden en que se descargó la
     * factura
     *
     * @param adherenciaDescargue
     */
    public void setAdherenciaDescargue(int adherenciaDescargue) {
        this.adherenciaDescargue = adherenciaDescargue;
    }

    /**
     * Método que devuelve el valor de un rechzo de una facura descargada
     *
     * @return el valor del rechazo
     */
    /**
     * Método que asigna el valor de un rechzo de una facura descargada
     *
     * @param valorRechazo el valor del rechazo
     */
    public void setValorRechazo(Double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    /**
     * Método que asigna el valor de un descuento de una facura descargada
     *
     * @param valorDescuento
     */
    public void setValorDescuento(Double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    /**
     * Método que devuelve el valor recaudado de una facura descargada
     *
     * @return el valor recaudado de la factura
     */
    /**
     * Método que asigna el valor de un rechzo de una facura descargada
     *
     * @param valorRecaudado el valor recaudado a asignar
     */
    public void setValorRecaudado(Double valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    /**
     * Método que devuelve el movimiento que tuvo la facura descargada: 1 NINGUN
     * MOVIMIENTO 2 ENTREGA TOTAL 3 RECHAZO TOTAL 4 ENTREGA CON NOVEDAD 5
     * RECOGIDAD
     *
     * @return un entero con el código del movimiento final de la factura
     */
    public int getIdTipoMovimiento() {
        return idMovimientoFactura;
    }

    /**
     * Método que signa el movimiento que tuvo la facura descargada: 1 NINGUN
     * MOVIMIENTO 2 ENTREGA TOTAL 3 RECHAZO TOTAL 4 ENTREGA CON NOVEDAD 5
     * RECOGIDAD
     *
     * @param movimientoFactura asigna un entero con el código del movimiento
     * final de la factura
     */
    public void setIdTipoDeMovimiento(int movimientoFactura) {
        this.idMovimientoFactura = movimientoFactura;
    }

    /**
     * Método que devuelve un entero el cual es el código del rechazo de una
     * facura descargada
     *
     * @return entero con el código del rechazo
     */
    public int getCausalDeRechazo() {
        return idMotivoRechazo;
    }

    /**
     * Método que asigna un entero el cual es el código del rechazo de una
     * facura descargada
     *
     * @param motivoRechazo entero con el código del rechazo
     */
    public void setCausalDeRechazo(int motivoRechazo) {
        this.idMotivoRechazo = motivoRechazo;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    /**
     * Método que devuelve un array co la lista de los productos descargados de
     * la factura
     *
     * @return el array con los elementos de la factura descargada
     */
    public ArrayList<CProductosPorFacturaDescargados> getListaDEProductosPorFacturaDescargada() {
        return listaDEProductosPorFacturaDescargada;
    }

    /**
     * Método que asigna un array co la lista de los productos descargados de la
     * factura
     *
     * @param itemsPorFactura lista de los elementos de la factura
     */
    public void setListaDEProductosPorFacturaDescargada(ArrayList<CProductosPorFacturaDescargados> itemsPorFactura) {
        this.listaDEProductosPorFacturaDescargada = itemsPorFactura;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Método constructor vacío
     *
     */
    public CFacturasDescargadas() {

    }

    /**
     * Método constructor con un parámetro
     *
     * @param ini archivo de configurción del sistema
     * @throws java.lang.Exception en el caso de que ini sea null
     */
    public CFacturasDescargadas(Inicio ini) throws Exception {
        super(ini);

    }

    /**
     * Método constructor con un parámetro
     *
     * @param ini archivo de configurción del sistema
     * @param manifiesto es el manifiesto al cual se encuentra asignado la
     * factura
     * @throws java.lang.Exception en el caso de que ini sea null
     */
    public CFacturasDescargadas(Inicio ini, CManifiestosDeDistribucion manifiesto) throws Exception {
        super(ini);
        Connection con;
        Statement st;
        ResultSet rst;
        //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         con = this.ini.getConnLocal();
        String sql = "SELECT  facturaspormanifiesto.consecutivo, facturaspormanifiesto.numeroManifiesto, facturaspormanifiesto.numeroFactura, facturaspormanifiesto.adherencia,"
                + "facturaspormanifiesto.zona, facturaspormanifiesto.regional, facturaspormanifiesto.agencia, facturaspormanifiesto.activo,"
                + " facturasdescargadas.valorRechazo,facturasdescargadas.valorDescuento, facturasdescargadas.valorRecaudado, facturasdescargadas.movimientoFactura, "
                + "facturasdescargadas.motivoRechazo "
                + "FROM facturaspormanifiesto,facturasdescargadas "
                + "WHERE "
                + "facturaspormanifiesto.consecutivo=facturasdescargadas.consecutivo AND "
                + "facturaspormanifiesto.consecutivo='" + consecutivo + "' '";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.consecutivo = rst.getInt("consecutivo");
                    this.numeroManifiesto = rst.getString("numeroManifiesto");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.adherencia = rst.getInt("adherencia");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.activo = rst.getInt("activo");
                    this.valorRechazo = rst.getDouble("valorRechazo");
                    this.valorDescuento = rst.getDouble("valorDescuento");
                    this.valorRecaudado = rst.getDouble("valorRecaudado");
                    this.idMovimientoFactura = rst.getInt("movimientoFactura");
                    this.idMotivoRechazo = rst.getInt("motivoRechazo");
                    this.usuario = rst.getString("usuario");

                } else {
                    this.consecutivo = 0;
                    this.numeroManifiesto = null;
                    this.numeroFactura = null;
                    this.adherencia = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.activo = 0;
                    this.valorRechazo = 0.0;
                    this.valorDescuento = 0.0;
                    this.valorRecaudado = 0.0;
                    this.idMovimientoFactura = 0;
                    this.idMotivoRechazo = 0;

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CFacturasDescargadas(Inicio ini, int consecutivo) sql " + ex.getMessage());
            }
        }

    }

    /**
     * Método constructor con un parámetro
     *
     * @param ini archivo de configurción del sistema
     * @param consecutivo corresponde al consecutivo de facturas por manifiestos
     * para crear el objeto
     * @throws java.lang.Exception en el caso de que ini sea null
     */
    public CFacturasDescargadas(Inicio ini, int consecutivo) throws Exception {
        super(ini);
        Connection con;
        Statement st;
        ResultSet rst;
        //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         con = this.ini.getConnLocal();
        String sql = "SELECT  facturaspormanifiesto.consecutivo, facturaspormanifiesto.numeroManifiesto, facturaspormanifiesto.numeroFactura,"
                + "facturaspormanifiesto.adherencia, facturaspormanifiesto.zona,facturaspormanifiesto.regional, facturaspormanifiesto.agencia,"
                + "facturaspormanifiesto.activo,"
                + "facturasdescargadas. valorRechazo, facturasdescargadas.valorDescuento, facturasdescargadas.valorRecaudado,"
                + "facturasdescargadas.movimientoFactura, facturasdescargadas.motivoRechazo "
                + "FROM facturaspormanifiesto,facturasdescargadas. "
                + "WHERE "
                + " facturaspormanifiesto.consecutivo=facturasdescargadas.consecutivo AND "
                + "facturaspormanifiesto.consecutivo='" + consecutivo + "' ; ";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.consecutivo = rst.getInt("consecutivo");
                    this.numeroManifiesto = rst.getString("numeroManifiesto");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.adherencia = rst.getInt("adherencia");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.activo = rst.getInt("activo");
                    this.valorRechazo = rst.getDouble("valorRechazo");
                    this.valorDescuento = rst.getDouble("valorDescuento");
                    this.valorRecaudado = rst.getDouble("valorRecaudado");
                    this.idMovimientoFactura = rst.getInt("movimientoFactura");
                    this.idMotivoRechazo = rst.getInt("motivoRechazo");
                    this.usuario = rst.getString("usuario");

                } else {
                    this.consecutivo = 0;
                    this.numeroManifiesto = null;
                    this.numeroFactura = null;
                    this.adherencia = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.activo = 0;
                    this.valorRechazo = 0.0;
                    this.valorDescuento = 0.0;
                    this.valorRecaudado = 0.0;
                    this.idMovimientoFactura = 0;
                    this.idMotivoRechazo = 0;

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CFacturasDescargadas(Inicio ini, int consecutivo) sql " + ex.getMessage());
            }
        }

    }

    /**
     * Método constructor con un parámetro
     *
     * @param ini archivo de configurción del sistema
     * @param numeroFactura equivale al numero de la factura
     * @throws java.lang.Exception en el caso de que ini sea null
     */
    public CFacturasDescargadas(Inicio ini, String numeroFactura) throws Exception {
        super(ini);
        Connection con;
        Statement st;
        ResultSet rst;
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

         con = this.ini.getConnRemota();
        String sql = "SELECT  * "
                + "FROM facturasdescargadas  "
                + "WHERE "
                + " facturasdescargadas.numeroFactura='" + numeroFactura + "' "
                + "order by fechaIng desc limit 1 ;";

        getDatosFacturaDescargada(con, sql);

    }

    private void getDatosFacturaDescargada(Connection con, String sql) {
        Statement st;
        ResultSet rst;
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.consecutivo = rst.getInt("consecutivo");
                    this.numeroManifiesto = rst.getString("numeroManifiesto");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.activo = rst.getInt("activo");
                    this.valorRechazo = rst.getDouble("valorRechazo");
                    this.valorDescuento = rst.getDouble("valorDescuento");
                    this.valorRecaudado = rst.getDouble("valorRecaudado");
                    this.idMovimientoFactura = rst.getInt("movimientoFactura");
                    this.idMotivoRechazo = rst.getInt("motivoRechazo");
                    this.usuario = rst.getString("usuario");

                } else {
                    this.consecutivo = 0;
                    this.numeroManifiesto = null;
                    this.numeroFactura = null;
                    this.activo = 0;
                    this.valorRechazo = 0.0;
                    this.valorDescuento = 0.0;
                    this.valorRecaudado = 0.0;
                    this.idMovimientoFactura = 0;
                    this.idMotivoRechazo = 0;

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CFacturasDescargadas(Inicio ini, int consecutivo) sql " + ex.getMessage());
            }
        }
    }

    /**
     * Método constructor con un parámetro
     *
     * @param ini archivo de configurción del sistema
     * @param numeroManifiesto
     * @param numeroFactura
     * @throws java.lang.Exception en el caso de que ini sea null
     */
    public CFacturasDescargadas(Inicio ini, int numeroManifiesto, String numeroFactura) throws Exception {
        super(ini);
        Connection con;
        Statement st;
        ResultSet rst;
       // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         con = this.ini.getConnLocal();

        String sql = "SELECT  facturaspormanifiesto.consecutivo, facturaspormanifiesto.numeroManifiesto, facturaspormanifiesto.numeroFactura, facturaspormanifiesto.adherencia,"
                + "facturaspormanifiesto.zona, facturaspormanifiesto.regional, facturaspormanifiesto.agencia, facturaspormanifiesto.activo,"
                + " facturasdescargadas.valorRechazo,"
                + "facturasdescargadas.valorDescuento, facturasdescargadas.valorRecaudado, facturasdescargadas.movimientoFactura, "
                + "facturasdescargadas.motivoRechazo "
                + "FROM facturaspormanifiesto,facturasdescargadas  "
                + "WHERE "
                + " facturaspormanifiesto.consecutivo=facturasdescargadas.consecutivo AND "
                + "facturaspormanifiesto.numeroManifiesto='" + numeroManifiesto + "' AND "
                + "facturaspormanifiesto.numeroFactura='" + numeroFactura + "';";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.consecutivo = rst.getInt("consecutivo");
                    this.numeroManifiesto = rst.getString("numeroManifiesto");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.adherencia = rst.getInt("adherencia");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.activo = rst.getInt("activo");
                    this.valorRechazo = rst.getDouble("valorRechazo");
                    this.valorDescuento = rst.getDouble("valorDescuento");
                    this.valorRecaudado = rst.getDouble("valorRecaudado");
                    this.idMovimientoFactura = rst.getInt("movimientoFactura");
                    this.idMotivoRechazo = rst.getInt("motivoRechazo");
                    this.usuario = rst.getString("usuario");

                } else {
                    this.consecutivo = 0;
                    this.numeroManifiesto = null;
                    this.numeroFactura = null;
                    this.adherencia = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.activo = 0;
                    this.valorRechazo = 0.0;
                    this.valorDescuento = 0.0;
                    this.valorRecaudado = 0.0;
                    this.idMovimientoFactura = 0;
                    this.idMotivoRechazo = 0;

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CFacturasDescargadas(Inicio ini, int numeroManifiesto, String numeroFactura)  sql " + ex.getMessage());
            }
        }
    }

    public ArrayList<CFacturas> facturasPendientesPorDescargar_(int manifiesto) {
        ArrayList<CFacturas> arrfacturasPendientes = new ArrayList();

        String sql = "SELECT facturaspormanifiesto.numeroFactura "
                + "FROM facturaspormanifiesto,facturasdescargadas. "
                + "WHERE "
                + " facturaspormanifiesto.consecutivo=facturasdescargadas.consecutivo AND "
                + " facturaspormanifiesto.numeroFactura NOT IN ("
                + "SELECT facturasdescargadas.numeroFactura "
                + "FROM facturasdescargadas  "
                + "WHERE "
                + "facturasdescargadas.numeroManifiesto='" + manifiesto + "' AND "
                + "facturasdescargadas.activo.activo=1 )  and "
                + "facturaspormanifiesto.numeroManifiesto=' " + manifiesto + "';";

        return arrfacturasPendientes;

    }

    /**
     * Método que permite guardar los datos en la BBDD
     *
     * @return devuelve true sí los datos son grabados correctamente en la BBDD,
     * y devuelv falso en caso de un error ó de un problema de conexión
     */
    public boolean grabarFacturasDescargadas() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO  facturasdescargadas(`consecutivo`,`numeroManifiesto`,`numeroFactura`,`valorRechazo`,`valorDescuento`,`valorRecaudado`,"
                    + "`movimientoFactura`,`motivoRechazo`,`activo`,`usuario`,`flag`) "
                    + "VALUES ('"
                    + this.consecutivo + "','"
                    + this.numeroManifiesto + "','"
                    + this.numeroFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorRecaudado + "','"
                    + this.idMovimientoFactura + "','"
                    + this.idMotivoRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + " `consecutivo`='" + this.consecutivo + "',"
                    + "`numeroManifiesto`='" + this.numeroManifiesto + "',"
                    + "`numeroFactura`='" + this.numeroFactura + "',"
                    + "`valorRechazo`='" + this.valorRechazo + "',"
                    + "`valorDescuento`='" + this.valorDescuento + "',"
                    + "`valorRecaudado`='" + this.valorRecaudado + "',"
                    + "`movimientoFactura`='" + this.idMovimientoFactura + "',"
                    + "`motivoRechazo`='" + this.idMotivoRechazo + "',"
                    + "`activo`='" + this.activo + "',"
                    + "`usuario`='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',"
                    + "`flag`=-1";

            ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            System.out.println("Error en grabarFacturasDescargadas sql " + ex.getMessage());
            Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean grabarFacturasDescargadas(String cadena) {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO  facturasdescargadas(`consecutivo`,`numeroManifiesto`,`numeroFactura`,`valorRechazo`,`valorDescuento`,`valorRecaudado`,"
                    + "`movimientoFactura`,`motivoRechazo`,`activo`,`usuario`,`flag`) "
                    + "VALUES ('"
                    + this.consecutivo + "','"
                    + this.numeroManifiesto + "','"
                    + this.numeroFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorRecaudado + "','"
                    + this.idMovimientoFactura + "','"
                    + this.idMotivoRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + " `consecutivo`='" + this.consecutivo + "',"
                    + "`numeroManifiesto`='" + this.numeroManifiesto + "',"
                    + "`numeroFactura`='" + this.numeroFactura + "',"
                    + "`valorRechazo`='" + this.valorRechazo + "',"
                    + "`valorDescuento`='" + this.valorDescuento + "',"
                    + "`valorRecaudado`='" + this.valorRecaudado + "',"
                    + "`movimientoFactura`='" + this.idMovimientoFactura + "',"
                    + "`motivoRechazo`='" + this.idMotivoRechazo + "',"
                    + "`activo`='" + this.activo + "',"
                    + "`usuario`='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',"
                    + "`flag`=-1;";

            ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            System.out.println("Error en grabarFacturasDescargadas sql " + ex.getMessage());
            Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarFacturasDescargadas() {
        boolean grabado = false;

        return grabado;
    }

    public int cantidadFacturasDescargadas() {
        int valor = 0;

        Connection con;
        Statement st;
        ResultSet rst;
       // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
       con = this.ini.getConnLocal();
       
        String sql = "SELECT COUNT(numeroFactura)"
                + "FROM facturasdescargadas "
                + "WHERE "
                + "numeroManifiesto=" + numeroManifiesto + ";";
        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    valor = rst.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return valor;
    }

    public void borrarFacturaDescargada(int manifiesto, String factura) {

        String sql = "DELETE "
                + "FROM facturasdescargadas "
                + "WHERE "
                + "numeroManifiesto='" + manifiesto + "' AND "
                + "numeroFactura='" + factura + "';";
        ini.insertarDatosRemotamente(sql);
    }

//    /**
//     * Método que elimina de la BBDD una factura que ha sido descargada
//     *
//     */
//    public void borrarFacturaDescargada() {
//
//        String sql = "DELETE "
//                + "FROM facturasdescargadas "
//                + "WHERE "
//                + "numeroManifiesto='" + numeroManifiesto + "' AND "
//                + "numeroFactura='" + numeroFactura + "' ;";
//        ini.insertarDatosRemotamente(sql);
//    }
    public boolean grabarTodasLasFacturasDescargadas() {
        boolean grabado = false;

        return grabado;
    }

    /**
     * Método actualiza una factura dando al campo isFree= 1 par poder con el
     * fin de poder utilizar el documento en un nuevo movimiento de salida a
     * distribución.
     *
     * @return devueleve una cabena con la sentencia SQL de actualizar, para
     * actualizar la BBDD remota
     */
    public String liberFacturaDescargada(String numeroFactura) {

        String sql = "";
        try {
            sql = "UPDATE facturascamdun "
                    + "set isFree=1 "
                    + "WHERE "
                    + "numeroFactura='" + numeroFactura + "' ;";
            ini.insertarDatosLocalmente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }

    /**
     * Método actualiza una factura dando al campo isFree= 1 par poder con el
     * fin de poder utilizar el documento en un nuevo movimiento de salida a
     * distribución.
     *
     * @return devueleve una cabena con la sentencia SQL de actualizar, para
     * actualizar la BBDD remota
     */
    public String liberFacturaDescargada() {

        String sql = "";
        try {
            sql = "UPDATE facturascamdun "
                    + "set isFree=1 "
                    + "WHERE "
                    + "numeroFactura='" + numeroFactura + "' ;";
            ini.insertarDatosLocalmente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }

    /**
     * Método que devuelve una cadena con la lista de todos los campos de la
     * tabla.
     *
     * @return una cadena con la lista de los campos
     */
    //@Override
    public String getCadenaConLosCampos() {
        String cadena;

        cadena = this.consecutivo + ","
                + this.numeroManifiesto + ","
                + this.adherenciaDescargue + ","
                + this.numeroFactura + ","
                + this.valorRechazo + ","
                + this.valorDescuento + ","
                + this.valorRecaudado + ","
                + this.idMovimientoFactura + ","
                + this.idMotivoRechazo + ","
                + this.activo;
        return cadena;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para insertar Datos en
     * la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    @Override
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {

            sql = "INSERT INTO  facturasdescargadas(`consecutivo`,`numeroManifiesto`,`adherencia`,`numeroFactura`,`valorRechazo`,`valorDescuento`,`valorRecaudado`,"
                    + "`movimientoFactura`,`motivoRechazo`,`activo`,`usuario`,`flag`) "
                    + "VALUES ('"
                    + this.consecutivo + "','"
                    + this.numeroManifiesto + "','"
                    + this.adherenciaDescargue + "','"
                    + this.numeroFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorRecaudado + "','"
                    + this.idMovimientoFactura + "','"
                    + this.idMotivoRechazo + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + " `consecutivo`='" + this.consecutivo + "',"
                    + "`numeroManifiesto`='" + this.numeroManifiesto + "',"
                    + "`adherencia`='" + this.adherenciaDescargue + "',"
                    + "`numeroFactura`='" + this.numeroFactura + "',"
                    + "`valorRechazo`='" + this.valorRechazo + "',"
                    + "`valorDescuento`='" + this.valorDescuento + "',"
                    + "`valorRecaudado`='" + this.valorRecaudado + "',"
                    + "`movimientoFactura`='" + this.idMovimientoFactura + "',"
                    + "`motivoRechazo`='" + this.idMotivoRechazo + "',"
                    + "`activo`='" + this.activo + "',"
                    + "`usuario`='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',"
                    + "`flag`=-1; ";

            /* si la factura es un rechazo total, se libera para tener la
             posibilidad de sacarla a ruta nuevamente 
            if (this.idMovimientoFactura == 3) {
                sql += "update facturascamdun set isFree=1 where numeroFactura='" + this.numeroFactura + "';";
            }
             */
        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    /**
     * Método que elimina los productos de una facturas descargadas
     *
     * @param archivo es el archivo donde se almacenan localmente los diferentes
     * productos de la factura descargada.
     */
    public boolean borrarProductosDeLaFacturaDescargada(File archivo) {
        boolean borrado = false;
        ArrayList<String> lineaAux = new ArrayList<>();
        FileReader fr;
        BufferedReader br;
        PrintWriter escrive;
        String[] cadena;

        try {

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            // Se eliminan las lineas que contienen los productos de la facturas desacrgada 
            while ((linea = br.readLine()) != null) {
                cadena = linea.split(",");

                if (!cadena[0].equals("" + this.consecutivo)) {
                    lineaAux.add(linea);
                } else {
                    System.out.println("Linea  borrada " + cadena[0]);
                }

            }
            borrado = true;

            // Se guardan en el archivo los productos descargados
            FileWriter fichero = new FileWriter(archivo);
            escrive = new PrintWriter(fichero);
            for (String obj : lineaAux) {
                cadena = obj.split(",");
                escrive.println(obj);
                System.out.println("linea guardado" + cadena[0]);
            }

            borrado = true;
            fichero.close();
            // this.listaDEProductosPorFacturaDescargada=null;

            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
            borrado = false;
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
            borrado = false;
        }

        return borrado;
    }

    /**
     * Método que elimina la factura seleccionada y los productos de la
     * respectiva factura descargadas
     *
     * @param manifiesto
     * @return
     */
    public boolean borrarFacturaDescargada(CManifiestosDeDistribucion manifiesto) {
        boolean borrado = false;

        // Se elimina la factura del archivo de facturas descargadas
        String rutaDeArchivoFacturaDescargada = manifiesto.getRutaArchivoDescargueFacturas();
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturaDescargada);

        /* Aca se borra la linea que contiene el numero de la factura*/
        if (archivo.borrarLinea(this.getCadenaConLosCampos())) {

            /* aca se borran los productos de la factura*/
            borrado = borrarProductosDeLaFacturaDescargada(new File(manifiesto.getRutaArchivoDescargueporductosPorFactura()));
        }

        return borrado;
    }

    /**
     * Método que elimina la factura seleccionada y los productos de la
     * respectiva factura descargadas
     *
     * @param manifiesto
     * @return
     */
    public boolean borrarFacturaDescargada(int fila, CManifiestosDeDistribucion manifiesto) {
        boolean borrado = false;

        // Se elimina la factura del archivo de facturas descargadas
        String rutaDeArchivoFacturaDescargada = manifiesto.getRutaArchivoDescargueFacturas();
       ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturaDescargada);

        /* Aca se borra la linea que contiene el numero de la factura*/
        archivo.borrarLinea(fila);

        File fichero = new File(manifiesto.getRutaArchivoDescargueporductosPorFactura());

        if (fichero.exists()) {
            /* aca se borran los productos de la factura*/
            borrado = borrarProductosDeLaFacturaDescargada(fichero);
        }else{
            /*Si el fichero no existe devuelve verdadero aduciendo a que se efectuo el
            borrado de los productos que fueron devueltos*/
            borrado=true;
        }

        return borrado;
    }

    public boolean borrarFacturaDescargada(String numeroFactura, CManifiestosDeDistribucion manifiesto) {
        boolean borrado = false;
        // Se elimina la factura del archivo de facturas descargadas
        String rutaDeArchivoFacturaDescargada = manifiesto.getRutaArchivoDescargueFacturas();
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturaDescargada);

        if (archivo.borrarLinea(this.getCadenaConLosCampos())) {
            borrado = borrarProductosDeLaFacturaDescargada(new File(manifiesto.getRutaArchivoDescargueporductosPorFactura()));
        }

        return borrado;
    }

    public String actualizarEstadoFactura() {
        String cadena = null;
        cadena = "update facturascamdun set estadoFactura='" + this.idMovimientoFactura + "' where numeroFactura='" + this.numeroFactura + "';";

        return cadena;

    }

    public String actualizarEstadoFactura(int movimientoFactura, String numeroFactura) {
        String cadena = null;
        
         cadena = "update facturascamdun set estadoFactura='" + movimientoFactura + "' where numeroFactura='" + numeroFactura + "';";
        
      /*  switch(movimientoFactura){
            case 6: // RE ENVIOS
                 cadena = "update facturascamdun set estadoFactura='" + movimientoFactura + "' "
                         + "where numeroFactura='" + numeroFactura + "';";
                break;
            case 7: // NO VISITADOS
                 cadena = "update facturascamdun set estadoFactura='" + movimientoFactura + "' "
                         + "where numeroFactura='" + numeroFactura + "';";
                break;
                
            default:
                 cadena = "update facturascamdun set estadoFactura='" + movimientoFactura + "' where numeroFactura='" + numeroFactura + "';";
                break;
        }*/
       

        return cadena;

    }
}
