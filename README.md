# Medical Linked Open Data (MLOD)

MLOD includes integrated semantic knowledge for the detection of drug-drug and drug-illness interactions.

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

To retrieve and load the latest MLOD data, see the instructions below. These steps could take a while (10-20 minutes) depending on your machine, but they only need to be executed initially (and whenever you want to load updates). 

- **Drugbank**: download the latest [Drugbank XML dump](https://www.drugbank.ca/releases/latest), 
set its path in `config.properties`, and initialize the TDB using the `Drugbank.initializeFromXml()` method. 
 
As shown in the config file, several custom folders also need to be created for storing internal XML, RDF and TDB files. These can be removed after the loading process is completed.
 
The `initializeFromXml()` method will segment the XML file, separately convert each XML segment into RDF, and load the RDF into a persistent [Triple DataBase (TDB)](https://jena.apache.org/documentation/tdb/). If a particular step fails for some reason (e.g., loading into TDB), the prior steps do not have to be repeated. (Or, you can simply start from the segment where the process left off). 
 
 - **Drugcentral**: download the latest [Drugcentral SQL dump](http://drugcentral.org/download), 
 load it into a PostgreSQL database and set the database credentials in `config.properties`.
 
E.g., you can create a `drugcentral` database using [pgAdmin](https://www.pgadmin.org/) and use `psql -U postgres drugcentral < drugcentral.dump.010_05_2021.sql` to populate the database (this assumes your root user is called `postgres`)

 ## Test
 
 See the `wvw.mlod.test.Test` class for tests.

## Code

See `mlod/docs` for JavaDoc. When running the code, classes of interest include `DrugRepository` and its subclasses.

## Troubleshooting

- If PostgreSQL throws the following error:

org.postgresql.util.PSQLException: The authentication type 10 is not supported. Check that you have configured the pg_hba.conf file to include the client's IP address or subnet, and that it is using an authentication scheme supported by the driver.

You should upgrade to the latest version of [postgresql](https://mvnrepository.com/artifact/org.postgresql/postgresql).


- If Ontop throws `java.lang.ClassFormatError` as below:
```
Unable to make protected final java.lang.Class java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain) throws java.lang.ClassFormatError accessible: module java.base does not "opens java.lang" to unnamed module @24912924
```

You are likely using an older version of Ontop (e.g., v. 3); try reverting to an earlier version of the JDK (1.8 and 11 are tested, but newer ones may work as well). 
