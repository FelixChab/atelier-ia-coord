package com.coordia.atelier.Controller;

import com.coordia.atelier.Entity.Details;
import com.coordia.atelier.Service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/details")
public class DetailsController {

    @Autowired
    private DetailsService detailsService;

    @PostMapping()
    public Details saveDetails(@RequestBody Details detail) {
       return detailsService.saveDetails(detail);
    }

    @GetMapping
    public List<Details> getAllDetails() {
        return detailsService.getAllDetails();
    }

    @GetMapping("/{id}")
    public Optional<Details> getDetailsById(@PathVariable("id") Long id) {
        return detailsService.getDetailsById(id);
    }

    @PutMapping("/{id}")
    public Details updateDetails(@RequestBody Details detail, @PathVariable("id") Long id) {
        return detailsService.updateDetails(detail, id);
    }

    @DeleteMapping("/{id}")
    public void deleteDetails(@PathVariable("id") Long id) {
        detailsService.deleteDetails(id);
    }

}
