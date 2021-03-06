/*
 *      Copyright (c) 2015-2016 Stuart Boston
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

/**
 * Actor information
 *
 * @author Stuart
 */
public class Actor extends AbstractJsonMapping {

    private static final long serialVersionUID = 1L;
    @JsonProperty("name")
    private String name;
    @JsonProperty("voice")
    private boolean voice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVoice() {
        return voice;
    }

    public void setVoice(boolean voice) {
        this.voice = voice;
    }

}
