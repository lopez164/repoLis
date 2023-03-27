/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.graficos;


import aplicacionlogistica.distribucion.formularios.administracion.IngresarDocumentosEmpleados;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
/**
 *
 * @author Usuario
 */
public class ChartPie1_copia {
IngresarDocumentosEmpleados rptind;

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public ChartPie1_copia(final String title,IngresarDocumentosEmpleados rptind) {
    this.rptind=rptind;
        //super(title);

        // create a dataset...
        final PieDataset dataset = createSampleDataset();
        
        // create the chart...
        final JFreeChart chart = createChart(dataset);
        
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(this.rptind.jPanel6.getSize()));
        //chart.setBackgroundPaint(Color.white);
        //chartPanel.setPreferredSize(new Dimension(rptind.jPanel5.getSize()));
        chartPanel.setVisible(true);
        
        this.rptind.jPanel6.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.rptind.jPanel6.add(chartPanel);

    }
    
    /**
     * Creates a sample dataset for the demo.
     * 
     * @return A sample dataset.
     */
    private PieDataset createSampleDataset() {
        /*final DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("al Día",rptind.aldia);
        result.setValue("< 3 meses", rptind.morosos1);
        result.setValue("< 6 meses",  rptind.morosos2);
        result.setValue("< 12 meses",  rptind.morosos3);
        result.setValue("> 12 meses", rptind.morosos4);
        return result;
        **/
        
        Double ss= Double.parseDouble(String.valueOf(rptind.getArrDocumentosPorEmpleado().size()));
        DecimalFormat df=  new DecimalFormat("#.##");
        System.out.println("PieDataset");
        Double s1,s2,s3,s0,s5;
        s0=Double.parseDouble(String.valueOf(rptind.getArrDocumentosPorEmpleado().size()));
        s1=Double.parseDouble(String.valueOf(rptind.getArrDocumentosFaltantes().size()));
        s2=Double.parseDouble(String.valueOf(rptind.getArrDocumentosVigentes().size()));
        s3=Double.parseDouble(String.valueOf(rptind.getArrDocumentosVencidos().size()));
        s5=s2+s3+s1;
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Documentos faltantes " ,(s1/s5)*100);
       
         //System.out.println("Al día " + rptind.aldia + " / " + rptind.arrFacturas.size());
        result.setValue("Documentos vigentes "   ,(s2/s5)*100); ;
         //System.out.println("< de 3 meses " + rptind.morosos1+" / "+ rptind.arrFacturas.size());
        result.setValue("Documentos vencidos "  , (s3/s5)*100);;
         //System.out.println("< de 3 meses " + rptind.morosos1+" / "+ rptind.arrFacturas.size());
        
          return result;
        
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
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private JFreeChart createChart(final PieDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createPieChart(
            "Grafica que muestra la Gestión Documental",  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        plot.setNoDataMessage("No Hay datos para mostrar");
        return chart;
        
    }
    
   

}
   

