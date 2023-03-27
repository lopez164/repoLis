/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHielera;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloBitacoraMovimientosEnElSistema implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    List<String> datos;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloBitacoraMovimientosEnElSistema(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *@param datos
     * @param ini
     */
    public HiloBitacoraMovimientosEnElSistema(Inicio ini, List<String> datos) {
        this.ini=ini;
        this.datos = datos;
        caso = "grabarRegistro";
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "grabarRegistro":
                       grabarRegistro();
                        break;
                   
                    case "imprimir":
                        // imprimir();
                        break;
                    
                    default:
                        //JOptionPane.showInternalMessageDialog(fDespachoHielera, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloBitacoraMovimientosEnElSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void grabarRegistro() throws SQLException {
        String sq = "INSERT INTO tablabitacoramovimientos "
                + "(xxusuario,xxFormulario,xxAccion,xxhora,xxestacion,ip) VALUES('"
                + datos.get(0) + "','"
                + datos.get(1) + "','"
                + datos.get(2) + "',"
                + "CURRENT_TIMESTAMP,'"
                + ini.getNombreEstacionLocal() +"','"
                + ini.getDirecionIpLocal() +"');";
//                 ini.insertarDatosLocalmente(sq);

    }

}
