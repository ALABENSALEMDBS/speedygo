package org.example.microservice.vehicules.Services;

import lombok.AllArgsConstructor;
import org.example.microservice.vehicules.Repository.MaintenanceRepository;
import org.example.microservice.vehicules.Repository.VehicleRepository;
import org.example.microservice.vehicules.entities.Maintenance;
import org.example.microservice.vehicules.entities.Vehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class VehicleMaintenanceService implements IVehicleMaintenanceService {
     VehicleRepository vehicleRepository;
    MaintenanceRepository maintenanceRepository;

    /**
     * Migrate maintenance data from Vehicle to Maintenance
     * @param vehicleId The ID of the vehicle to migrate
     * @return The list of created maintenance records
     */
    public List<Maintenance> migrateVehicleMaintenanceData(String vehicleId) {
        List<Maintenance> maintenanceList = new ArrayList<>();

        // Get the vehicle
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            System.out.println("❌ Vehicle not found with ID: " + vehicleId);
            return maintenanceList;
        }

        // Check if maintenance records already exist for this vehicle
        List<Maintenance> existingRecords = maintenanceRepository.findByVehicleId(vehicleId);
        if (!existingRecords.isEmpty()) {
            System.out.println("⚠️ Maintenance records already exist for vehicle: " + vehicleId);
            return existingRecords;
        }

        // Create a maintenance record based on the vehicle's maintenanceThresholdKm
        Maintenance generalMaintenance = new Maintenance();
        generalMaintenance.setVehicleId(vehicleId);
        generalMaintenance.setDescription("General Maintenance for " + vehicle.getBrand() + " " + vehicle.getModel());
        generalMaintenance.setTriggerDistanceKm(vehicle.getMaintenanceThresholdKm());
        generalMaintenance.setCurrentDistanceKm(vehicle.getCurrentDistanceKm());
        generalMaintenance.setCompleted(false);
        maintenanceList.add(maintenanceRepository.save(generalMaintenance));

        System.out.println("✅ Migrated maintenance data for vehicle: " + vehicleId);
        return maintenanceList;
    }

    /**
     * Create a default maintenance schedule for a vehicle
     * @param vehicleId The ID of the vehicle
     * @param currentMileage The current mileage of the vehicle
     * @return The list of created maintenance records
     */
    public List<Maintenance> createDefaultMaintenanceSchedule(String vehicleId, double currentMileage) {
        List<Maintenance> maintenanceList = new ArrayList<>();

        // Get the vehicle to use its information
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            System.out.println("❌ Vehicle not found with ID: " + vehicleId);
            return maintenanceList;
        }

        // Create maintenance records at different mileage intervals
        String brand = vehicle.getBrand();
        String model = vehicle.getModel();

        // Oil change every 5,000 km
        Maintenance oilChange = new Maintenance();
        oilChange.setVehicleId(vehicleId);
        oilChange.setDescription("Oil Change for " + brand + " " + model);
        oilChange.setTriggerDistanceKm(currentMileage + 5000);
        oilChange.setCurrentDistanceKm(currentMileage);
        oilChange.setCompleted(false);
        maintenanceList.add(maintenanceRepository.save(oilChange));

        // Tire rotation every 10,000 km
        Maintenance tireRotation = new Maintenance();
        tireRotation.setVehicleId(vehicleId);
        tireRotation.setDescription("Tire Rotation for " + brand + " " + model);
        tireRotation.setTriggerDistanceKm(currentMileage + 10000);
        tireRotation.setCurrentDistanceKm(currentMileage);
        tireRotation.setCompleted(false);
        maintenanceList.add(maintenanceRepository.save(tireRotation));

        // Brake inspection every 15,000 km
        Maintenance brakeInspection = new Maintenance();
        brakeInspection.setVehicleId(vehicleId);
        brakeInspection.setDescription("Brake Inspection for " + brand + " " + model);
        brakeInspection.setTriggerDistanceKm(currentMileage + 15000);
        brakeInspection.setCurrentDistanceKm(currentMileage);
        brakeInspection.setCompleted(false);
        maintenanceList.add(maintenanceRepository.save(brakeInspection));

        // Major service every 30,000 km
        Maintenance majorService = new Maintenance();
        majorService.setVehicleId(vehicleId);
        majorService.setDescription("Major Service for " + brand + " " + model);
        majorService.setTriggerDistanceKm(currentMileage + 30000);
        majorService.setCurrentDistanceKm(currentMileage);
        majorService.setCompleted(false);
        maintenanceList.add(maintenanceRepository.save(majorService));

        System.out.println("✅ Created " + maintenanceList.size() + " maintenance records for vehicle: " + vehicleId);
        return maintenanceList;
    }

    public Maintenance scheduleMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    public List<Maintenance> getUpcomingMaintenance() {
        return maintenanceRepository.findByCompletedFalse(); // déclencheur basé sur KM, pas date
    }

    public List<Vehicle> getVehiclesThatNeedMaintenanceSoon() {
        double thresholdMarginKm = 500.0;

        List<Maintenance> pending = maintenanceRepository.findByCompletedFalse();

        List<String> vehicleIds = pending.stream()
                .filter(m -> {
                    double kmRemaining = m.getTriggerDistanceKm() - m.getCurrentDistanceKm();
                    return kmRemaining >= 0 && kmRemaining <= thresholdMarginKm;
                })
                .map(Maintenance::getVehicleId)
                .distinct()
                .collect(Collectors.toList());

        return vehicleRepository.findAll().stream()
                .filter(v -> vehicleIds.contains(v.getIdV()))
                .collect(Collectors.toList());
    }

}
