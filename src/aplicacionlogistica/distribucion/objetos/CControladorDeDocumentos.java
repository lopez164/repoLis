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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class CControladorDeDocumentos {
    int idcontrolador;
    int tipoDocumento;
    int isFree;
    String clave;
    Inicio ini;

     

    /**
     * Método constructor vacío
     *
     */
    public CControladorDeDocumentos() {
        
    }

    /**
     * Método constructor con un  parámetro, la variables no tienen ningún valor
     *
     * @param ini archivo de configurción del sistema
     */
    public CControladorDeDocumentos(Inicio ini) {
        this.ini = ini;
    }

    /**
     * Método constructor con dss  parámetros
     *
     * @param idcontrolador consecutivo del controlador
     * @param ini archivo de configurción del sistema
     */
    public CControladorDeDocumentos(int idcontrolador, Inicio ini) {
        this.ini = ini;
        String sql="SELECT * "
                + "FROM controladordedocumentos "
                + "WHERE "
                + "idcontrolador='" + idcontrolador +"'";
        Connection con;
        Statement st;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idcontrolador = rst.getInt("idcontrolador");
                    this.tipoDocumento = rst.getInt("tipoDocumento");
                    this.isFree = rst.getInt("isFree");
                    this.clave = rst.getString("clave");
                    
                }else{
                    this.idcontrolador = 0;
                    this.tipoDocumento = 0;
                    this.isFree = 0;
                    this.clave=null;
                   
                    
                } 
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CFacturasDescargadas(Inicio ini, int consecutivo) sql " + ex.getMessage());
            }
        }
        
    }

    /**
     * Método que devuelve un consecutivoo que identifica al controlador
     * 
     * @return consecutivo asignado
     */
    public int getIdcontrolador() {
        return idcontrolador;
    }

     /**
     * Método que asigna un consecutivoo que identifica al controlador
     * 
     * @param idcontrolador consecutivo asignado
     */
    public void setIdcontrolador(int idcontrolador) {
        this.idcontrolador = idcontrolador;
    }

    /**
     * Método que devuelve un tipo de  documento al controlador 
     *
     * @return  un tipo de  documento al controlador
     */
    public int getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Método que asigna un tipo de  documento al controlador
     * usuario
     * 
     * @param tipoDocumento es el tip de documento que se va a controlar
     */
    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * Método que devuelve un entero 0 si documento está ocupado, 1 sí está libre 
     *
     * @return  devuelve 0 si está ocupdado, 1 sí está libre
     */
    public int getIsFree() {
        return isFree;
    }

    /**
     * Método que permite establecer que un documento está siendo utiliado por un
     * usuario
     *
     * @param isFree permite identificar si un documento está siendo utiliado por
     * un usuario ó no 
     */
    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }
    
     /**
     * Método que permite identificar plenmente el registro insertado por el usuario
     * 
     * @return devuelve el valor de la clave
     */
    public String getClave() {
        return clave;
    }

    
   
    
      /**
     * Método que permite grabar un controlador de documento en la  BBDD
     * usuario
     * @see http://www.iteramos.com/pregunta/1345/como-generar-un-azar-alfa-numerico-cadena-de-caracteres
     * @return  devuelve true si graba, flso sí hubo un problema
     */  
     public boolean grabarControladorDeDocumentos() {  
       boolean valor = false;
            Connection con;
            Statement st;
            ResultSet rst;
          
            /*Esta variable me permite identificar el registro cuando es insertado en la tabla
            y se invoca para tener la llave de identificación*/
           this.clave = UUID.randomUUID().toString();
            
         try {
 
             String sql="INSERT INTO `controladordedocumentos` (`tipoDocumento`, "
                     + "`isFree`, `clave`, `usuario`, `flag`) "
                    + "VALUES ('"
                    + this.tipoDocumento  + "','"
                    + this.isFree  + "','"
                    + this.clave +  "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "1');";
            

            if ( ini.insertarDatosRemotamente(sql)) {
                valor=true;
                sql = "SELECT idcontrolador "
                        + "FROM controladordedocumentos "
                        + "WHERE "
                        + "clave='" + this.clave + "'";
                
                 con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
           
                if (con != null) {
               
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        this.idcontrolador = rst.getInt("idcontrolador");
                        
                    }else{
                        this.idcontrolador = 0;
                      }
                    rst.close();
                    st.close();
                    con.close();
                   
                    
                }
                
            }     
        } catch (Exception ex) {
            Logger.getLogger(CControladorDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
         return valor;
     }
}
