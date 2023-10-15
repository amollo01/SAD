import java.io.BufferedReader;
import java.io.Reader;

public class Line {
    
    private String line;
    private boolean inputmode; //true = escriure, false = sobreesciure

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
        if(inputmode == true)
            line =  line + (char)carac;
        
        return line;
    }

    
    public String eliminar(){
        

        return line;
    }

    public String borrar(){
        

        return line;
    }
}
