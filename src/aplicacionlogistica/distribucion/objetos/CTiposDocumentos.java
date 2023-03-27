/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

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
 * @author root
 */
public class CTiposDocumentos {
    int idtipoDeDocumento;
    String nombreDeDocumento;
    int activo;
    Inicio ini;

    
    /**
     * Método constructor vacío
     *
     */
    public CTiposDocumentos() {
    }

     /**
     * Método constructor con un  parámetro, la variables no tienen ningún valor
     *
     * @param ini archivo de configurción del sistema
     */
    public CTiposDocumentos(Inicio ini) {
        this.ini = ini;
    }

    /**
     * Método constructor con dos  parámetros
     *
     * @param idtipoDeDocumento identifiacador del documento
     * @param ini archivo de configurción del sistema
     */
    public CTiposDocumentos(int idtipoDeDocumento, Inicio ini) {
        this.ini = ini;
        String sql="SELECT * "
                + "FROM tiposdedocumentos "
                + "WHERE "
                + "idtipoDeDocumento='" + idtipoDeDocumento +"';";
        Connection con;
        Statement st;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idtipoDeDocumento = rst.getInt("idtipoDeDocumento");
                    this.nombreDeDocumento = rst.getString("nombreDeDocumento");
                    this.activo = rst.getInt("activo");
                    
                }else{
                    this.idtipoDeDocumento = 0;
                    this.nombreDeDocumento = null;
                    this.activo = 0;
                    
                    
                } 
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CTiposDocumentos.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CTiposDocumentos(Inicio ini, int consecutivo) sql " + ex.getMessage());
            }
        }
    }
    
     /**
     * Método que devuelve un consecutivoo que identifica al documento
     * 
     * @return consecutivo asignado
     */ 
    public int getIdtipoDeDocumento() {
        return idtipoDeDocumento;
    }

     /**
     * Método que asigna un consecutivoo que identifica al documento
     * 
     * @param idtipoDeDocumento consecutivo asignado
     */
    public void setIdtipoDeDocumento(int idtipoDeDocumento) {
        this.idtipoDeDocumento = idtipoDeDocumento;
    }

    /**
     * Método que devuelve una cadena con el nombre del  tipo de  documento  
     *
     * @return  nombre del documento
     */
    public String getNombreDeDocumento() {
        return nombreDeDocumento;
    }

     /**
     * Método que asigna el nombre del  tipo de  documento  
     * 
     * @param nombreDeDocumento es el nombre del  tipo de  documento
     */
    public void setNombreDeDocumento(String nombreDeDocumento) {
        this.nombreDeDocumento = nombreDeDocumento;
    }

     /**
     * Método que devuelve un entero indicando sí el documento está activo ó no
     * 1 activo 0 no activo
     * @return 1 activo 0 no activo
     */
    public int getActivo() {
        return activo;
    }

     /**
     * Método que asigna un entero indicando sí el documento está activo ó no
     * 1 activo 0 no activo
     * @param activo   1 activo 0 no activo
     */
    public void setActivo(int activo) {
        this.activo = activo;
    }
    
    
     /**
     * Método que permite grabar los tipos de documentos en la  BBDD
     * usuario
     * @return  devuelve true si graba, flso sí hubo un problema
     */  
     public boolean grabarTiposDeDocumentos() {  
       boolean valor = false;
            Connection con;
            Statement st;
            ResultSet rst;
          
         try {
 
             String sql="INSERT INTO tiposdedocumentos (nombreDeDocumento,activo, usuario, flag) "
                    + "VALUES ('"
                    + this.nombreDeDocumento  + "','"
                    + "1','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "1');";
            

            if ( ini.insertarDatosRemotamente(sql)) {
                sql = "SELECT idtipoDeDocumento "
                        + "FROM tiposdedocumentos "
                        + "WHERE "
                        + "nombreDeDocumento='" + nombreDeDocumento + "';";
                        
                 con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
           
                if (con != null) {
                    
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        this.idtipoDeDocumento = rst.getInt("idtipoDeDocumento");
                        
                    }else{
                        this.idtipoDeDocumento = 0;
                      }
                    rst.close();
                    st.close();
                    con.close();
                   
                    
                }
                
            }     
        } catch (Exception ex) {
            Logger.getLogger(CTiposDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
         return valor;
     }
    
}
