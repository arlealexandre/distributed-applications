package ricm.nio.babystep2_3;

public class Automata {
	
	private ReaderAutomata readerAutomata;
	private WriterAutomata writerAutomata;
	
	public Automata(ReaderAutomata r, WriterAutomata w) {
		this.readerAutomata = r;
		this.writerAutomata = w;
	}
	
	public ReaderAutomata getReaderAutomata() {
		return this.readerAutomata;
	}
	
	public WriterAutomata getWriterAutomata() {
		return this.writerAutomata;
	}
}
