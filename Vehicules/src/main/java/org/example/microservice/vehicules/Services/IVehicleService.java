package org.example.microservice.vehicules.Services;

import org.example.microservice.vehicules.entities.Vehicle;

import java.util.List;

public interface IVehicleService {
    Vehicle addVehicle(Vehicle vehicle);
    void deleteVehicle(String idV);
    public Vehicle modifyVehicle(String idV, Vehicle updatedVehicle) ;
    List<Vehicle> getAllVehicles();
    Vehicle getVehicle(String VId);
    //public Vehicle modifystatusVehicle(Vehicle v);
    Vehicle updateVehicleStatus(String id, boolean approved);

    void approveVehicle(String VId);
    void rejectVehicle(String VId);
    List<Vehicle> searchVehicles(String brand);

//    List<Vehicle> getVehiclesByDriverId(String driverId);
    Vehicle updateVehicleMileage(String vehicleId, double additionalKm);
}
