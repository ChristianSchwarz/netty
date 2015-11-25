package io.netty.channel.uart;

import java.util.ServiceLoader;

import io.netty.channel.ChannelFactory;

public class UartChannelFactory implements ChannelFactory<UartChannel> {

	
	
	@Override
	public UartChannel newChannel() {
		ServiceLoader<UartChannel> uartChannelImplementations = ServiceLoader.load(UartChannel.class);
		for (UartChannel uartChannel : uartChannelImplementations) {
			
		}
		return null;
	}

}
