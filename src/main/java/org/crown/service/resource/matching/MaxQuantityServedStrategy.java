package org.crown.service.resource.matching;

import org.crown.domain.ReceiverResource;
import org.crown.domain.ResourceMatch;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;
import org.crown.service.MaxHeap;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaxQuantityServedStrategy {

    private static final int ZERO_QUANTITY = 0;

    public List<ResourceMatch> generateMatches(ResourceMatchMakingInput resourceMatchMakingInput) {

        List<ResourceMatch> resourceMatches = new ArrayList<>();

        List<ReceiverResource> sortedReceiverResources = resourceMatchMakingInput.getReceiverResources().stream()
            .sorted(Comparator.comparing(ReceiverResource::getQuantity).reversed())
            .collect(Collectors.toList());

        MaxHeap<SupplierResource> supplierResourceMaxHeap = new MaxHeap<>(
            resourceMatchMakingInput.getSupplierResources(),
            Comparator.comparing(SupplierResource::getQuantity)
        );

        sortedReceiverResources.forEach(receiverResource -> {
            Integer receiverQuantity = receiverResource.getQuantity();
            while (receiverQuantity > 0 && supplierResourceMaxHeap.isNotEmpty()) {
                SupplierResource supplier = supplierResourceMaxHeap.extractMax();
                if (receiverQuantity < supplier.getQuantity()) {
                    resourceMatches.add(resourceMatchOf(resourceMatchMakingInput.getResourceType(), receiverResource, supplier, receiverQuantity));
                    supplier.setQuantity(supplier.getQuantity() - receiverQuantity);
                    supplierResourceMaxHeap.insert(supplier);
                    receiverQuantity = ZERO_QUANTITY;
                }
                else {
                    resourceMatches.add(
                        resourceMatchOf(
                            resourceMatchMakingInput.getResourceType(),
                            receiverResource,
                            supplier,
                            supplier.getQuantity()
                        )
                    );
                    receiverQuantity -= supplier.getQuantity();
                    supplier.setQuantity(ZERO_QUANTITY);
                }
            }
        });

        return resourceMatches;
    }

    private ResourceMatch resourceMatchOf(
        ResourceType resourceType, ReceiverResource receiverResource,
        SupplierResource supplierResource,
        Integer quantity
    ) {
        ResourceMatch resourceMatch = new ResourceMatch();
        resourceMatch.setResourceType(resourceType);
        resourceMatch.setQuantity(quantity);
        resourceMatch.setReceiverResource(receiverResource);
        resourceMatch.setSupplierResource(supplierResource);
        resourceMatch.setTimestamp(LocalDateTime.now());
        return resourceMatch;
    }

}
