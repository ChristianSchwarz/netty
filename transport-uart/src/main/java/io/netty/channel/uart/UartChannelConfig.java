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

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

/**
 * A configuration class for UART device connections.
 *
 * <h3>Available options</h3>
 *
 * In addition to the options provided by {@link ChannelConfig},
 * {@link DefaultUartChannelConfig} allows the following options in the option map:
 *
 * <table border="1" cellspacing="0" cellpadding="6">
 * <tr>
 * <th>Name</th><th>Associated setter method</th>
 * </tr><tr>
 * <td>{@link UartChannelOption#BAUD_RATE}</td><td>{@link #setBaudrate(int)}</td>
 * </tr><tr>
 * <td>{@link UartChannelOption#DTR}</td><td>{@link #setDtr(boolean)}</td>
 * </tr><tr>
 * <td>{@link UartChannelOption#RTS}</td><td>{@link #setRts(boolean)}</td>
 * </tr><tr>
 * <td>{@link UartChannelOption#STOP_BITS}</td><td>{@link #setStopbits(Stopbits)}</td>
 * </tr><tr>
 * <td>{@link UartChannelOption#DATA_BITS}</td><td>{@link #setDatabits(Databits)}</td>
 * </tr><tr>
 * <td>{@link UartChannelOption#PARITY}</td><td>{@link #setParitybit(Parity)}</td>
 * </tr><tr>
 * <td>{@link UartChannelOption#WAIT_TIME}</td><td>{@link #setWaitTimeMillis(int)}</td>
 * </tr>
 * </table>
 */
public interface UartChannelConfig extends ChannelConfig {
    enum Stopbits {
        /**
         * 1 stop bit will be sent at the end of every character
         */
        STOPBITS_1,
        /**
         * 2 stop bits will be sent at the end of every character
         */
        STOPBITS_2,
        /**
         * 1.5 stop bits will be sent at the end of every character
         */
        STOPBITS_1_5;
    }

    enum Databits {
        /**
         * 5 data bits will be used for each character (ie. Baudot code)
         */
        DATABITS_5,
        /**
         * 6 data bits will be used for each character
         */
        DATABITS_6,
        /**
         * 7 data bits will be used for each character (ie. ASCII)
         */
        DATABITS_7,
        /**
         * 8 data bits will be used for each character (ie. binary data)
         */
        DATABITS_8;

        
    }

    enum Parity {
        /**
         * No parity bit will be sent with each data character at all
         */
        PARITY_NONE,
        /**
         * An odd parity bit will be sent with each data character, ie. will be set
         * to 1 if the data character contains an even number of bits set to 1.
         */
        PARITY_ODD,
        /**
         * An even parity bit will be sent with each data character, ie. will be set
         * to 1 if the data character contains an odd number of bits set to 1.
         */
        PARITY_EVEN,
        /**
         * A mark parity bit (ie. always 1) will be sent with each data character
         */
        
        PARITY_MARK,
        /**
         * A space parity bit (ie. always 0) will be sent with each data character
         */
        SPACE;
    }
    /**
     * Sets the baud rate (ie. bits per second) for communication with the serial device.
     * The baud rate will include bits for framing (in the form of stop bits and parity),
     * such that the effective data rate will be lower than this value.
     *
     * @param baudrate The baud rate (in bits per second)
     */
    UartChannelConfig setBaudrate(int baudrate);

    /**
     * Sets the number of stop bits to include at the end of every character to aid the
     * serial device in synchronising with the data.
     *
     * @param stopbits The number of stop bits to use
     */
    UartChannelConfig setStopbits(Stopbits stopbits);

    /**
     * Sets the number of data bits to use to make up each character sent to the serial
     * device.
     *
     * @param databits The number of data bits to use
     */
    UartChannelConfig setDatabits(Databits databits);

    /**
     * Sets the type of parity bit to be used when communicating with the serial device.
     *
     * @param paritybit The type of parity bit to be used
     */
    UartChannelConfig setParitybit(Parity paritybit);

    /**
     * @return The configured baud rate, defaulting to 115200 if unset
     */
    int getBaudrate();

    /**
     * @return The configured stop bits, defaulting to {@link Stopbits#STOPBITS_1} if unset
     */
    Stopbits getStopbits();

    /**
     * @return The configured data bits, defaulting to {@link Databits#DATABITS_8} if unset
     */
    Databits getDatabits();

    /**
     * @return The configured parity bit, defaulting to {@link Parity#PARITY_NONE} if unset
     */
    Parity getParitybit();

    /**
     * @return true if the serial device should support the Data Terminal Ready signal
     */
    boolean isDtr();

    /**
     * Sets whether the serial device supports the Data Terminal Ready signal, used for
     * flow control
     *
     * @param dtr true if DTR is supported, false otherwise
     */
    UartChannelConfig setDtr(boolean dtr);

    /**
     * @return true if the serial device should support the Ready to Send signal
     */
    boolean isRts();

    /**
     * Sets whether the serial device supports the Request To Send signal, used for flow
     * control
     *
     * @param rts true if RTS is supported, false otherwise
     */
    UartChannelConfig setRts(boolean rts);

    @Override
    UartChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis);

    @Override
    @Deprecated
    UartChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead);

    @Override
    UartChannelConfig setWriteSpinCount(int writeSpinCount);

    @Override
    UartChannelConfig setAllocator(ByteBufAllocator allocator);

    @Override
    UartChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator);

    @Override
    UartChannelConfig setAutoRead(boolean autoRead);

    @Override
    UartChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark);

    @Override
    UartChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark);

    @Override
    UartChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator);
}
