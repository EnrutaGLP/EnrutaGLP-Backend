package com.enrutaglp.backend.enums;

public enum TipoMantenimiento {
	PREVENTIVO((byte) 1) , 
	CORRECTIVO ((byte) 2);

	private byte value; 
	
	private TipoMantenimiento(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return this.value; 
	}
}
