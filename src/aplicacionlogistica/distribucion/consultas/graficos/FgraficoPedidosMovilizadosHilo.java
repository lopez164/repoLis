/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas.graficos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import aplicacionlogistica.configuracion.Inicio;
import java.awt.BasicStroke;
import java.text.DecimalFormat;
import java.util.Date;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;

/**
 *
 * @author Usuario
 */
public class FgraficoPedidosMovilizadosHilo implements Runnable {

    JPanel panel;
    Inicio ini;

    FgraficoPedidosMovilizados form;
    Date fechaInicial, fechaFinal;

    /**
     * Creates a new demo instance.
     *
     * @param ini
     * @param panel
     */
    //public FgraficoPedidosMovilizadosHilo(final String title, JPanel panel) {
    public FgraficoPedidosMovilizadosHilo(Inicio ini, JPanel panel) {
        this.panel = panel;
        this.ini = ini;
        // super(title);

        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setPreferredSize(new Dimension(500, 270));
        chart.setBackgroundPaint(Color.white);
        chartPanel.setPreferredSize(new Dimension(this.panel.getSize()));
        chartPanel.setVisible(true);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(chartPanel);

    }

    public FgraficoPedidosMovilizadosHilo(Inicio ini, FgraficoPedidosMovilizados form, Date fechaInicial, Date fechaFinal) {
        this.form = form;
        this.ini = ini;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        // super(title);

    }

    @Override
    public void run() {

        if (form.cantidadGraficos == 0) {
            this.form.lblCirculoDeProgreso.setVisible(true);
            this.form.lblCirculoDeProgreso.setEnabled(true);
            final CategoryDataset dataset = createDataset(fechaInicial, fechaFinal);
            final JFreeChart chart = createChart(dataset);
            final ChartPanel chartPanel = new ChartPanel(chart);
            //chartPanel.setPreferredSize(new Dimension(500, 270));
            chart.setBackgroundPaint(Color.white);
            chartPanel.setPreferredSize(new Dimension(this.form.pnlGrafico1.getSize()));
            chartPanel.setVisible(true);
            this.form.pnlGrafico1.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.form.pnlGrafico1.add(chartPanel, 0);
            this.form.repaint();
            this.form.lblCirculoDeProgreso.setVisible(false);
            form.cantidadGraficos = 1;
        }

    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDatasetxxx() {

        String sql;
        // row keys...
        final String series1 = "Al dia";
        final String series2 = "<= 3 meses";
        final String series3 = "<= 6 meses";
        final String series4 = "<= 12 meses";
        final String series5 = "> 12 meses";

        // column keys...
        final String category1 = "Condominos Morosos";
        //final String category2 = "Category 2";
        //final String category3 = "Category 3";
        //final String category4 = "Category 4";
        //final String category5 = "Category 5";

        // create the dataset...
        final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        //dataset2.addValue(this.rptind.aldia, series1, category1);
        //dataset2.addValue(4.0, series1, category2);
        //dataset2.addValue(3.0, series1, category3);
        //dataset2.addValue(5.0, series1, category4);
        //dataset2.addValue(5.0, series1, category5);
        //dataset2.addValue(this.rptind.morosos1, series2, category1);
        //dataset2.addValue(7.0, series2, category2);
        //dataset2.addValue(6.0, series2, category3);
        //dataset2.addValue(8.0, series2, category4);
        //dataset2.addValue(4.0, series2, category5);
        //dataset2.addValue(this.rptind.morosos2, series3, category1);
        //dataset2.addValue(3.0, series3, category2);
        //dataset2.addValue(2.0, series3, category3);
        //dataset2.addValue(3.0, series3, category4);
        //dataset2.addValue(6.0, series3, category5);
        //dataset2.addValue(this.rptind.morosos3, series4, category1);
        //dataset2.addValue(this.rptind.morosos4, series5, category1);
        return dataset2;

    }

    private CategoryDataset createDataset() {

        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        String sql;
        if (form.rBtnCantidadPedidos.isSelected()) {
            sql = "SELECT nombreCanal,count(numeroFactura) as valor FROM vst_movilizacionfacturasdescargadas "
                    + "where "
                    + "fechaDistribucion>='" + fechaInicial + "'  and "
                    + "fechaDistribucion<='" + fechaFinal + "' "
                    + "group by nombreCanal;";
        } else {
            sql = "SELECT nombreCanal,sum(valorFacturaSinIva) as valor FROM vst_movilizacionfacturasdescargadas "
                    + "where "
                    + "fechaDistribucion>='" + fechaInicial + "' and "
                    + "fechaDistribucion<='" + fechaFinal + "' "
                    + "group by nombreCanal;";
        }

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        // create the dataset..
        final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        // column keys...
        final String category1 = "Canal de Venta";

        try {
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    dataset2.addValue(rst.getInt("valor"), rst.getString("nombreCanal"), category1);

                }
            }
            rst.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dataset2;

    }

    private CategoryDataset createDataset(Date fechaInicial, Date fechaFinal) {

        Connection con = null;
        Statement st;
        ResultSet rst;
        String sql;

        if (form.rBtnCantidadPedidos.isSelected()) {
            sql = "SELECT nombreCanal,count(numeroFactura) as valor FROM vst_movilizacionfacturasdescargadas "
                    + "where "
                    + "fechaDistribucion>='" + fechaInicial + "'  and "
                    + "fechaDistribucion<='" + fechaFinal + "'  "
                    + "group by nombreCanal "
                    + " order by valor asc;";
        } else {
            sql = "SELECT nombreCanal,sum(valorFacturaSinIva) as valor FROM vst_movilizacionfacturasdescargadas "
                    + "where "
                    + "fechaDistribucion>='" + fechaInicial + "' and "
                    + "fechaDistribucion<='" + fechaFinal + "' "
                    + "group by nombreCanal "
                    + "order by valor asc;";
        }

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        // create the dataset..
        final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        // column keys...
        final String category1 = "Canal de Venta";

        try {
            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    if (form.rBtnCantidadPedidos.isSelected()) {
                        dataset2.addValue(rst.getInt("valor"), rst.getString("nombreCanal"), category1);
                    } else {
                        dataset2.addValue(rst.getDouble("valor"), rst.getString("nombreCanal"), category1);
                    }
                   

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dataset2;

    }

//    private DefaultCategoryDataset createDataset(ArrayList<CFacturas.ListaClaveValor> lst, String mes) {
//        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        String series1;
//        String category1 = mes;
//        for (CFacturas.ListaClaveValor obj : lst) {
//            series1 = String.valueOf(obj.getClave());
//            dataset.addValue(obj.getValor(), series1, category1);
//        }
//        return dataset;
//    }
    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart3D(
                "Pedidos Movilizados por Canal", // chart title
                "Nombre Canal", // domain axis label
                "Cant. Pedidos", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperMargin(0.15);

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        /*************************************************************************/
        DecimalFormat format;
        if(form.rBtnValorFactura.isSelected()){
            format = new DecimalFormat("$###,###");

         
        }else{
            format = new DecimalFormat("###,###");
        }
        StandardCategoryItemLabelGenerator labelGenerator = new StandardCategoryItemLabelGenerator("{2}", format);
       // final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
       // renderer.setBaseShapesVisible(true);
       // renderer.setBaseShapesFilled(true);
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        renderer.setSeriesItemLabelsVisible(0, true);
        renderer.setBaseItemLabelGenerator(labelGenerator);
        renderer.setBaseItemLabelFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 9));
 
       /*************************************************************************/   
        
        //CategoryItemRenderer renderer = plot.getRenderer();
        //renderer.setItemLabelGenerator(new LabelGenerator(0.0));
        renderer.setItemLabelFont(new Font("Serif", Font.BOLD, 15));
        renderer.setItemLabelsVisible(true);

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.BLUE,
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
                0.0f, 0.0f, Color.GREEN,
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.YELLOW,
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f, Color.ORANGE,
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp4 = new GradientPaint(
                0.0f, 0.0f, Color.RED,
                0.0f, 0.0f, Color.lightGray
        );
        
         for (int i=0 ; i<=dataset.getRowCount();i++){
           int numero = (int) (Math.random() * 9) + 1; 
           renderer.setSeriesPaint(0, getColorBarra(numero));
        }
        
//        renderer.setSeriesPaint(0, gp0);
//        renderer.setSeriesPaint(1, gp1);
//        renderer.setSeriesPaint(2, gp2);
//        renderer.setSeriesPaint(3, gp3);
//        renderer.setSeriesPaint(4, gp4);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(0.0) //Math.PI / 12.0
        );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

    private JFreeChart createChart(final CategoryDataset dataset, String mes) {
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "Diagarama de Barras", // chart title
                "Mes de recaudo " + mes, // domain axis label
                "$$", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperMargin(0.15);

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        //CategoryItemRenderer renderer = plot.getRenderer();
        // set up gradient paints for series...
//        final GradientPaint gp0 = new GradientPaint(
//                0.0f, 0.0f, Color.BLUE,
//                0.0f, 0.0f, Color.lightGray
//        );
//        final GradientPaint gp1 = new GradientPaint(
//                0.0f, 0.0f, Color.GREEN,
//                0.0f, 0.0f, Color.lightGray
//        );
//        final GradientPaint gp2 = new GradientPaint(
//                0.0f, 0.0f, Color.YELLOW,
//                0.0f, 0.0f, Color.lightGray
//        );
//        final GradientPaint gp3 = new GradientPaint(
//                0.0f, 0.0f, Color.ORANGE,
//                0.0f, 0.0f, Color.lightGray
//        );
//        final GradientPaint gp4 = new GradientPaint(
//                0.0f, 0.0f, Color.RED,
//                0.0f, 0.0f, Color.lightGray
//        );
        
        for (int i=0 ; i<=dataset.getRowCount();i++){
           int numero = (int) (Math.random() * 9) + 1; 
           renderer.setSeriesPaint(0, getColorBarra(numero));
        }
//        renderer.setSeriesPaint(0, gp0);
//        renderer.setSeriesPaint(1, gp1);
//        renderer.setSeriesPaint(2, gp2);
//        renderer.setSeriesPaint(3, gp3);
//        renderer.setSeriesPaint(4, gp4);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(0.0) //Math.PI / 12.0
        );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

        
    }
    
    private GradientPaint getColorBarra(int valor) {
        GradientPaint colorBarra = null;
        switch (valor) {
            case 0:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.BLUE,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 1:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.CYAN,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 2:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.DARK_GRAY,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 3:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.BLUE,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 4:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.GREEN,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 5:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.MAGENTA,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 6:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.ORANGE,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 7:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.PINK,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 8:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.RED,
                        0.0f, 0.0f, Color.lightGray);
                break;
            case 9:
                colorBarra = new GradientPaint(
                        0.0f, 0.0f, Color.YELLOW,
                        0.0f, 0.0f, Color.lightGray);
                break;
            
        }
        return colorBarra;
    }

    static class LabelGenerator extends AbstractCategoryItemLabelGenerator implements CategoryItemLabelGenerator {

        /**
         * The threshold.
         */
        private double threshold;

        /**
         * Creates a new generator that only displays labels that are greater
         * than or equal to the threshold value.
         *
         * @param threshold the threshold value.
         */
        public LabelGenerator(double threshold) {
            super("", NumberFormat.getInstance());
            this.threshold = threshold;
        }

        /**
         * Generates a label for the specified item. The label is typically a
         * formatted version of the data value, but any text can be used.
         *
         * @param dataset the dataset (<code>null</code> not permitted).
         * @param series the series index (zero-based).
         * @param category the category index (zero-based).
         *
         * @return the label (possibly <code>null</code>).
         */
        public String generateLabel(CategoryDataset dataset, int series, int category) {

            String result = null;
            Number value = dataset.getValue(series, category);
            if (value != null) {
                double v = value.doubleValue();
                if (v > this.threshold) {
                    result = value.toString(); // could apply formatting here
                }
            }
            return result;
        }
    }
}
