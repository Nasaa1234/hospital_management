package models;

import java.util.Objects;

public abstract class User {
    public String name;
    public String email;
    protected String role;
    protected String password;

    public User(String name, String email, String role, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public String getRole() {
        return role;
    }

    public abstract void accessPanel();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Doctor doctor = (Doctor) obj;
        return this.name.equals(doctor.name) && this.email.equals(doctor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
