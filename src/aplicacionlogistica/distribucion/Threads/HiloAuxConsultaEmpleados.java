/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloAuxConsultaEmpleados implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    int nivelDeAcceso;
    Inicio ini;
    ArrayList<CEmpleados> arrEmpleados = null;
    ReporteEmpleados form;
    CUsuarios user;
   
    /**
     * Constructor de clase
     */
    public HiloAuxConsultaEmpleados( Inicio ini,int tiempo, ReporteEmpleados form) throws Exception {
        this.tiempo = tiempo;
        this.form = form;
        this.ini=ini;
        this.nivelDeAcceso=form.getUser().getNivelAcceso();
        this.user=ini.getUser();
        

    }

    @Override
    public void run() {
//        try {
//             arrEmpleados = new ArrayList();
//             CEmpleados empleado;
//             String usuario=Inicio.deCifrar(user.getNombreUsuario());
//             ArrayList<CEmpleados.IdEmpleados> lista = null ;
//             
//            switch(user.getNivelAcceso()){
//                case 1:
//                     
//                    break;
//                case 2:
//                      empleado = new CEmpleados(ini);
//                 lista = new ArrayList();
//                lista = empleado.listadoDeTodosLosEmpleados(1);// 1=  SERVIDOR LOCAL; 2=SERVIDOR REMOTO
//                    break;
//                case 3:
//                      empleado = new CEmpleados(ini);
//                 lista = new ArrayList();
//                lista = empleado.listadoLosEmpleadosDelaZona(1,user.getZona());
//                    break;
//                case 4:
//                      empleado = new CEmpleados(ini);
//                 lista = new ArrayList();
//                lista = empleado.listadoLosEmpleadosDelaRegional(1,user.getRegional());
//                    break;
//                case 5:
//                      empleado = new CEmpleados(ini);
//                 lista = new ArrayList();
//                lista = empleado.listadoLosEmpleadosDelaAgencia(1,user.getAgencia());
//                    break;
//                default:
//                    break;
//                         
//            }
//            int k = 1;
//            int j = 0;
//            double l = 0.0;
//            try {
//               
//               
//                double m = l;
//                j = lista.size();
//                l = 10.0 / (double) j;
//               
//                for (CEmpleados obj : ini.getListaDeEmpleados()) {
//                    System.out.println("Haciendo algo divertido...(Empleados) -> " + k);
//                    
//                    arrEmpleados.add(obj);
//                    k++;
//                    Thread.sleep(1);
//                    m = m + l;
//                }
//               
//                
//                Thread.sleep(1);
//                band = true;
//                form.setListaDeEmpleados(arrEmpleados);
//   
//            } // fin try
//            catch (InterruptedException e) {
//                System.err.println(e.getMessage());
//                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
//                band = true;
//            } catch (Exception ex) {
//                Logger.getLogger(HiloAuxConsultaEmpleados.class.getName()).log(Level.SEVERE, null, ex);
//            }  
//        } // fin try
// catch (Exception ex) {
//            Logger.getLogger(HiloAuxConsultaEmpleados.class.getName()).log(Level.SEVERE, null, ex);
//        }  
   }
}
