package org.crown.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.crown.domain.ReceiverResource;
import org.crown.repository.ReceiverResourceRepository;
import org.crown.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.crown.domain.ReceiverResource}.
 */
@RestController
@RequestMapping("/api")
public class ReceiverResourceResource {

    private final Logger log = LoggerFactory.getLogger(ReceiverResourceResource.class);

    private static final String ENTITY_NAME = "receiverResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiverResourceRepository receiverResourceRepository;


    public ReceiverResourceResource(ReceiverResourceRepository receiverResourceRepository) {
        this.receiverResourceRepository = receiverResourceRepository;
    }

    /**
     * {@code POST  /receiver-resources} : Create a new receiverResource.
     *
     * @param receiverResource the receiverResource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiverResource, or with status {@code 400 (Bad Request)} if the receiverResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/receiver-resources")
    public ResponseEntity<ReceiverResource> createReceiverResource(@Valid @RequestBody ReceiverResource receiverResource) throws URISyntaxException {
        log.debug("REST request to save ReceiverResource : {}", receiverResource);
        if (receiverResource.getId() != null) {
            throw new BadRequestAlertException("A new receiverResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceiverResource result = receiverResourceRepository.save(receiverResource);
        
        return ResponseEntity.created(new URI("/api/receiver-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /receiver-resources} : Updates an existing receiverResource.
     *
     * @param receiverResource the receiverResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiverResource,
     * or with status {@code 400 (Bad Request)} if the receiverResource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receiverResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/receiver-resources")
    public ResponseEntity<ReceiverResource> updateReceiverResource(@Valid @RequestBody ReceiverResource receiverResource) throws URISyntaxException {
        log.debug("REST request to update ReceiverResource : {}", receiverResource);
        if (receiverResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReceiverResource result = receiverResourceRepository.save(receiverResource);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receiverResource.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /receiver-resources} : get all the receiverResources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receiverResources in body.
     */
    @GetMapping("/receiver-resources")
    public ResponseEntity<List<ReceiverResource>> getAllReceiverResources(Pageable pageable) {
        log.debug("REST request to get a page of ReceiverResources");
        Page<ReceiverResource> page = receiverResourceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /receiver-resources/:id} : get the "id" receiverResource.
     *
     * @param id the id of the receiverResource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receiverResource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/receiver-resources/{id}")
    public ResponseEntity<ReceiverResource> getReceiverResource(@PathVariable String id) {
        log.debug("REST request to get ReceiverResource : {}", id);
        Optional<ReceiverResource> receiverResource = receiverResourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(receiverResource);
    }

    /**
     * {@code DELETE  /receiver-resources/:id} : delete the "id" receiverResource.
     *
     * @param id the id of the receiverResource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/receiver-resources/{id}")
    public ResponseEntity<Void> deleteReceiverResource(@PathVariable String id) {
        log.debug("REST request to delete ReceiverResource : {}", id);
        receiverResourceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/receiver-resources?x=:x&y=:y&distance=:distance} : geo search - search for the receiverResource corresponding
     * to the query.
     *
     * @param x
     * @param y
     * @param distance - distance in meters
     * @return the result of the search.
     */
    @GetMapping("/_search/receiver-resources")
    public ResponseEntity<List<ReceiverResource>> searchReceiverResources(@RequestParam double x, double y, 
    		double distance, Pageable pageable, String units) {
        log.debug("REST request to search for a page of ReceiverResources for longitude: {} latitude:{} distance: {}", x, y, distance);
        Point point = new Point(x, y);
		Distance dist = new Distance(distance);
        Page<ReceiverResource> page = receiverResourceRepository.findByPositionNear(point, dist, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
