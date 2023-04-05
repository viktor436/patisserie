package com.patisserie.patisserie.cotrollers;


import com.patisserie.patisserie.entities.City;
import com.patisserie.patisserie.entities.Client;
import com.patisserie.patisserie.entities.RoyaltyBadge;
import com.patisserie.patisserie.payload.response.BadgeOwnerResponse;
import com.patisserie.patisserie.repositories.CityRepository;
import com.patisserie.patisserie.repositories.ClientRepository;
import com.patisserie.patisserie.repositories.RoyaltyBadgeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientRepository clientRepo;
    private final RoyaltyBadgeRepository badgeRepo;
    private final CityRepository cityRepo;


    ClientController(ClientRepository clientRepository,RoyaltyBadgeRepository badgeRepository,CityRepository cityRepository){

        this.clientRepo = clientRepository;
        this.badgeRepo = badgeRepository;
        this.cityRepo=cityRepository;
    }

    @PostMapping("/badge/save")
    public ResponseEntity<?> saveNewBadge(Integer discount,String clientTelNumber){
        Client client = clientRepo.findClientByTelNumber(clientTelNumber)
                .orElse(null);
        if(client == null)
            return ResponseEntity.ok("No such client!");
        if(badgeRepo.existsById(client.getId()))
            return ResponseEntity.ok("Badge is already present!");
        return ResponseEntity.ok(badgeRepo.save(new RoyaltyBadge("Golden French Macaron",30,client)));
    }
    @PostMapping("/badge/delete")
    public ResponseEntity<?> deleteBadge(String clientTelNumber){
        Client client = clientRepo.findClientByTelNumber(clientTelNumber)
                .orElse(null);
        if(client == null)
            return ResponseEntity.ok("No such client!");
        if(badgeRepo.existsById(client.getId())){
            badgeRepo.delete(client.getBadge());
            return ResponseEntity.ok("Badge was deleted!");
        }
        return ResponseEntity.ok("Not found badge associated with this client!");
    }
    @GetMapping("/badge/all")
    private List<BadgeOwnerResponse> getAllBadges(){
        List<RoyaltyBadge> royaltyBadges =badgeRepo.findAll();
        List<BadgeOwnerResponse> badgeOwnerResponses = new ArrayList<>();
        for(RoyaltyBadge royaltyBadge:royaltyBadges){
            BadgeOwnerResponse bOR = new BadgeOwnerResponse();
            bOR.setClientName(royaltyBadge.getClient().getFirstName()+" "+royaltyBadge.getClient().getLastName());
            bOR.setClientTelNumber(royaltyBadge.getClient().getTelNumber());
            bOR.setBadgeName(royaltyBadge.getBadgeName());
            bOR.setDiscountPercentage((royaltyBadge.getDiscountPercentage()));
            badgeOwnerResponses.add(bOR);
        }
        return badgeOwnerResponses;
    }

    @GetMapping("/fetch")
    private List<Client> getAllClients(){
        return clientRepo.findAll();
    }

    @GetMapping("/find/name")
    public ResponseEntity<?> findClientByName(String firstName,String lastName) {
        Optional<Client> client = clientRepo.findClientByFirstNameAndLastName(firstName, lastName);
        return client.isPresent() ? ResponseEntity.ok(client.get()) : ResponseEntity.ok("not found");
    }

    @GetMapping("/find/number")
    public ResponseEntity<?> findClientByTelNumber(String telNumber) {
        Optional<Client> result = clientRepo.findClientByTelNumber(telNumber);
        return result.isPresent() ? ResponseEntity.ok(result.get()) : ResponseEntity.ok("not found");
    }
    @GetMapping("/filter/city")
    public Set<Client> filterClientsByCity(String city){
        return clientRepo.findClientsByCity(city);
    }

    @PostMapping("/save")
    ResponseEntity<?> persistClient(String firstName, String lastName, String telNumber, String password, String address) {
        Client client = clientRepo.findClientByTelNumber(telNumber)
            .orElse(new Client(firstName, lastName, telNumber, password, address));
        if (client.getId() != null) {
        return ResponseEntity.ok("Client already exists!If you want to update it please go to the update section!");}

        clientRepo.save(client);
        return ResponseEntity.ok("New client with telephone number:" + telNumber + " was saved!");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateClient(String firstName, String lastName, String telNumber, String password, String address)
    {
        Client client =clientRepo.findClientByTelNumber(telNumber)
                .orElse(null);
        if(client == null){return  ResponseEntity.ok("Client with telephone number:"+telNumber+" wasn't found");}
        client.setAddress(address);
        client.setPassword(password);
        clientRepo.save(client);
        return ResponseEntity.ok("Client was updated!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteClient(String telNumber){
        Optional<Client> client = clientRepo.findClientByTelNumber(telNumber);
        if(client.isEmpty()) {
            return ResponseEntity.ok("client not found");
        }

        clientRepo.delete(client.get());
        return ResponseEntity.ok(client.get().getFirstName()+" "+client.get().getLastName()+" with number:"+telNumber+" was deleted!");
    }
}
