package com.Dhruv.EducationalPlatform.Repository;

import com.Dhruv.EducationalPlatform.DTO.DiscussionDTO;
import com.Dhruv.EducationalPlatform.Entity.DiscussionEntity;
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
public class DiscussionRepository {

    @Autowired
    private DynamoDBMapper mapper;

    private ModelMapper modelMapper = new ModelMapper();


    public void save(DiscussionDTO discussion) {
        mapper.save(modelMapper.map(discussion, DiscussionEntity.class));
    }

    public List<DiscussionDTO> findByCourseId(String courseId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":courseId", new AttributeValue().withS(courseId));

        DynamoDBQueryExpression<DiscussionEntity> queryExpression = new DynamoDBQueryExpression<DiscussionEntity>()
                .withIndexName("courseId-index")
                .withKeyConditionExpression("courseId=:courseId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        List<DiscussionEntity> list = mapper.query(DiscussionEntity.class, queryExpression);
        return list == null ? null : list.stream().map((entity) -> modelMapper.map(entity, DiscussionDTO.class)).toList();
    }

    public List<DiscussionDTO> findByUserId(String userId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<DiscussionEntity> queryExpression = new DynamoDBQueryExpression<DiscussionEntity>()
                .withIndexName("userId-index")
                .withKeyConditionExpression("userId =:userId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        List<DiscussionEntity> list = mapper.query(DiscussionEntity.class, queryExpression);
        return list == null ? null : list.stream().map((entity) -> modelMapper.map(entity, DiscussionDTO.class)).toList();
    }

    public DiscussionDTO findById(String id) {
        DiscussionEntity entity = mapper.load(DiscussionEntity.class, id);
        return entity == null ? null : modelMapper.map(entity, DiscussionDTO.class);
    }
}
