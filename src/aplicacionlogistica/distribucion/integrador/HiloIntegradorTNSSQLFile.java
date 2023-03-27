/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloIntegradorTNSSQLFile implements Runnable {

    private JProgressBar jProgressBar;
    private int value;//retardo en milisegundos
    //String mensaje = "";
    Inicio ini;

   FIntegradorExcelFile fIntegradorExcelFile = null;
    String listaDeNumerosDeFacturas;
    String fechaArchivo;
    
    

    List<String> listaDeSentenciasClientes = null;
    List<String> listaDeSentenciasFacturas = null;
    List<String> listaDeSentenciasProductosCamdum = null;
    List<String> listaDeSentenciasDetalleFactura = null;

    int cantidadClientes = 0;
    int cantidadProductos = 0;
    int cantidadFacturas = 0;
    int cantidadItemsFactura = 0;
    int cantidadRegistros = 0;
    Date fecha1 ;
    
   

    /**
     * Constructor de clase
     *
     * @param fIntegrador
     */
    public HiloIntegradorTNSSQLFile(FIntegradorExcelFile fIntegradorExcelFile) {
        this.fIntegradorExcelFile = fIntegradorExcelFile;
        this.ini = fIntegradorExcelFile.ini;

    }

    @Override
    public void run() {

        
        //FileReader f = null;
        //String listaDeNumerosDeFacturas = null;
        
        String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
        if(fIntegradorExcelFile.chkClientes.isSelected()){
            setCantidadClientes();
        }
        if(fIntegradorExcelFile.chkProductos.isSelected()){
            setCantidadProducto();
        }
        if(fIntegradorExcelFile.chkFacturas.isSelected()){
            setCantidadFacturas();
        }
        if(fIntegradorExcelFile.chkDetalleFacturas.isSelected()){
            setCantidadItemsFactura();
        }
        fIntegradorExcelFile.barra2.setValue(0);
         fIntegradorExcelFile.lblBarra2.setText( "0.0 %");
        cantidadRegistros=cantidadClientes + cantidadFacturas + cantidadItemsFactura + cantidadProductos;
        fIntegradorExcelFile.jBtnCirculo.setVisible(true);
        fecha1 = new Date();
        fIntegradorExcelFile.mensaje += "Inico de proceso de trasferencia : " + fecha1 + "\n";
        fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
        fIntegradorExcelFile.mensaje += "Cantidad de Registros = " + cantidadRegistros + "\n";
        
        if(cantidadRegistros==0){
             fIntegradorExcelFile.finDeTrasmision(fecha1);
             
            return;
        }
        fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
        if (fIntegradorExcelFile.chkClientes.isSelected()) {
            fIntegradorExcelFile.mensaje += "Ingresando los clientes \n";
            fIntegradorExcelFile.lblBarra1.setText("Ingresando los clientes");
            fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
            
            if(fIntegradorExcelFile.cancelar){
                     fIntegradorExcelFile.finDeTrasmision(fecha1);
              fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                    return ;
             }
            
            traerClientes();
            fIntegradorExcelFile.chkClientes.setSelected(false);
        }
        if (fIntegradorExcelFile.chkProductos.isSelected()) {
            fIntegradorExcelFile.mensaje += "Ingresando los productos \n";
              fIntegradorExcelFile.lblBarra1.setText("Ingresando los productos");
            fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

             if(fIntegradorExcelFile.cancelar){
                     fIntegradorExcelFile.finDeTrasmision(fecha1);
                      fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                  return ;
             }
            traerProductos();
            fIntegradorExcelFile.chkProductos.setSelected(false);
        }
        if (fIntegradorExcelFile.chkFacturas.isSelected()) {
            fIntegradorExcelFile.mensaje += "Ingresando las facturas \n";
             fIntegradorExcelFile.lblBarra1.setText("Ingresando las facturas");
            fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
             if(fIntegradorExcelFile.cancelar){
                     fIntegradorExcelFile.finDeTrasmision(fecha1);
                   fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                    return ;
             }
            traerFacturas();
            fIntegradorExcelFile.chkFacturas.setSelected(false);
        }
        if (fIntegradorExcelFile.chkDetalleFacturas.isSelected()) {
            fIntegradorExcelFile.mensaje += "Ingresando el detalle de las facturas \n";
            fIntegradorExcelFile.lblBarra1.setText("Ingresando el detalle de las facturas");
            fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
            
             if(fIntegradorExcelFile.cancelar){
                     fIntegradorExcelFile.finDeTrasmision(fecha1);
                     fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                    return ;
             }
            traerDetalleFacturas();
            
            fIntegradorExcelFile.chkDetalleFacturas.setSelected(false);
            fIntegradorExcelFile.barra2.setValue(100);
            fIntegradorExcelFile.lblBarra2.setText("100 %");
            fIntegradorExcelFile.lblBarra2.setText("Proceso de sincronizacio terminado");
        }
//        if(fIntegradorExcelFile.chkPesoFacturas.isSelected()){
//             if(fIntegradorExcelFile.cancelar){
//                     fIntegradorExcelFile.finDeTrasmision(fecha1);
//                     fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
//                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
//                    return ;
//             }
//            actualizarPesoFacturas();
//            fIntegradorExcelFile.mensaje += "Actualizado el peso de kg de las facturas \n";;
//            fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
//           
//        }
//        if(fIntegradorExcelFile.chkCanaFacturas.isSelected()){
//             if(fIntegradorExcelFile.cancelar){
//                     fIntegradorExcelFile.finDeTrasmision(fecha1);
//                     fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
//                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
//                    return ;
//             }
//            actualizarCanaleVentaClientes(true);
//            fIntegradorExcelFile.mensaje += "Actualizado el canal de venta de las facturas \n";;
//            fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
//            fIntegradorExcelFile.chkCanaFacturas.setSelected(false);
//        }
        
        fIntegradorExcelFile.finDeTrasmision(fecha1);

    }

  


    private Boolean traerClientes() {
        boolean ingresado = false;

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;
        java.io.BufferedWriter bufferedWriter ; // bufferedWriter = new BufferedWriter(new FileWriter(ini.getRutaDeApp() + "tmp/listaDeRegistros" + fechaArchivo + ".sql", true));  bufferedWriter.append(sqlClientes); bufferedWriter.newLine();  bufferedWriter.flush();
                
       
        String prefijos = ini.getPrefijos().replace("-", "");
        fIntegradorExcelFile.barra1.setValue(2);

        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;
            fechaArchivo=anio + mes + dia;
            bufferedWriter = new BufferedWriter(new FileWriter(ini.getRutaDeApp() + "tmp/listaDeRegistros_" + fechaArchivo + ".sql", true));

                   String sqlListaCodigosClientes = "select  DISTINCT t.NIT ,t.NITTRI,"
                    + "COALESCE(t.ESTABLECIMIENTO,'NA') as ESTABLECIMIENTO,"
                    + "t.NOMBRE as nombreCliente,t.DIRECC1,k.ZONATERCERO as barrio,"
                    + "c.NOMBRE as ciudad,'1' AS clasificacion,SUBSTRING(t.TELEF1 FROM 1 FOR 12) as telefonoCliente,"
                    + "t.EMAIL,'CURRENT_DATE()' AS fechaDeIngresoCliente,'0' AS latitud,"
                    + "'0' AS longitud,'1' AS canalDeVenta,'1' AS  ruta,"
                    + "'1' AS frecuencia,'1' AS zona,'1' AS regional,'1' AS agencia,"
                    + "'0' AS porcentajeDescuento,'1' AS activo,'CURRENT_TIMESTAMP()' AS fechaIng,"
                    + "'AUTOMATICO ' AS usuario, '1' AS flag "
                    + "from terceros t "
                    + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                    + "join KARDEX k on k.CLIENTE=t.TERID and k.FECHA>='" + fecha2 + ", 00:00:00' "
                    + "and k.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' ;";


          //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            
            
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaCodigosClientes);

            int i = 0;
            while (rstTNS.next()) {
                if(fIntegradorExcelFile.cancelar){
                      fIntegradorExcelFile.finDeTrasmision(fecha1);
                      fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                    return false;
                }
                String sqlClientes = "insert into clientescamdun(codigoInterno,nitCliente,nombreEstablecimiento,"
                        + "nombreDeCliente,direccion,barrio,ciudad,clasificacion,"
                        + "celularCliente,emailCliente,fechaDeIngresoCliente,"
                        + "latitud,longitud,canalDeVenta,ruta,frecuencia,"
                        + "zona,regional,agencia,porcentajeDescuento,"
                        + "activo,fechaIng,usuario,flag) VALUES('"
                        + rstTNS.getString("NIT") + "','" //codigoInterno +
                        + rstTNS.getString("NITTRI") + "','" // nitCliente +
                        + rstTNS.getString("ESTABLECIMIENTO").replaceAll(System.getProperty("line.separator"),"") + "','" // nombreEstablecimiento
                        + rstTNS.getString("nombreCliente") + "','" // nombreDeCliente
                        + rstTNS.getString("DIRECC1") + "','" // direccion
                        //+ rst.getString("ZONATERCERO") + "','" // barrio (vacio) ZONATERCERO
                        + "','" // barrio (vacio) ZONATERCERO
                        + rstTNS.getString("ciudad") + "','" // ciudad
                        + "1','" // clasificacion
                        + rstTNS.getString("telefonoCliente") + "','" // celularCliente
                        + rstTNS.getString("EMAIL") + "'," // emailCliente
                        + "CURRENT_DATE(),'" // fechaDeIngresoCliente +
                        + "0','" // latitud
                        + "0','" // longitud
                        + "1','" // canalDeVenta
                        + "1','" // ruta
                        + "1','" // frecuencia
                        + "1','" // zona
                        + "1','" // regional
                        + "1','" // agencia
                        + "0','" // porcentajeDescuento
                        + "1'," // activo
                        + "CURRENT_TIMESTAMP(),'" // fechaIng
                        + "AUTOMATICO','" //usuario
                        + "1') on duplicate key update "
                        + " flag='-1';";
                i++;

                
            bufferedWriter.append(sqlClientes);
            bufferedWriter.newLine();
           

                Thread.sleep(1);
                
                

            }


            bufferedWriter.flush();
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);

                 statementTNS.close();
                 conTNS.close();
                 rstTNS.close();
                fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de clientes \n" + ex + "\n";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private Boolean traerProductos() {
        boolean ingresado = false;

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;
        java.io.BufferedWriter bufferedWriter ;
        
        String prefijos = ini.getPrefijos().replace("-", "");;
        fIntegradorExcelFile.barra1.setValue(2);
        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;
            bufferedWriter = new BufferedWriter(new FileWriter(ini.getRutaDeApp() + "tmp/listaDeRegistros_" + fechaArchivo + ".sql", true));

            String sqlListaCodigosproductos = "SELECT DISTINCT r.MATID,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "lm.DESCRIP as linea,"
                    + "'0' as valorUnitario,"
                    + "'0' as valorUnitarioConIva,"
                    + "'1' as isFree,"
                    + "COALESCE(m.PESO,'0') as pesoProducto,"
                    + "'0' as largoProducto,"
                    + "'0' as anchoProducto,"
                    + "'0' as altoProducto,"
                    + "'1' as activo,"
                    + "'CURRENT_TIMESTAMP()' as fechaIng,"
                    + "'AUTOMATICO' AS usuario,"
                    + "'1' as flag,"
                    + "COALESCE(m.CODBARRA,'') as barcode "
                    + "FROM DEKARDEX r "
                    + "join MATERIAL m on m.MATID=r.MATID "
                    + "join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID AND  "
                    + "k.FECHA>='" + fecha2 + ", 00:00:00' and k.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' ;";


            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaCodigosproductos);

            int i = 0;
            while (rstTNS.next()) {
                
                 if(fIntegradorExcelFile.cancelar){
                       fIntegradorExcelFile.finDeTrasmision(fecha1);
                       fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                    return false;
                }
                String sqlProductos = "INSERT INTO productoscamdun(codigoProducto,descripcionProducto,linea,valorUnitario,"
                        + "valorUnitarioConIva,isFree,pesoProducto,largoProducto,anchoProducto,altoProducto,"
                        + "activo,fechaIng,usuario,flag,barcode)"
                        + "VALUES('"
                        + rstTNS.getString("codigoProducto") + "','"
                        + rstTNS.getString("descripcionProducto") + "','"
                        + rstTNS.getString("linea") + "','"
                        + rstTNS.getString("valorUnitario") + "','"
                        + rstTNS.getString("valorUnitarioConIva") + "','"
                        + rstTNS.getString("isFree") + "','"
                        + rstTNS.getString("pesoProducto") + "','"
                        + rstTNS.getString("largoProducto") + "','"
                        + rstTNS.getString("anchoProducto") + "','"
                        + rstTNS.getString("altoProducto") + "','"
                        + rstTNS.getString("activo") + "',"
                        + "CURRENT_TIMESTAMP()" + ",'"
                        + rstTNS.getString("usuario") + "','"
                        + rstTNS.getString("flag") + "','"
                        + rstTNS.getString("barcode") + "') "
                        + "on duplicate key update flag='-1',"
                        + "descripcionProducto='" + rstTNS.getString("descripcionProducto") + "';";
                i++;

                  bufferedWriter.append(sqlProductos);
                  bufferedWriter.newLine();
               
                
                 Thread.sleep(2);
            }

           bufferedWriter.flush();
            statementTNS.close();
            conTNS.close();
            rstTNS.close();
            
            

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                 conTNS.close();
                rstTNS.close();
                
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de productos \n " + ex + "\n";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private Boolean traerFacturas() {
        boolean ingresado = false;
        boolean encontrado=false;

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;
        
        java.io.BufferedWriter bufferedWriter ; // 
       
       
        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;

            bufferedWriter = new BufferedWriter(new FileWriter(ini.getRutaDeApp() + "tmp/listaDeRegistros_" + fechaArchivo + ".sql", true));             String prefijos = ini.getPrefijos().replace("-", "");

            fIntegradorExcelFile.barra1.setValue(2);
            
            
            String sqlListaFacturas = "SELECT r.KARDEXID,r.numero,"
                    + "r.CODPREFIJO||'-'||r.NUMERO as numeroFactura,"
                    + "r.FECHA as fechaDeVenta,"
                    + "t.NIT as cliente,"
                    + "t.DIRECC1 as  direccion,"
                    + "r.ZONATERCERO as barrio,"
                    + "c.NOMBRE as ciudad,"
                    + "SUBSTRING(t.TELEF1 FROM 1 FOR 12) as telefono,"
                    + "r.NOMVENDEDOR as vendedor," 
                    + "SUBSTRING(t2.TELEF1 FROM 1 FOR 12) as telefonoVendedor,"
                    + "'1' as formaDePago,"
                    + "'1' as canal,"
                    + "r.VRBASE AS valorFacturaSinIva,"
                    + "r.VRIVA AS valorIvaFactura,"
                    + "r.TOTAL AS valorTotalFactura,"
                    + "'0' as valorRechazo,"
                    + "'0' as valorDescuento,"
                    + "'0' as valorTotalRecaudado,"
                    //  + "-- imagenFactura,"
                    + "'PNG' as formato,"
                    + "'1' as zona,"
                    + "'1' as regional,"
                    + "'1' as agencia,"
                    + "'1' as isFree,"
                    + "'1' as estadoFactura,"
                    + "'1' as activo,'"
                    + "CURRENT_TIMESTAMP()' as fechaIng,"
                    + "'AUTOMATICO' AS usuario,"
                    + "'0' AS flag," // tiene que ser cero por las salidas a distribucion
                    + "'0' AS pesofactura,"
                    + "r.CODVEN AS ruta,"
                    + "'0' AS trasmitido,"
                    + "'0' AS numeroDescuento,"
                    + "'0' AS numeroRecogida "
                    + "FROM KARDEX r "
                    + "join TERCEROS t on  r.cliente=t.terid "
                    + "join TERCEROS t2 on r.CODVEN=t2.nit "
                    + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                    + "where r.FECHA>='" + fecha2 + ", 00:00:00' and r.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and r.CODPREFIJO in (" + prefijos + ") AND r.CODCOMP='FV' ;";
             

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaFacturas);

            int i = 0;
            while (rstTNS.next()) {
                
                 if(fIntegradorExcelFile.cancelar){
                       fIntegradorExcelFile.finDeTrasmision(fecha1);
                    return false;
                }
                               
                 String sqlfacturas = "INSERT INTO facturascamdun("
                        + "numeroFactura,fechaDeVenta,cliente,direccion,barrio,ciudad,telefono,"
                        + "formaDePago,vendedor,canal,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
                        + "valorRechazo,valorDescuento,valorTotalRecaudado,formato,zona,"
                        + "regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,flag,pesofactura,"
                        + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor,plazoDias) VALUES('"
                        + rstTNS.getString("numeroFactura") + "','"
                        + rstTNS.getString("fechaDeVenta") + "','"
                        + rstTNS.getString("cliente") + "','"
                        + rstTNS.getString("direccion") + "','"
                        + rstTNS.getString("barrio") + "','"
                        + rstTNS.getString("ciudad") + "','"
                        + rstTNS.getString("telefono") + "','";
                        if(rstTNS.getInt("plazoDias")> 1){
                         sqlfacturas += "CREDITO','";
                        }else{
                        sqlfacturas += "CONTADO','";
                        }
                        sqlfacturas += rstTNS.getString("vendedor") + "','"
                        + rstTNS.getString("canal") + "','"
                        + rstTNS.getString("valorFacturaSinIva") + "','"
                        + rstTNS.getString("valorIvaFactura") + "','"
                        + rstTNS.getString("valorTotalFactura") + "','"
                        + rstTNS.getString("valorRechazo") + "','"
                        + rstTNS.getString("valorDescuento") + "','"
                        + rstTNS.getString("valorTotalRecaudado") + "','"
                        + rstTNS.getString("formato") + "','"
                        + rstTNS.getString("zona") + "','"
                        + rstTNS.getString("regional") + "','"
                        + rstTNS.getString("agencia") + "','"
                        + rstTNS.getString("isFree") + "','"
                        + rstTNS.getString("estadoFactura") + "','"
                        + rstTNS.getString("activo") + "',"
                        + "CURRENT_TIMESTAMP(),'"
                        + rstTNS.getString("usuario") + "','"
                        + rstTNS.getString("flag") + "','"
                        + rstTNS.getString("pesofactura") + "','"
                        + rstTNS.getString("ruta") + "','"
                        + rstTNS.getString("trasmitido") + "','"
                        + rstTNS.getString("numeroDescuento") + "','"
                        + rstTNS.getString("numeroRecogida") + "','"
                        + rstTNS.getString("telefonoVendedor") + "','"
                        + rstTNS.getInt("plazoDias") + "') "
                        + "on duplicate key update flag='-1',"
                        + "telefonoVendedor='" +  rstTNS.getString("telefonoVendedor") + "';";

                i++;
                   bufferedWriter.append(sqlfacturas); 
                   bufferedWriter.newLine();  
       
               
                 Thread.sleep(2);
               
            }
           
           bufferedWriter.flush();
            statementTNS.close();
            conTNS.close();
            rstTNS.close();;
            

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
               
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de facturas \n" +ex;
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private Boolean traerDetalleFacturas() {
        boolean ingresado = false;

        Connection conTNS = null;
         Statement statementTNS = null;
        ResultSet rstTNS = null;
        
        java.io.BufferedWriter bufferedWriter ;
        
        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;
            
            bufferedWriter = new BufferedWriter(new FileWriter(ini.getRutaDeApp() + "tmp/listaDeRegistros_" + fechaArchivo + ".sql", true)); 

            String prefijos = ini.getPrefijos().replace("-", "");;
            fIntegradorExcelFile.barra1.setValue(2);
            
                          
            String sqlListaDetalleFactura = "SELECT k.NUMERO,"
                    + "k.CODPREFIJO||'-'||k.NUMERO as factura,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "SUM(d.CANLISTA) AS CANTIDAD, "
                    + "d.PRECIOBASE as valorUnitario,"
                    + " (d.PRECIOBASE * SUM(d.CANLISTA)) as valorTotal,"
                    + "d.PRECIOVTA as valorUnitarioConIva,"
                    + "(d.PRECIOVTA * SUM(d.CANLISTA)) as valorTotalConIva,"
                    + "(COALESCE(m.PESO,0) * SUM(d.CANLISTA)) as pesoProducto "
                    + "FROM "
                    + "DEKARDEX  d "
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID AND k.FECHA>='" + fecha2 + ", 00:00:00' "
                    + "and k.FECHA<='" + fecha2 + ", 23:59:59' and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' "
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "GROUP BY factura,codigoProducto,descripcionProducto,d.CANLISTA,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by factura,codigoProducto";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
           
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaDetalleFactura);
            int i = 0;

            while (rstTNS.next()) {
                 if(fIntegradorExcelFile.cancelar){
                       fIntegradorExcelFile.finDeTrasmision(fecha1);
                       fIntegradorExcelFile.lblBarra1.setText( "Proceso Cancelado");
                       fIntegradorExcelFile.lblBarra2.setText( "Proceso Cancelado");
                    return false;
                }
                    
                String sqlDetalleFacturas = "INSERT INTO productosporfactura"
                        + "(factura,codigoProducto,cantidad,valorUnitario,valorTotal,valorUnitarioConIva,"
                        + "valorTotalConIva,pesoProducto,activo,fechaIng,usuario,flag,descripcionProducto) VALUES('"
                        + rstTNS.getString("factura") + "','"
                        + rstTNS.getString("codigoProducto") + "','"
                        + rstTNS.getString("cantidad") + "','"
                        + rstTNS.getString("valorUnitario") + "','"
                        + rstTNS.getString("valorTotal") + "','"
                        + rstTNS.getString("valorUnitarioConIva") + "','"
                        + rstTNS.getString("valorTotalConIva") + "','"
                        + rstTNS.getString("pesoProducto") + "','"
                        + "1'," // activo
                        + "CURRENT_TIMESTAMP(),'"
                       + "AUTOMATICO','"
                        + "1','"
                        + rstTNS.getString("descripcionProducto") + "') "
                        + "on duplicate key update flag='-1',"
                        + "descripcionProducto ='" +  rstTNS.getString("descripcionProducto") + "';"; 
                       // + ",valorTotalConIva='" + rstTNS.getString("valorTotalConIva") + "';";

                i++;
                 bufferedWriter.append(sqlDetalleFacturas); 
                 bufferedWriter.newLine();  
                

                 Thread.sleep(2);
            
            }

            bufferedWriter.flush();
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion del detalle de las facturas \n" + ex + "\n";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);

                  statementTNS.close();
                  conTNS.close();
                 rstTNS.close();
                
 
            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private void setCantidadClientes() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;

        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;

            String prefijos = ini.getPrefijos().replace("-", "");;

            
            String sqlListaFacturas = "select  COUNT(DISTINCT t.NIT) as cantidad "
                    + "from terceros t "
                    + "join KARDEX k on k.CLIENTE=t.TERID and k.FECHA>='" + fecha2 + ", 00:00:00' "
                    + "and k.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV'; ";
            

             //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaFacturas);

            int i = 0;
            if (rstTNS.next()) {

                cantidadClientes = rstTNS.getInt("cantidad");

            }
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en cantidad de clientes";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadProducto() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;

        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;

            String prefijos = ini.getPrefijos().replace("-", "");;

          
            String sqlListaProductos = "SELECT count(DISTINCT r.MATID) as cantidad "
                    + "FROM DEKARDEX r "
                    + "join MATERIAL m on m.MATID=r.MATID "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID AND "
                    + "k.FECHA>='" + fecha2 + ", 00:00:00' and k.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                            + "and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' ;";
           
           // conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaProductos);

            int i = 0;
            if (rstTNS.next()) {

                cantidadProductos = rstTNS.getInt("cantidad");

            }
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de productos";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadFacturas() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;

        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;

            String prefijos = ini.getPrefijos().replace("-", "");;
  
            String sqlListaFacturas = "SELECT count(DISTINCT k.KARDEXID) as cantidad "
                    + "FROM KARDEX k "
                    + "where K.FECHA>='" + fecha2 + ", 00:00:00' and K.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos +") AND k.CODCOMP='FV'; ";
                  
            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaFacturas);

            int i = 0;
            if (rstTNS.next()) {

                cantidadFacturas = rstTNS.getInt("cantidad");

            }
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de facturas";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadItemsFactura() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;

        try {
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            String[] arrSplit = fecha.split("-");
            String anio = arrSplit[0];
            String mes = arrSplit[1];
            String dia = arrSplit[2];
            String fecha2 = dia + "." + mes + "." + anio;

            String prefijos = ini.getPrefijos().replace("-", "");;

               String dqlListaItemsFacturas = "SELECT count(DISTINCT r.DEKARDEXID) as cantidad "
                    + "FROM DEKARDEX r "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID "
                    + "where k.FECHA>='" + fecha2 + ", 00:00:00' and k.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos +") AND k.CODCOMP='FV'; ";
                   
           // conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(dqlListaItemsFacturas);

            int i = 0;
            if (rstTNS.next()) {

                cantidadItemsFactura = rstTNS.getInt("cantidad");

            }
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();
                
                 fIntegradorExcelFile.mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de items de la factura ";
                this.fIntegradorExcelFile.txtLog.setText(fIntegradorExcelFile.mensaje);

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
     public boolean actualizarPesoFacturas() {
        boolean actualizado = false;
        String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
        try {

            String sql = "update facturascamdun fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actualizado;
    }
     
     public boolean actualizarCanaleVentaClientes(boolean a) {
       boolean actualizado = false;
         try {
           
            String fecha = ("" + fIntegradorExcelFile.ini.getFechaSql(fIntegradorExcelFile.jFechaInicial));
            
            
            String sql = "update facturascamdun f "
                    + "join clasificacionRutas c "
                    + "on f.ruta=c.idclasificacionRutas "
                    + "set f.canal=c.tipoDeCanalDeVenta "
                    + "where f.fechaDeVenta='" + fecha + "';";
            
            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);
            
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNSSQLFile.class.getName()).log(Level.SEVERE, null, ex);
        }
         return actualizado;
    }

      
}
