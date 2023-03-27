/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeVehiculos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.proveedores.HiloIngresarProveedores;
import mtto.vehiculos.Administracion.CLineasPorMarca;
import mtto.vehiculos.Administracion.CMarcasDeVehiculos;
import mtto.vehiculos.Administracion.CTiposDeCarrocerias;
import mtto.vehiculos.Administracion.CTiposDeCombustibles;
import mtto.vehiculos.Administracion.CTiposDeContratosVehiculos;
import mtto.vehiculos.Administracion.CTiposDeServicio;
import mtto.vehiculos.Administracion.CTiposDeVehiculos;
import mtto.vehiculos.CCarros;

/**
 *
 * @author Usuario
 */
public class HiloIngresarCarros implements Runnable {

    Inicio ini = null;
    IngresarCarros fIngresarCarros = null;
    String caso;

    int idCargo = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIngresarCarros(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param IngresarCarros
     * @param comando
     */
    public HiloIngresarCarros(Inicio ini, IngresarCarros IngresarCarros, String comando) {
        this.ini = ini;
        this.fIngresarCarros = IngresarCarros;
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

                    case "consultarCarro":
                        consultarCarro();
                        break;
                    
                    case "refrescar":
                        refrescar();
                        break;
                        
                        
                    default:
                        JOptionPane.showInternalMessageDialog(fIngresarCarros, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }

               
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloIngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardar() {

        this.fIngresarCarros.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        this.fIngresarCarros.estaOcupadoGrabando = true;
        asignarValoresCarro();
         if (validar()) {
       
            this.fIngresarCarros.habilitar(false);
            if (fIngresarCarros.carro.grabarCarros()) {

                new Thread(new HiloListadoDeVehiculos(this.ini)).start();

                this.fIngresarCarros.estaOcupadoGrabando = false;
                this.fIngresarCarros.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

                JOptionPane.showInternalMessageDialog(fIngresarCarros, "El registro del Carro ha sido guardado perfectamente", "Registro guardado", 1);
                this.fIngresarCarros.actualizarFoto = false;
                this.fIngresarCarros.btnGrabar.setEnabled(false);
                this.fIngresarCarros.jBtnGrabar.setEnabled(false);

            } else {
                 this.fIngresarCarros.habilitar(true);
                this.fIngresarCarros.estaOcupadoGrabando = false;
                this.fIngresarCarros.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

                JOptionPane.showInternalMessageDialog(fIngresarCarros, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                this.fIngresarCarros.btnGrabar.setEnabled(true);
                 this.fIngresarCarros.jBtnGrabar.setEnabled(true);

            }

        } else {
            
            this.fIngresarCarros.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fIngresarCarros, fIngresarCarros.mensaje, "Error en asignacion de Datos", JOptionPane.INFORMATION_MESSAGE);

        }

    }

    public void cargarLaVista() {
        ini.setListaDeAgencias();
        
        fIngresarCarros.cbxAgencias.removeAllItems();
        fIngresarCarros.cbxLineaVehiculo.removeAllItems();
        fIngresarCarros.cbxMarcaVehiculo.removeAllItems();
        fIngresarCarros.cbxTipoCarroceria.removeAllItems();
        fIngresarCarros.cbxTipoCombustible.removeAllItems();
        fIngresarCarros.cbxTipoDeServicio.removeAllItems();
        fIngresarCarros.cbxTipoVehiculo.removeAllItems();
        fIngresarCarros.cbxTiposContratos.removeAllItems();

        for (CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()) {
            fIngresarCarros.cbxMarcaVehiculo.addItem(obj.getNombreMarcaDeVehiculos());
        }

        for (CTiposDeVehiculos obj : ini.getListaDeTiposDeVehiculos()) {
            fIngresarCarros.cbxTipoVehiculo.addItem(obj.getNombreTipoDeVehiculo());
        }

        for (CTiposDeCarrocerias obj : ini.getListaDeTiposDeCarrocerias()) {
            fIngresarCarros.cbxTipoCarroceria.addItem(obj.getNombreCarroceria());
        }

        for (CTiposDeServicio obj : ini.getListaDeTiposDeServicios()) {
            fIngresarCarros.cbxTipoDeServicio.addItem(obj.getNombreTipoDeServicio());
        }

        for (CTiposDeCombustibles obj : ini.getListaDeTiposDeCombustibles()) {
            fIngresarCarros.cbxTipoCombustible.addItem(obj.getNombreCombustible());
        }

        for (CAgencias obj : ini.getListaDeAgencias()) {
            fIngresarCarros.cbxAgencias.addItem(obj.getNombreAgencia());
        }

        for (CTiposDeContratosVehiculos obj : ini.getListaDeTiposDeContratosVehiculos()) {
            fIngresarCarros.cbxTiposContratos.addItem(obj.getNombreTipoDeContrato());

        }

        fIngresarCarros.panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
        fIngresarCarros.panelFotografia.updateUI();
        fIngresarCarros.colocarImagen();

    }

    private void consultarCarro() {
        fIngresarCarros.carro = new CCarros(ini);
        fIngresarCarros.carro.setPlaca("");
        try {
            if (!fIngresarCarros.txtPlaca.getText().isEmpty()) {
                fIngresarCarros.lblCirculoDeProgreso1.setVisible(true);

                if (this.ini.getListaDeVehiculos() != null) {
                    for (CCarros obj : this.ini.getListaDeVehiculos()) {
                        if (obj.getPlaca().equals(fIngresarCarros.txtPlaca.getText().trim())) {
                            fIngresarCarros.carro = obj;
                            break;
                        }
                    }

                    // carro = new CCarros(ini, txtPlaca.getText());
                    if (fIngresarCarros.carro.getPlaca().equals("")) { // si el carro no existe

                        fIngresarCarros.habilitar(true);
                        fIngresarCarros.cbxMarcaVehiculo.requestFocus();
                        fIngresarCarros.btnNuevo.setEnabled(false);
                        fIngresarCarros.jBtnNuevo.setEnabled(false);
                        
                        fIngresarCarros.btnGrabar.setEnabled(true);
                         fIngresarCarros.jBtnGrabar.setEnabled(true);

                    } else {  // si el carro existe
                        llenarCamposDeTexto();
                        fIngresarCarros.habilitar(false);
                        fIngresarCarros.actualizar = true;
                        fIngresarCarros.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                        fIngresarCarros.btnNuevo.setText("Actualizar");
                    }

                } else {
                    JOptionPane.showInternalMessageDialog(fIngresarCarros, "Cargando Variables", "vacior", JOptionPane.INFORMATION_MESSAGE);
                }
                fIngresarCarros.cbxMarcaVehiculo.requestFocus();
            } else {
                JOptionPane.showInternalMessageDialog(fIngresarCarros, "Campo de texto vacio", "vacior", JOptionPane.INFORMATION_MESSAGE);

            }
            fIngresarCarros.lblCirculoDeProgreso1.setVisible(false);

        } catch (Exception ex) {
            Logger.getLogger(IngresarCarros.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarCamposDeTexto() {
        try {

            fIngresarCarros.txtPlaca.setText(fIngresarCarros.carro.getPlaca());
            fIngresarCarros.txtModelo.setText("" + fIngresarCarros.carro.getModelo());
            fIngresarCarros.txtPropietario.setText(fIngresarCarros.carro.getPropietario());
            fIngresarCarros.txtPesoSinCarga.setText("" + fIngresarCarros.carro.getPesoTotalSinCarga());
            fIngresarCarros.txtLargo.setText("" + fIngresarCarros.carro.getLargoVehiculo());
            fIngresarCarros.txtAncho.setText("" + fIngresarCarros.carro.getAnchuraVehiculo());
            fIngresarCarros.txtAlto.setText("" + fIngresarCarros.carro.getAlturaVehiculo());
            fIngresarCarros.txtLongitud.setText("" + fIngresarCarros.carro.getLongitudVehiculo());
            fIngresarCarros.txtTarjetaDePropiedad.setText(fIngresarCarros.carro.getTarjetaPropiedad());
            fIngresarCarros.txtCantidadLLantas.setText("" + fIngresarCarros.carro.getCantidadLLantas());
            fIngresarCarros.txtTamanioLLantas.setText(fIngresarCarros.carro.getTamanioLlantas());
            fIngresarCarros.txtSerialChasis.setText(fIngresarCarros.carro.getSerialChasis());
            //txtTrailer.setText(fIngresarCarros.carro.getTrailer());
            fIngresarCarros.txtSerialMotor.setText(fIngresarCarros.carro.getSerialMotor());
            fIngresarCarros.txtKmCambioAceiteMotor.setText("" + fIngresarCarros.carro.getKmCambioAceiteMotor());
            fIngresarCarros.txtKmCambioValvulina.setText("" + fIngresarCarros.carro.getKmCambioValvulinaCaja());
            fIngresarCarros.txtKmCambioValvulinaTrasmision.setText("" + fIngresarCarros.carro.getKmCambioValvulinaTrasmision());
            fIngresarCarros.txtCapacidadInstalad.setText("" + fIngresarCarros.carro.getCapacidadInstalada());
            fIngresarCarros.txtPesoTotalAutorizado.setText("" + fIngresarCarros.carro.getPesoTotalAutorizado());
            fIngresarCarros.txtKmInicial.setText("" + fIngresarCarros.carro.getKmInicial());
            //fIngresarCarros.carro.setLineaVehiculo();

            // _tvh = new CTiposDeVehiculos(ini, fIngresarCarros.carro.getTipoVehiculo());
            for (CTiposDeVehiculos tv : ini.getListaDeTiposDeVehiculos()) {
                if (tv.getIdTipoDeVehiculo() == fIngresarCarros.carro.getTipoVehiculo()) {
                    fIngresarCarros.cbxTipoVehiculo.setSelectedItem(tv.getNombreTipoDeVehiculo());
                }
            }
            // _tcr = new CTiposDeCarrocerias(ini, fIngresarCarros.carro.getTipoCarroceria());
            for (CTiposDeCarrocerias tc : ini.getListaDeTiposDeCarrocerias()) {
                if (tc.getIdCarroceria() == fIngresarCarros.carro.getTipoCarroceria()) {
                    fIngresarCarros.cbxTipoCarroceria.setSelectedItem(tc.getNombreCarroceria());
                }
            }
            // _tsv = new CTiposDeServicio(ini, fIngresarCarros.carro.getTipoServicio());
            for (CTiposDeServicio ts : ini.getListaDeTiposDeServicios()) {
                if (ts.getIdTipoDeServicio() == fIngresarCarros.carro.getTipoServicio()) {
                    fIngresarCarros.cbxTipoDeServicio.setSelectedItem(ts.getNombreTipoDeServicio());
                }
            }

            //_tcb = new CTiposDeCombustibles(ini, fIngresarCarros.carro.getTipoCombustible());
            for (CTiposDeCombustibles tc : ini.getListaDeTiposDeCombustibles()) {
                if (tc.getIdCombustible() == fIngresarCarros.carro.getTipoCombustible()) {
                    fIngresarCarros.cbxTipoCombustible.setSelectedItem(tc.getNombreCombustible());
                }
            }
            // _ag = new CAgencias(ini, fIngresarCarros.carro.getAgencia());
            for (CAgencias ag : ini.getListaDeAgencias()) {
                if (ag.getIdAgencia() == fIngresarCarros.carro.getAgencia()) {
                    fIngresarCarros.cbxAgencias.setSelectedItem(ag.getNombreAgencia());
                }
            }
            // _tcv = new CTiposDeContratosVehiculos(ini, fIngresarCarros.carro.getTipoContrato());
            for (CTiposDeContratosVehiculos tc : ini.getListaDeTiposDeContratosVehiculos()) {
                if (tc.getIdTipoDeContrato() == fIngresarCarros.carro.getTipoContrato()) {
                    fIngresarCarros.cbxTiposContratos.setSelectedItem(tc.getNombreTipoDeContrato());
                }
            }

            //fIngresarCarros.cbxMarcaVehiculo.setSelectedItem(fIngresarCarros.carro.getLineaVehiculo().getNombreMarcaDeVehiculos());
            for (CLineasPorMarca obj : ini.getListaDeLineasPorMarca()) {
                if (obj.getIdlineasVehiculos() == fIngresarCarros.carro.getIdLineaVehiculo()) {
                    fIngresarCarros.cbxMarcaVehiculo.setSelectedItem(obj.getNombreMarcaDeVehiculos());
                    fIngresarCarros.cbxLineaVehiculo.setSelectedItem(obj.getNombreLineaVehiculo());

                }
            }

            if (fIngresarCarros.carro.getActivoCarro() == 1) {
                fIngresarCarros.chkVehiculoActivo.setSelected(true);
                fIngresarCarros.chkVehiculoActivo.setText("Vehiculo Activo");
            } else {
                fIngresarCarros.chkVehiculoActivo.setSelected(false);
                fIngresarCarros.chkVehiculoActivo.setText("Vehiculo No Activo");
            }

            if (fIngresarCarros.carro.getImagen() != null) {
                fIngresarCarros.tieneFoto = true;
            }
            fIngresarCarros.panelFotografia.setIcon(fIngresarCarros.carro.setImagen());
            fIngresarCarros.panelFotografia.updateUI();

            javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(fIngresarCarros.panelFotografia);
            fIngresarCarros.panelFotografia.setLayout(panelImage1Layout);
            fIngresarCarros.colocarImagen();

        } catch (Exception ex) {
            System.out.println("Error al actualizar Campos de Formulario Ingresar cars" + ex);
        }

        fIngresarCarros.lblCirculoDeProgreso1.setVisible(false);

    }
    public void asignarValoresCarro() {

      
       
            
            fIngresarCarros.carro.setPlaca(fIngresarCarros.txtPlaca.getText().trim());
            fIngresarCarros.carro.setTipoDeTracccion(1);
            fIngresarCarros.carro.setConductor("0");
            fIngresarCarros.carro.setPesoTotalSinCarga(Double.parseDouble(fIngresarCarros.txtPesoSinCarga.getText().trim()));
            fIngresarCarros.carro.setLargoVehiculo(Double.parseDouble(fIngresarCarros.txtLargo.getText().trim()));
            fIngresarCarros.carro.setAlturaVehiculo(Double.parseDouble(fIngresarCarros.txtAlto.getText().trim()));
            fIngresarCarros.carro.setAnchuraVehiculo(Double.parseDouble(fIngresarCarros.txtAncho.getText().trim()));
            fIngresarCarros.carro.setLongitudVehiculo(Double.parseDouble(fIngresarCarros.txtLongitud.getText().trim()));
            fIngresarCarros.carro.setPesoTotalAutorizado(Double.parseDouble(fIngresarCarros.txtPesoTotalAutorizado.getText().trim()));
            fIngresarCarros.carro.setCapacidadInstalada(Double.parseDouble(fIngresarCarros.txtCapacidadInstalad.getText().trim()));
            fIngresarCarros.carro.setPropietario(fIngresarCarros.txtPropietario.getText().trim());
            fIngresarCarros.carro.setTarjetaPropiedad(fIngresarCarros.txtTarjetaDePropiedad.getText().trim());
            fIngresarCarros.carro.setCantidadLLantas(Integer.parseInt(fIngresarCarros.txtCantidadLLantas.getText().trim()));
            fIngresarCarros.carro.setTamanioLlantas(fIngresarCarros.txtTamanioLLantas.getText().trim());
            fIngresarCarros.carro.setSerialChasis(fIngresarCarros.txtSerialChasis.getText().trim());
            fIngresarCarros.carro.setTrailer("0");
            fIngresarCarros.carro.setModelo(fIngresarCarros.txtModelo.getText().trim());
            fIngresarCarros.carro.setSerialMotor(fIngresarCarros.txtSerialMotor.getText().trim());
            fIngresarCarros.carro.setKmCambioValvulinaTrasmision(Integer.parseInt(fIngresarCarros.txtKmCambioValvulinaTrasmision.getText().trim()));
            fIngresarCarros.carro.setKmCambioAceiteMotor(Integer.parseInt(fIngresarCarros.txtKmCambioAceiteMotor.getText().trim()));
            fIngresarCarros.carro.setKmCambioValvulinaCaja(Integer.parseInt(fIngresarCarros.txtKmCambioValvulina.getText().trim()));
            fIngresarCarros.carro.setKmInicial(Integer.parseInt(fIngresarCarros.txtKmInicial.getText().trim()));

            int lineaVehiculo = 0;
            for (CLineasPorMarca obj : ini.getListaDeLineasPorMarca()) {
                if (obj.getNombreLineaVehiculo().equals(fIngresarCarros.cbxLineaVehiculo.getSelectedItem().toString())) {
                    lineaVehiculo = obj.getIdlineasVehiculos();
                }
            }

            fIngresarCarros.carro.setIdLineaVehiculo(lineaVehiculo);

            for (CTiposDeContratosVehiculos obj : ini.getListaDeTiposDeContratosVehiculos()) {
                if (obj.getNombreTipoDeContrato().equals(fIngresarCarros.cbxTiposContratos.getSelectedItem().toString())) {
                    fIngresarCarros.carro.setTipoContrato(obj.getIdTipoDeContrato());
                    break;
                }
            }

            for (CAgencias obj : ini.getListaDeAgencias()) {
                if (obj.getNombreAgencia().equals(fIngresarCarros.cbxAgencias.getSelectedItem().toString())) {
                    fIngresarCarros.carro.setAgencia(obj.getIdAgencia());
                    break;
                }
            }

            for (CTiposDeCombustibles obj : ini.getListaDeTiposDeCombustibles()) {
                if (obj.getNombreCombustible().equals(fIngresarCarros.cbxTipoCombustible.getSelectedItem().toString())) {
                    fIngresarCarros.carro.setTipoCombustible(obj.getIdCombustible());
                    break;
                }
            }

            for (CTiposDeVehiculos obj : ini.getListaDeTiposDeVehiculos()) {
                if (obj.getNombreTipoDeVehiculo().equals(fIngresarCarros.cbxTipoVehiculo.getSelectedItem().toString())) {
                    fIngresarCarros.carro.setTipoVehiculo(obj.getIdTipoDeVehiculo());
                    break;
                }
            }

            for (CTiposDeCarrocerias obj : ini.getListaDeTiposDeCarrocerias()) {
                if (obj.getNombreCarroceria().equals(fIngresarCarros.cbxTipoCarroceria.getSelectedItem().toString())) {
                    fIngresarCarros.carro.setTipoCarroceria(obj.getIdCarroceria());
                    break;
                }
            }

            for (CTiposDeServicio obj : ini.getListaDeTiposDeServicios()) {
                if (obj.getNombreTipoDeServicio().equals(fIngresarCarros.cbxTipoDeServicio.getSelectedItem().toString())) {
                    fIngresarCarros.carro.setTipoServicio(obj.getIdTipoDeServicio());
                    break;
                }
            }

            if (fIngresarCarros.chkVehiculoActivo.isSelected()) {
                fIngresarCarros.carro.setActivoCarro(1);
                fIngresarCarros.carro.setActivoVehiculo(1);
            } else {
                fIngresarCarros.carro.setActivoCarro(0);
                fIngresarCarros.carro.setActivoVehiculo(0);
            }
           
        fIngresarCarros.carro.setImagen(fIngresarCarros.foto);

      
    }

    public boolean validar() {
        boolean verificado = true;
        fIngresarCarros.mensaje = "";
        try {
            if (fIngresarCarros.txtPlaca.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado la placa del vehículo" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtModelo.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado el Modelo del vehiculo " + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtModelo.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado la dirección del car" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtPropietario.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado el nombre del propietario del vehículo" + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.txtCapacidadInstalad.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado la capacidad instalada del vehículo" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtLargo.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado el la medida del largo del vehículo" + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.txtAlto.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado el la medida del alto de la  carrocería" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtAncho.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado el la medida del ancho de la  carrocería" + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.txtLongitud.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha colocado el la medida de la Longitud de la  carrocería" + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.txtTarjetaDePropiedad.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el número de la tarjeta de propiedad del vehículo" + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.txtCantidadLLantas.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado la cantidad de llantas del vehículo " + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtTamanioLLantas.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el tamaño de las llantas del vehículo " + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtSerialChasis.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el serial del chasis del vehículo " + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.txtSerialMotor.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el Serial del motor del vehículo " + "  \n";
                verificado = false;
            }

             if (fIngresarCarros.txtPesoSinCarga.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el peso del vehiculo " + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtKmCambioAceiteMotor.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado Kilometraje para cambio de aceite del motor" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtKmCambioValvulina.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado Kilometraje para cambio de aceite de la  caja" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtKmCambioValvulinaTrasmision.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado Kilometraje para cambio de aceite de la trasmisión" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtPesoTotalAutorizado.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el peso total máximo autorizado de cargue " + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.txtKmInicial.getText().isEmpty()) {
                fIngresarCarros.mensaje += "No ha ingresado el Km inicial " + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.cbxLineaVehiculo.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha selecccionado la línea de la marca del vehículo" + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.cbxTipoVehiculo.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha seleccionado el tipo de vehículo" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.cbxTipoCarroceria.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha seleccionado el tipo de carrocería del vehículo " + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.cbxTipoDeServicio.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha seleccionado el tipo de servicio del vehículo " + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.cbxTipoCombustible.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha selecccionado la agencia donde está ubicado el vehículo" + "  \n";
                verificado = false;
            }
            if (fIngresarCarros.cbxTiposContratos.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha selecccionado el tipo de contrato " + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.cbxAgencias.getSelectedIndex() == -1) {
                fIngresarCarros.mensaje += "No ha seleccionado la agencia a la cual pertenence el vehículo " + "  \n";
                verificado = false;
            }

            if (fIngresarCarros.carro.getImagen() == null) {
                fIngresarCarros.mensaje += "No ha seleccionado la fotografía del car" + "  \n";
                verificado = false;
            } else {
                if (fIngresarCarros.carro.getTipoMime() == null) {
                    fIngresarCarros.mensaje += "El formato de la fotografía no es válido debe ser (*.jpg), (*.gif), (*.png)" + "  \n";
                    verificado = false;
                }
            }

        } catch (Exception ex) {
            verificado = false;
        }

        return verificado;
    }
    
     public void refrescar() {
         fIngresarCarros.jBtnRefrescar.setEnabled(false);
         fIngresarCarros.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
         ini.setListaDeAgencias();
         ini.setListaDeMarcasDeVehiculos();
         ini.setListaDeLineasPorMarca();
         ini.setListaDeTiposDeCarrocerias();
         ini.setListaDeTiposDeCombustibles();
         ini.setListaDeTiposDeServicios();
         ini.setListaDeTiposDeVehiculos();
         ini.setListaDeTiposDeContratosVehiculos();
         
         
        fIngresarCarros.cbxAgencias.removeAllItems();
        fIngresarCarros.cbxLineaVehiculo.removeAllItems();
        fIngresarCarros.cbxMarcaVehiculo.removeAllItems();
        fIngresarCarros.cbxTipoCarroceria.removeAllItems();
        fIngresarCarros.cbxTipoCombustible.removeAllItems();
        fIngresarCarros.cbxTipoDeServicio.removeAllItems();
        fIngresarCarros.cbxTipoVehiculo.removeAllItems();
        fIngresarCarros.cbxTiposContratos.removeAllItems();

        for (CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()) {
            fIngresarCarros.cbxMarcaVehiculo.addItem(obj.getNombreMarcaDeVehiculos());
        }

        for (CTiposDeVehiculos obj : ini.getListaDeTiposDeVehiculos()) {
            fIngresarCarros.cbxTipoVehiculo.addItem(obj.getNombreTipoDeVehiculo());
        }

        for (CTiposDeCarrocerias obj : ini.getListaDeTiposDeCarrocerias()) {
            fIngresarCarros.cbxTipoCarroceria.addItem(obj.getNombreCarroceria());
        }

        for (CTiposDeServicio obj : ini.getListaDeTiposDeServicios()) {
            fIngresarCarros.cbxTipoDeServicio.addItem(obj.getNombreTipoDeServicio());
        }

        for (CTiposDeCombustibles obj : ini.getListaDeTiposDeCombustibles()) {
            fIngresarCarros.cbxTipoCombustible.addItem(obj.getNombreCombustible());
        }

        for (CAgencias obj : ini.getListaDeAgencias()) {
            fIngresarCarros.cbxAgencias.addItem(obj.getNombreAgencia());
        }

        for (CTiposDeContratosVehiculos obj : ini.getListaDeTiposDeContratosVehiculos()) {
            fIngresarCarros.cbxTiposContratos.addItem(obj.getNombreTipoDeContrato());

        }

        fIngresarCarros.panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
        fIngresarCarros.panelFotografia.updateUI();
        fIngresarCarros.colocarImagen();
        
        fIngresarCarros.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));
         fIngresarCarros.jBtnRefrescar.setEnabled(true);


    }


}
