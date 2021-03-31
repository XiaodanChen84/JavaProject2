package view;

import entity.DonationRecord;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.Logic;
import logic.LogicFactory;

/**
 *
 * @author Gabriel Matte
 */
@WebServlet( name = "DonationRecordTable", urlPatterns = { "/DonationRecordTable" } )
public class DonationRecordTableView extends HttpServlet {
    
    
    protected void processRequest( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType( "text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            Logic<DonationRecord> logic = LogicFactory.getFor("DonationRecord");
                        
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<title>AccountViewNormal</title>" );
            out.println( "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6\" crossorigin=\"anonymous\">");
            out.println( "</head>" );
            out.println( "<body>" );
           
            
            out.println( "<table class=\"table table-striped\"");
            out.println( "<caption>Donation Record</caption>" );
            
            // TABLE HEADER

            out.println( "<thead>" );
            out.println( "<tr>" );
            logic.getColumnNames().forEach( c -> out.printf("<th>%s</th>", c));
            out.println( "</tr>" );  
            out.println( "</thead>");
            
            //TABLE DATA
            logic.getAll().forEach( e -> out.printf( "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", logic.extractDataAsList(e).toArray()));
            
         
            
            out.println( "</table>");
            out.println( "</body>");
            out.println( "</html>");
        }
    }
    
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log( "GET" );
        processRequest( request, response);
    }
}
