package com.Dhruv.EducationalPlatform.Repository;

import com.Dhruv.EducationalPlatform.DTO.CourseDTO;
import com.Dhruv.EducationalPlatform.Entity.CourseEntity;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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

    private ModelMapper modelMapper=new ModelMapper();


    public void save(CourseDTO courseEntity) {
        mapper.save(modelMapper.map(courseEntity, CourseEntity.class));
    }

    public List<CourseDTO> getAll(String id)
    {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":instructorId", new AttributeValue().withS(id));

        DynamoDBQueryExpression<CourseEntity> queryExpression = new DynamoDBQueryExpression<CourseEntity>()
                .withIndexName("instructorId-index")
                .withKeyConditionExpression("instructorId = :instructorId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return mapper.query(CourseEntity.class, queryExpression).stream().map((courseEntity -> modelMapper.map(courseEntity, CourseDTO.class))).toList();
    }

    public CourseDTO findCourseById(String id) {
       return modelMapper.map(mapper.load(CourseEntity.class,id), CourseDTO.class);
    }

}
