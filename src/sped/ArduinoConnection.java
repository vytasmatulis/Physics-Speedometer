package sped;

import gnu.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * @author ericjbruno
 */

interface ArduinoConnectionCallback {
    void serialEvent(String serialOutput);
}

public class ArduinoConnection implements SerialPortEventListener {
    SerialPort serialPort = null;
    ArduinoConnectionCallback connectionCallback;

    private static final String PORT_NAMES[] = { 
        "/dev/tty.usbmodem", // Mac OS X
//        "/dev/usbdev", // Linux
//        "/dev/tty", // Linux
//        "/dev/serial", // Linux
//        "COM3", // Windows
    };
    
    public enum ArduinoConnectionResult {
        Success, ErrorNotFound, ErrorPortInUse, ErrorUnknown;
        
        public String getMessage(){
            switch(this){
                case Success:
                    return "success";
                case ErrorNotFound:
                    return "no Arduino was found :(";
                case ErrorPortInUse:
                    return "something is already using the Arduino";
            }
            return "unknown error";
        }
    };
    
    private String appName;
    private BufferedReader input;
    private OutputStream output;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port
    
    public void setOnSerialListener(ArduinoConnectionCallback callback){
        connectionCallback = callback;
    }

    public ArduinoConnectionResult initialize() {
        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            // Enumerate system ports and try connecting to Arduino over each
            //
            System.out.println( "Trying:");
            while (portId == null && portEnum.hasMoreElements()) {
                // Iterate through your host computer's serial port IDs
                //
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                System.out.println( " port " + currPortId.getName() );
                for (String portName : PORT_NAMES) {
                    if ( currPortId.getName().equals(portName) 
                      || currPortId.getName().startsWith(portName)) {
                        

                        // Try to connect to the Arduino on this port
                        //
                        // Open serial port
                        System.out.println("Launching " + appName + " port " + portName + " currentPortId = " + currPortId.getName());
                        serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
                        portId = currPortId;
                        System.out.println( "Connected on port" + currPortId.getName() );
                        break;
                    }
                }
            }
        
            if (portId == null || serialPort == null) {
                System.out.println("Oops... Could not connect to Arduino");
                return ArduinoConnectionResult.ErrorNotFound;
            }
        
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            // Give the Arduino some time
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            
            return ArduinoConnectionResult.Success;
        }
        catch ( Exception e ) { 
            e.printStackTrace();
            System.out.println(e.getMessage());
            switch(e.getMessage().toLowerCase()){
                case "unknown application":
                    return ArduinoConnectionResult.ErrorPortInUse;
            }
        }
        return ArduinoConnectionResult.ErrorUnknown;
    }
    
    private void sendData(String data) {
        try {
            System.out.println("Sending data: '" + data +"'");
            
            // open the streams and send the "y" character
            output = serialPort.getOutputStream();
            output.write( data.getBytes() );
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }

    //
    // This should be called when you stop using the port
    //
    public synchronized void close() {
        if ( serialPort != null ) {
            System.out.println("Closing connection.");
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    //
    // Handle serial port event
    //
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        //System.out.println("Event received: " + oEvent.toString());
        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE: 
                    if ( input == null ) {
                        input = new BufferedReader(
                            new InputStreamReader(
                                    serialPort.getInputStream()));
                    }
                    String inputLine = input.readLine();
                    //System.out.println(inputLine);
                    if(connectionCallback != null){
                        connectionCallback.serialEvent(inputLine);
                    }
                    break;

                default:
                    break;
            }
        } 
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public ArduinoConnection() {
        appName = getClass().getName();
    }
}


