/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FIncidenciasSvC;
import aplicacionlogistica.costumerService.FGestionarIncidencias;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.formularios.FAnularFacturaSinMovimiento;
import aplicacionlogistica.distribucion.objetos.CBitacoraFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloListadoConsultadeFacturaBitacora implements Runnable {

    public static boolean band = false;
    //private int tiempo = 5;
    Inicio ini;
    CFacturas factura=null;
    FConsultarFacturasRemoto fConsultarFacturasRemoto=null;
    FGestionarIncidencias fSolucionarIncidencias=null;
    FIncidenciasSvC fIncidenciasSvC=null;
    FAnularFacturaSinMovimiento fAnularFacturaSinMovimiento = null;
     //ArrayList<CFacturasCamdun> arrFacturasPorManifiesto=null;
     ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;
     Object  ob;
    //ResultSet rst = null;
    

    /**
     * Constructor de clase
     * @param ini
     * @param factura
     */
    public HiloListadoConsultadeFacturaBitacora(Inicio ini,  CFacturas factura) {
        
       this.ini = ini;
       this.factura=factura;
       
    }

   
    
     public HiloListadoConsultadeFacturaBitacora(Inicio ini, FConsultarFacturasRemoto form) {
    
    this.ini = ini;
    this.fConsultarFacturasRemoto=form;
   
       
    }
     
    public HiloListadoConsultadeFacturaBitacora(Inicio ini, FAnularFacturaSinMovimiento form) {
    
    this.ini = ini;
    this.fAnularFacturaSinMovimiento=form;
   
       
    }
     
     public HiloListadoConsultadeFacturaBitacora(Inicio ini, FGestionarIncidencias form) {
    
    this.ini = ini;
    this.fSolucionarIncidencias=form;
    }
     
       public HiloListadoConsultadeFacturaBitacora(Inicio ini, FIncidenciasSvC form) {
    
    this.ini = ini;
    this.fIncidenciasSvC=form;
    }
    
    @Override
    public void run() {
    
          
      if(fConsultarFacturasRemoto != null){
          try {
              llenarFormularioRemoto();
              return;
          } catch (Exception ex) {
              Logger.getLogger(HiloListadoConsultadeFacturaBitacora.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
       
      if(fAnularFacturaSinMovimiento != null){
          try {
              llenarFormularioAnularFactura();
              return;
              
          } catch (Exception ex) {
              Logger.getLogger(HiloListadoConsultadeFacturaBitacora.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      if(fSolucionarIncidencias != null){
          try {
              llenarFormularioIncidencias();
              return;
          } catch (Exception ex) {
              Logger.getLogger(HiloListadoConsultadeFacturaBitacora.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      if(fIncidenciasSvC != null){
          try {
              llenarConsultaFacturaSvcIncidencia();
              return;
          } catch (Exception ex) {
              Logger.getLogger(HiloListadoConsultadeFacturaBitacora.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
       
    }

    private void listaDemovimientosBitacora() {
        ResultSet rst;
        Statement st;
        Connection con ; //= CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoConsultadeFacturaBitacora");
       
        con = ini.getConnRemota();
        
        String sql = " SELECT * "
                + "FROM bitacorafacturas "
                + "WHERE "
                + "bitacorafacturas.numeroFactura='" + factura.getNumeroDeFactura() + "' "
                + "order by fechaIng asc";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    System.out.println(new Date() + "Cargando  ->  bitacora factura N° " + factura.getNumeroDeFactura());
                    
                    CBitacoraFacturas bitacora = new CBitacoraFacturas();
    
                    bitacora.setIdbitacorafacturas(rst.getInt("idbitacorafacturas"));
                    bitacora.setNumeroDocumento(rst.getString("documento"));
                    bitacora.setNumeroFactura(rst.getString("numeroFactura"));
                    bitacora.setObservacion(rst.getString("movimiento"));
                    bitacora.setFechaIng(rst.getString("fechaIng"));
                    
                    
                    listaDeMovimientosBitacora.add(bitacora);
                }
                rst.close();
                st.close();
                //con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        this.factura.setListaDeMovimientosBitacora(listaDeMovimientosBitacora);
    }
    
  
    
   private void llenarFormularioRemoto() throws Exception{ 
        fConsultarFacturasRemoto.lblCirculoDeProgreso.setVisible(true);
        String numeroFactura = fConsultarFacturasRemoto.txtNumeroFactura.getText().trim();

       fConsultarFacturasRemoto.factura = new CFacturas(ini, numeroFactura, true);

       if (fConsultarFacturasRemoto.factura.getNumeroDeFactura() == null) {
           fConsultarFacturasRemoto.lblCirculoDeProgreso.setVisible(false);
           JOptionPane.showMessageDialog(fConsultarFacturasRemoto, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);
           return;
       }


        /*Se alista el insumo para las tablas de la factura*/
        fConsultarFacturasRemoto.factura.setListaDetalleFactura(true);
        fConsultarFacturasRemoto.factura.setListaDeMovimientosBitacora();
        fConsultarFacturasRemoto.factura.setListaDeMovimientosfactura();
        fConsultarFacturasRemoto.factura.setListaDeProductosRechazados();
       
//         int i = 0;
//        for (Vst_FacturasPorManifiesto obj :  fConsultarFacturasRemoto.factura.getListaDeMovimientosfactura()) {
//          
//            if (i == (this.fConsultarFacturasRemoto.listaDeMovimientosEnDistribucion.size() - 1)) {
//                this.fConsultarFacturasRemoto.listaDeProductosRechazados = factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), obj.getNumeroManifiesto());
//            }
//            i++;
//        }

        //this.fConsultarFacturas.limpiarTodo();
        this.fConsultarFacturasRemoto.txtNumeroFactura.setText(this.fConsultarFacturasRemoto.factura.getNumeroDeFactura());
        this.fConsultarFacturasRemoto.txtDireccionDelCliente.setText(this.fConsultarFacturasRemoto.factura.getDireccionDeCliente());
        this.fConsultarFacturasRemoto.txtBarrioCliente.setText(this.fConsultarFacturasRemoto.factura.getBarrio());
        this.fConsultarFacturasRemoto.txtFechaVenta.setText("" + this.fConsultarFacturasRemoto.factura.getFechaDeVenta());
       
        /* nombre del cliente y el código entre parétesis*/
        this.fConsultarFacturasRemoto.txtNombreDelCliente.setText(this.fConsultarFacturasRemoto.factura.getNombreDeCliente() +" (" + this.fConsultarFacturasRemoto.factura.getCodigoDeCliente()+ ")");
        this.fConsultarFacturasRemoto.txtNombreDelVendedor.setText(this.fConsultarFacturasRemoto.factura.getVendedor());
        this.fConsultarFacturasRemoto.txtTelefonoDelCliente.setText(this.fConsultarFacturasRemoto.factura.getTelefonoCliente());
       
        this.fConsultarFacturasRemoto.llenarTablaProductosPorFactura();
        this.fConsultarFacturasRemoto.llenarTablabitacora();
        this.fConsultarFacturasRemoto.llenarTablaDistribucion();  
        this.fConsultarFacturasRemoto.llenarTablaProductosRechazados();
                
        this.fConsultarFacturasRemoto.completado=false;
        
        this.fConsultarFacturasRemoto.lblValorTotalFactura.setText(this.fConsultarFacturasRemoto.nf.format(fConsultarFacturasRemoto.factura.getValorTotalFactura()));
        
        this.fConsultarFacturasRemoto.lblValorCobrado.setText(this.fConsultarFacturasRemoto.nf.format(fConsultarFacturasRemoto.factura.getValorTotalRecaudado()));
      
        this.fConsultarFacturasRemoto.lblCirculoDeProgreso.setVisible(false);
        
        this.fConsultarFacturasRemoto.txtNumeroFactura.setEditable(false);
        this.fConsultarFacturasRemoto.txtNumeroFactura.setEnabled(false);
        
        this.fConsultarFacturasRemoto.jBtnNuevo.requestFocus();
   }
   
     private void llenarFormularioAnularFactura() throws Exception{
        
        /*Se alista el insumo para las tablas de la factura*/
        fAnularFacturaSinMovimiento.factura.setListaDetalleFactura(true);
        fAnularFacturaSinMovimiento.factura.setListaDeMovimientosBitacora();
        fAnularFacturaSinMovimiento.factura.setListaDeMovimientosfactura();
        fAnularFacturaSinMovimiento.factura.setListaDeProductosRechazados();
       
//         int i = 0;
//        for (Vst_FacturasPorManifiesto obj :  fConsultarFacturasRemoto.factura.getListaDeMovimientosfactura()) {
//          
//            if (i == (this.fConsultarFacturasRemoto.listaDeMovimientosEnDistribucion.size() - 1)) {
//                this.fConsultarFacturasRemoto.listaDeProductosRechazados = factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), obj.getNumeroManifiesto());
//            }
//            i++;
//        }

        //this.fConsultarFacturas.limpiarTodo();
        this.fAnularFacturaSinMovimiento.txtNumeroFactura.setText(this.fAnularFacturaSinMovimiento.factura.getNumeroFactura());
        this.fAnularFacturaSinMovimiento.txtDireccionDelCliente.setText(this.fAnularFacturaSinMovimiento.factura.getDireccionDeCliente());
        this.fAnularFacturaSinMovimiento.txtBarrioCliente.setText(this.fAnularFacturaSinMovimiento.factura.getBarrio());
        this.fAnularFacturaSinMovimiento.txtFechaVenta.setText("" + this.fAnularFacturaSinMovimiento.factura.getFechaDeVenta());
       
        /* nombre del cliente y el código entre parétesis*/
        this.fAnularFacturaSinMovimiento.txtNombreDelCliente.setText(this.fAnularFacturaSinMovimiento.factura.getNombreDeCliente() +" (" + this.fAnularFacturaSinMovimiento.factura.getIdCliente() + ")");
        this.fAnularFacturaSinMovimiento.txtNombreDelVendedor.setText(this.fAnularFacturaSinMovimiento.factura.getVendedor());
        this.fAnularFacturaSinMovimiento.txtTelefonoDelCliente.setText(this.fAnularFacturaSinMovimiento.factura.getTelefonoCliente());
       
        this.fAnularFacturaSinMovimiento.llenarTablaProductosPorFactura();
        this.fAnularFacturaSinMovimiento.llenarTablabitacora();
        this.fAnularFacturaSinMovimiento.llenarTablaDistribucion();  
        this.fAnularFacturaSinMovimiento.llenarTablaProductosRechazados();
                
        this.fAnularFacturaSinMovimiento.completado=false;
        
        //this.fAnularFacturaSinMovimiento.lblValorRecaudado.setText(this.fAnularFacturaSinMovimiento.nf.format(fAnularFacturaSinMovimiento.factura.getValorRecaudado()));
         this.fConsultarFacturasRemoto.lblValorCobrado.setText(this.fConsultarFacturasRemoto.nf.format(fConsultarFacturasRemoto.factura.getValorTotalRecaudado()));

        this.fAnularFacturaSinMovimiento.lblCirculoDeProgreso.setVisible(false);
        
        this.fAnularFacturaSinMovimiento.txtNumeroFactura.setEditable(false);
        this.fAnularFacturaSinMovimiento.txtNumeroFactura.setEnabled(false);
        
         if ( fAnularFacturaSinMovimiento.factura.getSalidasDistribucion() < 1 || (
                fAnularFacturaSinMovimiento.factura.getEstadoFactura()==6 || fAnularFacturaSinMovimiento.factura.getEstadoFactura()==7)) {
                        fAnularFacturaSinMovimiento.pnlAnularFactura.setEnabled(true);
                        fAnularFacturaSinMovimiento.txtObservaciones.setEnabled(true);
                        fAnularFacturaSinMovimiento.jBtnCancelar.setEnabled(true);
                        fAnularFacturaSinMovimiento.jBtnAnular.setEnabled(true);
                    }
        
        this.fAnularFacturaSinMovimiento.jBtnNuevo.requestFocus();
   }
   
   private void llenarFormularioIncidencias() throws Exception{
       
//        
//        
//        //this.factura = new CFacturas(ini,this.fSolucionarIncidencias.txtNumeroFactura.getText().trim(),true);
//        this.factura = this.fSolucionarIncidencias.factura;
//        this.fSolucionarIncidencias.lblCirculoDeProgreso.setVisible(false);
//        this.fSolucionarIncidencias.vistafac = new Vst_Factura();
//        this.fSolucionarIncidencias.listaProductosPorFactura = new ArrayList();
//        this.fSolucionarIncidencias.listaDeMovimientosEnDistribucion = new ArrayList();
//        this.fSolucionarIncidencias.listaDeMovimientosBitacora=new ArrayList();
//        
//        this.factura.setVistaFactura(true);
//        this.factura.setVstProductosPorFactura(true);
//        this.factura.setListaDeMovimientosfactura();
//        //this.factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), manifiesto);
//        this.fSolucionarIncidencias.vistafac=this.factura.getVistaFactura();
//        //this.fSolucionarIncidencias.lblValorRecaudado.setText(this.fSolucionarIncidencias.nf.format(factura.getValorRecaudado()));
//                
//        this.fSolucionarIncidencias.listaProductosPorFactura=this.factura.getVstProductosPorFactura();
//        
//        this.fSolucionarIncidencias.listaDeMovimientosEnDistribucion=this.factura.getListaDeMovimientosfactura();
//        this.fSolucionarIncidencias.listaDeProductosRechazados = new ArrayList<>();
//         
//        int i = 0;
//        for (Vst_FacturasPorManifiesto obj : this.fSolucionarIncidencias.listaDeMovimientosEnDistribucion) {
//          
//            if (i == (this.fSolucionarIncidencias.listaDeMovimientosEnDistribucion.size() - 1)) {
//                this.fSolucionarIncidencias.listaDeProductosRechazados = factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), obj.getNumeroManifiesto());
//            }
//            i++;
//        }
//
//
//        this.fSolucionarIncidencias.listaDeMovimientosBitacora=this.factura.getListaDeMovimientosBitacora(factura.getNumeroFactura());
//        
//        
//        //this.fConsultarFacturas.limpiarTodo();
//        this.fSolucionarIncidencias.txtNumeroFactura.setText(this.fSolucionarIncidencias.vistafac.getNumeroFactura());
//        this.fSolucionarIncidencias.txtDireccionDelCliente.setText(this.fSolucionarIncidencias.vistafac.getDireccionDeCliente());
//        this.fSolucionarIncidencias.txtBarrioCliente.setText(this.fSolucionarIncidencias.vistafac.getBarrio());
//        this.fSolucionarIncidencias.txtFechaVenta.setText("" + this.fSolucionarIncidencias.vistafac.getFechaDeVenta());
//       
//        /* nombre del cliente y el código entre parétesis*/
//        this.fSolucionarIncidencias.txtNombreDelCliente.setText(this.fSolucionarIncidencias.vistafac.getNombreDeCliente() +" (" + this.fSolucionarIncidencias.vistafac.getCliente() + ")");
//        this.fSolucionarIncidencias.txtNombreDelVendedor.setText(this.fSolucionarIncidencias.vistafac.getVendedor());
//        this.fSolucionarIncidencias.txtTelefonoDelCliente.setText(this.fSolucionarIncidencias.vistafac.getTelefonoCliente());
//       
//        this.fSolucionarIncidencias.llenarTablaProductosPorFactura();
//        //this.fSolucionarIncidencias.llenarTablaProductosPorFacturaRechazados();
//        this.fSolucionarIncidencias.llenarTablabitacora();
//        this.fSolucionarIncidencias.llenarTablaProductosRechazados();
//        this.fSolucionarIncidencias.llenarTablaDistribucion();    
//      
//        this.fSolucionarIncidencias.completado=false;
//        
//        
//        this.fSolucionarIncidencias.lblCirculoDeProgreso.setVisible(false);
//        //this.fSolucionarIncidencias.jTabbedPane2.setIconAt(1, new javax.swing.ImageIcon(""));
//        this.fSolucionarIncidencias.txtNumeroFactura.requestFocus();
//        this.fSolucionarIncidencias.txtNumeroFactura.requestFocus();
   }
   
   
    private void llenarConsultaFacturaSvcIncidencia() throws Exception{
       
        
        
//        //this.factura = new CFacturas(ini,this.fSolucionarIncidencias.txtNumeroFactura.getText().trim(),true);
//        this.factura = this.fIncidenciasSvC.factura;
//        this.fIncidenciasSvC.lblCirculoDeProgreso.setVisible(false);
//        
//        this.fIncidenciasSvC.vistafac = new Vst_Factura();
//        this.fIncidenciasSvC.listaProductosPorFactura = new ArrayList();
//        this.fIncidenciasSvC.listaDeMovimientosEnDistribucion = new ArrayList();
//        this.fIncidenciasSvC.listaDeMovimientosBitacora=new ArrayList();
//        
//        this.factura.setVistaFactura(true);
//        this.factura.setVstProductosPorFactura(true);
//        this.factura.setListaDeMovimientosfactura();
//        //this.factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), manifiesto);
//        this.fIncidenciasSvC.vistafac=this.factura.getVistaFactura();
//        //this.fIncidenciasSvC.lblValorRecaudado.setText(this.fIncidenciasSvC.nf.format(factura.getValorRecaudado()));
//                
//        this.fIncidenciasSvC.listaProductosPorFactura=this.factura.getVstProductosPorFactura();
//        
//        this.fIncidenciasSvC.listaDeMovimientosEnDistribucion=this.factura.getListaDeMovimientosfactura();
//        this.fIncidenciasSvC.listaDeProductosRechazados = new ArrayList<>();
//         
//        int i = 0;
//        for (Vst_FacturasPorManifiesto obj : this.fIncidenciasSvC.listaDeMovimientosEnDistribucion) {
//          
//            if (i == (this.fIncidenciasSvC.listaDeMovimientosEnDistribucion.size() - 1)) {
//                this.fIncidenciasSvC.listaDeProductosRechazados = factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), obj.getNumeroManifiesto());
//            }
//            i++;
//        }
//
//
//        this.fIncidenciasSvC.listaDeMovimientosBitacora=this.factura.getListaDeMovimientosBitacora(factura.getNumeroFactura());
//        
//        
//        //this.fConsultarFacturas.limpiarTodo();
//        this.fIncidenciasSvC.txtNumeroFactura.setText(this.fIncidenciasSvC.vistafac.getNumeroFactura());
//        this.fIncidenciasSvC.txtDireccionDelCliente.setText(this.fIncidenciasSvC.vistafac.getDireccionDeCliente());
//        this.fIncidenciasSvC.txtBarrioCliente.setText(this.fIncidenciasSvC.vistafac.getBarrio());
//        this.fIncidenciasSvC.txtFechaVenta.setText("" + this.fIncidenciasSvC.vistafac.getFechaDeVenta());
//       
//        /* nombre del cliente y el código entre parétesis*/
//        this.fIncidenciasSvC.txtNombreDelCliente.setText(this.fIncidenciasSvC.vistafac.getNombreDeCliente() +" (" + this.fIncidenciasSvC.vistafac.getCliente() + ")");
//        this.fIncidenciasSvC.txtNombreDelVendedor.setText(this.fIncidenciasSvC.vistafac.getVendedor());
//        this.fIncidenciasSvC.txtTelefonoDelCliente.setText(this.fIncidenciasSvC.vistafac.getTelefonoCliente());
//       
//        this.fIncidenciasSvC.llenarTablaProductosPorFactura();
//        this.fIncidenciasSvC.llenarTablabitacora();
//        this.fIncidenciasSvC.llenarTablaProductosRechazados();
//        this.fIncidenciasSvC.llenarTablaDistribucion();    
//      
//        this.fIncidenciasSvC.completado=false;
//        
//        
//        this.fIncidenciasSvC.lblCirculoDeProgreso.setVisible(false);
//          this.fIncidenciasSvC.jTabbedPane2.setIconAt(1, null);
//        //this.fIncidenciasSvC.jTabbedPane2.setIconAt(1, new javax.swing.ImageIcon(""));
//        this.fIncidenciasSvC.txtNumeroFactura.requestFocus();
//        this.fIncidenciasSvC.txtNumeroFactura.requestFocus();
   }
}
