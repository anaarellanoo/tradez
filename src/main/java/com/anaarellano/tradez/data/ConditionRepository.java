package com.anaarellano.tradez.data;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.ConditionEntity;

/**
 * Conditions Repository
 * implements ConditionRepositoryInterface 
 */
@Repository
public class ConditionRepository implements ConditionRepositoryInterface
{
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor
     * @param jdbcTemplate
     */
    public ConditionRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Find all the conditions
     */
    @Override
    public List<ConditionEntity> findAll() 
    {

        String sql = "SELECT conditionId, conditionName, description FROM conditions";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ConditionEntity(
                rs.getInt("conditionId"),
                rs.getString("conditionName"),
                rs.getString("description")));
    }
    

    /**
     * Find the condition name from the id
     */
    @Override
    public String findConditionName(int id) 
    {
        String sql = "SELECT conditionName FROM conditions WHERE conditionId = ?";

        return jdbcTemplate.queryForObject(sql, String.class, id);

    }
    
}
