package h8c760d09c742.lib.bencode;

/**
 * Interface for the specific processing of the different kinds of Bencode entities.
 */
public interface BencodeVisitor {

	public void visit(BencodeInteger obj) throws BencodeException;
	public void visit(BencodeString obj) throws BencodeException;
	public void visit(BencodeList obj) throws BencodeException;
	public void visit(BencodeDictionary obj) throws BencodeException;
	
}
