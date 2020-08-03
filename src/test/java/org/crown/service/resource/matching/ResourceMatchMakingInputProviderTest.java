package org.crown.service.resource.matching;

import org.crown.domain.Claim;
import org.crown.domain.ReceiverResource;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;
import org.crown.repository.ClaimRepository;
import org.crown.repository.ReceiverResourceRepository;
import org.crown.repository.SupplierResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceMatchMakingInputProviderTest {

    private static final ResourceType FACE_SHIELD = new ResourceType().name("Face shield");
    private static final ReceiverResource RECEIVER_RESOURCE_1 = new ReceiverResource().name("receiver 1");
    private static final ReceiverResource RECEIVER_RESOURCE_2 = new ReceiverResource().name("receiver 2");
    private static final SupplierResource SUPPLIER_RESOURCE_1 = new SupplierResource();
    private static final Claim CLAIM_1 = new Claim()
        .receiverResource(RECEIVER_RESOURCE_1)
        .supplierResource(new SupplierResource());
    @Mock
    private ReceiverResourceRepository receiverResourceRepository;
    @Mock
    private SupplierResourceRepository supplierResourceRepository;
    @Mock
    private ClaimRepository claimRepository;

    private ResourceMatchMakingInputProvider resourceMatchMakingInputProvider;

    @BeforeEach
    public void init() {
        resourceMatchMakingInputProvider = new ResourceMatchMakingInputProvider(
            receiverResourceRepository,
            supplierResourceRepository,
            claimRepository
        );
    }

    @Test
    void fetch() {
        when(receiverResourceRepository.findAllByResourceType(FACE_SHIELD))
            .thenReturn(asList(RECEIVER_RESOURCE_1, RECEIVER_RESOURCE_2));
        when(supplierResourceRepository.findAllByResourceType(FACE_SHIELD))
            .thenReturn(asList(SUPPLIER_RESOURCE_1));
        when(claimRepository.findAllByReceiverResourceIsIn(asList(RECEIVER_RESOURCE_1, RECEIVER_RESOURCE_2)))
            .thenReturn(asList(CLAIM_1));
        when(claimRepository.findAllBySupplierResourceIsIn(asList(SUPPLIER_RESOURCE_1)))
            .thenReturn(Collections.emptyList());

        ResourceMatchMakingInput resourceMatchMakingInput = resourceMatchMakingInputProvider.fetch(FACE_SHIELD);

        assertThat(resourceMatchMakingInput.getResourceType()).isEqualTo(FACE_SHIELD);
        assertThat(resourceMatchMakingInput.getReceiverResources()).isEqualTo(asList(RECEIVER_RESOURCE_2));
        assertThat(resourceMatchMakingInput.getSupplierResources()).isEqualTo(asList(SUPPLIER_RESOURCE_1));
    }
}
