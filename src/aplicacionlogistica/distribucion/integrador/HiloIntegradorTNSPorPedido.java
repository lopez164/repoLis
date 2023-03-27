/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloIntegradorTNSPorPedido implements Runnable {
    //String mensaje = "";
    Inicio ini;

  
    FManifestarPedidosEnRuta fManifestarPedidosEnRuta;
    String listaDeNumerosDeFacturas;

    List<String> listaDeSentenciasClientes = null;
    List<String> listaDeSentenciasFacturas = null;
    List<String> listaDeSentenciasProductosCamdum = null;
    List<String> listaDeSentenciasDetalleFactura = null;

    String prefijo, numeroFactura;
    
   

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIntegradorTNSPorPedido(Inicio ini) {
       
        this.ini = ini;

    }
    
    
     public HiloIntegradorTNSPorPedido(String prefijo, String numeroFactura) {
      this.prefijo= prefijo;
      this.numeroFactura= numeroFactura;

    }
    @Override
    public void run() {
   
        traerClientes();
        traerProductos();
        traerFacturas();
        traerDetalleFacturas();
       
    }

    private Boolean traerClientes() {
        boolean ingresado = false;

        Connection conTNS = null;
        Connection conPIPLocal = null;
        Connection conPIPRemota = null;

        Statement statementTNS = null;
        Statement statementPIPLocal = null;
        Statement statementPIPRemota = null;

        ResultSet rstTNS = null;
        ResultSet rstPIP = null;

        String prefijos = ini.getPrefijos().replace("-", "");

        try {

            String codigosClientes = "select  DISTINCT t.NIT ,t.NITTRI,"
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
                    + "join KARDEX k on k.CLIENTE=t.TERID "
                    + "and k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' AND K.NUMERO='" + numeroFactura + "';";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(codigosClientes);

            int i = 0;
            while (rstTNS.next()) {
                String sqlClientes = "insert into clientescamdun(codigoInterno,nitCliente,nombreEstablecimiento,"
                        + "nombreDeCliente,direccion,barrio,ciudad,clasificacion,"
                        + "celularCliente,emailCliente,fechaDeIngresoCliente,"
                        + "latitud,longitud,canalDeVenta,ruta,frecuencia,"
                        + "zona,regional,agencia,porcentajeDescuento,"
                        + "activo,fechaIng,usuario,flag) VALUES('"
                        + rstTNS.getString("NIT") + "','" //codigoInterno +
                        + rstTNS.getString("NITTRI") + "','" // nitCliente +
                        + rstTNS.getString("ESTABLECIMIENTO").replaceAll(System.getProperty("line.separator"), "") + "','" // nombreEstablecimiento
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

                statementPIPLocal.addBatch(sqlClientes);
                statementPIPRemota.addBatch(sqlClientes);

                Thread.sleep(1);

                statementPIPLocal.executeBatch();
                statementPIPRemota.executeBatch();

            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            conPIPLocal.close();
            conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                conPIPLocal.close();
                conPIPRemota.close();

                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private Boolean traerProductos() {
        boolean ingresado = false;

        Connection conTNS = null;
        Connection conPIPLocal = null;
        Connection conPIPRemota = null;

        Statement statementTNS = null;
        Statement statementPIPLocal = null;
        Statement statementPIPRemota = null;

        ResultSet rstTNS = null;
        ResultSet rstPIP = null;


        try {

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
                    + "and k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and K.NUMERO='" + numeroFactura + "';";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(sqlListaCodigosproductos);

            int i = 0;
            while (rstTNS.next()) {

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

                statementPIPLocal.addBatch(sqlProductos);
                statementPIPRemota.addBatch(sqlProductos);

                /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                statementPIPLocal.executeBatch();
                statementPIPRemota.executeBatch();

            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            conPIPLocal.close();
            conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                conPIPLocal.close();
                conPIPRemota.close();

                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        return ingresado;
    }

    private Boolean traerFacturas() {
        boolean ingresado = false;
        boolean encontrado = false;

        Connection conTNS = null;
        Connection conPIPLocal = null;
        Connection conPIPRemota = null;

        Statement statementTNS = null;
        Statement statementPIPLocal = null;
        Statement statementPIPRemota = null;

        ResultSet rstTNS = null;
        ResultSet rstPIP;

        try {

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
                    + "where  "
                    + "r.CODPREFIJO ='" + prefijo + "' AND r.CODCOMP='FV' and r.NUMERO ='" + numeroFactura + "';";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(sqlListaFacturas);

            int i = 0;
            while (rstTNS.next()) {

                String sqlfacturas = "INSERT INTO facturascamdun("
                        + "numeroFactura,fechaDeVenta,cliente,direccion,barrio,ciudad,telefono,"
                        + "vendedor,formaDePago,canal,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
                        + "valorRechazo,valorDescuento,valorTotalRecaudado,formato,zona,"
                        + "regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,flag,pesofactura,"
                        + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor) VALUES('"
                        + rstTNS.getString("numeroFactura") + "','"
                        + rstTNS.getString("fechaDeVenta") + "','"
                        + rstTNS.getString("cliente") + "','"
                        + rstTNS.getString("direccion") + "','"
                        + rstTNS.getString("barrio") + "','"
                        + rstTNS.getString("ciudad") + "','"
                        + rstTNS.getString("telefono") + "','"
                        + rstTNS.getString("vendedor") + "','"
                        + rstTNS.getString("formaDePago") + "','"
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
                        + rstTNS.getString("telefonoVendedor") + "') "
                        + "on duplicate key update flag='-1',"
                        + "telefonoVendedor='" + rstTNS.getString("telefonoVendedor") + "';";

                i++;
                statementPIPLocal.addBatch(sqlfacturas);
                statementPIPRemota.addBatch(sqlfacturas);

                /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                statementPIPLocal.executeBatch();
                statementPIPRemota.executeBatch();

            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            conPIPLocal.close();
            conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                conPIPLocal.close();
                conPIPRemota.close();

    
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        return ingresado;
    }

    private Boolean traerDetalleFacturas() {
        boolean ingresado = false;

        Connection conTNS = null;
        Connection conPIPLocal = null;
        Connection conPIPRemota = null;

        Statement statementTNS = null;
        Statement statementPIPLocal = null;
        Statement statementPIPRemota = null;

        ResultSet rstTNS = null;
        ResultSet rstPIP;

        try {

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
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID  "
                    + "and k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO='" + numeroFactura + "' "
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "GROUP BY factura,codigoProducto,descripcionProducto,d.CANLISTA,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by factura,codigoProducto";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement(); //ojo

            rstTNS = statementTNS.executeQuery(sqlListaDetalleFactura);
            int i = 0;

            while (rstTNS.next()) {

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
                        + "descripcionProducto ='" + rstTNS.getString("descripcionProducto") + "';";
                // + ",valorTotalConIva='" + rstTNS.getString("valorTotalConIva") + "';";

                i++;
                statementPIPLocal.addBatch(sqlDetalleFacturas);
                statementPIPRemota.addBatch(sqlDetalleFacturas);

                /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                statementPIPLocal.executeBatch();
                statementPIPRemota.executeBatch();

                Thread.sleep(2);

            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            conPIPLocal.close();
            conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                conPIPLocal.close();
                conPIPRemota.close();

                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    public boolean actualizarPesoFacturas() {
        boolean actualizado = false;
        String fecha = "";
        try {

            String sql = "update facturascamdun fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actualizado;
    }

    public boolean actualizarCanaleVentaClientes(boolean a) {
        boolean actualizado = false;
        try {

            String fecha = "";

            String sql = "update facturascamdun f "
                    + "join clasificacionRutas c "
                    + "on f.ruta=c.idclasificacionRutas "
                    + "set f.canal=c.tipoDeCanalDeVenta "
                    + "where f.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNSPorPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actualizado;
    }

   
}
