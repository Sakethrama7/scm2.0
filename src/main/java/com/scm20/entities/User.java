package com.scm20.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
  
    @Id
    private String userId;
    @Column(name="user_name",nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @Column(length=1000)
    private String about;
    @Column(length=1000)
    private String profilePic;
    private String phoneNumber;

    private boolean enabled=false;
    private boolean emailverified=false;
    private boolean phoneverified=false;

    // SELF, GOOGLE, FACEBOOK, TWITTER, LINKEDIN, GITHUB
    private Providers provider=Providers.SELF;
    private String providerUserId;

    // one to many mapping
    // It means that one user can have many contacts
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts=new ArrayList<>();

}
