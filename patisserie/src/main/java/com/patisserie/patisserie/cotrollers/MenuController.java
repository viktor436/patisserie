package com.patisserie.patisserie.cotrollers;

import com.patisserie.patisserie.entities.*;
import com.patisserie.patisserie.entities.keys.OrderMenuKey;
import com.patisserie.patisserie.payload.request.MenuRequest;
import com.patisserie.patisserie.payload.response.OrderItemResponse;
import com.patisserie.patisserie.payload.response.OrderResponse;
import com.patisserie.patisserie.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepo;
    private final IngredientRepository ingredientRepo;
    private final OrderRepository orderRepo;
    private final ClientRepository clientRepo;
    private final OrderMenuRepository orderMenuRepo;

    MenuController(MenuRepository menuRepo,OrderRepository orderRepo,ClientRepository clientRepo,IngredientRepository ingredientRepo,OrderMenuRepository orderMenuRepo){
        this.menuRepo = menuRepo;
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
        this.ingredientRepo = ingredientRepo;
        this.orderMenuRepo = orderMenuRepo;
    }

    @PostMapping("/order/status")
    public ResponseEntity<?> changeOrderStatus(Long id){
        Order order = orderRepo.findOrderById(id);
        if(order == null)
            return  ResponseEntity.ok("Order not found!");
        order.setFinished(true);
        return ResponseEntity.ok("Order with id:" + order.getId() + " marked as finished!");
    }

    @GetMapping("/order/filter/status/true")
    public ResponseEntity<?> filterOrdersByTrueStatus(){
        List<OrderResponse> orderResponses = new ArrayList<>();
        Set<Order> orders = orderRepo.findOrdersByFinished(true);
        for(Order order:orders){
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            OrderResponse orderResponce = new OrderResponse();
            orderResponce.setTelNumber(order.getTelNumber());
            orderResponce.setOrderDate(order.getOrderDate());
            orderResponce.setTotalAmount(order.getTotalAmount());
            for(OrderMenu orderMenu:order.getOrderMenus()){
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setQuantity(orderMenu.getQuantity());
                itemResponse.setPrice(orderMenu.getItemPrice());
                itemResponse.setItem(orderMenu.getItemName());

                itemResponses.add(itemResponse);
            }
            orderResponce.setOrderItems(itemResponses);
            orderResponses.add(orderResponce);
        }
        if(orderResponses.isEmpty()){return ResponseEntity.ok("No orders with status finished!");}
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/order/filter/status/false")
    public ResponseEntity<?> filterOrdersByFalseStatus(){
        List<OrderResponse> orderResponses = new ArrayList<>();
        Set<Order> orders = orderRepo.findOrdersByFinished(false);
        for(Order order:orders){
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            OrderResponse orderResponce = new OrderResponse();
            orderResponce.setTelNumber(order.getTelNumber());
            orderResponce.setOrderDate(order.getOrderDate());
            orderResponce.setTotalAmount(order.getTotalAmount());
            for(OrderMenu orderMenu:order.getOrderMenus()){
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setQuantity(orderMenu.getQuantity());
                itemResponse.setPrice(orderMenu.getItemPrice());
                itemResponse.setItem(orderMenu.getItemName());

                itemResponses.add(itemResponse);
            }
            orderResponce.setOrderItems(itemResponses);
            orderResponses.add(orderResponce);
        }
        if(orderResponses.isEmpty()){return ResponseEntity.ok("No orders waiting to be finished!");}
        return ResponseEntity.ok(orderResponses);
    }

    @PostMapping("/save/ingredient")
    public ResponseEntity<?> persistIngredient(String name){
        Ingredient ingredient = ingredientRepo.findIngredientByName(name);
        if(ingredient!=null){
            return  ResponseEntity.ok("Ingredient exists");
        }
        return ResponseEntity.ok("Success "+ingredientRepo.save(new Ingredient(name)).getName()+" was saved!");
    }
    @GetMapping("/ingredients/all")
    public ResponseEntity<?> getMenuPages(@RequestParam(defaultValue ="5") int perPage,
                                          @RequestParam(defaultValue ="1") int currentPage){
        Pageable pageable = PageRequest.of(currentPage-1,perPage);
        Page<Ingredient> ingredients = ingredientRepo.findAll(pageable);
        Map<String,Object>responce = new HashMap<>();
        responce.put("totalIngredients", ingredients.getTotalElements());
        responce.put("numberOfPages", ingredients.getTotalPages());
        responce.put("ingredients page number: "+currentPage, ingredients.getContent());

        return  ResponseEntity.ok(responce);
    }

    @GetMapping("/find/ingredient")
    public ResponseEntity<?> findIngredientByName(String name){
        Ingredient ingredient = ingredientRepo.findIngredientByName(name);
        if(ingredient==null){
            return  ResponseEntity.ok("Ingredient doesn't exists");
        }
        return  ResponseEntity.ok("Ingredient "+name+" exists.");
    }

    @GetMapping("/menu/all")
    public List<Menu> getAllMenus(){
        return menuRepo.findAll();
    }
    @GetMapping("/find/menu")
    public ResponseEntity<?> getMenuByItem(String item){
        Menu menu =menuRepo.findMenuByItem(item);
        if(menu == null)
            return ResponseEntity.ok("Item "+item+" not found!");
        return ResponseEntity.ok(menu);
    }

    @PostMapping("/save/menu")
    public ResponseEntity<?> persistMenu(MenuRequest menuRequest){
        if(menuRepo.findMenuByItem(menuRequest.getItemName())!=null){
            return ResponseEntity.ok("Menu(item) already exists!");
        }
            String ingredientsTxt = menuRequest.getIngredients();
            List<String> ingrTextArray = Arrays.asList(ingredientsTxt.split(","));
            Set<Ingredient> ingredients = new HashSet<>();
            for(String ingr:ingrTextArray){
                if(ingredientRepo.findIngredientByName(ingr)!=null){
                    ingredients.add(ingredientRepo.findIngredientByName(ingr));
                }
                else{
                    Ingredient newIngredient = new Ingredient(ingr);
                    ingredientRepo.save(newIngredient);
                    ingredients.add(newIngredient);
                }
            }
        return ResponseEntity.ok("Success! "+menuRepo.save(new Menu(menuRequest.getItemName(),menuRequest.getPrice(),ingredients)).getItem()+" was saved!");
    }

    @PostMapping("/delete/menu")
    public ResponseEntity<?> removeMenu(String name){
        Menu menu = menuRepo.findMenuByItem(name);
        if (menu == null){
            return  ResponseEntity.ok("No such menu(item)!");
        }
        menuRepo.delete(menu);
        return ResponseEntity.ok(name+" was deleted!");
    }

    @PostMapping("/save/order")
    public ResponseEntity<?> persistOrder(String menuItem,String clientTelNumber,int quantity){
        Menu menuMenuItem = menuRepo.findMenuByItem(menuItem);
        if(menuMenuItem == null)
            return ResponseEntity.ok("no such pastry");
        Client client = clientRepo.findClientByTelNumber(clientTelNumber).orElse(null);
        if(client == null)
            return ResponseEntity.ok("no such client");
        Order newOrder = orderRepo.save(new Order(client,new Timestamp(System.currentTimeMillis())));
        OrderMenu orderMenu = orderMenuRepo.save(new OrderMenu(new OrderMenuKey(newOrder.getId(), menuMenuItem.getId()),newOrder,menuMenuItem,quantity));

        return ResponseEntity.ok("New order with id= "+orderMenu.getId().getOrderId()+" is placed.");
    }

    @GetMapping("/find/order/id")
    public ResponseEntity<?> getOrderById(Long id){
        Order order = orderRepo.findOrderById(id);
        if(order==null){return ResponseEntity.ok("No order with id="+id);}
        OrderResponse orderResponse = new OrderResponse();
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        orderResponse.setTelNumber(order.getTelNumber());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setOrderItems(itemResponses);
        for(OrderMenu orderMenu:order.getOrderMenus()){
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setQuantity(orderMenu.getQuantity());
            itemResponse.setPrice(orderMenu.getItemPrice());
            itemResponse.setItem(orderMenu.getItemName());

            itemResponses.add(itemResponse);
        }

        return  ResponseEntity.ok(orderResponse);
    }
    @GetMapping("/find/order/telNumber")
    public ResponseEntity<?> getOrdersByClientTelNumber(String telNumber){
        if(!clientRepo.findClientByTelNumber(telNumber).isPresent())
            return ResponseEntity.ok("No client with telephone number:"+telNumber);
        List<Order> orders = orderRepo.findOrdersByClient_TelNumber(telNumber);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order:orders){
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            OrderResponse orderResponce = new OrderResponse();
            orderResponce.setTelNumber(order.getTelNumber());
            orderResponce.setOrderDate(order.getOrderDate());
            orderResponce.setTotalAmount(order.getTotalAmount());
            for(OrderMenu orderMenu:order.getOrderMenus()){
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setQuantity(orderMenu.getQuantity());
                itemResponse.setPrice(orderMenu.getItemPrice());
                itemResponse.setItem(orderMenu.getItemName());

                itemResponses.add(itemResponse);
            }
            orderResponce.setOrderItems(itemResponses);
            orderResponses.add(orderResponce);
        }
        return ResponseEntity.ok(orderResponses);
    }
    @GetMapping("/all/orders")
    public List<OrderResponse> getAllOrders(){
       List<OrderResponse> orderResponses = new ArrayList<>();
       List<Order> orders = orderRepo.findAll();
       for(Order order:orders){
           List<OrderItemResponse> itemResponses = new ArrayList<>();
           OrderResponse orderResponce = new OrderResponse();
           orderResponce.setTelNumber(order.getTelNumber());
           orderResponce.setOrderDate(order.getOrderDate());
           orderResponce.setTotalAmount(order.getTotalAmount());
           for(OrderMenu orderMenu:order.getOrderMenus()){
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setQuantity(orderMenu.getQuantity());
                itemResponse.setPrice(orderMenu.getItemPrice());
                itemResponse.setItem(orderMenu.getItemName());

                itemResponses.add(itemResponse);
           }
           orderResponce.setOrderItems(itemResponses);
           orderResponses.add(orderResponce);
       }
       return orderResponses;
    }
}
