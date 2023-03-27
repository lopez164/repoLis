/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.PuntosGps;

import javax.swing.ImageIcon;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author lelopez
 */
public class PuntoDeRecorrido {

    private int id;
    private GeoPosition path;
    private ImageIcon icon;
    private String course;
    private String servertime;
    private Double speed;
    private String atributos;
    private Double distancia;
    private Double distanciaRecorrida;
    private Double kilometraje;
    private String conductor;
    private Double kilometrajeFinal;
    private String placa;
    

    public PuntoDeRecorrido() {

    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public GeoPosition getPath() {
        return path;
    }

    public void setPath(GeoPosition path) {
        this.path = path;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;

    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAtributos() {
        return atributos;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Double getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Double kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(Double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public Double getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(Double kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }

}
