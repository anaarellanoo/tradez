package com.anaarellano.tradez.models;

/**
 * Condition Entity 
 * -> Data/Repository layer
 */
public class ConditionEntity 
{
    private int conditionId;
    private String conditionName;
    private String description;

    /**
     * Constructor
     */
    public ConditionEntity() {
    }

    /**
     * Parametized COnstructor 
     * @param conditionId
     * @param description
     * @param conditionName
     */
    public ConditionEntity(int conditionId, String description, String conditionName) {
        this.conditionId = conditionId;
        this.description = description;
        this.conditionName = conditionName;
    }

    /********************* GETTERS AND SETTER *********************/

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    
}
