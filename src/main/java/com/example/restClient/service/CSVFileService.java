package com.example.restClient.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.restClient.dto.BoardDto;

//이전에 MVC 패턴에서 사용했던 Command와 비슷하게 비즈니스 로직을 실행하는 역할을 함
@Service("com.example.restClient.service.CSVFileService")
public class CSVFileService {

	// csvDirPath에 properties에 만든 property 주입
	@Value("${csv.download.directory}")
	String csvDirPath;
	
	// properties에 있는 api-url 주입
	@Value("${rest.api.url}")
	String apiUrl;

	// 만드려는 파일이 존재하는지 확인하는 메소드
	public void checkFile(String fileName, int startNum, int lastNum) throws Exception {
		File file = new File(csvDirPath + "/" + fileName + ".csv");

		if(!file.exists()) {
			// 파일 생성
			String column = "bNo,subject,content,writer,reg_date\n";
			createCsv(fileName, column, csvDirPath); // csvDirPath는 application.properties에 입력하여 주입받은 경로를 가지고 있다.
			writeFile(fileName, startNum, lastNum);
		}else {
			writeFile(fileName, startNum, lastNum);
		}
	}

	// Rest API에 접근해서 지정한 범위만큼  record를 가져오는 메소드
	private List<BoardDto> getRecord(int startNum, int limit) {

		// Json이나 XML형태의 응답을 쉽게 객체로 바꾸어 쓰게 해주는 RestTemplate 클래스 
		RestTemplate restTemplate = new RestTemplate();

		// API 요청 Header의 ContentType을 지정 : 요청의 내용이 어떤 형식인지 알려줌
		// MediaType.APPLICATION_FORM_URLENCODED : 요청을 html form 형태로 지정함
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// request의 body가 될 MultiValueMap에 startNum과 limit을 넣어준다.
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("startNum", startNum+"");
		map.add("limit", limit+"");

		// header와 body를 HttpEntity 객체에 저장
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		// HttpEntity를 상속받은 클래스인 ResponseEntity 객체로 응답을 받는다.
		// 요청할 api의 url과 요청 방식, request, ParameterizedReference 추상 클래스로 응답의 body 타입을 지정한다.
		ResponseEntity<List<BoardDto>> dtoResponse = restTemplate.exchange(apiUrl,
				HttpMethod.POST, request, new ParameterizedTypeReference<List<BoardDto>>() {});

		return dtoResponse.getBody(); // response의 body == List<BoardDto> 타입을 리턴한다.
	}

	// 내 서버에 백업용 csv파일을 만드는 메소드
	private void writeFile(String fileName, int startNum, int lastNum) throws Exception {

		// DB에 접근할때마다 가져올 레코드를 제한함
		int limit = 100;
		if(lastNum - startNum < limit) limit = lastNum - startNum; // 처음부터 100보다 작은 값이 들어올 수 있기때문에

		// 저장된 파일에 이어쓰기
		while(true) {
			List<BoardDto> tmpList = writeCsv(fileName, startNum, limit);

			if(tmpList.size() == 0 || lastNum == startNum + tmpList.size()) {
				break;
			}else {
				startNum += tmpList.size();
				if(lastNum - startNum < limit) limit = lastNum - startNum;
			}
		}
	}

	// 내 서버에 csv파일을 생성하는 메소드
	public void createCsv(String title, String column, String filepath) {
		try {
			// BufferedWriter 객체를 생성 
			FileWriter fileWriter = new FileWriter(filepath + "/" + title + ".csv", true);
			fileWriter.write(column); // 컬럼 설정

			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 내 서버에 있는 csv 파일에 이어서 레코드를 입력하는 메소드
	public List<BoardDto> writeCsv(String title, int startNum, int limit) throws Exception {
		List<BoardDto> tmpList = getRecord(startNum, limit); // Service 객체로 limit의 크기만큼 레코드를 가져옴
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csvDirPath+"/"+title+".csv", true));
			bufferedWriter.write(setContent(tmpList));
			bufferedWriter.flush();
			bufferedWriter.close();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return tmpList;
	}

	// board의 데이터를 csv파일에 옮겨주는 메소드
	private String setContent(List<BoardDto> dtoList) {
		StringBuffer sb = new StringBuffer();

		for(int i=0; i<dtoList.size(); i++) {
			int bId = dtoList.get(i).getbNo();
			String subject = dtoList.get(i).getSubject();
			String content = dtoList.get(i).getContent();
			String writer = dtoList.get(i).getWriter();
			String reg_date = dtoList.get(i).getReg_date().toString();

			sb.append(bId + "," + subject + "," + content + "," + writer + "," + reg_date + ",\n");
		}

		return sb.toString();
	}

	// 저장된 파일을 다운로드 받게 해주는 메소드
	public void download(HttpServletRequest request, HttpServletResponse response, String fileName) {
		try{
			InputStream in = null; // inputStream 
			OutputStream os = null; // outputStream
			File file = null;
			boolean skip = false;
			String client = "";

			//파일을 읽어 스트림에 담기  
			try{
				file = new File(csvDirPath, fileName);
				in = new FileInputStream(file);
			} catch (FileNotFoundException fe) {
				skip = true;
			}

			client = request.getHeader("User-Agent");

			//파일 다운로드 헤더 지정 
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Description", "JSP Generated Data");

			if (!skip) { // skip이 false일 때
				// IE
				if (client.indexOf("MSIE") != -1) {
					response.setHeader("Content-Disposition", "attachment; filename=\""
							+ java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
					// IE 11 이상.
				} else if (client.indexOf("Trident") != -1) {
					response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				} else {
					// 한글 파일명 처리
					response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
					response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
				}
				response.setHeader("Content-Length", "" + file.length());
				os = response.getOutputStream();
				byte b[] = new byte[(int) file.length()];
				int leng = 0;
				while ((leng = in.read(b)) > 0) {
					os.write(b, 0, leng);
				}
			} else {
				response.setContentType("text/html;charset=UTF-8");
				System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
			}
			in.close();
			os.close();
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getMessage());
		}
	}

}
