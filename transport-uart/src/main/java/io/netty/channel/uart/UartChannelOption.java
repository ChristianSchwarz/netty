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

import io.netty.channel.ChannelOption;
import io.netty.channel.uart.UartChannelConfig.Databits;
import io.netty.channel.uart.UartChannelConfig.FlowControl;
import io.netty.channel.uart.UartChannelConfig.Parity;
import io.netty.channel.uart.UartChannelConfig.Stopbits;
import static io.netty.channel.ChannelOption.*;

/**
 * Option to configure a serial port connection
 */
public final class UartChannelOption {

    private static final Class<UartChannelOption> T = UartChannelOption.class;

    public static final ChannelOption<Integer> BAUD_RATE = valueOf(T, "BAUD_RATE");
    public static final ChannelOption<Boolean> DTR = valueOf(T, "DTR");
    public static final ChannelOption<Boolean> RTS = valueOf(T, "RTS");
    public static final ChannelOption<Stopbits> STOP_BITS = valueOf(T, "STOP_BITS");
    public static final ChannelOption<Databits> DATA_BITS = valueOf(T, "DATA_BITS");
    public static final ChannelOption<Parity> PARITY = valueOf(T, "PARITY_BIT");
    public static final ChannelOption<Integer> WAIT_TIME = valueOf(T, "WAIT_TIME");
    public static final ChannelOption<Integer> READ_TIMEOUT = valueOf(T, "READ_TIMEOUT");
    public static final ChannelOption<FlowControl> FLOW_CONTROL = valueOf(T, "FLOW_CONTROL");

    private UartChannelOption() { }
}
