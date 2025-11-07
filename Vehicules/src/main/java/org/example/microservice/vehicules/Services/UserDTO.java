package org.example.microservice.vehicules.Services;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String assignedVehicleId;
    private List<String> roles;
    public UserDTO(String id, String firstName, String lastName, String email, String assignedVehicleId, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.assignedVehicleId = assignedVehicleId;
        this.roles = roles;
    }
}
