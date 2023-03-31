/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.report;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.websocket.Session;


import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class ReportServices {
    private static final String MODULE = ReportServices.class.getName();

    public static Map<String, Object> sendReportPushNotifications(DispatchContext dctx, Map<String, ? extends Object> context) {
        String reportId = (String) context.get("reportId");
        String message = (String) context.get("message");
        Set<Session> clients = ReportWebSockets.getClients();
        try {
            synchronized (clients) {
                for (Session client : clients) {
                    client.getBasicRemote().sendText(message + ": " + reportId);
                }
            }
        } catch (IOException e) {
            Debug.logError(e.getMessage(), MODULE);
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> listReport(DispatchContext ctx, Map<String, Object> context) throws GenericEntityException {
        Delegator delegator = ctx.getDelegator();
        String userLoginId = (String) context.get("userLoginId");


        List<GenericValue> list = null;
        if (userLoginId != null) {
            list = EntityQuery.use(delegator).from("Report").where("userLoginId", userLoginId)
                    .orderBy("reportId").cache(true).queryList();
        } else {
            return ServiceUtil.returnError(getMessage());
        }

        Map<String, Object> result = ServiceUtil.returnSuccess();

        result.put("list", list);

        return result;
    }

    public static Map<String, Object> listReportType(DispatchContext ctx, Map<String, Object> context) throws GenericEntityException {
        Delegator delegator = ctx.getDelegator();
        String reportUse = (String) context.get("reportUse");

        List<GenericValue> list = null;
        if (Objects.equals(reportUse, "Y")) {
            list = EntityQuery.use(delegator).from("ReportType").where("reportUse", reportUse).orderBy("reportTypeId").cache(true).queryList();
        } else {
            return ServiceUtil.returnError(getMessage());
        }


        Map<String, Object> result = ServiceUtil.returnSuccess();

        result.put("list", list);

        return result;
    }

    public static Map<String, Object> listReportTargetType(DispatchContext ctx, Map<String, Object> context) throws GenericEntityException {
        Delegator delegator = ctx.getDelegator();

        List<GenericValue> list = EntityQuery.use(delegator).from("ReportTargetType").orderBy("reportTargetTypeId").cache(true).queryList();

        Map<String, Object> result = ServiceUtil.returnSuccess();

        result.put("list", list);

        return result;
    }


    private static String getMessage() {
        return null;
    }
}
