package me.theyinspire.projects.grizzle.importer.input.reader.impl;

import me.theyinspire.projects.grizzle.importer.data.DataUtils;
import me.theyinspire.projects.grizzle.importer.data.model.Artist;
import me.theyinspire.projects.grizzle.importer.data.model.Track;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackInputReader extends AbstractSqliteInputReader<Track> {

    public TrackInputReader(final String file) throws SQLException {
        super(file);
    }

    @Override
    protected Track readObject(final ResultSet resultSet) throws SQLException {
        final Track track = new Track();
        track.setId(resultSet.getString("track_id"));
        track.setTitle(DataUtils.toValid3ByteUTF8String(resultSet.getString("title")));
        track.setAlbum(DataUtils.toValid3ByteUTF8String(resultSet.getString("release")));
        track.setDuration(resultSet.getDouble("duration"));
        track.setSongId(resultSet.getString("song_id"));
        track.setSevenDigitalId(resultSet.getInt("track_7digitalid"));
        track.setYear(resultSet.getInt("year"));
        final Artist artist = new Artist();
        artist.setId(resultSet.getString("artist_id"));
        track.setArtist(artist);
        return track;
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(*) FROM songs;";
    }

    @Override
    protected String selectQuery() {
        return "SELECT track_id, title, \"release\", duration, song_id, track_7digitalid, year, artist_id FROM songs;";
    }

}
