package by.test;

import java.awt.*;
import java.awt.Desktop.Action;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App{

    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";;

    private static final String CLIENT_ID = "1032384937901-ei825nub3iv75q13sa64rhmsfherh0pd.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "";

    public static void main( String[] args ) throws IOException, InterruptedException {

        /*if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Action.BROWSE)){
                desktop.browse(URI.create("http://oauth.com"));
            }
        }else {

        }*/

        String redirectUrl = "http://" + InetAddress.getLoopbackAddress().getHostAddress() + ":4546";
        System.out.println("redirect url: " + redirectUrl);

        ServerSocket serverSocket = new ServerSocket(4546);


        String authRequest = String.format("%1$s?response_type=code&scope=openid%%20profile&redirect_uri=%2$s&client_id=%3$s",
                AUTH_URL,
                redirectUrl,
                CLIENT_ID
                );
        System.out.println("auth request:" + authRequest);

        Process browser = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + authRequest);
                //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://oauth.com");
        Socket socket = serverSocket.accept();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String redirectRequest = bufferedReader.readLine();
        if (redirectRequest != null && redirectRequest.length() > 7){
            String query = redirectRequest.substring(6);
            Map<String, String> responseParameters = getQueryMap(query);
            System.out.println("code : " + responseParameters.get("code"));
        }



    }

    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
