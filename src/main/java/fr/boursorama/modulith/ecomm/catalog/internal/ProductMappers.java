package fr.boursorama.modulith.ecomm.catalog.internal;

import fr.boursorama.modulith.ecomm.catalog.internal.CatalogService.RegisterProductInCatalogCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ProductMappers {

	String SUMMARIZED = "summarized";

	int SUMMARY_SIZE = 16;

	@Mapping(source = "product", target = ".")
	@Mapping(source = "product.description", target = "smallDescription", qualifiedByName = SUMMARIZED)
	ProductSummaryDTO asProductSummaryDTO(Product product, boolean available);

	@Mapping(source = "product", target = ".")
	ProductDetailsDTO asProductDetailsDTO(Product product, boolean available);

	Product asProductEntity(RegisterProductInCatalogCommand registerNewProductCommand);

	@Named(SUMMARIZED)
	default String asSummary(String source) {
		StringBuilder builder = new StringBuilder(
				source.substring(0, Math.min(source.length(), SUMMARY_SIZE)));
		if (source.length() > SUMMARY_SIZE) {
			builder.append("...");
		}
		return builder.toString();
	}
}
