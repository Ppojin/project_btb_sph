package com.sph.exam.web.rest;

import com.sph.exam.ExamApp;
import com.sph.exam.domain.ExamStudent;
import com.sph.exam.repository.ExamStudentRepository;
import com.sph.exam.service.ExamStudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExamStudentResource} REST controller.
 */
@SpringBootTest(classes = ExamApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExamStudentResourceIT {

    private static final UUID DEFAULT_ID_CUSTOMER = UUID.randomUUID();
    private static final UUID UPDATED_ID_CUSTOMER = UUID.randomUUID();

    @Autowired
    private ExamStudentRepository examStudentRepository;

    @Autowired
    private ExamStudentService examStudentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamStudentMockMvc;

    private ExamStudent examStudent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamStudent createEntity(EntityManager em) {
        ExamStudent examStudent = new ExamStudent()
            .idCustomer(DEFAULT_ID_CUSTOMER);
        return examStudent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamStudent createUpdatedEntity(EntityManager em) {
        ExamStudent examStudent = new ExamStudent()
            .idCustomer(UPDATED_ID_CUSTOMER);
        return examStudent;
    }

    @BeforeEach
    public void initTest() {
        examStudent = createEntity(em);
    }

    @Test
    @Transactional
    public void createExamStudent() throws Exception {
        int databaseSizeBeforeCreate = examStudentRepository.findAll().size();
        // Create the ExamStudent
        restExamStudentMockMvc.perform(post("/api/exam-students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(examStudent)))
            .andExpect(status().isCreated());

        // Validate the ExamStudent in the database
        List<ExamStudent> examStudentList = examStudentRepository.findAll();
        assertThat(examStudentList).hasSize(databaseSizeBeforeCreate + 1);
        ExamStudent testExamStudent = examStudentList.get(examStudentList.size() - 1);
        assertThat(testExamStudent.getIdCustomer()).isEqualTo(DEFAULT_ID_CUSTOMER);
    }

    @Test
    @Transactional
    public void createExamStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examStudentRepository.findAll().size();

        // Create the ExamStudent with an existing ID
        examStudent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamStudentMockMvc.perform(post("/api/exam-students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(examStudent)))
            .andExpect(status().isBadRequest());

        // Validate the ExamStudent in the database
        List<ExamStudent> examStudentList = examStudentRepository.findAll();
        assertThat(examStudentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExamStudents() throws Exception {
        // Initialize the database
        examStudentRepository.saveAndFlush(examStudent);

        // Get all the examStudentList
        restExamStudentMockMvc.perform(get("/api/exam-students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examStudent.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCustomer").value(hasItem(DEFAULT_ID_CUSTOMER.toString())));
    }
    
    @Test
    @Transactional
    public void getExamStudent() throws Exception {
        // Initialize the database
        examStudentRepository.saveAndFlush(examStudent);

        // Get the examStudent
        restExamStudentMockMvc.perform(get("/api/exam-students/{id}", examStudent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examStudent.getId().intValue()))
            .andExpect(jsonPath("$.idCustomer").value(DEFAULT_ID_CUSTOMER.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingExamStudent() throws Exception {
        // Get the examStudent
        restExamStudentMockMvc.perform(get("/api/exam-students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExamStudent() throws Exception {
        // Initialize the database
        examStudentService.save(examStudent);

        int databaseSizeBeforeUpdate = examStudentRepository.findAll().size();

        // Update the examStudent
        ExamStudent updatedExamStudent = examStudentRepository.findById(examStudent.getId()).get();
        // Disconnect from session so that the updates on updatedExamStudent are not directly saved in db
        em.detach(updatedExamStudent);
        updatedExamStudent
            .idCustomer(UPDATED_ID_CUSTOMER);

        restExamStudentMockMvc.perform(put("/api/exam-students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExamStudent)))
            .andExpect(status().isOk());

        // Validate the ExamStudent in the database
        List<ExamStudent> examStudentList = examStudentRepository.findAll();
        assertThat(examStudentList).hasSize(databaseSizeBeforeUpdate);
        ExamStudent testExamStudent = examStudentList.get(examStudentList.size() - 1);
        assertThat(testExamStudent.getIdCustomer()).isEqualTo(UPDATED_ID_CUSTOMER);
    }

    @Test
    @Transactional
    public void updateNonExistingExamStudent() throws Exception {
        int databaseSizeBeforeUpdate = examStudentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamStudentMockMvc.perform(put("/api/exam-students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(examStudent)))
            .andExpect(status().isBadRequest());

        // Validate the ExamStudent in the database
        List<ExamStudent> examStudentList = examStudentRepository.findAll();
        assertThat(examStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExamStudent() throws Exception {
        // Initialize the database
        examStudentService.save(examStudent);

        int databaseSizeBeforeDelete = examStudentRepository.findAll().size();

        // Delete the examStudent
        restExamStudentMockMvc.perform(delete("/api/exam-students/{id}", examStudent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamStudent> examStudentList = examStudentRepository.findAll();
        assertThat(examStudentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
