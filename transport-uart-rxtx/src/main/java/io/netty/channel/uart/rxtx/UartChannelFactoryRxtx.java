package io.netty.channel.uart.rxtx;

import io.netty.channel.ChannelFactory;
import io.netty.channel.uart.UartChannel;

public class UartChannelFactoryRxtx implements ChannelFactory<UartChannel> {

	@Override
	public UartChannel newChannel() {
		
		return new RxtxChannel();
	}

}
