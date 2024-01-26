package ProdCons;

public abstract class ProdConsBuffer implements IProdConsBuffer {

    protected Message[] buffer; // Le buffer en lui même
    protected int nbMessage; // Le nombre de message actuellement dans le buffer
    protected int totalNbMessage; // Le nombre de message qui sont passé dans le buffer ou sont actuellement dans
                                  // le buffer

    protected int in; // L'index pour insérer le prochaine message dans le buffer
    protected int out; // L'index de lecture du prochaine message dans le buffer

    protected int size; // La taille du buffer

    protected int maxProdTime; // Le temps de production maximale (le minimale étant 0)
    protected int maxConsTime; // Le temps de consommation maximale (le minimale étant 0)

    /*
     * Parameter :
     * - size : The size of the buffer
     */
    public ProdConsBuffer(int size) {
        this.buffer = new Message[size];
        this.nbMessage = 0;
        this.in = 0;
        this.out = 0;
        this.size = size;
    }

    public abstract void put(Message m) throws InterruptedException;

    public abstract Message get() throws InterruptedException;

    public int nmsg() {
        return this.nbMessage;
    }

    public int totmsg() {
        return this.totalNbMessage;
    }

    public void put(Message m, int n) {
    }

    public int getSize() {
        return size;
    }
}
