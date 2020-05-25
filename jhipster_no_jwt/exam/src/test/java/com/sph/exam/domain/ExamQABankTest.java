package com.sph.exam.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sph.exam.web.rest.TestUtil;

public class ExamQABankTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamQABank.class);
        ExamQABank examQABank1 = new ExamQABank();
        examQABank1.setId(1L);
        ExamQABank examQABank2 = new ExamQABank();
        examQABank2.setId(examQABank1.getId());
        assertThat(examQABank1).isEqualTo(examQABank2);
        examQABank2.setId(2L);
        assertThat(examQABank1).isNotEqualTo(examQABank2);
        examQABank1.setId(null);
        assertThat(examQABank1).isNotEqualTo(examQABank2);
    }
}
