/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.objetos;

import aplicacionlogistica.configuracion.Inicio;

/**
 *
 * @author lelopez
 */
public class DocumentosPorTipoDeVehiculo {

    int idDocumentosPorTipoVehiculo;
    int idTipoVehiculo;
    String nombreTipoVehiculo;
    int idTipoDocumento;
    String nombreTipoDocumento;
    int isLogistica;
    int activo;
    Inicio ini;

    public int getIdDocumentosPorTipoVehiculo() {
        return idDocumentosPorTipoVehiculo;
    }

    public void setIdDocumentosPorTipoVehiculo(int idDocumentosPorTipoVehiculo) {
        this.idDocumentosPorTipoVehiculo = idDocumentosPorTipoVehiculo;
    }

    public int getIdTipoVehiculo() {
        return idTipoVehiculo;
    }

    public void setIdTipoVehiculo(int idTipoVehiculo) {
        this.idTipoVehiculo = idTipoVehiculo;
    }

    public String getNombreTipoVehiculo() {
        return nombreTipoVehiculo;
    }

    public void setNombreTipoVehiculo(String nombreTipoVehiculo) {
        this.nombreTipoVehiculo = nombreTipoVehiculo;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombreTipoDocumento() {
        return nombreTipoDocumento;
    }

    public void setNombreTipoDocumento(String nombreTipoDocumento) {
        this.nombreTipoDocumento = nombreTipoDocumento;
    }

    public int getIsLogistica() {
        return isLogistica;
    }

    public void setIsLogistica(int isLogistica) {
        this.isLogistica = isLogistica;
    }

    public DocumentosPorTipoDeVehiculo() {
    }

    public DocumentosPorTipoDeVehiculo(Inicio ini) {
        this.ini = ini;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
    
    
}
