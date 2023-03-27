/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcelPacheco;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarTrasmisionAlServidorPacheco implements Runnable {

    //public static boolean band = false;
    // private int tiempo = 5;
    Inicio ini;
    private boolean remoto;
    FImportarArchivoExcelPacheco form;
    List<String> listaDeSentenciasSQL;
    List<String> listaDeSentenciasSQLManifiestos;
    List<String> sqlGeneral;

    String listaDeFacturas;
    String listaDeManifiestos;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param listaDeSentenciasSQL
     * @param form
     * @param remoto
     * @param listaDeFacturas
     */
    public HiloGuardarTrasmisionAlServidorPacheco(Inicio ini, List<String> listaDeSentenciasSQL,
            FImportarArchivoExcelPacheco form, boolean remoto, String listaDeFacturas) {
        this.listaDeFacturas = listaDeFacturas;
        this.form = form;
        this.listaDeSentenciasSQL = form.sqlInsercionRemota;
        this.listaDeSentenciasSQLManifiestos = form.sqlInsercionRemotaManifiestos;

        this.remoto = remoto;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     * @param remoto
     * @param listaDeFacturas
     */
    public HiloGuardarTrasmisionAlServidorPacheco(Inicio ini, FImportarArchivoExcelPacheco form, boolean remoto, String listaDeFacturas, String listaDeManifiestos) {
        this.listaDeFacturas = listaDeFacturas;
        this.listaDeManifiestos = listaDeManifiestos;
        this.form = form;
        this.listaDeSentenciasSQL = form.sqlInsercionRemota;
        this.listaDeSentenciasSQLManifiestos = form.sqlInsercionRemotaManifiestos;
        this.remoto = remoto;
        this.ini = ini;

    }

    @Override
    public void run() {
        if (grabar_2()) {
            return;
        }

    }

    private boolean grabar() throws HeadlessException {
        int filas = 0;
        Connection con;//= null;
        Statement st = null;//= null;
        int size = listaDeSentenciasSQL.size();
        int lenght = 0;
        String sqlaInsertar = null;
        String cadena = null;
        int i = 0;
        form.lblRemoto.setText("Esperando Conexion Reemota");
        form.lblLocal.setText("Esperando Conexion Local");
        if (remoto) {
            con = ini.getConnRemota();
            form.lblRemoto.setText("Ingresando datos al Servidor Remoto");

        } else {
            con = ini.getConnLocal();
            form.lblLocal.setText("Ingresando datos al Servidor Local");
        }
        try {
            if (con != null) {
                con.setAutoCommit(false);
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {
                    lenght++;
                    if (form.cancelar) {
                        return true;
                    }
                    System.out.println(obj.substring(0, 37));
                    if (obj.length() > 0) {
                        st.addBatch(obj);
                        /*contador de filas*/
                        filas++;
                        /*Acumulador de registro*/
                        i++;

                        /* Graba bloque de 200 registros y luego valida el tamaño del resulSet*/
                        if (lenght == listaDeSentenciasSQL.size() || i % 100 == 0) {
                            st.executeBatch();
                            con.commit();
                            i = 0;
                        }

                        Thread.sleep(2);

                        if (remoto) {
                            form.lblRemoto.setText("Proceso de trasmision Remota");

                            this.form.barraInferior.setValue((int) ((filas * 100 / listaDeSentenciasSQL.size())));
                            this.form.barraInferior.repaint();
                            Thread.sleep(2);
                        } else {
                            form.lblLocal.setText("Proceso de trasmision Local");
                            this.form.barraSuperior.setValue((int) ((filas * 100 / listaDeSentenciasSQL.size())));
                            this.form.barraSuperior.repaint();
                            Thread.sleep(2);
                        }

                    }
                    if (form.cancelar) {
                        return true;
                    }
                }
                //st.executeBatch();
                con.commit();
                // st.close();
                //con.close();
                size = listaDeSentenciasSQLManifiestos.size();
            }
        } catch (InterruptedException ex) {
            this.form.cancelar = true;
            // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar consulta sql " + ex + "(sql=" + sqlaInsertar + ")");
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos de la Sincronizacion", "Ok", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            this.form.cancelar = true;
            Logger.getLogger(HiloGuardarTrasmisionAlServidorPacheco.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos de la Sincronizacion", "Ok", JOptionPane.ERROR_MESSAGE, null);
        }
        // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex );
        // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex );
        if (form.cancelar) {
            return true;
        }
        sqlGeneral = new ArrayList<>();
        for (String sql : listaDeSentenciasSQL) {
            sqlGeneral.add(sql);
        }
        for (String sql : listaDeSentenciasSQLManifiestos) {
            sqlGeneral.add(sql);
        }
        /*cadana  SQL que actualiza el valor de la factura con iva, valor de la
        factura sin iva, y el peso de la factura*/
        String sql1 = "update facturas fc "
                + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                + "set fc.valorTotalFactura=(select sum(pf2.valorTotalConIva) from productosporfactura pf2 where "
                + "pf2.factura= fc.numeroFactura group by fc.numeroFactura),"
                + "fc.fpContado=(select sum(pf3.valorTotalConIva) from productosporfactura pf3 where "
                + "pf3.factura= fc.numeroFactura group by fc.numeroFactura),"
                + "fc.valorFacturaSinIva=(select sum(pf4.valorUnitario * pf4.cantidad) from productosporfactura pf4 where "
                + "pf4.factura= fc.numeroFactura group by fc.numeroFactura), "
                + "fc.pesofactura=(select sum(pf5.pesoProducto) from productosporfactura pf5 where "
                + "pf5.factura= fc.numeroFactura group by fc.numeroFactura) "
                + "where fc.numeroFactura in (" + listaDeFacturas + ");";
        /*cadana  SQL que actualiza el valor de la factura sin iva*/
        String sql2A = "update facturas fc  "
                + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                + "set fc.valorFacturaSinIva=(select sum(pf2.valorUnitario * pf2.cantidad) from productosporfactura pf2 where "
                + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                + "where fc.numeroFactura in (" + listaDeFacturas + ");";
        /*cadana  SQL que actualiza el EL PESO EN gramos de la factura*/
        String sql2B = "update facturas fc  "
                + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                + "set fc.pesofactura=(select sum(pf2.pesoProducto * 1000) from productosporfactura pf2 where "
                + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                + "where fc.numeroFactura in (" + listaDeFacturas + ");";
        /*cadana  SQL que actualiza el peso en grs. del detalle de la factura*/
        String sql3 = "update productosporfactura pf "
                + "join productos pc on pc.codigoProducto=pf.codigoProducto "
                + "join facturas fc on fc.numeroFactura=pf.factura "
                + "set pf.pesoproducto=(pf.cantidad*pc.pesoProducto) "
                + "where fc.numeroFactura in (" + listaDeFacturas + "); ";
        // + "where fc.trasmitido =0";
        /*cadana  SQL que actualiza el canal de venta de la factura*/
        String sql4 = "update facturas f "
                + "join clasificacionRutas c "
                + "on f.ruta=c.idclasificacionRutas "
                + "set f.canal=c.tipoDeCanalDeVenta "
                + "where f.numeroFactura in (" + listaDeFacturas + "); ";
        try {

            if (remoto) {
                this.form.barraInferior.setValue(100);
                this.form.barraSuperior.repaint();

                /*Actualiza el VALOR DE LA FCTURA con iva*/
                ini.insertarDatosRemotamente(sql1);
                sqlGeneral.add(sql1);

                /*Actualiza valor de la factura sin iva*/
                // ini.insertarDatosRemotamente(sql2A);

                /*Actualiza el peso en  del detalle de las facturas y el valor a recaudar*/
                // ini.insertarDatosRemotamente(sql2B);
                //System.out.println("actualizo el peso de las productos");
                /*aactualiza el pesoy el valor a cobrar de cada factura en distribucion*/
                sql1 = "update  facturaspormanifiesto fm "
                        + "join facturas fc on fm.numeroFactura=fc.numeroFactura "
                        + "set fm.valorARecaudarFactura=fc.fpContado, "
                        + "fm.pesoFactura = fc.pesoFactura "
                        + "where fc.numeroFactura in (" + listaDeFacturas + ");";

                /*Actualiza los pesos de los manifiestos con la carga en distribucion*/
                sql2A = "update manifiestosdedistribucion m  "
                        + "join facturaspormanifiesto fxm on fxm.numeroManifiesto=m.consecutivo "
                        + "set m.pesoManifiesto=(select sum(fxm2.pesoFactura) from facturaspormanifiesto fxm2 where "
                        + "fxm2.numeroManifiesto= m.consecutivo group by m.consecutivo),"
                        + "m.valorTotalManifiesto=(select sum(fxm3.valorARecaudarFactura) from facturaspormanifiesto fxm3 where "
                        + "fxm3.numeroManifiesto= m.consecutivo group by m.consecutivo), "
                        + "m.cantidadPedidos=(select count(fxm4.numeroFactura) from facturaspormanifiesto fxm4 where "
                        + "fxm4.numeroManifiesto= m.consecutivo group by m.consecutivo) "
                        + "where m.consecutivo in (" + listaDeManifiestos + ");";

                /*Funcion para insertar los manifiestos y su facturas */
                insertarManifiestos(con);

                /*actualiza los pesos y el valor de cada factura en distribucion*/
                ini.insertarDatosRemotamente(sql1);
                sqlGeneral.add(sql1);

                /*actualiza los pesos y el valor del manifiesto */
                ini.insertarDatosRemotamente(sql2A);
                sqlGeneral.add(sql2A);

                /*Se genera el archivo trasmision.sql*/
                ArchivosDeTexto arch = new ArchivosDeTexto("tmp/trasmision.sql");
                arch.guardarArchivoNuevo(sqlGeneral);

            } else {

                this.form.barraSuperior.setValue(100);
                this.form.barraSuperior.repaint();

                /*Actualiza valor de la factura con iva*/
                ini.insertarDatosLocalmente(sql1);

                /*Actualiza valor de la factura sin iva*/
                ini.insertarDatosLocalmente(sql2A);
                /*Actualiza el peso en gramos del detalle de las facturas*/
                ini.insertarDatosLocalmente(sql2B);

            }

            if (form.rbtTrasmLocal.isSelected() && form.rbtTrasmRemota.isSelected()) {
                if ((this.form.barraSuperior.getValue() == 100) && (this.form.barraInferior.getValue() == 100)) {
                    JOptionPane.showMessageDialog(this.form, "Datos importados desde archivo correctamente a los dos servidores ", "Ok", JOptionPane.INFORMATION_MESSAGE, null);
                }
            } else {
                if (form.rbtTrasmLocal.isSelected()) {
                    if ((this.form.barraSuperior.getValue() == 100)) {
                        JOptionPane.showMessageDialog(this.form, "Datos importados desde archivo correctamente a Servidor Local", "Ok", JOptionPane.INFORMATION_MESSAGE, null);
                    }

                }
                if (form.rbtTrasmRemota.isSelected()) {
                    if ((this.form.barraInferior.getValue() == 100)) {
                        JOptionPane.showMessageDialog(this.form, "Datos importados desde archivo correctamente a Servidor Remoto", "Ok", JOptionPane.INFORMATION_MESSAGE, null);
                    }

                }

            }
            form.txtErrores.setText(form.txtErrores.getText() + " \n" + "Culmina la trasmision de la informacion " + new Date());
            form.barraInferior.setValue(0);
            form.barraSuperior.setValue(0);
            form.lblBarraDeProgreso.setVisible(false);
            form.lblLocal.setText("Proceso Terminado");
            form.lblRemoto.setText("Proceso RTerminado");

        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(this.form, "Error al Actualizar los valores de la Facturas", "Error", JOptionPane.ERROR_MESSAGE, null);
        }
        return false;
    }

    private boolean grabar_2() throws HeadlessException {
        int filas = 0;
        Connection con;//= null;
        Statement st = null;//= null;
        int size = listaDeSentenciasSQL.size();
        int lenght = 0;
        String sqlaInsertar = null;
        String tabla = null;
        int i = 0;
        form.lblRemoto.setText("Esperando Conexion Reemota");
        form.lblLocal.setText("Esperando Conexion Local");
        
        List<String> listadeSentencias = new ArrayList<>();

        if (remoto) {
            con = ini.getConnRemota();
            form.lblRemoto.setText("Ingresando datos al Servidor Remoto");

            /*Genera sentencias para archivo sql*/
            sqlGeneral = new ArrayList<>();
            for (String sql : listaDeSentenciasSQL) {
                sqlGeneral.add(sql);
            }
            for (String sql : listaDeSentenciasSQLManifiestos) {
                sqlGeneral.add(sql);
            }

            

            /*cadana  SQL que actualiza el valor de la factura con iva, valor de la
        factura sin iva, y el peso de la factura*/
            String sql1 = "update facturas fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.valorTotalFactura=(select sum(pf2.valorTotalConIva) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura),"
                    + "fc.fpContado=(select sum(pf3.valorTotalConIva) from productosporfactura pf3 where "
                    + "pf3.factura= fc.numeroFactura group by fc.numeroFactura),"
                    + "fc.valorFacturaSinIva=(select sum(pf4.valorUnitario * pf4.cantidad) from productosporfactura pf4 where "
                    + "pf4.factura= fc.numeroFactura group by fc.numeroFactura), "
                    + "fc.pesofactura=(select sum(pf5.pesoProducto) from productosporfactura pf5 where "
                    + "pf5.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.numeroFactura in (" + listaDeFacturas + ");";

            sqlGeneral.add(sql1);

            /*actualiza el pesoy el valor a cobrar de cada factura en distribucion*/
            sql1 = "update  facturaspormanifiesto fm "
                    + "join facturas fc on fm.numeroFactura=fc.numeroFactura "
                    + "set fm.valorARecaudarFactura=fc.fpContado, "
                    + "fm.pesoFactura = fc.pesoFactura "
                    + "where fc.numeroFactura in (" + listaDeFacturas + ");";

            sqlGeneral.add(sql1);

            /*Actualiza los pesos de los manifiestos con la carga en distribucion*/
            sql1 = "update manifiestosdedistribucion m  "
                    + "join facturaspormanifiesto fxm on fxm.numeroManifiesto=m.consecutivo "
                    + "set m.pesoManifiesto=(select sum(fxm2.pesoFactura) from facturaspormanifiesto fxm2 where "
                    + "fxm2.numeroManifiesto= m.consecutivo group by m.consecutivo),"
                    + "m.valorTotalManifiesto=(select sum(fxm3.valorARecaudarFactura) from facturaspormanifiesto fxm3 where "
                    + "fxm3.numeroManifiesto= m.consecutivo group by m.consecutivo), "
                    + "m.cantidadPedidos=(select count(fxm4.numeroFactura) from facturaspormanifiesto fxm4 where "
                    + "fxm4.numeroManifiesto= m.consecutivo group by m.consecutivo) "
                    + "where m.consecutivo in (" + listaDeManifiestos + ");";

            sqlGeneral.add(sql1);

            /*Se genera el archivo trasmision.sql*/
            ArchivosDeTexto arch = new ArchivosDeTexto("tmp/trasmision.sql");
            arch.guardarArchivoNuevo(sqlGeneral);
            
            listadeSentencias= sqlGeneral;

        } else {
            con = ini.getConnLocal();
            form.lblLocal.setText("Ingresando datos al Servidor Local");
           
            /* Sentencia sql donde s e actualiza el valor total de la factur, el valor a cobrardela factura,
            el valor de la facturA SIN IVA y el peso de la factura*/
            String sql1 = "update facturas fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.valorTotalFactura=(select sum(pf2.valorTotalConIva) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura),"
                    + "fc.fpContado=(select sum(pf3.valorTotalConIva) from productosporfactura pf3 where "
                    + "pf3.factura= fc.numeroFactura group by fc.numeroFactura),"
                    + "fc.valorFacturaSinIva=(select sum(pf4.valorUnitario * pf4.cantidad) from productosporfactura pf4 where "
                    + "pf4.factura= fc.numeroFactura group by fc.numeroFactura), "
                    + "fc.pesofactura=(select sum(pf5.pesoProducto) from productosporfactura pf5 where "
                    + "pf5.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.numeroFactura in (" + listaDeFacturas + ");";
            
            listaDeSentenciasSQL.add(sql1);

            listadeSentencias= listaDeSentenciasSQL;
        }

        /*Inicia el proceso de  grabado*/
        try {
            if (con != null) {
                con.setAutoCommit(false);
                st = con.createStatement();
                for (String obj : listadeSentencias) {
                    lenght++;
                    String[] val = obj.split(" ");
                    tabla= val[2];
                    if (form.cancelar) {
                        return true;
                    }
                    System.out.println(obj.substring(0, 37));
                    if (obj.length() > 0) {
                        st.addBatch(obj);
                        /*contador de filas*/
                        filas++;
                        /*Acumulador de registro*/
                        i++;

                        /* Graba bloque de 200 registros y luego valida el tamaño del resulSet*/
                        if (lenght == listadeSentencias.size() || i % 100 == 0) {
                            st.executeBatch();
                            con.commit();
                            i = 0;
                        }

                        Thread.sleep(2);

                        if (remoto) {
                            form.lblRemoto.setText("Proceso de trasmision Remota , tabla : " + tabla);

                            this.form.barraInferior.setValue((int) ((filas * 100 / listadeSentencias.size())));
                            this.form.barraInferior.repaint();
                            Thread.sleep(2);
                        } else {
                            form.lblLocal.setText("Proceso de trasmision Local, tabla : " + tabla);
                            this.form.barraSuperior.setValue((int) ((filas * 100 / listadeSentencias.size())));
                            this.form.barraSuperior.repaint();
                            Thread.sleep(2);
                        }

                    }
                    if (form.cancelar) {
                        return true;
                    }
                }
                //st.executeBatch();
                con.commit();
                // st.close();
                //con.close();
                size = listaDeSentenciasSQLManifiestos.size();
            }
        } catch (InterruptedException ex) {
            this.form.cancelar = true;
            // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar consulta sql " + ex + "(sql=" + sqlaInsertar + ")");
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos de la Sincronizacion", "Ok", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            this.form.cancelar = true;
            Logger.getLogger(HiloGuardarTrasmisionAlServidorPacheco.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos de la Sincronizacion", "Ok", JOptionPane.ERROR_MESSAGE, null);
        }
        // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex );
        // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex );
        if (form.cancelar) {
            return true;
        }

        try {

            if (form.rbtTrasmLocal.isSelected() && form.rbtTrasmRemota.isSelected()) {
                if ((this.form.barraSuperior.getValue() == 100) && (this.form.barraInferior.getValue() == 100)) {
                    JOptionPane.showMessageDialog(this.form, "Datos importados desde archivo correctamente a los dos servidores ", "Ok", JOptionPane.INFORMATION_MESSAGE, null);
                }
            } else {
                if (form.rbtTrasmLocal.isSelected()) {
                    if ((this.form.barraSuperior.getValue() == 100)) {
                        JOptionPane.showMessageDialog(this.form, "Datos importados desde archivo correctamente a Servidor Local", "Ok", JOptionPane.INFORMATION_MESSAGE, null);
                    }

                }
                if (form.rbtTrasmRemota.isSelected()) {
                    if ((this.form.barraInferior.getValue() == 100)) {
                        JOptionPane.showMessageDialog(this.form, "Datos importados desde archivo correctamente a Servidor Remoto", "Ok", JOptionPane.INFORMATION_MESSAGE, null);
                    }

                }

            }
            form.txtErrores.setText(form.txtErrores.getText() + " \n" + "Culmina la trasmision de la informacion " + new Date());
            form.barraInferior.setValue(0);
            form.barraSuperior.setValue(0);
            form.lblBarraDeProgreso.setVisible(false);
            form.lblLocal.setText("Proceso Terminado");
            form.lblRemoto.setText("Proceso RTerminado");

        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.form, "Error al Actualizar los valores de la Facturas", "Error", JOptionPane.ERROR_MESSAGE, null);
        }
        return false;
    }

    /**
     * Método que realiza la actualizacion del peso de los productos que
     * contiene la factura de venta
     *
     * @return devuelve verdadero si graba, false si se genera algún problea
     */
    public boolean actualizarPesoDeLaFactura(boolean remoto) {
        boolean actualizado = false;

        try {


            /*cadana  SQL que actualiza el peso de la factura con iva*/
            String sql = "update facturas fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura)"
                    + "where fc.numeroFactura in (" + listaDeFacturas + ");";
            if (remoto) {
                ini.insertarDatosRemotamente(sql);
            } else {
                ini.insertarDatosLocalmente(sql);
            }

            System.out.println("Final del hilo actualizacion  peso de la factura ");

        } catch (Exception ex) {

            Logger.getLogger(HiloGuardarTrasmisionAlServidorPacheco.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar() consulta sql " + ex);

        }

        return actualizado;
    }

    private void insertarManifiestos(Connection con) {
        Statement st;

        if (con != null) {
            try {
                form.lblRemoto.setText("Grabando Rutas de Distribucion ...");
                this.form.barraInferior.setValue((0));
                int filas = 0;
                int i = 0;
                con.setAutoCommit(false);
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQLManifiestos) {

                    if (form.cancelar) {
                        return;
                    }

                    System.out.println(obj.substring(0, 37));

                    if (obj.length() > 0) {
                        st.addBatch(obj);
                        /*contador de filas*/
                        filas++;
                        /*Acumulador de registro*/
                        i++;

                        /* Graba bloque de 200 registros y luego valida el tamaño del resulSet*/
                        if (filas >= listaDeSentenciasSQLManifiestos.size() || i % 200 == 0) {
                            st.executeBatch();
                            con.commit();
                            i = 0;
                        }

                        Thread.sleep(2);

                        this.form.barraInferior.setValue((int) ((filas * 100 / listaDeSentenciasSQLManifiestos.size())));
                        this.form.barraInferior.repaint();
                        Thread.sleep(2);

                    }

                    if (form.cancelar) {
                        return;
                    }

                }
                //st.executeBatch();
                // con.commit();
                // st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(HiloGuardarTrasmisionAlServidorPacheco.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloGuardarTrasmisionAlServidorPacheco.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
