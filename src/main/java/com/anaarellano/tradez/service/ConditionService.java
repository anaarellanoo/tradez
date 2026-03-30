package com.anaarellano.tradez.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.anaarellano.tradez.data.ConditionRepository;
import com.anaarellano.tradez.models.ConditionEntity;
import com.anaarellano.tradez.models.ConditionModel;
import com.anaarellano.tradez.models.Converter;

/**
 * Condition Service 
 */
@Service
public class ConditionService 
{

    private ConditionRepository conditionRepository;

    private Converter converter;

    /**
     * Constructor
     * @param conditionRepository
     * @param converter
     */
    public ConditionService(ConditionRepository conditionRepository, Converter converter) 
    {
        this.conditionRepository = conditionRepository;
        this.converter = converter;
    }

    /**
     * Get all the Conditions 
     * @return
     */
    public List<ConditionModel> getAllConditions()
    {
        List<ConditionEntity> entities = conditionRepository.findAll();

        List<ConditionModel> models = new ArrayList<>();

        for(ConditionEntity entity : entities)
        {
            ConditionModel model = converter.toModel(entity);
            models.add(model);
        }

        return models;
    }
}
