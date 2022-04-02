package com.lagou.edu.comment.repository;

import com.lagou.edu.comment.entity.CourseComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface CourseCommentRepository extends MongoRepository<CourseComment,Long> {
    @Query(value = "{'$and':[{'isDel':false},{'status':1},{'id':{'$in':?0}}]}")
    List<CourseComment> getReplyCourseCommentList(@Param("parentIds") List<String> parentIds);
}
