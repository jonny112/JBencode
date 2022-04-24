package h8c760d09c742.lib.bencode.io;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import h8c760d09c742.lib.bencode.BencodeReader;

/**
 * Deserialization of Bencode entities from a byte buffer.
 */
public class BencodeBufferReader extends BencodeReader {

	protected final ByteBuffer bufIn;
	
	/**
	 * Creates a new reader instance backed by a Buffer.
	 * @param bufIn The buffer to read from.
	 */
	public BencodeBufferReader(ByteBuffer bufIn) {
		this.bufIn = bufIn;
	}
	
	@Override
	public int getPosition() {
		return bufIn.position();
	}
	
	@Override
	protected byte readByte() throws IOException {
		byte b;
		try {
			b = bufIn.get();
		} catch (BufferUnderflowException e) {
			throw new EOFException();
		}
		return b;
	}
	
	@Override
	protected void read(byte[] data) throws IOException {
		try {
			bufIn.get(data);
		} catch (BufferUnderflowException e) {
			throw new EOFException();
		}
	}

}
