package org.cetc.vqm.warzone.service;

import java.util.List;
import java.util.Map;

import org.cetc.vqm.warzone.dao.WarzoneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarzoneService {

	@Autowired
	WarzoneMapper warzoneMapper;
	
	public List<Map<String, Object>> ListAllwarzones() {
		// TODO Auto-generated method stub
		return warzoneMapper.ListAllwarzone();
	}

}
