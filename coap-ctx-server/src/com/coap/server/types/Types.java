

package com.coap.server.types;
	
public enum Types {
	SENSOR("sensor"), ACTUATOR("actuator");
     
    private final String valor;
    Types(String valorOpcao){
        valor = valorOpcao;
    }
    public String getValor(){
        return valor;
    }
}