package br.com.acme.adapters.input.web.controller;

import br.com.acme.adapters.input.web.api.ClientApi;
import br.com.acme.adapters.input.web.api.request.ClientRequest;
import br.com.acme.adapters.input.web.api.request.ClientUpdateRequest;
import br.com.acme.adapters.input.web.api.response.ClientResponse;
import br.com.acme.adapters.input.web.response.CardResponse;
import br.com.acme.application.domain.entity.ClientDomain;
import br.com.acme.application.domain.exception.ClientNotFundException;
import br.com.acme.application.domain.exception.EmailClientExistsException;
import br.com.acme.application.mapper.ConverterDTO;
import br.com.acme.application.ports.in.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class ClientController implements ClientApi {

    private final ICreateClientDomainUseCase iCreateClientDomainUseCase;
    private final IListClientDomainUseCase iListClientDomainUseCase;
    private final IGetClientDomainGetByIdUseCase iGetClientDomainGetByIdUseCase;
    private final IDeleteClientDomainByIdUseCase iDeleteClientDomainByIdUseCase;
    private final IListCardAvailableClientUseCase iListCardAvailableClientUseCase;

    private final IUpdateClientUseCase iUpdateClientUseCase;

    private final ConverterDTO converterDTO;
    @Override
    public ResponseEntity<ClientResponse> create(ClientRequest clientRequest) {
            var domain = (ClientDomain) converterDTO.convertObject(clientRequest, ClientDomain.class);
        ClientDomain response = null;
        try {
            response = this.iCreateClientDomainUseCase.execute(domain);
            return ResponseEntity.ok((ClientResponse) converterDTO.convertObject(response, ClientResponse.class));
        } catch (EmailClientExistsException e) {
               throw new EmailClientExistsException("E-mail already exists");
        }
    }

    @Override
    public ResponseEntity<List<ClientResponse>> list() {
        var response = (List<ClientResponse>)
                converterDTO.convertLIstObjects(this.iListClientDomainUseCase.execute(), ClientResponse.class);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClientResponse> get(Long id){
        try {
            var domain = (ClientDomain) this.iGetClientDomainGetByIdUseCase.execute(id);
            return ResponseEntity.ok((ClientResponse) converterDTO
                    .convertObject(domain, ClientResponse.class));
        }catch (ClientNotFundException e) {
            throw new ClientNotFundException(id);
        }
    }
    @Override
    public ResponseEntity<?> delete(Long id) {
        this.iDeleteClientDomainByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> update(ClientUpdateRequest clientUpdateRequest, Long id) {
        var request = (ClientDomain) converterDTO.convertObject(clientUpdateRequest, ClientDomain.class);
        this.iUpdateClientUseCase.execute(request, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CardResponse>> getCardsAvailable(String income){
        var response = (List<CardResponse>)
                converterDTO.convertLIstObjects(this.iListCardAvailableClientUseCase.execute(income), CardResponse.class);

        return ResponseEntity.ok(response);
    }
}
