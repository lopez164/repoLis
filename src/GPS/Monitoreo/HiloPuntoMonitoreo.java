/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.Monitoreo;

import GPS.PuntosGps.PuntosGps;
import GPS.PuntosGps.RoutePainter;
import GPS.PuntosGps.Carro;
import GPS.PuntosGps.PuntoDeRecorrido;
import GPS.wayPoints.MyWayPoint;
import GPS.wayPoints.WayPointRenderer;
import aplicacionlogistica.configuracion.Inicio;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.PolylineOptions;
import eu.bitm.NominatimReverseGeocoding.Address;
import eu.bitm.NominatimReverseGeocoding.NominatimReverseGeocodingJAPI;
import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import mtto.vehiculos.CCarros;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import org.tritonus.share.ArraySet;

/**
 *
 * @author Usuario
 */
public class HiloPuntoMonitoreo implements Runnable {

    private PuntosMonitoreo mapa;
    private String value;//retardo en milisegundos
    private String placa;
    private Carro carro;

    List<ImageIcon> listaDeIconos;
    String listaDePlacas;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param mapa
     * @param value
     */
    public HiloPuntoMonitoreo(PuntosMonitoreo mapa, String value) {
        this.ini = mapa.ini;
        this.mapa = mapa;
        this.value = value;
    }

    /**
     * Constructor de clase
     *
     * @param mapa
     * @param value
     */
    public HiloPuntoMonitoreo(PuntosMonitoreo mapa, String value, int x) {
        this.mapa = mapa;
        this.value = value;
        this.ini = mapa.ini;

    }

    /**
     * Constructor de clase
     *
     * @param mapa
     * @param value
     */
    public HiloPuntoMonitoreo(PuntosMonitoreo mapa, String value, String placa) {
        this.mapa = mapa;
        this.value = value;
        this.placa = placa;
        this.ini = mapa.ini;

    }

    /**
     * Constructor de clase
     *
     * @param mapa
     * @param value
     * @param carro
     */
    public HiloPuntoMonitoreo(PuntosMonitoreo mapa, String value, Carro carro) {
        this.mapa = mapa;
        this.value = value;
        this.carro = carro;
        this.ini = mapa.ini;

    }

    @Override
    public void run() {
        switch (value) {
            case "setListaDeIconos":
                setListaDeIconos();
                break;

            case "setListaDeCarrosConGps": 
                try {
                setListaDeCarrosConGps();

                asignarPuntosCarrosConGps();
                seePuntosCarrosConGps();

            } catch (InterruptedException ex) {
                Logger.getLogger(HiloPuntoMonitoreo.class.getName()).log(Level.SEVERE, null, ex);
            }

            break;

            case "actualizarUltimaPosicion":

                if (!mapa.ocupado) {
                    actualizarUltimaPosicion();
                }

                break;

            case "asgnarCetroDelMapa":

                asgnarCetroDelMapa();
                break;

            case "cargarClientesPorCarro":
                cargarClientesPorCarro();
                break;

            case "putWayPointsClientes":

                if (mapa.listaWpClientes != null) {

                    delListaDeClientes();
                    // delListaDePuntosRecorridos();

                }

                mapa.placa = mapa.jListaDeCarros.getSelectedValue().toString();

                for (Carro car : mapa.listadeCarros) {

                    if (car.getPlaca().equals(mapa.placa)) {
                        if (car.getWpClientes() == null) {
                            car.setWpClientes(car.getImageIcon());
                        }
                        mapa.carro = new Carro(mapa.getConnServidorRuta(), mapa.getConnServidorGps());
                        mapa.carro.setPlaca(car.getPlaca());
                        mapa.carro.setConductor(car.getConductor());
                        mapa.carro.setWpClientes(car.getWpClientes());
                        mapa.carro.setIcon(car.getImageIcon());

                        break;
                    }

                }
//                asignarPuntoGeoClientes();
                seeListaDeClientes();
                mapa.chkRecorrido.setEnabled(true);
                break;

            case "mostrarRecorrido":

                mapa.ocupado = true;
                if (mapa.listaWpPuntosRecorridos != null) {
                   borrarRecorrido();
                    //borrarPuntosRecorridos();
                   // borrarPolyLinea();
                    mapa.carro = new Carro(mapa.ini);
                }

                for (Carro car : mapa.listadeDispositivosGps) {

                    if (car.getPlaca().equals(placa)) {

                        mapa.carro = car;
                        /* mapa.carro = new Carro(mapa.getConnServidorRuta(), mapa.getConnServidorGps());
                        mapa.carro.setPlaca(car.getPlaca());
                        mapa.carro.setConductor(car.getConductor());
                        mapa.carro.setIcon(car.getImageIcon()); */
                        mapa.carro.setFechaDistribucion(mapa.fechaDistribucion);
                        mapa.carro.setFechaDeInicio(mapa.fechaInicial);
                        mapa.carro.setFechaFinal(mapa.fechaFinal);

                        if (!mapa.carro.setListaDePuntosDeRecorrido()) {

                            JOptionPane.showMessageDialog(mapa, "Vehiculo no tiene Gps", "Sin Gps", JOptionPane.HEIGHT);

                            return;
                        }

                        mapa.listadeLineas = new ArrayList<>();
                        mapa.listadeLineas = mapa.carro.getListaDePuntosDeRecorrido();
                        break;
                    }

                }
                 {
                    try {


                        /*crea los waypoint gps*/
                        asignarPuntosRecorridos();

                    } catch (InterruptedException ex) {
                        Logger.getLogger(HiloPuntoMonitoreo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //seeListaDePuntosRecorridos();
                //seeLineDePuntosRecorridos2();
                dibujarLineasDePuntosRecorridos();
                mapa.ocupado = false;

                //dibujarRuta();            
                break;

            case "borrarRecorrido":
                borrarRecorrido();
                break;

            case "borrarWayPointsPuntosRecorridos":
                borrarPuntosRecorridos();
                //  putWayPoints();
                break;

            case "delListaDeClientes":
                delListaDeClientes();
                // mapa.carro.setListaDePuntosDeRecorrido();
                //armartWayPointsPuntosRecorridos();
                // verWayPointsPuntosRecorridos();
                //borrarWayPointsPuntosRecorridos();
                //delListaDeClientes(mapa.carro);
                break;

            case "delPolyLineas":
               ;

                //delMarcadoresRecorrido();
                //delPolyLineas();
                //  setListaDeWayPoints();
                //putWayPoints();
                break;

            case "borrarPuntosRecorridos":

                borrarPuntosRecorridos();
                borrarPolyLinea();
                // setListaDeWayPoints();
                //putWayPoints();
                break;

            case "delListaDeNegocios":
                //delListaDeNegocios();
//                  //  delListaDeClientes();
                //delMarcadoresRecorrido();
                //delPolyLineas();
                // setListaDeWayPoints();
                //putWayPoints();
                break;

            case "listaDeNegociosPorCarro":

//                    if ( mapa.carro.getListadeClientes() == null ||  mapa.carro.getListadeClientes().size() == 0) {
//                         mapa.carro.setListadeClientes( mapa.carro.getIcon());
//                    }
                break;

        }

    }

    private synchronized void actualizarUltimaPosicion() {

        RoutePainter routePainter = null;
        List<GeoPosition> track = new ArrayList<>();
        CompoundPainter<JXMapViewer> painters = new CompoundPainter<JXMapViewer>();

        /**
         * BOrra componente boton
         */
        Component[] componentes = mapa.jXMapViewer1.getComponents();

        for (int i = 0; i < componentes.length; i++) {

            if (componentes[i] instanceof JButton) {
                JButton btn = (JButton) componentes[i];

                if (btn.getText().equals(carro.getPlaca())) {
                    mapa.jXMapViewer1.remove(componentes[i]);
                    break;
                }

            }
        }

        carro.setUltimaUbicacion();
        // mapa.listaWpCarros.add(carro.getPuntoCarro());
        mapa.listaDeTodosWP.add(carro.getPuntoCarro());

        WaypointPainter<MyWayPoint> wpCarros = new WayPointRenderer();

        wpCarros.setWaypoints(mapa.listaDeTodosWP);

        painters.addPainter(wpCarros);

        mapa.jXMapViewer1.setOverlayPainter(painters);

        /**
         * *******************************************
         *
         */
        if (mapa.chkRecorrido.isSelected()) {

            if (mapa.listadeLineas.size() > 0) {
                /*Dibuja l lineaas del recorrido */
                for (PuntoDeRecorrido punto : mapa.listadeLineas) {
                    track.add(punto.getPath());

                }
                System.out.println("Dato no vacio " + carro.getPlaca() + " tamaño del array : " + carro.getListaDePuntosDeRecorrido().size() + "\n");

                routePainter = new RoutePainter(track);

                painters.addPainter(routePainter);

            } else {
                System.out.println("Dato viene vacio " + carro.getPlaca() + "\n");
            }
        } else {
            //carro.borrarPuntosDeRecorrido();
        }
        /**
         * *************************************************
         */

        mapa.jXMapViewer1.add(carro.getPuntoCarro().getButton());

        mapa.jXMapViewer1.setOverlayPainter(painters);

        mapa.centro = new GeoPosition(carro.getUltimaUbicacion().getLongitude(), carro.getUltimaUbicacion().getLatitude());

        mapa.repaint();
    }

    private synchronized void actualizarUltimaPosicion(Carro car) {

        this.carro = car;
        
        RoutePainter routePainter = null;
        List<GeoPosition> track = new ArrayList<>();
        CompoundPainter<JXMapViewer> painters = new CompoundPainter<JXMapViewer>();

        /**
         * BOrra componente boton
         */
        Component[] componentes = mapa.jXMapViewer1.getComponents();

        for (int i = 0; i < componentes.length; i++) {

            if (componentes[i] instanceof JButton) {
                JButton btn = (JButton) componentes[i];

                if (btn.getText().equals(carro.getPlaca())) {
                    mapa.jXMapViewer1.remove(componentes[i]);
                    break;
                }

            }
        }

        //carro.setUltimaUbicacion();
        // mapa.listaWpCarros.add(carro.getPuntoCarro());
        mapa.listaDeTodosWP.add(carro.getPuntoCarro());

        WaypointPainter<MyWayPoint> wpCarros = new WayPointRenderer();

        wpCarros.setWaypoints(mapa.listaDeTodosWP);

        painters.addPainter(wpCarros);

        mapa.jXMapViewer1.setOverlayPainter(painters);

        /**
         * *******************************************
         *
         */
        if (mapa.chkRecorrido.isSelected()) {

            if (mapa.listadeLineas.size() > 0) {
                /*Dibuja l lineaas del recorrido */
                for (PuntoDeRecorrido punto : mapa.listadeLineas) {
                    track.add(punto.getPath());

                }
                System.out.println("Dato no vacio " + carro.getPlaca() + " tamaño del array : " + carro.getListaDePuntosDeRecorrido().size() + "\n");

                routePainter = new RoutePainter(track);

                painters.addPainter(routePainter);

            } else {
                System.out.println("Dato viene vacio " + carro.getPlaca() + "\n");
            }
        } else {
            //carro.borrarPuntosDeRecorrido();
        }
        /**
         * *************************************************
         */

        mapa.jXMapViewer1.add(carro.getPuntoCarro().getButton());

        mapa.jXMapViewer1.setOverlayPainter(painters);

        mapa.centro = new GeoPosition(carro.getUltimaUbicacion().getLongitude(), carro.getUltimaUbicacion().getLatitude());

        mapa.repaint();
    }

    private void asignarPuntosRecorridos() throws InterruptedException {

        mapa.listaWpPuntosRecorridos = new HashSet<>();
        int i = 0;
        for (final PuntoDeRecorrido punto : mapa.carro.getListaDePuntosDeRecorrido()) {

            GeoPosition a = punto.getPath();

            DecimalFormat formato = new DecimalFormat("#,###.##");
            String info = "<html><body>"
                    + "<table>"
                    + "<tr>"
                    + "<td>Id Movimiento : </td>"
                    + "<td>" + punto.getId() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Placa Vehiculo : </td>"
                    + "<td>" + punto.getPlaca() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Conductor : </td>"
                    + "<td>" + punto.getConductor() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Localizacion : </td>"
                    + "<td>" + punto.getPath().getLatitude() + "," + punto.getPath().getLongitude() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Orientacion : </td>"
                    + "<td>" + punto.getCourse() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Hora : </td>"
                    + "<td>" + punto.getServertime() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Velocidad : </td>"
                    + "<td>" + formato.format(punto.getSpeed()) + " Km/h </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Distancia : </td>"
                    + "<td>" + formato.format(punto.getDistancia()) + " mts </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Total Recorrido : </td>"
                    + "<td>" + formato.format(punto.getDistanciaRecorrida() / 1000) + " Km </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Odometro  : </td>"
                    + "<td>" + formato.format(punto.getKilometrajeFinal()) + " Km </td>"
                    + "</tr>"
                    + "</table>"
                    + "</body></html>";

            final MyWayPoint marker = new MyWayPoint(
                    "WP-" + punto.getId(),
                    punto.getPath(),
                    punto.getIcon(),
                    info);

            mapa.listaWpPuntosRecorridos.add(marker);

            Thread.sleep(1);

        }

    }

    private void seeLineDePuntosRecorridos() {
        // Create a track from the geo-positions
        // List<GeoPosition> track = Arrays.asList(mapa.wpPuntosRecorridos);
        List<GeoPosition> track = new ArrayList<>();

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            try {

                track.add(d.getPosition());
                Thread.sleep(2);

            } catch (InterruptedException ex) {
                Logger.getLogger(HiloPuntoMonitoreo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        RoutePainter routePainter = new RoutePainter(track);
        // Set the focus
        mapa.jXMapViewer1.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<MyWayPoint> waypointPainter = new WaypointPainter<MyWayPoint>();
        waypointPainter.setWaypoints(mapa.listaWpPuntosRecorridos);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapa.jXMapViewer1.setOverlayPainter(painter);

    }

    private void seeLineDePuntosRecorridos2() {
        // Create a track from the geo-positions
        // List<GeoPosition> track = Arrays.asList(mapa.wpPuntosRecorridos);
        List<GeoPosition> track = new ArrayList<>();

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            try {

                track.add(d.getPosition());

                Thread.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloPuntoMonitoreo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void seeListaDeRecorrido2() {

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            try {
                mapa.jXMapViewer1.add(d.getButton());

                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloPuntoMonitoreo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void borrarPuntosRecorridos() {

        /**
         * BOrra componente
         */
        Set<MyWayPoint> listanueva = new ArraySet<>();

        Component[] componentes = mapa.jXMapViewer1.getComponents();

        for (int i = 0; i < componentes.length; i++) {

            if (componentes[i] instanceof JButton) {
                JButton btn = (JButton) componentes[i];

                if (btn.getText().equals("")) {
                    mapa.jXMapViewer1.remove(componentes[i]);

                }

            }
        }

        for (MyWayPoint wp : mapa.listaWpClientes) {
            if (wp.getCodigoCliente() != null) {
                if (!wp.getCodigoCliente().contains("WP")) {
                    listanueva.add(wp);

                }
            }

        }
        mapa.listaWpClientes = listanueva;

    }

    private void borrarPolyLinea() {
//
//        for (Polyline pol : mapa.listadeLineas) {
//            pol.setVisible(false);
//            System.out.println("Borrando linea");
//        }

    }

    public void seeListaDeClientes() {

        WaypointPainter<MyWayPoint> waypointPainter = new WayPointRenderer();
        waypointPainter.setWaypoints(mapa.listaWpClientes);
        mapa.painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(mapa.painters);
        mapa.jXMapViewer1.setOverlayPainter(painter);

        for (MyWayPoint d : mapa.listaWpClientes) {
            mapa.jXMapViewer1.add(d.getButton());

        }
        mapa.jXMapViewer1.setAddressLocation(mapa.centro);

    }

    public void seeListaDePuntosRecorridos() {

        WaypointPainter<MyWayPoint> wp = new WayPointRenderer();
        wp.setWaypoints(mapa.listaWpPuntosRecorridos);
        mapa.painters.add(wp);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(mapa.painters);
        mapa.jXMapViewer1.setOverlayPainter(painter);

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            mapa.jXMapViewer1.add(d.getButton());

        }
        //   mapa.jXMapViewer1.setAddressLocation(mapa.carro.getUltimaUbicacion());

    }

    public synchronized void dibujarLineasDePuntosRecorridos() {
        List<GeoPosition> track = new ArrayList<>();

        CompoundPainter<JXMapViewer> painters = new CompoundPainter<JXMapViewer>(mapa.painters);

        WaypointPainter<MyWayPoint> wpPCarros = new WayPointRenderer();
        wpPCarros.setWaypoints(mapa.listaDeTodosWP);

        mapa.listaDeTodosWP = new HashSet<>();

        // for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
        for (MyWayPoint d : mapa.listaWpCarros) {

            mapa.listaDeTodosWP.add(d);
            System.out.println("placa id : " + d.getButton().getText());

        }

        mapa.jXMapViewer1.setOverlayPainter(painters);

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {

            mapa.listaDeTodosWP.add(d);

            System.out.println("codigo id : " + d.getCodigoCliente());

        }

        for (MyWayPoint d : mapa.listaDeTodosWP) {
            mapa.jXMapViewer1.add(d.getButton());
            System.out.println("codigo id : " + d.getCodigoCliente());

        }

        wpPCarros.setWaypoints(mapa.listaDeTodosWP);
        painters.addPainter(wpPCarros);
        mapa.jXMapViewer1.setOverlayPainter(painters);


        /*Dibuja l lineaas del recorrido */
        for (PuntoDeRecorrido punto : mapa.carro.getListaDePuntosDeRecorrido()) {
            track.add(punto.getPath());

        }
        RoutePainter routePainter = new RoutePainter(track);

        // Set the focus
        mapa.jXMapViewer1.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);
        painters.addPainter(routePainter);

        // mapa.jXMapViewer1.add(mapa.carro.getPuntoCarro().getButton());
        mapa.jXMapViewer1.setOverlayPainter(painters);

        mapa.jXMapViewer1.setAddressLocation(mapa.carro.getUltimaUbicacion());
        mapa.jXMapViewer1.setZoom(5);

    }

    public void delListaDeClientes() {

        if (mapa.listaWpClientes != null || mapa.listaWpClientes.size() > 0) {
            for (MyWayPoint m : mapa.listaWpClientes) {
                mapa.jXMapViewer1.remove(m.getButton());
                System.out.println("Borrando marcador Cliente " + mapa.carro.getPlaca());
            }
            mapa.listaWpClientes.clear();
            //mapa.initWayPoints();
        }

    }

    /*
    public void delListaDePuntosRecorridos() {

        if (mapa.carro.getListaDeMarcadoresRecorrido() != null) {
            for (MyWayPoint m : mapa.carro.getListaDeMarcadoresRecorrido()) {
                mapa.jXMapViewer1.remove(m.getButton());
                System.out.println("Borrando marcador punto Recorridos " + mapa.carro.getPlaca());
            }
            mapa.carro.getListaDeMarcadoresRecorrido().clear();
            //mapa.initWayPoints();
        }

    }
**/
    private void setListaDeIconos() {

        listaDeIconos = new ArrayList<>();

        ImageIcon iconNegocios = new ImageIcon();

        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpBlack_2-remove.png"), "png");
        listaDeIconos.add(0, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpBlue_2-remove.png"), "png");
        listaDeIconos.add(1, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpBlue-remove.png"), "png");
        listaDeIconos.add(2, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpFucsia-remove.png"), "png");
        listaDeIconos.add(3, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpGray_2-remove.png"), "png");
        listaDeIconos.add(4, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpGreen2-remove.png"), "png");
        listaDeIconos.add(5, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpGreen_2-remove.png"), "png");
        listaDeIconos.add(6, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpGreen_22-remove.png"), "png");
        listaDeIconos.add(7, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpGreen-remove.png"), "png");
        listaDeIconos.add(8, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpMarron_2-remove.png"), "png");
        listaDeIconos.add(9, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpMorado_2-remove.png"), "png");
        listaDeIconos.add(10, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpOrange_2-remove.png"), "png");
        listaDeIconos.add(11, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpPink_2-remove.png"), "png");
        listaDeIconos.add(12, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpRed_2-remove.png"), "png");
        listaDeIconos.add(13, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpRed-remove.png"), "png");
        listaDeIconos.add(14, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpYellow_2-remove.png"), "png");
        listaDeIconos.add(15, iconNegocios);

        iconNegocios = new ImageIcon();
        iconNegocios = new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/wpYellow-remove.png"), "png");
        listaDeIconos.add(16, iconNegocios);

        mapa.setListaDeIconos(listaDeIconos);

    }

    /*Carga los clientes del carro seleccionado del Jlist*/
    private void cargarClientesPorCarro() {

        for (Carro car : mapa.listadeCarros) {
            if (car.getPlaca().equals(placa)) {
//                if (car.getListadeClientes() == null) {
//                    car.setListadeClientes(car.getImageIcon());
//                }

                break;
            }

            System.out.println("Cargando clientes placa " + car.getPlaca());

        }

    }

    private void setListaDeCarrosConGps() {
        listaDePlacas = "('";
       
       // ini.setListaDeCarrosConGps();

        for (CCarros car : ini.getListaDeCarrosConGps()) {
           
            listaDePlacas += car.getPlaca() + "','";
            
        }
        listaDePlacas = listaDePlacas.substring(0, listaDePlacas.length() - 2).concat(")");

        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        String sql = "";
        //String fechaDistribucion = "2022-06-21";

        try {
            sql = "SELECT p.id, p.protocol, p.deviceid,d.name,d.positionid,d.attributes,d.lastupdate,"
                    + "JSON_EXTRACT(d.attributes,'$.Kilometraje') as kilometrajeInical, "
                    + "ROUND((JSON_EXTRACT(p.attributes,'$.totalDistance')/1000),2) as distanciaTotal,"
                    //+ "DATE_SUB(p.servertime, INTERVAL 5 HOUR) as servertime,"
                    + "p.servertime as servertime,"
                    // + " p.fechaDeInicio, "
                    + "p.devicetime, p.fixtime, p.valid, p.latitude, "
                    + "p.longitude, p.altitude, p.speed, p.course, p.address, p.attributes, p.accuracy, p.network "
                    + "FROM tc_positions p "
                    + "join tc_devices d on d.id = p.deviceid "
                    + "where "
                    + "d.positionid = p.id  and "
                    + "d.name in" + listaDePlacas + " "
                    + "order by d.name;";

            con = ini.getConnGPS();

            if (con != null) {
                mapa.listadeDispositivosGps = new ArrayList<>();
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    Carro car = new Carro(ini);
                    car.setIdMovimiento(rst.getInt("id"));
                    car.setSpeed(rst.getDouble("speed"));
                    car.setPlaca(rst.getString("name"));
                    car.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/truck_32x32.png")));
                    car.setFechaDeInicio(rst.getString("servertime"));
                    car.setFechaFinal("");
                    car.setConductor("");
                    car.setFechaDistribucion(mapa.fechaDistribucion);
                    car.setUltimaUbicacion(new GeoPosition(rst.getDouble("latitude"), rst.getDouble("longitude")));
                    car.setOdometro(rst.getDouble("kilometrajeInical") + rst.getDouble("distanciaTotal"));
                    car.setCourse(rst.getDouble("course"));

                    mapa.listadeDispositivosGps.add(car);

                }

                rst.close();
                st.close();
                // 
                if (mapa.listadeDispositivosGps.isEmpty()) {
                    JOptionPane.showMessageDialog(mapa, "NO Hay vehiculos asignados con GPS", "No hay Vehiculos", JOptionPane.WARNING_MESSAGE);

                    return;
                }

            }
            /* Se asigna el conductor a cada dispositivo GPS  **/
            for (Carro carro : mapa.listadeDispositivosGps) {

                for (CCarros car : ini.getListaDeCarrosConGps()) {
                    if (carro.getPlaca().equals(car.getPlaca())) {
                        carro.setConductor(car.getConductor());
                        break;
                    }
                }
            }

            /* SE asignan los timer para carar los puntos de los carros en el mapa */
            mapa.setTemporizadoresDeLOsCArros();

        } catch (SQLException ex) {
            Logger.getLogger(PuntosGps.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void asignarPuntosCarrosConGps() throws InterruptedException {
//        LatLng puntoFinal = null;
//
//        PolylineOptions lineOptions = new PolylineOptions();
//        lineOptions.setGeodesic(true);
//        lineOptions.setStrokeColor("#4480CC");
//        lineOptions.setStrokeOpacity(1);
//        lineOptions.setStrokeWeight(2);

        if (mapa.listaWpCarros == null) {
            mapa.listaWpCarros = new HashSet<>();
        }

        // for (Carro carro : mapa.listadeCarrosConGps) {
        for (Carro carro : mapa.listadeDispositivosGps) {
            NominatimReverseGeocodingJAPI nominatim1;
            Address addres = null;
            GeoPosition a;
            String direcccion;
            try {
                nominatim1 = new NominatimReverseGeocodingJAPI(); //create instance with default zoom level (18)

                addres = nominatim1.getAdress(carro.getUltimaUbicacion().getLatitude(), carro.getUltimaUbicacion().getLongitude()); //returns Address object for the given position

                direcccion = addres.getDisplayName();

            } catch (Exception e) {
                direcccion = carro.getUltimaUbicacion().getLatitude() + "," + carro.getUltimaUbicacion().getLongitude();
            }

            DecimalFormat formato = new DecimalFormat("#,###.##");
            String info = "<html><body>"
                    + "<table>"
                    + "<tr>"
                    + "<td>Id Placa : </td>"
                    + "<td WIDTH=300 >" + carro.getPlaca() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Conductor : </td>"
                    + "<td WIDTH=300>" + carro.getConductor() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Localizacion : </td>"
                    + "<td WIDTH=300>" + direcccion + "</td>"
                    // + "<td>" + carro.getUltimaUbicacion().getLatitude() + "," + carro.getUltimaUbicacion().getLongitude() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Orientacion : </td>"
                    + "<td WIDTH=300>" + carro.getNombreCourse() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Hora : </td>"
                    + "<td WIDTH=300>" + carro.getFechaDeInicio() + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Velocidad : </td>"
                    + "<td WIDTH=300>" + formato.format(carro.getSpeed()) + " Km/h </td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>Odometro  : </td>"
                    + "<td WIDTH=300>" + formato.format(carro.getOdometro()) + " Km </td>"
                    + "</tr>"
                    + "</table>"
                    + "</body></html>";

            final MyWayPoint marker = new MyWayPoint(
                    "" + carro.getPlaca(),
                    carro.getUltimaUbicacion(),
                    info);

            mapa.listaWpCarros.add(marker);

            Thread.sleep(1);

        }

    }

    private void asignarPuntosCarrosConGps(Carro carro) throws InterruptedException {
        LatLng puntoFinal = null;

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.setGeodesic(true);
        lineOptions.setStrokeColor("#4480CC");
        lineOptions.setStrokeOpacity(1);
        lineOptions.setStrokeWeight(2);

        mapa.listaWpPuntosRecorridos = new HashSet<>();
        NominatimReverseGeocodingJAPI nominatim1;
        Address addres = null;
        GeoPosition a;
        String direcccion;
        try {
            nominatim1 = new NominatimReverseGeocodingJAPI(); //create instance with default zoom level (18)

            addres = nominatim1.getAdress(carro.getUltimaUbicacion().getLatitude(), carro.getUltimaUbicacion().getLongitude()); //returns Address object for the given position

            direcccion = addres.getDisplayName();

        } catch (Exception e) {
            direcccion = carro.getUltimaUbicacion().getLatitude() + "," + carro.getUltimaUbicacion().getLongitude();
        }

        DecimalFormat formato = new DecimalFormat("#,###.##");
        String info = "<html><body>"
                + "<table>"
                + "<tr>"
                + "<td>Id Placa : </td>"
                + "<td WIDTH=300 >" + carro.getPlaca() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Conductor : </td>"
                + "<td WIDTH=300>" + carro.getConductor() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Localizacion : </td>"
                // + "<td WIDTH=300>" + addres.getDisplayName() + "</td>"
                + "<td>" + direcccion + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Orientacion : </td>"
                + "<td WIDTH=300>" + carro.getNombreCourse() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Hora : </td>"
                + "<td WIDTH=300>" + carro.getFechaDeInicio() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Velocidad : </td>"
                + "<td WIDTH=300>" + formato.format(carro.getSpeed()) + " Km/h </td>"
                + "</tr>"
                + "<tr>"
                + "<td>Odometro  : </td>"
                + "<td WIDTH=300>" + formato.format(carro.getOdometro()) + " Km </td>"
                + "</tr>"
                + "</table>"
                + "</body></html>";

        final MyWayPoint marker = new MyWayPoint(
                "" + carro.getPlaca(),
                carro.getUltimaUbicacion(),
                info);

        mapa.listaWpPuntosRecorridos.add(marker);

        Thread.sleep(1);

    }

    public void seePuntosCarrosConGps() {
        CompoundPainter<JXMapViewer> painters = new CompoundPainter<JXMapViewer>(mapa.painters);

        WaypointPainter<MyWayPoint> wpPainterCarros = new WayPointRenderer();
        wpPainterCarros.setWaypoints(mapa.listaWpCarros);
        painters.addPainter(wpPainterCarros);

        for (MyWayPoint d : mapa.listaWpCarros) {
            mapa.jXMapViewer1.add(d.getButton());

        }

        mapa.jXMapViewer1.setOverlayPainter(painters);

        //mapa.jXMapViewer1.setAddressLocation(mapa.puntoCliente);
        //mapa.jXMapViewer1.setZoom(12);
    }

    public void seePuntosCarrosConGps(MyWayPoint placa) {

        WaypointPainter<MyWayPoint> waypointPainter = new WayPointRenderer();
        waypointPainter.setWaypoints(mapa.listaWpCarros);
        mapa.painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(mapa.painters);
        mapa.jXMapViewer1.setOverlayPainter(painter);

        for (MyWayPoint d : mapa.listaWpCarros) {
            mapa.jXMapViewer1.add(d.getButton());

        }
        mapa.jXMapViewer1.setAddressLocation(mapa.puntoCliente);
        //mapa.jXMapViewer1.setZoom(12);

    }

    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {
        //double radioTierra = 3958.75;//en millas  
        double radioTierra = 6371;//en kilómetros  
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
    }

    private void asgnarCetroDelMapa() {

        mapa.centro = new GeoPosition(carro.getUltimaUbicacion().getLongitude(), carro.getUltimaUbicacion().getLatitude());
        mapa.jXMapViewer1.setAddressLocation(mapa.centro);
        mapa.jXMapViewer1.setZoom(5);

    }

    private void borrarRecorrido() {
        String placa = mapa.jListaDeCarros.getSelectedValue().toString();
        CompoundPainter<JXMapViewer> painters = new CompoundPainter<JXMapViewer>(mapa.painters);        mapa.ocupado = true;
       
        mapa.listadeLineas.clear();
        mapa.listaDeTodosWP.clear();
        mapa.listaWpPuntosRecorridos.clear();
        
        for (Carro car : mapa.listadeDispositivosGps) {
            if (placa.equals(car.getPlaca())) {
                car.setUltimaUbicacion();
                carro = car;
               break;
            }

        }
         Component[] componentes = mapa.jXMapViewer1.getComponents();

        for (int i = 0; i < componentes.length; i++) {

            if (componentes[i] instanceof JButton) {
                    mapa.jXMapViewer1.remove(componentes[i]);
            }
        }

        for (MyWayPoint wp : mapa.listaWpCarros) {
            mapa.listaDeTodosWP.add(wp);
        }
       
     
        WaypointPainter<MyWayPoint> waypointPainter = new WayPointRenderer();
        waypointPainter.setWaypoints(mapa.listaDeTodosWP);
        painters.addPainter(waypointPainter);

        mapa.jXMapViewer1.setOverlayPainter(painters);

        for (MyWayPoint d : mapa.listaDeTodosWP) {
            mapa.jXMapViewer1.add(d.getButton());

        }
        

        mapa.jXMapViewer1.setAddressLocation(carro.getUltimaUbicacion());

        mapa.ocupado = false;
        mapa.repaint();
    }

    private void seeCarrosConGps() {

        CompoundPainter<JXMapViewer> painters = new CompoundPainter<JXMapViewer>();

        WaypointPainter<MyWayPoint> waypointPainter = new WayPointRenderer();
        waypointPainter.setWaypoints(mapa.listaWpCarros);

        //waypointPainter.setWaypoints(mapa.listaWpPuntosRecorridos);
        painters.addPainter(waypointPainter);

        // for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
        for (MyWayPoint d : mapa.listaWpCarros) {
            if (d.getCodigoCliente() != null) {
                mapa.jXMapViewer1.add(d.getButton());

                /* punto recorrido */
                // track.add(d.getPosition());
                System.out.println("codigo id : " + d.getCodigoCliente());

                mapa.centro = d.getPosition();
            }

        }
        mapa.jXMapViewer1.setOverlayPainter(painters);

    }

}
