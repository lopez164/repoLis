/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.proveedores;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.distribucion.objetos.CTiposDeEmpresas;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;

/**
 *
 * @author Usuario
 */
public class HiloIngresarProveedores implements Runnable {

    public IngresarProveedores fIngresarProveedores;
    Inicio ini;
    String caso;

    /**
     * Constructor de clase
     *
     * @param fIngresarProveedores
     * @param comando
     * @param ini
     */
    public HiloIngresarProveedores(Inicio ini, IngresarProveedores fIngresarProveedores, String comando) {
        this.ini = ini;
        this.fIngresarProveedores = fIngresarProveedores;
        this.caso = comando;

    }

    @Override
    public void run() {

        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCargos":

                        break;

                    case "guardar":
                        guardar();

                        break;
                    case "cargarLaVista":
                        cargarLaVista();
                        break;
                    case "seleccionarFila":

                        break;

                    case "consultarProveedor":
                        consultarProveedor();
                        break;

                    case "refrescar":
                        refrescar();
                        break;
                    default:
                        JOptionPane.showInternalMessageDialog(fIngresarProveedores, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }

                /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloIngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarLaVista() {
        fIngresarProveedores.habilitar(false);
        ini.setListadeCuentasSecundarias();
        ini.setListaDeProveedores();
        ini.setListaDeTiposDeEmpresas();

        if (ini.getListaDeDepartamentos() == null) {
            ini.setListaDeDepartamentos();
        }

        for (CDepartamentos dpto : ini.getListaDeDepartamentos()) {
            fIngresarProveedores.cbxDepartamentos.addItem(dpto.getNombreDepartamento());
        }
        if (ini.getListaDeAgencias() == null) {
            ini.setListaDeAgencias();
        }

        for (CAgencias ag : ini.getListaDeAgencias()) {
            fIngresarProveedores.cbxAgencias.addItem(ag.getNombreAgencia());
        }

        for (CTiposDeEmpresas te : ini.getListaDeTiposDeEmpresas()) {
            fIngresarProveedores.cbxTipoEmpresa.addItem(te.getNombreTipoEmpresa());
        }

        fIngresarProveedores.autoTxtNitProveedores = new TextAutoCompleter(fIngresarProveedores.txtNitProveedor);
        for (Cproveedores prov : this.ini.getListaDeProveedores()) {
            fIngresarProveedores.autoTxtNitProveedores.addItem(prov.getCedula());
        }

        fIngresarProveedores.llenarJTable();

        fIngresarProveedores.cargado = true;
    }

    private void guardar() {
        fIngresarProveedores.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        fIngresarProveedores.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18Na

        fIngresarProveedores.habilitar(false);
        fIngresarProveedores.estaOcupadoGrabando = true;
        asignarValoresProveedor();

        if (fIngresarProveedores.actualizar) {

            actualizarProveedor();

        } else {
            grabarNuevo();
        }
        this.fIngresarProveedores.estaOcupadoGrabando = false;
        this.fIngresarProveedores.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fIngresarProveedores.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

    }

    private void grabarNuevo() {
        /*Guarda los registros del proveedor*/

        fIngresarProveedores.proveedor.setActivo(fIngresarProveedores.chkActivo.isSelected() ? 1 : 0);

        if (fIngresarProveedores.proveedor.grabarProveedor()) {

            this.fIngresarProveedores.btnGrabar.setEnabled(false);
            this.fIngresarProveedores.jBtnGrabar.setEnabled(false);

            /* Actualiza la lita de los proveedores */
            ini.setListaDeProveedores();

            JOptionPane.showInternalMessageDialog(fIngresarProveedores, "El proveedor ha sido guardado perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);
            this.fIngresarProveedores.actualizarFoto = false;
            this.fIngresarProveedores.btnGrabar.setEnabled(false);
            this.fIngresarProveedores.jBtnGrabar.setEnabled(false);

        } else {
            JOptionPane.showInternalMessageDialog(fIngresarProveedores, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fIngresarProveedores.btnGrabar.setEnabled(true);
            this.fIngresarProveedores.jBtnGrabar.setEnabled(true);

        }
    }

    private void actualizarProveedor() {
        /* Se guardan(actualiza) los datos de las personas, proveedores y sucursal **/
        if (fIngresarProveedores.proveedor.actualizarProveedor()) {

            this.fIngresarProveedores.habilitar(false);
            //this.fIngresarProveedores.jBtnGrabar.setEnabled(false);
            ini.setListaDeProveedores();
            JOptionPane.showInternalMessageDialog(fIngresarProveedores, "El proveedor ha sido Actualizado perfectamente", "Registro actualizado", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showInternalMessageDialog(fIngresarProveedores, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fIngresarProveedores.btnGrabar.setEnabled(true);
            this.fIngresarProveedores.jBtnGrabar.setEnabled(true);

        }
    }

    public void consultarProveedor() {
        fIngresarProveedores.proveedor = null;
        fIngresarProveedores.lblCirculoDeProgreso.setVisible(true);
        try {
            if (!fIngresarProveedores.txtNitProveedor.getText().isEmpty()) {

                for (Cproveedores prov : ini.getListaDeProveedores()) {
                    if (prov.getCedula().equals(fIngresarProveedores.txtNitProveedor.getText().trim())) {
                        fIngresarProveedores.proveedor = new Cproveedores(ini);
                        fIngresarProveedores.proveedor = prov;

                        break;
                    }
                }

                if (fIngresarProveedores.proveedor == null) {
                    fIngresarProveedores.proveedor = new Cproveedores(ini);
                    fIngresarProveedores.habilitar(true);
                    fIngresarProveedores.txtNombres.requestFocus();
                    fIngresarProveedores.btnNuevo.setEnabled(false);
                    fIngresarProveedores.jBtnNuevo.setEnabled(false);
                    fIngresarProveedores.btnGrabar.setEnabled(false);
                    fIngresarProveedores.jBtnGrabar.setEnabled(false);

                } else {
                    llenarCamposDeTexto();
                    fIngresarProveedores.actualizar = true;
                    fIngresarProveedores.habilitar(false);
                    fIngresarProveedores.actualizar = true;
                    fIngresarProveedores.jBtnNuevo.setEnabled(true);
                    fIngresarProveedores.btnNuevo.setEnabled(true);
                    fIngresarProveedores.jBtnGrabar.setEnabled(false);
                    fIngresarProveedores.btnGrabar.setEnabled(false);

                    fIngresarProveedores.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                    fIngresarProveedores.btnNuevo.setText("Actualizar");
                }
                fIngresarProveedores.lblCirculoDeProgreso.setVisible(false);
                fIngresarProveedores.txtNombres.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarCamposDeTexto() {
        try {

            fIngresarProveedores.proveedor.setListaDeSucursales();
            fIngresarProveedores.proveedor.setListadeCuentasSecundarias();

            fIngresarProveedores.txtNombres.setText(fIngresarProveedores.proveedor.getNombres());
            for (int i = 0; i < fIngresarProveedores.cbxTipoEmpresa.getItemCount(); i++) {
                String xxx = fIngresarProveedores.cbxTipoEmpresa.getItemAt(i).toString();
                if (fIngresarProveedores.proveedor.getApellidos().equals(xxx)) {
                    fIngresarProveedores.cbxTipoEmpresa.setSelectedIndex(i);
                    break;
                }
            }

            fIngresarProveedores.txtDireccion.setText(fIngresarProveedores.proveedor.getDireccion());
            fIngresarProveedores.txtBarrio.setText(fIngresarProveedores.proveedor.getBarrio());

            CCiudades ciudad = new CCiudades(ini, fIngresarProveedores.proveedor.getCiudad());
            fIngresarProveedores.cbxDepartamentos.setSelectedItem(ciudad.getNombreDepartamento());
            fIngresarProveedores.cbxCiudades.setSelectedItem(ciudad.getNombreCiudad());

            fIngresarProveedores.txtTelefono.setText(fIngresarProveedores.proveedor.getTelefonoFijo());
            fIngresarProveedores.txtCelular.setText(fIngresarProveedores.proveedor.getTelefonoCelular());
            fIngresarProveedores.txtCelular.setText(fIngresarProveedores.proveedor.getTelefonoCelular());
            fIngresarProveedores.txtContacto.setText(fIngresarProveedores.proveedor.getContacto());
            fIngresarProveedores.txtLatitud.setText(fIngresarProveedores.proveedor.getLatitud());
            fIngresarProveedores.txtLongitud.setText(fIngresarProveedores.proveedor.getLongitud());

            fIngresarProveedores.txtEmail.setText(fIngresarProveedores.proveedor.getEmail());

            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
            String strFecha = fIngresarProveedores.proveedor.getFechaDeIngreso();
            Date fecha = null;
            fecha = formatoDelTexto.parse(strFecha);
            Date dt = new Date();

            fIngresarProveedores.dateFechaIngreso.setDate(fecha);

            int i = 0;
            boolean encontrado = false;
            for (CAgencias obj : ini.getListaDeAgencias()) {
                if (fIngresarProveedores.proveedor.getAgencia() == obj.getIdAgencia()) {
                    fIngresarProveedores.cbxAgencias.setSelectedIndex(i);
                    encontrado = true;
                }
                if (encontrado) {
                    break;
                }
                i++;
            }

            if (fIngresarProveedores.proveedor.getActivo() == 1) {
                fIngresarProveedores.chkActivo.setSelected(true);
                fIngresarProveedores.chkActivo.setText("Proveedor Activo");
            } else {
                fIngresarProveedores.chkActivo.setSelected(false);
                fIngresarProveedores.chkActivo.setText("Proveedor No Activo");
            }

            if (fIngresarProveedores.proveedor.getListaDeCuentasSecundarias() != null) {

                for (i = 0; i < fIngresarProveedores.tblCuentasSecundarias.getRowCount(); i++) {

                    for (CCuentaSecundariaLogistica cuenta : fIngresarProveedores.proveedor.getListaDeCuentasSecundarias()) {
                        if (cuenta.getNombreCuentaSecundaria().equals(fIngresarProveedores.tblCuentasSecundarias.getValueAt(i, 2))) {
                            ini.getListadeCuentasSecundarias().get(i).setActivo(i);
                            if (cuenta.getActivo() == 1) {
                                fIngresarProveedores.tblCuentasSecundarias.setValueAt(true, i, 0);
                                ini.getListadeCuentasSecundarias().get(i).setActivo(1);
                            } else {
                                fIngresarProveedores.tblCuentasSecundarias.setValueAt(false, i, 0);
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

    public void asignarValoresProveedor() {

        try {
            if (fIngresarProveedores.validar()) {

                fIngresarProveedores.proveedor.setCedula(fIngresarProveedores.txtNitProveedor.getText().trim());
                fIngresarProveedores.proveedor.setNombres(fIngresarProveedores.txtNombres.getText().trim());
                fIngresarProveedores.proveedor.setApellidos(fIngresarProveedores.cbxTipoEmpresa.getSelectedItem().toString());
                fIngresarProveedores.proveedor.setDireccion(fIngresarProveedores.txtDireccion.getText().trim());
                fIngresarProveedores.proveedor.setBarrio(fIngresarProveedores.txtBarrio.getText().trim());
                fIngresarProveedores.proveedor.setTelefonoFijo(fIngresarProveedores.txtTelefono.getText().trim());
                fIngresarProveedores.proveedor.setTelefonoCelular(fIngresarProveedores.txtCelular.getText().trim());
                fIngresarProveedores.proveedor.setCelularCorporativo(fIngresarProveedores.txtCelular.getText().trim());
                fIngresarProveedores.proveedor.setEmail(fIngresarProveedores.txtEmail.getText().trim());
                fIngresarProveedores.proveedor.setIdEstadoCivil(1);
                fIngresarProveedores.proveedor.setGenero("PROVEEDOR");
                fIngresarProveedores.proveedor.setIdTipoSangre(1);
                fIngresarProveedores.proveedor.setEscolaridad("PROVEEDOR");
                fIngresarProveedores.proveedor.setLugarNacimiento("PROVEEDOR");

                if (fIngresarProveedores.chkActivo.isSelected()) {
                    fIngresarProveedores.proveedor.setActivoPersona(1);
                } else {
                    fIngresarProveedores.proveedor.setActivoPersona(0);
                }
                fIngresarProveedores.proveedor.setContacto(fIngresarProveedores.txtContacto.getText());
                fIngresarProveedores.proveedor.setLatitud(fIngresarProveedores.txtLatitud.getText());
                fIngresarProveedores.proveedor.setLongitud(fIngresarProveedores.txtLongitud.getText());

                fIngresarProveedores.proveedor.setAgencia(ini.getUser().getAgencia());
                fIngresarProveedores.cbxDepartamentos.getSelectedItem().toString();
                CCiudades ciu = new CCiudades(ini, fIngresarProveedores.cbxCiudades.getSelectedItem().toString());
                fIngresarProveedores.proveedor.setCiudad(ciu.getIdCiudad());

//                for (CCiudades obj : ini.getListaDeCiudades()) {
//                    if (obj.getNombreCiudad().equals(fIngresarProveedores.cbxCiudades.getSelectedItem().toString())) {
//                        fIngresarProveedores.proveedor.setCiudad(obj.getIdCiudad());
//                        break;
//                    }
//                }
                Date dt = new Date();
                dt = ini.getFechaSql(fIngresarProveedores.dateFechaIngreso);
                fIngresarProveedores.proveedor.setCumpleanios(dt);

                dt = new Date();
                dt = ini.getFechaSql(fIngresarProveedores.dateFechaIngreso);
                fIngresarProveedores.proveedor.setFechaDeIngreso("" + dt);

                if (fIngresarProveedores.chkActivo.isSelected()) {
                    fIngresarProveedores.proveedor.setActivo(1);
                } else {
                    fIngresarProveedores.proveedor.setActivo(0);
                }
                List<CCuentaSecundariaLogistica> Listactasec = new ArrayList();

                /*Se llena la lista de la cuentas secundarias*/
                for (int i = 0; i < fIngresarProveedores.tblCuentasSecundarias.getRowCount(); i++) {

                    for (CCuentaSecundariaLogistica cuenta : ini.getListadeCuentasSecundarias()) {

                        if (cuenta.getNombreCuentaSecundaria().equals(fIngresarProveedores.tblCuentasSecundarias.getValueAt(i, 2))) {
                            CCuentaSecundariaLogistica ctaAux = new CCuentaSecundariaLogistica();
                            ctaAux.setCodigoCuentaSecundaria(cuenta.getCodigoCuentaSecundaria());
                            ctaAux.setIdCuentaPrincipal(cuenta.getIdCuentaPrincipal());
                            ctaAux.setIdCuentaSecundaria(cuenta.getIdCuentaSecundaria());
                            ctaAux.setNombreCuentaPrincipal(cuenta.getNombreCuentaPrincipal());
                            ctaAux.setNombreCuentaSecundaria(cuenta.getNombreCuentaSecundaria());

                            if ((boolean) fIngresarProveedores.tblCuentasSecundarias.getValueAt(i, 0)) {
                                ctaAux.setActivo(1);
                            } else {
                                ctaAux.setActivo(0);
                            }
                            Listactasec.add(ctaAux);
                            break;
                        }
                    }
                }
                fIngresarProveedores.proveedor.setListaDeCuentasSecundarias(Listactasec);
            }

        } catch (Exception ex) {
            Logger.getLogger(IngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private void refrescar() {

        fIngresarProveedores.cargado = false;

        fIngresarProveedores.jBtnRefrescar.setEnabled(false);
        fIngresarProveedores.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));

        ini.setListadeCuentasSecundarias();
        ini.setListaDeAgencias();
        ini.setListaDeDepartamentos();
        ini.setListaDeTiposDeEmpresas();
        ini.setListaDeProveedores();

        fIngresarProveedores.cbxAgencias.removeAllItems();
        fIngresarProveedores.cbxCiudades.removeAllItems();
        fIngresarProveedores.cbxDepartamentos.removeAllItems();
        fIngresarProveedores.cbxTipoEmpresa.removeAllItems();

        ini.setListadeCuentasSecundarias();
        ini.setListaDeProveedores();
        ini.setListaDeTiposDeEmpresas();

        for (CDepartamentos dpto : ini.getListaDeDepartamentos()) {
            fIngresarProveedores.cbxDepartamentos.addItem(dpto.getNombreDepartamento());
        }

        for (CAgencias ag : ini.getListaDeAgencias()) {
            fIngresarProveedores.cbxAgencias.addItem(ag.getNombreAgencia());
        }

        for (CTiposDeEmpresas te : ini.getListaDeTiposDeEmpresas()) {
            fIngresarProveedores.cbxTipoEmpresa.addItem(te.getNombreTipoEmpresa());
        }

        fIngresarProveedores.autoTxtNitProveedores = new TextAutoCompleter(fIngresarProveedores.txtNitProveedor);
        for (Cproveedores prov : this.ini.getListaDeProveedores()) {
            fIngresarProveedores.autoTxtNitProveedores.addItem(prov.getCedula());
        }

        fIngresarProveedores.llenarJTable();

        fIngresarProveedores.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));
        fIngresarProveedores.jBtnRefrescar.setEnabled(true);

        fIngresarProveedores.cargado = true;

    }

}
