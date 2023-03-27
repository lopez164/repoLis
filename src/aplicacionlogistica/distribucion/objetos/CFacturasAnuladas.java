/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class CFacturasAnuladas {

    int idfacturasanuladas;
    String numeroFactura;
    int numeroManifiesto;
    String observacion;
    Inicio ini;

    public CFacturasAnuladas() {

    }

    public CFacturasAnuladas(int idfacturasanuladas, String numeroFactura, String observacion) {
        this.idfacturasanuladas = idfacturasanuladas;
        this.numeroFactura = numeroFactura;
        this.observacion = observacion;
    }

    public CFacturasAnuladas(Inicio ini) {
        this.ini = ini;
    }

    public CFacturasAnuladas(String numeroFactura, Inicio ini) {
        this.numeroFactura = numeroFactura;
        this.ini = ini;

        String sql = "SELECT * "
                + "FROM facturasanuladas "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "';";

        traerDatosBBDD(sql);
    }

    private void traerDatosBBDD(String sql) {

        Connection con;
        Statement st;
        ResultSet rst;

       // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
         con = this.ini.getConnRemota();
        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                if (rst.next()) {

                    this.idfacturasanuladas = (rst.getInt("idfacturasanuladas"));
                    this.numeroFactura = (rst.getString("numeroFactura"));
                    this.numeroManifiesto = (rst.getInt("numeroManifiesto"));
                    this.observacion = (rst.getString("observacion"));

                }

            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String getCadenaConLosCampos() {
        String cadena = this.idfacturasanuladas + ","
                + this.numeroFactura + ","
                + this.numeroManifiesto + ","
                + this.observacion;
        return cadena;

    }

    /**
     * Método que permite guardar los registros en la base de datos
     *
     * @param lista es la lista de factura evaluadas y listas para grabar en el
     * sistema
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    public boolean grabarFacturasAnuladas(ArrayList<CFacturasParaAnular> lista) {
        String sql;
        CControladorDeDocumentos controlador = new CControladorDeDocumentos(ini);
        ArrayList<String> listaSentenciasInsert = new ArrayList<>();
        boolean grabado = false;

        if (lista != null) {

            try {
                controlador.setIsFree(1);
                controlador.setTipoDocumento(3);
                controlador.grabarControladorDeDocumentos();

                for (CFacturasParaAnular obj : lista) {
                    this.numeroManifiesto = controlador.getIdcontrolador();
                    this.numeroFactura = obj.getNumeroFactura();
                    this.observacion = obj.getCausalDeRechazo();
                    
                    sql = "INSERT INTO `facturasanuladas`(`numeroManifiesto`,`numeroFactura`,`observacion`,"
                            + "`activo`,`usuario`,`flag`) VALUES('"
                            + this.numeroManifiesto + "','"
                            + this.numeroFactura + "','"
                            + this.observacion + "','"
                            + "1','"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',' "
                            + "1')"
                            + " ON DUPLICATE KEY UPDATE "
                            + "numeroManifiesto='" + this.numeroManifiesto + "',"
                            + "numeroFactura='" + this.numeroFactura + "',"
                            + "observacion='" + this.observacion + "',"
                            + "activo='1',"
                            + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "', "
                            + "flag='-1';";

                    listaSentenciasInsert.add(sql);
                }

                grabado = ini.insertarDatosRemotamente(listaSentenciasInsert,"CFacturasAnuladas.grabarFacturasAnuladas");

            } catch (SQLException ex) {
                Logger.getLogger(CFacturasAnuladas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CFacturasAnuladas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return grabado;
    }

    public int getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(int numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public int getIdfacturasanuladas() {
        return idfacturasanuladas;
    }

    public void setIdfacturasanuladas(int idfacturasanuladas) {
        this.idfacturasanuladas = idfacturasanuladas;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

}
