package com.sph.qa.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sph.qa.web.rest.TestUtil;

public class QABankTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QABank.class);
        QABank qABank1 = new QABank();
        qABank1.setId(1L);
        QABank qABank2 = new QABank();
        qABank2.setId(qABank1.getId());
        assertThat(qABank1).isEqualTo(qABank2);
        qABank2.setId(2L);
        assertThat(qABank1).isNotEqualTo(qABank2);
        qABank1.setId(null);
        assertThat(qABank1).isNotEqualTo(qABank2);
    }
}
