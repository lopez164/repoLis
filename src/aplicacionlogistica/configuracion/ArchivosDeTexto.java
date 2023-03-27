/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lopez164
 */
public class ArchivosDeTexto {

    private File archivo = null;
    private FileWriter fichero = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private PrintWriter escrive = null;
    private ArrayList<String> lineas;

    public ArchivosDeTexto() {

    }

    public ArchivosDeTexto(String ruta) {
        archivo = new File(ruta);
    }

    public ArchivosDeTexto(File file) {
        archivo = file;
    }

    public void leerArchivo() {
        lineas = new ArrayList<>();
        try {
            if (archivo != null) {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas.add(linea);
                    System.out.println(linea);
                }
                br.close();
                fr.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    public void leerArchivo(String ruta) {
        lineas = new ArrayList<>();
        try {
            System.out.println(ruta);

            archivo = new File(ruta);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
                System.out.println(linea);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    public void leerArchivo(File file) {
        lineas = new ArrayList<>();
        try {
            System.out.println(file.getAbsolutePath());

            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
                System.out.println(linea);
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    public void borrarLinea(int n) {
        try {
            lineas = new ArrayList<>();

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            int cont = 0;
            String completo = "";
            while ((linea = br.readLine()) != null) {

                if (cont != n) {
                    lineas.add(linea);//AGREGAR LINEASS A UN VECTOR
                }
                cont++;
            }
            br.close();
            fr.close();

            guardarArchivoNuevo(lineas);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

    }

    public void borrarFactura(String numeroFactura) {
        try {
            lineas = new ArrayList<>();

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            int cont = 0;
           
           
            while ((linea = br.readLine()) != null) {
                String[] arrSplit = linea.split(",");
                if (numeroFactura != arrSplit[2]) {
                    lineas.add(linea);//AGREGAR LINEASS A UN VECTOR
                }
                cont++;
            }
            br.close();
            fr.close();

            guardarArchivoNuevo(lineas);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

    }

    public boolean borrarLinea(String cadena) {
        boolean borrado = false;
        ArrayList<String> lineaAux = new ArrayList<>();
        try {

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {

                if (!linea.equals(cadena)) {
                    lineaAux.add(linea);
                }

            }

            borrado = guardarArchivoNuevo(lineaAux);

            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return borrado;

    }

    public boolean borrarLinea(CFacturasDescargadas factura) {
        boolean borrado = false;
        ArrayList<String> lineaAux = new ArrayList<>();
        try {

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {

                String strMain = linea;
                String[] arrSplit = strMain.split(",");

                if (!arrSplit[3].equals(factura.getNumeroFactura())) {
                    lineaAux.add(linea);
                }

            }

            borrado = guardarArchivoNuevo(lineaAux);

            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return borrado;

    }

    public boolean borrarContenidoDelArchivo() {
        boolean borrado = false;
        ArrayList<String> lineaAux = new ArrayList<>();
        try {

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {

            }

            borrado = guardarArchivoNuevo(lineaAux);

            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return borrado;

    }

    public void guardarArchivo() {
        try {

            fichero = new FileWriter(archivo);
            escrive = new PrintWriter(fichero);
            for (String linea : lineas) {
                escrive.println(linea);
            }
            fichero.close();

        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    public void AgregarLineaAlInicio() {
        String NuevaLinea = "nueva linea o texto instroducido x";
        List<String> aux = new ArrayList<>();

        aux.add(NuevaLinea);
        for (String obj : lineas) {
            aux.add(obj);
        }
        guardarArchivoNuevo(aux);
    }

    public boolean guardarArchivoNuevo(List<String> arregloDeDatos) {
        boolean borrado = false;
        int i=0;
        try {
            fichero = new FileWriter(archivo);
            escrive = new PrintWriter(fichero);
            for (String obj : arregloDeDatos) {
                escrive.println(obj);
                System.out.println(i++ + "guardado");
            }
            borrado = true;

            fichero.close();

        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return borrado;
    }

    public void guardarLineaEnFormatoSQL(String linea) {

        try {
            fichero = new FileWriter(archivo);
            escrive = new PrintWriter(fichero);

            escrive.println(linea);
            fichero.close();

        } catch (IOException ex) {
            Logger.getLogger(ArchivosDeTexto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    /**
     * Método que graba los datos respectivos de una tabla en un archivo de
     * texto
     *
     * @param valores corresponde a los datos de una tabla separados por una
     * coma
     * @param rutaArchivo es la ruta del archivo en la cual se van a grabar los
     * datos en forma temporal
     * @return devuelve true si graba el dato en el archivo, falso sino es
     * grabado el dato en el archivo
     */
    public boolean insertarLineaEnFichero(String valores, String rutaArchivo) {
        boolean grabado = false;

        try {

            //java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tmp_" + this.numeroManifiesto + " .txt",true));
            java.io.BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(rutaArchivo, true));

            bufferedWriter.append(valores);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            grabado = true;

        } catch (IOException ex) {
            System.out.println("Error en GuardaConsultaEnFichero() " + ex);
            JOptionPane.showMessageDialog(null, "Error en GuardaConsultaEnFichero() " + ex, "No Existe", 1);
        }
        return grabado;
    }

    public boolean borrarArchivo() {

        return archivo.delete();

    }

}
