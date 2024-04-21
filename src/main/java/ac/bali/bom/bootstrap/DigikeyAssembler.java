package ac.bali.bom.bootstrap;

import ac.bali.bom.suppliers.digikey.DigikeySupplier;
import ac.bali.bom.suppliers.digikey.ProductSearchApi;
import ac.bali.bom.suppliers.digikey.model.BaseFilterV4;
import ac.bali.bom.suppliers.digikey.model.BaseProduct;
import ac.bali.bom.suppliers.digikey.model.BreakPrice;
import ac.bali.bom.suppliers.digikey.model.CategoriesResponse;
import ac.bali.bom.suppliers.digikey.model.Category;
import ac.bali.bom.suppliers.digikey.model.CategoryNode;
import ac.bali.bom.suppliers.digikey.model.CategoryResponse;
import ac.bali.bom.suppliers.digikey.model.Classifications;
import ac.bali.bom.suppliers.digikey.model.DKProblemDetails;
import ac.bali.bom.suppliers.digikey.model.Description;
import ac.bali.bom.suppliers.digikey.model.DigiReelPricing;
import ac.bali.bom.suppliers.digikey.model.FilterId;
import ac.bali.bom.suppliers.digikey.model.FilterOptions;
import ac.bali.bom.suppliers.digikey.model.FilterOptionsRequest;
import ac.bali.bom.suppliers.digikey.model.FilterValue;
import ac.bali.bom.suppliers.digikey.model.IsoSearchLocale;
import ac.bali.bom.suppliers.digikey.model.KeywordRequest;
import ac.bali.bom.suppliers.digikey.model.KeywordResponse;
import ac.bali.bom.suppliers.digikey.model.Manufacturer;
import ac.bali.bom.suppliers.digikey.model.ManufacturerInfo;
import ac.bali.bom.suppliers.digikey.model.ManufacturersResponse;
import ac.bali.bom.suppliers.digikey.model.MediaLinks;
import ac.bali.bom.suppliers.digikey.model.MediaResponse;
import ac.bali.bom.suppliers.digikey.model.PackageType;
import ac.bali.bom.suppliers.digikey.model.PackageTypeByQuantityProduct;
import ac.bali.bom.suppliers.digikey.model.PackageTypeByQuantityResponse;
import ac.bali.bom.suppliers.digikey.model.Parameter;
import ac.bali.bom.suppliers.digikey.model.ParameterFilterOptionsResponse;
import ac.bali.bom.suppliers.digikey.model.ParameterFilterRequest;
import ac.bali.bom.suppliers.digikey.model.ParameterValue;
import ac.bali.bom.suppliers.digikey.model.ParametricCategory;
import ac.bali.bom.suppliers.digikey.model.PriceBreak;
import ac.bali.bom.suppliers.digikey.model.Product;
import ac.bali.bom.suppliers.digikey.model.ProductAssociations;
import ac.bali.bom.suppliers.digikey.model.ProductAssociationsResponse;
import ac.bali.bom.suppliers.digikey.model.ProductDetails;
import ac.bali.bom.suppliers.digikey.model.ProductStatusV4;
import ac.bali.bom.suppliers.digikey.model.ProductSubstitute;
import ac.bali.bom.suppliers.digikey.model.ProductSubstitutesResponse;
import ac.bali.bom.suppliers.digikey.model.ProductSummary;
import ac.bali.bom.suppliers.digikey.model.ProductVariation;
import ac.bali.bom.suppliers.digikey.model.Recommendation;
import ac.bali.bom.suppliers.digikey.model.RecommendedProduct;
import ac.bali.bom.suppliers.digikey.model.RecommendedProductsResponse;
import ac.bali.bom.suppliers.digikey.model.Series;
import ac.bali.bom.suppliers.digikey.model.SortOptions;
import ac.bali.bom.suppliers.digikey.model.Supplier;
import ac.bali.bom.suppliers.digikey.model.TopCategory;
import ac.bali.bom.suppliers.digikey.model.TopCategoryNode;
import ac.bali.bom.suppliers.oauth2.OAuth2Authentication;
import org.apache.polygene.bootstrap.Assembler;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;

public class DigikeyAssembler
    implements Assembler
{

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.addServices(DigikeySupplier.class)
            .withTypes(OAuth2Authentication.class)
            .identifiedBy("digikey")
            .instantiateOnStartup()
        ;
        module.addServices(ProductSearchApi.class)
            .identifiedBy("digikey-product-search")
            .instantiateOnStartup()
        ;

        module.values(Product.class,
            Series.class,
            FilterValue.class,
            ProductStatusV4.class,
            Classifications.class,
            ProductVariation.class,
            CategoryNode.class,
            RecommendedProduct.class,
            CategoryResponse.class,
            IsoSearchLocale.class,
            ParameterFilterRequest.class,
            FilterOptions.class,
            Supplier.class,
            ProductSubstitutesResponse.class,
            ProductDetails.class,
            Recommendation.class,
            ProductAssociations.class,
            CategoriesResponse.class,
            KeywordRequest.class,
            Manufacturer.class,
            PackageTypeByQuantityResponse.class,
            ProductAssociationsResponse.class,
            TopCategoryNode.class,
            TopCategory.class,
            FilterId.class,
            MediaLinks.class,
            BaseProduct.class,
            FilterOptionsRequest.class,
            ManufacturersResponse.class,
            PriceBreak.class,
            PackageTypeByQuantityProduct.class,
            Description.class,
            BreakPrice.class,
            ParameterFilterOptionsResponse.class,
            MediaResponse.class,
            ManufacturerInfo.class,
            KeywordResponse.class,
            Category.class,
            ParametricCategory.class,
            PackageType.class,
            ProductSubstitute.class,
            SortOptions.class,
            RecommendedProductsResponse.class,
            ParameterValue.class,
            DKProblemDetails.class,
            Parameter.class,
            ProductSummary.class,
            BaseFilterV4.class,
            DigiReelPricing.class);
    }
}
