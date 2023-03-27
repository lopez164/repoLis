package chart.barChart;

import chart.blankchart.BlankPlotChart;
import chart.blankchart.BlankPlotChatRender;
import chart.blankchart.SeriesSize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class BarChart extends javax.swing.JPanel {

    private List<BarModelLegend> legends = new ArrayList<>();
    private List<BarModelChart> model = new ArrayList<>();
    private final int seriesSize = 20;
    private final int seriesSpace = 10;
    
    
    private final Animator animator;
    private float animate;

    public BarChart() {

        initComponents();
         BoxLayout boxlayout = new BoxLayout(panelLegend, BoxLayout.Y_AXIS);
         panelLegend.setLayout(boxlayout);
         
        TimingTarget target = new TimingTargetAdapter() {

            @Override
            public void timingEvent(float fraction) {

                animate = fraction;
                repaint();
            }

            @Override
            public void begin() {
            }

            @Override
            public void end() {
            }

            @Override
            public void repeat() {
            }

        };
        animator = new Animator(2000, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotChart chart, Graphics2D g2, SeriesSize size, int index) {
                double totalSeriesWidth = (seriesSize * legends.size()) + (seriesSpace * (legends.size() - 1));
                double x = (size.getWidth() - totalSeriesWidth) / 2;
                for (int i = 0; i < legends.size(); i++) {
                    BarModelLegend legend = legends.get(i);
                    g2.setColor(legend.getColor());
                    double seriesValues = chart.getSeriesValuesOf(model.get(index).getValues()[i], size.getHeight() * animate);
                    g2.fillRect((int) (size.getX() + x), (int) (size.getY() + size.getHeight() - seriesValues), seriesSize, (int) seriesValues);
                    x += seriesSpace + seriesSize;
                }
            }
        });
    }

    public void addLegend(String name, Color color) {
        BarModelLegend data = new BarModelLegend(name, color);
        legends.add(data);
        panelLegend.add(new LegendItem(data));
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(BarModelChart data) {
        model.add(data);
        blankPlotChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotChart.getMaxValues()) {
            blankPlotChart.setMaxValues(max);
        }
    }

    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void clear() {
        animate = 0;
        blankPlotChart.setLabelCount(0);
        model.clear();
        panelLegend.removeAll();
        repaint();
    }

    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://190.144.23.186:3306/comercializadoracam?useTimezone=true&serverTimezone=UTC&useSSL=false", "luislopez", "jslslpzmjc1212");
            Statement stmt = con.createStatement();

            String sql = "select m.vehiculo,count(numeroFactura) as cant "
                    + "from facturaspormanifiesto fm "
                    + "join manifiestosdedistribucion m on m.consecutivo = fm.numeroManifiesto "
                    + "where "
                    + "m.fechaDistribucion ='2022-09-03' "
                    + "group by m.vehiculo;";

            ResultSet rs = stmt.executeQuery(sql);
            int i = 0;
            int k = 1;
            while (rs.next()) {
                this.addLegend(rs.getString("vehiculo"), getColor(i++));
                if (i == 26) {
                    i = 0;
                }
                k++;
            }
            rs.beforeFirst();
            i = 0;
            double[] lista = new double[k];
            while (rs.next()) {
                lista[i] = rs.getDouble("cant");
                i++;
            }
            this.addData(new BarModelChart("", lista));

            con.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BarChart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BarChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Color getColor(int index) {
        Color color = new Color(255, 255, 255);
        switch (index) {
            case 0:
                color = new Color(69, 120, 177);
                break;
            case 1:
                color = new Color(236, 217, 21);
                break;
            case 2:
                color = new Color(179, 238, 154);
                break;
            case 3:
                color = new Color(99, 149, 202);
                break;
            case 4:
                color = new Color(241, 134, 191);
                break;
            case 5:
                color = new Color(162, 249, 52);
                break;
            case 6:
                color = new Color(199, 247, 255);
                break;
            case 7:
                color = new Color(241, 165, 89);
                break;
            case 8:
                color = new Color(186, 255, 182);

                break;
            case 9:
                color = new Color(206, 17, 253);
                break;
            case 10:
                color = new Color(210, 211, 111);
                break;
            case 11:
                color = new Color(240, 225, 232);
                break;
            case 12:
                color = new Color(255, 255, 190);
                break;
            case 13:
                color = new Color(225, 107, 97);
                break;
            case 14:
                color = new Color(178, 183, 246);
                break;
            case 15:
                color = new Color(153, 231, 150);
                break;
            case 16:
                color = new Color(255, 255, 138);
                break;
            case 17:
                color = new Color(225, 196, 209);
                break;
            case 18:
                color = new Color(241, 139, 126);
                break;
            case 19:
                color = new Color(231, 231, 143);
                break;
            case 20:
                color = new Color(209, 167, 186);
                break;
            case 21:
                color = new Color(167, 213, 237);
                break;
            case 22:
                color = new Color(143, 227, 255);
                break;
            case 23:
                color = new Color(240, 239, 176);
                break;
            case 24:
                color = new Color(120, 207, 119);
                break;
            case 25:
                color = new Color(134, 181, 220);
                break;
            case 26:
                color = new Color(255, 171, 157);
                break;
            case 27:
                color = new Color(255, 242, 205);
                break;
            case 28:
                color = new Color(222, 179, 223);
                break;
            case 29:
                color = new Color(166, 161, 237);
                break;
            case 30:
                color = new Color(186, 213, 249);
                break;
            case 31:
                color = new Color(255, 213, 178);
                break;
            case 32:
                color = new Color(255, 171, 157);
                break;

        }

        return color;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blankPlotChart = new chart.blankchart.BlankPlotChart();
        panelLegend = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new javax.swing.BoxLayout(panelLegend, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(blankPlotChart, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(blankPlotChart, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(166, 166, 166)))
                .addGap(2, 2, 2))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chart.blankchart.BlankPlotChart blankPlotChart;
    private javax.swing.JPanel panelLegend;
    // End of variables declaration//GEN-END:variables
}
