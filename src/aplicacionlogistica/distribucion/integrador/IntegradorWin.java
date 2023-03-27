/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class IntegradorWin {
  FIntegrador fIntegrador;
  String mensaje;
  public boolean terminado=false;

    public IntegradorWin() {
        
    }
    
     public IntegradorWin(FIntegrador fIntegrador) {
        this.fIntegrador = fIntegrador;
    }
    

    public void ejecutar(String ruta, String fecha){
        
        try {
           String commands = ruta + "/sincronizar.exe -p -c -t -f " + fecha;
           mensaje= ruta +"  \n";
            fIntegrador.txtLog.setText(mensaje);
            System.out.println("Inicia proceso de sincronizar");
            mensaje +="Inicia proceso de sincronizar \n";
            fIntegrador.txtLog.setText(mensaje);
            Process process = new ProcessBuilder(commands).start(); // se crea el proceso usando los comandos
            System.out.println("finaliza proceso de sincronizar");
            // Se lee la salida
            System.out.println("lee el inpusstream");
            InputStream is = process.getInputStream();
            System.out.println("llama el objeto inputstream");
            InputStreamReader isr = new InputStreamReader(is);
            System.out.println("llama al objeto bufferReader");
            BufferedReader br = new BufferedReader(isr);
            
            String line;
            int i = 0;
            System.out.println("Entra al ciclo ");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if(line.length() > 0){
                    mensaje += line;
                    fIntegrador.txtLog.setText(mensaje);
                    
                }
                //System.out.println(i++);
                
            } 
             System.out.println("finaliza el while");
            
        } catch (IOException ex) {
            Logger.getLogger(IntegradorWin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       

//            /**
//         * @param args
//         */
//        public static void main(String[] args) {
//                String ruta=
//                        "C:\\Program Files (x86)\\Adobe\\Reader 11.0\\Reader\\AcroRd32.exe";
//                Integrador lp = new Integrador();
//                lp.ejecutar(ruta);
//                System.out.println("Finalizado");
//        }

}
