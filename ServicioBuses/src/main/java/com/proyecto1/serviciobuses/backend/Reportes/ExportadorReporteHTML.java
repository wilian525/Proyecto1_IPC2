/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.Reportes;

import java.util.Collection;

/**
 *
 * @author wilian
 */
public class ExportadorReporteHTML {
    
    public String genera(String titulo, String[] encabezados, Collection<String[]> filas){
            
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        
        html.append("<html lang=\"es\">");
        
        html.append("<head>");
        
        html.append("<meta charset=\"UTF-8\">");
        
        html.append("<title>");
        
        html.append(escapar(titulo));
        
        html.append("</title>");
        
        html.append("</head>");
        
        html.append("<body>");
        
        html.append("<h1>");
        
        html.append(escapar(titulo));
        
        html.append("</h1>");
        
        html.append("<table border=\"1\">");
        
        html.append("<tr>");
        
        for(String encabezado : encabezados){
                html.append("<th>");
                html.append(escapar(encabezado));
                html.append("</th>");
        }
        html.append("/tr");
        
        for(String[] fila : filas){
                html.append("<tr>");
                
                for(String dato : fila){
                        html.append("<td>");
                        html.append(escapar(dato));
                        html.append("</td>");
                }
                html.append("</tr>");
        }
        html.append("</table>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    
    private String escapar(String texto){
        if (texto == null) {
              return "";
        }
        return texto.replace("&", "&amp;").replace(" <", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
    
}
