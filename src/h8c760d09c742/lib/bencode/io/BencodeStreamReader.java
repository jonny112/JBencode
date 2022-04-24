package h8c760d09c742.lib.bencode.io;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import h8c760d09c742.lib.bencode.BencodeReader;

/**
 * Deserialization of Bencode entities from an input stream.
 */
public class BencodeStreamReader extends BencodeReader implements Closeable {

	protected final InputStream strmIn;
	protected final boolean closeStrm;
	protected int pos;
	
	/**
	 * Creates a new reader instance backed by a stream.
	 * @param strmIn The stream to read from.
	 * @param close Whether to close the underlying stream if this reader is closed.
	 */
	public BencodeStreamReader(InputStream strmIn, boolean close) {
		this.strmIn = strmIn;
		this.closeStrm = close;
	}
	
	/**
	 * Creates a new reader instance backed by a stream.
	 * Closing the reader closes the underlying stream.
	 * @param strmIn The stream to read from.
	 */
	public BencodeStreamReader(InputStream strmIn) {
		this(strmIn, true);
	}
	
	@Override
	public int getPosition() {
		return pos;
	}
	
	@Override
	protected byte readByte() throws IOException {
		int b = strmIn.read();
		if (b < 0) throw new EOFException();
		pos++;
		return (byte)b;
	}
	
	@Override
	protected void read(byte[] data) throws IOException {
		if (strmIn.read(data) < data.length) throw new EOFException();
		pos += data.length;
	}
	
	@Override
	public void close() throws IOException {
		try {
			if (closeStrm) strmIn.close();
		} finally {
			super.close();
		}
	}

}
