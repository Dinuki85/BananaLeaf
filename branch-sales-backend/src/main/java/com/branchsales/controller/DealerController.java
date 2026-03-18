package com.branchsales.controller;

import com.branchsales.dto.DealerDTO;
import com.branchsales.service.DealerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dealers")
@CrossOrigin(origins = "http://localhost:5173")
public class DealerController {
    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    @GetMapping
    public ResponseEntity<List<DealerDTO>> getAllDealers() {
        return ResponseEntity.ok(dealerService.getAllDealers());
    }

    @PostMapping
    public ResponseEntity<DealerDTO> createDealer(@RequestBody DealerDTO dealerDTO) {
        return ResponseEntity.ok(dealerService.createDealer(dealerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealerDTO> updateDealer(@PathVariable Long id, @RequestBody DealerDTO dealerDTO) {
        return ResponseEntity.ok(dealerService.updateDealer(id, dealerDTO));
    }

}
