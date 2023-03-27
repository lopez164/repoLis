/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class HiloRefrescarListadoDeManifiestosSinDescargar implements Runnable {

    FManifestarPedidosHielera_2 fManifestarPedidosHielera_2 = null;
    DescargarFacturas_2 fDescargarFacturas_2 = null;
    Inicio ini;
    List<String> listadeConductoresConManifiestosPendietes= null;

    // ResultSet rst = null;
    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloRefrescarListadoDeManifiestosSinDescargar(Inicio ini, FManifestarPedidosHielera_2 fManifestarPedidosHielera_2) {

        this.ini = ini;
        this.fManifestarPedidosHielera_2 = fManifestarPedidosHielera_2;

    }

    public HiloRefrescarListadoDeManifiestosSinDescargar(Inicio ini, DescargarFacturas_2 fDescargarFacturas_2) {

        this.ini = ini;
        this.fDescargarFacturas_2 = fDescargarFacturas_2;

    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        if (fManifestarPedidosHielera_2 != null) {
            fManifestarPedidosHielera_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
           
            ini.setListaDeManifiestossinDescargar(3, true);
            ini.setListaDeEmpleados("");
            ini.setListaDeVehiculos(0);

//            fManifestarPedidosHielera_2.cargarmanifiestosSinDescargar();

            fManifestarPedidosHielera_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));
        }

        if (fDescargarFacturas_2 != null) {
            fDescargarFacturas_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
            
            listadeConductoresConManifiestosPendietes = ini.getListaDeConductoresConManifiestosPedientes(true);            

            //ini.setListaDeManifiestossinDescargar(3, true);
            ini.setListaDeEmpleados("");
            ini.setListaDeVehiculos(0);
            
           

           // fDescargarFacturas_2.cargarmanifiestosSinDescargar();

            fDescargarFacturas_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));

        }
    }
    
    private void asignarManifiestosaEmpleados(){
        
        
    }

}
