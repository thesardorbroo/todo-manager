package todo.manager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import todo.manager.web.rest.TestUtil;

class CommentsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentsDTO.class);
        CommentsDTO commentsDTO1 = new CommentsDTO();
        commentsDTO1.setId(1L);
        CommentsDTO commentsDTO2 = new CommentsDTO();
        assertThat(commentsDTO1).isNotEqualTo(commentsDTO2);
        commentsDTO2.setId(commentsDTO1.getId());
        assertThat(commentsDTO1).isEqualTo(commentsDTO2);
        commentsDTO2.setId(2L);
        assertThat(commentsDTO1).isNotEqualTo(commentsDTO2);
        commentsDTO1.setId(null);
        assertThat(commentsDTO1).isNotEqualTo(commentsDTO2);
    }
}
