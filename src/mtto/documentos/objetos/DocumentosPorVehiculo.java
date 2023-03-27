/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class DocumentosPorVehiculo {

    int idConsecutivo;
    String placa;
    int idTipoDocumento;
    String nombreTipoDocumento;
    String prefijo;
    String numeroDocumento;
    String entidadEmisora;
    String fechaExpedicion;
    String lugarExpedicion;
    String fechaVencimiento;
    File soporteDocumento;
    String formatoSoporteDocumento;
    int activo;
    Date fechaIng;
    String usuario;
    Inicio ini;
    String formatoSiglas;
    boolean documentoVencido=false;
    String obs;            

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    
    public boolean isDocumentoVencido() {
        return documentoVencido;
    }

    public void setDocumentoVencido(boolean documentoVencido) {
        this.documentoVencido = documentoVencido;
    }

    
    
    public String getFormatoSiglas() {
        return formatoSiglas;
    }

    public void setFormatoSiglas(String formatoSiglas) {
        this.formatoSiglas = formatoSiglas;
    }

    
    
    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    
    
    public int getIdConsecutivo() {
        return idConsecutivo;
    }

    public void setIdConsecutivo(int idConsecutivo) {
        this.idConsecutivo = idConsecutivo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombreTipoDocumento() {
        return nombreTipoDocumento;
    }

    public void setNombreTipoDocumento(String nombreTipoDocumento) {
        this.nombreTipoDocumento = nombreTipoDocumento;
    }

    
    
   
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getEntidadEmisora() {
        return entidadEmisora;
    }

    public void setEntidadEmisora(String entidadEmisora) {
        this.entidadEmisora = entidadEmisora;
    }

    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    public void setLugarExpedicion(String lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }


    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

     public File getSoporteDocumento() throws IOException {
          return soporteDocumento;
     }
    
    public File getArhivoDocumento() throws IOException {
        try {
            soporteDocumento=null;
            this.ini = ini;
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            int x=1;
                        
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] b = null;
            
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            
            sql=  "SELECT soporteDocumento FROM documentosporvehiculo where placa=? "
                    + "and idTipoDocumento=? and numeroDocumento=? and "
                    + "activo=?;";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, this.placa);
            ps.setInt(2, this.idTipoDocumento);
            ps.setString(3, this.numeroDocumento);
            ps.setInt(4,x);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                b = rs.getBytes(1);
            }
             
             InputStream bos = new ByteArrayInputStream(b);

            int tamanoInput = bos.available();
            byte[] datosPDF = new byte[tamanoInput];
            bos.read(datosPDF, 0, tamanoInput);

            OutputStream out = new FileOutputStream("tmp/" + this.prefijo + "_" + this.placa + ".pdf");
            out.write(datosPDF);
            
             out.close();
            bos.close();
            ps.close();
            rs.close();
            con.close();
            
             /***************************/
         soporteDocumento= new File("tmp/" +this.prefijo + "_" + this.placa + ".pdf");
          
        } catch (SQLException ex) {
            Logger.getLogger(DocumentosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return soporteDocumento;
    }

    public void setSoporteDocumento(File soporteDocumento) {
        this.soporteDocumento = soporteDocumento;
    }

    public String getFormatoSoporteDocumento() {
        return formatoSoporteDocumento;
    }

    public void setFormatoSoporteDocumento(String formatoSoporteDocumento) {
        this.formatoSoporteDocumento = formatoSoporteDocumento;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public DocumentosPorVehiculo() {
       
    }
    
    public DocumentosPorVehiculo(Inicio ini) {
        this.ini = ini;
    }
    
      public boolean grabarDocumentoPorVehiculo() {
        boolean grab = false;
        String sql;
        try {
            sql = "INSERT INTO documentosporvehiculo (placa,idTipoDocumento,"
                    + "numeroDocumento,entidadEmisora,fechaExpedicion,lugarExpedicion,"
                    + "fechaVencimiento,activo,usuario)VALUES('"
                    + placa + "','"
                    + idTipoDocumento + "','"
                    + numeroDocumento + "','"
                    + entidadEmisora + "','"
                    + fechaExpedicion + "','"
                    + lugarExpedicion + "','"
                    + fechaVencimiento + "','"
                    + activo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') on duplicate key update flag=1";

        
                grab = this.ini.insertarDatosRemotamente(sql);
                if (insertarDocumento()) {
                    grab = true;
                
            } else {
                System.out.println("Error en grabar Carro ");
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabar carros " + ex);
        }

        return grab;
    
    
}
      
       public boolean grabarDocumentoPorVehiculo(int a) {
        boolean grabado = false;
        String sql;
        InputStream entrada = null;
        Connection con=null;
         int ingresados = 0;
         
         sql="update documentosporvehiculo set activo=0 where placa='" + this.placa + "' and idTipoDocumento='" + this.idTipoDocumento + "';";
         ini.insertarDatosRemotamente(sql);
       
         try {
            sql = "INSERT INTO documentosporvehiculo (placa,idTipoDocumento,"
                    + "numeroDocumento,entidadEmisora,fechaExpedicion,lugarExpedicion,"
                    + "fechaVencimiento,soporteDocumento,formatoSoporteDocumento,activo,usuario)VALUES(?,?,?,?,?,?,?,?,?,?,?) on duplicate key update flag=1";
            
              PreparedStatement pst = null;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con.setAutoCommit(false);
            
            pst = con.prepareStatement(sql);
            
            entrada = new FileInputStream(soporteDocumento);
            
            pst.setString(1, this.placa);
            pst.setInt(2, this.idTipoDocumento);
            pst.setString(3, this.numeroDocumento);
            pst.setString(4, this.entidadEmisora);
            pst.setString(5, this.fechaExpedicion);
            pst.setString(6, this.lugarExpedicion);
            pst.setString(7, this.fechaVencimiento);
            pst.setBinaryStream(8, entrada, (int) soporteDocumento.length());
            pst.setString(9, "pdf");
            pst.setInt(10, 1);
            pst.setString(11, Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            

            ingresados = pst.executeUpdate();
            con.commit();
            
            pst.close();
            entrada.close();
            con.close();
            
            grabado=true;
           
        } catch (Exception ex) {
            Logger.getLogger(DocumentosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabar carros " + ex);
        }

        return grabado;
    
    
}
   
      public String toString() {
        //idCargo, nombreCargo, activo, fechaIng, usuario, flag
        String sql = "SELECT idConsecutivo, placa, idTipoDocumento, idTipoVehiculo, "
                + "numeroDocumento, entidadEmisora, fechaExpedicion, lugarExpedicion,"
                + " tieneVencimiento, fechaVencimiento, formatoSoporteDocumento, "
                + "activo, fechaIng, usuario "
                + "FROM documentosporvehiculo where activo=1;";
        return sql;
    }


      public boolean insertarDocumento() {
        boolean grab;

        try {
            Connection con;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            if (soporteDocumento != null) {
                //String extension = fotografia.getAbsolutePath().substring(fotografia.getAbsolutePath().lastIndexOf(".")+1);
                Statement sentencia = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                String sql2 = "select placa,idTipoDocumento,numeroDocumento "
                        + "from documentosporvehiculo  where placa='" + this.placa + "' "
                        + "and idTipoDocumento='" + idTipoDocumento + "' and numeroDocumento='" + numeroDocumento + "';";
                ResultSet rs;
                rs = sentencia.executeQuery(sql2);
                con.setAutoCommit(false);
                PreparedStatement pst;
                pst = con.prepareStatement("Update documentosporvehiculo set soporteDocumento=?, formatoSoporteDocumento=? where placa=?"
                        + "and idTipoDocumento=? and numeroDocumento=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
               
                while (rs.next()) {
                    //File file = new File(fotografia.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(soporteDocumento);
                    pst.setBinaryStream(1, fis, soporteDocumento.length());
                    pst.setString(2, "pdf");
                    pst.setString(3, rs.getString(1));
                    pst.setInt(4, rs.getInt(2));
                    pst.setString(5, rs.getString(3));
                    pst.executeUpdate();
                    con.commit();
                }

                pst.close();
                rs.close();
                con.close();

            }

            grab = true;

            con.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error en insertar() archivo no encontrado " + ex.getMessage());
            grab = false;
        } catch (SQLException ex) {
            System.out.println("Error en insertar() consulta sql " + ex.getMessage() + "(sql=");
            grab = false;
        }
        return grab;
    }
      
      public boolean obtenerArchivo(String nombreArchivoBuscar, String nombreArchivoSalida) {
        InputStream salida = null;
        try {
            PreparedStatement pst;
            ResultSet rs;
            Blob blob;
            FileOutputStream archivoSalida;
            String select;

            byte[] arreglo;
            int byteLeidos = 0;

           Connection con;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
           con.setAutoCommit(false);
            select = "select ARCHIVO from ARCHIVOS WHERE NOMBRE=?";
            pst = con.prepareStatement(select);
            pst.setString(1, nombreArchivoBuscar);

            rs = pst.executeQuery();

            if (rs != null) {
                rs.next();
                blob = rs.getBlob(1);
                salida = blob.getBinaryStream();

                arreglo = new byte[2048];

                archivoSalida = new FileOutputStream(nombreArchivoSalida);

                while ((byteLeidos = salida.read(arreglo)) > -1) {
                    archivoSalida.write(arreglo, 0, byteLeidos);
                }
                return true;
            } else {
                return false;
            }

        } catch (IOException ex) {
            Logger.getLogger(DocumentosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DocumentosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (salida != null) {
                    salida.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(DocumentosPorVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
