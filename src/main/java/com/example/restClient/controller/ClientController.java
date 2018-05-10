package com.example.restClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.restClient.dto.RequestDto;
import com.example.restClient.service.APICallService;


@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	APICallService service;
	
	@RequestMapping("/csvDownloadForm")
	public String csvDownloadForm() {
		return "client/csvDownloadForm";
	}

	// 입력받은 요청을 Queue에 담는 메소드
	@RequestMapping("/sectionProc")
	private String boardSectionProc(@ModelAttribute RequestDto requestDto) throws Exception {
		service.addQueue(requestDto);
		return "redirect:csvDownloadForm";
	}
}
