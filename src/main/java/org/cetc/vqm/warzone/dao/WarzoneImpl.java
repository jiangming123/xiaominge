package org.cetc.vqm.warzone.dao;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.warzone.dao.WarzoneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WarzoneImpl implements WarzoneMapper{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	

	@Override
	public List<Map<String, Object>> ListAllwarzone() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForList("select * from warzone order by zoneid");
	}

}
