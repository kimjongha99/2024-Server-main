package com.example.demo.src.article;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.article.model.PostArticleReq;
import com.example.demo.src.article.model.PostArticleRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.enums.UserState.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.INVALID_USER_ACCESS;
import static com.example.demo.common.response.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final  LikeRepository likeRepository;


    public PostArticleRes createArticle(PostArticleReq postArticleReq, Long jwtUserId) {

        // JWT에서 추출한 userId와 요청에서 받은 userId가 일치하는지 확인
        if (!postArticleReq.getUserId().equals(jwtUserId)) {
            throw new BaseException(INVALID_USER_ACCESS); // 적절한 예외 처리
        }



        User user = userRepository.findByIdAndState(postArticleReq.getUserId(),ACTIVE)
                .orElseThrow(()-> new BaseException(POST_USERS_EXISTS_EMAIL));

        Article article = postArticleReq.toEntity(user);
        articleRepository.save(article);

        return new PostArticleRes(article);
    }

}
