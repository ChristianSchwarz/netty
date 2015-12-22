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

import io.netty.channel.Channel;

/**
 * A channel to a UART device.
 */
public interface UartChannel extends Channel {

    @Override
    UartChannelConfig config();


    /**
     * Returns  always {@link UartDeviceAddress#LOCAL_ADDRESS} to indicate that UART-Ports have no local address.
     *
     * @return always {@link UartDeviceAddress#LOCAL_ADDRESS}
     */
    @Override
    UartDeviceAddress localAddress();

    /**
     * The UART-Address this channel is bound to, e.g. "COM1" on Windows or "dev/ttyS0" on Unix-System
     *
     * @return never {@code null}
     */
    @Override
    UartDeviceAddress remoteAddress();

    

   
}
