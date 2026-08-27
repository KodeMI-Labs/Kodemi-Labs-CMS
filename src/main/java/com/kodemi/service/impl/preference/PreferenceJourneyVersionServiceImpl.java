package com.kodemi.service.impl.preference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.kodemi.dto.reviewsandratings.preference.PreferenceJourneyVersionDto;
import com.kodemi.model.PreferenceJourneyVersion;
import com.kodemi.repository.preference.PreferenceJourneyVersionRepository;
import com.kodemi.service.preference.PreferenceJourneyVersionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceJourneyVersionServiceImpl implements PreferenceJourneyVersionService {

    private final PreferenceJourneyVersionRepository versionRepository;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private PreferenceJourneyVersion findOrThrow(String version_id) {
        PreferenceJourneyVersion v = versionRepository.findById(version_id);
        if (v == null) throw new ResourceNotFoundException("Journey version not found: " + version_id);
        return v;
    }

    private PreferenceJourneyVersionDto toDto(PreferenceJourneyVersion v) {
        PreferenceJourneyVersionDto dto = new PreferenceJourneyVersionDto();
        BeanUtils.copyProperties(v, dto);
        return dto;
    }

    // ── Internal: called by PreferenceJourneyService on publish ───────────────

    @Override
    public PreferenceJourneyVersionDto createVersion(String journey_id, Integer version, String remarks) {
        // Deactivate any previous active version for this journey
        versionRepository.findActiveByJourneyId(journey_id).forEach(prev -> {
            prev.setActive(false);
            versionRepository.save(prev);
        });

        PreferenceJourneyVersion snapshot = new PreferenceJourneyVersion();
        snapshot.setVersion_id(UUID.randomUUID().toString());
        snapshot.setJourney_id(journey_id);
        snapshot.setVersion(version);
        snapshot.setActive(true);
        snapshot.setPublished_at(LocalDateTime.now());
        snapshot.setRemarks(remarks);
        snapshot.setCreated_at(LocalDateTime.now());
        return toDto(versionRepository.save(snapshot));
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    public PreferenceJourneyVersionDto getVersionById(String version_id) {
        return toDto(findOrThrow(version_id));
    }

    @Override
    public List<PreferenceJourneyVersionDto> getVersionsByJourneyId(String journey_id) {
        return versionRepository.findByJourneyId(journey_id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PreferenceJourneyVersionDto getActiveVersion(String journey_id) {
        List<PreferenceJourneyVersion> active = versionRepository.findActiveByJourneyId(journey_id);
        if (active.isEmpty()) {
            throw new ResourceNotFoundException("No active version found for journey: " + journey_id);
        }
        return toDto(active.get(0));
    }

    @Override
    public String deleteVersion(String version_id) {
        findOrThrow(version_id);
        versionRepository.deleteById(version_id);
        return "Journey version deleted successfully";
    }
}
