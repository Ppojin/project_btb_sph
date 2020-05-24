package com.sph.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * A ExamStudent.
 */
@Entity
@Table(name = "exam_student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExamStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "id_customer", length = 36)
    private UUID idCustomer;

    @ManyToOne
    @JsonIgnoreProperties(value = "examStudents", allowSetters = true)
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdCustomer() {
        return idCustomer;
    }

    public ExamStudent idCustomer(UUID idCustomer) {
        this.idCustomer = idCustomer;
        return this;
    }

    public void setIdCustomer(UUID idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Exam getExam() {
        return exam;
    }

    public ExamStudent exam(Exam exam) {
        this.exam = exam;
        return this;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamStudent)) {
            return false;
        }
        return id != null && id.equals(((ExamStudent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamStudent{" +
            "id=" + getId() +
            ", idCustomer='" + getIdCustomer() + "'" +
            "}";
    }
}
