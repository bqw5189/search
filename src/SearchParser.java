import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by bqw on 14-10-1.
 */
public class SearchParser {

    public static void main(String[] args) throws UnsupportedEncodingException {
        try {
            System.getProperties().load(SearchParser.class.getResourceAsStream("application.properties"));

            ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Quartz.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}