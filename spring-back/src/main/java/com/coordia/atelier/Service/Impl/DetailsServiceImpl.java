package com.coordia.atelier.Service.Impl;

import com.coordia.atelier.Entity.Correction;
import com.coordia.atelier.Entity.Details;
import com.coordia.atelier.Repository.DetailsRepository;
import com.coordia.atelier.Service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailsServiceImpl implements DetailsService {
    @Autowired
    private DetailsRepository detailsRepo;

    @Override
    public Details saveDetails(Details details) {
        return detailsRepo.save(details);
    }

    @Override
    public List<Details> getAllDetails() {
        return (List<Details>) detailsRepo.findAll();
    }

    @Override
    public Optional<Details> getDetailsById(Long id) {
        return detailsRepo.findById(id);
    }

    @Override
    public Details updateDetails(Details details, Long id) {
        Details existingDetails = detailsRepo.findById(id).orElseThrow(
                ()-> new RuntimeException()
        );
        // Mise à jour des attributs
        existingDetails.setSeverity(details.getSeverity());
        existingDetails.setType(details.getType());
        existingDetails.setSuggestion(details.getSuggestion());
        existingDetails.setError(details.getError());
        detailsRepo.save(existingDetails);
        return existingDetails;
    }

    @Override
    public void deleteDetails(Long id) {
        detailsRepo.deleteById(id);
    }
}
