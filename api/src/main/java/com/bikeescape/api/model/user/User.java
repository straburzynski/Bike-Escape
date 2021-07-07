package com.bikeescape.api.model.user;

import com.bikeescape.api.model.Authority;
import com.bikeescape.api.model.Race;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true)
    private Long Id;

    @Column(name = "FirstName", length = 65)
    private String firstName;

    @Column(name = "Password", length = 64)
    @NotNull
    private String password;

    @Column(name = "Email", unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "UserAuthority",
            joinColumns = @JoinColumn(name = "UserId", referencedColumnName = "Id"),
            inverseJoinColumns = @JoinColumn(name = "AuthorityId", referencedColumnName = "Id"))
    private List<Authority> authorities;

    @Column(name = "Removed")
    private boolean removed = false;

    @Column(name = "RegisterDate")
    private Date registerDate;

    @Column(name = "RemoveDate")
    private Date removeDate;

    @Transient
    private String passwordConfirm;

    // reference to Race table
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private Set<Race> races = new HashSet<>(0);

    public User(String firstName, String password, String email) {
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.removed = false;
        this.registerDate =  Calendar.getInstance().getTime();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", removed=" + removed +
                ", registerDate=" + registerDate +
                ", removeDate=" + removeDate +
                '}';
    }
}