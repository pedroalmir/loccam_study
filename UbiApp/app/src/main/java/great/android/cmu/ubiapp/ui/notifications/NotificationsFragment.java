package great.android.cmu.ubiapp.ui.notifications;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import great.android.cmu.ubiapp.MainActivity;
import great.android.cmu.ubiapp.R;
import great.android.cmu.ubiapp.utils.Utils;

public class NotificationsFragment extends Fragment {

    private Context myContext;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Sending broadcast...");
            }
        });

        new UpdateContextTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        return root;
    }

    private class UpdateContextTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Utils.sendBroadcast(new ArrayList<String>());
            Long currentTime = System.currentTimeMillis();
            new UDP_Listener().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentTime);
            return null;
        }
    }

    private class UDP_Listener extends AsyncTask<Long, String, Long>{
        @Override
        protected Long doInBackground(Long... inputs) {
            int server_port = 6789;
            byte[] message = new byte[1024];
            try{
                DatagramPacket p = new DatagramPacket(message, message.length);
                DatagramSocket s = new DatagramSocket(server_port);
                s.receive(p);

                String clientMessage = new String(p.getData(), 0, p.getLength());
                String result = "IP " + p.getAddress().toString() + " sent: " + clientMessage;
                System.out.println(result);

                s.close();
                publishProgress(result);
            }catch(Exception e){
                Log.d("UDP_Listener","Error: " + e.toString());
            }
            return System.currentTimeMillis() - inputs[0];
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Toast.makeText(myContext, progress[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Long elapsedTime) {
            super.onPostExecute(elapsedTime);
            Toast.makeText(myContext, elapsedTime.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Elapsed Time: " + elapsedTime);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.myContext = context;
    }
}