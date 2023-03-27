/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos;

import aplicacionlogistica.configuracion.Inicio;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mtto.documentos.objetos.DocumentosPorTipoDeVehiculo;
import mtto.documentos.objetos.DocumentosPorVehiculo;
import mtto.proveedores.Cproveedores;
import mtto.proveedores.HiloIngresarProveedores;
import mtto.vehiculos.Administracion.CTiposDeDocumentos;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloFIngresarDocumentosVehiculos implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    public FIngresarDocumentosVehiculos fIngresarDocumentosVehiculos;
    Inicio ini;
    String caso = "";

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     * @param comando
     */
    public HiloFIngresarDocumentosVehiculos(Inicio ini, FIngresarDocumentosVehiculos form, String comando) {

        this.ini = ini;
        this.fIngresarDocumentosVehiculos = form;
        this.caso = comando;

    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "grabarDocumentos":
                        grabarDocumentos();

                        break;
                    case "cargarLaVista":
                        cargarLaVista();
                        break;

                    case "consultarCarro":
                        consultarCarro();
                        break;

                    case "consultarDocumento":
                        consultarDocumento();
                        break;

                          case "refrescar":
                        refrescar();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fIngresarDocumentosVehiculos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

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

    public void grabarDocumentos() {

        this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        asignarValoresDocumento();
        fIngresarDocumentosVehiculos.habilitar(false);
        this.fIngresarDocumentosVehiculos.estaOcupadoGrabando = true;
        //this.fIngresarDocumentosVehiculos.asignarValoresEmpleado();
        fIngresarDocumentosVehiculos.btnAgregarPdf.setEnabled(false);
        fIngresarDocumentosVehiculos.btnNuevo.setEnabled(false);
        fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(false);

        if (fIngresarDocumentosVehiculos.actualizar) {
            if (fIngresarDocumentosVehiculos.documento.insertarDocumento()) {
                try {
                    fIngresarDocumentosVehiculos.actualizar = false;
                    this.fIngresarDocumentosVehiculos.estaOcupadoGrabando = false;
                    fIngresarDocumentosVehiculos.vehiculo.setListaDeDocumentosPorPlaca();
                    fIngresarDocumentosVehiculos.limpiarTabla2();
                    fIngresarDocumentosVehiculos.llenarTabla2();
                    fIngresarDocumentosVehiculos.habilitar(false);
                    fIngresarDocumentosVehiculos.btnImprir.setEnabled(true);
                    fIngresarDocumentosVehiculos.btnGrabar.setEnabled(false);
                    fIngresarDocumentosVehiculos.btnNuevo.setEnabled(true);
                    fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(true);

                    this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "El documento ha sido Actualizado satisfactoriamente", "ok", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {

                    this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "Error al guardar documento", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "Error al actualizar  documento", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            };

        } else {
            if (!fIngresarDocumentosVehiculos.documento.grabarDocumentoPorVehiculo(1)) {
                this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "Error al guardar documento", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            } else {

                try {
                    fIngresarDocumentosVehiculos.actualizar = false;
                    this.fIngresarDocumentosVehiculos.estaOcupadoGrabando = false;
                    fIngresarDocumentosVehiculos.vehiculo.setListaDeDocumentosPorPlaca();
                    fIngresarDocumentosVehiculos.habilitar(false);
                    fIngresarDocumentosVehiculos.limpiarTabla2();
                    fIngresarDocumentosVehiculos.llenarTabla2();
                    fIngresarDocumentosVehiculos.btnImprir.setEnabled(true);
                    fIngresarDocumentosVehiculos.btnGrabar.setEnabled(false);

                    fIngresarDocumentosVehiculos.btnNuevo.setEnabled(true);
                    fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(true);
                    this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "El documento ha sido guardado satisfactoriamente", "ok", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    this.fIngresarDocumentosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showInternalMessageDialog(this.fIngresarDocumentosVehiculos, "Error al guardar documento", "Error al guardar", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void cargarLaVista() {
        try {
            fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(false);
            fIngresarDocumentosVehiculos.user = ini.getUser();

            if (ini.getListaDeCarrosPropios() == null) {
                fIngresarDocumentosVehiculos.btnNuevo.setEnabled(false);
                fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(false);
                fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(true);
                fIngresarDocumentosVehiculos.habilitar(false);
                ini.setListaDeCarrosPropios();

                fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(false);
                fIngresarDocumentosVehiculos.btnNuevo.setEnabled(true);
                fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(true);

            }

            fIngresarDocumentosVehiculos.cbxTipoDocumento.removeAllItems();
//            if(ini.getListaDeTiposDeDocumentos() == null || ini.getListaDeTiposDeDocumentos().size()== 0){
//                ini.setListaDeTiposDeDocumentos();
//            }
//            
//            for (CTiposDeDocumentos obj : ini.getListaDeTiposDeDocumentos()) {
//                fIngresarDocumentosVehiculos.cbxTipoDocumento.addItem(obj.getNombreTipoDocumento());
//            }

            fIngresarDocumentosVehiculos.autoTxtVehiculos = new TextAutoCompleter(fIngresarDocumentosVehiculos.txtPlaca);
            for (CVehiculos car : this.ini.getListaDeCarrosPropios()) {
                if (car.getActivoVehiculo() == 1) {
                    fIngresarDocumentosVehiculos.autoTxtVehiculos.addItem(car.getPlaca() + " ");
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void consultarCarro() throws Exception {
        try {
            this.fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(true);
            if (!fIngresarDocumentosVehiculos.txtPlaca.getText().isEmpty()) {
                fIngresarDocumentosVehiculos.limpiarTabla1();
                fIngresarDocumentosVehiculos.limpiarTabla2();
                for (CVehiculos obj : ini.getListaDeCarrosPropios()) {
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
                
                this.fIngresarDocumentosVehiculos.cbxTipoDocumento.removeAllItems();
                
                for(DocumentosPorTipoDeVehiculo doc : fIngresarDocumentosVehiculos.vehiculo.getListaDeDocumentosPorTipoDeVehiculo()){
                  this.fIngresarDocumentosVehiculos.cbxTipoDocumento.addItem(doc.getNombreTipoDocumento());
                }
                
                this.fIngresarDocumentosVehiculos.llenarTabla1();
                this.fIngresarDocumentosVehiculos.llenarTabla2();
                fIngresarDocumentosVehiculos.habilitar(true);
                this.fIngresarDocumentosVehiculos.btnGrabar.setEnabled(false);
                this.fIngresarDocumentosVehiculos.txtPlaca.setEnabled(false);
            }
            this.fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(false);

        } catch (Exception ex) {
            Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void consultarDocumento() {
        this.fIngresarDocumentosVehiculos.documento = new DocumentosPorVehiculo(ini);
        this.fIngresarDocumentosVehiculos.btnImprir.setEnabled(false);
        this.fIngresarDocumentosVehiculos.jbtnImprimir.setEnabled(false);

        int fila = this.fIngresarDocumentosVehiculos.tblDocumentosPorVehiculo.getSelectedRow();
        this.fIngresarDocumentosVehiculos.txtRutaArchivo.setText("");
        fIngresarDocumentosVehiculos.habilitar(false);

        if (this.fIngresarDocumentosVehiculos.tblDocumentosPorVehiculo.getValueAt(fila, 3).equals("PENDIENTE") || this.fIngresarDocumentosVehiculos.tblDocumentosPorVehiculo.getValueAt(fila, 4).equals("VENCIDO")) {
            this.fIngresarDocumentosVehiculos.btnAgregarPdf.setEnabled(false);
            this.fIngresarDocumentosVehiculos.btnImprir.setEnabled(false);
            this.fIngresarDocumentosVehiculos.actualizar = false;

            this.fIngresarDocumentosVehiculos.txtQuienEmite.setEnabled(true);
            this.fIngresarDocumentosVehiculos.dateFechaExpedicion.setEnabled(true);
            this.fIngresarDocumentosVehiculos.txtNumeroDocumento.setEnabled(true);
            this.fIngresarDocumentosVehiculos.txtLugarDeExpedicion.setEnabled(true);
            this.fIngresarDocumentosVehiculos.dateFechaVencimiento.setEnabled(true);

            this.fIngresarDocumentosVehiculos.txtQuienEmite.setEditable(true);
            this.fIngresarDocumentosVehiculos.txtNumeroDocumento.setEditable(true);
            this.fIngresarDocumentosVehiculos.txtLugarDeExpedicion.setEditable(true);

            this.fIngresarDocumentosVehiculos.txtQuienEmite.setText("");
            this.fIngresarDocumentosVehiculos.txtNumeroDocumento.setText("");
            this.fIngresarDocumentosVehiculos.txtLugarDeExpedicion.setText("");
            this.fIngresarDocumentosVehiculos.dateFechaExpedicion.setDate(new Date());

            this.fIngresarDocumentosVehiculos.colocarFechaDeVencimiento();

            this.fIngresarDocumentosVehiculos.txtQuienEmite.requestFocus();

            for (DocumentosPorVehiculo dxv : this.fIngresarDocumentosVehiculos.vehiculo.getListaDeDocumentosPorVehiculo()) {
                if (this.fIngresarDocumentosVehiculos.tblDocumentosPorVehiculo.getValueAt(fila, 2).equals(dxv.getNombreTipoDocumento())) {
                    this.fIngresarDocumentosVehiculos.documento = dxv;
                    this.fIngresarDocumentosVehiculos.cbxTipoDocumento.setSelectedItem(dxv.getNombreTipoDocumento());

                    break;
                }
            }

        } else if (this.fIngresarDocumentosVehiculos.tblDocumentosPorVehiculo.getValueAt(fila, 4).equals("VIGENTE")) {
            this.fIngresarDocumentosVehiculos.btnAgregarPdf.setEnabled(true);
            this.fIngresarDocumentosVehiculos.btnImprir.setEnabled(true);
            this.fIngresarDocumentosVehiculos.jbtnImprimir.setEnabled(true);
            this.fIngresarDocumentosVehiculos.actualizar = true;
            this.fIngresarDocumentosVehiculos.btnImprir.setEnabled(true);
            this.fIngresarDocumentosVehiculos.jbtnImprimir.setEnabled(true);

            for (DocumentosPorVehiculo dxv : this.fIngresarDocumentosVehiculos.vehiculo.getListaDeDocumentosPorVehiculo()) {
                if (this.fIngresarDocumentosVehiculos.tblDocumentosPorVehiculo.getValueAt(fila, 2).equals(dxv.getNombreTipoDocumento())) {
                    this.fIngresarDocumentosVehiculos.documento = dxv;
                    this.fIngresarDocumentosVehiculos.cbxTipoDocumento.setSelectedItem(dxv.getNombreTipoDocumento());
                    this.fIngresarDocumentosVehiculos.txtQuienEmite.setText(dxv.getEntidadEmisora());

                    // dateFechaExpedicion.setDateFormatString((dxv.getFechaExpedicion()));
                    this.fIngresarDocumentosVehiculos.dateFechaExpedicion.setDate(Inicio.getFechaSql(dxv.getFechaExpedicion().replace("-", "/")));
                    this.fIngresarDocumentosVehiculos.txtNumeroDocumento.setText(dxv.getNumeroDocumento());
                    this.fIngresarDocumentosVehiculos.txtLugarDeExpedicion.setText(dxv.getLugarExpedicion());

                    this.fIngresarDocumentosVehiculos.dateFechaVencimiento.setDate(Inicio.getFechaSql(dxv.getFechaVencimiento().replace("-", "/")));

                    break;
                }
            }
        }
    }

    public boolean asignarValoresDocumento() {
        boolean asignado = false;
        fIngresarDocumentosVehiculos.documento = new DocumentosPorVehiculo(ini);

        int i = 0;
        fIngresarDocumentosVehiculos.documento.setPlaca(fIngresarDocumentosVehiculos.txtPlaca.getText().trim());
        for (CTiposDeDocumentos td : ini.getListaDeTiposDeDocumentos()) {
            if (fIngresarDocumentosVehiculos.cbxTipoDocumento.getSelectedItem().toString().equals(td.getNombreTipoDocumento())) {
                fIngresarDocumentosVehiculos.documento.setIdTipoDocumento(td.getIdtiposDocumentos());
                fIngresarDocumentosVehiculos.documento.setPrefijo(td.getFormato());
                break;
            }
        }

        fIngresarDocumentosVehiculos.documento.setNumeroDocumento(fIngresarDocumentosVehiculos.txtNumeroDocumento.getText().trim());
        fIngresarDocumentosVehiculos.documento.setEntidadEmisora(fIngresarDocumentosVehiculos.txtQuienEmite.getText().trim());

        Date dt = new Date();
        dt = this.ini.getFechaSql(fIngresarDocumentosVehiculos.dateFechaExpedicion);
        fIngresarDocumentosVehiculos.documento.setFechaExpedicion("" + dt);

        fIngresarDocumentosVehiculos.documento.setLugarExpedicion(fIngresarDocumentosVehiculos.txtLugarDeExpedicion.getText().trim());
        fIngresarDocumentosVehiculos.documento.setSoporteDocumento(fIngresarDocumentosVehiculos.fileDocumento);

        dt = new Date();
        dt = this.ini.getFechaSql(fIngresarDocumentosVehiculos.dateFechaVencimiento);
        fIngresarDocumentosVehiculos.documento.setFechaVencimiento("" + dt);

        fIngresarDocumentosVehiculos.documento.setActivo(1);
        asignado = true;

        return asignado;
    }

    private void refrescar(){
        fIngresarDocumentosVehiculos.jBtnRefrescar.setEnabled(false);
        fIngresarDocumentosVehiculos.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
        
        try {
            
             ini.setListaDeCarrosPropios();
             ini.setListaDeTiposDeDocumentos();
             fIngresarDocumentosVehiculos.cbxTipoDocumento.removeAllItems();
             
            
            fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(false);
            fIngresarDocumentosVehiculos.user = ini.getUser();

            if (ini.getListaDeCarrosPropios() == null) {
                fIngresarDocumentosVehiculos.btnNuevo.setEnabled(false);
                fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(false);
                fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(true);
                fIngresarDocumentosVehiculos.habilitar(false);
                ini.setListaDeCarrosPropios();

                fIngresarDocumentosVehiculos.lblCirculoDeProgreso.setVisible(false);
                fIngresarDocumentosVehiculos.btnNuevo.setEnabled(true);
                fIngresarDocumentosVehiculos.jBtnNuevo.setEnabled(true);

            }

            fIngresarDocumentosVehiculos.cbxTipoDocumento.removeAllItems();
            
//            if(ini.getListaDeTiposDeDocumentos() == null || ini.getListaDeTiposDeDocumentos().size()== 0){
//                ini.setListaDeTiposDeDocumentos();
//            }
//            for (CTiposDeDocumentos obj : ini.getListaDeTiposDeDocumentos()) {
//                fIngresarDocumentosVehiculos.cbxTipoDocumento.addItem(obj.getNombreTipoDocumento());
//            }

            fIngresarDocumentosVehiculos.autoTxtVehiculos = new TextAutoCompleter(fIngresarDocumentosVehiculos.txtPlaca);
            for (CVehiculos car : this.ini.getListaDeCarrosPropios()) {
                if (car.getActivoVehiculo() == 1) {
                    fIngresarDocumentosVehiculos.autoTxtVehiculos.addItem(car.getPlaca() + " ");
                }
            }
            
            
        fIngresarDocumentosVehiculos.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));
        fIngresarDocumentosVehiculos.jBtnRefrescar.setEnabled(true);


        } catch (Exception ex) {
            Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
