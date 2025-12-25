package com.nazir.pos.posconfig;

import com.nazir.pos.posconfig.dto.PosConfigRequest;
import com.nazir.pos.posconfig.dto.PosConfigResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PosConfigService {

    private final PosConfigRepository repository;

    public PosConfigResponse saveOrUpdate(PosConfigRequest request, Long storeId, String adminUsername) {
        PosConfig pos;
        if (request.getId() != null) {
            pos = repository.findByIdAndStoreId(request.getId(), storeId)
                    .orElseThrow(() -> new IllegalArgumentException("POS config not found for id=" + request.getId()));
        } else {
            pos = PosConfig.builder()
                    .storeId(storeId)
                    .createdAt(LocalDateTime.now())
                    .createdBy(adminUsername)
                    .build();
        }

        pos.setPosName(request.getPosName());
        pos.setGstNo(request.getGstNo());
        pos.setAddress(request.getAddress());
        pos.setLocation(request.getLocation());
        pos.setMobile1(request.getMobile1());
        pos.setMobile2(request.getMobile2());
        pos.setUpdatedAt(LocalDateTime.now());
        pos.setUpdatedBy(adminUsername);

        PosConfig saved = repository.save(pos);
        log.info("POS CONFIG | Saved POS id={}, storeId={}", saved.getId(), storeId);
        return mapToResponse(saved);
    }

    public List<PosConfigResponse> configDtl(Long storeId) {
        return repository.findByStoreId(storeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PosConfigResponse mapToResponse(PosConfig pos) {
        return PosConfigResponse.builder()
                .id(pos.getId())
                .posName(pos.getPosName())
                .gstNo(pos.getGstNo())
                .address(pos.getAddress())
                .location(pos.getLocation())
                .mobile1(pos.getMobile1())
                .mobile2(pos.getMobile2())
                .build();
    }
}
