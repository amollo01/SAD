import java.io.BufferedReader;
import java.io.Reader;

public class Line {
    
    private String line;
    private boolean inputmode; //true = escriure, false = sobreesciure
	private int pos=1;

    public Line() {
        line = "";
        inputmode = true;
    }

   
    public void changeInputMode(){
        if(inputmode == true){
            inputmode = false;
        }else{
            inputmode = true;
        }
    }

    public int getLength(){
        return line.length();
    }

    public String getLine(){
        return line;
    }

    public String escriure(int carac){
        if (inputmode) {
            if(pos==line.length()+1){
                StringBuilder sb = new StringBuilder(line);
                sb.append((char)carac); 
                line = sb.toString();
            }else{
                StringBuilder sb = new StringBuilder(line);
            sb.insert(pos, (char)carac); 
            line = sb.toString(); 
            }
            
        }
        return line;
    }
    
    
    public String eliminar(){
        

        return line;
    }

    public String borrar(){
        

        return line;
    }

    public void dreta(){
        if(pos < line.length()){
            pos++;
        }
    }
    public void esquerra(){
        if(pos > 0){
            pos--;
        }
    }

    public int getPos(){
        return pos;
    }
}
