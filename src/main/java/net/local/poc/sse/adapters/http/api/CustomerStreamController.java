package net.local.poc.sse.adapters.http.api;

import static java.util.stream.Collectors.toCollection;
import static javax.management.timer.Timer.ONE_HOUR;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.local.poc.sse.domain.events.CustomerEvent;

@Slf4j
@RestController
@RequestMapping("v1/customers/stream")
public class CustomerStreamController {

    private static final ConcurrentMap<String, Set<WebClient>> EMITTERS = new ConcurrentReferenceHashMap<>();

    @GetMapping(value = "/{sessionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter customerEvents(@PathVariable("sessionId") String sessionId, HttpServletResponse response) {
        
        response.setHeader("Cache-Control", "no-store");
        
        log.info("Creating emitter for sessionId={}", sessionId);

        WebClient webClient = new WebClient(sessionId, new SseEmitter(ONE_HOUR));

        Set<WebClient> webClientsForDocument = EMITTERS.computeIfAbsent(sessionId, key -> Collections.newSetFromMap(new ConcurrentReferenceHashMap<>()));
        webClientsForDocument.add(webClient);

        webClient.getEmitter().onCompletion(() -> {
            log.info("Removing completed emitter for sessionId={}", sessionId);
            removeWebClientEmitter(sessionId, webClient);
        });

        webClient.getEmitter().onTimeout(() -> {
            log.warn("Removing timed out emitter for sessionId={}", sessionId);
            removeWebClientEmitter(sessionId, webClient);
        });

        return webClient.getEmitter();
    }

    @EventListener
    public void onEvent(CustomerEvent event) {
        processEvent(event);
    }

    protected void processEvent(CustomerEvent event) {
        event.stopTimer();

        Collection<WebClient> matchingEmitters = EMITTERS.values()
                                                         .stream()
                                                         .flatMap(Collection::stream)
                                                         .collect(toCollection(HashSet::new));
        matchingEmitters.parallelStream().forEach(webClient -> {
            if (webClient != null) {
                try {
                    log.debug("Sending customer={} to WebClient sessionId={}", event.getCustomer(), webClient.getSessionId());
                    webClient.emitter.send(event.getCustomer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void removeWebClientEmitter(String sessionId, WebClient emitter) {
        remove(EMITTERS, sessionId, emitter);
    }

    private static <K, V> void remove(final ConcurrentMap<K, Set<V>> multimap, final K key, final V value) {
        multimap.computeIfPresent(key, (k, oldValues) -> {
            final Set<V> newValues;
            if (oldValues.remove(value) && oldValues.isEmpty()) {
                newValues = null;
            } else {
                newValues = oldValues;
            }
            return newValues;
        });
    }

    @Data
    @RequiredArgsConstructor
    static class WebClient {
        private final String sessionId;
        private final SseEmitter emitter;
    }
}
