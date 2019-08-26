import java.net.*;
import java.io.*;

public class GameServer{
    private final ServerSocket server;
    private static final String index_page = "../htmlCodes/index.html";

    public GameServer(int port) throws IOException{
        server = new ServerSocket(port);
    }

    private void run(){
        java.net.Socket socket = null;

        try{
            socket = server.accept();
            systemon(socket);
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            if(socket != null)
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }


    }

    private void systemon(java.net.Socket socket){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

             BufferedReader htmlCode = new BufferedReader(new FileReader(index_page));

            //content is the content of the html file
            String content = "";

            while( htmlCode.ready()){
                content += htmlCode.readLine();
                content += "\n";
            }

            htmlCode.close();
            //for getting inputs from the user
            String input = in.readLine();
            System.out.println(input);

                out.println("HTTP/1.1 \nContent-Type: text/html\n\r\n");
                out.println(content);
                out.flush();





        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        }

    public static void main(String[] a) throws IOException{
        GameServer server = new GameServer(8080);
        while(true)
        server.run();
    }
}
