package com.thien.dao;

import com.thien.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class UserDao implements IUserDao{
    private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "admin";

    private static final String INSERT_USERS_SQL = "INSERT INTO users"+" (nameUser,email,country) VALUES"+"(?,?,?)";
    private static final String SELECT_USER_BY_ID = "select id, nameUser, email, country from users where id=?";
    private static final String SELECT_ALL_USERs = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id=?;";
    private static final String UPDATE_USERS_SQL = "update users set nameUser = ?, email = ?,country= ? where id=?;";
    private static final String SEARCH_BY_KEYWORD = "select * from users where country= ? or nameUser=? or email=?;";
    private static final String SORT_BY_NAME = "select * from users order by nameUser;";
    private static final String SEARCH_LIKE = "select * from users where nameUser like ? or email like ? or country like ?; ";

    //Transaction
    private static final String SQL_INSERT = "INSERT INTO employee(nameEmp, salary, created_date) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE employee SET salary = ? WHERE nameEmp = ?";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE employee"
            +"("
            +"id serial,"
            +"nameEmp varchar(100) not null,"
            +"salary numeric(15,2) not null,"
            +"created_date timestamp,"
            +"PRIMARY KEY (id)"
            +")";
    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS employee";


    public UserDao() {
    }

    protected Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername,jdbcPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
    //Them Moi User
    @Override
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);){
                preparedStatement.setString(1,user.getNameUser());
                preparedStatement.setString(2,user.getEmail());
                preparedStatement.setString(3,user.getCountry());
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
        }catch (SQLException e){
            printSQLException(e);
        }
    }

    @Override
    public void insertUserStore(User user) throws SQLException {
        String query = "{CALL insert_user(?,?,?)}";
        try(Connection connection = getConnection();
            CallableStatement callableStatement = connection.prepareCall(query);){
            callableStatement.setString(1,user.getNameUser());
            callableStatement.setString(2,user.getEmail());
            callableStatement.setString(3,user.getCountry());
            System.out.println(callableStatement);
            callableStatement.executeUpdate();
        }catch (SQLException ex){
            printSQLException(ex);
        }
    }
    //Tim Kiem
    @Override
    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
            preparedStatement.setInt(1,id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id,nameUser,email,country);
            }
        }catch (SQLException e){
            printSQLException(e);
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{CALL get_user_by_id(?)}";
        try(Connection connection = getConnection();
            CallableStatement callableStatement = connection.prepareCall(query);){
            callableStatement.setInt(1,id);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()){
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id,nameUser,email,country);
            }
        }catch (SQLException ex){
            printSQLException(ex);
        }return user;
    }

    public List<User> searchUser(String keyWord){
        List<User> list = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BY_KEYWORD);){
            preparedStatement.setString(1,keyWord);
            preparedStatement.setString(2,keyWord);
            preparedStatement.setString(3,keyWord);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                list.add(new User(id,nameUser,email,country));
            }
        }catch (SQLException e){
            printSQLException(e);
        }return list;
    }
    //Tim Kiem Gan Dung
    public List<User> searchLike(String keyWord) {
        List<User> list = new ArrayList<>();
        keyWord = "%"+keyWord+"%";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_LIKE);) {
            preparedStatement.setString(1,keyWord);
            preparedStatement.setString(2,keyWord);
            preparedStatement.setString(3,keyWord);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                list.add(new User(id,nameUser,email,country));
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }return list;
    }
    //Sap Xep
    public List<User> sortByName(){
        List<User> users = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SORT_BY_NAME);)    {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id,nameUser,email,country));
            }
        }catch (SQLException e){
            printSQLException(e);
        }return users;
    }
    //Show List User
    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERs);){
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, nameUser, email, country));
            }
        }catch (SQLException e){
            printSQLException(e);
        }
        return users;
    }

    @Override
    public List<User> displayUserStore() {
        List<User> list = new ArrayList<>();
        String query = "{CALL show_list_user}";
        try(Connection connection = getConnection();
        CallableStatement callableStatement = connection.prepareCall(query);){
            System.out.println(callableStatement);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String nameUser = rs.getString("nameUser");
                String email = rs.getString("email");
                String country = rs.getString("country");
                list.add(new User(id,nameUser,email,country));
            }
        }catch (SQLException e){
            printSQLException(e);
        }
        return list;
    }
    //Xoa User
    @Override
    public boolean deteleUser(int id) throws SQLException {
        boolean rowDeleted;
        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);){
            statement.setInt(1,id);
            rowDeleted = statement.executeUpdate()>0;
        }
        return rowDeleted;
    }
    public boolean deleteUserStore(int id) throws SQLException {
        boolean rowDeleted;
        String query = "{CALL sp_delete_user(?)}";
        try (Connection connection = getConnection();
        CallableStatement callableStatement = connection.prepareCall(query);){
        callableStatement.setInt(1,id);
        rowDeleted = callableStatement.executeUpdate()>0;
        }
        return rowDeleted;
    }
    //Sua User
    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);){
            statement.setString(1,user.getNameUser());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getCountry());
            statement.setInt(4,user.getId());
            rowUpdated = statement.executeUpdate()>0;
        }
        return rowUpdated;
    }
    public boolean updateUserStore(User user) throws SQLException {
        boolean rowUpdated;
        String query = "{CALL sp_update_user(?,?,?,?)}";
        try(Connection connection = getConnection();
        CallableStatement callableStatement = connection.prepareCall(query);){
            callableStatement.setInt(1,user.getId());
            callableStatement.setString(2,user.getNameUser());
            callableStatement.setString(3,user.getEmail());
            callableStatement.setString(4,user.getCountry());
            System.out.println(callableStatement);
            rowUpdated = callableStatement.executeUpdate()>0;
        }return rowUpdated;
    }
    //TranSaction

    @Override
    public void addUserTransaction(User user, int[] permisions) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement statementAssigment = null;
        ResultSet rs = null;
        try{
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,user.getNameUser());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            int rowAffected = preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            int userId = 0;
            if (rs.next()){
                userId =rs.getInt(1);
            }
            if (rowAffected==1){
                String sqlPivot = "INSERT INTO user_permision(user_id,permision_id)"
                        +"VALUES(?,?)";
                statementAssigment = connection.prepareStatement(sqlPivot);
                for (int permisionId: permisions){
                    statementAssigment.setInt(1,userId);
                    statementAssigment.setInt(2,permisionId);
                    statementAssigment.executeUpdate();
                }
                connection.commit();
            }else {
                connection.rollback();
            }
        }catch (SQLException ex){
            try{
                if (connection!=null) connection.rollback();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        }finally {
            try {
                if (connection!=null) connection.close();
                if (preparedStatement!=null) preparedStatement.close();
                if (statementAssigment!=null) statementAssigment.close();
                if (rs!=null) rs.close();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {
        try (Connection connection = getConnection();
        Statement statement = connection.createStatement();
        PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);
        PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);){
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);
            //Insert List
            psInsert.setString(1,"Nam");
            psInsert.setBigDecimal(2,new BigDecimal(10));
            psInsert.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psInsert.setString(1,"Han");
            psInsert.setBigDecimal(2,new BigDecimal(20));
            psInsert.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

//            psUpdate.setBigDecimal(2,new BigDecimal(999.99));
//            psUpdate.setString(2,"Quynh");
//            psUpdate.execute();
        }catch (SQLException e){
            printSQLException(e);
        }
    }

    @Override
    public void insertUpdateUseTransaction() {
        try(Connection connection = getConnection();
        Statement statement = connection.createStatement();
        PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);
        PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE);){
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);
            connection.setAutoCommit(false);
            psInsert.setString(1,"Quynh");
            psInsert.setBigDecimal(2,new BigDecimal(10));
            psInsert.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psInsert.setString(1,"Ngan");
            psInsert.setBigDecimal(2,new BigDecimal(20));
            psInsert.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

           // psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            psUpdate.setBigDecimal(1, new BigDecimal(999.99));
            psUpdate.setString(2, "Quynh");
            psUpdate.execute();
            connection.commit();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            printSQLException(e);
        }
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e:ex){
            if (e instanceof SQLException){
                e.printStackTrace(System.err);
                System.err.println("SQLState: "+((SQLException) e).getSQLState());
                System.err.println("Error Code: "+((SQLException) e).getErrorCode());
                System.err.println("Message: "+e.getMessage());
                Throwable t = ex.getCause();
                while (t!=null){
                    System.out.println("Cause: "+t);
                    t=t.getCause();
                }
            }
        }
    }
}
