# Document Similarity using Spark

A simple program built upon Apache Spark using Scala and sbt to find the similarity between multiple documents on their specified locations. Under development.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

The project requires Apache Spark (v2.4.0 used), Scala (v2.12.8 used) and sbt to initialize. The project can be configured directly using build.sbt.


## Running the tests

Their is a Java test file that runs the various preliminary tests. There are multiple features to be added and refining to be done currently. 


## Files Description

The data folder contains the sample documents for the various tests.
The src folder contains the tests as well as the source code.

CosineSimilarity.scala -> Gives a basic understanding of cosine similarity. It is not used in the project directly.

DocumentSimilarity.scala -> The primary class that carries out the entire functioning.


## Method

The documents are first gathered in a PairRDD in the form of [Location, Content]

Then they are converted to a Dataframe and undergo the following processes:
a) Tokenizing
b) Term Frequency (TF)
c) Inverse Document Frequency (IDF)
d) Cosine Similarity


##Output Format

The output is of the format *Index of Document A* -- *Index of Document B* -- *Corresponding Similarity Score*


## Authors

* **Shreyansh Sharma** - *Contact me on:* - [LinkedIn](https://www.linkedin.com/in/shreyansh-sharma-39a631147)

