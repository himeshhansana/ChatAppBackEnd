package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import hibernate.HibernateUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import socket.ProfileService;

@MultipartConfig
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String countryCode = request.getParameter("countryCode");
        String contactNo = request.getParameter("contactNo");
        Part profileImage = request.getPart("profileImage");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (firstName.isEmpty()) {
            responseObject.addProperty("message", "First name is required");
        } else if (lastName.isEmpty()) {
            responseObject.addProperty("message", "Last name is required");
        } else if (countryCode.isEmpty()) {
            responseObject.addProperty("message", "Country code is required");
        } else if (contactNo.isEmpty()) {
            responseObject.addProperty("message", "Contact number is required");
        } else if (profileImage == null) {
            responseObject.addProperty("message", "Select a profile image");
        } else {
            Session s = HibernateUtil.getSessionFactory().openSession();
            Criteria c1 = s.createCriteria(User.class);
            c1.add(Restrictions.eq("countryCode", countryCode));
            c1.add(Restrictions.eq("contactNo", contactNo));
            User user = (User) c1.uniqueResult();
            if (user != null) {
                responseObject.addProperty("message", "This contact number already exists!");
            } else {
                user = new User(firstName, lastName, countryCode, contactNo);
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());

                Transaction tr = s.beginTransaction();

                int id = (int) s.save(user);
                tr.commit();
                s.close();

                responseObject.add("user", gson.toJsonTree(user));

                new ProfileService().saveProfileImage(id, request);

                responseObject.addProperty("status", true);
                responseObject.addProperty("userId", id);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

}
