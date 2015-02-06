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
package com.omertron.moviemeter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.moviemeter.model.FilmInfo;
import com.omertron.moviemeter.model.SearchResult;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yamj.api.common.exception.ApiExceptionType;
import org.yamj.api.common.http.DigestedResponse;
import org.yamj.api.common.http.DigestedResponseReader;
import org.yamj.api.common.http.UserAgentSelector;

/**
 * API for the MovieMeter.nl website.
 *
 * @author Stuart.Boston
 */
public class MovieMeterApi {

    private static final Logger LOG = LoggerFactory.getLogger(MovieMeterApi.class);
    // The provided API KEY
    private String apiKey;
    // The HTTP Client to use
    private CloseableHttpClient httpClient;
    // Base URL
    private static final String MM_URL = "http://www.moviemeter.nl/api/film/";
    private static final String MM_API = "&api_key=";
    private static final String MM_QUERY = "?q=";
    // Constants
    private static final String URL_ENCODING = "UTF-8";
    private static final int HTTP_STATUS_300 = 300;
    private static final int HTTP_STATUS_500 = 500;
    // JSON Mapper
    private final ObjectMapper mapper;
    // Charset for web operations
    private final Charset charset;

    /**
     * Create the API with an API Key and HTTP Client
     *
     * You can get a key from http://www.moviemeter.nl/site/registerclient/
     *
     * @param apiKey Your API Key
     * @param httpClient An Apache Commons HTTP Client
     * @throws MovieMeterException
     */
    public MovieMeterApi(String apiKey, CloseableHttpClient httpClient) throws MovieMeterException {
        if (StringUtils.isBlank(apiKey)) {
            throw new MovieMeterException(ApiExceptionType.AUTH_FAILURE, "API Key must be provided");
        }

        if (httpClient == null) {
            throw new MovieMeterException(ApiExceptionType.HTTP_CLIENT_MISSING, "HTTP Client must be provided");
        }

        this.apiKey = apiKey;
        this.httpClient = httpClient;
        this.mapper = new ObjectMapper();
        this.charset = Charset.forName("UTF-8");
    }

    /**
     * Get movie information using the IMDB ID
     *
     * @param imdbId IMDB ID to get the information for
     * @return Film object with the details
     * @throws MovieMeterException
     */
    public FilmInfo getFilm(String imdbId) throws MovieMeterException {
        return readJsonObject(buildIdUrl(imdbId), FilmInfo.class);
    }

    /**
     * Get movie information using the MovieMeter ID
     *
     * @param movieMeterId Movie Meter ID to get the information for
     * @return Film object with the details
     * @throws MovieMeterException
     */
    public FilmInfo getFilm(int movieMeterId) throws MovieMeterException {
        return readJsonObject(buildIdUrl(movieMeterId), FilmInfo.class);
    }

    /**
     * Search for a movie
     *
     * @param query Name of the movie to search for
     * @return A list of results
     * @throws MovieMeterException
     */
    public List<SearchResult> search(String query) throws MovieMeterException {
        String url = buildSearchUrl(query);

        try {
            return mapper.readValue(requestWebPage(url), new TypeReference<List<SearchResult>>() {
            });
        } catch (IOException ex) {
            throw new MovieMeterException(ApiExceptionType.MAPPING_FAILED, "Failed to map search results", url, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="URL Builder Functions">
    /**
     * Build the ID URL
     *
     * @param id ID to retrieve
     * @return URL
     */
    private String buildIdUrl(int id) {
        return buildIdUrl(Integer.toString(id));
    }

    /**
     * Build the ID URL
     *
     * @param id ID to retrieve
     * @return URL
     */
    private String buildIdUrl(String id) {
        StringBuilder url = new StringBuilder(MM_URL);
        url.append(id);
        url.append(MM_API);
        url.append(apiKey);

        LOG.trace("MovieMeter URL: {}", url);
        return url.toString();
    }

    /**
     * Build the Query URL
     *
     * @param query Film to search for
     * @return URL
     */
    private String buildSearchUrl(String query) {
        StringBuilder url = new StringBuilder(MM_URL);
        url.append(MM_QUERY);
        url.append(encoder(query));
        url.append(MM_API);
        url.append(apiKey);

        LOG.trace("MovieMeter URL: {}", url);
        return url.toString();
    }

    /**
     * Encode a string for use in the URL
     *
     * @param toEncode
     * @return
     */
    private static String encoder(final String toEncode) {
        try {
            return URLEncoder.encode(toEncode, URL_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            LOG.warn("Failed to encode: {}", ex.getMessage(), ex);
            return "";
        }
    }
    //</editor-fold>

    /**
     * Get the information for a URL and process into an object
     *
     * @param <T> The class of the object
     * @param url The URL to retrieve
     * @param object The class of object to map the URL to
     * @return The correctly mapped object
     * @throws MovieMeterException
     */
    private <T> T readJsonObject(final String url, final Class<T> object) throws MovieMeterException {
        final String page = requestWebPage(url);
        if (StringUtils.isNotBlank(page)) {
            try {
                return mapper.readValue(page, object);
            } catch (IOException ex) {
                throw new MovieMeterException(ApiExceptionType.MAPPING_FAILED, "Failed to read JSON object", url, ex);
            }
        }
        throw new MovieMeterException(ApiExceptionType.MAPPING_FAILED, "Failed to read JSON object", url);
    }

    /**
     * Download the URL into a String
     *
     * @param url URL to get
     * @return String representation of the web page
     * @throws MovieMeterException
     */
    private String requestWebPage(String url) throws MovieMeterException {
        try {
            final HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("accept", "application/json");
            httpGet.addHeader(HTTP.USER_AGENT, UserAgentSelector.randomUserAgent());

            final DigestedResponse response = DigestedResponseReader.requestContent(httpClient, httpGet, charset);

            if (response.getStatusCode() >= HTTP_STATUS_500) {
                throw new MovieMeterException(ApiExceptionType.HTTP_503_ERROR, response.getContent(), response.getStatusCode(), url);
            } else if (response.getStatusCode() >= HTTP_STATUS_300) {
                throw new MovieMeterException(ApiExceptionType.HTTP_404_ERROR, response.getContent(), response.getStatusCode(), url);
            }

            return response.getContent();
        } catch (IOException ex) {
            throw new MovieMeterException(ApiExceptionType.CONNECTION_ERROR, "Error retrieving URL", url, ex);
        }
    }
}
