package io.netty.channel.uart;

import io.netty.channel.ChannelFactory;

import java.util.ServiceLoader;

public class UartChannelFactory implements ChannelFactory<UartChannel> {

	
	
	@Override
	public UartChannel newChannel() {
		ServiceLoader<UartChannel> uartChannelImplementations = ServiceLoader.load(UartChannel.class);
		for (UartChannel uartChannel : uartChannelImplementations) {
			
		}
		return null;
	}

}
