package org.example.conge.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.conge.Services.ILeaveSettingsService;
import org.example.conge.entities.LeaveSettings;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/leaveSettings")
public class LeaveSettingsController {
    ILeaveSettingsService leaveService;

    @Autowired
    public LeaveSettingsController(ILeaveSettingsService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping
    public ResponseEntity<LeaveSettings> getSettings() {
        return ResponseEntity.ok(leaveService.getSettings());
    }

    @PutMapping
    public ResponseEntity<LeaveSettings> updateSettings(@RequestBody Map<String, Integer> payload) {
        int newMax = payload.get("maxAllowedDays");
        return ResponseEntity.ok(leaveService.updateMaxAllowedDays(newMax));
    }
}
