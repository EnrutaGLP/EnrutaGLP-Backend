package com.enrutaglp.backend.enums;

public enum EstadoPedido {
	EN_COLA((byte) 1) , 
	EN_PROCESO ((byte) 2), 
	COMPLETADO ((byte) 3);

	private byte value; 
	
	private EstadoPedido(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return this.value; 
	}
}
