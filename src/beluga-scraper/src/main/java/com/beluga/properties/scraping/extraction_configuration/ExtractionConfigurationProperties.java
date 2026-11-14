package com.beluga.properties.scraping.extraction_configuration;

import com.beluga.properties.scraping.extraction_configuration.validation.annotation.HasExactlyOneExtractionConfiguration;
import jakarta.validation.Valid;

import java.util.*;

/**
 * A single data point to extract. Exactly one of the extraction type properties must be configured, for example:
 * <pre>{@code
 * data-points:
 *   - text:
 *       selector: p.price
 *       field: price
 *   - image: {}
 * }</pre>
 */
@HasExactlyOneExtractionConfiguration
public class ExtractionConfigurationProperties {

    @Valid
    private AttributeExtractionConfigurationProperties attribute;
    @Valid
    private DescriptionListExtractionConfigurationProperties descriptionList;
    @Valid
    private HtmlExtractionConfigurationProperties html;
    @Valid
    private ImageExtractionConfigurationProperties image;
    @Valid
    private TextExtractionConfigurationProperties text;

    public Map<String, ExtractionTypeProperties> getExtractionTypesByName() {
        final Map<String, ExtractionTypeProperties> extractionTypes = new LinkedHashMap<>();
        extractionTypes.put("attribute", attribute);
        extractionTypes.put("description-list", descriptionList);
        extractionTypes.put("html", html);
        extractionTypes.put("image", image);
        extractionTypes.put("text", text);
        return extractionTypes;
    }

    /**
     * Returns the configured extraction type properties.
     *
     * @throws IllegalStateException when not exactly one extraction type is configured, which validation prevents
     */
    public ExtractionTypeProperties getConfiguredExtractionType() {
        final List<ExtractionTypeProperties> configured = getExtractionTypesByName().values()
                .stream()
                .filter(Objects::nonNull)
                .toList();

        if (configured.size() != 1) {
            throw new IllegalStateException("Expected exactly one extraction type to be configured, but found " + configured.size());
        }

        return configured.getFirst();
    }

    public AttributeExtractionConfigurationProperties getAttribute() {
        return attribute;
    }

    public void setAttribute(final AttributeExtractionConfigurationProperties attribute) {
        this.attribute = attribute;
    }

    public DescriptionListExtractionConfigurationProperties getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(final DescriptionListExtractionConfigurationProperties descriptionList) {
        this.descriptionList = descriptionList;
    }

    public HtmlExtractionConfigurationProperties getHtml() {
        return html;
    }

    public void setHtml(final HtmlExtractionConfigurationProperties html) {
        this.html = html;
    }

    public ImageExtractionConfigurationProperties getImage() {
        return image;
    }

    public void setImage(final ImageExtractionConfigurationProperties image) {
        this.image = image;
    }

    public TextExtractionConfigurationProperties getText() {
        return text;
    }

    public void setText(final TextExtractionConfigurationProperties text) {
        this.text = text;
    }
}
