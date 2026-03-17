package com.branchsales.service;

import com.branchsales.entity.CloudChangeLog;
import com.branchsales.repository.CloudChangeLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudChangeLogService {

    private final CloudChangeLogRepository cloudChangeLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void logChange(String tableName, Long recordId, String action, Object data, Long branchId) {
        try {
            String convertedJson = objectMapper.writeValueAsString(data);

            CloudChangeLog changeLog = CloudChangeLog.builder()
                    .tableName(tableName)
                    .recordId(recordId)
                    .action(action)
                    .data(convertedJson)
                    .branchId(branchId)
                    .build();

            CloudChangeLog savedLog = cloudChangeLogRepository.save(changeLog);
            
            // Map sequence_no to id for sequential tracking
            savedLog.setSequenceNo(savedLog.getId());
            cloudChangeLogRepository.save(savedLog);

            log.debug("Logged change for table {}: id={}, action={}, sequence={}", tableName, recordId, action, savedLog.getSequenceNo());
        } catch (Exception e) {
            log.error("Failed to convert data to JSON for change log: table={}, recordId={}", tableName, recordId, e);
            // We might not want to throw here to avoid blocking the main transaction, 
            // but the prompt implies this is part of the flow.
            throw new RuntimeException("Error logging cloud change", e);
        }
    }
}
