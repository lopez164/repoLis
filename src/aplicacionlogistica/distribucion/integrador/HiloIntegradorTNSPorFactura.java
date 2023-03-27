/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.configuracion.Inicio;
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
public class HiloIntegradorTNSPorFactura implements Runnable {

    private JProgressBar jProgressBar;
    private int value;//retardo en milisegundos
    String kardexid="";
    String mensaje = "";
    String prefijo;
    String factura;
    Inicio ini;

  
    
     List<String> listaDeSentenciasSql = null;
    

    /**
     * Constructor de clase
     *
     * @param fIntegrador
     */
    public HiloIntegradorTNSPorFactura(String prefijo, String factura) {
    this.prefijo = prefijo;
    this.factura = factura;  
        

    }

    @Override
    public void run() {
       

        String listaDeNumerosDeFacturas = null;

        try {
           
            
            organizarClientesYfacturas();
                   
            organizarProductosydetalleFactura(listaDeNumerosDeFacturas);
            
            ini.insertarDatosLocalmente(listaDeSentenciasSql);
            
            ini.insertarDatosRemotamente(listaDeSentenciasSql,"HiloIntegradorTNS");
           
             
                    
            

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNSPorFactura.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloIntegradorTNSPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void organizarClientesYfacturas() throws ClassNotFoundException, SQLException {
                Connection con;
        Statement statement;
        ResultSet rst;
         
            //String prefijos = "'P1','EC','ER','PO'";  //  parametrizar
            //String db = "C:/DATOS TNS/HIELERA2021.gdb?charSet=utf8"; // parametrizar
            //String db = "C:\\DATOS TNS\\HIELERA2021.GDB?charSet=utf8"; // parametrizar
            String db = "F:/datos tns/camdun2021.gdb?charSet=utf8"; // parametrizar
            String user = "sysdba"; // parametrizar
            String password = "masterkey"; // parametrizar

           
        String sqlFacturas = " SELECT  r.KARDEXID, r.CODCOMP, r.CODPREFIJO,r.CODPREFIJO||'-'||r.NUMERO as numeroFactura,"
                + "r.NUMERO, r.FECHA, r.FECASENTAD,r.CLIENTE,t.NIT,t.NITTRI, t.NOMBRE as nombreCliente,"
                + "t.DIRECC1, t.EMAIL,r.ZONATERCERO as barrio,SUBSTRING(t.TELEF1 FROM 1 FOR 15) as telefonoCliente,t.ESTABLECIMIENTO, c.NOMBRE as ciudad,"
                + "r.VENDEDOR,t2.NOMBRE as nombrevendedor, r.CODVEN, r.NOMVENDEDOR,"
                + "r.VRBASE, r.VRIVA, r.TOTAL, r.DOCUID, r.FPCONTADO, r.FPCREDITO,"
                + "r.DESPACHAR_A, r.USUARIO, r.HORA, r.FACTORCONV, r.NROFACPROV, r.VEHICULOID,"
                + "r.FECANULADO, r.DESXCAMBIO, r.DEVOLXCAMBIO, r.TIPOICA2ID, r.MONEDA "
                + "FROM KARDEX r "
                + "join TERCEROS t on  r.cliente=t.terid "
                + "join TERCEROS t2 on r.vendedor=t2.terid "
                + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                + "where r.CODPREFIJO>='" + prefijo + "' and r.NUMERO<='" + factura + "' AND r.CODCOMP='FV'"
                + "ORDER BY .NUMERO";
        ;
        Class.forName("org.firebirdsql.jdbc.FBDriver"); // parametrizar
        //con = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password); // parametrizar
        con = DriverManager.getConnection("jdbc:firebirdsql://50.0.0.99:3050/" + db, user, password); // parametrizar
        System.out.println("Conectado a la base de datos [ " + db + "]");
        statement = con.createStatement();
        rst = statement.executeQuery(sqlFacturas);
        listaDeSentenciasSql = new ArrayList();
       System.out.println("Inicia el prceso de trasmision " + new Date());
        while (rst.next()) {
            kardexid=rst.getString("KARDEXID");
            //* ************************************************************
            String sqlClientes = "insert into clientescamdun(codigoInterno,nitCliente,nombreEstablecimiento,"
                    + "nombreDeCliente,direccion,barrio,ciudad,clasificacion,"
                    + "celularCliente,emailCliente,fechaDeIngresoCliente,"
                    + "latitud,longitud,canalDeVenta,ruta,frecuencia,"
                    + "zona,regional,agencia,porcentajeDescuento,"
                    + "activo,fechaIng,usuario,flag) VALUES('"
                    + rst.getString("NIT") + "','" //codigoInterno +
                    + rst.getString("NITTRI") + "','" // nitCliente +
                    + rst.getString("ESTABLECIMIENTO") + "','" // nombreEstablecimiento
                    + rst.getString("nombreCliente") + "','" // nombreDeCliente
                    + rst.getString("DIRECC1") + "','" // direccion
                    //+ rst.getString("ZONATERCERO") + "','" // barrio (vacio) ZONATERCERO
                    + "','" // barrio (vacio) ZONATERCERO
                    + rst.getString("ciudad") + "','" // ciudad
                    + "1','" // clasificacion
                    + rst.getString("TELEF1") + "','" // celularCliente
                    + rst.getString("EMAIL") + "'," // emailCliente
                    + "current_date(),'" // fechaDeIngresoCliente +
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
                    + "nombreEstablecimiento=,"
                    + "nombreDeCliente='" + rst.getString("nombreCliente") + "',"
                    + "direccion='" + rst.getString("DIRECC1") + "',"
                    //+ "barrio='" + rst.getString("ZONATERCERO") +  "',"
                    + "ciudad='" + rst.getString("ciudad") + "',"
                    + " flag='-1';";
            
            
            listaDeSentenciasSql.add(sqlClientes);
            
            String fecha=rst.getString("FECHA");
            String[] arrSplit = fecha.split(","); 
            fecha=arrSplit[0];
            fecha=fecha.replace(".", "-");
            fecha= "" + Inicio.getFechaSql(fecha);
            
            //*****************************************************************
            if (!rst.getString("NUMERO").contains("*")) {
                
                sqlFacturas = "INSERT INTO facturascamdun(numeroFactura,fechaDeVenta,"
                        + "cliente,direccion,barrio,ciudad,telefono,vendedor,formaDePago,canal,valorFacturaSinIva,"
                        + "valorIvaFactura,valorTotalFactura,valorRechazo,valorDescuento,valorTotalRecaudado,"
                        + "formato,zona,regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,"
                        + "flag,pesofactura,ruta,trasmitido,numeroDescuento,numeroRecogida) "
                        + "VALUES('"
                        + rst.getString("numeroFactura") + "','" //numeroFactura
                        + fecha + "','"//fechaDeVenta
                        + rst.getString("NIT") + "','"//cliente
                        + rst.getString("DIRECC1") + "','"//direccion
                        + "','"//barrio Vacio
                        + rst.getString("ciudad") + "','"//ciudad
                        + rst.getString("TELEF1") + "','"//telefono
                        + rst.getString("NOMVENDEDOR") + "','"//vendedor
                        + "0','"//formaDePago
                        + "1','"//canal
                        + rst.getString("VRBASE") + "','" //valorFacturaSinIva
                        + rst.getString("VRIVA") + "','"//valorIvaFactura
                        + rst.getString("TOTAL") + "','"//valorTotalFactura
                        + "0','"//valorRechazo
                        + "0','"//valorDescuento
                        + "0','" //valorTotalRecaudado
                        + "png','"//formato
                        + "1','"//zona
                        + "1','"//regional
                        + "1','"//agencia
                        + "1','"//isFree
                        + "1','"//estadoFactura
                        + "1',"//
                        + "CURRENT_TIMESTAMP(),'"//activo
                        + "AUTOMATICO','"//usuario
                        + "1','"//flag
                        + "0','"//pesofactura
                        + "1','"//ruta
                        + "0','"//trasmitido
                        + "0','"//numeroDescuento
                        + "0') ON DUPLICATE KEY UPDATE flag=-1;"; //
                listaDeSentenciasSql.add(sqlFacturas);
            }
            
            
            
        }
       
        statement.close();
        con.close();
        rst.close();
        
    }
    
    public void organizarProductosydetalleFactura(String listaDeNumerosDeFacturas) throws SQLException {
        Connection con;
        Statement statement;
        ResultSet rst;
        
           
            //String db = "C:/DATOS TNS/HIELERA2021.gdb?charSet=utf8"; // parametrizar
            //String db = "C:\\DATOS TNS\\HIELERA2021.GDB?charSet=utf8"; // parametrizar
            String db = "F:/datos tns/camdun2021.gdb?charSet=utf8"; // parametrizar
            String user = "sysdba"; // parametrizar
            String password = "masterkey"; // parametrizar
        /* Inicia el proceso de los productos y el detalle de las facturas **********************************************************/
       String sqlDetalleFacturas = "SELECT r.DEKARDEXID, r.KARDEXID, r.MATID,k.CODPREFIJO  ||'-'|| k.NUMERO as factura,"
                + "m.CODIGO as codigoProducto,"
                + "COALESCE(m.CODBARRA,'') as barcode,"
                + "m.DESCRIP as descripcionProducto,lm.DESCRIP as linea,"
                + "COALESCE(m.PESO,'0') as pesoProducto,"
                + "r.PRECIOLISTA as valorUnitarioConIva, r.PRECIOBASE as valorUnitario,"
                + "r.CANLISTA, r.CANMAT as cantidad, (r.PRECIOBASE * r.CANMAT) as  valorTotal,r.PRECIOVTA , "
                + "r.PRECIOIVA as valorIva,r.PARCVTA as valorTotalConIva "
                + "FROM DEKARDEX r "
                + "join MATERIAL m on m.MATID=r.MATID "
                + "join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID "
                + "join KARDEX k on k.KARDEXID=r.KARDEXID "
                + "where r.KARDEXID='" + this.kardexid + "';";
//        con = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
        con = DriverManager.getConnection("jdbc:firebirdsql://50.0.0.99:3050/" + db, user, password);
        statement = con.createStatement();
        rst = statement.executeQuery(sqlDetalleFacturas);
        while (rst.next()) {
            //listaDeProductos(); ******************************************
             String sqlProductos = "INSERT INTO productoscamdun "
                    + "(codigoProducto,descripcionProducto,linea,valorUnitario,valorUnitarioConIva, "
                    + "isFree,pesoProducto,largoProducto,anchoProducto,altoProducto,activo,fechaIng, "
                    + "usuario,flag,barcode) "
                    + "VALUES ('"
                    + rst.getString("codigoProducto") + "','" // codigoProducto
                    + rst.getString("descripcionProducto") + "','" // descripcionProducto
                    + rst.getString("linea") + "','" // linea
                    + rst.getString("valorUnitario") + "','" // valorUnitario
                    + rst.getString("valorUnitarioConIva") + "','" // valorUnitarioConIva
                    + "1','" // isFree
                    + rst.getString("pesoProducto") + "','" // pesoProducto
                    + "0','" // largoProducto
                    + "0','" // anchoProducto
                    + "0','" // altoProducto
                    + "1'," // activo
                    + "CURRENT_TIMESTAMP(),'" // fechaIng
                    + "AUTOMATICO','" // usuario
                    + "1','" // flag
                    + rst.getString("barcode") + "') ON DUPLICATE KEY UPDATE flag='-1';"; // barcode
            listaDeSentenciasSql.add(sqlProductos);
            
            //detalleDeLasFacturas(); **************************************
             sqlDetalleFacturas = "INSERT INTO productosporfactura"
                    + "(factura,codigoProducto,cantidad,valorUnitario,valorTotal,valorUnitarioConIva,"
                    + "valorTotalConIva,pesoProducto,activo,fechaIng,usuario,flag) VALUES('"
                    + rst.getString("factura") + "','" // factura
                    + rst.getString("codigoProducto") + "','" // codigoProducto
                    + rst.getString("cantidad") + "','" // cantidad
                    + rst.getString("valorUnitario") + "','" // valorUnitario
                    + rst.getString("valorTotal") + "','" // valorTotal
                    + rst.getString("valorUnitarioConIva") + "','" // valorUnitarioConIva
                    + rst.getString("valorTotalConIva") + "','" // valorTotalConIva
                    + rst.getString("pesoProducto") + "','" // pesoProducto
                    + "1'," // activo
                    + "CURRENT_TIMESTAMP(),'" // fechaIng
                    + "AUTOMATICO','" // usuario
                    + "1') ON DUPLICATE KEY UPDATE flag='-1';";
            listaDeSentenciasSql.add(sqlDetalleFacturas);
        }
        statement.close();
        con.close();
        rst.close();
    }
   

}
