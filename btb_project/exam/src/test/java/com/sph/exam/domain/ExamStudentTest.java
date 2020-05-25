package com.sph.exam.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sph.exam.web.rest.TestUtil;

public class ExamStudentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamStudent.class);
        ExamStudent examStudent1 = new ExamStudent();
        examStudent1.setId(1L);
        ExamStudent examStudent2 = new ExamStudent();
        examStudent2.setId(examStudent1.getId());
        assertThat(examStudent1).isEqualTo(examStudent2);
        examStudent2.setId(2L);
        assertThat(examStudent1).isNotEqualTo(examStudent2);
        examStudent1.setId(null);
        assertThat(examStudent1).isNotEqualTo(examStudent2);
    }
}
