package com.enrutaglp.backend.enums;

public enum EstadoCamion {
	EN_REPOSO((byte) 1) , 
	EN_RUTA ((byte) 2), 
	AVERIADO ((byte) 3), 
	EN_MANTENIMIENTO ((byte) 4);

	private byte value; 
	
	private EstadoCamion(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return this.value; 
	}
}
