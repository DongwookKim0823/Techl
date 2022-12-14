package com.umc.techl.src.controller;

import com.umc.techl.config.BaseException;
import com.umc.techl.config.BaseResponse;
import com.umc.techl.config.BaseResponseStatus;
import com.umc.techl.src.model.book.GetBookInfoRes;
import com.umc.techl.src.model.post.PostNewCommentReq;
import com.umc.techl.src.model.post.PostNewCommentRes;
import com.umc.techl.src.model.post.GetPostContentsRes;
import com.umc.techl.src.model.post.GetPostListRes;
import com.umc.techl.src.model.post.PostNewPostReq;
import com.umc.techl.src.model.post.PostNewPostRes;
import com.umc.techl.src.service.PostService;
import com.umc.techl.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.techl.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetPostListRes> getPostListInfo(@RequestParam int bookIdx) {
        try{
            GetPostListRes postListInfo = postService.getPostListInfo(bookIdx);
            return new BaseResponse<>(postListInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/new-post")
    public BaseResponse<GetBookInfoRes> getBookInfo(@RequestParam int bookIdx) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            GetBookInfoRes bookInfo = postService.getBookInfo(bookIdx);
            return new BaseResponse<>(bookInfo);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/new-post/create")
    public BaseResponse<PostNewPostRes> createPostContents(@RequestParam int bookIdx,
                                                           @RequestBody PostNewPostReq postNewPostReq) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            if (postNewPostReq.getTitle() == null || postNewPostReq.getTitle().length() == 0) {
                throw new BaseException(POST_EMPTY_TITLE);
            }

            if (postNewPostReq.getContent() == null || postNewPostReq.getContent().length() == 0) {
                throw new BaseException(POST_EMPTY_CONTENTS);
            }

            if (postNewPostReq.getConfirmMethod() == null || postNewPostReq.getConfirmMethod().length() == 0) {
                throw new BaseException(POST_EMPTY_CONFIRMMETHOD);
            }

            if (postNewPostReq.getStartDate() == null || postNewPostReq.getStartDate().length() == 0
                || postNewPostReq.getEndDate() == null || postNewPostReq.getEndDate().length() == 0) {
                throw new BaseException(POST_EMPTY_DATE);
            }

            PostNewPostRes postNewPostRes = postService.createPostContents(bookIdx, postNewPostReq);
            return new BaseResponse<>(postNewPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{postIdx}")
    public BaseResponse<GetPostContentsRes> getPostContentsInfo(@PathVariable("postIdx")int postIdx) {
        try{
            GetPostContentsRes getPostContentsRes = postService.getPostContentsInfo(postIdx);
            return new BaseResponse<>(getPostContentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/{postIdx}/new-post-comment")
    public BaseResponse<PostNewCommentRes> createNewPostComment(@PathVariable("postIdx")int postIdx,
                                                                 @RequestBody PostNewCommentReq postNewCommentReq) {

        try{
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            if (postNewCommentReq.getContent() == null || postNewCommentReq.getContent().length() == 0) {
                throw new BaseException(POST_EMPTY_CONTENTS);
            }

            PostNewCommentRes postNewCommentRes = postService.createNewPostComment(postIdx, postNewCommentReq);
            return new BaseResponse<>(postNewCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/{postIdx}/join-contents")
    public BaseResponse joinContents(@PathVariable("postIdx") int postIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            postService.joinContents(postIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/{postIdx}/bookmark")
    public BaseResponse bookmark(@PathVariable("postIdx") int postIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            postService.bookmark(postIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/post-comment-upvote/{postCommentIdx}")
    public BaseResponse postCommentUpvote(@PathVariable("postCommentIdx") int postCommentIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            postService.postCommentUpvote(postCommentIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @DeleteMapping("/{postIdx}/delete-post")
    public BaseResponse deletePost(@PathVariable("postIdx")int postIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            postService.postDelete(postIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @DeleteMapping("/delete-postComment/{postCommentIdx}")
    public BaseResponse deletePostComment(@PathVariable("postCommentIdx")int postCommentIdx) {
        try {
            String accessToken = jwtService.getJwt();
            if(accessToken == null || accessToken.length() == 0){
                throw new BaseException(EMPTY_JWT);
            }

            postService.postCommentDelete(postCommentIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
