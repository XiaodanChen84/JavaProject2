package view;

import entity.Account;
import entity.BloodBank;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import static javax.persistence.GenerationType.values;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.AccountLogic;
import logic.BloodBankLogic;
import logic.LogicFactory;

/**
 *
 * @author Jing Zhao    
 */
@WebServlet(name = "CreateBloodBank", urlPatterns = { "/CreateBloodBank"})
public class CreateBloodBank extends HttpServlet {
    private String errorMessage = null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType(  "text/html;charset=UTF-8" );
        try( PrintWriter out = response.getWriter()){
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<title>Create Blood Bank</title>");
            out.println( "</head>" );
            out.println( "<body>" );
            out.println( "<div style=\"text-align: center;\">" );
            out.println( "<div style=\"display: inline-block; text-align: left;\">" );
            out.println( "<form method=\"post\">");
            out.println( "Owner:<br>");
            out.printf( "<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.OWNER_ID );
            out.println("<br>");
            out.println("Name:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.NAME);
            out.println("<br>");
            out.println("Privately_owned:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.PRIVATELY_OWNED);
            out.println("<br>");
            out.println("Established:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.ESTABLISHED);
            out.println("<br>");
            out.println("EmployeeCount:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodBankLogic.EMPLOYEE_COUNT);
            out.println("<br>");
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println( "</form>" );
            
            if( errorMessage != null && !errorMessage.isEmpty() ){
                out.println("<p color=red>");
                out.println( "<font color=red size=4px>" );
                out.println( errorMessage );
                out.println( "</font>" );
                out.println( "</p>" );
                
            }
            out.println( "<pre>" );
            out.println( "Submitted keys and values:" );
            out.println( toStringMap( request.getParameterMap() ) );
            out.println( "</pre>" );
            out.println( "</div>" );
            out.println( "</div>" );
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach( ( k, v ) -> builder.append( "Key=" ).append( k )
                .append( ", " )
                .append( "Value/s=" ).append( Arrays.toString( v ) )
                .append( System.lineSeparator() ) );
        return builder.toString();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
     log("GET");
     processRequest( req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
    log( "POST" );
    BloodBankLogic logic = LogicFactory.getFor( "BloodBank" );
    String owner =req.getParameter(BloodBankLogic.OWNER_ID);
    if(logic.getBloodBanksWithOwner(Integer.parseInt(owner)) == null){
        try{
            BloodBank bloodBank = logic.createEntity(req.getParameterMap());
            logic.add(bloodBank);
        }catch(Exception ex){
            errorMessage = ex.getMessage();
        }
    }else{
       errorMessage = "Owner: \""+ owner +"\" already exists";
    }
    if(req.getParameter("add")!=null){
        processRequest(req, resp);
    }else if(req.getParameter("view")!=null){
        resp.sendRedirect("BloodBankTable");
    }
    }

      @Override
    public String getServletInfo() {
        return "Create a BloodBank Entity";
    }

    private static final boolean DEBUG = true;
    
    public void log( String msg ){
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message);
        }
    }
    
    
    public void log(String msg, Throwable t){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log(message, t);
        }
    }
    