package ui.swing.tableEditable.cells;

import ui.swing.tableEditable.ModelName;
import com.raven.table.cell.Cell;

public class CellNameRender extends Cell {

    public CellNameRender(ModelName data) {
        initComponents();
        lb.setText(data.toString());
        image.setImage(data.getProfile());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        image = new ui.swing.tableEditable.ImageAvatar();
        lb = new javax.swing.JLabel();

        image.setBorderSize(2);
        image.setBorderSpace(1);
        image.setGradientColor1(new java.awt.Color(63, 109, 217));
        image.setGradientColor2(new java.awt.Color(199, 42, 42));
        image.setImage(new javax.swing.ImageIcon(getClass().getResource("/ui/swing/icons/p1_2.jpg"))); // NOI18N

        lb.setForeground(new java.awt.Color(200, 200, 200));
        lb.setText("Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.swing.tableEditable.ImageAvatar image;
    private javax.swing.JLabel lb;
    // End of variables declaration//GEN-END:variables
}
