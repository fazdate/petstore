package com.chtrembl.petstore.product.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Pet
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-12-20T15:31:39.272-05:00")

@Entity
@Table(name="product")
public class Product {
	@JsonProperty("id")
	@Id
	@Column(name="product_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonProperty("category")
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@JsonProperty("name")
	@Column(name="product_name")
	private String name;

	@JsonProperty("photoURL")
	@Valid
	@Column(name="product_photoURL")
	private String photoURL;

	@JsonProperty("tags")
	@Valid
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = null;

	@JsonProperty("status")
	@Column(name="status")
	private String status;

	public Product id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 * 
	 * @return id
	 */
	@ApiModelProperty(value = "")

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product category(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * Get category
	 * 
	 * @return category
	 */
	@ApiModelProperty(value = "")

	@Valid

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Product name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 * 
	 * @return name
	 */
	@ApiModelProperty(example = "doggie", required = true, value = "")
	@NotNull

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get photoUrls
	 * 
	 * @return photoUrls
	 */
	@ApiModelProperty(required = true, value = "")
	@NotNull

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public Product tags(List<Tag> tags) {
		this.tags = tags;
		return this;
	}

	public Product addTagsItem(Tag tagsItem) {
		if (this.tags == null) {
			this.tags = new ArrayList<>();
		}
		this.tags.add(tagsItem);
		return this;
	}

	/**
	 * Get tags
	 * 
	 * @return tags
	 */
	@ApiModelProperty(value = "")

	@Valid

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Product status(String status) {
		this.status = status;
		return this;
	}

	/**
	 * pet status in the store
	 * 
	 * @return status
	 */
	@ApiModelProperty(value = "pet status in the store")

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Product product = (Product) o;
		return Objects.equals(this.id, product.id) && Objects.equals(this.category, product.category)
				&& Objects.equals(this.name, product.name) && Objects.equals(this.photoURL, product.photoURL)
				&& Objects.equals(this.tags, product.tags) && Objects.equals(this.status, product.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, category, name, photoURL, tags, status);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Pet {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    category: ").append(toIndentedString(category)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    photoUrls: ").append(toIndentedString(photoURL)).append("\n");
		sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
