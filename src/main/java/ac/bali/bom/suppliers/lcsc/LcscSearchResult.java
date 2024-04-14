package ac.bali.bom.suppliers.lcsc;

import java.util.List;
import org.apache.polygene.api.property.Property;

public interface LcscSearchResult
{
    Property<Integer> totalCount();

    Property<Integer> currentPage();

    Property<Integer> pageSize();

    Property<List<LcscPart>> productList();
}
