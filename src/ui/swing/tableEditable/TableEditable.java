package ui.swing.tableEditable;

import com.raven.table.TableCustom;
import ui.swing.scrollBar.ScrollBarCustom;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableEditable extends TableCustom {

    public TableEditable() {
    }

    public void addTableStyle(JScrollPane scroll) {
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);
        setBorder(null);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        JPanel panel = new JPanel();
        panel.setBackground(new Color(142, 68, 173 )); //214, 214, 214
        setForeground(new Color(0, 0, 0)); //60, 60, 60
        setSelectionForeground(new Color(52, 73, 94));
        setSelectionBackground(new Color(52, 152, 219));
        getTableHeader().setDefaultRenderer(new TableHeaderCustom());
        setRowHeight(47);
        setShowHorizontalLines(true);
        setGridColor(new Color(250, 250, 250));
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
        Component com = super.prepareRenderer(tcr, row, column);
        if (!isCellSelected(row, column)) {
            if( row % 2 == 0){
                com.setBackground(new Color(250, 250, 250)); 
            }else{
                 com.setBackground(new Color(242, 243, 244)); 
            }
           
        }
        return com;
    }

}
