package com.example.restClient.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardDto {
	
	private int bNo; // 게시글 번호
	private String subject; // 게시글 제목
	private String content; // 게시글 내용
	private String writer; // 작성자
	private Date reg_date; // 작성일
	
	public int getbNo() {
		return bNo;
	}
	
	public void setbNo(int bNo) {
		this.bNo = bNo;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getWriter() {
		return writer;
	}
	
	public void setWriter(String writer) {
		this.writer = writer;
	}
	
	public Date getReg_date() {
		return reg_date;
	}
	
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}	


}
