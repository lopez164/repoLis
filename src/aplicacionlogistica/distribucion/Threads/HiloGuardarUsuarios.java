/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;


import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarUsuarios implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    

   CUsuarios usuarioNuevo;
   CUsuarios usuario; 
   Inicio ini;
    
    /**
     * Constructor de clase
     */
    public HiloGuardarUsuarios(Inicio ini,int tiempo, CUsuarios usuNuevo,CUsuarios user) {
        this.tiempo = tiempo;
        this.usuarioNuevo=usuNuevo;
        this.usuario=user;
        this.ini=ini;
        
    }

   

    @Override
    public void run() {
      boolean ok=false;  
        
        try {
       while(!ok){
         ok=usuarioNuevo.grabarUsuario();
          Thread.sleep(1);
       }
        
       
       
        
           
        } // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
           
       }

   
}
    
   public  boolean insertar(CEmpleados empleado) {
        boolean insertar = false;
        Statement st = null;
        String sql = null ;
        String cadena = "jdbc:mysql" + "://"+ "www.logarea.net"+":" + "3306" + "/" + "bavaria";
            try {
               Connection con = CConexiones.GetConnection(cadena, "luislopez", "jslslpzmjc1212","HiloGuardarUsuarios");
                       
               sql = "INSERT INTO `empleados` (`cedula`, `cargo`, `celularCorporativo`, `fechaDeIngreso`, `idAgencia`,`idRegional`,`idZona`,"
                + "`idCentroDeCosto`,`formatoFotografia`,`idTipoDeContrato`,`numeroCuenta`,`entidadBancaria`,"
                + " `activo`, `usuario`) VALUES ('"
                + empleado.getCedula() + "',"
                + empleado.getCargo() + ",'"
                + empleado.getCelularCorporativo() + "','"
                + empleado.getFechaIngresoEmpresa() + "',"
                + empleado.getIdAgencia() + "," 
                + empleado.getRegional()+ "," 
                + empleado.getZona()+ "," 
                + empleado.getIdCentroDeCosto() + ",'"//  aqui va la imagen
                + empleado.getFormatoFotografia() + "',"
                + empleado.getIdTipoDeContrato()+ ",'"
                + empleado.getNumeroCuenta() + "',"
                + empleado.getIdBanco() + ","
                + empleado.getEmpleadoActivo() + ",'"
                + ini.getUser().getNombreUsuario() + "') ON DUPLICATE KEY UPDATE "
                + "cargo=" + empleado.getCargo() + ","
                + "celularCorporativo='" + empleado.getCelularCorporativo() + "',"
                + "fechaDeIngreso='" + empleado.getFechaIngresoEmpresa() + "',"
                + "idAgencia=" + empleado.getIdAgencia() + "," 
                + "idRegional=" + empleado.getRegional()+ "," 
                + "idZona=" +  empleado.getZona()+ "," 
                + "idCentroDeCosto=" +empleado.getIdCentroDeCosto() + ","//  aqui va la imagen
                + "idTipoDeContrato=" +empleado.getIdTipoDeContrato()+ ","
                + "numeroCuenta='" +empleado.getNumeroCuenta() + "',"
                + "entidadBancaria=" + empleado.getIdBanco() + ","
                + "usuario='" +empleado.getEmpleadoActivo() + "';"; 
               
                if (con != null) {
                    st = con.createStatement();
                    st.execute(sql);
                 }
                st.close();
                con.close();
            } catch (Exception ex) {
                System.out.println("Error en insertar() consulta sql " + ex +  "(sql=" + sql + ")");
                insertar = false;
                JOptionPane.showMessageDialog(null, "No se pudo grabar  en e sistema porque : \n" + ex);
            }
        

        return insertar;
    }
}
