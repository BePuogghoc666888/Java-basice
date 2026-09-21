package dto;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
}