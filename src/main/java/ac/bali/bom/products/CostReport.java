package ac.bali.bom.products;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.apache.polygene.api.property.Property;

public interface CostReport
{
    Property<String> name();
    Property<ZonedDateTime> dateTime();
    Property<BigDecimal> total();

    Property<List<CostItem>> itemized();

    Property<List<String>> errors();
}
