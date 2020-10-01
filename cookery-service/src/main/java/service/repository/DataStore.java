package service.repository;

import service.model.User;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;  


public class DataStore {
    private List<User> users = new ArrayList<>();

    public DataStore() {
        User user1 = new User("Rawan", "rawan@gmail.com", "1234");
        User user2 = new User("Ranim", "ranim@gmail.com", "12345");
        User user3 = new User("Robin", "robin@gmail.com", "123456");
        User user4 = new User("anas", "anas@gmail.com", "1234567");
        this.users.add(user1);
        this.users.add(user2);
        this.users.add(user3);
        this.users.add(user4);

        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "https://phpmyadmin.hera.fhict.nl/","dbi407847","new0991418228");  

        User user5 = new User("Rawan", "rawan@gmail.com", "1234");


    }

    public List<User> getUsers(){
        return users;
    }

    public User getUser(int id){
        for (User user: users) {
            if(user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public boolean addUser(User user){
        if(isUserOnFile(user) != null){ // user with this email already exists
            return false;
        }
        else {
            users.add(user);
            return true;
        }
    }

    public boolean updateUser(int id, User user){
        for (User u: users){
            if(u.getId() == id){
                //System.out.println(user.getId() + " = " + id);
                int index = users.indexOf(u); // Get index of user

                // Use same id
                user.setId(id);
                User.decreaseIdSeeder();

                users.set(index, user); // update user


                return true;
            }
        }

        return false;
    }

    public boolean deleteUser(int id){
        for (User user: users){
            if(user.getId() == id){
                users.remove(user);
                return true;
            }
        }

        return false;
    }

    public User isUserOnFile(User user){
        for (User u: users) {
            if(u.getEmail().equals(user.getEmail())){
                return u;
            }
        }

        return null;
    }
}
