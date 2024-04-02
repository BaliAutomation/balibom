package ac.bali.bom.lcsc;

import org.apache.polygene.api.property.Property;

import java.math.BigDecimal;
import java.util.List;

public interface LcscPart {


    Property<Integer> productId();

    Property<String> productCode();

    Property<String> productModel();

    Property<String> parentCatalogId();

    Property<String> parentCatalogName();

    Property<Integer> catalogId();

    Property<String> catalogName();

    Property<Integer> brandId();

    Property<String> brandNameEn();

    Property<String> encapStandard();

    Property<Float> split();

    Property<String> productUnit();

    Property<String> minPacketUnit();

    Property<Integer> minBuyNumber();

    Property<Integer> maxBuyNumber();

    Property<Integer> minPacketNumber();

    Property<Boolean> isEnvironment();

    Property<String> allHotLevel();

    Property<Boolean> isHot();

    Property<Boolean> isPreSale();

    Property<Boolean> isReel();

    Property<BigDecimal> reelPrice();

    Property<Long> stockNumber();

    Property<Long> shipImmediately();

    Property<Long> ship3Days();

    Property<Long> stockSz();

    Property<Long> stockJs();

    Property<List<LcscPartPrice>> productPriceList();

    Property<List<String>> productImages();

    Property<String> pdfUrl();

    Property<String> productDescEn();

    Property<String> productIntroEn();

    Property<String> productArrange();

    Property<Float> productWeight();

    Property<Float> foreignWeight();

    Property<Float> weight();

    Property<Boolean> isForeignOnsale();

    Property<Boolean> isAutoOffsale();

    Property<Boolean> isHasBattery();

    Property<String> title();

    Property<String> brandCategory();

    Property<Boolean> isFavorite();

    Property<List<LcscParameter>> paramVOList();

    Property<Boolean> isAddNotice();

    Property<Integer> subscribeQuantity();

}
