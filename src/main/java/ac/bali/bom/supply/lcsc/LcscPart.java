package ac.bali.bom.supply.lcsc;

import java.math.BigDecimal;
import java.util.List;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;

public interface LcscPart
{

    Property<Integer> productId();

    Property<String> productCode();

    Property<String> productModel();

    @Optional
    Property<String> parentCatalogId();

    @Optional
    Property<String> parentCatalogName();

    @Optional
    Property<Integer> catalogId();

    @Optional
    Property<String> catalogName();

    @Optional
    Property<Integer> brandId();

    Property<String> brandNameEn();

    @Optional
    Property<String> encapStandard();

    @Optional
    Property<Float> split();

    @Optional
    Property<String> productUnit();

    Property<String> minPacketUnit();

    Property<Integer> minBuyNumber();

    Property<Integer> maxBuyNumber();

    Property<Integer> minPacketNumber();

    @Optional
    Property<Boolean> isEnvironment();

    @Optional
    Property<String> allHotLevel();

    @UseDefaults
    Property<Boolean> isHot();

    @UseDefaults
    Property<Boolean> isPreSale();

    @UseDefaults
    Property<Boolean> isReel();

    @UseDefaults
    Property<BigDecimal> reelPrice();

    @Optional
    Property<Integer> stockNumber();

    @Optional
    Property<Integer> shipImmediately();

    @Optional
    Property<Integer> ship3Days();

    @Optional
    Property<Integer> stockSz();

    @Optional
    Property<Integer> stockJs();

    @UseDefaults
    Property<List<LcscPartPrice>> productPriceList();

    @Optional
    Property<List<String>> productImages();

    @Optional
    Property<String> pdfUrl();

    @Optional
    Property<String> productDescEn();

    @Optional
    Property<String> productIntroEn();

    @Optional
    Property<String> productArrange();

    @Optional
    Property<Float> productWeight();

    @Optional
    Property<Float> foreignWeight();

    @Optional
    Property<Float> weight();

    @Optional
    Property<Boolean> isForeignOnsale();

    @Optional
    Property<Boolean> isAutoOffsale();

    @Optional
    Property<Boolean> isHasBattery();

    @Optional
    Property<String> title();

    @Optional
    Property<String> brandCategory();

    @Optional
    Property<Boolean> isFavorite();

    @Optional
    Property<List<LcscParameter>> paramVOList();

    @Optional
    Property<Boolean> isAddNotice();

    @Optional
    Property<Integer> subscribeQuantity();

}
