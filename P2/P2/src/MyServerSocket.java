import java.net.ServerSocket;
import java.io.IOException;

public class MyServerSocket 
{
    private MySocket socket;
    private ServerSocket serversocket;

    // Constructor que crea un servidor que escolta al port especificat
    public MyServerSocket(int port) 
    {
        try 
        {
            serversocket = new ServerSocket(port);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public MySocket conectar() 
    {
        try 
        {
            socket = new MySocket(serversocket.accept());
            return socket;
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
}