package org.example.paiment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AdService implements IAd {

    @Autowired
    private AdRepo adRepo;
    @Autowired
    private UserClient userClient;


    @Override
    public List<Ad> getAllAds() {
        return adRepo.findAll();
    }



    @Override
    public Ad createAd(Ad ad) {
        try {


            // Save to database
            Ad savedAd = adRepo.save(ad);

            System.out.println("✅ Ad saved successfully: ID=" + savedAd.getId());
            return savedAd;

        } catch (Exception e) {
            System.err.println("❌ Error while creating ad: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public Ad getAdById(Long id) { // ✅ Changé String à Long
        return adRepo.findById(id).orElse(null);
    }

    @Override
    public Ad updateAd(Ad ad) {
        return adRepo.save(ad);
    }

    @Override
    public void deleteAd(Long id) { // ✅ Changé String à Long
        adRepo.deleteById(id);
    }

    // ===========> get USER from complaint microservice <============
    public UserDTO getUserById(Long id) {
        return userClient.getUserById(id);
    }
    public Ad createAdUSER(Ad ad) {
        UserDTO user = userClient.getUserById(ad.getUserId());
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id " + ad.getUserId());
        }
        ad.setUserId(user.getId());
        return adRepo.save(ad);
    }


}