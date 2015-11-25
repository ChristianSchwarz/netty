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

import static io.netty.channel.uart.UartChannelOption.BAUD_RATE;
import static io.netty.channel.uart.UartChannelOption.DATA_BITS;
import static io.netty.channel.uart.UartChannelOption.DTR;
import static io.netty.channel.uart.UartChannelOption.PARITY;
import static io.netty.channel.uart.UartChannelOption.RTS;
import static io.netty.channel.uart.UartChannelOption.STOP_BITS;
import static io.netty.channel.uart.UartChannelOption.WAIT_TIME;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

import java.util.Map;

/**
 * Default configuration class for RXTX device connections.
 */
public final class DefaultUartChannelConfig extends DefaultChannelConfig implements UartChannelConfig {

    private volatile int baudrate = 115200;
    private volatile boolean dtr;
    private volatile boolean rts;
    private volatile Stopbits stopbits = Stopbits.STOPBITS_1;
    private volatile Databits databits = Databits.DATABITS_8;
    private volatile Parity paritybit = Parity.PARITY_NONE;


    public DefaultUartChannelConfig(UartChannel channel) {
        super(channel);
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(super.getOptions(), BAUD_RATE, DTR, RTS, STOP_BITS, DATA_BITS, PARITY, WAIT_TIME);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOption(ChannelOption<T> option) {
        if (option == BAUD_RATE) {
            return (T) Integer.valueOf(getBaudrate());
        }
        if (option == DTR) {
            return (T) Boolean.valueOf(isDtr());
        }
        if (option == RTS) {
            return (T) Boolean.valueOf(isRts());
        }
        if (option == STOP_BITS) {
            return (T) getStopbits();
        }
        if (option == DATA_BITS) {
            return (T) getDatabits();
        }
        if (option == PARITY) {
            return (T) getParitybit();
        }
        return super.getOption(option);
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> option, T value) {
        validate(option, value);

        if (option == BAUD_RATE) {
            setBaudrate((Integer) value);
        } else if (option == DTR) {
            setDtr((Boolean) value);
        } else if (option == RTS) {
            setRts((Boolean) value);
        } else if (option == STOP_BITS) {
            setStopbits((Stopbits) value);
        } else if (option == DATA_BITS) {
            setDatabits((Databits) value);
        } else if (option == PARITY) {
            setParitybit((Parity) value);
        } else {
            return super.setOption(option, value);
        }
        return true;
    }

    @Override
    public UartChannelConfig setBaudrate(final int baudrate) {
        this.baudrate = baudrate;
        return this;
    }

    @Override
    public UartChannelConfig setStopbits(final Stopbits stopbits) {
        this.stopbits = stopbits;
        return this;
    }

    @Override
    public UartChannelConfig setDatabits(final Databits databits) {
        this.databits = databits;
        return this;
    }

    @Override
    public UartChannelConfig setParitybit(final Parity paritybit) {
        this.paritybit = paritybit;
        return  this;
    }

    @Override
    public int getBaudrate() {
        return baudrate;
    }

    @Override
    public Stopbits getStopbits() {
        return stopbits;
    }

    @Override
    public Databits getDatabits() {
        return databits;
    }

    @Override
    public Parity getParitybit() {
        return paritybit;
    }

    @Override
    public boolean isDtr() {
        return dtr;
    }

    @Override
    public UartChannelConfig setDtr(final boolean dtr) {
        this.dtr = dtr;
        return this;
    }

    @Override
    public boolean isRts() {
        return rts;
    }

    @Override
    public UartChannelConfig setRts(final boolean rts) {
        this.rts = rts;
        return this;
    }

    

    @Override
    public UartChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }

    @Override
    @Deprecated
    public UartChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }

    @Override
    public UartChannelConfig setWriteSpinCount(int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }

    @Override
    public UartChannelConfig setAllocator(ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }

    @Override
    public UartChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }

    @Override
    public UartChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override
    public UartChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }

    @Override
    public UartChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }

    @Override
    public UartChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
}
