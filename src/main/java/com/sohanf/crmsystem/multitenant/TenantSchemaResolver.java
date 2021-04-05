package com.sohanf.crmsystem.multitenant;

import com.sohanf.crmsystem.common.Constant;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver{

    @Override
    public String resolveCurrentTenantIdentifier() {
        String t =  TenantContext.getCurrentTenant();
        if(t != null) {
            return t;
        }
        else {
            return Constant.DEFAULT_TENANT;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
