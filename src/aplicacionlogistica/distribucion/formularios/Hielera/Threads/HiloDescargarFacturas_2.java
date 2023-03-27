/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloDescargarFacturas_2 implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    DescargarFacturas_2 fDescargarFacturas_2 = null;
    String caso;
    

    String senteciaSqlFacturasDescargadas = null;
    String senteciaSqlProductosPorFacturaDescargados = null;
    String senteciaSqlSoportesConsignaciones = null;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloDescargarFacturas_2(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param comando
     */
    public HiloDescargarFacturas_2(Inicio ini, DescargarFacturas_2 fDescargarFacturas_2, String comando) {
        this.ini = ini;
        this.fDescargarFacturas_2 = fDescargarFacturas_2;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "refrescarFormularios":
                        refrescarFormularios();
                        break;
                        
                    default:
                        JOptionPane.showInternalMessageDialog(fDescargarFacturas_2, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                    

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloDescargarFacturas_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void refrescarFormularios(){
       // String fecha= null;  
        try {
            
            fDescargarFacturas_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
          
            fDescargarFacturas_2.listaDeConductoresConManfiestos =  new ArrayList<>();
            fDescargarFacturas_2.listaDeConductores= ini.getListaDeConductoresConManifiestosPedientes(true);
          
           
            for (String cadena : fDescargarFacturas_2.listaDeConductores) {
                String[] arrSplit = cadena.split(",");
                String conductor = arrSplit[0];
                fDescargarFacturas_2.fechaDistribucion=arrSplit[3];
                CEmpleados empleado = new CEmpleados(ini);
                for(CEmpleados emp : ini.getListaDeEmpleados()){
                    if(emp.getCedula().equals(conductor)){
                        empleado = emp;
                        break;
                    }
                }
                
                empleado.setListaDeFacturaPorConductor(arrSplit[3]); 
                empleado.setListaFacturasDescargadas(arrSplit[3],false);
                empleado.setListaFacturasPendientesPorDescargar();
               //empleado.setEscolaridad(fecha); // esta es la fecha de distribucion
               
               
                fDescargarFacturas_2.listaDeConductoresConManfiestos.add(empleado);
                 Thread.sleep(10);
            }
           // new Thread(new HiloListadoDeManifiestosSinDescargar(this.ini, 3, true)).run();
            ini.setListaDeManifiestossinDescargar(3, true);

            /*Atualiza el listado de vehiculos de la BBDD*/
            //new Thread(new HiloListadoDePlacas(ini)).start();
            // lblBarraDeProgreso.setVisible(false);
            // listaDeManifiestos = new ArrayList<>();
            
            cargarmanifiestosSinDescargar();
            
           fDescargarFacturas_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));


        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     private void refrescarFormularios22(){
       // String fecha= null;  
        try {
            
            fDescargarFacturas_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
          
            fDescargarFacturas_2.listaDeConductoresConManfiestos =  new ArrayList<>();
            fDescargarFacturas_2.listaDeConductores= ini.getListaDeConductoresConManifiestosPedientes(true);
          
           
            for (String cadena : fDescargarFacturas_2.listaDeConductores) {
                String[] arrSplit = cadena.split(",");
                String conductor = arrSplit[0];
                fDescargarFacturas_2.fechaDistribucion=arrSplit[3];
                CEmpleados empleado = new CEmpleados(ini);
                for(CEmpleados emp : ini.getListaDeEmpleados()){
                    if(emp.getCedula().equals(conductor)){
                        empleado = emp;
                        break;
                    }
                }
                
                empleado.setListaDeFacturaPorConductor(arrSplit[3]); 
                empleado.setListaFacturasDescargadas(arrSplit[3],false);
                empleado.setListaFacturasPendientesPorDescargar();
               //empleado.setEscolaridad(fecha); // esta es la fecha de distribucion
               
               
                fDescargarFacturas_2.listaDeConductoresConManfiestos.add(empleado);
                 Thread.sleep(10);
            }
           // new Thread(new HiloListadoDeManifiestosSinDescargar(this.ini, 3, true)).run();
            

            /*Atualiza el listado de vehiculos de la BBDD*/
            //new Thread(new HiloListadoDePlacas(ini)).start();
            // lblBarraDeProgreso.setVisible(false);
            // listaDeManifiestos = new ArrayList<>();
            
            cargarmanifiestosSinDescargar();
            
           fDescargarFacturas_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));


        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void cargarmanifiestosSinDescargar() throws InterruptedException {
        
       if(fDescargarFacturas_2.jListManifiestos.getModel().getSize()>1){
       DefaultListModel listModel =(DefaultListModel) fDescargarFacturas_2.jListManifiestos.getModel();
       listModel.removeAllElements();
       }
        fDescargarFacturas_2.listaDeFormulariosManifiestos = new ArrayList<>();
        fDescargarFacturas_2.listaDeConductores = new ArrayList();
        
        if(fDescargarFacturas_2.fliquidarManifiestos != null){
         fDescargarFacturas_2.fliquidarManifiestos.setVisible(false);
        } 
        
        for (CManifiestosDeDistribucion manifiesto : ini.getListaDeManifiestossinDescargar()) {
            //conductor.setListaDeFacturaPorManifiesto();
            //conductor.setListaFacturasDescargadas();
           // conductor.setListaFacturasPendientesPorDescargar();
           String nombres = (manifiesto.getNombreConductor()+ " " + manifiesto.getApellidosConductor());
           if(nombres.length() >=26){
               nombres = (manifiesto.getNombreConductor()+ " " + manifiesto.getApellidosConductor()).substring(0, 25);
           }
            //this.listaDeConductores.add(conductor.getNombres()+ " " + conductor.getApellidos() + "-" + conductor.getEscolaridad());
            // fDescargarFacturas_2.listaDeConductores.add(nombres + "-" + conductor.getEscolaridad());
            // fDescargarFacturas_2.listaDeConductores.add(nombres + "-" + fDescargarFacturas_2.fechaDistribucion.replace("-", ""));
             fDescargarFacturas_2.listaDeConductores.add(nombres + "-" + manifiesto.getNumeroManifiesto());
             
             //fDescargarFacturas_2.fliquidarManifiestos = new FliquidarManifiestos(this.ini,fDescargarFacturas_2, manifiesto, fDescargarFacturas_2.fechaDistribucion);
             
             fDescargarFacturas_2.fliquidarManifiestos = new FliquidarManifiestos(this.ini,fDescargarFacturas_2, manifiesto);

             
             fDescargarFacturas_2.jPanel1.add(fDescargarFacturas_2.fliquidarManifiestos);
             fDescargarFacturas_2.fliquidarManifiestos.setVisible(false);
                    //fDescargarFacturasHielera.nuevo();
              fDescargarFacturas_2.fliquidarManifiestos.moveToFront();
              fDescargarFacturas_2.fliquidarManifiestos.txtNumeroDeFactura.setEnabled(true);
              fDescargarFacturas_2.fliquidarManifiestos.txtNumeroDeFactura.setEditable(true);
                    
                   // fliquidarManifiestos.llenarjTableFacturasDescargadas();
                    
                    //  FpnlSalidaRutaLaHielera fpnlSalidaRutaLaHielera = new FpnlSalidaRutaLaHielera();
              fDescargarFacturas_2.listaDeFormulariosManifiestos.add(fDescargarFacturas_2.fliquidarManifiestos);
              Thread.sleep(10);
                    
        }
        anexarElementosAlJlist();
        
//        if (this.ini.getListaDeManifiestossinDescargar() != null || this.ini.getListaDeManifiestossinDescargar().size() > 0) {
//            
//            for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {
//                
//                try {
//                    manifiesto.setListaFacturasPorManifiesto();
//                    manifiesto.setListaFacturasDescargadas();
//                    manifiesto.setListaFacturasPendientesPorDescargar();
//                    
//                    this.listaDeConductores.add(manifiesto.getNombreConductor() + " " + manifiesto.getApellidosConductor() + "-" + manifiesto.getVehiculo());
//                    
//                    CEmpleados conductor = new CEmpleados(ini, manifiesto.getConductor());
//                    
//                    fliquidarManifiestos = new FliquidarManifiestos(this.ini,this, conductor);
//                    
//                    jPanel1.add(fliquidarManifiestos);
//                    fliquidarManifiestos.setVisible(false);
//                    //fDescargarFacturasHielera.nuevo();
//                    fliquidarManifiestos.moveToFront();
//                    fliquidarManifiestos.txtNumeroDeFactura.setEnabled(true);
//                    fliquidarManifiestos.txtNumeroDeFactura.setEditable(true);
//                    
//                   // fliquidarManifiestos.llenarjTableFacturasDescargadas();
//                    
//                    //  FpnlSalidaRutaLaHielera fpnlSalidaRutaLaHielera = new FpnlSalidaRutaLaHielera();
//                    listaDeFormulariosManifiestos.add(fliquidarManifiestos);
//                    
//                } catch (Exception ex) {
//                    Logger.getLogger(DescargarFacturas_2.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            this.anexarElementosAlJlist();
//        }
    }
     
      public void anexarElementosAlJlist() {

        // listaDeConductores = new ArrayList<>();
        final DefaultListModel model = new DefaultListModel();
        for (String obj : fDescargarFacturas_2.listaDeConductores) {

            model.addElement(obj);
        }
        fDescargarFacturas_2.jListManifiestos.setModel(model);

    }
}
