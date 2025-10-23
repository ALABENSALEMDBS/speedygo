package com.esprit.microservice.complaint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private UserClient userClient;


    // ===========> get USER from complaint microservice <============
    public UserDTO getUserById(Long id) {
        return userClient.getUserById(id);
    }
    //================================================================
    public Complaint createComplaintUSER(Complaint complaint) {
        UserDTO user = userClient.getUserById(complaint.getUserId());
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id " + complaint.getUserId());
        }
        complaint.setUserId(user.getId());
        return complaintRepository.save(complaint);
    }





    // CREATE
    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // READ all complaints
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // READ by ID
    public Optional<Complaint> getComplaintById(int id) {
        return complaintRepository.findById(id);
    }

    // READ by userId
    public List<Complaint> getComplaintsByUserId(int userId) {
        return complaintRepository.findByUserId(userId);
    }

    // UPDATE
    public Complaint updateComplaint(int id, Complaint complaintDetails) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id " + id));

        complaint.setTitle(complaintDetails.getTitle());
        complaint.setDescription(complaintDetails.getDescription());
        complaint.setCategory(complaintDetails.getCategory());
        complaint.setStatus(complaintDetails.getStatus());
        complaint.setContactEmail(complaintDetails.getContactEmail());
        complaint.setContactPhone(complaintDetails.getContactPhone());
        complaint.setUserId(complaintDetails.getUserId());

        return complaintRepository.save(complaint);
    }

    // DELETE
    public void deleteComplaint(int id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id " + id));
        complaintRepository.delete(complaint);
    }
    //
}

