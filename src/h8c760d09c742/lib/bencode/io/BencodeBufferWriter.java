package h8c760d09c742.lib.bencode.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import h8c760d09c742.lib.bencode.BencodeWriter;

/**
 * Serialization of Bencode entities to a preallocated byte buffer.
 */
public class BencodeBufferWriter extends BencodeWriter {

	private final ByteBuffer bufOut;
	
	/**
	 * Creates a new writer instance backed by a Buffer.
	 * @param bufOut The buffer to write to.
	 */
	public BencodeBufferWriter(ByteBuffer bufOut) {
		this.bufOut = bufOut;
	}
	
	@Override
	public int getPosition() {
		return bufOut.position();
	}
	
	@Override
	protected void write(byte data) throws IOException {
		bufOut.put(data);
	}
	
	@Override
	protected void write(byte[] data) throws IOException {
		bufOut.put(data);
	}

}
