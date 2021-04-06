package view;
import entity.BloodBank;
import entity.BloodDonation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.BloodBankLogic;
import logic.BloodDonationLogic;
import logic.LogicFactory;

/**
 *
 * @author danping tang
 */
@WebServlet(name = "CreateBloodDonation", urlPatterns = {"/CreateBloodDonation"})
public class CreateBloodDonation extends HttpServlet {

    private String errorMessage = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create BloodDonation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            out.println("BloodBankID:<br>");
            //instead of typing the name of column manualy use the static vraiable in logic
            //use the same name as column id of the table. will use this name to get date
            //from parameter map.
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BANK_ID);
            out.println("<br>");
            out.println("Millitliters:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.MILLILITERS);
            out.println("<br>");
            out.println("Blood Group:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.BLOOD_GROUP);
            out.println("<br>");
            out.println("Rh Factor:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.RHESUS_FACTOR);
            out.println("<br>");
            out.println("Created DateTime:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", BloodDonationLogic.CREATED);
            out.println("<br>");
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }
     @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        log( "POST" );
        BloodDonationLogic bdLogic = LogicFactory.getFor("BloodDonation");
        String bankID = request.getParameter( BloodDonationLogic.BANK_ID );
   
            try {
            
                BloodBankLogic bLogic=LogicFactory.getFor("BloodBank");
                BloodBank bank=bLogic.getWithId(Integer.parseInt(bankID));
                BloodDonation bloodDonation = bdLogic.createEntity(request.getParameterMap());
                bloodDonation.setBloodBank(bank);             
                bdLogic.add(bloodDonation );
            } catch( Exception ex ) {
                errorMessage = ex.getMessage();
            }

        if( request.getParameter( "add" ) != null ){
            //if add button is pressed return the same page
            processRequest( request, response );
        } else if( request.getParameter( "view" ) != null ){
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect( "BloodDonationTable" );
        }
    }

        /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create a BloodDonation Entity";
    }

    private static final boolean DEBUG = true;

    public void log( String msg ) {
        if( DEBUG ){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
            getServletContext().log( message );
        }
    }

    public void log( String msg, Throwable t ) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg );
        getServletContext().log( message, t );
    }
}
