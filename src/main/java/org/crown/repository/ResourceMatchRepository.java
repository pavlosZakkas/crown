package org.crown.repository;

import org.crown.domain.ResourceMatch;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the {@link ResourceMatch} entity.
 */
public interface ResourceMatchRepository extends MongoRepository<ResourceMatch, String> {

}
