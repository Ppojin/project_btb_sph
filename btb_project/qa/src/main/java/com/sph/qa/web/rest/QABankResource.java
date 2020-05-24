package com.sph.qa.web.rest;

import com.sph.qa.domain.QABank;
import com.sph.qa.service.QABankService;
import com.sph.qa.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sph.qa.domain.QABank}.
 */
@RestController
@RequestMapping("/api")
public class QABankResource {

    private final Logger log = LoggerFactory.getLogger(QABankResource.class);

    private static final String ENTITY_NAME = "qaQaBank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QABankService qABankService;

    public QABankResource(QABankService qABankService) {
        this.qABankService = qABankService;
    }

    /**
     * {@code POST  /qa-banks} : Create a new qABank.
     *
     * @param qABank the qABank to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qABank, or with status {@code 400 (Bad Request)} if the qABank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/qa-banks")
    public ResponseEntity<QABank> createQABank(@Valid @RequestBody QABank qABank) throws URISyntaxException {
        log.debug("REST request to save QABank : {}", qABank);
        if (qABank.getId() != null) {
            throw new BadRequestAlertException("A new qABank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QABank result = qABankService.save(qABank);
        return ResponseEntity.created(new URI("/api/qa-banks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qa-banks} : Updates an existing qABank.
     *
     * @param qABank the qABank to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qABank,
     * or with status {@code 400 (Bad Request)} if the qABank is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qABank couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/qa-banks")
    public ResponseEntity<QABank> updateQABank(@Valid @RequestBody QABank qABank) throws URISyntaxException {
        log.debug("REST request to update QABank : {}", qABank);
        if (qABank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QABank result = qABankService.save(qABank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qABank.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /qa-banks} : get all the qABanks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qABanks in body.
     */
    @GetMapping("/qa-banks")
    public ResponseEntity<List<QABank>> getAllQABanks(Pageable pageable) {
        log.debug("REST request to get a page of QABanks");
        Page<QABank> page = qABankService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qa-banks/:id} : get the "id" qABank.
     *
     * @param id the id of the qABank to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qABank, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/qa-banks/{id}")
    public ResponseEntity<QABank> getQABank(@PathVariable Long id) {
        log.debug("REST request to get QABank : {}", id);
        Optional<QABank> qABank = qABankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qABank);
    }

    /**
     * {@code DELETE  /qa-banks/:id} : delete the "id" qABank.
     *
     * @param id the id of the qABank to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/qa-banks/{id}")
    public ResponseEntity<Void> deleteQABank(@PathVariable Long id) {
        log.debug("REST request to delete QABank : {}", id);

        qABankService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
