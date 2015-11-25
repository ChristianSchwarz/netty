package io.netty.channel.uart.rxtx;

import gnu.io.SerialPort;
import io.netty.channel.uart.UartChannelConfig.Databits;
import io.netty.channel.uart.UartChannelConfig.FlowControl;
import io.netty.channel.uart.UartChannelConfig.Parity;
import io.netty.channel.uart.UartChannelConfig.Stopbits;

class ChannelConfigConverter {
	private ChannelConfigConverter() {
	}

	public static int toRxtx(Databits databits) {
		switch (databits) {
		case DATABITS_5:
			return SerialPort.DATABITS_5;
		case DATABITS_6:
			return SerialPort.DATABITS_6;
		case DATABITS_7:
			return SerialPort.DATABITS_7;
		case DATABITS_8:
			return SerialPort.DATABITS_8;
		default:
			throw new IllegalArgumentException("Unsupported databits: "
					+ databits);
		}
	}

	public static int toRxtx(Parity parity) {
		switch (parity) {
		case PARITY_EVEN:
			return SerialPort.PARITY_EVEN;
		case PARITY_MARK:
			return SerialPort.PARITY_MARK;
		case PARITY_NONE:
			return SerialPort.PARITY_NONE;
		case PARITY_ODD:
			return SerialPort.PARITY_ODD;
		case SPACE:
			return SerialPort.PARITY_SPACE;
		default:
			throw new IllegalArgumentException("Unsupported parity: " + parity);

		}

	}

	public static int toRxtx(Stopbits stopbits) {
		switch (stopbits) {
		case STOPBITS_1:
			return SerialPort.STOPBITS_1;
		case STOPBITS_1_5:
			return SerialPort.STOPBITS_1_5;
		case STOPBITS_2:
			return SerialPort.STOPBITS_2;
		default:
			throw new IllegalArgumentException("Unsupported stopbits: "
					+ stopbits);
		}
	}
	
	public static int toRxtx(FlowControl flowControl) {
		throw new UnsupportedOperationException();
	}
}
