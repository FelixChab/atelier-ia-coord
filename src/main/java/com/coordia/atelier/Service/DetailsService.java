package com.coordia.atelier.Service;

import com.coordia.atelier.Entity.Details;

import java.util.List;
import java.util.Optional;

public interface DetailsService {
    Details saveDetails(Details details);

    List<Details> getAllDetails();

    Optional<Details> getDetailsById(Long id);

    Details updateDetails(Details details, Long id);

    void deleteDetails(Long id);
}
