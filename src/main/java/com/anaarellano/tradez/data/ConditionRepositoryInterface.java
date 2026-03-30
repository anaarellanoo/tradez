package com.anaarellano.tradez.data;

import java.util.List;

import com.anaarellano.tradez.models.ConditionEntity;

/**
 * ConditionRepository Interface 
 */
public interface ConditionRepositoryInterface 
{
    List<ConditionEntity> findAll();
}
