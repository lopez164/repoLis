/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarFacturasEnPicking implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;
    ArrayList<String> sentenciasSQL;
    ArrayList<String> sentenciasSQLRemota;

    FManifestarFacturasEnPicking form;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarFacturasEnPicking(Inicio ini, FManifestarFacturasEnPicking form) {

        this.form = form;
        this.ini = ini;

    }

    @Override
    public void run() {

        sentenciasSQL = new ArrayList<>();
        sentenciasSQLRemota = new ArrayList<>();

        this.form.btnImprimir.setEnabled(false);
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.form.btnGrabar.setEnabled(false);
        this.form.txtNumeroDeFactura.setEnabled(false);
       

        this.form.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;

        try {

            /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
            //           File fichero = new File(form.manifiestoActual.getRutArchivofacturasporManifiesto());
//            if (fichero.exists()) {
//                form.manifiestoActual.setListaFacturasPorManifiesto(fichero);
//
//            }

            /* Se crea un listado de las facturas que salen a distribucion en al cadena sql  y se actualiza 
             la tabla local fcaturascamdun en el campo isFree=0 para que no se pueda sacar la 
             factura a distribucion */
 /*Controla antidad de registros ppor ciclo */
            int y = 0;
            /*cuenta los ciclos para la barra de progreso*/
            int x = (int) form.listaDeFacturasPorManifiesto.size() / 90;

            /*controla la barra de progreso*/
            int z = 0;
            this.form.valorDespBarraProgreso = z;
            int i = 1;
            form.brrProgreso.setValue(z);

            for (String obj : form.listaDeFacturasPorManifiesto) {
                if (y == x) {
                    this.form.valorDespBarraProgreso = z++;
                    y = 0;
                }

                String cadena = "INSERT INTO facturasenpicking"
                        + "(numeroFactura,"
                        + "idManifiestopicking,"
                        + "adherencia,"
                        + "usuario,"
                        + "direccionIp,"
                        + "nombreEstacion)"
                        + "VALUES "
                        + "('" + obj + "',"
                        + "'" + form.numeroManifiesto + "',"
                        + "'" + i + "','"
                        + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                        + "'" + ini.getDirecionIpLocal() + "',"
                        + "'" + ini.getNombreEstacionLocal() + "') "
                        + "ON DUPLICATE KEY UPDATE  flag=1 ;";
                sentenciasSQL.add(cadena);

                String cadenaRemota = "INSERT INTO bitacorafacturas"
                        + "(documento,"
                        + "numeroFactura,"
                        + "movimiento,"
                        + "usuario)"
                        + "VALUES "
                        + "('" + form.numeroManifiesto + "',"
                        + "'" + obj + "','"
                        + "Factura(documento) con movimiento en mafto # " + form.numeroManifiesto + " con destino: " + 
                        form.cbxDestinos.getSelectedItem().toString() +";','"
                        + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                        + "ON DUPLICATE KEY UPDATE  flag=1 ;";

                sentenciasSQLRemota.add(cadenaRemota);

                this.form.repaint();
                i++;
                y++;
            }

            /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD LOCAL */
            if (ini.insertarDatosLocalmente(sentenciasSQL)) {

                ini.insertarDatosRemotamente(sentenciasSQLRemota,"HiloGuardarFacturasEnPicking");


                /* SE HABILITA BOTON DE IMPRIMIR */
                this.form.btnImprimir.setEnabled(true);

                /* SE DESHABILITA EL BOTON DE GRABAR */
                this.form.btnGrabar.setEnabled(false);
                this.form.btnImprimir.setEnabled(true);

                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

                this.form.repaint();

                JOptionPane.showMessageDialog(this.form, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

            } else {
                JOptionPane.showMessageDialog(this.form, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
                this.form.btnGrabar.setEnabled(true);
            }

        } catch (NumberFormatException | HeadlessException ex) {
            Logger.getLogger(HiloGuardarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            //  this.form.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/
            ;
        } catch (SQLException ex) {
            Logger.getLogger(HiloGuardarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            // this.form.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            // this.form.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }

        this.form.estaOcupadoGrabando = false;
        //this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N

    }
}
