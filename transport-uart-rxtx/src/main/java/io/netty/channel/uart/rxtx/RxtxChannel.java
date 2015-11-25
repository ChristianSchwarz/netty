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
package io.netty.channel.uart.rxtx;

import static io.netty.channel.uart.UartChannelOption.*;
import static io.netty.channel.uart.UartDeviceAddress.LOCAL_ADDRESS;
import static io.netty.channel.uart.rxtx.ChannelConfigConverter.toRxtx;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import io.netty.channel.ChannelPromise;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.uart.UartChannel;
import io.netty.channel.uart.UartChannelConfig;
import io.netty.channel.uart.DefaultUartChannelConfig;
import io.netty.channel.uart.UartChannelConfig.Databits;
import io.netty.channel.uart.UartChannelConfig.Parity;
import io.netty.channel.uart.UartChannelConfig.Stopbits;
import io.netty.channel.uart.UartChannelOption;
import io.netty.channel.uart.UartDeviceAddress;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * A channel to a serial device using the RXTX library.
 */
public class RxtxChannel extends OioByteStreamChannel implements UartChannel{

    

    private final UartChannelConfig config;

    private boolean open = true;
    private UartDeviceAddress deviceAddress;
    private SerialPort serialPort;

    public RxtxChannel() {
        super(null);

        config = new DefaultUartChannelConfig(this);
    }

    @Override
    public UartChannelConfig config() {
        return config;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new RxtxUnsafe();
    }

    @Override
    protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
    	UartDeviceAddress remote = (UartDeviceAddress) remoteAddress;
        final CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(remote.getPortName());
        final CommPort commPort = cpi.open(getClass().getName(), 1000);
       //commPort.enableReceiveTimeout(config().getOption(READ_TIMEOUT));
        deviceAddress = remote;

        serialPort = (SerialPort) commPort;
    }

    protected void doInit() throws Exception {
        Integer bauds = config().getOption(BAUD_RATE);
		Databits databits = config().getOption(DATA_BITS);
		Stopbits stopbits = config().getOption(STOP_BITS);
		Parity partiy = config().getOption(PARITY);
		
		serialPort.setSerialPortParams(
            bauds,
            toRxtx(databits),
            toRxtx(stopbits),
            toRxtx(partiy)
        );
        serialPort.setDTR(config().getOption(DTR));
        serialPort.setRTS(config().getOption(RTS));

        activate(serialPort.getInputStream(), serialPort.getOutputStream());
    }

    @Override
    public UartDeviceAddress localAddress() {
        return (UartDeviceAddress) super.localAddress();
    }

    @Override
    public UartDeviceAddress remoteAddress() {
        return (UartDeviceAddress) super.remoteAddress();
    }

    @Override
    protected UartDeviceAddress localAddress0() {
        return LOCAL_ADDRESS;
    }

    @Override
    protected UartDeviceAddress remoteAddress0() {
        return deviceAddress;
    }

    @Override
    protected void doBind(SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDisconnect() throws Exception {
        doClose();
    }

    @Override
    protected void doClose() throws Exception {
        open = false;
        try {
           super.doClose();
        } finally {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
                serialPort = null;
            }
        }
    }

    private final class RxtxUnsafe extends AbstractUnsafe {
        @Override
        public void connect(
                final SocketAddress remoteAddress,
                final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !ensureOpen(promise)) {
                return;
            }

            try {
                final boolean wasActive = isActive();
                doConnect(remoteAddress, localAddress);

                int waitTime = config().getOption(WAIT_TIME);
                if (waitTime > 0) {
                    eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                doInit();
                                safeSetSuccess(promise);
                                if (!wasActive && isActive()) {
                                    pipeline().fireChannelActive();
                                }
                            } catch (Throwable t) {
                                safeSetFailure(promise, t);
                                closeIfClosed();
                            }
                        }
                   }, waitTime, TimeUnit.MILLISECONDS);
                } else {
                    doInit();
                    safeSetSuccess(promise);
                    if (!wasActive && isActive()) {
                        pipeline().fireChannelActive();
                    }
                }
            } catch (Throwable t) {
                safeSetFailure(promise, t);
                closeIfClosed();
            }
        }
    }
}
