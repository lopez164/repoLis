/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador.sincronizacion;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.distribucion.integrador.sincronizacion.threads.HiloActualizarCerrarSincronizacion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//import sincronizacion.threads.HiloActualizarCerrarSincronizacion;

/**
 *
 * @author lelopez
 */
public class Sincronizacion {

    Inicio ini;
    String mensaje = "";
    FIntegrador fintegrador=null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        try {
            boolean ok = false;

            //Cargamos las variables del sistema
//            File archivo = new File("config.properties");
            String ruta = (new File(".").getAbsolutePath()).replace(".", "") + "config.properties";
            Properties prop = new Properties();
            prop.load(new FileReader(ruta));
//            prop.load(inputStream);

            IniciarSincronizacion sincronizador = new IniciarSincronizacion();

            /*Se cargan las variables de inicio del sistema*/
            sincronizador.setBdFuente(prop.getProperty("bdFuente"));
            sincronizador.setUrlFuente(prop.getProperty("urlFuente"));
            sincronizador.setServerFuente(prop.getProperty("serverFuente"));
            sincronizador.setPuertoFuente(prop.getProperty("puertoFuente"));
            sincronizador.setUsuarioBDFuente(prop.getProperty("usuarioBDFuente"));
            sincronizador.setClaveBDFuente(prop.getProperty("claveBDFuente"));

            sincronizador.setBdDestino(prop.getProperty("bdDestino"));
            sincronizador.setUrlDestino(prop.getProperty("urlDestino"));
            sincronizador.setServerDestino(prop.getProperty("serverDestino"));
            sincronizador.setPuertoDestino(prop.getProperty("puertoDestino"));
            sincronizador.setUsuarioBDDestino(prop.getProperty("usuarioBDDestino"));
            sincronizador.setClaveBDdestino(prop.getProperty("claveBDDestino"));

            //sincronizador.setCadenaFuente(sincronizador.getUrlFuente()+ "://" + sincronizador.getServerFuente()+ ":" + sincronizador.getPuertoFuente()+ "/" + sincronizador.getBdFuente());
            //sincronizador.setCadenaDestino(sincronizador.getUrlDestino()+ "://" + sincronizador.getServerDestino()+ ":" + sincronizador.getPuertoDestino()+ "/" + sincronizador.getBdDestino());
            String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota";
            String noUsarSSL = "&useSSL=false";

            sincronizador.setCadenaFuente(String.format("jdbc:mysql://%s/%s?%s%s", sincronizador.getServerFuente(), sincronizador.getBdFuente(), husoHorario, noUsarSSL));
            sincronizador.setCadenaDestino(String.format("jdbc:mysql://%s/%s?%s%s", sincronizador.getServerDestino(), sincronizador.getBdDestino(), husoHorario, noUsarSSL));

            /*Valida si hay registros para trasmitir*/
            if (sincronizador.verificarRegistros()) {

                /*Genera las sentencias para insetar datos trasmitidos */
                sincronizador.getInsertClientes(false);
                sincronizador.getInsertProductos(false);
                sincronizador.getInsertProductosPorFactura(false);

                /*Inserta los datos a la BBDD Local */
                if (sincronizador.isEjecutado()) {
                    ok = sincronizador.insertarRegistros(sincronizador.getInsertClientes(), false, "clientes");
                    if (!ok) {
                        JOptionPane.showInternalMessageDialog(null, "Error insertar clientes ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (sincronizador.isEjecutado()) {
                    ok = sincronizador.insertarRegistros(sincronizador.getInsertProductos(), false, "productos");
                    if (!ok) {
                        JOptionPane.showInternalMessageDialog(null, "Error insertar productos ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (sincronizador.isEjecutado()) {
                    ok = sincronizador.insertarRegistros(sincronizador.getInsertFacturas(), false, "facturas");
                    if (!ok) {
                        JOptionPane.showInternalMessageDialog(null, "Error insertar Facturas ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (sincronizador.isEjecutado()) {
                    ok = sincronizador.insertarRegistros(sincronizador.getInsertProductosPorFactura(), false, "detalle Factura");
                    if (!ok) {
                        JOptionPane.showInternalMessageDialog(null, "Error insertar el detalle de la Facturas ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                /*Termina el proceso indicando que ya se hizo la trasmision actualizando el campo de la factura trasmitido a 1 */
                new Thread(new HiloActualizarCerrarSincronizacion(sincronizador)).start();

            } else {
                System.out.println("No hay registros en el momento" + new java.util.Date());
            }

        } catch (IOException ex) {
            System.out.println("Proceso no culminado por errores de conexion" + new java.util.Date());
            Logger.getLogger(Sincronizacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Sincronizacion(Inicio ini) {
        boolean ok = false;
        IniciarSincronizacion sincronizador = new IniciarSincronizacion();
        /*Se cargan las variables de inicio del sistema*/
        sincronizador.setBdFuente(ini.getBdLocal());
        sincronizador.setUrlFuente(ini.getUrlLocal());
        sincronizador.setServerFuente(ini.getServerLocal());
        sincronizador.setPuertoFuente(ini.getPuertoLocal());
        sincronizador.setUsuarioBDFuente(ini.getUsuarioBDLocal());
        sincronizador.setClaveBDFuente(ini.getClaveBDLocal());
        sincronizador.setBdDestino(ini.getBdRemota());
        sincronizador.setUrlDestino(ini.getUrlRemota());
        sincronizador.setServerDestino(ini.getServerRemota());
        sincronizador.setPuertoDestino(ini.getPuertoRemota());
        sincronizador.setUsuarioBDDestino(ini.getUsuarioBDRemota());
        sincronizador.setClaveBDdestino(ini.getClaveBDRemota());
        //sincronizador.setCadenaFuente(sincronizador.getUrlFuente()+ "://" + sincronizador.getServerFuente()+ ":" + sincronizador.getPuertoFuente()+ "/" + sincronizador.getBdFuente());
        //sincronizador.setCadenaDestino(sincronizador.getUrlDestino()+ "://" + sincronizador.getServerDestino()+ ":" + sincronizador.getPuertoDestino()+ "/" + sincronizador.getBdDestino());
        String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota";
        String noUsarSSL = "&useSSL=false";
        sincronizador.setCadenaFuente(String.format("jdbc:mysql://%s/%s?%s%s", sincronizador.getServerFuente(), sincronizador.getBdFuente(), husoHorario, noUsarSSL));
        sincronizador.setCadenaDestino(String.format("jdbc:mysql://%s/%s?%s%s", sincronizador.getServerDestino(), sincronizador.getBdDestino(), husoHorario, noUsarSSL));
        /*Valida si hay registros para trasmitir*/
        if (sincronizador.verificarRegistros()) {

            /*Genera las sentencias para insetar datos trasmitidos */
            sincronizador.getInsertClientes(false);
            sincronizador.getInsertProductos(false);
            sincronizador.getInsertProductosPorFactura(false);

            /*Inserta los datos a la BBDD Local */
            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertClientes(), false, "clientes");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar clientes ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertProductos(), false, "productos");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar productos ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertFacturas(), false, "facturas");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar Facturas ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertProductosPorFactura(), false, "detalle Factura");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar el detalle de la Facturas ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            /*Termina el proceso indicando que ya se hizo la trasmision actualizando el campo de la factura trasmitido a 1 */
            new Thread(new HiloActualizarCerrarSincronizacion(sincronizador)).start();

        } else {
            System.out.println("No hay registros en el momento" + new java.util.Date());
        }

    }

    public Sincronizacion(FIntegrador fintegrador) {
        this.ini = fintegrador.ini;
       
        mensaje += fintegrador.mensaje;

        boolean ok = false;
        IniciarSincronizacion sincronizador = new IniciarSincronizacion();
        /*Se cargan las variables de inicio del sistema*/
        sincronizador.setBdFuente(ini.getBdLocal());
        sincronizador.setUrlFuente(ini.getUrlLocal());
        sincronizador.setServerFuente(ini.getServerLocal());
        sincronizador.setPuertoFuente(ini.getPuertoLocal());
        sincronizador.setUsuarioBDFuente(ini.getUsuarioBDLocal());
        sincronizador.setClaveBDFuente(ini.getClaveBDLocal());
        sincronizador.setBdDestino(ini.getBdRemota());
        sincronizador.setUrlDestino(ini.getUrlRemota());
        sincronizador.setServerDestino(ini.getServerRemota());
        sincronizador.setPuertoDestino(ini.getPuertoRemota());
        sincronizador.setUsuarioBDDestino(ini.getUsuarioBDRemota());
        sincronizador.setClaveBDdestino(ini.getClaveBDRemota());
        //sincronizador.setCadenaFuente(sincronizador.getUrlFuente()+ "://" + sincronizador.getServerFuente()+ ":" + sincronizador.getPuertoFuente()+ "/" + sincronizador.getBdFuente());
        //sincronizador.setCadenaDestino(sincronizador.getUrlDestino()+ "://" + sincronizador.getServerDestino()+ ":" + sincronizador.getPuertoDestino()+ "/" + sincronizador.getBdDestino());
        String husoHorario = "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/Bogota";
        String noUsarSSL = "&useSSL=false";
        sincronizador.setCadenaFuente(String.format("jdbc:mysql://%s/%s?%s%s", sincronizador.getServerFuente(), sincronizador.getBdFuente(), husoHorario, noUsarSSL));
        sincronizador.setCadenaDestino(String.format("jdbc:mysql://%s/%s?%s%s", sincronizador.getServerDestino(), sincronizador.getBdDestino(), husoHorario, noUsarSSL));
        
        /*Valida si hay registros para trasmitir*/
        if (sincronizador.verificarRegistros()) {

           /*Genera las sentencias para insetar datos trasmitidos */
            sincronizador.getInsertClientes(false);
            mensaje += " Iniciando recopilacion de  clientes \n";
            fintegrador.txtLog.setText(mensaje);
            sincronizador.getInsertProductos(false);
            mensaje += " Iniciando recopilacion de   Productos \n";
            fintegrador.txtLog.setText(mensaje);
            sincronizador.getInsertProductosPorFactura(false);
            mensaje += " Iniciando recopilacion de   detalles de la factura \n";
            fintegrador.txtLog.setText(mensaje);
            

            /*Inserta los datos a la BBDD Local */
             mensaje += " Iniciando el ingreso de datos a la BBDD remota \n";
            fintegrador.txtLog.setText(mensaje);
            
            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertClientes(), false, "clientes");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar clientes ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                mensaje += " Clientes ingresados a la BBDD Remota \n";
                fintegrador.txtLog.setText(mensaje);
            }

            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertProductos(), false, "productos");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar productos ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                mensaje += " Productos ingresados a la BBDD Remota  \n";
                fintegrador.txtLog.setText(mensaje);
            }

            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertFacturas(), false, "facturas");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar Facturas ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                mensaje += " Facturas ingresadas a la BBDD Remota  \n";
                fintegrador.txtLog.setText(mensaje);
            }

            if (sincronizador.isEjecutado()) {
                ok = sincronizador.insertarRegistros(sincronizador.getInsertProductosPorFactura(), false, "detalle Factura");
                if (!ok) {
                    JOptionPane.showInternalMessageDialog(null, "Error insertar el detalle de la Facturas ", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                mensaje += " Detalle de la factura ingresado a la BBDD  Remota  \n";
                fintegrador.txtLog.setText(mensaje);
            }

            /*Termina el proceso indicando que ya se hizo la trasmision actualizando el campo de la factura trasmitido a 1 */
            sincronizador.cerrarSincronizacion(true);
            //new Thread(new HiloActualizarCerrarSincronizacion(sincronizador)).start();
             mensaje += " Fin de la sincronizacionn a la BBDD  Remota  \n";
             fintegrador.txtLog.setText(mensaje);

        } else {
            System.out.println("No hay registros en el momento" + new java.util.Date());
        }

    }

}
