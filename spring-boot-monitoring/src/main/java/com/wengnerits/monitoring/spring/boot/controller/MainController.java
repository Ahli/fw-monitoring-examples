/*
 * Copyright (c) 2022, Miroslav Wengner
 *
 * fw-monitoring-examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fw-monitoring-examples is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with fw-monitoring-examples. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wengnerits.monitoring.spring.boot.controller;

import com.wengnerits.monitoring.spring.boot.service.HalloService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final PrometheusMeterRegistry registry;
    private final HalloService halloService;
    private final Counter indexCounter;

    @Autowired
    public MainController(PrometheusMeterRegistry registry, HalloService halloService) {
        this.halloService = halloService;
        this.registry = registry;
        indexCounter = registry.counter("index_counter");
    }

    @GetMapping("/")
    public String index() {
        indexCounter.increment();
        return halloService.hallo();
    }

    @GetMapping(value = "/metrics", produces = MediaType.TEXT_PLAIN_VALUE )
    public String metrics() {
        return registry.scrape(TextFormat.CONTENT_TYPE_004);
    }

}
