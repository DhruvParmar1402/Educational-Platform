package com.Dhruv.EducationalPlatform.Repository;

import com.Dhruv.EducationalPlatform.DTO.AnswerDTO;
import com.Dhruv.EducationalPlatform.Entity.AnswerEntity;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.xspec.L;
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

    private ModelMapper modelMapper = new ModelMapper();


    public void save(AnswerDTO answerDTO) {
        mapper.save(modelMapper.map(answerDTO, AnswerEntity.class));
    }

    public List<AnswerDTO> findByDiscussionId(String discussionId,int pageSize,String lastEvaluatedKey) {
        Map<String , AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("discussionId",new AttributeValue().withS(discussionId));
            startKey.put("id",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":discussionId", new AttributeValue().withS(discussionId));

        DynamoDBQueryExpression<AnswerEntity> queryExpression = new DynamoDBQueryExpression<AnswerEntity>()
                .withIndexName("discussionId-id-index")
                .withKeyConditionExpression("discussionId = :discussionId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            queryExpression.withExclusiveStartKey(startKey);
        }

        List<AnswerEntity> list = mapper.queryPage(AnswerEntity.class, queryExpression).getResults();

        return list == null ? null : list.stream().map((entity) -> modelMapper.map(entity, AnswerDTO.class)).toList();
    }

    public List<AnswerDTO> findByUserId(String userId,int pageSize,String lastEvaluatedKey) {
        Map<String ,AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("userId",new AttributeValue().withS(userId));
            startKey.put("id",new AttributeValue().withS(lastEvaluatedKey));
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId", new AttributeValue().withS(userId));

        DynamoDBQueryExpression<AnswerEntity> queryExpression = new DynamoDBQueryExpression<AnswerEntity>()
                .withIndexName("userId-id-index")
                .withKeyConditionExpression("userId=:userId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            queryExpression.withExclusiveStartKey(startKey);
        }

        List<AnswerEntity> list = mapper.queryPage(AnswerEntity.class, queryExpression).getResults();

        return list == null ? null : list.stream().map((entity) -> modelMapper.map(entity, AnswerDTO.class)).toList();
    }

}
