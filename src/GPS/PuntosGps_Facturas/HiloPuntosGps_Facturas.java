/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.PuntosGps_Facturas;

import GPS.PuntosGps.*;
import GPS.wayPoints.MyWayPoint;
import GPS.wayPoints.WayPointRenderer;
import aplicacionlogistica.configuracion.Inicio;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.PolylineOptions;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import mtto.vehiculos.CCarros;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author Usuario
 */
public class HiloPuntosGps_Facturas implements Runnable {

    private PuntosGps_Facturas mapa;
    private String value;//retardo en milisegundos
    private String placa;
    Connection conn = null;

    List<ImageIcon> listaDeIconos;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param mapa
     * @param value
     */
    public HiloPuntosGps_Facturas(PuntosGps_Facturas mapa, String value) {
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
    public HiloPuntosGps_Facturas(PuntosGps_Facturas mapa, String value, int x) {
        this.mapa = mapa;
        this.value = value;

    }

    /**
     * Constructor de clase
     *
     * @param mapa
     * @param value
     */
    public HiloPuntosGps_Facturas(PuntosGps_Facturas mapa, String value, String placa) {
        this.mapa = mapa;
        this.value = value;
        this.placa = placa;

    }

    @Override
    public void run() {
        switch (value) {
            case "setListaDeIconos":
                setListaDeIconos();
                mapa.setListaDeCarros(1);
                break;

            case "llenarJlistaVehiculos":
                mapa.llenarJlistaVehiculos();
                break;
            case "cargarClientesPorCarro":
                //cargarClientesPorCarro();
                break;

            case "putWayPointsClientes":

                if (mapa.listaWpClientes != null) {

                    delListaDeClientes();
                    delListaDePuntosRecorridos();

                }

                for (CCarros car : mapa.listadeCarros) {

                    if (car.getPlaca().equals(mapa.placa)) {
                        if (car.getWpClientes() == null || car.getWpClientes().size() == 0) {
                            car.setListaDeClientes(mapa.fechaDistribucion);
                        }
                        mapa.carro = car;

                        mapa.listaWpClientes = new HashSet<>();;
                        for (MyWayPoint m : car.getWpClientes()) {
                            mapa.listaWpClientes.add(m);
                        }

                        break;
                    }

                }
                //asignarPuntoGeoClientes();
                seeListaDeClientes();
                mapa.jCheckRecorrido.setEnabled(true);
                break;

            case "mostrarRecorrido":

                mapa.placa = mapa.jListaDeCarros.getSelectedValue().toString();
                for (CCarros car : mapa.listadeCarros) {

                    if (car.getPlaca().equals(mapa.placa)) {
                        mapa.carro = car;
                        // mapa.carro.setPlaca(car.getPlaca());
                        //mapa.carro.setConductor(car.getConductor());
                        //mapa.carro.setIconc(car.getImageIcon());
                        //mapa.carro.setFechaDistribucion(mapa.fechaDistribucion);
                        //mapa.carro.setFechaDeInicio(mapa.servertime);
                        // mapa.carro.setFechaFinal(mapa.servertime2);
                        /*asigna coordenadas de los puntos recorridos*/
                        if (!mapa.carro.setListaDePuntosDeRecorrido(mapa.fechaInicial, mapa.fechaFinal)) {

                            JOptionPane.showMessageDialog(mapa, "Vehiculo no tiene Gps", "Sin Gps", JOptionPane.HEIGHT);

                            return;
                        }

                        break;
                    }

                }
                /*crea los waypoint gps*/
                mapa.listaWpPuntosRecorridos = mapa.carro.getWpPuntosRecorridos();
                //asignarPuntosRecorridos();
                //seeListaDePuntosRecorridos();
                //se//eLineDePuntosRecorridos2();
                dibujarLineasDePuntosRecorridos();
                //dibujarRuta();

                //  putWayPoints();
                break;

            case "borrarWayPointsPuntosRecorridos":
                // borrarPuntosRecorridos();
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

//                borrarPuntosRecorridos();
                //   borrarPolyLinea();
                // setListaDeWayPoints();
                //putWayPoints();
                break;

            case "delListaDeNegocios":
                //delListaDeNegocios();
//                    delListaDeClientes();
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

    private void asignarPuntosRecorridos() throws InterruptedException {
        LatLng puntoFinal = null;

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.setGeodesic(true);
        lineOptions.setStrokeColor("#4480CC");
        lineOptions.setStrokeOpacity(1);
        lineOptions.setStrokeWeight(0.8);

        mapa.listaWpPuntosRecorridos = new HashSet<>();
//        mapa.carro.listaDeMarcadoresClientes = new ArrayList<>();

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
                    "" + punto.getId(),
                    punto.getPath(),
                    punto.getIcon(),
                    info);

            //mapa.listaWpPuntosRecorridos.add(marker);
            mapa.listaWpClientes.add(marker);
            //mapa.carro.listaDeMarcadoresClientes.add(marker);

            Thread.sleep(1);

        }

    }

    private void seeLineDePuntosRecorridos() {
        // Create a track from the geo-positions
        // List<GeoPosition> track = Arrays.asList(mapa.listaDePuntosRecorridosHash);
        List<GeoPosition> track = new ArrayList<>();

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            try {

                track.add(d.getPosition());
                Thread.sleep(2);

            } catch (InterruptedException ex) {
                Logger.getLogger(HiloPuntosGps_Facturas.class.getName()).log(Level.SEVERE, null, ex);
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
        // List<GeoPosition> track = Arrays.asList(mapa.listaDePuntosRecorridosHash);
        List<GeoPosition> track = new ArrayList<>();

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            try {

                track.add(d.getPosition());

                Thread.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloPuntosGps_Facturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void seeListaDeClientes() {

        boolean gps = false;
        WaypointPainter<MyWayPoint> waypointPainter = new WayPointRenderer();
        
        if (mapa.carro.getConGps() == 1) {
            mapa.carro.setUltimaPosicion();

            /* Se anexa el punto del vehiculo */
            if (mapa.carro.getUltimaPosicion() != null) {
                MyWayPoint car = new MyWayPoint(mapa.carro.getPlaca(),
                        new GeoPosition(mapa.carro.getGeoposition().getLatitude(), mapa.carro.getGeoposition().getLongitude()),
                        // mapa.carro.getUltimaUbicacion(),
                        "" + mapa.carro.getPlaca());
                mapa.listaWpClientes.add(car);
                gps = true;
            }

        }
        if (mapa.listaWpClientes != null || mapa.listaWpClientes.size() > 0) {
            waypointPainter.setWaypoints(mapa.listaWpClientes);
            mapa.painters.add(waypointPainter);

            CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(mapa.painters);
            mapa.jXMapViewer1.setOverlayPainter(painter);

            for (MyWayPoint d : mapa.listaWpClientes) {
                mapa.jXMapViewer1.add(d.getButton());
                mapa.centro = d.getPosition();
            }

//             JOptionPane.showMessageDialog(mapa, "clientes =  " + mapa.carro.getWpClientes().size() ); 
//             JOptionPane.showMessageDialog(mapa, "mapa.centro=  " +  mapa.centro ); 
//             JOptionPane.showMessageDialog(mapa, "carro=  " +  mapa.carro.getPlaca() ); 
            mapa.jXMapViewer1.setAddressLocation(mapa.centro);
            mapa.jXMapViewer1.setZoom(5);
            mapa.repaint();

        } else {
            JOptionPane.showMessageDialog(mapa, "Sin clientes ");
        }
        if (!gps) {
            JOptionPane.showMessageDialog(mapa, "Carro de placa " + mapa.carro.getPlaca() + "  sin Gps");
        }

    }

    public void seeListaDePuntosRecorridos() {

        WaypointPainter<MyWayPoint> wp = new WayPointRenderer();
        wp.setWaypoints(mapa.listaWpPuntosRecorridos);
        mapa.painters.add(wp);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(mapa.painters);
        mapa.jXMapViewer1.setOverlayPainter(painter);

        for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
            mapa.jXMapViewer1.add(d.getButton());
            mapa.centro = d.getPosition();

        }
//        mapa.carro.setUltimaUbicacion();
//         MyWayPoint car = new MyWayPoint(placa,
//                mapa.carro.getUltimaUbicacion(),
//                "prueba");
//        mapa.jXMapViewer1.add(car.getButton());
//        
        mapa.jXMapViewer1.setAddressLocation(mapa.centro);
        mapa.jXMapViewer1.setZoom(5);

    }

    public void dibujarLineasDePuntosRecorridos() {
        List<GeoPosition> track = new ArrayList<>();

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(mapa.painters);

        WaypointPainter<MyWayPoint> waypointPainter = new WayPointRenderer();
        waypointPainter.setWaypoints(mapa.listaWpClientes);

        //waypointPainter.setWaypoints(mapa.listaWpPuntosRecorridos);
        painter.addPainter(waypointPainter);

        mapa.painters.add(waypointPainter);

        mapa.jXMapViewer1.setOverlayPainter(waypointPainter);

        int i = 0;
        // for (MyWayPoint d : mapa.listaWpPuntosRecorridos) {
        for (MyWayPoint d : mapa.listaWpClientes) {
            mapa.jXMapViewer1.add(d.getButton());

            /* punto recorrido */
            // track.add(d.getPosition());
            System.out.println("codigo id : " + d.getCodigoCliente());

            mapa.centro = d.getPosition();

        }
        /*Dibuja l lineaas del recorrido */
        for (PuntoDeRecorrido punto : mapa.carro.getListaDePuntosDeRecorrido()) {
            track.add(punto.getPath());

        }
        RoutePainter routePainter = new RoutePainter(track);

        // Set the focus
        mapa.jXMapViewer1.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        painter.addPainter(routePainter);

        mapa.jXMapViewer1.setOverlayPainter(painter);

        mapa.jXMapViewer1.setAddressLocation(mapa.centro);
        mapa.jXMapViewer1.setZoom(5);

    }

    public void delListaDeClientes() {

        //JOptionPane.showMessageDialog(mapa, "tamaño lista antes : " + mapa.carro.getWpClientes().size());
        if (mapa.listaWpClientes != null || mapa.listaWpClientes.size() > 0) {
            for (MyWayPoint m : mapa.listaWpClientes) {
                mapa.jXMapViewer1.remove(m.getButton());
                System.out.println("Borrando marcador Cliente " + mapa.carro.getPlaca());
            }
            mapa.listaWpClientes.clear();
            mapa.jXMapViewer1.repaint();

            mapa.repaint();
            // JOptionPane.showMessageDialog(mapa, "tamaño lista despues : " + mapa.carro.getWpClientes().size());

        }
    }

    public void delListaDePuntosRecorridos() {
        if (mapa.listaWpPuntosRecorridos != null) {
            for (MyWayPoint m : mapa.listaWpPuntosRecorridos) {
                mapa.jXMapViewer1.remove(m.getButton());
                System.out.println("Borrando marcador punto Recorridos " + mapa.carro.getPlaca());
            }
            mapa.listaWpPuntosRecorridos.clear();
            mapa.repaint();
            //mapa.initWayPoints();
        }

    }

    private void dibujarRuta() {

        // Create a track from the geo-positions
        List<GeoPosition> track = new ArrayList<>();

        for (PuntoDeRecorrido punto : mapa.carro.getListaDePuntosDeRecorrido()) {
            track.add(punto.getPath());

        }
        RoutePainter routePainter = new RoutePainter(track);

        // Set the focus
        mapa.jXMapViewer1.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Create waypoints from the geo-positions
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

        mapa.setListaDeIconosClientes(listaDeIconos);

    }

}
