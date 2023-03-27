/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRutaConIntegrador;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloCopiarfacturasEnJlist implements Runnable {

    
    Inicio ini;
    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador = null;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloCopiarfacturasEnJlist(FManifestarPedidosEnRuta form) {

        this.fManifestarPedidosEnRuta = form;
      
    } // jslslpzmjC12  jslslpzmjc1212 
    
      /**
     * Constructor de clase
     *
     * @param fManifestarPedidosEnRutaConIntegrador
     */
    public HiloCopiarfacturasEnJlist(FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador) {

        this.fManifestarPedidosEnRutaConIntegrador = fManifestarPedidosEnRutaConIntegrador;
      
    } // jslslpzmjC12  jslslpzmjc1212 
     
    

    @Override
    public void run() {

        ArrayList<String> lista = new ArrayList();
        if (fManifestarPedidosEnRuta != null) {

            fManifestarPedidosEnRuta.lblBarraDeProgreso.setVisible(true);

            /* Se realiza el proceso para verificar que no hayan facturas repetidas*/
            lista = listaDeFacturasNoRepetidas();

            /* Se invoca éste método de tal manera que ya teniendo la lista de las facturas, se le asignan al
         manifiesto actual */
            agregarFacturasDesdeArchivo(lista);
        }
        
        

        if (fManifestarPedidosEnRutaConIntegrador != null) {
            fManifestarPedidosEnRutaConIntegrador.lblBarraDeProgreso.setVisible(true);

            /* Se realiza el proceso para verificar que no hayan facturas repetidas*/
            lista = listaDeFacturasNoRepetidasConIntegrador();

            /* Se invoca éste método de tal manera que ya teniendo la lista de las facturas, se le asignan al
         manifiesto actual */
            agregarFacturasDesdeArchivoConIntegrador(lista);
        }

    }
    
    
    
    private ArrayList<String> listaDeFacturasNoRepetidas() {
        ArrayList<String> cadena = new ArrayList<>();
        boolean aparece = false;
        /* Se recorren todos los elementos del listado de  las facturas en el archivo */
        for (String obj : fManifestarPedidosEnRuta.listaDeFacturasEnElArchivo) {
            aparece = false;

            /* se hace la primera validacion en el que la cadena se encuentra vacía*/
            if (cadena.isEmpty()) {
                cadena.add(obj);

                /* En el caso cuando tiene la cadena mas de un elemento*/
            } else {
                /* Se recorre la segunda lista para validar sí el numero de factura existe */
                for (String obj2 : cadena) {

                    /* Si la factura ya existe en la lista devuelve true para indicar que ya existe el numero de la factura en el array 
                     y sale del bucle para continuar con el siguiente elemento */
                    if (obj.equals(obj2)) {
                        aparece = true;
                        break;
                    }
                }
                /* al salir del bucle se valida que no existe la facura en el segundo array y se agrega el elmento.  */
                if (!aparece) {
                    cadena.add(obj);
                }
            }

        }

        return cadena;

    }
    
     private ArrayList<String> listaDeFacturasNoRepetidasConIntegrador() {
        ArrayList<String> cadena = new ArrayList<>();
        boolean aparece = false;
        /* Se recorren todos los elementos del listado de  las facturas en el archivo */
        for (String obj : fManifestarPedidosEnRutaConIntegrador.listaDeFacturasEnElArchivo) {
            aparece = false;

            /* se hace la primera validacion en el que la cadena se encuentra vacía*/
            if (cadena.isEmpty()) {
                cadena.add(obj);

                /* En el caso cuando tiene la cadena mas de un elemento*/
            } else {
                /* Se recorre la segunda lista para validar sí el numero de factura existe */
                for (String obj2 : cadena) {

                    /* Si la factura ya existe en la lista devuelve true para indicar que ya existe el numero de la factura en el array 
                     y sale del bucle para continuar con el siguiente elemento */
                    if (obj.equals(obj2)) {
                        aparece = true;
                        break;
                    }
                }
                /* al salir del bucle se valida que no existe la facura en el segundo array y se agrega el elmento.  */
                if (!aparece) {
                    cadena.add(obj);
                }
            }

        }

        return cadena;

    }

private boolean agregarFacturasDesdeArchivo(ArrayList<String> lista) {

        String numeroDeFactura;
        int contadorDeFacturas = 0;
//        fManifestarPedidosEnRuta.listaDeNumerosDeFacturasNoEncontrados = null;
        fManifestarPedidosEnRuta.lblBarraDeProgreso.setVisible(true);
        boolean huboError=false;
        
        try {
            Thread.sleep(300);
       
           int adherencia = 1;

            /* Se hace el recorrido por el array para crear los objetos*/
            for (String obj : lista) {
                numeroDeFactura = obj;
//                fManifestarPedidosEnRuta.agregarFactura(numeroDeFactura, true, adherencia);
                adherencia++;
                Thread.sleep(10);

            }
             fManifestarPedidosEnRuta.lblBarraDeProgreso.setVisible(false);
             fManifestarPedidosEnRuta.btnAgregarFacturas.setEnabled(false);
             
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            huboError=true;
        }
        
        return huboError;

    }

private boolean agregarFacturasDesdeArchivoConIntegrador(ArrayList<String> lista) {

        String numeroDeFactura;
        int contadorDeFacturas = 0;
//        fManifestarPedidosEnRuta.listaDeNumerosDeFacturasNoEncontrados = null;
        fManifestarPedidosEnRutaConIntegrador.lblBarraDeProgreso.setVisible(true);
        boolean huboError=false;
        
        try {
            Thread.sleep(300);
       
           int adherencia = 1;

            /* Se hace el recorrido por el array para crear los objetos*/
            for (String obj : lista) {
                numeroDeFactura = obj;
                fManifestarPedidosEnRutaConIntegrador.agregarFactura(numeroDeFactura, true, adherencia);
                adherencia++;
                Thread.sleep(10);

            }
             fManifestarPedidosEnRutaConIntegrador.lblBarraDeProgreso.setVisible(false);
             fManifestarPedidosEnRutaConIntegrador.btnAgregarFacturas.setEnabled(false);
             
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            huboError=true;
        }
        
        return huboError;

    }
}
