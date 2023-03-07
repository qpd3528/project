package org.apache.ofbiz.service;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import java.util.List;
import java.util.Map;

public class StatusCustomService {

    public static Map<String, Object> listStatusItem(DispatchContext ctx, Map<String, Object> context) throws GenericEntityException {
        Delegator delegator = ctx.getDelegator();
        String statusTypeId = (String) context.get("statusTypeId");


        List<GenericValue> list = null;
        if (statusTypeId != null) {
            list = EntityQuery.use(delegator).from("StatusItem").where("statusTypeId", statusTypeId)
                    .orderBy("sequenceId").cache(true).queryList();
        } else {
            return ServiceUtil.returnError(getMessage());
        }

        Map<String, Object> result = ServiceUtil.returnSuccess();

        result.put("list", list);

        return result;
    }
    private static String getMessage() {
        return null;
    }

}
