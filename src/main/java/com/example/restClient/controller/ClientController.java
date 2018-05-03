package com.example.restClient.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.restClient.service.CSVFileService;


@Controller
@RequestMapping("/client")
public class ClientController {
	

	@Autowired
	CSVFileService service;
	
	@RequestMapping("/csvDownloadForm")
	public String csvDownloadForm() {
		return "client/csvDownloadForm";
	}


	// 구간을 입력받아 입력 받은 구간만큼 DB의 레코드를 설정하여 반환 받는 메소드
	@RequestMapping("/sectionProc")
	private void boardSectionProc(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fileName = request.getParameter("fileName");
		int startNum = Integer.parseInt(request.getParameter("startNum")) - 1; // MySQL 쿼리문에서 그래도 입력하면 입력된 startNum의 그 다음 수 부터 가져오기 때문에 -1을 해줌
		int lastNum = Integer.parseInt(request.getParameter("lastNum"));

		service.checkFile(fileName, startNum, lastNum);

		// 경로에 저장된 csv파일 다운로드

		// 게시물에 저장된 파일의 이름은 확장자까지 포함되어 있지만 fileName에는 확장자를 제외한 이름이기에 확장자를 포함해줌
		String fullFileName = fileName + ".csv"; 
		// 디렉토리에 저장된 파일의 이름 그대로 다운 받기 위해 디렉토리에 저장된 이름(fileName)과 원래 파일이름(oriFileName)에 같은 값을 넣어준다.
		service.download(request, response, fullFileName);

	}




}
