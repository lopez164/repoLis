    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author lelopez
 */
public class RowsRenderer extends DefaultTableCellRenderer {

    private int columna;

    public RowsRenderer(int Colpatron) {
        this.columna = Colpatron;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        //setBackground(Color.white);
        //table.setForeground(Color.black);
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        
        //Establecemos las filas que queremos cambiar el color. == 0 para pares y != 0 para impares
        boolean oddRow = (row % 2 == 0);
        Color c = new Color(227, 225, 225);

        if (table.getValueAt(row, columna).equals("DEVOLUCION TOTAL")) {
            setBackground(Color.RED);
            this.setForeground(Color.white);

        } else if (table.getValueAt(row, columna).equals("ENTREGA CON NOVEDAD")) {
             setBackground(new Color(241, 224, 27));
            
            this.setForeground(Color.black);
        } else if (table.getValueAt(row, columna).equals("ENTREGA TOTAL")) {
            if (oddRow) {
                setBackground(c);
            } else {
                setBackground(Color.white);
            }

            this.setForeground(Color.black);
        } else if (table.getValueAt(row, columna).equals("VOLVER A ZONIFICAR")) {
            if (oddRow) {
                setBackground(c);
            } else {
                setBackground(Color.white);
            }
            this.setForeground(Color.black);
        }
        return this;
    }
}
