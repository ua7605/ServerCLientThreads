import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();



                byte b[] = new byte[20002];

                OutputStream outputStream = s.getOutputStream();


                FileInputStream file = new FileInputStream( "src/file_on_Server.txt");

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos,outputStream,b);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

}


// ClientHandler class
class ClientHandler extends Thread
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    OutputStream outputStream;
    byte[] b;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos,OutputStream outputStream,byte[] b)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.outputStream = outputStream;
        this.b = b;

    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {

                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time+file]..\n"+
                        "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();


                if(received.equals("Exit"))
                {
                    outputStream.write(b,0,b.length);

                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    outputStream.write(b,0,b.length);
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                switch (received) {

                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time" :

                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);


                        FileInputStream file = new FileInputStream( "src/file_on_Server.txt");
                        file.read(b,0,b.length);//Hier lezen we de file van uit de server.




                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

