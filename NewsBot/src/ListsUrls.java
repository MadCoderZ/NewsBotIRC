import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListsUrls {
	public List newsFeedsUrl = new ArrayList();

	public int addFeedsToList(String newUrl) {
		this.newsFeedsUrl.add(newUrl);
	return 0;
	}
	
	public int getFeeds() {
		Iterator listIt = newsFeedsUrl.listIterator();

		while (listIt.hasNext()) {
			Object element = listIt.next();
			System.out.println(element);
		}
		return 0;
	}
}
