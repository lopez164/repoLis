package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.Inicio;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class MetodoPost {
    
    public MetodoPost(String sql,Inicio ini){
        
        try {
            // DESCRIPCIÓN SERVICIO POR STIVENSON RINCÓN
            
            /*
            
            * Url del servicio: http://www.logarea.net/distribucion/public/service/insert (Solo se atiende peticiones por post)
            *
            * SE DEBE ENVIAR
            * Variable de nombre: SQL (Contiene el insert que se quiere ejecutar)
            * Variable de nombre: USERNAME
            * Variable de nombre: PASSWORD
            
            *  DEVUELVE
            *   0: Si hubo un error autenticando
            *   1: Si el insert se ejecuto con éxito
            *   2: Si huvo un error en la ejecución de la consulta
            *   3: Si hubo en error recogiendo los parametros que se reciben
            */
            
            
            //* ESTE CODIGO FUE PROBADO CON java version "1.8.0_25", compilado con javac 1.8.0_25 y funciona.
            
            
            URL url = new URL("http://www.logarea.net/distribucion/public/service/insert");
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("SQL",sql);
            params.put("USERNAME", ini.getUsuarioBDLocal());
            params.put("PASSWORD", ini.getClaveBDLocal());
            
            StringBuilder postData = new StringBuilder();
            
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            for (int c; (c = in.read()) >= 0; System.out.print((char)c)); // Se recibe respuesta
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(MetodoPost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MetodoPost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MetodoPost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
