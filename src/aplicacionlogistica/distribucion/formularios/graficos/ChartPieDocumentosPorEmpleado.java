/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.graficos;

import aplicacionlogistica.distribucion.formularios.administracion.IngresarDocumentosEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.rptFichaEmpleado;
import java.awt.Color;
import java.awt.FlowLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
/**
 *
 * @author VLI_488
 */
public class ChartPieDocumentosPorEmpleado extends ApplicationFrame{
     /**
     * Default constructor.
     *
     * @param title  the frame title.
     */
    IngresarDocumentosEmpleados rptind=null;
    rptFichaEmpleado rptind2=null;
    String titulo;
    public ChartPieDocumentosPorEmpleado(String title,rptFichaEmpleado rptind) {

        super(title);
        this.rptind2=rptind;
        this.titulo=title;
        final PieDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(this.rptind2.jPanel8.getSize()));
        setContentPane(chartPanel);
        
        // chart2D.setPreferredSize();
        this.rptind2.jPanel8.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.rptind2.jPanel8.add(chartPanel);

    }

     public ChartPieDocumentosPorEmpleado(String title,IngresarDocumentosEmpleados rptind) {

        super(title);
        this.rptind=rptind;
        this.titulo=title;
        final PieDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(this.rptind.jPanel6.getSize()));
        setContentPane(chartPanel);
        
        // chart2D.setPreferredSize();
        this.rptind.jPanel6.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.rptind.jPanel6.add(chartPanel);

    }
    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private PieDataset createDataset() {
        
        final DefaultPieDataset dataset = new DefaultPieDataset();
       // int numSets = 3, numCats = 1, numItems = 1;
        float s1 = 0,s2 = 0,s3 = 0,s0,s5 = 0;
        if(rptind != null){
          s0=Float.parseFloat(String.valueOf(rptind.getArrDocumentosPorEmpleado().size()));
        s1=Float.parseFloat(String.valueOf(rptind.getArrDocumentosFaltantes().size()));
        s2=Float.parseFloat(String.valueOf(rptind.getArrDocumentosVigentes().size()));
        s3=Float.parseFloat(String.valueOf(rptind.getArrDocumentosVencidos().size()));
        s5=s2+s3+s1;  
        } 
            if( rptind2 != null){
                s0=Float.parseFloat(String.valueOf(rptind2.getArrDocumentosPorEmpleado().size()));
        s1=Float.parseFloat(String.valueOf(rptind2.getArrDocumentosFaltantes().size()));
        s2=Float.parseFloat(String.valueOf(rptind2.getArrDocumentosVigentes().size()));
        s3=Float.parseFloat(String.valueOf(rptind2.getArrDocumentosVencidos().size()));
        s5=s2+s3+s1;  
            }
            
        
         dataset.setValue("Documentos faltantes", ((s1/s5)*100));
         dataset.setValue("Documentos vigentes", (s2/s5)*100);
         dataset.setValue("Documentos vencidos", (s3/s5)*100);
        return dataset;
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final PieDataset dataset) {
        final JFreeChart chart = ChartFactory.createPieChart(
            titulo  ,  // chart title
            dataset,             // dataset
            true,                // include legend
            true,
            false
        );
        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setIgnoreZeroValues(true);
        plot.setIgnoreNullValues(true);
        plot.setSectionPaint("Documentos faltantes", new Color(250, 22, 22));
        plot.setSectionPaint("Documentos vigentes", new Color(4, 222, 149));
        plot.setSectionPaint("Documentos vencidos", new Color(255 , 153, 0));
        plot.setSimpleLabels(false);
        plot.setNoDataMessage("No hay datos disponibles");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));
        plot.setExplodePercent("Documentos faltantes", 0.20);
        plot.setInteriorGap(0.0D);
        
        return chart;
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
//    public static void main(final String[] args) {
//
//        final ChartPieDocumentosPorEmpleado demo = new ChartPieDocumentosPorEmpleado("Pie Chart Demo 2");
//        demo.pack();
//        RefineryUtilities.centerFrameOnScreen(demo);
//        demo.setVisible(true);
//
//    }

}
