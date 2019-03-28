name := "Document Similarity"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
"org.apache.spark" %% "spark-core" % "2.4.0",
 "org.apache.spark" %% "spark-sql" % "2.4.0" % Test classifier "tests",
"org.apache.spark" %% "spark-sql" % "2.4.0",
"org.apache.spark" %% "spark-mllib" % "2.4.0",
"org.apache.spark" %% "spark-streaming" % "2.4.0",
"org.apache.spark" %% "spark-graphx" % "2.4.0",
"org.postgresql" % "postgresql" % "9.4-1200-jdbc41"


)

libraryDependencies += "junit" % "junit" % "4.11" % Test
