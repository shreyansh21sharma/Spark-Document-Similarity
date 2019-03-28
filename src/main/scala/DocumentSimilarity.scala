import java.io.IOException

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.ml.feature._
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, VectorUDT}
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, MatrixEntry, RowMatrix}
import org.apache.spark.mllib.util
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}



class DocumentSimilarity {

  def initialize(): Unit ={
    val spark = SparkSession.builder().master("local").getOrCreate()
    val sc = spark.sparkContext
  }

  def getTFIDFDataframe(textRDD: RDD[(String,String)],numFeatures: Int): Dataset[_] ={

    val spark = SparkSession.builder().master("local").getOrCreate()

    val sc = spark.sparkContext

    val textDF = spark.createDataFrame(textRDD).toDF("document", "text")

    val tokenizer = new Tokenizer().setInputCol("text").setOutputCol("tokens")

    val tokenized = tokenizer.transform(textDF)

    //TO REMOVE STOPWORDS(OPTIONAL)
    /*
    val remover = new StopWordsRemover().setInputCol("tokens").setOutputCol("filtered")

    val stopRemoved=remover.transform(tokenized)
    */

    val hashingTF = new HashingTF().setInputCol("tokens").setOutputCol("onlyTF").setNumFeatures(numFeatures)

    val onlyTFData = hashingTF.transform(tokenized)

    val idfModel = new IDF().setInputCol("onlyTF").setOutputCol("TFIDF").fit(onlyTFData)

    val finalData = idfModel.transform(onlyTFData)

    finalData.select("document","tokens", "TFIDF").show()

    //println(finalData.schema)

    val tfidfData = finalData.select("TFIDF")

    return tfidfData

  }


  @throws(classOf[Exception])
  def calculateSimilarity(path: String, numFeatures: Int): String = {

    //var conf = new SparkConf().setAppName("Similarity Columns").setMaster("local")
    val spark = SparkSession.builder().master("local").getOrCreate()
    val sc = spark.sparkContext


    val textRDD = sc.wholeTextFiles(path)

    if (textRDD.count() == 0) {
      throw new IOException()
    }


    val tfidfData=getTFIDFDataframe(textRDD,numFeatures)


    val dataFrameRDD = util.MLUtils.convertMatrixColumnsFromML(tfidfData).rdd.map { x =>
      val data = x.get(0)
      
      data match {
        case vector: org.apache.spark.ml.linalg.SparseVector =>
          SparseVector.fromML(vector)

        case _ =>
          DenseVector.fromML(data.asInstanceOf[org.apache.spark.ml.linalg.DenseVector])
      }
    }



    //make a matrix
    val matrix = new IndexedRowMatrix(dataFrameRDD.zipWithIndex.map {
      case (value, index) => IndexedRow(index, value)}
    )
    
    val dist = matrix.toCoordinateMatrix.transpose().toIndexedRowMatrix().columnSimilarities()



    val transformedRDD = dist.entries.map { case MatrixEntry(row: Long, col: Long, score: Double) =>
      Array(row, col, score).mkString(",")
    }


    //val transformedRDD=dist.toIndexedRowMatrix();
    //println(transformedRDD.count())

    transformedRDD.collect().foreach(println)

    var resultScore: String = transformedRDD.collect().mkString("--")

    resultScore="Result is: ".concat(resultScore);

    if(resultScore.equalsIgnoreCase("Result is: "))
      resultScore+="No matching documents"

    return resultScore
  }
}
