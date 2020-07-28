package org.crown.service;

import org.crown.domain.Claim;
import org.crown.domain.ReceiverResource;
import org.crown.domain.ResourceType;
import org.crown.domain.SupplierResource;

import java.util.List;

public class ResourceMatchMakingInput {

    private ResourceType resourceType;

    private List<SupplierResource> supplierResources;

    private List<ReceiverResource> receiverResources;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public List<SupplierResource> getSupplierResources() {
        return supplierResources;
    }

    public void setSupplierResources(List<SupplierResource> supplierResources) {
        this.supplierResources = supplierResources;
    }

    public List<ReceiverResource> getReceiverResources() {
        return receiverResources;
    }

    public void setReceiverResources(List<ReceiverResource> receiverResources) {
        this.receiverResources = receiverResources;
    }

}
