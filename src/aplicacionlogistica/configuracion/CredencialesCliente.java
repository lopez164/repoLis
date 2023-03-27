/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
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
public class CredencialesCliente {

    private String servidorLocal = null;
    private String ServidorRemoto = null;
    private String servidorALD = null;
    private String servidorERP = null;

    private String cadenaServidorLocal = null;
    private String cadenaServidorRemoto = null;
    private String cadenaServidorALD = null;
    private String cadenaServidorERP = null;
    
    private String bdLocal = null;
    private String urlLocal = null;
    private String  puertoLocal = null;
    private String usuarioBDLocal = null;
    private String claveBDLocal = null;
    
    private String bdRemota = null;
    private String urlRemota = null;
    private String  puertoRemota = null;
    private String usuarioBDRemota = null;
    private String claveBDRemota = null;
    
    

    private String nitCliente = null;
    private String nombreCliente = null;
    private String direccionCliente = null;
    private String ciudad = null;
    private String contacto = null;
    private String emailContacto = null;
    private String telefonoContacto = null;
    private String celularContacto = null;

    private int clienteActivo = 0;
    private Date fechaInicialServicio = null;
    private Date fechaFinalServicio = null;
    private int GPSservice = 0;
    private int APPservice = 0;
    private int MTTOservice = 0;
    private String geoPositionCliente = null;


    public CredencialesCliente() {
        
        String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota";
        String noUsarSSL = "&useSSL=false";
        servidorALD = "129.151.107.181";
        cadenaServidorALD = String.format("jdbc:mysql://%s/%s?%s%s", this.servidorALD, "misClientes", husoHorario, noUsarSSL);
        
       
        
        init();
        setCredenciales();

    }
/** 
 * Funcionn que trae los datos del cliente
 * 
 */
    private void init() {
        boolean verificado = false;
        this.clienteActivo = 0;
        Connection con;
        // config();
        ResultSet rs = null;
        Statement st;
        java.util.Date fechaActual = new java.util.Date();
        try {

            con = CConexiones.GetConnection(cadenaServidorALD, "luislopez", "%jslslpzmjC12%", "Inicio.isClienteActivo");

            if (con != null) {
                st = con.createStatement();
                String sql = "select * from configuracion where nit='" + nitCliente.replace(".", "") + "';";
                rs = st.executeQuery(sql);
                if (rs.next()) {

                    this.nitCliente = rs.getString("nit");
                    this.nombreCliente = rs.getString("nombreCliente");
                    this.direccionCliente = rs.getString("direccion");
                    this.ciudad = rs.getString("ciudad");
                    this.contacto = rs.getString("");
                    this.emailContacto = rs.getString("email");
                    this.telefonoContacto = rs.getString("telefono");
                    this.celularContacto = rs.getString("celular");
                    this.clienteActivo = rs.getInt("activo");
                    this.fechaInicialServicio = rs.getDate("fechaIng");
                    this.fechaFinalServicio = rs.getDate("fechaFin");
                    this.GPSservice = rs.getInt("GPSservice");
                    this.APPservice = rs.getInt("APPservice");
                    this.MTTOservice = rs.getInt("MTTOservice");
                    this.geoPositionCliente = rs.getString("geoPositionCliente");

                }
                //System.out.println("mensaje al salir de la verificacion " + verificado);
                rs.close();
                st.close();
                con.close();
            }

        } catch (SQLException | HeadlessException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Funcion que asgina las credenciales desde un archivo plano
     */
    private void setCredenciales(){
        String nada;
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        FileReader fr = null;
        try {
            File file = new File(rutaDeArchivo + "ReadUs.ini");
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            
            nada = Inicio.deCifrar(br.readLine()); //idAliado
            nada = Inicio.deCifrar(br.readLine()); //nitCliente
            nada = Inicio.deCifrar(br.readLine()); // nombreDelCliente
            nada = Inicio.deCifrar(br.readLine()); //direccionCliente
            nada = Inicio.deCifrar(br.readLine()); //barrioCliente
            nada = Inicio.deCifrar(br.readLine()); //ciudadCliente
            nada = Inicio.deCifrar(br.readLine()); //contactoCliente
            nada = Inicio.deCifrar(br.readLine()); // emailCliente
            nada = Inicio.deCifrar(br.readLine()); //telefonoCliente
            nada = Inicio.deCifrar(br.readLine()); //celularCliente
            bdLocal = Inicio.deCifrar(br.readLine()); // bdLocal
            urlLocal = Inicio.deCifrar(br.readLine());//  urlLocal
            servidorLocal = Inicio.deCifrar(br.readLine()); //servidorLocal;
            puertoLocal = Inicio.deCifrar(br.readLine()); //puertoLocal
            usuarioBDLocal = Inicio.deCifrar(br.readLine());
            claveBDLocal = Inicio.deCifrar(br.readLine());
            bdRemota = Inicio.deCifrar(br.readLine());
            urlRemota = Inicio.deCifrar(br.readLine());
            ServidorRemoto = Inicio.deCifrar(br.readLine());
            puertoRemota = Inicio.deCifrar(br.readLine());
            usuarioBDRemota = Inicio.deCifrar(br.readLine());
            claveBDRemota = Inicio.deCifrar(br.readLine());
            
            nada = Inicio.deCifrar(br.readLine()); //bdMantenimientos
            nada = Inicio.deCifrar(br.readLine());// urlMantenimientos
            nada = Inicio.deCifrar(br.readLine()); //serverMantenimientos
            nada = Inicio.deCifrar(br.readLine()); //puertoMantenimientos
            nada = Inicio.deCifrar(br.readLine()); //usuarioBDMantenimientos
            nada = Inicio.deCifrar(br.readLine()); //claveBDMantenimientos
            cadenaServidorLocal = urlLocal + "://?useSSL=false" + servidorLocal + ":" + puertoLocal + "/" + bdLocal; 
            cadenaServidorRemoto = urlRemota + "://?useSSL=false" + ServidorRemoto + ":" + puertoRemota + "/" + bdRemota;
            //cadenaMantenimientos = urlMantenimientos + "://?useSSL=false" + serverMantenimientos + ":" + puertoMantenimientos + "/" + bdMantenimientos;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CredencialesCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CredencialesCliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(CredencialesCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getContacto() {
        return contacto;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public String getCelularContacto() {
        return celularContacto;
    }

    public int getClienteActivo() {
        return clienteActivo;
    }

    public Date getFechaInicialServicio() {
        return fechaInicialServicio;
    }

    public Date getFechaFinalServicio() {
        return fechaFinalServicio;
    }

    public int getGPSservice() {
        return GPSservice;
    }

    public int getAPPservice() {
        return APPservice;
    }

    public int getMTTOservice() {
        return MTTOservice;
    }

    public String getGeoPositionCliente() {
        return geoPositionCliente;
    }

    public void setClienteActivo(int clienteActivo) {
        this.clienteActivo = clienteActivo;
    }

    public void setClienteActivo() {
        boolean verificado = false;
        this.clienteActivo = 0;
        Connection con;
        // config();
        ResultSet rs = null;
        Statement st;
        java.util.Date fechaActual = new java.util.Date();
        try {
            con = CConexiones.GetConnection(cadenaServidorALD, "luislopez", "%jslslpzmjC12%", "Inicio.isClienteActivo");

            if (con != null) {
                st = con.createStatement();
                String sql = "select clienteActivo from configuracion where nit='" + nitCliente.replace(".", "") + "';";
                rs = st.executeQuery(sql);
                if (rs.next()) {
                    this.clienteActivo = rs.getInt("activo");
                }
                //System.out.println("mensaje al salir de la verificacion " + verificado);
                rs.close();
                st.close();
                con.close();
            }

        } catch (SQLException | HeadlessException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
