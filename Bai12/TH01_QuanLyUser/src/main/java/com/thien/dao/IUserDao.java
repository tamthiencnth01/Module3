package com.thien.dao;

import com.thien.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    public void insertUser(User user)throws SQLException;
    public User selectUser(int id);
    public List<User> selectAllUsers();
    public boolean deteleUser(int id) throws SQLException;
    public boolean updateUser(User user) throws SQLException;
    public User getUserById(int id);
    //Produce Storage
    public void insertUserStore(User user)throws SQLException;
    public List<User> displayUserStore();
    //Transaction
    public void addUserTransaction(User user, int[] permisions);
    public void insertUpdateWithoutTransaction();
    public void insertUpdateUseTransaction();
}
