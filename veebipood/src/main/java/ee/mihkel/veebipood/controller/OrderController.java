package ee.mihkel.veebipood.controller;

import ee.mihkel.veebipood.entity.Order;
import ee.mihkel.veebipood.entity.OrderRow;
import ee.mihkel.veebipood.model.ParcelMachine;
import ee.mihkel.veebipood.repository.OrderRepository;
import ee.mihkel.veebipood.repository.PersonRepository;
import ee.mihkel.veebipood.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

//    @Autowired
//    private PersonRepository personRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("orders")
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("my-orders")
    public List<Order> getPersonOrders(){
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return orderRepository.findByPerson_Id(personId);
    }

    @PostMapping("order")
    public String createOrder(@RequestBody List<OrderRow> orderRows) throws ExecutionException {
        Long personId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Order order = orderService.saveOrder(orderRows, personId);
        return orderService.makePayment(order.getId(), order.getTotal());
    }

    @GetMapping("parcelmachines")
    public List<ParcelMachine> parcelmachines(@RequestParam(required = false) String country){
        System.out.println(restTemplate);
        String url = "https://www.omniva.ee/locations.json";
        // restTemplate.exchange(URL kuhu tehakse päring, METHOD, body+headers, andmetüüp mis tagastatakse)
        ParcelMachine[] body = restTemplate.exchange(url, HttpMethod.GET,null, ParcelMachine[].class).getBody();

        if (country != null){
            return Arrays.stream(body)
                    .filter(e -> e.getA0_NAME().equals(country.toUpperCase()))
                    .toList();
        } else {
            return Arrays.stream(body)
                    .toList();
        }

    }

}
