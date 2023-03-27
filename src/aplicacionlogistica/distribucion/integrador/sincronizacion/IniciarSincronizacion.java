/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador.sincronizacion;

import aplicacionlogistica.configuracion.CConexiones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lelopez
 */
public class IniciarSincronizacion {

    boolean ejecutado = true;

    String bdFuente = null;
    String urlFuente = null;
    String serverFuente = null;
    String puertoFuente = null;
    String usuarioBDFuente = null;
    String claveBDFuente = null;

    String bdDestino = null;
    String urlDestino = null;
    String serverDestino = null;
    String puertoDestino = null;
    String usuarioBDDestino = null;
    String claveBDdestino = null;

    String cadenaFuente = null;
    String cadenaDestino = null;

    ArrayList<String> insertClientes = null;
    ArrayList<String> insertFacturas = null;
    ArrayList<String> insertProductos = null;
    ArrayList<String> insertProductosPorFactura = null;

    String listaDeFacturas = "('";
    String listaDeClientes = "";
    String listaDeProductos = "";
    String listaDeProductosPorFactura = "";
    String tabla;

    boolean cancelar = false;

    public boolean isEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(boolean ejecutado) {
        this.ejecutado = ejecutado;
    }

    public void setBdFuente(String bdFuente) {
        this.bdFuente = bdFuente;
    }

    public void setUrlFuente(String urlFuente) {
        this.urlFuente = urlFuente;
    }

    public void setServerFuente(String serverFuente) {
        this.serverFuente = serverFuente;
    }

    public void setPuertoFuente(String puertoFuente) {
        this.puertoFuente = puertoFuente;
    }

    public void setUsuarioBDFuente(String usuarioBDFuente) {
        this.usuarioBDFuente = usuarioBDFuente;
    }

    public void setClaveBDFuente(String claveBDFuente) {
        this.claveBDFuente = claveBDFuente;
    }

    public void setCadenaLocal(String cadenaLocal) {
        this.cadenaFuente = cadenaLocal;
    }

    public void setCadenaRemota(String cadenaRemota) {
        this.cadenaDestino = cadenaRemota;
    }

    public String getBdFuente() {
        return bdFuente;
    }

    public String getUrlFuente() {
        return urlFuente;
    }

    public String getServerFuente() {
        return serverFuente;
    }

    public String getPuertoFuente() {
        return puertoFuente;
    }

    public String getUsuarioBDFuente() {
        return usuarioBDFuente;
    }

    public String getClaveBDFuente() {
        return claveBDFuente;
    }

    public String getCadenaLocal() {
        return cadenaFuente;
    }

    public String getCadenaRemota() {
        return cadenaDestino;
    }

    public ArrayList<String> getInsertClientes() {
        return insertClientes;
    }

    public ArrayList<String> getInsertFacturas() {
        return insertFacturas;
    }

    public ArrayList<String> getInsertProductos() {
        return insertProductos;
    }

    public ArrayList<String> getInsertProductosPorFactura() {
        return insertProductosPorFactura;
    }

    public String getBdDestino() {
        return bdDestino;
    }

    public void setBdDestino(String bdDestino) {
        this.bdDestino = bdDestino;
    }

    public String getUrlDestino() {
        return urlDestino;
    }

    public void setUrlDestino(String urlDestino) {
        this.urlDestino = urlDestino;
    }

    public String getServerDestino() {
        return serverDestino;
    }

    public void setServerDestino(String serverDestino) {
        this.serverDestino = serverDestino;
    }

    public String getPuertoDestino() {
        return puertoDestino;
    }

    public void setPuertoDestino(String puertoDestino) {
        this.puertoDestino = puertoDestino;
    }

    public String getUsuarioBDDestino() {
        return usuarioBDDestino;
    }

    public void setUsuarioBDDestino(String usuarioBDDestino) {
        this.usuarioBDDestino = usuarioBDDestino;
    }

    public String getClaveBDdestino() {
        return claveBDdestino;
    }

    public void setClaveBDdestino(String claveBDdestino) {
        this.claveBDdestino = claveBDdestino;
    }

    public String getCadenaFuente() {
        return cadenaFuente;
    }

    public void setCadenaFuente(String cadenaFuente) {
        this.cadenaFuente = cadenaFuente;
    }

    public String getCadenaDestino() {
        return cadenaDestino;
    }

    public void setCadenaDestino(String cadenaDestino) {
        this.cadenaDestino = cadenaDestino;
    }

    public String getListaDeFacturas() {
        return listaDeFacturas;
    }

    public void setListaDeFacturas(String listaDeFacturas) {
        this.listaDeFacturas = listaDeFacturas;
    }

    public String getListaDeClientes() {
        return listaDeClientes;
    }

    public void setListaDeClientes(String listaDeClientes) {
        this.listaDeClientes = listaDeClientes;
    }

    public String getListaDeProductosPorFactura() {
        return listaDeProductosPorFactura;
    }

    public void setListaDeProductosPorFactura(String listaDeProductosPorFactura) {
        this.listaDeProductosPorFactura = listaDeProductosPorFactura;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * Método que inicia el proceso de seleccion de los registros que se van a
     * los ingresar. a la BBDD local
     *
     * @return true si hay registros para actualizar de lo contrario devuelve
     * false
     */
    
    public boolean verificarRegistros() {

        boolean verificado = false;
        System.out.println("Inicia proceso de verificacion");
     
        String sql = "select * from facturascamdun "
                + "where "
                + "trasmitido=0";
        //+ "fechaDeVenta>='2019-12-24'";

        listaDeClientes = "select DISTINCT codigoInterno, nitCliente, nombreEstablecimiento, "
                + "nombreDeCliente, direccion, barrio, ciudad, clasificacion, celularCliente, "
                + "emailCliente, fechaDeIngresoCliente, latitud, longitud, canalDeVenta, ruta, "
                + "frecuencia, zona, regional, agencia, porcentajeDescuento, activo, fechaIng, usuario, flag "
                + "from clientescamdun where codigointerno in ('";

        listaDeProductos = "SELECT DISTINCT  codigoProducto,descripcionProducto,linea,valorUnitario,"
                + "valorUnitarioConIva,isFree,pesoProducto,largoProducto,anchoProducto,altoProducto,"
                + "activo,fechaIng,usuario,flag FROM productoscamdun where codigoProducto in ";

        listaDeProductosPorFactura = "select * from productosporfactura where factura in ";

        /*Se actualiza el peso del detalle de la factura fuente*/
        System.out.println("Inicia proceso de peso de productos");
        
        verificado = actualizarPesoProductos(true);
        if (!verificado) {
           JOptionPane.showMessageDialog(null, "Error al actualizar el peso de productos ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /*Se actualiza el peso de la factura fuente*/
        System.out.println("Inicia proceso de peso de facturas");
        verificado = actualizarPesoFacturas(true);
        if (!verificado) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el peso de la factura ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /*Se actualiza el canal de venta  de la factura fuente*/
        System.out.println("Inicia proceso de canal de ventas");
        verificado = actualizarCanaleVentaClientes(true);
        if (!verificado) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el Canal de venta de la factura ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /*Se actualiza la ciudad  de la factura fuente*/
        System.out.println("Inicia proceso actualizar ciudad de la factura");
        verificado = actualizarCiudadFactura(true);
        if (!verificado) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la ciudad de la factura ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date fecha = null;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;

        con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                insertFacturas = new ArrayList<>();
                System.out.println("Inicio de proceso " + new java.util.Date());

                rst.last();
                int x = rst.getRow();
                rst.beforeFirst();

                /*Se  verifican si hay registros sin trasmision */
                if (x > 0) {
                    x = 0;
                    while (rst.next()) {

                        sql = "INSERT INTO facturascamdun (numeroFactura,fechaDeVenta,"
                                + "cliente,direccion,"
                                + "barrio,ciudad,telefono,vendedor,formaDePago,canal,"
                                + "valorFacturaSinIva,valorIvaFactura,valorTotalFactura,valorRechazo,"
                                + "valorDescuento,valorTotalRecaudado,formato,zona,regional,"
                                + "agencia,isFree,estadoFactura,activo,usuario,flag,"
                                + "pesofactura,ruta,trasmitido)  VALUES ('"
                                + rst.getString("numeroFactura") + "','"
                                + rst.getDate("fechaDeVenta") + "','"
                                + rst.getString("cliente") + "','"
                                + rst.getString("direccion") + "','"
                                + rst.getString("barrio") + "','"
                                + rst.getString("ciudad") + "','"
                                + rst.getString("telefono") + "','"
                                + rst.getString("vendedor") + "','"
                                + rst.getInt("formaDePago") + "','"
                                + rst.getInt("canal") + "','"
                                + rst.getDouble("valorFacturaSinIva") + "','"
                                + rst.getDouble("valorIvaFactura") + "','"
                                + rst.getDouble("valorTotalFactura") + "','"
                                + rst.getDouble("valorRechazo") + "','"
                                + rst.getDouble("valorDescuento") + "','"
                                + rst.getDouble("valorTotalRecaudado") + "',"
                                + rst.getString("formato") + ",'"
                                + rst.getInt("zona") + "','"
                                + rst.getInt("regional") + "','"
                                + rst.getInt("agencia") + "','"
                                + rst.getInt("isFree") + "','"
                                + rst.getInt("estadoFactura") + "','"
                                + rst.getInt("activo") + "','"
                                + rst.getString("usuario") + "','"
                                + rst.getInt("flag") + "','"
                                + rst.getDouble("pesofactura") + "','"
                                + rst.getString("ruta") + "',"
                                + "1)"
                                + " ON DUPLICATE KEY UPDATE  flag=1; \n";

                        insertFacturas.add(sql);
                        listaDeFacturas += rst.getString("numeroFactura") + "','";
                        listaDeClientes += rst.getString("cliente") + "','";

                        x++;

                    }

                    con.close();
                    st.close();
                    rst.close();

                    listaDeFacturas = listaDeFacturas.substring(0, listaDeFacturas.length() - 2) + ");";
                    listaDeClientes = listaDeClientes.substring(0, listaDeClientes.length() - 2) + ");";
                    listaDeProductos = listaDeProductos + this.getListaDeProductos();
                    listaDeProductosPorFactura += listaDeFacturas;

                    System.out.println("Fin de proceso " + new java.util.Date() + " con " + x + " Registros");
                    String y = listaDeFacturas;
                    verificado = true;
                } else {
                    System.out.println("No hay registros para trasmitir \n" + "Trasmision Finalizada");
                    verificado = false;
                }

            } catch (SQLException ex) {
                try {
                    ejecutado = false;
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en consulta sql " + ex + "(sql=" + sql + ")");
                    con.close();
                    st.close();
                    rst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }

        }

        return verificado;
    }

    /**
     * Método que fabrica el grupo de sentencias SQL con las cuales se van a
     * insertar los productos a la BBDD local
     *
     * @param a parametro booleano diferenciador
     */
    @SuppressWarnings("null")
    private String getListaDeProductos() {
        String listaDeproductos = "('";
        String sql = "select codigoProducto from productosporfactura where factura in" + listaDeFacturas;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;

        con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                int x = 0;
                while (rst.next()) {

                    listaDeproductos += rst.getString("codigoProducto") + "','";
                    x++;

                }
                listaDeproductos = listaDeproductos.substring(0, listaDeproductos.length() - 2) + ");";

                con.close();
                st.close();
                rst.close();

            } catch (SQLException ex) {
                try {
                    ejecutado = false;
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en consulta sql " + ex + "(sql=" + sql + ")");
                    con.close();
                    st.close();
                    rst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
        return listaDeproductos;
    }

    /**
     * Método que fabrica el grupo de sentencias SQL con las cuales se van a
     * insertar los clientes a la BBDD local
     *
     * @param a parametro booleano diferenciador
     */
    @SuppressWarnings("null")
    public void getInsertClientes(boolean a) {

        //Date fecha = null;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        insertClientes = new ArrayList<>();
        con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(listaDeClientes);

                int x = 0;
                while (rst.next()) {

                    String sql = "INSERT INTO clientescamdun(codigoInterno,nitCliente,"
                            + "nombreEstablecimiento,nombreDeCliente,direccion,barrio,ciudad,"
                            + "clasificacion,celularCliente,emailCliente,"
                            + "fechaDeIngresoCliente,latitud,longitud,canalDeVenta,ruta,"
                            + "frecuencia,zona,regional,agencia,porcentajeDescuento,"
                            + "activo,usuario) VALUES('"
                            + rst.getString("codigoInterno") + "','"
                            + rst.getString("nitCliente") + "','"
                            + rst.getString("nombreEstablecimiento") + "','"
                            + rst.getString("nombreDeCliente") + "','"
                            + rst.getString("direccion") + "','"
                            + rst.getString("barrio") + "','"
                            + rst.getString("ciudad") + "','"
                            + rst.getString("clasificacion") + "','"
                            + rst.getString("celularCliente") + "','"
                            + rst.getString("emailCliente") + "',"
                            + "CURRENT_DATE() ,'"
                            //+ rst.getDate("fechaDeIngresoCliente") + ",'"
                            + rst.getDouble("latitud") + "','"
                            + rst.getDouble("longitud") + "','"
                            + rst.getInt("canalDeVenta") + "','"
                            + rst.getInt("ruta") + "','"
                            + rst.getInt("frecuencia") + "','"
                            + rst.getInt("zona") + "','"
                            + rst.getInt("regional") + "','"
                            + rst.getInt("agencia") + "','"
                            + rst.getDouble("porcentajeDescuento") + "','"
                            + rst.getInt("activo") + "','"
                            + rst.getString("usuario") + "') "
                            + "ON DUPLICATE KEY UPDATE  flag=1; \n";
                    insertClientes.add(sql);

                }
                con.close();
                st.close();
                rst.close();

            } catch (SQLException ex) {
                try {
                    ejecutado = false;
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    st.close();
                    rst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    /**
     * Método que fabrica el grupo de sentencias SQL con las cuales se van a
     * insertar los productosen la BBDD local
     *
     * @param a parametro booleano diferenciador
     */
    @SuppressWarnings("null")
    public void getInsertProductos(boolean a) {

        Date fecha = null;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        insertProductos = new ArrayList<>();
        con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(listaDeProductos);

                int x = 0;
                while (rst.next()) {

                    String sql = "INSERT INTO productoscamdun (codigoProducto,descripcionProducto,"
                            + "linea,valorUnitario,valorUnitarioConIva,isFree,pesoProducto,"
                            + "largoProducto,anchoProducto,altoProducto,activo,usuario,flag)"
                            + "VALUES ('"
                            + rst.getString("codigoProducto") + "','"
                            + rst.getString("descripcionProducto") + "','"
                            + rst.getString("linea") + "','"
                            + rst.getDouble("valorUnitario") + "','"
                            + rst.getDouble("valorUnitarioConIva") + "','"
                            + rst.getInt("isFree") + "','"
                            + rst.getDouble("pesoProducto") + "','"
                            + rst.getDouble("largoProducto") + "','"
                            + rst.getDouble("anchoProducto") + "','"
                            + rst.getDouble("altoProducto") + "','"
                            + rst.getInt("activo") + "','"
                            + rst.getString("usuario") + "','"
                            + rst.getInt("flag") + "') "
                            + "ON DUPLICATE KEY UPDATE  flag=1;\n ";
                    insertProductos.add(sql);

                }
                con.close();
                st.close();
                rst.close();

            } catch (SQLException ex) {
                try {
                    ejecutado = false;
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    st.close();
                    rst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
        }
    }

    /**
     * Método que fabrica el grupo de sentencias SQL con las cuales se van a
     * insertar los productos de la factura en la BBDD local
     *
     * @param a parametro booleano diferenciador
     */
    @SuppressWarnings("null")
    public void getInsertProductosPorFactura(boolean a) {

        //Date fecha = null;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        insertProductosPorFactura = new ArrayList<>();
        con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(listaDeProductosPorFactura);
                System.out.println("Inicio de proceso " + new java.util.Date());
                int x = 0;
                while (rst.next()) {

                    String sql = "INSERT INTO  productosporfactura (factura,codigoProducto,"
                            + "cantidad,valorUnitario,valorTotal,valorUnitarioConIva,"
                            + "valorTotalConIva,pesoProducto,activo,usuario,flag) VALUES('"
                            + rst.getString("factura") + "','"
                            + rst.getString("codigoProducto") + "','"
                            + rst.getInt("cantidad") + "','"
                            + rst.getDouble("valorUnitario") + "','"
                            + rst.getDouble("valorTotal") + "','"
                            + rst.getDouble("valorUnitarioConIva") + "','"
                            + rst.getDouble("valorTotalConIva") + "','"
                            + rst.getDouble("pesoProducto") + "','"
                            + rst.getInt("activo") + "','"
                            + rst.getString("usuario") + "','"
                            + rst.getInt("flag") + "')"
                            + "ON DUPLICATE KEY UPDATE  flag=1;\n ";
                    insertProductosPorFactura.add(sql);

                }

                con.close();
                st.close();
                rst.close();

            } catch (SQLException ex) {
                try {
                    ejecutado = false;
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    st.close();
                    rst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    /**
     * Método que realiza la actualizacion el canal de venta al cual pertenece
     * el cliente
     *
     * @param a parametro booleano diferenciador
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    @SuppressWarnings("null")
    public boolean actualizarCanaleVentaClientes(boolean a) {
        boolean actualizado = false;

        Statement st = null;
        Connection con = null;
        ResultSet rst = null;

        try {
            con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

            String sql = "update facturascamdun f "
                    + "join clasificacionRutas c "
                    + "on f.ruta=c.idclasificacionRutas "
                    + "set f.canal=c.tipoDeCanalDeVenta "
                    + "where f.trasmitido='0'";
            if (con != null) {

                st = con.createStatement();

                st.execute(sql);
                actualizado = true;
                System.out.println("Actualizado el canal de ventas de los clientes ");

            }
            st.close();
            con.close();

            System.out.println("Final del hilo actualizacion del canal del cliente ");

        } catch (SQLException ex) {
            try {
                actualizado = false;
                ejecutado = false;
                Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertar() consulta sql " + ex);
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return actualizado;
    }

    /**
     * Método que realiza la actualizacion la ciudaad en la factura al cual
     * pertenece el cliente
     *
     * @param a parametro booleano diferenciador
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    @SuppressWarnings("null")
    public boolean actualizarCiudadFactura(boolean a) {
        boolean actualizado = false;

        Statement st = null;
        Connection con = null;
        ResultSet rst = null;

        try {
            con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

//            String sql = "update facturascamdun f "
//                    + "join clasificacionRutas c "
//                    + "on f.ruta=c.idclasificacionRutas "
//                    + "set f.canal=c.tipoDeCanalDeVenta "
//                    + "where f.trasmitido='0'";
            String sql = "update facturascamdun f "
                    + "join clientescamdun cc on f.cliente=cc.codigoInterno "
                    + "set f.ciudad=cc.ciudad "
                    + "where f.trasmitido='0'";

            if (con != null) {

                st = con.createStatement();

                st.execute(sql);
                actualizado = true;
                System.out.println("Actualizado la ciudad en las facturas ");

            }
            st.close();
            con.close();

            System.out.println("Final del hilo actualizacion ciudad en la factura ");

        } catch (SQLException ex) {
            try {
                actualizado = false;
                ejecutado = false;
                Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertar() consulta sql " + ex);
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return actualizado;
    }

    /**
     * Método que realiza la actualizacion del peso de los productos que
     * contiene la factura de venta
     *
     * @param a parametro booleano diferenciador
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
  
    public boolean actualizarPesoProductos(boolean a) {
        boolean actualizado = false;

        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        try {
            con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

            String sql = "update productosporfactura pf "
                    + "join productoscamdun pc on pc.codigoProducto=pf.codigoProducto "
                    + "join facturascamdun fc on fc.numeroFactura=pf.factura "
                    + "set pf.pesoproducto=(pf.cantidad * pc.pesoProducto) "
                    + "where fc.trasmitido =0";

            if (con != null) {

                st = con.createStatement();

                st.execute(sql);
                actualizado = true;

                System.out.println("Actualizado el peso de los productos ");
                
                st.close();
                con.close();
                
            }else{
                actualizado = false;  
            }
           
            

            System.out.println("Final del hilo actualizacion  peso de los productos ");

        } catch (SQLException ex) {
            actualizado = false;
            ejecutado = false;
             System.out.println("Error en insertar() consulta sql " + ex);
             Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
           
            
        }finally{
            return actualizado;
        }

        
    }

    /**
     * Método que realiza la actualizacion del peso total de la factura de venta
     *
     * @param a parametro booleano diferenciador
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    @SuppressWarnings("null")
    public boolean actualizarPesoFacturas(boolean a) {
        boolean actualizado = false;

        Statement st = null;
        Connection con = null;
        ResultSet rst = null;

        con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);

        String sql = "update facturascamdun fc "
                + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                + "where fc.trasmitido=0;";

        if (con != null) {
            try {
                st = con.createStatement();

                try {
                    st.execute(sql);
                    actualizado = true;
                    System.out.println("Actualizado el peso de facturas ");

                } catch (SQLException ex) {
                    System.out.println("Error en insertar() consulta sql " + ex);
                    Logger
                            .getLogger(IniciarSincronizacion.class
                                    .getName()).log(Level.SEVERE, null, ex);
                }

                st.close();
                con.close();

                System.out.println("Final del hilo actualizacion peso de facturas");

            } catch (SQLException ex) {
                try {
                    actualizado = false;
                    ejecutado = false;
                    Logger.getLogger(IniciarSincronizacion.class
                            .getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en insertar() consulta sql " + ex);
                    st.close();
                    con.close();

                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }
        return actualizado;
    }

    /**
     * Método que realiza el cierre de la sincronizacion de los datos de los
     * servidores"
     *
     * @param a parametro booleano diferenciador
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    @SuppressWarnings("null")
    public boolean cerrarSincronizacion(boolean a) {
        boolean cerrado = false;
        Statement st = null;
        Connection con = null;
        ResultSet rst = null;
        try {
            con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDFuente);
            String sql = "update facturascamdun set trasmitido=1 where numeroFactura in " + listaDeFacturas;
            if (con != null) {
                st = con.createStatement();
                st.execute(sql);
                cerrado = true;
            }
 
                st.close(); 
                con.close();
            
          
           

            System.out.println("Final del Hilo actualizacion de la sincronizacion ");

        } catch (SQLException ex) {
            try {
                cerrado = false;
                Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en insertar() consulta sql " + ex);
                st.close();
                con.close();

            } catch (SQLException ex1) {
                Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }

        return cerrado;
    }

    /**
     * Método que realiza la inserción de los datos en la BBDD local, los cuales
     * provienen de un archivo en excel tomado previamente y del cual puede
     * salir la lista de Sentencias SQL para la inserción de los datos."
     *
     * @param listaDeSentenciasSQL corresponde a una lista de sentencias SQL
     * guardadas en un arrayList
     * @param fuente
     * @param tabla
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    /*insertar multiples registros en una Base de Datos MySQL*/
    // https://www.arquitecturajava.com/jdbc-batch-y-rendimiento/
    @SuppressWarnings("null")
    public boolean insertarRegistros(ArrayList<String> listaDeSentenciasSQL, boolean fuente, String tabla) {
        boolean insertar = false;
        String server = "";
        Connection con = null;
        Statement st = null;
        String cadena = "";

        /*Se aabre la conexion de la BBDD*/
        if (fuente) {
            con = CConexiones.GetConnection(cadenaFuente, usuarioBDFuente, claveBDdestino);
            server = "local";
        } else {
            con = CConexiones.GetConnection(cadenaDestino, usuarioBDDestino, claveBDdestino);
            server = "remoto";
        }

        if (con != null) {
            try {
                con.setAutoCommit(false);
                st = con.createStatement();
                int x = 0;
                for (String obj : listaDeSentenciasSQL) {
                    try {
                        // st.execute(obj);

                        /*Se agrega la sentencia al batch para ejecutar*/
                        st.addBatch(obj);
                        cadena = obj;
                        System.out.println("(" + tabla + ") dato sincronizado con servidor " + server + " -->" + x);
                    } catch (SQLException ex) {
                        System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");
                        Logger.getLogger(IniciarSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    x++;
                }
                //  System.out.println("(" + tabla + ") datos sincronizado con servidor " + server + ", tabla : " + tabla);

                /*Se graba la informacion de la lista de tentencias*/
                st.executeBatch();
                insertar = true;
                con.commit();
                st.close();
                con.close();
                System.out.println("(" + tabla + ") datos sincronizado con servidor " + server + ", tabla : " + tabla);

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(IniciarSincronizacion.class .getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en insertar() consulta sql " + ex + "(sql=" + cadena + ")");

                    st.close();
                    con.close();

                } catch (SQLException ex1) {
                    Logger.getLogger(IniciarSincronizacion.class
                            .getName()).log(Level.SEVERE, null, ex1);
                }
            }

        }

        return insertar;
    }

}
