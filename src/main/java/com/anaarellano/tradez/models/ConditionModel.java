package com.anaarellano.tradez.models;

/**
 * Condition Model 
 * -> Service/Controller
 */
public class ConditionModel 
{
    private int conditionId;
    private String conditionName;
    private String description;

    /**
     * Constructor
     */
    public ConditionModel()
    {

    }

    /**
     * Parametized Constructor
     * @param conditionId
     * @param description
     * @param conditionName
     */
    public ConditionModel(int conditionId, String conditionName, String description) 
    {
        this.conditionId = conditionId;
        this.conditionName = conditionName;
        this.description = description;
    }

    /********************* GETTERS AND SETTER *********************/
    
    public Integer getConditionId() {
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
