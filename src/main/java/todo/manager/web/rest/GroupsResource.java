package todo.manager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import todo.manager.repository.GroupsRepository;
import todo.manager.security.AuthoritiesConstants;
import todo.manager.service.GroupsService;
import todo.manager.service.dto.GroupsDTO;
import todo.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link todo.manager.domain.Groups}.
 */
@RestController
@RequestMapping("/api")
public class GroupsResource {

    private final Logger log = LoggerFactory.getLogger(GroupsResource.class);

    private static final String ENTITY_NAME = "groups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupsService groupsService;

    private final GroupsRepository groupsRepository;

    public GroupsResource(GroupsService groupsService, GroupsRepository groupsRepository) {
        this.groupsService = groupsService;
        this.groupsRepository = groupsRepository;
    }

    /**
     * {@code POST  /groups} : Create a new groups.
     *
     * @param groupsDTO the groupsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupsDTO, or with status {@code 400 (Bad Request)} if the groups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/groups")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<GroupsDTO> createGroups(@RequestBody GroupsDTO groupsDTO) throws URISyntaxException {
        log.debug("REST request to save Groups : {}", groupsDTO);
        if (groupsDTO.getId() != null) {
            throw new BadRequestAlertException("A new groups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GroupsDTO result = groupsService.save(groupsDTO);
        return ResponseEntity
            .created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groups/:id} : Updates an existing groups.
     *
     * @param id the id of the groupsDTO to save.
     * @param groupsDTO the groupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupsDTO,
     * or with status {@code 400 (Bad Request)} if the groupsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/groups/{id}")
    public ResponseEntity<GroupsDTO> updateGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GroupsDTO groupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Groups : {}, {}", id, groupsDTO);
        if (groupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GroupsDTO result = groupsService.update(groupsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /groups/:id} : Partial updates given fields of an existing groups, field will ignore if it is null
     *
     * @param id the id of the groupsDTO to save.
     * @param groupsDTO the groupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupsDTO,
     * or with status {@code 400 (Bad Request)} if the groupsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GroupsDTO> partialUpdateGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GroupsDTO groupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Groups partially : {}, {}", id, groupsDTO);
        if (groupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GroupsDTO> result = groupsService.partialUpdate(groupsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("/groups")
    public List<GroupsDTO> getAllGroups() {
        log.debug("REST request to get all Groups");
        return groupsService.findAll();
    }

    /**
     * {@code GET  /groups/:id} : get the "id" groups.
     *
     * @param id the id of the groupsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupsDTO> getGroups(@PathVariable Long id) {
        log.debug("REST request to get Groups : {}", id);
        Optional<GroupsDTO> groupsDTO = groupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupsDTO);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" groups.
     *
     * @param id the id of the groupsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroups(@PathVariable Long id) {
        log.debug("REST request to delete Groups : {}", id);
        groupsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
