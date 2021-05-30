package com.thien.controller;

import com.thien.dao.UserDao;
import com.thien.model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;
    public void init(){
        userDao = new UserDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action==null){
            action="";
        }
        switch (action){
            case "create":
                showNewForm(request,response);
                break;
            case "search":
                showSearchForm(request,response);
                break;
            case "sort":
                showSortForm(request,response);
                break;
            case "edit":
                showEditForm(request,response);
                break;
            case "permision":
                addUserPermision(request,response);
                break;
            case "test-without-tran":
                testWithoutTran(request,response);
                break;
            case "test-use-tran":
                testUseTran(request,response);
                break;
            case "delete":
                try {
                    deleteUser(request,response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            default:
                listUser(request,response);
                break;
        }
    }

    private void testUseTran(HttpServletRequest request, HttpServletResponse response) {
        userDao.insertUpdateUseTransaction();
    }

    private void testWithoutTran(HttpServletRequest request, HttpServletResponse response) {
        userDao.insertUpdateWithoutTransaction();
    }

    private void addUserPermision(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("huy", "huyle@gmail.com","hue");
        int[] permision = {2,4};
        userDao.addUserTransaction(user,permision);
    }

    private void showSortForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> list = userDao.sortByName();
        request.setAttribute("listUser",list);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request,response);
    }

    private void showSearchForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyWord = request.getParameter("keyWord");
       // List<User> exitingUser = userDao.searchUser(keyWord);
        List<User> exitingUser = userDao.searchLike(keyWord);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/search.jsp");
        request.setAttribute("users",exitingUser);
        dispatcher.forward(request,response);
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<User> listUser = userDao.selectAllUsers();
        List<User> listUser = userDao.displayUserStore();

        request.setAttribute("listUser",listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request,response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        //userDao.deteleUser(id);
        userDao.deleteUserStore(id);

        List<User> list =userDao.selectAllUsers();
        request.setAttribute("listUser",list);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request,response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        //User exitingUser = userDao.selectUser(id);
        User exitingUser = userDao.getUserById(id);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user",exitingUser);
        dispatcher.forward(request,response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action==null){
            action="";
        }
        switch (action){
            case "create":
                try {
                    insertUser(request,response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "edit":
                try {
                    updateUser(request,response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
        }

    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nameUser = request.getParameter("nameUser");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User user = new User(id,nameUser,email,country);

      //  userDao.updateUser(user);
        userDao.updateUserStore(user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(request,response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String nameUser = request.getParameter("nameUser");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User newUser = new User(nameUser,email,country);
        //userDao.insertUser(newUser);
        userDao.insertUserStore(newUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request,response);
    }
}
