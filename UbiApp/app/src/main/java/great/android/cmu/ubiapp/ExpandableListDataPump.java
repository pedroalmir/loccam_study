package great.android.cmu.ubiapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    static List<Device> coapDevicesList = new ArrayList<>();

    public static void setDevicesList(Device device){
        coapDevicesList.add(device);
        System.out.println("device que entrou no expandable" + device);
        System.out.println("setou dados");

    }

    public static void setDevicesList(List<Device> listOfDevices){
        coapDevicesList.addAll(listOfDevices);
        for(int i=0;i<coapDevicesList.size();i++) {
            System.out.println(listOfDevices.get(i).getResourceType());


        }
        System.out.println("setou dados");
    }


    public static HashMap<String, List<String>> getData() {
        System.out.println("entrou no get");
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        System.out.println("entrou no get");


        List<String> myDevices = new ArrayList<String>();
        System.out.println("entrou no get");

        for(int i=0;i<coapDevicesList.size();i++) {
            System.out.println("Dispositivo que chegou: " + coapDevicesList.get(i).getResourceType());
            myDevices.add(coapDevicesList.get(i).getResourceType());


        }


//        myDevices.add( "Air Conditioning - Room 1");
//        myDevices.add("Philips Lamb - Room 1");
//        myDevices.add("Smart Doors - Room 2");


        List<String> myEnvironment = new ArrayList<String>();
        myEnvironment.add("Sala NLP");
        myEnvironment.add("Lab Aula");
        myEnvironment.add("Laboratório de Pesquisa - 1 º Andar");

//        List<String> basketball = new ArrayList<String>();
//        basketball.add("United States");
//        basketball.add("Spain");
//        basketball.add("Argentina");
//        basketball.add("France");
//        basketball.add("Russia");

        expandableListDetail.put("GENERAL DEVICES", myDevices);
        expandableListDetail.put("MY ENVIRONMENT", myEnvironment);
//        expandableListDetail.put("BASKETBALL TEAMS", basketball);
        return expandableListDetail;
    }
}