# CS:GO Stash Item Scraper
<img src="https://github.com/csgocollection/csgostash-item-scraper/actions/workflows/maven-build.yml/badge.svg?branch=main" alt=“maven-build” width="110">

Java based scraper for https://csgostash.com that can extract skin meta information about in-game items in Counter-Strike: Global
Offensive. Built utilizing the RxJava library.

## Usage

The project is designed for concurrent and functional use and therefore uses the RxJava library
due to the nature of how https://csgocollection.com utilizes the data.

```java
var conf = new ItemScraperConfig(Fixtures.CSGOSTASH_HOST);
Observable<Item> items = new RxJavaItemScraperExecutor(new DocumentItemScraper(), conf)
    .execute();
```

You can avoid the overhead of the RxJava library by using the ItemScraperExecutorFacade API (WIP).

Note that the API is in the early phase, and breaking changes can occur
until the first release. Currently, the priority is to obtain the data needed
to continue development with https://csgocollection.com.
Still, people can feel free to contribute or create issues.

## Item Properties

The following properties are currently extracted from the website:

| Field                         | Data Type        | Implemented| Tested |
|-------------------------------|------------------|------------|--------|
| name                          | String           | ✅          |
| description                   | String           | ✅          |
| flavorText                    | String           | ✅          |
| exteriorMeta                  | ExteriorMeta     |            |
| condition                     | Condition        | ✅          |
| modifier                      | Modifier         | ✅          |
| collections                   | Set              |            |
| finishStyle                   | String           | ✅          |
| finishCatalog                 | String           | ✅          |
| floatRange                    | FloatRange       |            |
| floatRangeByCondition         | Map              |            |
| priceRange                    | PriceRange       |            |
| priceByCondition              | Map              |            |
| priceByConditionWithModifiers | Map              |            |
| inspectLinks                  | Set<InspectLink> | ✅          |
| texturePatternLink            | String           |            |
| previewVideoUrl               | String           | ✅          |


## Item Types

The following item types are currently supported:

| Item Type | Implemented            | Tested |
|-----------|------------------------|--------|
| WEAPON    | ✅                      |        |
| GLOVE     | ✅                      |        |

## License

The MIT License.
