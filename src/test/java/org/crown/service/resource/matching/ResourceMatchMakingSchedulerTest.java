package org.crown.service.resource.matching;

import org.crown.domain.ResourceType;
import org.crown.repository.ResourceTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceMatchMakingSchedulerTest {

    private static final ResourceType FACE_SHIELD = new ResourceType().name("Face shield");
    private static final ResourceType MASKS = new ResourceType().name("Masks");

    @Mock
    private ResourceTypeRepository resourceTypeRepository;
    @Mock
    private ResourceMatchMakingService resourceMatchMakingService;

    @Autowired
    private ResourceMatchMakingScheduler scheduler;

    @BeforeEach
    public void init() {
        scheduler = new ResourceMatchMakingScheduler(
            resourceTypeRepository,
            resourceMatchMakingService
        );
    }

    @Test
    void run() {
        when(resourceTypeRepository.findAll())
            .thenReturn(asList(FACE_SHIELD, MASKS));

        scheduler.run();

        verify(resourceMatchMakingService, times(2))
            .matchResourcesFor(any());
        verify(resourceMatchMakingService).matchResourcesFor(FACE_SHIELD);
        verify(resourceMatchMakingService).matchResourcesFor(MASKS);
    }
}
