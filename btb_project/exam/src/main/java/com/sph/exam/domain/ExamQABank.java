package com.sph.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A ExamQABank.
 */
@Entity
@Table(name = "exam_qa_bank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExamQABank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "id_qa_bank", length = 36)
    private UUID idQABank;

    @ManyToMany(mappedBy = "examQABanks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Exam> exams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdQABank() {
        return idQABank;
    }

    public ExamQABank idQABank(UUID idQABank) {
        this.idQABank = idQABank;
        return this;
    }

    public void setIdQABank(UUID idQABank) {
        this.idQABank = idQABank;
    }

    public Set<Exam> getExams() {
        return exams;
    }

    public ExamQABank exams(Set<Exam> exams) {
        this.exams = exams;
        return this;
    }

    public ExamQABank addExam(Exam exam) {
        this.exams.add(exam);
        exam.getExamQABanks().add(this);
        return this;
    }

    public ExamQABank removeExam(Exam exam) {
        this.exams.remove(exam);
        exam.getExamQABanks().remove(this);
        return this;
    }

    public void setExams(Set<Exam> exams) {
        this.exams = exams;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamQABank)) {
            return false;
        }
        return id != null && id.equals(((ExamQABank) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamQABank{" +
            "id=" + getId() +
            ", idQABank='" + getIdQABank() + "'" +
            "}";
    }
}
