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
	
	

	public static final int ESCAPE = '\033';
	public  int valor;
	
    public static final int ENTER =13;
    public static final int ESC = 27;
    public static final int BORRAR = 127;
    public static final int DRETA = 1000;
    public static final int ESQUERRA = 1001;
    public static final int INICI = 1002;
    public static final int FIN = 1003;
    public static final int INSERTAR = 1004;
    public static final int ELIMINAR = 1005;
    
    public Reader entrada;
    private Line line;

	
	public EditableBufferedReader(Reader entrada){
		super(entrada);
		this.line = new Line();
		setRaw();

	}
	
	
	public void setRaw() {
		String[] raw = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
		try {
			//System.out.println("");
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
	
	
	public int read() throws IOException {
		
		setRaw();
		
		int car1 = super.read();
		
		if(car1 != ESCAPE){

            valor = car1;
           
        }else{
            
           int car2 = super.read();
           
           if(car2 != '['){

        	   valor = ESC;

            }else{
            	
               int car3 = super.read();

               switch(car3) {

                  case 'C':
                	  valor = DRETA;

                      break;

                  case 'D':
                	  valor = ESQUERRA;
                      break; 

                  case 'F':
                	  valor = FIN;
                      break;  
                      
                  case 'H':
                	  valor = INICI;

                      break;
                  case '2':
                	  valor = INSERTAR;
                	  break;
                  case '3':
                	  valor = ELIMINAR;
                	  break;
                	
               }
            }
        }
		return valor;
	}
	
	@Override
	public String readLine() {
		
		try {
			System.out.print("\033[2K\033[2;1H");
			//int cursor = 1;
			
			while(true) {
				
				int carac = read();
				
				switch(carac) {
					case ENTER:
						return line.getLine();

					case DRETA:
						line.dreta();
						break;
						
					case ESQUERRA:
						line.esquerra();
						break;
						
					case FIN:
						line.getLength();
						System.out.print("\u001b[<" + line.getLength() + ">;<0>H");
						break;
						
					case INICI:
                	  	System.out.print("\u001b[<0>;<0>H");
						break;
						
					case INSERTAR:
						line.changeInputMode();
						break;
						
					case BORRAR:
						line.borrar();
						break;
						
					case ELIMINAR:
						line.eliminar();
						break;
						
					default:
						line.escriure(carac);
						line.dreta();
					}

				System.out.print("\u001b[2J");
				System.out.print("\033[2K\033[1G");
				System.out.print(line.getLine());	
				System.out.print("\033[" + line.getPos() + "G");

	
			}
        	
		} catch (IOException IO) {
			
			System.err.println("Error");
			
		}
		return null;

	}
	
	
}