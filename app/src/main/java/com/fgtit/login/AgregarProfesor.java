package com.fgtit.login;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.fgtit.login.model.Model;
import com.fgtit.login.model.Profesor;
import com.fgtit.login.model.api.AbstractAPIListener;
import com.fgtit.reader.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fgtit.data.wsq;
import com.fgtit.fpcore.FPMatch;
import com.fgtit.utils.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class AgregarProfesor extends AppCompatActivity {

    //directory for saving the fingerprint images
    private String sDirectory = "";
    private static final String TAG = "BluetoothReader";

    //default image size
    public static final int IMG_WIDTH = 256;
    public static final int IMG_HEIGHT = 288;
    public static final int IMG_SIZE = IMG_WIDTH * IMG_HEIGHT;
    public static final int WSQBUFSIZE = 200000;

    //other image size
    public static final int IMG200 = 200;
    public static final int IMG288 = 288;
    public static final int IMG360 = 360;

    //definition of commands
    private final static byte CMD_PASSWORD = 0x01;    //Password
    private final static byte CMD_ENROLID = 0x02;        //Enroll in Device
    private final static byte CMD_VERIFY = 0x03;        //Verify in Device
    private final static byte CMD_IDENTIFY = 0x04;    //Identify in Device
    private final static byte CMD_DELETEID = 0x05;    //Delete in Device
    private final static byte CMD_CLEARID = 0x06;        //Clear in Device

    private final static byte CMD_ENROLHOST = 0x07;    //Enroll to Host
    private final static byte CMD_CAPTUREHOST = 0x08;    //Caputre to Host
    private final static byte CMD_MATCH = 0x09;        //Match
    private final static byte CMD_GETIMAGE = 0x30;      //GETIMAGE
    private final static byte CMD_GETIMAGE2 = 0x50;      //GETIMAGE
    private final static byte CMD_GETCHAR = 0x31;       //GETDATA


    private final static byte CMD_WRITEFPCARD = 0x0A;    //Write Card Data
    private final static byte CMD_READFPCARD = 0x0B;    //Read Card Data
    private final static byte CMD_CARDSN = 0x55;        //Read Card Sn
    private final static byte CMD_GETSN = 0x10;

    private final static byte CMD_FPCARDMATCH = 0x13;   //

    private final static byte CMD_WRITEDATACARD = 0x14;    //Write Card Data
    private final static byte CMD_READDATACARD = 0x15;     //Read Card Data

    private final static byte CMD_PRINTCMD = 0x20;        //Printer Print
    private final static byte CMD_GETBAT = 0x21;        //get battery info
    private final static byte CMD_GETBAT_NEW = (byte) 0x21; //get battery info
    private final static byte CMD_UPCARDSN = 0x43;          //get device SN
    private final static byte CMD_GET_VERSION = 0x22;        //get Version

    private byte mDeviceCmd = 0x00;
    private boolean mIsWork = false;
    private byte mCmdData[] = new byte[10240];
    private int mCmdSize = 0;

    private Timer mTimerTimeout = null;
    private TimerTask mTaskTimeout = null;
    private Handler mHandlerTimeout;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private ListView mConversationView;
    private ImageView fingerprintImage;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothReaderService mChatService = null;

    //definition of variables which used for storing the fingerprint template
    public byte mRefData[] = new byte[512]; //enrolled FP template data
    public int mRefSize = 0;
    public byte mMatData[] = new byte[512];  // match FP template data
    public int mMatSize = 0;
    //Definicion local de las plantillas de las huellas para ser almacenadas
    public  byte plantillaHuella1[] = new byte[512];
    public  byte plantillaHuella2[] = new byte[512];

    public byte mCardSn[] = new byte[7];
    public byte mCardData[] = new byte[4096];
    public int mCardSize = 0;

    public byte mBat[] = new byte[2];  // data of battery status
    public byte mUpImage[] = new byte[73728]; // image data
    public byte mUpImage2[] = new byte[73728]; // image data
    public int mUpImageSize = 0;
    public int mUpImageCount = 0;


    public byte mRefCoord[] = new byte[512];
    public byte mMatCoord[] = new byte[512];

    public byte mIsoData[] = new byte[378];
    private byte lowHighByte;
    private Toolbar mToolbar;
    private TextView textSize;
    private int imgSize;

    private int userId; // User ID number
    private SQLiteDatabase userDB; //SQLite database object

    //Contador de huellas
    private  int contHuella;

    //dynamic setting of the permission for writing the data into phone memory
    private int REQUEST_PERMISSION_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    Button bSaveProfesor;

    private String huella1 = "";
    private String huella2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the window layout
        setContentView(R.layout.addprofesor);

        //checking the permission
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                        REQUEST_PERMISSION_CODE);
            }
        }

        CreateDirectory();

        final EditText txtNombreP = findViewById(R.id.txtNombreP);
        final EditText txtApellidoPP = findViewById(R.id.txtApellidoPP);
        final EditText txtApellidoMP = findViewById(R.id.txtApellidoMP);
        final EditText txtNoP = findViewById(R.id.txtNoP);
        bSaveProfesor = findViewById(R.id.bSaveProfesor);

        textSize = (TextView) findViewById(R.id.textSize2);
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("Lector Bluetooth");
        mToolbar.setSubtitle("no conectado");
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(onMenuItemClick);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no disponible", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //initialize the match function of the fingerprint
        int feedback = FPMatch.getInstance().InitMatch(1, "https://www.hfteco.com/");
        if (feedback == 0) {
            Toast.makeText(this, "Init Match ok", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Init Match failed", Toast.LENGTH_SHORT).show();
        }

        //initialize the SQLite
        userId = 1;
        contHuella = 0;
        DBHelper userDBHelper = new DBHelper(this);
        userDB = userDBHelper.getWritableDatabase();

        bSaveProfesor.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String nombre = txtNombreP.getText().toString();
                String apellidoP = txtApellidoPP.getText().toString();
                String apellidoM = txtApellidoMP.getText().toString();
                String noT = txtNoP.getText().toString();

                final Model model = Model.getInstance(AgregarProfesor.this.getApplication());
                model.profesor(nombre, apellidoP, apellidoM, noT, huella1, huella2, new AbstractAPIListener() {
                    @Override
                    public void onProfesor(Profesor profesor) {
                        if (profesor != null) {
                            model.setProfesor(profesor);

                            Toast.makeText(AgregarProfesor.this, "ID: " + profesor.getId() + "(" + profesor.getNombre() +" "+ profesor.getApellidoP() +" "+ profesor.getApellidoM()+")", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(AgregarProfesor.this, "Invalid login", Toast.LENGTH_LONG).show();
                        }
                    }

                } );

                userId = 1;
                userDB.delete(DBHelper.TABLE_USER, null, null);
                userDB.execSQL("update sqlite_sequence set seq=0 where name='User'");

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothReaderService.STATE_CONNECTED:
                            mToolbar.setSubtitle(mConnectedDeviceName);
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothReaderService.STATE_CONNECTING:
                            mToolbar.setSubtitle(R.string.title_connecting);
                            break;
                        case BluetoothReaderService.STATE_LISTEN:
                        case BluetoothReaderService.STATE_NONE:
                            mToolbar.setSubtitle(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    if (readBuf.length > 0) {
                        if (readBuf[0] == (byte) 0x1b) {
                            AddStatusListHex(readBuf, msg.arg1);
                        } else {
                            ReceiveCommand(readBuf, msg.arg1);
                        }
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public synchronized void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothReaderService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }


    private void AddStatusList(String text) {
        mConversationArrayAdapter.add(text);
    }

    private void AddStatusListHex(byte[] data, int size) {
        String text = "";
        for (int i = 0; i < size; i++) {
            text = text + " " + Integer.toHexString(data[i] & 0xFF).toUpperCase() + "  ";
        }
        mConversationArrayAdapter.add(text);
    }

    /**
     * method of copying the byte[] data with specific length
     * @param dstbuf byte[] for storing the copied data with specific length
     * @param dstoffset the starting point for storing
     * @param srcbuf the source byte[] used for copying.
     * @param srcoffset the starting point for copying
     * @param size the length required to copy
     */
    private void memcpy(byte[] dstbuf, int dstoffset, byte[] srcbuf, int srcoffset, int size) {
        for (int i = 0; i < size; i++) {
            dstbuf[dstoffset + i] = srcbuf[srcoffset + i];
        }
    }

    /**
     * calculate the check sum of the byte[]
     * @param buffer byte[] required for calculating
     * @param size the size of the byte[]
     * @return the calculated check sum
     */
    private int calcCheckSum(byte[] buffer, int size) {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum = sum + buffer[i];
        }
        return (sum & 0x00ff);
    }

    private byte[] changeByte(int data) {
        byte b4 = (byte) ((data) >> 24);
        byte b3 = (byte) (((data) << 8) >> 24);
        byte b2 = (byte) (((data) << 16) >> 24);
        byte b1 = (byte) (((data) << 24) >> 24);
        byte[] bytes = {b1, b2, b3, b4};
        return bytes;
    }


    /**
     * generate the image data into Bitmap format
     * @param width width of the image
     * @param height height of the image
     * @param data image data
     * @return bitmap image data
     */
    private byte[] toBmpByte(int width, int height, byte[] data) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            int bfType = 0x424d;
            int bfSize = 54 + 1024 + width * height;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            int bfOffBits = 54 + 1024;

            dos.writeShort(bfType);
            dos.write(changeByte(bfSize), 0, 4);
            dos.write(changeByte(bfReserved1), 0, 2);
            dos.write(changeByte(bfReserved2), 0, 2);
            dos.write(changeByte(bfOffBits), 0, 4);

            int biSize = 40;
            int biWidth = width;
            int biHeight = height;
            int biPlanes = 1;
            int biBitcount = 8;
            int biCompression = 0;
            int biSizeImage = width * height;
            int biXPelsPerMeter = 0;
            int biYPelsPerMeter = 0;
            int biClrUsed = 256;
            int biClrImportant = 0;

            dos.write(changeByte(biSize), 0, 4);
            dos.write(changeByte(biWidth), 0, 4);
            dos.write(changeByte(biHeight), 0, 4);
            dos.write(changeByte(biPlanes), 0, 2);
            dos.write(changeByte(biBitcount), 0, 2);
            dos.write(changeByte(biCompression), 0, 4);
            dos.write(changeByte(biSizeImage), 0, 4);
            dos.write(changeByte(biXPelsPerMeter), 0, 4);
            dos.write(changeByte(biYPelsPerMeter), 0, 4);
            dos.write(changeByte(biClrUsed), 0, 4);
            dos.write(changeByte(biClrImportant), 0, 4);

            byte[] palatte = new byte[1024];
            for (int i = 0; i < 256; i++) {
                palatte[i * 4] = (byte) i;
                palatte[i * 4 + 1] = (byte) i;
                palatte[i * 4 + 2] = (byte) i;
                palatte[i * 4 + 3] = 0;
            }
            dos.write(palatte);

            dos.write(data);
            dos.flush();
            buffer = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * generate the fingerprint image
     * @param data image data
     * @param width width of the image
     * @param height height of the image
     * @param offset default setting as 0
     * @return bitmap image data
     */
    public byte[] getFingerprintImage(byte[] data, int width, int height, int offset) {
        if (data == null) {
            return null;
        }
        byte[] imageData = new byte[width * height];
        for (int i = 0; i < (width * height / 2); i++) {
            imageData[i * 2] = (byte) (data[i + offset] & 0xf0);
            imageData[i * 2 + 1] = (byte) (data[i + offset] << 4 & 0xf0);
        }
        byte[] bmpData = toBmpByte(width, height, imageData);
        return bmpData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
    }

    /**
     * configure for the UI components
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        fingerprintImage = (ImageView) findViewById(R.id.imageView1);


        final Button mButton6 = (Button) findViewById(R.id.bBuscarHuella);
        mButton6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendCommand(CMD_ENROLHOST, null, 0);
            }
        });

        final Button mButton14 = (Button) findViewById(R.id.bDeviceSN);
        mButton14.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendCommand(CMD_GETSN, null, 0);
            }
        });

        final Button mButton15 = (Button) findViewById(R.id.bDeviceBattery);
        mButton15.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SendCommand(CMD_GETBAT, null, 0);
            }
        });

        mChatService = new BluetoothReaderService(this, mHandler);    // Initialize the BluetoothChatService to perform bluetooth connections
        mOutStringBuffer = new StringBuffer("");                    // Initialize the buffer for outgoing messages
    }

    /**
     * stat the timer for counting
     */
    public void TimeOutStart() {
        if (mTimerTimeout != null) {
            return;
        }
        mTimerTimeout = new Timer();
        mHandlerTimeout = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                TimeOutStop();
                if (mIsWork) {
                    mIsWork = false;
                    //AddStatusList("Time Out");
                }
                super.handleMessage(msg);
            }
        };
        mTaskTimeout = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandlerTimeout.sendMessage(message);
            }
        };
        mTimerTimeout.schedule(mTaskTimeout, 10000, 10000);
    }

    /**
     * stop the timer
     */
    public void TimeOutStop() {
        if (mTimerTimeout != null) {
            mTimerTimeout.cancel();
            mTimerTimeout = null;
            mTaskTimeout.cancel();
            mTaskTimeout = null;
        }
    }

    /**
     * Generate the command package sending via bluetooth
     * @param cmdid command code for different function achieve.
     * @param data the required data need to send to the device
     * @param size the size of the byte[] data
     */
    private void SendCommand(byte cmdid, byte[] data, int size) {
        if (mIsWork) return;

        int sendsize = 9 + size;
        byte[] sendbuf = new byte[sendsize];
        sendbuf[0] = 'F';
        sendbuf[1] = 'T';
        sendbuf[2] = 0;
        sendbuf[3] = 0;
        sendbuf[4] = cmdid;
        sendbuf[5] = (byte) (size);
        sendbuf[6] = (byte) (size >> 8);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sendbuf[7 + i] = data[i];
            }
        }
        int sum = calcCheckSum(sendbuf, (7 + size));
        sendbuf[7 + size] = (byte) (sum);
        sendbuf[8 + size] = (byte) (sum >> 8);

        mIsWork = true;
        TimeOutStart();
        mDeviceCmd = cmdid;
        mCmdSize = 0;
        mChatService.write(sendbuf);

        switch (sendbuf[4]) {
            case CMD_PASSWORD:
                break;
            case CMD_ENROLHOST:
                AddStatusList("Enrolando Plantilla ...");
                break;
            case CMD_GETSN:
                AddStatusList("Get Device SN ...");
                break;
            case CMD_GETBAT:
                AddStatusList("Get Battery Value ...");
                break;
        }
    }

    /**
     * Received the response from the device
     * @param databuf the data package response from the device
     * @param datasize the size of the data package
     */
    private void ReceiveCommand(byte[] databuf, int datasize) {
        if (mDeviceCmd == CMD_GETIMAGE) { //receiving the image data from the device
            if (imgSize == IMG200) {   //image size with 152*200
                memcpy(mUpImage, mUpImageSize, databuf, 0, datasize);
                mUpImageSize = mUpImageSize + datasize;
                if (mUpImageSize >= 15200) {
                    File file = new File("/sdcard/test.raw");
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(mUpImage);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    byte[] imageData = checkHaveTis(mUpImage);
                    byte[] bmpdata = getFingerprintImage(imageData, 152, 200, 0/*18*/);
                    textSize.setText("152 * 200");
                    Bitmap image = BitmapFactory.decodeByteArray(bmpdata, 0, bmpdata.length);
                    saveJPGimage(image);
                    Log.d(TAG, "bmpdata.length:" + bmpdata.length);
                    fingerprintImage.setImageBitmap(image);
                    mUpImageSize = 0;
                    mUpImageCount = mUpImageCount + 1;
                    mIsWork = false;
                    AddStatusList("Display Image");
                }
            } else if (imgSize == IMG288) {   //image size with 256*288
                memcpy(mUpImage, mUpImageSize, databuf, 0, datasize);
                mUpImageSize = mUpImageSize + datasize;
                if (mUpImageSize >= 36864) {
                    File file = new File("/sdcard/test.raw");
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(mUpImage);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] imageData = checkHaveTis(mUpImage);
                    byte[] bmpdata = getFingerprintImage(imageData, 256, 288, 0/*18*/);
                    textSize.setText("256 * 288");
                    Bitmap image = BitmapFactory.decodeByteArray(bmpdata, 0, bmpdata.length);
                    saveJPGimage(image);

                    byte[] inpdata = new byte[73728];
                    int inpsize = 73728;
                    System.arraycopy(bmpdata, 1078, inpdata, 0, inpsize);
                    SaveWsqFile(inpdata, inpsize, "fingerprint.wsq");

                    Log.d(TAG, "bmpdata.length:" + bmpdata.length);
                    fingerprintImage.setImageBitmap(image);
                    mUpImageSize = 0;
                    mUpImageCount = mUpImageCount + 1;
                    mIsWork = false;
                    AddStatusList("Display Image");
                }
            } else if (imgSize == IMG360) {   //image size with 256*360
                memcpy(mUpImage, mUpImageSize, databuf, 0, datasize);
                mUpImageSize = mUpImageSize + datasize;
                //AddStatusList("Image Len="+Integer.toString(mUpImageSize)+"--"+Integer.toString(mUpImageCount));
                if (mUpImageSize >= 46080) {
                    File file = new File("/sdcard/test.raw");
                    try {
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(mUpImage);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] imageData = checkHaveTis(mUpImage);
                    byte[] bmpdata = getFingerprintImage(imageData, 256, 360, 0/*18*/);
                    textSize.setText("256 * 360");
                    Bitmap image = BitmapFactory.decodeByteArray(bmpdata, 0, bmpdata.length);
                    saveJPGimage(image);

                    byte[] inpdata = new byte[92160];
                    int inpsize = 92160;
                    System.arraycopy(bmpdata, 1078, inpdata, 0, inpsize);
                    SaveWsqFile(inpdata, inpsize, "fingerprint.wsq");

                    Log.d(TAG, "bmpdata.length:" + bmpdata.length);
                    fingerprintImage.setImageBitmap(image);
                    mUpImageSize = 0;
                    mUpImageCount = mUpImageCount + 1;
                    mIsWork = false;
                    AddStatusList("Display Image");

                }

           /*     File f = new File("/sdcard/fingerprint.png");
                if (f.exists()) {
                    f.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    image.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] inpdata=new byte[73728];
                int inpsize=73728;
                System.arraycopy(bmpdata,1078, inpdata, 0, inpsize);
                SaveWsqFile(inpdata,inpsize,"fingerprint.wsq");*/
            }
        } else { //other data received from the device
            // append the databuf received into mCmdData.
            memcpy(mCmdData, mCmdSize, databuf, 0, datasize);
            mCmdSize = mCmdSize + datasize;
            int totalsize = (byte) (mCmdData[5]) + ((mCmdData[6] << 8) & 0xFF00) + 9;
            if (mCmdSize >= totalsize) {
                mCmdSize = 0;
                mIsWork = false;
                TimeOutStop();

                //parsing the mCmdData
                if ((mCmdData[0] == 'F') && (mCmdData[1] == 'T')) {
                    switch (mCmdData[4]) {
                        case CMD_PASSWORD: {
                        }
                        break;
                        case CMD_ENROLHOST: {

                                int size = (byte) (mCmdData[5]) + ((mCmdData[6] << 8) & 0xFF00) - 1;
                                if (mCmdData[7] == 1) {
                                    memcpy(mRefData, 0, mCmdData, 8, size);
                                    mRefSize = size;
                                    //---- Se almacenan las platillas de byte a String
                                    if (contHuella < 2) {
                                        if (contHuella == 0) {
                                            for (byte aux : mRefData) {
                                                int b = aux & 0xff;
                                                if (Integer.toHexString(b).length()== 1) huella1 += "0";
                                                huella1 += Integer.toHexString(b);
                                            }
                                            AddStatusList("Huella principal enrolada");
                                        } else if (contHuella == 1) {
                                            for (byte aux2 : mRefData) {
                                                int b2 = aux2 & 0xff;
                                                if (Integer.toHexString(b2).length()== 1) huella2 += "0";
                                                huella2 += Integer.toHexString(b2);
                                            }
                                            AddStatusList("Huella secundaria enrolada");
                                        }
                                        contHuella++;
                                    } else {
                                        AddStatusList("Huellas asignadas!");
                                    }


                                    userId += 1;

                                } else if (mCmdData[7] == 2) {
                                    AddStatusList(getString(R.string.put_your_finger));
                                } else if (mCmdData[7] == 3) {
                                    AddStatusList(getString(R.string.lift_your_finger));
                                } else if (mCmdData[7] == 20) {
                                    AddStatusList(getString(R.string.time_out));
                                } else if (mCmdData[7] == 0) {
                                    AddStatusList(getString(R.string.enrol_fail));
                                }
                        }
                        break;
                        case CMD_GETSN: {
                            int size = (byte) (mCmdData[5]) + ((mCmdData[6] << 8) & 0xFF00) - 1;
                            if (mCmdData[7] == 1) {
                                byte[] snb = new byte[32];
                                memcpy(snb, 0, mCmdData, 8, size);
                                String sn = null;
                                try {
                                    sn = new String(snb, 0, size, "UNICODE");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                AddStatusList("SN:" + sn);
                            } else
                                AddStatusList("Fail");
                        }
                        break;
                        case CMD_GETBAT: {
                            int size = (byte) (mCmdData[5]) + ((mCmdData[6] << 8) & 0xFF00) - 1;
                            if (size > 0) {
                                memcpy(mBat, 0, mCmdData, 8, size);
                                double batVal = mBat[0] / 10.0;
                                double batPercent = ((batVal - 3.45) / 0.75) * 100;
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String batPercentage = decimalFormat.format(batPercent) + " %";
                                AddStatusList("Battery Percentage:" + batPercentage);
                            } else
                                AddStatusList("Get Battery Fail");
                        }
                        break;
                    }
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // setting for the tool bar menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void ensureDiscoverable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Create directory folder for storing the images
     */
    public void CreateDirectory() {
        sDirectory = Environment.getExternalStorageDirectory() + "/Fingerprint Images/";
        File destDir = new File(sDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

    }

    /**
     * method for saving the fingerprint image as JPG
     * @param bitmap bitmap image
     */
    public void saveJPGimage(Bitmap bitmap) {
        String dir = sDirectory;
        String imageFileName = String.valueOf(System.currentTimeMillis());

        try {
            File file = new File(dir + imageFileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * method of saving the image into WSQ format
     * @param rawdata raw image data.
     * @param rawsize size of the raw image data.
     * @param filename the file name of the image.
     */
    public void SaveWsqFile(byte[] rawdata, int rawsize, String filename) {
        byte[] outdata = new byte[rawsize];
        int[] outsize = new int[1];

        if (rawsize == 73728) {
            wsq.getInstance().RawToWsq(rawdata, rawsize, 256, 288, outdata, outsize, 2.833755f);
        } else if (rawsize == 92160) {
            wsq.getInstance().RawToWsq(rawdata, rawsize, 256, 360, outdata, outsize, 2.833755f);
        }

        try {
            File fs = new File("/sdcard/" + filename);
            if (fs.exists()) {
                fs.delete();
            }
            new File("/sdcard/" + filename);
            RandomAccessFile randomFile = new RandomAccessFile("/sdcard/" + filename, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(outdata, 0, outsize[0]);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.scan:
                    // Launch the DeviceListActivity to see devices and do scan
                    Intent serverIntent = new Intent(AgregarProfesor.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    return true;
                case R.id.discoverable:
                    // Ensure this device is discoverable by others
                    ensureDiscoverable();
                    return true;
            }
            return true;
        }
    };


    public String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    //display the first 6 bytes of data.
    public String bytesToAscii(byte[] bytes) {
        return bytesToAscii(bytes, 0, 6);
    }


    /**
     * Check if have indication info
     *
     * @param mUpImage
     * @return
     */
    private byte[] checkHaveTis(byte[] mUpImage) {
        //?????????
        if ((mUpImage[0] == 'F') && (mUpImage[1] == 'T')) {
            if (mUpImage[7] == 2) {
                AddStatusList(getString(R.string.put_your_finger));
            } else if (mUpImage[7] == 3) {
                AddStatusList(getString(R.string.lift_your_finger));
            } else if (mUpImage[7] == 20) {
                AddStatusList(getString(R.string.time_out));
            }
            memcpy(mUpImage2, 0, mUpImage, 20, mUpImageSize - 20);
        } else {
            memcpy(mUpImage2, 0, mUpImage, 0, mUpImageSize);
        }
        return mUpImage2;
    }
}
