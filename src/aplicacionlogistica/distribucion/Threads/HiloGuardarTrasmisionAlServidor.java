/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarTrasmisionAlServidor implements Runnable {

    //public static boolean band = false;
    // private int tiempo = 5;
    Inicio ini;
    private boolean remoto;
    FImportarArchivoExcel form;
    List<String> listaDeSentenciasSQL;
    String listaDeFacturas;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param listaDeSentenciasSQL
     * @param form
     * @param remoto
     * @param listaDeFacturas
     */
    public HiloGuardarTrasmisionAlServidor(Inicio ini, List<String> listaDeSentenciasSQL,
            FImportarArchivoExcel form, boolean remoto, String listaDeFacturas) {
        this.listaDeFacturas = listaDeFacturas;
        this.listaDeSentenciasSQL = listaDeSentenciasSQL;
        this.form = form;
        this.remoto = remoto;
        this.ini = ini;

    }

    @Override
    public void run() {
        int filas = 0;
        Connection con;//= null;
        Statement st = null;//= null;

        String sqlaInsertar = null;

        String cadena = null;

        form.jLabel2.setText("Sin Trasmision Remota");
        form.jLabel1.setText("Sin Trasmision Local");

        if (remoto) {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            form.jLabel2.setText("Ingresando datos al Servidor Remoto");
        } else {
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            form.jLabel1.setText("Ingresando datos al Servidor Local");
        }

        try {
            if (con != null) {
                con.setAutoCommit(false);
                st = con.createStatement();
                for (String obj : listaDeSentenciasSQL) {

                    if (form.cancelar) {
                        return;
                    }

                    System.out.println(obj);

                    if (obj.length() > 0) {
                        st.addBatch(obj);
                        filas++;

                        if (remoto) {
                            this.form.barraInferior.setValue((int) ((filas * 100 / listaDeSentenciasSQL.size())));
                            this.form.barraInferior.repaint();
                            Thread.sleep(2);
                        } else {
                            this.form.barraSuperior.setValue((int) ((filas * 100 / listaDeSentenciasSQL.size())));
                            this.form.barraSuperior.repaint();
                            Thread.sleep(2);
                        }

                    }

                    if (form.cancelar) {
                        return;
                    }

                }
                st.executeBatch();
                con.commit();
                st.close();
                con.close();

            }
        } catch (InterruptedException ex) {
            this.form.cancelar = true;
            // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar consulta sql " + ex + "(sql=" + sqlaInsertar + ")");
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos de la Sincronizacion", "Ok", JOptionPane.ERROR_MESSAGE, null);
        } catch (SQLException ex) {
            this.form.cancelar = true;
            Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos de la Sincronizacion", "Ok", JOptionPane.ERROR_MESSAGE, null);
        }
        // Logger.getLogger(HiloGuardarTrasmisionAlServidor.class.getName()).log(Level.SEVERE, null, ex );

        if (form.cancelar) {
            return;
        }

        /*cadana  SQL que actualiza el valor de la factura con iva*/
        String sql1 = "update facturascamdun fc "
                + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                + "set fc.valorTotalFactura=(select sum(pf2.valorTotalConIva) from productosporfactura pf2 where "
                + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                + "where fc.numeroFactura in (" + listaDeFacturas + ");";

        /*cadana  SQL que actualiza el valor de la factura sin iva*/
        String sql2 = "update facturascamdun fc  "
                + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                + "set fc.valorFacturaSinIva=(select sum(pf2.valorUnitario * pf2.cantidad) from productosporfactura pf2 where "
                + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                + "where fc.numeroFactura in (" + listaDeFacturas + ");";

        /*cadana  SQL que actualiza el peso en grs. del detalle de la factura*/
        String sql3 = "update productosporfactura pf "
                + "join productoscamdun pc on pc.codigoProducto=pf.codigoProducto "
                + "join facturascamdun fc on fc.numeroFactura=pf.factura "
                + "set pf.pesoproducto=(pf.cantidad*pc.pesoProducto) "
                + "where fc.numeroFactura in (" + listaDeFacturas + "); ";
        // + "where fc.trasmitido =0";

        /*cadana  SQL que actualiza el canal de venta de la factura*/
        String sql4 = "update facturascamdun f "
                + "join clasificacionRutas c "
                + "on f.ruta=c.idclasificacionRutas "
                + "set f.canal=c.tipoDeCanalDeVenta "
                + "where f.numeroFactura in (" + listaDeFacturas + "); ";

        try {

            if (remoto) {
                this.form.barraInferior.setValue(100);
                this.form.barraSuperior.repaint();

                /*Actualiza valor de la factura con iva*/
 /*ya no se actualiza el valor de la factura con iva, debido  a que el dato correspondiente fue agregado en
                    el archivo de trasmision y no hay necesidad de actualizarlo */
                // ini.insertarDatosRemotamente(sql1);
                /*Actualiza valor de la factura sin iva*/
                ini.insertarDatosRemotamente(sql2);

                /*Actualiza el peso en gramos del detalle de las facturas*/
                ini.insertarDatosRemotamente(sql3);
                System.out.println("actualizo el peso de las productos");

                /*Actualiza el canal de venta de las facturas*/
                ini.insertarDatosRemotamente(sql4);

                actualizarPesoDeLaFactura(true);
                System.out.println("actualizo el peso de las facturas");

            } else {

                this.form.barraSuperior.setValue(100);
                this.form.barraSuperior.repaint();

                /*Actualiza valor de la factura con iva*/
                ini.insertarDatosLocalmente(sql1);

                /*Actualiza valor de la factura sin iva*/
                ini.insertarDatosLocalmente(sql2);
                /*Actualiza el peso en gramos del detalle de las facturas*/
                ini.insertarDatosLocalmente(sql3);

                actualizarPesoDeLaFactura(false);

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

        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(this.form, "Error al Actualizar los valores de la Facturas", "Error", JOptionPane.ERROR_MESSAGE, null);
        }

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
            String sql = "update facturascamdun fc "
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

            Logger.getLogger(HiloGuardarTrasmisionAlServidor.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en insertar() consulta sql " + ex);

        }

        return actualizado;
    }
}
