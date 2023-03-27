/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.Threads.HiloGuardarUsuario;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeUsuarios;
import aplicacionlogistica.distribucion.objetos.CEstadosCiviles;
import aplicacionlogistica.distribucion.objetos.CNivelesDeAcceso;
import aplicacionlogistica.distribucion.objetos.CTiposDeAcceso;
import aplicacionlogistica.distribucion.objetos.CTiposDeSangre;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.HeadlessException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.proveedores.HiloIngresarProveedores;

/**
 *
 * @author Usuario
 */
public class HiloIngresarUsuarios implements Runnable {

    Inicio ini = null;
    IngresarUsuarios fIngresarUsuarios = null;
    String caso;

    int idCargo = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIngresarUsuarios(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param ingresarUsuarios
     * @param comando
     */
    public HiloIngresarUsuarios(Inicio ini, IngresarUsuarios ingresarUsuarios, String comando) {
        this.ini = ini;
        this.fIngresarUsuarios = ingresarUsuarios;
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

                    case "consultarUsuario":
                        consultarUsuario();
                        break;
                    default:
                        JOptionPane.showInternalMessageDialog(fIngresarUsuarios, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

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

    private void guardar() {

        if (asignarValoresUsuario()) {
            this.fIngresarUsuarios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
            this.fIngresarUsuarios.estaOcupadoGrabando = true;
            this.fIngresarUsuarios.habilitar(false);
            fIngresarUsuarios.btnGrabar.setEnabled(false);
            try {

                if (fIngresarUsuarios.nuevoUsuario.grabarUsuario()) {

                    new Thread(new HiloListadoDeUsuarios(ini)).start(); //ok
                    JOptionPane.showInternalMessageDialog(fIngresarUsuarios, "El registro del empleado ha sido guardado perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);
                    fIngresarUsuarios.habilitar(false);
                } else {
                    JOptionPane.showInternalMessageDialog(fIngresarUsuarios, "Se presentó un error al guardar el registro", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException | HeadlessException ex) {
                Logger.getLogger(HiloGuardarUsuario.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(fIngresarUsuarios, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);

                /*   Se libera el manifiesto*/
                Logger.getLogger(HiloGuardarUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(HiloGuardarUsuario.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(fIngresarUsuarios, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
                /*   Se libera el manifiesto*/

            }

            fIngresarUsuarios.estaOcupadoGrabando = false;
            fIngresarUsuarios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

        }

    }

    public void cargarLaVista() {
        fIngresarUsuarios.lblCirculoDeProgreso.setVisible(true);
        fIngresarUsuarios.btnNuevo.setEnabled(false);
        fIngresarUsuarios.jBtnNuevo.setEnabled(false);

        if (ini.getListaDeDepartamentos() == null) {
            ini.setListaDeDepartamentos();

        }
        if (ini.getListaDeCiudades() == null) {
            ini.setListaDeCiudades();
        }

        fIngresarUsuarios.cbxAgencias.removeAllItems();
        fIngresarUsuarios.cbxCiudades.removeAllItems();
        fIngresarUsuarios.cbxDepartamentos.removeAllItems();
        fIngresarUsuarios.cbxEstadoCivil.removeAllItems();
        fIngresarUsuarios.cbxGenero.removeAllItems();
        fIngresarUsuarios.cbxNivelesDeAcceso.removeAllItems();
        fIngresarUsuarios.cbxRegionales.removeAllItems();
        fIngresarUsuarios.cbxTipoDeAcceso.removeAllItems();
        fIngresarUsuarios.cbxTipoDeSangre.removeAllItems();
        fIngresarUsuarios.cbxZonas.removeAllItems();
        for (CDepartamentos obj : ini.getListaDeDepartamentos()) {
            if (obj.getActivoDepartamento() == 1) {
                fIngresarUsuarios.cbxDepartamentos.addItem(obj.getNombreDepartamento());
            }

        }
//        for (CCiudades obj : ini.getListaDeCiudades()) {
//            cbxCiudades.addItem(obj.getNombreCiudad());
//        }
        for (CTiposDeAcceso obj : ini.getListaDeTiposDeAcceso()) {
            if (obj.getActivoTipoDeAcceso() == 1) {
                fIngresarUsuarios.cbxTipoDeAcceso.addItem(obj.getNombreTipoDeAcceso());
            }

        }
        for (CNivelesDeAcceso obj : ini.getListaDeNivelesDeAcceso()) {
            if (obj.getActivoNivelDeAcceso() == 1) {
                fIngresarUsuarios.cbxNivelesDeAcceso.addItem(obj.getNombreNivelDeAcceso());
            }

        }
        for (CTiposDeSangre obj : ini.getListaDeTiposDeSangre()) {
            fIngresarUsuarios.cbxTipoDeSangre.addItem(obj.getNombreTipoDeSAngre());
        }
        for (CEstadosCiviles obj : ini.getListaDeEstadosCiviles()) {
            fIngresarUsuarios.cbxEstadoCivil.addItem(obj.getNombreEstadoCivil());
        }
        for (CZonas obj : ini.getListaDeZonas()) {
            fIngresarUsuarios.cbxZonas.addItem(obj.getNombreZona());
        }
        for (CRegionales obj : ini.getListaDeRegionales()) {
            fIngresarUsuarios.cbxRegionales.addItem(obj.getNombreRegional());
        }
        for (CAgencias obj : ini.getListaDeAgencias()) {
            fIngresarUsuarios.cbxAgencias.addItem(obj.getNombreAgencia());
        }
        fIngresarUsuarios.cbxGenero.addItem("MASCULINO");
        fIngresarUsuarios.cbxGenero.addItem("FEMENINO");

        fIngresarUsuarios.lblCirculoDeProgreso.setVisible(false);
        fIngresarUsuarios.btnNuevo.setEnabled(true);
        fIngresarUsuarios.jBtnNuevo.setEnabled(true);
        fIngresarUsuarios.btnCancelar.setEnabled(true);
        fIngresarUsuarios.jBtnCancelar.setEnabled(true);
    }

    private void consultarUsuario() {
        this.fIngresarUsuarios.lblCirculoDeProgreso.setVisible(true);
        this.fIngresarUsuarios.txtCedula.setEnabled(false);
        fIngresarUsuarios.nuevoUsuario = null;

        try {
            if (!fIngresarUsuarios.txtCedula.getText().isEmpty()) {

                fIngresarUsuarios.nuevoUsuario = new CUsuarios(ini, fIngresarUsuarios.txtCedula.getText());

                if (fIngresarUsuarios.nuevoUsuario.getNombreUsuario() == null) { // si el usuario no existe
                    fIngresarUsuarios.habilitar(true);
                    fIngresarUsuarios.lblCirculoDeProgreso.setVisible(false);
                    fIngresarUsuarios.txtNombres.requestFocus();
                    fIngresarUsuarios.btnNuevo.setEnabled(false);
                    fIngresarUsuarios.jBtnNuevo.setEnabled(false);
                    llenarCamposDeTextoUsuarioNuevo();
                    JOptionPane.showInternalMessageDialog(fIngresarUsuarios, "Usuario nuevo en el sistema", "Nuevo", JOptionPane.INFORMATION_MESSAGE);

                } else { // si el usuario existe

                    llenarCamposDeTexto();
                    fIngresarUsuarios.actualizar = true;
                    fIngresarUsuarios.btnNuevo.setEnabled(true);
                    fIngresarUsuarios.jBtnNuevo.setEnabled(true);
                    fIngresarUsuarios.btnGrabar.setEnabled(false);
                    fIngresarUsuarios.jBtnGrabar.setEnabled(false);

                    fIngresarUsuarios.lblCirculoDeProgreso.setVisible(false);
                    fIngresarUsuarios.habilitar(false);
                    fIngresarUsuarios.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                    fIngresarUsuarios.btnNuevo.setText("Actualizar");
                }

                fIngresarUsuarios.txtNombres.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(IngresarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en txtCedulaKeyPressed " + ex.getMessage().toString());
        }
        //this.fIngresarUsuarios.lblCirculoDeProgreso.setVisible(false);

    }

    public void llenarCamposDeTexto() {

        try {
            fIngresarUsuarios.txtNombres.setText(fIngresarUsuarios.nuevoUsuario.getNombres());
            fIngresarUsuarios.txtApellidos.setText(fIngresarUsuarios.nuevoUsuario.getApellidos());
            fIngresarUsuarios.txtDireccion.setText(fIngresarUsuarios.nuevoUsuario.getDireccion());
            fIngresarUsuarios.txtBarrio.setText(fIngresarUsuarios.nuevoUsuario.getBarrio());
            for (CCiudades ciu : ini.getListaDeCiudades()) {
                if (ciu.getIdCiudad() == fIngresarUsuarios.nuevoUsuario.getCiudad()) {
                    fIngresarUsuarios.cbxDepartamentos.setSelectedItem(ciu.getNombreDepartamento());
                    fIngresarUsuarios.cbxCiudades.setSelectedItem(ciu.getNombreCiudad());
                }
            }
            fIngresarUsuarios.txtTelefono.setText(fIngresarUsuarios.nuevoUsuario.getTelefonoFijo());
            fIngresarUsuarios.txtCelular.setText(fIngresarUsuarios.nuevoUsuario.getTelefonoCelular());
            fIngresarUsuarios.txtEscolaridad.setText(fIngresarUsuarios.nuevoUsuario.getEscolaridad());

            fIngresarUsuarios.txtUsuarioDeEntrada.setText(Inicio.deCifrar(fIngresarUsuarios.nuevoUsuario.getNombreUsuario()));
            fIngresarUsuarios.txtUsuarioDeEntrada.setEditable(false);
            //cbxGenero.removeAllItems();
            fIngresarUsuarios.cbxGenero.setSelectedItem(fIngresarUsuarios.nuevoUsuario.getGenero());
            fIngresarUsuarios.dateCumpleanios.setDate(fIngresarUsuarios.nuevoUsuario.getCumpleanios());
            fIngresarUsuarios.txtLugarDeNacimiento.setText(fIngresarUsuarios.nuevoUsuario.getLugarNacimiento());
            //cbxEstadoCivil.removeAllItems();
            for (CEstadosCiviles ec : ini.getListaDeEstadosCiviles()) {
                if (ec.getIdEstadoCivil() == fIngresarUsuarios.nuevoUsuario.getEstadoCivil()) {
                    fIngresarUsuarios.cbxEstadoCivil.setSelectedItem(ec.getNombreEstadoCivil());

                }
            }

            fIngresarUsuarios.txtEmail.setText(fIngresarUsuarios.nuevoUsuario.getEmail());

            for (CTiposDeSangre ts : ini.getListaDeTiposDeSangre()) {
                if (ts.getIdTipoDeSAngre() == fIngresarUsuarios.nuevoUsuario.getIdTipoSangre()) {
                    fIngresarUsuarios.cbxTipoDeSangre.setSelectedItem(ts.getNombreTipoDeSAngre());
                }
            }

            for (CAgencias ag : ini.getListaDeAgencias()) {
                if (ag.getIdAgencia() == fIngresarUsuarios.nuevoUsuario.getAgencia()) {
                    fIngresarUsuarios.cbxAgencias.setSelectedItem(ag.getNombreAgencia());
                    fIngresarUsuarios.cbxZonas.setSelectedItem(ag.getNombreZona());
                    fIngresarUsuarios.cbxRegionales.setSelectedItem(ag.getNombreRegional());

                }
            }
            for (CTiposDeAcceso ta : ini.getListaDeTiposDeAcceso()) {
                if (ta.getIdTipoDeAcceso() == fIngresarUsuarios.nuevoUsuario.getTipoAcceso()) {
                    fIngresarUsuarios.cbxTipoDeAcceso.setSelectedItem(ta.getNombreTipoDeAcceso());

                }
            }
            for (CNivelesDeAcceso na : ini.getListaDeNivelesDeAcceso()) {
                if (na.getIdNivelDeAcceso() == fIngresarUsuarios.nuevoUsuario.getNivelAcceso()) {
                    fIngresarUsuarios.cbxNivelesDeAcceso.setSelectedItem(na.getNombreNivelDeAcceso());

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloIngresarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarCamposDeTextoUsuarioNuevo() {

        try {
            fIngresarUsuarios.txtNombres.setText(fIngresarUsuarios.nuevoUsuario.getNombres());
            fIngresarUsuarios.txtApellidos.setText(fIngresarUsuarios.nuevoUsuario.getApellidos());
            fIngresarUsuarios.txtDireccion.setText(fIngresarUsuarios.nuevoUsuario.getDireccion());
            fIngresarUsuarios.txtBarrio.setText(fIngresarUsuarios.nuevoUsuario.getBarrio());
            for (CCiudades ciu : ini.getListaDeCiudades()) {
                if (ciu.getIdCiudad() == fIngresarUsuarios.nuevoUsuario.getCiudad()) {
                    fIngresarUsuarios.cbxDepartamentos.setSelectedItem(ciu.getNombreDepartamento());
                    fIngresarUsuarios.cbxCiudades.setSelectedItem(ciu.getNombreCiudad());
                }
            }
            fIngresarUsuarios.txtTelefono.setText(fIngresarUsuarios.nuevoUsuario.getTelefonoFijo());
            fIngresarUsuarios.txtCelular.setText(fIngresarUsuarios.nuevoUsuario.getTelefonoCelular());
            fIngresarUsuarios.txtEscolaridad.setText(fIngresarUsuarios.nuevoUsuario.getEscolaridad());

            fIngresarUsuarios.txtUsuarioDeEntrada.setEditable(false);
            //cbxGenero.removeAllItems();
            fIngresarUsuarios.cbxGenero.setSelectedItem(fIngresarUsuarios.nuevoUsuario.getGenero());
            fIngresarUsuarios.dateCumpleanios.setDate(fIngresarUsuarios.nuevoUsuario.getCumpleanios());
            fIngresarUsuarios.txtLugarDeNacimiento.setText(fIngresarUsuarios.nuevoUsuario.getLugarNacimiento());
            //cbxEstadoCivil.removeAllItems();
            for (CEstadosCiviles ec : ini.getListaDeEstadosCiviles()) {
                if (ec.getIdEstadoCivil() == fIngresarUsuarios.nuevoUsuario.getEstadoCivil()) {
                    fIngresarUsuarios.cbxEstadoCivil.setSelectedItem(ec.getNombreEstadoCivil());

                }
            }

            fIngresarUsuarios.txtEmail.setText(fIngresarUsuarios.nuevoUsuario.getEmail());

            String strMain = fIngresarUsuarios.txtEmail.getText().trim();
            String[] arrSplit = strMain.split("@");
            fIngresarUsuarios.txtUsuarioDeEntrada.setText(arrSplit[0]);

            for (CTiposDeSangre ts : ini.getListaDeTiposDeSangre()) {
                if (ts.getIdTipoDeSAngre() == fIngresarUsuarios.nuevoUsuario.getIdTipoSangre()) {
                    fIngresarUsuarios.cbxTipoDeSangre.setSelectedItem(ts.getNombreTipoDeSAngre());
                }
            }

            for (CAgencias ag : ini.getListaDeAgencias()) {
                if (ag.getIdAgencia() == fIngresarUsuarios.nuevoUsuario.getAgencia()) {
                    fIngresarUsuarios.cbxAgencias.setSelectedItem(ag.getNombreAgencia());
                    fIngresarUsuarios.cbxZonas.setSelectedItem(ag.getNombreZona());
                    fIngresarUsuarios.cbxRegionales.setSelectedItem(ag.getNombreRegional());

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloIngresarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean asignarValoresUsuario() {
        boolean aceptado = false;
        if (validar()) {

            try {
                fIngresarUsuarios.nuevoUsuario = new CUsuarios(ini);
                fIngresarUsuarios.nuevoUsuario.setCedula(fIngresarUsuarios.txtCedula.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setNombres(fIngresarUsuarios.txtNombres.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setApellidos(fIngresarUsuarios.txtApellidos.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setDireccion(fIngresarUsuarios.txtDireccion.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setBarrio(fIngresarUsuarios.txtBarrio.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setTelefonoFijo(fIngresarUsuarios.txtTelefono.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setTelefonoCelular(fIngresarUsuarios.txtCelular.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setEscolaridad(fIngresarUsuarios.txtEscolaridad.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setLugarNacimiento(fIngresarUsuarios.txtLugarDeNacimiento.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setEmail(fIngresarUsuarios.txtEmail.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setNombreUsuario(fIngresarUsuarios.txtUsuarioDeEntrada.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setClaveUsuario(fIngresarUsuarios.clave);
                //existeNombreDeUsuario=user.verificarUsuario(txtUsuarioDeEntrada.getText().trim());
                fIngresarUsuarios.nuevoUsuario.setGenero(fIngresarUsuarios.cbxGenero.getSelectedItem().toString());

                fIngresarUsuarios.cbxDepartamentos.getSelectedItem().toString();
                for (CCiudades obj : ini.getListaDeCiudades()) {
                    if (obj.getNombreCiudad().equals(fIngresarUsuarios.cbxCiudades.getSelectedItem().toString())) {
                        fIngresarUsuarios.nuevoUsuario.setCiudad(obj.getIdCiudad());
                    }
                }

                for (CEstadosCiviles obj : ini.getListaDeEstadosCiviles()) {
                    if (obj.getNombreEstadoCivil().equals(fIngresarUsuarios.cbxEstadoCivil.getSelectedItem().toString())) {
                        fIngresarUsuarios.nuevoUsuario.setIdEstadoCivil(obj.getIdEstadoCivil());
                    }
                }
                for (CTiposDeSangre obj : ini.getListaDeTiposDeSangre()) {
                    if (obj.getNombreTipoDeSAngre().equals(fIngresarUsuarios.cbxTipoDeSangre.getSelectedItem().toString())) {
                        fIngresarUsuarios.nuevoUsuario.setIdTipoSangre(obj.getIdTipoDeSAngre());
                    }
                }
                // fIngresarUsuarios.nuevoUsuario.setIdTipoSangre(new CTiposDeSangre(ini, cbxTipoDeSangre.getSelectedItem().toString()));
                fIngresarUsuarios.cbxZonas.getSelectedItem().toString();
                fIngresarUsuarios.cbxRegionales.getSelectedItem().toString();

                CAgencias agen = new CAgencias(ini, fIngresarUsuarios.cbxAgencias.getSelectedItem().toString());

                fIngresarUsuarios.nuevoUsuario.setAgencia(agen.getIdAgencia());
                fIngresarUsuarios.nuevoUsuario.setRegional(agen.getIdRegional());
                fIngresarUsuarios.nuevoUsuario.setZona(agen.getIdZona());

                fIngresarUsuarios.cbxTipoDeAcceso.getSelectedItem().toString();
                fIngresarUsuarios.cbxNivelesDeAcceso.getSelectedItem().toString();

                for (CTiposDeAcceso obj : ini.getListaDeTiposDeAcceso()) {
                    if (obj.getNombreTipoDeAcceso().equals(fIngresarUsuarios.cbxTipoDeAcceso.getSelectedItem().toString())) {
                        fIngresarUsuarios.nuevoUsuario.setTipoAcceso(obj.getIdTipoDeAcceso());
                    }
                }

                for (CNivelesDeAcceso obj : ini.getListaDeNivelesDeAcceso()) {
                    if (obj.getNombreNivelDeAcceso().equals(fIngresarUsuarios.cbxNivelesDeAcceso.getSelectedItem().toString())) {
                        fIngresarUsuarios.nuevoUsuario.setNivelAcceso(obj.getIdNivelDeAcceso());
                    }
                }

                fIngresarUsuarios.nuevoUsuario.setActivoUsuario(1);
                Date dt = new Date();
                dt = ini.getFechaSql(fIngresarUsuarios.dateCumpleanios);
                fIngresarUsuarios.nuevoUsuario.setCumpleanios(dt);
            } catch (Exception ex) {
                Logger.getLogger(HiloIngresarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            aceptado = false;
            this.fIngresarUsuarios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fIngresarUsuarios, fIngresarUsuarios.mensaje, "Error en asignacion de Datos", JOptionPane.INFORMATION_MESSAGE);

        }

        return aceptado;

    }

    public boolean validar() {
        boolean verificado = true;
        fIngresarUsuarios.mensaje = "";
        if (validarUsuario()) {
            return verificado;
        }

        if (fIngresarUsuarios.nuevoUsuario != null) {

            if (fIngresarUsuarios.txtNombres.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha colocado el nombre del empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.txtApellidos.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha colocado los apellidos del empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.txtBarrio.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha colocado la dirección del empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.txtDireccion.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha colocado el barrio donde vive el empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.cbxDepartamentos.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha selecccionado el departamento donde vive el empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.cbxCiudades.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha seleccionado la ciudad donde vive el empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.txtTelefono.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha ingresado el telefono del empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.txtCelular.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha ingresado el celular del empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.txtEscolaridad.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha ingresado el nivel educativo del empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.txtLugarDeNacimiento.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha ingresado el Lugar de Nacimiento del empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.cbxEstadoCivil.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha seleccionado el estado civil del empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.txtEmail.getText().isEmpty()) {
                fIngresarUsuarios.mensaje += "No ha ingresado el Email del empleado" + "  \n";
                verificado = false;
            }
            if (!Inicio.validateEmail(fIngresarUsuarios.txtEmail.getText())) {
                fIngresarUsuarios.mensaje += "El formato del email no es válido" + "  \n";
                verificado = false;

            }

            if (fIngresarUsuarios.cbxTipoDeSangre.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha selecccionado el tipo de sangre del empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.cbxZonas.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha seleccionado la zona de ubicación del empleado" + "  \n";
                verificado = false;
            }

            if (fIngresarUsuarios.cbxRegionales.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha seleccionado la regional donde está ubicado el empleado" + "  \n";
                verificado = false;
            }
            if (fIngresarUsuarios.cbxAgencias.getSelectedIndex() == -1) {
                fIngresarUsuarios.mensaje += "No ha selecccionado la agencia donde está ubicado el empleado" + "  \n";
                verificado = false;
            }

        } else {
            fIngresarUsuarios.mensaje += "Debe digitar el número de cédula para crear un empleado";
            verificado = false;
        }

        System.out.println("Se han validado los datos del empleado... ");

        return verificado;
    }

    public boolean validarUsuario() {
        boolean verificado;
        for (CUsuarios usu : ini.getListaDeUsuarios()) {
            try {
                if (Inicio.deCifrar(usu.getNombreUsuario()).equals(fIngresarUsuarios.txtUsuarioDeEntrada.getText().trim())) {
                    fIngresarUsuarios.mensaje += "El nombre de usuario ya existe en el sistema";
                    verificado = false;
                    return true;
                }
            } catch (Exception ex) {
                Logger.getLogger(HiloIngresarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

}
