package org.crown.service.resource.matching;

import org.crown.domain.ReceiverResource;
import org.crown.domain.ResourceMatch;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;
import org.crown.helper.ReceiverResourceCreator;
import org.crown.helper.SupplierResourceCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class MaxQuantityServedStrategyTest {

    private static final ResourceType FACE_SHIELD = new ResourceType().name("Face shield");

    @Autowired
    private MaxQuantityServedStrategy maxQuantityServedStrategy = new MaxQuantityServedStrategy();

    @Test
    void gererateMatchedReturnsNoMatchesWhenSuppliersDoNotExist() {
        List<ReceiverResource> receiverResources = ReceiverResourceCreator.createReceiverResourcesWithQuantities(
            new ArrayList<>(asList(300, 50, 100)),
            FACE_SHIELD
        );
        List<SupplierResource> supplierResources = SupplierResourceCreator.createSupplierResourcesWithQuantities(
            new ArrayList<>(emptyList()),
            FACE_SHIELD
        );

        ResourceMatchMakingInput resourceMatchMakingInput = inputFrom(receiverResources, supplierResources);
        List<ResourceMatch> resourceMatches = maxQuantityServedStrategy.generateMatches(resourceMatchMakingInput);

        assertThat(resourceMatches.size()).isEqualTo(0);
    }

    @Test
    void generateMatchesWhenRequestedAndSuppliedQuantityMatchOneByOne() {
        List<ReceiverResource> receiverResources = ReceiverResourceCreator.createReceiverResourcesWithQuantities(
            new ArrayList<>(asList(300, 50, 100)),
            FACE_SHIELD
        );
        List<SupplierResource> supplierResources = SupplierResourceCreator.createSupplierResourcesWithQuantities(
            new ArrayList<>(asList(300, 50, 100)),
            FACE_SHIELD
        );
        ResourceMatchMakingInput resourceMatchMakingInput = inputFrom(receiverResources, supplierResources);

        List<ResourceMatch> resourceMatches = maxQuantityServedStrategy.generateMatches(resourceMatchMakingInput);

        assertThat(resourceMatches.size()).isEqualTo(3);
        assertThat(resourceMatches.get(0).getSupplierResource()).isEqualTo(supplierResources.get(0));
        assertThat(resourceMatches.get(0).getReceiverResource()).isEqualTo(receiverResources.get(0));
        assertThat(resourceMatches.get(0).getQuantity()).isEqualTo(300);
        assertThat(resourceMatches.get(1).getSupplierResource()).isEqualTo(supplierResources.get(2));
        assertThat(resourceMatches.get(1).getReceiverResource()).isEqualTo(receiverResources.get(2));
        assertThat(resourceMatches.get(1).getQuantity()).isEqualTo(100);
        assertThat(resourceMatches.get(2).getSupplierResource()).isEqualTo(supplierResources.get(1));
        assertThat(resourceMatches.get(2).getReceiverResource()).isEqualTo(receiverResources.get(1));
        assertThat(resourceMatches.get(2).getQuantity()).isEqualTo(50);
    }

    @Test
    void generateMatchesWhenTotalQuantityCannotBeCovered() {
        List<ReceiverResource> receiverResources = ReceiverResourceCreator.createReceiverResourcesWithQuantities(
            new ArrayList<>(asList(500, 50, 100)),
            FACE_SHIELD
        );
        List<SupplierResource> supplierResources = SupplierResourceCreator.createSupplierResourcesWithQuantities(
            new ArrayList<>(asList(300, 50, 100)),
            FACE_SHIELD
        );
        ResourceMatchMakingInput resourceMatchMakingInput = inputFrom(receiverResources, supplierResources);

        List<ResourceMatch> resourceMatches = maxQuantityServedStrategy.generateMatches(resourceMatchMakingInput);

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


    @Test
    void generateMatchesWhenTotalQuantityIsLessThanTheQuantitySupplied() {
        List<ReceiverResource> receiverResources = ReceiverResourceCreator.createReceiverResourcesWithQuantities(
            new ArrayList<>(asList(300, 50, 100)),
            FACE_SHIELD
        );
        List<SupplierResource> supplierResources = SupplierResourceCreator.createSupplierResourcesWithQuantities(
            new ArrayList<>(asList(200, 40 , 100, 120, 60)),
            FACE_SHIELD
        );
        ResourceMatchMakingInput resourceMatchMakingInput = inputFrom(receiverResources, supplierResources);

        List<ResourceMatch> resourceMatches = maxQuantityServedStrategy.generateMatches(resourceMatchMakingInput);

        assertThat(resourceMatches.size()).isEqualTo(4);
        assertThat(resourceMatches.get(0).getReceiverResource()).isEqualTo(receiverResources.get(0));
        assertThat(resourceMatches.get(0).getSupplierResource()).isEqualTo(supplierResources.get(0));
        assertThat(resourceMatches.get(0).getQuantity()).isEqualTo(200);
        assertThat(resourceMatches.get(1).getReceiverResource()).isEqualTo(receiverResources.get(0));
        assertThat(resourceMatches.get(1).getSupplierResource()).isEqualTo(supplierResources.get(3));
        assertThat(resourceMatches.get(1).getQuantity()).isEqualTo(100);

        assertThat(resourceMatches.get(2).getReceiverResource()).isEqualTo(receiverResources.get(2));
        assertThat(resourceMatches.get(2).getSupplierResource()).isEqualTo(supplierResources.get(2));
        assertThat(resourceMatches.get(2).getQuantity()).isEqualTo(100);

        assertThat(resourceMatches.get(3).getReceiverResource()).isEqualTo(receiverResources.get(1));
        assertThat(resourceMatches.get(3).getSupplierResource()).isEqualTo(supplierResources.get(4));
        assertThat(resourceMatches.get(3).getQuantity()).isEqualTo(50);
    }


    private ResourceMatchMakingInput inputFrom(List<ReceiverResource> receiverResources, List<SupplierResource> supplierResources) {
        ResourceMatchMakingInput resourceMatchMakingInput = new ResourceMatchMakingInput();
        resourceMatchMakingInput.setResourceType(FACE_SHIELD);
        resourceMatchMakingInput.setSupplierResources(supplierResources);
        resourceMatchMakingInput.setReceiverResources(receiverResources);
        return resourceMatchMakingInput;
    }
}
