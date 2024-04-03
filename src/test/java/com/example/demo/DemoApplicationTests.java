//package com.example.demo;
//
//
//import com.example.demo.src.article.ArticleService;
//import com.example.demo.src.article.model.PostArticleReq;
//import com.example.demo.src.article.model.PostArticleRes;
//import com.example.demo.src.comment.CommentService;
//import com.example.demo.src.comment.model.CreateCommentReq;
//import com.example.demo.src.comment.model.CreateCommentRes;
//import com.example.demo.src.user.UserService;
//import com.example.demo.src.user.model.PostUserReq;
//import com.example.demo.src.user.model.PostUserRes;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest(properties = {"spring.profiles.active=dev"})
//class DemoApplicationTests {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ArticleService articleService;
//    @Autowired
//    private CommentService commentService;
//
//
//    @Test
//    void contextLoads() {
//    }
//
//    private List<Long> userIds = new ArrayList<>();
//
//    @Test
//    public void createMultipleUsersTest() {
//        for (int i = 0; i < 100; i++) {
//            PostUserReq testUserReq = new PostUserReq(
//                    "testuser" + i + "@example.com",
//                    "Test1234!",
//                    "Test User " + i,
//                    LocalDate.of(1990, 1, 1),
//                    true,
//                    true,
//                    true
//            );
//
//            PostUserRes response = userService.createUser(testUserReq);
//            assertThat(response.getId()).isNotNull();
//            userIds.add(response.getId()); // Store the created user IDs
//        }
//    }
//
//    @Test
//    public void createMultipleArticlesTest() {
//        createMultipleUsersTest();
//
//        for (Long userId : userIds) {
//            for (int j = 0; j < 100; j++) {
//                PostArticleReq articleReq = new PostArticleReq(
//                        userId,
//                        "Content for article by user " + userId + " number " + j,
//                        Collections.singletonList("https://example.com/image" + j + ".jpg"),
//                        Collections.singletonList("https://example.com/video" + j + ".mp4")
//                );
//
//                PostArticleRes articleRes = articleService.createArticle(articleReq, userId);
//
//                assertThat(articleRes.getArticleId()).isNotNull();
//            }
//        }
//    }
//    @Test
//    public void createCommentOnEveryArticleByUserOne() {
//        Long userId = 1L;
//
//        List<Long> articleIds = articleService.getAllArticleIds();
//
//        for (Long articleId : articleIds) {
//            CreateCommentReq request = new CreateCommentReq(articleId, "This is a test comment.");
//
//            CreateCommentRes response = commentService.createComment(request, userId);
//
//            assertThat(response).isNotNull();
//            assertThat(response.getCommentId()).isNotNull();
//        }
//    }
//}