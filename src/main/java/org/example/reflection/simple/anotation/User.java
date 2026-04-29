package org.example.reflection.simple.anotation;



public class User {
    @JsonField("user_name")
    private String name;

    @JsonField("user_email")
    private String email;

    private String password; // Không có annotation này, chúng ta sẽ bỏ qua khi xử lý

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


}
