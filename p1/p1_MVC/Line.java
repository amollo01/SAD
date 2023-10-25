import java.io.BufferedReader;
import java.io.Reader;

public class Line {
    
    private StringBuilder line;
    private boolean inputmode; //true = escriure, false = sobreesciure
	private int pos=0;

    public Line() {
        line = new StringBuilder();
        inputmode = true;
    }

    public void changeInputMode(){
        inputmode=!inputmode;
    }

    public int getLength(){
        return line.length();
    }

    public String getLine(){
        return line.toString();
    }

    public String escriure(char carac){
        if (inputmode) {
            if(pos==line.length()+1){
                line.append(carac); 
            }else{
                line.insert(pos, carac);
            }
        }else{
            line.replace(pos, pos+1, Character.toString(carac));
        }
        return line.toString();
    }
    
    public String eliminar(){
        line.delete(pos, pos+1);
        return line.toString();
    }

    public String borrar(){
        line.delete(pos-1, pos);
        pos--;
        return line.toString();
    }
    public void dreta(){
        if(pos < line.length())
            pos++;
    }
    public void esquerra(){
        if(pos > 0)
            pos--;
    }

    public int getPos(){
        return pos+1;
    }

    public void setPos(int pos){
        this.pos=pos;
    }
}
