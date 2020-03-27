import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException
    {
        try
        {
            Scanner scn = new Scanner(System.in);


            byte b[] = new byte[20002];

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket socket_callServer = new Socket(ip, 5056);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(socket_callServer.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket_callServer.getOutputStream());


            InputStream is =socket_callServer.getInputStream();//Wat we hierr doen is


            // the following loop performs the exchange of
            // information between client and client handler
            while (true)
            {

                System.out.println(dis.readUTF());
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);

                //is.read(b,0,b.length);




                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit"))
                {
                    FileOutputStream fileoutput = new FileOutputStream("OutputFile.txt");
                    is.read(b,0,b.length);
                    fileoutput.write(b,0,b.length);

                    System.out.println("Closing this connection : " + socket_callServer);
                    socket_callServer.close();
                    System.out.println("Connection closed");


                    break;
                }

                // printing date or time as requested by client
                String received = dis.readUTF();
                System.out.println(received);
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
