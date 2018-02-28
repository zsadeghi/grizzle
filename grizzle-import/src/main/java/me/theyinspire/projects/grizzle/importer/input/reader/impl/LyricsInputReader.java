package me.theyinspire.projects.grizzle.importer.input.reader.impl;

import me.theyinspire.projects.grizzle.utils.DataUtils;
import me.theyinspire.projects.grizzle.model.Lyrics;
import me.theyinspire.projects.grizzle.model.Token;
import me.theyinspire.projects.grizzle.model.Track;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class LyricsInputReader extends AbstractSqliteInputReader<Lyrics> {

    private final PreparedStatement statement;

    public LyricsInputReader(final String file) throws SQLException {
        super(file);
        statement = getConnection().prepareStatement("SELECT word, count FROM lyrics WHERE track_id = ?;");
    }

    @Override
    protected Lyrics readObject(final ResultSet resultSet) throws SQLException {
        final String trackId = resultSet.getString("track_id");
        final Lyrics lyrics = new Lyrics();
        final Track track = new Track();
        track.setId(trackId);
        lyrics.setTrack(track);
        lyrics.setMusixMatchId(resultSet.getInt("mxm_tid"));
        final Set<Token> tokens = new HashSet<>();
        lyrics.setTokens(tokens);
        statement.setString(1, trackId);
        final ResultSet query = statement.executeQuery();
        while (query.next()) {
            final Token token = new Token();
            token.setLyrics(lyrics);
            token.setWord(DataUtils.toValid3ByteUTF8String(query.getString("word")));
            token.setCount(query.getInt("count"));
            tokens.add(token);
        }
        query.close();
        return lyrics;
    }

    @Override
    protected void finalize() throws Throwable {
        statement.close();
        super.finalize();
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(DISTINCT track_id) FROM lyrics;";
    }

    @Override
    protected String selectQuery() {
        return "SELECT DISTINCT track_id, mxm_tid FROM lyrics ORDER BY track_id;";
    }

}
