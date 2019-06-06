import operations.*;
import student.*;
import org.junit.Test;
import tests.TestHandler;
import tests.TestRunner;

import java.math.BigDecimal;
import java.util.Calendar;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new id150325_ArticleOperations();
        BuyerOperations buyerOperations = new id150325_BuyerOperations();
        CityOperations cityOperations = new id150325_CityOperations();
        GeneralOperations generalOperations = null;
        OrderOperations orderOperations = null;
        ShopOperations shopOperations = new id150325_ShopOperations();
        TransactionOperations transactionOperations = null;
//
//        Calendar c = Calendar.getInstance();
//        c.clear();
//        c.set(2010, Calendar.JANUARY, 01);
//
//
//        Calendar c2 = Calendar.getInstance();
//        c2.clear();
//        c2.set(2010, Calendar.JANUARY, 01);
//
//        if(c.equals(c2)) System.out.println("jednako");
//        else System.out.println("nije jednako");

 /*       TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );*/

        TestRunner.runTests();
        
        /*	id150325_CityOperations op = new id150325_CityOperations();
        	op.createCity("Beograd");
        */
        id150325_ShopOperations so = new id150325_ShopOperations();
        id150325_ArticleOperations ao = new id150325_ArticleOperations();
//           
//        buyerOperations.createBuyer("Dajana Ilic", 1);
//        buyerOperations.createBuyer("Andrija Cicovic", 2);
//        buyerOperations.setCity(1, 2);
//	    System.out.println(buyerOperations.getCity(1));
//	    System.out.println(buyerOperations.getCity(2));
//	    
//	    buyerOperations.increaseCredit(1, new BigDecimal(100));
//	    buyerOperations.createOrder(1);
//	    buyerOperations.createOrder(1);
	    buyerOperations.getOrders(1);
	    buyerOperations.getCredit(1);
	    
	    
       
    }
}
