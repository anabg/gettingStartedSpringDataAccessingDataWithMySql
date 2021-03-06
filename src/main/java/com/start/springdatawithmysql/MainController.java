package com.start.springdatawithmysql;

import com.start.springdatawithmysql.entities.Address;
import com.start.springdatawithmysql.entities.Mail;
import com.start.springdatawithmysql.entities.User;
import com.start.springdatawithmysql.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by �Anita on 13/5/2018.
 */

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    //localhost:8080/demo/all/
    //http://localhost:8082/demo/add?name=First&email=someemail@someemailprovider.com
    @GetMapping(path="/add") // Map ONLY GET Requests
    public @ResponseBody
    String addNewUser (@RequestParam String name
            , @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request




        Set<Address> a = new HashSet<Address>();
        Set<Mail> emails = new HashSet<Mail>();






        User n = new User();
        n.setName(name);
        //n.setEmail(mail);


        Mail mail = new Mail();
        mail.setValue("algo@algo.com");

        Address ad = new Address();
        ad.setStreet("lavoz");
        ad.setUser(n);
        emails.add(mail);
        mail.setUser(n);
        a.add(ad);
        n.setAddress(a);
        n.setMail(emails);
        userRepository.save(n);

        //String filter = "{'capability.platform.type': 'Buitin'}";

        //String filter1 = "{'hidden':false}";
        //String filter1 ="{'name':First}";

        //String filter1 = "{'address.street':lavoz, 'address.zipCode':5008'}";
        //String filter1 = "{'address.street.st':lavoz}";
        String filter1 = "{'address.street':lavoz, 'mail.value':algo@algo.com}";
        //String filter1 = "{'address.street':lavoz, 'mail.value':algo@algo.com, 'address.zipCode':1010}";

        //userRepository.getQuery(filter);
        userRepository.getQuery(filter1, User.class);






        return "Saved";


    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}