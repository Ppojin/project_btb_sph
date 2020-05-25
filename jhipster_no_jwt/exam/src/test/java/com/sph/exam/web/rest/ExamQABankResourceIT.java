package com.sph.exam.web.rest;

import com.sph.exam.RedisTestContainerExtension;
import com.sph.exam.ExamApp;
import com.sph.exam.domain.ExamQABank;
import com.sph.exam.repository.ExamQABankRepository;
import com.sph.exam.service.ExamQABankService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
 * Integration tests for the {@link ExamQABankResource} REST controller.
 */
@SpringBootTest(classes = ExamApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class ExamQABankResourceIT {

    private static final UUID DEFAULT_ID_QA_BANK = UUID.randomUUID();
    private static final UUID UPDATED_ID_QA_BANK = UUID.randomUUID();

    @Autowired
    private ExamQABankRepository examQABankRepository;

    @Autowired
    private ExamQABankService examQABankService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamQABankMockMvc;

    private ExamQABank examQABank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamQABank createEntity(EntityManager em) {
        ExamQABank examQABank = new ExamQABank()
            .idQABank(DEFAULT_ID_QA_BANK);
        return examQABank;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamQABank createUpdatedEntity(EntityManager em) {
        ExamQABank examQABank = new ExamQABank()
            .idQABank(UPDATED_ID_QA_BANK);
        return examQABank;
    }

    @BeforeEach
    public void initTest() {
        examQABank = createEntity(em);
    }

    @Test
    @Transactional
    public void createExamQABank() throws Exception {
        int databaseSizeBeforeCreate = examQABankRepository.findAll().size();
        // Create the ExamQABank
        restExamQABankMockMvc.perform(post("/api/exam-qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(examQABank)))
            .andExpect(status().isCreated());

        // Validate the ExamQABank in the database
        List<ExamQABank> examQABankList = examQABankRepository.findAll();
        assertThat(examQABankList).hasSize(databaseSizeBeforeCreate + 1);
        ExamQABank testExamQABank = examQABankList.get(examQABankList.size() - 1);
        assertThat(testExamQABank.getIdQABank()).isEqualTo(DEFAULT_ID_QA_BANK);
    }

    @Test
    @Transactional
    public void createExamQABankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examQABankRepository.findAll().size();

        // Create the ExamQABank with an existing ID
        examQABank.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamQABankMockMvc.perform(post("/api/exam-qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(examQABank)))
            .andExpect(status().isBadRequest());

        // Validate the ExamQABank in the database
        List<ExamQABank> examQABankList = examQABankRepository.findAll();
        assertThat(examQABankList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExamQABanks() throws Exception {
        // Initialize the database
        examQABankRepository.saveAndFlush(examQABank);

        // Get all the examQABankList
        restExamQABankMockMvc.perform(get("/api/exam-qa-banks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examQABank.getId().intValue())))
            .andExpect(jsonPath("$.[*].idQABank").value(hasItem(DEFAULT_ID_QA_BANK.toString())));
    }
    
    @Test
    @Transactional
    public void getExamQABank() throws Exception {
        // Initialize the database
        examQABankRepository.saveAndFlush(examQABank);

        // Get the examQABank
        restExamQABankMockMvc.perform(get("/api/exam-qa-banks/{id}", examQABank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examQABank.getId().intValue()))
            .andExpect(jsonPath("$.idQABank").value(DEFAULT_ID_QA_BANK.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingExamQABank() throws Exception {
        // Get the examQABank
        restExamQABankMockMvc.perform(get("/api/exam-qa-banks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExamQABank() throws Exception {
        // Initialize the database
        examQABankService.save(examQABank);

        int databaseSizeBeforeUpdate = examQABankRepository.findAll().size();

        // Update the examQABank
        ExamQABank updatedExamQABank = examQABankRepository.findById(examQABank.getId()).get();
        // Disconnect from session so that the updates on updatedExamQABank are not directly saved in db
        em.detach(updatedExamQABank);
        updatedExamQABank
            .idQABank(UPDATED_ID_QA_BANK);

        restExamQABankMockMvc.perform(put("/api/exam-qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExamQABank)))
            .andExpect(status().isOk());

        // Validate the ExamQABank in the database
        List<ExamQABank> examQABankList = examQABankRepository.findAll();
        assertThat(examQABankList).hasSize(databaseSizeBeforeUpdate);
        ExamQABank testExamQABank = examQABankList.get(examQABankList.size() - 1);
        assertThat(testExamQABank.getIdQABank()).isEqualTo(UPDATED_ID_QA_BANK);
    }

    @Test
    @Transactional
    public void updateNonExistingExamQABank() throws Exception {
        int databaseSizeBeforeUpdate = examQABankRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamQABankMockMvc.perform(put("/api/exam-qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(examQABank)))
            .andExpect(status().isBadRequest());

        // Validate the ExamQABank in the database
        List<ExamQABank> examQABankList = examQABankRepository.findAll();
        assertThat(examQABankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExamQABank() throws Exception {
        // Initialize the database
        examQABankService.save(examQABank);

        int databaseSizeBeforeDelete = examQABankRepository.findAll().size();

        // Delete the examQABank
        restExamQABankMockMvc.perform(delete("/api/exam-qa-banks/{id}", examQABank.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamQABank> examQABankList = examQABankRepository.findAll();
        assertThat(examQABankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
