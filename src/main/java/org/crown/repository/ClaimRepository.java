package org.crown.repository;

import org.crown.domain.Claim;
import org.crown.domain.ReceiverResource;
import org.crown.domain.SupplierResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Claim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClaimRepository extends MongoRepository<Claim, String> {

    /**
     * Finds a list Claims asked for a specific list of supplier resources
     * @param supplierResources
     * @return
     */
    List<Claim> findAllBySupplierResourceIsIn(List<SupplierResource> supplierResources);

    /**
     * Finds a list of Claims asked for a specific list of receiver resources
     * @param receiverResources
     * @return
     */
    List<Claim> findAllByReceiverResourceIsIn(List<ReceiverResource> receiverResources);
}
