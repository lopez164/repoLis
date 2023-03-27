/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.integrador.sincronizacion.Sincronizacion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloIntegrador implements Runnable {

    private JProgressBar jProgressBar;
    private int value;//retardo en milisegundos
    String mensaje = "";

    FIntegrador fIntegrador;

    /**
     * Constructor de clase
     *
     * @param fIntegrador
     */
    public HiloIntegrador(FIntegrador fIntegrador) {
        this.fIntegrador = fIntegrador;

    }

    @Override
    public void run() {

//        try {
//            if (fIntegrador.jRadioButton1.isSelected()) {
//                mensaje += "Incio de la sincronizacion "  + new Date() +" \n" ;
//                fIntegrador.txtLog.setText(mensaje);
//                sincronizacionServidorLocal();
//
//                mensaje += "Fin  de la sincronizacion " +  new Date() +" \n" ;
//                fIntegrador.txtLog.setText(mensaje);
//                JOptionPane.showMessageDialog(null, "Fin de la Sincronizacion local", " Fin Integracion Local ", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//            if (fIntegrador.jRadioButton2.isSelected()) {
//                mensaje += " \n \n Inicio de proceso con servidor Remoto "  + new Date() +" \n" ;
//                fIntegrador.txtLog.setText(mensaje);
//
//                Sincronizacion sincronizar = new Sincronizacion(fIntegrador);
//
//                mensaje += "Fin  de la sincronizacion "  + new Date() +" \n" ;
//                fIntegrador.txtLog.setText(mensaje);
//
//                JOptionPane.showMessageDialog(null, "Fin de la Sincronizacion Remota", " Fin integracion Remota ", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//            if (fIntegrador.jRadioButton3.isSelected()) {
//
//                mensaje += "Inicio de proceso de Sincronzacion "  + new Date() +" \n" ;
//                fIntegrador.txtLog.setText(mensaje);
//                if (sincronizacionServidorLocal()) {
//
//                    Sincronizacion sincronizar = new Sincronizacion(fIntegrador);
//
//                    mensaje += "Fin  de la sincronizacion "  + new Date() +" \n" ;
//                    fIntegrador.txtLog.setText(mensaje);
//                    JOptionPane.showMessageDialog(null, "Fin de la Sincronizacion entre servidores", " Fin integracion ", JOptionPane.INFORMATION_MESSAGE);
//
//                };
//
//            }
//
//            fIntegrador.band = true;
//            fIntegrador.jBtnCirculo.setVisible(false);
//
//        } catch (IOException ex) {
//            Logger.getLogger(HiloIntegrador.class.getName()).log(Level.SEVERE, null, ex);
//            mensaje += ex;
//            fIntegrador.txtLog.setText(mensaje);
//            fIntegrador.jBtnCirculo.setVisible(false);
//            fIntegrador.band = true;
//            JOptionPane.showMessageDialog(null, "Se ha presentado un error en la Sincronizacion entre servidores", " Fin integracion ", JOptionPane.ERROR_MESSAGE);
//        }

    }

    public boolean sincronizacionServidorLocal() throws IOException {
        boolean ejecutado = false;
        String fecha = "" + Inicio.getFechaSql(fIntegrador.jFechaInicial);

        fIntegrador.jBtnCirculo.setVisible(true);
        /* se crea un archivo .bat para ejecutar el comando de la incronizacion de
        el servidor de la BBDD de las facturas al servidor local de logistica*/
        crearArchivo(fecha);

        String commands = "integrador.bat";
        mensaje += commands + "  \n";
        fIntegrador.txtLog.setText(mensaje);
        System.out.println("Inicia proceso de sincronizar");
        mensaje += "Inicia proceso de sincronizar \n";
        fIntegrador.txtLog.setText(mensaje);

        Process proceso = new ProcessBuilder(commands).start(); // se crea el proceso usando los comandos
        InputStreamReader entrada = new InputStreamReader(proceso.getInputStream());
        BufferedReader stdInput = new BufferedReader(entrada);
        int i = 0;
        String salida;
        if ((salida = stdInput.readLine()) != null) {
            System.out.println("Comando ejecutado Correctamente");
            while ((salida = stdInput.readLine()) != null) {
                //System.out.println(salida);
                // mensaje += salida +"\n ";
                fIntegrador.txtLog.setText(mensaje += " Mensaje de salida N. : " + i++ + "\n");
                ejecutado = true;
            }
        } else {
            System.out.println("No se a producido ninguna salida");
            mensaje += "No se a producido ninguna salida \n"
                    + "Verifique la trasmision y la conexion de red... ";
            fIntegrador.txtLog.setText(mensaje);
        }
        return ejecutado;
    }

    private void crearArchivo(String fecha) {
        FileWriter flwriter = null;
        String path = (new File(".").getAbsolutePath()).replace(".", "");
        String pathArchivo = path + "integrador.bat";
        try {
            File archivo = new File(pathArchivo);
            archivo.setExecutable(true);

//            if (archivo.exists()) {
//                archivo.delete();
//
//            }
            System.out.println("Inicia a crear el archivo..");

            System.out.println("ruta final del archivo : " + pathArchivo);
            //crea el flujo para escribir en el archivo
            flwriter = new FileWriter(pathArchivo);
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo

            flwriter.write("@echo \n");
            flwriter.write("cd dll \n");
            flwriter.write("sincronizar.exe -p -c -t -f " + fecha);
            mensaje += path + "dll/sincronizar.exe -p -c -t -f " + fecha + "\n";
            fIntegrador.txtLog.setText(mensaje);
            System.out.println("Archivo creado satisfactoriamente..");
            flwriter.flush();
            System.out.println("se cierra el filewriter");
            flwriter.close();
            System.out.println("Archivo creado satisfactoriamente..");

        } catch (IOException ex) {
            Logger.getLogger(HiloIntegrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
