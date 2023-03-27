/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica;

import aplicacionlogistica.configuracion.formularios.IngresoAlSistema_LIS_ALD;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author VLI_488
 */
public class AplicacionLogistica {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AplicacionLogistica.class);
    private static final int port = 9966;
    static ServerSocket serverSocket;
    private static Properties propiedades;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        int x;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        propiedades = new Properties();
        propiedades.load(new FileReader(rutaDeArchivo + "propiedadesLogistica.properties"));
       
        try {

           x = Integer.parseInt(propiedades.getProperty("idOperador"));
             
            switch (x) {
                case 1:
                    /*INGRESO POR DISTRILOG B2B*/

                    logger.debug("Intentando iniciar la aplicacion Distrilog B2B");
                  
                    /* look and feel para camdun*/
                    //NimRODTheme nt1 = new NimRODTheme(rutaDeArchivo + "NimRODThemeFile_" + propiedades.getProperty("idOperador") + ".theme");
                    NimRODTheme nt1 = new NimRODTheme(rutaDeArchivo  + propiedades.getProperty("skinDelSistema")) ; 
                    NimRODLookAndFeel nf1 = new NimRODLookAndFeel();
                    nf1.setCurrentTheme(nt1);
                    javax.swing.UIManager.setLookAndFeel(nf1);
      
                    IngresoAlSistema_LIS_ALD ingresoAlSistema = new IngresoAlSistema_LIS_ALD(propiedades,singleAPP(Integer.parseInt(propiedades.getProperty("puerto"))),1);
                    ingresoAlSistema.setTitle("Ingreso al sistema de Información de Logistica");
                    ingresoAlSistema.setResizable(false);
                    ingresoAlSistema.setLocation((screenSize.width - ingresoAlSistema.getSize().width) / 2, (screenSize.height - ingresoAlSistema.getSize().height) / 2);
                    System.out.println("tiempo carga formulario " + new Date());
                    ingresoAlSistema.setVisible(true);
                   
                    break;
                case 2:
                    /*INGRESO POR ALD PLUS */
                    logger.debug("Intentando iniciar la aplicacion ALD PLUS");
                    
                    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    //Look and Feel para ALD
                     rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
                    //NimRODTheme nt2 = new NimRODTheme(rutaDeArchivo + "NimRODThemeFile_" + propiedades.getProperty("idOperador") + ".theme");
                     NimRODTheme nt2 = new NimRODTheme(rutaDeArchivo  + propiedades.getProperty("skinDelSistema")) ; 
                    NimRODLookAndFeel nf2 = new NimRODLookAndFeel();
                    nf2.setCurrentTheme(nt2);
                    javax.swing.UIManager.setLookAndFeel(nf2);

                    IngresoAlSistema_LIS_ALD ingresoAlSistemaLis = new IngresoAlSistema_LIS_ALD(propiedades,singleAPP(Integer.parseInt(propiedades.getProperty("puerto"))),2);
                    ingresoAlSistemaLis.setTitle("Ingreso al sistema de Información de Logistica");
                    ingresoAlSistemaLis.setResizable(false);
                    ingresoAlSistemaLis.setLocation((screenSize.width - ingresoAlSistemaLis.getSize().width) / 2, (screenSize.height - ingresoAlSistemaLis.getSize().height) / 2);
                    System.out.println("tiempo carga formulario " + new Date());
                    ingresoAlSistemaLis.setVisible(true);

                    break;

                case 3:
                    /*INGRESO POR Mantenimientos */
                    logger.debug("Intentando iniciar la aplicacion ALD PLUS");

                    //Look and Feel para ALD
                    rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
//                    nt = new NimRODTheme(rutaDeArchivo + "ALDThemeFile.theme");
//                    nf = new NimRODLookAndFeel();
//                    nf.setCurrentTheme(nt);

                   // javax.swing.UIManager.setLookAndFeel(nf);

                    //IngresoAlSistemaGlobalMarcas ingresoAlSistemaGlobalMarcas = new IngresoAlSistema_LIS_ALD(singleAPP(port));
                    //ingresoAlSistemaGlobalMarcas.setVisible(true);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "No se ha encontrado la ruta de inicio para el Sistema", "Sin ruta ", JOptionPane.WARNING_MESSAGE);

                    break;
            }

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AplicacionLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("imagenes/turbo_64x64.png"));

        return retValue;
    }

    private static boolean singleAPP(int port) {

        try {
            serverSocket = new java.net.ServerSocket(port);
        } catch (java.io.IOException ex) {
            return false;

        }
        return true;

    }

}
