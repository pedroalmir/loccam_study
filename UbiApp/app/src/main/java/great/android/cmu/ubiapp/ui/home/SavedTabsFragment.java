package great.android.cmu.ubiapp.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.serialization.DataParser;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.fragment.app.Fragment;

import great.android.cmu.ubiapp.CustomExpandableListAdapter;
import great.android.cmu.ubiapp.Device;
import great.android.cmu.ubiapp.ExpandableListDataPump;
import great.android.cmu.ubiapp.R;

public class SavedTabsFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    List<Device> coapDevicesList = new ArrayList<Device>();



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_list, null);
//        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.expandableListView);
//        elv.setAdapter(new SavedTabsListAdapter());
//        return v;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_list, null);
        //ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);


        test();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                setTheRest();
            }
        }, 5000);


        //ExpandableListDataPump.setDevicesList(coapDevicesList);





        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                //AQUI FICA O CLIQUE NO FILHO DA LISTA
//                Toast.makeText(
//                        getContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                return false;
            }
        });


        return v;
    }


    public void test(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

//force
                final CoapClient client = new CoapClient("coap://18.229.202.214:5683/.well-known/core");
                client.get(new CoapHandler() {
                    @Override
                    public void onLoad(CoapResponse response) {
                        if(response != null){
                            String[] devices = response.advanced().getPayloadString().split(",");
                            for(int i = 1; i < devices.length; i++){
                                String[] params = devices[i].split(";");
                                Device device = new Device();
                                LinkedHashMap<String, String> context = new LinkedHashMap<>();
                                for(int p = 0; p < params.length; p++){
                                    if(p == 0) device.setUid(params[p].split("/")[1].split(">")[0]);
                                    else{
                                        String[] keyValue = params[p].split("=");
                                        if(keyValue[0].equals("ct")) device.setContextType(Integer.valueOf(keyValue[1]));
                                        else if(keyValue[0].equals("rt")) device.setResourceType(keyValue[1]);
                                        else if(keyValue[0].equals("if")) device.setType(keyValue[1]);
                                        else if(keyValue[0].equals("ip")) device.setIp(keyValue[1]);
                                        else context.put(keyValue[0], keyValue[1]);
                                    }
                                }
                                device.setContext(context);
                                coapDevicesList.add(device);
                                ExpandableListDataPump.setDevicesList(device);
                            }

                        }
                    }

                    @Override
                    public void onError() {

                        System.out.println("Exception");

                    }
                });


            }
        });
    }

    public void setTheRest(){
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }




























//
//    public class SavedTabsListAdapter extends BaseExpandableListAdapter {
//
//        private String[] groups = { "Basquete", "Futebol"};
//
//        private String[][] children = {
//                { "India", "Pakistan", "Australia", "England" },
//                { "Brazil", "Spain", "Germany", "Netherlands" }
//        };
//
//        @Override
//        public int getGroupCount() {
//            return groups.length;
//        }
//
//        @Override
//        public int getChildrenCount(int i) {
//            return children[i].length;
//        }
//
//        @Override
//        public Object getGroup(int i) {
//            return groups[i];
//        }
//
//        @Override
//        public Object getChild(int i, int i1) {
//            return children[i][i1];
//        }
//
//        @Override
//        public long getGroupId(int i) {
//            return i;
//        }
//
//        @Override
//        public long getChildId(int i, int i1) {
//            return i1;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        @Override
//        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//            TextView textView = new TextView(SavedTabsFragment.this.getActivity());
//            textView.setText(getGroup(i).toString());
//            return textView;
//        }
//
//        @Override
//        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//            TextView textView = new TextView(SavedTabsFragment.this.getActivity());
//            textView.setText(getChild(i, i1).toString());
//            return textView;
//        }
//
//        @Override
//        public boolean isChildSelectable(int i, int i1) {
//            return true;
//        }
//
//    }

}