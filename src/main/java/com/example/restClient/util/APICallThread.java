package com.example.restClient.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.restClient.dto.RequestDto;

public class APICallThread implements Runnable{
	
	private String url;
	
	public APICallThread() {}
	
	public APICallThread(String url) {
		this.url = url;
	}

	@Override
	/** 스레드가 실제로 작업하는 부분 */
	public void run() {
		System.out.println(this.url + ":running");
		while (true) {
			try {
				Thread.sleep(1000); // 1초 휴식
				RequestDto request = ThreadListener.queue.poll(); // Queue에서 요청을 꺼낸다.
				if(request != null) { // 다른 큐에서 요청을 받을때 혹은 요청이 없으면 request는 null값이다.
					String result = sendRequest(request, this.url); // 요청
					
					// 응답 받은 내용 표시
					System.out.println(this.url+":"+result);
				} 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Rest API에 요청을 보내고 응답을 받는 메소드
	private String sendRequest(RequestDto requestDto, String url) {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("fileName", requestDto.getFileName());
		map.add("startNum", requestDto.getStartNum());
		map.add("lastNum", requestDto.getLastNum());

		// Json이나 XML형태의 응답을 쉽게 객체로 바꾸어 쓰게 해주는 RestTemplate 클래스 
		RestTemplate restTemplate = new RestTemplate();

		// API 요청 Header의 ContentType을 지정 : 요청의 내용이 어떤 형식인지 알려줌
		// MediaType.APPLICATION_FORM_URLENCODED : 요청을 html form 형태로 지정함
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// header와 body를 HttpEntity 객체에 저장
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);


		// HttpEntity를 상속받은 클래스인 ResponseEntity 객체로 응답을 받는다. post 방식으로 요청을 보내서 응답의 body타입을 String으로 받는다. 
		ResponseEntity<String> response = restTemplate.postForEntity(url, request , String.class );

		return response.getBody();
	}
}
