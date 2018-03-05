package me.theyinspire.proejcts.grizzle.service;

import me.theyinspire.projects.grizzle.model.Track;
import me.theyinspire.projects.grizzle.repository.TokenRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class SearchService {

    private final TokenRepository tokenRepository;
    private final StringService stringService;
    private final EntityManager entityManager;

    public SearchService(final TokenRepository tokenRepository,
                         final StringService stringService, final EntityManager entityManager) {
        this.tokenRepository = tokenRepository;
        this.stringService = stringService;
        this.entityManager = entityManager;
    }

    public List<Track> search(String query) {
        final String[] tokens = stringService.normalize(query);

        return null;
    }

}
