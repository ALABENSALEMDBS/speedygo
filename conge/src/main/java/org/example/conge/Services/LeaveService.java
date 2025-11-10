package org.example.conge.Services;

import org.example.conge.client.UserClient;
import org.example.conge.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.conge.Repository.LeaveRepo;
import org.example.conge.Repository.LeaveSettingsRepo;

import org.example.conge.entities.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveService implements ILeaveService {

    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private UserClient userClient;

    @Autowired
    private LeaveSettingsService leaveSettingsService;

    @Autowired
    private LeaveSettingsRepo leaveSettingsRepo;

    @Override
    public Leave createLeave(Leave leave) {
        leave.setStatus(Status.PENDING);
        return leaveRepo.save(leave);
    }

    @Override
    public Map<String, Object> checkLeaveExceeding(Leave leave) {
        long daysRequested = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        int maxAllowed = leaveSettingsService.getSettings().getMaxAllowedDays();

        // 🔁 On récupère toutes les demandes existantes du conducteur
        List<Leave> existingLeaves = leaveRepo.findByDriverId(leave.getDriverId()).stream()
                .filter(l -> l.getStatus() == Status.APPROVED || l.getStatus() == Status.PENDING)
                .collect(Collectors.toList());

        // ✅ Ignore la demande actuelle si c’est un update (si elle a un ID)
        if (leave.getId() != null) {
            existingLeaves = existingLeaves.stream()
                    .filter(l -> !l.getId().equals(leave.getId()))
                    .collect(Collectors.toList());
        }

        // 🧮 On calcule le total des jours *hors* la demande actuelle
        long totalTaken = existingLeaves.stream()
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();

        long totalAfter = totalTaken + daysRequested;
        boolean exceeds = totalAfter > maxAllowed;

        Map<String, Object> result = new HashMap<>();
        result.put("daysRequested", daysRequested);
        result.put("totalBefore", totalTaken);
        result.put("totalAfter", totalAfter);
        result.put("maxAllowed", maxAllowed);
        result.put("exceeds", exceeds);
        result.put("exceededDays", Math.max(0, totalAfter - maxAllowed));

        // 🔎 Log optionnel (utile pour debug)
        System.out.println("🔢 checkLeaveExceeding: daysRequested = " + daysRequested);
        System.out.println("📄 Existing leaves count = " + existingLeaves.size());
        System.out.println("📊 totalTaken (excluding current) = " + totalTaken);
        System.out.println("🧮 totalAfter = " + totalAfter + " / exceeds = " + exceeds);

        return result;
    }


    @Override
    public List<Leave> getLeavesByDriver(String driverId) {
        return leaveRepo.findByDriverId(driverId);
    }

    @Override
    public List<Leave> getAllLeaves() {
        return leaveRepo.findAll();
    }

    @Override
    public Leave updateLeave(String id, Leave updatedLeave, boolean allowExceeding) {
        Leave existingLeave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!existingLeave.getStatus().equals(Status.PENDING)) {
            throw new IllegalStateException("Only pending leave requests can be updated.");
        }

        long updatedDays = ChronoUnit.DAYS.between(updatedLeave.getStartDate(), updatedLeave.getEndDate()) + 1;
        System.out.println("🔢 UpdatedDays (nouvelle demande) = " + updatedDays);
        System.out.println("🗓️ StartDate: " + updatedLeave.getStartDate() + " / EndDate: " + updatedLeave.getEndDate());

        List<Leave> otherLeaves = leaveRepo.findByDriverId(existingLeave.getDriverId()).stream()
                .filter(l -> !l.getId().equals(existingLeave.getId()))
                .filter(l -> l.getStatus() == Status.APPROVED || l.getStatus() == Status.PENDING)
                .toList();

        long totalDaysBefore = otherLeaves.stream()
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();

        System.out.println("📊 TotalDaysBefore (autres demandes) = " + totalDaysBefore);
        System.out.println("📄 Nombre de demandes exclues = " + otherLeaves.size());

        LeaveSettings settings = leaveSettingsRepo.findTopByOrderByIdDesc()
                .orElseGet(() -> {
                    LeaveSettings defaultSettings = new LeaveSettings();
                    defaultSettings.setMaxAllowedDays(10);
                    return defaultSettings;
                });

        System.out.println("⚙️ MaxAllowed = " + settings.getMaxAllowedDays());

        long totalAfterUpdate = totalDaysBefore + updatedDays;
        System.out.println("🧮 Total après mise à jour = " + totalAfterUpdate);

        if (!allowExceeding && totalAfterUpdate > settings.getMaxAllowedDays()) {
            System.out.println("❌ Dépassement détecté : " + (totalAfterUpdate - settings.getMaxAllowedDays()) + " jours");
            throw new IllegalArgumentException("Leave days limit exceeded for this driver.");
        }

        updatedLeave.setId(existingLeave.getId());
        updatedLeave.setStatus(existingLeave.getStatus());
        updatedLeave.setDriverId(existingLeave.getDriverId());

        System.out.println("✅ Mise à jour de la demande validée.");
        return leaveRepo.save(updatedLeave);
    }


    @Override
    public void deleteLeave(String id) {
        Leave leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (leave.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending leaves can be deleted.");
        }

        leaveRepo.deleteById(id);
    }

    @Override
    public Optional<Leave> getLeaveById(String id) {
        return leaveRepo.findById(id);
    }



    @Override
    public Leave updateLeaveStatus(String leaveId, Status  status) {
        Leave leave = leaveRepo.findById(leaveId).orElseThrow();
        leave.setStatus(status);
        return leaveRepo.save(leave);
    }


    @Override
    public void approveLeave(String id) {
        Leave leave = leaveRepo.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus(Status.APPROVED);

            // Calculate exceeded days based on all previous leaves and current leave
            Map<String, Object> checkResult = calculateExceededDaysForLeave(leave);
            boolean exceeds = (boolean) checkResult.get("exceedsLimit");

            if (exceeds) {
                int exceededDays = ((Number) checkResult.get("exceededDays")).intValue();
                leave.setExceededDays(exceededDays);
                System.out.println("⚠️ Leave exceeds limit by " + exceededDays + " days");
                // TODO: Implement salary cut calculation when SalaryService is available
            } else {
                // If no exceeded days, reset salary cut
                leave.setExceededDays(0);
                leave.setExceededSalaryCut(0);
            }

            // Save the leave with the updated status
            leaveRepo.save(leave);
        }
    }


    @Override
    public void rejectLeave(String id) {
        Leave leave = leaveRepo.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus(Status.REJECTED);
            leaveRepo.save(leave);
        }
    }

    @Override
    public long calculateTotalLeaveDays(String driverId) {
        int currentYear = LocalDate.now().getYear();

        List<Leave> leaves = leaveRepo.findByDriverId(driverId).stream()
                .filter(leave -> leave.getStatus() == Status.APPROVED || leave.getStatus() == Status.PENDING)
                .filter(leave -> leave.getStartDate().getYear() == currentYear) // 🆕 Ignore old years
                .toList();

        return leaves.stream()
                .mapToLong(leave -> ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1)
                .sum();
    }


    @Override
    public List<Map<String, Object>> getTotalDaysByDriver() {
        // Get all drivers from User microservice via Feign
        List<UserDTO> allDrivers = userClient.getAllUsers()
                .stream()
                .filter(u -> u.getRoles() != null && u.getRoles().contains("driver"))
                .toList();

        List<Leave> allLeaves = leaveRepo.findAll();

        List<Map<String, Object>> result = new ArrayList<>();

        for (UserDTO driver : allDrivers) {
            long totalDays = allLeaves.stream()
                    .filter(leave -> leave.getDriverId() != null && leave.getDriverId().equals(String.valueOf(driver.getId())))
                    .filter(leave -> leave.getStatus() == Status.APPROVED || leave.getStatus() == Status.PENDING)
                    .mapToLong(leave -> ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1)
                    .sum();

            Map<String, Object> entry = new HashMap<>();
            entry.put("firstName", driver.getFirstName());
            entry.put("lastName", driver.getLastName());
            entry.put("totalDaysTaken", totalDays);
            result.add(entry);
        }

        return result;
    }

    public List<Map<String, Object>> getDetailedSummaryByDriver() {
        // Get all drivers from User microservice via Feign
        List<UserDTO> drivers = userClient.getAllUsers().stream()
                .filter(user -> user.getRoles() != null && user.getRoles().stream()
                        .anyMatch(role -> role.equalsIgnoreCase("driver")))
                .toList();

        List<Leave> allLeaves = leaveRepo.findAll();

        List<Map<String, Object>> summary = new ArrayList<>();

        for (UserDTO driver : drivers) {
            long totalDays = allLeaves.stream()
                    .filter(leave -> leave.getDriverId() != null && leave.getDriverId().equals(String.valueOf(driver.getId())))
                    .filter(leave -> leave.getStatus() == Status.APPROVED || leave.getStatus() == Status.PENDING)
                    .mapToLong(leave -> ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1)
                    .sum();

            Map<String, Object> map = new HashMap<>();
            map.put("driverId", driver.getId());
            map.put("firstName", driver.getFirstName());
            map.put("lastName", driver.getLastName());
            map.put("totalDaysTaken", totalDays);
            map.put("limit", leaveSettingsService.getSettings().getMaxAllowedDays());
            summary.add(map);
        }

        return summary;
    }

    public List<LeaveDTO> getAllLeavesWithDriverNames() {
        List<Leave> leaves = leaveRepo.findAll();
        // Get all users from User microservice via Feign
        List<UserDTO> users = userClient.getAllUsers();
        Map<String, UserDTO> userMap = users.stream()
                .collect(Collectors.toMap(u -> String.valueOf(u.getId()), user -> user));

        return leaves.stream().map(leave -> {
            UserDTO driver = userMap.get(leave.getDriverId());

            String firstName = (driver != null) ? driver.getFirstName() : "Unknown";
            String lastName = (driver != null) ? driver.getLastName() : "";
            String fullName = firstName + " " + lastName;

            LeaveDTO dto = new LeaveDTO();
            dto.setId(leave.getId());
            dto.setStartDate(leave.getStartDate());
            dto.setEndDate(leave.getEndDate());
            dto.setReason(leave.getReason());
            dto.setStatus(leave.getStatus());
            dto.setDriverId(leave.getDriverId());
            dto.setDriverFirstName(firstName);
            dto.setDriverLastName(lastName);
            dto.setDriverFullName(fullName.trim());

            return dto;
        }).toList();
    }

    public LeaveDTO getLeaveWithDriverNameById(String id) {
        Leave leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // Get driver info from User microservice via Feign
        UserDTO driver = null;
        try {
            Long driverId = Long.parseLong(leave.getDriverId());
            driver = userClient.getUserById(driverId);
        } catch (Exception e) {
            System.out.println("⚠️ Could not fetch driver info: " + e.getMessage());
        }

        String firstName = (driver != null) ? driver.getFirstName() : "Unknown";
        String lastName = (driver != null) ? driver.getLastName() : "Driver";

        LeaveDTO dto = new LeaveDTO();
        dto.setId(leave.getId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        dto.setDriverId(leave.getDriverId());
        dto.setDriverFirstName(firstName);
        dto.setDriverLastName(lastName);
        dto.setDriverFullName((firstName + " " + lastName).trim());

        return dto;
    }

    @Override
    public Map<String, Object> calculateExceededDaysForLeave(Leave leave) {
        // Calculate the total days requested for the current leave
        long daysRequested = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        System.out.println("🔢 Requested days for current leave: " + daysRequested);

        // Fetch the driver's current leave requests
        List<Leave> existingLeaves = leaveRepo.findByDriverId(leave.getDriverId()).stream()
                .filter(l -> l.getStatus() == Status.APPROVED || l.getStatus() == Status.PENDING)
                .collect(Collectors.toList());

        // Exclude the current leave if it already has an ID (i.e., if it's an update)
        if (leave.getId() != null) {
            existingLeaves = existingLeaves.stream()
                    .filter(l -> !l.getId().equals(leave.getId()))
                    .collect(Collectors.toList());
        }

        // Sum of all previously taken leave days for the driver
        long totalDaysTaken = existingLeaves.stream()
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();
        System.out.println("📊 Total days already taken by driver: " + totalDaysTaken);

        // Get the maximum allowed leave days from the settings (ensure it’s treated as a long)
        long maxAllowed = leaveSettingsService.getSettings().getMaxAllowedDays();
        System.out.println("⚙️ Max allowed leave days: " + maxAllowed);

        // Calculate remaining days (how many more days the driver can take)
        long remainingAllowed = maxAllowed - totalDaysTaken;
        System.out.println("⚙️ Remaining allowed days: " + remainingAllowed);

        long exceededDays = 0;

        if (totalDaysTaken >= maxAllowed) {
            // If the total days already taken exceed the limit, all the requested leave days are exceeded
            exceededDays = daysRequested;
        } else {
            // If the total days have not exceeded the limit, we calculate the exceeded days
            exceededDays = Math.max(0, daysRequested - remainingAllowed);
        }

        // Return the results as long values
        Map<String, Object> result = new HashMap<>();
        result.put("daysRequested", daysRequested);
        result.put("totalBefore", totalDaysTaken);
        result.put("totalAfter", totalDaysTaken + daysRequested);
        result.put("exceededDays", exceededDays);
        result.put("exceedsLimit", exceededDays > 0); // Flag if exceeded limit

        // Log the final result
        System.out.println("✅ Calculation result: " + result);

        return result;
    }


}
