package com.example.restClient.service;

import org.springframework.stereotype.Service;

import com.example.restClient.dto.RequestDto;
import com.example.restClient.util.ThreadListener;

//이전에 MVC 패턴에서 사용했던 Command와 비슷하게 비즈니스 로직을 실행하는 역할을 함
@Service
public class APICallService {
	
	// 요청을 queue에 담는 메소드
	public void addQueue(RequestDto requestDto) throws InterruptedException{
		ThreadListener.queue.offer(requestDto);
	}
}
