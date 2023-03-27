/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.wayPoints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author lelopez
 */
public class MyWayPointRecorrido extends DefaultWaypoint {

    private JButton button;
    private ImageIcon icono;
    private String info;
    private int id;
    
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

        

    public MyWayPointRecorrido() {

    }

    public MyWayPointRecorrido(int id,GeoPosition coord,ImageIcon icon, String info) {
        super(coord);
        this.id = id;
        this.icono = icon;
        this.info = info;
        
        InitButton();

    }

    private void InitButton() {
        button = new ButtonWayPointCliente();
        button.setIcon(icono);
        button.setToolTipText(info);
        
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInternalMessageDialog(button, 
                        button.getToolTipText(),
                        "idMovimiento del Cliente " + id,
                        JOptionPane.INFORMATION_MESSAGE);
                
            }

        });

    }

}

