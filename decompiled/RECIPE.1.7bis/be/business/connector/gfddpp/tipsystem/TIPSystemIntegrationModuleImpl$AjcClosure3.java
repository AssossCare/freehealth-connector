package be.business.connector.gfddpp.tipsystem;

import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.AroundClosure;

public class TIPSystemIntegrationModuleImpl$AjcClosure3 extends AroundClosure {
   public TIPSystemIntegrationModuleImpl$AjcClosure3(Object[] var1) {
      super(var1);
   }

   public Object run(Object[] var1) {
      Object[] var2 = super.state;
      return TIPSystemIntegrationModuleImpl.archivePrescriptionComment_aroundBody2((TIPSystemIntegrationModuleImpl)var2[0], (String)var2[1], (String)var2[2], (JoinPoint)var2[3]);
   }
}
