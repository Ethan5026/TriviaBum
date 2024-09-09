package coms309.TriviaBum.Organization;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import coms309.TriviaBum.Organization.OrganizationRepository;
import coms309.TriviaBum.Users.User;
import coms309.TriviaBum.Users.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity

public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orgid;

    @OneToMany(mappedBy = "organization",fetch = FetchType.LAZY, cascade = CascadeType.ALL) //-> creates some sort of recursion error
    public List<User> user;
    private String orgName;

    public Organization(String orgName) {

        this.orgName = orgName;
        this.user = new ArrayList<>();
        addUser(new User("no","data"));
    }

    public Organization() {

    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setId(int id) {
        this.orgid = id;
    }

    public int getId() {
        return orgid;
    }

    public void addUser(User user) {
        this.user.add(user);
    }

    public void removeUser(User user) { this.user.remove(user);
    }

    public List<User> getUsers() {
        return user;
    }


    }

