package com.speedygo.fastpostdelivery.Controllers;

import com.speedygo.fastpostdelivery.Services.FastPostService;
import com.speedygo.fastpostdelivery.entities.FastPost;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fastpost")

public class FastPostController {

    @Autowired
    private FastPostService fastPostService;

    @GetMapping
    public List<FastPost> getAllFastPosts() {
        return fastPostService.getAllFastPosts();
    }

    @GetMapping("/{id}")
    public FastPost getFastPost(@PathVariable Long id) {
        return fastPostService.getFastPost(id);
    }

    @PostMapping
    public FastPost addFastPost(@RequestBody FastPost fastPost) {
        return fastPostService.addFastPost(fastPost);
    }

    @PutMapping("/{id}")
    public FastPost modifyFastPost(@PathVariable Long id, @RequestBody FastPost fastPost) {
        return fastPostService.modifyFastPost(id, fastPost);
    }

    @DeleteMapping("/{id}")
    public void deleteFastPost(@PathVariable Long id) {
        fastPostService.deleteFastPost(id);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Void> approveFastPost(@PathVariable Long id) {
        fastPostService.approveFastPost(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Void> rejectFastPost(@PathVariable Long id) {
        fastPostService.rejectFastPost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/approved")
    public List<FastPost> getApprovedFastPosts() {
        return fastPostService.getApprovedFastPosts();
    }
}
