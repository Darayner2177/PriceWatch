package cs4330.cs.utep.edu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckNetworkStatus extends BroadcastReceiver{

    public CheckConnection checkConnection;
    private boolean check;

    public CheckNetworkStatus(){
    }

    public interface CheckConnection{
        void connection(boolean isConnection);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn =  (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        check = (networkInfo != null) && (networkInfo.isConnectedOrConnecting());

        checkConnection.connection(check);

    }


}
