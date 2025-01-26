package com.barocook.barocookboard.repository;

import com.barocook.barocookboard.config.JpaConfig;
import com.barocook.barocookboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
@ActiveProfiles("testdb")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest   //slice test
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);

    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long count = articleRepository.count();
        // When

        Article article = articleRepository.save(Article.of("new article", "new content", "#new"));

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(count+1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String newHashtag = "#springboot";

        article.setHashtag(newHashtag);
        // When
        Article savedArticle = articleRepository.saveAndFlush(article);
        // Test 는 기본적으로 트랜잭션 걸린 상태
        // 기본 설정이 rollback
        // save 만 하면 영속성 컨택스트 에 변화가 없어서 update 문 미생성 -> saveAndFlush

        // Then
        assertThat(savedArticle)
                .hasFieldOrPropertyWithValue("hashtag",newHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticles = articleRepository.count();
        long previousArticleComments = articleCommentRepository.count();
        int deletedCommentsCnt = article.getArticleComments().size();
        // When
        articleRepository.delete(article);

        // Then
       assertThat(articleRepository.count())
               .isEqualTo(previousArticles-1);
       assertThat(articleCommentRepository.count())
               .isEqualTo(previousArticleComments-deletedCommentsCnt);


    }
}