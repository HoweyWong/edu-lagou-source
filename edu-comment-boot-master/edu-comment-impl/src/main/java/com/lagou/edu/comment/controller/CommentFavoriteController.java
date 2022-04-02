package com.lagou.edu.comment.controller;


import com.lagou.edu.comment.api.dto.CourseCommentFavoriteDTO;
import com.lagou.edu.comment.remote.CourseCommentFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commentfavorite/")
public class CommentFavoriteController {

    @Autowired
    private CourseCommentFavoriteService courseCommentFavoriteService;


    @GetMapping("favorite")
    public CourseCommentFavoriteDTO favorite(@RequestParam("userId") Integer userId,
                                             @RequestParam("commentId") String commentId) {
        return this.courseCommentFavoriteService.favorite(userId, commentId);
    }

    @GetMapping("cancelFavorite")
    public boolean cancelFavorite(@RequestParam("userId") Integer userId,
                                  @RequestParam("commentId") String commentId) {
        return this.courseCommentFavoriteService.cancelFavorite(userId, commentId);
    }
}
