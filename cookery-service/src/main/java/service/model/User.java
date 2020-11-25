package service.model;

public class User {
    private int id;
    private static int idSeeder = 0;
    private String name;
    private String email;
    private String password;
    private Role role;

    public User() {
        this.id = idSeeder;
        idSeeder++;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.user;
        this.id = idSeeder;
        idSeeder++;
    }

    public User(int id, String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.user;
        this.id = id;
    }

    public User(int id, String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.id = id;
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
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static void decreaseIdSeeder() {
        idSeeder--;
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
}
