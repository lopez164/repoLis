/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.wayPoints;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author lelopez
 */
public class ButtonWayPointRecorrido extends JButton {

    public ButtonWayPointRecorrido() {
        setContentAreaFilled(false);
        //setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
//          setIcon(new ImageIcon(getClass().getResource("icon/wpBlue-remove.png")));
        setBorder(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(22, 36));

    }
}
