package org.crown.service.resource.matching;

import org.crown.domain.ResourceType;
import org.crown.repository.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ResourceMatchMakingScheduler {

    @Autowired
    private ResourceTypeRepository resourceTypeRepository;
    @Autowired
    private ResourceMatchMakingService resourceMatchMakingService;

    private List<ResourceType> resourceTypes;

    public ResourceMatchMakingScheduler(
        ResourceTypeRepository resourceTypeRepository,
        ResourceMatchMakingService resourceMatchMakingService
    ) {
        this.resourceTypeRepository = resourceTypeRepository;
        this.resourceMatchMakingService = resourceMatchMakingService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        resourceTypeRepository.findAll().forEach(resourceType ->
            resourceMatchMakingService.matchResourcesFor(resourceType)
        );
    }
}
