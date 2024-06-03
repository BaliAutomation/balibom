package ac.bali.bom.supply.lcsc;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.manufacturers.ManufacturersService;
import ac.bali.bom.parts.Part;
import ac.bali.bom.parts.PartsService;
import ac.bali.bom.parts.Price;
import ac.bali.bom.supply.Supplier;
import ac.bali.bom.supply.Supply;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.entitystore.memory.assembly.MemoryEntityStoreAssembler;
import org.apache.polygene.test.AbstractPolygeneTest;
import org.junit.Ignore;

@Ignore
public class LcscModelTest extends AbstractPolygeneTest
{
    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        new MemoryEntityStoreAssembler().assemble(module);
        module.services(ManufacturersService.class);
        module.services(PartsService.class);
        module.entities(Manufacturer.class, Part.class, Supplier.class);
        module.values(Manufacturer.class, Part.class, Supplier.class);
        module.values(Supply.class, Price.class);
        module.values(Manufacturer.class);

        module.values(
                LcscParameter.class,
                LcscPart.class,
                LcscPartPrice.class,
                LcscPartResponse.class
            )
            .visibleIn(Visibility.module);

        module.services(LcscSupplier.class)
            .identifiedBy("lcsc-connection")
            .instantiateOnStartup()
            .visibleIn(Visibility.application)
        ;
    }

//    @Test
//    public void givenProductDetailResponseWhenDeserializingExpectSuccess()
//        throws Exception
//    {
//        try (UnitOfWork uow = unitOfWorkFactory.newUnitOfWork())
//        {
//
//            PartSupplyConnector underTest = serviceFinder.findService(PartSupplyConnector.class).get();
//            LcscPartResponse lcscResponse = underTest.parseLcscJsonResponse(new ByteArrayInputStream(response1.getBytes()));
//            LcscPart lcscPart = lcscResponse.result().get();
//            Part part = underTest.buildPart(lcscPart);
//            assertThat(part.manufacturer().get().fullName().get(), equalTo("Analog Devices"));
//            assertThat(part.mpn().get(), equalTo("ADUM1400BRWZ-RL"));
//            assertThat(part.supply().get().get("lcsc").reelSize().get(), equalTo(1000));
//        }
//    }

    private static final String response1 = "{\n"
        + "  \"code\": 200,\n"
        + "  \"msg\": null,\n"
        + "  \"result\": {\n"
        + "    \"productId\": 74023,\n"
        + "    \"productCode\": \"C72910\",\n"
        + "    \"productModel\": \"ADUM1400BRWZ-RL\",\n"
        + "    \"parentCatalogId\": 575,\n"
        + "    \"parentCatalogName\": \"Communication Interface Chip/UART/485/232\",\n"
        + "    \"catalogId\": 581,\n"
        + "    \"catalogName\": \"Digital Isolators\",\n"
        + "    \"brandId\": 178,\n"
        + "    \"brandNameEn\": \"Analog Devices\",\n"
        + "    \"encapStandard\": \"SOIC-16-300mil\",\n"
        + "    \"split\": 1,\n"
        + "    \"productUnit\": \"Piece\",\n"
        + "    \"minPacketUnit\": \"Reel\",\n"
        + "    \"minBuyNumber\": 1,\n"
        + "    \"maxBuyNumber\": -1,\n"
        + "    \"minPacketNumber\": 1000,\n"
        + "    \"isEnvironment\": true,\n"
        + "    \"allHotLevel\": null,\n"
        + "    \"isHot\": true,\n"
        + "    \"isPreSale\": false,\n"
        + "    \"isReel\": false,\n"
        + "    \"reelPrice\": null,\n"
        + "    \"stockNumber\": 1782,\n"
        + "    \"shipImmediately\": 521,\n"
        + "    \"ship3Days\": 1261,\n"
        + "    \"stockSz\": 521,\n"
        + "    \"stockJs\": 1261,\n"
        + "    \"productPriceList\": [\n"
        + "      {\n"
        + "        \"ladder\": 1,\n"
        + "        \"productPrice\": \"3.6464\",\n"
        + "        \"discountRate\": \"0.95\",\n"
        + "        \"currencyPrice\": 3.6464,\n"
        + "        \"usdPrice\": 3.6464,\n"
        + "        \"currencySymbol\": \"US$\",\n"
        + "        \"cnyProductPrice\": \"22.92\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"ladder\": 10,\n"
        + "        \"productPrice\": \"3.2423\",\n"
        + "        \"discountRate\": \"0.95\",\n"
        + "        \"currencyPrice\": 3.2423,\n"
        + "        \"usdPrice\": 3.2423,\n"
        + "        \"currencySymbol\": \"US$\",\n"
        + "        \"cnyProductPrice\": \"20.38\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"ladder\": 30,\n"
        + "        \"productPrice\": \"3.0371\",\n"
        + "        \"discountRate\": \"0.95\",\n"
        + "        \"currencyPrice\": 3.0371,\n"
        + "        \"usdPrice\": 3.0371,\n"
        + "        \"currencySymbol\": \"US$\",\n"
        + "        \"cnyProductPrice\": \"19.09\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"ladder\": 100,\n"
        + "        \"productPrice\": \"2.7619\",\n"
        + "        \"discountRate\": \"0.95\",\n"
        + "        \"currencyPrice\": 2.7619,\n"
        + "        \"usdPrice\": 2.7619,\n"
        + "        \"currencySymbol\": \"US$\",\n"
        + "        \"cnyProductPrice\": \"17.36\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"ladder\": 500,\n"
        + "        \"productPrice\": \"2.6616\",\n"
        + "        \"discountRate\": \"0.95\",\n"
        + "        \"currencyPrice\": 2.6616,\n"
        + "        \"usdPrice\": 2.6616,\n"
        + "        \"currencySymbol\": \"US$\",\n"
        + "        \"cnyProductPrice\": \"16.73\"\n"
        + "      },\n"
        + "      {\n"
        + "        \"ladder\": 1000,\n"
        + "        \"productPrice\": \"2.6203\",\n"
        + "        \"discountRate\": \"0.95\",\n"
        + "        \"currencyPrice\": 2.6203,\n"
        + "        \"usdPrice\": 2.6203,\n"
        + "        \"currencySymbol\": \"US$\",\n"
        + "        \"cnyProductPrice\": \"16.47\"\n"
        + "      }\n"
        + "    ],\n"
        + "    \"productImages\": [\n"
        + "      \"https://assets.lcsc.com/images/lcsc/900x900/20221231_Analog-Devices-ADUM1400BRWZ-RL_C72910_front.jpg\",\n"
        + "      \"https://assets.lcsc.com/images/lcsc/900x900/20221231_Analog-Devices-ADUM1400BRWZ-RL_C72910_back.jpg\",\n"
        + "      \"https://assets.lcsc.com/images/lcsc/900x900/20221231_Analog-Devices-ADUM1400BRWZ-RL_C72910_blank.jpg\"\n"
        + "    ],\n"
        + "    \"pdfUrl\": \"https://datasheet.lcsc.com/lcsc/1811091711_Analog-Devices-ADUM1400BRWZ-RL_C72910.pdf\",\n"
        + "    \"productDescEn\": null,\n"
        + "    \"productIntroEn\": \"4 2.7V~5.5V Magnetic isolation Without voltage isolation 0 4 10Mbps General-purpose SOIC-16-300mil  Digital Isolators ROHS\",\n"
        + "    \"productArrange\": \"Tape & Reel (TR)\",\n"
        + "    \"productWeight\": 0.000836,\n"
        + "    \"foreignWeight\": 0.0,\n"
        + "    \"weight\": 0.836,\n"
        + "    \"isForeignOnsale\": true,\n"
        + "    \"isAutoOffsale\": false,\n"
        + "    \"isHasBattery\": false,\n"
        + "    \"title\": \"Analog Devices ADUM1400BRWZ-RL\",\n"
        + "    \"brandCategory\": \"international\",\n"
        + "    \"isFavorite\": false,\n"
        + "    \"paramVOList\": [\n"
        + "      {\n"
        + "        \"paramCode\": \"param_10408_n\",\n"
        + "        \"paramName\": \"正向通道数\",\n"
        + "        \"paramNameEn\": \"Forward Channel\",\n"
        + "        \"paramValue\": \"4\",\n"
        + "        \"paramValueEn\": \"4\",\n"
        + "        \"paramValueEnForSearch\": 4.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_10411_s\",\n"
        + "        \"paramName\": \"电源电压(Vdd)\",\n"
        + "        \"paramNameEn\": \"Supply Voltage\",\n"
        + "        \"paramValue\": \"2.7V~5.5V\",\n"
        + "        \"paramValueEn\": \"2.7V~5.5V\",\n"
        + "        \"paramValueEnForSearch\": null,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_14997_s\",\n"
        + "        \"paramName\": \"工作温度\",\n"
        + "        \"paramNameEn\": \"Operating Temperature\",\n"
        + "        \"paramValue\": \"-\",\n"
        + "        \"paramValueEn\": \"-\",\n"
        + "        \"paramValueEnForSearch\": null,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_10403\",\n"
        + "        \"paramName\": \"隔离技术\",\n"
        + "        \"paramNameEn\": \"Isolation Technology\",\n"
        + "        \"paramValue\": \"磁隔离\",\n"
        + "        \"paramValueEn\": \"Magnetic isolation\",\n"
        + "        \"paramValueEnForSearch\": -1.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_10404\",\n"
        + "        \"paramName\": \"是否带电压隔离\",\n"
        + "        \"paramNameEn\": \"Voltage Isolation or Not\",\n"
        + "        \"paramValue\": \"不带电压隔离\",\n"
        + "        \"paramValueEn\": \"Without voltage isolation\",\n"
        + "        \"paramValueEnForSearch\": -1.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_14998_n\",\n"
        + "        \"paramName\": \"反向通道数\",\n"
        + "        \"paramNameEn\": \"Reverse channel\",\n"
        + "        \"paramValue\": \"0\",\n"
        + "        \"paramValueEn\": \"0\",\n"
        + "        \"paramValueEnForSearch\": 0.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_12619_n\",\n"
        + "        \"paramName\": \"通道数\",\n"
        + "        \"paramNameEn\": \"Number of Channels\",\n"
        + "        \"paramValue\": \"4\",\n"
        + "        \"paramValueEn\": \"4\",\n"
        + "        \"paramValueEnForSearch\": 4.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_10410_n\",\n"
        + "        \"paramName\": \"数据速率\",\n"
        + "        \"paramNameEn\": \"Data Rate\",\n"
        + "        \"paramValue\": \"10Mbps\",\n"
        + "        \"paramValueEn\": \"10Mbps\",\n"
        + "        \"paramValueEnForSearch\": 10.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      },\n"
        + "      {\n"
        + "        \"paramCode\": \"param_10402\",\n"
        + "        \"paramName\": \"应用\",\n"
        + "        \"paramNameEn\": \"Applications\",\n"
        + "        \"paramValue\": \"通用\",\n"
        + "        \"paramValueEn\": \"General-purpose\",\n"
        + "        \"paramValueEnForSearch\": -1.0,\n"
        + "        \"isMain\": true,\n"
        + "        \"sortNumber\": 1\n"
        + "      }\n"
        + "    ],\n"
        + "    \"isAddNotice\": null,\n"
        + "    \"subscribeQuantity\": null\n"
        + "  }\n"
        + "}\n";

    private static final String response2 = "{\"code\":200,\"msg\":null,\"result\":{\"brandHomeVO\":null,\"catalogVOS\":[{\"catalogId\":365,\"parentId\":null,\"parentName\":null,\"catalogName\":null,\"catalogNameEn\":\"Connectors\",\"childCatelogs\":[{\"catalogId\":11071,\"parentId\":null,\"parentName\":null,\"catalogName\":null,\"catalogNameEn\":\"Pluggable System Terminal Block\",\"childCatelogs\":null,\"productNum\":1}],\"productNum\":1}],\"productSearchResultVO\":{\"totalCount\":1,\"currentPage\":1,\"pageSize\":25,\"productList\":[{\"eccn\":\"EAR99\",\"productId\":4153581,\"productCode\":\"C3586867\",\"productWeight\":0.001000000,\"foreignWeight\":null,\"weight\":1.000,\"isOnsale\":true,\"dollarLadderPrice\":null,\"isForeignOnsale\":true,\"minBuyNumber\":1,\"maxBuyNumber\":-1,\"isAutoOffsale\":false,\"minPacketUnit\":\"Bag\",\"productUnit\":\"Piece\",\"productArrange\":\"Bag-packed\",\"minPacketNumber\":50,\"encapStandard\":\"Plugin,P=5.08mm\",\"productModel\":\"1924318\",\"brandId\":968,\"brandNameEn\":\"Phoenix Contact\",\"catalogId\":11071,\"catalogName\":\"Pluggable System Terminal Block\",\"parentCatalogId\":365,\"parentCatalogName\":\"Connectors\",\"productDescEn\":null,\"productIntroEn\":\"16A 3 Straight pin 1 5.08mm Green Plugin,P=5.08mm  Pluggable System Terminal Block ROHS\",\"isHasBattery\":false,\"isDiscount\":false,\"isHot\":false,\"isEnvironment\":true,\"isPreSale\":true,\"productLadderPrice\":null,\"ladderDiscountRate\":null,\"productPriceList\":[{\"ladder\":1,\"productPrice\":\"1.0262\",\"discountRate\":\"1\",\"currencyPrice\":1.0262,\"usdPrice\":1.0262,\"currencySymbol\":\"$\",\"cnyProductPriceList\":[{\"ladder\":1,\"productPrice\":\"6.4500\",\"discountRate\":\"1\",\"currencyPrice\":null,\"usdPrice\":6.4500,\"currencySymbol\":null,\"cnyProductPriceList\":null,\"isForeignDiscount\":null,\"ladderLevel\":null},{\"ladder\":10,\"productPrice\":\"6.4500\",\"discountRate\":\"1\",\"currencyPrice\":null,\"usdPrice\":6.4500,\"currencySymbol\":null,\"cnyProductPriceList\":null,\"isForeignDiscount\":null,\"ladderLevel\":null},{\"ladder\":30,\"productPrice\":\"6.4500\",\"discountRate\":\"1\",\"currencyPrice\":null,\"usdPrice\":6.4500,\"currencySymbol\":null,\"cnyProductPriceList\":null,\"isForeignDiscount\":null,\"ladderLevel\":null}],\"isForeignDiscount\":null,\"ladderLevel\":null},{\"ladder\":200,\"productPrice\":\"0.3978\",\"discountRate\":\"1\",\"currencyPrice\":0.3978,\"usdPrice\":0.3978,\"currencySymbol\":\"$\",\"cnyProductPriceList\":[{\"ladder\":200,\"productPrice\":\"2.5000\",\"discountRate\":\"1\",\"currencyPrice\":null,\"usdPrice\":2.5000,\"currencySymbol\":null,\"cnyProductPriceList\":null,\"isForeignDiscount\":null,\"ladderLevel\":null}],\"isForeignDiscount\":null,\"ladderLevel\":null},{\"ladder\":500,\"productPrice\":\"0.3835\",\"discountRate\":\"1\",\"currencyPrice\":0.3835,\"usdPrice\":0.3835,\"currencySymbol\":\"$\",\"cnyProductPriceList\":[{\"ladder\":500,\"productPrice\":\"2.4100\",\"discountRate\":\"1\",\"currencyPrice\":null,\"usdPrice\":2.4100,\"currencySymbol\":null,\"cnyProductPriceList\":null,\"isForeignDiscount\":null,\"ladderLevel\":null}],\"isForeignDiscount\":null,\"ladderLevel\":null},{\"ladder\":1000,\"productPrice\":\"0.3771\",\"discountRate\":\"1\",\"currencyPrice\":0.3771,\"usdPrice\":0.3771,\"currencySymbol\":\"$\",\"cnyProductPriceList\":[{\"ladder\":1000,\"productPrice\":\"2.3700\",\"discountRate\":\"1\",\"currencyPrice\":null,\"usdPrice\":2.3700,\"currencySymbol\":null,\"cnyProductPriceList\":null,\"isForeignDiscount\":null,\"ladderLevel\":null}],\"isForeignDiscount\":null,\"ladderLevel\":null}],\"stockJs\":0,\"stockSz\":0,\"stockHk\":0,\"wmStockHk\":0,\"domesticStockVO\":{\"total\":0,\"shipImmediately\":0,\"ship3Days\":0},\"overseasStockVO\":{\"total\":0,\"shipImmediately\":0,\"ship3Days\":0},\"shipImmediately\":0,\"ship3Days\":0,\"smtAloneNumberSz\":null,\"smtAloneNumberJs\":null,\"stockNumber\":0,\"split\":1,\"productImageUrl\":null,\"productImageUrlBig\":null,\"pdfUrl\":\"\",\"productImages\":null,\"paramVOList\":[{\"paramCode\":\"param_13322_n\",\"paramName\":\"额定电流\",\"paramNameEn\":\"Current Rating (Max)\",\"paramValue\":\"16A\",\"paramValueEn\":\"16A\",\"paramValueEnForSearch\":16.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_13320_n\",\"paramName\":\"每排PIN数\",\"paramNameEn\":\"Number of PINs Per Row\",\"paramValue\":\"3\",\"paramValueEn\":\"3\",\"paramValueEnForSearch\":3.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_13321\",\"paramName\":\"安装方式\",\"paramNameEn\":\"Mounting Style\",\"paramValue\":\"直针\",\"paramValueEn\":\"Straight pin\",\"paramValueEnForSearch\":-1.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_13328_s\",\"paramName\":\"工作温度范围\",\"paramNameEn\":\"Operating Temperature Range\",\"paramValue\":\"-\",\"paramValueEn\":\"-\",\"paramValueEnForSearch\":null,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_18212\",\"paramName\":\"总PIN数\",\"paramNameEn\":\"Number of Pins\",\"paramValue\":\"3P\",\"paramValueEn\":\"3P\",\"paramValueEnForSearch\":-1.0,\"isMain\":false,\"sortNumber\":1},{\"paramCode\":\"param_13326\",\"paramName\":\"触头材质\",\"paramNameEn\":\"Contact Material\",\"paramValue\":\"-\",\"paramValueEn\":\"-\",\"paramValueEnForSearch\":-1.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_13319_n\",\"paramName\":\"排数\",\"paramNameEn\":\"Number of Rows\",\"paramValue\":\"1\",\"paramValueEn\":\"1\",\"paramValueEnForSearch\":1.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_13318_n\",\"paramName\":\"间距\",\"paramNameEn\":\"Pitch\",\"paramValue\":\"5.08mm\",\"paramValueEn\":\"5.08mm\",\"paramValueEnForSearch\":5.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_15629\",\"paramName\":\"结构\",\"paramNameEn\":\"Structure\",\"paramValue\":\"-\",\"paramValueEn\":\"-\",\"paramValueEnForSearch\":-1.0,\"isMain\":true,\"sortNumber\":1},{\"paramCode\":\"param_13407\",\"paramName\":\"颜色\",\"paramNameEn\":\"Color\",\"paramValue\":\"绿色\",\"paramValueEn\":\"Green\",\"paramValueEnForSearch\":-1.0,\"isMain\":true,\"sortNumber\":1}],\"isReel\":false,\"reelPrice\":0,\"productModelHighlight\":\"<span style=\\\"background-color:#ff0;\\\">1924318</span>\",\"productCodeHighlight\":null,\"catalogCode\":\"011354\",\"parentCatalogCode\":\"0113\",\"pdfLinkUrl\":null,\"url\":\"https://www.lcsc.com/product-detail/pluggable system terminal block_phoenix contact_1924318_C3586867.html\",\"shareCostPrice\":null,\"activityId\":null,\"activityName\":null,\"activityStartTime\":null,\"activityEndTime\":null,\"stockPrice\":null,\"isFavorite\":false}]},\"lcBookProductResultVO\":null,\"lcOrderLastPage\":0,\"productTotalPage\":1,\"isToDetail\":null,\"isToBrand\":null,\"tipProductDetailUrlVO\":null,\"brandUrlMap\":null}}";

}
