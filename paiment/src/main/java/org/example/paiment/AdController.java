package org.example.paiment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private IAd adService;

    // ✅ Get all ads
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Ad> getAllAds() {
        return adService.getAllAds();
    }

    // ✅ Get ad by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Ad getAdById(@PathVariable Long id) {
        return adService.getAdById(id);
    }

    // ✅ Create ad
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Ad createAd(@RequestBody Ad ad) {
        return adService.createAd(ad);
    }

    // ✅ Update ad
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Ad updateAd(@RequestBody Ad ad) {
        return adService.updateAd(ad);
    }

    // ✅ Delete ad
    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
    }
}
