/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.distribucion.administracion.TalentoHUmano.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEmpleados;
import aplicacionlogistica.distribucion.objetos.CEstadosCiviles;
import aplicacionlogistica.distribucion.objetos.CTiposDeSangre;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.proveedores.HiloIngresarProveedores;
import org.apache.poi.hpsf.ClassID;

/**
 *
 * @author Usuario
 */
public class HiloIngresarEmpleados implements Runnable {
    
    Inicio ini = null;
    IngresarEmpleados fIngresarEmpleados = null;
    String caso;
    
    int idCargo = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIngresarEmpleados(Inicio ini) {
        this.ini = ini;
        
    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param IngresarEmpleados
     * @param comando
     */
    public HiloIngresarEmpleados(Inicio ini, IngresarEmpleados IngresarEmpleados, String comando) {
        this.ini = ini;
        this.fIngresarEmpleados = IngresarEmpleados;
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
                    
                    case "consultarEmpleado":
                        consultarEmpleado();
                        break;
                    
                    case "refrescar":
                        refrescar();
                        break;
                    
                    default:
                        JOptionPane.showInternalMessageDialog(fIngresarEmpleados, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);
                    
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
        
        this.fIngresarEmpleados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        this.fIngresarEmpleados.estaOcupadoGrabando = true;
        this.fIngresarEmpleados.habilitar(false);
        
        if (validar()) {
            asignarValoresEmpleado();
            if (fIngresarEmpleados.empleado.grabarEmpleados()) {
                if (fIngresarEmpleados.empleado.insertarFofografia()) {
                    
                    new Thread(new HiloListadoDeEmpleados(fIngresarEmpleados.ini)).start();
                    
                    this.fIngresarEmpleados.estaOcupadoGrabando = false;
                    this.fIngresarEmpleados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

                    JOptionPane.showInternalMessageDialog(fIngresarEmpleados, "El registro del empleado ha sido guardado perfectamente", "Registro guardado", 1);
                    this.fIngresarEmpleados.actualizarFoto = false;
                    this.fIngresarEmpleados.btnGrabar.setEnabled(false);
                    
                }
                
            } else {
                this.fIngresarEmpleados.estaOcupadoGrabando = false;
                this.fIngresarEmpleados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

                JOptionPane.showInternalMessageDialog(fIngresarEmpleados, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                this.fIngresarEmpleados.btnGrabar.setEnabled(true);
                
            }
            
        } else {
            this.fIngresarEmpleados.habilitar(true);
            this.fIngresarEmpleados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fIngresarEmpleados, fIngresarEmpleados.mensaje, "Error para guardar", 0);
            
        }
        
    }
    
    public void cargarLaVista() {
        fIngresarEmpleados.lblCirculoDeProgreso.setVisible(true);
        fIngresarEmpleados.btnNuevo.setEnabled(false);
        fIngresarEmpleados.jBtnNuevo.setEnabled(false);
        
        if (ini.getListaDeDepartamentos() == null) {
            ini.setListaDeDepartamentos();
            
        }
        if (ini.getListaDeCiudades() == null) {
            ini.setListaDeCiudades();
        }
        
        fIngresarEmpleados.cbxDepartamentos.removeAllItems();
        fIngresarEmpleados.cbxEstadoCivil.removeAllItems();
        fIngresarEmpleados.cbxTipoDeSangre.removeAllItems();
        fIngresarEmpleados.cbxZonas.removeAllItems();
        fIngresarEmpleados.cbxAgencias.removeAllItems();
        fIngresarEmpleados.cbxRegionales.removeAllItems();
        fIngresarEmpleados.cbxCargos.removeAllItems();
        fIngresarEmpleados.cbxCentrosDeCosto.removeAllItems();
        fIngresarEmpleados.cbxCiudades.removeAllItems();
        fIngresarEmpleados.cbxEntidadesBancarias.removeAllItems();
        fIngresarEmpleados.cbxTiposDeContrato.removeAllItems();
        fIngresarEmpleados.cbxZonas.removeAllItems();
        fIngresarEmpleados.cbxGenero.removeAllItems();
        
        for (CEstadosCiviles obj : this.ini.getListaDeEstadosCiviles()) {
            if (obj.getActivoEstadoCivil() == 1) {
                fIngresarEmpleados.cbxEstadoCivil.addItem(obj.getNombreEstadoCivil());
            }
        }
        
        for (CDepartamentos obj : this.ini.getListaDeDepartamentos()) {
            if (obj.getActivoDepartamento() == 1) {
                fIngresarEmpleados.cbxDepartamentos.addItem(obj.getNombreDepartamento());
            }
        }
        
        for (CTiposDeSangre obj : this.ini.getListaDeTiposDeSangre()) {
            if (obj.getActivoTipoDeSangre() == 1) {
                fIngresarEmpleados.cbxTipoDeSangre.addItem(obj.getNombreTipoDeSAngre());
            }
            
        }
        for (CZonas obj : this.ini.getListaDeZonas()) {
            if (obj.getActivoZona() == 1) {
                fIngresarEmpleados.cbxZonas.addItem(obj.getNombreZona());
            }
            
        }
        
        for (CCargos obj : this.ini.getListaDeCargos()) {
            if (obj.getActivoCargo() == 1) {
                fIngresarEmpleados.cbxCargos.addItem(obj.getNombreCargo());
            }
            
        }
        
        for (CCentrosDeCosto obj : this.ini.getListaDeCentrosDeCosto()) {
            if (obj.getactivoCentroDeCosto() == 1) {
                fIngresarEmpleados.cbxCentrosDeCosto.addItem(obj.getNombreCentroDeCosto());
            }
            
        }
        
        for (CEntidadesBancarias obj : this.ini.getListaDeEntidadesBancarias()) {
            if (obj.getActivoEntidadBancaria() == 1) {
                fIngresarEmpleados.cbxEntidadesBancarias.addItem(obj.getNombreEntidadBancaria());
            }
            
        }
        
        for (CTiposDeContratosPersonas obj : this.ini.getListaDeTiposContratosPer()) {
            if (obj.getActivoTipoDeContrato() == 1) {
                fIngresarEmpleados.cbxTiposDeContrato.addItem(obj.getNombreTipoDeContrato());
            }
            
        }
        
        for (CAgencias obj : this.ini.getListaDeAgencias()) {
            if (obj.getActivoAgencia() == 1) {
                fIngresarEmpleados.cbxAgencias.addItem(obj.getNombreAgencia());
            }
            
        }
        
        fIngresarEmpleados.cbxGenero.addItem("MASCULINO");
        fIngresarEmpleados.cbxGenero.addItem("FEMENINO");
        fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);
        fIngresarEmpleados.btnNuevo.setEnabled(true);
        fIngresarEmpleados.jBtnNuevo.setEnabled(true);
        fIngresarEmpleados.jBtnCancel.setEnabled(true);
        fIngresarEmpleados.btnCancelar.setEnabled(true);
        fIngresarEmpleados.moveToFront();
        
    }
    
    private void consultarEmpleado() {
        this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(true);
        this.fIngresarEmpleados.txtCedula.setEnabled(false);
        this.fIngresarEmpleados.empleado = null;
        
        try {
            
            if (!this.fIngresarEmpleados.txtCedula.getText().isEmpty()) {
                
                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(this.fIngresarEmpleados.txtCedula.getText().trim())) {
                        obj.setImage();
                        fIngresarEmpleados.empleado = obj;
                    }
                }
                
                if (fIngresarEmpleados.empleado != null) {
                    /*El empleado existe*/
                    //this.fIngresarEmpleados.empleado = empleado;
                    llenarCamposDeTexto();
                    
                    this.fIngresarEmpleados.habilitar(false);
                    this.fIngresarEmpleados.actualizar = true;
                    this.fIngresarEmpleados.btnNuevo.setEnabled(true);
                    this.fIngresarEmpleados.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                    this.fIngresarEmpleados.jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_32x32.png"))); // NOI18N
                    this.fIngresarEmpleados.btnNuevo.setText("Actualizar");
                    
                } else {

                    //empleado= new CEmpleados(ini);
                    this.fIngresarEmpleados.habilitar(true);
                    
                    this.fIngresarEmpleados.txtNombres.setEnabled(true);
                    this.fIngresarEmpleados.txtNombres.setEditable(true);
                    this.fIngresarEmpleados.btnNuevo.setEnabled(false);
                    this.fIngresarEmpleados.jBtnNuevo.setEnabled(false);
                    
                    this.fIngresarEmpleados.empleado = new CEmpleados(ini, this.fIngresarEmpleados.txtCedula.getText().trim());
                    
                    if (this.fIngresarEmpleados.empleado.getNombres() != null) {
                        llenarCamposDeTexto();
                    } else {
                        //this.fIngresarEmpleados.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg"))); // NOI18N
                        this.fIngresarEmpleados.empleado.setFormatoFotografia("nada");
                    }
                    
                }
                
                this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);
                this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);
                this.fIngresarEmpleados.txtNombres.requestFocus();
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(IngresarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);

    }
    
    public void llenarCamposDeTexto() {
        try {
            
            fIngresarEmpleados.txtNombres.setText(fIngresarEmpleados.empleado.getNombres());
            fIngresarEmpleados.txtApellidos.setText(fIngresarEmpleados.empleado.getApellidos());
            fIngresarEmpleados.txtBarrio.setText(fIngresarEmpleados.empleado.getBarrio());
            fIngresarEmpleados.txtDireccion.setText(fIngresarEmpleados.empleado.getDireccion());
            
            CCiudades ciudad = new CCiudades(ini, fIngresarEmpleados.empleado.getCiudad());
            
            fIngresarEmpleados.cbxDepartamentos.setSelectedItem(ciudad.getNombreDepartamento());
            //fIngresarEmpleados.cbxCiudades.setSelectedItem(ciudad.getNombreCiudad());
            fIngresarEmpleados.txtCiudad.setText(ciudad.getNombreCiudad());
            
            fIngresarEmpleados.txtTelefono.setText(fIngresarEmpleados.empleado.getTelefonoFijo());
            fIngresarEmpleados.txtCelular.setText(fIngresarEmpleados.empleado.getTelefonoCelular());
            fIngresarEmpleados.txtCelularCorporativo.setText(fIngresarEmpleados.empleado.getCelularCorporativo());
            fIngresarEmpleados.txtEscolaridad.setText(fIngresarEmpleados.empleado.getEscolaridad());
            
            fIngresarEmpleados.cbxGenero.setSelectedItem(fIngresarEmpleados.empleado.getGenero());
            
            fIngresarEmpleados.dateCumpleanios.setDate(fIngresarEmpleados.empleado.getCumpleanios());
            fIngresarEmpleados.txtLugarDeNacimiento.setText(fIngresarEmpleados.empleado.getLugarNacimiento());
            
            for (CEstadosCiviles obj : this.ini.getListaDeEstadosCiviles()) {
                if (obj.getIdEstadoCivil() == fIngresarEmpleados.empleado.getIdTipoSangre()) {
                    fIngresarEmpleados.cbxEstadoCivil.setSelectedItem(obj.getNombreEstadoCivil());
                }
            }
            
            fIngresarEmpleados.txtEmail.setText(fIngresarEmpleados.empleado.getEmail());
            
            for (CTiposDeSangre obj : this.ini.getListaDeTiposDeSangre()) {
                if (obj.getIdTipoDeSAngre() == fIngresarEmpleados.empleado.getIdTipoSangre()) {
                    fIngresarEmpleados.cbxTipoDeSangre.setSelectedItem(obj.getNombreTipoDeSAngre());
                }
            }
            
            for (CCargos obj : this.ini.getListaDeCargos()) {
                if (obj.getIdCargo() == fIngresarEmpleados.empleado.getIdCargo()) {
                    fIngresarEmpleados.cbxCargos.setSelectedItem(obj.getNombreCargo());
                }
            }
            
            fIngresarEmpleados.dateFechaIngreso.setDate(fIngresarEmpleados.empleado.getFechaIngresoEmpresa());
            
            CAgencias ag = new CAgencias(ini);
            
            for (CAgencias obj : this.ini.getListaDeAgencias()) {
                if (obj.getIdAgencia() == fIngresarEmpleados.empleado.getIdAgencia()) {
                    ag = obj;
                    fIngresarEmpleados.cbxZonas.setSelectedItem(obj.getNombreZona());
                    
                    fIngresarEmpleados.cbxAgencias.setSelectedItem(obj.getNombreAgencia());
                }
            }

            /*se llena la lista desplegable de la regionales*/
            for (CRegionales obj : this.ini.getListaDeRegionales()) {
                if (obj.getNombreZona().equals(fIngresarEmpleados.cbxZonas.getSelectedItem().toString())) {
                    fIngresarEmpleados.cbxRegionales.addItem(obj.getNombreRegional());
                }
            }
            /*Selecciona la regional del empleado*/
            for (CRegionales obj : this.ini.getListaDeRegionales()) {
                if (ag.getIdRegional() == obj.getIdRegional()) {
                    fIngresarEmpleados.cbxRegionales.setSelectedItem(obj.getNombreRegional());
                }
            }
            
            for (CCentrosDeCosto obj : this.ini.getListaDeCentrosDeCosto()) {
                if (obj.getIdCentroDeCosto() == fIngresarEmpleados.empleado.getIdCentroDeCosto()) {
                    fIngresarEmpleados.cbxCentrosDeCosto.setSelectedItem(obj.getNombreCentroDeCosto());
                }
            }
            for (CTiposDeContratosPersonas obj : this.ini.getListaDeTiposContratosPer()) {
                if (obj.getIdTipoDeContrato() == fIngresarEmpleados.empleado.getIdTipoDeContrato()) {
                    fIngresarEmpleados.cbxTiposDeContrato.setSelectedItem(obj.getNombreTipoDeContrato());
                }
            }
            
            fIngresarEmpleados.txtNumeroDeCuenta.setText(fIngresarEmpleados.empleado.getNumeroCuenta());
            
            for (CEntidadesBancarias obj : this.ini.getListaDeEntidadesBancarias()) {
                if (obj.getIdEntidadBancaria() == fIngresarEmpleados.empleado.getIdBanco()) {
                    fIngresarEmpleados.cbxEntidadesBancarias.setSelectedItem(obj.getNombreEntidadBancaria());
                }
            }
            
            if (fIngresarEmpleados.empleado.getEmpleadoActivo() == 1) {
                fIngresarEmpleados.chkActivo.setSelected(true);
                fIngresarEmpleados.chkActivo.setText("Empleado Activo");
            } else {
                fIngresarEmpleados.chkActivo.setSelected(false);
                fIngresarEmpleados.chkActivo.setText("Empleado No Activo");
            }

//            ImageIcon filfoto = fIngresarEmpleados.empleado.setImage();
//            fIngresarEmpleados.panelFotografia.setIcon(filfoto);
//            fIngresarEmpleados.panelFotografia.updateUI();
//            //colocarImagen(caso);
//            
            fIngresarEmpleados.panelFotografia.setIcon(fIngresarEmpleados.empleado.getImage());
            fIngresarEmpleados.panelFotografia.updateUI();

//            javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(fIngresarEmpleados.panelFotografia);
//            fIngresarEmpleados.panelFotografia.setLayout(panelImage1Layout);
//            fIngresarEmpleados.colocarImagen();
//            
        } catch (Exception ex) {
            Logger.getLogger(IngresarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al actualizar Campos de Formulario Ingresar empleados" + ex);
            JOptionPane.showInternalMessageDialog(fIngresarEmpleados, "", "Error al actualizar Campos de Formulario Ingresar empleados \n" + ex, 0);
            
        }
        
    }
    
    public boolean asignarValoresEmpleado() {
        
        boolean aceptado = true;
        try {
            
            fIngresarEmpleados.empleado.setCedula(fIngresarEmpleados.txtCedula.getText().trim());
            fIngresarEmpleados.empleado.setNombres(fIngresarEmpleados.txtNombres.getText().trim());
            fIngresarEmpleados.empleado.setApellidos(fIngresarEmpleados.txtApellidos.getText().trim());
            fIngresarEmpleados.empleado.setDireccion(fIngresarEmpleados.txtDireccion.getText().trim());
            fIngresarEmpleados.empleado.setBarrio(fIngresarEmpleados.txtBarrio.getText().trim());
            fIngresarEmpleados.empleado.setTelefonoFijo(fIngresarEmpleados.txtTelefono.getText().trim());
            fIngresarEmpleados.empleado.setTelefonoCelular(fIngresarEmpleados.txtCelular.getText().trim());
            fIngresarEmpleados.empleado.setCelularCorporativo(fIngresarEmpleados.txtCelularCorporativo.getText().trim());
            fIngresarEmpleados.empleado.setEscolaridad(fIngresarEmpleados.txtEscolaridad.getText().trim());
            fIngresarEmpleados.empleado.setLugarNacimiento(fIngresarEmpleados.txtLugarDeNacimiento.getText().trim());
            fIngresarEmpleados.empleado.setNumeroCuenta(fIngresarEmpleados.txtNumeroDeCuenta.getText().trim());
            fIngresarEmpleados.empleado.setEmail(fIngresarEmpleados.txtEmail.getText().trim());
            
            fIngresarEmpleados.empleado.setGenero(fIngresarEmpleados.cbxGenero.getSelectedItem().toString());
            
            fIngresarEmpleados.cbxDepartamentos.getSelectedItem().toString();
            
            for (CCiudades obj : this.ini.getListaDeCiudades()) {
                if (obj.getNombreCiudad().equals(fIngresarEmpleados.txtCiudad.getText())) {
                    fIngresarEmpleados.empleado.setCiudad(obj.getIdCiudad());
                    break;
                }
            }
            
            for (CEstadosCiviles obj : this.ini.getListaDeEstadosCiviles()) {
                if (obj.getNombreEstadoCivil().equals(fIngresarEmpleados.cbxEstadoCivil.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdEstadoCivil(obj.getIdEstadoCivil());
                    break;
                }
            }
            for (CTiposDeSangre obj : this.ini.getListaDeTiposDeSangre()) {
                if (obj.getNombreTipoDeSAngre().equals(fIngresarEmpleados.cbxTipoDeSangre.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdTipoSangre(obj.getIdTipoDeSAngre());
                    break;
                }
            }
            
            for (CCargos obj : this.ini.getListaDeCargos()) {
                if (obj.getNombreCargo().equals(fIngresarEmpleados.cbxCargos.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdCargo(obj.getIdCargo());
                    break;
                }
            }
            
            fIngresarEmpleados.cbxZonas.getSelectedItem().toString();
            
            fIngresarEmpleados.cbxRegionales.getSelectedItem().toString();
            for (CAgencias obj : this.ini.getListaDeAgencias()) {
                if (obj.getNombreAgencia().equals(fIngresarEmpleados.cbxAgencias.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdAgencia(obj.getIdAgencia());
                    break;
                }
            }
            
            for (CCentrosDeCosto obj : this.ini.getListaDeCentrosDeCosto()) {
                if (obj.getNombreCentroDeCosto().equals(fIngresarEmpleados.cbxCentrosDeCosto.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdCentroDeCosto(obj.getIdCentroDeCosto());
                    break;
                }
            }
            
            for (CTiposDeContratosPersonas obj : this.ini.getListaDeTiposContratosPer()) {
                if (obj.getNombreTipoDeContrato().equals(fIngresarEmpleados.cbxTiposDeContrato.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdTipoDeContrato(obj.getIdTipoDeContrato());
                    break;
                }
            }
            
            for (CEntidadesBancarias obj : this.ini.getListaDeEntidadesBancarias()) {
                if (obj.getNombreEntidadBancaria().equals(fIngresarEmpleados.cbxEntidadesBancarias.getSelectedItem().toString())) {
                    fIngresarEmpleados.empleado.setIdBanco(obj.getIdEntidadBancaria());
                    break;
                }
            }
            
            Date dt = new Date();
            dt = this.ini.getFechaSql(fIngresarEmpleados.dateCumpleanios);
            fIngresarEmpleados.empleado.setCumpleanios(dt);
            
            dt = new Date();
            dt = this.ini.getFechaSql(fIngresarEmpleados.dateFechaIngreso);
            fIngresarEmpleados.empleado.setFechaDeIngreso(dt);
            fIngresarEmpleados.empleado.setImage(fIngresarEmpleados.filfoto);
            
            if (fIngresarEmpleados.chkActivo.isSelected()) {
                fIngresarEmpleados.empleado.setEmpleadoActivo(1);
                fIngresarEmpleados.empleado.setActivoPersona(1);
            } else {
                fIngresarEmpleados.empleado.setEmpleadoActivo(0);
                fIngresarEmpleados.empleado.setActivoPersona(0);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(IngresarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return aceptado;
    }
    
    public boolean validar() {
        boolean verificado = true;
        fIngresarEmpleados.mensaje = "";
        try {
            if (fIngresarEmpleados.empleado != null) {
                
                if (fIngresarEmpleados.txtNombres.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha colocado el nombre del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtApellidos.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha colocado los apellidos del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtBarrio.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha colocado la dirección del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtDireccion.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha colocado el barrio donde vive el empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxDepartamentos.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha selecccionado el departamento donde vive el empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.txtCiudad.getText().trim() == "" || fIngresarEmpleados.txtCiudad.getText().trim() == null) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado la ciudad donde vive el empleado" + "  \n";
                    verificado = false;
                }
                boolean encontrado = false;
                for (CCiudades ciu : ini.getListaDeCiudades()) {
                    if (ciu.getNombreCiudad().equals(fIngresarEmpleados.txtCiudad.getText().trim())) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    fIngresarEmpleados.mensaje += "El  nombre de la ciudad no existe en el sistema" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtTelefono.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el telefono del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtCelular.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el celular del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtCelularCorporativo.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el celular corporativo del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtEscolaridad.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el nivel educativo del empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.txtLugarDeNacimiento.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el Lugar de Nacimiento del empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxEstadoCivil.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado el estado civil del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtEmail.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el Email del empleado" + "  \n";
                    verificado = false;
                }
                if (!Inicio.validateEmail(fIngresarEmpleados.txtEmail.getText())) {
                    fIngresarEmpleados.mensaje += "El formato del email no es válido" + "  \n";
                    verificado = false;
                    
                }
                
                if (fIngresarEmpleados.cbxTipoDeSangre.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha selecccionado el tipo de sangre del empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxCargos.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado el cargo del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.cbxZonas.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado la zona de ubicación del empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxRegionales.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado la regional donde está ubicado el empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.cbxAgencias.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha selecccionado la agencia donde está ubicado el empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxCentrosDeCosto.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado el centro de costo al cual pertenece el empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxTiposDeContrato.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleecionado el tipo de contrato del empleado" + "  \n";
                    verificado = false;
                }
                if (fIngresarEmpleados.txtNumeroDeCuenta.getText().isEmpty()) {
                    fIngresarEmpleados.mensaje += "No ha ingresado el número de cuenta del empleado" + "  \n";
                    verificado = false;
                }
                
                if (fIngresarEmpleados.cbxEntidadesBancarias.getSelectedIndex() == -1) {
                    fIngresarEmpleados.mensaje += "No ha seleccionado el Banco donde tiene la cuenta el empleado" + "  \n";
                    verificado = false;
                }
                
                String cadena = fIngresarEmpleados.empleado.getFormatoFotografia();

                //String extension = foto.getAbsolutePath().substring(foto.getAbsolutePath().lastIndexOf(".")); 
                if (fIngresarEmpleados.empleado.getImage() == null) {
                    fIngresarEmpleados.mensaje += "No se ha asignado una imagen (*.jpg), (*.gif), (*.png)  ->>";
                    verificado = false;
                    
                }
                
            } else {
                fIngresarEmpleados.mensaje += "Debe digitar el número de cédula para crear un empleado";
                verificado = false;
            }
            
            System.out.println("Se han validado los datos del empleado... ");
            
        } catch (Exception ex) {
            verificado = false;
        }
        
        return verificado;
    }
    
    public void colocarImagen(String pathFoto) {
        
        fIngresarEmpleados.panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource(pathFoto)));
        fIngresarEmpleados.panelFotografia.updateUI();
        javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(fIngresarEmpleados.panelFotografia);
        fIngresarEmpleados.panelFotografia.setLayout(panelImage1Layout);
        panelImage1Layout.setHorizontalGroup(
                panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 160, Short.MAX_VALUE));
        panelImage1Layout.setVerticalGroup(
                panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 171, Short.MAX_VALUE));
    }
    
    private void refrescar() {
        
        fIngresarEmpleados.jBtnRefrescar.setEnabled(false);
        fIngresarEmpleados.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
        
        fIngresarEmpleados.lblCirculoDeProgreso.setVisible(true);
        fIngresarEmpleados.btnNuevo.setEnabled(false);
        fIngresarEmpleados.jBtnNuevo.setEnabled(false);
        
        ini.setListaDeDepartamentos();
        ini.setListaDeCiudades();
        
        ini.setListaDeEstadosCiviles();
        ini.setListaDeTiposDeSangre();
        ini.setListaDeCargos();
        ini.setListaDeCentrosDeCosto();
        ini.setListaDeEntidadesBancarias();
        ini.setListaDeTiposContratosPer();
        ini.setListaDeAgencias();
        ini.setListaDeZonas();
        ini.setListaDeRegionales();
        
        fIngresarEmpleados.cbxDepartamentos.removeAllItems();
        fIngresarEmpleados.cbxEstadoCivil.removeAllItems();
        fIngresarEmpleados.cbxTipoDeSangre.removeAllItems();
        fIngresarEmpleados.cbxZonas.removeAllItems();
        fIngresarEmpleados.cbxAgencias.removeAllItems();
        fIngresarEmpleados.cbxRegionales.removeAllItems();
        fIngresarEmpleados.cbxCargos.removeAllItems();
        fIngresarEmpleados.cbxCentrosDeCosto.removeAllItems();
        fIngresarEmpleados.cbxCiudades.removeAllItems();
        fIngresarEmpleados.cbxEntidadesBancarias.removeAllItems();
        fIngresarEmpleados.cbxTiposDeContrato.removeAllItems();
        fIngresarEmpleados.cbxZonas.removeAllItems();
        fIngresarEmpleados.cbxGenero.removeAllItems();
        
        for (CEstadosCiviles obj : this.ini.getListaDeEstadosCiviles()) {
            if (obj.getActivoEstadoCivil() == 1) {
                fIngresarEmpleados.cbxEstadoCivil.addItem(obj.getNombreEstadoCivil());
            }
        }
        
        for (CDepartamentos obj : this.ini.getListaDeDepartamentos()) {
            if (obj.getActivoDepartamento() == 1) {
                fIngresarEmpleados.cbxDepartamentos.addItem(obj.getNombreDepartamento());
            }
        }
        
        for (CTiposDeSangre obj : this.ini.getListaDeTiposDeSangre()) {
            if (obj.getActivoTipoDeSangre() == 1) {
                fIngresarEmpleados.cbxTipoDeSangre.addItem(obj.getNombreTipoDeSAngre());
            }
            
        }
        for (CZonas obj : this.ini.getListaDeZonas()) {
            if (obj.getActivoZona() == 1) {
                fIngresarEmpleados.cbxZonas.addItem(obj.getNombreZona());
            }
            
        }
        
        for (CCargos obj : this.ini.getListaDeCargos()) {
            if (obj.getActivoCargo() == 1) {
                fIngresarEmpleados.cbxCargos.addItem(obj.getNombreCargo());
            }
            
        }
        
        for (CCentrosDeCosto obj : this.ini.getListaDeCentrosDeCosto()) {
            if (obj.getactivoCentroDeCosto() == 1) {
                fIngresarEmpleados.cbxCentrosDeCosto.addItem(obj.getNombreCentroDeCosto());
            }
            
        }
        
        for (CEntidadesBancarias obj : this.ini.getListaDeEntidadesBancarias()) {
            if (obj.getActivoEntidadBancaria() == 1) {
                fIngresarEmpleados.cbxEntidadesBancarias.addItem(obj.getNombreEntidadBancaria());
            }
            
        }
        
        for (CTiposDeContratosPersonas obj : this.ini.getListaDeTiposContratosPer()) {
            if (obj.getActivoTipoDeContrato() == 1) {
                fIngresarEmpleados.cbxTiposDeContrato.addItem(obj.getNombreTipoDeContrato());
            }
            
        }
        
        for (CAgencias obj : this.ini.getListaDeAgencias()) {
            if (obj.getActivoAgencia() == 1) {
                fIngresarEmpleados.cbxAgencias.addItem(obj.getNombreAgencia());
            }
            
        }
        
        fIngresarEmpleados.cbxGenero.addItem("MASCULINO");
        fIngresarEmpleados.cbxGenero.addItem("FEMENINO");
        fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);
        fIngresarEmpleados.btnNuevo.setEnabled(true);
        fIngresarEmpleados.jBtnNuevo.setEnabled(true);
        fIngresarEmpleados.jBtnCancel.setEnabled(true);
        fIngresarEmpleados.btnCancelar.setEnabled(true);
        
        fIngresarEmpleados.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));
        fIngresarEmpleados.jBtnRefrescar.setEnabled(true);
        fIngresarEmpleados.moveToFront();
        
    }
    
}
