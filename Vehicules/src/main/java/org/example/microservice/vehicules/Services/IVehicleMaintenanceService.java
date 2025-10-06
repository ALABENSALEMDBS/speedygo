package org.example.microservice.vehicules.Services;

import org.example.microservice.vehicules.entities.Maintenance;
import org.example.microservice.vehicules.entities.Vehicle;

import java.util.List;

public interface IVehicleMaintenanceService {
    Maintenance scheduleMaintenance(Maintenance maintenance);
    List<Maintenance> getUpcomingMaintenance();
    //averfier
    List<Vehicle> getVehiclesThatNeedMaintenanceSoon();
    List<Maintenance> createDefaultMaintenanceSchedule(String vehicleId, double currentMileage);
    List<Maintenance> migrateVehicleMaintenanceData(String vehicleId);
}
