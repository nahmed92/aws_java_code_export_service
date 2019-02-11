/*
 * #region
 * burraq-import-service
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

package com.etilize.burraq.eas.specification;

import java.util.Map;

import com.etilize.burraq.eas.specification.value.UnitAttribute;
import com.etilize.burraq.eas.specification.value.UnitValue;
import com.etilize.burraq.eas.specification.value.Value;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Test fixture for {@link UpdateSpecificationRequest}
 *
 * @author Nasir Ahmed
 * @since 1.0
 *
 */
public class UpdateSpecificationRequestFixture {

    public static UpdateSpecificationRequest createWithUpdatedAttributes() {
        final UpdateSpecificationRequest request = new UpdateSpecificationRequest();
        request.addUpdatedAttributes("mfgId", new Value("Acer"));
        request.addUpdatedAttributes("mfgPartNoId", new Value("ABC"));
        request.addUpdatedAttributes("sizeId", new Value(3));
        final Map<String, UnitAttribute> unitAttrs = Maps.newHashMap();
        unitAttrs.put("EXTRACTED", new UnitAttribute(3.2, "kg"));
        request.addUpdatedAttributes("processId", new UnitValue(unitAttrs));

        request.addUpdatedAttributes("stringRepId", Lists.newArrayList(new Value("def")));
        request.addUpdatedAttributes("numberRepId", Lists.newArrayList(new Value(2)));
        final Map<String, UnitAttribute> unitAttrsForRep = Maps.newHashMap();
        unitAttrsForRep.put("EXTRACTED", new UnitAttribute(2, "mb"));
        request.addUpdatedAttributes("unitRepId",
                Lists.newArrayList(new UnitValue(unitAttrsForRep)));
        request.addUpdatedAttributes("notexistsId", new Value(4));
        return request;
    }

    public static UpdateSpecificationRequest createWithAddedAttributeValue() {
        final UpdateSpecificationRequest request = new UpdateSpecificationRequest();
        request.addAddedToSetAttributes("stringRepId", new Value("ghi"));
        request.addAddedToSetAttributes("numberRepId", new Value(3));
        request.addAddedToSetAttributes("notexistsId", new Value(4));
        return request;
    }

    public static UpdateSpecificationRequest createWithRemovedAttributeValue() {
        final UpdateSpecificationRequest request = new UpdateSpecificationRequest();
        request.addRemovedFromSetAttributes("stringRepId", new Value("abc"));
        request.addRemovedFromSetAttributes("numberRepId", new Value(1));
        request.addRemovedFromSetAttributes("notexistsId", new Value(4));
        return request;
    }

    public static UpdateSpecificationRequest createWithRemovedAttributeIds() {
        final UpdateSpecificationRequest request = new UpdateSpecificationRequest();
        request.addRemovedAttributeIds("stringRepId");
        request.addRemovedAttributeIds("numberRepId");
        request.addRemovedAttributeIds("unitRepId");
        request.addRemovedAttributeIds("sizeId");
        request.addRemovedAttributeIds("notexistsId");
        return request;
    }

    public static UpdateSpecificationRequest createWithMiscUpdates() {
        final UpdateSpecificationRequest request = new UpdateSpecificationRequest();
        request.addUpdatedAttributes("mfgId", new Value("Acer"));
        request.addAddedToSetAttributes("stringRepId",
                Lists.newArrayList(new Value("def"), new Value("ghi")));
        request.addRemovedFromSetAttributes("numberRepId",
                Lists.newArrayList(new Value(5), new Value(1)));
        request.addRemovedAttributeIds("sizeId");
        request.addRemovedAttributeIds("notexistsId");
        return request;
    }

}
