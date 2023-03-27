package ui.swing.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Table extends JTable {

    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(25);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                if (i1 == 4) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int row, int column) {
                if (column != 4) {
                    Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, row, column);
                    com.setBackground(Color.WHITE);
                    setBorder(noFocusBorder);
                    if (selected) {
                        com.setForeground(new Color(15, 89, 140));
                    } else {
                        com.setForeground(new Color(102, 102, 102));
                    }
                    if(isCellSelected(row, column)){
                       
                    com.setBackground(new Color(15, 89, 140));
                    com.setForeground(new Color(250, 250, 249));
                }else{
                         if(row % 2 == 0){
                            com.setBackground(Color.WHITE);
                        }else{
                             com.setBackground(new Color(244, 246, 246));
                        }
                        
                    }
                    return com;
                } else {
                    StatusType type = (StatusType) o;
                    CellStatus cell = new CellStatus(type);
                    return cell;
                }
                
            }
        });
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }
}
