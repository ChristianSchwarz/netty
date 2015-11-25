package io.netty.channel.uart.rxtx;

import io.netty.channel.uart.UartChannelConfig.Databits;
import io.netty.channel.uart.UartChannelConfig.Stopbits;
import io.netty.channel.uart.UartChannelConfig.Parity;

public class ChannelConfigConverter {

	public static int toRxtx(Databits databits){
		switch (databits) {
		case DATABITS_5:
			return 5;
		case DATABITS_6:
			return 6;
		case DATABITS_7:
			return 7;
		case DATABITS_8:
			return 8;
		default:
			throw new IllegalStateException("Unsupported databits: "+databits);
		}
	}
	
	public static int toRxtx(Parity parity){
		return 0;
		
	}
	
	public static int toRxtx(Stopbits stopbits){
		return 0;
		
	}

	
	
	
}
