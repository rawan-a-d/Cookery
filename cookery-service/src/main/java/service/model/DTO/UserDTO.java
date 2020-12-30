package service.model.DTO;

import service.model.Role;

import java.util.Objects;

// Used for following, getting user's data
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private Role role;

    public UserDTO() {
    }

    public UserDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDTO(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public UserDTO(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserDTO(int id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id &&
                Objects.equals(name, userDTO.name) &&
                Objects.equals(email, userDTO.email) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, role);
    }
}
