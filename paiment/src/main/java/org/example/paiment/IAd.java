package org.example.paiment;

import java.util.List;

public interface IAd {

    // ✅ Get all ads
    List<Ad> getAllAds();

    // ✅ Create a new ad
    Ad createAd(Ad ad);

    // ✅ Get ad by ID
    Ad getAdById(Long id);

    // ✅ Update an existing ad
    Ad updateAd(Ad ad);

    // ✅ Delete ad by ID
    void deleteAd(Long id);
     UserDTO getUserById(Long id);
     Ad createAdUSER(Ad ad);
}