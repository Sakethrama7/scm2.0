package com.scm20.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
public class User implements UserDetails {
  // Whenever you write User implements UserDetails . In Spring security we need UserDetails so wherever you want to use UserDetails you can use User class.


    @Id
    private String userId;
    @Column(name="user_name",nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Getter(AccessLevel.NONE)
    private String password;
   

    @Column(length=1000)
    private String about;
    @Column(length=1000)
    private String profilePic;
    private String phoneNumber;

    private boolean enabled=true;
    private boolean emailverified=false;
    private boolean phoneverified=false;

    @Enumerated(value = EnumType.STRING)
    // SELF, GOOGLE, FACEBOOK, TWITTER, LINKEDIN, GITHUB
    private Providers provider=Providers.SELF;
    private String providerUserId;

    
    // one to many mapping
    // It means that one user can have many contacts
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts=new ArrayList<>();

    @ElementCollection(fetch=FetchType.EAGER)
     private List<String> roleList=new ArrayList<>();


     // implemented methods of UserDetails for spring security.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list of roles[USER,ADMIN]
        // collection of SimpleGrantedAuthority[roles{ADMIN,USER}]
       Collection<SimpleGrantedAuthority> roles= roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        // TODO Auto-generated method stub
        return roles;
    }

   // for this project the email id is our username
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto
        return true;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
