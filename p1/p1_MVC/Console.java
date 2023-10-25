public class Console {
    public void dreta() {
        System.out.print("\u001b[1C");
    }
    public void esquerra() {
        System.out.print("\u001b[1D");
    }
    public void fin(int length){
        System.out.print("\u001b[" + length + "G");
    }
    public void inici(){
        System.out.print("\u001b[1G"); 
    }
    public void borrar(){
        System.out.print("\033[D");
		System.out.print("\033[P");
    }
    public void eliminar(){
        System.out.print("\033[P");
    }
    public void dflt(String line, int pos){
        System.out.print("\u001b[2J");
		System.out.print("\033[2K\033[1G");
		System.out.print(line);	
		System.out.print("\033[" + pos + "G");
    }
}
