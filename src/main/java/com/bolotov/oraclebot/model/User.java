package com.bolotov.oraclebot.model;

import com.bolotov.oraclebot.repository.UserRepository;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity(name = "users")
public class User {


    @Id
    @Column(name = "chat_id")
    Long chatId;

    @Column(name = "date_visit")
    LocalDateTime dateVisit;

    @Column(name = "user_name")
    String username;

    @Column(name = "first_name")
    String firstname;

    @Column(name = "last_name")
    String lastname;

    @Column(name = "date_registration")
    Timestamp dateRegistration;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ElementCollection
    @CollectionTable(name="select_sources", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "group_name")
    @Column(name = "source_set_name")
    Map<String, String> selectSources = new HashMap<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Balance balance;

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public boolean isAdmin() {
        for(Role role : roles){
            if(role.getName().equals(Role.RoleName.ADMIN))
                return true;
        }
        return false;
    }

    public void addSelectSource(String groupName, String sourceSetName) {
        selectSources.put(groupName, sourceSetName);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(LocalDateTime dateVisit) {
        this.dateVisit = dateVisit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Timestamp getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(Timestamp dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Map<String, String> getSelectSources() {
        return selectSources;
    }

    public void setSelectSources(Map<String, String> selectSources) {
        this.selectSources = selectSources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return username;
    }
}
