package com.laioffer.jupiter.servlet;

import com.laioffer.jupiter.db.MySQLConnection;
import com.laioffer.jupiter.db.MySQLException;
import com.laioffer.jupiter.entity.FavoriteRequestBody;
import com.laioffer.jupiter.entity.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite"})
public class FavoriteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        //        String userId = request.getParameter("user_id");
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        try (MySQLConnection conn = new MySQLConnection()) {
            Map<String, List<Item>> itemMap = conn.getFavoriteItems(userId);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().print(
//                    new ObjectMapper().writeValueAsString(itemMap)
//            );
            ServletUtil.writeData(response, itemMap);
        } catch (MySQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        // Get user ID from request URL, this is a temporary solution since we don’t support session now
//        String userId = request.getParameter("user_id");
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

        // Get favorite item information from request body
//        ObjectMapper mapper = new ObjectMapper();
//        FavoriteRequestBody body = mapper.readValue(
//                request.getReader(), FavoriteRequestBody.class
//        );
        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);
        // if previous conversion fails
        if (body == null) {
            System.err.println(
                    "Convert to JSON to FavoriteRequestBody failed.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (MySQLConnection conn = new MySQLConnection()) {
            // Save the favorite item to the database
            conn.setFavoriteItem(userId, body.getFavoriteItem());
        } catch (MySQLException e) {
            throw new ServletException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
//        String userId = request.getParameter("user_id");
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");

//        ObjectMapper mapper = new ObjectMapper();
//        FavoriteRequestBody body = mapper.readValue(
//                request.getReader(), FavoriteRequestBody.class
//        );
        FavoriteRequestBody body = ServletUtil.readRequestBody(FavoriteRequestBody.class, request);
        if (body == null) {
            System.err.println(
                    "Convert JSON to FavoriteRequestBody failed."
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        try (MySQLConnection conn = new MySQLConnection()) {
            conn.unsetFavoriteItem(
                    userId,
                    body.getFavoriteItem().getId()
            );
        } catch (MySQLException e) {
            throw new ServletException(e);
        }
    }



}
