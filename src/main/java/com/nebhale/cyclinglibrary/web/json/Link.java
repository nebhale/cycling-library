
package com.nebhale.cyclinglibrary.web.json;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nebhale.cyclinglibrary.model.Identifiable;

final class Link {

    private final String rel;

    private final String href;

    Link(String rel, Object... pathSegments) {
        this.rel = rel;

        String[] normalizedPathSegments = normalizePathSegments(pathSegments);
        this.href = ServletUriComponentsBuilder.fromCurrentContextPath().pathSegment(normalizedPathSegments).build().toUriString();
    }

    String getRel() {
        return rel;
    }

    String getHref() {
        return href;
    }

    private String[] normalizePathSegments(Object[] raw) {
        String[] normalized = new String[raw.length];

        for (int i = 0; i < raw.length; i++) {
            Object candidate = raw[i];
            if (candidate instanceof Identifiable) {
                normalized[i] = Long.toString(((Identifiable) candidate).getId());
            } else if (candidate instanceof String) {
                normalized[i] = (String) candidate;
            } else {
                normalized[i] = candidate.toString();
            }
        }

        return normalized;
    }

    @Override
    public String toString() {
        return "Link [rel=" + rel + ", href=" + href + "]";
    }

}
