package com.sph.exam.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.sph.exam.domain.enumeration.Level;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "uuid-char")
    @Column(name = "id_exam", length = 36, nullable = false)
    private UUID idExam;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @OneToMany(mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ExamStudent> examStudents = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "exam_examqabank",
               joinColumns = @JoinColumn(name = "exam_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "examqabank_id", referencedColumnName = "id"))
    private Set<ExamQABank> examQABanks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdExam() {
        return idExam;
    }

    public Exam idExam(UUID idExam) {
        this.idExam = idExam;
        return this;
    }

    public void setIdExam(UUID idExam) {
        this.idExam = idExam;
    }

    public String getTitle() {
        return title;
    }

    public Exam title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Level getLevel() {
        return level;
    }

    public Exam level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Exam startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Exam endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Set<ExamStudent> getExamStudents() {
        return examStudents;
    }

    public Exam examStudents(Set<ExamStudent> examStudents) {
        this.examStudents = examStudents;
        return this;
    }

    public Exam addExamStudent(ExamStudent examStudent) {
        this.examStudents.add(examStudent);
        examStudent.setExam(this);
        return this;
    }

    public Exam removeExamStudent(ExamStudent examStudent) {
        this.examStudents.remove(examStudent);
        examStudent.setExam(null);
        return this;
    }

    public void setExamStudents(Set<ExamStudent> examStudents) {
        this.examStudents = examStudents;
    }

    public Set<ExamQABank> getExamQABanks() {
        return examQABanks;
    }

    public Exam examQABanks(Set<ExamQABank> examQABanks) {
        this.examQABanks = examQABanks;
        return this;
    }

    public Exam addExamQABank(ExamQABank examQABank) {
        this.examQABanks.add(examQABank);
        examQABank.getExams().add(this);
        return this;
    }

    public Exam removeExamQABank(ExamQABank examQABank) {
        this.examQABanks.remove(examQABank);
        examQABank.getExams().remove(this);
        return this;
    }

    public void setExamQABanks(Set<ExamQABank> examQABanks) {
        this.examQABanks = examQABanks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return id != null && id.equals(((Exam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", idExam='" + getIdExam() + "'" +
            ", title='" + getTitle() + "'" +
            ", level='" + getLevel() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
