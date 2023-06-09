package ui.swing.tableEditable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.table.DefaultTableCellRenderer;

public class TableHeaderCustom extends DefaultTableCellRenderer {

    public TableHeaderCustom() {
        setPreferredSize(new Dimension(0, 35));
        setBackground(new Color(250,250,250));
        setForeground(new Color(0, 0, 0));
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        grphcs.setColor(new Color(100, 100, 100));
        grphcs.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}
