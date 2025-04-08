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

    public List<DiscussionDTO> findByCourseId(String courseId,int pageSize,String lastEvaluatedKey) {
        Map<String, AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("courseId",new AttributeValue().withS(courseId));
            startKey.put("id",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":courseId", new AttributeValue().withS(courseId));

        DynamoDBQueryExpression<DiscussionEntity> queryExpression = new DynamoDBQueryExpression<DiscussionEntity>()
                .withIndexName("courseId-id-index")
                .withKeyConditionExpression("courseId=:courseId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            queryExpression.withExclusiveStartKey(startKey);
        }

        List<DiscussionEntity> list = mapper.queryPage(DiscussionEntity.class, queryExpression).getResults();
        return list == null ? null : list.stream().map((entity) -> modelMapper.map(entity, DiscussionDTO.class)).toList();
    }

    public List<DiscussionDTO> findByUserId(String userId,int pageSize,String lastEvaluatedKey) {
        Map<String ,AttributeValue> startKey=new HashMap<>();
        if(lastEvaluatedKey!=null)
        {
            startKey.put("userId",new AttributeValue().withS(userId));
            startKey.put("id",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<DiscussionEntity> queryExpression = new DynamoDBQueryExpression<DiscussionEntity>()
                .withIndexName("userId-id-index")
                .withKeyConditionExpression("userId =:userId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            queryExpression.withExclusiveStartKey(startKey);
        }

        List<DiscussionEntity> list = mapper.queryPage(DiscussionEntity.class, queryExpression).getResults();
        return list == null ? null : list.stream().map((entity) -> modelMapper.map(entity, DiscussionDTO.class)).toList();
    }

    public DiscussionDTO findById(String id) {
        DiscussionEntity entity = mapper.load(DiscussionEntity.class, id);
        return entity == null ? null : modelMapper.map(entity, DiscussionDTO.class);
    }
}
