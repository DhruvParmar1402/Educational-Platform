package com.Dhruv.EducationalPlatform.Repository;

import com.Dhruv.EducationalPlatform.DTO.EnrollmentDTO;
import com.Dhruv.EducationalPlatform.Entity.EnrollmentEntity;
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
public class EnrollmentRepository {

    @Autowired
    private DynamoDBMapper mapper;

    private ModelMapper modelMapper=new ModelMapper();


    public void save(EnrollmentDTO enrollmentDTO) {
        mapper.save(modelMapper.map(enrollmentDTO,EnrollmentEntity.class));
    }

    public List<EnrollmentDTO> findAll (String userId,int pageSize,String lastEvaluatedKey) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":userId",new AttributeValue().withS(userId));

        Map<String , AttributeValue> startKey=new HashMap<>();

        if(lastEvaluatedKey!=null)
        {
            startKey.put("userId",new AttributeValue().withS(userId));
            startKey.put("courseId",new AttributeValue().withS(lastEvaluatedKey));
        }

        DynamoDBQueryExpression<EnrollmentEntity> queryExpression = new DynamoDBQueryExpression<EnrollmentEntity>()
                .withKeyConditionExpression("userId=:userId")
                .withExpressionAttributeValues(eav)
                .withLimit(pageSize)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        if(!startKey.isEmpty())
        {
            queryExpression.withExclusiveStartKey(startKey);
        }

        List<EnrollmentEntity>list= mapper.queryPage(EnrollmentEntity.class, queryExpression).getResults();
        return list==null?null:list.stream().map((entity)->modelMapper.map(entity, EnrollmentDTO.class)).toList();
    }
}
