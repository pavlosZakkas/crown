package org.crown.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Resource matches are used to match resource requests from receivers to
 * the corresponding resource suppliers.
 */
@Document(collection = "resource_match")
public class ResourceMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @DBRef
    @Field("resourceType")
    private ResourceType resourceType;

    @DBRef
    @Field("supplierResource")
    private SupplierResource supplierResource;

    @DBRef
    @Field("receiverResource")
    private ReceiverResource receiverResource;

    @NotNull
    @Field("quantity")
    private Integer quantity;

    @Field("timestamp")
    private LocalDateTime timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceMatch resourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public SupplierResource getSupplierResource() {
        return supplierResource;
    }

    public void setSupplierResource(SupplierResource supplierResource) {
        this.supplierResource = supplierResource;
    }

    public ResourceMatch supplierResource(SupplierResource supplierResource) {
        this.supplierResource = supplierResource;
        return this;
    }

    public ReceiverResource getReceiverResource() {
        return receiverResource;
    }

    public void setReceiverResource(ReceiverResource receiverResource) {
        this.receiverResource = receiverResource;
    }

    public ResourceMatch receiverResource(ReceiverResource receiverResource) {
        this.receiverResource = receiverResource;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ResourceMatch quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceMatch)) {
            return false;
        }
        return id != null && id.equals(((ResourceMatch) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ResourceMatch{" + "id=" + getId() + ", quantity=" + getQuantity() + "}";
    }
}
