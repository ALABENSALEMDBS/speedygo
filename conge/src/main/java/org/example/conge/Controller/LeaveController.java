package org.example.conge.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.conge.Services.ILeaveService;
import org.example.conge.entities.Leave;
import org.example.conge.entities.LeaveDTO;
import org.example.conge.entities.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private ILeaveService leaveService;

    @PostMapping("/create")
    public ResponseEntity<Leave> createLeave(@RequestBody Leave leave) {
        return ResponseEntity.ok(leaveService.createLeave(leave));
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkLeave(@RequestBody Leave leave) {
        return ResponseEntity.ok(leaveService.checkLeaveExceeding(leave));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Leave>> getMyLeaves(@RequestParam String driverId) {
        return ResponseEntity.ok(leaveService.getLeavesByDriver(driverId));
    }

    @GetMapping
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @PutMapping("/confirm-update/{id}")
    public ResponseEntity<Leave> confirmUpdate(
            @PathVariable String id,
            @RequestBody Leave updatedLeave) {
        return ResponseEntity.ok(leaveService.updateLeave(id, updatedLeave, true));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable String id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable String id) {
        Optional<Leave> leave = leaveService.getLeaveById(id);
        return leave.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Leave> updateStatus(
            @PathVariable String id,
            @RequestParam Status status) {
        return ResponseEntity.ok(leaveService.updateLeaveStatus(id, status));
    }

    @PutMapping("/approve/{id}")
    public void approveLeave(@PathVariable("id") String id) {
        leaveService.approveLeave(id);
    }

    @PutMapping("/reject/{id}")
    public void rejectLeave(@PathVariable("id") String id) {
        leaveService.rejectLeave(id);
    }

    @GetMapping("/total-days")
    public ResponseEntity<Long> getTotalLeaveDays(@RequestParam String driverId) {
        long totalDays = leaveService.calculateTotalLeaveDays(driverId);
        return ResponseEntity.ok(totalDays);
    }

    @GetMapping("/total-days-by-driver") // Renommer ce endpoint pour l’admin
    public ResponseEntity<List<Map<String, Object>>> getTotalDaysPerDriver() {
        return ResponseEntity.ok(leaveService.getTotalDaysByDriver());
    }

    @GetMapping("/by-driver/{driverId}")
    public ResponseEntity<List<Leave>> getLeavesByDriverId(@PathVariable String driverId) {
        return ResponseEntity.ok(leaveService.getLeavesByDriver(driverId));
    }

    @GetMapping("/summary-by-driver")
    public ResponseEntity<List<Map<String, Object>>> getSummaryByDriver() {
        return ResponseEntity.ok(leaveService.getDetailedSummaryByDriver());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Leave>> getLeavesByStatus(@RequestParam Status status) {
        return ResponseEntity.ok(leaveService.getAllLeaves().stream()
                .filter(leave -> leave.getStatus() == status)
                .toList());
    }

    @GetMapping("/detailed")
    public ResponseEntity<List<LeaveDTO>> getDetailedLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeavesWithDriverNames());
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<LeaveDTO> getLeaveDTOById(@PathVariable String id) {
        try {
            LeaveDTO dto = leaveService.getLeaveWithDriverNameById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/salary-cuts/{driverId}")
    public ResponseEntity<Map<String, Double>> getSalaryCutsByMonth(@PathVariable String driverId) {
        List<Leave> leaves = leaveService.getLeavesByDriver(driverId); // 👉 IMPORTANT: use service not repo directly

        Map<String, Double> salaryCutsByMonth = new HashMap<>();

        for (Leave leave : leaves) {
            if (leave.getExceededSalaryCut() > 0) {
                String key = leave.getStartDate().getYear() + "-" + String.format("%02d", leave.getStartDate().getMonthValue());
                salaryCutsByMonth.merge(key, leave.getExceededSalaryCut(), Double::sum);
            }
        }

        return ResponseEntity.ok(salaryCutsByMonth);
    }

}