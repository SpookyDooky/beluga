# Jobs

Jobs contain the scraping configuration for all tasks that belong to that job. Per job the configuration is the same for
all tasks. A job defines:
- what URLs should be scraped
- how pages should be scraped
- how results should be stored
- how execution should be configured

Each URL in the configured URL file becomes an individual scraping task during execution.

This way of configuring jobs is mainly offered for ease of use, there is also a complete REST API 
for managing jobs which is more suited for a production environment. You can find the documentation here.

# Contents

* [Configuration](#configuration)
  * [Name](#belugajobsname)
    * [beluga.jobs[].name](#belugajobsname)
  * [Urls](#belugajobsurlurl-file)
    * [beluga.jobs[].url.url-file](#belugajobsurlurl-file)
  * [Execution configuration](#execution-configuration)
    * [beluga.jobs[].execution.tasks-per-second](#belugajobsexecutiontasks-per-second)
  * [Scraping configuration](#scraping-configuration)
    * [beluga.jobs[].scraping.item-selector](#belugajobsscrapingitem-selector)
  * [Data point configuration](#data-point-configuration)
    * [beluga.jobs[].scraping.data-points[]](#belugajobsscrapingdata-points)
  * [Attribute data point configuration](#attribute-data-point-configuration)
    * [beluga.jobs[].scraping.data-points[].attribute.selector](#belugajobsscrapingdata-pointsattributeselector)
    * [beluga.jobs[].scraping.data-points[].attribute.field](#belugajobsscrapingdata-pointsattributefield)
    * [beluga.jobs[].scraping.data-points[].attribute.attribute](#belugajobsscrapingdata-pointsattributeattribute)
  * [Description list data point configuration](#description-list-data-point-configuration)
    * [beluga.jobs[].scraping.data-points[].description-list.selector](#belugajobsscrapingdata-pointsdescription-listselector)
    * [beluga.jobs[].scraping.data-points[].description-list.data-points[].dt-value](#belugajobsscrapingdata-pointsdescription-listdata-pointsdt-value)
    * [beluga.jobs[].scraping.data-points[].description-list.data-points[].field](#belugajobsscrapingdata-pointsdescription-listdata-pointsfield)
  * [HTML data point configuration](#html-data-point-configuration)
    * [beluga.jobs[].scraping.data-points[].html.selector](#belugajobsscrapingdata-pointshtmlselector)
    * [beluga.jobs[].scraping.data-points[].html.field](#belugajobsscrapingdata-pointshtmlfield)
  * [Image data point configuration](#image-data-point-configuration)
    * [beluga.jobs[].scraping.data-points[].image](#belugajobsscrapingdata-pointsimage)
  * [Text data point configuration](#text-data-point-configuration)
    * [beluga.jobs[].scraping.data-points[].text.selector](#belugajobsscrapingdata-pointstextselector)
    * [beluga.jobs[].scraping.data-points[].text.field](#belugajobsscrapingdata-pointstextfield)
* [Example](#example)

# Configuration

## Name
### `beluga.jobs[].name`
- **Type:** string
- **Required:** true
- **Description:** The name of the job
- **Constraints:** Must be unique across all jobs

## Urls

### `beluga.jobs[].url.url-file`
- **Type:** string
- **Required:** true
- **Description:** Path to file containing URLs
- **Constraints:** Each URL must on a separate line

## Storage configuration

### `beluga.jobs[].storage.format`
- **Type:** string
- **Required:** true
- **Allowed values:** JSON
- **Description:** Format in which the results will be stored

### `beluga.jobs[].storage.folder`
- **Type:** string
- **Required:** true
- **Description:** Location in which the job results will be stored

## Execution configuration

### `beluga.jobs[].execution.tasks-per-second`
- **Type:** integer
- **Required:** false
- **Default:** 1
- **Description:** Amount of tasks that are allowed to execute per second

## Scraping configuration

### `beluga.jobs[].scraping.item-selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector for an element

## Data point configuration

### `beluga.jobs[].scraping.data-points[]`
- **Type:** list
- **Description:** The data to extract from each item. Each data point configures **exactly one** of the extraction
  types below by setting its block: `attribute`, `description-list`, `html`, `image` or `text`. There is no separate
  `type` property. Configuring no block, or more than one block, in the same data point fails startup with a message
  such as `Exactly one of [attribute, description-list, html, image, text] must be configured, but found: [html, text]`.

## Attribute data point configuration
Extracts the value of an attribute of the selected element.

### `beluga.jobs[].scraping.data-points[].attribute.selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector, relative to the item, of the element to extract the data from

### `beluga.jobs[].scraping.data-points[].attribute.field`
- **Type:** string
- **Required:** true
- **Description:** Name of the property in the results

### `beluga.jobs[].scraping.data-points[].attribute.attribute`
- **Type:** string
- **Required:** true
- **Description:** Name of the attribute from which to retrieve the value

## Description list data point configuration
Extracts values from a description list (`dl`). Each configured data point matches a `dt` element by its text and stores
the text of the `dd` element that follows it.

### `beluga.jobs[].scraping.data-points[].description-list.selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector, relative to the item, of the description list element

### `beluga.jobs[].scraping.data-points[].description-list.data-points[]`
- **Type:** list
- **Required:** true
- **Constraints:** Must contain at least one data point

### `beluga.jobs[].scraping.data-points[].description-list.data-points[].dt-value`
- **Type:** string
- **Required:** true
- **Description:** The value to match of the dt HTML tag

### `beluga.jobs[].scraping.data-points[].description-list.data-points[].field`
- **Type:** string
- **Required:** true
- **Description:** Name of the property in the results

## HTML data point configuration
Extracts the HTML of the selected element.

### `beluga.jobs[].scraping.data-points[].html.selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector, relative to the item, of the element to extract the data from

### `beluga.jobs[].scraping.data-points[].html.field`
- **Type:** string
- **Required:** true
- **Description:** Name of the property in the results

## Image data point configuration

### `beluga.jobs[].scraping.data-points[].image`
- **Type:** empty block
- **Description:** Extracts an image. It has no settings, so configure it as an empty block: `image: {}`

## Text data point configuration
Extracts the text of the selected element.

### `beluga.jobs[].scraping.data-points[].text.selector`
- **Type:** string
- **Required:** true
- **Description:** CSS selector, relative to the item, of the element to extract the data from

### `beluga.jobs[].scraping.data-points[].text.field`
- **Type:** string
- **Required:** true
- **Description:** Name of the property in the results

# Example
```yaml
beluga:
  jobs:
    - name: job_name
      url:
        url-file: "/mounted/file_containing_urls.txt"
      execution:
        tasks-per-second: 1
      scraping:
        item-selector: "div.product"
        data-points:
          - text:
              selector: p.price
              field: price
          - attribute:
              selector: a.details
              field: link
              attribute: href
          - html:
              selector: div.description
              field: description
          - image: {}
          - description-list:
              selector: dl.specifications
              data-points:
                - dt-value: Color
                  field: color
```
