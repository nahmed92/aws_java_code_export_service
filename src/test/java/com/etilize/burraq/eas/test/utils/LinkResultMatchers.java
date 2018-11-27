/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 Etilize
 * %%
 * NOTICE: All information contained herein is, and remains the property of ETILIZE.
 * The intellectual and technical concepts contained herein are proprietary to
 * ETILIZE and may be covered by U.S. and Foreign Patents, patents in process, and
 * are protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from ETILIZE. Access to the source code contained herein
 * is hereby forbidden to anyone except current ETILIZE employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements
 * explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication
 * or disclosure of this source code, which includes information that is confidential
 * and/or proprietary, and is a trade secret, of ETILIZE. ANY REPRODUCTION, MODIFICATION,
 * DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS
 * SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF ETILIZE IS STRICTLY PROHIBITED,
 * AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT
 * OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR
 * IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO
 * MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * #endregion
 */

package com.etilize.burraq.eas.test.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Factory for response link assertions. An instance of this class is typically accessed
 * via {@link LinkResultMatchers#link()}.
 *
 * @author Faisal Feroz
 * @since 1.0
 */
public class LinkResultMatchers {

    /**
     * Access to response links assertions.
     */
    public static LinkResultMatchers links() {
        return new LinkResultMatchers();
    }

    /**
     * Creates a {@link ResultMatcher} that checks for the presence of a link with the
     * given rel.
     */
    public ResultMatcher withRelIsPresent(final String rel) {
        return new LinkWithRelMatcher(rel, true);
    }

    /**
     * Creates a {@link ResultMatcher} that checks for the non-presence of a link with the
     * given rel.
     */
    public ResultMatcher withRelIsNotPresent(final String rel) {
        return new LinkWithRelMatcher(rel, false);
    }

    private static class LinkWithRelMatcher implements ResultMatcher {

        private final String rel;

        private final boolean present;

        private final LinkDiscoverer links;

        public LinkWithRelMatcher(final String rel, final boolean present) {
            this.rel = rel;
            this.present = present;
            links = new HalLinkDiscoverer();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void match(final MvcResult result) throws Exception {

            final String content = result.getResponse().getContentAsString();
            assertThat(links.findLinkWithRel(rel, content),
                    is(present ? notNullValue() : nullValue()));
        }
    }

}
