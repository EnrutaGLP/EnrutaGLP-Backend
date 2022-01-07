package com.enrutaglp.backend.enums;

public enum ModoEjecucion {
	DIA_A_DIA((byte) 1) , 
	SIM_TRES_DIAS ((byte) 2), 
	SIM_COLAPSO ((byte) 3);

	private byte value; 
	
	private ModoEjecucion(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return this.value; 
	}
}
