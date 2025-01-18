package com.han.internproject.domain.user.entity;

import com.han.internproject.domain.common.entity.Timestamped;
import com.han.internproject.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String username, String password , String nickname, UserRole userRole ){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
