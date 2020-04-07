# Medical Linked Open Data (MLOD)

MLOD stands for integrated semantic knowledge for the detection of drug-drug and drug-illness interactions.

While relevant sources are freely available, such as [DrugBank](http://www.drugbank.com/) and 
[DrugCentral](http://drugcentral.org/), these exist in custom formats (XML,  CSV) and are not interlinked. 
This limits the development of real-world solutions for detecting adverse interactions.

To fill this gap, we provide a semantic integrated MLOD, together with means for querying it and keeping it up-to-date.

The MLOD currently includes [DrugBank](http://www.drugbank.com/) and [DrugCentral](http://drugcentral.org/). 
The [Bio2RDF drug vocabulary](http://bio2rdf.org/) is used as a common vocabulary for the two drug-related sources. 
We utilized the [Ontop Platform](https://github.com/ontop/ontop) as a semantic layer on top of the DrugCentral SQL dump, 
as well as [JAXB](https://www.oracle.com/technical-resources/articles/javase/jaxb.html), 
[Apache Jena TDB](https://jena.apache.org/documentation/tdb/) and [JenaBean](https://code.google.com/archive/p/jenabean/) 
for converting the DrugBank XML dump into semantic format.

## Setup

To get the latest MLOD data:

- **Drugbank**: download the latest [Drugbank XML dump](https://www.drugbank.ca/releases/latest), 
set its path in `config.properties`, and initialize the TDB using the `Drugbank.initializeFromXml()` method.
 
 - **Drugcentral**: download the latest [Drugcentral SQL dump](http://drugcentral.org/download), 
 load it into a PostgreSQL database and set the database credentials in `config.properties`.
 
 ## Test
 
 See the `wvw.mlod.test.Test` class for tests.

## Code

See `mlod/docs` for JavaDoc. When running the code, the interesting classes are `DrugRepository` and its subclasses.
