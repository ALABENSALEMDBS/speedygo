package com.speedygo.fastpostdelivery.entities;

public enum DeliveryStatus {
    PENDING,        // En attente d'acceptation du chauffeur
    ACCEPTED,       // Acceptée par le chauffeur
    REJECTED,       // Rejetée par le chauffeur
    IN_PROGRESS,    // En cours de livraison (chauffeur en route)
    DELIVERED,      // Livrée avec succès
    CANCELLED       // Annulée
}
