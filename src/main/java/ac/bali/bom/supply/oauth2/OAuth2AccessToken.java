package ac.bali.bom.supply.oauth2;

import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
public interface OAuth2AccessToken {

	Property<String> access_token();
	Property<String> token_type();
//	Property<String> refresh_token();

	Property<Long> expires_in();
}