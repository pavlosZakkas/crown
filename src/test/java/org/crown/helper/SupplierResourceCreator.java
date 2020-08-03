package org.crown.helper;

import org.crown.domain.ReceiverSupplier;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SupplierResourceCreator {

    public static List<SupplierResource> createSupplierResourcesWithQuantities(
        ArrayList<Integer> quantities,
        ResourceType resourceType
    ) {
        List<SupplierResource> supplierResources = new ArrayList<>();
        IntStream.range(0, quantities.size()).forEach(index -> {
            SupplierResource supplierResource = aSupplierResourceWith(
                quantities.get(index),
                aSupplierWithOrgName("org" + index),
                resourceType
            );
            supplierResources.add(supplierResource);
        });
        return supplierResources;
    }

    private static SupplierResource aSupplierResourceWith(
        Integer quantity,
        ReceiverSupplier receiverSupplier,
        ResourceType resourceType
    ) {
        return new SupplierResource()
            .resourceType(resourceType)
            .quantity(quantity)
            .supplier(receiverSupplier);
    }

    private static ReceiverSupplier aSupplierWithOrgName(String orgName) {
        ReceiverSupplier receiverSupplier = new ReceiverSupplier().isSupplier(true).isReceiver(false).orgName(orgName);
        receiverSupplier.setId(orgName);
        return receiverSupplier;
    }

}
