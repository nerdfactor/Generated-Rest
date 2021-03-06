package eu.nerdfactor.springutil.generatedrest.config;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for controller generation.
 *
 * @author Daniel Klug
 */
public class ControllerConfiguration {

	/**
	 * Class name for the controller.
	 */
	private ClassName className;

	/**
	 * Base path for requests to the generated controller.
	 * Will be added in a RequestMapping annotation on the class.
	 */
	private String request;

	/**
	 * Class of the entity that will be accessed by the generated controller.
	 */
	private TypeName entity;

	/**
	 * Type of the id for the accessed entity.
	 */
	private TypeName id;

	private String idAccessor = "getId";

	/**
	 * The generated controller can use data access objects in responses.
	 */
	private boolean withDtos = false;

	/**
	 * Class of the data transfer objet that may be in the response of the
	 * generated controller.
	 */
	private TypeName dto = TypeName.OBJECT;

	private TypeName response = TypeName.OBJECT;

	/**
	 * Class of a data accessor that can be used to access entities.
	 * This can be a DataAccessController, DataAccessService, DataAccessRepository
	 * or any other class implementing DataAccessor.
	 */
	private ParameterizedTypeName dataAccessorClass;

	/**
	 * Class of a data mapper that will be used to map between entities and dtos.
	 * This can be one of the well known DataMappers or any other class implementing DataMapper.
	 */
	private TypeName dataMapperClass;

	private TypeName dataMergerClass;

	/**
	 * The generated controller can contain methods to access relational data.
	 */
	private boolean withRelations = false;

	private SecurityConfiguration security;

	private List<String> existingRequests;

	private TypeName dataWrapper;

	/**
	 * Map of relations that will be added to the controller.
	 */
	private Map<String, RelationConfiguration> relations = new HashMap<>();

	public ControllerConfiguration() {
	}

	public ControllerConfiguration(@NotNull ClassName className, @NotNull String request,
	                               @NotNull TypeName entity, @NotNull TypeName id, @NotNull String idAccessor,
	                               boolean withDtos, TypeName dto,
	                               @NotNull ParameterizedTypeName dataAccessorClass,
	                               TypeName dataMapperClass, TypeName dataMergerClass,
	                               @Nullable Map<String, RelationConfiguration> relations,
	                               List<String> existingRequests,
	                               TypeName dataWrapper) {
		this.className = className;
		this.request = request;
		this.entity = entity;
		this.id = id;
		this.idAccessor = idAccessor;
		this.dto = dto;
		this.dataAccessorClass = dataAccessorClass;
		this.dataMapperClass = dataMapperClass;
		this.dataMergerClass = dataMergerClass;
		if (relations != null && relations.size() > 0) {
			this.withRelations = true;
			this.relations = relations;
		}
		this.withDtos = withDtos;
		this.response = this.withDtos && !this.dto.equals(TypeName.OBJECT) ? this.dto : this.entity;
		this.existingRequests = existingRequests;
		this.dataWrapper = dataWrapper;
	}

	/**
	 * Creates a builder for controller configuration.
	 *
	 * @return A new ControllerConfigurationBuilder.
	 */
	public static @NotNull ControllerConfigurationBuilder builder() {
		return new ControllerConfigurationBuilder();
	}


	public ClassName getClassName() {
		return className;
	}

	public String getRequest() {
		return request;
	}

	public TypeName getEntity() {
		return entity;
	}

	public TypeName getId() {
		return id;
	}

	public String getIdAccessor() {
		return idAccessor;
	}

	public boolean isWithDtos() {
		return withDtos;
	}

	public TypeName getDto() {
		return dto;
	}

	public TypeName getResponse() {
		return response;
	}

	public ParameterizedTypeName getDataAccessorClass() {
		return dataAccessorClass;
	}

	public TypeName getDataMapperClass() {
		return dataMapperClass;
	}

	public TypeName getDataMergerClass() {
		return dataMergerClass;
	}

	public boolean isWithRelations() {
		return withRelations;
	}

	public SecurityConfiguration getSecurity() {
		return security;
	}

	public void setSecurity(SecurityConfiguration security) {
		this.security = security;
	}

	public Map<String, RelationConfiguration> getRelations() {
		return relations;
	}

	public List<String> getExistingRequests() {
		return existingRequests;
	}

	public boolean hasExistingRequest(RequestMethod method, String request) {
		return this.hasExistingRequest(method.name().toUpperCase(), request);
	}

	public boolean hasExistingRequest(String method, String request) {
		return this.existingRequests.contains(method.toUpperCase() + request.toLowerCase());
	}

	public TypeName getDataWrapper() {
		return dataWrapper;
	}
}
