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
    public static final int ENTER =13;
    public static final int ESC = 27;
	public static final int RALLA = 126;
    public static final int BORRAR = 127;
    public static final int DRETA = 1000;
    public static final int ESQUERRA = 1001;
    public static final int INICI = 1002;
    public static final int FIN = 1003;
    public static final int INSERTAR = 1004;
    public static final int ELIMINAR = 1005;
	public static final int RATOLI = 1205;

    public Reader entrada;
    private Line line;
	public int valor;
	Console consola;

	public EditableBufferedReader(Reader entrada){
		super(entrada);
		this.line = new Line();
		setRaw();
		this.consola = new Console();
	}

	public void setRaw() {
		String[] raw = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
		try {
			Runtime.getRuntime().exec(raw);	
			System.out.print("\033[?1000h"); //activa mode cursor

		} catch (IOException excep) {
			System.out.println(excep);
		}
	}

	public void unsetRaw() {
		String[] cooked = {"/bin/sh", "-c", "stty -echo cooked </dev/tty"};
		try {	
			Runtime.getRuntime().exec(cooked);
			System.out.print("\033[?1000l"); //desactiva mode cursor
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
				int car4 = 0;
               switch(car3) {
				  case 'M': //event ratoli
					int button = super.read();
               	 	int x = super.read() - 32;
                	int y = super.read() - 32;
					consola.fin(x);
					line.setPos(x);
					valor=RATOLI;
					break;
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
				  	car4 = super.read();
				  	if(car4 == RALLA)
						valor = INSERTAR;
				 	else
						valor = car4;
                	  break;
                  case '3':
                	car4 = super.read();
				  	if(car4 == RALLA)
						valor = ELIMINAR;
				 	else
						valor = car4;
                	  break;	
               }
            }
        }
		return valor;
	}
	
	@Override
	public String readLine() {
		try {
			while(true) {
				int carac = read();
				switch(carac) {
					case ENTER:
						return line.getLine();
					case DRETA:
						//System.out.print("\u001b[1C");
						consola.dreta();
						line.dreta();
						break;	
					case ESQUERRA:
						//System.out.print("\u001b[1D");
						consola.esquerra();
						line.esquerra();
						break;	
					case FIN:
						line.getLength();
						line.setPos(line.getLength());
						consola.fin(line.getLength()+1);
						//System.out.print("\u001b[" + (line.getLength() + 1) + "G");
						break;
					case INICI:
						line.setPos(0);
                	  	//System.out.print("\u001b[1G"); 
						consola.inici();
						break;
					case INSERTAR:
						line.changeInputMode();
						break;
					case BORRAR: //backspace
						if(line.getPos() > 1) {
							line.borrar();
							consola.borrar();
							//System.out.print("\033[D");
							//System.out.print("\033[P");
						}
						break;
					case ELIMINAR: //delete
						if(line.getPos() > 0) {
							consola.eliminar();
							//System.out.print("\033[P");
							line.eliminar();
						}
						break;
						case RATOLI:
							break;
					default:
						line.escriure((char)carac);
						line.dreta();
						consola.dflt(line.getLine(), line.getPos());
						//System.out.print("\u001b[2J");
						//System.out.print("\033[2K\033[1G");
						//System.out.print(line.getLine());	
						//System.out.print("\033[" + line.getPos() + "G");
						break;
					}
			}
		} catch (IOException IO) {
			System.err.println("Error");
		}
		return null;
	}
}