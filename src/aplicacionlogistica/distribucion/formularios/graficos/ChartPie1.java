/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.graficos;


import aplicacionlogistica.ejemplosGraficos.Chart2DProperties;
import aplicacionlogistica.ejemplosGraficos.Dataset;
import aplicacionlogistica.ejemplosGraficos.LegendProperties;
import aplicacionlogistica.ejemplosGraficos.MultiColorsProperties;
import aplicacionlogistica.ejemplosGraficos.Object2DProperties;
import aplicacionlogistica.ejemplosGraficos.PieChart2D;
import aplicacionlogistica.ejemplosGraficos.PieChart2DProperties;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarDocumentosEmpleados;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
/**
 *
 * @author Usuario
 */
public class ChartPie1 {
IngresarDocumentosEmpleados rptind;
Object2DProperties object2DProps;
Chart2DProperties chart2DProps;
MultiColorsProperties multiColorsProps;
PieChart2DProperties pieChart2DProps;
PieChart2D chart2D;
LegendProperties legendProps;

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public ChartPie1(String title,IngresarDocumentosEmpleados rptind) {
    this.rptind=rptind;
        //super(title);

     Dataset dataset = createSampleDataset();

    //Configure object properties
    object2DProps = new Object2DProperties();
    object2DProps.setObjectTitleText (title); // Título del Gráfico

    //Configure chart properties
    chart2DProps = new Chart2DProperties();
    chart2DProps.setChartDataLabelsPrecision (-3);
    
    multiColorsProps = new MultiColorsProperties();

    //Configure pie area
     pieChart2DProps = new PieChart2DProperties();

    //Configure chart
    chart2D = new PieChart2D();
    chart2D.setObject2DProperties (object2DProps);
    chart2D.setChart2DProperties (chart2DProps);
    chart2D.setLegendProperties (legendProps);
    chart2D.setDataset (dataset);
    chart2D.setMultiColorsProperties (multiColorsProps);
    chart2D.setPieChart2DProperties (pieChart2DProps);
    
     if (!chart2D.validate (false)) chart2D.validate (true);
     
     // create the chart...
          createChart();
    }
    
    /**
     * Creates a sample dataset for the demo.
     * 
     * @return A sample dataset.
     */
    private Dataset createSampleDataset() {
         DecimalFormat df=  new DecimalFormat("#.##");
        //Configure legend properties
    legendProps = new LegendProperties(); // etiquetas de las categorias
    String[] legendLabels =
      {"Documentos faltantes", "Documentos vigentes", "Documentos vencidos"};
    legendProps.setLegendLabelsTexts (legendLabels);
    
    //Configure dataset
    int numSets = 3, numCats = 1, numItems = 1;
        float s1,s2,s3,s0,s5;
        s0=Float.parseFloat(String.valueOf(rptind.getArrDocumentosPorEmpleado().size()));
        s1=Float.parseFloat(String.valueOf(rptind.getArrDocumentosFaltantes().size()));
        s2=Float.parseFloat(String.valueOf(rptind.getArrDocumentosVigentes().size()));
        s3=Float.parseFloat(String.valueOf(rptind.getArrDocumentosVencidos().size()));
        s5=s2+s3+s1;
        
    Dataset dataset = new Dataset (numSets, numCats, numItems);
    dataset.set (0, 0, 0, (s1/s5)*100);
    dataset.set (1, 0, 0, (s2/s5)*100);
    dataset.set (2, 0, 0, (s3/s5)*100);
    
        
          return dataset;
        
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
    private void createChart() {
        
        // create a dataset...
        
        
       
        
        // add the chart to a panel...
       
        chart2D.setPreferredSize(new java.awt.Dimension(this.rptind.jPanel6.getSize()));
        this.rptind.jPanel6.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.rptind.jPanel6.add(chart2D);
        
    }
    
   

}
   

