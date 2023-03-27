/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;



import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloAuxBkUpBBDD implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
   
    

    private int BUFFER = 10485760;  
    //para guardar en memmoria
    private StringBuffer temp = null;
    //para guardar el archivo SQL
    private FileWriter  fichero = null;
    private PrintWriter pw = null;
    
    /**
     * Constructor de clase
     */
    public HiloAuxBkUpBBDD(int tiempo, String form) {
        this.tiempo = tiempo;
 
        
        
    }

   

    @Override
    public void run() {
        int k = 1;
        int j=0;
        double l=0.0;
        // for (int k = 0; k <= 70; k++) { inicio for
        
        try {
          String comando="mysqldump --host=" + "www.logarea.net" + " --port=" + "3306" +
        " --user=" + "luislopez" + " --password=" + "jslslpzmjc1212" +
        " --compact --complete-insert --extended-insert --skip-quote-names" +
        " --skip-comments --skip-triggers " + "bavaria";
            //sentencia para crear el BackUp
         Process run = Runtime.getRuntime().exec(
        " mysqldump --host=" + "www.logarea.net" + " --port=" + "3306" +
        " --user=" + "luislopez" + " --password=" + "jslslpzmjc1212" +
        " --compact --complete-insert --extended-insert --skip-quote-names" +
        " --skip-comments --skip-triggers " + "bavaria");
        //se guarda en memoria el backup
        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        temp = new StringBuffer();
        int count;
        char[] cbuf = new char[BUFFER];
        while ((count = br.read(cbuf, 0, BUFFER)) != -1)
            temp.append(cbuf, 0, count);
            br.close();
        in.close();        
        /* se crea y escribe el archivo SQL */
        fichero = new FileWriter("flebuup.sql"); //(""file_backup);
        pw = new PrintWriter(fichero);                                                    
        pw.println(temp.toString());  
        
        Thread.sleep(1);
       // form.barraSuperior.setValue(100);
        band=true;
           
        } // fin try
        catch (IOException | InterruptedException e) {
            System.err.println("Error en HiloAuxBkUpBBDD " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
       }

   
}
    
   public boolean CrearBackup(String host, String port, String user, String password, String db, String file_backup){
    boolean ok=false;
    try{       
        //sentencia para crear el BackUp
         Process run = Runtime.getRuntime().exec(
        " mysqldump --host=" + host + " --port=" + port +
        " --user=" + user + " --password=" + password +
        " --compact --complete-insert --extended-insert --skip-quote-names" +
        " --skip-comments --skip-triggers " + db);
        

        //se guarda en memoria el backup
        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        temp = new StringBuffer();
        int count;
        char[] cbuf = new char[BUFFER];
        while ((count = br.read(cbuf, 0, BUFFER)) != -1)
            temp.append(cbuf, 0, count);
            br.close();
        in.close();        
        /* se crea y escribe el archivo SQL */
        fichero = new FileWriter("baupfile.sql"); //(""file_backup);
        pw = new PrintWriter(fichero);                                                    
        pw.println(temp.toString());  
        ok=true;
        Thread.sleep(1);
//        form.barraSuperior.setValue(100);
        band=true;
   }
    catch (IOException | InterruptedException ex){
             System.err.println("Error en HiloAuxBkUpBBDD " + ex.getMessage());
            
    } finally {
       try {           
         if (null != fichero)
              fichero.close();
       } catch (Exception e2) {
            System.err.println("Error en HiloAuxBkUpBBDD " + e2.getMessage());
       }
    }   
    return ok; 
 }  
}
