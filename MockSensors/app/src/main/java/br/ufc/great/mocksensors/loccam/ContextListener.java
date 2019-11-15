package br.ufc.great.mocksensors.loccam;

public interface ContextListener {
    void onContextReady(String data);
    String getContextKey();
}
