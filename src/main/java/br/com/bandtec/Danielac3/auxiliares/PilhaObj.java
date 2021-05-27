package br.com.bandtec.Danielac3.auxiliares;

public class PilhaObj <T>{
    private int topo;
    private T[] pilha;

    public PilhaObj(int tam) {
        this.topo = -1;
        this.pilha = (T[]) new Object[tam];
    }

    public boolean isEmpty(){
        return topo == -1;
    }

    public boolean isFull(){
        return topo == pilha.length - 1;
    }

    public void push(T valor){
        if(isFull()){
            return;
        }
        topo+= 1;
        pilha[topo] = valor;

    }

    public T pop(){
        return isEmpty() ? null : pilha[topo--];
    }

    public T peek(){
        return isEmpty() ? null : pilha[topo];
    }
    public void exibe(){
        if(isEmpty()){
            System.out.println("Pilha Vazia");
            return;
        }
        for(int i = topo; i >= 0; i--){
            System.out.print(pilha[i] + "\t");
        }
    }

    public PilhaObj<T> multPop(int n){
        if(n > topo + 1 || n < 0){
            return null;
        }else{
            PilhaObj pilhaAux = new PilhaObj(n);
            for(int i = 0; !pilhaAux.isFull(); i++){
                pilhaAux.push(pop());
            }
            return pilhaAux;
        }
    }

    public void multiPush(PilhaObj<T> aux){
        while (!aux.isEmpty()){
            this.push(aux.pop());
        }
    }
}
