package com.Dhruv.EducationalPlatform.Repository;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.Entity.CourseEntity;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CourseRepository {

    @Autowired
    private DynamoDBMapper mapper;

    private ModelMapper modelMapper = new ModelMapper();


    public void save(CourseDTO courseEntity) {
        mapper.save(modelMapper.map(courseEntity, CourseEntity.class));
    }

    public List<CourseDTO> getAll(String id,int pageSize,String lastEvaluatedKey) {
        Map<String,AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("instructorId",new AttributeValue().withS(id));
            startKey.put("id",new AttributeValue().withS(id));
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":instructorId", new AttributeValue().withS(id));

        DynamoDBQueryExpression<CourseEntity> queryExpression = new DynamoDBQueryExpression<CourseEntity>()
                .withIndexName("instructorId-id-index")
                .withKeyConditionExpression("instructorId = :instructorId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            queryExpression.withExclusiveStartKey(startKey);
        }

        List<CourseEntity>list=mapper.queryPage(CourseEntity.class, queryExpression).getResults();
        return list==null?null:list.stream().map((courseEntity -> modelMapper.map(courseEntity, CourseDTO.class))).toList();
    }

    public CourseDTO findCourseById(String id) {
        CourseEntity course=mapper.load(CourseEntity.class, id);
        return course==null?null:modelMapper.map(course, CourseDTO.class);
    }

    public void deleteCourse(CourseEntity course) {
        mapper.delete(course);
    }

    public List<CourseDTO> getAllCourses(int pageSize,String lastEvaluatedKey) {
        Map<String , AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("id",new AttributeValue(lastEvaluatedKey));
        }

        DynamoDBScanExpression expression=new DynamoDBScanExpression()
                .withLimit(pageSize)
                .withConsistentRead(false);

        if (!startKey.isEmpty())
        {
            expression.withExclusiveStartKey(startKey);
        }

        List<CourseEntity> courses = mapper.scanPage(CourseEntity.class,expression).getResults();
        return courses==null?null:courses.stream().map((entity) -> modelMapper.map(entity, CourseDTO.class)).toList();
    }

}
