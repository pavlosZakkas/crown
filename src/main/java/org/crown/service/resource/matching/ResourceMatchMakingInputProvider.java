package org.crown.service.resource.matching;

import org.crown.domain.Claim;
import org.crown.domain.ReceiverResource;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;
import org.crown.repository.ClaimRepository;
import org.crown.repository.ReceiverResourceRepository;
import org.crown.repository.SupplierResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceMatchMakingInputProvider {
    @Autowired
    private ReceiverResourceRepository receiverResourceRepository;
    @Autowired
    private SupplierResourceRepository supplierResourceRepository;
    @Autowired
    private ClaimRepository claimRepository;

    public ResourceMatchMakingInputProvider(
        ReceiverResourceRepository receiverResourceRepository,
        SupplierResourceRepository supplierResourceRepository,
        ClaimRepository claimRepository
    ) {

        this.receiverResourceRepository = receiverResourceRepository;
        this.supplierResourceRepository = supplierResourceRepository;
        this.claimRepository = claimRepository;
    }

    public ResourceMatchMakingInput fetch(ResourceType resourceType) {
        List<ReceiverResource> receiverResources = receiverResourceRepository.findAllByResourceType(resourceType);
        List<SupplierResource> supplierResources = supplierResourceRepository.findAllByResourceType(resourceType);
        List<ReceiverResource> claimedReceiverResources = claimedReceiverResourcesFrom(receiverResources);
        List<SupplierResource> claimedSupplierResources = claimedSupplierResourcesFrom(supplierResources);

        List<ReceiverResource> unclaimedReceiverResources = unclaimedReceiverResourcesFrom(receiverResources, claimedReceiverResources);
        List<SupplierResource> unclaimedSupplierResources = unclaimedSupplierResourcesFrom(supplierResources, claimedSupplierResources);

        return resourceMatchMakingInputFrom(resourceType, unclaimedReceiverResources, unclaimedSupplierResources);
    }

    private List<ReceiverResource> claimedReceiverResourcesFrom(List<ReceiverResource> receiverResources) {
        return claimRepository.findAllByReceiverResourceIsIn(receiverResources).stream()
            .map(Claim::getReceiverResource)
            .collect(Collectors.toList());
    }

    private List<SupplierResource> claimedSupplierResourcesFrom(List<SupplierResource> supplierResources) {
        return claimRepository.findAllBySupplierResourceIsIn(supplierResources).stream()
            .map(Claim::getSupplierResource)
            .collect(Collectors.toList());
    }

    private List<ReceiverResource> unclaimedReceiverResourcesFrom(List<ReceiverResource> receiverResources, List<ReceiverResource> claimedReceiverResources) {
        return receiverResources.stream().filter(receiverResource -> !claimedReceiverResources.contains(receiverResource)).collect(Collectors.toList());
    }

    private List<SupplierResource> unclaimedSupplierResourcesFrom(List<SupplierResource> supplierResources, List<SupplierResource> claimedSupplierResources) {
        return supplierResources.stream().filter(supplierResource -> !claimedSupplierResources.contains(supplierResource)).collect(Collectors.toList());
    }

    private ResourceMatchMakingInput resourceMatchMakingInputFrom(ResourceType resourceType, List<ReceiverResource> unclaimedReceiverResources, List<SupplierResource> unclaimedSupplierResources) {
        ResourceMatchMakingInput resourceMatchMakingInput = new ResourceMatchMakingInput();
        resourceMatchMakingInput.setResourceType(resourceType);
        resourceMatchMakingInput.setReceiverResources(unclaimedReceiverResources);
        resourceMatchMakingInput.setSupplierResources(unclaimedSupplierResources);
        return resourceMatchMakingInput;
    }
}
