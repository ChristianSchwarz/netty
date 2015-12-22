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

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import io.netty.channel.ChannelPromise;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.uart.DefaultUartChannelConfig;
import io.netty.channel.uart.UartChannel;
import io.netty.channel.uart.UartChannelConfig;
import io.netty.channel.uart.UartChannelConfig.Databits;
import io.netty.channel.uart.UartChannelConfig.FlowControl;
import io.netty.channel.uart.UartChannelConfig.Parity;
import io.netty.channel.uart.UartChannelConfig.Stopbits;
import io.netty.channel.uart.UartDeviceAddress;
import io.netty.util.internal.OneTimeTask;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import static io.netty.channel.uart.UartChannelOption.WAIT_TIME;
import static io.netty.channel.uart.UartDeviceAddress.LOCAL_ADDRESS;
import static io.netty.channel.uart.rxtx.ChannelConfigConverter.toRxtx;

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
        serialPort = (SerialPort) cpi.open(getClass().getName(), 1000);
       //commPort.enableReceiveTimeout(config().getOption(READ_TIMEOUT));
        deviceAddress = remote;
    }

    protected void doInit() throws Exception {
        int bauds = config().getBaudrate();
		Databits databits = config().getDatabits();
		Stopbits stopbits = config().getStopbits();
		Parity partiy = config().getParitybit();
		FlowControl flowControl = config().getFlowControl();
		serialPort.setSerialPortParams(
            bauds,
            toRxtx(databits),
            toRxtx(stopbits),
            toRxtx(partiy)
        );
		serialPort.setFlowControlMode(toRxtx(flowControl));
        serialPort.setDTR(config().isDtr());
        serialPort.setRTS(config().isRts());

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

    /**
     * dshdfh
     */
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
                    activateDelayed(promise, wasActive, waitTime);
                } else {
                    activateNow(promise, wasActive);
                }
            } catch (Throwable t) {
                emergencyClose(promise, t);
            }
        }

        private void activateDelayed(final ChannelPromise promise, final boolean wasActive, int waitTime) {
            eventLoop().schedule(new OneTimeTask() {
                @Override
                public void run() {
                    try {
                        activateNow(promise, wasActive);
                    } catch (Throwable t) {
                        emergencyClose(promise, t);
                    }
                }
            }, waitTime, TimeUnit.MILLISECONDS);
        }

        private void activateNow(ChannelPromise promise, boolean wasActive) throws Exception {
            doInit();
            safeSetSuccess(promise);
            if (!wasActive && isActive()) {
                pipeline().fireChannelActive();
            }
        }

        private void emergencyClose(ChannelPromise promise, Throwable t) {
            safeSetFailure(promise, t);
            closeIfClosed();
        }


    }
}
