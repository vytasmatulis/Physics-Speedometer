package sped;

import gnu.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ericjbruno
 */

/**
 * The callback for Arduino serial events
 * @author edwinfinch
 */
interface ArduinoConnectionCallback {
    void serialEvent(String serialOutput);
}

/**
 * The ArduinoConnection class allows for easy use of hooking onto an Arduino and reading
 * data live from the serial port. It implements this through SerialPortEventListener.
 * 
 * @author edwinfinch
 */
public class ArduinoConnection implements SerialPortEventListener {
    SerialPort serialPort = null;
    ArduinoConnectionCallback connectionCallback;
    public boolean connected = false;
    ArduinoConnectionResult mostRecentResult;

    /**
     * Change this depending on the platform
     */
    private static final String PORT_NAMES[] = { 
        "/dev/tty.usbmodem", // Mac OS X
//        "/dev/usbdev", // Linux
//        "/dev/tty", // Linux
//        "/dev/serial", // Linux
//        "COM3", // Windows
    };
    
    /**
     * The ArduinoConnectionResult is an enum which keeps track of the possibilities
     * of an ArduinoConnection being initialized.
     */
    public enum ArduinoConnectionResult {
        Success, ErrorNotFound, ErrorPortInUse, ErrorUnknown, NotInitialized;
        
        public String getMessage(){
            switch(this){
                case Success:
                    return "success";
                case ErrorNotFound:
                    return "no Arduino was found :(";
                case ErrorPortInUse:
                    return "something is already using the Arduino";
                case NotInitialized:
                    return "the Arduino connection has not been initialized";
            }
            return "unknown error";
        }
    };
    
    private String appName;
    private BufferedReader input;
    private OutputStream output;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port
    
    /**
     * Sets the serial data listener for streaming to an external source
     * @param callback The callback to hook
     */
    public void setOnSerialListener(ArduinoConnectionCallback callback){
        connectionCallback = callback;
    }

    /**
     * Initializes the ArduinoConnection if there is an Arduino available
     * @return The ArduinoConnectionResult of how the initialization went
     */
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
            
            connected = true;
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
    
    /**
     * Send data to the Arduino. We currently have no need for this and are keeping it for future use.
     * @param data 
     */
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

    /**
     * Prepares the ArduinoConnection instance for close. Do any cleanup here.
     */
    public synchronized void prepareForClose(){
        input = null;
    }
    
    /**
     * Closes the connection to the Arduino.
     */
    public synchronized void close() {
        System.out.println("Got close request");
        input = null;
        if ( serialPort != null ) {
            System.out.println("Closing connection.");
            serialPort.removeEventListener();
            System.out.println("Removed event listener");
            serialPort.close();
            System.out.println("Closed");
        }
        else{
            System.out.println("Can't close, serialPort is null");
        }
    }

    /**
     * Handles new serial events from the Arduino. Updates live.
     * @param oEvent The event
     */
    public void serialEvent(SerialPortEvent oEvent) {
        //System.out.println("Event received: " + oEvent.toString());
        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE: 
                    if ( input == null ) {
                        System.out.println("Building input");
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

    /**
     * Sets up a new ArduinoConnection instance
     */
    public ArduinoConnection() {
        appName = getClass().getName();
    }
}



