package com.sph.exam.web.rest;

import com.sph.exam.domain.ExamStudent;
import com.sph.exam.service.ExamStudentService;
import com.sph.exam.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sph.exam.domain.ExamStudent}.
 */
@RestController
@RequestMapping("/api")
public class ExamStudentResource {

    private final Logger log = LoggerFactory.getLogger(ExamStudentResource.class);

    private static final String ENTITY_NAME = "examExamStudent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamStudentService examStudentService;

    public ExamStudentResource(ExamStudentService examStudentService) {
        this.examStudentService = examStudentService;
    }

    /**
     * {@code POST  /exam-students} : Create a new examStudent.
     *
     * @param examStudent the examStudent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examStudent, or with status {@code 400 (Bad Request)} if the examStudent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-students")
    public ResponseEntity<ExamStudent> createExamStudent(@RequestBody ExamStudent examStudent) throws URISyntaxException {
        log.debug("REST request to save ExamStudent : {}", examStudent);
        if (examStudent.getId() != null) {
            throw new BadRequestAlertException("A new examStudent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamStudent result = examStudentService.save(examStudent);
        return ResponseEntity.created(new URI("/api/exam-students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-students} : Updates an existing examStudent.
     *
     * @param examStudent the examStudent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examStudent,
     * or with status {@code 400 (Bad Request)} if the examStudent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examStudent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-students")
    public ResponseEntity<ExamStudent> updateExamStudent(@RequestBody ExamStudent examStudent) throws URISyntaxException {
        log.debug("REST request to update ExamStudent : {}", examStudent);
        if (examStudent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExamStudent result = examStudentService.save(examStudent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examStudent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exam-students} : get all the examStudents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examStudents in body.
     */
    @GetMapping("/exam-students")
    public List<ExamStudent> getAllExamStudents() {
        log.debug("REST request to get all ExamStudents");
        return examStudentService.findAll();
    }

    /**
     * {@code GET  /exam-students/:id} : get the "id" examStudent.
     *
     * @param id the id of the examStudent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examStudent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-students/{id}")
    public ResponseEntity<ExamStudent> getExamStudent(@PathVariable Long id) {
        log.debug("REST request to get ExamStudent : {}", id);
        Optional<ExamStudent> examStudent = examStudentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examStudent);
    }

    /**
     * {@code DELETE  /exam-students/:id} : delete the "id" examStudent.
     *
     * @param id the id of the examStudent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-students/{id}")
    public ResponseEntity<Void> deleteExamStudent(@PathVariable Long id) {
        log.debug("REST request to delete ExamStudent : {}", id);

        examStudentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
