/*
 * Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.tractusx.edc.tests;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.eclipse.edc.test.system.utils.Participant;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.time.Duration.ofSeconds;
import static org.eclipse.edc.junit.testfixtures.TestUtils.getFreePort;

public class TxParticipant extends Participant {
    public static final String API_KEY = "testkey";
    private static final Duration ASYNC_TIMEOUT = ofSeconds(60);
    private static final Duration ASYNC_POLL_INTERVAL = ofSeconds(1);

    private final URI controlPlaneDefault = URI.create("http://localhost:" + getFreePort());
    private final URI controlPlaneControl = URI.create("http://localhost:" + getFreePort() + "/control");
    private final URI backendProviderProxy = URI.create("http://localhost:" + getFreePort() + "/events");
    private final URI dataPlaneProxy = URI.create("http://localhost:" + getFreePort());
    private final URI dataPlanePublic = URI.create("http://localhost:" + getFreePort() + "/public");

    private ParticipantEdrApi edrs;

    public String getBpn() {
        return getId();
    }

    /**
     * Returns the base configuration
     */
    public Map<String, String> getConfiguration() {
        return new HashMap<>() {
            {
                put("edc.connector.name", name);
                put("edc.participant.id", id);
                put("web.http.port", String.valueOf(controlPlaneDefault.getPort()));
                put("web.http.path", "/api");
                put("web.http.protocol.port", String.valueOf(protocolEndpoint.getUrl().getPort()));
                put("web.http.protocol.path", protocolEndpoint.getUrl().getPath());
                put("web.http.management.port", String.valueOf(managementEndpoint.getUrl().getPort()));
                put("web.http.management.path", managementEndpoint.getUrl().getPath());
                put("web.http.control.port", String.valueOf(controlPlaneControl.getPort()));
                put("web.http.control.path", controlPlaneControl.getPath());
                put("edc.dsp.callback.address", protocolEndpoint.getUrl().toString());
                put("edc.api.auth.key", "testkey");
                put("web.http.public.path", "/api/public");
                put("web.http.public.port", String.valueOf(dataPlanePublic.getPort()));
                put("edc.transfer.proxy.token.signer.privatekey.alias", "private-key");
                put("edc.transfer.proxy.token.verifier.publickey.alias", "public-key");
                put("edc.transfer.send.retry.limit", "1");
                put("edc.transfer.send.retry.base-delay.ms", "100");
                put("tx.dpf.consumer.proxy.port", String.valueOf(dataPlaneProxy.getPort()));
                put("edc.dataplane.token.validation.endpoint", controlPlaneControl + "/token");
                put("edc.dataplane.selector.httpplane.url", controlPlaneControl.toString());
                put("edc.dataplane.selector.httpplane.sourcetypes", "HttpData");
                put("edc.dataplane.selector.httpplane.destinationtypes", "HttpProxy");
                put("edc.dataplane.selector.httpplane.properties", "{\"publicApiUrl\":\"http://localhost:" + dataPlanePublic.getPort() + "/api/public\"}");
                put("edc.receiver.http.dynamic.endpoint", "http://localhost:" + controlPlaneDefault.getPort() + "/api/consumer/datareference");
                put("tractusx.businesspartnervalidation.log.agreement.validation", "true");
                put("edc.agent.identity.key", "BusinessPartnerNumber");
                put("edc.data.encryption.keys.alias", "test-alias");
                put("tx.dpf.proxy.gateway.aas.proxied.path", backendProviderProxy.toString());
                put("tx.dpf.proxy.gateway.aas.authorization.type", "none");
            }
        };
    }

    public ParticipantEdrApi edrs() {
        return edrs;
    }

    public static final class Builder extends Participant.Builder<TxParticipant, Builder> {

        private Builder() {
            super(new TxParticipant());
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        @Override
        public TxParticipant build() {
            super.managementEndpoint(new Endpoint(URI.create("http://localhost:" + getFreePort() + "/api/management"), Map.of("x-api-key", API_KEY)));
            super.protocolEndpoint(new Endpoint(URI.create("http://localhost:" + getFreePort() + "/protocol")));
            super.timeout(ASYNC_TIMEOUT);
            super.build();

            this.participant.edrs = new ParticipantEdrApi(participant);
            return participant;
        }
    }
}