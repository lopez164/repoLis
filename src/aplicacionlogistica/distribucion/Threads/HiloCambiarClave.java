/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloCambiarClave implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    CUsuarios usuario;
    String clave;

    /**
     * Constructor de clase
     */
    public HiloCambiarClave(int tiempo, CUsuarios usuario, String claveFinal) {
        this.tiempo = tiempo;
        this.usuario = usuario;
        this.clave = claveFinal;

    }

    @Override
    public void run() {
        boolean ok = false;

        try {
            while (!ok) {
                ok = usuario.cambiarClave(Inicio.deCifrar(usuario.getNombreUsuario()), clave);
                Thread.sleep(1);
            }
        } // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        }


    }
}
