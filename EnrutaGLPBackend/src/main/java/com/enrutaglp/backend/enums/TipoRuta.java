package com.enrutaglp.backend.enums;

public enum TipoRuta {
	ENTREGA((byte) 1) , 
	RECARGA ((byte) 2);

	private byte value; 
	
	private TipoRuta(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return this.value; 
	}
}
