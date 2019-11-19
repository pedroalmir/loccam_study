package great.android.cmu.ubiapp.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import great.android.cmu.ubiapp.model.DeviceAction;
import great.android.cmu.ubiapp.model.DeviceActionMessage;

public class Utils {

    public final static int UDP_OUT_PORT = 4445;
    public final static int UDP_IN_PORT = 54809;

    public static void sendBroadcast(List<String> ips){
        Gson gson = new Gson();
        DeviceAction vibrate = new DeviceAction("vibrate", 2000);
        DeviceAction light = new DeviceAction("light", 2000);
        List<DeviceAction> actions = new ArrayList<DeviceAction>();
        actions.add(vibrate); actions.add(light);

        DeviceActionMessage deviceActionMessage = new DeviceActionMessage("CMU-2019", actions, ips);

        try {
            broadcast(gson.toJson(deviceActionMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String broadcastMessage) throws IOException {
        DatagramSocket socket = null;

        socket = new DatagramSocket(UDP_IN_PORT);
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), UDP_OUT_PORT);
        socket.send(packet);
        socket.close();
    }

}
