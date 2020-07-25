package org.crown.service;

import org.crown.domain.ResourceMatch;
import org.crown.domain.ResourceType;
import org.crown.repository.ResourceMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceMatchMakingService {

    @Autowired
    private final ResourceMatchMakingInputProvider resourceMatchMakingInputProvider;
    @Autowired
    private final MaxQuantityServedStrategy maxQuantityServedStrategy;
    @Autowired
    private final ResourceMatchRepository resourceMatchRepository;

    public ResourceMatchMakingService(
        ResourceMatchMakingInputProvider resourceMatchMakingInputProvider,
        MaxQuantityServedStrategy maxQuantityServedStrategy,
        ResourceMatchRepository resourceMatchRepository
    ) {
        this.resourceMatchMakingInputProvider = resourceMatchMakingInputProvider;
        this.maxQuantityServedStrategy = maxQuantityServedStrategy;
        this.resourceMatchRepository = resourceMatchRepository;
    }

    public void matchResourcesFor(ResourceType resourceType) {
        ResourceMatchMakingInput resourceMatchMakingInput = resourceMatchMakingInputProvider.fetch(resourceType);
        List<ResourceMatch>  resourceMatches = maxQuantityServedStrategy.generateMatches(resourceMatchMakingInput);
        resourceMatchRepository.deleteAll();
        resourceMatchRepository.saveAll(resourceMatches);
    }
}
