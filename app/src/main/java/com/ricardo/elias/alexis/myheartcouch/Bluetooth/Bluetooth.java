package com.ricardo.elias.alexis.myheartcouch.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Bluetooth {

    private static final String TAG = "BluetoothService";
    private static final boolean D = true;
    private static final String NAME = "BluetoothChat";
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    /**
     * Constructor de esta clase
     *
     * @param handler necesita un handler , que es la manera en la que se tendré
     *                contacto con la interfaz de usuario posteriormente
     */
    public Bluetooth( Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        //if (D) for (BluetoothDevice bd : mAdapter.getBondedDevices())
        //    Log.d(TAG, "Bounded device " + bd);
        mState = STATE_NONE;
        mHandler = handler;
    }

    /**
     * Le da un estado al handler , mismo que se usará para
     * comunicarse con la clase que contenga el thread de la interfaz de usuario
     *
     * @param state Alguno de los estados definidos en esta clase
     */
    private synchronized void setState(int state) {
        if (D)
            Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
        // Le da un estado al handler para que la interfaz de usuario lo
        //maneje posteriormente
        mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1)
                .sendToTarget();
    }

    /**
     * Getter del estado
     *
     * @return el estado actual
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Inicia la conectividad
     */
    public synchronized void start() {
        if (D)
            Log.d(TAG, "start");
        //Cancela cualquier thread que intente crear una conexion
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        //Cancela cualcquier thread que actualmente maneje la conexion
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_LISTEN);
        //Inicia el thread que estará escuchando el BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    /**
     * 2)
     * Se conecta a un dispositivo bluetooth
     * Inicia un nuevo Connect Thread y se cambia el estado general a
     * "Estado Conectado"
     *
     * @param device el dispositivo bluetooth al que se desea conectar
     */
    private synchronized void connect(BluetoothDevice device) {
        if (D)
            Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        //Cancela cualquier thread en una conexion atualmente
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        //Inicia una conexion dado el dispositvo
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }


    /**
     * 5)
     * Este metodo lo manda a llamar el COnnectThread dentro de run() ,
     * Cambia el estado de esta clase a conectado
     *
     * @param socket     el socket al que se va a conectar
     * @param device     el dispositivo
     * @param socketType
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socketType) {
        if (D)
            Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancela cualquier thread que completó la conexión
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancela cualquiera que esté corriendo una conexión
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        //Cancela el thread de aceptación porque nosotros solo queremos conectar
        //a un dispositivo
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        //Inicia un thread para manejar la conexión y realizar transmisiones
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        //Manda un mensaje con el nombre del dispositivo conectado a la interfaz de usuario
        Message msg = mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString("Connected", device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }

    /**
     * Detiene todos los threads
     */
    public synchronized void stop() {
        if (D)
            Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        setState(STATE_NONE);
    }

    /**
     * Sincroniza una copia de este thread
     * @param out
     */
    private void write(byte[] out) {
        // Crea un objeto temporal
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        // Sincroniza una copia de este thread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }


    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("Toast", "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Start the service over to restart listening mode
        Bluetooth.this.start();
    }

    /**
     * Le regresa un mensaje a la actividad de que la conexión se ha perdido
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("Toast", "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Start the service over to restart listening mode
        Bluetooth.this.start();
    }


    /**
     *
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (D)
                Log.d(TAG, "Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);
            BluetoothSocket socket = null;
            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + mSocketType
                            + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (Bluetooth.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice(),
                                        mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate
                                // new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            if (D)
                Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

        }

        public void cancel() {
            if (D)
                Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType
                        + "close() of server failed", e);
            }
        }
    }

    /**
     * Thread que inicializa la conexion
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        /**
         * 3)
         * Inicializa un thread de este tipo , se obtiene un Bluetooth socket dado un
         * Bluetooth device, se crea este thread en conn
         *
         * @param device un Bluetoothdevice, se construye a partir del nombe del dispositivo
         */
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            //Obtiene un BluetoothSocket dado el BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }

        /**
         * 4)
         * Manda a crear un nuevo thread de conectado
         */
        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);
            //Siempre escondiendo descubrir porque puede alentar la conexion
            mAdapter.cancelDiscovery();
            //Hace una conexion al bluetooth socket
            try {
                //Este bloque regresa una conexion exitosa o fallida
                mmSocket.connect();
            } catch (IOException e) {
                Log.e(TAG, "Unable to connect socket ", e);
                //Cierra el socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType + " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }
            // Reset the ConnectThread because we're done
            synchronized (Bluetooth.this) {
                mConnectThread = null;
            }
            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType
                        + " socket failed", e);
            }
        }
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        /**
         * 6) Inicializa un thread que manejará la conexión
         *
         * @param socket
         * @param socketType
         */
        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        /**
         * 7)
         * Empieza a escuchar los mensajes Provenientes del dispositivo conectado y envia los
         * resultados al thread de la interfaz de usuario
         */
        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            //byte[] buffer = new byte[1024];
            byte[] buffer = new byte[1024];
            int bytes;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    //Log.d(TAG, "mensaje bytes " + bytes);
                    //Log.d(TAG, "mensaje buffer " + new String(buffer));
                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "desconectado", e);
                    connectionLost();
                    // Start the service over to restart listening mode
                    Bluetooth.this.start();
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(MESSAGE_WRITE, -1, -1,
                        buffer).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }


    public void sendMessage(String message) {
        //Verifica si estamos conectados antes de hacer algo
        if (this.getState() != Bluetooth.STATE_CONNECTED) {
            Log.w(TAG, "bluetooth is not connected");
            return;        }
        //Verificamos si hay algo que enviar
        if (message.length() > 0) {
            char EOT = (char) 3;
            // Get the message bytes and tell the BluetoothChatService to write
            //Obtenemos los bytes del mensaje y mandamos a llamar a write
            byte[] send = (message + EOT).getBytes();
            this.write(send);
        }
    }

    public void sendMessageWithInte(int message) {

        {
            char EOT = (char) 3;

            byte[] bytes = {(byte) (message & 0xff)};
            this.write(bytes);
        }


    }

    /**
     * 1)
     * Se Intentará conectar a un dispositivo dado su nombre
     * Crea un nuevo BluetoothDevice y con esto se manda a llamar al
     * método connect ya con el dispositivo creado
     *
     * @param deviceName el nombre del dispositivoa al que se desea conectar
     */
    public void connectDevice(String deviceName) {
        //Obtiene la dirección MAC
        String address = null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        for (BluetoothDevice d : adapter.getBondedDevices()) {
            if (d.getName().equals(deviceName)) address = d.getAddress();
        }
        try {
            //Trata de obtener el ebjeto  BluetoothDevice e intenta conectarse a él
            BluetoothDevice device = adapter.getRemoteDevice(address);
            this.connect(device);
        } catch (Exception e) {

        }
    }
}
