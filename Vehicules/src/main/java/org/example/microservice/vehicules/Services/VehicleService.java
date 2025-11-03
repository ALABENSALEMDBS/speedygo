package org.example.microservice.vehicules.Services;

import lombok.AllArgsConstructor;
import org.example.microservice.vehicules.Repository.VehicleRepository;
import org.example.microservice.vehicules.entities.Status;
import org.example.microservice.vehicules.entities.Vehicle;
import org.example.microservice.vehicules.entities.VehicleStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class VehicleService implements IVehicleService{
    VehicleRepository vehicleRepository;


    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleStatusD() == null) {
            vehicle.setVehicleStatusD(Status.APPROVED);
        }
        return vehicleRepository.save(vehicle);}




    public void deleteVehicle(String idV) {
        vehicleRepository.deleteById(idV);
    }

    public Vehicle modifyVehicle(String idV, Vehicle updatedVehicle) {
        return vehicleRepository.findById(idV).map(vehicle -> {
            vehicle.setBrand(updatedVehicle.getBrand());
            vehicle.setModel(updatedVehicle.getModel());
            vehicle.setCapacity(updatedVehicle.getCapacity());
            vehicle.setLicensePlate(updatedVehicle.getLicensePlate());
            vehicle.setVin(updatedVehicle.getVin());
            vehicle.setFabricationDate(updatedVehicle.getFabricationDate());
            vehicle.setFuelType(updatedVehicle.getFuelType());
            vehicle.setVehicleStatus(updatedVehicle.getVehicleStatus());
            vehicle.setVehicleType(updatedVehicle.getVehicleType());
            vehicle.setVehicleStatusD(updatedVehicle.getVehicleStatusD());

            // Update mileage fields if provided
            if (updatedVehicle.getCurrentDistanceKm() > 0) {
                vehicle.setCurrentDistanceKm(updatedVehicle.getCurrentDistanceKm());
            }

            return vehicleRepository.save(vehicle);
        }).orElseThrow(() -> new RuntimeException("❌ Vehicle not found with ID: " + idV));
    }

    public List<Vehicle> getAllVehicles() {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles;
    }


    public Vehicle getVehicle(String VId) {

        System.out.println("🔍 VehicleService.getVehicle called with ID: " + VId);
        try {
            Vehicle vehicle = vehicleRepository.findById(VId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + VId));

            System.out.println("✅ Found vehicle: " + vehicle.getBrand() + " " + vehicle.getModel());
            System.out.println("📊 Current distance: " + vehicle.getCurrentDistanceKm() + " km");
            System.out.println("📊 Maintenance threshold: " + vehicle.getMaintenanceThresholdKm() + " km");

            return vehicle;
        } catch (Exception e) {
            System.out.println("❌ Error retrieving vehicle: " + e.getMessage());
            throw e;
        }
    }


    public Vehicle updateVehicleStatus(String id, boolean approved) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setVehicleStatusD(approved ? Status.APPROVED : Status.REJECTED);
            return vehicleRepository.save(vehicle);
        }
        return null;
    }

    public void approveVehicle(String VId) {

        Vehicle vehicle = vehicleRepository.findById(VId).orElse(null); // Retrieve the vehicle by ID.findById(id).orElse(null);
        if (vehicle != null) {
            vehicle.setVehicleStatusD(Status.APPROVED);
            vehicleRepository.save(vehicle);
        }

    }

    public void rejectVehicle(String VId) {

        Vehicle vehicle = vehicleRepository.findById(VId).orElse(null); // Retrieve the vehicle by ID.findById(id).orElse(null);
        if (vehicle != null) {
            vehicle.setVehicleStatusD(Status.REJECTED);
            vehicleRepository.save(vehicle);
        }

    }

    public List<Vehicle> searchVehicles(String brand) {

        return vehicleRepository.findByBrand(brand);
    }

    public Vehicle updateVehicleMileage(String vehicleId, double additionalKm) {

        System.out.println("🔍 updateVehicleMileage called with vehicleId: " + vehicleId + ", additionalKm: " + additionalKm);

        return vehicleRepository.findById(vehicleId).map(vehicle -> {
            System.out.println("🚗 Found vehicle: " + vehicle.getBrand() + " " + vehicle.getModel() + " (ID: " + vehicle.getIdV() + ")");

            // Update the current distance
            double currentDistance = vehicle.getCurrentDistanceKm();
            System.out.println("📊 Current distance: " + currentDistance + " km");

            double newDistance = currentDistance + additionalKm;
            System.out.println("📊 New distance: " + newDistance + " km (+" + additionalKm + " km)");

            vehicle.setCurrentDistanceKm(newDistance);
            System.out.println("✅ Updated vehicle currentDistanceKm to: " + vehicle.getCurrentDistanceKm() + " km");

            // Check against vehicle's maintenanceThresholdKm
            double maintenanceThreshold = vehicle.getMaintenanceThresholdKm();
            System.out.println("📊 Maintenance threshold: " + maintenanceThreshold + " km");

            boolean maintenanceNeeded = false;

            // Check if maintenance is needed based on vehicle's threshold
            if (newDistance >= maintenanceThreshold) {
                System.out.println("⚠️ Vehicle " + vehicle.getIdV() + " has reached maintenance threshold (" + maintenanceThreshold + " km)");
                maintenanceNeeded = true;
            }

            // If any maintenance is needed, set vehicle status to out of service
            if (maintenanceNeeded) {
                vehicle.setVehicleStatus(VehicleStatus.outOfService);
                System.out.println("🔧 Vehicle status updated to: " + vehicle.getVehicleStatus());
            }

            try {
                System.out.println("💾 Attempting to save vehicle to database...");
                Vehicle savedVehicle = vehicleRepository.save(vehicle);
                System.out.println("💾 Vehicle saved to database with currentDistanceKm: " + savedVehicle.getCurrentDistanceKm() + " km");
                System.out.println("🚗 Vehicle mileage updated: " + currentDistance + " + " + additionalKm + " = " + savedVehicle.getCurrentDistanceKm() + " km");

                // Log maintenance status
                double remainingBeforeMaintenance = maintenanceThreshold - savedVehicle.getCurrentDistanceKm();
                System.out.println("🔧 Maintenance threshold: " + maintenanceThreshold + " km");
                System.out.println("🔧 Remaining before maintenance: " + (remainingBeforeMaintenance > 0 ? remainingBeforeMaintenance : 0) + " km");

                // Verify the saved vehicle by retrieving it again
                Vehicle verifiedVehicle = vehicleRepository.findById(vehicle.getIdV()).orElse(null);
                if (verifiedVehicle != null) {
                    System.out.println("✅ Verified vehicle in database - currentDistanceKm: " + verifiedVehicle.getCurrentDistanceKm() + " km");
                } else {
                    System.out.println("❌ Failed to verify vehicle in database after save");
                }

                return savedVehicle;
            } catch (Exception e) {
                System.out.println("❌ Error saving vehicle to database: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }).orElseThrow(() -> {
            System.out.println("❌ Vehicle not found with ID: " + vehicleId);
            return new RuntimeException("Vehicle not found with ID: " + vehicleId);
        });
    }
    }
