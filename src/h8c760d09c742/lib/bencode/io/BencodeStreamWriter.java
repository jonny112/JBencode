package h8c760d09c742.lib.bencode.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

import h8c760d09c742.lib.bencode.BencodeObject;
import h8c760d09c742.lib.bencode.BencodeWriter;

/**
 * Serialization of Bendode entities to an output stream.
 * @author jonny
 *
 */
public class BencodeStreamWriter extends BencodeWriter implements Closeable, Flushable {

	protected final OutputStream strmOut;
	protected final boolean closeStrm;
	protected int pos;
	
	/**
	 * Creates a new writer instance backed by a stream.
	 * @param strmOut The stream to write to.
	 * @param close Whether to close the underlying stream if this reader is closed.
	 */
	public BencodeStreamWriter(OutputStream strmOut, boolean close) {
		this.strmOut = strmOut;
		this.closeStrm = close;
	}
	
	/**
	 * Creates a new writer instance backed by a stream.
	 * Closing the writer closes the underlying stream.
	 * @param strmOut The stream to write to.
	 */
	public BencodeStreamWriter(OutputStream strmOut) {
		this(strmOut, true);
	}
	
	/**
	 * Writes all specified Bencode objects to the specified stream.
	 * @param strmOut The stream to write to.
	 * @param obj The objects to serialize.
	 * @throws IOException
	 */
	public static void writeAll(OutputStream strmOut, BencodeObject... obj) throws IOException {
		try (BencodeStreamWriter wr = new BencodeStreamWriter(strmOut)) {	
			for (BencodeObject o : obj) wr.write(o);
		}
	}
	
	@Override
	public int getPosition() {
		return pos;
	}
	
	@Override
	public BencodeStreamWriter write(BencodeObject obj) throws IOException {
		super.write(obj);
		return this;
	}

	@Override
	protected void write(byte data) throws IOException {
		strmOut.write(data);
		pos++;
	}
	
	@Override
	protected void write(byte[] data) throws IOException {
		strmOut.write(data);
		pos += data.length;
	}
	
	@Override
	public void close() throws IOException {
		try {
			if (closeStrm) strmOut.close();
		} finally {
			super.close();
		}
	}

	@Override
	public void flush() throws IOException {
		strmOut.flush();
	}

}
