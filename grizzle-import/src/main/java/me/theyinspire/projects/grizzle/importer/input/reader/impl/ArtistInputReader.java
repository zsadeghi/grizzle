package me.theyinspire.projects.grizzle.importer.input.reader.impl;

import me.theyinspire.projects.grizzle.utils.DataUtils;
import me.theyinspire.projects.grizzle.model.Artist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistInputReader extends AbstractSqliteInputReader<Artist> {

    public ArtistInputReader(final String file) throws SQLException {
        super(file);
    }


    @Override
    protected Artist readObject(final ResultSet resultSet) throws SQLException {
        final Artist artist = new Artist();
        artist.setId(resultSet.getString("artist_id"));
        artist.setMusicBrainzId(resultSet.getString("artist_mbid"));
        artist.setName(DataUtils.toValid3ByteUTF8String(resultSet.getString("artist_name")));
        artist.setFamiliarity(resultSet.getDouble("artist_familiarity"));
        artist.setHotttnesss(resultSet.getDouble("artist_hotttnesss"));
        return artist;
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(DISTINCT artist_id) FROM songs;";
    }

    @Override
    protected String selectQuery() {
        return "SELECT DISTINCT artist_id, artist_mbid, artist_name, artist_familiarity, artist_hotttnesss FROM songs"
                + " ORDER BY artist_id;";
    }

}
