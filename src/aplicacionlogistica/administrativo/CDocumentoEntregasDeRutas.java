/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.administrativo;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.objetos.CControladorDeDocumentos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase define el Objeto documento para la Entrega de Rutas
 *
 * @author Luis Eduardo López Casanova
 *
 */
public class CDocumentoEntregasDeRutas {//extends Inicio {

//  Se definen los atributos de la clase
    int idDocumento;
    String usuarioEntregador;
    String usuarioRevisor;
    String usuarioQueRecibe;
    int estadoDocumento;
    Date fecha;
    Date fechaRevisado;
    Date fechaEntregado;
    int zona, regional, agencia;
    int activo;
    int isFree;
    Double valorTotalDocumento=0.0;
    Double valorRecaudado=0.0;
    Double valorConsignado=0.0;
    Inicio ini;

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getUsuarioEntregador() {
        return usuarioEntregador;
    }

    public void setUsuarioEntregador(String usuarioEntregador) {
        this.usuarioEntregador = usuarioEntregador;
    }

    public String getUsuarioRevisor() {
        return usuarioRevisor;
    }

    public void setUsuarioRevisor(String usuarioRevisor) {
        this.usuarioRevisor = usuarioRevisor;
    }

    public String getUsuarioQueRecibe() {
        return usuarioQueRecibe;
    }

    public void setUsuarioQueRecibe(String usuarioQueRecibe) {
        this.usuarioQueRecibe = usuarioQueRecibe;
    }

    public int getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(int estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaRevisado() {
        return fechaRevisado;
    }

    public void setFechaRevisado(Date fechaRevisado) {
        this.fechaRevisado = fechaRevisado;
    }

    public Date getFechaEntregado() {
        return fechaEntregado;
    }

    public void setFechaEntregado(Date fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public Double getValorTotalDocumento() {
        return valorTotalDocumento;
    }

    public void setValorTotalDocumento(Double valorTotalDocumento) {
        this.valorTotalDocumento = valorTotalDocumento;
    }

    public Double getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(Double valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public Double getValorConsignado() {
        return valorConsignado;
    }

    public void setValorConsignado(Double valorConsignado) {
        this.valorConsignado = valorConsignado;
    }

    public CDocumentoEntregasDeRutas(Inicio ini) {
        this.ini = ini;
    }

    /**
     * Método que permite guardar los registros en la base de datos
     *
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    public boolean grabarDocumentoEntrgaDeRuta() {

        CControladorDeDocumentos controlador = new CControladorDeDocumentos(ini);

        boolean grabado = false;
        try {
            controlador.setIsFree(1);
            controlador.setTipoDocumento(4);
            controlador.grabarControladorDeDocumentos();

            this.agencia = ini.getUser().getAgencia();
            this.zona = ini.getUser().getZona();
            this.regional = ini.getUser().getRegional();
            this.idDocumento = controlador.getIdcontrolador();
            ArrayList<String> sql = new ArrayList();

            sql.add("INSERT INTO documentosEntregasDeRuta (idDocumento,usuarioEntregador,usuarioRevisor,"
                    + "usuarioQueRecibe,estadoDocumento,fecha,fechaRevisado,fechaEntregado,zona,regional,agencia,isFree,valorTotalDocumento,"
                    + "valorRecaudado,valorConsignado,activo,fechaIng,usuario,flag) VALUES("
                    + this.idDocumento + "','"
                    + this.usuarioEntregador + "','"
                    + this.usuarioRevisor + "','"
                    + this.usuarioQueRecibe + "','"
                    + this.estadoDocumento + "','"
                    + this.fecha + "','"
                    + this.fechaRevisado + "','"
                    + this.fechaEntregado + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + this.isFree + "','"
                    + this.valorTotalDocumento + "','"
                    + this.valorRecaudado + "','"
                    + this.valorConsignado + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                    + "-1')"
                    + " ON DUPLICATE KEY UPDATE "
                    + "idDocumento ='" + this.idDocumento + "',"
                    + "usuarioEntregador ='" + this.usuarioEntregador + "',"
                    + "usuarioRevisor ='" + this.usuarioRevisor + "',"
                    + "usuarioQueRecibe ='" + this.usuarioQueRecibe + "',"
                    + "estadoDocumento ='" + this.estadoDocumento + "',"
                    + "fechaRevisado ='" + this.fechaRevisado + "',"
                    + "fechaEntregado ='" + this.fechaEntregado + "',"
                    + "zona ='" + this.zona + "',"
                    + "regional ='" + this.regional + "',"
                    + "agencia ='" + this.agencia + "',"
                    + "isFree ='" + this.isFree + "',"
                    + "valorTotalDocumento ='" + this.valorTotalDocumento + "',"
                    + "valorRecaudado ='" + this.valorRecaudado + "',"
                    + "valorConsignado ='" + this.valorConsignado + "',"
                    + "activo ='" + this.activo + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',flag='-1';");

            grabado = ini.insertarDatosRemotamente(sql,"CDocumentoEntregasDeRutas.grabarDocumentoEntrgaDeRuta");

        } catch (SQLException ex) {
            Logger.getLogger(CDocumentoEntregasDeRutas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CDocumentoEntregasDeRutas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean buscarManifiestosDescargados() {
        boolean encontrado = false;
        Connection con;
        Statement st;
        ResultSet rst;
        String sql = "select * from manifiestosdedistribucion where estadoManifiesto=4 and flag=1";
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    encontrado = true;

                }
                rst.close();
                st.close();
                con.close();

                return encontrado;
            } catch (SQLException ex) {
                Logger.getLogger(CDocumentoEntregasDeRutas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         return encontrado;
    }
    }
