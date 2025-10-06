package com.esprit.microservice.complaint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // CREATE
    @PostMapping
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    // READ all
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable int id) {
        return complaintService.getComplaintById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ by userId
    @GetMapping("/user/{userId}")
    public List<Complaint> getComplaintsByUserId(@PathVariable int userId) {
        return complaintService.getComplaintsByUserId(userId);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Complaint updateComplaint(@PathVariable int id, @RequestBody Complaint complaintDetails) {
        return complaintService.updateComplaint(id, complaintDetails);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(@PathVariable int id) {
        try {
            complaintService.deleteComplaint(id);
            return ResponseEntity.ok("Complaint with ID " + id + " deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}

