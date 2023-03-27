/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CBitacoraSms { //extends Inicio {

    private String idBitacoraSMS;
    private String remitente;
    private String destinatario;
    private String mensaje;
    private String fechaIngreso;
    private String usuario;
    private Inicio ini;

    public CBitacoraSms(Inicio ini) {
        this.ini=ini;
    }

    public CBitacoraSms(Inicio ini,String remitente, String destinatario, String mensaje, String usuario) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.ini=ini;
    }

    public String getIdBitacoraSMS() {
        return idBitacoraSMS;
    }

    public void setIdBitacoraSMS(String idBitacoraSMS) {
        this.idBitacoraSMS = idBitacoraSMS;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

   
    public boolean grabarRegistroSms() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO bitacoraSms (remitente,destinatario,mensaje,usuario) VALUES ("
                    + "'" + this.remitente + "'"
                    + "'" + this.destinatario + "'"
                    + "'" + this.mensaje + "'"
                    +"'" + ini.getUser().getNombreUsuario() + "') "
                    + "ON DUPLICATE KEY UPDATE FLAG='-1'";

            grabado = ini.insertarDatosRemotamente(sql);
            

        } catch (Exception ex) {
            Logger.getLogger(CBitacoraSms.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

  

   

}
