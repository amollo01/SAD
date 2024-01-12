import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Client 
{
    public static void main(String[] args) 
    {
        // Establim una connexió amb el servidor al localhost i el port 55555
        MySocket socket = new MySocket("localhost", 55555);

        // Thread per llegir les respostes del servidor i mostrar-les a la consola
        new Thread() 
        {
            public void run() 
            {
                String linea;
                while ((linea = socket.llegirLinea()) != null) 
                {
                    System.out.println(linea);
                }
            }
        }.start();

        // Thread per llegir l'entrada de l'usuari i enviar-la al servidor
        new Thread() 
        {
            public void run() 
            {
                String linea;
                try 
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    while ((linea = in.readLine()) != null) 
                    {
                        socket.escriureLinea(linea);
                    }
                    socket.tancar();
                } catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }.start();  
    }
}