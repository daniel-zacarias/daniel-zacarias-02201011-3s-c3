package br.com.bandtec.Danielac3.dominios;

public class Processo <T>{
    private String protocolo;
    private T objeto;

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public T getObjeto() {
        return objeto;
    }

    public void setObjeto(T objeto) {
        this.objeto = objeto;
    }

    public Processo(String protocolo, T objeto) {
        this.protocolo = protocolo;
        this.objeto = objeto;
    }
}
