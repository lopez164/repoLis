/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author lelopez
 */
public class RowsRenderer extends DefaultTableCellRenderer {
    private int columna ;

public RowsRenderer(int Colpatron)
{
    this.columna = Colpatron;
}

@Override
public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column)
{        
   // setBackground(Color.white);
//    table.setForeground(Color.black);
//    super.getTableCellRendererComponent(table, value, selected, focused, row, column);
//    if(table.getValueAt(row,columna).equals("ENTREGA")){
//        //this.setBackground(Color.getHSBColor(108,100,48));
//        this.setForeground( Color.GREEN);
//    }else if(table.getValueAt(row,columna).equals("DEVOLUCION")){
//       // this.setBackground(Color.red);
//         this.setForeground( Color.red);
//    }else if(table.getValueAt(row, columna).equals("PARCIALES")){
//        this.setForeground(Color.ORANGE);
//        //this.setBackground(Color.red);
//    }else if(table.getValueAt(row, columna).equals("RECOGIDAS")){
//        //this.setForeground( Color.getHSBColor(298,69,89));
//    }else if(table.getValueAt(row, columna).equals("RE ENVIOS")){
//        this.setForeground(Color.getHSBColor(252,69,41));
//    }else if(table.getValueAt(row, columna).equals("NO VISITADOS")){
//        //this.setForeground(Color.getHSBColor(267,73,74));
//    }else if(table.getValueAt(row, columna).equals("Sin Movimiento")){
//        this.setForeground(Color.black);
//    }
    
    table.setForeground(Color.black);
    super.getTableCellRendererComponent(table, value, selected, focused, row, column);
    if(table.getValueAt(row,columna) != null){
      if(table.getValueAt(row,columna).equals("ENTREGA")){
        //this.setBackground(Color.green);
        this.setBackground(new Color(142,255,124));
        this.setForeground( Color.black);
        
    }else if(table.getValueAt(row,columna).equals("DEVOLUCION")){
       // this.setBackground(Color.red);
        this.setBackground(new Color(252,135,135));
         this.setForeground( Color.white);
    }else if(table.getValueAt(row, columna).equals("PARCIALES")){
       this.setBackground(Color.orange);
         this.setForeground( Color.black);
    }else if(table.getValueAt(row, columna).equals("RECOGIDAS")){
        //this.setForeground( Color.getHSBColor(298,69,89));
    }else if(table.getValueAt(row, columna).equals("RE ENVIOS")){
       this.setBackground(Color.getHSBColor(252,69,41));
         this.setForeground( Color.black); 
    }else if(table.getValueAt(row, columna).equals("NO VISITADOS")){
        //this.setForeground(Color.getHSBColor(267,73,74));
    }else if(table.getValueAt(row, columna).equals("Sin Movimiento")){
        setBackground(Color.white);
       this.setForeground(Color.black);
    }  
    }
    
    return this;
  }
    
}
