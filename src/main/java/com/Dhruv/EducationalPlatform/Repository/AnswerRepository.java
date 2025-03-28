package com.Dhruv.EducationalPlatform.Repository;

import com.Dhruv.EducationalPlatform.DTO.AnswerDTO;
import com.Dhruv.EducationalPlatform.Entity.AnswerEntity;
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
public class AnswerRepository {

    @Autowired
    private DynamoDBMapper mapper;

    private ModelMapper modelMapper=new ModelMapper();


    public void save(AnswerDTO answerDTO) {
        mapper.save(modelMapper.map(answerDTO, AnswerEntity.class));
    }

    public List<AnswerDTO> findByDiscussionId(String discussionId) {
        Map<String, AttributeValue> eav=new HashMap<>();
        eav.put(":discussionId",new AttributeValue().withS(discussionId));

        DynamoDBQueryExpression<AnswerEntity> queryExpression = new DynamoDBQueryExpression<AnswerEntity>()
                .withIndexName("discussionId-index")
                .withKeyConditionExpression("discussionId=:discussionId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return mapper.query(AnswerEntity.class,queryExpression).stream().map((entity)->modelMapper.map(entity,AnswerDTO.class)).toList();
    }

    public List<AnswerDTO> findByUserId(String userId) {
        Map<String, AttributeValue> eav=new HashMap<>();
        eav.put(":userId",new AttributeValue().withS(userId));

        DynamoDBQueryExpression<AnswerEntity> queryExpression = new DynamoDBQueryExpression<AnswerEntity>()
                .withIndexName("userId-index")
                .withKeyConditionExpression("userId=:userId")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        return mapper.query(AnswerEntity.class,queryExpression).stream().map((entity)->modelMapper.map(entity,AnswerDTO.class)).toList();
    }

}
