/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CCuentasBancarias { //extends Inicio {

int idcuentasBancarias;
int idBanco;
String nombreDeBanco;
String tipoDeCuenta;
String numeroDeCuenta;
int activo;
Inicio ini;

    
    public CCuentasBancarias() {
    }

    public CCuentasBancarias(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public int getIdcuentasBancarias() {
        return idcuentasBancarias;
    }

    public void setIdcuentasBancarias(int idcuentasBancarias) {
        this.idcuentasBancarias = idcuentasBancarias;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombreDeBanco() {
        return nombreDeBanco;
    }

    public void setNombreDeBanco(String nombreDeBanco) {
        this.nombreDeBanco = nombreDeBanco;
    }

        
    public String getTipoDeCuenta() {
        return tipoDeCuenta;
    }

    public void setTipoDeCuenta(String tipoDeCuenta) {
        this.tipoDeCuenta = tipoDeCuenta;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    public void setNumeroDeCuenta(String numeroDeCuenta) {
        this.numeroDeCuenta = numeroDeCuenta;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

   
    
    
    
    
    
    
    

    public int getIdEntidadBancaria(String nombreEntidadBancaria) {
        int id = 0;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
            
             sql = "SELECT idEntidadBancaria FROM `bancos` where nombreEntidadBancaria='" + nombreEntidadBancaria
                    + "' and activo=1;";
            rst = st.executeQuery(sql);
            if (rst.next()) {
                id = rst.getInt(1);
            } else {
                id = 0;
            }
            rst.close();
            st.close();
            con.close();
             }
            
        } catch (SQLException ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

//    public boolean grabarEntidadesBancarias() throws SQLException {
//        boolean grabado = false;
//        try {
//           
//            String sql = "INSERT INTO `entidadesBancarias`(`idEntidadBancaria`,"
//                    + "`nombreEntidadBancaria`,`activo`,`usuario`,) VALUES("
//                    + this.idEntidadBancaria + ",'"
//                    + this.nombreEntidadBancaria + "',"
//                    + this.activoEntidadBancaria + ",'"
//                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
//                    + ") ON DUPLICATE KEY UPDATE "
//                    + "nombreDepartamento='" + this.nombreEntidadBancaria + "',"
//                    + "activo=" + this.activoEntidadBancaria + ","
//                    + "flag=1";
//            grabado = ini.insertarDatosLocalmente(sql);
//            
//        } catch (Exception ex) {
//            Logger.getLogger(CCuentasBancarias.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return grabado;
//    }

     public String arrListadoDeCuentasBancarias(){
          //idCargo, nombreCargo, activo, fechaIng, usuario, flag
         String sql= "SELECT * "
                 + "FROM vst_cuentasbancarias ";
         return sql;
   

        
   
}
}