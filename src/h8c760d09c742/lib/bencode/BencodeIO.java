package h8c760d09c742.lib.bencode;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Common infrastructure for serializing and deserializing Bencode objects.
 */
public abstract class BencodeIO implements Iterable<BencodeObject> {

	/**
	 * Location of a serialized Bencode object in a buffer.
	 */
	public class Range {
		private final int start, end;
		
		Range(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		/**
		 * Starting index (inclusive) of the serialized object.
		 * @return Zero-based byte offset.
		 */
		public int getStart() {
			return start;
		}
		
		/**
		 * Ending index (exclusive) of the serialized object.
		 * @return Zero-based byte offset.
		 */
		public int getEnd() {
			return end;
		}
	}
	
	protected static class BencodeTerminator extends BencodeObject {
		@Override
		void accept(BencodeVisitor visitor) throws BencodeException {
			throw new IllegalArgumentException();
		}
	}
	
	protected boolean closed;
	protected final Map<BencodeObject, BencodeIO.Range> objRef;
	
	protected BencodeIO() {
		this.closed = false;
		this.objRef = new LinkedHashMap<>();
	}
	
	/**
	 * Get the current position of this reader or writer.
	 * @return The number of bytes processed since the initial stream or buffer position.
	 */
	public abstract int getPosition();
	
	/**
	 * Get the stream or buffer position for an object previously read from or written to this instance.
	 * @param obj The Bencode object to query.
	 * @return The range of the specified object or null if the object is not found.
	 */
	public BencodeIO.Range getPosition(BencodeObject obj) {
		return objRef.get(obj);
	}
	
	/**
	 * Get the set of objects read from or written to this instance.
	 * @return Set of Bencode objects processed.
	 */
	public Set<BencodeObject> getObjects() {
		return objRef.keySet();
	}

	/**
	 * Iterate over the Bencode objects that have been processed by this instance.
	 */
	@Override
	public Iterator<BencodeObject> iterator() {
		return objRef.keySet().iterator();
	}

	/**
	 * Mark this IO instance as closed, preventing further read or write operations.
	 * An underlying stream might be closed as well (see constructors).
	 * @throws IOException If closing the underlying stream fails.
	 */
	public void close() throws IOException {
		closed = true;
	}

}
