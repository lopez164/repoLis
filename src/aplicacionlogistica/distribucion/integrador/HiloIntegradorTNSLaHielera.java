/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEmpleados;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloIntegradorTNSLaHielera implements Runnable {

    private JProgressBar jProgressBar;
    private int value;//retardo en milisegundos
    String mensaje = "";
    Inicio ini;

    FIntegrador fIntegrador;
    String listaDeNumerosDeFacturas;

    List<String> listaDeSentenciasClientes = null;
    List<String> listaDeSentenciasFacturas = null;
    List<String> listaDeSentenciasProductosCamdum = null;
    List<String> listaDeSentenciasDetalleFactura = null;

    int cantidadClientes = 0;
    int cantidadProductos = 0;
    int cantidadFacturas = 0;
    int cantidadItemsFactura = 0;
    int cantidadRegistros = 0;

    int kardexid;
    int kardexidAnterior;
    int kardexidNuevo;

    int intervalo = 200;

    Date fecha1;
    String prefijo;
    String numero;
    int opcionHilo = 0;
    JMenuItem menu = null;
    List<String> listaDeSentenciasPersonas = null;
    List<String> listaDeSentenciasEmpleados = null;
    List<String> listaDeSentenciasPorFactura = null;

    /**
     * Constructor de clase
     *
     * @param fIntegrador
     */
    public HiloIntegradorTNSLaHielera(FIntegrador fIntegrador) {
        this.fIntegrador = fIntegrador;
        this.ini = fIntegrador.ini;
        opcionHilo = 1;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIntegradorTNSLaHielera(Inicio ini) {
        this.ini = ini;
       
    }
    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIntegradorTNSLaHielera(Inicio ini, int oca) {
        this.ini = ini;
        opcionHilo = oca;
        run();

    }
   

    /**
     * Constructor de clase permite realizar la integracion de bbdd por medio
     * del numero de la factura;
     *
     * @param ini el objeto de configuracion
     * @param prefijo es el prefijo de la factura
     */
    public HiloIntegradorTNSLaHielera(Inicio ini, String prefijo, String numero) {
        this.prefijo = prefijo;
        this.numero = numero;
        this.ini = ini;
        opcionHilo = 2;
        

    }

    /**
     * Constructor de clase permite realizar la integracion de bbdd por medio
     * del numero de la factura;
     *
     * @param ini el objeto de configuracion
     */
    public HiloIntegradorTNSLaHielera(Inicio ini, JMenuItem menu) {
        this.menu = menu;
        this.ini = ini;
        opcionHilo = 3;

    }

    @Override
    public void run() {

        if (this.fIntegrador != null) {
            // conFormulario();
        } else {
           // sinFormulario();
        }

        switch (opcionHilo) {
            case 0:
                 sinFormulario();
                break;
            case 1:
                if (trasmisionCompleta()) {
                    return;
                }
                break;
            case 2:
                trasmisionPorFactura();

                break;
            case 3:
                salvarConductor();

                break;
            case 4:
                   sinFormulario();
            default:
        }

    }

    private void sinFormulario() throws NumberFormatException {
        //String listaDeNumerosDeFacturas = null;

        String fecha = ("" + ini.getFechaSql());

        kardexidNuevo = 1;
        kardexidAnterior = 1;
        kardexid = 1;

        mensaje += "Inicia en : " + kardexid + "\n";
        // fIntegrador.txtLog.setText(mensaje);
        System.out.println(mensaje);

        // setCantidadClientes();
        //setCantidadProducto();
        // setCantidadFacturas();
        //setCantidadItemsFactura();
        // fIntegrador.barra2.setValue(0);
        // fIntegrador.lblBarra2.setText("0.0 %");
        cantidadRegistros = cantidadClientes + cantidadFacturas + cantidadItemsFactura + cantidadProductos;
        // fIntegrador.jBtnCirculo.setVisible(true);

        Date fecha1 = new Date();
        mensaje = "Inico de proceso de trasferencia : " + fecha1 + "\n";
        // fIntegrador.txtLog.setText(mensaje);
        mensaje = "Cantidad de Registros = " + cantidadRegistros + "\n";
        // fIntegrador.txtLog.setText(mensaje);
        System.out.println(mensaje);

        mensaje += "Ingresando los clientes \n";
        // fInterador.txtLog.setText(mensaje);
        System.out.println(mensaje);

        traerClientes(fecha);
        mensaje = "Ingresando los productos \n";
        System.out.println(mensaje);

        traerProductos(fecha);

        mensaje = "Ingresando las facturas \n";
        System.out.println(mensaje);

        traerFacturas(fecha);
        mensaje = "Ingresando el detalle de las facturas \n";
        System.out.println(mensaje);

        traerDetalleFacturas(fecha);

        Date fecha2 = new Date();
        int segundos = (int) ((fecha2.getTime() - fecha1.getTime()) / 1000);
        mensaje = "Fin del proceso de trasmision de datos : " + new Date() + "\n";
        System.out.println(mensaje);

    }

    private void conFormulario() throws NumberFormatException {
        try {
            //String listaDeNumerosDeFacturas = null;

            String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));

            if (!ini.getPropiedades().getProperty("idOperador").equals("1")) {

                FileReader f = new FileReader(new File(ini.getRutaDeApp() + "kardexid.txt"));
                BufferedReader b = new BufferedReader(f);
                String h = b.readLine();

                if (h.equals(null) || fIntegrador.chkTrasmisionCompleta.isSelected()) {
                    kardexidNuevo = 1;
                    kardexidAnterior = 1;
                    kardexid = 1;

                } else {
                    kardexidNuevo = 1;
                    kardexidAnterior = Integer.parseInt(h);
                    kardexid = kardexidAnterior;

                }
                b.close();
                f.close();
            }

            mensaje += "Inicia en : " + kardexid + "\n";
            fIntegrador.txtLog.setText(mensaje);

            if (fIntegrador.chkClientes.isSelected()) {
                setCantidadClientes();
            }
            if (fIntegrador.chkProductos.isSelected()) {
                setCantidadProducto();
            }
            if (fIntegrador.chkFacturas.isSelected()) {
                setCantidadFacturas();
            }
            if (fIntegrador.chkDetalleFacturas.isSelected()) {
                setCantidadItemsFactura();
            }
            fIntegrador.barra2.setValue(0);
            fIntegrador.lblBarra2.setText("0.0 %");
            cantidadRegistros = cantidadClientes + cantidadFacturas + cantidadItemsFactura + cantidadProductos;
            fIntegrador.jBtnCirculo.setVisible(true);

            Date fecha1 = new Date();
            mensaje += "Inico de proceso de trasferencia : " + fecha1 + "\n";
            fIntegrador.txtLog.setText(mensaje);
            mensaje += "Cantidad de Registros = " + cantidadRegistros + "\n";
            fIntegrador.txtLog.setText(mensaje);

            if (fIntegrador.chkClientes.isSelected()) {
                mensaje += "Ingresando los clientes \n";
                fIntegrador.lblBarra1.setText("Ingresando los clientes");
                fIntegrador.txtLog.setText(mensaje);

                traerClientes();
                fIntegrador.chkClientes.setSelected(false);
            }
            if (fIntegrador.chkProductos.isSelected()) {
                mensaje += "Ingresando los productos \n";
                fIntegrador.lblBarra1.setText("Ingresando los productos");
                fIntegrador.txtLog.setText(mensaje);

                traerProductos();
                fIntegrador.chkProductos.setSelected(false);
            }
            if (fIntegrador.chkFacturas.isSelected()) {
                mensaje += "Ingresando las facturas \n";
                fIntegrador.lblBarra1.setText("Ingresando las facturas");
                fIntegrador.txtLog.setText(mensaje);

                traerFacturas();
                fIntegrador.chkFacturas.setSelected(false);
            }
            if (fIntegrador.chkDetalleFacturas.isSelected()) {
                mensaje += "Ingresando el detalle de las facturas \n";
                fIntegrador.lblBarra1.setText("Ingresando el detalle de las facturas");
                fIntegrador.txtLog.setText(mensaje);

                traerDetalleFacturas();
                fIntegrador.chkDetalleFacturas.setSelected(false);
                fIntegrador.barra2.setValue(100);
                fIntegrador.lblBarra2.setText("Proceso de sincronizacio terminado");
            }
            if (fIntegrador.chkPesoFacturas.isSelected()) {
                actualizarPesoFacturas();
                mensaje += "Actualizado el peso de kg de las facturas \n";;
                fIntegrador.txtLog.setText(mensaje);
                fIntegrador.chkPesoFacturas.setSelected(false);
            }
            if (fIntegrador.chkCanaFacturas.isSelected()) {
                actualizarCanaleVentaClientes(true);
                mensaje += "Actualizado el canal de venta de las facturas \n";;
                fIntegrador.txtLog.setText(mensaje);
                fIntegrador.chkCanaFacturas.setSelected(false);
            }
            Date fecha2 = new Date();
            int segundos = (int) ((fecha2.getTime() - fecha1.getTime()) / 1000);
            mensaje += "Fin del proceso de trasmision de datos : " + new Date() + "\n";
            ;
            fIntegrador.txtLog.setText(mensaje);
            if (segundos < 60) {
                mensaje += "total tiempo trascurrido en segundos =  " + segundos + "\n\n\n";
            }
            if (segundos >= 60 && segundos < 3600) {
                segundos = segundos / 60;
                mensaje += "total tiempo trascurrido en minutos  = " + segundos + "\n\n\n";;
            }   // mensaje += "total tiempo trascurrido en Minutos = " + segundos + "\n";;
            fIntegrador.txtLog.setText(mensaje);
            fIntegrador.band = true;
            fIntegrador.jBtnCirculo.setVisible(false);

            if (fIntegrador.cancelar) {
                fIntegrador.barra1.setValue(0);
                fIntegrador.barra2.setValue(0);
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                JOptionPane.showInternalMessageDialog(fIntegrador, "El proceso de sincronizacon  ha sido cancelado", "Fin de la Trasmision", JOptionPane.INFORMATION_MESSAGE);

            } else {
                fIntegrador.barra1.setValue(100);
                fIntegrador.lblBarra1.setText("100 %");
                fIntegrador.barra2.setValue(100);
                fIntegrador.lblBarra2.setText("Proceso de sincronizacio terminado");
                JOptionPane.showInternalMessageDialog(fIntegrador, "El proceso de sincronizacon  ha finalizado con exito", "Fin de la Trasmision", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (FileNotFoundException ex) {
            this.fIntegrador.txtLog.setText("Error en HiloIntegradorTNSLaHielera \n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.fIntegrador.txtLog.setText("Error en HiloIntegradorTNSLaHielera \n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String organizarClientesYfacturas() throws ClassNotFoundException, SQLException {

        Connection con;
        Statement statement;
        ResultSet rst;

        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;

        //String db = "C:/DATOS TNS/HIELERA2021.gdb?charSet=utf8"; // parametrizar
        String db = "F:/datos tns/2021.gdb?charSet=utf8"; // parametrizar
        String user = "sysdba"; // parametrizar
        String password = "masterkey"; // parametrizar

        String sqlFacturas = " SELECT  r.KARDEXID, r.CODCOMP, r.CODPREFIJO,r.CODPREFIJO||'-'||r.NUMERO as numeroFactura,"
                + "r.NUMERO, r.FECHA, r.FECASENTAD,r.CLIENTE,t.NIT,t.NITTRI, t.NOMBRE as nombreCliente,"
                + "t.DIRECC1, t.EMAIL,r.ZONATERCERO as barrio,SUBSTRING(t.TELEF1 FROM 1 FOR 12) as telefonoCliente,t.ESTABLECIMIENTO, c.NOMBRE as ciudad,"
                + "r.VENDEDOR,t2.NOMBRE as nombrevendedor, r.CODVEN, r.NOMVENDEDOR,"
                + "r.VRBASE, r.VRIVA, r.TOTAL, r.DOCUID, r.FPCONTADO, r.FPCREDITO,"
                + "r.DESPACHAR_A, r.USUARIO, r.HORA, r.FACTORCONV, r.NROFACPROV, r.VEHICULOID,"
                + "r.FECANULADO, r.DESXCAMBIO, r.DEVOLXCAMBIO, r.TIPOICA2ID, r.MONEDA "
                + "FROM KARDEX r "
                + "join TERCEROS t on  r.cliente=t.terid "
                + "join TERCEROS t2 on r.vendedor=t2.terid "
                + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                + "where r.FECHA>='" + fecha2 + ", 00:00:00' and r.FECHA<='" + fecha2 + ", 23:59:59' and r.CODPREFIJO in (" + prefijos + ") AND r.CODCOMP='FV'"
                + "ORDER BY R.NUMERO";
        listaDeNumerosDeFacturas = "('";
        Class.forName("org.firebirdsql.jdbc.FBDriver"); // parametrizar
        con = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password); // parametrizar
        //con = DriverManager.getConnection("jdbc:firebirdsql://" + ini.getServidorFuente() + "/" + db, user, password); // parametrizar
        System.out.println("Conectado a la base de datos [ " + db + "]");
        statement = con.createStatement();
        rst = statement.executeQuery(sqlFacturas);
        listaDeSentenciasClientes = new ArrayList();
        listaDeSentenciasFacturas = new ArrayList();
        System.out.println("Inicia el prceso de trasmision " + new Date());
        while (rst.next()) {
            //*****************************************************************
            if (!rst.getString("NUMERO").contains("*")) {

                sqlFacturas = "INSERT INTO facturas(numeroFactura,fechaDeVenta,"
                        + "cliente,direccion,barrio,ciudad,telefono,vendedor,formaDePago,canal,valorFacturaSinIva,"
                        + "valorIvaFactura,valorTotalFactura,valorRechazo,valorDescuento,valorTotalRecaudado,"
                        + "formato,zona,regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,"
                        + "flag,pesofactura,ruta,trasmitido,numeroDescuento,numeroRecogida,fpContado) "
                        + "VALUES('"
                        + rst.getString("numeroFactura") + "','" //numeroFactura
                        + fecha + "','"//fechaDeVenta
                        + rst.getString("NIT") + "','"//cliente
                        + rst.getString("DIRECC1") + "','"//direccion
                        + "','"//barrio Vacio
                        + rst.getString("ciudad") + "','"//ciudad
                        + rst.getString("telefonoCliente") + "','"//telefono
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
                        + "0','"
                        + rst.getString("fpContado") + "') ON DUPLICATE KEY UPDATE trasmitido='1';"; //
                listaDeSentenciasFacturas.add(sqlFacturas);
            }

            //listaDeFacturas();
            listaDeNumerosDeFacturas += rst.getString("KARDEXID") + "','";

        }
        listaDeNumerosDeFacturas = listaDeNumerosDeFacturas.substring(0, listaDeNumerosDeFacturas.length() - 2).concat(")");
        statement.close();
        con.close();
        rst.close();
        return listaDeNumerosDeFacturas;
    }

    public void organizarProductosydetalleFactura(String listaDeNumerosDeFacturas) throws SQLException {
        Connection con;
        Statement statement;
        ResultSet rst;

        //String db = "C:/DATOS TNS/HIELERA2021.gdb?charSet=utf8"; // parametrizar
        String db = "F:/datos tns/2021.gdb?charSet=utf8"; // parametrizar
        String user = "sysdba"; // parametrizar
        String password = "masterkey"; // parametrizar
        /* Inicia el proceso de los productos y el detalle de las facturas **********************************************************/
        String sqlDetalleFacturas = " SELECT DISTINCT r.MATID, m.CODIGO as codigoProducto,COALESCE(m.CODBARRA,'') as barcode,\n"
                + "m.DESCRIP as descripcionProducto,lm.DESCRIP as linea,COALESCE(m.PESO,'0') as pesoProducto,\n"
                + "r.PRECIOVTA as valorUnitarioConIva, r.PRECIOBASE as valorUnitario,\n"
                + "r.CANLISTA, r.CANMAT as cantidad, (r.PRECIOBASE * r.CANMAT) as  valorTotal,r.PRECIOVTA , \n"
                + "r.PRECIOIVA as valorIva,r.PARCVTA as valorTotalConIva \n"
                + "FROM DEKARDEX r \n"
                + "join MATERIAL m on m.MATID=r.MATID \n"
                + "join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID \n"
                + "join KARDEX k on k.KARDEXID=r.KARDEXID AND  k.FECASENTAD >='23.04.2021, 00:00:00' and k.FECASENTAD <='23.04.2021, 23:59:59' and k.CODCOMP='FV'\n";

        con = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
        //con = DriverManager.getConnection("jdbc:firebirdsql://" + ini.getServidorFuente() + "/" + db, user, password);
        statement = con.createStatement();
        rst = statement.executeQuery(sqlDetalleFacturas);
        listaDeSentenciasProductosCamdum = new ArrayList();
        listaDeSentenciasDetalleFactura = new ArrayList();
        while (rst.next()) {
            //listaDeProductos(); ******************************************
            String sqlProductos = "INSERT INTO productos "
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
            listaDeSentenciasProductosCamdum.add(sqlProductos);

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
            listaDeSentenciasDetalleFactura.add(sqlDetalleFacturas);
        }
        statement.close();
        con.close();
        rst.close();
    }

    private Connection fb_connection() {
        Connection con = null;
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;
        String db = "E:/firebird_db/test.fdb";
        String user = "sysdba";
        String password = "masterkey";
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            con = DriverManager.getConnection("jdbc:firebirdsql://localhost/" + db, user, password);
            System.out.println("Conectado a la base de datos [" + db + "]");
        } catch (Exception ex) {
            this.fIntegrador.txtLog.setText("Error en Fb_ connection \n" + ex.getMessage());
            System.out.println(ex);
        }
        return con;
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
        fIntegrador.barra1.setValue(2);
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return ingresado;
            }

            String listaCodigosClientes = "select  DISTINCT t.NIT ,t.NITTRI,"
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
                    + "join KARDEX k on k.CLIENTE=t.TERID and k.FECASENTAD >='" + fecha2 + ", 00:00:00' "
                    + "and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                    + "AND K.numero not like '*%' ";

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaCodigosClientes += ";";
            } else {
                listaCodigosClientes += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal = ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaCodigosClientes);

            int i = 0;
            while (rstTNS.next()) {

                if (fIntegrador.cancelar) {
                    fIntegrador.lblBarra1.setText("Proceso Cancelado");
                    fIntegrador.lblBarra2.setText("Proceso Cancelado");
                    return ingresado;
                }

                String sqlClientes = "insert into clientes(codigoInterno,nitCliente,nombreEstablecimiento,"
                        + "nombreDeCliente,direccion,barrio,ciudad,clasificacion,"
                        + "celularCliente,emailCliente,fechaDeIngresoCliente,"
                        + "latitud,longitud,canalDeVenta,ruta,frecuencia,"
                        + "zona,regional,agencia,porcentajeDescuento,"
                        + "activo,fechaIng,usuario,flag) VALUES('"
                        + rstTNS.getString("NIT") + "','" //codigoInterno +
                        + rstTNS.getString("NITTRI") + "','" // nitCliente +
                        + rstTNS.getString("ESTABLECIMIENTO") + "','" // nombreEstablecimiento
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

                /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                if (i % fIntegrador.intervalo == 0 || rstTNS.isLast()) {

                    statementPIPLocal.executeBatch();
                    statementPIPRemota.executeBatch();

                    int valorBarra = fIntegrador.barra1.getValue() + (int) (fIntegrador.intervalo * 100 / cantidadClientes);
                    fIntegrador.barra1.setValue(valorBarra);
                    //fIntegrador.lblBarra1.setText(valorBarra + " %");

                    i = 0;
                }
                if (rstTNS.isLast()) {

                    int valorBarra2 = (int) ((cantidadClientes * 100) / cantidadRegistros);
                    fIntegrador.barra2.setValue(valorBarra2);
                    fIntegrador.lblBarra2.setText(valorBarra2 + " %");
                    fIntegrador.barra1.setValue(100);
                    fIntegrador.lblBarra1.setText("100 %");
                }

            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
           // conPIPLocal.close();
            //conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                rstTNS.close();
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de clientes \n" + ex;
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());

                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
               // conPIPLocal.close();
                // conPIPRemota.close();

                ingresado = false;

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
                ingresado = false;
            }

        } catch (InterruptedException ex) {
            this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
            ingresado = false;
        }
        return ingresado;
    }

    private Boolean traerClientes(String fecha) {
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
        //fIntegrador.barra1.setValue(2);
        // String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;
        try {

            String listaCodigosClientes = "select  DISTINCT t.NIT ,t.NITTRI,"
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
                    + "join KARDEX k on k.CLIENTE=t.TERID and k.FECASENTAD >='" + fecha2 + ", 00:00:00' "
                    + "and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                    + "AND k.NUMERO not like '*%' ";

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaCodigosClientes += ";";
            } else {
                listaCodigosClientes += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());

            conPIPLocal = ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaCodigosClientes);

            int i = 0;
            while (rstTNS.next()) {

                System.out.print("codigo cliente " + rstTNS.getString("NITTRI") + "\n");

                String sqlClientes = "insert into clientes(codigoInterno,nitCliente,nombreEstablecimiento,"
                        + "nombreDeCliente,direccion,barrio,ciudad,clasificacion,"
                        + "celularCliente,emailCliente,fechaDeIngresoCliente,"
                        + "latitud,longitud,canalDeVenta,ruta,frecuencia,"
                        + "zona,regional,agencia,porcentajeDescuento,"
                        + "activo,fechaIng,usuario,flag) VALUES('"
                        + rstTNS.getString("NIT") + "','" //codigoInterno +
                        + rstTNS.getString("NITTRI") + "','" // nitCliente +
                        + rstTNS.getString("ESTABLECIMIENTO") + "','" // nombreEstablecimiento
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

                /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                if (i % intervalo == 0 || rstTNS.isLast()) {

                    statementPIPLocal.executeBatch();
                    statementPIPRemota.executeBatch();

                    i = 0;
                }

            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
           // conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                System.out.println(mensaje + "\n" + ex.getMessage());
                rstTNS.close();
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de clientes \n" + ex;
                System.out.println(mensaje + "\n" + ex.getMessage());

                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                //conPIPLocal.close();
                //conPIPRemota.close();

                ingresado = false;

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
                ingresado = false;
            }

        } catch (InterruptedException ex) {
            System.out.println(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
            ingresado = false;
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

        String prefijos = ini.getPrefijos().replace("-", "");
        fIntegrador.barra1.setValue(2);
        String fecha = ("" + Inicio.getFechaSql(fIntegrador.jFechaInicial));  //3205360393 Elvis
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return ingresado;
            }

            String listaCodigosClientes = "SELECT DISTINCT r.MATID,"
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
                    + "k.FECASENTAD >='" + fecha2 + ", 00:00:00' and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                    + "AND K.numero not like '*%' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaCodigosClientes += ";";
            } else {
                listaCodigosClientes += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal= ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaCodigosClientes);

            int i = 0;
            while (rstTNS.next()) {

                if (fIntegrador.cancelar) {
                    fIntegrador.lblBarra1.setText("Proceso Cancelado");
                    fIntegrador.lblBarra2.setText("Proceso Cancelado");
                    return ingresado;
                }

                String sqlProductos = "INSERT INTO productos(codigoProducto,descripcionProducto,linea,valorUnitario,"
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
                if (i % fIntegrador.intervalo == 0 || rstTNS.isLast()) {
                    statementPIPLocal.executeBatch();
                    statementPIPRemota.executeBatch();

                    int valorBarra = fIntegrador.barra1.getValue() + (int) (fIntegrador.intervalo * 100 / cantidadProductos);
                    fIntegrador.barra1.setValue(valorBarra);
                    //fIntegrador.lblBarra1.setText(valorBarra + " %");
                    i = 0;
                }
                if (rstTNS.isLast()) {
                    int valorBarra = (int) ((cantidadClientes + cantidadProductos) * 100 / cantidadRegistros);
                    fIntegrador.barra2.setValue(valorBarra);
                    fIntegrador.lblBarra2.setText(valorBarra + " %");
                    fIntegrador.barra1.setValue(100);
                    fIntegrador.lblBarra1.setText("100 %");
                }

                Thread.sleep(2);
            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
            //conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de productos \n " + ex;
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
                ingresado = false;
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
               // conPIPLocal.close();
                // conPIPRemota.close();

                rstTNS.close();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
                ingresado = false;
            }

        } catch (InterruptedException ex) {
            this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
            ingresado = false;
        }
        return ingresado;
    }

    private Boolean traerProductos(String fecha) {
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
        //String fecha = ("" + Inicio.getFechaSql(fIntegrador.jFechaInicial));  //3205360393 Elvis
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;
        try {

            String listaCodigosClientes = "SELECT DISTINCT r.MATID,"
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
                    + "k.FECASENTAD >='" + fecha2 + ", 00:00:00' and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                    + "AND K.numero not like '*%' ";

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaCodigosClientes += ";";
            } else {
                listaCodigosClientes += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
           // conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal= ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaCodigosClientes);

            int i = 0;
            while (rstTNS.next()) {
                System.out.print("codigo producto " + rstTNS.getString("codigoProducto") + "\n");

                String sqlProductos = "INSERT INTO productos(codigoProducto,descripcionProducto,linea,valorUnitario,"
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
                if (i % intervalo == 0 || rstTNS.isLast()) {
                    statementPIPLocal.executeBatch();
                    statementPIPRemota.executeBatch();

                    i = 0;
                }

                Thread.sleep(2);
            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
            // conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de productos \n " + ex;
                System.out.println(mensaje + "\n" + ex.getMessage());
                ingresado = false;
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                //conPIPLocal.close();
                //conPIPRemota.close();

                rstTNS.close();

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
                ingresado = false;
            }

        } catch (InterruptedException ex) {
            System.out.println(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
            ingresado = false;
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
        String fecha = ("" + Inicio.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");

        fIntegrador.barra1.setValue(2);
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return ingresado;
            }

            CFacturas factura = new CFacturas(ini);

            String listaFacturas = CFacturas.getSelectFacturasTNS();
            /*"SELECT r.KARDEXID,r.numero,"
                    + "r.CODPREFIJO||r.NUMERO as numeroFactura,"
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
                    + "r.PLAZODIAS AS plazoDias,"
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
                    + "'1' activo,'"
                    + "CURRENT_TIMESTAMP()' as fechaIng,"
                    + "'AUTOMATICO' AS usuario,"
                    + "'0' AS flag," // tiene que ser cero para las salidas a distribucion
                    + "'0' AS pesofactura,"
                    + "r.CODVEN AS ruta,"
                    + "'0' AS trasmitido,"
                    + "'0' AS numeroDescuento,"
                    + "'0' AS numeroRecogida "
                    + "FROM KARDEX r " **/
            listaFacturas += "join TERCEROS t on  r.cliente=t.terid "
                    + "join TERCEROS t2 on r.vendedor=t2.terid "
                    + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                    + "where r.FECHA>='" + fecha2 + ", 00:00:00' and r.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and r.CODPREFIJO in (" + prefijos + ") AND r.CODCOMP='FV' "
                    + "and r.numero not like '*%' ";

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaFacturas += "ORDER BY r.KARDEXID ;";
            } else {
                listaFacturas += "AND r.KARDEXID>='" + this.kardexid + "' ORDER BY r.KARDEXID ;";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal= ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaFacturas);

            int i = 0;
            while (rstTNS.next()) {
                if (fIntegrador.cancelar) {
                    fIntegrador.lblBarra1.setText("Proceso Cancelado");
                    fIntegrador.lblBarra2.setText("Proceso Cancelado");
                    return ingresado;
                }
                System.out.print(rstTNS.getString("KARDEXID") + " , " + rstTNS.getString("numero") + "\n");

                if (!rstTNS.getString("numero").startsWith("*")) {

//                    String sqlfacturas = "INSERT INTO facturas("
//                            + "numeroFactura,fechaDeVenta,cliente,direccion,barrio,ciudad,telefono,"
//                            + "formaDePago,vendedor,canal,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
//                            + "valorRechazo,valorDescuento,valorTotalRecaudado,formato,zona,"
//                            + "regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,flag,pesofactura,"
//                            + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor,plazoDias) VALUES('"
//                            + rstTNS.getString("numeroFactura") + "','"
//                            + rstTNS.getString("fechaDeVenta") + "','"
//                            + rstTNS.getString("idCliente") + "','"
//                            + rstTNS.getString("direccionDeCliente") + "','"
//                            + rstTNS.getString("barrio") + "','"
//                            + rstTNS.getString("ciudad") + "','"
//                            + rstTNS.getString("telefonoCliente") + "','";
//                    if (rstTNS.getInt("plazoDias") > 1) {
//                        sqlfacturas += "CREDITO','";
//                    } else {
//                        sqlfacturas += "CONTADO','";
//                    }
//                    sqlfacturas += rstTNS.getString("vendedor") + "','"
//                            + rstTNS.getString("canal") + "','"
//                            + rstTNS.getString("valorFacturaSinIva") + "','"
//                            + rstTNS.getString("valorIvaFactura") + "','"
//                            + rstTNS.getString("valorTotalFactura") + "','"
//                            + rstTNS.getString("valorRechazo") + "','"
//                            + rstTNS.getString("valorDescuento") + "','"
//                            + rstTNS.getString("valorTotalRecaudado") + "','"
//                            + rstTNS.getString("formato") + "','"
//                            + rstTNS.getString("zona") + "','"
//                            + rstTNS.getString("regional") + "','"
//                            + rstTNS.getString("agencia") + "','"
//                            + rstTNS.getString("isFree") + "','"
//                            + rstTNS.getString("estadoFactura") + "','"
//                            + rstTNS.getString("activo") + "',"
//                            + "CURRENT_TIMESTAMP(),'"
//                            + rstTNS.getString("usuario") + "','"
//                            + rstTNS.getString("flag") + "','"
//                            + rstTNS.getString("pesofactura") + "','"
//                            + rstTNS.getString("ruta") + "','"
//                            + rstTNS.getString("trasmitido") + "','"
//                            + rstTNS.getString("numeroDescuento") + "','"
//                            + rstTNS.getString("numeroRecogida") + "','"
//                            + rstTNS.getString("telefonoVendedor") + "','"
//                            + rstTNS.getInt("plazoDias") + "') "
//                            + "on duplicate key update flag='-1',"
//                            + "telefonoVendedor='" + rstTNS.getString("telefonoVendedor") + "';";
                    String sqlfacturas = "INSERT INTO facturas("
                            + "numeroFactura,fechaDeVenta,cliente,direccion,barrio,ciudad,telefono,"
                            + "formaDePago,vendedor,canal,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
                            + "valorRechazo,valorDescuento,valorTotalRecaudado,formato,zona,"
                            + "regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,flag,pesofactura,"
                            + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor,plazoDias,prefijo,numero,fpContado,observaciones) VALUES('"
                            + rstTNS.getString("numeroFactura") + "','"
                            + rstTNS.getString("fechaDeVenta") + "','"
                            + rstTNS.getString("nitCliente") + "','";

                    /*Se evalua la direccion de la entrega del pedido */
                    if (rstTNS.getString("observaciones") != null) {
                        if (rstTNS.getString("observaciones").contains("ENTREGAR EN")) {
                            // String cadena[] = rstTNS.getString("observaciones").split(":");
                            // sqlfacturas += cadena[1].trim() + "','";
                            sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";

                        } else {
                            //sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                            sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                        }
                    } else {
                        sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                    }

                    sqlfacturas += rstTNS.getString("barrio") + "','"
                            + rstTNS.getString("ciudad") + "','"
                            + rstTNS.getString("telefonoCliente") + "','";

                    /*Se evalua la forma de pago */
                    if (rstTNS.getInt("plazoDias") > 1) {
                        sqlfacturas += "CREDITO','";
                    } else {
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
                            + rstTNS.getInt("plazoDias") + "','"
                            + rstTNS.getString("prefijo") + "','"
                            + rstTNS.getString("numero") + "','"
                            + rstTNS.getString("fpContado") + "','"
                            + rstTNS.getString("observaciones") + "') on duplicate key update trasmitido='1',"
                            + "telefonoVendedor='" + rstTNS.getString("telefonoVendedor") + "',"
                            + "fpContado='" + rstTNS.getString("fpContado") + "',"
                            + "observaciones='" + rstTNS.getString("observaciones") + "';";

                    i++;
                    statementPIPLocal.addBatch(sqlfacturas);
                    statementPIPRemota.addBatch(sqlfacturas);

                    /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                    if (i % fIntegrador.intervalo == 0 || rstTNS.isLast()) {
                        statementPIPLocal.executeBatch();
                        statementPIPRemota.executeBatch();

                        int valorBarra = fIntegrador.barra1.getValue() + (int) (fIntegrador.intervalo * 100 / cantidadFacturas);
                        fIntegrador.barra1.setValue(valorBarra);
                        //fIntegrador.lblBarra1.setText(valorBarra + " %");

                        i = 0;
                    }

                    if (rstTNS.isLast()) {
                        int valorBarra = (int) ((cantidadClientes + cantidadProductos + cantidadFacturas) * 100 / cantidadRegistros);
                        fIntegrador.barra2.setValue(valorBarra);
                        fIntegrador.lblBarra2.setText(valorBarra + " %");
                        fIntegrador.barra1.setValue(100);
                        fIntegrador.lblBarra1.setText("100 %");
                    }

                    if (!encontrado) {
                        this.kardexidNuevo = Integer.parseInt(rstTNS.getString("KARDEXID")) - 1;

                    }
                    Thread.sleep(2);
                } else {
                    if (!encontrado) {
                        this.kardexidNuevo = Integer.parseInt(rstTNS.getString("KARDEXID")) - 1;
                        encontrado = true;
                    }

                }
            }

            // salvarKardexId();
            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
            //conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {

                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de facturas \n" + ex;
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                //conPIPLocal.close();
                //conPIPRemota.close();

                rstTNS.close();

                kardexid = kardexidAnterior;
                salvarKardexId();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private Boolean traerFacturas(String fecha) {
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
        //String fecha = ("" + Inicio.getFechaSql(fIntegrador.jFechaInicial));
      
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");

        try {

            CFacturas factura = new CFacturas(ini);

            String listaFacturas = CFacturas.getSelectFacturasTNS();
          
            listaFacturas += "join TERCEROS t on  r.cliente=t.terid "
                    + "join TERCEROS t2 on r.vendedor=t2.terid "
                    + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                    + "where "
                    + "r.numero not like '*%' AND "
                    + "r.FECASENTAD>='" + fecha2 + ", 00:00:00' and r.FECASENTAD<='" + fecha2 + ", 23:59:59' "
                    + "and r.CODPREFIJO in (" + prefijos + ") AND r.CODCOMP='FV' "
                    + "AND r.numero not like '*%' ";

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaFacturas += "ORDER BY r.KARDEXID ;";
            } else {
                listaFacturas += "AND r.KARDEXID>='" + this.kardexid + "' ORDER BY r.KARDEXID ;";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal= ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaFacturas);

            int i = 0;
            while (rstTNS.next()) {
                System.out.print(rstTNS.getString("KARDEXID") + " , " + rstTNS.getString("prefijo") + rstTNS.getString("numero") + "\n");

                if (!rstTNS.getString("numero").contains("*")) {

                    String sqlfacturas = "INSERT INTO facturas("
                            + "numeroFactura,fechaDeVenta,cliente,direccion,barrio,ciudad,telefono,"
                            + "formaDePago,vendedor,canal,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
                            + "valorRechazo,valorDescuento,valorTotalRecaudado,formato,zona,"
                            + "regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,flag,pesofactura,"
                            + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor,plazoDias,prefijo,numero,fpContado,observaciones) VALUES('"
                            + rstTNS.getString("numeroFactura") + "','"
                            + rstTNS.getString("fechaDeVenta") + "','"
                            + rstTNS.getString("nitCliente") + "','";

                    /*Se evalua la direccion de la entrega del pedido */
                    if (rstTNS.getString("observaciones") != null) {
                        if (rstTNS.getString("observaciones").contains("ENTREGAR EN")) {
                            // String cadena[] = rstTNS.getString("observaciones").split(":");
                            // sqlfacturas += cadena[1].trim() + "','";
                            sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";

                        } else {
                            sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                        }
                    } else {
                        sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                    }

                    sqlfacturas += rstTNS.getString("barrio") + "','"
                            + rstTNS.getString("ciudad") + "','"
                            + rstTNS.getString("telefonoCliente") + "','";

                    /*Se evalua la forma de pago */
                    if (rstTNS.getInt("plazoDias") > 1) {
                        sqlfacturas += "CREDITO','";
                    } else {
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
                            + rstTNS.getInt("plazoDias") + "','"
                            + rstTNS.getString("prefijo") + "','"
                            + rstTNS.getString("numero") + "','"
                            + rstTNS.getString("fpContado") + "','"
                            + rstTNS.getString("observaciones") + "') on duplicate key update trasmitido='1',"
                            + "telefonoVendedor='" + rstTNS.getString("telefonoVendedor") + "',"
                            + "fpContado='" + rstTNS.getString("fpContado") + "';";

                    i++;
                    statementPIPLocal.addBatch(sqlfacturas);
                    statementPIPRemota.addBatch(sqlfacturas);

                    /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                  if ( rstTNS.isLast() || i % intervalo == 0 ) {
                        statementPIPLocal.executeBatch();
                        statementPIPRemota.executeBatch();
                        i = 0;
                    }

                    if (!encontrado) {
                        this.kardexidNuevo = Integer.parseInt(rstTNS.getString("KARDEXID")) - 1;

                    }
                    Thread.sleep(2);
                } 
                else {
                    if ( rstTNS.isLast() || i % intervalo == 0 ) {
                        statementPIPLocal.executeBatch();
                        statementPIPRemota.executeBatch();
                        i = 0;
                    }

                    if (!encontrado) {
                        this.kardexidNuevo = Integer.parseInt(rstTNS.getString("KARDEXID")) - 1;
                        encontrado = true;
                    }

                } 
            }

            // salvarKardexId();
            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
            //conPIPRemota.close();

            rstTNS.close();

            ingresado = true;

        } catch (SQLException ex) {
            try {

                mensaje = "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion de facturas \n" + ex;
                System.out.println(mensaje + "\n" + ex.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                statementPIPLocal.close();
                statementPIPRemota.close();

                conTNS.close();
                //conPIPLocal.close();
                //conPIPRemota.close();

                rstTNS.close();

                kardexid = kardexidAnterior;
                salvarKardexId();

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            System.out.println(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    public void salvarKardexId() throws IOException {
        FileWriter fichero = new FileWriter(ini.getRutaDeApp() + "kardexid.txt");
        PrintWriter escrive = new PrintWriter(fichero);
        escrive.println("" + kardexidNuevo);
        fichero.close();
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
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;
        String sqlDetalleFacturas;

        String prefijos = ini.getPrefijos().replace("-", "");;
        fIntegrador.barra1.setValue(2);

        try {
            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return ingresado;
            }
            String listaDetalleFactura = "SELECT k.NUMERO,"
                    + "k.CODPREFIJO||k.NUMERO as numeroFactura,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "SUM(d.CANMAT) AS CANTIDAD, "
                    + "d.PRECIOBASE as valorUnitario,"
                    + " (d.PRECIOBASE * SUM(d.CANMAT)) as valorTotal,"
                    + "d.PRECIOVTA as valorUnitarioConIva,"
                    + "(d.PRECIOVTA * SUM(d.CANMAT)) as valorTotalConIva,"
                    + "(COALESCE(m.PESO,0) * SUM(d.CANMAT)) as pesoProducto "
                    + "FROM "
                    + "DEKARDEX  d "
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID AND k.FECASENTAD >='" + fecha2 + ", 00:00:00' "
                    + "and k.FECASENTAD <='" + fecha2 + ", 23:59:59' and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' "
                    + "AND k.KARDEXID>='" + this.kardexid + "' "
                    + "AND K.numero not like '*%' "
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "GROUP BY numeroFactura,codigoProducto,descripcionProducto,d.CANMAT,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by numeroFactura,codigoProducto";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal= ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaDetalleFactura);
            int i = 0;

            while (rstTNS.next()) {
                if (!rstTNS.getString("numero").startsWith("*")) {

                    System.out.print(rstTNS.getString("numeroFactura") + "\n");

                    if (fIntegrador.cancelar) {
                        fIntegrador.lblBarra1.setText("Proceso Cancelado");
                        fIntegrador.lblBarra2.setText("Proceso Cancelado");
                        return ingresado;
                    }

                    sqlDetalleFacturas = "INSERT INTO productosporfactura"
                            + "(factura,codigoProducto,cantidad,valorUnitario,valorTotal,valorUnitarioConIva,"
                            + "valorTotalConIva,pesoProducto,activo,fechaIng,usuario,flag,descripcionProducto) VALUES('"
                            + rstTNS.getString("numeroFactura") + "','"
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

                    System.out.print(sqlDetalleFacturas + "\n");
                    i++;
                    statementPIPLocal.addBatch(sqlDetalleFacturas);
                    statementPIPRemota.addBatch(sqlDetalleFacturas);

                    /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                    if (i % fIntegrador.intervalo == 0 || rstTNS.isLast()) {

                        statementPIPLocal.executeBatch();
                        statementPIPRemota.executeBatch();

                        int valorBarra1 = fIntegrador.barra1.getValue() + (int) (fIntegrador.intervalo * 100 / cantidadItemsFactura);
                        fIntegrador.barra1.setValue(valorBarra1);
                        //fIntegrador.lblBarra1.setText(valorBarra1 + " %");

                        int valorBarra = fIntegrador.barra2.getValue() + (int) (fIntegrador.intervalo * 100 / cantidadRegistros);
                        fIntegrador.barra2.setValue(valorBarra);
                        fIntegrador.lblBarra2.setText(valorBarra + " %");
                        i = 0;
                    }
                    if (rstTNS.isLast()) {
                        //int valorBarra=(int)((cantidadClientes + cantidadProductos + cantidadFacturas + cantidadItemsFactura)*100/cantidadRegistros);
                        fIntegrador.barra2.setValue(100);
                        fIntegrador.lblBarra2.setText("Proceso de sincronizacio terminado");
                        fIntegrador.barra1.setValue(100);
                        fIntegrador.lblBarra1.setText("100 %");
                    }

                    Thread.sleep(2);
                }
            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
            //conPIPRemota.close();

            rstTNS.close();

            ingresado = true;
            this.kardexid = kardexidNuevo;
            salvarKardexId();

        } catch (SQLException ex) {
            try {
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion del detalle de las facturas \n" + ex + "\n "
                        + "";
                this.fIntegrador.txtLog.setText(mensaje);
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                //statementPIPLocal.close();
                //statementPIPRemota.close();

                conTNS.close();
                conPIPLocal.close();
                conPIPRemota.close();

                rstTNS.close();

                kardexid = kardexidAnterior;
                salvarKardexId();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            try {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                kardexid = kardexidAnterior;
                salvarKardexId();
            } catch (IOException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private Boolean traerDetalleFacturas(String fecha) {
        boolean ingresado = false;

        Connection conTNS = null;
        Connection conPIPLocal = null;
        Connection conPIPRemota = null;

        Statement statementTNS = null;
        Statement statementPIPLocal = null;
        Statement statementPIPRemota = null;

        ResultSet rstTNS = null;
        ResultSet rstPIP;
        // String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;
        String sqlDetalleFacturas;

        String prefijos = ini.getPrefijos().replace("-", "");;

        try {
            String listaDetalleFactura = "SELECT k.NUMERO,"
                    + "k.CODPREFIJO||k.NUMERO as numeroFactura,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "SUM(d.CANMAT) AS CANTIDAD, "
                    + "d.PRECIOBASE as valorUnitario,"
                    + " (d.PRECIOBASE * SUM(d.CANMAT)) as valorTotal,"
                    + "d.PRECIOVTA as valorUnitarioConIva,"
                    + "(d.PRECIOVTA * SUM(d.CANMAT)) as valorTotalConIva,"
                    + "(COALESCE(m.PESO,0) * SUM(d.CANMAT)) as pesoProducto "
                    + "FROM "
                    + "DEKARDEX  d "
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID AND k.FECASENTAD >='" + fecha2 + ", 00:00:00' "
                    + "and k.FECASENTAD <='" + fecha2 + ", 23:59:59' and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' "
                    + "AND k.KARDEXID>='" + this.kardexid + "' "
                    +  "AND K.numero not like '*%' "
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "GROUP BY numeroFactura,codigoProducto,descripcionProducto,d.CANMAT,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by numeroFactura,codigoProducto";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            //conPIPLocal = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal(), "local");
            //conPIPRemota = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "remoto");

            conPIPLocal= ini.getConnLocal();
            conPIPRemota = ini.getConnRemota();
            
            conPIPLocal.setAutoCommit(false);
            conPIPRemota.setAutoCommit(false);

            statementTNS = conTNS.createStatement();
            statementPIPLocal = conPIPLocal.createStatement();
            statementPIPRemota = conPIPRemota.createStatement();

            rstTNS = statementTNS.executeQuery(listaDetalleFactura);
            int i = 0;

            while (rstTNS.next()) {
                if (!rstTNS.getString("numero").contains("*")) {

                    System.out.print("Productos de la factura " + rstTNS.getString("numeroFactura") + "\n");

                    sqlDetalleFacturas = "INSERT INTO productosporfactura"
                            + "(factura,codigoProducto,cantidad,valorUnitario,valorTotal,valorUnitarioConIva,"
                            + "valorTotalConIva,pesoProducto,activo,fechaIng,usuario,flag,descripcionProducto) VALUES('"
                            + rstTNS.getString("numeroFactura") + "','"
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

                    // System.out.print(sqlDetalleFacturas + "\n");
                    i++;
                    statementPIPLocal.addBatch(sqlDetalleFacturas);
                    statementPIPRemota.addBatch(sqlDetalleFacturas);

                    /* Graba bloque de 500 registros y luego valida el tamaño del resulSet*/
                    if (i % intervalo == 0 || rstTNS.isLast()) {

                        statementPIPLocal.executeBatch();
                        statementPIPRemota.executeBatch();

                        i = 0;
                    }

                    Thread.sleep(2);
                }else{
                     if (i % intervalo == 0 || rstTNS.isLast()) {

                        statementPIPLocal.executeBatch();
                        statementPIPRemota.executeBatch();

                        i = 0;
                    }
                }
            }

            conPIPLocal.commit();
            conPIPRemota.commit();

            statementTNS.close();
            statementPIPLocal.close();
            statementPIPRemota.close();

            conTNS.close();
            //conPIPLocal.close();
            //conPIPRemota.close();

            rstTNS.close();

            ingresado = true;
            this.kardexid = kardexidNuevo;
            salvarKardexId();

        } catch (SQLException ex) {
            try {
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la insercion del detalle de las facturas \n" + ex + "\n "
                        + "";
                System.out.println(mensaje);
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);

                conPIPLocal.rollback();
                conPIPRemota.rollback();

                statementTNS.close();
                //statementPIPLocal.close();
                //statementPIPRemota.close();

                conTNS.close();
                //conPIPLocal.close();
                //conPIPRemota.close();

                rstTNS.close();

                kardexid = kardexidAnterior;
                salvarKardexId();

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        } catch (InterruptedException ex) {
            try {
                System.out.println(mensaje + "\n" + ex.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                kardexid = kardexidAnterior;
                salvarKardexId();
            } catch (IOException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            System.out.println(mensaje + "\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresado;
    }

    private void setCantidadClientes() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");
        try {
            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return;
            }

            String listaFacturas = "select  COUNT(DISTINCT t.NIT) as cantidad "
                    + "from terceros t "
                    + "join KARDEX k on k.CLIENTE=t.TERID and k.FECASENTAD >='" + fecha2 + ", 00:00:00' "
                    + "and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaFacturas += ";";
            } else {
                listaFacturas += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaFacturas);

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
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en cantidad de clientes";
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());

                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText("el errror es aca" + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);

            }
        }
    }

    private void setCantidadClientes(String fecha) {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        // String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");
        try {

            String listaFacturas = "select  COUNT(DISTINCT t.NIT) as cantidad "
                    + "from terceros t "
                    + "join KARDEX k on k.CLIENTE=t.TERID and k.FECASENTAD >='" + fecha2 + ", 00:00:00' "
                    + "and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaFacturas += ";";
            } else {
                listaFacturas += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaFacturas);

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
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en cantidad de clientes";
                System.out.println(mensaje + "\n" + ex.getMessage());

                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);

            }
        }
    }

    private void setCantidadProducto() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return;
            }

            String listaProductos = "SELECT count(DISTINCT r.MATID) as cantidad "
                    + "FROM DEKARDEX r "
                    + "join MATERIAL m on m.MATID=r.MATID "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID AND "
                    + "k.FECASENTAD >='" + fecha2 + ", 00:00:00' and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                    + "and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaProductos += ";";
            } else {
                listaProductos += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            // conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaProductos);

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
                rstTNS.close();
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de productos";
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadProducto(String fecha) {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        //  String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;
        try {

            String listaProductos = "SELECT count(DISTINCT r.MATID) as cantidad "
                    + "FROM DEKARDEX r "
                    + "join MATERIAL m on m.MATID=r.MATID "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID AND "
                    + "k.FECASENTAD >='" + fecha2 + ", 00:00:00' and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' "
                    + "and k.CODPREFIJO in(" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaProductos += ";";
            } else {
                listaProductos += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            // conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaProductos);

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
                rstTNS.close();
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de productos";
                System.out.println(mensaje + "\n" + ex.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadFacturas() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return;
            }

            String listaFacturas = "SELECT count(DISTINCT k.KARDEXID) as cantidad "
                    + "FROM KARDEX k "
                    + "where K.FECHA>='" + fecha2 + ", 00:00:00' and K.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaFacturas += ";";
            } else {
                listaFacturas += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaFacturas);

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
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de facturas";
                this.fIntegrador.txtLog.setText(mensaje);
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadFacturas(String fecha) {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        //String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;
        try {

            String listaFacturas = "SELECT count(DISTINCT k.KARDEXID) as cantidad "
                    + "FROM KARDEX k "
                    + "where K.FECHA>='" + fecha2 + ", 00:00:00' and K.FECHA<='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaFacturas += ";";
            } else {
                listaFacturas += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaFacturas);

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
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de facturas";
                System.out.println(mensaje);
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadItemsFactura() {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return;
            }

            String listaItemsFacturas = "SELECT count(DISTINCT r.DEKARDEXID) as cantidad "
                    + "FROM DEKARDEX r "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID "
                    + "where k.FECASENTAD >='" + fecha2 + ", 00:00:00' and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaItemsFacturas += ";";
            } else {
                listaItemsFacturas += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            // conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaItemsFacturas);

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
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de items de la factura ";
                this.fIntegrador.txtLog.setText(mensaje);
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                this.fIntegrador.txtLog.setText(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setCantidadItemsFactura(String fecha) {
        boolean ingresado = false;

        Connection conTNS = null;

        Statement statementTNS = null;

        ResultSet rstTNS = null;
        // String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        String[] arrSplit = fecha.split("-");
        String anio = arrSplit[0];
        String mes = arrSplit[1];
        String dia = arrSplit[2];
        String fecha2 = dia + "." + mes + "." + anio;

        String prefijos = ini.getPrefijos().replace("-", "");;
        try {

            String listaItemsFacturas = "SELECT count(DISTINCT r.DEKARDEXID) as cantidad "
                    + "FROM DEKARDEX r "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID "
                    + "where k.FECASENTAD >='" + fecha2 + ", 00:00:00' and k.FECASENTAD <='" + fecha2 + ", 23:59:59' "
                    + "and k.CODPREFIJO in (" + prefijos + ") AND k.CODCOMP='FV' ";
            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                listaItemsFacturas += ";";
            } else {
                listaItemsFacturas += "AND k.KARDEXID>='" + this.kardexid + "';";
            }

            // conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(listaItemsFacturas);

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
                mensaje += "\n\n\nSe ha presentado un error en la sinronizacion de la informacion \n"
                        + "en la cantidad de items de la factura ";
                System.out.println(mensaje);
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                System.out.println(mensaje + "\n" + ex1.getMessage());
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public boolean actualizarPesoFacturas() {
        boolean actualizado = false;
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return false;
            }
            String sql = "update facturas fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            this.fIntegrador.txtLog.setText("Error en actualizar el peso de las facturas\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
            actualizado = false;
        }
        return actualizado;
    }

    public boolean actualizarCanaleVentaClientes(boolean a) {
        boolean actualizado = false;
        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        try {

            if (fIntegrador.cancelar) {
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return false;
            }

            String sql = "update facturas f "
                    + "join clasificacionRutas c "
                    + "on f.ruta=c.idclasificacionRutas "
                    + "set f.canal=c.tipoDeCanalDeVenta "
                    + "where f.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            this.fIntegrador.txtLog.setText("Error en actualizar Canal de Ventas del Cliente\n" + ex.getMessage());
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
            actualizado = false;
        }
        return actualizado;
    }

    private void salvarConductor() {
        String msg = "";
        if (this.menu != null) {
            this.menu.setEnabled(false);
            this.menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));

        }

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;
        listaDeSentenciasPersonas = new ArrayList<>();
        listaDeSentenciasEmpleados = new ArrayList<>();

        try {

            String sqlCodigoEmpledos = "SELECT "
                    + "t.NITTRI as cedula,"
                    + "t.NOMBRE1 as NOMBRE1,"
                    + " t.NOMBRE2 as NOMBRE2,"
                    + "t.APELLIDO1 as APELLIDO1,"
                    + "t.APELLIDO2 as APELLIDO2,"
                    + "t.DIRECC1 as direccion,"
                    + "Z.NOMBRE as barrio,"
                    + "'780' as ciudad,"
                    + "t.TELEF1 as telefonoFijo,"
                    + "t.TELEF1 as telefonoCelular,"
                    + "'BACHILLER' as escolaridad,"
                    + "'1' as genero,"
                    + "'2022-01-01' as cumpleanios,"
                    + "'CUCUTA' as lugarNacimiento,"
                    + "'1' as estadoCivil,"
                    + "t.EMAIL as email,"
                    + "'1' as tipoSangre,"
                    + " '1' as activo,"
                    + "'CURRENT_TIMESTAMP' as fechaIng,"
                    + " 'AUTOMATICO' as usuario,"
                    + " '1' as flag,"
                    + "'3' as cargo,"
                    + "'0' as celularCorporativo,"
                    + "'CURRENT_TIMESTAMP' as fechaDeIngreso,"
                    + "'1' as idAgencia,"
                    + "'1' as idRegional,"
                    + "'1' as idZona,"
                    + "'1' as idCentroDeCosto,"
                    + "NULL as fotografia,"
                    + "NULL as formatoFotografia,"
                    + "'1' as idTipoDeContrato,"
                    + "'0' as numeroCuenta,"
                    + "'1' as isFree, "
                    + "'1' as entidadBancaria "
                    + "FROM terceros t "
                    + "join ZONAS Z ON Z.ZONAID = t.ZONA1 "
                    + "WHERE "
                    + "t.CLASIFICAID='4' and t.NIT like 'D%';";

            msg = " Error de Conexion TNS";
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlCodigoEmpledos);

            while (rstTNS.next()) {

                String nombres = "";
                String apellidos = "";
                String sqlPersonas = "INSERT INTO personas "
                        + " (cedula,nombres,apellidos,direccion,barrio,ciudad,telefonoFijo,telefonoCelular,"
                        + " escolaridad,genero,cumpleanios,lugarNacimiento,estadoCivil,email,tipoSangre,"
                        + " activo,fechaIng,usuario,flag)"
                        + " VALUES ('"
                        + rstTNS.getString("cedula") + "','";
                if (rstTNS.getString("NOMBRE1") != null) {
                    nombres += rstTNS.getString("NOMBRE1") + ' ';
                }
                if (rstTNS.getString("NOMBRE2") != null) {
                    nombres += rstTNS.getString("NOMBRE2") + ' ';
                }
                if (rstTNS.getString("APELLIDO1") != null) {
                    apellidos += rstTNS.getString("APELLIDO1") + ' ';
                }
                if (rstTNS.getString("APELLIDO2") != null) {
                    apellidos += rstTNS.getString("APELLIDO2");
                }
                //+ rstTNS.getString("NOMBRE1").replace("null", "") + ' ' + rstTNS.getString("NOMBRE2").replace("null", "") + "','"
                //+ (rstTNS.getString("APELLIDO1").replace("null", "") + ' ' + rstTNS.getString("APELLIDO2")).replace("null", "") + "','"
                sqlPersonas += nombres + "','"
                        + apellidos + "','"
                        + rstTNS.getString("direccion") + "','"
                        + rstTNS.getString("barrio") + "','"
                        + rstTNS.getString("ciudad") + "','";

                if (rstTNS.getString("telefonoFijo") != null) {
                    int val = rstTNS.getString("telefonoFijo").length();
                    if (val >= 9) {
                        val = 9;
                    } else {
                        val = rstTNS.getString("telefonoFijo").length();
                    }
                    sqlPersonas += rstTNS.getString("telefonoFijo").substring(0, val) + "','";
                } else {
                    sqlPersonas += "00','";
                }
                if (rstTNS.getString("telefonoCelular") != null) {
                    int val = rstTNS.getString("telefonoCelular").length();
                    if (val >= 9) {
                        val = 9;
                    } else {
                        val = rstTNS.getString("telefonoFijo").length();
                    }
                    sqlPersonas += rstTNS.getString("telefonoCelular").substring(0, val) + "','";
                } else {
                    sqlPersonas += "00','";
                }

                sqlPersonas += rstTNS.getString("escolaridad") + "','"
                        + rstTNS.getString("genero") + "','"
                        + rstTNS.getString("cumpleanios") + "','"
                        + rstTNS.getString("lugarNacimiento") + "','"
                        + rstTNS.getString("estadoCivil") + "','";
                if (rstTNS.getString("email") != null) {
                    sqlPersonas += rstTNS.getString("email") + "','";
                } else {
                    sqlPersonas += "notieneemail@gmail.com','";
                }

                sqlPersonas += rstTNS.getString("tipoSangre") + "','"
                        + rstTNS.getString("activo") + "',"
                        + rstTNS.getString("fechaIng") + ",'"
                        + rstTNS.getString("usuario") + "','"
                        + rstTNS.getString("flag") + "') on duplicate key update flag='0';";

                listaDeSentenciasPersonas.add(sqlPersonas);

                String sqlEmpleados = "INSERT INTO empleados "
                        + "(cedula,cargo,celularCorporativo,fechaDeIngreso,idAgencia,idRegional,idZona,"
                        + "idCentroDeCosto,fotografia,formatoFotografia,idTipoDeContrato,numeroCuenta,"
                        + "entidadBancaria,isFree,activo,fechaIng,usuario,flag) "
                        + "VALUES('"
                        + rstTNS.getString("cedula") + "','"
                        + rstTNS.getString("cargo") + "','"
                        + rstTNS.getString("celularCorporativo") + "',"
                        + rstTNS.getString("fechaDeIngreso") + ",'"
                        + rstTNS.getString("idAgencia") + "','"
                        + rstTNS.getString("idRegional") + "','"
                        + rstTNS.getString("idZona") + "','"
                        + rstTNS.getString("idCentroDeCosto") + "','"
                        + rstTNS.getString("fotografia") + "','"
                        + rstTNS.getString("formatoFotografia") + "','"
                        + rstTNS.getString("idTipoDeContrato") + "','"
                        + rstTNS.getString("numeroCuenta") + "','"
                        + rstTNS.getString("entidadBancaria") + "','"
                        + rstTNS.getString("isFree") + "','"
                        + rstTNS.getString("activo") + "',"
                        + rstTNS.getString("fechaIng") + ",'"
                        + rstTNS.getString("usuario") + "','"
                        + rstTNS.getString("flag") + "') on duplicate key update flag='-1'";

                listaDeSentenciasEmpleados.add(sqlEmpleados);

            }

            statementTNS.close();
            conTNS.close();
            rstTNS.close();

            ini.insertarBBDDRemota(listaDeSentenciasPersonas, "personas");
            ini.insertarBBDDRemota(listaDeSentenciasEmpleados, "empleados");
            if (this.menu != null) {
                this.ini.setListaDeEmpleados();
                this.menu.setEnabled(true);
                this.menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N

            }

            new Thread(new HiloListadoDeEmpleados(ini)).start();

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNS.class.getName()).log(Level.SEVERE, null, ex + msg);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNS.class.getName()).log(Level.SEVERE, null, ex1 + msg);
            }

        }
    }

    public boolean trasmisionCompleta() {
        //FileReader f = null;
        //String listaDeNumerosDeFacturas = null;

        String fecha = ("" + fIntegrador.ini.getFechaSql(fIntegrador.jFechaInicial));
        if (fIntegrador.chkClientes.isSelected()) {
            setCantidadClientes();
        }
        if (fIntegrador.chkProductos.isSelected()) {
            setCantidadProducto();
        }
        if (fIntegrador.chkFacturas.isSelected()) {
            setCantidadFacturas();
        }
        if (fIntegrador.chkDetalleFacturas.isSelected()) {
            setCantidadItemsFactura();
        }
        fIntegrador.barra2.setValue(0);
        fIntegrador.lblBarra2.setText("0.0 %");
        cantidadRegistros = cantidadClientes + cantidadFacturas + cantidadItemsFactura + cantidadProductos;
        fIntegrador.jBtnCirculo.setVisible(true);
        fecha1 = new Date();
        fIntegrador.mensaje += "Inico de proceso de trasferencia : " + fecha1 + "\n";
        fIntegrador.txtLog.setText(fIntegrador.mensaje);
        fIntegrador.mensaje += "Cantidad de Registros = " + cantidadRegistros + "\n";
        if (cantidadRegistros == 0) {
            fIntegrador.finDeTrasmision(fecha1);
            return true;
        }
        fIntegrador.txtLog.setText(fIntegrador.mensaje);
        if (fIntegrador.chkClientes.isSelected()) {
            fIntegrador.mensaje += "Ingresando los clientes \n";
            fIntegrador.lblBarra1.setText("Ingresando los clientes");
            fIntegrador.txtLog.setText(fIntegrador.mensaje);
            if (fIntegrador.cancelar) {
                fIntegrador.finDeTrasmision(fecha1);
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return true;
            }
            traerClientes();
            fIntegrador.chkClientes.setSelected(false);
        }
        if (fIntegrador.chkProductos.isSelected()) {
            fIntegrador.mensaje += "Ingresando los productos \n";
            fIntegrador.lblBarra1.setText("Ingresando los productos");
            fIntegrador.txtLog.setText(fIntegrador.mensaje);
            if (fIntegrador.cancelar) {
                fIntegrador.finDeTrasmision(fecha1);
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return true;
            }
            traerProductos();
            fIntegrador.chkProductos.setSelected(false);
        }
        if (fIntegrador.chkFacturas.isSelected()) {
            fIntegrador.mensaje += "Ingresando las facturas \n";
            fIntegrador.lblBarra1.setText("Ingresando las facturas");
            fIntegrador.txtLog.setText(fIntegrador.mensaje);
            if (fIntegrador.cancelar) {
                fIntegrador.finDeTrasmision(fecha1);
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return true;
            }
            traerFacturas();
            fIntegrador.chkFacturas.setSelected(false);
        }
        if (fIntegrador.chkDetalleFacturas.isSelected()) {
            fIntegrador.mensaje += "Ingresando el detalle de las facturas \n";
            fIntegrador.lblBarra1.setText("Ingresando el detalle de las facturas");
            fIntegrador.txtLog.setText(fIntegrador.mensaje);
            if (fIntegrador.cancelar) {
                fIntegrador.finDeTrasmision(fecha1);
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return true;
            }
            traerDetalleFacturas();
            fIntegrador.chkDetalleFacturas.setSelected(false);
            fIntegrador.barra2.setValue(100);
            fIntegrador.lblBarra2.setText("100 %");
            fIntegrador.lblBarra2.setText("Proceso de sincronizacio terminado");
        }
        if (fIntegrador.chkPesoFacturas.isSelected()) {
            if (fIntegrador.cancelar) {
                fIntegrador.finDeTrasmision(fecha1);
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return true;
            }
            actualizarPesoFacturas();
            fIntegrador.mensaje += "Actualizado el peso de kg de las facturas \n";
            ;
            fIntegrador.txtLog.setText(fIntegrador.mensaje);
            fIntegrador.chkPesoFacturas.setSelected(false);
        }
        if (fIntegrador.chkCanaFacturas.isSelected()) {
            if (fIntegrador.cancelar) {
                fIntegrador.finDeTrasmision(fecha1);
                fIntegrador.lblBarra1.setText("Proceso Cancelado");
                fIntegrador.lblBarra2.setText("Proceso Cancelado");
                return true;
            }
            actualizarCanaleVentaClientes(true);
            fIntegrador.mensaje += "Actualizado el canal de venta de las facturas \n";
            ;
            fIntegrador.txtLog.setText(fIntegrador.mensaje);
            fIntegrador.chkCanaFacturas.setSelected(false);
        }
        fIntegrador.finDeTrasmision(fecha1);
        return false;
    }

    /**
     * **********************************************************************
     */
    public boolean trasmisionPorFactura() {
        boolean grabado = false;
        try {
            listaDeSentenciasPorFactura = new ArrayList<>();
            salvarCliente();
            salvarProductos();
            salvarFactura();
            salvarDetalleFacturas();

            if (ini.insertarDatosLocalmente(listaDeSentenciasPorFactura)) {
                grabado = ini.insertarDatosRemotamente(listaDeSentenciasPorFactura, "HiloIntegrador TNS");
            }

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    private void salvarCliente() {

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;

        try {

            String sqlCodigoClientes = "select  DISTINCT t.NIT ,t.NITTRI,"
                    + "COALESCE(t.ESTABLECIMIENTO,'NA') as ESTABLECIMIENTO,"
                    + "t.NOMBRE as nombreCliente,t.DIRECC1, Z.NOMBRE as barrio,"
                    + "c.NOMBRE as ciudad,'1' AS clasificacion,SUBSTRING(t.TELEF1 FROM 1 FOR 12) as telefonoCliente,"
                    + "t.EMAIL,'CURRENT_DATE()' AS fechaDeIngresoCliente,'0' AS latitud,"
                    + "'0' AS longitud,'1' AS canalDeVenta,'1' AS  ruta,"
                    + "'1' AS frecuencia,'1' AS zona,'1' AS regional,'1' AS agencia,"
                    + "'0' AS porcentajeDescuento,'1' AS activo,'CURRENT_TIMESTAMP()' AS fechaIng,"
                    + "'AUTOMATICO ' AS usuario, '1' AS flag "
                    + "from terceros t "
                    + "join ZONAS Z on Z.ZONAID = t.ZONA1 "
                    + "join CIUDANE c on c.CIUDANEID = t.CIUDANEID "
                    + "join KARDEX k on k.CLIENTE = t.TERID  "
                    // + "and k.CODPREFIJO ='" + prefijo + "' AND k.NUMERO='" + numeroFactura + "' ;";
                    + "AND k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO ='" + numero + "';";

            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlCodigoClientes);

            if (rstTNS.next()) {

                String sqlClientes = "insert into clientes(codigoInterno,nitCliente,nombreEstablecimiento,"
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
                        + rstTNS.getString("barrio") + "','" // barrio (vacio) ZONATERCERO
                        //+ "','" // barrio (vacio) ZONATERCERO
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
                        + " flag='-1',"
                        + "nitCliente='" + rstTNS.getString("NITTRI") + "',"
                        + "nombreDeCliente='" + rstTNS.getString("nombreCliente") + "',"
                        + "nombreEstablecimiento='" + rstTNS.getString("ESTABLECIMIENTO").replaceAll(System.getProperty("line.separator"), "") + "',"
                        + "direccion='" + rstTNS.getString("DIRECC1") + "',"
                        + "barrio='" + rstTNS.getString("barrio") + "',"
                        + "ciudad='" + rstTNS.getString("ciudad") + "';";

                listaDeSentenciasPorFactura.add(sqlClientes);
                ini.insertarDatosRemotamente(sqlClientes);
                ini.insertarDatosLocalmente(sqlClientes);

            }

            statementTNS.close();
            conTNS.close();
            rstTNS.close();

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
    }

    private void salvarProductos() {

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;

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
                    // + "b.CODBARRA as barcode2 "
                    + "FROM DEKARDEX r "
                    + "join MATERIAL m on m.MATID=r.MATID "
                    // + "left outer join DEMATBARRA b on b.MATID=r.MATID "
                    + "join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID "
                    + "join KARDEX k on k.KARDEXID=r.KARDEXID "
                    + "AND k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO ='" + numero + "';";

            /* sqlListaCodigosproductos = "SELECT DISTINCT r.MATID,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "lm.DESCRIP as linea,"
                    + "'0' as valorUnitario,"
                    + "'0' as valorUnitarioConIva,"
                    + "'1' as isFree,"
                   + " COALESCE(m.PESO,'0') as pesoProducto,"
                    + "'0' as largoProducto,"
                   + " '0' as anchoProducto,"
                   + " '0' as altoProducto,"
                   + " '1' as activo,"
                   + " 'CURRENT_TIMESTAMP()' as fechaIng,"
                   + " 'AUTOMATICO' AS usuario,"
                   + " '0' as flag,"
                   + " COALESCE(m.CODBARRA,'') as barcode "
                   + " FROM DEKARDEX r "
                   + " join MATERIAL m on m.MATID=r.MATID "
                   + " join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID" 
                   + " join KARDEX k on k.KARDEXID=r.KARDEXID -- AND " 
                   // " -- k.FECHA>='07-27-2022, 00:00:00' and k.FECHA<='07-27-2022, 23:59:59' 
                   + " and k.CODPREFIJO = '"+ prefijo +"' AND k.CODCOMP='FV' and k.NUMERO = '" + numero + "' ;";
             */
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaCodigosproductos);

            while (rstTNS.next()) {

                String sqlProductos = "INSERT INTO productos(codigoProducto,descripcionProducto,linea,valorUnitario,"
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
                        + "descripcionProducto='" + rstTNS.getString("descripcionProducto") + "',"
                        + "barcode='" + rstTNS.getString("barcode") + "',"
                        + "pesoProducto='" + rstTNS.getString("pesoProducto") + "';";

                listaDeSentenciasPorFactura.add(sqlProductos);
                ini.insertarDatosRemotamente(sqlProductos);
                ini.insertarDatosLocalmente(sqlProductos);

            }
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }

    }

    private void salvarFactura() {

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;

        try {

            String sqlListaFacturas = CFacturas.getSelectFacturasTNS()
                    + "join TERCEROS t on  r.cliente=t.terid "
                    + "join TERCEROS t2 on r.CODVEN=t2.nit "
                    + "join CIUDANE c on c.CIUDANEID=t.CIUDANEID "
                    + "where  "
                    + "r.CODPREFIJO ='" + prefijo + "' AND r.CODCOMP='FV' and r.NUMERO ='" + numero + "';";

            //conTNS = DriverManager.getConnection("jdbc:firebirdsql://192.168.0.121:3050/" + db, user, password);
            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaFacturas);

            if (rstTNS.next()) {

                String sqlfacturas = "INSERT INTO facturas("
                        + "numeroFactura,fechaDeVenta,cliente,direccion,barrio,ciudad,telefono,"
                        + "formaDePago,vendedor,canal,valorFacturaSinIva,valorIvaFactura,valorTotalFactura,"
                        + "valorRechazo,valorDescuento,valorTotalRecaudado,formato,zona,"
                        + "regional,agencia,isFree,estadoFactura,activo,fechaIng,usuario,flag,pesofactura,"
                        + "ruta,trasmitido,numeroDescuento,numeroRecogida,telefonoVendedor,plazoDias,prefijo,numero,fpContado,observaciones) VALUES('"
                        + rstTNS.getString("numeroFactura") + "','"
                        + rstTNS.getString("fechaDeVenta") + "','"
                        + rstTNS.getString("nitCliente") + "','";

                /*Se evalua la direccion de la entrega del pedido */
                if (rstTNS.getString("observaciones") != null) {
                    if (rstTNS.getString("observaciones").contains("ENTREGAR EN:")) {
                        String cadena[] = rstTNS.getString("observaciones").split(":");
                        sqlfacturas += cadena[1].trim() + "','";

                    } else {
                        sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                    }
                } else {
                    sqlfacturas += rstTNS.getString("direccionDeCliente") + "','";
                }

                sqlfacturas += rstTNS.getString("barrio") + "','"
                        + rstTNS.getString("ciudad") + "','"
                        + rstTNS.getString("telefonoCliente") + "','";

                /*Se evalua la forma de pago */
                if (rstTNS.getInt("plazoDias") > 1) {
                    sqlfacturas += "CREDITO','";
                } else {
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
                        + rstTNS.getInt("plazoDias") + "','"
                        + rstTNS.getString("prefijo") + "','"
                        + rstTNS.getString("numero") + "','"
                        + rstTNS.getString("fpContado") + "','"
                        + rstTNS.getString("observaciones") + "') on duplicate key update "
                        + "telefonoVendedor='" + rstTNS.getString("telefonoVendedor") + "',"
                        + "observaciones='" + rstTNS.getString("observaciones") + "';";
                
                if(rstTNS.getString("observaciones").length()> 0 || rstTNS.getString("observaciones")!= null){
                    ini.setNovedadEnFactura("Novedad en factura # "
                            + rstTNS.getString("numeroFactura") + "\n "
                            + rstTNS.getString("observaciones"));
                }else{
                    ini.setNovedadEnFactura(null);
                }

                listaDeSentenciasPorFactura.add(sqlfacturas);
                ini.insertarDatosRemotamente(sqlfacturas);
                ini.insertarDatosLocalmente(sqlfacturas);

            }
            statementTNS.close();
            conTNS.close();
            rstTNS.close();

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }

    }

    private void salvarDetalleFacturas() {

        Connection conTNS = null;
        Statement statementTNS = null;
        ResultSet rstTNS = null;

        try {

            String sqlListaDetalleFactura = "SELECT k.NUMERO,"
                    + "k.CODPREFIJO||k.NUMERO as numeroFactura,"
                    + "m.CODIGO as codigoProducto,"
                    + "m.DESCRIP as descripcionProducto,"
                    + "SUM(d.CANMAT) AS CANTIDAD, "
                    + "d.PRECIOBASE as valorUnitario,"
                    + " (d.PRECIOBASE * SUM(d.CANMAT)) as valorTotal,"
                    + "d.PRECIOVTA as valorUnitarioConIva,"
                    + "(d.PRECIOVTA * SUM(d.CANMAT)) as valorTotalConIva,"
                    + "(COALESCE(m.PESO,0) * SUM(d.CANMAT)) as pesoProducto "
                    + "FROM "
                    + "DEKARDEX  d "
                    + "join KARDEX k on k.KARDEXID=d.KARDEXID and "
                    + "k.CODPREFIJO ='" + prefijo + "' AND k.CODCOMP='FV' and k.NUMERO ='" + numero + "' "
                    + "join MATERIAL m ON m.MATID=d.MATID "
                    + "GROUP BY numeroFactura,codigoProducto,descripcionProducto,d.CANMAT,valorUnitario,numero,m.peso,d.PRECIOVTA "
                    + "order by numeroFactura,codigoProducto";

            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//" + ini.getServidorFuente() + "/" + ini.getDbFuente(), ini.getUserFuente(), ini.getPsdFuente());
            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sqlListaDetalleFactura);

            while (rstTNS.next()) {

                String sqlDetalleFacturas = "INSERT INTO productosporfactura"
                        + "(factura,codigoProducto,cantidad,valorUnitario,valorTotal,valorUnitarioConIva,"
                        + "valorTotalConIva,pesoProducto,activo,fechaIng,usuario,flag,descripcionProducto) VALUES('"
                        + rstTNS.getString("numeroFactura") + "','"
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

                listaDeSentenciasPorFactura.add(sqlDetalleFacturas);

            }

            statementTNS.close();
            conTNS.close();
            rstTNS.close();

        } catch (SQLException ex) {
            try {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex);
                statementTNS.close();
                conTNS.close();
                rstTNS.close();

            } catch (SQLException ex1) {
                Logger.getLogger(HiloIntegradorTNSLaHielera.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }

    }

}
