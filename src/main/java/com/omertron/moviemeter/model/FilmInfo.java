/*
 *      Copyright (c) 2015-2015 Stuart Boston
 *
 *      This file is part of the MovieMeter API.
 *
 *      The MovieMeter API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      The MovieMeter API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheMovieDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.moviemeter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * MovieMeter Film
 *
 * @author Stuart.Boston
 */
public class FilmInfo extends AbstractJsonMapping {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private int id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("year")
    private int year;
    @JsonProperty("imdb")
    private String imdbId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("display_title")
    private String displayTitle;
    @JsonProperty("alternative_title")
    private String alternativeTitle;
    @JsonProperty("plot")
    private String plot;
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("votes_count")
    private int votesCount;
    @JsonProperty("average")
    private float average;
    @JsonProperty("posters")
    private Poster posters;
    @JsonProperty("countries")
    private List<String> countries;
    @JsonProperty("genres")
    private List<String> genres;
    @JsonProperty("actors")
    private List<Actor> actors;
    @JsonProperty("directors")
    private List<String> directors;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public Poster getPosters() {
        return posters;
    }

    public void setPosters(Poster posters) {
        this.posters = posters;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }
}
