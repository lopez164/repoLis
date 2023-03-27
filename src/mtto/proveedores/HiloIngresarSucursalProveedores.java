/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.proveedores;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;

/**
 *
 * @author Usuario
 */
public class HiloIngresarSucursalProveedores implements Runnable {

    public IngresarSucursalDeProveedor fingresarSucursalDeProveedor;
    Inicio ini;
    String caso;

    /**
     * Constructor de clase
     *
     * @param fingresarSucursalDeProveedor
     * @param comando
     * @param ini
     */
    public HiloIngresarSucursalProveedores(Inicio ini, IngresarSucursalDeProveedor fingresarSucursalDeProveedor, String comando) {
        this.ini = ini;
        this.fingresarSucursalDeProveedor = fingresarSucursalDeProveedor;
        this.caso = comando;

    }

    @Override
    public void run() {

        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCargos":

                        break;

                    case "guardarSucursal":
                        guardarSucursal();

                        break;
                    case "cargarLaVista":
                        cargarLaVista();
                        break;
                    case "seleccionarFila":

                        break;

                    case "consultarSucursalProveedor":
                        consultarSucursalProveedor();
                        break;
                    default:
                        JOptionPane.showInternalMessageDialog(fingresarSucursalDeProveedor, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }

                /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloIngresarSucursalProveedores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarLaVista() {
       // fingresarSucursalDeProveedor.habilitar(false);
        ini.setListadeCuentasSecundarias();
        ini.setListaDeProveedores();
        ini.setListaDeTiposDeEmpresas();
        if (ini.getListaDeDepartamentos() == null) {
            ini.setListaDeDepartamentos();
        }

        for (CDepartamentos dpto : ini.getListaDeDepartamentos()) {
            fingresarSucursalDeProveedor.cbxDepartamentos.addItem(dpto.getNombreDepartamento());
        }
        if (ini.getListaDeAgencias() == null) {
            ini.setListaDeAgencias();
        }

        for (CAgencias ag : ini.getListaDeAgencias()) {
            fingresarSucursalDeProveedor.cbxAgencias.addItem(ag.getNombreAgencia());
        }

        fingresarSucursalDeProveedor.autoTxtNitProveedores = new TextAutoCompleter(fingresarSucursalDeProveedor.txtNit);
        for (Cproveedores prov : this.ini.getListaDeProveedores()) {
            fingresarSucursalDeProveedor.autoTxtNitProveedores.addItem(prov.getCedula());
        }

        fingresarSucursalDeProveedor.llenarJTable();

        fingresarSucursalDeProveedor.cargado = true;
    }

    private void guardarSucursal() {
        fingresarSucursalDeProveedor.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        fingresarSucursalDeProveedor.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18Na

        fingresarSucursalDeProveedor.habilitar(false);
        fingresarSucursalDeProveedor.estaOcupadoGrabando = true;

        asignarValoresSucursal();

        
            grabarSucursal();
        
        this.fingresarSucursalDeProveedor.estaOcupadoGrabando = false;
        this.fingresarSucursalDeProveedor.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fingresarSucursalDeProveedor.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

    }

    private void grabarSucursal() {
        /*Guarda los registros del proveedor*/

        fingresarSucursalDeProveedor.proveedor.setActivo(fingresarSucursalDeProveedor.chkActivo.isSelected() ? 1 : 0);

        if (fingresarSucursalDeProveedor.sucursal.grabarSucursalProveedor()) {

            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(false);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(false);

            /* Actualiza la lita de los sucursales */
            this.fingresarSucursalDeProveedor.proveedor.setListaDeSucursales();

            JOptionPane.showInternalMessageDialog(fingresarSucursalDeProveedor, "La sucursal ha sido guardada perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(false);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(false);

        } else {
            JOptionPane.showInternalMessageDialog(fingresarSucursalDeProveedor, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(true);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(true);

        }
    }

    private void actualizarSucursal() {
        /* Se guardan(actualiza) los datos de las personas, proveedores y sucursal **/
        if (fingresarSucursalDeProveedor.sucursal.grabarSucursalProveedor()) {

            this.fingresarSucursalDeProveedor.habilitar(false);
            //this.fIngresarProveedores.jBtnGrabar.setEnabled(false);
            fingresarSucursalDeProveedor.proveedor.setListaDeSucursales();
            
            JOptionPane.showInternalMessageDialog(fingresarSucursalDeProveedor, "El proveedor ha sido Actualizado perfectamente", "Registro actualizado", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showInternalMessageDialog(fingresarSucursalDeProveedor, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(true);
            this.fingresarSucursalDeProveedor.btnGrabar.setEnabled(true);

        }
    }

    public void consultarSucursalProveedor() {
        if(fingresarSucursalDeProveedor.validado == false){
        fingresarSucursalDeProveedor.actualizar = false;
        //fingresarSucursalDeProveedor.lblCirculoDeProgreso.setVisible(true);
        try {
            if (!fingresarSucursalDeProveedor.txtNit.getText().isEmpty()) {

                for (Cproveedores prov : ini.getListaDeProveedores()) {
                    if (prov.getCedula().equals(fingresarSucursalDeProveedor.txtNit.getText().trim())) {
                        fingresarSucursalDeProveedor.proveedor = new Cproveedores(ini);
                        fingresarSucursalDeProveedor.proveedor = prov;
                        fingresarSucursalDeProveedor.proveedor.setListaDeSucursales();
                        fingresarSucursalDeProveedor.sucursalElegida = true;

                        break;
                    }
                }

                /*Se validaa que el proveedor exista */
                if (fingresarSucursalDeProveedor.proveedor == null) {

                    return;

                } else {

                    
                    for (SucursalesPorproveedor sucursalesPorproveedor : fingresarSucursalDeProveedor.proveedor.getListaDeSucursales()) {
                        if (sucursalesPorproveedor.getNombreSucursal().equals(
                            fingresarSucursalDeProveedor.txtNombreSucursal.getText().trim())) {
                            // si existe el nombre de la sucursal 
                            fingresarSucursalDeProveedor.sucursal = sucursalesPorproveedor;
                            fingresarSucursalDeProveedor.sucursal.setListaDeCuentasSecundarias();
                            fingresarSucursalDeProveedor.actualizar = true;
                            llenarCamposDeTexto();
                           
                            fingresarSucursalDeProveedor.habilitar(false);
                            fingresarSucursalDeProveedor.jBtnNuevo.setEnabled(true);
                            fingresarSucursalDeProveedor.btnNuevo.setEnabled(true);
                            fingresarSucursalDeProveedor.btnGrabar.setEnabled(false);
                            fingresarSucursalDeProveedor.btnGrabar.setEnabled(false);

                            fingresarSucursalDeProveedor.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                            fingresarSucursalDeProveedor.btnNuevo.setText("Actualizar");

                            break;
                        } else {
                            fingresarSucursalDeProveedor.habilitar(true);
                        }
                    }
                }
                fingresarSucursalDeProveedor.validado= true;
                
                //fingresarSucursalDeProveedor.lblCirculoDeProgreso.setVisible(false);
                fingresarSucursalDeProveedor.txtNombreSucursal.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
        }

   }
        
    }

    public void llenarCamposDeTexto() {
        try {

            // fingresarSucursalDeProveedor.proveedor.setListaDeSucursales();
            // fingresarSucursalDeProveedor.proveedor.setListadeCuentasSecundarias();
            fingresarSucursalDeProveedor.txtCodigoInternoSucursal.setText("" + fingresarSucursalDeProveedor.sucursal.getCodigoInterno());
            fingresarSucursalDeProveedor.txtNombreSucursal.setText(fingresarSucursalDeProveedor.sucursal.getNombreSucursal());

            fingresarSucursalDeProveedor.txtDireccion.setText(fingresarSucursalDeProveedor.sucursal.getDireccionSucursal());
            fingresarSucursalDeProveedor.txtBarrio.setText(fingresarSucursalDeProveedor.sucursal.getBarrioSucursal());

            CCiudades ciudad = new CCiudades(ini, fingresarSucursalDeProveedor.proveedor.getCiudad());
            fingresarSucursalDeProveedor.cbxDepartamentos.setSelectedItem(ciudad.getNombreDepartamento());
            fingresarSucursalDeProveedor.cbxCiudades.setSelectedItem(ciudad.getNombreCiudad());

            fingresarSucursalDeProveedor.txtTelefono.setText(fingresarSucursalDeProveedor.sucursal.getTelefonoSucursal());
            fingresarSucursalDeProveedor.txtCelular.setText(fingresarSucursalDeProveedor.sucursal.getCelularSucursal());
            //fingresarSucursalDeProveedor.txt.setText(fingresarSucursalDeProveedor.sucursal.getTelefonoCelular());
            fingresarSucursalDeProveedor.txtContacto.setText(fingresarSucursalDeProveedor.sucursal.getContactoSucursal());
            fingresarSucursalDeProveedor.txtLatitud.setText(fingresarSucursalDeProveedor.sucursal.getLatitud());
            fingresarSucursalDeProveedor.txtLongitud.setText(fingresarSucursalDeProveedor.sucursal.getLongitud());

            fingresarSucursalDeProveedor.txtEmail.setText(fingresarSucursalDeProveedor.proveedor.getEmail());

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
            String strFecha = fingresarSucursalDeProveedor.proveedor.getFechaDeIngreso();
            Date fecha = null;
            fecha = formatoDelTexto.parse(strFecha);
            Date dt = new Date();

            //  fingresarSucursalDeProveedor.dateFech.setDate(fecha);
            int i = 0;
            boolean encontrado = false;

            for (CAgencias obj : ini.getListaDeAgencias()) {
                if (fingresarSucursalDeProveedor.sucursal.getAgencia() == obj.getIdAgencia()) {
                    fingresarSucursalDeProveedor.cbxAgencias.setSelectedIndex(i);
                    encontrado = true;
                    break;
                }

                i++;
            }

            if (fingresarSucursalDeProveedor.proveedor.getActivo() == 1) {
                fingresarSucursalDeProveedor.chkActivo.setSelected(true);
                fingresarSucursalDeProveedor.chkActivo.setText("Proveedor Activo");
            } else {
                fingresarSucursalDeProveedor.chkActivo.setSelected(false);
                fingresarSucursalDeProveedor.chkActivo.setText("Proveedor No Activo");
            }

             if (fingresarSucursalDeProveedor.sucursal.getListaDeCuentasSecundarias() != null) {

                for (i = 0; i < fingresarSucursalDeProveedor.tblCuentasSecundarias.getRowCount(); i++) {

                    for (CCuentaSecundariaLogistica cuenta : fingresarSucursalDeProveedor.sucursal.getListaDeCuentasSecundarias()) {
                        if (cuenta.getNombreCuentaSecundaria().equals(fingresarSucursalDeProveedor.tblCuentasSecundarias.getValueAt(i, 2))) {
                            ini.getListadeCuentasSecundarias().get(i).setActivo(i);
                            if (cuenta.getActivo() == 1) {
                                fingresarSucursalDeProveedor.tblCuentasSecundarias.setValueAt(true, i, 0);
                                ini.getListadeCuentasSecundarias().get(i).setActivo(1);
                            } else {
                                fingresarSucursalDeProveedor.tblCuentasSecundarias.setValueAt(false, i, 0);
                                ini.getListadeCuentasSecundarias().get(i).setActivo(0);
                            }
                            break;
                        }
                    }
                }
            }

        
        } catch (Exception ex) {
            Logger.getLogger(IngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al actualizar Campos de Formulario Ingresar empleados" + ex);

        }

    }

    public void asignarValoresSucursal() {

        try {
            fingresarSucursalDeProveedor.sucursal = new SucursalesPorproveedor(ini);

            fingresarSucursalDeProveedor.sucursal.setCedula(fingresarSucursalDeProveedor.txtNit.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setNombreSucursal(fingresarSucursalDeProveedor.txtNombreSucursal.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setCodigoInterno(fingresarSucursalDeProveedor.txtCodigoInternoSucursal.getText().trim());
            // sucursal.setApellidos(cbxTipoEmpresa.getSelectedItem().toString());
            fingresarSucursalDeProveedor.sucursal.setDireccionSucursal(fingresarSucursalDeProveedor.txtDireccion.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setBarrioSucursal(fingresarSucursalDeProveedor.txtBarrio.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setTelefonoSucursal(fingresarSucursalDeProveedor.txtTelefono.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setCelularSucursal(fingresarSucursalDeProveedor.txtCelular.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setCelularCorporativo(fingresarSucursalDeProveedor.txtCelular.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setEmailSucursal(fingresarSucursalDeProveedor.txtEmail.getText().trim());

            fingresarSucursalDeProveedor.sucursal.setContactoSucursal(fingresarSucursalDeProveedor.txtContacto.getText());
            fingresarSucursalDeProveedor.sucursal.setAgencia(ini.getUser().getAgencia());

            fingresarSucursalDeProveedor.cbxDepartamentos.getSelectedItem().toString();

            for (CCiudades obj : fingresarSucursalDeProveedor.departamento.getListaDeCiudades()) {
                if (obj.getNombreCiudad().equals(fingresarSucursalDeProveedor.cbxCiudades.getSelectedItem().toString())) {
                    fingresarSucursalDeProveedor.sucursal.setCiudadSucursal(obj.getIdCiudad());
                    break;
                }
            }

            fingresarSucursalDeProveedor.sucursal.setLatitud(fingresarSucursalDeProveedor.txtLatitud.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setLongitud(fingresarSucursalDeProveedor.txtLongitud.getText().trim());
            fingresarSucursalDeProveedor.sucursal.setCoordenadas(
                    fingresarSucursalDeProveedor.txtLatitud.getText().trim() + ","
                    + fingresarSucursalDeProveedor.txtLongitud.getText().trim());

            Date dt = new Date();
            //  dt = ini.getFechaSql(dateFechaIngreso);
            //  fingresarSucursalDeProveedor.setFechaDeIngreso(dt);

            dt = new Date();
            //    dt = ini.getFechaSql(dateFechaIngreso);
            //   fingresarSucursalDeProveedor.setFechaDeIngreso(dt);

            if (fingresarSucursalDeProveedor.chkActivo.isSelected()) {
                fingresarSucursalDeProveedor.sucursal.setActivo(1);
            } else {
                fingresarSucursalDeProveedor.sucursal.setActivo(0);
            }

            fingresarSucursalDeProveedor.listaDeServiciosSucursal = new ArrayList<>();;
            for (int i = 0; i < fingresarSucursalDeProveedor.tblCuentasSecundarias.getRowCount(); i++) {
                boolean valor = (boolean) fingresarSucursalDeProveedor.tblCuentasSecundarias.getValueAt(i, 0);
                
                    for (CCuentaSecundariaLogistica cuenta : ini.getListadeCuentasSecundarias()) {
                        if (cuenta.getNombreCuentaSecundaria().equals(fingresarSucursalDeProveedor.tblCuentasSecundarias.getValueAt(i, 2).toString())) {
                           if(valor){
                               cuenta.setActivo(1);
                           }else{
                               cuenta.setActivo(0);
                           }
                            fingresarSucursalDeProveedor.listaDeServiciosSucursal.add(cuenta);
                        }
                    }

                
            }
            fingresarSucursalDeProveedor.sucursal.setListaDeCuentasSecundarias(fingresarSucursalDeProveedor.listaDeServiciosSucursal);

        } catch (Exception ex) {
            Logger.getLogger(IngresarSucursalDeProveedor.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}
