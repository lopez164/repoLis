/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas.minutas;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductosPorMinuta;
import com.opencsv.CSVWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class MinutasDeDistribucion {

    String consulta;
//= "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
//            + "from facturaspormanifiesto fm"
//            + "join productosporfactura pf on pf.factura=fm.numerofactura"
//            + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
//            + "where numeroManifiesto='17856'  and"
//            + "-- p.codigoProducto like('029%')"
//            + "pf.descripcionProducto like('%suavitel%')"
//            + "group by pf.codigoProducto"
//            + "order by pf.codigoProducto asc";
    Inicio ini;
    String numeroManifiesto, codigoProducto, descripcionProducto, cantidad;
    ArrayList<String[]> productos;

    List<CProductosPorMinuta> listaProductosPorMinuta;

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public MinutasDeDistribucion(Inicio ini) {
        this.ini = ini;
    }

    public void minutaPorLineaDeProducto() {

        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto,pf.descripcionProducto "
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorCodigoDeProducto() {

        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto,pf.descripcionProducto "
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorNombreDeProducto() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto,pf.descripcionProducto "
                + "order by pf.codigoProducto asc";

    }

    public void minutaPoManifiesto(String numeromanifiesto) {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String clave = null;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad) as cantidad, sum(pf.pesoProducto)/1000 as peso  "
                + "from facturaspormanifiesto fm "
                + "join productosporfactura pf on pf.factura=fm.numerofactura "
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto "
                + "join facturascamdun f on f.numeroFactura=pf.factura "
                + "where fm.numeroManifiesto='" + numeromanifiesto + "' and fm.activo=1 "
                // + "group by f.vendedor,pf.codigoProducto "
                // + "order by f.vendedor,pf.codigoProducto asc ;";
                + "group by pf.codigoProducto,pf.descripcionProducto "
                + "order by pf.codigoProducto asc ;";
        if (con != null) {

            try {
                productos = new ArrayList<>();

                st = con.createStatement();
                rst = st.executeQuery(consulta);

//                while (rst.next()) {
//                    String[] elemento = new String[4];
//
//                    elemento[0] = rst.getString("numeroManifiesto");
//                    elemento[1] = rst.getString("codigoProducto");
//                    elemento[2] = rst.getString("descripcionProducto");
//                    elemento[3] = rst.getString("cantidad");
//
//                    productos.add(elemento);
//                }

                /*Se genera un nombre aleatorio para el archivo*/
                clave = UUID.randomUUID().toString().substring(0, 8);

                try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/minutaDeCargue_" + numeromanifiesto + ".csv"), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);

                } catch (IOException ex) {
                    Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                }
                rst.close();
                st.close();
                con.close();
                Desktop.getDesktop().open(new File("tmp/minutaDeCargue_" + numeromanifiesto + ".csv"));

            } catch (SQLException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void minutaPoManifiesto(String numeromanifiesto, String listaDeProductos) {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String clave = null;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        consulta = "Select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad) as cantidad, sum(pf.pesoProducto)/1000 as peso  "
                + "from facturaspormanifiesto fm "
                + "join productosporfactura pf on pf.factura=fm.numerofactura "
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto "
                + "join facturascamdun f on f.numeroFactura=pf.factura "
                + "where fm.numeroManifiesto='" + numeromanifiesto + "' and fm.activo=1 and "
                + "pf.codigoProducto in(" + listaDeProductos + ") "
                // + "group by f.vendedor,pf.codigoProducto "
                // + "order by f.vendedor,pf.codigoProducto asc ;";
                + "group by pf.codigoProducto,pf.descripcionProducto "
                + "order by pf.codigoProducto asc ;";
        if (con != null) {

            try {
                productos = new ArrayList<>();

                st = con.createStatement();
                rst = st.executeQuery(consulta);

//                while (rst.next()) {
//                    String[] elemento = new String[4];
//
//                    elemento[0] = rst.getString("numeroManifiesto");
//                    elemento[1] = rst.getString("codigoProducto");
//                    elemento[2] = rst.getString("descripcionProducto");
//                    elemento[3] = rst.getString("cantidad");
//
//                    productos.add(elemento);
//                }

                /*Se genera un nombre aleatorio para el archivo*/
                clave = UUID.randomUUID().toString().substring(0, 8);

                try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/minutaDeCargue_" + numeromanifiesto + ".csv"), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);

                } catch (IOException ex) {
                    Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                }
                rst.close();
                st.close();
                con.close();
                Desktop.getDesktop().open(new File("tmp/minutaDeCargue_" + numeromanifiesto + ".csv"));

            } catch (SQLException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void minutaPorManifiestoAbierto(List<CFacturasPorManifiesto> lista) {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String clave = null;
        //String numeroManifiesto="";
        String listaDeFacturas = "";

        for (CFacturasPorManifiesto fac : lista) {

            listaDeFacturas += "'" + fac.getNumeroFactura() + "',";
            numeroManifiesto = fac.getNumeroManifiesto();
        }

        listaDeFacturas = listaDeFacturas.substring(0, listaDeFacturas.length() - 1);

        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        consulta = "select 00, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad) as cantidad,sum(pf.pesoProducto)/1000 as peso "
                + "from productosporfactura pf "
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto "
                + "join facturascamdun f on f.numeroFactura=pf.factura "
                + "where f.numeroFactura in(" + listaDeFacturas + ")"
                //  group by f.vendedor,pf.codigoProducto 
                //   order by f.vendedor,pf.codigoProducto asc ;;
                + "group by pf.codigoProducto,pf.descripcionProducto "
                + "order by pf.codigoProducto asc ;";

        if (con != null) {
            try {
                productos = new ArrayList<String[]>();

                st = con.createStatement();
                rst = st.executeQuery(consulta);

                if (rst.next()) {
//                    String[] elemento= new String[4];
//                    
//                    elemento[0] = rst.getString("numeroManifiesto");
//                    elemento[1] = rst.getString("codigoProducto");
//                    elemento[2] = rst.getString("descripcionProducto");
//                    elemento[3] = rst.getString("cantidad");
//                    
//                    productos.add(elemento);

                    /*Se genera un nombre aleatorio para el archivo*/
                    clave = UUID.randomUUID().toString().substring(0, 8);

                    try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/minutaDeCargue_" + numeroManifiesto + ".csv"), '\t')) {
                        //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                        Boolean includeHeaders = true;
                        writer.writeAll(rst, includeHeaders);
                        // Desktop.getDesktop().open(new File("yourfile.csv"));
                    }
                }
                // }
                rst.close();
                st.close();
                con.close();
                Desktop.getDesktop().open(new File("tmp/minutaDeCargue_" + numeroManifiesto + ".csv"));

            } catch (SQLException | IOException ex) {
                try {
                    rst.close();
                    st.close();
                    con.close();
                    Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }

        }

    }

    public void minutaPoManifiestoCerrado() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad), sum(pf.pesoProducto)/1000 as peso "
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorListaDeFacturas(String listadeFacturas) {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String clave = null;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        consulta = "select 'SIN MANIFIESTO' as numeroManifiesto, pf.codigoProducto, pf.descripcionProducto , "
                + "sum(pf.cantidad) as cantidad, sum(pf.pesoProducto)/1000 as peso "
                + "from productosporfactura pf "
                + "where pf.factura in(" + listadeFacturas + ")"
                + "group by  pf.codigoProducto,pf.descripcionProducto "
                + "order by  pf.codigoProducto asc;";
        if (con != null) {

            try {
                productos = new ArrayList<>();

                st = con.createStatement();
                rst = st.executeQuery(consulta);

                /*Se genera un nombre aleatorio para el archivo*/
                clave = UUID.randomUUID().toString().substring(0, 8);

                try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/minutaDeCargue_" + clave + ".csv"), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);

                } catch (IOException ex) {
                    Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                }

                rst.beforeFirst();
                while (rst.next()) {
                    String[] elemento = new String[5];

                    elemento[0] = rst.getString("numeroManifiesto");
                    elemento[1] = rst.getString("codigoProducto");
                    elemento[2] = rst.getString("descripcionProducto");
                    elemento[3] = rst.getString("cantidad");
                    elemento[4] = rst.getString("peso");

                    productos.add(elemento);
                }

                rst.close();
                st.close();
                con.close();
                Desktop.getDesktop().open(new File("tmp/minutaDeCargue_" + clave + ".csv"));

            } catch (SQLException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Método que devuelve la minuta de las facturas que un conductor va a sacar
     * a distribucion
     *
     * @param listadeFacturas es una cadena con la lista de las facturas con el
     * siguiente formato '#######','######','#######','######' y asi
     * sucesivamente
     * @return devuelve una cadena con los campos referencia, descripcion,
     * cantidad, y valor total del productos
     */
    public List<CProductosPorMinuta> getMinutaPorConductor(String listadeFacturas) {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String clave = null;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        String sql = "select pf.codigoProducto, pf.descripcionProducto ,sum(pf.pesoProducto) as peso, "
                + "sum(pf.cantidad) as cantidad, sum(pf.valorTotalConIva) as valor "
                + "from productosporfactura pf "
                + "where pf.factura in(" + listadeFacturas + ")"
                + "group by  pf.codigoProducto,pf.descripcionProducto "
                + "order by  pf.codigoProducto asc;";
        if (con != null) {

            try {

                listaProductosPorMinuta = new ArrayList<>();

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CProductosPorMinuta productosMinuta = new CProductosPorMinuta();

                    productosMinuta.setCodigoProducto(rst.getString("codigoProducto"));
                    productosMinuta.setDescripcionProducto(rst.getString("descripcionProducto"));
                    productosMinuta.setCantidad(rst.getDouble("cantidad"));
                    productosMinuta.setPeso(rst.getDouble("peso"));
                    productosMinuta.setValor(rst.getDouble("valor"));

                    listaProductosPorMinuta.add(productosMinuta);
                }

                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return listaProductosPorMinuta;

    }

    public void minutaPorListaDeFacturas(String listadeFacturas, String codigosDeProductos) {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String clave = null;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        consulta = "select 'SIN MANIFIESTO' as numeroManifiesto, pf.codigoProducto, pf.descripcionProducto , "
                + "sum(pf.cantidad) as cantidad, sum(pf.pesoProducto)/1000 as peso "
                + "from productosporfactura pf "
                + "where pf.factura in(" + listadeFacturas + ") and "
                + "pf.codigoProducto in(" + codigosDeProductos + ") "
                + "group by  pf.codigoProducto,pf.descripcionProducto "
                + "order by  pf.codigoProducto asc;";
        if (con != null) {

            try {
                productos = new ArrayList<>();

                st = con.createStatement();
                rst = st.executeQuery(consulta);

                /*Se genera un nombre aleatorio para el archivo*/
                clave = UUID.randomUUID().toString().substring(0, 8);

                try (CSVWriter writer = new CSVWriter(new FileWriter("tmp/minutaDeCargue_" + clave + ".csv"), '\t')) {
                    //try (CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),',')) {
                    Boolean includeHeaders = true;
                    writer.writeAll(rst, includeHeaders);

                } catch (IOException ex) {
                    Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
                }

                rst.beforeFirst();
                while (rst.next()) {
                    String[] elemento = new String[5];

                    elemento[0] = rst.getString("numeroManifiesto");
                    elemento[1] = rst.getString("codigoProducto");
                    elemento[2] = rst.getString("descripcionProducto");
                    elemento[3] = rst.getString("cantidad");
                    elemento[4] = rst.getString("peso");

                    productos.add(elemento);
                }

                rst.close();
                st.close();
                con.close();
                Desktop.getDesktop().open(new File("tmp/minutaDeCargue_" + clave + ".csv"));

            } catch (SQLException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MinutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void minutaPoFechaDeDistribucion() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorFechaDeVenta() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorVendedor() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorConductor() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorDespachador() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorCanalDeDistribucion() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorCanalDeVenta() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }

    public void minutaPorTipoPortafolio() {
        consulta = "select fm.numeroManifiesto, pf.codigoProducto,pf.descripcionProducto,sum(pf.cantidad)"
                + "from facturaspormanifiesto fm"
                + "join productosporfactura pf on pf.factura=fm.numerofactura"
                + "join productoscamdun p on p.codigoProducto=pf.codigoProducto"
                + "where numeroManifiesto='17856'  and"
                + "-- p.codigoProducto like('029%')"
                + "pf.descripcionProducto like('%suavitel%')"
                + "group by pf.codigoProducto"
                + "order by pf.codigoProducto asc";

    }
}
