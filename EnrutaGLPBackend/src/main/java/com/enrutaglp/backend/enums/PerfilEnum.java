package com.enrutaglp.backend.enums;

public enum PerfilEnum {
	ADMINISTRADOR((byte) 1) , 
	GESTOR_PEDIDOS ((byte) 2), 
	GESTOR_RUTAS ((byte) 3);

	private byte value; 
	
	private PerfilEnum(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return this.value; 
	}
}
