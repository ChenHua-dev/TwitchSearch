package com.laioffer.jupiter.servlet;

import com.laioffer.jupiter.external.TwitchClient;
import com.laioffer.jupiter.external.TwitchException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String gameId = request.getParameter("game_id");
        //int limit = Integer.parseInt(request.getParameter("limit"));
        if (gameId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TwitchClient client = new TwitchClient();
        try {
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().print(
//                    new ObjectMapper().writeValueAsString(
//                            client.searchItems(gameId, limit)
////                            client.searchItems(gameId)
//                    ));
//            ServletUtil.writeData(response, client.searchItems(gameId, limit));
            ServletUtil.writeData(response, client.searchItems(gameId));

        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }

}
