package be.business.connector.recipe.prescriber;

import be.recipe.services.prescriber.ListRidsHistoryParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.AroundClosure;

public class PrescriberIntegrationModuleDevV4Impl$AjcClosure21 extends AroundClosure {
   public PrescriberIntegrationModuleDevV4Impl$AjcClosure21(Object[] var1) {
      super(var1);
   }

   public Object run(Object[] var1) {
      Object[] var2 = super.state;
      return PrescriberIntegrationModuleDevV4Impl.getData_aroundBody20((PrescriberIntegrationModuleDevV4Impl)var2[0], (ListRidsHistoryParam)var2[1], (JoinPoint)var2[2]);
   }
}
