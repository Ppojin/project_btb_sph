package com.sph.exam.web.rest;

import com.sph.exam.domain.Exam;
import com.sph.exam.service.ExamService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sph.exam.domain.Exam}.
 */
@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "examExam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamService examService;

    public ExamResource(ExamService examService) {
        this.examService = examService;
    }

    /**
     * {@code POST  /exams} : Create a new exam.
     *
     * @param exam the exam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exam, or with status {@code 400 (Bad Request)} if the exam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exams")
    public ResponseEntity<Exam> createExam(@Valid @RequestBody Exam exam) throws URISyntaxException {
        log.debug("REST request to save Exam : {}", exam);
        if (exam.getId() != null) {
            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Exam result = examService.save(exam);
        return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exams} : Updates an existing exam.
     *
     * @param exam the exam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exam,
     * or with status {@code 400 (Bad Request)} if the exam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exams")
    public ResponseEntity<Exam> updateExam(@Valid @RequestBody Exam exam) throws URISyntaxException {
        log.debug("REST request to update Exam : {}", exam);
        if (exam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Exam result = examService.save(exam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exam.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exams} : get all the exams.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exams in body.
     */
    @GetMapping("/exams")
    public ResponseEntity<List<Exam>> getAllExams(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Exams");
        Page<Exam> page;
        if (eagerload) {
            page = examService.findAllWithEagerRelationships(pageable);
        } else {
            page = examService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exams/:id} : get the "id" exam.
     *
     * @param id the id of the exam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exams/{id}")
    public ResponseEntity<Exam> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        Optional<Exam> exam = examService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exam);
    }

    /**
     * {@code DELETE  /exams/:id} : delete the "id" exam.
     *
     * @param id the id of the exam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exams/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);

        examService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
