/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeFacturasPendientesPorConductor implements Runnable {

    int caso=0;
    int estadoManifiesto = 0;
    Inicio ini;
    String fecha= null;
    CEmpleados conductor;

    // ResultSet rst = null;
    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloListadoDeFacturasPendientesPorConductor(Inicio ini) {

        this.ini = ini;
         caso=1;

    }

    public HiloListadoDeFacturasPendientesPorConductor(Inicio ini, int estadoManifiesto) {

        this.ini = ini;
        this.estadoManifiesto = estadoManifiesto;
         caso=2;
       

    }

     public HiloListadoDeFacturasPendientesPorConductor(Inicio ini, CEmpleados conductor) {

        this.ini = ini;
        this.conductor = conductor;
         
        
    }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

      //  conductor.setListaDeFacturaPorManifiesto();
      
        // metodoViejo();
    }

    public void metodoViejo() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        boolean existe = false;
        String sql="";
        switch(caso){
            case 1:
                sql= sql = "SELECT * "
                        + "FROM vst_manifiestoDeDistribucion ;";
                
                break;
            case 2:
                 sql = "SELECT * "
                        + "FROM vst_manifiestoDeDistribucion "
                        + "WHERE  estadoManifiesto<='" + estadoManifiesto + "'  order by  numeroManifiesto asc;";
                break;
            
            case 3:
                 sql = "SELECT * "
                        + "FROM vst_manifiestoDeDistribucion "
                        + "WHERE  estadoManifiesto<='" + estadoManifiesto + "' "
                        + "and fechaDistribucion = curdate() "
                        + "order by  numeroManifiesto asc; ";
                
            case 4:
                
              

                break;
                
                
        }
        try {

            List<CManifiestosDeDistribucion> listaDeManifiestosSinDescargar = new ArrayList();

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeManifiestosSinDescargar");

            con = ini.getConnRemota();
            
            CManifiestosDeDistribucion mfto = new CManifiestosDeDistribucion(ini);
            ini.setListaDeManifiestossinDescargar(listaDeManifiestosSinDescargar);

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    mfto = new CManifiestosDeDistribucion(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    mfto.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    mfto.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    mfto.setVehiculo(rst.getString("vehiculo"));
                    mfto.setTipoContrato(rst.getString("tipoContrato"));
                    mfto.setConductor(rst.getString("conductor"));
                    mfto.setNombreConductor(rst.getString("nombreConductor"));
                    mfto.setApellidosConductor(rst.getString("apellidosConductor"));
                    mfto.setDespachador(rst.getString("despachador"));
                    mfto.setNombreDespachador(rst.getString("nombreDespachador"));
                    mfto.setApellidosDespachador(rst.getString("apellidosDespachador"));
                    mfto.setIdCanal(rst.getInt("idCanal"));
                    mfto.setNombreCanal(rst.getString("nombreCanal"));
                    mfto.setIdRuta(rst.getInt("idRuta"));
                    mfto.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    mfto.setTipoRuta(rst.getString("tipoRuta"));
                    mfto.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                    mfto.setKmSalida(rst.getInt("kmSalida"));
                    mfto.setKmEntrada(rst.getInt("kmEntrada"));
                    mfto.setKmRecorrido(rst.getInt("kmRecorrido"));
                    //mfto.setzona(rst.getInt("zona"));
                    //mfto.setregional(rst.getInt("regional"));
                    mfto.setAgencia(rst.getInt("agencia"));
                    mfto.setIsFree(rst.getInt("isFree"));
                    mfto.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    mfto.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    mfto.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                    mfto.setPesoKgManifiesto(rst.getDouble("pesoManifiesto"));
                    mfto.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                    mfto.setActivo(rst.getInt("activo"));
                    mfto.setUsuarioManifiesto(rst.getString("usuarioManifiesto"));
                    mfto.setFechaReal(rst.getString("fechaReal"));
                    mfto.setCantidadPedidos(rst.getInt("cantidadPedidos"));
                    mfto.setTrazabilidad(rst.getInt("trazabilidad"));
                    mfto.setObservaciones(rst.getString("observaciones"));

                    //mfto.setListaDeAuxiliares("" + rst.getInt("numeroManifiesto"));
                    String codigomanifiesto = mfto.codificarManifiesto();

                    mfto.setRutaArchivoDescargueFacturas(ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_FacturasDescargados.txt");
                    mfto.setRutaArchivoDescargueporductosPorFactura(ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_ProductosDescargados.txt");
                    mfto.setRutArchivoRecogidasporManifiesto(ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_RecogidasDescargados.txt");
                    mfto.setRutArchivofacturasporManifiesto(ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + ".txt");
                    mfto.setRutArchivoFacturasSinManifestar(ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_SinManifestar.txt");
                    mfto.setRutaArchivoSoportesDeConsgnaciones(ini.getRutaDeApp() + "tmp/tmp_" + codigomanifiesto + "_consignaciones.txt");

                    if (ini.getListaDeManifiestossinDescargar() != null) {
                        for (CManifiestosDeDistribucion mf : ini.getListaDeManifiestossinDescargar()) {
                            if (mfto.getNumeroManifiesto().equals(mf.getNumeroManifiesto())) {
                                existe = true;
                                break;
                            }

                        }
                        if (!existe) {
                            ini.getListaDeManifiestossinDescargar().add(mfto);
                            System.out.println("Cargando Manifiestos 2 # -> " + mfto.getNumeroManifiesto());
                            existe=false;
                        }
                    }else{
                        ini.setListaDeManifiestossinDescargar(listaDeManifiestosSinDescargar);
                        ini.getListaDeManifiestossinDescargar().add(mfto);
                        System.out.println("Cargando Manifiestos 1 # -> " + mfto.getNumeroManifiesto());
                    }
                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(1);

                }

                rst.close();
                st.close();
                //con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            try {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                rst.close();
                st.close();
               // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeFacturasPendientesPorConductor.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeFacturasPendientesPorConductor.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
