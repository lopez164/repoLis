/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GPS.PuntosGps;

import javax.swing.ImageIcon;

/**
 *
 * @author lelopez
 */
public class Cliente {

        String codigoCliente, nombreDeCliente, nombreEstablecimiento, direccion, telefono;
        Double latitud, longitud;
        private ImageIcon icon;

        public Cliente() {

        }

        public ImageIcon getIcon() {
            return icon;
        }

        public void setIcon(ImageIcon icon) {
            this.icon = icon;
        }

        public String getCodigoCliente() {
            return codigoCliente;
        }

        public void setCodigoCliente(String codigoCliente) {
            this.codigoCliente = codigoCliente;
        }

        public String getNombreDeCliente() {
            return nombreDeCliente;
        }

        public void setNombreDeCliente(String nombreDeCliente) {
            this.nombreDeCliente = nombreDeCliente;
        }

        public String getNombreEstablecimiento() {
            return nombreEstablecimiento;
        }

        public void setNombreEstablecimiento(String nombreEstablecimiento) {
            this.nombreEstablecimiento = nombreEstablecimiento;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public Double getLatitud() {
            return latitud;
        }

        public void setLatitud(Double latitud) {
            this.latitud = latitud;
        }

        public Double getLongitud() {
            return longitud;
        }

        public void setLongitud(Double longitud) {
            this.longitud = longitud;
        }

    }
    
