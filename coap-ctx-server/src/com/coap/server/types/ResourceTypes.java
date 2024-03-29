package com.coap.server.types;

/**
 * @author PedroAlmir
 */
public enum ResourceTypes {
	TEMPERATURE("temperature"), PRESSURE("pressure"), LIGHTNESS("lightness"), PRINT("print");

	private final String valor;

	ResourceTypes(String valorOpcao) {
		valor = valorOpcao;
	}

	public String getValor() {
		return valor;
	}
}