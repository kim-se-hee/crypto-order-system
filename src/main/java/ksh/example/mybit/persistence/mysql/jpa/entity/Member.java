package ksh.example.mybit.persistence.mysql.jpa.entity;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String name;

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member() {
    }

    public Long getId() {
        return id;
    }
}
