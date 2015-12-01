/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel.uart;

import java.net.SocketAddress;

/**
 * A {@link SocketAddress} subclass to wrap the serial port address of a RXTX
 * device (e.g. COM1, /dev/ttyUSB0).
 */
public class UartDeviceAddress extends SocketAddress {

    private static final long serialVersionUID = -2907820090993709523L;

    private final String portName;

    public final static UartDeviceAddress LOCAL_ADDRESS = new UartDeviceAddress();
    
    private UartDeviceAddress(){
    	portName=null;
    }
    /**
     * Creates a {@link UartDeviceAddress} representing the address of the serial port.
     *
     * @param portName the address of the device (e.g. "COM1", "/dev/ttyUSB0", ...)
     */
    public UartDeviceAddress(String portName) {
    	if (portName==null)
    		throw new IllegalArgumentException("Argument >portName< must not be null!");
        this.portName = portName;
    }

    /**
     * @return The serial port address of the device (e.g. COM1, /dev/ttyUSB0, ...)
     */
    public String getPortName() {
        return portName;
    }
}