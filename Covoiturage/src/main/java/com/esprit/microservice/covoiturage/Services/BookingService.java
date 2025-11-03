package com.esprit.microservice.covoiturage.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.esprit.microservice.covoiturage.Repository.BookingRepository;
import com.esprit.microservice.covoiturage.Repository.CarpoolingRepository;
import com.esprit.microservice.covoiturage.entities.Booking;
import com.esprit.microservice.covoiturage.entities.Carpooling;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private CarpoolingRepository carpoolingRepo;

    public Booking createBooking(Booking booking) {
        Carpooling ride = carpoolingRepo.findById(booking.getRideId()).orElseThrow();
        if (ride.getAvailableSeats() < booking.getSeatsBooked()) {
            throw new IllegalStateException("Not enough seats available");
        }

        booking.setStatus("PENDING");
        return bookingRepo.save(booking);
    }

    public Booking confirmBooking(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow();
        Carpooling ride = carpoolingRepo.findById(booking.getRideId()).orElseThrow();

        // Check if there are sufficient seats available for the booking
        if (ride.getAvailableSeats() < booking.getSeatsBooked()) {
            booking.setStatus("FULL"); // Set the status to FULL when no seats are available
            bookingRepo.save(booking); // Save the booking status as FULL
            throw new IllegalStateException("No available seats for this booking.");
        }

        // If seats are available, confirm the booking
        booking.setStatus("CONFIRMED");
        ride.setAvailableSeats(ride.getAvailableSeats() - booking.getSeatsBooked());

        // Update the carpooling ride seats and the booking status
        carpoolingRepo.save(ride);
        return bookingRepo.save(booking);
    }

    public List<Booking> getBookingsForRide(String rideId) {
        return bookingRepo.findByRideId(rideId);
    }

    public List<Booking> getBookingsByDriver(String driverId) {
        List<Carpooling> rides = carpoolingRepo.findByDriverId(driverId);
        return rides.stream()
                .flatMap(ride -> bookingRepo.findByRideId(ride.getId()).stream())
                .filter(b -> b.getSeatsBooked() > 0)
                .toList();
    }

    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepo.findByPassengerId(userId);
    }

    public Booking declineBooking(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow();

        if (!"PENDING".equals(booking.getStatus())) {
            throw new IllegalStateException("Only PENDING bookings can be declined");
        }

        booking.setStatus("DECLINED");
        return bookingRepo.save(booking);
    }

    public Booking cancelBooking(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow();
        Carpooling ride = carpoolingRepo.findById(booking.getRideId()).orElseThrow();

        // Check if the booking is not already canceled or declined
        if (!booking.getStatus().equals("CANCELED") && !booking.getStatus().equals("DECLINED")) {
            // Set the status to CANCELED
            booking.setStatus("CANCELED");

            // Return the reserved seats back to the available seats
            ride.setAvailableSeats(ride.getAvailableSeats() + booking.getSeatsBooked());

            // Save the updated ride and booking
            carpoolingRepo.save(ride);
            return bookingRepo.save(booking);
        }

        return booking; // If already canceled or declined, return as is
    }

}

