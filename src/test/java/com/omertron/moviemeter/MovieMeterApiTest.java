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

import com.omertron.moviemeter.model.MMFilm;
import com.omertron.moviemeter.model.SearchResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stuart.Boston
 */
public class MovieMeterApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(MovieMeterApiTest.class);
    private static final String PROP_FIlENAME = "testing.properties";
    private static String API_KEY;

    private static MovieMeterApi api;
    private static CloseableHttpClient httpClient;

    private static final int ID_IRON_MAN = 41789;
    private static final String IMDB_IRON_MAN = "tt0371746";

    public MovieMeterApiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws MovieMeterException {
        // This must be the first statement in the beforeClass method
        TestLogger.Configure();
        httpClient = HttpClients.createDefault();

        Properties props = new Properties();
        File f = new File(PROP_FIlENAME);
        if (f.exists()) {
            LOG.info("Loading properties from '{}'", PROP_FIlENAME);
            TestLogger.loadProperties(props, f);

            API_KEY = props.getProperty("API_Key");
        } else {
            LOG.info("Property file '{}' not found, creating dummy file.", PROP_FIlENAME);

            props.setProperty("API_Key", "INSERT_YOUR_KEY_HERE");

            TestLogger.saveProperties(props, f, "Properties file for tests");
            fail("Failed to get key information from properties file '" + PROP_FIlENAME + "'");
        }

        api = new MovieMeterApi(API_KEY, httpClient);
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        httpClient.close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFilm method, of class MovieMeterApi.
     *
     * @throws com.omertron.moviemeter.MovieMeterException
     */
    @Ignore
    public void testGetFilm_String() throws MovieMeterException {
        LOG.info("getFilm (IMDB ID)");

        // Search for Iron Man
        MMFilm result = api.getFilm(IMDB_IRON_MAN);

        assertNotNull("No movie returned", result);
        assertEquals("Wrong Movie ID returned", ID_IRON_MAN, result.getId());
        assertEquals("Wrong Movie ID returned", IMDB_IRON_MAN, result.getImdbId());
    }

    /**
     * Test of getFilm method, of class MovieMeterApi.
     *
     * @throws com.omertron.moviemeter.MovieMeterException
     */
    @Test
    public void testGetFilm_int() throws MovieMeterException {
        LOG.info("getFilm (MovieMeter ID)");

        // Search for Iron Man
        MMFilm result = api.getFilm(ID_IRON_MAN);

        assertNotNull("No movie returned", result);
        assertEquals("Wrong Movie ID returned", ID_IRON_MAN, result.getId());
        assertEquals("Wrong Movie ID returned", IMDB_IRON_MAN, result.getImdbId());
    }

    /**
     * Test of search method, of class MovieMeterApi.
     *
     * @throws com.omertron.moviemeter.MovieMeterException
     */
    @Ignore
    public void testSearch() throws MovieMeterException {
        LOG.info("search");
        String query = "Iron Man";
        List<SearchResult> result = api.search(query);
        assertFalse("No results found", result.isEmpty());

        boolean found = false;
        for (SearchResult r : result) {
            if (r.getId() == ID_IRON_MAN) {
                found = true;
                break;
            }
        }
        assertTrue("Failed to locate the correct movie in the results", found);

    }

}
