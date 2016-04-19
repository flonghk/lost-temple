package com.zf.controller.index;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zf.common.AbstractController;

@Controller
public class IndexController extends AbstractController {
	
//	@Autowired
//	private PatchService patchService;
	
	@RequestMapping(value="/")
	public String homePatch(Map<String, Object> map) {
		return "/jsp/home/home.jsp";
	}
}
