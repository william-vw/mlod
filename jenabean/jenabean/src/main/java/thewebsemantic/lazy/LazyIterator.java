package thewebsemantic.lazy;

import java.util.Iterator;

/**
 * 
 * 
 * @author wvw
 *
 */

public class LazyIterator implements Iterator {

	private Lazy lazy;
	private Iterator it;

	public LazyIterator(Lazy lazy) {
		this.lazy = lazy;

		it = lazy.data().iterator();
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public Object next() {
		return it.next();
	}

	public void remove() {
		lazy.modified(true);
		
		it.remove();
	}
}
