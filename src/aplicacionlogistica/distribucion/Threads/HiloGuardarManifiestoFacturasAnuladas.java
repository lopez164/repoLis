/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasAnuladas;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarManifiestoFacturasAnuladas implements Runnable {

    Inicio ini;

    FAnularFacturas form;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarManifiestoFacturasAnuladas(Inicio ini, FAnularFacturas form) {

        this.form = form;
        this.ini = ini;

    }

    public HiloGuardarManifiestoFacturasAnuladas(Inicio ini, JInternalFrame form) {

    }

    @Override
    public void run() {

        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.estaOcupadoGrabando = true;
        
        if ((this.form.listaDeFacturasParaAnular.size() > 0)) {

            CFacturasAnuladas facAnuladas = new CFacturasAnuladas(ini);

            /*limpiar componentes */
            this.form.txtNumeroDeFactura.setEnabled(false);
            this.form.txtJustificacion.setEnabled(false);
            this.form.btnAgregar.setEnabled(false);

            try {
                /* Se graba las facturas anuladas */
                if (facAnuladas.grabarFacturasAnuladas(this.form.listaDeFacturasParaAnular)) {

                    /* Se prrocede a borrar el archivo temporal de las facturas anuladas*/
                    ArchivosDeTexto archivo = new ArchivosDeTexto(this.form.rutaDeArchivoFacturaAnulada);
                   // archivo.borrarArchivo();

                    /*Mensajes de rigor*/
                    System.out.println("Termina de guardar los datos de formulario facturas anuladas  ");
                    this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
                    JOptionPane.showMessageDialog(this.form, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

                    this.form.estaOcupadoGrabando = false;
                    this.form.btnImprimir.setEnabled(true);
                    this.form.btnImprimir.requestFocus();

                    /* si presenta algun error en grabar el manifiesto  */
                } else {
                    JOptionPane.showMessageDialog(this.form, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            }

        }else
        JOptionPane.showMessageDialog(this.form, "No hay facturas para anular del sistema ", "Error", JOptionPane.WARNING_MESSAGE, null);    
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }
    }

