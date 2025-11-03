package org.example.paiment;

/**
 * Enum representing the possible statuses of an ad
 */
public enum AdStatus {
    APPROVED,       // Ad has been approved by admin
    PENDING_ADMIN_REVIEW, // Ad is waiting for admin review
    REJECTED,       // Ad has been rejected by admin
    EXPIRED         // Ad has expired (past its expiration date)
}
