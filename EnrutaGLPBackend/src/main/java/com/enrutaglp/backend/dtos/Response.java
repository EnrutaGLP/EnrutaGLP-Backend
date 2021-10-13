package com.enrutaglp.backend.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response {
	private String status;
	private String message;
	private Object data;
	
	public Response(Object data) {
		this.status = "success";
		this.message = null;
		this.data = data;
	}
	
	public Response(String status, String mensaje) {
		this.status = status;
		this.message = mensaje;
		this.data = null;
	}

	public Response(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public boolean isSuccess() {
		return status.equals("success");
	}	
}
