package org.example.microservice.vehicules.Controller;
import lombok.RequiredArgsConstructor;
import org.example.microservice.vehicules.Services.IVehicleService;
import org.example.microservice.vehicules.entities.Status;
import org.example.microservice.vehicules.entities.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vehicle")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {

    private final IVehicleService vehicleService;
   MultipartFile imageFile;

    @GetMapping("/retrieve-all-vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleService.getAllVehicles();
    }

    // Direct endpoint to get vehicle by ID (simpler path)
    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String vehicleId) {
        System.out.println("🔍 Direct retrieval of vehicle with ID: " + vehicleId);
        try {
            Vehicle vehicle = vehicleService.getVehicle(vehicleId);
            System.out.println("✅ Retrieved vehicle: " + vehicle.getBrand() + " " + vehicle.getModel());
            System.out.println("📊 Current mileage: " + vehicle.getCurrentDistanceKm() + " km");
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            System.out.println("❌ Error retrieving vehicle: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/add-vehicle")
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {

        // Définir un admin par défaut
        vehicle.setAdminName("Admin Test");

        // Statut par défaut
        if (vehicle.getVehicleStatusD() == null) {
            vehicle.setVehicleStatusD(Status.PENDING);
        }

        return vehicleService.addVehicle(vehicle);
    }


    @DeleteMapping("/remove-vehicle/{vehicle-id}")
    public void removeVehicle(@PathVariable("vehicle-id") String VId) {
        vehicleService.deleteVehicle(VId);
    }

    @PutMapping("/modify-vehicle/{vehicle-id}")
    public Vehicle updateVehicle(@PathVariable("vehicle-id") String idV,
                                 @RequestBody Vehicle updatedVehicle) {
        return vehicleService.modifyVehicle(idV, updatedVehicle);
    }

    @PutMapping("/updateStatus/{id}")
    public Vehicle updateVehicleStatus(@PathVariable String id, @RequestParam boolean approved) {
        return vehicleService.updateVehicleStatus(id, approved);
    }

    @PutMapping("/approve/{id}")
    public void approveVehicle(@PathVariable String id) {
        vehicleService.approveVehicle(id);
    }

    @PutMapping("/reject/{id}")
    public void rejectVehicle(@PathVariable String id) {
        vehicleService.rejectVehicle(id);
    }



    @PutMapping("/update-mileage/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicleMileage(
            @PathVariable String vehicleId,
            @RequestParam double additionalKm) {
        Vehicle updatedVehicle = vehicleService.updateVehicleMileage(vehicleId, additionalKm);
        return ResponseEntity.ok(updatedVehicle);
    }

    // ✅ Recherche par marque
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<Vehicle>> searchVehicles(@RequestParam String brand) {
        List<Vehicle> vehicles = vehicleService.searchVehicles(brand);
        if (vehicles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vehicles);
    }

}

