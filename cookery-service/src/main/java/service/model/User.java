package service.model;

import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public User() { }

//    @ConstructorProperties({"name", "email", "password"})
    public User(String name, String email, String password) {
//        this.setName(name);
//        this.setEmail(email);
//        this.setPassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.user;
    }

    public User(int id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
//        if(name.length() >= 4) {
            this.name = name;
//        }
//        else {
//            System.out.println("Invalid name");
//
//            throw new IllegalStateException("Name is invalid");
//        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
//        String regex = "^[a-zA-Z0-9_!#$%&�*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
//
//        Pattern pattern = Pattern.compile(regex);
//
//        Matcher matcher = pattern.matcher(email);
//
//        if(matcher.matches()) {
            this.email = email;
//        }
//        else {
//            System.out.println("Invalid email");
//            throw new IllegalStateException("Email is invalid");
//        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
//        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
//
//        Pattern pattern = Pattern.compile(regex);
//
//        Matcher matcher = pattern.matcher(password);
//
//        if(matcher.matches()) {
//            System.out.println("Correct pass");
            this.password = password;
//        }
//        else {
//            System.out.println("Incorrect pass");
//
//            throw new IllegalStateException("Password is invalid");
//        }
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, role);
    }
}