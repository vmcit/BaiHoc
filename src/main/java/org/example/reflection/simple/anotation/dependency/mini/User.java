package org.example.reflection.simple.anotation.dependency.mini;

@Entity(table = "users")
class User {
    @Id(autoIncrement = true)
    @Column(name = "id", type = "INT")
    private int id;

    @Column(name = "full_name", type = "VARCHAR(100)")
    private String name;

    @Column(name = "email", type = "VARCHAR(100)")
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
