package org.crown.service.resource.matching;

import org.crown.CrownApp;
import org.crown.domain.ReceiverResource;
import org.crown.domain.ResourceMatch;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;
import org.crown.helper.ReceiverResourceCreator;
import org.crown.helper.SupplierResourceCreator;
import org.crown.repository.ReceiverResourceRepository;
import org.crown.repository.ResourceMatchRepository;
import org.crown.repository.ResourceTypeRepository;
import org.crown.repository.SupplierResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CrownApp.class)
public class ResourceMatchMakingServiceIT {

    private static final ResourceType FACE_SHIELD = new ResourceType().name("Face shield");

    @Autowired
    private ResourceMatchRepository resourceMatchRepository;

    @Autowired
    private ResourceTypeRepository resourceTypeRepository;

    @Autowired
    private SupplierResourceRepository supplierResourceRepository;

    @Autowired
    private ReceiverResourceRepository receiverResourceRepository;

    @Autowired
    private ResourceMatchMakingService resourceMatchMakingService;

    @BeforeEach
    public void init() {
        resourceTypeRepository.deleteAll();
        receiverResourceRepository.deleteAll();
        supplierResourceRepository.deleteAll();
        resourceMatchRepository.deleteAll();
    }

    @Test
    public void testMatchResources() {
        resourceTypeRepository.save(FACE_SHIELD);
        List<ReceiverResource> receiverResources = ReceiverResourceCreator.createReceiverResourcesWithQuantities(
            new ArrayList<>(asList(500, 50, 100)),
            FACE_SHIELD
        );
        receiverResourceRepository.saveAll(receiverResources);
        List<SupplierResource> supplierResources = SupplierResourceCreator.createSupplierResourcesWithQuantities(
            new ArrayList<>(asList(300, 50, 100)),
            FACE_SHIELD
        );
        supplierResourceRepository.saveAll(supplierResources);

        resourceMatchMakingService.matchResourcesFor(FACE_SHIELD);

        List<ResourceMatch> resourceMatches = resourceMatchRepository.findAll();
        assertThat(resourceMatches.size()).isEqualTo(3);
        assertThat(resourceMatches.get(0).getReceiverResource()).isEqualTo(receiverResources.get(0));
        assertThat(resourceMatches.get(0).getSupplierResource()).isEqualTo(supplierResources.get(0));
        assertThat(resourceMatches.get(0).getQuantity()).isEqualTo(300);
        assertThat(resourceMatches.get(1).getReceiverResource()).isEqualTo(receiverResources.get(0));
        assertThat(resourceMatches.get(1).getSupplierResource()).isEqualTo(supplierResources.get(2));
        assertThat(resourceMatches.get(1).getQuantity()).isEqualTo(100);
        assertThat(resourceMatches.get(2).getReceiverResource()).isEqualTo(receiverResources.get(0));
        assertThat(resourceMatches.get(2).getSupplierResource()).isEqualTo(supplierResources.get(1));
        assertThat(resourceMatches.get(2).getQuantity()).isEqualTo(50);

    }
}
