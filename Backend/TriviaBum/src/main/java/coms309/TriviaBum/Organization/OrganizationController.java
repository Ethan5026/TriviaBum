package coms309.TriviaBum.Organization;

import coms309.TriviaBum.Organization.Organization;
import coms309.TriviaBum.Organization.OrganizationRepository;
import coms309.TriviaBum.Users.User;
import coms309.TriviaBum.Users.UserController;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class OrganizationController {
    @Autowired
    OrganizationRepository organizationRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Method to get all organizations")
    @GetMapping(path = "organizations")
    List<Organization> getAllOrganizations(){
        return organizationRepository.findAll();
    }


    @Operation(summary = "Method to make a new organization")
    @PostMapping(path = "organizations/post/{organizationname}")
    Organization PostOrganizationByPath( @PathVariable String organizationname){
        Organization newOrganization = new Organization(organizationname);
        if(organizationRepository.existsByOrgName(organizationname)) {
            throw new UserController.UsernameAlreadyExistsException("Username already exists: " + organizationname);
        }
        return organizationRepository.save(newOrganization);

    }

    @Operation(summary = "Method to return organization object by orgName")
    @GetMapping(path = "organizations/{organizationname}")
    Organization GetOrganizationByPath(@PathVariable String organizationname){
        List<Organization> organizationList = organizationRepository.findAll();
        for(int i = 0; i < organizationList.size(); i++){
            if(organizationList.get(i).getOrgName().equals(organizationname)){
                return organizationList.get(i);
            }
        }
        return null;
    }


    @Operation(summary = "Method to update organization by orgId and org json object")
    @PutMapping("organizations/{id}/{newOrgName}")
    Optional<Organization> updateOrganization(@PathVariable int id, @PathVariable String newOrgName){
        if(organizationRepository.findById((long)id) == null){
            return null;
        }
        Organization org = organizationRepository.findByOrgid(id);
        org.setOrgName(newOrgName);
        organizationRepository.save(org);


        return organizationRepository.findById((long) id);
    }

    @Operation(summary = "Method to delete organization by ID")
    @DeleteMapping(path = "organizations/{id}")
    String deleteOrganization(@PathVariable Long id){
       if(id == 6L) {
           return null;
       }

        organizationRepository.deleteById(id);

        return success;
    }

    @Operation(summary = "Method to get all users in organization")
    @GetMapping(path = "organizations/{orgName}/users")
    List<User> getUsersInOrg(@PathVariable String orgName) {
        Organization organization = organizationRepository.findByOrgName(orgName);
        if (organization != null) {
            return organization.getUsers();
        } else {
            return null; // Handle the case where the organization with the given ID does not exist
        }
    }




}
