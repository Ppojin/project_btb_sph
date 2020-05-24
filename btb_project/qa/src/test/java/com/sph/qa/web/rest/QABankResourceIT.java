package com.sph.qa.web.rest;

import com.sph.qa.QaApp;
import com.sph.qa.domain.QABank;
import com.sph.qa.repository.QABankRepository;
import com.sph.qa.service.QABankService;

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

import com.sph.qa.domain.enumeration.Category;
/**
 * Integration tests for the {@link QABankResource} REST controller.
 */
@SpringBootTest(classes = QaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class QABankResourceIT {

    private static final UUID DEFAULT_ID_QA_BANK = UUID.randomUUID();
    private static final UUID UPDATED_ID_QA_BANK = UUID.randomUUID();

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENTS = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS = "BBBBBBBBBB";

    private static final String DEFAULT_GIT_URL = "AAAAAAAAAA";
    private static final String UPDATED_GIT_URL = "BBBBBBBBBB";

    private static final Category DEFAULT_CATEGORY = Category.SERVICE;
    private static final Category UPDATED_CATEGORY = Category.CONTROLLER;

    @Autowired
    private QABankRepository qABankRepository;

    @Autowired
    private QABankService qABankService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQABankMockMvc;

    private QABank qABank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QABank createEntity(EntityManager em) {
        QABank qABank = new QABank()
            .idQABank(DEFAULT_ID_QA_BANK)
            .title(DEFAULT_TITLE)
            .contents(DEFAULT_CONTENTS)
            .gitUrl(DEFAULT_GIT_URL)
            .category(DEFAULT_CATEGORY);
        return qABank;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QABank createUpdatedEntity(EntityManager em) {
        QABank qABank = new QABank()
            .idQABank(UPDATED_ID_QA_BANK)
            .title(UPDATED_TITLE)
            .contents(UPDATED_CONTENTS)
            .gitUrl(UPDATED_GIT_URL)
            .category(UPDATED_CATEGORY);
        return qABank;
    }

    @BeforeEach
    public void initTest() {
        qABank = createEntity(em);
    }

    @Test
    @Transactional
    public void createQABank() throws Exception {
        int databaseSizeBeforeCreate = qABankRepository.findAll().size();
        // Create the QABank
        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isCreated());

        // Validate the QABank in the database
        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeCreate + 1);
        QABank testQABank = qABankList.get(qABankList.size() - 1);
        assertThat(testQABank.getIdQABank()).isEqualTo(DEFAULT_ID_QA_BANK);
        assertThat(testQABank.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQABank.getContents()).isEqualTo(DEFAULT_CONTENTS);
        assertThat(testQABank.getGitUrl()).isEqualTo(DEFAULT_GIT_URL);
        assertThat(testQABank.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    public void createQABankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = qABankRepository.findAll().size();

        // Create the QABank with an existing ID
        qABank.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        // Validate the QABank in the database
        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIdQABankIsRequired() throws Exception {
        int databaseSizeBeforeTest = qABankRepository.findAll().size();
        // set the field null
        qABank.setIdQABank(null);

        // Create the QABank, which fails.


        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = qABankRepository.findAll().size();
        // set the field null
        qABank.setTitle(null);

        // Create the QABank, which fails.


        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = qABankRepository.findAll().size();
        // set the field null
        qABank.setContents(null);

        // Create the QABank, which fails.


        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGitUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = qABankRepository.findAll().size();
        // set the field null
        qABank.setGitUrl(null);

        // Create the QABank, which fails.


        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = qABankRepository.findAll().size();
        // set the field null
        qABank.setCategory(null);

        // Create the QABank, which fails.


        restQABankMockMvc.perform(post("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQABanks() throws Exception {
        // Initialize the database
        qABankRepository.saveAndFlush(qABank);

        // Get all the qABankList
        restQABankMockMvc.perform(get("/api/qa-banks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qABank.getId().intValue())))
            .andExpect(jsonPath("$.[*].idQABank").value(hasItem(DEFAULT_ID_QA_BANK.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS)))
            .andExpect(jsonPath("$.[*].gitUrl").value(hasItem(DEFAULT_GIT_URL)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }
    
    @Test
    @Transactional
    public void getQABank() throws Exception {
        // Initialize the database
        qABankRepository.saveAndFlush(qABank);

        // Get the qABank
        restQABankMockMvc.perform(get("/api/qa-banks/{id}", qABank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qABank.getId().intValue()))
            .andExpect(jsonPath("$.idQABank").value(DEFAULT_ID_QA_BANK.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.contents").value(DEFAULT_CONTENTS))
            .andExpect(jsonPath("$.gitUrl").value(DEFAULT_GIT_URL))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingQABank() throws Exception {
        // Get the qABank
        restQABankMockMvc.perform(get("/api/qa-banks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQABank() throws Exception {
        // Initialize the database
        qABankService.save(qABank);

        int databaseSizeBeforeUpdate = qABankRepository.findAll().size();

        // Update the qABank
        QABank updatedQABank = qABankRepository.findById(qABank.getId()).get();
        // Disconnect from session so that the updates on updatedQABank are not directly saved in db
        em.detach(updatedQABank);
        updatedQABank
            .idQABank(UPDATED_ID_QA_BANK)
            .title(UPDATED_TITLE)
            .contents(UPDATED_CONTENTS)
            .gitUrl(UPDATED_GIT_URL)
            .category(UPDATED_CATEGORY);

        restQABankMockMvc.perform(put("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQABank)))
            .andExpect(status().isOk());

        // Validate the QABank in the database
        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeUpdate);
        QABank testQABank = qABankList.get(qABankList.size() - 1);
        assertThat(testQABank.getIdQABank()).isEqualTo(UPDATED_ID_QA_BANK);
        assertThat(testQABank.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQABank.getContents()).isEqualTo(UPDATED_CONTENTS);
        assertThat(testQABank.getGitUrl()).isEqualTo(UPDATED_GIT_URL);
        assertThat(testQABank.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void updateNonExistingQABank() throws Exception {
        int databaseSizeBeforeUpdate = qABankRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQABankMockMvc.perform(put("/api/qa-banks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(qABank)))
            .andExpect(status().isBadRequest());

        // Validate the QABank in the database
        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQABank() throws Exception {
        // Initialize the database
        qABankService.save(qABank);

        int databaseSizeBeforeDelete = qABankRepository.findAll().size();

        // Delete the qABank
        restQABankMockMvc.perform(delete("/api/qa-banks/{id}", qABank.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QABank> qABankList = qABankRepository.findAll();
        assertThat(qABankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
