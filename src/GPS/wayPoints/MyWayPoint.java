/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.wayPoints;

import GPS.PuntosGps.PuntosGps;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author lelopez
 */
public class MyWayPoint extends DefaultWaypoint {

    private JButton button;
    private ImageIcon icono;
    private String info;
    String codigoCliente;
    String placa;
    GeoPosition coord;

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public ImageIcon getIcono() {
        return icono;
    }

    public void setIcono(ImageIcon icono) {
        this.icono = icono;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public GeoPosition getCoord() {
        return coord;
    }

    public void setCoord(GeoPosition coord) {
        this.coord = coord;
    }
    
    

    public MyWayPoint() {

    }
    
     

    public MyWayPoint(String codigoCliente, GeoPosition coord, ImageIcon icon, String info) {
        super(coord);
        this.icono = icon;
        this.info = info;
        this.codigoCliente = codigoCliente;
        this.coord=coord;

        InitButton();

    }

    public MyWayPoint(String placa, GeoPosition coord, String info) {
        super(coord);
        this.info = info;
        this.placa = placa;
         this.coord=coord;

        InitButton2();

    }

    private void InitButton() {
        button = new ButtonWayPointCliente();
        button.setIcon(icono);
        button.setToolTipText(info);
        button.setText("");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInternalMessageDialog(button,
                        button.getToolTipText(),
                        "Codigo del Cliente " + codigoCliente,
                        JOptionPane.INFORMATION_MESSAGE);

            }

        });

    }

    private void InitButton2() {
        button = new ButtonWayPointCliente();
        //button = new JButton();
        button.setContentAreaFilled(false);
        //setIcon(new javax.swing.ImageIcon(getClass().getResource("GPS/icons/truck_32x32.png"))); // NOI18N
        //setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
//          setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
        button.setForeground(Color.red);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setSize(new Dimension(60, 60));
        button.setText(placa);
        button.setBorder(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        button.setToolTipText(info);
        button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/truck_32x32.png")));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInternalMessageDialog(button,
                        button.getToolTipText(),
                        "Placa del Carro " + placa,
                        JOptionPane.INFORMATION_MESSAGE);
                
            }

        });

    }
    
   
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

}
