package com.example.cloudstore.domain;

public class Message {

	private String notice;

	public Message() {
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Message(String notice) {

		this.notice = notice;
	}
}
