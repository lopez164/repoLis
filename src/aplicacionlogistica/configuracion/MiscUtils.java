/*http://www.rgagnon.com/javadetails/java-0580.html
 * http://casidiablo.net/obtener-variables-entorno-informacion-sistema-java/
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * ip=java.net.InetAddress.getLocalHost().getHostAddress(); 
 * @author Usuario
 */
public class MiscUtils {
    
private MiscUtils() {  }

public static String getIpDir() {
    String valor=null;
        try { 
            valor=java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MiscUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    return valor;
}

public static String getComputerName() {
    String valor=null;
    valor=System.getenv("COMPUTERNAME");
    return valor;
}
public static String getUsuarioSistema()
    {
     String valor=null;
       valor= System.getProperty("user.name");
        return valor;
    }

  public static String getMotherboardSN() {
  String result = "";
    try {
      File file = File.createTempFile("realhowto",".vbs");
      file.deleteOnExit();
      FileWriter fw = new java.io.FileWriter(file);

      String vbs =
         "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
        + "Set colItems = objWMIService.ExecQuery _ \n"
        + "   (\"Select * from Win32_BaseBoard\") \n"
        + "For Each objItem in colItems \n"
        + "    Wscript.Echo objItem.SerialNumber \n"
        + "    exit for  ' do the first cpu only! \n"
        + "Next \n";

      fw.write(vbs);
      fw.close();
      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
      BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = input.readLine()) != null) {
         result += line;
      }
      input.close();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return result.trim();
  }

 
}
