/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.wayPoints;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author lelopez
 */
public class ButtonWayPointCliente extends JButton {

    public ButtonWayPointCliente() {
        setContentAreaFilled(false);
        //setIcon(new javax.swing.ImageIcon(getClass().getResource("GPS/icons/truck_32x32.png"))); // NOI18N
        //setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
//          setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
        setBorder(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(36, 36));

    }
    
    public ButtonWayPointCliente(int valor) {
        setContentAreaFilled(false);
        setForeground(Color.red);
        setFont(new Font("Arial", Font.BOLD,11));
        
        //setIcon(new javax.swing.ImageIcon(getClass().getResource("GPS/icons/truck_32x32.png"))); // NOI18N
        //setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
//          setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
        setBorder(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(60, 60));

    }
}
