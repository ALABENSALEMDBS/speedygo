package org.example.microservice.vehicules.Controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.microservice.vehicules.Services.IVehicleMaintenanceService;
import org.example.microservice.vehicules.entities.Maintenance;
import org.example.microservice.vehicules.entities.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {


    private final IVehicleMaintenanceService vehicleMaintenanceService;


    // ✅ Maintenance: Planification
    @Operation(summary = "Créer une maintenance")
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleMaintenance(@RequestBody Maintenance maintenance) {
        try {
            Maintenance saved = vehicleMaintenanceService.scheduleMaintenance(maintenance);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace(); // utile en dev
            return ResponseEntity.status(500).body("❌ Erreur serveur : " + e.getMessage());
        }
    }

    // ✅ Maintenance: À venir
    @GetMapping("/upcoming")
    public ResponseEntity<List<Maintenance>> getUpcomingMaintenance() {
        return ResponseEntity.ok(vehicleMaintenanceService.getUpcomingMaintenance());
    }

    // ✅ Maintenance: Véhicules à entretenir
    @GetMapping("/alerts")
    public ResponseEntity<List<Vehicle>> getVehiclesThatNeedMaintenanceSoon() {
        return ResponseEntity.ok(vehicleMaintenanceService.getVehiclesThatNeedMaintenanceSoon());
    }
    // ✅ Test debug injection (optionnel)
    @GetMapping("/debug-injection")
    public String debugInjection() {
        return (vehicleMaintenanceService == null) ? "❌ Injection échouée" : "✅ Injection OK";
    }

    // ✅ Create default maintenance schedule for a vehicle
    @PostMapping("/create-default-schedule/{vehicleId}")
    public ResponseEntity<List<Maintenance>> createDefaultMaintenanceSchedule(
            @PathVariable String vehicleId,
            @RequestParam(defaultValue = "0") double currentMileage) {
        try {
            System.out.println("🔧 Creating default maintenance schedule for vehicle: " + vehicleId);
            System.out.println("📊 Current mileage: " + currentMileage + " km");

            List<Maintenance> maintenanceList = vehicleMaintenanceService.createDefaultMaintenanceSchedule(vehicleId, currentMileage);

            if (maintenanceList.isEmpty()) {
                return ResponseEntity.status(404).body(maintenanceList);
            }

            return ResponseEntity.ok(maintenanceList);
        } catch (Exception e) {
            System.out.println("❌ Error creating default maintenance schedule: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ Migrate maintenance data from Vehicle to Maintenance
    @PostMapping("/migrate-maintenance/{vehicleId}")
    public ResponseEntity<List<Maintenance>> migrateVehicleMaintenanceData(
            @PathVariable String vehicleId) {
        try {
            System.out.println("🔄 Migrating maintenance data for vehicle: " + vehicleId);

            List<Maintenance> maintenanceList = vehicleMaintenanceService.migrateVehicleMaintenanceData(vehicleId);

            if (maintenanceList.isEmpty()) {
                return ResponseEntity.status(404).body(maintenanceList);
            }

            return ResponseEntity.ok(maintenanceList);
        } catch (Exception e) {
            System.out.println("❌ Error migrating maintenance data: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
