/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        return this.rel;
    }

    String getHref() {
        return this.href;
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
        return "Link [rel=" + this.rel + ", href=" + this.href + "]";
    }

}
