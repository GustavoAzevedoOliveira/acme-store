package br.com.acme.adapters.output.database.h2.service;

import br.com.acme.adapters.output.database.h2.repository.CardRepository;
import br.com.acme.adapters.output.external.GetCardsAvailableToClient;
import br.com.acme.adapters.output.external.response.CardsAvailableClient;
import br.com.acme.application.domain.CardDomain;
import br.com.acme.application.mapper.ConverterDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ListCardAvailableRepositoryService implements GetCardsAvailableToClient {

    private final CardRepository cardRepository;
    private final ConverterDTO converterDTO;

    @Override
    public List<CardsAvailableClient> getCardsAvailable(String income){
        return (List<CardsAvailableClient>) converterDTO
                .convertLIstObjects(this.cardRepository.findByIncomeLessThanEqual(income),
                        CardDomain.class);
    }

}
