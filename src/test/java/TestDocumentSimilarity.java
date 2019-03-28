import java.io.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;


public class TestDocumentSimilarity {


    @Test
    public void testSparkSetup(){

        DocumentSimilarity docSim=new DocumentSimilarity();

        try {
            docSim.initialize();
        }
        catch (Exception e) {
            System.out.println("Caught exception in setting up spark" +e.toString());
            Assert.fail();
        }
    }


    @Test
    public void testWithPath() {

        DocumentSimilarity docSim = new DocumentSimilarity();


        try {
            String result = docSim.calculateSimilarity("data/test/doc_class/*", 1000);
            System.out.println(result);

            Assert.assertTrue(result.contains("--"));

        } catch (IOException e) {
            System.out.println("IOException caught " + e.toString());
            Assert.fail();
        }
        catch (Exception e2) {
            System.out.println("Exception caught " + e2.toString());
            Assert.fail();
        }


    }

    @Test
    public void testWithInvalidPath() {
        DocumentSimilarity docSim = new DocumentSimilarity();

        try {
            String result = docSim.calculateSimilarity("data/test/doc_clas/*", 1000);
            System.out.println(result);

        } catch (IOException e) {
            System.out.println("Exception caught " + e.toString());

            Assert.assertTrue(e.getMessage().contains("matches 0 files"));
        }
        catch (Exception e2) {
            System.out.println("Exception caught " + e2.toString());
            Assert.fail();
        }

    }

    @Test
    public void testValuesWithPredefinedData() {
        DocumentSimilarity docSim = new DocumentSimilarity();

        try {
            String result = docSim.calculateSimilarity("data/test/doc_class/*", 300);
            System.out.println(result);

            String temp = result.split("--")[1].split(",")[2];

            Assert.assertTrue(Float.parseFloat(temp) >0.0);
        } catch (IOException e) {
            System.out.println("Exception caught " + e.toString());
            Assert.fail();
        }
        catch (Exception e2) {
            System.out.println("Exception caught " + e2.toString());
            Assert.fail();
        }


    }

    @Test
    public void testWithSameData() {

        DocumentSimilarity docSim = new DocumentSimilarity();


        try {
            String result = docSim.calculateSimilarity("data/test/check/*", 1000);
            System.out.println(result);

            Assert.assertTrue(result.contains("No matching"));

        } catch (IOException e) {
            System.out.println("IOException caught " + e.toString());
            Assert.fail();
        }
        catch (Exception e2) {
            System.out.println("Exception caught " + e2.toString());
            Assert.fail();
        }


    }

}