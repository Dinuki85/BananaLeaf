package com.branchsales.controller;

import com.branchsales.entity.SyncLog;
import com.branchsales.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SyncController {
    private final ProductService productService;

    @GetMapping("/updates")
    public ResponseEntity<List<SyncLog>> getUpdates(
            @RequestParam(name = "sinceVersion") Long sinceVersion,
            @RequestParam(name = "branchId") Long branchId) {
        return ResponseEntity.ok(productService.getUpdates(sinceVersion, branchId));
    }

    @PostMapping("/ack")
    public ResponseEntity<Void> acknowledgeSync(@RequestParam(name = "branchId") Long branchId, 
                                              @RequestParam(name = "version") Long version) {
        // In a real system, you might store the last synced version per branch in a 'Branch' table
        // For now, we return 200 OK to signify receipt
        return ResponseEntity.ok().build();
    }
}
