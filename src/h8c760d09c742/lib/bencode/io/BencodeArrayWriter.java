package h8c760d09c742.lib.bencode.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import h8c760d09c742.lib.bencode.BencodeObject;

/**
 * Serialization of Bencode entities in-memory to a dynamically allocated byte array.
 */
public class BencodeArrayWriter extends BencodeStreamWriter {

	/**
	 * Creates a new Bencode writer instance backed by an internal buffer.
	 */
	public BencodeArrayWriter() {
		super(new ByteArrayOutputStream(), false);
	}
	
	/**
	 * Writes all specified Bencode objects to a byte array in order.
	 * @param obj The objects to serialize.
	 * @return The serialization output.
	 */
	public static byte[] writeAll(BencodeObject... obj) {
		try (BencodeArrayWriter wr = new BencodeArrayWriter()) {	
			for (BencodeObject o : obj) wr.write(o);
			return wr.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public BencodeArrayWriter write(BencodeObject obj) throws IOException {
		super.write(obj);
		return this;
	}
	
	/**
	 * Retrieves the content of the buffer backing this writer.
	 * @return The content of the buffer.
	 */
	public byte[] toByteArray() {
		return ((ByteArrayOutputStream)strmOut).toByteArray();
	}

}
