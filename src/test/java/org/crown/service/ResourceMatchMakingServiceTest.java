package org.crown.service;

import org.crown.domain.ResourceMatch;
import org.crown.domain.ResourceType;
import org.crown.repository.ResourceMatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceMatchMakingServiceTest {

    private ResourceType resourceType;

    @Mock
    private ResourceMatchMakingInputProvider resourceMatchMakingInputProvider;

    @Mock
    private MaxQuantityServedStrategy maxQuantityServedStrategy;

    @Mock
    private ResourceMatchRepository resourceMatchRepository;

    @Autowired
    private ResourceMatchMakingService resourceMatchMakingService;

    @Test
    void matchResourcesFor() {
        resourceType = new ResourceType();
        ResourceMatchMakingInput resourceMatchMakingInput = new ResourceMatchMakingInput();
        List<ResourceMatch> resourceMatches = new ArrayList<ResourceMatch>();

        when(resourceMatchMakingInputProvider.fetch(resourceType)).thenReturn(resourceMatchMakingInput);
        when(maxQuantityServedStrategy.generateMatches(resourceMatchMakingInput)).thenReturn(resourceMatches);

        resourceMatchMakingService = new ResourceMatchMakingService(
            resourceMatchMakingInputProvider,
            maxQuantityServedStrategy,
            resourceMatchRepository
        );
        resourceMatchMakingService.matchResourcesFor(resourceType);

        verify(resourceMatchRepository).deleteAll();
        verify(resourceMatchRepository).saveAll(resourceMatches);
    }
}
