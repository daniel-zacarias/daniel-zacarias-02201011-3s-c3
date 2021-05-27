package br.com.bandtec.Danielac3.auxiliares;

public class FilaObj<T> {
    private int tamanho;
    private T[] fila;

    public FilaObj(int tamanho) {
        this.tamanho = 0;
        this.fila =  (T[]) new Object[tamanho];
    }

    public boolean isEmpty(){
        return tamanho == 0;
    }

    public boolean isFull(){
        return tamanho + 1 >= fila.length;
    }

    public void insert(T info){
        if(isFull()){
            return;
        }
        fila[tamanho++] = info;
    }

    public T peek(){
        return fila[0];
    }

    public T pool(){
        if(isEmpty()){
            return null;
        }
        T valor = peek();
        for(int i = 0; i < fila.length-1; i++){
            fila[i] = fila[i+1];
        }
        fila[tamanho--] = null;
        return valor;
    }

    public void exibe(){
        for(int i = 0; i < tamanho; i++){
            System.out.print(fila[i] + "\t");
        }

    }
}
