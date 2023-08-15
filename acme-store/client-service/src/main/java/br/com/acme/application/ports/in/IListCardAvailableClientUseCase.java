package br.com.acme.application.ports.in;

import br.com.acme.application.domain.CardDomain;

import java.util.List;

public interface IListCardAvailableClientUseCase {

    List<CardDomain> execute(String income);

}
