/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.personas;

import aplicacionlogistica.configuracion.Inicio;
import java.util.Date;

/**
 *
 * @author lelopez
 */
public class Vst_empleados {
    Inicio ini;
    String cedula;
    String nombres;
    String apellidos;
    String direccion;
    String barrio;
    int ciudad;
    String nombreCiudad;
    String telefonoFijo;
    String telefonoCelular;
    String celularCorporativo;
    String escolaridad;
    String genero;
    Date cumpleanios;
    String lugarNacimiento;
    int idEstadoCivil;
    String nombreEstadoCivil;
    String email;
    int idTipoSangre;
    String tipoSangre;
    int idcargo;
    String cargo;
    Date fechaDeIngreso;
    int idAgencia;
    String nombreAgencia;
    int idCentroDeCosto;
    String nombreCentroDeCosto;
    int idTipoDeContrato;
    String tipoContrato;
    String numeroCuenta;
    int idBanco;
    String nombreBanco;
    Date fechaDeRetiro;
    int activo;

    public Vst_empleados() {
    }

    public Vst_empleados(Inicio ini) {
        this.ini = ini;
    }
   
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public int getCiudad() {
        return ciudad;
    }

    public void setCiudad(int ciudad) {
        this.ciudad = ciudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getCelularCorporativo() {
        return celularCorporativo;
    }

    public void setCelularCorporativo(String celularCorporativo) {
        this.celularCorporativo = celularCorporativo;
    }

    public String getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(String escolaridad) {
        this.escolaridad = escolaridad;
    }
       

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getCumpleanios() {
        return cumpleanios;
    }

    public void setCumpleanios(Date cumpleanios) {
        this.cumpleanios = cumpleanios;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }
    
    

    public int getIdEstadoCivil() {
        return idEstadoCivil;
    }

    public void setIdEstadoCivil(int idEstadoCivil) {
        this.idEstadoCivil = idEstadoCivil;
    }

    public String getNombreEstadoCivil() {
        return nombreEstadoCivil;
    }

    public void setNombreEstadoCivil(String nombreEstadoCivil) {
        this.nombreEstadoCivil = nombreEstadoCivil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdTipoSangre() {
        return idTipoSangre;
    }

    public void setIdTipoSangre(int idTipoSangre) {
        this.idTipoSangre = idTipoSangre;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public int getIdCargo() {
        return idcargo;
    }

    public void setIdCargo(int idcargo) {
        this.idcargo = idcargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public void setFechaDeIngreso(Date fechaDeIngreso) {
        this.fechaDeIngreso = fechaDeIngreso;
    }

    public int getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(int idAgencia) {
        this.idAgencia = idAgencia;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public int getIdCentroDeCosto() {
        return idCentroDeCosto;
    }

    public void setIdCentroDeCosto(int idCentroDeCosto) {
        this.idCentroDeCosto = idCentroDeCosto;
    }

    public String getNombreCentroDeCosto() {
        return nombreCentroDeCosto;
    }

    public void setNombreCentroDeCosto(String nombreCentroDeCosto) {
        this.nombreCentroDeCosto = nombreCentroDeCosto;
    }

    public int getIdTipoDeContrato() {
        return idTipoDeContrato;
    }

    public void setIdTipoDeContrato(int idTipoDeContrato) {
        this.idTipoDeContrato = idTipoDeContrato;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public Date getFechaDeRetiro() {
        return fechaDeRetiro;
    }

    public void setFechaDeRetiro(Date fechaDeRetiro) {
        this.fechaDeRetiro = fechaDeRetiro;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

 
}
