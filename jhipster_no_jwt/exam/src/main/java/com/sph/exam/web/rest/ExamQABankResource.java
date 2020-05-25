package com.sph.exam.web.rest;

import com.sph.exam.domain.ExamQABank;
import com.sph.exam.service.ExamQABankService;
import com.sph.exam.web.rest.errors.BadRequestAlertException;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sph.exam.domain.ExamQABank}.
 */
@RestController
@RequestMapping("/api")
public class ExamQABankResource {

    private final Logger log = LoggerFactory.getLogger(ExamQABankResource.class);

    private static final String ENTITY_NAME = "examExamQaBank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamQABankService examQABankService;

    public ExamQABankResource(ExamQABankService examQABankService) {
        this.examQABankService = examQABankService;
    }

    /**
     * {@code POST  /exam-qa-banks} : Create a new examQABank.
     *
     * @param examQABank the examQABank to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examQABank, or with status {@code 400 (Bad Request)} if the examQABank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-qa-banks")
    public ResponseEntity<ExamQABank> createExamQABank(@RequestBody ExamQABank examQABank) throws URISyntaxException {
        log.debug("REST request to save ExamQABank : {}", examQABank);
        if (examQABank.getId() != null) {
            throw new BadRequestAlertException("A new examQABank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamQABank result = examQABankService.save(examQABank);
        return ResponseEntity.created(new URI("/api/exam-qa-banks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-qa-banks} : Updates an existing examQABank.
     *
     * @param examQABank the examQABank to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examQABank,
     * or with status {@code 400 (Bad Request)} if the examQABank is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examQABank couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-qa-banks")
    public ResponseEntity<ExamQABank> updateExamQABank(@RequestBody ExamQABank examQABank) throws URISyntaxException {
        log.debug("REST request to update ExamQABank : {}", examQABank);
        if (examQABank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExamQABank result = examQABankService.save(examQABank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examQABank.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exam-qa-banks} : get all the examQABanks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examQABanks in body.
     */
    @GetMapping("/exam-qa-banks")
    public ResponseEntity<List<ExamQABank>> getAllExamQABanks(Pageable pageable) {
        log.debug("REST request to get a page of ExamQABanks");
        Page<ExamQABank> page = examQABankService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-qa-banks/:id} : get the "id" examQABank.
     *
     * @param id the id of the examQABank to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examQABank, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-qa-banks/{id}")
    public ResponseEntity<ExamQABank> getExamQABank(@PathVariable Long id) {
        log.debug("REST request to get ExamQABank : {}", id);
        Optional<ExamQABank> examQABank = examQABankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examQABank);
    }

    /**
     * {@code DELETE  /exam-qa-banks/:id} : delete the "id" examQABank.
     *
     * @param id the id of the examQABank to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-qa-banks/{id}")
    public ResponseEntity<Void> deleteExamQABank(@PathVariable Long id) {
        log.debug("REST request to delete ExamQABank : {}", id);

        examQABankService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
