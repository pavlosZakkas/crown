package org.crown.helper;

import org.crown.domain.ReceiverResource;
import org.crown.domain.ReceiverSupplier;
import org.crown.domain.ResourceType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ReceiverResourceCreator {

    private static final int DEFAULT_DAILY_USE = 100;

    public static List<ReceiverResource> createReceiverResourcesWithQuantities(
        ArrayList<Integer> quantities,
        ResourceType resourceType
    ) {
        List<ReceiverResource> receiverResources = new ArrayList<>();
        IntStream.range(0, quantities.size()).forEach(index -> {
            ReceiverResource receiverResource = aReceiverResourceWith(
                "name" + index,
                quantities.get(index),
                aReceiverWithOrgName("org" + index),
                resourceType
            );
            receiverResources.add(receiverResource);
        });
        return receiverResources;
    }

    private static ReceiverResource aReceiverResourceWith(
        String name,
        Integer quantity,
        ReceiverSupplier receiverSupplier,
        ResourceType resourceType
    ) {
        return new ReceiverResource()
            .name(name)
            .resourceType(resourceType)
            .quantity(quantity)
            .dailyUse(DEFAULT_DAILY_USE)
            .postedDate(LocalDate.now())
            .receiver(receiverSupplier);
    }

    private static ReceiverSupplier aReceiverWithOrgName(String orgName) {
        ReceiverSupplier receiverSupplier = new ReceiverSupplier().isSupplier(false).isReceiver(true).orgName(orgName);
        receiverSupplier.setId(orgName);
        return receiverSupplier;
    }
}
