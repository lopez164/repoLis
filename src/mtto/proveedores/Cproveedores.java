/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.proveedores;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CPersonas;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtto.documentos.Threads.HiloListadoDeCuentasSecundarias;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;

/**
 *
 * @author lelopez
 */
public class Cproveedores extends CPersonas {

    String contacto;
    String celularCorporativo;
    String fechaDeIngreso;
    String fechaIng;
    int agencia;
    int activo;
    String usuario;
    int flag;
    String latitud = "0";
    String longitud = "0";
    String coordenadas;
    List<SucursalesPorproveedor> listaDeSucursales = null;
    //List<CCuentaSecundariaLogistica> listaDeServiciosSucursal = null;
    List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias = null;
    SucursalesPorproveedor sucursal;

    public List<CCuentaSecundariaLogistica> getListaDeCuentasSecundarias() {
        return listaDeCuentasSecundarias;
    }

    public void setListaDeCuentasSecundarias(List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias) {
        this.listaDeCuentasSecundarias = listaDeCuentasSecundarias;
    }

    public void setListadeCuentasSecundarias() {
        ResultSet rst = null;
        Statement st;
        Connection con;
        try {
            listaDeCuentasSecundarias = new ArrayList<>();
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            con = ini.getConnRemota();
            String sql = null;

            sql = "SELECT csp.nitProveedor,"
                    + "csp.idCuentaSecundaria,"
                    + "cs.nombreCuentaSecundaria,"
                    + "cp.idCuentaPrincipal,"
                    + "cp.nombreCuentaPrincipal,"
                    + "csp.activo,"
                    + "csp.fechaIng,"
                    + "csp.usuario,"
                    + "csp.flag,"
                    + "cs.codigoCuentaSecundaria "
                    + "FROM cuentasporproveedor csp "
                    + "join cuentassecundariaslogistica cs on cs.idCuentaSecundaria=csp.idCuentaSecundaria "
                    + "join cuentasprincipaleslogistica cp on cs.idcuentaPrincipal=cp.idCuentaPrincipal "
                    + "where csp.nitProveedor='" + this.cedula + "';";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CCuentaSecundariaLogistica cuenta = new CCuentaSecundariaLogistica();

                    cuenta.setIdCuentaPrincipal(rst.getInt("idCuentaPrincipal"));
                    cuenta.setNombreCuentaPrincipal(rst.getString("nombreCuentaPrincipal"));
                    cuenta.setIdCuentaSecundaria(rst.getInt("idCuentaSecundaria"));
                    cuenta.setNombreCuentaSecundaria(rst.getString("nombreCuentaSecundaria"));
                    cuenta.setCodigoCuentaSecundaria(rst.getString("codigoCuentaSecundaria"));
                    cuenta.setActivo(rst.getInt("activo"));

                    listaDeCuentasSecundarias.add(cuenta);

                }
                rst.close();
                st.close();
               // con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getContacto() {
        return contacto;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCelularCorporativo() {
        return celularCorporativo;
    }

    public void setCelularCorporativo(String celularCorporativo) {
        this.celularCorporativo = celularCorporativo;
    }

    public String getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public void setFechaDeIngreso(String fechaDeIngreso) {
        this.fechaDeIngreso = fechaDeIngreso;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<SucursalesPorproveedor> getListaDeSucursales() {
        return listaDeSucursales;
    }

    public void setListaDeSucursales() {
        ResultSet rst;

        Connection con; //= CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();

        listaDeSucursales = new ArrayList();
        String sql = "SELECT idSucursalProveedor,cedula, codigoInterno,nombreSucursal,"
                + "contactoSucursalProveedor,direccionSucursalProveedor,"
                + "barrioSucursalProveedor,ciudadSucursalProveedor,telefonoSucursalProveedor,"
                + "celularSucursalProveedor, celularCorporativo, emailSucursalProveedor, agencia,"
                + "fechaDeIngreso,activo,fechaIng, usuario,flag,coordenadas "
                + "From proveedoressucursales "
                + "where cedula='" + cedula + "';";

        if (con != null) {
            try {
                Statement st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    SucursalesPorproveedor sucursal = new SucursalesPorproveedor(ini);

                    sucursal.setIdSucursal(rst.getInt("idSucursalProveedor"));
                    sucursal.setCedula(rst.getString("cedula"));
                    sucursal.setCodigoInterno(rst.getString("codigoInterno"));
                    sucursal.setNombreSucursal(rst.getString("nombreSucursal"));
                    sucursal.setContactoSucursal(rst.getString("contactoSucursalProveedor"));
                    sucursal.setDireccionSucursal(rst.getString("direccionSucursalProveedor"));
                    sucursal.setBarrioSucursal(rst.getString("barrioSucursalProveedor"));
                    sucursal.setCiudadSucursal(rst.getInt("ciudadSucursalProveedor"));
                    sucursal.setTelefonoSucursal(rst.getString("telefonoSucursalProveedor"));
                    sucursal.setCelularSucursal(rst.getString("celularSucursalProveedor"));
                    sucursal.setCelularCorporativo(rst.getString("celularCorporativo"));
                    sucursal.setEmailSucursal(rst.getString("emailSucursalProveedor"));
                    sucursal.setAgencia(rst.getInt("agencia"));
                    sucursal.setFechaDeIngreso(rst.getString("fechaDeIngreso"));
                    sucursal.setActivo(rst.getInt("activo"));
                    sucursal.setFechaIng(rst.getString("fechaIng"));
                    sucursal.setUsuario(rst.getString("usuario"));
                    sucursal.setFlag(rst.getInt("flag"));
                   
                   sucursal.setCoordenadas(rst.getString("coordenadas"));
                  
                   String[] parts = sucursal.getCoordenadas().split(",");
                   sucursal.setLatitud(parts[0]);
                   sucursal.setLongitud(parts[1]);

                    listaDeSucursales.add(sucursal);

                }
                rst.close();
                st.close();
               // con.close();

                //flistaEmpleados.setListaDeEmpleados(listaProveedor listaDeServiciosSucursal;
            } catch (SQLException ex) {
                Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setListaDeSucursales(List<SucursalesPorproveedor> listaDeSucursales) {
        this.listaDeSucursales = listaDeSucursales;
    }

    public Cproveedores(Inicio ini) {
        super(ini);
    }

    public Cproveedores(Inicio ini, String cedula) throws Exception {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
        sql = "select * from view_proveedores where cedula='" + cedula + "';";
        st = con.createStatement();
        if (con != null) {
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.cedula = rst.getString("cedula");
                    this.nombres = rst.getString("nombres");
                    this.apellidos = rst.getString("apellidos");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.idCiudad = rst.getInt("ciudad");
                    this.telefonoFijo = rst.getString("telefonoFijo");
                    this.telefonoCelular = rst.getString("telefonoCelular");
                    this.escolaridad = rst.getString("escolaridad");
                    this.genero = rst.getString("genero");
                    this.cumpleanios = rst.getDate("cumpleanios");
                    this.lugarNacimiento = rst.getString("lugarNacimiento");
                    this.idEstadoCivil = 2;
                    this.email = rst.getString("email");
                    this.idTipoSangre = 1;
                    this.contacto = rst.getString("contacto");
                    this.celularCorporativo = rst.getString("celularCorporativo");
                    this.fechaDeIngreso = rst.getString("fechaDeIngreso");
                    this.agencia = rst.getInt("agencia");
                    this.activo = rst.getInt("activo");
                    this.fechaIng = rst.getString("fechaIng");
                    this.usuario = rst.getString("usuario");
                    this.flag = rst.getInt("flag");
                    this.activo = rst.getInt("activo");
                    this.coordenadas = rst.getString("coordenadas");
                    
                    String parts[] =coordenadas.split(",");
                    this.latitud = parts[0];
                    this.longitud = parts[1];

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CPersonas " + ex.getMessage());
            }
        }
    }

    public boolean grabarProveedor() {
        boolean grabado = false;
        List<String> sql = new ArrayList();
        String usuario;

        try {

            usuario = Inicio.deCifrar(this.ini.getUser().getNombreUsuario());
            /*ingreso a tabla personas*/
            String texto = "INSERT INTO  personas  ( cedula ,  nombres ,  apellidos ,  direccion ,  barrio ,  ciudad ,  telefonoFijo ,"
                    + " telefonoCelular ,  escolaridad ,  genero , cumpleanios ,  lugarNacimiento ,  estadoCivil ,  email ,  tipoSangre ,  activo ,"
                    + " usuario) VALUES ('"
                    + this.cedula + "','"
                    + this.nombres + "','"
                    + this.apellidos + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.idCiudad + "','"
                    + this.telefonoFijo + "','"
                    + this.telefonoCelular + "','"
                    + this.escolaridad + "','"
                    + this.genero + "','"
                    + this.cumpleanios + "','"
                    + this.lugarNacimiento + "','"
                    + this.idEstadoCivil + "','"
                    + this.email + "','"
                    + this.idTipoSangre + "','"
                    + this.activoPersona + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "')"
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.idCiudad + "',"
                    + "telefonoFijo='" + this.telefonoFijo + "',"
                    + "telefonoCelular='" + this.telefonoCelular + "',"
                    + "escolaridad='" + this.escolaridad + "',"
                    + "genero='" + this.genero + "',"
                    + "cumpleanios='" + this.cumpleanios + "',"
                    + "lugarNacimiento='" + this.lugarNacimiento + "',"
                    + "estadoCivil='" + this.idEstadoCivil + "',"
                    + "email='" + this.email + "',"
                    + "tipoSangre='" + this.idTipoSangre + "',"
                    + "usuario='" + usuario + "',"
                    + "activo='" + this.activoPersona + "',"
                    + "flag='2';";
            sql.add(texto);

            /*ingreso a tabla proveedores*/
            texto = "INSERT INTO  proveedores  (cedula, contacto, celularCorporativo, "
                    + "fechaDeIngreso, agencia, activo, usuario ,coordenadas) "
                    + "VALUES ('"
                    + this.cedula + "','"
                    + this.contacto + "','"
                    + this.celularCorporativo + "',"
                    //+ this.fechaDeIngreso + "','"
                    + "CURDATE(),'"
                    + this.agencia + "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + this.latitud + "," + this.longitud + "')"
                    + " ON DUPLICATE KEY UPDATE "
                    + "contacto='" + this.contacto + "',"
                    + "celularCorporativo='" + this.celularCorporativo + "',"
                    + "fechaDeIngreso = CURDATE(),"
                    + "agencia='" + this.agencia + "',"
                    + "activo='" + this.activo + "',"
                    + "usuario='" + usuario + "',"
                    + "coordenadas='" + this.latitud + "," + this.longitud + "',"
                    + "flag='-1';";

            sql.add(texto);

            /*Borra registros de los tipos de servicio*/
            texto = "delete from cuentasporproveedor where nitProveedor='" + this.cedula + "';";
            sql.add(texto);

            /*Asigna   las cuentas secundarias al proveedor */
            for (CCuentaSecundariaLogistica cta : ini.getListadeCuentasSecundarias()) {
                texto = "INSERT INTO cuentasporproveedor "
                        + "(nitProveedor, idCuentaSecundaria,activo,fechaIng,usuario,flag) "
                        + "VALUES('"
                        + this.cedula + "','"
                        + cta.getIdCuentaSecundaria() + "','"
                        + cta.getActivo() + "',"
                        + "CURRENT_TIMESTAMP,'"
                        + usuario + "','"
                        + "1') on duplicate key update "
                        + "activo='" + cta.getActivo() + "',"
                        + "fechaIng = CURRENT_TIMESTAMP,"
                        + "usuario='" + usuario + "',"
                        + "flag= '-1;'";
                sql.add(texto);

            }

            /*Sentencia para Crear una Sucursal proveedor */
            listaDeSucursales = new ArrayList<>();
            SucursalesPorproveedor sucursal = new SucursalesPorproveedor(ini);
            sucursal.setCedula(this.cedula);
            sucursal.setNombreSucursal(this.nombres + "(OF. PPAL)");
            sucursal.setCodigoInterno("1");
            sucursal.setContactoSucursal(contacto);
            sucursal.setDireccionSucursal(direccion);
            sucursal.setBarrioSucursal(barrio);
            sucursal.setCiudadSucursal(idCiudad);
            sucursal.setTelefonoSucursal(telefonoFijo);
            sucursal.setCelularSucursal(telefonoCelular);
            sucursal.setCelularCorporativo(celularCorporativo);
            sucursal.setEmailSucursal(email);
            sucursal.setAgencia(agencia);
            sucursal.setFechaDeIngreso(fechaDeIngreso);
            sucursal.setActivo(this.activo);
            sucursal.setListaDeCuentasSecundarias(this.listaDeCuentasSecundarias);
            sucursal.setLatitud(this.latitud);
            sucursal.setLongitud(this.longitud);

            listaDeSucursales.add(sucursal);

            texto = "INSERT INTO  proveedoressucursales (cedula,codigoInterno,nombreSucursal,"
                    + " contactoSucursalProveedor, direccionSucursalProveedor,barrioSucursalProveedor, ciudadSucursalProveedor, telefonoSucursalProveedor, "
                    + "celularSucursalProveedor,celularCorporativo,emailSucursalProveedor, agencia, "
                    + "fechaDeIngreso, usuario,coordenadas) VALUES ('"
                    + sucursal.getCedula() + "','"
                    + sucursal.getCodigoInterno() +  "','"   
                    + sucursal.getNombreSucursal() + "','"
                    + sucursal.getContactoSucursal() + "','"
                    + sucursal.getDireccionSucursal() + "','"
                    + sucursal.getBarrioSucursal() + "','"
                    + sucursal.getCiudadSucursal() + "','"
                    + sucursal.getTelefonoSucursal() + "','"
                    + sucursal.getCelularSucursal() + "','"
                    + sucursal.getCelularCorporativo() + "','"
                    + sucursal.getEmailSucursal() + "','"
                    + sucursal.getAgencia() + "',"
                    // + sucursal.getFechaDeIngreso() + "','"
                    + "CURDATE() ,'"
                    //+ "1','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    //+ "1','"
                    + sucursal.getLatitud() + "," + sucursal.getLongitud() + "')"
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreSucursal='" + sucursal.getNombreSucursal() + "',"
                    + "codigoInterno='" + sucursal.getCodigoInterno() + "',"
                    + "contactoSucursalProveedor='" + sucursal.getContactoSucursal() + "',"
                    + "direccionSucursalProveedor='" + sucursal.getDireccionSucursal() + "',"
                    + "barrioSucursalProveedor='" + sucursal.getBarrioSucursal() + "',"
                    + "ciudadSucursalProveedor='" + sucursal.getCiudadSucursal() + "',"
                    + "telefonoSucursalProveedor='" + sucursal.getTelefonoSucursal() + "',"
                    + "celularSucursalProveedor='" + sucursal.getCelularSucursal() + "',"
                    + "celularCorporativo='" + sucursal.getCelularCorporativo() + "',"
                    + "emailSucursalProveedor='" + sucursal.getEmailSucursal() + "',"
                    + "agencia='" + sucursal.getAgencia() + "',"
                    + "coordenadas='" + sucursal.getLatitud() + "," + sucursal.getLongitud() + "',"
                    + "usuario='" + usuario + "',"
                    + "flag='-1';";

            sql.add(texto);

            if (ini.insertarDatosRemotamente(sql, "Cproveedores")) {

                setListaDeSucursales();

                //sucursal = new SucursalesPorproveedor(ini);
                sucursal = getListaDeSucursales().get(0);
                sql = new ArrayList<>(); /*Asigna   las cuentas secundarias a la suucursal */
                for (CCuentaSecundariaLogistica cta : this.getListaDeCuentasSecundarias()) {
                    texto = "INSERT INTO cuestasporsucursalproveedor(idSucursalProveedor,"
                            + "cedula,idCuentaSecundaria,activo,fechaIng,usuario,flag) VALUES('"
                            + sucursal.getIdSucursal() + "','"
                            + sucursal.getCedula() + "','"
                            + cta.getIdCuentaSecundaria() + "','"
                            + cta.getActivo() + "',"
                            + "CURRENT_TIMESTAMP,'"
                            + usuario + "','"
                            + "1') on duplicate key update "
                            + "activo ='" + cta.getActivo() + "',"
                            + "fechaIng=CURRENT_TIMESTAMP,"
                            + "usuario ='" + usuario + "',"
                            + "flag='-1';";

                    sql.add(texto);
                }
                grabado = ini.insertarDatosRemotamente(sql, "Cproveedores");

            }

        } catch (Exception ex) {
            Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarProveedor() {
        boolean grabado = false;
        List<String> sql = new ArrayList();
        String usuario;

        try {

            usuario = Inicio.deCifrar(this.ini.getUser().getNombreUsuario());
            /*ingreso a tabla personas*/
            String texto = "update  personas "
                    + "set nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "', "
                    + "direccion='" + this.direccion + "', "
                    + "barrio='" + this.barrio + "', "
                    + "ciudad='" + this.idCiudad + "', "
                    + "telefonoFijo='" + this.telefonoFijo + "', "
                    + "telefonoCelular='" + this.telefonoCelular + "', "
                    + "escolaridad='" + this.escolaridad + "', "
                    + "genero='" + this.genero + "', "
                    + "cumpleanios='" + this.cumpleanios + "', "
                    + "lugarNacimiento='" + this.lugarNacimiento + "', "
                    + "estadoCivil='" + this.idEstadoCivil + "',"
                    + "email='" + this.email + "', "
                    + "tipoSangre='" + this.idTipoSangre + "', "
                    + "activo='" + this.activoPersona + "', "
                    + "fechaIng = CURRENT_TIMESTAMP,"
                    + "usuario='" + usuario + "' "
                    + "where cedula='" + this.cedula + "';";
            sql.add(texto);

            /*ingreso a tabla proveedores*/
            texto = "UPDATE proveedores "
                    + "SET "
                    + "contacto = '" + this.contacto + "',"
                    + "celularCorporativo = '" + this.celularCorporativo + "',"
                    + "fechaDeIngreso = '" + this.fechaDeIngreso + "',"
                    + "agencia = '" + this.agencia + "',"
                    + "activo = '" + this.activo + "',"
                    + "fechaIng = CURRENT_TIMESTAMP,"
                    + "usuario='" + usuario + "', "
                    + "flag = '-1', "
                    + "coordenadas ='" + this.latitud + "," + this.longitud + "' "
                  //  + "latitud = '" + this.latitud + "',"
                  //  + "longitud = '" + this.longitud + "' "
                    + "WHERE cedula = '" + this.cedula + "';";

            sql.add(texto);

            /*Borra registros de los tipos de servicio*/
            texto = "delete from cuentasporproveedor where nitProveedor='" + this.cedula + "';";

            sql.add(texto);

            /*Asigna   las cuentas secundarias al proveedor */
            for (CCuentaSecundariaLogistica cta : this.getListaDeCuentasSecundarias()) {
                texto = "INSERT INTO cuentasporproveedor "
                        + "(nitProveedor, idCuentaSecundaria,activo,fechaIng,usuario,flag) VALUES('"
                        + this.cedula + "','"
                        + cta.getIdCuentaSecundaria() + "','"
                        + cta.getActivo() + "',"
                        + "CURRENT_TIMESTAMP,'"
                        + usuario + "','"
                        + "1') on duplicate key update "
                        + "activo='" + cta.getActivo() + "',"
                        + "fechaIng = CURRENT_TIMESTAMP,"
                        + "usuario='" + usuario + "',"
                        + "flag= -1;";
                sql.add(texto);

            }

            /*Sentencia para Crear una Sucursal proveedor */
            SucursalesPorproveedor sucursal = new SucursalesPorproveedor(ini);
            sucursal = getListaDeSucursales().get(0);
            sucursal.setCedula(this.cedula);
            sucursal.setNombreSucursal(this.nombres + "(OF. PPAL)");
            sucursal.setCodigoInterno("1");
            sucursal.setContactoSucursal(contacto);
            sucursal.setDireccionSucursal(direccion);
            sucursal.setBarrioSucursal(barrio);
            sucursal.setCiudadSucursal(idCiudad);
            sucursal.setTelefonoSucursal(telefonoFijo);
            sucursal.setCelularSucursal(telefonoCelular);
            sucursal.setCelularCorporativo(celularCorporativo);
            sucursal.setEmailSucursal(email);
            sucursal.setAgencia(agencia);
            sucursal.setFechaDeIngreso(fechaDeIngreso);
            sucursal.setActivo(this.activo);
            sucursal.setUsuario(usuario);
            sucursal.setLatitud(latitud);
            sucursal.setLongitud(longitud);
            sucursal.setListaDeCuentasSecundarias();

            texto = "delete from cuestasporsucursalproveedor where nitProveedor='" + sucursal.getIdSucursal() + "';";

            texto = "UPDATE proveedoressucursales "
                    + "SET "
                    + "cedula ='" + sucursal.cedula + "', "
                    + "codigoInterno ='" + sucursal.codigoInterno + "', "
                    + "nombreSucursal ='" + sucursal.nombreSucursal + "', "
                    + "contactoSucursalProveedor ='" + sucursal.contactoSucursal + "', "
                    + "direccionSucursalProveedor ='" + sucursal.direccionSucursal + "', "
                    + "barrioSucursalProveedor ='" + sucursal.barrioSucursal + "', "
                    + "ciudadSucursalProveedor ='" + sucursal.ciudadSucursal + "', "
                    + "telefonoSucursalProveedor ='" + sucursal.telefonoSucursal + "', "
                    + "celularSucursalProveedor ='" + sucursal.celularSucursal + "', "
                    + "celularCorporativo ='" + sucursal.celularCorporativo + "', "
                    + "emailSucursalProveedor ='" + sucursal.emailSucursal + "', "
                    + "agencia ='" + sucursal.agencia + "', "
                    + "fechaDeIngreso ='" + sucursal.fechaDeIngreso + "', "
                    + "activo ='" + sucursal.activo + "', "
                    + "fechaIng =CURRENT_TIMESTAMP, "
                    + "usuario ='" + sucursal.usuario + "', "
                    + "flag = '-1', "
                    + "coordenadas ='" + sucursal.latitud + ","+sucursal.longitud +"' "
                    //+ "latitud ='" + sucursal.latitud + "', "
                    //+ "longitud ='" + sucursal.longitud + "' "
                    + "WHERE idSucursalProveedor ='" + sucursal.idSucursal + "';";

            sql.add(texto);

            if (ini.insertarDatosRemotamente(sql, "Cproveedores")) {

                texto = "";
                setListaDeSucursales();
                sucursal = new SucursalesPorproveedor(ini);
                sucursal = getListaDeSucursales().get(0);
                sucursal.setListaDeCuentasSecundarias();

                sql = new ArrayList<>();

                for (CCuentaSecundariaLogistica cta : getListaDeCuentasSecundarias()) {
                    texto = "INSERT INTO cuestasporsucursalproveedor(idSucursalProveedor,"
                            + "cedula,idCuentaSecundaria,activo,fechaIng,usuario,flag) VALUES('"
                            + sucursal.getIdSucursal() + "','"
                            + sucursal.getCedula() + "','"
                            + cta.getIdCuentaSecundaria() + "','"
                            + cta.getActivo() + "',"
                            + "CURRENT_TIMESTAMP,'"
                            + usuario + "','"
                            + "1') on duplicate key update "
                            + "activo ='" + cta.getActivo() + "',"
                            + "fechaIng=CURRENT_TIMESTAMP,"
                            + "usuario ='" + usuario + "',"
                            + "flag='-1';";

                    sql.add(texto);
                }

                grabado = ini.insertarDatosRemotamente(sql, "Cproveedores");
            }
        } catch (Exception ex) {
            Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public List<String> getSentenciaGuardarServicios(List<CCuentaSecundariaLogistica> lista, boolean actualizar) throws SQLException, Exception {
        String texto;
        Connection con;
        Statement st;
        ResultSet rst;
        List<String> sql = new ArrayList();
        listaDeCuentasSecundarias = new ArrayList();
        listaDeCuentasSecundarias = lista;

        String sql2;

        if (actualizar) {

            // sql2 = "select idSucursalProveedor from proveedoresSucursales where codigoInterno='" + sucursal.getCodigoInterno() + "';";
        } else {
            sql2 = "select idSucursalProveedor from proveedoressucursales where codigoInterno='" + sucursal.getCodigoInterno() + "';";

            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = ini.getConnRemota();
            if (con != null) {

                st = con.createStatement();
                rst = st.executeQuery(sql2);
                if (rst.next()) {
                    sucursal.setIdSucursal(rst.getInt("idSucursalProveedor"));

                }
                rst.close();
                st.close();
               // con.close();

            }
        }

        texto = "delete from cuentasporproveedor where idSucursalProveedor='" + sucursal.getIdSucursal() + "';";
        sql.add(texto);

        for (CCuentaSecundariaLogistica cuenta : listaDeCuentasSecundarias) {
            //for (int i = 0; i < tabla.getRowCount(); i++) {
            // boolean valor = (boolean) tabla.getValueAt(i, 0);

            texto = "INSERT INTO cuentasporproveedor (idSucursalProveedor, idCuentaSecundaria,activo,usuario) "
                    + "VALUES ('"
                    + sucursal.getIdSucursal() + "','"
                    + cuenta.getIdCuentaSecundaria() + "','"
                    + cuenta.getActivo() + "','"
                    + usuario + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "flag='-1';";

            sql.add(texto);
        }
        // }

        return sql;
    }

}
