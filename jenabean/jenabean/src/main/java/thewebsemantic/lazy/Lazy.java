package thewebsemantic.lazy;

import java.util.Collection;

public interface Lazy {

	boolean isConnected();
	boolean modified();

	// START EDIT wvw
	
	Collection data();
	void modified(boolean modified);
	
	// END EDIT wvw
}
