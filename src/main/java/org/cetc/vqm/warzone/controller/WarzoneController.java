package org.cetc.vqm.warzone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cetc.vqm.warzone.service.WarzoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarzoneController {
	
	@Autowired // (required = false) Spring容器不再抛出异而是认为这两个属性为null。
	private WarzoneService warzoneService;

	@PostMapping(value = "/warzone/getAllWarzones")
	public Map<String, Object> getAllWarzones()
	{
		
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<Map<String, Object>> warzones = warzoneService.ListAllwarzones();
		
		rs.put("Data", warzones);
		rs.put("flag", "success");
		rs.put("msg", "展示所有战区成功");
	
		return rs;
	}
}
