package mypc.bluetoothreceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
public class MainActivity extends AppCompatActivity  {
    private TextView Tvaccele;
    private BluetoothAdapter bluetoothAdapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Tvaccele.setText(msg.obj.toString());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("数据接收端");
        Tvaccele = (TextView) findViewById(R.id.sensor);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        new AcceptThread().start();
    }
    public class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        private InputStream inputStream;
        private  OutputStream outputStream;
        {
            try{
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("MyBluetooth", UUID.fromString("d7afed6b-43c9-4a36-b63b-d2966aa23a91"));
            }catch (IOException e){
            }
        }
        public void run(){
            try{
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                while (true){
                    byte[] buffer = new byte[100];
                    int count = inputStream.read(buffer);
                    Message message = new Message();
                    message.obj = new String(buffer,0,count,"utf-8");
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
