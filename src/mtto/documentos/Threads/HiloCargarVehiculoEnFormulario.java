/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CambiarClave;
import mtto.PrincipalMantenimiento;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import mtto.ingresoDeRegistros.IngresarFacturasDeGastos;
import mtto.proveedores.IngresarSucursalDeProveedor;
import mtto.proveedores.IngresarProveedores;
import mtto.reportes.FReporteGastosFlota;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mtto.documentos.FIngresarDocumentosVehiculos;
import mtto.proveedores.Cproveedores;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloCargarVehiculoEnFormulario implements Runnable {

    static boolean band;

   Inicio ini;
    FIngresarDocumentosVehiculos fIngresarDocumentosVehiculos= null;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloCargarVehiculoEnFormulario(FIngresarDocumentosVehiculos fIngresarDocumentosVehiculos) {
        this.fIngresarDocumentosVehiculos = fIngresarDocumentosVehiculos;
        this.ini = fIngresarDocumentosVehiculos.ini;

    }

    @Override
    public void run() {
        try {

            instanciarFormularios();

        } catch (Exception ex) {
            Logger.getLogger(HiloCargarVehiculoEnFormulario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void instanciarFormularios() throws Exception {
       try {
             this.fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(true);
                if (!fIngresarDocumentosVehiculos.txtPlaca.getText().isEmpty()) {
                    fIngresarDocumentosVehiculos.limpiarTabla1();
                    fIngresarDocumentosVehiculos.limpiarTabla2();
                    for (CVehiculos obj : ini.getListaDeVehiculos()) {
                        if (obj.getPlaca().equals(fIngresarDocumentosVehiculos.txtPlaca.getText().trim())) {

                            this.fIngresarDocumentosVehiculos.vehiculo = obj;
                            this.fIngresarDocumentosVehiculos.lblMarca.setText(fIngresarDocumentosVehiculos.vehiculo.getNombreMarcaDeVehiculo() + " - " + fIngresarDocumentosVehiculos.vehiculo.getNombreLineaVehiculo());
                            this.fIngresarDocumentosVehiculos.lblModelo.setText(fIngresarDocumentosVehiculos.vehiculo.getModelo());
                            this.fIngresarDocumentosVehiculos.lblKilometraje.setText("" + fIngresarDocumentosVehiculos.vehiculo.getKilometrajeActual());

                            break;
                        }
                    }

                }
                if (this.fIngresarDocumentosVehiculos.vehiculo == null) {
                    JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "NO existe el vehiculo en el sistema", "Error", JOptionPane.ERROR_MESSAGE);
                    this.fIngresarDocumentosVehiculos.btnNuevo.setEnabled(true);
                  
                } else {
                    this.fIngresarDocumentosVehiculos.vehiculo.setListaDeDocumentosPorTipoDeVehiculo();
                    this.fIngresarDocumentosVehiculos.vehiculo.setListaDeDocumentosPorPlaca();
                    this.fIngresarDocumentosVehiculos.llenarTabla1();
                    this.fIngresarDocumentosVehiculos.llenarTabla2();
                    this.fIngresarDocumentosVehiculos.habilitar(true);
                    this.fIngresarDocumentosVehiculos.btnGrabar.setEnabled(false);
                    this.fIngresarDocumentosVehiculos.txtPlaca.setEnabled(false);
                }
                 this.fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(false); 

            } catch (Exception ex) {
                Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
            }

       
    }
}
