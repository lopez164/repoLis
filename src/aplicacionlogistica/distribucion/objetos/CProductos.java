/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CProductos {//extends Inicio {

    String codigoProducto;
    String descripcionProducto;
    String linea;
    Double valorUnitarioSinIva;
    Double valorUnitarioConIva;
    int isFree;
    double pesoProducto;
    double largoProducto;
    double anchoProducto;
    double altoProducto;
    String codigoDeBarras;

    int activo;

    Inicio ini;

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public Double getValorUnitarioSinIva() {
        return valorUnitarioSinIva;
    }

    public void setValorUnitarioSinIva(Double valorUnitario) {
        this.valorUnitarioSinIva = valorUnitario;
    }

    public Double getValorUnitarioConIva() {
        return valorUnitarioConIva;
    }

    public void setValorUnitarioConIva(Double valorUnitarioConIva) {
        this.valorUnitarioConIva = valorUnitarioConIva;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(String codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public CProductos() {
    }

    public CProductos(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public int getIsFree() {

        return isFree;
    }

    public void setIsFree(int isFree) {

        this.isFree = isFree;

    }

    public double getPesoProducto() {
        return pesoProducto;
    }

    public void setPesoProducto(double pesoProducto) {
        this.pesoProducto = pesoProducto;
    }

    public double getLargoProducto() {
        return largoProducto;
    }

    public void setLargoProducto(double largoProducto) {
        this.largoProducto = largoProducto;
    }

    public double getAnchoProducto() {
        return anchoProducto;
    }

    public void setAnchoProducto(double anchoProducto) {
        this.anchoProducto = anchoProducto;
    }

    public double getAltoProducto() {
        return altoProducto;
    }

    public void setAltoProducto(double altoProducto) {
        this.altoProducto = altoProducto;
    }

    /**
     * Método Constructor un objeto producto con los campos de la BBDD s"
     *
     * @param ini
     * @param codigoProducto id del producto
     * @throws java.lang.Exception
     */
    public CProductos(Inicio ini, String codigoProducto) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = this.ini.getConnLocal();

            sql = "SELECT codigoProducto, descripcionProducto, linea, valorUnitario,valorUnitarioConIva,"
                    + " isFree, activo, usuario, flag "
                    + "FROM productos "
                    + "WHERE "
                    + "codigoProducto='" + codigoProducto + "' and "
                    + "activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {

                    this.codigoProducto = rst.getString("codigoProducto");
                    this.descripcionProducto = rst.getString("descripcionProducto");
                    this.linea = rst.getString("linea");
                    this.valorUnitarioSinIva = rst.getDouble("valorUnitario");
                    this.valorUnitarioConIva = rst.getDouble("valorUnitarioConIva");
                    this.isFree = rst.getInt("isFree");
                    this.pesoProducto = rst.getDouble("pesoProducto");

                    this.largoProducto = rst.getDouble("largoProductov");
                    this.altoProducto = rst.getDouble("altoProducto");
                    this.anchoProducto = rst.getDouble("anchoProducto");

                    this.activo = rst.getInt("activo");

                } else {
                    this.codigoProducto = null;

                }
                rst.close();
                st.close();
                //con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método Constructor un objeto producto con los campos de la BBDD
     *
     * @param ini
     * @param codigoProducto id del producto
     * @param codigoDebarras codigo de barras del producto seleccionado
     *
     * uno de los dos parametros debe ser null, solo admite un parametro
     * diferente de null
     * @throws java.lang.Exception
     *
     */
    public CProductos(Inicio ini, String codigoProducto, String codigoDebarras) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql = null;
            ResultSet rst;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = this.ini.getConnLocal();

            if (codigoProducto != null) {
                sql = "SELECT codigoProducto, descripcionProducto, linea, valorUnitario,valorUnitarioConIva,"
                        + " isFree, activo, usuario, flag "
                        + "FROM productos "
                        + "WHERE "
                        + "codigoProducto='" + codigoProducto + "' and "
                        + "activo=1;";
            }
            if (codigoDebarras != null) {
                sql = "SELECT p.codigoProducto, p.descripcionProducto, p.linea, p.valorUnitario,p.valorUnitarioConIva,"
                        + " p.isFree, p.activo, p.usuario, p.flag, p.barcode"
                        + "FROM productos p "
                        // + "join productosCodigoDeBarra cb on cb.codigoProducto= p.codigoProducto"
                        + "WHERE "
                        + "p.barcode='" + codigoDebarras + "';";
            }

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {

                    this.codigoProducto = rst.getString("codigoProducto");
                    this.descripcionProducto = rst.getString("descripcionProducto");
                    this.linea = rst.getString("linea");
                    this.valorUnitarioSinIva = rst.getDouble("valorUnitario");
                    this.valorUnitarioConIva = rst.getDouble("valorUnitarioConIva");
                    this.isFree = rst.getInt("isFree");
                    this.pesoProducto = rst.getDouble("pesoProducto");

                    this.largoProducto = rst.getDouble("largoProductov");
                    this.altoProducto = rst.getDouble("altoProducto");
                    this.anchoProducto = rst.getDouble("anchoProducto");

                    this.activo = rst.getInt("activo");
                    this.codigoDeBarras = rst.getString("barcode");

                } else {
                    this.codigoProducto = null;

                }
                rst.close();
                st.close();
                //con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar Manifiestos De Distribucion consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarProductos() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO productos (codigoProducto, descripcionProducto, linea, valorUnitario,valorUnitarioConIva,"
                    + " isFree,pesoProducto,largoProducto,altoProducto,anchoProducto, activo, usuario, flag) VALUES ('"
                    + this.codigoProducto + "','"
                    + this.descripcionProducto + "','"
                    + this.linea + "','"
                    + this.valorUnitarioSinIva + "','"
                    + this.valorUnitarioConIva + "','"
                    + this.isFree + "','"
                    + this.pesoProducto + "','"
                    + this.largoProducto + "','"
                    + this.altoProducto + "','"
                    + this.anchoProducto + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "codigoProducto='" + this.codigoProducto + "',"
                    + "descripcionProducto='" + this.descripcionProducto + "',"
                    + "linea='" + this.linea + "',"
                    + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                    + "valorUnitario='" + this.valorUnitarioConIva + "',"
                    + "isFree='" + this.isFree + "',"
                    // + "pesoProducto='" + this.pesoProducto + "',"
                    + "largoProducto='" + this.largoProducto + "',"
                    + "altoProducto='" + this.altoProducto + "',"
                    + "anchoProducto='" + this.anchoProducto + "',"
                    + "activo='" + this.activo + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',flag='-1';";

            if (grabado = ini.insertarDatosLocalmente(sql)) {

            }

        } catch (SQLException ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;

    }

    public boolean actualizarProductos() {
        boolean grabado = false;
        try {

            String sql = "UPDATE  productos SET "
                    + "codigoProducto='" + this.codigoProducto + "',"
                    + "descripcionProducto='" + this.descripcionProducto + "',"
                    + "linea='" + this.linea + "',"
                    + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                    + "valorUnitario='" + this.valorUnitarioConIva + "',"
                    + "isFree='" + this.isFree + "',"
                    + "pesoProducto='" + this.pesoProducto + "',"
                    + "largoProducto='" + this.largoProducto + "',"
                    + "altoProducto='" + this.altoProducto + "',"
                    + "anchoProducto='" + this.anchoProducto + "',"
                    + "activo='" + this.activo + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',flag='-1';";

            if (grabado = ini.insertarDatosLocalmente(sql)) {
                //new Thread(new HiloGuardarBBDDRemota(ini, sql)).start();
            }

        } catch (SQLException ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;

    }

    /**
     * Método que devuelve una cadena con la lista de los campos de la BBDD
     * separados por ","
     *
     * @return una cadena con la lista de todos los campos de la tabla clientes
     *
     */
    public String getCadenaConLosCampos() {
        String cadena = null;
        cadena = this.codigoProducto + ","
                + this.descripcionProducto + ","
                + this.linea + ","
                + this.valorUnitarioSinIva + ","
                + this.valorUnitarioConIva + ","
                + this.isFree + ","
                + this.activo;

        return cadena;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {

            sql = "INSERT INTO productos (codigoProducto, descripcionProducto, linea, "
                    + "valorUnitario,valorUnitarioConIva,pesoProducto,largoProducto,"
                    + "anchoProducto,altoProducto,isFree, activo, usuario, flag) VALUES ('"
                    + this.codigoProducto + "','"
                    + this.descripcionProducto + "','"
                    + this.linea + "','"
                    + this.valorUnitarioSinIva + "','"
                    + this.valorUnitarioConIva + "','"
                    + this.pesoProducto + "','"
                    + this.largoProducto + "','"
                    + this.anchoProducto + "','"
                    + this.altoProducto + "','"
                    + this.isFree + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','2') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "codigoProducto='" + this.codigoProducto + "',"
                    + "descripcionProducto='" + this.descripcionProducto + "',"
                    + "linea='" + this.linea + "',"
                    + "valorUnitario='" + this.valorUnitarioSinIva + "',"
                    + "valorUnitarioConIva='" + this.valorUnitarioConIva + "',"
                    + "pesoProducto='" + this.pesoProducto + "',"
                    + "largoProducto='" + this.largoProducto + "',"
                    + "anchoProducto='" + this.anchoProducto + "',"
                    + "altoProducto='" + this.altoProducto + "',"
                    + "isFree='" + this.isFree + "',"
                    + "activo='" + this.activo + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    public String arrListaDeProductos() {

        String sql = "SELECT * "
                + "FROM productos;";

        return sql;
    }

    public String validarProducto(String codigo) {
        String producto = null;
        try {
            Connection conTNS = null;
            Statement statementTNS = null;
            ResultSet rstTNS = null;
            String sql = "SELECT m.MATID,"
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
                    + "COALESCE(m.CODBARRA,'') as barcode, "
                    + "b.CODBARRA as barcode2 "
                    + "FROM MATERIAL m "
                    + "join LINEAMAT lm on lm.LINEAMATID=m.LINEAMATID "
                    + "left outer join DEMATBARRA b on b.MATID = m.MATID "
                    + "where "
                    + " ("
                    + " b.CODBARRA='" + codigo + "' or  m.codigo='" + codigo + "');";

            conTNS = DriverManager.getConnection(ini.getuRLFuente() + "//"
                    + ini.getServidorFuente()
                    + "/" + ini.getDbFuente(),
                    ini.getUserFuente(),
                    ini.getPsdFuente());

            statementTNS = conTNS.createStatement();
            rstTNS = statementTNS.executeQuery(sql);

            if (rstTNS.next()) {
                producto = ""
                        + rstTNS.getString("codigoProducto") + "#"
                        + rstTNS.getString("descripcionProducto") + "#"
                        + rstTNS.getString("linea") + "#"
                        + rstTNS.getString("valorUnitario") + "#"
                        + rstTNS.getString("valorUnitarioConIva") + "#"
                        + rstTNS.getString("isFree") + "#"
                        + rstTNS.getString("pesoProducto") + "#"
                        + rstTNS.getString("largoProducto") + "#"
                        + rstTNS.getString("anchoProducto") + "#"
                        + rstTNS.getString("altoProducto") + "#"
                        + rstTNS.getString("activo") + "#"
                        + "CURRENT_TIMESTAMP()" + "#"
                        + rstTNS.getString("usuario") + "#"
                        + rstTNS.getString("flag") + "#"
                        + rstTNS.getString("barcode2") + "";

            } else {
                producto = null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CProductos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return producto;
    }

}
