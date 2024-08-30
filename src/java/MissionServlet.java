import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;



@WebServlet("/MissionServlet")
public class MissionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");

            if (action == null || action.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'action' parameter.");
                return;
            }

            switch (action) {
                case "copy":
                    // Copy data from 'creation' to 'daf'
                    copyData(request, response);
                    break;
                case "retrieve":
                    // Retrieve and display data from 'creation'
                    retrieveData(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'action' parameter value.");
                    break;
            }
        }
    }

    private void copyData(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try (PrintWriter out = response.getWriter(); Connection con = DBUtil.getConnection()) {
            String sql = "INSERT INTO daf (person_name, Purpose_of_mission, Destination, Date_of_dispatcher) " +
                         "SELECT person_name, Purpose_of_mission, Destination, Date_of_dispatcher FROM creation";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int rowsAffected = ps.executeUpdate();
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Data Transfer Result</title>");
                out.println("</head>");
                out.println("<body>");
                if (rowsAffected > 0) {
                    out.println("<h1>Data successfully copied to 'daf' table.</h1>");
                } else {
                    out.println("<h1>No data was copied.</h1>");
                }
                out.println("</body>");
                out.println("</html>");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error executing SQL query: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error connecting to the database: " + e.getMessage());
        }
    }

    private void retrieveData(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        try (PrintWriter out = response.getWriter(); Connection con = DBUtil.getConnection()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'>");
            out.println("<meta http-equiv='X-UA-Compatible' content='IE=edge'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>");
            out.println("<meta name='description' content=''>");
            out.println("<meta name='author' content=''>");
            out.println("<title>Dashboard</title>");
            out.println("<link href='vendor/fontawesome-free/css/all.min.css' rel='stylesheet' type='text/css'>");
            out.println("<link href='https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i' rel='stylesheet'>");
            out.println("<link href='css/sb-admin-2.min.css' rel='stylesheet'>");
            out.println("<style>");
            out.println(".scrollable-form-container { max-height: 500px; overflow-y: auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); margin: 20px auto; }");
            out.println("table {width: 100%; border-collapse: collapse;}");
            out.println("table, th, td {border: 1px solid black;}");
            out.println("th, td {padding: 8px; text-align: left;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body id='page-top'>");
            out.println("<div id='wrapper'>");
            out.println("<ul class='navbar-nav bg-gradient-primary sidebar sidebar-dark accordion' id='accordionSidebar'>");
            out.println("<a class='sidebar-brand d-flex align-items-center justify-content-center' href='dashboard.jsp'>");
            String username = (String) request.getSession().getAttribute("username");
            if (username == null) {
                username = request.getParameter("username");
                request.getSession().setAttribute("username", username);
            }
            out.println("<div class='sidebar-brand-text mx-3'>Mission Order Management System | " + username + "</div>");
            out.println("</a>");
            out.println("<hr class='sidebar-divider my-0'>");
            out.println("<center><p style='color: white;'>Current time: " + new Date() + "</p></center>");
            out.println("<br>");
            out.println("<center><a href='secretary.jsp'><h7 style='color: white;'>HOME</h7></a></center>");
            out.println("<br>");
            out.println("<li class='nav-item'>");
            out.println("<a class='nav-link collapsed' href='#' data-toggle='collapse' data-target='#collapsePages1' aria-expanded='true' aria-controls='collapsePages1'>");
            out.println("<i class='fas fa-fw fa-users'></i>");
            out.println("<span>Missions</span>");
            out.println("</a>");
            out.println("<div id='collapsePages1' class='collapse' aria-labelledby='headingPages' data-parent='#accordionSidebar'>");
            out.println("<div class='bg-white py-2 collapse-inner rounded'>");
            out.println("<a class='collapse-item' href='missionform.jsp'>Add</a>");
            out.println("<a class='collapse-item' href='viewMissions.jsp'>Manage</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</li>");
            out.println("<hr class='sidebar-divider d-none d-md-block'>");
            out.println("<div class='text-center d-none d-md-inline'>");
            out.println("<button class='rounded-circle border-0' id='sidebarToggle'></button>");
            out.println("</div>");
            out.println("</ul>");
            out.println("<div id='content-wrapper' class='d-flex flex-column'>");
            out.println("<div id='content'>");
            out.println("<nav class='navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow'>");
            out.println("<button id='sidebarToggleTop' class='btn btn-link d-md-none rounded-circle mr-3'>");
            out.println("<i class='fa fa-bars'></i>");
            out.println("</button>");
            out.println("<ul class='navbar-nav ml-auto'>");
            out.println("<div class='topbar-divider d-none d-sm-block'></div>");
            out.println("<li class='nav-item dropdown no-arrow'>");
            out.println("<a class='nav-link dropdown-toggle' href='#' id='userDropdown' role='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>");
            out.println("<span class='mr-2 d-none d-lg-inline text-gray-600 small'>Secretary&nbsp;&nbsp;<img class='img-profile rounded-circle' src='uploadeddata/1.png'></span>");
            out.println("</a>");
            out.println("<div class='dropdown-menu dropdown-menu-right shadow animated--grow-in' aria-labelledby='userDropdown'>");
            out.println("<a class='dropdown-item' href='profile.jsp'>");
            out.println("<i class='fas fa-user fa-sm fa-fw mr-2 text-gray-400'></i>");
            out.println("Profile");
            out.println("</a>");
            out.println("<a class='dropdown-item' href='change-password.jsp'>");
            out.println("<i class='fas fa-cogs fa-sm fa-fw mr-2 text-gray-400'></i>");
            out.println("Change Password");
            out.println("</a>");
            out.println("<div class='dropdown-divider'></div>");
            out.println("<a class='dropdown-item' href='#' data-toggle='modal' data-target='#logoutModal'>");
            out.println("<i class='fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400'></i>");
            out.println("Logout");
            out.println("</a>");
            out.println("</div>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</nav>");
            out.println("<div class='container-fluid'>");
            out.println("<h1 class='h3 mb-2 text-gray-800'>Mission Orders</h1>");
            out.println("<div class='card shadow mb-4'>");
            out.println("<div class='card-header py-3'>");
            out.println("<h6 class='m-0 font-weight-bold text-primary'>Mission Orders List</h6>");
            out.println("</div>");
            out.println("<div class='card-body'>");
            out.println("<div class='table-responsive scrollable-form-container'>");
            out.println("<table class='table table-bordered' id='dataTable' width='100%' cellspacing='0'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Person Name</th>");
            out.println("<th>Function of Person</th>");
            out.println("<th>Purpose of Mission</th>");
            out.println("<th>Destination</th>");
            out.println("<th>Means of Transportation</th>");
            out.println("<th>Date of Departure</th>");
            out.println("<th>Return Date</th>");
            out.println("<th>Duration (Days)</th>");
            out.println("<th>Account Number</th>");
            out.println("<th>Mission Allowance</th>");
            out.println("<th>Name of Supervisor</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            
                String sql = "SELECT * FROM creation";
                try (PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<td>" + rs.getInt("Id") + "</td>");
                        out.println("<td>" + rs.getString("Person_Name") + "</td>");
                        out.println("<td>" + rs.getString("Function_of_person") + "</td>");
                        out.println("<td>" + rs.getString("Purpose_of_mission") + "</td>");
                        out.println("<td>" + rs.getString("Destination") + "</td>");
                        out.println("<td>" + rs.getString("Means_of_transportation") + "</td>");
                        out.println("<td>" + rs.getString("Date_of_Depature") + "</td>");
                        out.println("<td>" + rs.getString("Return_Date") + "</td>");
                        out.println("<td>" + rs.getInt("Duration_days") + "</td>");
                        out.println("<td>" + rs.getString("Account_number") + "</td>");
                        out.println("<td>" + rs.getDouble("Mission_Allowance") + "</td>");
                        out.println("<td>" + rs.getString("Name_of_supervisor") + "</td>");
                        out.println("<td>");
                        out.println("<a href='UpdateMissionServlet?id=" + rs.getInt("Id") + "'>Edit</a> | ");
                        out.println("<a href='DeleteMissionServlet?id=" + rs.getInt("Id") + "'>Delete</a>");
                        out.println("</td>");
                        out.println("</tr>");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error executing SQL query: " + e.getMessage());
                }
            

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<a class='scroll-to-top rounded' href='#page-top'>");
            out.println("<i class='fas fa-angle-up'></i>");
            out.println("</a>");
            out.println("<div class='modal fade' id='logoutModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>");
            out.println("<div class='modal-dialog' role='document'>");
            out.println("<div class='modal-content'>");
            out.println("<div class='modal-header'>");
            out.println("<h5 class='modal-title' id='exampleModalLabel'>Ready to Leave?</h5>");
            out.println("<button class='close' type='button' data-dismiss='modal' aria-label='Close'>");
            out.println("<span aria-hidden='true'>×</span>");
            out.println("</button>");
            out.println("</div>");
            out.println("<div class='modal-body'>Select 'Logout' below if you are ready to end your current session.</div>");
            out.println("<div class='modal-footer'>");
            out.println("<button class='btn btn-secondary' type='button' data-dismiss='modal'>Cancel</button>");
            out.println("<a class='btn btn-primary' href='LogoutServlet'>Logout</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<script src='vendor/jquery/jquery.min.js'></script>");
            out.println("<script src='vendor/bootstrap/js/bootstrap.bundle.min.js'></script>");
            out.println("<script src='vendor/jquery-easing/jquery.easing.min.js'></script>");
            out.println("<script src='js/sb-admin-2.min.js'></script>");
            out.println("<script src='vendor/chart.js/Chart.min.js'></script>");
            out.println("<script src='js/demo/chart-area-demo.js'></script>");
            out.println("<script src='js/demo/chart-pie-demo.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(MissionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(MissionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet that handles both copying data to 'daf' and retrieving mission data from 'creation'";
    }
}
