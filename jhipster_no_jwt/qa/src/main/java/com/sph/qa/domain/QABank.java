package com.sph.qa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.UUID;

import com.sph.qa.domain.enumeration.Category;

/**
 * A QABank.
 */
@Entity
@Table(name = "qa_bank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QABank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "uuid-char")
    @Column(name = "id_qa_bank", length = 36, nullable = false)
    private UUID idQABank;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "contents", nullable = false)
    private String contents;

    @NotNull
    @Column(name = "git_url", nullable = false)
    private String gitUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

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

    public QABank idQABank(UUID idQABank) {
        this.idQABank = idQABank;
        return this;
    }

    public void setIdQABank(UUID idQABank) {
        this.idQABank = idQABank;
    }

    public String getTitle() {
        return title;
    }

    public QABank title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public QABank contents(String contents) {
        this.contents = contents;
        return this;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public QABank gitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
        return this;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public Category getCategory() {
        return category;
    }

    public QABank category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QABank)) {
            return false;
        }
        return id != null && id.equals(((QABank) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QABank{" +
            "id=" + getId() +
            ", idQABank='" + getIdQABank() + "'" +
            ", title='" + getTitle() + "'" +
            ", contents='" + getContents() + "'" +
            ", gitUrl='" + getGitUrl() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
