package com.branchsales.service;

import com.branchsales.dto.DealerDTO;
import com.branchsales.entity.Dealer;
import com.branchsales.repository.DealerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DealerService {
    private final DealerRepository dealerRepository;

    public DealerService(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    @Transactional(readOnly = true)
    public List<DealerDTO> getAllDealers() {
        return dealerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DealerDTO createDealer(DealerDTO dealerDTO) {
        Dealer dealer = Dealer.builder()
                .name(dealerDTO.getName())
                .phone(dealerDTO.getPhone())
                .isActive(dealerDTO.getIsActive() != null ? dealerDTO.getIsActive() : true)
                .build();
        return convertToDTO(dealerRepository.save(dealer));
    }

    public DealerDTO updateDealer(Long id, DealerDTO dealerDTO) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer not found"));
        dealer.setName(dealerDTO.getName());
        dealer.setPhone(dealerDTO.getPhone());
        dealer.setIsActive(dealerDTO.getIsActive());
        return convertToDTO(dealerRepository.save(dealer));
    }

    public void deleteDealer(Long id) {
        dealerRepository.deleteById(id);
    }

    private DealerDTO convertToDTO(Dealer dealer) {
        return DealerDTO.builder()
                .id(dealer.getId())
                .name(dealer.getName())
                .phone(dealer.getPhone())
                .isActive(dealer.getIsActive())
                .build();
    }
}
