/*
 * Copyright (c) 2022 Mercedes-Benz Tech Innovation GmbH
 * Copyright (c) 2021,2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.tractusx.edc.hashicorpvault;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class HashicorpVaultGetEntryResponsePayload {

    @JsonProperty("data")
    private GetVaultEntryData data;

    public GetVaultEntryData getData() {
        return data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GetVaultEntryData {

        @JsonProperty("data")
        private Map<String, String> data;

        @JsonProperty("metadata")
        private HashicorpVaultEntryMetadata metadata;

        public Map<String, String> getData() {
            return data;
        }

        public HashicorpVaultEntryMetadata getMetadata() {
            return metadata;
        }
    }
}
