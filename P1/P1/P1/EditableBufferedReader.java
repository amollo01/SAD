package P1;
import java.io.*;

/*
 *  UP -> \ESC[A
 *  DOWN -> \ESC[B
 *  LEFT -> \ESC[D
 *  RIGHT -> \ESC[C
 *  HOME -> \ESC[H
 *  END-> \ESC[F , \ESC[4~ , \ESC[8~
 *  INSERT -> \ESC[2~
 *  DELETE -> \ESC[3~
 *  BKSP -> 
 */
public class EditableBufferedReader extends BufferedReader {
	
	
	public EditableBufferedReader(Reader in){
		super(in);
		setRaw();
	}
	
	
	public void setRaw() {
		String[] raw = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
		try {
			
			Runtime.getRuntime().exec(raw);
			
		} catch (IOException excep) {
			System.out.println(excep);
		}
	}
	
	public void unsetRaw() {
		String[] cooked = {"/bin/sh", "-c", "stty -echo cooked </dev/tty"};

		try {
			
			Runtime.getRuntime().exec(cooked);
			
		} catch (IOException excep) {
			System.out.println(excep);
		}
		
	}
	
	
	public int read() {
		
		
		
		
		
		
		
	}
	
	public String readLine() {
		
		
	}
	
	
	
}
